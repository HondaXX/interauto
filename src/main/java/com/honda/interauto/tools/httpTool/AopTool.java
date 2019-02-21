package com.honda.interauto.tools.httpTool;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.honda.interauto.dto.ServerDto;
import com.honda.interauto.dto.UserDto;
import com.honda.interauto.pojo.BaseError;
import com.honda.interauto.pojo.InnerResPojo;
import com.honda.interauto.pojo.ReqPojo;
import com.honda.interauto.pojo.ResPojo;
import com.honda.interauto.services.ServerService;
import com.honda.interauto.tools.dbTool.RedisUtil;
import com.honda.interauto.tools.sysTool.SysInitData;
import com.honda.interauto.tools.sysTool.SysInitRunner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@Aspect
public class AopTool {
    private Logger logger = LogManager.getLogger(AopTool.class);

//    @Autowired
//    private RedisTemplate redisTemplate;

    @Around("execution(* com.honda.interauto.controllers.*.*(..)) && !execution(* com.honda.interauto.controllers.UserCtrl.*(..))")
    public Object checkInter(ProceedingJoinPoint joinpoint){
        //可以从redis里面取，但有点耗时，直接从启动后初始化数据取
//        RedisUtil ru = new RedisUtil();
//        ru.setRedisTemplate(redisTemplate);
        //redis不支持list<String>，直接用fastjson存一个字符串
//        String a = ru.get("apis").toString();
//        List<String> serverIdList = JSONArray.parseArray(a, String.class);

        List<String> serverIdList = SysInitData.serverList;

        ReqPojo rp = new ReqPojo();
        Object returnObj;

        HttpServletRequest request = RequestTool.getCurrentRequest();
        String tokenStr = request.getHeader("token");
        if (tokenStr == null){
            logger.info("========>请求token为空");
            ResPojo res = new ResPojo();
            res.setErrorCode(BaseError.USER_TOKENLESS);
            res.setErrorDesc(BaseError.USER_TOKENLESS_DESC);
            returnObj = res;
            return returnObj;
        }

        if (!SysInitData.ru.hasKey(tokenStr)) {
            logger.info("========>token已过期或失效或在其他设备登录");
            ResPojo res = new ResPojo();
            res.setErrorCode(BaseError.USER_TOKENOVERDUE);
            res.setErrorDesc(BaseError.USER_TOKENOVERDUE_DESC);
            returnObj = res;
            return returnObj;
        }

        UserDto userDto = (UserDto) SysInitData.ru.get(tokenStr);
        logger.info("========>{}-req url: {}", userDto.getName(), request.getServletPath());
        if (null == joinpoint.getArgs() || joinpoint.getArgs().length < 0){
            logger.debug("请求参数格式错误: " + request.getServletPath());
            ResPojo res = new ResPojo();
            res.setErrorCode(BaseError.FOMAT_ERROR);
            res.setErrorDesc(BaseError.FOMAT_ERROR_DESC);
            returnObj = res;
            return returnObj;
        }

        for (Object arg : joinpoint.getArgs()) {
            if (arg instanceof ReqPojo) {
                rp = (ReqPojo) arg;
                break;
            }
        }
        //判断请求不能为空
        InnerResPojo irp = RequestTool.jugeReqPojo(rp, serverIdList);
        if (null != irp){
            ResPojo rsp = new ResPojo();
            rsp.setErrorCode(irp.getInnerCode());
            rsp.setErrorDesc(irp.getInnerDesc());
            returnObj = rsp;
            return returnObj;
        }else {
            try{
                returnObj = joinpoint.proceed();
                logger.debug(JSONObject.toJSON(returnObj));
                return returnObj;
            }catch (Throwable e){
                e.printStackTrace();
                ResPojo rsp = new ResPojo();
                rsp.setErrorCode(BaseError.SYS_ERROR);
                rsp.setErrorDesc(BaseError.SYS_ERROR_DESC);
                returnObj = rsp;
                return returnObj;
            }
        }
    }
}
