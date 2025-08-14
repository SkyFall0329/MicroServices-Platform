package cn.bitoffer.msgcenter.consumer;

import cn.bitoffer.common.redis.ReentrantDistributeLock;
import cn.bitoffer.msgcenter.common.conf.SendMsgConf;
import cn.bitoffer.msgcenter.consumer.poll.TimerMsgResendPollTask;
import cn.bitoffer.msgcenter.enums.MsgStatus;
import cn.bitoffer.msgcenter.enums.TemplateStatus;
import cn.bitoffer.msgcenter.exception.BusinessException;
import cn.bitoffer.msgcenter.exception.ErrorCode;
import cn.bitoffer.msgcenter.manager.SendMsgManager;
import cn.bitoffer.msgcenter.mapper.MsgQueueTimerMapper;
import cn.bitoffer.msgcenter.model.MsgQueueTimerModel;
import cn.bitoffer.msgcenter.model.TemplateModel;
import cn.bitoffer.msgcenter.model.dto.SendMsgReq;
import cn.bitoffer.msgcenter.redis.TimerMsgCache;
import cn.bitoffer.msgcenter.service.TemplateService;
import cn.bitoffer.msgcenter.tools.MsgRecordService;
import cn.bitoffer.msgcenter.utils.JSONUtil;
import cn.bitoffer.msgcenter.utils.SQLUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TimerMsgConsumer {

    @Autowired
    MsgQueueTimerMapper msgQueueTimerMapper;


    @Autowired
    SendMsgManager sendMsgManager;

    @Autowired
    SendMsgConf sendMsgConf;


    @Autowired
    TimerMsgCache timerMsgCache;

    @Autowired
    TimerMsgResendPollTask timerMsgResendPollTask;

    @Autowired
    ReentrantDistributeLock reentrantDistributeLock;

    private boolean isLeader = false;

    private static final int LOCK_TIMER_RETRY_INTERVAL_SECONDS = 10;


    @Scheduled(fixedRate = 100)
    public void consume() throws InterruptedException {
        if (isLeader){
            consumeTimerMsgs();
        }else{
            // 作为备用节点，定期尝试获取锁
            log.info("定时消费者作为备用节点，等待成为主节点");
            Thread.sleep(LOCK_TIMER_RETRY_INTERVAL_SECONDS*1000);
            isLeader = tryBeLeader();
            if (isLeader) {
                log.info("%s定时消费者从备用节点升级为主节点");
            }
        }
    }

    private boolean tryBeLeader(){
        String lockToken = System.currentTimeMillis()+Thread.currentThread().getName();
        boolean ok = reentrantDistributeLock.lockWithDog("TIMER_MSG_LEADER_CONSUMER_JAVA",
                lockToken, LOCK_TIMER_RETRY_INTERVAL_SECONDS);
        if(!ok){
            log.warn("timer consumer get lock failed！");
            return false;
        }
        return true;
    }
    
    private void consumeTimerMsgs(){
        // 1. 从Redis获取是否存在到点的 时间点
        List<String>  times = timerMsgCache.getOnTimePointsFromCache();
        if(times == null || times.size() == 0){
            return;
        }

        // 根据缓存判读，当前时间点已经存在到点消息
        // 2.从数据库查询出具体到点的消息列表
        List<MsgQueueTimerModel> onTimeMsgs = msgQueueTimerMapper.getOnTimeMsgsList(MsgStatus.Pending.getStatus(), new Date().getTime());
        if(onTimeMsgs == null || onTimeMsgs.size() == 0){
            return;
        }

        // 3. 将msgList这里皮消息全部变为处理中Processiong
        List<String> msgIdList = onTimeMsgs.stream()
                .map(MsgQueueTimerModel::getMsgId)
                .collect(Collectors.toList());
        String msgIdListStr = SQLUtil.convertListToSQLString(msgIdList);
        msgQueueTimerMapper.batchSetStatus(msgIdListStr,MsgStatus.Processiong.getStatus());

        // 4. 遍历挨个处理到点消息
        for (MsgQueueTimerModel dbModel:onTimeMsgs) {
            // 线程池异步处理
            timerMsgResendPollTask.asyncHandleMsg(dbModel.getReq());
        }
    }
}
