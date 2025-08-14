package cn.bitoffer.msgcenter.tools;

import cn.bitoffer.msgcenter.enums.MsgStatus;
import cn.bitoffer.msgcenter.model.MsgRecordModel;
import cn.bitoffer.msgcenter.model.TemplateModel;
import cn.bitoffer.msgcenter.model.dto.SendMsgReq;

public interface MsgRecordService {

    MsgRecordModel GetMsgRecordWithCache(String msgId);

    void CreateMsgRecord(String msgId,SendMsgReq sendMsgReq, TemplateModel tp, MsgStatus status);

    void CreateOrUpdateMsgRecord(String msgId,SendMsgReq sendMsgReq, TemplateModel tp, MsgStatus status);
}
