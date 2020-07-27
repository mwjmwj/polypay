package com.polypay.platform.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.polypay.platform.bean.Menu;
import com.polypay.platform.bean.MerchantAccountInfo;
import com.polypay.platform.bean.MerchantApi;
import com.polypay.platform.bean.MerchantFinance;
import com.polypay.platform.bean.SystemConsts;
import com.polypay.platform.consts.MerchantAccountInfoStatusConsts;
import com.polypay.platform.consts.MerchantFinanceStatusConsts;
import com.polypay.platform.consts.MerchantHelpPayConsts;
import com.polypay.platform.consts.MerchantPayLevelConsts;
import com.polypay.platform.consts.RequestStatus;
import com.polypay.platform.consts.RoleConsts;
import com.polypay.platform.consts.SystemConstans;
import com.polypay.platform.dao.MenuMapper;
import com.polypay.platform.dao.MerchantAccountInfoMapper;
import com.polypay.platform.dao.MerchantApiMapper;
import com.polypay.platform.dao.MerchantFinanceMapper;
import com.polypay.platform.dao.SystemConstsMapper;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantAccountInfoService;
import com.polypay.platform.utils.MD5;
import com.polypay.platform.utils.UUIDUtils;
import com.polypay.platform.vo.MerchantAccountInfoVO;

@Service
public class MerchantAccountInfoService implements IMerchantAccountInfoService {

	@Autowired
	private MerchantAccountInfoMapper merchantAccountInfoMapper;

	@Autowired
	private MerchantApiMapper merchantApiMapper;

	@Autowired
	private MerchantFinanceMapper merchantFinanceMapper;

	@Autowired
	private SystemConstsMapper systemConstsMapper;

	@Autowired
	private MenuMapper menuMapper;

	@Override
	public int deleteByPrimaryKey(Integer id) throws ServiceException {
		try {
			merchantAccountInfoMapper.deleteByPrimaryKey(id);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int insert(MerchantAccountInfo record) throws ServiceException {
		try {
			merchantAccountInfoMapper.insert(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int insertSelective(MerchantAccountInfo record) throws ServiceException {
		try {
			merchantAccountInfoMapper.insertSelective(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public MerchantAccountInfo selectByPrimaryKey(Integer id) throws ServiceException {
		try {
			MerchantAccountInfo selectByPrimaryKey = merchantAccountInfoMapper.selectByPrimaryKey(id);
			return selectByPrimaryKey;
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public int updateByPrimaryKeySelective(MerchantAccountInfo record) throws ServiceException {
		try {
			merchantAccountInfoMapper.updateByPrimaryKeySelective(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public int updateByPrimaryKey(MerchantAccountInfo record) throws ServiceException {
		try {
			merchantAccountInfoMapper.updateByPrimaryKey(record);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return 0;
	}

	@Override
	public MerchantAccountInfo getMerchantInfo(MerchantAccountInfoVO merchantInfo) throws ServiceException {
		MerchantAccountInfo resulMerchant;
		try {
			resulMerchant = merchantAccountInfoMapper.getMerchantInfo(merchantInfo);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
		return resulMerchant;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void registerAndSave(MerchantAccountInfoVO requestMerchantInfo) throws ServiceException {
		try {

			// 保存基础信息
			MerchantAccountInfo merchantAccountInfo = new MerchantAccountInfo();

			BeanUtils.copyProperties(requestMerchantInfo, merchantAccountInfo);

//			String uuid = UUIDUtils.get32UUID();

			merchantAccountInfo.setStatus(MerchantAccountInfoStatusConsts.PRE_AUDIT);
			String passWord = requestMerchantInfo.getPassWord();
			String md5Password = MD5.md5(passWord);
			merchantAccountInfo.setPassWord(md5Password);
			merchantAccountInfo.setHelppayStatus(
					merchantAccountInfo.getHelppayStatus() == null ? MerchantHelpPayConsts.CLOSE_HELP_PAY
							: merchantAccountInfo.getHelppayStatus());
			merchantAccountInfo.setPayLevel(requestMerchantInfo.getPayLevel());
			merchantAccountInfo.setRoleId(RoleConsts.MERCHANT);
			merchantAccountInfo.setCreateTime(new Date());
			merchantAccountInfo.setChannelId(Integer.parseInt(requestMerchantInfo.getPayChannel()));
			
			merchantAccountInfo.setHandAmount(requestMerchantInfo.getHandAmount());
			
			merchantAccountInfoMapper.insertSelective(merchantAccountInfo);
			Integer merchant_uuid = 13350 + merchantAccountInfo.getId();
			merchantAccountInfo.setUuid(merchant_uuid.toString());
			requestMerchantInfo.setUuid(merchant_uuid.toString());
			merchantAccountInfoMapper.updateByPrimaryKeySelective(merchantAccountInfo);
			
			// 保存财务信息
			MerchantFinance merchantFinance = new MerchantFinance();
			merchantFinance.setMerchantId(merchant_uuid.toString());
			merchantFinance.setCreateTime(new Date());
			merchantFinance.setBlanceAmount(new BigDecimal(0.0));
			String payPassWord = requestMerchantInfo.getPayPassword();
			String md5PayPassword = MD5.md5(payPassWord);
			merchantFinance.setPayPassword(md5PayPassword);
			merchantFinance.setFronzeAmount(new BigDecimal(0.0));
			merchantFinance.setStatus(MerchantFinanceStatusConsts.FREEZE);
			merchantFinanceMapper.insertSelective(merchantFinance);

			// 保存用户Api信息
			MerchantApi merchantApi = new MerchantApi();
			merchantApi.setMerchantId(merchant_uuid.toString());
			merchantApi.setCreateTime(new Date());
			String secretKey = UUIDUtils.get32UUID();
			merchantApi.setSecretKey(secretKey);
			merchantApi.setMd5Key(UUIDUtils.get32UUID().substring(0, 8));
			// api文档信息
			SystemConsts constsByKey = systemConstsMapper.getConstsByKey(SystemConstans.API_DOC);
			if (null != constsByKey) {
				merchantApi.setApiDocUrl(constsByKey.getConstsValue());
			}
			merchantApiMapper.insertSelective(merchantApi);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public MerchantAccountInfo getMerchantInfoByUUID(MerchantAccountInfoVO merchantInfo) throws ServiceException {
		try {

			return merchantAccountInfoMapper.getMerchantInfoByUUID(merchantInfo);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public List<Menu> getMerchantMenu(Integer roleId) throws ServiceException {
		try {

			List<Menu> menus = menuMapper.getMenusByRoleId(roleId);

			Map<Integer, List<Menu>> menuMaps = Maps.newHashMap();

			List<Menu> listM;
			List<Menu> pMenus = Lists.newArrayList();
			for (Menu menu : menus) {
				if (null == menu.getMenuPid() && !menuMaps.containsKey(menu.getMenuId())) {
					listM = Lists.newArrayList();
					menuMaps.put(menu.getMenuId(), listM);
					pMenus.add(menu);
					continue;
				}
				menuMaps.get(menu.getMenuPid()).add(menu);
			}

			for (Menu menu : pMenus) {
				menu.setChildMenu(menuMaps.get(menu.getMenuId()));
			}

			return pMenus;
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public PageList<MerchantAccountInfoVO> listMerchantAccountInfo(PageBounds pageBounds, MerchantAccountInfoVO param)
			throws ServiceException {
		try {

			return merchantAccountInfoMapper.listMerchantAccountInfo(pageBounds, param);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

	@Override
	public List<String> listMerchantAccountInfoByProxy(String proxyId) throws ServiceException {
		try {

			return merchantAccountInfoMapper.listMerchantAccountInfoByProxy(proxyId);
		} catch (DataAccessException e) {
			throw new ServiceException(e, RequestStatus.FAILED.getStatus());
		}
	}

}
