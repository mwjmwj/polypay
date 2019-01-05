package com.polypay.platform.exception;

public class ServiceException extends Exception{

	
	private int status = 0;
	public ServiceException() {
		super();
	}

	public ServiceException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public ServiceException(String arg0,int status) {
		super(arg0);
		this.setStatus(status);
	}
	
	public ServiceException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ServiceException(String arg0) {
		super(arg0);
	}

	public ServiceException(Throwable arg0) {
		super(arg0);
	}
	public ServiceException(Throwable arg0,int status) {
		super(arg0);
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	
	
}
