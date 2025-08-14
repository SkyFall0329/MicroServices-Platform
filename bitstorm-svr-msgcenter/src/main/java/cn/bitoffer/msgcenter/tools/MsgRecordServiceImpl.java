package cn.bitoffer.msgcenter.tools;

import cn.bitoffer.msgcenter.common.conf.SendMsgConf;
import cn.bitoffer.msgcenter.constant.Constants;
import cn.bitoffer.msgcenter.enums.MsgStatus;
import cn.bitoffer.msgcenter.mapper.MsgRecordMapper;
import cn.bitoffer.msgcenter.model.MsgRecordModel;
import cn.bitoffer.msgcenter.model.TemplateModel;
import cn.bitoffer.msgcenter.model.dto.SendMsgReq;
import cn.bitoffer.msgcenter.tools.MsgRecordService;
import cn.bitoffer.msgcenter.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;

@Service
@Slf4j
public class MsgRecordServiceImpl implements MsgRecordService {

    @Autowired
    private MsgRecordMapper msgRecordMapper;

    @Autowired
    private SendMsgConf sendMsgConf;

    @Resource
    private RedisTemplate<String,String> redisTemplate;


    @Override
    public MsgRecordModel GetMsgRecordWithCache(String msgId) {
        return getMsgRecordWithCache(msgId);
    }

    @Override
    public void CreateMsgRecord(String msgId, SendMsgReq sendMsgReq, TemplateModel tp, MsgStatus status) {
        // 6. 存储消息发送记录
        MsgRecordModel msgRd = new MsgRecordModel();
        msgRd.setMsgId(msgId);
        msgRd.setTo(sendMsgReq.getTo());
        msgRd.setSubject(sendMsgReq.getSubject());
        msgRd.setTemplateId(sendMsgReq.getTemplateId());
        msgRd.setTemplateData(JSONUtil.toJsonString(sendMsgReq.getTemplateData()));
        msgRd.setMsgId(sendMsgReq.getMsgID());
        msgRd.setSourceId(tp.getSourceId());
        msgRd.setChannel(tp.getChannel());
        msgRd.setStatus(status.getStatus());
        try{
            msgRecordMapper.save(msgRd);
        }catch (Exception e){
            log.error("存储消息发送记录失败， msgId",msgRd.getMsgId());
        }
    }

    @Override
    public void CreateOrUpdateMsgRecord(String msgId, SendMsgReq sendMsgReq, TemplateModel tp, MsgStatus status) {
        MsgRecordModel msgRd = msgRecordMapper.getMsgById(msgId);
        if(msgRd == null){
            msgRd = new MsgRecordModel();
            msgRd.setMsgId(msgId);
            msgRd.setTo(sendMsgReq.getTo());
            msgRd.setSubject(sendMsgReq.getSubject());
            msgRd.setTemplateId(sendMsgReq.getTemplateId());
            msgRd.setTemplateData(JSONUtil.toJsonString(sendMsgReq.getTemplateData()));
            msgRd.setMsgId(sendMsgReq.getMsgID());
            msgRd.setSourceId(tp.getSourceId());
            msgRd.setChannel(tp.getChannel());
            msgRd.setStatus(status.getStatus());
            try{
                msgRecordMapper.save(msgRd);
            }catch (Exception e){
                log.error("存储消息发送记录失败， msgId",msgRd.getMsgId());
            }
        }else{
            try{
                msgRecordMapper.setStatus(msgId,status.getStatus());
            }catch (Exception e){
                log.error("更新消息发送记录状态失败， msgId,status",msgRd.getMsgId(),status);
            }
        }
    }

    public MsgRecordModel getMsgRecordWithCache(String msgId) {
        String msgRecordCacheKey = Constants.REDIS_KEY_MES_RECORD+msgId;
        String cacheMr = redisTemplate.opsForValue().get(msgRecordCacheKey);
        MsgRecordModel mr = null;
        if(!StringUtils.isEmpty(cacheMr) && sendMsgConf.isOpenCache()){
            mr = JSONUtil.parseObject(cacheMr,MsgRecordModel.class);
            if(mr != null){
                return mr;
            }
        }

        // 从数据库获取
        mr = msgRecordMapper.getMsgById(msgId);

        // 存入缓存
        redisTemplate.opsForValue().set(msgRecordCacheKey,JSONUtil.toJsonString(mr), Duration.ofSeconds(30));

        return mr;
    }
}
