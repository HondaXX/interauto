package com.honda.interauto.services;

import com.honda.interauto.dao.auto.CaseResOverViewDao;
import com.honda.interauto.dto.CaseResOverViewDto;
import com.honda.interauto.entity.CaseResOverViewEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CaseResOverViewService {
    @Autowired
    private CaseResOverViewDao caseResOverViewDao;

    public Integer recordOverView(CaseResOverViewEntity caseResOverViewEntity){
        return caseResOverViewDao.recordOverView(caseResOverViewEntity);
    }

    public List<CaseResOverViewDto> getOverView(String queryType, Integer pageNum, Integer pageSize, Integer proId, Integer appId, String operator){
        if (queryType.equals("pro")){
            return caseResOverViewDao.getProOverView(pageNum, pageSize, proId, operator);
        }
        if (queryType.equals("app")){
            return caseResOverViewDao.getAppOverView(pageNum, pageSize, appId, operator);
        }
        return null;
    }


    public Integer getCountRes(String queryType, Integer proId, Integer appId){
        if (queryType.equals("pro")){
            return caseResOverViewDao.getProCountRes(proId);
        }
        if (queryType.equals("app")){
            return caseResOverViewDao.getAppCountRes(appId);
        }
        return null;
    }

    public List<CaseResOverViewDto> getAllOverView(Integer pageNum, Integer pageSize, Integer proId, Integer appId){
        List<CaseResOverViewDto> resOverViewDtoList = new ArrayList<CaseResOverViewDto>();
        if (proId == null && appId == null){
            resOverViewDtoList.addAll(caseResOverViewDao.getAllProOverView(pageNum, pageSize, proId));
            resOverViewDtoList.addAll(caseResOverViewDao.getAllAppOverView(pageNum, pageSize, appId));
            return resOverViewDtoList;
        }
        if (proId != null){
            resOverViewDtoList.addAll(caseResOverViewDao.getAllProOverView(pageNum, pageSize, proId));
        }
        if (appId != null){
            resOverViewDtoList.addAll(caseResOverViewDao.getAllAppOverView(pageNum, pageSize, appId));
        }
        return resOverViewDtoList;
    }

}
