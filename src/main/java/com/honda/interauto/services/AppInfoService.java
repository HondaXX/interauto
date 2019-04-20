package com.honda.interauto.services;

import com.honda.interauto.dao.auto.AppInfoDao;
import com.honda.interauto.entity.AppInfoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppInfoService {
    @Autowired
    private AppInfoDao appInfoDao;

    public Integer newApp(AppInfoEntity appInfoEntity){
        return appInfoDao.newApp(appInfoEntity);
    }

    public List<AppInfoEntity> getAllApps(){
        return appInfoDao.getAllApps();
    }

    public AppInfoEntity getAppById(Integer appId){
        return appInfoDao.getAppById(appId);
    }
}
