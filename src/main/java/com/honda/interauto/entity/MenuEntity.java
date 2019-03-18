package com.honda.interauto.entity;

import lombok.Data;

@Data
public class MenuEntity {
    private Integer id;
    private String fatherMenu;
    private String menuName;
    private String menuDes;
}
