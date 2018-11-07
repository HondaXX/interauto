package com.honda.interauto.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/ApiManage")
public class RunApiCtrl {
    Logger logger = LogManager.getLogger(RunApiCtrl.class);

    @RequestMapping(value = "/RunApi.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String runApi(@RequestBody Map<String, Object> reqInfo){
        logger.info(reqInfo);
        return "success";
    }
}
