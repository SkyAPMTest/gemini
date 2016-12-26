package com.a.eye.gemini.sniffer.setting;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gemini.sniffer")
public class GeminiSettings {

	private String online = "";
	private String offlineWriter = "";
	private String offlineReader = "";
	private String filePath = "";
	private String intervalTime = "";

	public String getIntervalTime() {
		return intervalTime;
	}

	public void setIntervalTime(String intervalTime) {
		this.intervalTime = intervalTime;
	}

	public String getOnline() {
		return online;
	}

	public void setOnline(String online) {
		this.online = online;
	}

	public String getOfflineWriter() {
		return offlineWriter;
	}

	public void setOfflineWriter(String offlineWriter) {
		this.offlineWriter = offlineWriter;
	}

	public String getOfflineReader() {
		return offlineReader;
	}

	public void setOfflineReader(String offlineReader) {
		this.offlineReader = offlineReader;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
