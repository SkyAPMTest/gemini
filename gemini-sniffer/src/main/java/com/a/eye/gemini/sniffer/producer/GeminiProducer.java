package com.a.eye.gemini.sniffer.producer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.a.eye.gemini.sniffer.cmd.GeminiCmd;

@Component
public class GeminiProducer {

	private static String kafkaproperty = "properties/kafka.properties";
	private static Properties properties = new Properties();
	private static KafkaProducer<Long, byte[]> producer;

	static {
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(kafkaproperty);
		try {
			properties.load(inputStream);
			if (!StringUtils.isEmpty(GeminiCmd.Cmd_K_Value)) {
				properties.setProperty("bootstrap.servers", GeminiCmd.Cmd_K_Value);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		producer = new KafkaProducer<Long, byte[]>(properties);
	}

	public KafkaProducer<Long, byte[]> getProducer() {
		return producer;
	}
}
