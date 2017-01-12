package com.a.eye.gemini.simulator.resolve;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.a.eye.gemini.simulator.Mapping;
import com.a.eye.gemini.simulator.resolve.Attribute.DataType;

public class PcapUDPStructResolve extends GeminiResolveLowBase {

	public static final String Name = "UDP";

	private PcapUDPStructResolve() {
	}

	public static PcapUDPStructResolve inst = new PcapUDPStructResolve();

	public static final String destination = "destination";
	public static final String source = "source";
	public static final String length = "length";
	public static final String data = "data";
	public static final String checksum = "checksum";

	static {
		Map<String, Attribute> udpAttrs = new HashMap<String, Attribute>();
		udpAttrs.put(source, new Attribute(source, 2 * BYTE, DataType.Integer, Attribute.First));
		udpAttrs.put(destination, new Attribute(destination, 2 * BYTE, DataType.Integer, source));
		udpAttrs.put(length, new Attribute(length, 2 * BYTE, DataType.Integer, destination));
		udpAttrs.put(checksum, new Attribute(checksum, 2 * BYTE, DataType.Integer, length));
		udpAttrs.put(data, new Attribute(data, 0, DataType.Data, checksum));
		createAttrs(Name, udpAttrs);
	}

	@Override
	public void dataResolve(String name, InputStream in, long frameId) throws IOException {
		Integer lengthValue = getAsInteger(length);
		bitInputStream.readBit(in, (lengthValue - 8) * 8);

		if (Port.DNS.value == getAsInteger(destination) || Port.DNS.value == getAsInteger(source)) {
			// ResolverRegister.getResolve(Port.DNS.resolve).resolve(Port.DNS.resolve,
			// in, frameId);
		}
	}

	@Override
	public void custom(Attribute attr, Long frameId, InputStream in) {
	}

	public enum Port implements Mapping {
		DNS(53, (byte) 0x35, PcapDNSStructResolve.Name, "DNS primarily uses the User Datagram Protocol (UDP) on port number 53 to serve requests.");

		private int value;
		private byte hexValue;
		private String resolve;
		private String description;

		private Port(int value, byte hexValue, String resolve, String description) {
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
