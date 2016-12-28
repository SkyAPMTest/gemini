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

	public static String getYesterdaySlot() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(new Date().getTime());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 1);

		long slotStart = calendar.getTimeInMillis();

		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		long slotEnd = calendar.getTimeInMillis();

		return String.valueOf(slotStart) + "-" + String.valueOf(slotEnd);
	}

	public static String[][] getHours24(int minusDay) {
		String[][] hours = new String[24][2];

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(new Date().getTime());
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - minusDay);
		for (int i = 0; i < 24; i++) {
			calendar.set(Calendar.HOUR_OF_DAY, i);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);

			long slotStart = calendar.getTimeInMillis();

			calendar.set(Calendar.HOUR_OF_DAY, i);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			long slotEnd = calendar.getTimeInMillis();

			hours[i][0] = String.valueOf(slotStart) + "-" + String.valueOf(slotEnd);
			hours[i][1] = DateUtil.data2String(DateUtil.hourDf, slotStart) + " - " + DateUtil.data2String(DateUtil.hourDf, slotEnd);
		}
		return hours;
	}

	public static String[][] getSlotDays(int minusDay) {
		String[][] days = new String[minusDay][2];

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(new Date().getTime());
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - minusDay);
		for (int i = 0; i < minusDay; i++) {
			calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);

			long slotStart = calendar.getTimeInMillis();

			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			long slotEnd = calendar.getTimeInMillis();

			days[i][0] = String.valueOf(slotStart) + "-" + String.valueOf(slotEnd);
			days[i][1] = DateUtil.data2String(DateUtil.dayDf, slotStart);
		}
		return days;
	}
}
