package com.hjf.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

/**
 * @author Gail_Hu
 * @describe 阿里云大于发送短信。
 *  备注: 
 *  	Demo工程编码采用UTF-8。 
 *  	短信字数＝短信模板内容字数 + 签名字数。
 *  	短信字数<=70个字数，按照70个字数一条短信计算。 
 *  	短信字数>70个字数，即为长短信，按照67个字数记为一条短信计算。
 *      国际短信发送请勿参照此DEMO。
 */
public class JMSUtils {
	// 产品名称:云通信短信API产品,开发者无需替换
	private static final String product = "Dysmsapi";
	// 产品域名,开发者无需替换
	private static final String domain = "dysmsapi.aliyuncs.com";
	private String accessKeyId;
	private String accessKeySecret;

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}

	/**
	 * 若使用该工具类，需要填写accessKeyId和accessKeySecret，登录阿里云服务平台获取。
	 * 访问密钥AccessKey（AK）相当于登录密码，只是使用场景不同。AccessKey用于程序方式调用云服务API，而登录密码用于登录控制台。如果您不需要调用API，那么就不需要创建AccessKey。
	 * 
	 * @param accessKeyId
	 *            AccessKeyId用于标识用户。
	 * @param accessKeySecret
	 *            AccessKeySecret是用来验证用户的密钥。AccessKeySecret必须保密。
	 */
	public JMSUtils(String accessKeyId, String accessKeySecret) {
		this.accessKeyId = accessKeyId;
		this.accessKeySecret = accessKeySecret;
	}

	public JMSUtils() {
	}

	/**
	 * 发送短信，直接调用，需要传入accessKeyID和accessKeySecret
	 * 
	 * @param accessKeyId
	 *            AccessKeyId用于标识用户。
	 * @param accessKeySecret
	 *            AccessKeySecret是用来验证用户的密钥。AccessKeySecret必须保密。
	 * @param signName
	 *            签名名称，登录阿里云平台获取。
	 * @param templateCode
	 *            短信模板Code，登录阿里云平台获取。
	 * @param phone
	 *            发送人电话号码
	 * @param message
	 *            发送的短信内容，格式为：如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为"{\"name\":\"Tom\",
	 *            \"code\":\"123\"}"
	 * @return 发送的结果
	 * @throws ClientException
	 */
	public static SendSmsResponse sendSms(String accessKeyId, String accessKeySecret, String signName, String templateCode,
			String phone, String message) throws ClientException {
		// 可自助调整超时时间
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");
		// 初始化acsClient,暂不支持region化
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
		DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
		IAcsClient acsClient = new DefaultAcsClient(profile);
		// 组装请求对象-具体描述见控制台-文档部分内容
		SendSmsRequest request = new SendSmsRequest();
		// 必填:待发送手机号
		request.setPhoneNumbers(phone);
		// 必填:短信签名-可在短信控制台中找到
		request.setSignName(signName);
		// 必填:短信模板-可在短信控制台中找到
		request.setTemplateCode(templateCode);
		// 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
		// "{\"name\":\"" + name + "\"}"
		request.setTemplateParam(message);
		// 选填-上行短信扩展码(无特殊需求用户请忽略此字段)
		// request.setSmsUpExtendCode("90997");
		// 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
		request.setOutId("yourOutId");
		// hint 此处可能会抛出异常，注意catch
		SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
		return sendSmsResponse;
	}

	/**
	 * 查询短信发送结果，需要传入accessKeyID和accessKeySecret
	 * 
	 * @param accessKeyId
	 *            AccessKeyId用于标识用户。
	 * @param accessKeySecret
	 *            AccessKeySecret是用来验证用户的密钥。AccessKeySecret必须保密。
	 * @param bizId
	 *            直接拿发送短信返回的response.getBizId()
	 * @param phone
	 *            查询的手机号码
	 * @return 查询的结果
	 * @throws ClientException
	 */
	public static QuerySendDetailsResponse querySendDetails(String accessKeyId, String accessKeySecret, String signName, String templateCode, String bizId, String phone) throws ClientException {
		// 可自助调整超时时间
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");
		// 初始化acsClient,暂不支持region化
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
		DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
		IAcsClient acsClient = new DefaultAcsClient(profile);
		// 组装请求对象
		QuerySendDetailsRequest request = new QuerySendDetailsRequest();
		// 必填-号码
		request.setPhoneNumber(phone);
		// 可选-流水号
		request.setBizId(bizId);
		// 必填-发送日期 支持30天内记录查询，格式yyyyMMdd
		SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
		request.setSendDate(ft.format(new Date()));
		// 必填-页大小
		request.setPageSize(10L);
		// 必填-当前页码从1开始计数
		request.setCurrentPage(1L);
		// hint 此处可能会抛出异常，注意catch
		QuerySendDetailsResponse querySendDetailsResponse = acsClient.getAcsResponse(request);
		return querySendDetailsResponse;
	}
	
	/**
	 * 发送短信
	 * 
	 * @param signName
	 *            签名名称，登录阿里云平台获取。
	 * @param templateCode
	 *            短信模板Code，登录阿里云平台获取。
	 * @param phone
	 *            发送人电话号码
	 * @param message
	 *            发送的短信内容，格式为：如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为"{\"name\":\"Tom\",
	 *            \"code\":\"123\"}"
	 * @return 发送的结果
	 * @throws ClientException
	 */
	public SendSmsResponse sendSms(String signName, String templateCode, String phone, String message)
			throws ClientException {
		// 可自助调整超时时间
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");
		// 初始化acsClient,暂不支持region化
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
		DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
		IAcsClient acsClient = new DefaultAcsClient(profile);
		// 组装请求对象-具体描述见控制台-文档部分内容
		SendSmsRequest request = new SendSmsRequest();
		// 必填:待发送手机号
		request.setPhoneNumbers(phone);
		// 必填:短信签名-可在短信控制台中找到
		request.setSignName(signName);
		// 必填:短信模板-可在短信控制台中找到
		request.setTemplateCode(templateCode);
		// 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
		// "{\"name\":\"" + name + "\"}"
		request.setTemplateParam(message);
		// 选填-上行短信扩展码(无特殊需求用户请忽略此字段)
		// request.setSmsUpExtendCode("90997");
		// 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
		request.setOutId("yourOutId");
		// hint 此处可能会抛出异常，注意catch
		SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
		return sendSmsResponse;
	}

	/**
	 * 查询短信发送结果
	 * 
	 * @param bizId
	 *            直接拿发送短信返回的response.getBizId()
	 * @param phone
	 *            查询的手机号码
	 * @return 查询的结果
	 * @throws ClientException
	 */
	public QuerySendDetailsResponse querySendDetails(String bizId, String phone) throws ClientException {
		// 可自助调整超时时间
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");
		// 初始化acsClient,暂不支持region化
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
		DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
		IAcsClient acsClient = new DefaultAcsClient(profile);
		// 组装请求对象
		QuerySendDetailsRequest request = new QuerySendDetailsRequest();
		// 必填-号码
		request.setPhoneNumber(phone);
		// 可选-流水号
		request.setBizId(bizId);
		// 必填-发送日期 支持30天内记录查询，格式yyyyMMdd
		SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
		request.setSendDate(ft.format(new Date()));
		// 必填-页大小
		request.setPageSize(10L);
		// 必填-当前页码从1开始计数
		request.setCurrentPage(1L);
		// hint 此处可能会抛出异常，注意catch
		QuerySendDetailsResponse querySendDetailsResponse = acsClient.getAcsResponse(request);
		return querySendDetailsResponse;
	}
}
