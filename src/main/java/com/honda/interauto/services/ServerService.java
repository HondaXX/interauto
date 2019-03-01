package com.honda.interauto.services;

import com.honda.interauto.dao.auto.ServerDao;
import com.honda.interauto.entity.ServerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServerService {
    @Autowired
    private ServerDao serverDao;

    public List<ServerEntity> getAllServers(){
        return serverDao.getAllServers();
    }
}
