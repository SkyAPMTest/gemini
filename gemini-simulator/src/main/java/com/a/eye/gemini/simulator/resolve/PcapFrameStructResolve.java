package com.a.eye.gemini.simulator.resolve;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.a.eye.gemini.simulator.Mapping;
import com.a.eye.gemini.simulator.resolve.Attribute.DataType;

public class PcapFrameStructResolve extends GeminiResolveHighBase {

	public static final String Name = "Frame";

	public static PcapFrameStructResolve inst = new PcapFrameStructResolve();

	public static final String frame_id = "frame_id";
	public static final String magic = "magic";
	public static final String version_major = "version_major";
	public static final String version_minor = "version_minor";
	public static final String thiszone = "thiszone";
	public static final String sigfigs = "sigfigs";
	public static final String snaplen = "snaplen";
	public static final String linktype = "linktype";
	public static final String ts_seconds = "ts_seconds";
	public static final String ts_microseconds = "ts_microseconds";
	public static final String caplen = "caplen";
	public static final String len = "len";
	public static final String data = "data";

	static {
		Map<String, Attribute> frameAttrs = new HashMap<String, Attribute>();
		frameAttrs.put(frame_id, new Attribute(frame_id, 0, DataType.Frame, Attribute.First, true));
		frameAttrs.put(magic, new Attribute(magic, 4 * BYTE, DataType.Hex, frame_id, true));
		frameAttrs.put(version_major, new Attribute(version_major, 2 * BYTE, DataType.Integer, magic, true));
		frameAttrs.put(version_minor, new Attribute(version_minor, 2 * BYTE, DataType.Integer, version_major, true));
		frameAttrs.put(thiszone, new Attribute(thiszone, 4 * BYTE, DataType.Integer, version_minor, true));
		frameAttrs.put(sigfigs, new Attribute(sigfigs, 4 * BYTE, DataType.Integer, thiszone, true));
		frameAttrs.put(snaplen, new Attribute(snaplen, 4 * BYTE, DataType.Integer, sigfigs, true));
		frameAttrs.put(linktype, new Attribute(linktype, 4 * BYTE, DataType.Integer, snaplen, true));
		frameAttrs.put(ts_seconds, new Attribute(ts_seconds, 4 * BYTE, DataType.Integer, linktype));
		frameAttrs.put(ts_microseconds, new Attribute(ts_microseconds, 4 * BYTE, DataType.Integer, ts_seconds));
		frameAttrs.put(caplen, new Attribute(caplen, 4 * BYTE, DataType.Integer, ts_microseconds));
		frameAttrs.put(len, new Attribute(len, 4 * BYTE, DataType.Integer, caplen));
		frameAttrs.put(data, new Attribute(data, 0, DataType.Data, len));
		createAttrs(Name, frameAttrs);
	}

	@Override
	public void dataResolve(String name, InputStream in, long frameId) throws IOException {
		Integer linkType = getAsInteger(linktype);
		if (Linktype.Ethernet.value == linkType) {
			ResolverRegister.getResolve(Linktype.Ethernet.resolve).resolve(Linktype.Ethernet.resolve, in, frameId);
		}
	}

	@Override
	public void custom(Attribute attr, Long frameId, InputStream in) {
	}

	public enum Linktype implements Mapping {
			BSD(0, (byte) 0x00, null, ""),
			Ethernet(1, (byte) 0x01, PcapEthernetStructResolve.Name, ""),
			W802_5(6, (byte) 0x06, null, ""),
			ARCnet(7, (byte) 0x07, null, ""),
			SLIP(8, (byte) 0x08, null, ""),
			PPP(9, (byte) 0x09, null, ""),
			FDDI(10, (byte) 0x0a, null, ""),
			LLC(100, (byte) 0x64, null, ""),
			IP(101, (byte) 0x65, null, ""),
			BSD_OS_SLIP(102, (byte) 0x66, null, ""),
			BSD_OS_PPP(103, (byte) 0x67, null, ""),
			Cisco_HDLC(104, (byte) 0x68, null, ""),
			W802_11(105, (byte) 0x69, null, ""),
			OpenBSD(108, (byte) 0x6c, null, ""),
			special_Linux(113, (byte) 0x71, null, ""),
			LocalTalk(114, (byte) 0x72, null, "");

		private int value;
		private byte hexValue;
		private String resolve;
		private String description;

		private Linktype(int value, byte hexValue, String resolve, String description) {
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
