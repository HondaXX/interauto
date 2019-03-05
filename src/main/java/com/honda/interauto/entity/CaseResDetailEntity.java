package com.honda.interauto.entity;

import org.springframework.stereotype.Component;

@Component
public class CaseResDetailEntity {
    private Integer id;
    private String tagId;
    private Integer caseId;
    private String caseRes;
    private String caseResCode;
    private String caseDes;
    private String caseResDetail;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public String getCaseRes() {
        return caseRes;
    }

    public void setCaseRes(String caseRes) {
        this.caseRes = caseRes;
    }

    public String getCaseResCode() {
        return caseResCode;
    }

    public void setCaseResCode(String caseResCode) {
        this.caseResCode = caseResCode;
    }

    public String getCaseDes() {
        return caseDes;
    }

    public void setCaseDes(String caseDes) {
        this.caseDes = caseDes;
    }

    public String getCaseResDetail() {
        return caseResDetail;
    }

    public void setCaseResDetail(String caseResDetail) {
        this.caseResDetail = caseResDetail;
    }

    public CaseResDetailEntity(String tagId, Integer caseId, String caseRes, String caseResCode, String caseDes, String caseResDetail) {
        this.tagId = tagId;
        this.caseId = caseId;
        this.caseRes = caseRes;
        this.caseResCode = caseResCode;
        this.caseDes = caseDes;
        this.caseResDetail = caseResDetail;
    }
}
