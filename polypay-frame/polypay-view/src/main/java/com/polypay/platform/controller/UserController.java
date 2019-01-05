//package com.zcy.zcmorefun.website.business.controller;
//
//import java.io.File;
//import java.io.IOException;
//import java.lang.reflect.InvocationTargetException;
//import java.security.NoSuchAlgorithmException;
//import java.security.spec.InvalidKeySpecException;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.Map;
//import java.util.UUID;
//
//import javax.inject.Inject;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.commons.beanutils.BeanUtils;
//import org.apache.commons.io.FilenameUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.zcy.zcmorefun.core.ServiceResponse;
//import com.zcy.zcmorefun.core.constant.RequestStatus;
//import com.zcy.zcmorefun.core.exception.ServiceException;
//import com.zcy.zcmorefun.core.utils.ResponseUtils;
//import com.zcy.zcmorefun.website.business.aspect.AuthAspect;
//import com.zcy.zcmorefun.website.business.utils.MailUtil;
//import com.zcy.zcmorefun.website.business.utils.SmsProcess;
//import com.zcy.zcmorefun.website.constans.PhoneConstants;
//import com.zcy.zcmorefun.website.constans.VerifyTypeEnum;
//import com.zcy.zcmorefun.website.model.system.TbUser;
//import com.zcy.zcmorefun.website.model.system.TbVerifycode;
//import com.zcy.zcmorefun.website.service.business.IConstsService;
//import com.zcy.zcmorefun.website.service.business.ITbUserService;
//import com.zcy.zcmorefun.website.service.utils.Base64Utils;
//import com.zcy.zcmorefun.website.sms.RegexUtil;
//import com.zcy.zcmorefun.website.utils.DateUtils;
//import com.zcy.zcmorefun.website.utils.RSAUtils;
//import com.zcy.zcmorefun.website.utils.RandomUtils;
//import com.zcy.zcmorefun.website.utils.UserUtil;
//import com.zcy.zcmorefun.website.vo.TbUserVO;
//import com.zcy.zcmorefun.website.vo.TbVerifycodeVO;
//
///**
// * <用户相关接口>
// * 
// * @author tom
// *
// *         2018年11月26日
// */
//@RestController
//public class UserController {
//
//	@Inject
//	private SmsProcess smsProcess;
//
//	@Inject
//	private ITbUserService userService;
//
//	@Autowired
//	private IConstsService constsService;
//
//	private Logger log = LoggerFactory.getLogger(UserController.class);
//
//	private static final String BASE_FILE_PATH = "BASE_FILE_PATH";
//
//	private static final String USER = "user";
//
//	private static final String HOST_PORT = "HOST_PORT";
//
//	/**
//	 * 异步任务
//	 */
////	@SuppressWarnings("unused")
////	private ExecutorService executorService = Executors.newFixedThreadPool(1);
//
//	/**
//	 * <商户注册>
//	 * 
//	 * @param tbUserVO
//	 * @return
//	 */
//	@RequestMapping("/user/register")
//	public ServiceResponse registerTbUser(@RequestBody TbUserVO tbUserVO) throws ServiceException {
//
//		ServiceResponse response = new ServiceResponse();
//		TbVerifycode queryUserVerifyCode;
//		boolean verifyFlag;
//		TbUser user;
//		try {
//
//			// 根据用户邮箱或手机号获取用户
//			user = userService.getTbUser(tbUserVO);
//			// 用户存在返回
//			if (null != user) {
//				ResponseUtils.exception(response, "该用户已存在！", RequestStatus.FAILED.getStatus());
//				return response;
//			}
//
//			// 用户输入密码后台校验 必须由 6-20 数字+字符或字母组成
//			if (!rexCheckPassword(tbUserVO.getPassword())) {
//				ResponseUtils.exception(response, "密码需要由6-20位字母,数字，字符组成！", RequestStatus.FAILED.getStatus());
//				return response;
//			}
//
//			// 获取用户验证码
//			TbVerifycodeVO tbVerifycodeVO = new TbVerifycodeVO();
//			tbVerifycodeVO.setMobileNum(tbUserVO.getPhone());
//			tbVerifycodeVO.setType((VerifyTypeEnum.REGISTER));
//
//			queryUserVerifyCode = userService.queryUserVerifyCode(tbVerifycodeVO);
//
//			// 没有验证码或者验证码不匹配 返回
//			if (null == queryUserVerifyCode
//					|| !tbUserVO.getVerificationCode().equals(queryUserVerifyCode.getVerifyCode())) {
//				ResponseUtils.exception(response, null == queryUserVerifyCode ? "未获取验证码,获取有效验证码" : "验证码不正确,请重新输入",
//						RequestStatus.FAILED.getStatus());
//				return response;
//			}
//
//			// 验证码失效重新提示用户重新获取
//			verifyFlag = DateUtils.comperDate(new Date(), queryUserVerifyCode.getAvalibleDate());
//			if (!verifyFlag) {
//				ResponseUtils.exception(response, "验证码已失效,请重新获取", RequestStatus.FAILED.getStatus());
//				return response;
//			}
//
//			// 用户注册允许,保存数据 返回注册成
//			userService.registerAndSave(tbUserVO);
//			response.setMessage("注册成功!");
//
//			// 密码加密返回
//			String decodePwd = Base64Utils.encode(tbUserVO.getPassword());
//			tbUserVO.setPassword(decodePwd);
//			response.setData(tbUserVO);
//
//		} catch (ServiceException e) {
//			// 注册 插入数据库异常返回数据库异常信息
//			log.error(response.getRequestId() + " register TbUsert fail ");
//			ResponseUtils.exception(response, e.getMessage(), RequestStatus.FAILED.getStatus());
//		} catch (Exception e) {
//			// 其他异常 捕获返回异常msg
//			log.error(response.getRequestId() + "get verifycode fail ");
//			ResponseUtils.exception(response, e.getMessage(), RequestStatus.FAILED.getStatus());
//		}
//
//		return response;
//	}
//
//	/**
//	 * <检查是否存在该用户>
//	 * 
//	 * @param TbUserVO
//	 * @return
//	 */
//	@RequestMapping("/user/check")
//	public ServiceResponse checkTbUser(@RequestBody TbUserVO TbUserVO) throws ServiceException {
//		ServiceResponse response = new ServiceResponse();
//		try {
//			// 根据商户 邮箱或者手机 判断该邮箱或手机是否有商户使用
//			TbUser merchat = userService.getTbUser(TbUserVO);
//			if (null != merchat) {
//				ResponseUtils.exception(response, "用戶已存在", RequestStatus.FAILED.getStatus());
//				return response;
//			}
//			response.setMessage("该用户可用！");
//		} catch (ServiceException e) {
//			log.error(response.getRequestId() + " " + e.getMessage());
//			ResponseUtils.exception(response, e.getMessage(), RequestStatus.FAILED.getStatus());
//		} catch (Exception e) {
//			log.error(response.getRequestId() + " " + e.getMessage());
//			ResponseUtils.exception(response, e.getMessage(), RequestStatus.FAILED.getStatus());
//		}
//		return response;
//	}
//
//	/**
//	 * <获取验证码> 包含 登录 注册 修改密码
//	 * 
//	 * @aram tbVerifycodeVO
//	 * @return
//	 * @throws ServiceException
//	 */
//	@RequestMapping("/user/verifycode")
//	public ServiceResponse getVeriryCode(@RequestBody TbVerifycodeVO tbVerifycodeVO) throws ServiceException {
//
//		ServiceResponse response = new ServiceResponse();
//
//		TbVerifycode userVerifyCode = null;
//
//		Integer verifyCodeId;
//		try {
//			VerifyTypeEnum type = tbVerifycodeVO.getType();
//
//			// 获取验证码需要传入类型否则获取失败
//			if (null == type) {
//				response.setMessage("请设置验证码类型！");
//				return response;
//			}
//
//			// 如果是注册验证码需要判断用户是否为null 为null 则可以注册
//			if (VerifyTypeEnum.REGISTER.equals(type)) {
//				TbUserVO userVO = new TbUserVO();
//				userVO.seteMail(tbVerifycodeVO.getEmail());
//				userVO.setPhone(tbVerifycodeVO.getMobileNum());
//				TbUser merchat = userService.getTbUser(userVO);
//				if (null != merchat) {
//					ResponseUtils.exception(response, "用戶已存在", RequestStatus.FAILED.getStatus());
//					return response;
//				}
//			}
//
//			// 如果是登录或者修改密码验证码 需要判断用户是否已注册 存在该用户才可以登录或者修改密码
//			if (VerifyTypeEnum.LOGIN.equals(type) || VerifyTypeEnum.UPDATEPWD.equals(type)
//					|| VerifyTypeEnum.FORGETPWD.equals(type)) {
//				TbUserVO userVO = new TbUserVO();
//				userVO.seteMail(tbVerifycodeVO.getEmail());
//				userVO.setPhone(tbVerifycodeVO.getMobileNum());
//				TbUser merchat = userService.getTbUser(userVO);
//				if (null == merchat) {
//					ResponseUtils.exception(response, "用戶未存在", RequestStatus.FAILED.getStatus());
//					return response;
//				}
//			}
//
//			// 验证手机号或邮箱是否正确
//			regex(tbVerifycodeVO, response);
//			if (response.getStatus() != RequestStatus.SUCCESS.getStatus()) {
//				return response;
//			}
//
//			// 获取用户已存在的验证码
//			userVerifyCode = userService.queryUserVerifyCode(tbVerifycodeVO);
//
//			// 新生成验证码
//			String newVerifyCode = RandomUtils.random(6) + "";
//
//			if (null == userVerifyCode) {
//				userVerifyCode = new TbVerifycode();
//				userVerifyCode.setMobileNum(tbVerifycodeVO.getMobileNum());
//				userVerifyCode.setEmail(tbVerifycodeVO.getEmail());
//				userVerifyCode.setVerifyCode(newVerifyCode);
//				userVerifyCode.setAvalibleDate(getFiveMinuteLater());
//				userVerifyCode.setType(tbVerifycodeVO.getType());
//				verifyCodeId = userService.saveUserVerifyCode(userVerifyCode);
//				userVerifyCode.setId(verifyCodeId);
//				tbVerifycodeVO.setVerifyCode(newVerifyCode);
//
//				this.sendVerifyCode(response, userVerifyCode);
//
//				return response;
//			}
//
//			boolean comperDate = DateUtils.comperDate(new Date(), userVerifyCode.getAvalibleDate());
//
//			if (comperDate) {
//				response.setMessage("验证码在有效期,5分钟再发送!");
//				return response;
//			}
//
//			this.sendVerifyCode(response, userVerifyCode);
//
//			if (response.getStatus() != RequestStatus.SUCCESS.getStatus()) {
//				return response;
//			}
//
//			// 发送成功 修改验证码有效期
//			userVerifyCode.setAvalibleDate(getFiveMinuteLater());
//			userService.updateUserVeriFyCode(userVerifyCode);
//
//			return response;
//		} catch (ServiceException e) {
//			log.error(response.getRequestId() + " " + e.getMessage());
//			ResponseUtils.exception(response, "验证码获取失败,请重新获取" + e.getMessage(), RequestStatus.FAILED.getStatus());
//			userVerifyCode.setAvalibleDate(new Date());
//			userService.updateUserVeriFyCode(userVerifyCode);
//		} catch (Exception e) {
//			log.error(response.getRequestId() + " " + e.getMessage());
//			ResponseUtils.exception(response, "验证码获取失败,请重新获取" + e.getMessage(), RequestStatus.FAILED.getStatus());
//			userVerifyCode.setAvalibleDate(new Date());
//			userService.updateUserVeriFyCode(userVerifyCode);
//		}
//
//		return response;
//	}
//
//	/**
//	 * <退出>
//	 * 
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping("/user/exit")
//	public ServiceResponse exit(HttpServletRequest request) throws ServiceException {
//		ServiceResponse response = new ServiceResponse();
//
//		// 商户退出登录 清除session
//		request.getSession().setAttribute(USER, null);
//		response.setData("退出成功!");
//		return response;
//	}
//
//	/**
//	 * <登录>
//	 * 
//	 * @param request
//	 * @param TbUserVO
//	 * @return
//	 * @throws InvalidKeySpecException
//	 * @throws NoSuchAlgorithmException
//	 */
//	@RequestMapping("/user/login")
//	public ServiceResponse login(HttpServletRequest request, @RequestBody TbUserVO TbUserVO)
//			throws ServiceException, NoSuchAlgorithmException, InvalidKeySpecException {
//		ServiceResponse response = new ServiceResponse();
//		TbUser tbUser;
//		String pwd;
//		try {
//			tbUser = userService.getTbUser(TbUserVO);
//
//			// 根据手机 邮箱查询商户是否存在
//			if (null == tbUser) {
//				ResponseUtils.exception(response, "该用户不存在！", RequestStatus.FAILED.getStatus());
//				return response;
//			}
//
//			// 密码登录
//			if (!StringUtils.isEmpty(TbUserVO.getPassword())) {
//				pwd = Base64Utils.decode(tbUser.getPassword());
//
//				if (!pwd.equals(TbUserVO.getPassword())) {
//					ResponseUtils.exception(response, "密码或账号不正确！", RequestStatus.FAILED.getStatus());
//					return response;
//				}
//			} else { // 验证码登录
//				TbUserVO.setType(VerifyTypeEnum.LOGIN);
//				boolean checkVerifyCodeFlag = checkVerifyCode(TbUserVO, response);
//				if (!checkVerifyCodeFlag) {
//					return response;
//				}
//			}
//
//			// 登录成功后 保存新的登录信息
//			TbUserVO updatevo = new TbUserVO();
//			updatevo.setId(tbUser.getId());
//			updatevo.setLastLoginTime(new Date());
//			userService.updateUserSelectivce(updatevo);
//			String token = UUID.randomUUID().toString().toUpperCase();
//
//			Map<String, String> createKeys = RSAUtils.createKeys(512);
//			String publicToken = RSAUtils.publicEncrypt(token,
//					RSAUtils.getPublicKey(createKeys.get("publicKey").toString()));
//			request.getSession().setAttribute(AuthAspect.TOKEN, publicToken);
//			request.getSession().setAttribute(USER, tbUser);
//			tbUser.setToken(publicToken);
//			response.setData(tbUser);
//			response.setMessage("登录成功！");
//			return response;
//
//		} catch (ServiceException e) {
//			log.error(response.getRequestId() + " " + e.getMessage());
//			ResponseUtils.exception(response, "内部错误,服务异常！", RequestStatus.FAILED.getStatus());
//		} catch (Exception e) {
//			log.error(response.getRequestId() + " " + e.getMessage());
//			ResponseUtils.exception(response, e.getMessage(), RequestStatus.FAILED.getStatus());
//		}
//
//		return response;
//	}
//
//	/**
//	 * 修改密码
//	 * 
//	 * @param tbUserVO
//	 * @return
//	 */
//	@RequestMapping("/user/update/password")
//	public ServiceResponse updatePassword(@RequestBody TbUserVO tbUserVO) throws ServiceException {
//		ServiceResponse response = new ServiceResponse();
//
//		TbUser tbUser;
//		try {
//			tbUser = userService.getTbUser(tbUserVO);
//
//			if (null == tbUser) {
//				ResponseUtils.exception(response, "该用户不存在！", RequestStatus.FAILED.getStatus());
//				return response;
//			}
//
//			// 两次新密码必须一致
//			if (!tbUserVO.getNewPassword().equals(tbUserVO.getAffirmPassword())) {
//				ResponseUtils.exception(response, "新密码两次输入不一致！", RequestStatus.FAILED.getStatus());
//				return response;
//			}
//
//			// 用户输入密码后台校验 必须由 6-20 数字+字符或字母组成
//			if (!rexCheckPassword(tbUserVO.getAffirmPassword())) {
//				ResponseUtils.exception(response, "密码需要由6-20位字母,数字，字符组成！", RequestStatus.FAILED.getStatus());
//				return response;
//			}
//
//			tbUserVO.setType(VerifyTypeEnum.UPDATEPWD);
//			boolean checkVerifyCode = checkVerifyCode(tbUserVO, response);
//			if (!checkVerifyCode) {
//				ResponseUtils.exception(response, "验证码错误,密码修改失败！", RequestStatus.FAILED.getStatus());
//				return response;
//			}
//
//			// 修改密码
//			tbUserVO.setId(tbUser.getId());
//			userService.updateTbUserPwd(tbUserVO);
//
//		} catch (ServiceException e) {
//			log.error(response.getRequestId() + " " + e.getMessage());
//			ResponseUtils.exception(response, "密码修改失败！", RequestStatus.FAILED.getStatus());
//			return response;
//		} catch (Exception e) {
//			log.error(response.getRequestId() + " " + e.getMessage());
//			ResponseUtils.exception(response, "密码修改失败！", RequestStatus.FAILED.getStatus());
//			return response;
//		}
//
//		response.setMessage("密码修改成功");
//		return response;
//	}
//
//	/**
//	 * 单个查找
//	 * 
//	 * @param id
//	 * @return
//	 */
//	@GetMapping("/user/find")
//	public ServiceResponse get() throws ServiceException {
//
//		ServiceResponse response = new ServiceResponse();
//		TbUser user = UserUtil.getUser();
//		response.setData(user);
//		return response;
//	}
//
//	/**
//	 * 修改商户信息
//	 * 
//	 * @param TbUserVO
//	 * @return
//	 */
//	@PostMapping("/user/update")
//	public ServiceResponse updateTbUser(@RequestBody TbUserVO TbUserVO) throws ServiceException {
//		ServiceResponse response = new ServiceResponse();
//		try {
//			userService.updateTbUser(TbUserVO);
//			response.setMessage("修改成功");
//		} catch (ServiceException e) {
//			log.error(response.getRequestId() + " " + e.getMessage());
//			ResponseUtils.exception(response, "修改失败: " + e.getMessage(), RequestStatus.FAILED.getStatus());
//		}
//		return response;
//	}
//
//	@RequestMapping("/user/update/account")
//	public ServiceResponse updateAccount(@RequestBody TbUserVO tbUserVO) throws ServiceException {
//		ServiceResponse response = new ServiceResponse();
//		try {
//
//			tbUserVO.setType(VerifyTypeEnum.UPDATE_ACCOUNT);
//			boolean checkVerifyCode = checkVerifyCode(tbUserVO, response);
//			if (!checkVerifyCode) {
//				ResponseUtils.exception(response, "验证码错误,修改账户信息失败！", RequestStatus.FAILED.getStatus());
//				return response;
//			}
//
//			userService.updateTbUser(tbUserVO);
//			response.setMessage("修改成功");
//		} catch (ServiceException e) {
//			log.error(response.getRequestId() + " " + e.getMessage());
//			ResponseUtils.exception(response, "修改失败: " + e.getMessage(), RequestStatus.FAILED.getStatus());
//		}
//		return response;
//	}
//
//	/**
//	 * 正则表达式验证密码
//	 * 
//	 * @param input
//	 * @return
//	 */
//	private boolean rexCheckPassword(String input) {
//		// 6-20 位，字母、数字、字符
//		String regStr = "^([A-Z]|[a-z]|[0-9]|[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“'。，、？]){6,20}$";
//		return input.matches(regStr);
//	}
//
//	/**
//	 * 检验验证码
//	 * 
//	 * @param tbUserVO
//	 * @param response
//	 * @return
//	 * @throws ServiceException
//	 */
//	private boolean checkVerifyCode(TbUserVO tbUserVO, ServiceResponse response) throws ServiceException {
//
//		TbVerifycodeVO tbVerifycodeVO = new TbVerifycodeVO();
//		tbVerifycodeVO.setEmail(tbUserVO.geteMail());
//		tbVerifycodeVO.setMobileNum(tbUserVO.getPhone());
//		tbVerifycodeVO.setType(tbUserVO.getType());
//		TbVerifycode queryUserVerifyCode = userService.queryUserVerifyCode(tbVerifycodeVO);
//
//		if (null == queryUserVerifyCode) {
//			ResponseUtils.exception(response, "验证码无效请重新获取！", RequestStatus.FAILED.getStatus());
//			return false;
//		}
//
//		if (!queryUserVerifyCode.getVerifyCode().equals(tbUserVO.getVerificationCode())) {
//			ResponseUtils.exception(response, "验证码不正确！", RequestStatus.FAILED.getStatus());
//			return false;
//		}
//
//		boolean comperDate = DateUtils.comperDate(new Date(), queryUserVerifyCode.getAvalibleDate());
//
//		if (!comperDate) {
//			ResponseUtils.exception(response, "验证码过期,请重新获取！", RequestStatus.FAILED.getStatus());
//			return false;
//		}
//		return true;
//	}
//
//	private void regex(TbVerifycodeVO tbVerifycodeVO, ServiceResponse response) {
//
//		if (StringUtils.isNotBlank(tbVerifycodeVO.getMobileNum())) {
//			boolean mobile = RegexUtil.isMobile(tbVerifycodeVO.getMobileNum());
//			if (mobile) {
//				return;
//			}
//
//			ResponseUtils.exception(response, "非手机号,请重新输入", RequestStatus.FAILED.getStatus());
//			return;
//		}
//
//		if (StringUtils.isNotBlank(tbVerifycodeVO.getEmail())) {
//			boolean email = RegexUtil.isEmail(tbVerifycodeVO.getEmail());
//			if (email) {
//				return;
//			}
//			ResponseUtils.exception(response, "非邮箱,请重新输入", RequestStatus.FAILED.getStatus());
//		}
//	}
//
//	private Date getFiveMinuteLater() {
//		Calendar instance = Calendar.getInstance();
//		instance.set(Calendar.MINUTE, instance.get(Calendar.MINUTE) + 5);
//		return instance.getTime();
//	}
//
//	private void sendVerifyCode(ServiceResponse response, TbVerifycode tbVerifycode)
//			throws ServiceException, IllegalAccessException, InvocationTargetException {
//		TbVerifycodeVO tbVerifycodeVO = new TbVerifycodeVO();
//		BeanUtils.copyProperties(tbVerifycodeVO, tbVerifycode);
//		if (null != tbVerifycode.getMobileNum()) {
//			int sendCode = smsProcess.sendVerifyCode(tbVerifycodeVO);
//
//			if (PhoneConstants.SEND_SUCCESS == sendCode) {
//				response.setMessage("手机验证码发送成功");
//				return;
//			}
//
//			// 设置验证码有效期为过期
//			tbVerifycode.setAvalibleDate(new Date());
//			userService.updateUserVeriFyCode(tbVerifycode);
//
//			// 如果超过发送次数则不可发送 一人一天最多20次
//			if (PhoneConstants.EXCESS_NUMBER == sendCode) {
//				ResponseUtils.exception(response, "验证码发送失败,请重新获取", RequestStatus.FAILED.getStatus());
//				return;
//			}
//
//			ResponseUtils.exception(response, "验证码发送失败,请重新获取", RequestStatus.FAILED.getStatus());
//			return;
//
//		}
//
//		// 发送邮箱
//		MailUtil.sendRegisterCodeEmail(tbVerifycode);
//		response.setMessage("邮箱验证码发送成功");
//
//	}
//
//	@RequestMapping("/user/upload")
//	public ServiceResponse uploadPic(@RequestParam("file") MultipartFile pic, HttpServletRequest request,
//			HttpServletResponse response) throws IllegalStateException, IOException, ServiceException {
//		ServiceResponse result = new ServiceResponse();
//		try {
//			// 获取图片原始文件名
//			String originalFilename = pic.getOriginalFilename();
//
//			// 文件名使用当前时间
//			String name = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
//
//			// 获取上传图片的扩展名(jpg/png/...)
//			String extension = FilenameUtils.getExtension(originalFilename);
//
//			// 图片上传的相对路径（因为相对路径放到页面上就可以显示图片）
//			String path = "/headimg/" + name + "." + extension;
//
//			// 图片上传的绝对路径
//			String url = getBaseFilePath() + path;
//			String dirUrl = getBaseFilePath() + "/headimg/";
//
//			File dir = new File(dirUrl);
//			if (!dir.exists()) {
//				dir.mkdirs();
//			}
//
//			// 上传图片
//			pic.transferTo(new File(url));
//			result.setData("http://" + getHostPort() + path);
//			return result;
//		} catch (Exception e) {
//			ResponseUtils.exception(result, "上传失败！", RequestStatus.FAILED.getStatus());
//		}
//
//		return result;
//	}
//
//	private String getBaseFilePath() {
//		return constsService.getConsts(BASE_FILE_PATH);
//	}
//
//	private String getHostPort() {
//		return constsService.getConsts(HOST_PORT);
//	}
//
//}
