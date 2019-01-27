package com.honda.interauto.services;

import com.honda.interauto.dao.auto.ModelDao;
import com.honda.interauto.dto.ModelDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModelService {

    @Autowired
    private ModelDao modelDao;

    public List<ModelDto> getProModels(Integer proId){
        return modelDao.getProModels(proId);
    }
}
