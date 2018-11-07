package com.honda.interauto.tools.fileTool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileZipTool {
    public static void fileToZip(List<String> filePathList, String zipFilePath){
        ZipOutputStream zos = null;
        try{
            byte[] buffer = new byte[1024];
            FileOutputStream fos = new FileOutputStream(zipFilePath);
            zos = new ZipOutputStream(fos);
            for (String filePath : filePathList) {
                try {
                    File file = new File(filePath);
                    FileInputStream fis = new FileInputStream(file);
                    zos.putNextEntry(new ZipEntry(file.getName()));
                    int len;
                    //读入需要下载的文件的内容，打包到zip文件
                    while ((len = fis.read(buffer)) != -1) {
                        zos.write(buffer, 0, len);
                    }
                    zos.flush();
                    zos.closeEntry();
                    fis.close();
                } catch (Exception e) {
                    System.out.println("get file error");
                    e.printStackTrace();
                }
            }
            zos.close();
        }catch (Exception e){
            System.out.println("open zip file error");
            e.printStackTrace();
        }
    }
}
