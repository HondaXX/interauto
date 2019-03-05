package com.honda.interauto.entity;

import org.springframework.stereotype.Component;

@Component
public class CaseResOverViewEntity {
    private Integer id;
    private String tagId;
    private Integer totalCount;
    private Integer failCount;
    private Integer successCount;
    private String operator;
    private String startTime;
    private String endTime;

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

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getFailCount() {
        return failCount;
    }

    public void setFailCount(Integer failCount) {
        this.failCount = failCount;
    }

    public Integer getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public CaseResOverViewEntity(String tagId, Integer totalCount, Integer failCount, Integer successCount, String operator, String startTime, String endTime) {
        this.tagId = tagId;
        this.totalCount = totalCount;
        this.failCount = failCount;
        this.successCount = successCount;
        this.operator = operator;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
