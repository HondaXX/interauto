package com.honda.interauto.tools.fileTool;

import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TxTOperateTool {
    private static final Logger logger = LogManager.getLogger(TxTOperateTool.class);

    public static Map<Integer, String> getTxtLines(String filePath){
        Map<Integer, String> lineMap = new HashMap<Integer, String>();
        try{
            File file = new File(filePath);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
            BufferedReader br = new BufferedReader(reader);

            String line = "";
            int i = 1;

            line = br.readLine();
            while (line != null) {
                lineMap.put(i, line);
                line = br.readLine(); // 一次读入一行数据
                i++;
            }
            return lineMap;
        }catch (Exception e){
            e.printStackTrace();
            logger.info("file not exist or error path: " + filePath);
            return null;
        }
    }

    public static void writeTxt(Map<Integer, String> lineMap, String dirPath, String fileName){
        try {
            File writeName = new File( dirPath + File.separator + fileName);  //"D:\\work\\idea\\temp"
            writeName.createNewFile();
            FileWriter writer = new FileWriter(writeName);
            BufferedWriter out = new BufferedWriter(writer);
            for (int i = 1; i <= lineMap.size(); i++){
                out.write(lineMap.get(i));
            }
            out.flush();
            if (writer != null) writer.close();
            if (out != null) out.close();
        } catch (IOException e) {
            logger.info("写入txt文件时出错:{}", fileName);
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        Map<Integer, String> lineMap = getTxtLines("D:\\work\\idea\\temp\\aaa.txt");
        System.out.println(JSONObject.toJSON(lineMap));
        String[] strArray = {};

        for (int i = 1; i <= lineMap.size(); i++){
            String lineStr = lineMap.get(i);
            for (int j = 0; j < lineStr.length(); j++){
                String subStr = String.valueOf(lineStr.charAt(j));
                if (!Arrays.asList(strArray).contains(subStr)){
                    strArray = Arrays.copyOf(strArray, strArray.length + 1);
                    strArray[strArray.length - 1] = subStr;
                }
            }
        }
        System.out.println(Arrays.deepToString(strArray));
        String resStr = Arrays.asList(strArray).stream().collect(Collectors.joining());
        Map<Integer, String> newTxtMap = new HashMap<Integer, String>();
        newTxtMap.put(1, resStr); //Arrays.deepToString(strArray)
        writeTxt(newTxtMap, "D:\\work\\idea\\temp", "newFile.txt");
    }
}
