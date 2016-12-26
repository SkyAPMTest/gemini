package com.a.eye.gemini.webui.mongo;

import org.springframework.stereotype.Repository;

@Repository
public interface IndicatorItemRepository {
	<T> T findIndicatorData(Class<T> entityClass, String host, String timeSlot);
}