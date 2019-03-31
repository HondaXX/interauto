package com.honda.interauto.dao.auto;

import com.honda.interauto.dto.CaseResOverViewDto;
import com.honda.interauto.entity.CaseResOverViewEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaseResOverViewDao {
    Integer recordOverView(CaseResOverViewEntity caseResOverViewEntity);

    List<CaseResOverViewDto> getOverView(@Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize,
                                         @Param("proId")Integer proId, @Param("operator")String operator);

    List<CaseResOverViewDto> getAllOverView(@Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize);

    Integer getCountRes(@Param("proId")Integer proId);
}
