package cn.bitoffer.msgcenter.consumer;

import cn.bitoffer.common.redis.ReentrantDistributeLock;
import cn.bitoffer.msgcenter.common.conf.SendMsgConf;
import cn.bitoffer.msgcenter.constant.Constants;
import cn.bitoffer.msgcenter.consumer.poll.MysqlMsgPollTask;
import cn.bitoffer.msgcenter.enums.MsgStatus;
import cn.bitoffer.msgcenter.enums.PriorityEnum;
import cn.bitoffer.msgcenter.manager.SendMsgManager;
import cn.bitoffer.msgcenter.mapper.MsgQueueMapper;
import cn.bitoffer.msgcenter.mapper.MsgRecordMapper;
import cn.bitoffer.msgcenter.model.MsgQueueModel;
import cn.bitoffer.msgcenter.model.dto.SendMsgReq;
import cn.bitoffer.msgcenter.utils.JSONUtil;
import cn.bitoffer.msgcenter.utils.SQLUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class MysqlMsgConsumer {

    @Autowired
    MsgQueueMapper msgQueueMapper;

    @Autowired
    MysqlMsgPollTask mysqlMsgPollTask;


    @Autowired
    MsgRecordMapper msgRecordMapper;

    @Autowired
    SendMsgConf sendMsgConf;

    @Autowired
    SendMsgManager sendMsgManager;

    @Autowired
    ReentrantDistributeLock reentrantDistributeLock;

    private static final int LOCK_RETRY_INTERVAL_SECONDS = 10;

    private HashMap<PriorityEnum,Boolean> isLeaderMap = new HashMap<>();


    @Scheduled(fixedRate = 1000)
    public void consumeLow() throws InterruptedException {
        consumeMySQLMsgWithLeaderCheck(PriorityEnum.PRIORITY_LOW,10);
        consumeMySQLMsgWithLeaderCheck(PriorityEnum.PRIORITY_MIDDLE,30);
        consumeMySQLMsgWithLeaderCheck(PriorityEnum.PRIORITY_HIGH,60);
        consumeMySQLMsgWithLeaderCheck(PriorityEnum.PRIORITY_RETRY,10);
    }

    private void consumeMySQLMsgWithLeaderCheck(PriorityEnum priorityEnum,int pullNum) {
        if (isLeaderMap.get(priorityEnum) != null && isLeaderMap.get(priorityEnum)){
            consumeMySQLMsg(priorityEnum,pullNum);
        }else{
            // 作为备用节点，定期尝试获取锁
            log.info(PriorityEnum.GetPriorityStr(priorityEnum.getPriorty())+"消费者作为备用节点，等待成为主节点");
            try{
                Thread.sleep(LOCK_RETRY_INTERVAL_SECONDS*1000);
            }catch (Exception e){
                log.error("定时异常");
            }
            boolean isLeader = tryBeLeader(priorityEnum);
            if (isLeader) {
                log.info("Low优先级消费者从备用节点升级为主节点");
                isLeaderMap.put(priorityEnum,true);
            }
        }
    }

    private boolean tryBeLeader(PriorityEnum priorityEnum){
        String lockToken = System.currentTimeMillis()+Thread.currentThread().getName();
        boolean ok = reentrantDistributeLock.lockWithDog(PriorityEnum.GetPriorityStr(priorityEnum.getPriorty())+"_MSG_LEADER_CONSUMER_JAVA",
                lockToken, LOCK_RETRY_INTERVAL_SECONDS);
        if(!ok){
            log.warn("timer consumer get lock failed！");
            return false;
        }
        return true;
    }
    
    private void consumeMySQLMsg(PriorityEnum priority,int pullNum){

        // 1. 根据有限级确定表明
        String tableName = Constants.TableNamePre_MsgQueue+ PriorityEnum.GetPriorityStr(priority.getPriorty());

        // 2. 获取一批待处理消息
        List<MsgQueueModel> msgList = msgQueueMapper.getMsgsByStatus(tableName,MsgStatus.Pending.getStatus(),pullNum);

        // 如果消息为空，则退出
        if(msgList == null || msgList.size() == 0){
            return;
        }

        // 4. 批量将msgList全部变为处理中
        List<String> msgIdList = msgList.stream()
                .map(MsgQueueModel::getMsgId)
                .collect(Collectors.toList());
        String msgIdListStr = SQLUtil.convertListToSQLString(msgIdList);
        msgQueueMapper.batchSetStatus(tableName,msgIdListStr,MsgStatus.Processiong.getStatus());

        // 5. 遍历处理这一批消息
        for (MsgQueueModel dbModel:msgList) {
            SendMsgReq req = new SendMsgReq();
            req.setMsgID(dbModel.getMsgId());
            req.setPriority(dbModel.getPriority());
            req.setTo(dbModel.getTo());
            req.setSubject(dbModel.getSubject());
            req.setTemplateId(dbModel.getTemplateId());

            Map<String,String> templateData = JSONUtil.parseMap(dbModel.getTemplateData(),String.class,String.class);
            req.setTemplateData(templateData);

            //线程池处理单个请求
            mysqlMsgPollTask.asyncHandleMsg(req);
        }

    }
}
