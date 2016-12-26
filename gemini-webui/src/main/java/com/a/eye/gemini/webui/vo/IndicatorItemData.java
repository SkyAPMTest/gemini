package com.a.eye.gemini.webui.vo;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "indicator_host_day_pv")
public class IndicatorItemData extends ItemData {

	@Field ("indiKey")
	private String indiKey;

	@Field ("last_name")
	private String indiVal;

	public String getIndiKey() {
		return indiKey;
	}

	public void setIndiKey(String indiKey) {
		this.indiKey = indiKey;
	}

	public String getIndiVal() {
		return indiVal;
	}

	public void setIndiVal(String indiVal) {
		this.indiVal = indiVal;
	}
}
