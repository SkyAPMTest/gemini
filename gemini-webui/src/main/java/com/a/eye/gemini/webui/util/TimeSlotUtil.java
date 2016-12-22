package com.a.eye.gemini.webui.util;

import java.util.Calendar;
import java.util.Date;

public class TimeSlotUtil {

	public static String getTodaySlot() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(new Date().getTime());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		long slotStart = calendar.getTimeInMillis();

		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		long slotEnd = calendar.getTimeInMillis();

		return String.valueOf(slotStart) + "-" + String.valueOf(slotEnd);
	}
}
