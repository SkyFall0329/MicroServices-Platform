package cn.bitoffer.msgcenter.mapper;


import cn.bitoffer.msgcenter.model.MsgQueueModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MsgQueueMapper {

    void save(@Param("tableName") String tableName,@Param("msgQueueModel") MsgQueueModel msgQueueModel);

    MsgQueueModel getMsgById(@Param("tableName") String tableName,@Param("msgId") String msgId);

    List<MsgQueueModel> getMsgsByStatus(@Param("tableName") String tableName,@Param("status") int status,@Param("limit") int limit);

    void batchSetStatus(@Param("tableName") String tableName,@Param("msgIdList") String msgIdList,@Param("status") int status);

    void setStatus(@Param("tableName") String tableName,@Param("msgId") String msgId,@Param("status") int status);
}
