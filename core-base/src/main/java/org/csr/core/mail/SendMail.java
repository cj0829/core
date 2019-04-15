package org.csr.core.mail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.csr.core.util.PropertiesUtil;


/**
 * ClassName:SendMail.java <br/>
 * System Name： 核心框架<br/>
 * Date:     2014-2-28上午10:07:15 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public class SendMail {
	private String sms_account;
	private String sms_password; 
	private String sms_gateway;
	
	private String mailHost; 
	private String mailFrom ; 
	private String mailPassword;
	private String system_name;
	 
	public SendMail() {
		sms_account=PropertiesUtil.getConfigureValue("sms_account");
		sms_password=PropertiesUtil.getConfigureValue("sms_password"); 
		sms_gateway=PropertiesUtil.getConfigureValue("sms_gateway");
		
		mailHost =PropertiesUtil.getConfigureValue("mail_host"); 
		mailFrom =PropertiesUtil.getConfigureValue("mail_from"); 
		mailPassword =PropertiesUtil.getConfigureValue("mail_password");
		system_name=PropertiesUtil.getConfigureValue("system_name");
	} 
	
	//发送电子邮件
	public boolean sendMail(String title, String mailContent, String mailTo) { 
		try { 
			Properties props = System.getProperties(); 
			// Setup mail server
			props.put("mail.smtp.host", mailHost); 
			// Get session
			props.put("mail.smtp.auth", "true"); // 这样才能通过验证
			// 写上发件人的邮箱地址及密码
			MyAuthenticator myauth = new MyAuthenticator(mailFrom, mailPassword);
			Session session = Session.getDefaultInstance(props, myauth);  

			// Define message
			MimeMessage message = new MimeMessage(session);

			// Set the from address
			message.setFrom(new InternetAddress(mailFrom,system_name));

			// Set the to address
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailTo));
			// 发送主题
			message.setSubject(title);
			Multipart mp = new MimeMultipart("related");// related意味着可以发送html格式的邮件  
			BodyPart bodyPart = new MimeBodyPart();// 正文 
			bodyPart.setDataHandler(new DataHandler(mailContent, "text/html;charset=GBK"));// 网页格式 

			mp.addBodyPart(bodyPart); 
			message.setContent(mp); 

			message.saveChanges(); 
			Transport.send(message); 
			return true; 
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 
	} 

    /**
     * 发送短信
     * @param mobile
     * @param message
     * @return
     */
	public int sendSms(String mobile, String message) { 
		URL url = null;
		InputStream in = null;
		BufferedReader br = null;
		String resultStr = null;
		try { 
			String realUrl = genRealUrl(mobile, message); 
			url = new URL(realUrl);
			URLConnection conn = url.openConnection();
			in = conn.getInputStream();
			br = new BufferedReader(new InputStreamReader(in));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			resultStr = sb.toString().trim();
		} catch (MalformedURLException e) {
			System.out.println("URL格式错误!");
			e.printStackTrace();
			return -1;
		} catch (IOException e) {
			System.out.println("IO错误!");
			e.printStackTrace();
			return -1;
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} 
		}
		
		if (resultStr.equals("0")) {
			System.out.println("接收短信 手机号:" + mobile + "   内容:" + message+ "  成功 " + resultStr);
		} else {
			System.out.println("接收短信 手机号:" + mobile + "   内容:" + message+ "  失败 " + resultStr);
		} 
		int result = -1;
		try {
			result = Integer.parseInt(resultStr);
		} catch (Exception e) {
		}
		return result;
	}
	/**
	 * 拼接短信url
	 * @param mobile
	 * @param message
	 * @return
	 */
	private  String genRealUrl(String mobile, String message) {
		StringBuilder sb = new StringBuilder(sms_gateway);
		sb.append("?circle="+sms_account);
		sb.append("&pwd="+sms_password);
		sb.append("&mobile="+mobile);
		sb.append("&service=8056927984800");
		sb.append("&msgid=3956724");

		String msg = null;
		try {
			msg = URLEncoder.encode(message, "gb2312");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sb.append("&message=" + msg);
		return sb.toString();
	}
	public static void main(String[] args) {
	
	}
}
