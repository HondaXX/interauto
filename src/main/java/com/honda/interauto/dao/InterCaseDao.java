package com.honda.interauto.dao;

import com.honda.interauto.dto.InterCaseDto;
import org.springframework.stereotype.Repository;

@Repository
public interface InterCaseDao {
    public Integer newInterCase(InterCaseDto interCaseDto);
}
