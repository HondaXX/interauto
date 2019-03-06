package com.honda.interauto.entity;

import org.springframework.stereotype.Component;

@Component
public class InterCaseEntity {
    private Integer caseId;
    private Integer proId;
    private Integer modelId;

    private String DNS;
    private String needCookie;
    private String caseAim;
    private String interUrl;
    private String requestMethod;//0post,1get
    private String needDesignReq;
    private String requestJson;
    private String reqParam;
    private String needInit;//0no,1sql,2redis
    private String initCode;
    private String compareType;  //0全等,1key等
    private String needDesignRes;
    private String expectRes;
    private String resParam;
    private String needRoll;//0no,1sql,2redis
    private String rollCode;
    private String saveParam;
    private String usedParam;
    private String caseStatus;
    private String createTime;
    private String updataTime;

    public Integer getCaseId() {
        return caseId;
    }

    public Integer getModelId() {
        return modelId;
    }

    public Integer getProId() {
        return proId;
    }

    public void setProId(Integer proId) {
        this.proId = proId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public String getDNS() {
        return DNS;
    }

    public void setDNS(String DNS) {
        this.DNS = DNS;
    }

    public String getNeedCookie() {
        return needCookie;
    }

    public void setNeedCookie(String needCookie) {
        this.needCookie = needCookie;
    }

    public String getCaseAim() {
        return caseAim;
    }

    public void setCaseAim(String caseAim) {
        this.caseAim = caseAim;
    }

    public String getInterUrl() {
        return interUrl;
    }

    public void setInterUrl(String interUrl) {
        this.interUrl = interUrl;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getNeedDesignReq() {
        return needDesignReq;
    }

    public void setNeedDesignReq(String needDesignReq) {
        this.needDesignReq = needDesignReq;
    }

    public String getRequestJson() {
        return requestJson;
    }

    public void setRequestJson(String requestJson) {
        this.requestJson = requestJson;
    }

    public String getReqParam() {
        return reqParam;
    }

    public void setReqParam(String reqParam) {
        this.reqParam = reqParam;
    }

    public String getNeedInit() {
        return needInit;
    }

    public void setNeedInit(String needInit) {
        this.needInit = needInit;
    }

    public String getInitCode() {
        return initCode;
    }

    public void setInitCode(String initCode) {
        this.initCode = initCode;
    }

    public String getCompareType() {
        return compareType;
    }

    public void setCompareType(String compareType) {
        this.compareType = compareType;
    }

    public String getNeedDesignRes() {
        return needDesignRes;
    }

    public void setNeedDesignRes(String needDesignRes) {
        this.needDesignRes = needDesignRes;
    }

    public String getExpectRes() {
        return expectRes;
    }

    public void setExpectRes(String expectRes) {
        this.expectRes = expectRes;
    }

    public String getResParam() {
        return resParam;
    }

    public void setResParam(String resParam) {
        this.resParam = resParam;
    }

    public String getNeedRoll() {
        return needRoll;
    }

    public void setNeedRoll(String needRoll) {
        this.needRoll = needRoll;
    }

    public String getRollCode() {
        return rollCode;
    }

    public void setRollCode(String rollCode) {
        this.rollCode = rollCode;
    }

    public String getSaveParam() {
        return saveParam;
    }

    public void setSaveParam(String saveParam) {
        this.saveParam = saveParam;
    }

    public String getUsedParam() {
        return usedParam;
    }

    public void setUsedParam(String usedParam) {
        this.usedParam = usedParam;
    }

    public String getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(String caseStatus) {
        this.caseStatus = caseStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdataTime() {
        return updataTime;
    }

    public void setUpdataTime(String updataTime) {
        this.updataTime = updataTime;
    }
}
