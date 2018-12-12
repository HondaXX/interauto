package com.honda.interauto.controllers;

import com.honda.interauto.dto.UserDto;
import com.honda.interauto.pojo.BaseConfigs;
import com.honda.interauto.pojo.ReqPojo;
import com.honda.interauto.services.UserServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping(value = "/ApiManage")
@Service
public class RunApiCtrl {
    Logger logger = LogManager.getLogger(RunApiCtrl.class);

    @Autowired
    private BaseConfigs baseConfigs;
    @Autowired
    private UserServices userServices;

//    @Autowired
//    @Qualifier("autoJdbcTemplate")
//    protected JdbcTemplate autoJdbcTemplate;
//    @Autowired
//    @Qualifier("testJdbcTemplate")
//    protected JdbcTemplate testJdbcTemplate;

    @RequestMapping(value = "/RunApi.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public void runApi(@RequestBody ReqPojo reqInfo, HttpServletResponse response){
        List<UserDto> userList = userServices.getAllUsers();
        logger.info(userList.toString());
    }
}
