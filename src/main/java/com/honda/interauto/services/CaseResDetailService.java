package com.honda.interauto.services;

import com.honda.interauto.dao.auto.CaseResDetailDao;
import com.honda.interauto.entity.CaseResDetailEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CaseResDetailService {
    @Autowired
    private CaseResDetailDao caseResDetailDao;

    public Integer saveCaseRes(CaseResDetailEntity caseResDetailEntity){
        return caseResDetailDao.saveCaseRes(caseResDetailEntity);
    }
}
