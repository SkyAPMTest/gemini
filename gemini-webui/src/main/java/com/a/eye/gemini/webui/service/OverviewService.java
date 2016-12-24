package com.a.eye.gemini.webui.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a.eye.gemini.webui.util.Constants;
import com.a.eye.gemini.webui.util.EsIndexNameUtil;
import com.a.eye.gemini.webui.util.TimeSlotUtil;
import com.a.eye.gemini.webui.vo.IndicatorData;
import com.a.eye.gemini.webui.vo.TodayTrafficVO;

@Service
public class OverviewService {
	private Logger logger = LogManager.getFormatterLogger(OverviewService.class);

	@Autowired
	private IndicatorHostService indicatorHostService;

	public TodayTrafficVO getTodayTraffic(String host) {
		String index = EsIndexNameUtil.getHostIndexName(Constants.Indicator_PV, EsIndexNameUtil.Day);
		IndicatorData pvIndiData = indicatorHostService.getIndicatorData(index, Constants.Indicator_PV, host, TimeSlotUtil.getTodaySlot());
		
		index = EsIndexNameUtil.getHostIndexName(Constants.Indicator_UV, EsIndexNameUtil.Day);
		IndicatorData uvIndiData = indicatorHostService.getIndicatorData(index, Constants.Indicator_UV, host, TimeSlotUtil.getTodaySlot());
		
		index = EsIndexNameUtil.getHostIndexName(Constants.Indicator_IP, EsIndexNameUtil.Day);
		IndicatorData ipIndiData = indicatorHostService.getIndicatorData(index, Constants.Indicator_IP, host, TimeSlotUtil.getTodaySlot());

		TodayTrafficVO todayTrafficVO = new TodayTrafficVO();
		todayTrafficVO.setPvIndiData(pvIndiData);
		todayTrafficVO.setUvIndiData(uvIndiData);
		todayTrafficVO.setIpIndiData(ipIndiData);
		return todayTrafficVO;
	}
}
