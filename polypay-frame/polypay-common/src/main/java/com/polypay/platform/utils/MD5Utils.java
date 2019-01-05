package com.polypay.platform.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Utils {
	 public static String md5(String text, String key) throws Exception {
	        //加密后的字符串
	        String encodeStr=DigestUtils.md5Hex(text + key);
	        return encodeStr;
	        }

	    /**
	     * MD5验证方法
	     * 
	     * @param text 明文
	     * @param key 密钥
	     * @param md5 密文
	     * @return true/false
	     * @throws Exception
	     */
	    public static boolean verify(String text, String key, String md5) throws Exception {
	        //根据传入的密钥进行验证
	        String md5Text = md5(text, key);
	        if(md5Text.equalsIgnoreCase(md5))
	        {
	            System.out.println("MD5验证通过");
	            return true;
	        }

	            return false;
	    }
	    public static void main(String[] args) throws Exception {
	    	String md5 = md5("merchant_id=141b6ccb8bde4b10b1d0c4a5db91cf52&order_number=7&pay_amount=5000000&time=1155522&bank_code=ICBC&pay_channel=WY","qqq");
	    	System.out.println(md5);
		}

}
