package cn.bitoffer.msgcenter.model;


import cn.bitoffer.common.model.BaseModel;

import java.io.Serializable;

/**
 * TemplateModel 消息模板
 *
 **/
public class TemplateModel extends BaseModel implements Serializable {

    private Long id;

    private String templateId;

    private String relTemplateId;

    private String name;

    private String signName;

    private String sourceId;

    private int channel;

    private String subject;

    private String content;

    private int status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getRelTemplateId() {
        return relTemplateId;
    }

    public void setRelTemplateId(String relTemplateId) {
        this.relTemplateId = relTemplateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TemplateModel{" +
                "id=" + id +
                ", templateId='" + templateId + '\'' +
                ", relTemplateId='" + relTemplateId + '\'' +
                ", name='" + name + '\'' +
                ", signName='" + signName + '\'' +
                ", sourceId='" + sourceId + '\'' +
                ", channel=" + channel +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                ", status=" + status +
                '}';
    }
}
