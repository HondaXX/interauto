package com.honda.interauto.services;

import com.honda.interauto.dao.auto.ProDao;
import com.honda.interauto.entity.ProEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProService {
    @Autowired
    private ProDao proDao;

    public List<ProEntity> getAllPros(){
        return proDao.getAllPros();
    }

}
