package com.honda.interauto.services;

import com.honda.interauto.dao.ServerDao;
import com.honda.interauto.dto.ServerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServerService {
    @Autowired
    ServerDao serverDao;

    public List<ServerDto> getAllServers(){
        return serverDao.getAllServers();
    }
}
