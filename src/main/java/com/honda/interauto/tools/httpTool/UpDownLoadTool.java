package com.honda.interauto.tools.httpTool;

import com.honda.interauto.tools.sysTool.TypeChangeTool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

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

        InputStream is = null;
        response.addHeader("pragma", "NO-cache");
        response.addHeader("Cache-Control","no-cache");
        response.addDateHeader("Expries",0);
        response.setContentType("application/vnd.ms-excel;charset=utf-8");

        String fileName = file.getName();
        response.addHeader("Content-Disposition","attachment;filename=" + fileName);

        OutputStream out = null;
        try{
            is = new FileInputStream(file);
            out = response.getOutputStream();
            int length = 0;
            byte buffer[] = new byte[1024];
            while((length = is.read(buffer)) != -1){
                out.write(buffer, 0, length);
            }
            if (is != null){
                is.close();
            }
            if (out != null){
                out.close();
            }
        } catch (Exception e) {
            logger.error("get file error with path: " + filePath);
            e.printStackTrace();
        }
    }
}
