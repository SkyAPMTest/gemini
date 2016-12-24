package com.a.eye.gemini.webui.web;

import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 1800, redisNamespace = "gemini-webui-session")
public class HttpSessionConfig {

}
