package com.skl.cloud.util.common;

import java.util.Random;

public class RandomTools {

	private final static String[] digits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	public static String getRandom() {
		Random ran = new Random();
		StringBuilder strBD = new StringBuilder();

		for (int i = 0; i < 16; i++) {
			strBD.append(digits[ran.nextInt(digits.length)]);
		}

		return strBD.toString();
	}
	
	public static void main(String[] args) {
	
		String str1 = RandomTools.getRandom();
		System.out.println(" =====> str1 : " + str1  + "|" + str1.length());
		
		
		String str2 = RandomTools.getRandom();
		System.out.println(" =====> str2 : " + str2+ "|" + str1.length());
		
		
	}
}
