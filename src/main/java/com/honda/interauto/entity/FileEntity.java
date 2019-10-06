package com.honda.interauto.entity;

import lombok.Data;

@Data
public class FileEntity {
    private Integer id;
    private String createTime;
    private String creator;
    private String fileName;
    private String filePath;
    private String fileDes;
    private String status;
}
