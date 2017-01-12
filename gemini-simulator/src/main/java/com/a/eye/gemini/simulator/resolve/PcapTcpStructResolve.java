package com.a.eye.gemini.simulator.resolve;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.a.eye.gemini.common.util.StringUtils;
import com.a.eye.gemini.simulator.resolve.Attribute.DataType;

public class PcapTcpStructResolve extends GeminiResolveLowBase {

	public static final String Name = "Tcp";

	private PcapTcpStructResolve() {
	}

	public static PcapTcpStructResolve inst = new PcapTcpStructResolve();

	public static final String destination = "destination";
	public static final String source = "source";
	public static final String sequence = "sequence";
	public static final String ack_sequence = "ack_sequence";
	public static final String data_offset = "data_offset";
	public static final String reserved = "reserved";
	public static final String ns = "ns";
	public static final String cwr = "cwr";
	public static final String ecr = "ecr";
	public static final String urg = "urg";
	public static final String ack = "ack";
	public static final String psh = "psh";
	public static final String rst = "rst";
	public static final String syn = "syn";
	public static final String fin = "fin";
	public static final String window_size = "window_size";
	public static final String checksum = "checksum";
	public static final String urgent_pointer = "urgent_pointer";
	public static final String option_kind = "option_kind";
	public static final String option_length = "option_length";
	public static final String mss = "mss";
	public static final String data = "data";

	static {
		Map<String, Attribute> tcpAttrs = new HashMap<String, Attribute>();
		tcpAttrs.put(source, new Attribute(source, 2 * BYTE, DataType.Integer, Attribute.First));
		tcpAttrs.put(destination, new Attribute(destination, 2 * BYTE, DataType.Integer, source));
		tcpAttrs.put(sequence, new Attribute(sequence, 4 * BYTE, DataType.Long, destination));
		tcpAttrs.put(ack_sequence, new Attribute(ack_sequence, 4 * BYTE, DataType.Long, sequence));
		tcpAttrs.put(data_offset, new Attribute(data_offset, 4, DataType.Integer, ack_sequence));
		tcpAttrs.put(reserved, new Attribute(reserved, 3, DataType.Hex, data_offset));
		tcpAttrs.put(ns, new Attribute(ns, 1 * BIT, DataType.Integer, reserved));
		tcpAttrs.put(cwr, new Attribute(cwr, 1 * BIT, DataType.Integer, ns));
		tcpAttrs.put(ecr, new Attribute(ecr, 1 * BIT, DataType.Integer, cwr));
		tcpAttrs.put(urg, new Attribute(urg, 1 * BIT, DataType.Integer, ecr));
		tcpAttrs.put(ack, new Attribute(ack, 1 * BIT, DataType.Integer, urg));
		tcpAttrs.put(psh, new Attribute(psh, 1 * BIT, DataType.Integer, ack));
		tcpAttrs.put(rst, new Attribute(rst, 1 * BIT, DataType.Integer, psh));
		tcpAttrs.put(syn, new Attribute(syn, 1 * BIT, DataType.Integer, rst));
		tcpAttrs.put(fin, new Attribute(fin, 1 * BIT, DataType.Integer, syn));
		tcpAttrs.put(window_size, new Attribute(window_size, 2 * BYTE, DataType.Long, fin));
		tcpAttrs.put(checksum, new Attribute(checksum, 2 * BYTE, DataType.Long, window_size));
		tcpAttrs.put(urgent_pointer, new Attribute(urgent_pointer, 2 * BYTE, DataType.Long, checksum));
		tcpAttrs.put(data, new Attribute(data, 0, DataType.Data, urgent_pointer));
		createAttrs(Name, tcpAttrs);
	}

	@Override
	public void dataResolve(String name, InputStream in, long frameId) throws IOException {
//		Integer ipTotalLength = getAsInteger(PcapIPv4StructResolve.total_length);
//		Integer ipHeadLength = 20;
//		Integer tcpHeadLength = 20;
//		Integer dataLength = ipTotalLength - ipHeadLength - tcpHeadLength;
//		if (dataLength > 0) {
//			bitInputStream.readBit(in, dataLength * 8);
//		}
//
//		Integer frameLen = getAsInteger(PcapFrameStructResolve.len);
//		Integer ethernetHeadLength = 14;
//		Integer aa = frameLen - ipTotalLength - ethernetHeadLength;
//		if (aa > 0) {
//			bitInputStream.readBit(in, aa * 8);
//		}
	}

	@Override
	public void custom(Attribute attr, Long frameId, InputStream in) throws IOException {
		Integer tcpIpTotalLength = getAsInteger(PcapIPv4StructResolve.total_length);
		Integer ipHeadLength = 20;
		Integer tcpHeadLength = 20;
		if (tcpIpTotalLength - ipHeadLength - tcpHeadLength > 0) {
			if (option_kind.equals(attr.getName())) {
				byte[] byteread = bitInputStream.readBit(in, attr.getLength());
				String strread = binaryToHexString(byteread);
				setValue(option_kind, Integer.parseInt(strread, 16));
			}
			if (option_length.equals(attr.getName())) {
				byte[] byteread = bitInputStream.readBit(in, attr.getLength());
				String strread = binaryToHexString(byteread);
				setValue(option_length, Integer.parseInt(strread, 16));
			}
			if (mss.equals(attr.getName())) {
				byte[] byteread = bitInputStream.readBit(in, attr.getLength());
				String strread = binaryToHexString(byteread);
				setValue(mss, Integer.parseInt(strread, 16));
			}
			if (data.equals(attr.getName())) {
				Integer optionLengthValue = getAsInteger(option_length);
				byte[] dataValue = bitInputStream.readBit(in, optionLengthValue * 8 * 2);
				System.out.printf("%s %s >> %s, %s, %sbit \n", frameId, Name, data, StringUtils.BinaryToHexString(dataValue), optionLengthValue * 8 * 2);
			}
		}
	}
}
