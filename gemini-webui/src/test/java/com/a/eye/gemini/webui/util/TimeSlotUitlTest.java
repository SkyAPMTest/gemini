package com.a.eye.gemini.webui.util;

import org.junit.Test;

public class TimeSlotUitlTest {

	@Test
	public void test() {
		String[][] hours = TimeSlotUtil.getHours24(0);
		for (String[] hour : hours) {
			String[] startEnd = hour[0].split("-");
			System.out.println(DateUtil.date2String(Long.valueOf(startEnd[0])) + " -- " + DateUtil.date2String(Long.valueOf(startEnd[1])));
			System.out.println(hour[1]);
		}
	}
}
