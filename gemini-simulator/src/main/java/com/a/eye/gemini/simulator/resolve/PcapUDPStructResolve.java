package com.a.eye.gemini.simulator.resolve;

import java.io.InputStream;

import com.a.eye.gemini.simulator.Mapping;
import com.a.eye.gemini.simulator.resolve.Attribute.DataType;
import com.a.eye.gemini.simulator.resolve.PcapUDPStructResolve.UDPStruct;

public class PcapUDPStructResolve extends GeminiResolveLowBase<UDPStruct> {

	public static final String Name = "UDP";

	private PcapUDPStructResolve() {
	}

	public static PcapUDPStructResolve inst = new PcapUDPStructResolve();

	public enum UDPStruct {
		source, destination, length, checksum, data
	}

	static {
		inst.addAttr(new Attribute<UDPStruct>(UDPStruct.source, 2 * BYTE, DataType.Integer, 1));
		inst.addAttr(new Attribute<UDPStruct>(UDPStruct.destination, 2 * BYTE, DataType.Integer, 2));
		inst.addAttr(new Attribute<UDPStruct>(UDPStruct.length, 2 * BYTE, DataType.Integer, 3));
		inst.addAttr(new Attribute<UDPStruct>(UDPStruct.checksum, 2 * BYTE, DataType.Integer, 4));
		inst.addAttr(new Attribute<UDPStruct>(UDPStruct.data, 0, DataType.Data, 5));
	}

	@Override
	public String getDataResolve() {
		Integer port = getAsInteger(UDPStruct.destination);
		if (Port.DNS.value == port) {
			return Port.DNS.resolve;
		}
		return null;
	}
	
	@Override
	public void custom(UDPStruct name, InputStream in) {
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
