package com.honda.interauto.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class RoleMenuDto {
    private String firstLevel;
    private List<Map<String, String>> secondLevel;

}
