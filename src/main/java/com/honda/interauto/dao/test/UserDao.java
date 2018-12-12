package com.honda.interauto.dao.test;

import com.honda.interauto.dto.UserDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {
    public List<UserDto> getAllUsers();
}
