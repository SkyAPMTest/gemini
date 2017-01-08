package com.a.eye.gemini.sniffer.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;

public abstract class SnifferCoreBase {

	private Logger logger = LogManager.getFormatterLogger(this.getClass().getName());

	public List<PcapIf> getDevs() {
		List<PcapIf> devs = new ArrayList<PcapIf>();
		StringBuilder errsb = new StringBuilder();
		int result = Pcap.findAllDevs(devs, errsb);
		int i = 0;
		for (PcapIf device : devs) {
			String description = (device.getDescription() != null) ? device.getDescription() : "No description available";
			logger.info("#%d: %s [%s]", i++, device.getName(), description);
		}
		if (result != Pcap.OK || devs.isEmpty()) {
			logger.error("错误 %s \n", errsb.toString());
			return null;
		} else {
			return devs;
		}
	}

	public void startCapture() {// 选择一个网卡开启抓包
		PcapIf device = this.getDevs().get(2);
		StringBuilder errsb = new StringBuilder();
		int snaplen = Pcap.DEFAULT_SNAPLEN;// 长度65536
		int flags = Pcap.MODE_PROMISCUOUS;// 混杂模式
		int timeout = 10 * 1000;

		Pcap pcap = Pcap.openLive(device.getName(), snaplen, flags, timeout, errsb);
		if (pcap == null) {
			logger.debug("错误 %s \n", errsb.toString());
			return;
		}

		this.loop(pcap);
		pcap.close();
	}

	protected abstract void loop(Pcap pcap);
}
