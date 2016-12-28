package com.a.eye.gemini.webui.service;

import java.util.Calendar;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a.eye.gemini.webui.model.TrafficItemVO;
import com.a.eye.gemini.webui.model.TrafficVO;
import com.a.eye.gemini.webui.mongo.CollectionNameConstants;
import com.a.eye.gemini.webui.util.Constants;
import com.a.eye.gemini.webui.util.DateUtil;
import com.a.eye.gemini.webui.util.TimeSlotUtil;
import com.a.eye.gemini.webui.vo.IndicatorHostItemData;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service
public class OverviewService {
	private Logger logger = LogManager.getFormatterLogger(OverviewService.class);

	@Autowired
	private IndicatorHostService indicatorHostService;

	public TrafficVO getTraffic(String host) {
		TrafficItemVO today = new TrafficItemVO();
		IndicatorHostItemData pvIndiDataToday = indicatorHostService.findIndicatorData(CollectionNameConstants.INDICATOR_HOST_DAY_PV, host, TimeSlotUtil.getTodaySlot());
		today.setPv(pvIndiDataToday.getValue());
		IndicatorHostItemData uvIndiDataToday = indicatorHostService.findIndicatorData(CollectionNameConstants.INDICATOR_HOST_DAY_UV, host, TimeSlotUtil.getTodaySlot());
		today.setUv(uvIndiDataToday.getValue());
		IndicatorHostItemData ipIndiDataToday = indicatorHostService.findIndicatorData(CollectionNameConstants.INDICATOR_HOST_DAY_IP, host, TimeSlotUtil.getTodaySlot());
		today.setIp(ipIndiDataToday.getValue());
		IndicatorHostItemData costIndiDataToday = indicatorHostService.findIndicatorData(CollectionNameConstants.INDICATOR_HOST_DAY_COST, host, TimeSlotUtil.getTodaySlot());
		logger.debug("今日PV值：%s，今日cost值：%s", pvIndiDataToday.getValue(), costIndiDataToday.getValue());
		today.setCost(costIndiDataToday.getValue() / pvIndiDataToday.getValue());

		TrafficItemVO yesterday = new TrafficItemVO();
		IndicatorHostItemData pvIndiDataYesterday = indicatorHostService.findIndicatorData(CollectionNameConstants.INDICATOR_HOST_DAY_PV, host, TimeSlotUtil.getYesterdaySlot());
		yesterday.setPv(pvIndiDataYesterday.getValue());
		IndicatorHostItemData uvIndiDataYesterday = indicatorHostService.findIndicatorData(CollectionNameConstants.INDICATOR_HOST_DAY_UV, host, TimeSlotUtil.getYesterdaySlot());
		yesterday.setUv(uvIndiDataYesterday.getValue());
		IndicatorHostItemData ipIndiDataYesterday = indicatorHostService.findIndicatorData(CollectionNameConstants.INDICATOR_HOST_DAY_IP, host, TimeSlotUtil.getYesterdaySlot());
		yesterday.setIp(ipIndiDataYesterday.getValue());
		IndicatorHostItemData costIndiDataYesterday = indicatorHostService.findIndicatorData(CollectionNameConstants.INDICATOR_HOST_DAY_COST, host, TimeSlotUtil.getYesterdaySlot());
		yesterday.setCost(costIndiDataYesterday.getValue() / pvIndiDataYesterday.getValue());

		TrafficVO trafficVO = new TrafficVO();
		trafficVO.setToday(today);
		trafficVO.setYesterday(yesterday);
		return trafficVO;
	}

	public JsonObject getTrendChart(String host, String indicator) {
		JsonObject reply = new JsonObject();

		JsonObject today = this.getTrendOneSlotChart(0, host, indicator);
		reply.add("today", today);

		JsonObject yesterday = this.getTrendOneSlotChart(1, host, indicator);
		reply.add("yesterday", yesterday);

		JsonArray legend = new JsonArray();
		legend.addAll(today.get("legend").getAsJsonArray());
		legend.addAll(yesterday.get("legend").getAsJsonArray());
		reply.add("legend", legend);

		return reply;
	}

	public JsonObject getTrendOneSlotChart(int minusDay, String host, String indicator) {
		JsonObject reply = new JsonObject();

		JsonArray categories = new JsonArray();
		JsonArray data = new JsonArray();
		JsonArray legend = new JsonArray();

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(new Date().getTime());
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - minusDay);
		legend.add(DateUtil.data2String(DateUtil.dayDf, calendar.getTimeInMillis()));

		String[][] hours = TimeSlotUtil.getHours24(minusDay);

		for (String[] hour : hours) {
			if (Constants.Indicator_PV.equals(indicator)) {
				IndicatorHostItemData pvHour = indicatorHostService.findIndicatorData(CollectionNameConstants.INDICATOR_HOST_HOUR_PV, host, hour[0]);
				data.add(pvHour.getValue());
				categories.add(hour[1]);
			}
		}
		reply.add("legend", legend);
		reply.add("data", data);
		reply.add("categories", categories);
		return reply;
	}
}
