package com.polypay.platform.utils;

import java.security.SecureRandom;

public class RandomUtils {
	
	static SecureRandom random = new SecureRandom();
	 
	
	/**
	 *   获取1-9位随机数
	 * @param numberSize
	 * @return
	 */
	public static int random(int numberSize)
	{
		int intValue = Double.valueOf(Math.pow(10, numberSize)).intValue();
		int nextInt = random.nextInt(intValue-intValue/10) + intValue/10;
		return nextInt;
	}
	


}
