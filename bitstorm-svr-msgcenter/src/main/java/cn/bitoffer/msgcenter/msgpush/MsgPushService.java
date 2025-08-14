package cn.bitoffer.msgcenter.msgpush;

import cn.bitoffer.msgcenter.model.dto.SendMsgReq;
import cn.bitoffer.msgcenter.msgpush.base.ChannelMsgBase;

public interface MsgPushService {
    void pushMsg(ChannelMsgBase msgBase);
}
