package com.honda.interauto.controllers;

import com.honda.interauto.dto.ModelDto;
import com.honda.interauto.dto.ProDto;
import com.honda.interauto.pojo.BaseError;
import com.honda.interauto.pojo.ReqPojo;
import com.honda.interauto.pojo.ResPojo;
import com.honda.interauto.services.ModelService;
import com.honda.interauto.services.ProService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/ModelManage")
@Service
public class SetModelCtrl {
    @Autowired
    private ProService proService;
    @Autowired
    private ModelService modelService;


    @PostMapping(value = "/GetAllPro.json", produces = "application/json;charset=UTF-8")
    public ResPojo getAllApi(@RequestBody ReqPojo reqInfo){
        ResPojo res = new ResPojo();
        List<ProDto> proList =  proService.getAllPros();
        if (proList.size() <= 0){
            res.setErrorCode(BaseError.DB_ERROR);
            res.setErrorDesc(BaseError.DB_ERROR_DESC);
            return res;
        }
        res.setResCode(BaseError.RESPONSE_OK);
        res.putData("proList", proList);
        return res;
    }

    @PostMapping(value = "/GetProModels.json", produces = "application/json;charset=UTF-8")
    public ResPojo getProModels(@RequestBody ReqPojo reqInfo){
        ResPojo res = new ResPojo();

        Integer proId = Integer.parseInt(reqInfo.getRequestBody().get("proId").toString());
        List<ModelDto> modelList = modelService.getProModels(proId);
        if (modelList.size() <= 0){
            res.setErrorCode(BaseError.DB_ERROR);
            res.setErrorDesc(BaseError.DB_ERROR_DESC);
            return res;
        }
        res.setResCode(BaseError.RESPONSE_OK);
        res.putData("modelList", modelList);
        return res;
    }
}
