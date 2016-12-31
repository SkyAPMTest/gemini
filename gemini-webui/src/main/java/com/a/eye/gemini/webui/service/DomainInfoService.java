package com.a.eye.gemini.webui.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class DomainInfoService {

	private static Map<String, String> domain = new HashMap<String, String>();

	static {
		domain.put("redmine.asiainfo.com", "CIT项目管理平台");
		domain.put("work.asiainfo.com", "Home");
	}

	public String getDomainName(String dns) {
		return domain.get(dns);
	}
}
