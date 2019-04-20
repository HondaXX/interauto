package com.honda.interauto.dao.auto;

import com.honda.interauto.entity.PhoneInfoEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhoneInfoDao {

    List<PhoneInfoEntity> getAllPhones();

    PhoneInfoEntity getPhoneById(@Param("deviceId") Integer deviceId);

    Integer newPhone(PhoneInfoEntity phoneInfo);
}
