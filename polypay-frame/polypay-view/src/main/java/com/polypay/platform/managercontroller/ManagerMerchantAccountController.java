package com.polypay.platform.managercontroller;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.polypay.platform.Page;
import com.polypay.platform.ResponseUtils;
import com.polypay.platform.ServiceResponse;
import com.polypay.platform.bean.MerchantAccountInfo;
import com.polypay.platform.consts.MerchantHelpPayConsts;
import com.polypay.platform.consts.RequestStatus;
import com.polypay.platform.controller.BaseController;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantAccountInfoService;
import com.polypay.platform.utils.MerchantUtils;
import com.polypay.platform.vo.MerchantAccountInfoVO;

@Controller
public class ManagerMerchantAccountController extends BaseController<MerchantAccountInfoVO> {

	private Logger log = LoggerFactory.getLogger(ManagerMerchantAccountController.class);

	@Autowired
	private IMerchantAccountInfoService merchantAccountInfoService;

	@RequestMapping("/merchantmanager/account/list")
	@ResponseBody
	public ServiceResponse listMerchantAccountInfo() throws ServiceException {

		ServiceResponse response = new ServiceResponse();
		try {
			PageBounds pageBounds = this.getPageBounds();
			PageList<MerchantAccountInfoVO> pageList = null;
			MerchantAccountInfoVO param = new MerchantAccountInfoVO();

			// 代理商
			String proxyid = getRequest().getParameter("proxyId");
			if (StringUtils.isNotEmpty(proxyid)) {
				param.setProxyId(proxyid);
			}

			// 商户状态
			String status = getRequest().getParameter("status");
			if (StringUtils.isNotEmpty(status)) {
				param.setStatus(Integer.parseInt(status));
			}

			// 代付等级
			String paylevel = getRequest().getParameter("paylevel");
			if (StringUtils.isNotEmpty(paylevel)) {
				param.setPayLevel(Integer.parseInt(paylevel));
			}

			// 商户手机号
			String mobileNumber = getRequest().getParameter("mobileNumber");
			if (StringUtils.isNotEmpty(mobileNumber)) {
				param.setMobileNumber(mobileNumber);
			}

			pageList = merchantAccountInfoService.listMerchantAccountInfo(pageBounds, param);

			Page<MerchantAccountInfoVO> pageData = getPageData(pageList);
			response = ResponseUtils.buildResult(pageData);
		} catch (ServiceException e) {
			log.error(response.getRequestId() + " " + e.getMessage());
			throw new ServiceException(e.getMessage(), RequestStatus.FAILED.getStatus());
		} catch (Exception e) {
			log.error(response.getRequestId() + " " + e.getMessage());
			throw new ServiceException(e.getMessage(), RequestStatus.FAILED.getStatus());
		}
		return response;

	}

	@RequestMapping("merchantmanager/account/query")
	public String getMerchantAccountInfo(@RequestParam("id") String merchantUUId, Map<String, Object> result)
			throws ServiceException {
		
		MerchantAccountInfoVO param = new MerchantAccountInfoVO();
		param.setUuid(merchantUUId);
		MerchantAccountInfo merchantInfoByUUID = merchantAccountInfoService.getMerchantInfoByUUID(param);
		result.put("merchantAccount", merchantInfoByUUID);
		
		return "adminmanager/managermerchantaccountaudit";
		
	}
	
	@RequestMapping("merchantmanager/accountinfo/update")
	@ResponseBody
	public String updateMerchantAccountInfo(MerchantAccountInfo uMerchant) throws ServiceException {
		if ("on".equals(uMerchant.getHelppayoff())) {
			uMerchant.setHelppayStatus(MerchantHelpPayConsts.OPEN_HELP_PAY);
		} else {
			uMerchant.setHelppayStatus(MerchantHelpPayConsts.CLOSE_HELP_PAY);
		}
		merchantAccountInfoService.updateByPrimaryKeySelective(uMerchant);
		return "success";
	}
	
	

}
