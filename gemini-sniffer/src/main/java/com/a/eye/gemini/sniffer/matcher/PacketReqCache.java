package com.a.eye.gemini.sniffer.matcher;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;

@Component
public class PacketReqCache {

	private int counter = 0;

	private static int windowSize = 3;

	private int interval = 1000 * 60 * 5;

	private int currentWindow = 1;

	private long currentWindowTime = System.currentTimeMillis();

	private static Map<Integer, Map<Long, JsonObject>> reqCache = new HashMap<Integer, Map<Long, JsonObject>>();

	static {
		for (int i = 1; i <= windowSize; i++) {
			reqCache.put(i, new HashMap<Long, JsonObject>());
		}
	}

	public void cacheData(Long reqAckSeq, JsonObject reqData) {
		counter++;
		changeWindow();
		reqCache.get(currentWindow).put(reqAckSeq, reqData);
	}

	private void changeWindow() {
		if (counter % 1000 == 0) {
			long now = System.currentTimeMillis();
			if ((now - currentWindowTime) > interval) {
				if (currentWindow == windowSize) {
					currentWindow = 1;
				} else {
					currentWindow++;
				}
			}
			reqCache.remove(currentWindow);
			reqCache.put(currentWindow, new HashMap<Long, JsonObject>());
		}
	}

	public JsonObject findPair(Long resSeq) {
		for (int i = 1; i < windowSize; i++) {
			Map<Long, JsonObject> windowData = reqCache.get(i);
			if (windowData.containsKey(resSeq)) {
				JsonObject reqData = windowData.get(resSeq);
				reqCache.remove(reqData);
				return reqData;
			}
		}
		return null;
	}
}
