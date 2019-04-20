package com.honda.interauto.entity;

import lombok.Data;

@Data
public class AppInfoEntity {
    private Integer appId;
    private String appPackage;
    private String appActivity;
    private String appWaitActivity;
    private String appDes;
    private String creator;
    private String createTime;
    private String updataTime;
}
