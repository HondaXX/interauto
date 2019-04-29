package com.honda.interauto.pojo;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@ConfigurationProperties(prefix = "baseconfigs")
public class BaseConfigs {
    private String zipFilePath;

    private String appiumServerHost;

    private String appiumScreenFile;

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

    public String getAppiumScreenFile() {
        return appiumScreenFile;
    }

    public void setAppiumScreenFile(String appiumScreenFile) {
        this.appiumScreenFile = appiumScreenFile;
    }

}
