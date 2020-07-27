package com.polypay.platform.advice;

import com.polypay.platform.utils.MD5;

public class Demo {
	
		public static void main(String[] args) {

			String url = "http://www.bigchengs.cn/open/hfbapi/recharge?";
			String str = "merchant_id=10000&order_number=888105899193444376&" +
					"pay_amount=501&time=5522541&pay_channel=0202" +
					"&notify_url=http://www.xx.com&bank_code=105&api_key=10f4a5534852475da415c1eec12bbfbc";
//			String url = "http://127.0.0.1:8888/open/hfbapi/recharge?";
//			String str = "merchant_id=10000&order_number=8881076665&" +
//					"pay_amount=168&time=5522541&pay_channel=0202" +
//					"&notify_url=http://www.xx.com&bank_code=105&api_key=10f4a5534852475da415c1eec12bbfbc";

			String sign = MD5.md5(str);

			System.out.println(url+str+"&sign="+sign);
	}

}
