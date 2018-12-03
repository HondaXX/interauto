package com.honda.interauto.tools.fileTool;


import com.honda.interauto.tools.sysTool.TypeChangeTool;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReqFileTool {
    static Logger logger = LogManager.getLogger(ReqFileTool.class);
    //文件上传
    public static String uploadFile(String path, MultipartFile file){
        Map<String, Object> resMap = new HashMap<String, Object>();
        if(!file.isEmpty()){
            try{
                Date date = new Date();
                File dir = new File(path + File.separator + TypeChangeTool.timeToStringWithDayO(date));
                if (!dir.exists()){
                    dir.mkdirs();
                    logger.info("the path dir isn't exist: " + dir);
                }
                //保存文件到临时目录
                File serverFile = new File(dir.getAbsolutePath() + File.separator + file.getOriginalFilename());
                file.transferTo(serverFile);
                logger.info( "successfully uploaded file: " + file.getOriginalFilename());
                return serverFile.getAbsolutePath();
            }catch (Exception e) {
                logger.error( "failed to upload " + file.getOriginalFilename());
                e.printStackTrace();
                return null;
            }
        } else{
            logger.info( "failed to upload " +  file.getOriginalFilename() + " because the file is empty.");
            return null;
        }
    }

    //文件返回下载
    public static ResponseEntity<byte[]> downloadFile(String filePath) throws IOException {
        //下载文件路径
        File file = new File(filePath);
        HttpHeaders headers = new HttpHeaders();
        //下载显示的文件名，解决中文名称乱码问题
//        String downloadFielName = new String(filename.getBytes("UTF-8"),"iso-8859-1");
        //通知浏览器以attachment（下载方式）打开
        headers.setContentDispositionFormData("attachment", file.getName());
        //application/octet-stream ： 二进制流数据（最常见的文件下载）。
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
    }
}
