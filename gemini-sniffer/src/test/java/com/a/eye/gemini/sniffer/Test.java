package com.a.eye.gemini.sniffer;

import java.util.Calendar;
import java.util.Locale;

public class Test {

	public static void main(String[] args) {
		long aa = 1484144980445l;
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.setTimeInMillis(aa);

		System.out.println(calendar.get(Calendar.HOUR_OF_DAY));
		System.out.println(calendar.get(Calendar.DAY_OF_MONTH));
	}

}
