package com.a.eye.gemini.webui.model;

public class TrafficVO {
	private TrafficItemVO today;
	private TrafficItemVO yesterday;
	private TrafficItemVO expectedToday;

	public TrafficItemVO getToday() {
		return today;
	}

	public void setToday(TrafficItemVO today) {
		this.today = today;
	}

	public TrafficItemVO getYesterday() {
		return yesterday;
	}

	public void setYesterday(TrafficItemVO yesterday) {
		this.yesterday = yesterday;
	}

	public TrafficItemVO getExpectedToday() {
		return expectedToday;
	}

	public void setExpectedToday(TrafficItemVO expectedToday) {
		this.expectedToday = expectedToday;
	}
}
