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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = InterautoApplication.class)
@WebAppConfiguration
public class TestSetApiCtrl {
    private Logger logger = LogManager.getLogger(TestSetApiCtrl.class);

//    @Autowired
    private MockMvc mockMvc;

//    @Resource
//    private SetApiCtrl setApiCtrl;

//    @Resource
//    private InterCaseDto interCaseDto;
//    @Resource
//    private InterCaseService interCaseService;

    @Before
    public void setUp(){
//        logger.info("starting test...");
        System.out.println("starting test...");
        mockMvc = MockMvcBuilders.standaloneSetup(new SetApiCtrl(), new InterCaseDto(), new InterCaseService()).build();
    }

    @Test
    public void testGetAllApi() throws Exception{
        RequestBuilder req = null;

        ReqPojo reqPojo = new ReqPojo();
        reqPojo.setServerId("GetAllApi");
        reqPojo.setRequestBody(null);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/ApiSetter/GetAllApi.json")
                .contentType(MediaType.APPLICATION_JSON).content(JSONObject.toJSONString(reqPojo)).accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("\"caseId\": 1")));

        MvcResult mvcResult = resultActions.andReturn();
        String res = mvcResult.getResponse().getContentAsString();
//        logger.info(res);
        System.out.println(res);
    }

    @After
    public void tearOff(){
//        logger.info("stoped test");
        System.out.println("stoped test");
    }
}
