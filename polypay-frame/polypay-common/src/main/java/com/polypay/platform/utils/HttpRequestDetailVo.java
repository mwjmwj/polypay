package com.polypay.platform.utils;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * http请求明细vo
 * @author:  
 * @since: 2017年4月11日下午4:57:33 
 * @version: 1.0
 */
public class HttpRequestDetailVo implements Serializable {

    private static final long serialVersionUID = 1L;
    private String url; // 请求地址
    private String method; // 请求方式
    private String charSet; // 编码
    private boolean isExceptionEnd; // 是否异常结束
    private Exception exception; // 异常信息
    private int status; // 状态码
    private Map<String, String> reqParam; // 请求参数
    private Map<String, String> reqHeader; // 请求头
    private Map<String, String> resHeader; // 相应头
    private String sendContent; // 发送的字符
    private byte[] resultByte; // 结果
    private String resultStr; // 结果字节转成字符
    private long startTime; // 开始时间
    private long endTime; // 结束时间

    public String getUrl(){
        return url;
    }

    public String getMethod(){
        return method;
    }

    public void setMethod(String method){
        this.method = method;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public String getCharSet(){
        return charSet;
    }

    public void setCharSet(String charSet){
        this.charSet = charSet;
    }

    public int getStatus(){
        return status;
    }

    public void setStatus(int status){
        this.status = status;
    }

    public Map<String, String> getReqParam(){
        return reqParam;
    }

    public void setReqParam(Map<String, String> reqParam){
        this.reqParam = reqParam;
    }

    public Map<String, String> getReqHeader(){
        return reqHeader;
    }

    public void setReqHeader(Map<String, String> reqHeader){
        this.reqHeader = reqHeader;
    }

    public Map<String, String> getResHeader(){
        return resHeader;
    }

    public void setResHeader(Map<String, String> resHeader){
        this.resHeader = resHeader;
    }

    public String getSendContent(){
        return sendContent;
    }

    public void setSendContent(String sendContent){
        this.sendContent = sendContent;
    }

    public byte[] getResultByte(){
        return resultByte;
    }

    public void setResultByte(byte[] resultByte){
        this.resultByte = resultByte;
    }

    public String getResultAsString(){
        if(ArrayUtils.isEmpty(this.resultByte)){ return null; }
        if(this.resultStr == null){
            if(StringUtils.isBlank(charSet)){
                this.resultStr = new String(this.resultByte, Charset.forName("UTF-8"));
            }else{
                this.resultStr = new String(this.resultByte, Charset.forName(this.charSet));
            }
        }
        return this.resultStr;
    }

    public boolean getIsExceptionEnd(){
        return isExceptionEnd;
    }

    public void setIsExceptionEnd(boolean isExceptionEnd){
        this.isExceptionEnd = isExceptionEnd;
    }

    public Exception getException(){
        return exception;
    }

    public void setException(Exception exception){
        this.exception = exception;
    }

    public long getStartTime(){
        return startTime;
    }

    public void setStartTime(long startTime){
        this.startTime = startTime;
    }

    public long getEndTime(){
        return endTime;
    }

    public void setEndTime(long endTime){
        this.endTime = endTime;
    }

    /**
     * 请勿随便更改
     */
    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("HTTP请求明细 [url=");
        builder.append(url);
        builder.append(", method=");
        builder.append(method);
        builder.append(", charSet=");
        builder.append(charSet);
        builder.append(", reqHeader=");
        builder.append(reqHeader);
        builder.append(", reqParam=");
        builder.append(reqParam);
        builder.append(", sendContent=");
        builder.append(sendContent);
        builder.append(", startTime=");
        builder.append(startTime);
        builder.append(", endTime=");
        builder.append(endTime);
        builder.append(", status=");
        builder.append(status);
        builder.append(", resHeader=");
        builder.append(resHeader);
        builder.append(", resultByte=");
        builder.append(Arrays.toString(resultByte));
        builder.append(", resultStr=");
        builder.append(resultStr);
        builder.append(", isExceptionEnd=");
        builder.append(isExceptionEnd);
        builder.append(", exception=");
        builder.append(exception);
        builder.append("]");
        return builder.toString();
    }
}
