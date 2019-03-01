package com.honda.interauto.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.*;


public class HttpUtils {

    /**单位毫秒*/
    private static final int DEFAULT_TIMEOUT = 600000;
    private static final String SEPARATOR = "=";
    private static final String SEPARATOR_2 = "&";
    private static final int RETRY_TIME = 3;

    private static final Logger log = LogManager.getLogger(HttpUtils.class);

    /**
     * post Object
     */
    public static String httpReqPost(String reqUrl, Object req, Map<String, String> cookieMap){
        CloseableHttpClient client = HttpClients.createDefault();
        try{
            HttpPost hp = new HttpPost(reqUrl);
            if (null != cookieMap || cookieMap.size() > 0){
                for (String keyStr : cookieMap.keySet()){
                    hp.setHeader(keyStr, cookieMap.get(keyStr));
                }
            }
            StringEntity entity = setEntity(JSON.toJSONString(req));
            hp.setEntity(entity);
            hp.setHeader("Content-Type", "application/json");
            CloseableHttpResponse response = client.execute(hp);
            String resInfo = EntityUtils.toString(response.getEntity(), "UTF-8");
            return resInfo;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static StringEntity setEntity(String reqStr) {
        StringEntity se = null;
        try {
            se = new StringEntity(reqStr);
            se.setContentType("application/json");
            return se;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 【http post】 请求 @param url 请求连接 @param params 请求参数 @param headers
     * 头部信息 @throws
     */
    public static String post(String url, Map<String, String> params, Map<String, String> headers, int timeOut) {

        RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(timeOut)
                .setConnectTimeout(timeOut).setConnectionRequestTimeout(timeOut)
                .build();

        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultRequestConfig(defaultRequestConfig).setRetryHandler((exception, executionCount, context) -> {
                    if (executionCount > RETRY_TIME) {
                        log.warn("Maximum tries reached for client http pool ");
                        return false;
                    }
                    if (exception instanceof NoHttpResponseException
                            || exception instanceof ConnectTimeoutException
                            ) {
                        log.warn("NoHttpResponseException on " + executionCount + " call");
                        return true;
                    }
                    return false;
                }).build();
        try {
            HttpPost httppost = new HttpPost(url);
            List<NameValuePair> nvps = new ArrayList<>();
            mapToString(params, httppost, nvps);
            if (headers != null && headers.size() > 0) {
                for (String key : headers.keySet()) {
                    httppost.setHeader(key, String.valueOf(headers.get(key)));
                }
            }

            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return EntityUtils.toString(entity);
                }
            } finally {
                httpclient.close();
                response.close();
            }
        } catch (UnsupportedEncodingException e) {
            log.error("UnsupportedEncodingException异常： " + url, e);
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            log.error("ClientProtocol异常： " + url, e);
            e.printStackTrace();
        } catch (IOException e) {
            log.error("IO异常： " + url, e);
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 【http post】 请求 @param url 请求连接 @param params 请求参数 @param headers
     * 头部信息 @throws
     */
    public static String postIgnoreVerifySSL(String url, Map<String,String> params){

        //采用绕过验证的方式处理https请求
        SSLContext sslcontext = createIgnoreVerifySSL();
        //设置协议http和https对应的处理socket链接工厂的对象
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslcontext,NoopHostnameVerifier.INSTANCE))
                .build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

        //创建自定义的httpclient对象
        CloseableHttpClient httpclient = HttpClients.custom().setConnectionManager(connManager).setRetryHandler((exception, executionCount, context) -> {
            if (executionCount > RETRY_TIME) {
                log.warn("Maximum tries reached for client http pool ");
                return false;
            }
            if (exception instanceof NoHttpResponseException
                    || exception instanceof ConnectTimeoutException
                    ) {
                log.warn("NoHttpResponseException on " + executionCount + " call");
                return true;
            }
            return false;
        }).build();
        try {
            HttpPost httppost = new HttpPost(url);
            List<NameValuePair> nvps = new ArrayList<>();
            mapToString(params, httppost, nvps);

            CloseableHttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity);
            }
            httpclient.close();
        } catch (UnsupportedEncodingException e) {
            log.error("UnsupportedEncodingException异常： " + url, e);
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            log.error("ClientProtocol异常： " + url, e);
            e.printStackTrace();
        } catch (IOException e) {
            log.error("IO异常： " + url, e);
            e.printStackTrace();
        }

        return null;
    }

    private static void mapToString(Map<String, String> params, HttpPost httppost, List<NameValuePair> nvps) throws UnsupportedEncodingException {
        if (params != null && params.size() > 0) {
            for (String key : params.keySet()) {
                String value = params.get(key);
                if(null != value){
                    nvps.add(new BasicNameValuePair(key, String.valueOf(value)));
                }
            }
            httppost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
        }
    }

    /**
     * get 方法添加默认请求延迟
     * @return String    返回类型
     */
    public static String get(String url, Map<String, String> params, Map<String, String> headers, String coding){
        return get(url,params,headers,coding,DEFAULT_TIMEOUT);
    }

    /**
     * 【http get】 参数带有header params @param url 请求连接 @param params 请求参数 @param
     * headers 头部信息 @throws
     */
    public static String get(String url, Map<String, String> params, Map<String, String> headers,
                             String coding , int timeOut) {
        RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(timeOut)
                .setConnectTimeout(timeOut).setConnectionRequestTimeout(timeOut)
                .build();
        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultRequestConfig(defaultRequestConfig).build();

        try {
            if (params != null && params.size() > 0) {
                url = url + "?" + createLinkString(params);
            }
            HttpGet httpGet = new HttpGet(url);
            httpGet.setConfig(defaultRequestConfig);
            if (headers != null && headers.size() > 0) {
                for (String key : headers.keySet()) {
                    httpGet.setHeader(key, String.valueOf(headers.get(key)));
                }
            }

            CloseableHttpResponse response = httpclient.execute(httpGet);

            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    if (StringUtils.isNotBlank(coding)) {
                        return EntityUtils.toString(entity, coding);
                    } else {
                        return EntityUtils.toString(entity);
                    }
                }
            } finally {
                httpclient.close();
                response.close();
            }
        } catch (ClientProtocolException e) {
            log.error("ClientProtocol异常： " + url, e);
        } catch (IOException e) {
            log.error("IO异常： " + url, e);
        } catch (Exception e) {
            log.error("异常： " + url, e);

        }

        return null;
    }

    /**
     * 【拼接参数】 @Title: createLinkString @Description:
     * TODO(这里用一句话描述这个方法的作用) @throws
     */
    private static String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<>(params.keySet());
        //这里需要排序 短信那边要用
        Collections.sort(keys);
        String linkString;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {
                sb.append(key).append(SEPARATOR).append(value);
            } else {
                sb.append(key).append(SEPARATOR).append(value).append(SEPARATOR_2);
            }
        }
        linkString=sb.toString();
        return linkString;
    }

    private static SSLContext createIgnoreVerifySSL(){
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSLv3");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        try {
            assert sc != null;
            sc.init(null, new TrustManager[] { trustManager }, null);
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return sc;
    }
}

