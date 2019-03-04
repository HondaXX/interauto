package com.honda.interauto.dao.auto;

import com.honda.interauto.dto.ProModelDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProModelDao {
    public List<ProModelDto> getAllProModels(@Param("proId") Integer proId);

    public List<ProModelDto> getProModels(@Param("proId") Integer proId, @Param("modelId") Integer modelId, @Param("caseList") List<Integer> caseList);
}
