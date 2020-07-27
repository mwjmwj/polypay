package com.polypay.platform.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.polypay.platform.ResponseUtils;
import com.polypay.platform.ServiceResponse;
import com.polypay.platform.bean.Menu;
import com.polypay.platform.bean.MerchantAccountInfo;
import com.polypay.platform.bean.MerchantFinance;
import com.polypay.platform.bean.MerchantLoginLog;
import com.polypay.platform.bean.MerchantVerify;
import com.polypay.platform.consts.MerchantGlobaKeyConsts;
import com.polypay.platform.consts.RequestStatus;
import com.polypay.platform.consts.VerifyTypeEnum;
import com.polypay.platform.exception.ServiceException;
import com.polypay.platform.service.IMerchantAccountInfoService;
import com.polypay.platform.service.IMerchantFinanceService;
import com.polypay.platform.service.IMerchantLoginLogSerivce;
import com.polypay.platform.service.IMerchantVerifyService;
import com.polypay.platform.utils.DateUtils;
import com.polypay.platform.utils.HttpClientUtil;
import com.polypay.platform.utils.HttpRequestDetailVo;
import com.polypay.platform.utils.IPUtils;
import com.polypay.platform.utils.RandomUtils;
import com.polypay.platform.utils.RegexUtil;
import com.polypay.platform.utils.UUIDUtils;
import com.polypay.platform.vo.MerchantAccountInfoVO;

@Controller
public class MerchantLoginController {

	private Logger log = LoggerFactory.getLogger(MerchantLoginController.class);

	@Autowired
	private IMerchantVerifyService merchantVerifyService;

	@Autowired
	private IMerchantAccountInfoService merchantAccountInfoService;

	@Autowired
	private IMerchantLoginLogSerivce merchantLoginLogSerivce;

	@Autowired
	private IMerchantFinanceService merchantFinanceService;

	private ExecutorService yncService = Executors.newFixedThreadPool(1);

	@RequestMapping("/merchant/register")
	@ResponseBody
	public ServiceResponse registerMerchant(MerchantAccountInfoVO requestMerchantInfo) throws ServiceException {

		ServiceResponse response = new ServiceResponse();
		MerchantVerify merchantVerify;
		boolean verifyFlag;
		MerchantAccountInfo merchantAccountInfo;
		try {

			// 根据用户邮箱或手机号获取用户
			merchantAccountInfo = merchantAccountInfoService.getMerchantInfo(requestMerchantInfo);

			// 用户存在返回
			if (null != merchantAccountInfo) {
				ResponseUtils.exception(response, "该用户已存在！", RequestStatus.FAILED.getStatus());
				return response;
			}

			// 密码输入为null
			if (StringUtils.isEmpty(requestMerchantInfo.getPassWord())) {
				ResponseUtils.exception(response, "请输入密码！", RequestStatus.FAILED.getStatus());
				return response;
			}

			// 密码输入为null
			if (StringUtils.isEmpty(requestMerchantInfo.getPayPassword())) {
				ResponseUtils.exception(response, "请输入支付密码！", RequestStatus.FAILED.getStatus());
				return response;
			}

			// 用户输入密码后台校验 必须由 6-20 数字+字符或字母组成
			if (!rexCheckPassword(requestMerchantInfo.getPassWord())) {
				ResponseUtils.exception(response, "密码需要由6-20位字母,数字，字符组成！", RequestStatus.FAILED.getStatus());
				return response;
			}

			// 获取用户验证码
			merchantVerify = new MerchantVerify();
			merchantVerify.setMobileNumber(requestMerchantInfo.getMobileNumber());
			merchantVerify.setType((VerifyTypeEnum.REGISTER));

			merchantVerify = merchantVerifyService.queryMerchantVerifyCode(merchantVerify);

			// 没有验证码或者验证码不匹配 返回
			if (null == merchantVerify || !requestMerchantInfo.getVerifyCode().equals(merchantVerify.getCode())) {
				ResponseUtils.exception(response, null == merchantVerify ? "未获取验证码,获取有效验证码" : "验证码不正确,请重新输入",
						RequestStatus.FAILED.getStatus());
				return response;
			}

			// 验证码失效重新提示用户重新获取
			verifyFlag = DateUtils.comperDate(new Date(), merchantVerify.getAvaliableTime());
			if (!verifyFlag) {
				ResponseUtils.exception(response, "验证码已失效,请重新获取", RequestStatus.FAILED.getStatus());
				return response;
			}

			// 用户注册
			merchantAccountInfoService.registerAndSave(requestMerchantInfo);

			response.setMessage("注册成功!");

			// 密码加密返回
			requestMerchantInfo.setPassWord(null);
			requestMerchantInfo.setPayPassword(null);

		} catch (ServiceException e) {
			// 注册 插入数据库异常返回数据库异常信息
			log.error(response.getRequestId() + " register Merchant fail ");
			ResponseUtils.exception(response, e.getMessage(), RequestStatus.FAILED.getStatus());
		} catch (Exception e) {
			// 其他异常 捕获返回异常msg
			log.error(response.getRequestId() + "get verifycode fail ");
			ResponseUtils.exception(response, e.getMessage(), RequestStatus.FAILED.getStatus());
		}

		return response;
	}

	/**
	 * <检查是否存在该用户>
	 * 
	 * @param merchantAccountInfoVO
	 * @return
	 */
	@RequestMapping("/merchant/check")
	@ResponseBody
	public ServiceResponse checkMerchant(MerchantAccountInfoVO merchantAccountInfoVO) throws ServiceException {
		ServiceResponse response = new ServiceResponse();
		try {

			regex(merchantAccountInfoVO, response);
			if (response.getStatus() != RequestStatus.SUCCESS.getStatus()) {
				return response;
			}
			// 根据商户手机 判断手机是否有商户使用
			MerchantAccountInfo merchat = merchantAccountInfoService.getMerchantInfo(merchantAccountInfoVO);
			if (null != merchat) {
				ResponseUtils.exception(response, "用戶已存在", RequestStatus.FAILED.getStatus());
				return response;
			}
			response.setMessage("该用户可用！");
		} catch (ServiceException e) {
			log.error(response.getRequestId() + " " + e.getMessage());
			ResponseUtils.exception(response, e.getMessage(), RequestStatus.FAILED.getStatus());
		} catch (Exception e) {
			log.error(response.getRequestId() + " " + e.getMessage());
			ResponseUtils.exception(response, e.getMessage(), RequestStatus.FAILED.getStatus());
		}
		return response;
	}

	@RequestMapping("/merchant/login")
	@ResponseBody
	public String login(MerchantAccountInfoVO requestMerchantInfo, HttpServletResponse httpResponse,
			HttpServletRequest request) throws ServiceException, IOException {
		MerchantAccountInfo merchantAccountInfo;

		if (StringUtils.isEmpty(requestMerchantInfo.getMobileNumber())) {
			return "手机号不能为空！";
		}
		merchantAccountInfo = merchantAccountInfoService.getMerchantInfo(requestMerchantInfo);

		// 根据手机账号查询商户是否存在
		if (null == merchantAccountInfo) {
			return "该用户不存在！";
		}

		// 密码登录
		if (!StringUtils.isEmpty(requestMerchantInfo.getPassWord())) {
			if (!requestMerchantInfo.getPassWord().equals(merchantAccountInfo.getPassWord())) {
				return "密码不正确！";
			}
		} else {
			if (StringUtils.isEmpty(requestMerchantInfo.getVerifyCode())) {
				return "请输入验证码或密码！";
			}

			// 验证码登录
			requestMerchantInfo.setVerifyType(VerifyTypeEnum.LOGIN);
			String checkVerifyCodeFlag = checkVerifyCode(requestMerchantInfo);
			if (StringUtils.isNotEmpty(checkVerifyCodeFlag)) {
				return checkVerifyCodeFlag;
			}
		}

		List<Menu> merchantMenu = merchantAccountInfoService.getMerchantMenu(merchantAccountInfo.getRoleId());

		yncService.execute(() -> nycSaveLoginLog(request, merchantAccountInfo.getUuid()));

		String token = UUIDUtils.get32UUID();

		request.getSession().setAttribute(MerchantGlobaKeyConsts.TOKEN, token);
		request.getSession().setAttribute(MerchantGlobaKeyConsts.USER, merchantAccountInfo);
		request.getSession().setAttribute(MerchantGlobaKeyConsts.MENU, merchantMenu);

		merchantAccountInfo.setToken(token);
		merchantAccountInfo.setPassWord(null);

		return "success";

	}

	private void nycSaveLoginLog(HttpServletRequest request, String uuid) {
		// 登录成功后 保存新的登录信息
		MerchantLoginLog merchantLoginLog = new MerchantLoginLog();
		merchantLoginLog.setLoginTime(new Date());
		merchantLoginLog.setIp(IPUtils.getIpAddress(request));
		merchantLoginLog.setLoginAddress(IPUtils.getLoginAddress(request));
		merchantLoginLog.setMerchantId(uuid);
		try {
			merchantLoginLogSerivce.insertSelective(merchantLoginLog);
		} catch (ServiceException e) {
		}
	}

	@RequestMapping("/merchant/exit")
	public String exit(HttpServletRequest request) {
		ServiceResponse response = new ServiceResponse();
		request.getSession().setAttribute(MerchantGlobaKeyConsts.USER, null);
		request.getSession().setAttribute(MerchantGlobaKeyConsts.MENU, null);
		request.getSession().setAttribute(MerchantGlobaKeyConsts.TOKEN, null);

		// 获取session
		HttpSession session = request.getSession();

		// 获取所有sessionkey
		Enumeration<String> attributeNames = session.getAttributeNames();
		String sessionKey;

		// 清空session所有的key
		while (attributeNames.hasMoreElements()) {
			sessionKey = attributeNames.nextElement();
			session.setAttribute(sessionKey, null);
		}

		response.setMessage("退出成功!");
		return "adminlogin";
	}

	@RequestMapping("/merchant/verifycode")
	@ResponseBody
	public ServiceResponse getVeriryCode(MerchantAccountInfoVO merchantAccountInfoVO) throws ServiceException {

		ServiceResponse response = new ServiceResponse();
		MerchantVerify merchantVerify = null;
		try {
			VerifyTypeEnum type = merchantAccountInfoVO.getVerifyType();

			// 获取验证码需要传入类型否则获取失败
			if (null == type) {
				response.setMessage("请设置验证码类型！");
				return response;
			}

			// 如果是注册验证码需要判断用户是否为null 为null 则可以注册
			if (VerifyTypeEnum.REGISTER.equals(type)) {
				MerchantAccountInfoVO merchantAccount = new MerchantAccountInfoVO();
				merchantAccount.setMobileNumber(merchantAccountInfoVO.getMobileNumber());
				MerchantAccountInfo exitMerchantAccountInfo = merchantAccountInfoService
						.getMerchantInfo(merchantAccount);
				if (null != exitMerchantAccountInfo) {
					ResponseUtils.exception(response, "用戶已存在", RequestStatus.FAILED.getStatus());
					return response;
				}

			} else {

				// 登录或者修改密码验证码 需要判断用户是否已注册 存在该用户才可以登录或者修改密码
				MerchantAccountInfoVO merchantAccount = new MerchantAccountInfoVO();
				merchantAccount.setMobileNumber(merchantAccountInfoVO.getMobileNumber());
				MerchantAccountInfo merchantAccountInfo = merchantAccountInfoService.getMerchantInfo(merchantAccount);
				if (null == merchantAccountInfo) {
					ResponseUtils.exception(response, "用戶未存在", RequestStatus.FAILED.getStatus());
					return response;
				}

			}
			// 验证手机号或邮箱是否正确
			regex(merchantAccountInfoVO, response);
			if (response.getStatus() != RequestStatus.SUCCESS.getStatus()) {
				return response;
			}
			MerchantVerify merchantVerifyQuery = new MerchantVerify();
			merchantVerifyQuery.setMobileNumber(merchantAccountInfoVO.getMobileNumber());
			merchantVerifyQuery.setType(merchantAccountInfoVO.getVerifyType());
			// 获取用户已存在的验证码
			merchantVerify = merchantVerifyService.queryMerchantVerifyCode(merchantVerifyQuery);

			// 新生成验证码
			String newVerifyCode = RandomUtils.random(6) + "";

			if (null == merchantVerify) {
				merchantVerify = new MerchantVerify();
				merchantVerify.setMobileNumber(merchantAccountInfoVO.getMobileNumber());
				merchantVerify.setCode(newVerifyCode);
				merchantVerify.setAvaliableTime(getFiveMinuteLater());
				merchantVerify.setType(merchantAccountInfoVO.getVerifyType());
				merchantVerifyService.insertSelective(merchantVerify);
				this.sendVerifyCode(response, merchantVerify);
				return response;
			}

			/*boolean comperDate = DateUtils.comperDate(new Date(), merchantVerify.getAvaliableTime());

			if (comperDate) {
				response.setMessage("验证码在有效期,5分钟再发送!");
				return response;
			}*/

			merchantVerify.setCode(newVerifyCode);
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

	/**
	 * 修改 密码以及支付密码 需要手机验证码支持
	 * 
	 * @param merchantInfo
	 * @return
	 * @throws ServiceException
	 */
	@RequestMapping("/merchant/password/update")
	@ResponseBody
	public ServiceResponse updateMerchantAccountInfo(MerchantAccountInfoVO merchantInfo) throws ServiceException {

		ServiceResponse response = new ServiceResponse();
		MerchantAccountInfo exitMerchant = merchantAccountInfoService.getMerchantInfo(merchantInfo);

		if (null == exitMerchant) {
			ResponseUtils.exception(response, "该用户不存在!", RequestStatus.FAILED.getStatus());
			return response;
		}

		// 查看验证码是否正确
		String checkVerifyCode = checkVerifyCode(merchantInfo);
		if (StringUtils.isNotEmpty(checkVerifyCode)) {
			ResponseUtils.exception(response, checkVerifyCode, RequestStatus.FAILED.getStatus());
			return response;
		}
		String newPassword = merchantInfo.getNewPassword();
		Boolean flag = false;
		if (!StringUtils.isEmpty(newPassword) && VerifyTypeEnum.UPDATE_PWD.equals(merchantInfo.getVerifyType())) {
			if (newPassword.equals(exitMerchant.getPassWord())) {
				ResponseUtils.exception(response, "新旧密码相同", RequestStatus.FAILED.getStatus());
				return response;
			}
			exitMerchant.setPassWord(newPassword);

			merchantAccountInfoService.updateByPrimaryKeySelective(exitMerchant);
			flag = true;
		}

		String newPayPassword = merchantInfo.getNewPayPassword();
		MerchantFinance merchantFinance;
		if (!StringUtils.isEmpty(newPayPassword)
				&& VerifyTypeEnum.UPDATE_PAY_PWD.equals(merchantInfo.getVerifyType())) {
			merchantFinance = merchantFinanceService.getMerchantFinanceByUUID(exitMerchant.getUuid());

			if (null == merchantFinance) {
				ResponseUtils.exception(response, "该用户财务信息缺失!", RequestStatus.FAILED.getStatus());
				return response;
			}

			if (newPayPassword.equals(merchantFinance.getPayPassword())) {
				ResponseUtils.exception(response, "新旧支付密码相同", RequestStatus.FAILED.getStatus());
				return response;
			}

			merchantFinance.setPayPassword(newPayPassword);
			merchantFinanceService.updateByPrimaryKeySelective(merchantFinance);
			flag = true;
		}

		if (flag) {
			response.setMessage("修改成功");
		}

		return response;
	}

	private Date getFiveMinuteLater() {
		Calendar instance = Calendar.getInstance();
		instance.set(Calendar.MINUTE, instance.get(Calendar.MINUTE) + 5);
		return instance.getTime();
	}

	@SuppressWarnings("deprecation")
	private void sendVerifyCode(ServiceResponse response, MerchantVerify merchantVerify)
			throws ServiceException, IllegalAccessException, InvocationTargetException, UnsupportedEncodingException {
		MerchantVerify tbVerifycodeVO = new MerchantVerify();
		BeanUtils.copyProperties(tbVerifycodeVO, merchantVerify);
		if (null != merchantVerify.getMobileNumber()) {

			/**
			 * 发送手机验证码 成功直接return
			 */

			String utf8Str = "【大成支付】您的验证码是 :" + merchantVerify.getCode() + ",有效2分钟。请勿泄露验证码！如不是您本人操作,请忽略";
			
			StringBuilder url = new StringBuilder();
			url.append("http://m.5c.com.cn/api/send/index.php?").append("username=sayou")
					.append("&password_md5=b9e9f233f083012debdecc16027f0389")
					.append("&apikey=6a6a321aed544626e843e2c3be4a7ae0").append("&mobile=")
					.append(merchantVerify.getMobileNumber()).append("&encode=GBK").append("&content=")
					.append(URLEncoder
							.encode(utf8Str));

			HttpRequestDetailVo httpGet = HttpClientUtil.httpGet(url.toString());

			log.debug(" send mobile " + httpGet.getResultAsString());
			if (StringUtils.isNotEmpty(httpGet.getResultAsString())&&httpGet.getResultAsString().contains("success")) {
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
//	public static void main(String[] args) {
//		String utf8Str = "【乾通支付】您的验证码是 :" + 123456 + ",有效2分钟。请勿泄露验证码！如不是您本人操作,请忽略";
//		
//		StringBuilder url = new StringBuilder();
//		url.append("http://m.5c.com.cn/api/send/index.php?").append("username=sayou")
//				.append("&password_md5=b9e9f233f083012debdecc16027f0389")
//				.append("&apikey=6a6a321aed544626e843e2c3be4a7ae0").append("&mobile=")
//				.append("17666126557").append("&encode=UTF-8").append("&content=")
//				.append(URLEncoder
//						.encode(utf8Str));
//
//		HttpRequestDetailVo httpGet = HttpClientUtil.httpGet(url.toString());
//
//	}

	private void regex(MerchantAccountInfoVO merchantAccountInfo, ServiceResponse response) {
		if (StringUtils.isNotBlank(merchantAccountInfo.getMobileNumber())) {
			boolean mobile = RegexUtil.isMobile(merchantAccountInfo.getMobileNumber());
			if (mobile) {
				return;
			}
			ResponseUtils.exception(response, "非手机号,请重新输入", RequestStatus.FAILED.getStatus());
			return;
		}
		ResponseUtils.exception(response, "请输入手机号", RequestStatus.FAILED.getStatus());
	}

	private String checkVerifyCode(MerchantAccountInfoVO merchantAccountInfo) throws ServiceException {
		MerchantVerify tbVerifycodeVO = new MerchantVerify();
		tbVerifycodeVO.setMobileNumber(merchantAccountInfo.getMobileNumber());
		tbVerifycodeVO.setType(merchantAccountInfo.getVerifyType());

		MerchantVerify merchantVerify = merchantVerifyService.queryMerchantVerifyCode(tbVerifycodeVO);

		if (null == merchantVerify) {
			return "验证码无效请重新获取！";
		}

		boolean comperDate = DateUtils.comperDate(new Date(), merchantVerify.getAvaliableTime());
		if (!comperDate) {
			return "验证码过期,请重新获取！";
		}

		if (!merchantVerify.getCode().equals(merchantAccountInfo.getVerifyCode())) {
			return "验证码不正确！";
		}
		return null;
	}

	private boolean rexCheckPassword(String input) {
		// 8-20 位，字母、数字、字符
		String regStr = "^([A-Z]|[a-z]|[0-9]|[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“'。，、？]){8,20}$";
		return input.matches(regStr);
	}

}
