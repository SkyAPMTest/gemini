package com.a.eye.gemini.webui.util;

import java.text.SimpleDateFormat;

public class DateUtil {
	private static SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	public static SimpleDateFormat dayDf = new SimpleDateFormat("yyyy/MM/dd");

	public static SimpleDateFormat hourDf = new SimpleDateFormat("HH:mm");

	public static String date2String(Long timeStamp) {
		return df.format(timeStamp);
	}

	public static String data2String(SimpleDateFormat format, Long timeStamp) {
		return format.format(timeStamp);
	}
}
