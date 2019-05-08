package com.honda.interauto.dao.auto;

import com.honda.interauto.dto.CaseResDto;
import com.honda.interauto.entity.CaseResDetailEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaseResDetailDao {
    Integer saveCaseRes(CaseResDetailEntity caseResDetailEntity);

    List<CaseResDto> getProResDetail(@Param("runTagId")String runTagId, @Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize,
                                      @Param("caseRes")String caseRes, @Param("caseId") Integer caseId,
                                      @Param("caseAim")String caseAim, @Param("interUrl")String interUrl);

    List<CaseResDto> getAppResDetail(@Param("runTagId")String runTagId, @Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize,
                                     @Param("caseRes")String caseRes, @Param("evenId") Integer evenId,
                                     @Param("evenName")String evenName, @Param("appId")Integer appId);

    Integer getTagResCount(@Param("runTagId")String runTagId);

}
