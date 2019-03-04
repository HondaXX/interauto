package com.honda.interauto.dto;

import org.springframework.stereotype.Component;

@Component
public class ProModelDto {
    private Integer proId;
    private String proName;
    private Integer modelId;
    private String modelName;
    private Integer caseID;
    private String caseAim;

    public Integer getProId() {
        return proId;
    }

    public void setProId(Integer proId) {
        this.proId = proId;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Integer getCaseID() {
        return caseID;
    }

    public void setCaseID(Integer caseID) {
        this.caseID = caseID;
    }

    public String getCaseAim() {
        return caseAim;
    }

    public void setCaseAim(String caseAim) {
        this.caseAim = caseAim;
    }
}
