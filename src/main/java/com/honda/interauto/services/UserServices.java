package com.honda.interauto.services;

import com.honda.interauto.entity.UserEntity;
import com.honda.interauto.dao.user.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServices {
    @Autowired
    private UserDao userDao;

    public List<UserEntity> getAllUsers() {
        return userDao.getAllUsers();
    }

    public UserEntity identifyUserByNP(String name, String password){
        return userDao.identifyUserByNP(name, password);
    }
}
