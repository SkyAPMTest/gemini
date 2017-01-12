package com.a.eye.gemini.sniffer.util;

import java.util.concurrent.atomic.AtomicInteger;

public class CallCountUtil {

	private static AtomicInteger atomicInteger = new AtomicInteger(0);

	public static void increment() {
		atomicInteger.getAndIncrement();
	}
}
