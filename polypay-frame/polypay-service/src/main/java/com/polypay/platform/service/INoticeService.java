package com.polypay.platform.service;

import java.util.List;

import com.polypay.platform.bean.Notice;
import com.polypay.platform.exception.ServiceException;

public interface INoticeService extends IBaseService<Notice> {

	List<Notice> listNotice() throws ServiceException;

}