package com.polypay.platform.pay.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public static String getFullDate(){
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}
	
	public static String getDate(){
		return new SimpleDateFormat("yyyyMMdd").format(new Date());
	}
	
	public static String getTime(){
		return new SimpleDateFormat("HHmmss").format(new Date());
	}
	
	public static String getTimeMillis(){
		return new SimpleDateFormat("yyyyMMddHHmmssSS").format(new Date());
	}

}
