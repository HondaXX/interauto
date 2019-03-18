package com.honda.interauto.controllers;

import com.honda.interauto.dao.user.MenuDao;
import com.honda.interauto.entity.MenuEntity;
import com.honda.interauto.entity.RoleEntity;
import com.honda.interauto.pojo.ReqPojo;
import com.honda.interauto.pojo.ResPojo;
import com.honda.interauto.services.MenuService;
import com.honda.interauto.services.RoleService;
import com.honda.interauto.tools.sysTool.OtherTool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping(value = "/RoleMenu")
public class RoleMenuCtrl {
    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuService menuService;

    @PostMapping(value = "/GetUserMenu.json", produces = "application/json;charset=UTF-8")
    public ResPojo getUserMenu(@RequestBody ReqPojo reqPojo){
        Integer id = Integer.parseInt(reqPojo.getRequestBody().get("userId").toString());
        RoleEntity roleEntity = roleService.getRoleById(id);
        if (StringUtils.isBlank(roleEntity.getFatherMenu())){
            ResPojo res = new ResPojo();
            res.setErrorCode("父列表不能为空");
            return res;
        }
        String[] fatherArray = OtherTool.splitStr(roleEntity.getFatherMenu(), ",");
        String[] cutArray = OtherTool.splitStr(roleEntity.getCutMenu(), ",");

        List<MenuEntity> menuList = new ArrayList<MenuEntity>();
        List<String> fatherList = Arrays.asList(fatherArray);
        if (cutArray.length > 0){
            List<String> cutList = Arrays.asList(cutArray);
            menuList = menuService.getMenuById(fatherList, cutList);
        }else {
            menuList = menuService.getMenuById(fatherList, null);
        }

        ResPojo res = new ResPojo();
        res.putData("role", roleEntity);
        res.putData("menu", menuList);
        return res;
    }
}
