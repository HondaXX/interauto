package com.honda.interauto.dao.auto;

import com.honda.interauto.dto.InterCaseDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterCaseDao {
    public Integer newInterCase(InterCaseDto interCaseDto);

    public Integer updataInterCase(InterCaseDto interCaseDto);

    public Integer deleteInterCase(@Param("caseId")Integer caseId);

    public List<InterCaseDto> getAllInterCases();

    public List<InterCaseDto> getInterCaseByCaseAim(@Param("caseAim")String caseAim);
}
