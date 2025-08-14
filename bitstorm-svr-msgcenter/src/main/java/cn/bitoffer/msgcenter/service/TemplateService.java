package cn.bitoffer.msgcenter.service;

import cn.bitoffer.msgcenter.model.TemplateModel;

public interface TemplateService {

    String CreateTemplate(TemplateModel templateModel);

    void DeleteTemplate(String templateID);

    void UpdateTemplate(TemplateModel templateModel);

    TemplateModel GetTemplate(String templateID);

    TemplateModel GetTemplateWithCache(String templateID);
}
