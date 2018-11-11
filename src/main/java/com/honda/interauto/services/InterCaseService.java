package com.honda.interauto.services;

import com.honda.interauto.dao.InterCaseDao;
import com.honda.interauto.dto.InterCaseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InterCaseService {
    @Autowired
    private InterCaseDao interCaseDao;

    public Integer newInterCase(InterCaseDto interCaseDto){
        return interCaseDao.newInterCase(interCaseDto);
    }
}
