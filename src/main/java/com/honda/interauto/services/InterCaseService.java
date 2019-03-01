package com.honda.interauto.services;

import com.honda.interauto.dao.auto.InterCaseDao;
import com.honda.interauto.entity.InterCaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InterCaseService {
    @Autowired
    private InterCaseDao interCaseDao;

    public Integer newInterCase(InterCaseEntity interCaseDto){
        return interCaseDao.newInterCase(interCaseDto);
    }

    public Integer updataInterCase(InterCaseEntity interCaseDto){
        return interCaseDao.updataInterCase(interCaseDto);
    }

    public Integer deleteInterCase(Integer caseId){
        return interCaseDao.deleteInterCase(caseId);
    }

    public InterCaseEntity getInterCaseByCaseId(Integer caseId){
        return interCaseDao.getInterCaseByCaseId(caseId);
    }

    public List<InterCaseEntity> getAllInterCases(Integer proId, Integer modelId){
        return interCaseDao.getAllInterCases(proId, modelId);
    }

    public List<InterCaseEntity> getInterCaseByCaseAim(String caseAim){
        return interCaseDao.getInterCaseByCaseAim(caseAim);
    }

    public Map<Integer, List<InterCaseEntity>> getInterCaseByModel(Integer proId, List<Integer> modelIdList){
        List<InterCaseEntity> caseList = interCaseDao.getInterCaseByModel(proId, modelIdList);

        Map<Integer, List<InterCaseEntity>> proModelMap = new HashMap<Integer, List<InterCaseEntity>>();

        for (Integer modelId : modelIdList){
            Map<Integer, List<InterCaseEntity>> proModelMapTemp = new HashMap<Integer, List<InterCaseEntity>>();
            List<InterCaseEntity> modelListSub = new ArrayList<InterCaseEntity>();
            for (int i = 0; i < caseList.size(); i++){
                InterCaseEntity interCaseDto = caseList.get(i);
                if (interCaseDto.getModelId() == modelId){
                    modelListSub.add(interCaseDto);
                }
            }
            proModelMapTemp.put(modelId, modelListSub);
            proModelMap.putAll(proModelMapTemp);
        }
        return proModelMap;
    }
}
