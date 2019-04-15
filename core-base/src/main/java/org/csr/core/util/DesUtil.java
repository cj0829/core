package org.csr.core.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;

/**
 * DES或DESede加密与解密算法的工具类
 * @author Think
 *
 */
public class DesUtil {
	
	public static String DES = "DES";
	
	public static String DESede = "DESede";
	
	public static String key="8decf288b65e47848566955f4bd6065e";

	/**
	 * 使用DES的算法进行数据加密
	 * @param content 数据原文
	 * @param key 加密秘钥，秘钥长度为8位，不足8位时会抛出InvalidKeyException。秘钥长度超出8位后，8位后的内容对加密不会生效。
	 * @return 加密后数据密文
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static byte[] encryptByDES(byte[] content, byte[] key)
			throws InvalidKeyException, NoSuchAlgorithmException,
			InvalidKeySpecException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException {
		return cipher(content, key, "DES", Cipher.ENCRYPT_MODE);
	}

	/**
	 * 使用DES的算法进行数据解密
	 * @param content 密文
	 * @param key 解密秘钥，秘钥长度为8位，不足8位时会抛出InvalidKeyException。秘钥长度超出8位后，8位后的内容对解密不会生效。
	 * @return 解密后数据原文
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws NoSuchPaddingException
	 */
	public static byte[] decryptByDES(byte[] content, byte[] key)
			throws InvalidKeyException, NoSuchAlgorithmException,
			InvalidKeySpecException, IllegalBlockSizeException,
			BadPaddingException, NoSuchPaddingException {
		return cipher(content, key, "DES", Cipher.DECRYPT_MODE);
	}
	
	/**
	 * 使用DESede的算法进行数据加密
	 * @param content 数据原文
	 * @param key 加密秘钥，秘钥长度为24位，不足24位时会抛出InvalidKeyException。秘钥长度超出24位后，24位后的内容对加密不会生效。
	 * @return 加密后数据密文
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static byte[] encryptByDESede(byte[] content, byte[] key)
			throws InvalidKeyException, NoSuchAlgorithmException,
			InvalidKeySpecException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException {
		return cipher(content, key, "DESede", Cipher.ENCRYPT_MODE);
	}
	
	/**
	 * 使用DESede的算法进行数据加密
	 * 
	 * @param content 数据原文
	 * @param key  加密秘钥，秘钥长度为24位，不足24位时会抛出InvalidKeyException。秘钥长度超出24位后，24位后的内容对加密不会生效。
	 * @return 加密后数据密文
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static String encByDESede(String content) {
		if(ObjUtil.isBlank(content)) {
			return "";
		}
		byte[] data = null;
		try {
			data = encryptByDESede(content.getBytes(), key.getBytes());
			data = org.apache.commons.codec.binary.Base64.encodeBase64(data);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new String(data);
	}
	
	

	/**
	 * 使用DESede的算法进行数据解密
	 * @param content 密文
	 * @param key 解密秘钥
	 * @return
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws NoSuchPaddingException
	 */
	public static byte[] decryptByDESede(byte[] content, byte[] key)
			throws InvalidKeyException, NoSuchAlgorithmException,
			InvalidKeySpecException, IllegalBlockSizeException,
			BadPaddingException, NoSuchPaddingException {
		return cipher(content, key, "DESede", Cipher.DECRYPT_MODE);
	}
	

	/**
	 * 使用DESede的算法进行数据解密
	 * 
	 * @param content 密文
	 * @param key  解密秘钥
	 * @return
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws NoSuchPaddingException
	 */
	public static String decByDESede(String content) {
		if(ObjUtil.isBlank(content)) {
			return "";
		}
		byte[] tempData = org.apache.commons.codec.binary.Base64.decodeBase64(content.getBytes());
		try {
			tempData = decryptByDESede(tempData, key.getBytes());
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new String(tempData);
	}

	/**
	 * 加密或解密的执行
	 * @param content 需要加密或解密的内容
	 * @param key 秘钥
	 * @param algorithm 加密或解密的算法
	 * @param mode 加密/解密
	 * @return 加密或解密后的数据内容
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	private static byte[] cipher(byte[] content, byte[] key,
			String algorithm, int mode) throws InvalidKeyException,
			NoSuchAlgorithmException, InvalidKeySpecException,
			NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException {
		
		KeySpec deskey = null;
		
		if(DES.equals(algorithm)){
			deskey = new DESKeySpec(key);
		}else if(DESede.equals(algorithm)){
			deskey = new DESedeKeySpec(key);
		}else{
			throw new IllegalArgumentException("生成秘钥的算法参数不正确，请使用DES或者DESede，当前取值为："+algorithm);
		}
		
		SecretKeyFactory skf = SecretKeyFactory.getInstance(algorithm);
		SecretKey sk = skf.generateSecret(deskey);
		Cipher cip = Cipher.getInstance(algorithm);
		cip.init(mode, sk);
		return cip.doFinal(content);
	}

	public static void main1(String[] args) throws Exception{
//		byte[] d = Des.encryptByDES("1111111111生成秘钥的算法参数不正确，请使用DES或者DESede，当前取值为".getBytes(), "qazwsxed".getBytes());
//		
//		byte[] r = Des.decryptByDES(d, "qazwsxed".getBytes());
		
		//byte[] e = Des.encryptByDESede("1111111111".getBytes(), "qazwsxedcrfvtgbyhnujm123".getBytes());
//		System.out.println(Base64.encoder(d));
//		System.out.println(new String(r));
		
		String temp = ""+
				"<?xml version=\"1.0\" encoding=\"GBK\"?>" +
				"<advice>" + 
				"  <common>" + 
				"    <remoteIP>127.0.0.1</remoteIP>" + 
				"    <sysCode></sysCode>" + 
				"    <reqDate>201001040930</reqDate>" + 
				"    <key>1234567890</key>" + 
				"    <operateNum>10001</operateNum>" + 
				"    <serialNum>接入系统生成规则</serialNum>" + 
				"    <bizCode>1001</bizCode>" + 
				"    <bizName>验证码</bizName>" + 
				"    <serviceInfo></service>" + 
				"    <msgCount></msgCount>" + 
				"    <msgLength></msgLength>" + 
				"  </common>" + 
				"  <data>" + 
				"    <sms>" + 
				"      <content><![CDATA[短信发送内容]]></content>" + 
				"      <target>" + 
				"        <address>13144291131</address>" + 
				"        <addresses>" + 
				"          <begin>13144290001</begin>" + 
				"          <end>13144299999</end>" + 
				"        </addresses>" + 
				"      </target>" + 
				"      <time>" + 
				"        <begin>201001040930</begin>" + 
				"        <end>201001041030</end>" + 
				"      </time>" + 
				"    </sms>" + 
				"  </data>" + 
				"</advice>";
		
		System.out.println(temp);
		//加密
		byte[] datas = DesUtil.encryptByDESede(temp.getBytes(),"a701b5i11a701b5i11a701b5i11".getBytes());
		
		 datas = org.apache.commons.codec.binary.Base64.encodeBase64(datas);
		
		 System.out.println(new String(datas));
		long l1 = System.currentTimeMillis();
		byte[] tempData = org.apache.commons.codec.binary.Base64.decodeBase64(datas);
		 tempData = DesUtil.decryptByDESede(tempData, "a701b5i11a701b5i11a701b5i11".getBytes());
		long l2 = System.currentTimeMillis();
		System.out.println(l2 - l1);
		String rs = new String(tempData);
		System.out.println(rs);
		System.out.println(2<<0);
		System.out.println(Math.pow(2, 1));
		System.out.println();
	}
	public static void main(String[] args) throws Exception{
		String doc = "";
		doc = decByDESede(doc);
		System.out.println(doc);
		System.out.println(encByDESede(""));
		

	}
}
