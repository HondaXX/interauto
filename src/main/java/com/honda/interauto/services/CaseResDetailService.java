package com.honda.interauto.services;

import com.honda.interauto.dao.auto.CaseResDetailDao;
import com.honda.interauto.dto.CaseResDto;
import com.honda.interauto.entity.CaseResDetailEntity;
import com.honda.interauto.entity.UserEntity;
import com.honda.interauto.tools.httpTool.RequestTool;
import com.honda.interauto.tools.sysTool.SysInitData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class CaseResDetailService {
    @Autowired
    private CaseResDetailDao caseResDetailDao;

    public Integer saveCaseRes(CaseResDetailEntity caseResDetailEntity){
        return caseResDetailDao.saveCaseRes(caseResDetailEntity);
    }

    public List<CaseResDto> getCaseResDetail(String queryType, String runTagId, Integer pageNum, Integer pageSize, String caseRes,
                                             Integer caseId, String caseAim, String interUrl,
                                             Integer evenId, String evenAim, String evenName, Integer appId){
        if (queryType.equals("pro")){
            return caseResDetailDao.getProResDetail(runTagId, pageNum, pageSize, caseRes, caseId, caseAim, interUrl);
        }
        if (queryType.equals("app")){
            return caseResDetailDao.getAppResDetail(runTagId, pageNum, pageSize, caseRes, evenId, evenAim, evenName, appId);
        }
        return null;
    }

    public Integer getTagResCount(String runTagId){
        return caseResDetailDao.getTagResCount(runTagId);
    }


    //跑用例时独立id方法
    public String runApiTagId(Date date){
        HttpServletRequest request = RequestTool.getCurrentRequest();
        String tokenStr = request.getHeader("token");
        UserEntity userEntity = (UserEntity) SysInitData.ru.get(tokenStr);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String timeStr = format.format(date);
        String tagIdStr = userEntity.getName() + "-" + timeStr;
        return tagIdStr;
    }

}
