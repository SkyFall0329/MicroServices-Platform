package cn.bitoffer.msgcenter.manager;

import cn.bitoffer.msgcenter.model.dto.SendMsgReq;

public interface SendMsgManager{
    public String SendToMysql(SendMsgReq sendMsgReq);
    public String SendToMq(SendMsgReq sendMsgReq);

    public String SendToTimer(SendMsgReq sendMsgReq);
}
