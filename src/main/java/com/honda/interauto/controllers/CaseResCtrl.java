package com.honda.interauto.controllers;

import com.honda.interauto.dto.CaseResDto;
import com.honda.interauto.dto.CaseResOverViewDto;
import com.honda.interauto.entity.CaseResDetailEntity;
import com.honda.interauto.entity.CaseResOverViewEntity;
import com.honda.interauto.pojo.BaseError;
import com.honda.interauto.pojo.ReqPojo;
import com.honda.interauto.pojo.ResPojo;
import com.honda.interauto.services.CaseResDetailService;
import com.honda.interauto.services.CaseResOverViewService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/Result")
public class CaseResCtrl {
    @Autowired
    private CaseResOverViewService caseResOverViewService;
    @Autowired
    private CaseResDetailService caseResDetailService;

    @GetMapping(value = "/main")
    public String backMain(Model model){
        return "main";
    }

    @PostMapping(value = "/GetRes.json", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResPojo getRes(@RequestBody ReqPojo reqPojo){
        Integer pageNum = Integer.parseInt(reqPojo.getRequestBody().get("pageNum").toString());
        Integer pageSize = Integer.parseInt(reqPojo.getRequestBody().get("pageSize").toString());

        String operator = reqPojo.getRequestBody().get("operator").toString();
        String proIdStr = reqPojo.getRequestBody().get("proId").toString();
        String appIdStr = reqPojo.getRequestBody().get("appId").toString();

        String queryType = reqPojo.getRequestBody().get("queryType").toString();

        Integer proId = null;
        if (!StringUtils.isBlank(proIdStr)){
            proId = Integer.parseInt(proIdStr);
        }
        Integer appId = null;
        if (!StringUtils.isBlank(appIdStr)){
            appId = Integer.parseInt(appIdStr);
        }

        List<CaseResOverViewDto> caseResList = new ArrayList<CaseResOverViewDto>();
        caseResList = caseResOverViewService.getOverView(queryType, pageNum, pageSize, proId, appId, operator);

        int resCount = caseResOverViewService.getCountRes(queryType, proId, appId);

        ResPojo res = new ResPojo();
        res.putData("count", resCount);
        res.setResCode(BaseError.RESPONSE_OK);
        res.putData("resList", caseResList);
        return res;
    }

    @PostMapping(value = "GetResDetail.json", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResPojo getResDetail(@RequestBody ReqPojo reqPojo){
        Integer pageNum = Integer.parseInt(reqPojo.getRequestBody().get("pageNum").toString());
        Integer pageSize = Integer.parseInt(reqPojo.getRequestBody().get("pageSize").toString());
        String runTagId = reqPojo.getRequestBody().get("tagId").toString();

        String caseIdStr = reqPojo.getRequestBody().get("caseId").toString();
        String caseRes = reqPojo.getRequestBody().get("caseRes").toString();
        String interUrl = reqPojo.getRequestBody().get("interUrl").toString();
        String caseAim = reqPojo.getRequestBody().get("caseAim").toString();

        String appIdStr = reqPojo.getRequestBody().get("appId").toString();
        String evenName  = reqPojo.getRequestBody().get("evenName").toString();
        String evenIdStr = reqPojo.getRequestBody().get("evenId").toString();

        String queryType = reqPojo.getRequestBody().get("queryType").toString();

        Integer caseId = null;
        if (!StringUtils.isBlank(caseIdStr)){
            caseId = Integer.parseInt(caseIdStr);
        }
        Integer appId = null;
        if (!StringUtils.isBlank(appIdStr)){
            appId = Integer.parseInt(appIdStr);
        }
        Integer evenId = null;
        if (!StringUtils.isBlank(evenIdStr)){
            evenId = Integer.parseInt(evenIdStr);
        }

        List<CaseResDto> resDetailList = caseResDetailService.getCaseResDetail(queryType, runTagId, pageNum, pageSize, caseRes, caseId, caseAim, interUrl, evenId, evenName, appId);

        ResPojo res = new ResPojo();
        res.setResCode(BaseError.RESPONSE_OK);
        if (!StringUtils.isBlank(runTagId)){
            int detailCount = caseResDetailService.getTagResCount(runTagId);
            res.putData("count", detailCount);
        }
        res.putData("resList", resDetailList);
        return res;
    }

    @PostMapping(value = "GetAllProRes.json", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResPojo getAllProRes(@RequestBody ReqPojo reqPojo){
        Integer pageNum = Integer.parseInt(reqPojo.getRequestBody().get("pageNum").toString());
        Integer pageSize = Integer.parseInt(reqPojo.getRequestBody().get("pageSize").toString());

        Integer proId = null;
        if (StringUtils.isNotBlank(reqPojo.getRequestBody().get("proId").toString())){
            proId = Integer.parseInt(reqPojo.getRequestBody().get("proId").toString());
        }
        Integer appId = null;
        if (StringUtils.isNotBlank(reqPojo.getRequestBody().get("appId").toString())){
            appId = Integer.parseInt(reqPojo.getRequestBody().get("appId").toString());
        }

        List<CaseResOverViewDto> proOverView = caseResOverViewService.getAllOverView(pageNum, pageSize, proId, appId);

        ResPojo res = new ResPojo();
        res.setResCode(BaseError.RESPONSE_OK);
        res.putData("resList", proOverView);
        return res;
    }
}
