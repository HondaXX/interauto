package com.honda.interauto.tools.sysTool;

import com.google.gson.internal.LinkedTreeMap;
import com.honda.interauto.dao.auto.GetParamValue;
import com.honda.interauto.pojo.SqlVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class ParamTool {
    private static Logger logger = LogManager.getLogger(ParamTool.class);

    public static Object getValueForParam(Map<String, Object> paramSqlMap, GetParamValue gpv, SqlVo sqlVo) {
        Map<String, Object> vfpMap = new HashMap<String, Object>();
        for (String dbUser : paramSqlMap.keySet()) {
            Map<String, String> sqlMap = (Map<String, String>) paramSqlMap.get(dbUser);
            for(String paramStr : sqlMap.keySet()) {
                String sql = sqlMap.get(paramStr);
                sqlVo.setSqlStr(sql);
                Object value = gpv.getParamValue(sqlVo);
                if (StringUtils.isBlank(value.toString())) {
                    logger.info("========>get value from db fail, param:{}, sql:{}", paramStr, sql);
                    return paramStr + "@" + sql;
                }
                vfpMap.put(paramStr, value);
            }
        }
        return vfpMap;
    }

    public static Object replaceSettingParam(Map<String, Object> paramAndValueMap, Map<String, Object> originMap) {
        Set<String> paramSet = paramAndValueMap.keySet();
        Set<String> keySetFirst = originMap.keySet();

        List<String> mapParamList = new ArrayList<String>();
        for(String paramReq : originMap.keySet()) {
            if (originMap.get(paramReq) instanceof Map) {
                mapParamList.add(paramReq);
            }
        }

        for(String param : paramSet) {
            if (keySetFirst.contains(param) && originMap.get(param).equals("#")) {
                originMap.put(param, paramAndValueMap.get(param));
                return originMap;
            }else {
                for(String mapParam : mapParamList) {
                    Map<String, Object> secondMap = (Map<String, Object>) originMap.get(mapParam);
                    Set<String> keySetSecond = secondMap.keySet();
                    if (keySetSecond.contains(param) && secondMap.get(param).equals("#")) {
                        secondMap.put(param, paramAndValueMap.get(param));
                        originMap.put(mapParam, secondMap);
                    }else {
                        logger.info("====>bind param error, can't find param: " + param);
                        return param;
                    }
                }
            }
        }
        return originMap;
    }

    public static String operateDB(Map<String, Object> dbCodeMap, GetParamValue gpv, SqlVo sqlVo) {
        if (null == dbCodeMap) {
            return "badCode";
        }else {
            for(String dbUser : dbCodeMap.keySet()) {
                List<String> sqlList = (List<String>) dbCodeMap.get(dbUser);
                for(String sqlStr : sqlList) {
                    sqlVo.setSqlStr(sqlStr);
                    try {
                        gpv.updateSql(sqlVo);
                    } catch (Exception e) {
                        logger.info("====>update error which sql: {}", sqlStr);
                        e.printStackTrace();
                        return sqlStr;
                    }
                }
            }
            return "success";
        }
    }

    public static Map<String, String> compareRes(Map<String, Object> trueResMap, Map<String, Object> trueRxpectMap) {
        //对比结果存放，0失败 1成功
        Map<String, String> compareResMap = new HashMap<String, String>();

        for(String trueKey : trueResMap.keySet()) {
            if (trueRxpectMap.containsKey(trueKey)) {
                Object trueValue = trueResMap.get(trueKey);
                Object expectValue = trueRxpectMap.get(trueKey);
                //如果返回有多层，继续往下走，最多处理两层
                if (!(trueValue instanceof LinkedTreeMap && expectValue instanceof LinkedTreeMap)) {
                    if (!trueValue.equals(expectValue)) {
                        logger.info("========>外层结果比对不通过: {}--[result: {}, except: {}]", trueKey, trueValue, expectValue);
                        compareResMap.put("comRes", "0");
                        compareResMap.put("unequalParam", trueKey + ": except-[" + expectValue + "],real-[" + trueValue + "]");
                        return compareResMap;
                    }
                }else{
                    Map<String, Object> trueValueMap = (Map<String, Object>) trueValue;
                    Map<String, Object> expectValueMap = (Map<String, Object>) expectValue;
                    for(String childTrueKey : trueValueMap.keySet()) {
                        if (expectValueMap.containsKey(childTrueKey)) {
                            Object childTrueValue = trueValueMap.get(childTrueKey);
                            Object childExpectValue = expectValueMap.get(childTrueKey);
                            if (!childTrueValue.equals(childExpectValue)) {
                                logger.info("========>内层结果比对不通过: {}--[result: {}, except: {}]", childTrueKey, childTrueValue, childExpectValue);
                                compareResMap.put("comRes", "0");
                                compareResMap.put("unequalParam", childTrueKey + ": except-[" + childExpectValue + "],real-[" + childTrueValue + "]");
                                return compareResMap;
                            }
                        }else {
                            logger.info("========>预期不存在的内层对比值: " + childTrueKey);
                            compareResMap.put("comRes", "0");
                            compareResMap.put("lessParam", childTrueKey);
                            return compareResMap;
                        }
                    }
                }
            }else {
                logger.info("========>预期不存在的外层对比值: " + trueKey);
                compareResMap.put("comRes", "0");
                compareResMap.put("lessParam", trueKey);
                return compareResMap;
            }
        }
        compareResMap.put("comRes", "1");
        return compareResMap;
    }

    public static Map<String, String> compareResKey(Map<String, Object> trueResMap, Map<String, Object> expectMap){
        //对比结果存放，0失败 1成功
        Map<String, String> compareResMap = new HashMap<String, String>();
        for(String trueKey : trueResMap.keySet()){
            if (expectMap.containsKey(trueKey)) {
                Object trueValue = trueResMap.get(trueKey);
                Object expectValue = expectMap.get(trueKey);
                //如果返回有多层，继续往下走，最多处理两层
                if (!(trueValue instanceof LinkedTreeMap && expectValue instanceof LinkedTreeMap)) {
                    continue;
                }else{
                    Map<String, Object> trueValueMap = (Map<String, Object>) trueValue;
                    Map<String, Object> expectValueMap = (Map<String, Object>) expectValue;
                    for(String childTrueKey : trueValueMap.keySet()) {
                        if (expectValueMap.containsKey(childTrueKey)) {
                            continue;
                        }else {
                            logger.info("========>预期不存在的内层key值: " + childTrueKey);
                            compareResMap.put("comRes", "0");
                            compareResMap.put("lessParam", childTrueKey);
                            return compareResMap;
                        }
                    }
                }
            }else {
                logger.info("========>预期不存在的外层key值: " + trueKey);
                compareResMap.put("comRes", "0");
                compareResMap.put("lessParam", trueKey);
                return compareResMap;
            }
        }
        compareResMap.put("comRes", "1");
        return compareResMap;
    }
}
