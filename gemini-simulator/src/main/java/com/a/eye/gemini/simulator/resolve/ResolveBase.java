package com.a.eye.gemini.simulator.resolve;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ResolveBase<T> {

	public final static int BIT = 1;

	public final static int BYTE = 8;
	
	public abstract String getDataResolve();

	protected List<Attribute<T>> attrList = new ArrayList<Attribute<T>>();

	public abstract void resolve(InputStream in, long frameId) throws IOException;

	private Map<T, Object> values = new HashMap<T, Object>();

	public void setValue(T name, Object value) {
		values.put(name, value);
	}

	public String getAsString(T name) {
		return (String) values.get(name);
	}

	public Integer getAsInteger(T name) {
		return (Integer) values.get(name);
	}

	public Long getAsLong(T name) {
		return (Long) values.get(name);
	}
}
