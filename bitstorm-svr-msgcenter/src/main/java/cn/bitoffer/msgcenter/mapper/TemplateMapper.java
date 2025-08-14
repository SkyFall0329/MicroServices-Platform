package cn.bitoffer.msgcenter.mapper;


import cn.bitoffer.msgcenter.model.TemplateModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TemplateMapper {


    void save(@Param("templateModel") TemplateModel templateModel);

    void deleteById(@Param("templateId") String templateId);

    void update(@Param("templateModel") TemplateModel templateModel);

    TemplateModel getTemplateById(@Param("templateId") String templateId);
}
