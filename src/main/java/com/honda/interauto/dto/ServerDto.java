package com.honda.interauto.dto;

import org.springframework.stereotype.Component;

@Component
public class ServerDto {
    private Integer id;
    private String serverId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }
}
