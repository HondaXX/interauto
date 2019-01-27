package com.honda.interauto.dto;

import org.springframework.stereotype.Component;

@Component
public class ProDto {
    private Integer proId;
    private String des;

    public Integer getProId() {
        return proId;
    }

    public void setProId(Integer proId) {
        this.proId = proId;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
