package com.a.eye.gemini.simulator.resolve;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.a.eye.gemini.simulator.Mapping;
import com.a.eye.gemini.simulator.resolve.Attribute.DataType;

public class PcapIPv6StructResolve extends GeminiResolveLowBase {

	public static final String Name = "IPv6";

	private PcapIPv6StructResolve() {
	}

	public static PcapIPv6StructResolve inst = new PcapIPv6StructResolve();

	public static final String version = "version";
	public static final String traffic_class = "traffic_class";
	public static final String flow_label = "flow_label";
	public static final String payload_length = "payload_length";
	public static final String next_header = "next_header";
	public static final String hop_limit = "hop_limit";
	public static final String destination = "destination";
	public static final String source = "source";
	public static final String data = "data";

	static {
		Map<String, Attribute> ipv4Attrs = new HashMap<String, Attribute>();
		ipv4Attrs.put(version, new Attribute(version, 4 * BIT, DataType.Integer, Attribute.First));
		ipv4Attrs.put(traffic_class, new Attribute(traffic_class, 8 * BIT, DataType.Integer, version));
		ipv4Attrs.put(flow_label, new Attribute(flow_label, 20 * BIT, DataType.Long, traffic_class));
		ipv4Attrs.put(payload_length, new Attribute(payload_length, 16 * BIT, DataType.Integer, flow_label));
		ipv4Attrs.put(next_header, new Attribute(next_header, 8 * BIT, DataType.Integer, payload_length));
		ipv4Attrs.put(hop_limit, new Attribute(hop_limit, 8 * BIT, DataType.Integer, next_header));
		ipv4Attrs.put(source, new Attribute(source, 16 * BYTE, DataType.IP, hop_limit));
		ipv4Attrs.put(destination, new Attribute(destination, 16 * BYTE, DataType.IP, source));
		ipv4Attrs.put(data, new Attribute(data, 0, DataType.Data, destination));
		createAttrs(Name, ipv4Attrs);
	}

	@Override
	public void dataResolve(String name, InputStream in, long frameId) throws IOException {
		Integer protocolValue = getAsInteger(next_header);
		if (Protocol.TCP.value == protocolValue) {
			ResolverRegister.getResolve(Protocol.TCP.resolve).resolve(Protocol.TCP.resolve, in, frameId);
		}
		if (Protocol.UDP.value == protocolValue) {
			ResolverRegister.getResolve(Protocol.UDP.resolve).resolve(Protocol.UDP.resolve, in, frameId);
		}
	}

	@Override
	public void custom(Attribute attr, Long frameId, InputStream in) {
	}

	public enum Protocol implements Mapping {
		ICMP(1, (byte) 0x01, null, ""), IGMP(2, (byte) 0x02, null, ""), TCP(6, (byte) 0x06, PcapTcpStructResolve.Name, ""), UDP(17, (byte) 0x11, PcapUDPStructResolve.Name, ""), ENCAP(41, (byte) 0x29,
			null, ""), OSPF(89, (byte) 0x59, null, ""), SCTP(132, (byte) 0x84, null, "");

		private int value;
		private byte hexValue;
		private String resolve;
		private String description;

		private Protocol(int value, byte hexValue, String resolve, String description) {
			this.value = value;
			this.hexValue = hexValue;
			this.resolve = resolve;
			this.description = description;
		}

		public int getValue() {
			return value;
		}

		public byte getHexValue() {
			return hexValue;
		}

		public String getResolve() {
			return resolve;
		}

		public String getDescription() {
			return description;
		}
	}
}
