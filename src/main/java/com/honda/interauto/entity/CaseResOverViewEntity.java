package com.honda.interauto.entity;

import lombok.Data;

@Data
public class CaseResOverViewEntity {
    private Integer id;
    private Integer proId;
    private String tagId;
    private Integer totalCount;
    private Integer failCount;
    private Integer successCount;
    private String operator;
    private String startTime;
    private String endTime;

    public CaseResOverViewEntity() {
    }

    public CaseResOverViewEntity(String tagId, Integer proId, Integer totalCount, Integer failCount, Integer successCount, String operator, String startTime, String endTime) {
        this.tagId = tagId;
        this.proId = proId;
        this.totalCount = totalCount;
        this.failCount = failCount;
        this.successCount = successCount;
        this.operator = operator;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
