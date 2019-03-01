package com.honda.interauto.dao.auto;

import com.honda.interauto.entity.ServerEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServerDao {
    public List<ServerEntity> getAllServers();
}
