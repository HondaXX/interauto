package com.honda.interauto.tools.sysTool;

public class OtherTool {
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
    //生成随机数
    public static int randomInt(){
        int x = (int) (Math.random() * 2000);
        return x;
    }
}
