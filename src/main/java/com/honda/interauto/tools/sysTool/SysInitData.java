package com.honda.interauto.tools.sysTool;

import com.honda.interauto.tools.dbTool.RedisUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SysInitData {
    public static Map<String, String > serverMap = new HashMap<String, String>();
    public static List<String> serverList = null;

    public static RedisUtil ru = new RedisUtil();



    public static List<String> getServerList(){
        for (String keyStr : serverMap.keySet()){
            serverList.add(keyStr);
        }
        return serverList;
    }
}
