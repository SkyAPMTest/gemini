package com.a.eye.gemini.webui.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class IndicatorItemRepositoryImpl implements IndicatorItemRepository {

	@Autowired
	MongoOperations mongoOperations;

	@Override
	public <T> T findIndicatorData(Class<T> entityClass, String host, String timeSlot) {
		return mongoOperations.findOne(Query.query(Criteria.where("host").is(host).and("timeSlot").is(timeSlot)), entityClass);
	}
}
