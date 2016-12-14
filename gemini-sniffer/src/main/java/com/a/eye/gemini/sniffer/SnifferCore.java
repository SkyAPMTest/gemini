package com.a.eye.gemini.sniffer;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SnifferCore {

	private Logger logger = LogManager.getFormatterLogger(SnifferCore.class.getName());

	@Autowired
	private SnifferHandler snifferHandler;

	public List<PcapIf> getDevs() {
		List<PcapIf> devs = new ArrayList<PcapIf>();
		StringBuilder errsb = new StringBuilder();
		int result = Pcap.findAllDevs(devs, errsb);
		int i = 0;
		for (PcapIf device : devs) {
			String description = (device.getDescription() != null) ? device.getDescription() : "No description available";
			logger.debug("#%d: %s [%s]", i++, device.getName(), description);
		}
		if (result == Pcap.NOT_OK || devs.isEmpty()) {
			logger.debug("错误 %s \n", errsb.toString());
			return null;
		} else {
			return devs;
		}
	}

	@PostConstruct
	public void startCapture() {// 选择一个网卡开启抓包
		PcapIf device = this.getDevs().get(5);
		StringBuilder errsb = new StringBuilder();
		int snaplen = Pcap.DEFAULT_SNAPLEN;// 长度65536
		int flags = Pcap.MODE_PROMISCUOUS;// 混杂模式
		int timeout = 10 * 1000;

		Pcap pcap = Pcap.openLive(device.getName(), snaplen, flags, timeout, errsb);
		if (pcap == null) {
			logger.debug("错误 %s \n", errsb.toString());
			return;
		}

		pcap.loop(0, snifferHandler, "jnetpcap");
		pcap.close();
	}
}
