package com.honda.interauto.pojo;

//常量类
public interface BaseError {
    String RESPONSE_OK = "0";
    //错误
    String SYS_ERROR = "100";
    String SYS_ERROR_DESC = "system error";
    String NULL_ERROR = "101";
    String NULL_ERROR_DESC = "serverId is null";
    String FOMAT_ERROR = "102";
    String FOMAT_ERROR_DESC = "请求参数格式错误";
    String SERVERID_NOT_FOUND = "103";
    String SERVERID_NOT_FOUND_DESC = "serverId not found";
    String PARAM_ERROR = "104";
    String PARAM_ERROR_DESC = "param not found or null";
    String DB_ERROR = "105";
    String DB_ERROR_DESC = "db error";
    String PARAM_NULL = "106";
    String PARAM_NULL_DESC = "该传值的字段为空";


    String CASE_OK = "00";
    //用例执行错误
    String CASE_RES_ERROR = "200";
    String CASE_RES_ERROR_DESC = "用例响应错误";
    String CASE_PARAM_GETV = "201";
    String CASE_PARAM_GETV_DESC = "字段参数化(请求/响应)获取值错误";
    String CASE_PARAM_REP = "202";
    String CASE_PARAM_REP_DESC = "字段参数化(请求/响应)替换值错误";
    String CASE_DB_OPER = "203";
    String CASE_DB_OPER_DESC = "(初始化/还原)用例数据错误";
    String CASE_COMP_UNEQ = "204";
    String CASE_COMP_UNEQ_DESC = "结果比对不一致";
    String CASE_COMP_LESSKEY = "205";
    String CASE_CONP_LESSKEY_DESC = "不存在的对比值";


    //用户错误
    String USER_NOTFOUND = "300";
    String USER_NOTFOUND_DESC = "登录名或密码错误";
    String USER_TOKENLESS = "301";
    String USER_TOKENLESS_DESC = "token为空";
    String USER_TOKENOVERDUE = "302";
    String USER_TOKENOVERDUE_DESC = "无效的token信息，请重新登录";
    String USER_TOKENNOTMATCH = "303";
    String USER_TOKENNOTMATCH_DESC = "token与用户不匹配，请重新登录";


    //系统常量
    String RESULT_CODE = "resCode";
    String ERROR_CODE = "errorCode";
    String ERROR_DESC = "errorDesc";
}
