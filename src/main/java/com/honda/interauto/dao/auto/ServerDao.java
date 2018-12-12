package com.honda.interauto.dao.auto;

import com.honda.interauto.dto.ServerDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServerDao {
    public List<ServerDto> getAllServers();
}
