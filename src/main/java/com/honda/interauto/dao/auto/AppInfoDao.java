package com.honda.interauto.dao.auto;

import com.honda.interauto.entity.AppInfoEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppInfoDao {
    Integer newApp(AppInfoEntity appInfo);

    List<AppInfoEntity> getAllApps();

    AppInfoEntity getAppById(@Param("appId")Integer appid);
}
