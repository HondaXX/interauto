package com.honda.interauto.controllers;

import com.honda.interauto.dto.InterCaseDto;
import com.honda.interauto.dto.UserDto;
import com.honda.interauto.pojo.BaseConfigs;
import com.honda.interauto.pojo.BaseError;
import com.honda.interauto.pojo.ReqPojo;
import com.honda.interauto.pojo.ResPojo;
import com.honda.interauto.services.InterCaseService;
import com.honda.interauto.services.UserServices;
import com.honda.interauto.tools.httpTool.HttpReqTool;
import com.honda.interauto.tools.sysTool.TypeChangeTool;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/ApiManage")
@Service
public class RunApiCtrl {
    Logger logger = LogManager.getLogger(RunApiCtrl.class);

    @Autowired
    private BaseConfigs baseConfigs;
    @Autowired
    private InterCaseService interCaseService;

//    @Autowired
//    @Qualifier("autoJdbcTemplate")
//    protected JdbcTemplate autoJdbcTemplate;
//    @Autowired
//    @Qualifier("testJdbcTemplate")
//    protected JdbcTemplate testJdbcTemplate;

    @RequestMapping(value = "/RunApi.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResPojo runApi(@RequestBody ReqPojo reqInfo){
        ResPojo res = new ResPojo();

        Integer proId = Integer.parseInt(reqInfo.getRequestBody().get("proId").toString());
        Integer modelId = Integer.parseInt(reqInfo.getRequestBody().get("modelId").toString());
        List<Integer> caseList = (List<Integer>) reqInfo.getRequestBody().get("caseList");

        if (StringUtils.isBlank(proId.toString()) || StringUtils.isBlank(modelId.toString()) || caseList.size() < 0){
            res.setErrorCode(BaseError.PARAM_ERROR);
            res.setErrorDesc(BaseError.PARAM_ERROR_DESC);
            return res;
        }

        List<Map<String, Object>> resList = new ArrayList<Map<String, Object>>();
        for (Integer caseId : caseList){
            InterCaseDto interCaseDto = interCaseService.getInterCaseByCaseId(caseId);
            String resInfo = HttpReqTool.httpReqJson(interCaseDto, null);
            if (null != resInfo){
                Map<String, Object> resMap = TypeChangeTool.strToMap(resInfo);
                resList.add(resMap);
            }else {
                Map<String, Object> resMap = new HashMap<String, Object>();
                resMap.put(caseId.toString(), "用例执行失败");
                resList.add(resMap);
            }
        }
        res.setResCode(BaseError.RESPONSE_OK);
        res.putData("resList", resList);
        return res;
    }

    public static void main(String[] args){
        Integer i = 1;
        System.out.println(i.toString());
    }
}
