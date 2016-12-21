package com.a.eye.gemini.webui.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a.eye.gemini.webui.util.ElasticSearchFactoryBean;
import com.a.eye.gemini.webui.vo.IndicatorData;
import com.a.eye.gemini.webui.vo.IndicatorItemData;

@Service
public class IndicatorService {
	private Logger logger = LogManager.getFormatterLogger(IndicatorService.class);

	@Autowired
	private ElasticSearchFactoryBean factory;

	public void getIndicatorData() {
		SearchRequestBuilder searchRequestBuilder = factory.getClient().prepareSearch("func_idx");
		searchRequestBuilder.setTypes("func");
		searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		searchRequestBuilder.setQuery(QueryBuilders.multiMatchQuery(content, "name_cn"));

		SearchResponse response = searchRequestBuilder.execute().actionGet();
		logger.info("条数：%d", response.getHits().totalHits());

		IndicatorData indicatorData = new IndicatorData();
		indicatorData.setCount(response.getHits().totalHits());

		for (SearchHit hit : response.getHits().getHits()) {
			IndicatorItemData item = new IndicatorItemData();
			item.setHost(hit.getSource().get("host").toString());
			result.getItem().add(item);
		}
		return result;
	}
}
