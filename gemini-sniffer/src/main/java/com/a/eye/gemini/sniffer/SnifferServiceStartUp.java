package com.a.eye.gemini.sniffer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SnifferServiceStartUp {
	private static Logger logger = LogManager.getLogger(SnifferServiceStartUp.class.getName());

	public static void main(String args[]) throws InterruptedException {
		logger.debug("应用启动开始");
		SpringApplication application = new SpringApplication(SnifferServiceStartUp.class);
		application.setWebEnvironment(false);

		// Set<Object> set = new HashSet<Object>();
		// set.add("classpath:applicationContext-service.xml");
		// application.setSources(set);
		application.run(args);
		logger.debug("应用启动结束");
	}
}
