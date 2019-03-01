package com.honda.interauto.entity;

import org.springframework.stereotype.Component;

@Component
public class ProEntity {
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
