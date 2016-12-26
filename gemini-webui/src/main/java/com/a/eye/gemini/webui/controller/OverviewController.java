package com.a.eye.gemini.webui.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.a.eye.gemini.webui.model.TrafficVO;
import com.a.eye.gemini.webui.service.OverviewService;
import com.a.eye.gemini.webui.web.ControllerBase;
import com.google.gson.Gson;

@Controller
public class OverviewController extends ControllerBase {

	private Logger logger = LogManager.getFormatterLogger(OverviewController.class);

	@Autowired
	private OverviewService dashboardService;

	private Gson gson = new Gson();

	@RequestMapping(value = "getTraffic", method = RequestMethod.GET)
	@ResponseBody
	public void getTraffic(@ModelAttribute("host") String host, HttpServletResponse response) throws IOException {
		logger.debug("host=%s", host);
		TrafficVO data = dashboardService.getTraffic(host);
		reply(gson.toJson(data), response);
	}
}
