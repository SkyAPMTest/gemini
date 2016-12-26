package com.a.eye.gemini.sniffer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.a.eye.gemini.sniffer.setting.GeminiSettings;

@SpringBootApplication
@EnableConfigurationProperties({GeminiSettings.class})
public class GeminiSnifferStartUp {
	private static Logger logger = LogManager.getLogger(GeminiSnifferStartUp.class.getName());

	public static void main(String args[]) throws InterruptedException {
		logger.debug("应用启动开始");
		SpringApplication application = new SpringApplication(GeminiSnifferStartUp.class);
		application.setWebEnvironment(false);

		// Set<Object> set = new HashSet<Object>();
		// set.add("classpath:applicationContext-service.xml");
		// application.setSources(set);
		application.run(args);
		logger.debug("应用启动结束");
	}
}
