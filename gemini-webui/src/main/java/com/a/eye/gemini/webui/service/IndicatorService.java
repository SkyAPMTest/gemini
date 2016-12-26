package com.a.eye.gemini.webui.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a.eye.gemini.webui.mongo.IndicatorItemRepository;
import com.a.eye.gemini.webui.vo.IndicatorData;
import com.a.eye.gemini.webui.vo.IndicatorItemData;

@Service
public class IndicatorService {
	private Logger logger = LogManager.getFormatterLogger(IndicatorService.class);

	@Autowired
	private IndicatorItemRepository repository;

	public IndicatorData getIndicatorData(String collect, String host, String timeSlot) {
//		List<IndicatorItemData> itemDataList = repository.findIndicatorData(host, timeSlot);
//
//		IndicatorData indicatorData = new IndicatorData();
//		logger.info("collect=%s, host=%s, timeSlot=%s, count=%d", collect, host, timeSlot, itemDataList.size());
//		indicatorData.setCount(itemDataList.size());
//		indicatorData.setItems(itemDataList);

		return null;
	}
}
