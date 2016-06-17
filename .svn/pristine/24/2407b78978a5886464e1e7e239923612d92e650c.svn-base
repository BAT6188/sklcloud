package com.skl.cloud.util.encrypt;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.skl.cloud.common.exception.ManagerException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class EncrytorUtil
{
	private static String byte2ArrayString(byte value)
	{
		int iRet = value;
		if (iRet < 0)
		{
			iRet += 256;
		}
		int x = iRet / 16;
		int y = iRet % 16;

		return digits[x] + digits[y];
	}

	private static String byte2String(byte[] value)
	{
		StringBuffer sBuffer = new StringBuffer();
		for (int i = 0; i < value.length; i++)
		{
			String sTmp = byte2ArrayString(value[i]);
			sBuffer.append(sTmp);
		}
		return sBuffer.toString();
	}

	/**
	 * 对字符串做MD5加密
	 * 
	 * @param sKey
	 * @return
	 */
	public static String getMD5(String sKey)
	{
		String sResult = null;

		try
		{
			sResult = new String(sKey);
			MessageDigest digest = MessageDigest.getInstance("MD5");

			byte[] tBytes = digest.digest(sKey.getBytes());
			sResult = byte2String(tBytes);
		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}

		return sResult;
	}

	public static String encode(byte[] tBytes)
	{
		return new BASE64Encoder().encode(tBytes);
	}

	public static byte[] decode(String sData) throws IOException
	{
		return new BASE64Decoder().decodeBuffer(sData);
	}
	/**
	 * 使用base64解密
	 * 
	 * @param sSrc
	 * @param sKey
	 * @param sIV
	 * @return
	 * @throws Exception
	 */
	public static String Encrypt(String sSrc, String sKey, String sIV) throws ManagerException
			
	{  byte[] encrypted;
		try{
		byte[] raw = sKey.getBytes();
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		IvParameterSpec iv = new IvParameterSpec(sIV.getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		 encrypted = cipher.doFinal(sSrc.getBytes());
		}catch(Exception e){
			throw new ManagerException(e);
		}
		return new BASE64Encoder().encode(encrypted);
	}

	/**
	 * 使用base64解密
	 * 
	 * @param sSrc
	 * @param sKey
	 * @param sIV
	 * @return
	 * @throws Exception
	 */
	public static String Decrypt(String sSrc, String sKey, String sIV)
			throws Exception
	{
		// byte[] raw = sKey.getBytes("UTF-8");
		// SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		// Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		// IvParameterSpec iv = new IvParameterSpec(sIV.getBytes());
		// cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
		// byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);
		// byte[] original = cipher.doFinal(encrypted1);
		// String originalString = new String(original);
		//
		// return originalString;
		try
		{
			@SuppressWarnings("restriction")
			byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);

			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
			SecretKeySpec keyspec = new SecretKeySpec(sKey.getBytes(), "AES");
			IvParameterSpec ivspec = new IvParameterSpec(sIV.getBytes());

			cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

			byte[] original = cipher.doFinal(encrypted1);
			String originalString = new String(original);
			return originalString;
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}

	}

	public static void main2(String... arg) throws Exception
	{
		String sPassword = "<session>" + "<product>" + "<model>0103</model>"
				+ "<pn>HPC03-WT-NET1</pn>" + "<sn>48B1517B142D9</sn>"
				+ "<version>2.3.10.045</version>"
				+ "<makedate>2015/1/14 14:09:34</makedate>"
				+ "<expired>2018/1/14 14:09:34</expired>" + "<ipv4>"
				+ "<ip>22.48.6.10</ip>" + "<mac>6C:BD:CE:C4:BA:D6</mac>"
				+ "</ipv4>" + "<attrs>"
				+ "<uuid name=\"X1\">ABC0128A4E0B</uuid>"
				+ "<uuid name=\"X2\">ABC0128A4E0C</uuid>"
				+ "<uuid name=\"X3\">ABC0128A4E0D</uuid>"
				+ "<uuid name=\"X4\">ABC0128A4E0E</uuid>"
				+ "<uuid name=\"X5\">ABC0128A4E0F</uuid>" + "</attrs>"
				+ "</product>" + "<nat>" + "<mode>UPNP</mode>"
				+ "<protocol>HTTP</protocol>" + "<port>60000</port>" + "</nat>"
				+ "<random>39F6A039012345678</random>" + "</session>";

		// String sKey = "6CB0CEC4BAD61005";
		String sKey = "6CBDCEC4BAD62857";
		String sIV = "0000000000000000";

		System.out.println("XML = " + sPassword);

		String sValue = Encrypt(sPassword, sKey, sIV);
		System.out.println("sValue = " + sValue);
	}

	private final static String[] digits =
	{ "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D",
			"E", "F" };

	public static void main3(String... arg) throws Exception
	{
		String sPath = "C:\\Users\\jiafan\\Desktop\\加密数据.txt";

		InputStream inputstream = new FileInputStream(sPath);
		InputStreamReader inputstreamReader = new InputStreamReader(inputstream);
		BufferedReader reader = new BufferedReader(inputstreamReader);

		String sLine = "";
		StringBuilder builder = new StringBuilder();
		while ((sLine = reader.readLine()) != null)
		{
			builder.append(sLine);
		}
		reader.close();
		inputstreamReader.close();
		inputstream.close();

		sLine = builder.toString();
		System.out.println("sLine = " + sLine);

		String sKey = "6CBDCEC4BAD62857";
		String sIV = "0000000000000000";

		String sValue = Encrypt(sLine, sKey, sIV);
		System.out.println("sValue = " + sValue);
	}

	public static void main(String... arg) throws Exception
	{
		/*
		 * String sPassword = "123456789"; String sUID =
		 * UUID.randomUUID().toString().toUpperCase();
		 * System.out.println(" sUID : " + sUID + "  |" + sUID.length()); //
		 * String sKey = "6CB0CEC4BAD61005"; //String sKey = "susanlotteryEFGH";
		 * String sKey = "super"+"super"+"EFGHIJ"; String sIV =
		 * "0000000000000000";
		 */

		String sKey = "icewongwangbing0";
		String sIV = "0000000000000000";
		String sPassword = "123456";

		String sValue = Encrypt(sPassword, sKey, sIV);
		System.out.println("sValue = " + sValue + "  ||| " + sValue.length());

		sValue = Decrypt(sValue, sKey, sIV);
		System.out.println("sValue = " + sValue);

	}
}