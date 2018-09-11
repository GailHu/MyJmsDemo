package com.hjf.test;

import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.hjf.utils.JMSUtils;

public class SendMessageTest {
	public static void main(String[] args) throws Exception {
		// 用于标识用户。
		String yourAccessKeyId = "yourAccessKeyId";
		// 用来验证用户的密钥。AccessKeySecret必须保密。
		String yourAccessKeySecret = "yourAccessKeySecret";
		// 签名
		String signName = "signName";
		// 模板
		String templateCode = "templateCode";
		// 接收人
		String phone = "16666666666";
		// 发送参数【根据模板填写】
		// 如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为"{\"name\":\"Tom\",\"code\":\"123\"}"
		String message = "{\"name\":\"测试\"}";
		// SendSmsResponse response =
		// JMSUtils.sendSms(yourAccessKeyId,yourAccessKeySecret,signName,templateCode,phone,message);
		// QuerySendDetailsResponse send = JMSUtils.querySendDetails(yourAccessKeyId,
		// yourAccessKeySecret, signName, templateCode, response.getBizId(), phone);
		JMSUtils jsmUtil = new JMSUtils(yourAccessKeyId, yourAccessKeySecret);
		SendSmsResponse response = jsmUtil.sendSms(signName, templateCode, phone, message);
		QuerySendDetailsResponse send = jsmUtil.querySendDetails(response.getBizId(), phone);
		Thread.sleep(3000L);
		System.out.println("发送状态：" + send.getCode());
		for (QuerySendDetailsResponse.SmsSendDetailDTO sms : send.getSmsSendDetailDTOs()) {
			System.out.println("发送到：" + sms.getPhoneNum());
			System.out.println("发送时间：" + sms.getSendDate());
			System.out.println("短信内容：" + sms.getContent());
		}
		System.out.println("流水账号：" + send.getRequestId());
	}

}
