package com.a.eye.gemini.simulator.resolve;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.a.eye.gemini.simulator.resolve.Attribute.DataType;

// https://en.wikipedia.org/wiki/Domain_Name_System
// https://tools.ietf.org/html/rfc1035 4.1.1. Header section format
public class PcapDNSStructResolve extends GeminiResolveLowBase {

	public static final String Name = "DNS";

	private PcapDNSStructResolve() {
	}

	public static PcapDNSStructResolve inst = new PcapDNSStructResolve();

	public static final String id = "id";
	public static final String qr = "qr";
	public static final String opcode = "opcode";
	public static final String aa = "aa";
	public static final String tc = "tc";
	public static final String rd = "rd";
	public static final String ra = "ra";
	public static final String z = "z";
	public static final String rcode = "rcode";
	public static final String question = "question";
	public static final String answer = "answer";
	public static final String authority = "authority";
	public static final String additional = "additional";
	public static final String qname = "qname";
	public static final String qtype = "qtype";
	public static final String qclass = "qclass";
	public static final String answerdata = "answerdata";

	static {
		Map<String, Attribute> dnsAttrs = new HashMap<String, Attribute>();
		dnsAttrs.put(id, new Attribute(id, 2 * BYTE, DataType.Hex, Attribute.First));
		dnsAttrs.put(qr, new Attribute(qr, 1 * BIT, DataType.Integer, id));
		dnsAttrs.put(opcode, new Attribute(opcode, 4 * BIT, DataType.Integer, qr));
		dnsAttrs.put(aa, new Attribute(aa, 1 * BIT, DataType.Integer, opcode));
		dnsAttrs.put(tc, new Attribute(tc, 1 * BIT, DataType.Integer, aa));
		dnsAttrs.put(rd, new Attribute(rd, 1 * BIT, DataType.Integer, tc));
		dnsAttrs.put(ra, new Attribute(ra, 1 * BIT, DataType.Integer, rd));
		dnsAttrs.put(z, new Attribute(z, 3 * BIT, DataType.Integer, ra));
		dnsAttrs.put(rcode, new Attribute(rcode, 4 * BIT, DataType.Integer, z));
		dnsAttrs.put(question, new Attribute(question, 2 * BYTE, DataType.Integer, rcode));
		dnsAttrs.put(answer, new Attribute(answer, 2 * BYTE, DataType.Integer, question));
		dnsAttrs.put(authority, new Attribute(authority, 2 * BYTE, DataType.Integer, answer));
		dnsAttrs.put(additional, new Attribute(additional, 2 * BYTE, DataType.Integer, authority));
		dnsAttrs.put(qname, new Attribute(qname, 0, DataType.Custom, additional));
		dnsAttrs.put(qtype, new Attribute(qtype, 2 * BYTE, DataType.Integer, qname));
		dnsAttrs.put(qclass, new Attribute(qclass, 2 * BYTE, DataType.Integer, qtype));
		dnsAttrs.put(answerdata, new Attribute(answerdata, 0, DataType.Data, qclass));
		createAttrs(Name, dnsAttrs);
	}

	@Override
	public void dataResolve(String name, InputStream in, long frameId) throws IOException {
		if (getAsInteger(answer) >= 1) {
			ResolverRegister.getResolve(PcapDNSAnswerStructResolve.Name).resolve(PcapDNSAnswerStructResolve.Name, in, frameId);
		}
	}

	@Override
	public void custom(Attribute attr, Long frameId, InputStream in) throws IOException {
		if (qname.equals(attr.getName())) {
			String dns = "";
			int length = 0;
			for (int i = 0; i < 100; i++) {
				byte[] segLength = bitInputStream.readBit(in, 8);
				String strread = binaryToHexString(segLength);
				length = length + 1;
				Integer lengthValue = Integer.parseInt(strread, 16);
				length = length + lengthValue;
				if (lengthValue == 0) {
					break;
				}
				byte[] dnsSeg = bitInputStream.readBit(in, lengthValue * 8);
				if (i == 0) {
					dns = dns + new String(dnsSeg);
				} else {
					dns = dns + "." + new String(dnsSeg);
				}
			}
			setValue(qname, dns);
			System.out.printf("%s %s >> %s, %s %sbit \n", frameId, Name, qname, dns, length * 8);
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
