package com.polypay.platform;



import java.util.UUID;

import com.polypay.platform.consts.RequestStatus;
import com.polypay.platform.utils.UUIDUtils;

public class ServiceResponse {
	
	private Object page;
	private int status = RequestStatus.SUCCESS.getStatus();
	private String message = "";
	private Object data;
	private String requestId;
	
	public ServiceResponse()
	{
		this.requestId = UUIDUtils.get32UUID();
	}
	public Object getPage() {
		return page;
	}
	public void setPage(Object page) {
		this.page = page;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	
	

}
