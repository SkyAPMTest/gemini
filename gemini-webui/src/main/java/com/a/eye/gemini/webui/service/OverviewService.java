package com.a.eye.gemini.webui.service;

import java.util.Calendar;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
		today.setCost(costIndiDataToday.getValue() == 0 ? 0 : costIndiDataToday.getValue() / pvIndiDataToday.getValue());

		TrafficItemVO yesterday = new TrafficItemVO();
		IndicatorHostItemData pvIndiDataYesterday = indicatorHostService.findIndicatorData(CollectionNameConstants.INDICATOR_HOST_DAY_PV, host, TimeSlotUtil.getYesterdaySlot());
		yesterday.setPv(pvIndiDataYesterday.getValue());
		IndicatorHostItemData uvIndiDataYesterday = indicatorHostService.findIndicatorData(CollectionNameConstants.INDICATOR_HOST_DAY_UV, host, TimeSlotUtil.getYesterdaySlot());
		yesterday.setUv(uvIndiDataYesterday.getValue());
		IndicatorHostItemData ipIndiDataYesterday = indicatorHostService.findIndicatorData(CollectionNameConstants.INDICATOR_HOST_DAY_IP, host, TimeSlotUtil.getYesterdaySlot());
		yesterday.setIp(ipIndiDataYesterday.getValue());
		IndicatorHostItemData costIndiDataYesterday = indicatorHostService.findIndicatorData(CollectionNameConstants.INDICATOR_HOST_DAY_COST, host, TimeSlotUtil.getYesterdaySlot());
		yesterday.setCost(costIndiDataYesterday.getValue() == 0 ? 0 : costIndiDataYesterday.getValue() / pvIndiDataYesterday.getValue());

		TrafficVO trafficVO = new TrafficVO();
		trafficVO.setToday(today);
		trafficVO.setYesterday(yesterday);
		return trafficVO;
	}

	public JsonArray getTrendChartHour(String host, String indicator, int minusDay, String check1, String check2) {
		JsonArray data = new JsonArray();

		JsonObject one = this.getTrendOneSlotChartHour(0 + minusDay, host, indicator);
		data.add(one);

		if (!StringUtils.isEmpty(check1)) {
			Integer minusCheck1 = Integer.parseInt(check1);
			JsonObject two = this.getTrendOneSlotChartHour(minusDay + minusCheck1, host, indicator);
			two.remove("categories");
			data.add(two);

			JsonArray legend = one.get("legend").getAsJsonArray();
			legend.addAll(two.get("legend").getAsJsonArray());
			one.add("legend", legend);
		}
		if (!StringUtils.isEmpty(check2)) {
			Integer minusCheck2 = Integer.parseInt(check2);
			JsonObject three = this.getTrendOneSlotChartHour(minusDay + minusCheck2, host, indicator);
			three.remove("categories");
			data.add(three);

			JsonArray legend = one.get("legend").getAsJsonArray();
			legend.addAll(three.get("legend").getAsJsonArray());
			one.add("legend", legend);
		}

		return data;
	}

	public JsonArray getTrendChartDay(String host, String indicator, int minusDay) {
		JsonArray data = new JsonArray();

		JsonObject one = this.getTrendOneSlotChartDay(minusDay, host, indicator);
		data.add(one);

		return data;
	}

	public JsonObject getTrendOneSlotChartHour(int minusDay, String host, String indicator) {
		JsonObject reply = new JsonObject();

		JsonArray categories = new JsonArray();
		JsonArray data = new JsonArray();
		JsonArray legend = new JsonArray();

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(new Date().getTime());
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - minusDay);

		String name = DateUtil.data2String(DateUtil.dayDf, calendar.getTimeInMillis());
		legend.add(name);

		String[][] hours = TimeSlotUtil.getHours24(minusDay);

		for (String[] hour : hours) {
			if (Constants.Indicator_PV.equals(indicator)) {
				IndicatorHostItemData pvHour = indicatorHostService.findIndicatorData(CollectionNameConstants.INDICATOR_HOST_HOUR_PV, host, hour[0]);
				data.add(pvHour.getValue());
				categories.add(hour[1]);
			}
			if (Constants.Indicator_UV.equals(indicator)) {
				IndicatorHostItemData pvHour = indicatorHostService.findIndicatorData(CollectionNameConstants.INDICATOR_HOST_HOUR_UV, host, hour[0]);
				data.add(pvHour.getValue());
				categories.add(hour[1]);
			}
			if (Constants.Indicator_IP.equals(indicator)) {
				IndicatorHostItemData pvHour = indicatorHostService.findIndicatorData(CollectionNameConstants.INDICATOR_HOST_HOUR_IP, host, hour[0]);
				data.add(pvHour.getValue());
				categories.add(hour[1]);
			}
			if (Constants.Indicator_COST.equals(indicator)) {
				IndicatorHostItemData costHour = indicatorHostService.findIndicatorData(CollectionNameConstants.INDICATOR_HOST_HOUR_COST, host, hour[0]);
				IndicatorHostItemData pvHour = indicatorHostService.findIndicatorData(CollectionNameConstants.INDICATOR_HOST_HOUR_PV, host, hour[0]);
				data.add(costHour.getValue() == 0 ? 0 : costHour.getValue() / pvHour.getValue());

				categories.add(hour[1]);
			}
		}
		reply.add("legend", legend);
		reply.addProperty("name", name);
		reply.add("data", data);
		reply.add("categories", categories);
		return reply;
	}

	public JsonObject getTrendOneSlotChartDay(int minusDay, String host, String indicator) {
		JsonObject reply = new JsonObject();

		JsonArray categories = new JsonArray();
		JsonArray data = new JsonArray();
		JsonArray legend = new JsonArray();

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(new Date().getTime());
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - minusDay);

		String[][] days = TimeSlotUtil.getSlotDays(minusDay);

		for (String[] day : days) {
			if (Constants.Indicator_PV.equals(indicator)) {
				IndicatorHostItemData pvHour = indicatorHostService.findIndicatorData(CollectionNameConstants.INDICATOR_HOST_DAY_PV, host, day[0]);
				data.add(pvHour.getValue());
				categories.add(day[1]);
			}
			if (Constants.Indicator_UV.equals(indicator)) {
				IndicatorHostItemData pvHour = indicatorHostService.findIndicatorData(CollectionNameConstants.INDICATOR_HOST_DAY_UV, host, day[0]);
				data.add(pvHour.getValue());
				categories.add(day[1]);
			}
			if (Constants.Indicator_IP.equals(indicator)) {
				IndicatorHostItemData pvHour = indicatorHostService.findIndicatorData(CollectionNameConstants.INDICATOR_HOST_DAY_IP, host, day[0]);
				data.add(pvHour.getValue());
				categories.add(day[1]);
			}
			if (Constants.Indicator_COST.equals(indicator)) {
				IndicatorHostItemData costHour = indicatorHostService.findIndicatorData(CollectionNameConstants.INDICATOR_HOST_DAY_COST, host, day[0]);
				IndicatorHostItemData pvHour = indicatorHostService.findIndicatorData(CollectionNameConstants.INDICATOR_HOST_DAY_PV, host, day[0]);
				data.add(costHour.getValue() == 0 ? 0 : costHour.getValue() / pvHour.getValue());
				categories.add(day[1]);
			}
		}
		reply.add("legend", legend);
		reply.add("data", data);
		reply.add("categories", categories);
		return reply;
	}
}
