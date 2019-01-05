package com.polypay.platform.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.httpclient.util.IdleConnectionTimeoutThread;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;


/**
 * HTTP工具类
 * @author:  
 * @since: 2018年10月11日下午5:43:24 
 * @version: 1.0
 */
public class HttpClientUtil {

    private static final int TIMEOUT = 180000;
    public static final String UTF8_CHARSET = "UTF-8";
    public static final String GET = "GET";
    public static final String POST = "POST";
    private static final int CONN_MANAGER_TIMEOUT = 30000; // 从连接池获取链接超时30秒
    private static final int MAX_CONNECTIONS = 500; // httpConnectionManager管理的最大连接数最多500个链接，默认为20
    private static final int DEFAULT_MAX_CONNECTIONS_PER_HOST = 100; // 每台主机允许的最大连接数，默认为2
    private static HttpClient defaultClient;
    /** 闲置连接超时时间, 缺省为60秒钟 */
    private static final int defaultIdleConnTimeout = 60000;

    /**
     * 获取新的httpClient
     * @author:  
     * @param maxConnetcion  最大连接数
     * @param maxConnectPerHost 每台主机允许的最大连接数
     * @return
     */
    public static final HttpClient getNewClient(int maxConnetcion, int maxConnectPerHost){
        return getNewClient(maxConnetcion, CONN_MANAGER_TIMEOUT, maxConnectPerHost);
    }

    /**
     * 获取新的httpClient
     * @author:  
     * @param maxConnetcion  最大连接数
     * @param getConnTimeOut 从连接池获取链接超时
     * @param maxConnectPerHost 每台主机允许的最大连接数
     * @return
     */
    public static final HttpClient getNewClient(int maxConnetcion, int getConnTimeOut, int maxConnectPerHost){
        HttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
        HttpConnectionManagerParams connectionManagerParams = new HttpConnectionManagerParams();
        connectionManagerParams.setConnectionTimeout(TIMEOUT);
        connectionManagerParams.setSoTimeout(TIMEOUT);
        connectionManagerParams.setMaxTotalConnections(maxConnetcion);// httpConnectionManager管理的最大连接数，默认为20
        connectionManagerParams.setDefaultMaxConnectionsPerHost(maxConnectPerHost);// 定义每台主机允许的最大连接数，默认为2
        connectionManagerParams.setStaleCheckingEnabled(true); // 设置是否启用旧连接检查
        connectionManager.setParams(connectionManagerParams);

        // 检测闲置链接
        IdleConnectionTimeoutThread ict = new IdleConnectionTimeoutThread();
        ict.addConnectionManager(connectionManager);
        ict.setConnectionTimeout(defaultIdleConnTimeout);
        ict.start();

        HttpClientParams httpClientParams = new HttpClientParams();
        /* 从连接池中取连接的超时时间 */
        httpClientParams.setConnectionManagerTimeout(getConnTimeOut);
        /* 读取超时时间 */
        httpClientParams.setSoTimeout(TIMEOUT);
        // 声明
        ProtocolSocketFactory fcty = new MySecureProtocolSocketFactory();
        // 加入相关的https请求方式
        Protocol.registerProtocol("https", new Protocol("https", fcty, 443));
        return new HttpClient(httpClientParams, connectionManager);
    }

    /**
     * 初始化客户端
     * @author:  
     * @return
     */
    private static final synchronized HttpClient initDefault(){
        if(null == defaultClient){
            defaultClient = getNewClient(MAX_CONNECTIONS, CONN_MANAGER_TIMEOUT, DEFAULT_MAX_CONNECTIONS_PER_HOST);
        }
        return defaultClient;
    }

    /**
     * 获取默认的httpClient
     * @author:  
     * @return
     */
    public static final HttpClient getDefaultClient(){
        if(null == defaultClient){ return initDefault(); }
        return defaultClient;
    }

    /**
     * 请求参数
     * @author:  
     * @param reqParam
     * @return
     */
    private static NameValuePair[] paramToNameValuePair(Map<String, String> reqParam){
        if(null != reqParam && reqParam.size() > 0){
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            for(Entry<String, String> entry : reqParam.entrySet()){
                list.add(new NameValuePair(entry.getKey(), entry.getValue()));
            }
            NameValuePair[] valuePairs = new NameValuePair[list.size()];
            return list.toArray(valuePairs);
        }
        return null;
    }

    /**
     * http响应头转换map
     * @author:  
     * @param headers
     * @return
     */
    private static Map<String, String> responseHeaderToMap(Header[] headers){
        if(ArrayUtils.isEmpty(headers)){ return new HashMap<String, String>(); }
        Map<String, String> map = new HashMap<String, String>();
        for(Header header : headers){
            map.put(header.getName(), header.getValue());
        }
        return map;
    }

    /**
     * 设置请求参数
     * @author:  
     * @param method
     * @param reqHeader
     */
    private static void setReqParam(HttpMethodBase method, Map<String, String> reqParam){
        NameValuePair[] params = paramToNameValuePair(reqParam);
        if(ArrayUtils.isNotEmpty(params)){
            if(method instanceof PostMethod){
                ((PostMethod)method).setRequestBody(params);
            }else{
                method.setQueryString(params);
            }
        }
    }

    /**
     * 设置方法参数
     * @author:  
     * @param method
     * @param timeOut
     * @param charset
     */
    private static void setMethodParam(HttpMethodBase method, int timeOut, String charset){
        method.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES); // 通过禁用Cookie来提高一点点性能
        method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, timeOut);
        method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, charset);
    }

    /**
     * 设置请求头
     * @author:  
     * @param method
     * @param reqHeader
     */
    private static void setReqHeader(HttpMethodBase method, Map<String, String> reqHeader){
        if(reqHeader != null && reqHeader.size() > 0){
            for(Entry<String, String> entry : reqHeader.entrySet()){
                method.setRequestHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 释放链接
     * @author:  
     * @param method
     */
    private static void release(HttpMethodBase method){
        if(null == method){ return; }

        try{
            method.releaseConnection();
        }catch(Exception e){

        }
    }

    /**
     * 获取http客户端
     * @author:  
     * @param charset
     * @param timeOut
     * @return
     */
    private static HttpClient getHttpClient(String charset, int timeOut){
        HttpClient httpClient = getDefaultClient();
        httpClient.getParams().setContentCharset(charset);
        httpClient.getParams().setConnectionManagerTimeout(timeOut);
        return httpClient;
    }

    /**
     * httpGet请求
     * @author:  
     * @param url
     * @return
     */
    public static HttpRequestDetailVo httpGet(String url){
        return httpGet(url, null, null, UTF8_CHARSET, TIMEOUT);
    }

    /**
     * httpGet请求
     * @author:  
     * @param client
     * @param url
     * @return
     */
    public static HttpRequestDetailVo httpGet(HttpClient client, String url){
        return httpGet(client, url, null, null, UTF8_CHARSET, TIMEOUT);
    }

    /**
     * httpGet请求
     * @author:  
     * @param url
     * @param reqHeader
     * @param reqParam
     * @return
     */
    public static HttpRequestDetailVo httpGet(String url, Map<String, String> reqHeader, Map<String, String> reqParam){
        return httpGet(url, reqHeader, reqParam, UTF8_CHARSET, TIMEOUT);
    }

    /**
     * httpGet请求
     * @author:  
     * @param client
     * @param url
     * @param reqHeader
     * @param reqParam
     * @return
     */
    public static HttpRequestDetailVo httpGet(HttpClient client, String url, Map<String, String> reqHeader,
            Map<String, String> reqParam){
        return httpGet(client, url, reqHeader, reqParam, UTF8_CHARSET, TIMEOUT);
    }

    /**
     * httpGet请求
     * @author:  
     * @param url
     * @param reqHeader
     * @param reqParam
     * @param charset
     * @return
     */
    public static HttpRequestDetailVo httpGet(String url, Map<String, String> reqHeader, Map<String, String> reqParam,
            String charset){
        return httpGet(url, reqHeader, reqParam, charset, TIMEOUT);
    }

    /**
     * httpGet请求
     * @author:  
     * @param client
     * @param url
     * @param reqHeader
     * @param reqParam
     * @param charset
     * @return
     */
    public static HttpRequestDetailVo httpGet(HttpClient client, String url, Map<String, String> reqHeader,
            Map<String, String> reqParam, String charset){
        return httpGet(client, url, reqHeader, reqParam, charset, TIMEOUT);
    }

    /**
     * httpGet请求
     * @author:  
     * @param url
     * @param reqHeader
     * @param reqParam
     * @param timeOut
     * @return
     */
    public static HttpRequestDetailVo httpGet(String url, Map<String, String> reqHeader, Map<String, String> reqParam,
            int timeOut){
        return httpGet(url, reqHeader, reqParam, UTF8_CHARSET, timeOut);
    }

    /**
     * httpGet请求
     * @author:  
     * @param client
     * @param url
     * @param reqHeader
     * @param reqParam
     * @param timeOut
     * @return
     */
    public static HttpRequestDetailVo httpGet(HttpClient client, String url, Map<String, String> reqHeader,
            Map<String, String> reqParam, int timeOut){
        return httpGet(client, url, reqHeader, reqParam, UTF8_CHARSET, timeOut);
    }

    /**
     * httpGet请求
     * @author:  
     * @param url
     * @param reqHeader
     * @param reqParam
     * @param charset
     * @param timeOut
     * @return
     */
    public static HttpRequestDetailVo httpGet(String url, Map<String, String> reqHeader, Map<String, String> reqParam,
            String charset, Integer timeOut){
        HttpClient httpClient = getHttpClient(charset, timeOut);
        return httpGet(httpClient, url, reqHeader, reqParam, charset, timeOut);
    }

    /**
     * httpGet请求
     * @author:  
     * @param httpClient
     * @param url
     * @param reqHeader
     * @param reqParam
     * @param charset
     * @param timeOut
     * @return
     */
    public static HttpRequestDetailVo httpGet(HttpClient httpClient, String url, Map<String, String> reqHeader,
            Map<String, String> reqParam, String charset, Integer timeOut){
        HttpRequestDetailVo reqDetail = new HttpRequestDetailVo();
        reqDetail.setUrl(url);
        reqDetail.setMethod(GET);
        reqDetail.setReqHeader(reqHeader);
        reqDetail.setReqParam(reqParam);
        GetMethod method = null;
        try{
            method = new GetMethod(url);
            setReqParam(method, reqParam);
            setReqHeader(method, reqHeader);
            setMethodParam(method, timeOut, charset);

            reqDetail.setStartTime(System.currentTimeMillis());
            int status = httpClient.executeMethod(method);
            Header[] resHeader = method.getResponseHeaders();
            reqDetail.setStatus(status);
            reqDetail.setResHeader(responseHeaderToMap(resHeader));
            reqDetail.setResultByte(IOUtils.toByteArray(method.getResponseBodyAsStream()));
            reqDetail.setIsExceptionEnd(false);
        }catch(Exception e){
            reqDetail.setIsExceptionEnd(true);
            reqDetail.setException(e);
        }finally{
            reqDetail.setEndTime(System.currentTimeMillis());
            release(method);
        }
        return reqDetail;
    }

    /**
     * httpPost请求
     * @author:  
     * @param url
     * @param reqHeader
     * @param reqParam
     * @return
     */
    public static HttpRequestDetailVo httpPost(String url, Map<String, String> reqHeader, Map<String, String> reqParam){
        return httpPost(url, reqHeader, reqParam, TIMEOUT);
    }

    /**
     * httpPost请求
     * @author:  
     * @param httpClient
     * @param url
     * @param reqHeader
     * @param reqParam
     * @return
     */
    public static HttpRequestDetailVo httpPost(HttpClient httpClient, String url, Map<String, String> reqHeader,
            Map<String, String> reqParam){
        return httpPost(httpClient, url, reqHeader, reqParam, TIMEOUT);
    }

    /**
     * httpPost请求
     * @author:  
     * @param url
     * @param reqHeader
     * @param reqParam
     * @param timeOut
     * @return
     */
    public static HttpRequestDetailVo httpPost(String url, Map<String, String> reqHeader, Map<String, String> reqParam,
            int timeOut){
        return httpPost(url, reqHeader, reqParam, null, UTF8_CHARSET, timeOut);
    }

    /**
     * httpPost请求
     * @author:  
     * @param httpClient
     * @param url
     * @param reqHeader
     * @param reqParam
     * @param timeOut
     * @return
     */
    public static HttpRequestDetailVo httpPost(HttpClient httpClient, String url, Map<String, String> reqHeader,
            Map<String, String> reqParam, int timeOut){
        return httpPost(httpClient, url, reqHeader, reqParam, null, UTF8_CHARSET, timeOut);
    }

    /**
     * httpPost请求
     * @author:  
     * @param url
     * @param reqHeader
     * @param reqParam
     * @param content
     * @return
     */
    public static HttpRequestDetailVo httpPost(String url, Map<String, String> reqHeader, Map<String, String> reqParam,
            String content){
        return httpPost(url, reqHeader, reqParam, content, TIMEOUT);
    }

    /**
     * httpPost请求
     * @author:  
     * @param httpClient
     * @param url
     * @param reqHeader
     * @param reqParam
     * @param content
     * @return
     */
    public static HttpRequestDetailVo httpPost(HttpClient httpClient, String url, Map<String, String> reqHeader,
            Map<String, String> reqParam, String content){
        return httpPost(httpClient, url, reqHeader, reqParam, content, TIMEOUT);
    }

    /**
     * httpPost请求
     * @author:  
     * @param url
     * @param reqHeader
     * @param reqParam
     * @param content
     * @param timeOut
     * @return
     */
    public static HttpRequestDetailVo httpPost(String url, Map<String, String> reqHeader, Map<String, String> reqParam,
            String content, int timeOut){
        return httpPost(url, reqHeader, reqParam, content, UTF8_CHARSET, timeOut);
    }

    /**
     * httpPost请求
     * @author:  
     * @param httpClient
     * @param url
     * @param reqHeader
     * @param reqParam
     * @param content
     * @param timeOut
     * @return
     */
    public static HttpRequestDetailVo httpPost(HttpClient httpClient, String url, Map<String, String> reqHeader,
            Map<String, String> reqParam, String content, int timeOut){
        return httpPost(httpClient, url, reqHeader, reqParam, content, UTF8_CHARSET, timeOut);
    }

    /**
     * httpPost请求
     * @author:  
     * @param url
     * @param reqHeader
     * @param reqParam
     * @param content
     * @param contentType
     * @param timeOut
     * @return
     */
    public static HttpRequestDetailVo httpPost(String url, Map<String, String> reqHeader, Map<String, String> reqParam,
            String content, String contentType, int timeOut){
        return httpPost(url, reqHeader, reqParam, content, contentType, UTF8_CHARSET, timeOut);
    }

    /**
     * httpPost请求
     * @author:  
     * @param httpClient
     * @param url
     * @param reqHeader
     * @param reqParam
     * @param content
     * @param contentType
     * @param timeOut
     * @return
     */
    public static HttpRequestDetailVo httpPost(HttpClient httpClient, String url, Map<String, String> reqHeader,
            Map<String, String> reqParam, String content, String contentType, int timeOut){
        return httpPost(httpClient, url, reqHeader, reqParam, content, contentType, UTF8_CHARSET, timeOut);
    }

    /**
     * httpPost请求
     * @author:  
     * @param url
     * @param reqHeader
     * @param reqParam
     * @param content
     * @param contentType
     * @param charset
     * @return
     */
    public static HttpRequestDetailVo httpPost(String url, Map<String, String> reqHeader, Map<String, String> reqParam,
            String content, String contentType, String charset){
        return httpPost(url, reqHeader, reqParam, content, contentType, charset, TIMEOUT);
    }

    /**
     * httpPost请求
     * @author:  
     * @param httpClient
     * @param url
     * @param reqHeader
     * @param reqParam
     * @param content
     * @param contentType
     * @param charset
     * @return
     */
    public static HttpRequestDetailVo httpPost(HttpClient httpClient, String url, Map<String, String> reqHeader,
            Map<String, String> reqParam, String content, String contentType, String charset){
        return httpPost(httpClient, url, reqHeader, reqParam, content, contentType, charset, TIMEOUT);
    }

    /**
     * httpPost请求
     * @author:  
     * @param url
     * @param reqHeader
     * @param reqParam
     * @param content
     * @param contentType
     * @param charset
     * @param timeOut
     * @return
     */
    public static HttpRequestDetailVo httpPost(String url, Map<String, String> reqHeader, Map<String, String> reqParam,
            String content, String contentType, String charset, Integer timeOut){
        HttpClient httpClient = getHttpClient(charset, timeOut);
        return httpPost(httpClient, url, reqHeader, reqParam, content, contentType, charset, timeOut);
    }

    /**
     * httpPost请求
     * @author:  
     * @param url
     * @param reqHeader
     * @param reqParam
     * @param content
     * @param contentType
     * @param charset
     * @param timeOut
     * @return
     */
    public static HttpRequestDetailVo httpPost(HttpClient httpClient, String url, Map<String, String> reqHeader,
            Map<String, String> reqParam, String content, String contentType, String charset, Integer timeOut){
        HttpRequestDetailVo reqDetail = new HttpRequestDetailVo();
        reqDetail.setUrl(url);
        reqDetail.setMethod(POST);
        reqDetail.setReqHeader(reqHeader);
        reqDetail.setReqParam(reqParam);
        PostMethod method = null;
        try{
            method = new PostMethod(url);
            setReqParam(method, reqParam);
            setReqHeader(method, reqHeader);
            setMethodParam(method, timeOut, charset);
            if(null != content){
                RequestEntity entity = new StringRequestEntity(content, contentType, charset);
                method.setRequestEntity(entity);
                reqDetail.setSendContent(content);
            }

            reqDetail.setStartTime(System.currentTimeMillis());
            int status = httpClient.executeMethod(method);
            Header[] resHeader = method.getResponseHeaders();
            reqDetail.setStatus(status);
            reqDetail.setResHeader(responseHeaderToMap(resHeader));
            reqDetail.setResultByte(IOUtils.toByteArray(method.getResponseBodyAsStream()));
            reqDetail.setIsExceptionEnd(false);
        }catch(Exception e){
            reqDetail.setIsExceptionEnd(true);
            reqDetail.setException(e);
        }finally{
            reqDetail.setEndTime(System.currentTimeMillis());
            release(method);
        }
        return reqDetail;
    }
}
