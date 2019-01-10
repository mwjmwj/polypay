package com.polypay.platform;

import java.util.List;

public class ResponseUtils {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ServiceResponse buildResult(Page page) {
		ServiceResponse response = new ServiceResponse();
		List rows = page.getRows();
		response.setData(rows);
		page.setRows(null);
		response.setPage(page);
		response.setCount(page.getTotal());
		return response;
	}

	public static ServiceResponse buildSuccessResult(Object obj) {
		ServiceResponse response = new ServiceResponse();
		response.setData(obj);
		return response;
	}

	public static void exception(ServiceResponse response, String msg, int status) {
		response.setData(null);
		response.setStatus(status);
		response.setMessage(msg);
	}

}
