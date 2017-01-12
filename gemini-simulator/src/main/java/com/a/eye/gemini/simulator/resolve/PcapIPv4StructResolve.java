package com.a.eye.gemini.simulator.resolve;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.a.eye.gemini.simulator.Mapping;
import com.a.eye.gemini.simulator.resolve.Attribute.DataType;

public class PcapIPv4StructResolve extends GeminiResolveLowBase {

	public static final String Name = "IPv4";

	private PcapIPv4StructResolve() {
	}

	public static PcapIPv4StructResolve inst = new PcapIPv4StructResolve();

	public static final String version = "version";
	public static final String ihl = "ihl";
	public static final String dscp = "dscp";
	public static final String ecn = "ecn";
	public static final String total_length = "total_length";
	public static final String identification = "identification";
	public static final String reserved = "reserved";
	public static final String df = "df";
	public static final String mf = "mf";
	public static final String fragment_offset = "fragment_offset";
	public static final String time_to_live = "time_to_live";
	public static final String protocol = "protocol";
	public static final String header_checksum = "header_checksum";
	public static final String destination = "destination";
	public static final String source = "source";
	public static final String data = "data";

	static {
		Map<String, Attribute> ipv4Attrs = new HashMap<String, Attribute>();
		ipv4Attrs.put(version, new Attribute(version, 4 * BIT, DataType.Integer, Attribute.First));
		ipv4Attrs.put(ihl, new Attribute(ihl, 4 * BIT, DataType.Integer, version));
		ipv4Attrs.put(dscp, new Attribute(dscp, 6 * BIT, DataType.Integer, ihl));
		ipv4Attrs.put(ecn, new Attribute(ecn, 2 * BIT, DataType.Integer, dscp));
		ipv4Attrs.put(total_length, new Attribute(total_length, 2 * BYTE, DataType.Integer, ecn));
		ipv4Attrs.put(identification, new Attribute(identification, 2 * BYTE, DataType.Integer, total_length));
		ipv4Attrs.put(reserved, new Attribute(reserved, 1 * BIT, DataType.Integer, identification));
		ipv4Attrs.put(df, new Attribute(df, 1 * BIT, DataType.Integer, reserved));
		ipv4Attrs.put(mf, new Attribute(mf, 1 * BIT, DataType.Integer, df));
		ipv4Attrs.put(fragment_offset, new Attribute(fragment_offset, 13 * BIT, DataType.Integer, mf));
		ipv4Attrs.put(time_to_live, new Attribute(time_to_live, 1 * BYTE, DataType.Integer, fragment_offset));
		ipv4Attrs.put(protocol, new Attribute(protocol, 1 * BYTE, DataType.Integer, time_to_live));
		ipv4Attrs.put(header_checksum, new Attribute(header_checksum, 2 * BYTE, DataType.Integer, protocol));
		ipv4Attrs.put(source, new Attribute(source, 4 * BYTE, DataType.IP, header_checksum));
		ipv4Attrs.put(destination, new Attribute(destination, 4 * BYTE, DataType.IP, source));
		ipv4Attrs.put(data, new Attribute(data, 0, DataType.Data, destination));
		createAttrs(Name, ipv4Attrs);
	}

	@Override
	public void dataResolve(String name, InputStream in, long frameId) throws IOException {
		Integer protocolValue = getAsInteger(protocol);
		if (Protocol.TCP.value == protocolValue) {
			ResolverRegister.getResolve(Protocol.TCP.resolve).resolve(Protocol.TCP.resolve, in, frameId);
		} else if (Protocol.UDP.value == protocolValue) {
			ResolverRegister.getResolve(Protocol.UDP.resolve).resolve(Protocol.UDP.resolve, in, frameId);
		} else {
			Integer totalLengthValue = getAsInteger(total_length);
			if (totalLengthValue - 20 > 0) {
				bitInputStream.readBit(in, (totalLengthValue - 20) * 8);
			}
		}
	}

	@Override
	public void custom(Attribute attr, Long frameId, InputStream in) {
	}

	public enum Protocol implements Mapping {
		ICMP(1, (byte) 0x01, null, ""), TCP(6, (byte) 0x06, PcapTcpStructResolve.Name, ""), UDP(17, (byte) 0x11, PcapUDPStructResolve.Name, ""), ENCAP(41, (byte) 0x29, null, ""), OSPF(89,
			(byte) 0x59, null, ""), SCTP(132, (byte) 0x84, null, "");

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
