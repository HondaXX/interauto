package com.honda.interauto.pojo;

import java.util.HashMap;

public class InnerResPojo extends HashMap<String, Object> {
    private static final long serialVersionUID = 870714850748751853L;

    public void setInnerCode(String innerCode) {
        put(BaseError.ERROR_CODE, innerCode);
    }

    public String getInnerCode() {
        return (getData(BaseError.ERROR_CODE)).toString();
    }

    public void setInnerDesc(String innerDesc){
        put(BaseError.ERROR_DESC, innerDesc);
    }

    public String getInnerDesc(){
        return (getData(BaseError.ERROR_DESC)).toString();
    }

    public Object getData(String key) {
        return get(key);
    }
}
