package com.a.eye.gemini.simulator;

import java.util.Map;

import com.a.eye.gemini.simulator.resolve.Attribute;
import com.a.eye.gemini.simulator.resolve.PcapDNSStructResolve;
import com.a.eye.gemini.simulator.resolve.PcapEthernetStructResolve;
import com.a.eye.gemini.simulator.resolve.PcapFrameStructResolve;
import com.a.eye.gemini.simulator.resolve.PcapIPv4StructResolve;
import com.a.eye.gemini.simulator.resolve.PcapTcpStructResolve;
import com.a.eye.gemini.simulator.resolve.PcapUDPStructResolve;
import com.a.eye.gemini.simulator.resolve.ResolverRegister;

public class ProtocolAttrsTest {

	public static void main(String[] args) {
		ResolverRegister.registeResolve(PcapFrameStructResolve.Name, PcapFrameStructResolve.inst);
		ResolverRegister.registeResolve(PcapEthernetStructResolve.Name, PcapEthernetStructResolve.inst);
		ResolverRegister.registeResolve(PcapIPv4StructResolve.Name, PcapIPv4StructResolve.inst);
		ResolverRegister.registeResolve(PcapTcpStructResolve.Name, PcapTcpStructResolve.inst);
		ResolverRegister.registeResolve(PcapUDPStructResolve.Name, PcapUDPStructResolve.inst);
		ResolverRegister.registeResolve(PcapDNSStructResolve.Name, PcapDNSStructResolve.inst);

		PcapFrameStructResolve.createInvertedAttrs();

		Map<String, Map<String, Attribute>> attrMap = PcapFrameStructResolve.getAttrs();
		for (Map.Entry<String, Map<String, Attribute>> entry : attrMap.entrySet()) {
			System.out.println(entry.getKey());
			for (Map.Entry<String, Attribute> attrEntry : entry.getValue().entrySet()) {
				Attribute attr = attrEntry.getValue();
				System.out.printf("  >> %s : \t next = %s, \t parent = %s, \t length = %s, \t data_type = %s \n", attrEntry.getKey(), attr.getNext(), attr.getParent(), attr.getLength(),
						attr.getDataType());
			}
		}
	}

}
