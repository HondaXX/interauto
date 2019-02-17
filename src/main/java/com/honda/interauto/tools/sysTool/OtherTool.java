package com.honda.interauto.tools.sysTool;

import java.util.Iterator;
import java.util.Map;

public class OtherTool {
    //打印map
    public static String printMapStr(Map<String, String> map){
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = iter.next();
            sb.append(entry.getKey());
            sb.append('=').append('"');
            sb.append(entry.getValue());
            sb.append('"');
            if (iter.hasNext()) {
                sb.append(',').append(' ');
            }
        }
        return sb.toString();
    }

    //分隔符处理
    public static String[] splitStr(String str, String tagStr){
        String[] strList = str.split(tagStr);
        return strList;
    }

    public static void main(String[] args){
        String a = "aa^b&^cc";
        String[] strList = splitStr(a, "&");
        System.out.println(strList[0]);
    }
    //生成随机数
    public static int randomInt(){
        int x = (int) (Math.random() * 2000);
        return x;
    }
}
