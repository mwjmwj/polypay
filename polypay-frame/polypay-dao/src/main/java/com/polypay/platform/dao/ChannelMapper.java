package com.polypay.platform.dao;

import com.polypay.platform.bean.Channel;

public interface ChannelMapper extends BaseMapper<Channel>{
    int deleteByPrimaryKey(Integer id);

    int insert(Channel record);

    int insertSelective(Channel record);

    Channel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Channel record);

    int updateByPrimaryKey(Channel record);

	Channel selectChannelByMerchantId(String merchantUUID);
}