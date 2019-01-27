package com.honda.interauto.services;

import com.honda.interauto.dao.auto.ModelDao;
import com.honda.interauto.dao.auto.ProDao;
import com.honda.interauto.dto.ModelDto;
import com.honda.interauto.dto.ProDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProService {
    @Autowired
    private ProDao proDao;

    public List<ProDto> getAllPros(){
        return proDao.getAllPros();
    }

}
