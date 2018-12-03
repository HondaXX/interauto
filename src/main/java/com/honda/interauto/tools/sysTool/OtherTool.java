package com.honda.interauto.tools.sysTool;

public class StringTool {
    //分隔符处理
    public static String[] splitStr(String str, String tagStr){
        if (tagStr.equals(":")){
            String[] strList = str.split(tagStr);
            if (strList.length < 2){
                String[] strListA = str.split("：");
                return strListA;
            }else {
                return strList;
            }
        }
        return null;
    }
}
