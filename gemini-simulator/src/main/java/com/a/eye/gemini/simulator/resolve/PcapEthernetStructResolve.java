package com.a.eye.gemini.simulator.resolve;

import java.io.InputStream;

import com.a.eye.gemini.simulator.Mapping;
import com.a.eye.gemini.simulator.resolve.Attribute.DataType;
import com.a.eye.gemini.simulator.resolve.PcapEthernetStructResolve.EthernetStruct;

public class PcapEthernetStructResolve extends GeminiResolveLowBase<EthernetStruct> {

	public static final String Name = "Ethernet";

	private PcapEthernetStructResolve() {
	}

	public static PcapEthernetStructResolve inst = new PcapEthernetStructResolve();

	public enum EthernetStruct {
		destination, source, ether_type, data, checksum
	}

	static {
		inst.addAttr(new Attribute<EthernetStruct>(EthernetStruct.destination, 6 * BYTE, DataType.Mac, 1));
		inst.addAttr(new Attribute<EthernetStruct>(EthernetStruct.source, 6 * BYTE, DataType.Mac, 2));
		inst.addAttr(new Attribute<EthernetStruct>(EthernetStruct.ether_type, 2 * BYTE, DataType.Integer, 3));
		inst.addAttr(new Attribute<EthernetStruct>(EthernetStruct.data, 0, DataType.Data, 4));
	}

	@Override
	public String getDataResolve() {
		Integer etherType = getAsInteger(EthernetStruct.ether_type);
		if (EtherType.IPv4.value == etherType) {
			return EtherType.IPv4.resolve;
		}
		return null;
	}
	
	@Override
	public void custom(EthernetStruct name, InputStream in) {
	}

	// https://en.wikipedia.org/wiki/EtherType
	public enum EtherType implements Mapping {
		IPv4(2048, (byte) 0x0800, PcapIPv4StructResolve.Name, "Internet Protocol version 4"), IPv6(34525, (byte) 0x86DD, null, "Internet Protocol Version 6 (IPv6)"), Ethernet_Flow_Control(34824,
			(byte) 0x8808, null, "Ethernet flow control");

		private int value;
		private byte hexValue;
		private String resolve;
		private String description;

		private EtherType(int value, byte hexValue, String resolve, String description) {
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
