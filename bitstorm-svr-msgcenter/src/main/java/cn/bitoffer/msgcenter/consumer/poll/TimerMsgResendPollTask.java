package cn.bitoffer.msgcenter.consumer.poll;

import cn.bitoffer.msgcenter.common.conf.SendMsgConf;
import cn.bitoffer.msgcenter.enums.MsgStatus;
import cn.bitoffer.msgcenter.enums.TemplateStatus;
import cn.bitoffer.msgcenter.exception.BusinessException;
import cn.bitoffer.msgcenter.exception.ErrorCode;
import cn.bitoffer.msgcenter.manager.SendMsgManager;
import cn.bitoffer.msgcenter.mapper.MsgQueueTimerMapper;
import cn.bitoffer.msgcenter.model.TemplateModel;
import cn.bitoffer.msgcenter.model.dto.SendMsgReq;
import cn.bitoffer.msgcenter.service.TemplateService;
import cn.bitoffer.msgcenter.tools.MsgRecordService;
import cn.bitoffer.msgcenter.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TimerMsgResendPollTask {

    @Autowired
    MsgQueueTimerMapper msgQueueTimerMapper;


    @Autowired
    SendMsgManager sendMsgManager;

    @Autowired
    SendMsgConf sendMsgConf;

    @Autowired
    MsgRecordService msgRecordService;

    @Autowired
    TemplateService templateService;

    @Async("timerMsgPoll")
    public void asyncHandleMsg(String  reqStr) {
        SendMsgReq sendMsgReq = JSONUtil.parseObject(reqStr,SendMsgReq.class);
        if (sendMsgReq == null){
            return;
        }
        TemplateModel tp = templateService.GetTemplateWithCache(sendMsgReq.getTemplateId());
        if(tp.getStatus() != TemplateStatus.TEMPLATE_STATUS_NORMAL.getStatus()){
            throw new BusinessException(ErrorCode.TEMPLATE_STATUS_ERROR, "模板尚未准备好，检查模板状态");
        }
        boolean success = false;
        try {
            if(sendMsgConf.isMysqlAsMq()){
                // 发送到 Mysql
                sendMsgManager.SendToMysql(sendMsgReq);
            }else{
                // 发送到 MQ
                sendMsgManager.SendToMq(sendMsgReq);
            }
            success = true;
        }catch (Exception e){
            // 重试一次
            if(sendMsgConf.isMysqlAsMq()){
                // 发送到 Mysql
                sendMsgManager.SendToMysql(sendMsgReq);
            }else{
                // 发送到 MQ
                sendMsgManager.SendToMq(sendMsgReq);
            }
            success = true;
        }

        // 2.更新消息记录状态
        if (success) {
            msgRecordService.CreateOrUpdateMsgRecord(sendMsgReq.getMsgID(),sendMsgReq,tp,MsgStatus.Pending);
        }else{
            msgRecordService.CreateOrUpdateMsgRecord(sendMsgReq.getMsgID(),sendMsgReq,tp,MsgStatus.Failed);
        }

        // 将msgId消息变为处理中Succeed
        msgQueueTimerMapper.setStatus(sendMsgReq.getMsgID(), MsgStatus.Succeed.getStatus());
    }
}
