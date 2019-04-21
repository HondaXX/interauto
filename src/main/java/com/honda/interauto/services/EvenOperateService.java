package com.honda.interauto.services;

import com.honda.interauto.dao.auto.EvenOperateDao;
import com.honda.interauto.entity.EvenOperateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvenOperateService {
    @Autowired
    private EvenOperateDao evenOperateDao;

    public List<EvenOperateEntity> getEvenByEvenId(Integer evenId){
        return evenOperateDao.getEvenByEvenId(evenId);
    }
}
