package com.honda.interauto.entity;

import lombok.Data;

@Data
public class PhoneInfoEntity {
    private Integer deviceId;
    private String phoneName;
    private String platformName;
    private String deviceName;
    private String platformVersion;
    private String creator;
    private String createTime;
    private String updataTime;
}
