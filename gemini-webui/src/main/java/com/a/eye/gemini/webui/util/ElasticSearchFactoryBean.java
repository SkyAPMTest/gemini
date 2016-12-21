package com.a.eye.gemini.webui.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ElasticSearchFactoryBean {

	private Logger logger = LogManager.getLogger(ElasticSearchFactoryBean.class.getName());

	private TransportClient client;

	@Value("${elasticsearch.servers}")
	private String servers;

	@PostConstruct
	public void init() throws UnknownHostException {
		logger.info("开始初始化ElasticSearch的Client");
		Settings settings = Settings.builder().put("cluster.name", "asiasearch").put("client.transport.sniff", true).build();
		// Settings settings = Settings.builder().put("client.transport.sniff",
		// true).build();

		String[] serverList = servers.split(",");
		PreBuiltTransportClient preBuiltTransportClient = new PreBuiltTransportClient(settings);
		client = preBuiltTransportClient;
		for (String server : serverList) {
			String[] addr = server.split(":");
			client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(addr[0]), Integer.parseInt(addr[1])));
		}

		// client = new
		// PreBuiltTransportClient(settings).addTransportAddress(new
		// InetSocketTransportAddress(InetAddress.getByName("localhost"),
		// 9300));

		logger.info("初始化ElasticSearch的Client完成");
	}

	public TransportClient getClient() {
		return client;
	}
}
