package com.a.eye.gemini.simulator.resolve;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public abstract class ResolveBase {

	public final static int BIT = 1;

	public final static int BYTE = 8;

	public abstract void dataResolve(String name, InputStream in, long frameId) throws IOException;

	private static Map<String, Map<String, Attribute>> attrMap = new HashMap<String, Map<String, Attribute>>();

	public static void createAttrs(String protocolName, Map<String, Attribute> protocolAttrs) {
		attrMap.put(protocolName, protocolAttrs);
	}

	public static void createInvertedAttrs() {
		for (Map.Entry<String, Map<String, Attribute>> entry : attrMap.entrySet()) {
			entry.getValue().put(Attribute.First, new Attribute(Attribute.First, 0, null, null));
			for (Map.Entry<String, Attribute> attrEntry : entry.getValue().entrySet()) {
				Attribute attr = attrEntry.getValue();
				String parent = attr.getParent();
				if (parent != null) {
					entry.getValue().get(parent).setNext(attr.getName());
				}
			}
		}
	}

	public static Map<String, Map<String, Attribute>> getAttrs() {
		return attrMap;
	}

	public abstract void resolve(String name, InputStream in, long frameId) throws IOException;

	private static Map<String, Object> values = new HashMap<String, Object>();

	public void setValue(String name, Object value) {
		values.put(name, value);
	}

	public String getAsString(String name) {
		return (String) values.get(name);
	}

	public Integer getAsInteger(String name) {
		return (Integer) values.get(name);
	}

	public Long getAsLong(String name) {
		return (Long) values.get(name);
	}

	public byte[] getAsHex(String name) {
		return (byte[]) values.get(name);
	}
}
