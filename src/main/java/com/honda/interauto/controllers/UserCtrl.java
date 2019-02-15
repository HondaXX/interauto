package com.honda.interauto.controllers;

import com.honda.interauto.dto.UserDto;
import com.honda.interauto.pojo.BaseError;
import com.honda.interauto.pojo.ReqPojo;
import com.honda.interauto.pojo.ResPojo;
import com.honda.interauto.services.UserServices;
import com.honda.interauto.tools.dbTool.RedisUtil;
import com.honda.interauto.tools.httpTool.JwtAuthTool;
import com.honda.interauto.tools.sysTool.SysInitData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@RestController
@ResponseBody
@RequestMapping(value = "/UserManage")
@Service
public class UserCtrl {

    @Autowired
    private UserServices userServices;
    @Autowired
    private UserDto userDto;
    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping(value = "/login.json", produces = "application/json;charset=UTF-8")
    public ResPojo login(@RequestBody ReqPojo reqInfo){
        ResPojo res = new ResPojo();

        String userName = reqInfo.getRequestBody().get("name").toString();
        String password = reqInfo.getRequestBody().get("passwd").toString();
        userDto = userServices.identifyUserByNP(userName, password);
        if(userDto == null){
            res.setErrorCode(BaseError.USER_NOTFOUND);
            res.setErrorDesc(BaseError.USER_NOTFOUND_DESC);
            return res;
        }

        String token = JwtAuthTool.getToken(userDto);
        SysInitData.ru.set(token, userDto);

        res.setResCode(BaseError.RESPONSE_OK);
        res.putData("token", token);
        res.putData("user", SysInitData.ru.get(token));
        return res;
    }
}
