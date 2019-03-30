package com.honda.interauto.dao.auto;

import com.honda.interauto.entity.CaseResDetailEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaseResDetailDao {
    Integer saveCaseRes(CaseResDetailEntity caseResDetailEntity);

    List<CaseResDetailEntity> getTagResDetail(@Param("runTagId")String runTagId, @Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize, @Param("caseRes")String caseRes);

    Integer getTagResCount(@Param("runTagId")String runTagId);

}
