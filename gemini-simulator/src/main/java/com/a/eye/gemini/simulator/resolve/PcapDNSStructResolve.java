package com.a.eye.gemini.simulator.resolve;

import java.io.IOException;
import java.io.InputStream;

import com.a.eye.gemini.simulator.resolve.Attribute.DataType;
import com.a.eye.gemini.simulator.resolve.PcapDNSStructResolve.DNSStruct;

// https://en.wikipedia.org/wiki/Domain_Name_System
// https://tools.ietf.org/html/rfc1035 4.1.1. Header section format
public class PcapDNSStructResolve extends GeminiResolveLowBase<DNSStruct> {

	public static final String Name = "DNS";

	private PcapDNSStructResolve() {
	}

	public static PcapDNSStructResolve inst = new PcapDNSStructResolve();

	public enum DNSStruct {
		id, qr, opcode, aa, tc, rd, ra, z, rcode, question, answer, authority, additional, qname, qtype, qclass
	}

	static {
		inst.addAttr(new Attribute<DNSStruct>(DNSStruct.id, 2 * BYTE, DataType.String, 1));
		inst.addAttr(new Attribute<DNSStruct>(DNSStruct.qr, 1 * BIT, DataType.Integer, 2));
		inst.addAttr(new Attribute<DNSStruct>(DNSStruct.opcode, 4 * BIT, DataType.Integer, 3));
		inst.addAttr(new Attribute<DNSStruct>(DNSStruct.aa, 1 * BIT, DataType.Integer, 4));
		inst.addAttr(new Attribute<DNSStruct>(DNSStruct.tc, 1 * BIT, DataType.Integer, 5));
		inst.addAttr(new Attribute<DNSStruct>(DNSStruct.rd, 1 * BIT, DataType.Integer, 6));
		inst.addAttr(new Attribute<DNSStruct>(DNSStruct.ra, 1 * BIT, DataType.Integer, 7));
		inst.addAttr(new Attribute<DNSStruct>(DNSStruct.z, 3 * BIT, DataType.Integer, 8));
		inst.addAttr(new Attribute<DNSStruct>(DNSStruct.rcode, 4 * BIT, DataType.Integer, 9));
		inst.addAttr(new Attribute<DNSStruct>(DNSStruct.question, 2 * BYTE, DataType.Integer, 14));
		inst.addAttr(new Attribute<DNSStruct>(DNSStruct.answer, 2 * BYTE, DataType.Integer, 15));
		inst.addAttr(new Attribute<DNSStruct>(DNSStruct.authority, 2 * BYTE, DataType.Integer, 16));
		inst.addAttr(new Attribute<DNSStruct>(DNSStruct.additional, 2 * BYTE, DataType.Integer, 17));
		inst.addAttr(new Attribute<DNSStruct>(DNSStruct.qname, 0, DataType.Custom, 18));
		inst.addAttr(new Attribute<DNSStruct>(DNSStruct.qtype, 2 * BYTE, DataType.Integer, 19));
		inst.addAttr(new Attribute<DNSStruct>(DNSStruct.qclass, 2 * BYTE, DataType.Integer, 20));
	}

	@Override
	public String getDataResolve() {
		return null;
	}

	@Override
	public void custom(DNSStruct name, InputStream in) throws IOException {
		if (DNSStruct.qname.equals(name)) {
			String dns = "";
			for (int i = 0; i < 4; i++) {
				byte length = bitInputStream.readOneByteBits(in, 8);
				if (length == 0) {
					break;
				}
				byte[] dnsSeg = bitInputStream.readBit(in, length * 8);
				if (i == 0) {
					dns = dns + new String(dnsSeg);
				} else {
					dns = dns + "." + new String(dnsSeg);
				}
			}
			setValue(DNSStruct.qname, dns);
			System.out.println(DNSStruct.qname + " : " + dns);
		}
	}

	public enum QR {
		QUERY(0, "A one bit field that specifies whether this message is a query (0)"), RESPONSE(1, "or a response (1)");

		private int value;

		private QR(int value, String description) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	public enum OPCODE {
		QUERY(0, "a standard query (QUERY)"), IQUERY(1, "an inverse query (IQUERY)"), STATUS(2, "a server status request (STATUS)");

		private int value;

		private OPCODE(int value, String description) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	public enum RCODE {
			NO_ERROR(0, " No error condition"),
			FORMAT_ERROR(1, "Format error - The name server was unable to interpret the query."),
			SERVER_FAILURE(2, "Server failure - The name server was unable to process this query due to a problem with the name server."),
			NAME_ERROR(3, "Name Error - Meaningful only for responses from an authoritative name server, this code signifies that the domain name referenced in the query does not exist."),
			NOT_IMPLEMENTED(4, "Not Implemented - The name server does not support the requested kind of query."),
			REFUSED(
				5,
				"The name server refuses to perform the specified operation for policy reasons.  For example, a name server may not wish to provide the information to the particular requester, or a name server may not wish to perform a particular operation (e.g., zone transfer) for particular data.");

		private int value;

		private RCODE(int value, String description) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
}
