package com.honda.interauto.pojo;

public interface BaseError {
    //常量类
    //错误
    String SYS_ERROR = "100";
    String SYS_ERROR_DESC = "system error";
    String NULL_ERROR = "101";
    String NULL_ERROR_DESC = "param is null";
    String FOMAT_ERROR = "102";
    String FOMAT_ERROR_DESC = "Format error";
    String SERVERID_NOT_FOUND = "103";
    String SERVERID_NOT_FOUND_DESC = "serverId not found";

    //空值
    String NULL_VALUE = "null";
    String EMPTY_VALUE = "";
    String EMPTY_HAS_VALUE = " ";

    //日志类型
    String LOGGER_INTERFACE = "interface";
    String LOGGER_ACCESS = "access";
    //业务id
    String TRANSACTION_ID = "transactionId";
    String SERVICE_ID = "serviceId";
    String MODULE_ID = "moduleId";
    String PROCESS_ID = "processId";
    //请求响应
    String RESULT_CODE = "resultCode";
    String ERROR_CODE = "errorCode";
    String ERROR_DESC = "errorDesc";
    String REQUEST_BODY = "requestBody";
    //相应状态
    String RESPONSE_OK = "0";
}
