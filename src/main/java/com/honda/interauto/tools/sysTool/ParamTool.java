package com.honda.interauto.tools.sysTool;

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
                    logger.info("========>get value from db error, param:{}, sql:{}", paramStr, sql);
                    return paramStr;
                }
                vfpMap.put(paramStr, value);
            }
        }
        return vfpMap;
    }

    public static Object replaceSettingParam(Map<String, Object> paramAndValueMap, Map<String, Object> reqMap) {
        Set<String> paramSet = paramAndValueMap.keySet();
        Set<String> keySetFirst = reqMap.keySet();

        List<String> mapParamList = new ArrayList<String>();
        for(String paramReq : reqMap.keySet()) {
            if (reqMap.get(paramReq) instanceof Map) {
                mapParamList.add(paramReq);
            }
        }

        for(String param : paramSet) {
            if (keySetFirst.contains(param) && reqMap.get(param).equals("#")) {
                reqMap.put(param, paramAndValueMap.get(param));
                return reqMap;
            }else {
                for(String mapParam : mapParamList) {
                    Map<String, Object> secondMap = (Map<String, Object>) reqMap.get(mapParam);
                    Set<String> keySetSecond = secondMap.keySet();
                    if (keySetSecond.contains(param) && secondMap.get(param).equals("#")) {
                        secondMap.put(param, paramAndValueMap.get(param));
                        reqMap.put(mapParam, secondMap);
                    }else {
                        logger.info("====>bind param error, can't find param: " + param);
                        return param;
                    }
                }
            }
        }
        return reqMap;
    }

    public static boolean operateDB(Map<String, Object> dbCodeMap, GetParamValue gpv, SqlVo sqlVo) {
        if (null == dbCodeMap) {
            return false;
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
                        return false;
                    }
                }
            }
        }
        return true;
    }

}
