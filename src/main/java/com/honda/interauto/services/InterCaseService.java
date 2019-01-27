package com.honda.interauto.services;

import com.honda.interauto.dao.auto.InterCaseDao;
import com.honda.interauto.dto.InterCaseDto;
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

    public Integer newInterCase(InterCaseDto interCaseDto){
        return interCaseDao.newInterCase(interCaseDto);
    }

    public Integer updataInterCase(InterCaseDto interCaseDto){
        return interCaseDao.updataInterCase(interCaseDto);
    }

    public Integer deleteInterCase(Integer caseId){
        return interCaseDao.deleteInterCase(caseId);
    }

    public InterCaseDto getInterCaseByCaseId(Integer caseId){
        return interCaseDao.getInterCaseByCaseId(caseId);
    }

    public List<InterCaseDto> getAllInterCases(Integer proId, Integer modelId){
        return interCaseDao.getAllInterCases(proId, modelId);
    }

    public List<InterCaseDto> getInterCaseByCaseAim(String caseAim){
        return interCaseDao.getInterCaseByCaseAim(caseAim);
    }

    public Map<Integer, List<InterCaseDto>> getInterCaseByModel(Integer proId, List<Integer> modelIdList){
        List<InterCaseDto> caseList = interCaseDao.getInterCaseByModel(proId, modelIdList);

        Map<Integer, List<InterCaseDto>> proModelMap = new HashMap<Integer, List<InterCaseDto>>();

        for (Integer modelId : modelIdList){
            Map<Integer, List<InterCaseDto>> proModelMapTemp = new HashMap<Integer, List<InterCaseDto>>();
            List<InterCaseDto> modelListSub = new ArrayList<InterCaseDto>();
            for (int i = 0; i < caseList.size(); i++){
                InterCaseDto interCaseDto = caseList.get(i);
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
