package com.honda.interauto.controllers;

import com.alibaba.fastjson.JSON;
import com.honda.interauto.dao.auto.GetParamValue;
import com.honda.interauto.dto.ProModelDto;
import com.honda.interauto.entity.CaseResDetailEntity;
import com.honda.interauto.entity.CaseResOverViewEntity;
import com.honda.interauto.entity.InterCaseEntity;
import com.honda.interauto.entity.UserEntity;
import com.honda.interauto.pojo.*;
import com.honda.interauto.services.CaseResDetailService;
import com.honda.interauto.services.CaseResOverViewService;
import com.honda.interauto.services.InterCaseService;
import com.honda.interauto.services.ProModelService;
import com.honda.interauto.tools.httpTool.HttpReqTool;
import com.honda.interauto.tools.httpTool.RequestTool;
import com.honda.interauto.tools.sysTool.OtherTool;
import com.honda.interauto.tools.sysTool.ParamTool;
import com.honda.interauto.tools.sysTool.SysInitData;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(value = "/ApiManage")
@Service
public class RunApiCtrl {
    private final Logger logger = LogManager.getLogger(RunApiCtrl.class);

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
    @Autowired
    private CaseResDetailService caseResDetailService;
    @Autowired
    private CaseResOverViewService caseResOverViewService;

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
        int totalCount = 0;
        int failCount = 0;
        int successCount = 0;

        Integer proId = Integer.parseInt(reqInfo.getRequestBody().get("proId").toString());
        Map<String, List<Integer>> modelCaseMap = (Map<String, List<Integer>>) reqInfo.getRequestBody().get("modelCaseList");

        if (StringUtils.isBlank(proId.toString()) || !(modelCaseMap.size() > 0)){
            res.setErrorCode(BaseError.PARAM_ERROR);
            res.setErrorDesc(BaseError.PARAM_ERROR_DESC);
            return res;
        }

        //跑用例的动作id
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTime = simpleDateFormat.format(date);
        String runTagId = runApiTagId(date);

        for(String modelIdStr : modelCaseMap.keySet()){
            Integer modelId = Integer.parseInt(modelIdStr);
            List<Integer> caseList = modelCaseMap.get(modelIdStr);
            List<ProModelDto> proModelList = proModelService.getProModels(proId, modelId, caseList);
            //获取的有效用例加到总数
            totalCount += proModelList.size();
            for (ProModelDto proModelDto : proModelList){
                Integer caseId = proModelDto.getCaseID();
                logger.info("========>【{}】模块, 用例【{}】开始, id: {}", proModelDto.getModelName(), proModelDto.getCaseAim(), caseId);
                InterCaseEntity interCaseEntity = interCaseService.getInterCaseByCaseId(caseId);
                if (interCaseEntity.getNeedInit().equals("1")){
                    //用例初始化执行
                    logger.info("========>开始初始化用例数据");
                    Map<String, Object> dbSCodeMap = (Map) JSON.parse(interCaseEntity.getInitCode());
                    String operateDBStr = ParamTool.operateDB(dbSCodeMap, gpv, sqlVo);
                    if (!operateDBStr.equals("success")){
                        logger.info("========>初始化用例数据失败");
                        logger.info("========>用例结果详情存库，caseId-->{}", caseId);
                        CaseResDetailEntity caseResDetailEntity = new CaseResDetailEntity(null, runTagId, caseId, "1", BaseError.CASE_DB_OPER, BaseError.CASE_DB_OPER_DESC, "初始化错误sql:" + operateDBStr);
                        caseResDetailService.saveCaseRes(caseResDetailEntity);
                        caseResMap.put(caseId, BaseError.CASE_DB_OPER_DESC);
                        failCount += 1;
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
                        String[] paramAndSql = OtherTool.splitStr(paramWithValueMap.toString(), "@");
                        String paramStr = paramAndSql[0];
                        String sqlStr = paramAndSql[1];
                        logger.info("========>用例结果详情存库，caseId-->{}", caseId);
                        CaseResDetailEntity caseResDetailEntity = new CaseResDetailEntity(null, runTagId, caseId, "1", BaseError.CASE_PARAM_GETV, BaseError.CASE_PARAM_GETV_DESC, "请求参数化取值错误key:" + paramStr + ",sql:" + sqlStr);
                        caseResDetailService.saveCaseRes(caseResDetailEntity);
                        caseResMap.put(caseId, BaseError.CASE_PARAM_GETV_DESC + ": {" + paramWithValueMap + "}");
                        if (rollBackDB(interCaseEntity).equals(false)){
                            logger.info("========>用例还原失败：{}", caseId);
                        }
                        failCount += 1;
                        continue;
                    }
                    //获取值替换原有请求值
                    Object trueReqMap = ParamTool.replaceSettingParam((Map<String, Object>) paramWithValueMap, originReqMap);
                    if (trueReqMap instanceof String){
                        logger.info("========>请求替换参数化值失败");
                        logger.info("========>用例结果详情存库，caseId-->{}", caseId);
                        CaseResDetailEntity caseResDetailEntity = new CaseResDetailEntity(null, runTagId, caseId, "1", BaseError.CASE_PARAM_REP, BaseError.CASE_PARAM_REP_DESC, "请求时参数化替换错误key:" + trueReqMap);
                        caseResDetailService.saveCaseRes(caseResDetailEntity);
                        caseResMap.put(caseId, BaseError.CASE_PARAM_REP_DESC + ": {" + trueReqMap + "}");
                        if (rollBackDB(interCaseEntity).equals(false)){
                            logger.info("========>用例还原失败：{}", caseId);
                        }
                        failCount += 1;
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
//                    String params = interCaseEntity.getNeedCookie();
//                    if (!StringUtils.isBlank(params)){
//                        String[] paramList = OtherTool.splitStr(params, ",");
//                        for (int i = 0; i < paramList.length; i++){
//                            String param = paramList[i];
//                            Object oValue = SysInitData.ru.get(param);
//                            cookieMap.put(OtherTool.splitStr(param, "@")[0], oValue.toString());
//                        }
//                    }
                    cookieMap.put("token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxIn0.qfd0G-elhE1aGr15LrnYlIZ_3UToaOM5HeMcXrmDGBM1551864653842");
                    resInfo = HttpReqTool.httpReqJson(interCaseEntity, cookieMap);
                }
                if (null == resInfo){
                    logger.info("========>接口请求响应出错");
                    logger.info("========>用例结果详情存库，caseId-->{}", caseId);
                    CaseResDetailEntity caseResDetailEntity = new CaseResDetailEntity(null, runTagId, caseId, "1", BaseError.CASE_RES_ERROR, BaseError.CASE_RES_ERROR_DESC, "接口调用失败，响应为空");
                    caseResDetailService.saveCaseRes(caseResDetailEntity);
                    caseResMap.put(caseId, BaseError.CASE_RES_ERROR_DESC);
                    if (rollBackDB(interCaseEntity).equals(false)){
                        logger.info("========>用例还原失败：{}", caseId);
                    }
                    failCount += 1;
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
                        String[] paramAndSql = OtherTool.splitStr(paramWithValueMap.toString(), "@");
                        String paramStr = paramAndSql[0];
                        String sqlStr = paramAndSql[1];
                        logger.info("========>用例结果详情存库，caseId-->{}", caseId);
                        CaseResDetailEntity caseResDetailEntity = new CaseResDetailEntity(null, runTagId, caseId, "1", BaseError.CASE_PARAM_GETV, BaseError.CASE_PARAM_GETV_DESC, "返回参数化取值错误key:" + paramStr + ",sql:" + sqlStr);
                        caseResDetailService.saveCaseRes(caseResDetailEntity);
                        caseResMap.put(caseId, BaseError.CASE_PARAM_GETV_DESC + ": {" + paramWithValueMap + "}");
                        if (rollBackDB(interCaseEntity).equals(false)){
                            logger.info("========>用例还原失败：{}", caseId);
                        }
                        failCount += 1;
                        continue;
                    }
                    //获取值替换原有预期值
                    Object trueExpectO = ParamTool.replaceSettingParam((Map<String, Object>) paramWithValueMap, originResMap);
                    if (trueExpectO instanceof String){
                        logger.info("========>结果替换参数化值失败");
                        logger.info("========>用例结果详情存库，caseId-->{}", caseId);
                        CaseResDetailEntity caseResDetailEntity = new CaseResDetailEntity(null, runTagId, caseId, "1", BaseError.CASE_PARAM_REP, BaseError.CASE_PARAM_REP_DESC, "响应参数化替换错误key:" + trueExpectO);
                        caseResDetailService.saveCaseRes(caseResDetailEntity);
                        caseResMap.put(caseId, BaseError.CASE_PARAM_REP_DESC + ": {" + trueExpectO + "}");
                        if (rollBackDB(interCaseEntity).equals(false)){
                            logger.info("========>用例还原失败：{}", caseId);
                        }
                        failCount += 1;
                        continue;
                    }
                    trueExpectMap = (Map<String, Object>) JSON.parse(trueExpectO.toString());
                    logger.info("========>结果参数化成功");
                }else {
                    trueExpectMap = (Map<String, Object>) JSON.parse(interCaseEntity.getExpectRes());
                }

                //开始做对比
                if (interCaseEntity.getCompareType().equals("0")){
                    logger.info("========>开始对预期与接口响应结果做精确比较...");
                    Map<String, String> compareRes = ParamTool.compareRes(resMap, trueExpectMap);
                    if (compareRes.get("comRes").equals("0")){
                        //如果是值不匹配
                        if (compareRes.containsKey("unequalParam")){
                            String failStr = compareRes.get("unequalParam");
                            logger.info("========>用例结果详情存库，caseId-->{}", caseId);
                            CaseResDetailEntity caseResDetailEntity = new CaseResDetailEntity(null, runTagId, caseId, "1", BaseError.CASE_COMP_UNEQ, BaseError.CASE_COMP_UNEQ_DESC, "响应与预期比对不相等:" + failStr);
                            caseResDetailService.saveCaseRes(caseResDetailEntity);
                            caseResMap.put(caseId, BaseError.CASE_COMP_UNEQ_DESC + failStr);
                        }
                        //如果是键不存在
                        if (caseResMap.containsKey("lessParam")){
                            String failStr = compareRes.get("lessParam");
                            logger.info("========>用例结果详情存库，caseId-->{}", caseId);
                            CaseResDetailEntity caseResDetailEntity = new CaseResDetailEntity(null, runTagId, caseId, "1", BaseError.CASE_COMP_LESSKEY, BaseError.CASE_CONP_LESSKEY_DESC, "响应存在预期不存在的值:" + failStr);
                            caseResDetailService.saveCaseRes(caseResDetailEntity);
                            caseResMap.put(caseId, BaseError.CASE_CONP_LESSKEY_DESC + failStr);
                        }
                        if (rollBackDB(interCaseEntity).equals(false)){
                            logger.info("========>用例还原失败：{}", caseId);
                        }
                        failCount += 1;
                        continue;
                    }else {
                        logger.info("========>精确结果对比通过，caseId: {}", caseId);
                    }
                }else {
                    logger.info("========>开始对预期与接口响应结果做模糊匹配...");
                    Map<String, Object> originResMap = (Map) JSON.parse(interCaseEntity.getExpectRes());
                    Map<String, String> compareRes = ParamTool.compareResKey(resMap, originResMap);
                    if (compareRes.get("comRes").equals("0")){
                        String lessStr = compareRes.get("lessParam");
                        logger.info("========>模糊匹比较例结果详情存库，caseId-->{}", caseId);
                        CaseResDetailEntity caseResDetailEntity = new CaseResDetailEntity(null, runTagId, caseId, "1", BaseError.CASE_COMP_LESSKEY, BaseError.CASE_CONP_LESSKEY_DESC, "响应存在预期不存在的值:" + lessStr);
                        caseResDetailService.saveCaseRes(caseResDetailEntity);
                        caseResMap.put(caseId, BaseError.CASE_CONP_LESSKEY_DESC + lessStr);
                        if (rollBackDB(interCaseEntity).equals(false)){
                            logger.info("========>用例还原失败：{}", caseId);
                        }
                        failCount += 1;
                        continue;
                    }else {
                        logger.info("========>模糊结果匹配通过，caseId: {}", caseId);
                    }
                }


                //对比成功开始保存返回值里需要的字段
                String params = interCaseEntity.getSaveParam();
                if (!StringUtils.isBlank(params)){
                    String[] paramList = OtherTool.splitStr(params, ",");
                    for (int i = 0; i < paramList.length; i++){
                        String param = paramList[i];
                        String paramVal = resMap.get(param).toString();
                        String realParam = param + "@" + caseId.toString();
                        SysInitData.ru.set(realParam, paramVal, 3600);
                        logger.info("========>保存到redis键: {}, 值: {}", realParam, paramVal);
                    }
                }

                //对比成功开始还原数据库
                if (rollBackDB(interCaseEntity).equals(false)){
                    logger.info("========>用例执行完还原失败：{}", caseId);
                    logger.info("========>用例结果详情存库，caseId-->{}", caseId);
                    CaseResDetailEntity caseResDetailEntity = new CaseResDetailEntity(null, runTagId, caseId, "0", BaseError.CASE_DB_OPER, BaseError.CASE_DB_OPER_DESC, "用例成功执行，但还原数据库错误，请查日志:【用例执行完还原失败】");
                    caseResDetailService.saveCaseRes(caseResDetailEntity);
                    caseResMap.put(caseId, BaseError.CASE_DB_OPER_DESC);
                    successCount += 1;
                    continue;
                }
                logger.info("========>用例结果详情存库，caseId-->{}", caseId);
                CaseResDetailEntity caseResDetailEntity = new CaseResDetailEntity(null, runTagId, caseId, "1", BaseError.CASE_OK, "success", "用例通过");
                caseResDetailService.saveCaseRes(caseResDetailEntity);
                successCount += 1;
                caseResMap.put(caseId, BaseError.CASE_OK);
            }
        }

        //用例执行完插一个总记录
        String userName = OtherTool.splitStr(runTagId, "-")[0];
        String endTime = simpleDateFormat.format(new Date());
        logger.info("========>统计批次【{}】用例概览,共{}条用例,成功-{},失败-{}", runTagId, totalCount, successCount, failCount);
        CaseResOverViewEntity caseResOverViewEntity = new CaseResOverViewEntity(runTagId, totalCount, failCount, successCount, userName, startTime, endTime);
        caseResOverViewService.recordOverView(caseResOverViewEntity);
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
            String operateDBStr = ParamTool.operateDB(dbSCodeMap, gpv, sqlVo);
            if (!operateDBStr.equals("success")){
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

    //跑用例时独立id方法
    public String runApiTagId(Date date){
        HttpServletRequest request = RequestTool.getCurrentRequest();
        String tokenStr = request.getHeader("token");
        UserEntity userEntity = (UserEntity) SysInitData.ru.get(tokenStr);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String timeStr = format.format(date);
        String tagIdStr = userEntity.getName() + "-" + timeStr;
        return tagIdStr;
    }

}
