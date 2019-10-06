package com.honda.interauto.tools.httpTool;

import com.honda.interauto.tools.sysTool.TypeChangeTool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UpDownLoadTool {
    private static Logger logger = LogManager.getLogger(UpDownLoadTool.class);

    public static String uploadFile(String path, MultipartFile file){
        Map<String, Object> resMap = new HashMap<String, Object>();
        if(!file.isEmpty()){
            try{
                Date date = new Date();
                File dir = new File(path + File.separator + TypeChangeTool.timeToStringWithDayO(date));
                if (!dir.exists()){
                    dir.mkdirs();
                }
                //保存文件到临时目录
                File serverFile = new File(dir.getAbsolutePath() + File.separator + file.getOriginalFilename());
                file.transferTo(serverFile);
                logger.info( "successfully uploaded file; " + file.getOriginalFilename());
                String filePath = dir.getAbsolutePath() + file.getOriginalFilename();
                return filePath;
            }catch (Exception e) {
                logger.debug( "failed to upload " + file.getOriginalFilename());
                e.printStackTrace();
                return null;
            }
        } else{
            logger.debug( "failed to upload empty file" +  file.getOriginalFilename());
            return null;
        }
    }

    public static void downLoadFile(String filePath, HttpServletResponse response){
        File file = new File(filePath);

        response.reset();
        response.addHeader("pragma", "NO-cache");
        response.addHeader("Cache-Control", "must-revalidate, no-transform");
        response.addHeader("Content-Type", "application/octet-stream; charset=utf-8");
        response.setDateHeader("Expries", 0);
//        response.setContentType("application/octet-stream; charset=ISO-8859-1");
        response.setContentType("application/force-download; charset=ISO-8859-1");
        response.setCharacterEncoding("UTF-8");

        String fileName = file.getName();
//        response.setHeader("Content-Disposition", "attachment:filename=" + fileName);
        response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
//        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        InputStream is = null;
        OutputStream os = null;
        try{
            is = new FileInputStream(file);
            os = response.getOutputStream();
            int length = 0;
            byte[] buffer = new byte[1024];
            while ((length = is.read(buffer)) != -1){
                os.write(buffer, 0, length);
            }
            if (is != null){
                is.close();
            }
            if (os != null){
                response.flushBuffer();
//                os.flush();
                os.close();
            }
        }catch (Exception e){
            logger.error("file tranfer error: {}", file.getName());
            e.printStackTrace();
        }

    }

    public ResponseEntity<InputStreamResource> downLoadFileTwo(String fileFullPath){
        FileSystemResource fileRs = new FileSystemResource(fileFullPath);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", "attachment; filename=" + fileRs.getFilename());
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        try {
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentLength(fileRs.contentLength())
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(new InputStreamResource(fileRs.getInputStream()));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
