package com.honda.interauto.services;

import com.honda.interauto.dao.auto.FileDao;
import com.honda.interauto.entity.FileEntity;
import com.honda.interauto.tools.sysTool.TypeChangeTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;

@Service
public class FileService {
    @Autowired
    private FileDao fileDao;

    public List<FileEntity> getCreatorFiles(String creator){
        Date date = new Date();
        String createTime = TypeChangeTool.addOrSubDay(-3, null);
        return fileDao.getCreatorFiles(creator, createTime);
    }

    public FileEntity downFileById(Integer id, String creator){
        Date date = new Date();
        String createTime = TypeChangeTool.addOrSubDay(-3, null);
        FileEntity fileEntity = fileDao.downFileById(id, creator, createTime);
        if (fileEntity != null){
            if (creator.equals(fileEntity.getCreator())){
                return fileEntity;
            }
            return null;
        }
        return null;
    }
}
