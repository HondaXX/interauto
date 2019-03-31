package com.honda.interauto.services;

import com.honda.interauto.dao.auto.CaseResDetailDao;
import com.honda.interauto.dto.CaseResDto;
import com.honda.interauto.entity.CaseResDetailEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CaseResDetailService {
    @Autowired
    private CaseResDetailDao caseResDetailDao;

    public Integer saveCaseRes(CaseResDetailEntity caseResDetailEntity){
        return caseResDetailDao.saveCaseRes(caseResDetailEntity);
    }

    public List<CaseResDto> getCaseResDetail(String runTagId, Integer pageNum, Integer pageSize, String caseRes, Integer caseId, String caseAim, String interUrl){
        return caseResDetailDao.getCaseResDetail(runTagId, pageNum, pageSize, caseRes, caseId, caseAim, interUrl);
    }

    public Integer getTagResCount(String runTagId){
        return caseResDetailDao.getTagResCount(runTagId);
    }

}
