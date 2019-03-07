package com.honda.interauto.controllers;

import com.honda.interauto.entity.CaseResDetailEntity;
import com.honda.interauto.entity.CaseResOverViewEntity;
import com.honda.interauto.pojo.BaseError;
import com.honda.interauto.pojo.ReqPojo;
import com.honda.interauto.pojo.ResPojo;
import com.honda.interauto.services.CaseResDetailService;
import com.honda.interauto.services.CaseResOverViewService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/Result")
public class CaseResCtrl {
    @Autowired
    private CaseResOverViewService caseResOverViewService;
    @Autowired
    private CaseResDetailService caseResDetailService;

    @PostMapping(value = "/GetRes.json", produces = "application/json;charset=UTF-8")
    public ResPojo getRes(@RequestBody ReqPojo reqPojo){
        Integer pageNum = Integer.parseInt(reqPojo.getRequestBody().get("pageNum").toString());
        Integer pageSize = Integer.parseInt(reqPojo.getRequestBody().get("pageSize").toString());
        String operator = reqPojo.getRequestBody().get("operator").toString();

        List<CaseResOverViewEntity> caseResList = new ArrayList<CaseResOverViewEntity>();
        if (StringUtils.isBlank(operator)){
            caseResList = caseResOverViewService.getAllOverView(pageNum, pageSize);
        }else {
            caseResList = caseResOverViewService.getOperatorOverView(pageNum, pageSize, operator);
        }
        int resCount = caseResOverViewService.getCountRes();
        ResPojo res = new ResPojo();
        res.putData("count", resCount);
        res.setResCode(BaseError.RESPONSE_OK);
        res.putData("resList", caseResList);
        return res;
    }

    @PostMapping(value = "GetResDetail.json", produces = "application/json;charset=UTF-8")
    public ResPojo getResDetail(@RequestBody ReqPojo reqPojo){
        Integer pageNum = Integer.parseInt(reqPojo.getRequestBody().get("pageNum").toString());
        Integer pageSize = Integer.parseInt(reqPojo.getRequestBody().get("pageSize").toString());
        String runTagId = reqPojo.getRequestBody().get("tagId").toString();
        String status = reqPojo.getRequestBody().get("status").toString();

        List<CaseResDetailEntity> caseDetailList = caseResDetailService.getTagResDetail(runTagId, pageNum, pageSize, status);
        int detailCount = caseResDetailService.getTagResCount(runTagId);
        ResPojo res = new ResPojo();
        res.setResCode(BaseError.RESPONSE_OK);
        res.putData("count", detailCount);
        res.putData("resList", caseDetailList);
        return res;
    }
}
