package com.a.eye.gemini.webui.vo;

import java.util.ArrayList;
import java.util.List;

public class IndicatorData {

	private long count;

	private List<ItemData> items = new ArrayList<ItemData>();

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public List<ItemData> getItems() {
		return items;
	}

	public void setItems(List<ItemData> items) {
		this.items = items;
	}
}
