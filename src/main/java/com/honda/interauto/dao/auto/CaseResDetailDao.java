package com.honda.interauto.dao.auto;

import com.honda.interauto.entity.CaseResDetailEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface CaseResDetailDao {
    public Integer saveCaseRes(CaseResDetailEntity caseResDetailEntity);
}
