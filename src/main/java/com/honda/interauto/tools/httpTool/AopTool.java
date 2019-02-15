package com.honda.interauto.tools.httpTool;

import com.alibaba.fastjson.JSONArray;
import com.honda.interauto.dto.ServerDto;
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

    @Autowired
    private RedisTemplate redisTemplate;

    @Around("execution(* com.honda.interauto.controllers.*.*(..)) && !execution(* com.honda.interauto.controllers.*.*.*(..))")
    public Object checkInter(ProceedingJoinPoint joinpoint){
        //可以从redis里面取，但有点耗时，直接从启动后初始化数据取
//        RedisUtil ru = new RedisUtil();
//        ru.setRedisTemplate(redisTemplate);
//        String a = ru.get("apis").toString();
//        List<String> serverIdList = JSONArray.parseArray(a, String.class);

        List<String> serverIdList = SysInitData.serverList;

        HttpServletRequest request = RequestTool.getCurrentRequest();
        if (request.getHeader("token") == null){
            return null;
        }
        ReqPojo rp = new ReqPojo();
        Object returnObj;

        try{
            if (null != joinpoint.getArgs() && joinpoint.getArgs().length > 0){
                logger.debug(joinpoint.getArgs().length + " args of request: " + request.getServletPath());
                for (Object arg : joinpoint.getArgs()){
                    if (arg instanceof ReqPojo){
                        rp = (ReqPojo) arg;
                        break;
                    }else {
                        logger.debug("request fomat not right of request: " + request.getServletPath());
                        return null;
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
                    returnObj = joinpoint.proceed();
                    return returnObj;
                }
            }else {
                logger.debug("get request args error of request: " + request.getServletPath());
                return null;
            }
        }catch (Throwable e){
            logger.debug("joinpoint error when request: " + request.getServletPath());
            e.printStackTrace();
            return null;
        }
    }
}
