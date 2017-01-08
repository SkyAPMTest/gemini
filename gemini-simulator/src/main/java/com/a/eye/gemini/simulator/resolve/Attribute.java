package com.a.eye.gemini.simulator.resolve;

public class Attribute<T> implements Comparable<Attribute<T>> {

	private T name;

	private Integer length = 0;

	private Integer order = 0;

	private Enum<DataType> dataType;

	private boolean skipAfterFirstTime = false;

	public Attribute(T name, Integer length, Enum<DataType> dataType, Integer order) {
		this.name = name;
		this.length = length;
		this.dataType = dataType;
		this.order = order;
	}

	public Attribute(T name, Integer length, Enum<DataType> dataType, Integer order, boolean skipAfterFirstTime) {
		this.name = name;
		this.length = length;
		this.dataType = dataType;
		this.order = order;
		this.skipAfterFirstTime = skipAfterFirstTime;
	}

	public T getName() {
		return name;
	}

	public Integer getLength() {
		return length;
	}

	public Integer getOrder() {
		return order;
	}

	public Enum<DataType> getDataType() {
		return dataType;
	}

	public boolean isSkipAfterFirstTime() {
		return skipAfterFirstTime;
	}

	public enum DataType {
		Frame, String, Integer, Hex, Long, Mac, IP, Custom, Data
	}

	@Override
	public int compareTo(Attribute<T> attr) {
		return this.order.compareTo(attr.getOrder());
	}
}
