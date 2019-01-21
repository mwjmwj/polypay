package com.polypay.platform.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class DateUtils {

	private static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

	public static boolean comperDate(Date beforeDate, Date endDate) {
		long btime = beforeDate.getTime();
		long eime = endDate.getTime();
		if (eime > btime) {
			return true;
		}
		return false;
	}

	public static Date computeDate(Date beforeDate, int type, int num) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(type, num);
		return cal.getTime();
	}

	public static Date getMonthTime(int mounth) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MONTH, mounth);
		return cal.getTime();
	}

	public static Date getNewDate() {
		return new Date();
	}

	public static Date getBeforeMonthBegin() {
		// 获取前一个月第一天
		Calendar calendar1 = Calendar.getInstance();
		calendar1.add(Calendar.MONTH, -1);
		calendar1.set(Calendar.DAY_OF_MONTH, 1);
		calendar1.set(Calendar.HOUR_OF_DAY, 0);
		calendar1.set(Calendar.MINUTE, 0);
		calendar1.set(Calendar.SECOND, 0);

		return calendar1.getTime();
	}

	public static Date getBeforeMonthEnd() {
		// 获取前一个月最后一天
		Calendar calendar2 = Calendar.getInstance();
		calendar2.set(Calendar.DAY_OF_MONTH, 0);
		calendar2.set(Calendar.HOUR_OF_DAY, 23);
		calendar2.set(Calendar.MINUTE, 59);
		calendar2.set(Calendar.SECOND, 59);

		return calendar2.getTime();
	}

	public static Date getMonthBegin() {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.set(Calendar.DAY_OF_MONTH, 1);
		calendar1.set(Calendar.HOUR_OF_DAY, 0);
		calendar1.set(Calendar.MINUTE, 0);
		calendar1.set(Calendar.SECOND, 0);

		return calendar1.getTime();
	}

	public static Date getAfterDayDate(String days) {
		int daysInt = Integer.parseInt(days);

		Calendar canlendar = Calendar.getInstance(); // java.util包
		canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
		Date date = canlendar.getTime();

		return date;
	}

	/**
	 * 传入字符串 2017-01-02 - 2018-03-04 解析成 2017-01-02 00:00:00 和 2018-03-04 23:59:59
	 * 
	 * @param datastrs
	 * @return
	 */
	public static Date[] changeDate(String datastrs) {
		Date[] result = new Date[2];
		String[] split = datastrs.split(" - ");

		int index = 0;
		for (String datestr : split) {

			if (StringUtils.isEmpty(datestr)) {
				continue;
			}
			try {
				if (index == 0) {
					result[index] = SDF.parse(datestr);
					index++;
				} else {
					Date parse = SDF.parse(datestr);
					Calendar c = Calendar.getInstance();
					c.setTime(parse);
					c.set(Calendar.HOUR_OF_DAY, 23);
					c.set(Calendar.MINUTE, 59);
					c.set(Calendar.SECOND, 59);
					result[index] = c.getTime();
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}

		return result;
	}

}
