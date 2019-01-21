package com.honda.interauto.dao.auto;

import com.honda.interauto.pojo.SqlVo;
import org.springframework.stereotype.Repository;

@Repository
public interface GetParamValue {
    public String getParamValue(SqlVo sqlVo);
    public Boolean updateSql(SqlVo sqlVo);
}
