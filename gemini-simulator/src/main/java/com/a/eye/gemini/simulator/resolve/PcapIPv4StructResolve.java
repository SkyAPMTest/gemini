package com.a.eye.gemini.simulator.resolve;

import java.io.InputStream;

import com.a.eye.gemini.simulator.Mapping;
import com.a.eye.gemini.simulator.resolve.Attribute.DataType;
import com.a.eye.gemini.simulator.resolve.PcapIPv4StructResolve.IPv4Struct;

public class PcapIPv4StructResolve extends GeminiResolveLowBase<IPv4Struct> {

	public static final String Name = "IPv4";

	private PcapIPv4StructResolve() {
	}

	public static PcapIPv4StructResolve inst = new PcapIPv4StructResolve();

	public enum IPv4Struct {
		version, ihl, dscp, ecn, total_length, identification, reserved, df, mf, fragment_offset, time_to_live, protocol, header_checksum, destination, source, data
	}

	static {
		inst.addAttr(new Attribute<IPv4Struct>(IPv4Struct.version, 4 * BIT, DataType.Integer, 1));
		inst.addAttr(new Attribute<IPv4Struct>(IPv4Struct.ihl, 4 * BIT, DataType.Integer, 2));
		inst.addAttr(new Attribute<IPv4Struct>(IPv4Struct.dscp, 6 * BIT, DataType.Integer, 3));
		inst.addAttr(new Attribute<IPv4Struct>(IPv4Struct.ecn, 2 * BIT, DataType.Integer, 4));
		inst.addAttr(new Attribute<IPv4Struct>(IPv4Struct.total_length, 2 * BYTE, DataType.Integer, 5));
		inst.addAttr(new Attribute<IPv4Struct>(IPv4Struct.identification, 2 * BYTE, DataType.Integer, 6));
		inst.addAttr(new Attribute<IPv4Struct>(IPv4Struct.reserved, 1 * BIT, DataType.Integer, 7));
		inst.addAttr(new Attribute<IPv4Struct>(IPv4Struct.df, 1 * BIT, DataType.Integer, 8));
		inst.addAttr(new Attribute<IPv4Struct>(IPv4Struct.mf, 1 * BIT, DataType.Integer, 9));
		inst.addAttr(new Attribute<IPv4Struct>(IPv4Struct.fragment_offset, 13 * BIT, DataType.Integer, 10));
		inst.addAttr(new Attribute<IPv4Struct>(IPv4Struct.time_to_live, 1 * BYTE, DataType.Integer, 11));
		inst.addAttr(new Attribute<IPv4Struct>(IPv4Struct.protocol, 1 * BYTE, DataType.Integer, 12));
		inst.addAttr(new Attribute<IPv4Struct>(IPv4Struct.header_checksum, 2 * BYTE, DataType.Integer, 13));
		inst.addAttr(new Attribute<IPv4Struct>(IPv4Struct.source, 4 * BYTE, DataType.IP, 14));
		inst.addAttr(new Attribute<IPv4Struct>(IPv4Struct.destination, 4 * BYTE, DataType.IP, 15));
		inst.addAttr(new Attribute<IPv4Struct>(IPv4Struct.data, 0, DataType.Data, 16));
	}

	@Override
	public String getDataResolve() {
		Integer protocol = getAsInteger(IPv4Struct.protocol);
		if (Protocol.TCP.value == protocol) {
			return Protocol.TCP.resolve;
		}
		if (Protocol.UDP.value == protocol) {
			return Protocol.UDP.resolve;
		}
		return null;
	}
	
	@Override
	public void custom(IPv4Struct name, InputStream in) {
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
