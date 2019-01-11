package com.polypay.platform.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.polypay.platform.bean.Notice;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.INoticeService;

@Controller
public class NoticeController {

	@Autowired
	private INoticeService noticeService;
	
	
	@RequestMapping("notice/list")
	public String listNotice(Map<String,Object> result) throws ServiceException
	{
		List<Notice> noticeList = noticeService.listNotice();
		result.put("list", noticeList);
		return "admin/merchantnotice";
	}
	
}
