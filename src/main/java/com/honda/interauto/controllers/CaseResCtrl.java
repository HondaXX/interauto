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
        Integer proId = Integer.parseInt(reqPojo.getRequestBody().get("proId").toString());

        List<CaseResOverViewEntity> caseResList = new ArrayList<CaseResOverViewEntity>();
        if (StringUtils.isBlank(operator)){
            caseResList = caseResOverViewService.getAllOverView(pageNum, pageSize, proId);
        }else {
            caseResList = caseResOverViewService.getOperatorOverView(pageNum, pageSize, operator);
        }
        int resCount = caseResOverViewService.getCountRes(proId);
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
        String caseRes = reqPojo.getRequestBody().get("caseRes").toString();

        List<CaseResDetailEntity> caseDetailList = caseResDetailService.getTagResDetail(runTagId, pageNum, pageSize, caseRes);
        int detailCount = caseResDetailService.getTagResCount(runTagId);
        ResPojo res = new ResPojo();
        res.setResCode(BaseError.RESPONSE_OK);
        res.putData("count", detailCount);
        res.putData("resList", caseDetailList);
        return res;
    }

    @PostMapping(value = "GetAllProRes.json", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResPojo getAllProRes(@RequestBody ReqPojo reqPojo){
        Integer pageNum = Integer.parseInt(reqPojo.getRequestBody().get("pageNum").toString());
        Integer pageSize = Integer.parseInt(reqPojo.getRequestBody().get("pageSize").toString());

        List<CaseResOverViewEntity> proOverView = caseResOverViewService.getAllProRes(pageNum, pageSize);

        ResPojo res = new ResPojo();
        res.setResCode(BaseError.RESPONSE_OK);
        res.putData("resList", proOverView);
        return res;
    }
}
