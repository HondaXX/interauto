package com.honda.interauto.dao.user;

import com.honda.interauto.entity.UserEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {
    public List<UserEntity> getAllUsers();

    public UserEntity identifyUserByNP(@Param("name") String name, @Param("password") String password);
}
