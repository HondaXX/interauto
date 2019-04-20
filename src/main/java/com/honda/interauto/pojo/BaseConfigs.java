package com.honda.interauto.pojo;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@ConfigurationProperties(prefix = "baseconfigs")
public class BaseConfigs {
    private String zipFilePath;

    private String appiumServerHost;

    public String getZipFilePath() {
        return zipFilePath;
    }

    public void setZipFilePath(String zipFilePath) {
        this.zipFilePath = zipFilePath;
    }

    public String getAppiumServerHost() {
        return appiumServerHost;
    }

    public void setAppiumServerHost(String appiumServerHost) {
        this.appiumServerHost = appiumServerHost;
    }
}
