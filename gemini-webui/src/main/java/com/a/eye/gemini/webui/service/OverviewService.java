package com.a.eye.gemini.webui.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a.eye.gemini.webui.model.TrafficItemVO;
import com.a.eye.gemini.webui.model.TrafficVO;
import com.a.eye.gemini.webui.util.TimeSlotUtil;
import com.a.eye.gemini.webui.vo.IndiHostDayIpItemData;
import com.a.eye.gemini.webui.vo.IndiHostDayPvItemData;
import com.a.eye.gemini.webui.vo.IndiHostDayUvItemData;

@Service
public class OverviewService {
	private Logger logger = LogManager.getFormatterLogger(OverviewService.class);

	@Autowired
	private IndicatorHostService indicatorHostService;

	public TrafficVO getTraffic(String host) {
		TrafficItemVO today = new TrafficItemVO();
		IndiHostDayPvItemData pvIndiDataToday = indicatorHostService.getIndicatorData(IndiHostDayPvItemData.class, host, TimeSlotUtil.getTodaySlot());
		today.setPv(pvIndiDataToday.getAnalysisVal());
		IndiHostDayUvItemData uvIndiDataToday = indicatorHostService.getIndicatorData(IndiHostDayUvItemData.class, host, TimeSlotUtil.getTodaySlot());
		today.setUv(uvIndiDataToday.getAnalysisVal());
		IndiHostDayIpItemData ipIndiDataToday = indicatorHostService.getIndicatorData(IndiHostDayIpItemData.class, host, TimeSlotUtil.getTodaySlot());
		today.setIp(ipIndiDataToday.getAnalysisVal());

		TrafficItemVO yesterday = new TrafficItemVO();
		IndiHostDayPvItemData pvIndiDataYesterday = indicatorHostService.getIndicatorData(IndiHostDayPvItemData.class, host, TimeSlotUtil.getYesterdaySlot());
		yesterday.setPv(pvIndiDataYesterday.getAnalysisVal());
		IndiHostDayUvItemData uvIndiDataYesterday = indicatorHostService.getIndicatorData(IndiHostDayUvItemData.class, host, TimeSlotUtil.getYesterdaySlot());
		yesterday.setPv(uvIndiDataYesterday.getAnalysisVal());
		IndiHostDayIpItemData ipIndiDataYesterday = indicatorHostService.getIndicatorData(IndiHostDayIpItemData.class, host, TimeSlotUtil.getYesterdaySlot());
		yesterday.setPv(ipIndiDataYesterday.getAnalysisVal());

		TrafficVO trafficVO = new TrafficVO();
		trafficVO.setToday(today);
		trafficVO.setYesterday(yesterday);
		return trafficVO;
	}
}
