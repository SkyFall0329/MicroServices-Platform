package cn.bitoffer.msgcenter.controller;

import cn.bitoffer.common.model.BaseModel;
import cn.bitoffer.common.model.ResponseEntity;
import cn.bitoffer.msgcenter.model.TemplateModel;
import cn.bitoffer.msgcenter.model.dto.SendMsgReq;
import cn.bitoffer.msgcenter.tools.MsgRecordService;
import cn.bitoffer.msgcenter.service.SendMsgService;
import cn.bitoffer.msgcenter.service.TemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * web服务接口：http 接口
 **/

@RestController
@RequestMapping("/msg")
@Slf4j
public class MsgCenterController {

    @Resource
    private TemplateService templateService;

    @Resource
    private SendMsgService sendMsgService;

    @Resource
    private MsgRecordService msgRecordService;

    @PostMapping(value = "/create_template")
    public ResponseEntity<String> createTemplate(@RequestBody TemplateModel templateModel){
        String templateId = templateService.CreateTemplate(templateModel);
        return ResponseEntity.ok(templateId);
    }

    @GetMapping(value = "/get_template")
    public ResponseEntity<TemplateModel> getTemplate(@RequestParam(value = "templateId") String templateId){
        TemplateModel templateModel= templateService.GetTemplateWithCache(templateId);
        return ResponseEntity.ok(templateModel);
    }

    @PostMapping(value = "/update_template")
    public ResponseEntity<Void> updateTemplate(@RequestBody TemplateModel templateModel){
        templateService.UpdateTemplate(templateModel);
        return ResponseEntity.ok();
    }

    @PostMapping(value = "/del_template")
    public ResponseEntity<Void> delTemplate(@RequestParam(value = "templateId") String templateId){
        templateService.DeleteTemplate(templateId);
        return ResponseEntity.ok();
    }

    @PostMapping(value = "/send_msg")
    public ResponseEntity<String> send_msg(@RequestBody SendMsgReq sendMsgReq){
        String msgId = sendMsgService.SendMsg(sendMsgReq);
        return ResponseEntity.ok(msgId);
    }

    @GetMapping(value = "/get_msg_record")
    public ResponseEntity<BaseModel> getMsgRecord(@RequestParam(value = "msgId") String msgId){
        BaseModel msgRecordModel= msgRecordService.GetMsgRecordWithCache(msgId);
        return ResponseEntity.ok(msgRecordModel);
    }
}
