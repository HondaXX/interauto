package com.honda.interauto.services;

import com.honda.interauto.dao.auto.ProModelDao;
import com.honda.interauto.dto.ProModelDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProModelService {
    @Autowired
    private ProModelDao proModelDao;

    public List<ProModelDto> getAllProModels(Integer proId){
        return proModelDao.getAllProModels(proId);
    }

    public List<ProModelDto> getProModels(Integer proId, Integer modelId,List<Integer> caseList){
        return proModelDao.getProModels(proId, modelId, caseList);
    }
}
