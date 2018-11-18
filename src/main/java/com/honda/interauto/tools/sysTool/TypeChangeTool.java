package com.honda.interauto.tools.sysTool;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static java.time.LocalDate.now;

public class TypeChangeTool {
    static Logger logger = LogManager.getLogger(TypeChangeTool.class);

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

    public static String stampToDateStr(Long timeLong){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(timeLong);
        String res = simpleDateFormat.format(date);
        return res;
    }

    public static void main(String[] args){
        Long timeLong = new Date().getTime();
        String s = TypeChangeTool.stampToDateStr(timeLong);
        System.out.print(s);
    }

}
