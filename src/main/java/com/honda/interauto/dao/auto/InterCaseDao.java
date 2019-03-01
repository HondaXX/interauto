package com.honda.interauto.dao.auto;

import com.honda.interauto.entity.InterCaseEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterCaseDao {
    public Integer newInterCase(InterCaseEntity interCaseDto);

    public Integer updataInterCase(InterCaseEntity interCaseDto);

    public Integer deleteInterCase(@Param("caseId")Integer caseId);

    public InterCaseEntity getInterCaseByCaseId(@Param("caseId") Integer caseId);

    public List<InterCaseEntity> getAllInterCases(@Param("proId")Integer proId, @Param("modelId")Integer modelId);

    public List<InterCaseEntity> getInterCaseByCaseAim(@Param("caseAim")String caseAim);

    public List<InterCaseEntity> getInterCaseByModel(@Param("proId")Integer proId, @Param("modelList") List<Integer> modelList);
}
