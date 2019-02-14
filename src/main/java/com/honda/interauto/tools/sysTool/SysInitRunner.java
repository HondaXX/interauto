package com.honda.interauto.tools.sysTool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.honda.interauto.dto.ServerDto;
import com.honda.interauto.services.ServerService;
import com.honda.interauto.tools.dbTool.RedisUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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

        List<String> serverIdList = new ArrayList<String>();

        RedisUtil ru = new RedisUtil();
        ru.setRedisTemplate(redisTemplate);

        if (ru.hasKey("apis")){
            ru.del("apis");
        }

        List<ServerDto> serverDtoList = serverService.getAllServers();
        for(int i = 0; i < serverDtoList.size(); i++){
            String serverId = serverDtoList.get(i).getServerId();
            serverIdList.add(serverId);
        }

        String listJsonStr = JSONArray.parseArray(JSON.toJSONString(serverIdList)).toJSONString();
        ru.set("apis", listJsonStr);
        if (ru.hasKey("apis")){
            logger.info("complate init api!!!");
        }else {
            logger.info("init api failed, please check the server!");
        }
    }
}
