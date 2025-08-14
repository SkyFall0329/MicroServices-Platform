package cn.bitoffer.msgcenter.service;

import cn.bitoffer.msgcenter.model.TemplateModel;
import cn.bitoffer.msgcenter.model.dto.SendMsgReq;

public interface SendMsgService {

    String SendMsg(SendMsgReq sendMsgReq);

}
