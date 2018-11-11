package com.honda.interauto.tools.sysTool;

import com.honda.interauto.dto.ServerDto;
import org.apache.commons.beanutils.BeanUtils;

import java.util.Map;

public class TypeChangeTool {
    public static void main(String args[]) throws Exception{
        Class cla = Class.forName("AopTool");
        ServerDto sd = (ServerDto) cla.newInstance();
        System.out.print(sd.getClass().toString());
    }

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
            return null;
        }
    }

}
