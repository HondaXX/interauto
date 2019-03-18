package com.honda.interauto.dao.user;

import com.honda.interauto.entity.MenuEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuDao {
    List<MenuEntity> getMenuById(@Param("fatherList")List<String> fatherList, @Param("cutList")List<String> cutList);
}
