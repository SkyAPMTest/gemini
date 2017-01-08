package com.a.eye.gemini.simulator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.a.eye.gemini.simulator.resolve.PcapDNSStructResolve;
import com.a.eye.gemini.simulator.resolve.PcapEthernetStructResolve;
import com.a.eye.gemini.simulator.resolve.PcapFrameStructResolve;
import com.a.eye.gemini.simulator.resolve.PcapIPv4StructResolve;
import com.a.eye.gemini.simulator.resolve.PcapTcpStructResolve;
import com.a.eye.gemini.simulator.resolve.PcapUDPStructResolve;
import com.a.eye.gemini.simulator.resolve.ResolverRegister;

public class Creator {

	public static void main(String[] args) {
		ResolverRegister.registeResolve(PcapFrameStructResolve.Name, PcapFrameStructResolve.inst);
		ResolverRegister.registeResolve(PcapEthernetStructResolve.Name, PcapEthernetStructResolve.inst);
		ResolverRegister.registeResolve(PcapIPv4StructResolve.Name, PcapIPv4StructResolve.inst);
		ResolverRegister.registeResolve(PcapTcpStructResolve.Name, PcapTcpStructResolve.inst);
		ResolverRegister.registeResolve(PcapUDPStructResolve.Name, PcapUDPStructResolve.inst);
		ResolverRegister.registeResolve(PcapDNSStructResolve.Name, PcapDNSStructResolve.inst);

		URL url = Thread.currentThread().getContextClassLoader().getResource("20161226222122_gemini_sniffer.pcap");
		InputStream in = null;

		try {
			in = new FileInputStream(url.getPath());
			for (int i = 1; i < 10; i++) {
				PcapFrameStructResolve.inst.resolve(in, i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e1) {
				}
			}
		}
	}

	public static byte[] read(InputStream in, int length) throws IOException {
		byte[] byteread = new byte[length];
		in.read(byteread, 0, length);
		return byteread;
	}
}
