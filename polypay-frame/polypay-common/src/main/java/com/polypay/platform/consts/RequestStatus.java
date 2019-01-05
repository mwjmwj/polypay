package com.polypay.platform.consts;

public enum RequestStatus {
	SUCCESS(0, "成功"),
	FAILED(-1, "失败"),
	NOT_LOGIN(-101,"未登錄"),
	SIGN_ERROR(-9001,"签名失效"),
	PERMISSION_DENIED(-9007,"权限不足");
	private Integer status;
	private String message;

	RequestStatus(Integer status, String message) {
		this.status = status;
		this.message = message;
	}

	public Integer getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public static RequestStatus stateOf(int index) {
		for (RequestStatus requestStatus : values()) {
			if (requestStatus.getStatus() == index) {
				return requestStatus;
			}
		}
		return null;
	}
}

