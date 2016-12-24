package com.a.eye.gemini.webui.vo;


public abstract class ItemData {
	private String host;

	private String hostName;

	private String timeSlot;

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
