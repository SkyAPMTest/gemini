package com.a.eye.gemini.sniffer;

import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.a.eye.gemini.sniffer.cmd.GeminiCmd;

@SpringBootApplication
public class GeminiSnifferStartUp {
	private static Logger logger = LogManager.getLogger(GeminiSnifferStartUp.class.getName());

	public static void main(String args[]) throws ParseException {
		Boolean runable = GeminiCmd.parse(args);
		if (runable) {
			logger.debug("应用启动开始");
			SpringApplication application = new SpringApplication(GeminiSnifferStartUp.class);
			application.setWebEnvironment(false);
			application.run(args);
			logger.debug("应用启动结束");
		}
	}
}
