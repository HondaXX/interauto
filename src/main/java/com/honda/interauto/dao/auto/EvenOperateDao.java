package com.honda.interauto.dao.auto;

import com.honda.interauto.entity.EvenOperateEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvenOperateDao {
    List<EvenOperateEntity> getEvenByEvenId(@Param("evenId")Integer evenId);
}
