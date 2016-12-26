package com.a.eye.gemini.webui.vo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

public abstract class ItemData {

	@Id
	protected String id;

	@Field ("host")
	private String host;

	private String hostName;

	@Field ("time_slot")
	private String timeSlot;

	@Field ("analysis_val")
	private String analysisVal;

	public String getAnalysisVal() {
		return analysisVal;
	}

	public void setAnalysisVal(String analysisVal) {
		this.analysisVal = analysisVal;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getTimeSlot() {
		return timeSlot;
	}

	public void setTimeSlot(String timeSlot) {
		this.timeSlot = timeSlot;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}
}
