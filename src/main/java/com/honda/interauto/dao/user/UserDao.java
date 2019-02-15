package com.honda.interauto.dao.user;

import com.honda.interauto.dto.UserDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {
    public List<UserDto> getAllUsers();

    public UserDto identifyUserByNP(@Param("name") String name, @Param("password") String password);
}
