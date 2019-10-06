package com.honda.interauto.controllers;

import com.honda.interauto.entity.FileEntity;
import com.honda.interauto.pojo.BaseConfigs;
import com.honda.interauto.pojo.BaseError;
import com.honda.interauto.pojo.ReqPojo;
import com.honda.interauto.pojo.ResPojo;
import com.honda.interauto.services.FileService;
import com.honda.interauto.tools.httpTool.UpDownLoadTool;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@RestController
@RequestMapping(value = "/File")
public class FileCtrl {
    private final Logger logger = LogManager.getLogger(FileCtrl.class);
    @Autowired
    private FileService fileService;
    @Autowired
    private BaseConfigs baseConfigs;

    @PostMapping(value = "/fileList.json", produces = "application/json;charset=UTF-8")
    public ResPojo fIleList(@RequestBody ReqPojo reqPojo){
        String creator = reqPojo.getRequestBody().get("creator").toString();

        List<FileEntity> fileList = fileService.getCreatorFiles(creator);

        ResPojo res = new ResPojo();
        res.setResCode(BaseError.RESPONSE_OK);
        res.put("fileList", fileList);
        return res;
    }

    @GetMapping(value = "/downFile.json")
        public ResponseEntity<byte[]> downFile(@RequestParam("id")Integer id, @RequestParam("creator")String creator, HttpServletRequest request, HttpServletResponse response){
        FileEntity fileEntity = fileService.downFileById(id, creator);

        if (fileEntity == null){
//            ResPojo res = new ResPojo();
//            res.setErrorCode(BaseError.FILE_NOT_FUND);
//            res.setErrorDesc(BaseError.FILE_NOT_FUND_DESC);
            return null;
        }
        String fileFullPath = baseConfigs.getZipFilePath() + File.separator + fileEntity.getFilePath() + File.separator + fileEntity.getFileName();
//        UpDownLoadTool.downLoadFile(fileFullPath, response);
//        ResPojo res = new ResPojo();
//        res.setResCode(BaseError.RESPONSE_OK);
//        return res;
        File file = new File(fileFullPath);
        HttpHeaders headers = new HttpHeaders();

        try {
            String downloadfile =  new String(fileEntity.getFileName().getBytes("UTF-8"),"iso-8859-1");
            // 以下载方式打开文件
            headers.setContentDispositionFormData("attachment", downloadfile);
            // 二进制流
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),headers, HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
