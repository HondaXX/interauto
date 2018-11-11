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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/ApiSetter")
@Service

public class SetApiCtrl {
    Logger logger = LogManager.getLogger(SetApiCtrl.class);

    @Autowired
    private InterCaseDto interCaseDto;
    @Autowired
    private InterCaseService interCaseService;

    @RequestMapping(value = "/NewApi.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResPojo newApi(@RequestBody ReqPojo reqInfo){
        ResPojo res = new ResPojo();
        interCaseDto = (InterCaseDto) TypeChangeTool.mapToObject(reqInfo.getRequestBody(), InterCaseDto.class);

        Integer backCode = interCaseService.newInterCase(interCaseDto);
            if (backCode == 1){
                res.setResCode(BaseError.RESPONSE_OK);
                res.putData("res", "success");
                return res;
            }else {
                res.setResCode(BaseError.SYS_ERROR);
                res.putData("res", BaseError.SYS_ERROR_DESC);
                return res;
        }
    }

    @RequestMapping(value = "UpdataApi.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResPojo UpdataApi(@RequestBody ResPojo resPojo){
        return null;
    }
}
