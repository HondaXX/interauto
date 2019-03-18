package com.honda.interauto.services;

import com.honda.interauto.dao.user.MenuDao;
import com.honda.interauto.entity.MenuEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {
    @Autowired
    private MenuDao menuDao;

    public List<MenuEntity> getMenuById(List<String> fatherList,List<String> cutList){
        return menuDao.getMenuById(fatherList, cutList);
    }
}
