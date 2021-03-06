package com.honda.interauto.tools.sysTool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.honda.interauto.entity.ServerEntity;
import com.honda.interauto.services.ServerService;
import com.honda.interauto.tools.dbTool.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Order(value = 1)
public class SysInitRunner implements ApplicationRunner {
    private final Logger logger = LogManager.getLogger(SysInitRunner.class);

    @Autowired(required = false)
    private ServerService serverService;
    @Autowired
    private RedisTemplate redisTemplate;

    public void run(ApplicationArguments args) throws Exception{
        logger.info("Start init system api...");

        Map<String, String> serverIdMap = new HashMap<String, String>();
        List<String> serverIdList = new ArrayList<String>();

        RedisUtil ruT = new RedisUtil();
        ruT.setRedisTemplate(redisTemplate);

        if (ruT.hasKey("apis")){
            ruT.del("apis");
        }

        SysInitData.ru = ruT;

        List<ServerEntity> serverDtoList = serverService.getAllServers();
        for(int i = 0; i < serverDtoList.size(); i++){
            String serverId = serverDtoList.get(i).getServerId();
            String reqParams = serverDtoList.get(i).getReqParam();
            if (StringUtils.isBlank(reqParams)){
                reqParams = "nullStr";
            }
            serverIdList.add(serverId);
            serverIdMap.put(serverId, reqParams);
        }
        SysInitData.serverMap = serverIdMap;
        SysInitData.serverList = serverIdList;

        String mapJsonStr = JSON.toJSONString(serverIdMap);
        logger.info(serverIdList);
        ruT.set("apis", mapJsonStr);
        if (ruT.hasKey("apis")){
            logger.info("complate init api!!!");
            logger.info("初始化redis成功");
        }else {
            logger.info("init api failed, please check the server!");
        }
    }
}
