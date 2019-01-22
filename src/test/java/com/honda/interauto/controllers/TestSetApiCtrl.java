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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
//@PropertySource("classpath:application.yml")
//当ctrl没装配其他bean使用
//@WebMvcTest(controllers = ExcelCtrl.class)
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

    @Before
    public void setUp(){
        logger.info("starting test...");
        mockMvc = MockMvcBuilders.standaloneSetup(new SetApiCtrl(), new InterCaseDto(), new InterCaseService()).build();
    }

    @Test//mcok方式
    public void testJunitC201() throws Exception{
//        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/Excel/testJunit.json")
////                .contentType(MediaType.ALL).content(JSONObject.toJSONString(reqParams)).accept(MediaType.APPLICATION_JSON))  //json请求用法
//                .param("name", "honda"))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk())
//                .andExpect(content().string(containsString("thanks")));
//        MvcResult mvcResult = resultActions.andReturn();
//        String res = mvcResult.getResponse().getContentAsString();
//        logger.info(res);
    }

    @Test//ctrl.方法
    public void testOtherCtrl001() throws Exception{
//        Map<String, Object> reqMap = new HashMap<>();
//        System.out.println("request=============>" + reqMap.toString());
//        String resStr = otherController.addServiceLimit(reqMap.get("org").toString(), reqMap.get("projectId").toString(), Integer.parseInt(reqMap.get("dailyLimit").toString()), Integer.parseInt(reqMap.get("totalLimit").toString()));
//        System.out.println("respone=============>" + JSONObject.toJSONString(resStr));
//        Map<String, Object> resMap = (Map<String, Object>) JSONObject.parse(resStr);
//        Assert.assertEquals("0", resMap.get("code"));
//        Assert.assertThat(resMap.get("message").toString(), containsString("成功新增一条数据"));
    }


    @Ignore
    @Test   //restTemplate
    public void testRiskAdvice3(){
//        MultiValueMap<String, String> reqMap = reqMapTemp;
//        reqMap.set("entNo", null);
//        String reqUrl = "/weshareholding/zx-get-result";
//        System.out.println("reqMap=============>" + reqMap.toString());
//        String result = restTemplate.postForObject(reqUrl, reqMap, String.class);
//        System.out.println("respone=========>" + result);
//        Assert.assertThat(result, containsString("100001"));
    }

    @After
    public void tearOff(){
        logger.info("stoped test");
    }
}
