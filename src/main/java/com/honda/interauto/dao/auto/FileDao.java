package com.honda.interauto.dao.auto;

import com.honda.interauto.entity.FileEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileDao {
    List<FileEntity> getCreatorFiles(@Param("creator")String creator, @Param("createTime")String createTime);

    FileEntity downFileById(@Param("id") Integer id, @Param("creator")String creator, @Param("createTime")String createTime);
}
