package com.honda.interauto.controllers;

import com.honda.interauto.dto.InterCaseDto;
import com.honda.interauto.pojo.BaseError;
import com.honda.interauto.pojo.ReqPojo;
import com.honda.interauto.pojo.ResPojo;
import com.honda.interauto.services.InterCaseService;
import com.honda.interauto.tools.sysTool.TypeChangeTool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/ApiSetter")
@Service
public class SetApiCtrl {
    Logger logger = LogManager.getLogger(SetApiCtrl.class);

    @Autowired
    private InterCaseDto interCaseDto;
    @Autowired
    private InterCaseService interCaseService;
    @Autowired
    private ResPojo res;

    @RequestMapping(value = "/NewApi.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResPojo newApi(@RequestBody ReqPojo reqInfo){
        interCaseDto = (InterCaseDto) TypeChangeTool.mapToObject(reqInfo.getRequestBody(), InterCaseDto.class);
        if (interCaseDto.equals(null)){
            res.setErrorCode(BaseError.SYS_ERROR);
            res.setErrorDesc(BaseError.SYS_ERROR_DESC);
            return res;
        }

        interCaseDto.setCreateTime(TypeChangeTool.stampToDateStr(new Date().getTime()));
        interCaseDto.setUpdataTime(TypeChangeTool.stampToDateStr(new Date().getTime()));
        Integer backCode = interCaseService.newInterCase(interCaseDto);
            if (backCode == 1){
                res.setResCode(BaseError.RESPONSE_OK);
                res.putData("res", "new api success");
                return res;
            }else {
                res.setResCode(BaseError.DB_ERROR);
                res.putData("res", BaseError.DB_ERROR_DESC);
                return res;
        }
    }

    @RequestMapping(value = "/UpdataApi.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResPojo updataApi(@RequestBody ReqPojo reqInfo){
        interCaseDto = (InterCaseDto) TypeChangeTool.mapToObject(reqInfo.getRequestBody(), InterCaseDto.class);
        if (interCaseDto.equals(null)){
            res.setErrorCode(BaseError.SYS_ERROR);
            res.setErrorDesc(BaseError.SYS_ERROR_DESC);
            return res;
        }

        interCaseDto.setUpdataTime(TypeChangeTool.stampToDateStr(new Date().getTime()));
        Integer backCode = interCaseService.updataInterCase(interCaseDto);
        if (backCode == 1){
            res.setResCode(BaseError.RESPONSE_OK);
            res.putData("res", "update api success");
            return res;
        }else {
            res.setResCode(BaseError.DB_ERROR);
            res.putData("res", BaseError.DB_ERROR_DESC);
            return res;
        }
    }

    @PostMapping(value = "/DelApi.json", produces = "application/json;charset=UTF-8")
    public ResPojo delApi(@RequestBody ReqPojo reqInfo){
        Integer caseId = Integer.parseInt((reqInfo.getRequestBody().get("caseId")).toString());

        if (null != caseId){
            Integer backCode = interCaseService.deleteInterCase(caseId);
            if (backCode == 1){
                res.setResCode(BaseError.RESPONSE_OK);
                res.putData("res", "delete api success");
                return res;
            }else {
                res.setResCode(BaseError.DB_ERROR);
                res.putData("res", BaseError.DB_ERROR_DESC);
                return res;
            }
        }else {
            res.setErrorCode(BaseError.PARAM_ERROR);
            res.setErrorDesc(BaseError.PARAM_ERROR_DESC + ": caseID");
            return res;
        }

    }

    @PostMapping(value = "/GetAllApi.json", produces = "application/json;charset=UTF-8")
    public ResPojo getAllApi(@RequestBody ReqPojo reqInfo){
        List<InterCaseDto> interCaseList = interCaseService.getAllInterCases();
        if (interCaseList != null && interCaseList.size() > 0){
            logger.info("get " + interCaseList.size() + " nums of interCase");
            res.setResCode(BaseError.RESPONSE_OK);
            res.putData("interList", interCaseList);
            return res;
        }else {
            res.setErrorCode(BaseError.DB_ERROR);
            res.setErrorDesc(BaseError.DB_ERROR_DESC);
            return res;
        }
    }

    @PostMapping(value = "/GetApiByAim.json", produces = "application/json;charset=UTF-8")
    public ResPojo getApiByAim(@RequestBody ReqPojo reqInfo){
        List<InterCaseDto> interCaseList = interCaseService.getInterCaseByCaseAim(reqInfo.getRequestBody().get("caseAim").toString());
        if (interCaseList != null && interCaseList.size() > 0){
            logger.info("get " + interCaseList.size() + " nums of interCase");
            res.setResCode(BaseError.RESPONSE_OK);
            res.putData("interList", interCaseList);
            return res;
        }else {
            res.setErrorCode(BaseError.DB_ERROR);
            res.setErrorDesc(BaseError.DB_ERROR_DESC);
            return res;
        }
    }
}
