package com.a.eye.gemini.webui.web;

import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

public class SessionInitializer extends AbstractHttpSessionApplicationInitializer {

	public SessionInitializer() {
		super(HttpSessionConfig.class);
	}
}
