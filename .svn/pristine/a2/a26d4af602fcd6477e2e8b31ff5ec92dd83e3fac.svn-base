package com.skl.cloud.util.common;

import java.security.MessageDigest;



public class HeartMd5Util
{
	public final static String getMD5(String s)
	{
		char hexDigits[] =
		{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
				'e', 'f' };

		try
		{
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++)
			{
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args)
	{

		String username = "admin";
		String realm = ".";
		String password = "admin";

		String method = "GET";
		String uri = "/HttpClient4.4Demo/test3/*";

		String nouce = "dcd98b7102dd2f0e8b11d0f600bfb0c093";
		String cnonce = "749714038988d966";
		String qop = "auth";
		String nc = "00000001";

		// HA1=md5("username:real:pwd");
		// HA2=md5("method:uri");
		// HA3=md5("HA1:nouce:nc:cnonce:qop:HA2");
		String HA1b = HeartMd5Util.getMD5(username + ":" + realm + ":"
				+ password);
		String HA2b = HeartMd5Util.getMD5(method + ":" + uri);
		String HA3b = HeartMd5Util.getMD5(HA1b + ":" + nouce + ":" + nc + ":"
				+ cnonce + ":" + qop + ":" + HA2b);
		System.out.println("===========>HA3b:  " + HA3b);

		/*
		 * Digest username="admin", realm=".",
		 * nonce="dcd98b7102dd2f0e8b11d0f600bfb0c093",
		 * uri="/HttpClient4.4Demo/test3/*",
		 * response="385ed09f71ae8dd659e1bfa8f314598a", qop=auth, nc=00000001,
		 * cnonce="749714038988d966"
		 */

	}
}