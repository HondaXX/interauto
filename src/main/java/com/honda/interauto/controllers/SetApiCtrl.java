package com.honda.interauto.controllers;


import com.honda.interauto.dto.InterCaseDto;
import com.honda.interauto.pojo.ReqPojo;
import com.honda.interauto.pojo.ResPojo;
import com.honda.interauto.services.InterCaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
        interCaseDto = (InterCaseDto) reqInfo.getRequestBody();
        Integer backCode = interCaseService.newInterCase(interCaseDto);
        if (backCode == 1){
            res.setResCode("0");
            res.putData("res", "success");
            return res;
        }else {
            res.setResCode("1");
            res.putData("res", "failed");
            return res;
        }
    }
}
