package com.honda.interauto.tools.httpTool;

import com.alibaba.fastjson.JSON;
import com.honda.interauto.dto.InterCaseDto;
import com.honda.interauto.tools.sysTool.TypeChangeTool;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.*;

public class HttpReqTool {
    private static Logger logger = LogManager.getLogger(HttpReqTool.class);

    public static StringEntity setEntity(String reqStr) {
        StringEntity se = null;
        try {
            se = new StringEntity(reqStr);
//            se.setContentEncoding("UTF-8");
            se.setContentType("application/json");
            return se;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String httpReqJson(InterCaseDto interCaseDto, String cookie){
        Integer caseID = interCaseDto.getCaseId();
        Map<String, Object> reqMap = (Map) JSON.parse(interCaseDto.getRequestJson());
        String reqUrl = interCaseDto.getDNS() + interCaseDto.getInterUrl();

        if (interCaseDto.getRequestMethod().equals("1")){
            CloseableHttpClient client = HttpClients.createDefault();
            try {
                URIBuilder uri = new URIBuilder(reqUrl);
                HttpGet hg = new HttpGet(uri.build());
                if (null != cookie){
                    hg.setHeader("Cookie", cookie);
                }
                logger.info("call request url: " + reqUrl + "\n" + "call request response: ");
                CloseableHttpResponse response = client.execute(hg);
                return returnHttpRes(response, reqMap);
            }catch (Exception e){
                logger.info("request error");
                e.printStackTrace();
                return null;
            }
        }else if (interCaseDto.getRequestMethod().equals("0")){
            CloseableHttpClient client = HttpClients.createDefault();
            try{
                HttpPost hp = new HttpPost(reqUrl);
                if (null != cookie){
                    hp.setHeader("Cookie", cookie);
                }
                StringEntity entity = setEntity(interCaseDto.getRequestJson());
                hp.setEntity(entity);
                hp.setHeader("Content-Type", "application/json");
                logger.debug("call request url: " + reqUrl);
                logger.debug("call request param: " + reqMap.toString());
                CloseableHttpResponse response = client.execute(hp);
                return returnHttpRes(response, reqMap);
            }catch (Exception e){
                logger.info("request error" + interCaseDto.getCaseId());
                e.printStackTrace();
                return null;
            }
        }else {
            logger.info("unknow request method" + interCaseDto.getCaseId());
            return null;
        }
    }

    //文件请求
    public static String httpReqFile(File file, InterCaseDto interCaseDto){
        logger.info("========>start case with id: " + interCaseDto.getCaseId());
        String reqUrl = interCaseDto.getDNS() + interCaseDto.getInterUrl();

        CloseableHttpClient client = HttpClients.createDefault();

        try{
            HttpPost hp = new HttpPost(reqUrl);
            logger.info("upload file url: " + reqUrl + "\n" + "upload file response: ");
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.addBinaryBody("file", file, ContentType.create("multipart/form-data"), file.getName());
            HttpEntity entity = builder.build();
            hp.setEntity(entity);
            CloseableHttpResponse response = client.execute(hp);
            return returnHttpRes(response, null);
        }catch (Exception e){
            logger.info("upload file error caseid: {}" + interCaseDto.getCaseId());
            e.printStackTrace();
            return null;
        }
    }

    //表单请求
    public static String httpReqForm(InterCaseDto interCaseDto){
        logger.info("========>start case with id: " + interCaseDto.getCaseId());
        Map<String, Object> reqMap = TypeChangeTool.strToMap(interCaseDto.getRequestJson());
        String reqUrl = interCaseDto.getDNS() + interCaseDto.getInterUrl();

        CloseableHttpClient client = HttpClients.createDefault();
        try{
            HttpPost hp = new HttpPost(reqUrl);
            logger.info("call request url: " + reqUrl + "\n" + "call request param: " + reqMap.toString() + "\n" + "call request response: ");
            List<NameValuePair> form = getReqParamList(reqMap);
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form, Charset.forName("UTF-8"));
            hp.setEntity(entity);
            CloseableHttpResponse response = client.execute(hp);
            return returnHttpRes(response, reqMap);
        }catch (Exception e){
            logger.info("request error caseid {}" + interCaseDto.getCaseId());
            e.printStackTrace();
            return null;
        }
    }

    public static Map<String, String> getCookies(InterCaseDto interCaseDto){
        logger.info("========>start case with id: " + interCaseDto.getCaseId());
        Map<String, Object> reqMap = TypeChangeTool.strToMap(interCaseDto.getRequestJson());
        String reqUrl = interCaseDto.getDNS() + interCaseDto.getInterUrl();

        List<NameValuePair> list = getReqParamList(reqMap);
        CloseableHttpClient client = HttpClients.createDefault();
        CookieStore cookieStore = new BasicCookieStore();
        client = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        HttpPost hp = new HttpPost(reqUrl);
        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
            hp.setEntity(entity);
            CloseableHttpResponse response = client.execute(hp);
        }catch (Exception e){
            logger.error("run case error id: " + interCaseDto.getCaseId());
            e.printStackTrace();
        }
        //根据实际情况自定义cookies值
        String JSESSIONID = null;
        String cookie_qluin = null;
        String cookie_qlskey = null;
        List<Cookie> cookieList = cookieStore.getCookies();
        Map<String, String> cacheMap = new HashMap<String, String>();
        for (int i = 0; i < cookieList.size(); i++) {
            if (cookieList.get(i).getName().equals("JSESSIONID")) {
                JSESSIONID = cookieList.get(i).getValue();
                cacheMap.put("JSESSIONID", JSESSIONID);
            }
            if (cookieList.get(i).getName().equals("qluin")) {
                cookie_qluin= cookieList.get(i).getValue();
                cacheMap.put("qluin", cookie_qluin);
            }
            if (cookieList.get(i).getName().equals("qlskey")) {
                cookie_qlskey= cookieList.get(i).getValue();
                cacheMap.put("qlskey", cookie_qlskey);
            }
        }
        return cacheMap;
    }

    public static List<NameValuePair> getReqParamList(Map<String, Object> reqMap){
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        Iterator<Map.Entry<String, Object>> iterator = reqMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> elem = iterator.next();
            String key = elem.getKey();
            String value = elem.getValue().toString();
            list.add(new BasicNameValuePair(key,value));
        }
        return list;
    }

    public static String returnHttpRes(CloseableHttpResponse response, Map<String, Object> reqMap){
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200){
            try {
                String resInfo = EntityUtils.toString(response.getEntity(), "UTF-8");
                logger.debug("========>case response: " + resInfo);
                return resInfo;
            }catch (Exception e){
                logger.error("get response Entity error!");
                e.printStackTrace();
                return null;
            }
        }else if (statusCode == 302){
            //若登陆重定向处理
            Header header = response.getFirstHeader("location");
            String newUrl = header.getValue();
            logger.info("redict url: " + newUrl);
            HttpPost hpRed = new HttpPost(newUrl);
            hpRed.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded"));
            hpRed.setHeader(new BasicHeader("Accept", "text/plain;charset=utf-8"));
            try{
                StringEntity entityRed = new StringEntity(reqMap.toString());
                entityRed.setContentType("*/*");
                hpRed.setEntity(entityRed);
                CloseableHttpClient clientRed = HttpClients.createDefault();
                HttpResponse responseRed = clientRed.execute(hpRed);
                if (responseRed.getStatusLine().getStatusCode() == 200){
                    String resInfo = EntityUtils.toString(responseRed.getEntity(), "UTF8");
                    logger.info("========>case response: " + resInfo);
                    return resInfo;
                }else {
                    logger.error("get response Entity error!");
                    return null;
                }
            }catch (Exception e){
                logger.error("set request Entity error!");
                e.printStackTrace();
                return null;
            }
        }else if (statusCode == 406){
            //返回参数不是json处理
            try {
                String resInfo = EntityUtils.toString(response.getEntity(), "UTF8");
                logger.info("========>case response: " + resInfo);
                return resInfo;
            }catch (Exception e){
                logger.error("get response Entity error!");
                e.printStackTrace();
                return null;
            }
        }else {
            logger.info("statusCode: " + statusCode + ", responce error");
            return null;
        }
    }
}
