package com.a.eye.gemini.simulator.resolve;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.a.eye.gemini.simulator.resolve.Attribute.DataType;

public class PcapDNSAnswerStructResolve extends GeminiResolveLowBase {

	public static final String Name = "DNS_ANSWER";

	private PcapDNSAnswerStructResolve() {
	}

	public static PcapDNSAnswerStructResolve inst = new PcapDNSAnswerStructResolve();

	public static final String flag = "flag";
	public static final String atype = "atype";
	public static final String aclass = "aclass";
	public static final String ttl = "ttl";
	public static final String rdlength = "rdlength";
	public static final String rdata = "rdata";

	static {
		Map<String, Attribute> dnsAnswerAttrs = new HashMap<String, Attribute>();
		dnsAnswerAttrs.put(flag, new Attribute(flag, 2 * BYTE, DataType.Hex, Attribute.First));
		dnsAnswerAttrs.put(atype, new Attribute(atype, 2 * BYTE, DataType.Integer, flag));
		dnsAnswerAttrs.put(aclass, new Attribute(aclass, 2 * BYTE, DataType.Integer, atype));
		dnsAnswerAttrs.put(ttl, new Attribute(ttl, 4 * BYTE, DataType.Long, aclass));
		dnsAnswerAttrs.put(rdlength, new Attribute(rdlength, 2 * BYTE, DataType.Integer, ttl));
		dnsAnswerAttrs.put(rdata, new Attribute(rdata, 0, DataType.Custom, rdlength));
		createAttrs(Name, dnsAnswerAttrs);
	}

	@Override
	public void dataResolve(String name, InputStream in, long frameId) throws IOException {
	}

	@Override
	public void custom(Attribute attr, Long frameId, InputStream in) throws IOException {
		Integer answer = getAsInteger(PcapDNSStructResolve.answer);
		for (int i = 0; i < answer; i++) {

		}
		if (rdata.equals(attr.getName())) {
			Integer rdlengthValue = getAsInteger(rdlength);
			String address = "";
			for (int i = 0; i < rdlengthValue; i++) {
				String addr = binaryToHexString(bitInputStream.readBit(in, 8));
				if (i == 0) {
					address = "" + Integer.parseInt(addr, 16);
				} else {
					address = address + "." + Integer.parseInt(addr, 16);
				}
			}
			setValue(rdata, address);
			System.out.printf("%s %s >> %s, %s %sbit \n", frameId, Name, rdata, address, rdlengthValue * 8);
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

	public enum QTYPE {
		A("A", "a host address"), CNAME("CNAME", "identifies the canonical name of an alias"), HINFO("HINFO", "identifies the CPU and OS used by a host"), MX("MX",
			"identifies a mail exchange for the domain.  See [RFC-974 for details."), NS("NS", "the authoritative name server for the domain"), PTR("PTR",
			"a pointer to another part of the domain name space"), SOA("SOA", "identifies the start of a zone of authority]");

		private String value;

		private QTYPE(String value, String description) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	public enum QCLASS {
		IN("IN", "the Internet system"), CH("CH", "the Chaos system");

		private String value;

		private QCLASS(String value, String description) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
}
