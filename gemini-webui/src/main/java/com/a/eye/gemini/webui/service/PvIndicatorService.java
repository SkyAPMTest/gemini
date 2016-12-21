package com.a.eye.gemini.webui.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a.eye.gemini.webui.util.ElasticSearchFactoryBean;

@Service
public class PvIndicatorService {
	private Logger logger = LogManager.getFormatterLogger(PvIndicatorService.class);

	@Autowired
	private ElasticSearchFactoryBean factory;

	public void getSystemRanking() {
		SearchRequestBuilder searchRequestBuilder = factory.getClient().prepareSearch("func_idx");
		searchRequestBuilder.setTypes("func");
		searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);

	}
}
