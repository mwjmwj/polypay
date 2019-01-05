package com.polypay.platform.consts;

public enum VerifyTypeEnum {
	LOGIN("LOGIN"),
	UPDATE_PWD("UPDATEPWD"),
	UPDATE_PAY_PWD("UPDATEPAYPWD"),
	PLACE_PAY("PLACEPAY"), REGISTER("REGISTER");
	private String name;

	VerifyTypeEnum(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
