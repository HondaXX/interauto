package com.honda.interauto.pojo;

import org.springframework.stereotype.Service;

import java.util.HashMap;


@Service
public class ResPojo extends HashMap<String, Object>{
    private static final long serialVersionUID = 870714850748751852L;

    public void setResCode(String resultCode) {
        put(BaseError.RESULT_CODE, resultCode);
    }

    public String getResCode() {
        return getData(BaseError.RESULT_CODE).toString();
    }

    public void setErrorCode(String errorCode) {
        put(BaseError.ERROR_CODE, errorCode);
    }

    public String getErrorCode() {
        return getData(BaseError.ERROR_CODE).toString();
    }

    public void setErrorDesc(String errorDesc) {
        put(BaseError.ERROR_DESC, errorDesc);
    }

    public String getErrorDesc() {
        return getData(BaseError.ERROR_DESC).toString();
    }

    public Object getData(String key) {
        return get(key);
    }

    public Object putData(String key, Object value) {
        return put(key, value);
    }

    public boolean hasErrors() {
        if (!BaseError.RESPONSE_OK.equals(getResCode())) {
            return true;
        }else {
            return false;
        }
    }


}
