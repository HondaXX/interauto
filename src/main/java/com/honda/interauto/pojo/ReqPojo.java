package com.honda.interauto.pojo;

import java.util.Map;

public class ReqPojo{

    private String serverId;
    private Map<String, Object> requestBody;

    public String getServerId() {
        return serverId;
    }
    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public Map<String, Object> getRequestBody() {
        return requestBody;
    }
    public void setRequestBody(Map<String, Object> requestBody) {
        this.requestBody = requestBody;
    }
}
