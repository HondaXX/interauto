package com.honda.interauto.dao.auto;

import com.honda.interauto.dto.InterCaseDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface InterCaseDao {
    public Integer newInterCase(InterCaseDto interCaseDto);

    public Integer updataInterCase(InterCaseDto interCaseDto);

    public Integer deleteInterCase(@Param("caseId")Integer caseId);

    public InterCaseDto getInterCaseByCaseId(@Param("caseId") Integer caseId);

    public List<InterCaseDto> getAllInterCases(@Param("proId")Integer proId, @Param("modelId")Integer modelId);

    public List<InterCaseDto> getInterCaseByCaseAim(@Param("caseAim")String caseAim);

    public List<InterCaseDto> getInterCaseByModel(@Param("proId")Integer proId, @Param("modelList") List<Integer> modelList);
}
