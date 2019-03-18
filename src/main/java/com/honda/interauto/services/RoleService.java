package com.honda.interauto.services;

import com.honda.interauto.dao.user.RoleDao;
import com.honda.interauto.entity.RoleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    @Autowired
    private RoleDao roleDao;

    public RoleEntity getRoleById(Integer id){
        return roleDao.getRoleById(id);
    }
}
