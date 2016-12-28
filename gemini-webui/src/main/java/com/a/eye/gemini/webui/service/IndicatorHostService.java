package com.a.eye.gemini.webui.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a.eye.gemini.webui.mongo.IndicatorItemRepository;
import com.a.eye.gemini.webui.vo.IndicatorHostItemData;

@Service
public class IndicatorHostService {
	private Logger logger = LogManager.getFormatterLogger(IndicatorHostService.class);

	@Autowired
	private IndicatorItemRepository repository;

	@Autowired
	private DomainInfoService domainInfoService;

	public IndicatorHostItemData findIndicatorData(String indicator, String host, String timeSlot) {
		logger.info("indicator=%s, host=%s, timeSlot=%s", indicator, host, timeSlot);
		IndicatorHostItemData itemData = repository.findIndicatorData(indicator, host, timeSlot);
		if (itemData == null) {
			itemData = new IndicatorHostItemData();
			itemData.setValue(0l);
		}
		return itemData;
	}
}
