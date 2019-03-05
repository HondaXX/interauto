package com.honda.interauto.dao.auto;

import com.honda.interauto.entity.CaseResOverViewEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface CaseResOverViewDao {
    public Integer recordOverView(CaseResOverViewEntity caseResOverViewEntity);
}
