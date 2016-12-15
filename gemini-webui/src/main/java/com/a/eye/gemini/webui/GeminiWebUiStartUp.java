package com.a.eye.gemini.webui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@ComponentScan("com.a.eye.gemini.webui")
public class GeminiWebUiStartUp extends SpringBootServletInitializer {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(GeminiWebUiStartUp.class, args);
	}
}
