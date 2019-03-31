package com.honda.interauto.services;

import com.honda.interauto.dao.auto.CaseResOverViewDao;
import com.honda.interauto.dto.CaseResOverViewDto;
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

    public List<CaseResOverViewDto> getOverView(Integer pageNum, Integer pageSize, Integer proId, String operator){
        return caseResOverViewDao.getOverView(pageNum, pageSize, proId, operator);
    }


    public Integer getCountRes(Integer proId){
        return caseResOverViewDao.getCountRes(proId);
    }

    public List<CaseResOverViewDto> getAllOverView(Integer pageNum, Integer pageSize){
        return caseResOverViewDao.getAllOverView(pageNum, pageSize);
    }

}
