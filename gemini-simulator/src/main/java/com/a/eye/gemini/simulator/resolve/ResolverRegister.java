package com.a.eye.gemini.simulator.resolve;

import java.util.HashMap;
import java.util.Map;

public class ResolverRegister {

	private static Map<String, ResolveBase> resolves = new HashMap<String, ResolveBase>();

	public static void registeResolve(String name, ResolveBase resolve) {
		resolves.put(name, resolve);
	}

	public static ResolveBase getResolve(String name) {
		return resolves.get(name);
	}
}
