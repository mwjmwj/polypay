package com.polypay.platform.pay.secret;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class CertUtil {

	private PropertiesConfiguration config;
	private Properties properties;
	private String keyStorePath;
	private InputStream keyStoreIn;
	private String cerPath;
	private InputStream cerIn;
	private String keyPass;
	private Secret secret;
	private boolean existPk = false; // 是否存大密钥信息

	private String publicKeyContent;
	private String privateKeyContent;

	private String cfgFileName = "hfb.cert.properties";

	/**
	 * 初始化类型，0:配置文件；1:properties；2:公私钥字符串； 默认0
	 */
	private String initType = "0";
	private static CertUtil certUtil = new CertUtil();
	
	public static CertUtil getInstance() {
		return certUtil;
	}
	
	private CertUtil() {
		this.initType = "0";
	}

	/**
	 * 初始化参数
	 */
	private void init() {
		try {
			SecretConfig csc = null;
			int type = Integer.valueOf(initType);
			switch (type) {
			case 0: // 配置文件初始化

				config = new PropertiesConfiguration(cfgFileName);

				if (config == null) {
					System.out.println("初始化加载证书配置文件失败，请确认配置参数是否正确");
				}

				keyStorePath = config.getString("keyStorePath");
				cerPath = config.getString("cerPath");
				keyPass = config.getString("keyPass");
				keyStoreIn = getBasePathAsStream(keyStorePath);
				cerIn = getBasePathAsStream(cerPath);

				csc = cerIn != null && keyStoreIn != null ? new SecretConfig(cerIn, keyStoreIn, keyPass)
						: new SecretConfig(cerPath, keyStorePath, keyPass, false);
				existPk = true;
				break;
			case 1: // 自定义properties初始化
				if (properties == null) {
					System.out.println("初始化加载证书配置失败，请确认配置参数是否正确");
				}

				keyStorePath = properties.getProperty("keyStorePath");
				cerPath = properties.getProperty("cerPath");
				keyPass = properties.getProperty("keyPass");
				keyStoreIn = getBasePathAsStream(keyStorePath);
				cerIn = getBasePathAsStream(cerPath);

				csc = cerIn != null && keyStoreIn != null ? new SecretConfig(cerIn, keyStoreIn, keyPass)
						: new SecretConfig(cerPath, keyStorePath, keyPass, false);
				existPk = true;
				break;
			case 2: // 证书内容初始化
				if (null == publicKeyContent || "".equals(publicKeyContent.trim()) || null == privateKeyContent
						|| "".equals(privateKeyContent) || null == keyPass || "".equals(keyPass)) {
					throw new Exception("证书配置参数不能为空");
				}
				csc = new SecretConfig(publicKeyContent, privateKeyContent, keyPass);
				existPk = true;
				break;
			default:
				System.out.println("请选择正确的初始化方式");
				break;
			}
			if (null == csc) {
				throw new Exception("证书加密初始化失败");
			}
			secret = new Secret(csc);
		} catch (ConfigurationException e) {
			System.out.println("初始化加载证书配置文件失败，请确认配置文件已添加到classpath中");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("初始化加载证书处理失败," + e);
			e.printStackTrace();
		}
	}

	URL locateFromClasspath(String resourceName) {
		URL url = null;

		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		if (loader != null) {
			url = loader.getResource(resourceName);

			if (url != null) {
				System.out.println(
						"Loading configuration from the context classpath (" + resourceName + ",url=" + url + ")");
			}

		}

		if (url == null) {
			url = ClassLoader.getSystemResource(resourceName);

			if (url != null) {
				System.out.println("Loading configuration from the system classpath (" + resourceName + ")");
			}
		}
		return url;
	}

	InputStream getBasePathAsStream(String resourcesName) {
		return this.getClass().getClassLoader().getResourceAsStream(resourcesName);
	}

	String getBasePath(URL url) {
		if (url == null) {
			return null; // file:\
		}
		String s = url.toString();
		try {
			File file = new File(url.toURI());
			s = file.exists() ? file.getAbsolutePath() : null;
		} catch (URISyntaxException e) {

		}
		return s;
	}

	/**
	 * 签名
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public String sign(String data) throws Exception {
		if(!existPk) init();
		return secret.sign(data);
	}

	/**
	 * 验签
	 * 
	 * @param data
	 * @param sign
	 * @return
	 * @throws Exception
	 */
	public boolean verify(String data, String sign) throws Exception {
		if(!existPk) init();
		sign = sign.replaceAll(" ", "+");
		return secret.verify(data, sign);
	}

	/**
	 * 加密
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public String encrypt(String data) throws Exception {
		if(!existPk) init();
		return secret.encrypt(data);
	}

	/**
	 * 解密
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public String decrypt(String data) throws Exception {
		if(!existPk) init();
		data = data.replaceAll(" ", "+");
		return secret.decrypt(data);
	}

	public String getCfgFileName() {
		return cfgFileName;
	}
	
	public String getPublicKeyContent() {
		return publicKeyContent;
	}

	public String getPrivateKeyContent() {
		return privateKeyContent;
	}
	
	public String getKeyPass() {
		return keyPass;
	}

	public CertUtil setCfgFileName(String cfgFileName) {
		this.cfgFileName = cfgFileName;
		this.initType = "0";
		return certUtil;
	}

	public CertUtil setProperties(Properties properties) {
		this.properties = properties;
		this.initType = "1";
		return certUtil;
	}

	public CertUtil setPublicKeyContent(String publicKeyContent, String privateKeyContent, String keyPass) {
		this.publicKeyContent = publicKeyContent;
		this.privateKeyContent = privateKeyContent;
		this.keyPass = keyPass;
		this.initType = "2";
		return certUtil;
	}
}
