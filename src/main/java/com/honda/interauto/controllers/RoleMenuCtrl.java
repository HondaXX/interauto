package com.honda.interauto.controllers;

import com.honda.interauto.dao.user.MenuDao;
import com.honda.interauto.entity.MenuEntity;
import com.honda.interauto.entity.RoleEntity;
import com.honda.interauto.pojo.BaseError;
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

import java.util.*;


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

        Map<String, List<Map<String, String>>>  roleMenuMap = new HashMap<String, List<Map<String, String>>>();
        for (MenuEntity menuEntity : menuList){
            String firstLV = new String();
            List<Map<String, String>> secondLV = new ArrayList<Map<String, String>>();
            if (StringUtils.isBlank(menuEntity.getFatherMenu())){
                firstLV = menuEntity.getId().toString() + menuEntity.getMenuName();
                secondLV = new ArrayList<Map<String, String>>();
                Map<String, String> secondLVMap = new HashMap<String, String>();
                for (MenuEntity menuEntityChild : menuList){
                    if (menuEntityChild.getFatherMenu().equals(menuEntity.getId())){
                        secondLVMap.put(menuEntityChild.getId().toString(), menuEntityChild.getMenuName());
                    }
                }
                secondLV.add(secondLVMap);
            }
            roleMenuMap.put(firstLV, secondLV);
        }


        ResPojo res = new ResPojo();
        res.setResCode(BaseError.RESPONSE_OK);
        res.putData("menu", roleMenuMap);
        return res;
    }
}
