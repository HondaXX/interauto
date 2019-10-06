package com.honda.interauto.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class CaseResDetailEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String tagId;
    private Integer caseId;
    private Integer evenId;
    private String caseRes;
    private String caseResCode;
    private String caseDes;
    private String caseResDetail;

    public CaseResDetailEntity() {
    }

    public CaseResDetailEntity(Integer id, String tagId, Integer caseId, Integer evenId, String caseRes, String caseResCode, String caseDes, String caseResDetail) {
        this.id = id;
        this.tagId = tagId;
        this.caseId = caseId;
        this.evenId = evenId;
        this.caseRes = caseRes;
        this.caseResCode = caseResCode;
        this.caseDes = caseDes;
        this.caseResDetail = caseResDetail;
    }
}
