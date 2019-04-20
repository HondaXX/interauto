package com.honda.interauto.tools.sysTool;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.time.LocalDate.now;

public class TypeChangeTool {
    static Logger logger = LogManager.getLogger(TypeChangeTool.class);
    //map转object
    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass){
        if (map != null){
            try{
                Object obj = beanClass.newInstance();
                BeanUtils.populate(obj, map);
                return obj;
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }else {
            logger.error("map change to bean: " + beanClass + " is null");
            return null;
        }
    }
    //str转map
    public static Map<String, Object> strToMap(String str){
        Gson gs = new Gson();
        Map<String, Object> returnMap = new HashMap<String, Object>();
        try{
            returnMap = gs.fromJson(str, new TypeToken<Map<String, Object>>() {}.getType());
            return returnMap;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    //object转json(map/list...)
    public static String mapToJson(Map<String, Object> map){
        Gson gs = new Gson();
        String jsonStr = gs.toJson(map);
        return jsonStr;
        //JSONArray.parseArray(JSON.toJSONString(map))).toJSONString();
    }
    //double转string
    public static String doubleToStr(double value){
        int i = (int) value;
        String doubleStr = String.valueOf(i);
        return doubleStr;
    }
    //时间相关
    public static String stampToDateStr(Long timeLong){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(timeLong);
        String res = simpleDateFormat.format(date);
        return res;
    }

    public static String timeToStringWithDayO(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String str = format.format(date);
        return str;
    }

    public static String timeToStringWithDayT(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String str = format.format(date);
        return str;
    }

    public static String changeTimeWithSecond(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String str = format.format(date);
        return str;
    }

    public static String changeTimeJustSecond(Date date){
        SimpleDateFormat format = new SimpleDateFormat("HHmmss");
        String str = format.format(date);
        return str;
    }

    public static void sysSleep(int sleepTime){
        try {
            logger.debug("sleep...{} seconds", sleepTime);
            Thread.sleep(sleepTime * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        Long timeLong = new Date().getTime();
        String s = TypeChangeTool.stampToDateStr(timeLong);
        System.out.print(s);
    }

}
