package com.polypay.platform.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	
	public static boolean comperDate(Date beforeDate,Date endDate)
	{
		long btime = beforeDate.getTime();
		long eime = endDate.getTime();
		if(eime>btime)
		{
			return true;
		}
		return false;
	}

	public static Date computeDate(Date beforeDate , int type , int num) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(type, num);
		return cal.getTime();
	}
}
