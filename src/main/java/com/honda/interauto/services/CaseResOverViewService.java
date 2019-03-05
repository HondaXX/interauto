package com.honda.interauto.services;

import com.honda.interauto.dao.auto.CaseResOverViewDao;
import com.honda.interauto.entity.CaseResOverViewEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CaseResOverViewService {
    @Autowired
    private CaseResOverViewDao caseResOverViewDao;

    public Integer recordOverView(CaseResOverViewEntity caseResOverViewEntity){
        return caseResOverViewDao.recordOverView(caseResOverViewEntity);
    }
}
