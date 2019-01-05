package com.polypay.platform.utils;

public class Base64Utils {
	
	public static String encode(String pwd)
	{
		return new String(org.springframework.util.Base64Utils.encode(pwd.getBytes()));
	}
	public static String decode(String pwd)
	{
		return new String(org.springframework.util.Base64Utils.decode(pwd.getBytes()));
	}
	
}
