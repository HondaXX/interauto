package com.honda.interauto.services;

import com.honda.interauto.dao.auto.CaseResOverViewDao;
import com.honda.interauto.entity.CaseResOverViewEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CaseResOverViewService {
    @Autowired
    private CaseResOverViewDao caseResOverViewDao;

    public Integer recordOverView(CaseResOverViewEntity caseResOverViewEntity){
        return caseResOverViewDao.recordOverView(caseResOverViewEntity);
    }

    public List<CaseResOverViewEntity> getAllOverView(Integer pageNum, Integer pageSize, Integer proId){
        return caseResOverViewDao.getAllOverView(pageNum, pageSize, proId);
    }

    public List<CaseResOverViewEntity> getOperatorOverView(Integer pageNum, Integer pageSize, String operator){
        return caseResOverViewDao.getOperatorOverView(pageNum, pageSize, operator);
    }

    public Integer getCountRes(Integer proId){
        return caseResOverViewDao.getCountRes(proId);
    }

    public List<CaseResOverViewEntity> getAllProRes(Integer pageNum, Integer pageSize){
        return caseResOverViewDao.getAllProRes(pageNum, pageSize);
    }

}
