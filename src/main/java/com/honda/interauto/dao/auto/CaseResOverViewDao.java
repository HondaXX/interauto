package com.honda.interauto.dao.auto;

import com.honda.interauto.dto.CaseResOverViewDto;
import com.honda.interauto.entity.CaseResOverViewEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaseResOverViewDao {
    Integer recordOverView(CaseResOverViewEntity caseResOverViewEntity);

    List<CaseResOverViewDto> getProOverView(@Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize,
                                         @Param("proId")Integer proId, @Param("operator")String operator);

    List<CaseResOverViewDto> getAppOverView(@Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize,
                                            @Param("appId")Integer appId, @Param("operator")String operator);

    List<CaseResOverViewDto> getAllProOverView(@Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize,
                                            @Param("proId") Integer proId);

    List<CaseResOverViewDto> getAllAppOverView(@Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize,
                                               @Param("appId") Integer appId);

    Integer getProCountRes(@Param("proId")Integer proId);
    Integer getAppCountRes(@Param("appId")Integer appId);
}
