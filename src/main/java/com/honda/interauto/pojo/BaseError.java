package com.honda.interauto.pojo;

public interface BaseError {
    //常量类
    //请求响应
    String RESPONSE_OK = "0";

    //错误
    String SYS_ERROR = "100";
    String SYS_ERROR_DESC = "system error";
    String NULL_ERROR = "101";
    String NULL_ERROR_DESC = "serverId is null";
    String FOMAT_ERROR = "102";
    String FOMAT_ERROR_DESC = "Format error";
    String SERVERID_NOT_FOUND = "103";
    String SERVERID_NOT_FOUND_DESC = "serverId not found";
    String PARAM_ERROR = "104";
    String PARAM_ERROR_DESC = "param not found or null";
    String DB_ERROR = "105";
    String DB_ERROR_DESC = "db error";

    //用例执行错误
    String CASE_RES_ERROR = "200";
    String CASE_RES_ERROR_DESC = "用例响应错误";
    String CASE_PARAM_CHN = "201";
    String CASE_PARAM_GETV_DESC = "字段初始化获取值错误";
    String CASE_PARAM_REP = "202";
    String CASE_PARAM_REP_DESC = "字段初始化替换值错误";
    String CASE_DB_OPER = "203";
    String CASE_DB_OPER_DESC = "初始化/还原用例数据错误";

    //空值
    String NULL_VALUE = "null";

    //系统常量
    String RESULT_CODE = "resultCode";
    String ERROR_CODE = "errorCode";
    String ERROR_DESC = "errorDesc";

}
