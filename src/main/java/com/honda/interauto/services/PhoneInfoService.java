package com.honda.interauto.services;

import com.honda.interauto.dao.auto.PhoneInfoDao;
import com.honda.interauto.entity.PhoneInfoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhoneInfoService {

    @Autowired
    private PhoneInfoDao phoneInfoDao;

    public List<PhoneInfoEntity> getAllPhones(){
        return phoneInfoDao.getAllPhones();
    }

    public PhoneInfoEntity getPhoneById(Integer deviceId){
        return phoneInfoDao.getPhoneById(deviceId);
    }

    public Integer newPhone(PhoneInfoEntity phoneInfoEntity){
        return phoneInfoDao.newPhone(phoneInfoEntity);
    }

}
