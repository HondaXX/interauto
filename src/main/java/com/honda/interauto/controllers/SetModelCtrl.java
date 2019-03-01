package com.honda.interauto.controllers;

import com.alibaba.fastjson.JSONArray;
import com.honda.interauto.entity.ModelEntity;
import com.honda.interauto.entity.ProEntity;
import com.honda.interauto.pojo.BaseError;
import com.honda.interauto.pojo.ReqPojo;
import com.honda.interauto.pojo.ResPojo;
import com.honda.interauto.services.ModelService;
import com.honda.interauto.services.ProService;
import com.honda.interauto.tools.dbTool.RedisUtil;
import com.honda.interauto.tools.sysTool.SysInitData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/ModelManage")
@Service
public class SetModelCtrl {
    @Autowired
    private ProService proService;
    @Autowired
    private ModelService modelService;
    @Autowired
    private RedisTemplate redisTemplate;

    private final Logger logger = LogManager.getLogger(SetModelCtrl.class);

    @PostMapping(value = "/GetAllPro.json", produces = "application/json;charset=UTF-8")
    public ResPojo getAllApi(@RequestBody ReqPojo reqInfo){
        ResPojo res = new ResPojo();
        List<ProEntity> proList =  proService.getAllPros();
        if (proList.size() <= 0){
            res.setErrorCode(BaseError.DB_ERROR);
            res.setErrorDesc(BaseError.DB_ERROR_DESC);
            return res;
        }
        res.setResCode(BaseError.RESPONSE_OK);
        res.putData("proList", proList);
        return res;
    }

    @PostMapping(value = "/GetProModels.json", produces = "application/json;charset=UTF-8")
    public ResPojo getProModels(@RequestBody ReqPojo reqInfo){
        ResPojo res = new ResPojo();

        Integer proId = Integer.parseInt(reqInfo.getRequestBody().get("proId").toString());
        List<ModelEntity> modelList = modelService.getProModels(proId);
        if (modelList.size() <= 0){
            res.setErrorCode(BaseError.DB_ERROR);
            res.setErrorDesc(BaseError.DB_ERROR_DESC);
            return res;
        }
        res.setResCode(BaseError.RESPONSE_OK);
        res.putData("modelList", modelList);
        return res;
    }

    @PostMapping(value = "/testRedis.json", produces = "application/json;charset=UTF-8")
    public ResPojo testtest(@RequestBody ReqPojo reqInfo){
        String getVal = reqInfo.getRequestBody().get("val").toString();
        ResPojo res = new ResPojo();
        RedisUtil ru = new RedisUtil();
        ru.setRedisTemplate(redisTemplate);
        String a = ru.get(getVal).toString();
        List<String> listO = JSONArray.parseArray(a, String.class);

        List<String> listT = SysInitData.serverList;

        res.putData("val", listO);
        res.putData("val2", listT);
        return res;
    }
}
