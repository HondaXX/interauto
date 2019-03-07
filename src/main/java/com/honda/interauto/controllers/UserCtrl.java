package com.honda.interauto.controllers;

import com.alibaba.fastjson.JSONObject;
import com.honda.interauto.entity.UserEntity;
import com.honda.interauto.pojo.BaseError;
import com.honda.interauto.pojo.ReqPojo;
import com.honda.interauto.pojo.ResPojo;
import com.honda.interauto.services.UserServices;
import com.honda.interauto.tools.httpTool.JwtAuthTool;
import com.honda.interauto.tools.httpTool.RequestTool;
import com.honda.interauto.tools.sysTool.OtherTool;
import com.honda.interauto.tools.sysTool.SysInitData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping(value = "/UserManage")
public class UserCtrl {
    private final Logger logger = LogManager.getLogger(UserCtrl.class);

    @Autowired
    private UserServices userServices;
    @Autowired
    private UserEntity userDto;
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping(value = "/index")
    public String backIndex(Model model){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = format.format(new Date());
        String hmsStr = OtherTool.splitStr(dateStr, " ")[1];
        Integer hourInt = Integer.parseInt(OtherTool.splitStr(hmsStr, ":")[0]);
        String timeStr = null;
        if (hourInt > 0 && hourInt <= 7){
            timeStr = "凌晨";
        }else if (hourInt > 7 && hourInt <= 12){
            timeStr = "上午";
        }else if (hourInt > 12 && hourInt <= 18){
            timeStr = "下午";
        }else {
            timeStr = "晚上";
        }
        model.addAttribute("timeStr", timeStr);
        model.addAttribute("time", dateStr);
        return "login";
    }

    @PostMapping(value = "/login.json", produces = "application/json;charset=UTF-8")
    @ResponseBody
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
            SysInitData.ru.del(oldToken);
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

    @PostMapping(value = "/loginOut.json", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResPojo loginOut(@RequestBody ReqPojo reqInfo){
        HttpServletRequest request = RequestTool.getCurrentRequest();
        String tokenStr = request.getHeader("token");
        if (tokenStr == null){
            logger.info("========>请求token为空,退出登录失败");
            ResPojo res = new ResPojo();
            res.setErrorCode(BaseError.USER_TOKENLESS);
            res.setErrorDesc(BaseError.USER_TOKENLESS_DESC);
            return res;
        }

        if (!SysInitData.ru.hasKey(tokenStr)){
            logger.info("========>退出登录失败,错误的token信息:{}" + tokenStr);
            ResPojo res = new ResPojo();
            res.setErrorCode(BaseError.USER_TOKENOVERDUE);
            res.setErrorDesc(BaseError.USER_TOKENOVERDUE_DESC);
            return res;
        }

        Integer useId = Integer.parseInt(reqInfo.getRequestBody().get("id").toString());
        UserEntity user = (UserEntity) SysInitData.ru.get(tokenStr);
        if (!(user.getId() == useId)){
            logger.info("========>操作用户与token信息不匹配:{}" + tokenStr);
            ResPojo res = new ResPojo();
            res.setErrorCode(BaseError.USER_TOKENNOTMATCH);
            res.setErrorDesc(BaseError.USER_TOKENNOTMATCH_DESC);
            return res;
        }

        String idKey = String.valueOf(useId);
        String isLoginStr = "isLogin" + idKey;
        SysInitData.ru.del(tokenStr);
        SysInitData.ru.del(isLoginStr);
        ResPojo res = new ResPojo();
        res.setErrorCode(BaseError.RESPONSE_OK);
        res.putData("msg", "退出登录成功");
        logger.info("{}-用户退出登录成功", user.getName());
        return res;
    }
}
