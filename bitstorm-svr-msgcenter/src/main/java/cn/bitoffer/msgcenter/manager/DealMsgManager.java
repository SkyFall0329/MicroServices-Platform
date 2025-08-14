package cn.bitoffer.msgcenter.manager;

import cn.bitoffer.msgcenter.model.dto.SendMsgReq;

public interface DealMsgManager {

    public void DealOneMsg(SendMsgReq sendMsgReq);
}
