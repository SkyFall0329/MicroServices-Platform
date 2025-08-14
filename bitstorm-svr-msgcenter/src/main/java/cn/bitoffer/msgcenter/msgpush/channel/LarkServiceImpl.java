package cn.bitoffer.msgcenter.msgpush.channel;

import cn.bitoffer.msgcenter.msgpush.MsgPushService;
import cn.bitoffer.msgcenter.msgpush.base.ChannelMsgBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LarkServiceImpl implements MsgPushService {

    @Override
    public void pushMsg(ChannelMsgBase msgBase) {

        log.info("发送 Lark!!!!! content:"+msgBase.getContent());
    }
}
