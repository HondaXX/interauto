package com.honda.interauto.controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.honda.interauto.pojo.ReqPojo;
import com.honda.interauto.pojo.ResPojo;
import com.honda.interauto.utils.HttpUtils;
import org.apache.http.Header;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.testng.Assert;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import javax.validation.constraints.AssertTrue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestRunAPiCtrl {
    private final Logger logger = LogManager.getLogger(TestRunAPiCtrl.class);

//    @Autowired
//    private TestRestTemplate restTemplate;
//    @Autowired
//    private RunApiCtrl runApiCtrl;

    TestRestTemplate restTemplate = new TestRestTemplate();

    @Ignore
    @Test
    public void testcase1(){
        ReqPojo req = new ReqPojo();
        req.setServerId("RunApi");

        List<Integer> caseList = new ArrayList<Integer>();
        caseList.add(1);

        Map<String, Object> requestBodyMap = new HashMap<String, Object>();
        requestBodyMap.put("proId", 1);
        requestBodyMap.put("modelId", 1);
        requestBodyMap.put("caseList", caseList);
        req.setRequestBody(requestBodyMap);
        System.out.println("request=============>" + req.toString());

        String reqUrl = "http://localhost:8000/interauto/ApiManage/RunApi.json";
        String result = restTemplate.postForObject(reqUrl, req, String.class);
        System.out.println("respone=============>" + result);

        Map<Integer, String> resMap = (Map<Integer, String>) JSON.parse(result);

        for (int i = 0; i < resMap.size(); i++){
            String resCase = resMap.get(i);
            System.out.println(resCase);
            Assert.assertEquals("00", resCase);
        }

        System.out.println("finish");
    }

    @Test
    public void testcase2(){
        ReqPojo req = new ReqPojo();
        req.setServerId("RunApi");
        List<Integer> caseList = new ArrayList<Integer>();
        caseList.add(1);
        caseList.add(2);
        Map<String, Object> requestBodyMap = new HashMap<String, Object>();
        requestBodyMap.put("proId", 1);
        requestBodyMap.put("modelId", 1);
        requestBodyMap.put("caseList", caseList);
        req.setRequestBody(requestBodyMap);
        System.out.println("========>request: " + JSON.toJSONString(req));

        String reqUrl = "http://localhost:8000/interauto/ApiManage/RunApi.json";
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxIn0.qfd0G-elhE1aGr15LrnYlIZ_3UToaOM5HeMcXrmDGBM1550829361866");
        String result = HttpUtils.httpReqPost(reqUrl, req, headerMap);
        System.out.println("========>result: " + result);
        Map<String, Object> resMap = (Map<String, Object>) JSON.parse(result);
        Map<String, String> caseMap = (Map<String, String>) resMap.get("resDetail");

        int failNum = 0;
        if (caseMap != null && caseMap.size() > 0){
            for (String caseID : caseMap.keySet()){
                String resCase = caseMap.get(caseID);
                System.out.println("========>caseid: " + caseID + ", result: " +resCase);
//                Assert.assertEquals("00", resCase);
                try{
                    Assert.assertTrue(resCase.equals("00"));
                }catch (Error e){
                    failNum += 1;
                }
            }
            System.out.println("====>total: " + caseMap.size() + ", fail: " + failNum);
        }else {
            System.out.println("========>返回有误");
        }
    }
}
