package com.polypay.platform.service;

import com.polypay.platform.bean.Channel;
import com.polypay.platform.exception.ServiceException;

public interface IChannelService extends IBaseService<Channel>{

	Channel selectChannelByMerchantId(String merchantUUID) throws ServiceException;

}
