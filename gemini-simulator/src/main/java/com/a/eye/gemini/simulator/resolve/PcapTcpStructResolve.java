package com.a.eye.gemini.simulator.resolve;

import java.io.InputStream;

import com.a.eye.gemini.simulator.resolve.Attribute.DataType;
import com.a.eye.gemini.simulator.resolve.PcapTcpStructResolve.TcpStruct;

public class PcapTcpStructResolve extends GeminiResolveLowBase<TcpStruct> {

	public static final String Name = "Tcp";

	private PcapTcpStructResolve() {
	}

	public static PcapTcpStructResolve inst = new PcapTcpStructResolve();

	public enum TcpStruct {
			destination,
			source,
			sequence,
			ack_sequence,
			data_offset,
			reserved,
			ns,
			cwr,
			ecr,
			urg,
			ack,
			psh,
			rst,
			syn,
			fin,
			window_size,
			checksum,
			urgent_pointer,
			option_kind,
			option_length,
			mss,
			data,
			test
	}

	static {
		inst.addAttr(new Attribute<TcpStruct>(TcpStruct.source, 2 * BYTE, DataType.Integer, 1));
		inst.addAttr(new Attribute<TcpStruct>(TcpStruct.destination, 2 * BYTE, DataType.Integer, 2));
		inst.addAttr(new Attribute<TcpStruct>(TcpStruct.sequence, 4 * BYTE, DataType.Long, 3));
		inst.addAttr(new Attribute<TcpStruct>(TcpStruct.ack_sequence, 4 * BYTE, DataType.Long, 4));
		inst.addAttr(new Attribute<TcpStruct>(TcpStruct.data_offset, 4, DataType.Integer, 5));
		inst.addAttr(new Attribute<TcpStruct>(TcpStruct.reserved, 3, DataType.String, 6));
		inst.addAttr(new Attribute<TcpStruct>(TcpStruct.ns, 1 * BIT, DataType.Integer, 7));
		inst.addAttr(new Attribute<TcpStruct>(TcpStruct.cwr, 1 * BIT, DataType.Integer, 8));
		inst.addAttr(new Attribute<TcpStruct>(TcpStruct.ecr, 1 * BIT, DataType.Integer, 9));
		inst.addAttr(new Attribute<TcpStruct>(TcpStruct.urg, 1 * BIT, DataType.Integer, 10));
		inst.addAttr(new Attribute<TcpStruct>(TcpStruct.ack, 1 * BIT, DataType.Integer, 11));
		inst.addAttr(new Attribute<TcpStruct>(TcpStruct.psh, 1 * BIT, DataType.Integer, 12));
		inst.addAttr(new Attribute<TcpStruct>(TcpStruct.rst, 1 * BIT, DataType.Integer, 13));
		inst.addAttr(new Attribute<TcpStruct>(TcpStruct.syn, 1 * BIT, DataType.Integer, 14));
		inst.addAttr(new Attribute<TcpStruct>(TcpStruct.fin, 1 * BIT, DataType.Integer, 15));
		inst.addAttr(new Attribute<TcpStruct>(TcpStruct.window_size, 2 * BYTE, DataType.Long, 16));
		inst.addAttr(new Attribute<TcpStruct>(TcpStruct.checksum, 2 * BYTE, DataType.Long, 17));
		inst.addAttr(new Attribute<TcpStruct>(TcpStruct.urgent_pointer, 2 * BYTE, DataType.Long, 18));
		inst.addAttr(new Attribute<TcpStruct>(TcpStruct.option_kind, 1 * BYTE, DataType.Long, 19));
		inst.addAttr(new Attribute<TcpStruct>(TcpStruct.option_length, 1 * BYTE, DataType.Integer, 20));
		inst.addAttr(new Attribute<TcpStruct>(TcpStruct.mss, 2 * BYTE, DataType.Integer, 21));
		inst.addAttr(new Attribute<TcpStruct>(TcpStruct.test, 8 * BYTE, DataType.String, 22));
		// inst.addAttr(new Attribute<TcpStruct>(TcpStruct.data, 0,
		// DataType.Data, 22));
	}

	@Override
	public String getDataResolve() {
		return null;
	}

	@Override
	public void custom(TcpStruct name, InputStream in) {
	}
}
