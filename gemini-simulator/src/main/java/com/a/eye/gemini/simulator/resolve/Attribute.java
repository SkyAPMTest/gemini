package com.a.eye.gemini.simulator.resolve;

public class Attribute {

	public static final String First = "first_node";

	private String name;

	private Integer length = 0;

	private String parent;

	private String next;

	private Enum<DataType> dataType;

	private boolean skipAfterFirstTime = false;

	public Attribute(String name, Integer length, Enum<DataType> dataType, String parent) {
		this.name = name;
		this.parent = parent;
		this.length = length;
		this.dataType = dataType;
	}

	public Attribute(String name, Integer length, Enum<DataType> dataType, String parent, boolean skipAfterFirstTime) {
		this.name = name;
		this.parent = parent;
		this.length = length;
		this.dataType = dataType;
		this.skipAfterFirstTime = skipAfterFirstTime;
	}

	public String getName() {
		return name;
	}

	public Integer getLength() {
		return length;
	}

	public String getParent() {
		return parent;
	}

	public Enum<DataType> getDataType() {
		return dataType;
	}

	public boolean isSkipAfterFirstTime() {
		return skipAfterFirstTime;
	}

	public String getNext() {
		return next;
	}

	public void setNext(String next) {
		this.next = next;
	}

	public enum DataType {
		Frame, String, Integer, Hex, Long, Mac, IP, Custom, Data
	}
}
