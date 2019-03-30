package com.honda.interauto.dao.auto;

import com.honda.interauto.entity.CaseResOverViewEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaseResOverViewDao {
    Integer recordOverView(CaseResOverViewEntity caseResOverViewEntity);

    List<CaseResOverViewEntity> getAllOverView(@Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize, @Param("proId")Integer proId);

    List<CaseResOverViewEntity> getOperatorOverView(@Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize, @Param("operator")String operator);

    Integer getCountRes(@Param("proId")Integer proId);

    List<CaseResOverViewEntity> getAllProRes(@Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize);
}
