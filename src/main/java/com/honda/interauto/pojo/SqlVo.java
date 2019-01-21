package com.honda.interauto.pojo;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SqlVo {
    private String sqlStr;
    private Map<String, String> sqlMap;

    public String getSqlStr() {
        return sqlStr;
    }
    public void setSqlStr(String sqlStr) {
        this.sqlStr = sqlStr;
    }
    public Map<String, String> getSqlMap() {
        return sqlMap;
    }
    public void setSqlMap(Map<String, String> sqlMap) {
        this.sqlMap = sqlMap;
    }
}