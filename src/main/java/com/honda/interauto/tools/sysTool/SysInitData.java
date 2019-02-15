package com.honda.interauto.tools.sysTool;

import com.honda.interauto.tools.dbTool.RedisUtil;

import java.util.ArrayList;
import java.util.List;

public class SysInitData {
    public static List<String> serverList = new ArrayList<>();

    public static RedisUtil ru = new RedisUtil();
}
