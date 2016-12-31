package com.a.eye.gemini.webui.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a.eye.gemini.webui.mongo.IndicatorItemRepository;
import com.a.eye.gemini.webui.vo.IndicatorData;

@Service
public class IndicatorService {

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
