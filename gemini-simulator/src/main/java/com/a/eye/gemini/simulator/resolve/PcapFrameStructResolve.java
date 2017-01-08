package com.a.eye.gemini.simulator.resolve;

import java.io.InputStream;

import com.a.eye.gemini.simulator.Mapping;
import com.a.eye.gemini.simulator.resolve.Attribute.DataType;
import com.a.eye.gemini.simulator.resolve.PcapFrameStructResolve.PcapFileStruct;

public class PcapFrameStructResolve extends GeminiResolveHighBase<PcapFileStruct> {

	public static final String Name = "Frame";

	private PcapFrameStructResolve() {
	}

	public static PcapFrameStructResolve inst = new PcapFrameStructResolve();

	public enum PcapFileStruct {
		frame, magic, version_major, version_minor, thiszone, sigfigs, snaplen, linktype, ts_seconds, ts_microseconds, caplen, len, data
	}

	static {
		inst.addAttr(new Attribute<PcapFileStruct>(PcapFileStruct.frame, 0, DataType.Frame, 1, true));
		inst.addAttr(new Attribute<PcapFileStruct>(PcapFileStruct.magic, 4 * BYTE, DataType.String, 2, true));
		inst.addAttr(new Attribute<PcapFileStruct>(PcapFileStruct.version_major, 2 * BYTE, DataType.Integer, 3, true));
		inst.addAttr(new Attribute<PcapFileStruct>(PcapFileStruct.version_minor, 2 * BYTE, DataType.Integer, 4, true));
		inst.addAttr(new Attribute<PcapFileStruct>(PcapFileStruct.thiszone, 4 * BYTE, DataType.Integer, 5, true));
		inst.addAttr(new Attribute<PcapFileStruct>(PcapFileStruct.sigfigs, 4 * BYTE, DataType.Integer, 6, true));
		inst.addAttr(new Attribute<PcapFileStruct>(PcapFileStruct.snaplen, 4 * BYTE, DataType.Integer, 7, true));
		inst.addAttr(new Attribute<PcapFileStruct>(PcapFileStruct.linktype, 4 * BYTE, DataType.Integer, 8, true));
		inst.addAttr(new Attribute<PcapFileStruct>(PcapFileStruct.ts_seconds, 4 * BYTE, DataType.Integer, 9));
		inst.addAttr(new Attribute<PcapFileStruct>(PcapFileStruct.ts_microseconds, 4 * BYTE, DataType.Integer, 10));
		inst.addAttr(new Attribute<PcapFileStruct>(PcapFileStruct.caplen, 4 * BYTE, DataType.Integer, 11));
		inst.addAttr(new Attribute<PcapFileStruct>(PcapFileStruct.len, 4 * BYTE, DataType.Integer, 12));
		inst.addAttr(new Attribute<PcapFileStruct>(PcapFileStruct.data, 0, DataType.Data, 13));
	}

	@Override
	public String getDataResolve() {
		Integer linktype = getAsInteger(PcapFileStruct.linktype);
		if (Linktype.Ethernet.value == linktype) {
			return Linktype.Ethernet.resolve;
		}
		return null;
	}
	
	@Override
	public void custom(PcapFileStruct name, InputStream in) {
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
