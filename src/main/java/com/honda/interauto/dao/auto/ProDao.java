package com.honda.interauto.dao.auto;

import com.honda.interauto.entity.ProEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProDao {
    public List<ProEntity> getAllPros();
}
