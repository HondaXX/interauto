package com.honda.interauto.dao.auto;

import com.honda.interauto.entity.ModelEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelDao {
    public List<ModelEntity> getProModels(@Param("proId") Integer proId);
}
