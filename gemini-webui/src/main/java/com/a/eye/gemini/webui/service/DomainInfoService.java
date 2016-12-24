package com.a.eye.gemini.webui.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class DomainInfoService {
	private Logger logger = LogManager.getFormatterLogger(DomainInfoService.class);

	private static Map<String, String> domain = new HashMap<String, String>();

	static {
		domain.put("redmine.asiainfo.com", "CIT项目管理平台");
		domain.put("work.asiainfo.com", "Home");
	}

	public String getDomainName(String dns) {
		return domain.get(dns);
	}
}
