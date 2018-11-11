package com.honda.interauto.pojo;

public interface BaseError {
    //常量类
    //请求响应
    String RESPONSE_OK = "0";

    //错误
    String SYS_ERROR = "100";
    String SYS_ERROR_DESC = "system error";
    String NULL_ERROR = "101";
    String NULL_ERROR_DESC = "param null";
    String FOMAT_ERROR = "102";
    String FOMAT_ERROR_DESC = "Format error";
    String SERVERID_NOT_FOUND = "103";
    String SERVERID_NOT_FOUND_DESC = "serverId not found";

    //空值
    String NULL_VALUE = "null";
    String EMPTY_VALUE = "";

    //系统常量
    String RESULT_CODE = "resultCode";
    String ERROR_CODE = "errorCode";
    String ERROR_DESC = "errorDesc";

}
