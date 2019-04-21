package com.honda.interauto.entity;

import lombok.Data;

@Data
public class EvenOperateEntity {
    private Integer operateId;
    private Integer evenId;
    private Integer sort;
    private Integer eleMethod;
    private String eleIdentify;
    private Integer eleOperate;
    private String eleText;
    private Integer slipDerection;
    private String beforeEven;
    private String creator;
    private String createTime;
    private String updataTime;
}
