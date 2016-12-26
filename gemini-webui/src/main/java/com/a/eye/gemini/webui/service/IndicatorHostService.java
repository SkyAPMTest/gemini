package com.a.eye.gemini.webui.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a.eye.gemini.webui.mongo.IndicatorItemRepository;

@Service
public class IndicatorHostService {
	private Logger logger = LogManager.getFormatterLogger(IndicatorHostService.class);

	@Autowired
	private IndicatorItemRepository repository;

	@Autowired
	private DomainInfoService domainInfoService;

	public <T> T getIndicatorData(Class<T> entityClass, String host, String timeSlot) {
		logger.info("host=%s, timeSlot=%s", host, timeSlot);
		T entity = repository.findIndicatorData(entityClass, host, timeSlot);
		return entity;
	}
}
