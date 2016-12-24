package com.a.eye.gemini.webui.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a.eye.gemini.webui.util.ElasticSearchFactoryBean;
import com.a.eye.gemini.webui.vo.IndicatorData;
import com.a.eye.gemini.webui.vo.IndicatorHostItemData;

@Service
public class IndicatorHostService {
	private Logger logger = LogManager.getFormatterLogger(IndicatorHostService.class);

	@Autowired
	private ElasticSearchFactoryBean factory;

	@Autowired
	private DomainInfoService domainInfoService;

	public IndicatorData getIndicatorData(String index, String type, String host, String timeSlot) {
		logger.debug("index=%s, type=%s, host=%s, timeSlot=%s", index, type, host, timeSlot);
		SearchRequestBuilder searchRequestBuilder = factory.getClient().prepareSearch(index);
		searchRequestBuilder.setTypes(type);
		searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);

		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		queryBuilder.must(QueryBuilders.matchQuery("host", host));
		queryBuilder.must(QueryBuilders.matchQuery("timeSlot", timeSlot));

		searchRequestBuilder.setQuery(queryBuilder);

		SearchResponse response = searchRequestBuilder.execute().actionGet();
		logger.info("条数：%d", response.getHits().totalHits());

		IndicatorData indicatorData = new IndicatorData();
		indicatorData.setCount(response.getHits().totalHits());

		for (SearchHit hit : response.getHits().getHits()) {
			IndicatorHostItemData item = new IndicatorHostItemData();
			item.setHost(hit.getSource().get("host").toString());
			item.setTimeSlot(hit.getSource().get("timeSlot").toString());
			item.setAnalysisVal(hit.getSource().get("analysisVal").toString());
			item.setHostName(domainInfoService.getDomainName(item.getHost()));
			indicatorData.getItems().add(item);
		}
		return indicatorData;
	}
}
