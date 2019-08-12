package com.lhd.common.util;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by xiaomi on 2019/07/24
 */
public class HttpClientUtils {

    private static final String DEFAULT_ENCODING = "UTF-8";

    // 设置连接超时时间，单位毫秒。
    private static final int CONNECT_TIMEOUT = 6000;

    // 请求获取数据的超时时间(即响应时间)，单位毫秒。
    private static final int SOCKET_TIMEOUT = 6000;

    /**
     * 发送get请求；不带请求头和请求参数
     *
     * @param url 请求地址
     * @return
     * @throws Exception
     */
    public static String doGet(String url) throws Exception {
        return doGet(url, null, null);
    }

    /**
     * 发送get请求；带请求参数
     *
     * @param url 请求地址
     * @param params 请求参数集合
     * @return
     * @throws Exception
     */
    public static String doGet(String url, Map<String, String> params) throws Exception {
        return doGet(url, null, params);
    }

    /**
     * 发送get请求；带请求头和请求参数
     *
     * @param url 请求地址
     * @param headers 请求头集合
     * @param params 请求参数集合
     * @return
     * @throws Exception
     */
    public static String doGet(String url, Map<String, String> headers, Map<String, String> params) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        URIBuilder uriBuilder = new URIBuilder(url);
        if (params != null) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                uriBuilder.setParameter(entry.getKey(), entry.getValue());
            }
        }

        HttpGet httpGet = new HttpGet(uriBuilder.build());
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
        httpGet.setConfig(requestConfig);

        packageHeader(headers, httpGet);

        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

        try {
            return getHttpClientResult(httpResponse, httpClient);
        } finally {
            release(httpResponse, httpClient);
        }
    }

    /**
     * 发送post请求；不带请求头和请求参数
     *
     * @param url 请求地址
     * @return
     * @throws Exception
     */
    public static String doPost(String url) throws Exception {
        return doPost(url, null, null);
    }

    /**
     * 发送post请求；带请求参数
     *
     * @param url 请求地址
     * @param params 参数集合
     * @return
     * @throws Exception
     */
    public static String doPost(String url, Map<String, String> params) throws Exception {
        return doPost(url, null, params);
    }

    /**
     * 发送post请求；带请求头和请求参数
     *
     * @param url 请求地址
     * @param headers 请求头集合
     * @param params 请求参数集合
     * @return
     * @throws Exception
     */
    public static String doPost(String url, Map<String, String> headers, Map<String, String> params) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
        httpPost.setConfig(requestConfig);
        packageHeader(headers, httpPost);

        packageParam(params, httpPost);

        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);

        try {
            return getHttpClientResult(httpResponse, httpClient);
        } finally {
            release(httpResponse, httpClient);
        }
    }

    public static String doPostWithJson(String url, String jsonData) throws Exception {
        return doPostWithJson(url, jsonData, null);
    }

    public static String doPostWithJson(String url, String jsonData, Map<String, String> headers) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
        packageHeader(headers, httpPost);

        // 解决中文乱码问题
        StringEntity stringEntity = new StringEntity(jsonData, DEFAULT_ENCODING);
        stringEntity.setContentEncoding(DEFAULT_ENCODING);

        httpPost.setEntity(stringEntity);

        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);

        try {
            return getHttpClientResult(httpResponse, httpClient);
        } finally {
            release(httpResponse, httpClient);
        }
    }

    /**
     * 发送put请求；不带请求参数
     *
     * @param url 请求地址
     * @return
     */
    public static String doPut(String url) {
        return doPut(url);
    }

    /**
     * 发送put请求；带请求参数
     *
     * @param url 请求地址
     * @param params 参数集合
     * @return
     * @throws Exception
     */
    public static String doPut(String url, Map<String, String> params) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPut httpPut = new HttpPut(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
        httpPut.setConfig(requestConfig);

        packageParam(params, httpPut);

        CloseableHttpResponse httpResponse = httpClient.execute(httpPut);

        try {
            return getHttpClientResult(httpResponse, httpClient);
        } finally {
            release(httpResponse, httpClient);
        }
    }

    /**
     * 发送delete请求；不带请求参数
     *
     * @param url 请求地址
     * @return
     * @throws Exception
     */
    public static String doDelete(String url) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpDelete httpDelete = new HttpDelete(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
        httpDelete.setConfig(requestConfig);

        CloseableHttpResponse httpResponse = httpClient.execute(httpDelete);
        try {
            return getHttpClientResult(httpResponse, httpClient);
        } finally {
            release(httpResponse, httpClient);
        }
    }

    /**
     * 发送delete请求；带请求参数
     *
     * @param url 请求地址
     * @param params 参数集合
     * @return
     * @throws Exception
     */
    public static String doDelete(String url, Map<String, String> params) throws Exception {
        if (params == null) {
            params = new HashMap<>();
        }

        params.put("_method", "delete");
        return doPost(url, params);
    }

    /**
     * Description: 封装请求头
     * @param params
     * @param httpMethod
     */
    private static void packageHeader(Map<String, String> params, HttpRequestBase httpMethod) {
        if (params != null) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                httpMethod.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Description: 封装请求参数
     *
     * @param params
     * @param httpMethod
     */
    private static void packageParam(Map<String, String> params, HttpEntityEnclosingRequestBase httpMethod) throws UnsupportedEncodingException {
        // 封装请求参数
        if (params != null) {
            List<NameValuePair> nvps = new ArrayList<>();
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }

            httpMethod.setEntity(new UrlEncodedFormEntity(nvps, DEFAULT_ENCODING));
        }
    }

    /**
     * Description: 获得响应结果
     *
     * @param httpResponse
     * @param httpClient
     * @return
     * @throws Exception
     */
    private static String getHttpClientResult(CloseableHttpResponse httpResponse, CloseableHttpClient httpClient) throws Exception {

        if (httpResponse != null && httpResponse.getStatusLine() != null) {
            String content = "";
            if (httpResponse.getEntity() != null) {
                content = EntityUtils.toString(httpResponse.getEntity(), DEFAULT_ENCODING);
            }
            return content;
        }
        return null;
    }

    /**
     * Description: 释放资源
     *
     * @param httpResponse
     * @param httpClient
     * @throws IOException
     */
    private static void release(CloseableHttpResponse httpResponse, CloseableHttpClient httpClient) throws IOException {
        if (httpResponse != null) {
            httpResponse.close();
        }
        if (httpClient != null) {
            httpClient.close();
        }
    }

}
