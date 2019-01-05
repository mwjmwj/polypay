package com.polypay.platform.bean;

import java.io.Serializable;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public class IpAdressResult implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8949031545692307884L;
	private String code;
	private Map data;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Map getData() {
		return data;
	}
	public void setData(Object data) {
		if(null!=data)
		{
			this.data = JSON.parseObject(data.toString(), Map.class);
		}
	}

}
