package com.polypay.platform.dao;

import java.util.List;

import com.polypay.platform.bean.Notice;

public interface NoticeMapper extends BaseMapper<Notice> {

	List<Notice> listNotice();

}