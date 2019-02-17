package com.honda.interauto.controllers;

import com.alibaba.fastjson.JSONObject;
import com.honda.interauto.dto.UserDto;
import com.honda.interauto.pojo.BaseError;
import com.honda.interauto.pojo.ReqPojo;
import com.honda.interauto.pojo.ResPojo;
import com.honda.interauto.services.UserServices;
import com.honda.interauto.tools.httpTool.JwtAuthTool;
import com.honda.interauto.tools.sysTool.OtherTool;
import com.honda.interauto.tools.sysTool.SysInitData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/UserManage")
@Service
public class UserCtrl {
    private final Logger logger = LogManager.getLogger(UserCtrl.class);

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
        logger.debug("{}-用户开始登录，请求参数-{}", userName, reqInfo.toString());

        userDto = userServices.identifyUserByNP(userName, password);
        if(userDto == null){
            res.setErrorCode(BaseError.USER_NOTFOUND);
            res.setErrorDesc(BaseError.USER_NOTFOUND_DESC);
            logger.info("{}-用户登录失败", userName);
            logger.debug(res.toString());
            return res;
        }

        //判断是否已经登录，是把之前token清除
        String token = new String();
        String idKey = userDto.getId().toString();
        String isLoginStr = "isLogin" + idKey;
        if (!SysInitData.ru.hasKey(isLoginStr)){
            token = JwtAuthTool.getToken(userDto) + System.currentTimeMillis();
            SysInitData.ru.set(token, userDto, 3600);
            SysInitData.ru.set(isLoginStr, idKey + "&" + token, 3600);
        }else {
            String oldToken = OtherTool.splitStr(SysInitData.ru.get(isLoginStr).toString(), "&")[1];
            logger.info(oldToken);
            SysInitData.ru.del(oldToken);
            logger.info(SysInitData.ru.hasKey(oldToken));
            SysInitData.ru.del(isLoginStr);
            token = JwtAuthTool.getToken(userDto) + System.currentTimeMillis();
            SysInitData.ru.set(token, userDto, 3600);
            SysInitData.ru.set(isLoginStr, idKey + "&" + token, 3600);
        }
        res.setResCode(BaseError.RESPONSE_OK);
        res.putData("token", token);
        res.putData("user", SysInitData.ru.get(token));
        logger.info("{}-用户登录成功", userName);
        logger.debug(JSONObject.toJSON(res));
        return res;
    }
}
