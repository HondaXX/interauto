package com.honda.interauto.entity;

import org.springframework.stereotype.Component;

@Component
public class ModelEntity {
    private Integer proId;
    private Integer modelId;
    private String des;

    public Integer getProId() {
        return proId;
    }

    public void setProId(Integer proId) {
        this.proId = proId;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
