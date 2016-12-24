package com.a.eye.gemini.webui.vo;

public class TodayTrafficVO {
	private IndicatorData pvIndiData;

	private IndicatorData uvIndiData;

	private IndicatorData ipIndiData;

	public IndicatorData getPvIndiData() {
		return pvIndiData;
	}

	public void setPvIndiData(IndicatorData pvIndiData) {
		this.pvIndiData = pvIndiData;
	}

	public IndicatorData getUvIndiData() {
		return uvIndiData;
	}

	public void setUvIndiData(IndicatorData uvIndiData) {
		this.uvIndiData = uvIndiData;
	}

	public IndicatorData getIpIndiData() {
		return ipIndiData;
	}

	public void setIpIndiData(IndicatorData ipIndiData) {
		this.ipIndiData = ipIndiData;
	}
}
