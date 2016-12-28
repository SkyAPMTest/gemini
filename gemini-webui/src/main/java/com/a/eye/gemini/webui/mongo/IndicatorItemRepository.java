package com.a.eye.gemini.webui.mongo;

import org.springframework.stereotype.Repository;

import com.a.eye.gemini.webui.vo.IndicatorHostItemData;

@Repository
public interface IndicatorItemRepository {
	public IndicatorHostItemData findIndicatorData(String indicator, String host, String timeSlot);
}