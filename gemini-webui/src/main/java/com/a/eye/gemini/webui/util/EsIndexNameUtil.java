package com.a.eye.gemini.webui.util;

public class EsIndexNameUtil {

	public static String Atom = "atom";
	public static String Hour = "hour";
	public static String Day = "day";
	public static String Week = "week";
	public static String Month = "month";

	public static String getIndiIndexName(String indKeyName, String slotType) {
		return "gemini_" + indKeyName + "_ind_" + slotType + "_indi_idx";
	}

	public static String getHostIndexName(String indKeyName, String slotType) {
		return "gemini_" + indKeyName + "_ind_" + slotType + "_host_idx";
	}
}
