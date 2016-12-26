package com.a.eye.gemini.sniffer.core;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jnetpcap.Pcap;
import org.jnetpcap.protocol.JProtocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.a.eye.gemini.sniffer.handler.SnifferOnlineHandler;
import com.a.eye.gemini.sniffer.setting.GeminiSettings;

@Component
public class SnifferCoreOfflineReader extends SnifferCoreBase {

	private Logger logger = LogManager.getFormatterLogger(this.getClass().getName());

	@Autowired
	private GeminiSettings settings;

	@Autowired
	private SnifferOnlineHandler handler;

	@Override
	public void startCapture() {
		StringBuilder errbuf = new StringBuilder();
		File pcapFiles = new File(settings.getFilePath());
		String[] pcapFilelist = pcapFiles.list();
		for (String file : pcapFilelist) {
			String absolutePath = settings.getFilePath() + "/" + file;
			logger.info("读取文件：%s", absolutePath);
			Pcap pcap = Pcap.openOffline(absolutePath, errbuf);
			this.loop(pcap);
		}
	}

	@Override
	protected void loop(Pcap pcap) {
		pcap.loop(0, JProtocol.ETHERNET_ID, handler, "gemini");
	}
}
