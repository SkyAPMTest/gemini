package com.a.eye.gemini.simulator.resolve;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.a.eye.gemini.simulator.Mapping;
import com.a.eye.gemini.simulator.resolve.Attribute.DataType;

public class PcapEthernetStructResolve extends GeminiResolveLowBase {

	public static final String Name = "Ethernet";

	public static PcapEthernetStructResolve inst = new PcapEthernetStructResolve();

	public static final String destination = "destination";
	public static final String source = "source";
	public static final String ether_type = "ether_type";
	public static final String data = "data";
	public static final String checksum = "checksum";

	static {
		Map<String, Attribute> ethernetAttrs = new HashMap<String, Attribute>();
		ethernetAttrs.put(destination, new Attribute(destination, 6 * BYTE, DataType.Mac, Attribute.First));
		ethernetAttrs.put(source, new Attribute(source, 6 * BYTE, DataType.Mac, destination));
		ethernetAttrs.put(ether_type, new Attribute(ether_type, 2 * BYTE, DataType.Integer, source));
		ethernetAttrs.put(data, new Attribute(data, 0, DataType.Data, ether_type));
		createAttrs(Name, ethernetAttrs);
	}

	@Override
	public void dataResolve(String name, InputStream in, long frameId) throws IOException {
		Integer etherType = getAsInteger(ether_type);
		if (EtherType.IPv4.value == etherType) {
			ResolverRegister.getResolve(EtherType.IPv4.resolve).resolve(EtherType.IPv4.resolve, in, frameId);
		} else if (EtherType.IPv6.value == etherType) {
			ResolverRegister.getResolve(EtherType.IPv6.resolve).resolve(EtherType.IPv6.resolve, in, frameId);
		} else if (EtherType.ARP.value == etherType) {
			ResolverRegister.getResolve(EtherType.ARP.resolve).resolve(EtherType.ARP.resolve, in, frameId);
		}
		Integer frameLen = getAsInteger(PcapFrameStructResolve.len);
		Integer lengthSum = bitInputStream.getLengthSum();
		Integer paddingLength = (frameLen * 8) - lengthSum;
		System.out.println("frameLen: " + frameLen + ", lengthsum:" + lengthSum / 8 + ", cha: " + paddingLength);
		if (paddingLength > 0) {
			byte[] padding = bitInputStream.readBit(in, (frameLen * 8) - lengthSum);
			System.out.println(binaryToHexString(padding));
		}
	}

	@Override
	public void custom(Attribute attr, Long frameId, InputStream in) {
	}

	// https://en.wikipedia.org/wiki/EtherType
	public enum EtherType implements Mapping {
		IPv4(2048, (byte) 0x0800, PcapIPv4StructResolve.Name, "Internet Protocol version 4"), ARP(2054, (byte) 0x0806, PcapARPStructResolve.Name, "Address Resolution Protocol"), IPv6(34525,
			(byte) 0x86DD, PcapIPv6StructResolve.Name, "Internet Protocol Version 6 (IPv6)"), Ethernet_Flow_Control(34824, (byte) 0x8808, null, "Ethernet flow control");

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
