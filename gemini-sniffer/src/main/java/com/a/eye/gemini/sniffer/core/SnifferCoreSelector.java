package com.a.eye.gemini.sniffer.core;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.a.eye.gemini.sniffer.cmd.GeminiCmd;

@Component
public class SnifferCoreSelector {

	private Logger logger = LogManager.getFormatterLogger(this.getClass().getName());

	@Autowired
	private SnifferCoreCard coreCard;

	@Autowired
	private SnifferCoreOnline onlineCore;

	@Autowired
	private SnifferCoreOfflineWriter offlineWriterCore;

	@Autowired
	private SnifferCoreOfflineReader offlineReaderCore;

	@PostConstruct
	public void start() {
		Class<?>[] sniffer = GeminiCmd.sniffer;
		for (Class<?> clazz : sniffer) {
			if (clazz != null) {
				logger.info("启动：%s", clazz);
				if (coreCard.getClass() == clazz) {
					coreCard.getDevs();
				}
				if (onlineCore.getClass() == clazz) {
					onlineCore.startCapture();
				}
				if (offlineWriterCore.getClass() == clazz) {
					offlineWriterCore.startCapture();
				}
				if (offlineReaderCore.getClass() == clazz) {
					offlineReaderCore.startCapture();
				}
			}
		}
	}
}
