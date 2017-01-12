package com.a.eye.gemini.simulator.resolve;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.a.eye.gemini.simulator.resolve.Attribute.DataType;

public class PcapARPStructResolve extends GeminiResolveLowBase {

	public static final String Name = "ARP";

	public static PcapARPStructResolve inst = new PcapARPStructResolve();

	public static final String hardware_type = "hardware_type";
	public static final String protocol_type = "protocol_type";
	public static final String hardware_address_length = "hardware_address_length";
	public static final String protocol_address_length = "protocol_address_length";
	public static final String operation = "operation";
	public static final String sender_hardware_address = "sender_hardware_address";
	public static final String sender_protocol_address = "sender_protocol_address";
	public static final String target_hardware_address = "target_hardware_address";
	public static final String target_protocol_address = "target_protocol_address";

	static {
		Map<String, Attribute> arpAttrs = new HashMap<String, Attribute>();
		arpAttrs.put(hardware_type, new Attribute(hardware_type, 2 * BYTE, DataType.Integer, Attribute.First));
		arpAttrs.put(protocol_type, new Attribute(protocol_type, 2 * BYTE, DataType.Integer, hardware_type));
		arpAttrs.put(hardware_address_length, new Attribute(hardware_address_length, 1 * BYTE, DataType.Integer, protocol_type));
		arpAttrs.put(protocol_address_length, new Attribute(protocol_address_length, 1 * BYTE, DataType.Integer, hardware_address_length));
		arpAttrs.put(operation, new Attribute(operation, 2 * BYTE, DataType.Integer, protocol_address_length));
		arpAttrs.put(sender_hardware_address, new Attribute(sender_hardware_address, 6 * BYTE, DataType.Mac, operation));
		arpAttrs.put(sender_protocol_address, new Attribute(sender_protocol_address, 4 * BYTE, DataType.IP, sender_hardware_address));
		arpAttrs.put(target_hardware_address, new Attribute(target_hardware_address, 6 * BYTE, DataType.Mac, sender_protocol_address));
		arpAttrs.put(target_protocol_address, new Attribute(target_protocol_address, 4 * BYTE, DataType.IP, target_hardware_address));
		createAttrs(Name, arpAttrs);
	}

	@Override
	public void dataResolve(String name, InputStream in, long frameId) throws IOException {
	}

	@Override
	public void custom(Attribute attr, Long frameId, InputStream in) {
	}
}
