package com.a.eye.gemini.sniffer.core;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.a.eye.gemini.sniffer.setting.GeminiSettings;

@Component
public class SnifferCoreSelector {

	private Logger logger = LogManager.getFormatterLogger(this.getClass().getName());

	private static final String On = "on";

	@Autowired
	private GeminiSettings settings;

	@Autowired
	private SnifferCoreOnline onlineCore;

	@Autowired
	private SnifferCoreOfflineWriter offlineWriterCore;

	@Autowired
	private SnifferCoreOfflineReader offlineReaderCore;

	@PostConstruct
	public void start() {
		if (On.equals(settings.getOnline().toLowerCase())) {
			onlineCore.startCapture();
			logger.info("开启在线处理模式");
		}
		if (On.equals(settings.getOfflineWriter().toLowerCase())) {
			offlineWriterCore.startCapture();
			logger.info("开启离线写入模式");
		}
		if (On.equals(settings.getOfflineReader().toLowerCase())) {
			offlineReaderCore.startCapture();
			logger.info("开启离线读取模式");
		}
	}
}
