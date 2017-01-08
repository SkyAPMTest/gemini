package com.a.eye.gemini.common.util;

public class StringUtils {

	private static String hexStr = "0123456789abcdef"; // 全局

	public static String BinaryToHexString(byte[] bytes) {
		String result = "";
		String hex = "";
		for (int i = 0; i < bytes.length; i++) {
			hex = String.valueOf(hexStr.charAt((bytes[i] & 0xF0) >> 4));
			hex += String.valueOf(hexStr.charAt(bytes[i] & 0x0F));
			result = hex + result;
		}
		return result;
	}

	public static final String bytesToHexString(byte[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp);
		}
		return sb.toString();
	}
}
