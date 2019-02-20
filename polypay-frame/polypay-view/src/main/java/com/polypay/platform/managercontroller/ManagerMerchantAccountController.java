package com.polypay.platform.managercontroller;

import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
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
import com.polypay.platform.bean.MerchantVerify;
import com.polypay.platform.consts.MerchantHelpPayConsts;
import com.polypay.platform.consts.RequestStatus;
import com.polypay.platform.controller.BaseController;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantAccountInfoService;
import com.polypay.platform.service.IMerchantVerifyService;
import com.polypay.platform.utils.HttpClientUtil;
import com.polypay.platform.utils.HttpRequestDetailVo;
import com.polypay.platform.vo.MerchantAccountInfoVO;

@Controller
public class ManagerMerchantAccountController extends BaseController<MerchantAccountInfoVO> {

	private Logger log = LoggerFactory.getLogger(ManagerMerchantAccountController.class);

	@Autowired
	private IMerchantAccountInfoService merchantAccountInfoService;

	@Autowired
	private IMerchantVerifyService merchantVerifyService;

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

	@RequestMapping("merchantmanager/verifycode")
	@ResponseBody
	public ServiceResponse sendVerifyCode() throws ServiceException {
		ServiceResponse response = new ServiceResponse();
		MerchantVerify merchantVerify = new MerchantVerify();
		try {
			this.sendVerifyCode(response, merchantVerify);
			if (response.getStatus() != RequestStatus.SUCCESS.getStatus()) {
				return response;
			}

			// 发送成功 修改验证码有效期
			merchantVerify.setAvaliableTime(getFiveMinuteLater());
			merchantVerifyService.updateByPrimaryKeySelective(merchantVerify);

			return response;
		} catch (ServiceException e) {
			log.error(response.getRequestId() + " " + e.getMessage());
			ResponseUtils.exception(response, "验证码获取失败,请重新获取" + e.getMessage(), RequestStatus.FAILED.getStatus());
			merchantVerify.setAvaliableTime(new Date());
			merchantVerifyService.updateByPrimaryKeySelective(merchantVerify);
		} catch (Exception e) {
			log.error(response.getRequestId() + " " + e.getMessage());
			ResponseUtils.exception(response, "验证码获取失败,请重新获取" + e.getMessage(), RequestStatus.FAILED.getStatus());
			merchantVerify.setAvaliableTime(new Date());
			merchantVerifyService.updateByPrimaryKeySelective(merchantVerify);
		}
		return response;
	}

	private void sendVerifyCode(ServiceResponse response, MerchantVerify merchantVerify)
			throws ServiceException, IllegalAccessException, InvocationTargetException {
		MerchantVerify tbVerifycodeVO = new MerchantVerify();
		BeanUtils.copyProperties(tbVerifycodeVO, merchantVerify);
		if (null != merchantVerify.getMobileNumber()) {

			/**
			 * 发送手机验证码 成功直接return
			 */

			StringBuilder url = new StringBuilder();
			url.append("http://m.5c.com.cn/api/send/index.php?").append("username=zhang1")
					.append("&password_md5=1adbb3178591fd5bb0c248518f39bf6d")
					.append("&apikey=2cd1102e4b32661f0aadee35d9940985").append("&mobile=")
					.append(merchantVerify.getMobileNumber()).append("&encode=UTF-8").append("&content=")
					.append(URLEncoder
							.encode("【源盛丰】您的验证码是 :" + merchantVerify.getCode() + ",有效2分钟。请勿泄露验证码！如不是您本人操作，请忽略."));

			HttpRequestDetailVo httpGet = HttpClientUtil.httpGet(url.toString());

			if (StringUtils.isNotEmpty(httpGet.getResultAsString())
					&& httpGet.getResultAsString().contains("success")) {
				response.setMessage("发送成功");
				return;
			}

			// 设置验证码有效期为过期
			merchantVerify.setAvaliableTime(new Date());
			merchantVerifyService.updateByPrimaryKeySelective(merchantVerify);
			ResponseUtils.exception(response, "验证码发送失败,请重新获取", RequestStatus.FAILED.getStatus());
			return;
		}
	}

	private Date getFiveMinuteLater() {
		Calendar instance = Calendar.getInstance();
		instance.set(Calendar.MINUTE, instance.get(Calendar.MINUTE) + 5);
		return instance.getTime();
	}

}
