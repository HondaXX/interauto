package com.honda.interauto.dao.auto;

import com.honda.interauto.dto.ProDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProDao {
    public List<ProDto> getAllPros();
}
