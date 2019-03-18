package com.honda.interauto.dao.user;

import com.honda.interauto.entity.RoleEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDao {
    RoleEntity getRoleById(@Param("id")Integer id);
}
