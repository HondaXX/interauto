package com.honda.interauto.controllers;

import com.alibaba.fastjson.JSON;
import com.honda.interauto.dao.auto.GetParamValue;
import com.honda.interauto.dto.InterCaseDto;
import com.honda.interauto.dto.UserDto;
import com.honda.interauto.pojo.*;
import com.honda.interauto.services.InterCaseService;
import com.honda.interauto.services.UserServices;
import com.honda.interauto.tools.httpTool.HttpReqTool;
import com.honda.interauto.tools.sysTool.ParamTool;
import com.honda.interauto.tools.sysTool.TypeChangeTool;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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
        Integer modelId = Integer.parseInt(reqInfo.getRequestBody().get("modelId").toString());
        List<Integer> caseList = (List<Integer>) reqInfo.getRequestBody().get("caseList");

        if (StringUtils.isBlank(proId.toString()) || StringUtils.isBlank(modelId.toString()) || caseList.size() < 0){
            res.setErrorCode(BaseError.PARAM_ERROR);
            res.setErrorDesc(BaseError.PARAM_ERROR_DESC);
            return res;
        }

        for (Integer caseId : caseList){
            logger.debug("start========> case with id: " + caseId);
            InterCaseDto interCaseDto = interCaseService.getInterCaseByCaseId(caseId);

            if (interCaseDto.getNeedInit().equals("1")){
                //用例初始化执行
                Map<String, Object> dbSCodeMap = (Map) JSON.parse(interCaseDto.getInitCode());
                if (!ParamTool.operateDB(dbSCodeMap, gpv, sqlVo)){
                    logger.info("========>初始化用例数据失败");
                    caseResMap.put(caseId, BaseError.CASE_DB_OPER_DESC);
                    continue;
                }
                logger.info("========>初始化用例数据成功");
            }

            if (interCaseDto.getNeedDesignReq().equals("1")){
                //请求前获取参数化请求字段值
                logger.debug("========>参数化开始");
                Map<String, Object> originReqMap = (Map) JSON.parse(interCaseDto.getRequestJson());
                Map<String, Object> paramSqlMap = (Map) JSON.parse(interCaseDto.getReqParam());
                Object paramWithValueMap = ParamTool.getValueForParam(paramSqlMap, gpv, sqlVo);
                if (paramWithValueMap instanceof String){
                    logger.info("========>获取字段值失败");
                    caseResMap.put(caseId, BaseError.CASE_PARAM_GETV_DESC + "{" + paramWithValueMap + "}");
                    continue;
                }
                //获取值替换原有请求值
                Object trueReqMap = ParamTool.replaceSettingParam((Map<String, Object>) paramWithValueMap, originReqMap);
                if (trueReqMap instanceof String){
                    logger.info("========>替换参数化值失败");
                    caseResMap.put(caseId, BaseError.CASE_PARAM_REP_DESC + "{" + trueReqMap + "}");
                    continue;
                }
                logger.info("========>参数化成功，替换请求值...");
                interCaseDto.setRequestJson(trueReqMap.toString());
            }

            //初始化与参数化完成后开始请求接口
            String resInfo = HttpReqTool.httpReqJson(interCaseDto, null);
            if (null == resInfo){
                logger.info("========>接口请求响应出错");
                caseResMap.put(caseId, BaseError.CASE_RES_ERROR_DESC);
                continue;
            }

            Map<String, Object> resMap = (Map<String, Object>) JSON.parse(resInfo);
            //开始处理响应结果

        }
        res.setResCode(BaseError.RESPONSE_OK);
        res.putData("resDetail", caseResMap);
        return res;
    }
}
