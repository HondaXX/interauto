package com.honda.interauto.tools.fileTool;

import com.honda.interauto.pojo.ResultMerged;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ExcelOperateTool {
    private static Logger logger = LogManager.getLogger(ExcelOperateTool.class);

    public static Map<String, Sheet> getExcelSheet(String filePath){
        Map<String, Sheet> excelSheetMap = new HashMap<String, Sheet>();
        try{
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            Workbook workbook  = WorkbookFactory.create(fis);
            int sheetNum = workbook.getNumberOfSheets();
            logger.info("get " + sheetNum + " sheets in excel: " + file.getName());
            for (int i = 0; i < sheetNum; i++){
                Sheet sheet = workbook.getSheetAt(i);
                String sheetName = sheet.getSheetName();
                excelSheetMap.put(sheetName, sheet);
            }
            return excelSheetMap;
        }catch (Exception e){
            e.printStackTrace();
            logger.info("file not exist or error path: " + filePath);
            return null;
        }
    }

    //生成excel
    public static String createExcel(Map<Integer, Object> objectMap, String newExcelPath){
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("sheetName");
        //设置固定的第一行
        Row firstRow = sheet.createRow((short ) 0);
        //那行的第几列开始
        Cell firstRowCell = null;
        firstRowCell.setCellType(CellType.STRING);

        firstRowCell = firstRow.createCell((short) 0);
        firstRowCell.setCellValue("用例目录");
        firstRowCell = firstRow.createCell((short) 1);
        firstRowCell.setCellValue("用例名称");
        firstRowCell = firstRow.createCell((short) 2);
        firstRowCell.setCellValue("需求ID");
        firstRowCell = firstRow.createCell((short) 3);
        firstRowCell.setCellValue("前置条件");
        firstRowCell = firstRow.createCell((short) 4);
        firstRowCell.setCellValue("用例步骤");
        firstRowCell = firstRow.createCell((short) 5);
        firstRowCell.setCellValue("预期结果");


        for (int i = 1; i <= objectMap.size(); i++ ){
            Row rowLine = sheet.createRow((short) i);
            Cell cellRow = rowLine.createCell((short) 0);
            for (int j = 0; j <= 5; j ++){
                cellRow.setCellType(CellType.STRING);
                cellRow = rowLine.createCell((short) j);
                if (j == 0){
                    cellRow.setCellValue(objectMap.get(i - 1).toString());
                }else if (j == 1){
                    cellRow.setCellValue(objectMap.get(i - 1).toString());
                }else if (j == 2){
                    cellRow.setCellValue(objectMap.get(i - 1).toString());
                }else if (j == 5){
                    cellRow.setCellValue(objectMap.get(i - 1).toString());
                }else {
                    continue;
                }
            }
//            logger.debug("====>" + i + " row set value complete");
        }
        logger.info("complete set sheet value");
        try{
            FileOutputStream fOut = new FileOutputStream(newExcelPath);
            workbook.write(fOut);
            fOut.flush();
            fOut.close();
            logger.info("new file path: " + newExcelPath);
            return "0";
        }catch (Exception e){
            e.printStackTrace();
            logger.info("create excel error");
            return null;
        }
    }

    //判断单元格并获取值
    public static ResultMerged isMergedRegion(Sheet sheet, int row , int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if(row >= firstRow && row <= lastRow){
                if(column >= firstColumn && column <= lastColumn){
                    Row mergedRow = sheet.getRow(firstRow);
                    String mergedValue = mergedRow.getCell(firstColumn).toString();
                    return new ResultMerged(true, firstRow, lastRow, firstColumn, lastColumn, mergedValue);
                }
            }
        }
        return new ResultMerged(false,0,0,0,0, null);
    }
    //拆分单元格
    public static void unMergedCell(HSSFSheet sheet, int rowIndex, int colIndex) {
        int num = sheet.getNumMergedRegions();
        for(int i = 0; i < num; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            if(range.getFirstColumn() == colIndex && range.getFirstRow() == rowIndex) {
                sheet.removeMergedRegion(i) ;
                break;
            }
        }
    }
}
