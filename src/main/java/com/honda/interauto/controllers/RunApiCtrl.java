package com.honda.interauto.controllers;

import com.alibaba.fastjson.JSON;
import com.honda.interauto.dao.auto.GetParamValue;
import com.honda.interauto.dto.ProModelDto;
import com.honda.interauto.entity.InterCaseEntity;
import com.honda.interauto.pojo.*;
import com.honda.interauto.services.InterCaseService;
import com.honda.interauto.services.ProModelService;
import com.honda.interauto.tools.httpTool.HttpReqTool;
import com.honda.interauto.tools.sysTool.OtherTool;
import com.honda.interauto.tools.sysTool.ParamTool;
import com.honda.interauto.tools.sysTool.SysInitData;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/ApiManage")
@Service
public class RunApiCtrl {
    Logger logger = LogManager.getLogger(RunApiCtrl.class);

    @Autowired
    private GetParamValue gpv;
    @Autowired
    private SqlVo sqlVo;

    @Autowired
    private BaseConfigs baseConfigs;
    @Autowired
    private InterCaseService interCaseService;
    @Autowired
    private ProModelService proModelService;

//    @Autowired
//    @Qualifier("autoJdbcTemplate")
//    protected JdbcTemplate autoJdbcTemplate;
//    @Autowired
//    @Qualifier("testJdbcTemplate")
//    protected JdbcTemplate testJdbcTemplate;

    @RequestMapping(value = "/RunApi.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResPojo runApi(@RequestBody ReqPojo reqInfo){
        ResPojo res = new ResPojo();
        Map<Integer, String> caseResMap = new HashMap<Integer, String>();

        Integer proId = Integer.parseInt(reqInfo.getRequestBody().get("proId").toString());
        Map<String, List<Integer>> modelCaseMap = (Map<String, List<Integer>>) reqInfo.getRequestBody().get("modelCaseList");

        if (StringUtils.isBlank(proId.toString()) || !(modelCaseMap.size() > 0)){
            res.setErrorCode(BaseError.PARAM_ERROR);
            res.setErrorDesc(BaseError.PARAM_ERROR_DESC);
            return res;
        }

        for(String modelIdStr : modelCaseMap.keySet()){
            Integer modelId = Integer.parseInt(modelIdStr);
            List<Integer> caseList = modelCaseMap.get(modelIdStr);
            List<ProModelDto> proModelList = proModelService.getProModels(proId, modelId, caseList);
            for (ProModelDto proModelDto : proModelList){
                Integer caseId = proModelDto.getCaseID();
                logger.info("========>【{}】模块, 用例【{}】开始, id: {}", proModelDto.getModelName(), proModelDto.getCaseAim(), caseId);
                InterCaseEntity interCaseEntity = interCaseService.getInterCaseByCaseId(caseId);
                if (interCaseEntity.getNeedInit().equals("1")){
                    //用例初始化执行
                    logger.info("========>开始初始化用例数据");
                    Map<String, Object> dbSCodeMap = (Map) JSON.parse(interCaseEntity.getInitCode());
                    if (!ParamTool.operateDB(dbSCodeMap, gpv, sqlVo)){
                        logger.info("========>初始化用例数据失败");
                        caseResMap.put(caseId, BaseError.CASE_DB_OPER_DESC);
                        continue;
                    }
                    logger.info("========>初始化用例数据成功");
                }

                if (interCaseEntity.getNeedDesignReq().equals("1")){
                    //请求前参数化请求字段值
                    logger.debug("========>开始请求参数化");
                    Map<String, Object> originReqMap = (Map) JSON.parse(interCaseEntity.getRequestJson());
                    Map<String, Object> paramSqlMap = (Map) JSON.parse(interCaseEntity.getReqParam());
                    Object paramWithValueMap = ParamTool.getValueForParam(paramSqlMap, gpv, sqlVo);
                    if (paramWithValueMap instanceof String){
                        logger.info("========>请求获取字段值失败");
                        caseResMap.put(caseId, BaseError.CASE_PARAM_GETV_DESC + ": {" + paramWithValueMap + "}");
                        if (rollBackDB(interCaseEntity).equals(false)){
                            logger.info("========>用例还原失败：{}", caseId);
                        }
                        continue;
                    }
                    //获取值替换原有请求值
                    Object trueReqMap = ParamTool.replaceSettingParam((Map<String, Object>) paramWithValueMap, originReqMap);
                    if (trueReqMap instanceof String){
                        logger.info("========>请求替换参数化值失败");
                        caseResMap.put(caseId, BaseError.CASE_PARAM_REP_DESC + ": {" + trueReqMap + "}");
                        if (rollBackDB(interCaseEntity).equals(false)){
                            logger.info("========>用例还原失败：{}", caseId);
                        }
                        continue;
                    }
                    logger.info("========>请求参数化成功");
                    interCaseEntity.setRequestJson(trueReqMap.toString());
                }

                //初始化与参数化完成后开始请求接口,先判断是否需要cookie
                String resInfo = null;
                if (interCaseEntity.getNeedCookie().equals(null)){
                    resInfo = HttpReqTool.httpReqJson(interCaseEntity, null);
                }else {
                    Map<String, String> cookieMap = new HashMap<>();
                    String params = interCaseEntity.getUsedParam();
                    if (!StringUtils.isBlank(params)){
                        String[] paramList = OtherTool.splitStr(params, ",");
                        for (int i = 0; i < paramList.length; i++){
                            String param = paramList[i];
                            Object oValue = SysInitData.ru.get(param);
                            cookieMap.put(OtherTool.splitStr(param, "@")[0], oValue.toString());
                        }
                    }
//                    cookieMap.put("token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxIn0.qfd0G-elhE1aGr15LrnYlIZ_3UToaOM5HeMcXrmDGBM1551691999335");
                    resInfo = HttpReqTool.httpReqJson(interCaseEntity, cookieMap);
                }
                if (null == resInfo){
                    logger.info("========>接口请求响应出错");
                    caseResMap.put(caseId, BaseError.CASE_RES_ERROR_DESC);
                    if (rollBackDB(interCaseEntity).equals(false)){
                        logger.info("========>用例还原失败：{}", caseId);
                    }
                    continue;
                }

                //返回结果与替换后预期转为map，后面做对比用到
                Map<String, Object> resMap = (Map<String, Object>) JSON.parse(resInfo);
                Map<String, Object> trueExpectMap = new HashMap<String, Object>();

                //响应后参数化结果字段值
                if (interCaseEntity.getNeedDesignRes().equals("1")){
                    logger.debug("========>开始结果参数化");
                    Map<String, Object> originResMap = (Map) JSON.parse(interCaseEntity.getExpectRes());
                    Map<String, Object> paramSqlMap = (Map) JSON.parse(interCaseEntity.getResParam());
                    Object paramWithValueMap = ParamTool.getValueForParam(paramSqlMap, gpv, sqlVo);
                    if (paramWithValueMap instanceof String){
                        logger.info("========>结果获取字段值失败");
                        caseResMap.put(caseId, BaseError.CASE_PARAM_GETV_DESC + ": {" + paramWithValueMap + "}");
                        if (rollBackDB(interCaseEntity).equals(false)){
                            logger.info("========>用例还原失败：{}", caseId);
                        }
                        continue;
                    }
                    //获取值替换原有预期值
                    Object trueExpectO = ParamTool.replaceSettingParam((Map<String, Object>) paramWithValueMap, originResMap);
                    if (trueExpectO instanceof String){
                        logger.info("========>结果替换参数化值失败");
                        caseResMap.put(caseId, BaseError.CASE_PARAM_REP_DESC + ": {" + trueExpectO + "}");
                        if (rollBackDB(interCaseEntity).equals(false)){
                            logger.info("========>用例还原失败：{}", caseId);
                        }
                        continue;
                    }
                    trueExpectMap = (Map<String, Object>) JSON.parse(trueExpectO.toString());
                    logger.info("========>结果参数化成功");
                }else {
                    trueExpectMap = (Map<String, Object>) JSON.parse(interCaseEntity.getExpectRes());
                }

                //开始做对比
                Map<String, String> compareRes = ParamTool.compareRes(resMap, trueExpectMap);
                if (compareRes.get("comRes").equals("0")){
//                logger.info("========>这里从map里取不通过的值存库");
                    String failStr = compareRes.get("unequalParam");
                    caseResMap.put(caseId, BaseError.CASE_CONP_UNEQ_DESC + ": {" + failStr + "}");
                    if (rollBackDB(interCaseEntity).equals(false)){
                        logger.info("========>用例还原失败：{}", caseId);
                    }
                    continue;
                }

                //对比成功开始保存返回值里需要的字段
                String params = interCaseEntity.getSaveParam();
                if (!StringUtils.isBlank(params)){
                    String[] paramList = OtherTool.splitStr(params, ",");
                    for (int i = 0; i < paramList.length; i++){
                        String param = paramList[i];
                        String paramVal = resMap.get(param).toString();
                        SysInitData.ru.set(param + "@" + caseId.toString(), paramVal, 3600);
                    }
                }

                //对比成功开始还原数据库
                if (rollBackDB(interCaseEntity).equals(false)){
                    logger.info("========>用例还原失败：{}", caseId);
                    caseResMap.put(caseId, BaseError.CASE_DB_OPER_DESC);
                    continue;
                }

                caseResMap.put(caseId, BaseError.CASE_OK);

            }
        }


        res.setResCode(BaseError.RESPONSE_OK);
        res.putData("resDetail", caseResMap);
        return res;
    }

    //还原数据库独立方法
    public Boolean rollBackDB(InterCaseEntity interCaseDto){
        Boolean rollRes = true;
        if (interCaseDto.getNeedRoll().equals("1")){
            //用例初始化执行
            logger.info("========>开始还原用例数据");
            Map<String, Object> dbSCodeMap = (Map) JSON.parse(interCaseDto.getRollCode());
            if (!ParamTool.operateDB(dbSCodeMap, gpv, sqlVo)){
                rollRes = false;
                return rollRes;
            }
            logger.info("========>还原用例数据成功");
            return rollRes;
        }else {
            logger.info("========>该用例不需要还原");
            return rollRes;
        }
    }

}
