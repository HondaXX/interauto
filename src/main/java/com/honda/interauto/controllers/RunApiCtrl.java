package com.honda.interauto.controllers;

import com.honda.interauto.pojo.BaseConfigs;
import com.honda.interauto.pojo.ReqPojo;
import com.honda.interauto.tools.httpTool.UpDownLoadTool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping(value = "/ApiManage")
public class RunApiCtrl {
    Logger logger = LogManager.getLogger(RunApiCtrl.class);

    @Autowired
    private BaseConfigs baseConfigs;

    @RequestMapping(value = "/RunApi.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public void runApi(@RequestBody ReqPojo reqInfo, HttpServletResponse response){
        UpDownLoadTool.downLoadFile("D:\\media\\picture\\wallpaper\\1541823492311.jpg", response);
    }
}
