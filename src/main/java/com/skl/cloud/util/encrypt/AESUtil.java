package com.skl.cloud.util.encrypt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class AESUtil {

	public static String SIV = "0000000000000000";

	public static String Encrypt(String content, String sKey, String ivStr) throws Exception {
		if (sKey == null) {
			System.out.print("Key为空null");
			throw new Exception();
		}
		// 判断Key是否为16位
		if (sKey.length() != 16) {
			System.out.print("Key长度不是16位" + sKey.length());
			throw new Exception();
		}
		Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
		int blockSize = cipher.getBlockSize();
		byte[] dataBytes = content.getBytes();
		int plaintextLength = dataBytes.length;
		if (plaintextLength % blockSize != 0) {
			plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
		}
		byte[] plaintext = new byte[plaintextLength];
		System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
		SecretKeySpec keyspec = new SecretKeySpec(sKey.getBytes(), "AES");
		IvParameterSpec ivspec = new IvParameterSpec(ivStr.getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
		byte[] encrypted = cipher.doFinal(plaintext);
		return new BASE64Encoder().encode(encrypted);// 此处使用BASE64做转码功能，同时能起到2次加密的作用。
	}

	public static String desEncrypt(String data, String key, String iv) throws Exception {
		byte[] encrypted1 = new BASE64Decoder().decodeBuffer(data);
		Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
		SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
		IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
		cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
		byte[] original = cipher.doFinal(encrypted1);
		String originalString = new String(original);
		return originalString;
	}

	/***
	 * MD5加码 生成32位md5码
	 */
	public static String string2MD5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];
		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

	public static void main(String[] args) throws Exception {
		String str = AESUtil.Encrypt("123456789", "02138464941638d0", AESUtil.SIV);
		//System.out.println(str + "\n" + AESUtil.desEncrypt(str, "02138464941638d0", AESUtil.SIV));
		//str = "VRmLnRfw8K+ozoxb8OticfWtILNe6YDu4k7RpaaAsR8OKV/8ACGZb7H8Kj/XWfF18n4/n4g+2XZjIXLD0tpimtj1dUoCdLTuVAjfi1mwCKVfdfVJvxiifbNZ7WQ0L0gPHFnICmkiQcbi8KyBbCdpB1rPW8PVkLGgVu11xzwmsa9PkIiBz/o++wbsYojgp7MhdNGJWVEThJgdNRrb9GWXyor5iPghw0/sjXYiAUuZwAHQxyd7UcmTHTAISVcs0z93+ctOmKfQ4LBv8DyLilvBh7TzXqSQ8ivkbo1cJWuprc1DGpJuLL0/u46upZv7fCblBW+QpVUOF6pqzr5ypEptHk8KSdqf/ZOoaNo+Gj0V/eawqePSs85L83k3coKhCQXjLZlcAbvtJsJKQFjqQ8mz/+CI8ooEqC/N/r9ZFd5kaK0MD8POfNaICj2VqmF6it99NIHDf9DLZe9zX21mCX/xRsxXmiEyMLujRQFl9am3k7uldHftolg20xmxExaKQXZgNsY5VlwaNpF9+GsXOnUs0NKjZNHudjB/ufOhTsiaGuS8NiyFqTQnlm1wpc6l+sUrKfCxVRUI6RMT6BCIiSkUfpwNP3rmfgiBOPlIpWruX2InDPrntYPE2e2PjeHYddj33AA3KZu2lI08uPtnGG7orX+S8SvYnq6L+hEry+XG+K6H/z3aTgrbRWt+AQXVwOfOo+8XP6tF1s0r+2//hDQfaTUIZWbfi+s7885d/Uvsg1viHRgNiQJXQjaj698MmUJV16NBjrDl2/3ZTpu8JgefA/FxxWSscg8mMxv3nUeT0f/pYVUc+g8IB1eeaQoXoVyoHp11RyjJQPttYC5wAkP4BAF086Y48A/CoP5inSWeiaZtgwBHAyPJEAR5jcPA4Rd26JTeXkmKAw0TwtgZAJLIs9PpUs4YgZ2aZTl0jzl+W7i5HLhfAm3JqM5aiBdP5nnwHGe8Gbz2eTSOJXvGXEmrSGjdLC6GRTe2n3HCkH5186DBetqNn6rBivbz1/sN3XBjUj57gyHaCTruanrj4V97jHM7wNgGDL4TOFJO2AjANSqByY8GUSQPOWNqUrcG3kHbEjG168aMzmFnIOF9uU0TAg==";
		str = "<product>" + "<model>f001</model>" + "<pn>HPC03-WT-NET1</pn>" + "<sn>48B1517B142C6</sn>"
				+ "<version>2.3.10.045</version>" + "<makedate>2015/06/16 16:58:10</makedate>"
				+ "<expired>2018/01/14 00:00:00</expired>" + "<ipv4>" + "<ip>192.168.0.232</ip>"
				+ "<mac>6C:B0:CE:C5:BA:D6</mac>" + "</ipv4>" + "<attrs>" + "<uuid name=\"X1\">ABC0128A4E0B</uuid>"
				+ "<uuid name=\"X2\">ABC0128A4E0C</uuid>" + "<uuid name=\"X3\">ABC0128A4E0C</uuid>" + "</attrs>"
				+ "</product>" + "<nat>" + "<mode>UPNP</mode>" + "<uPNP>" + "<Mapping name=\"HTTP\">60000</Mapping>"
				+ "<Mapping name=\"RTSP\">60001</Mapping>" + "<Mapping name=\"RTP\">60002</Mapping>"
				+ "<MappedIP>22.98.103.88</MappedIP>" + "<private>" + "<Mapping name=\"HTTP\">80</Mapping>"
				+ "<Mapping name=\"RTSP\">554</Mapping>" + "<Mapping name=\"RTP\">556</Mapping>"
				+ "<MappedIP>192.168.0.232</MappedIP>\n" + "</private>\n" + "</uPNP>" + "</nat><random>123ABD234098DBke</random>";
		// str = obtainXml("sessioninfo.txt");
		System.out.println();
		System.out.println(str);
		str = AESUtil.Encrypt(str, "6CBEBEBEBEBD2510", AESUtil.SIV);
		System.out.println();
		 System.out.println(str);
		// System.out.println(AESUtil.desEncrypt(str, "6CB0CEC5BAD65333", AESUtil.SIV));
		// String pwd = "6666";
		// System.out.println("  ==> " + string2MD5(pwd));
		// str = AESUtil.desEncrypt("QVPXdlxpL98g5T7TNNNYs3ltOhCBsghTSNButsokgLxRxGpncS2rqpwvovgiOw6c",
		// "02138464941638d0", "0000000000000000");
		//str = AESUtil.Encrypt("123456789", "02138464941638d0", "0000000000000000");
		//System.out.println(str);
		str = AESUtil.desEncrypt(str.trim(), "6CBEBEBEBEBD2510", "0000000000000000");
		System.out.println();
		System.out.println(str);

	}

	public static String obtainXml(String fileName) throws IOException {
		String requestData = "";
		InputStream is = AESUtil.class.getResourceAsStream("./" + fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String tmp = null;
		while ((tmp = br.readLine()) != null) {
			requestData += tmp;
		}
		br.close();
		return requestData;
	}
}
