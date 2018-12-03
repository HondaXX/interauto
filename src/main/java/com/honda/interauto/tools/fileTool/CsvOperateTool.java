package com.honda.interauto.tools.fileTool;

import com.opencsv.CSVWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CsvOperateTool {
    private static Logger logger = LogManager.getLogger(CsvOperateTool.class);

    public static Map<Integer, Map<Integer, String>> getCsvLineInfo(String csvPath){
        try{
            Map<Integer, Map<Integer, String>> lineMapWithValue = new HashMap<Integer, Map<Integer, String>>();
            File csvFile = new File(csvPath);
            FileInputStream fis = new FileInputStream(csvFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
            logger.info("get the csv line info:");
            int LineNum = 0;
            String line = new String();
            while ((line = br.readLine()) != null){
                String[] valueOfLine = line.split(",");
                Map<Integer, String> perLineValue = new HashMap<Integer, String>();
                for (int i = 0; i < valueOfLine.length; i++){
                    perLineValue.put(i, valueOfLine[i]);
                }
                lineMapWithValue.put(LineNum, perLineValue);
                LineNum += 1;
            }
            logger.info(lineMapWithValue.toString());
            return lineMapWithValue;
        }catch (Exception e){
            logger.info("read csv file path or file info error");
            e.printStackTrace();
            return null;
        }
    }

    public static void createCsvFile(Map<Integer, Map<Integer, String>> realRowMap, String csvPath){
        Writer writer = null;
        CSVWriter csvWriter = null;
        try {
            File file = new File(csvPath);
            if(file.exists()){
                file.delete();
            }
            file.createNewFile();
            // 手动加上BOM标识
            writer = new FileWriter(csvPath);
            writer.write(new String(new byte[] {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF }));
            csvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER,CSVWriter.DEFAULT_LINE_END);

            for (int i = 0; i < realRowMap.size(); i++) {
                Map<Integer, String> innerMap = realRowMap.get(i);
                String[] lineStrs = {};
                for (int j = 0; j < innerMap.size(); j++) {
                    String strValue = innerMap.get(j);
                    lineStrs = Arrays.copyOf(lineStrs, lineStrs.length + 1);
                    lineStrs[lineStrs.length - 1] = strValue;
                }
                csvWriter.writeNext(lineStrs);
            }
        } catch (Exception ea) {
            logger.info("create csv file error");
            ea.printStackTrace();
        }finally {
            try {
                if(csvWriter != null){
                    csvWriter.close();
                }
                if(writer != null){
                    writer.close();
                }
            }catch (Exception de){
                logger.info("close csvWriter error");
            }
        }
    }
}
