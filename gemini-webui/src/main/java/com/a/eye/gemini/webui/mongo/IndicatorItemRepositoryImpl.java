package com.a.eye.gemini.webui.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.a.eye.gemini.webui.vo.IndicatorHostItemData;

@Component
public class IndicatorItemRepositoryImpl implements IndicatorItemRepository {

	@Autowired
	MongoOperations mongoOperations;

	@Override
	public IndicatorHostItemData findIndicatorData(String indicator, String host, String timeSlot) {
		return mongoOperations.findOne(Query.query(Criteria.where("host").is(host).and("timeSlot").is(timeSlot)), IndicatorHostItemData.class, indicator);
	}
}
