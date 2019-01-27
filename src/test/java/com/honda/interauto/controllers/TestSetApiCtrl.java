package com.honda.interauto.controllers;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.honda.interauto.InterautoApplication;
import com.honda.interauto.InterautoApplicationTests;
import com.honda.interauto.dto.InterCaseDto;
import com.honda.interauto.pojo.ReqPojo;
import com.honda.interauto.pojo.ResPojo;
import com.honda.interauto.services.InterCaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
//@PropertySource("classpath:application.yml")
//当ctrl没装配其他bean使用
//@WebMvcTest(controllers = SetApiCtrl.class)
//@WebAppConfiguration
//当ctrl装配其他bean时使用
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {InterautoApplication.class})
@AutoConfigureMockMvc
public class TestSetApiCtrl {
    private Logger logger = LogManager.getLogger(TestSetApiCtrl.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    public TestRestTemplate restTemplate;

    @Autowired
    private SetApiCtrl setApiCtrl;

    Map<String, Object> delReqMap = new HashMap<String, Object>();
    Map<String, Object> delReqBody = new HashMap<String, Object>();

    @Before
    public void setUp(){
        logger.info("starting test...");
        mockMvc = MockMvcBuilders.standaloneSetup(new SetApiCtrl(), new InterCaseDto(), new InterCaseService()).build();

        delReqMap.put("serverId", "DelApi");
        delReqBody.put("caseId", 1);
        delReqMap.put("requestBody", delReqBody);

    }

//    @Ignore
    @Test//mcok方式
    public void testSetApi200() throws Exception{
        Map<String, Object> reqMap = delReqMap;

        MockHttpServletRequestBuilder request = post("/ApiSetter/DelApi.json");
        request.header("Content-Type", "application/json");
        request.content(JSONObject.toJSONString(reqMap));

        ResultActions resultActions = mockMvc.perform(request)  //json请求用法
//                .param("name", "honda"))  //param用法
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("success")));
        MvcResult mvcResult = resultActions.andReturn();
        String resStr = mvcResult.getResponse().getContentAsString();
        Map<String, Object> resMap = (Map) JSONObject.parse(resStr);
        Assert.assertEquals("0", resMap.get("resultCode"));
    }

    @Ignore
    @Test//ctrl.方法
    public void testSetApi201(){
        Map<String, Object> reqMap = delReqMap;
        ReqPojo reqPojo = new ReqPojo();
        reqPojo.setServerId(delReqMap.get("serverId").toString());
        reqPojo.setRequestBody((Map<String, Object>) delReqMap.get("requestBody"));
        logger.info("request=============>" + reqMap.toString());
        String resStr = (setApiCtrl.delApi(reqPojo)).toString();
        logger.info("respone=============>" + JSONObject.toJSONString(resStr));
        Map<String, Object> resMap = (Map<String, Object>) JSONObject.parse(resStr);
        Assert.assertEquals("0", resMap.get("resultCode"));
        Assert.assertThat(resMap.get("data").toString(), containsString("1"));
    }


    @Ignore
    @Test   //restTemplate
    public void testRiskAdvice3(){
        Map<String, Object> reqMap = delReqMap;
        String reqUrl = "/ApiSetter/DelApi.json";
        logger.info("reqMap=============>" + reqMap.toString());
        String result = restTemplate.postForObject(reqUrl, reqMap, String.class);
        logger.info("respone=============>" + result);
        Assert.assertThat(result, containsString("res"));
    }

    @After
    public void tearOff(){
        logger.info("stoped test!!!");
    }
}
