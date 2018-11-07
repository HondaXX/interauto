package com.honda.interauto.controllers;


import com.honda.interauto.pojo.ReqPojo;
import com.honda.interauto.pojo.ResPojo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/ApiSetter")

public class SetApiCtrl {
    Logger logger = LogManager.getLogger(SetApiCtrl.class);

    @RequestMapping(value = "/NewApi.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResPojo newApi(@RequestBody ReqPojo reqInfo){
        ResPojo res = new ResPojo();
        res.setResCode("0");
        res.putData("res", "success");
        return res;
    }
}
