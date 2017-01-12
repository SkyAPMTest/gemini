package com.a.eye.gemini.simulator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import com.a.eye.gemini.simulator.bit.BitInputStream;
import com.a.eye.gemini.simulator.resolve.PcapARPStructResolve;
import com.a.eye.gemini.simulator.resolve.PcapDNSAnswerStructResolve;
import com.a.eye.gemini.simulator.resolve.PcapDNSStructResolve;
import com.a.eye.gemini.simulator.resolve.PcapEthernetStructResolve;
import com.a.eye.gemini.simulator.resolve.PcapFrameStructResolve;
import com.a.eye.gemini.simulator.resolve.PcapIPv4StructResolve;
import com.a.eye.gemini.simulator.resolve.PcapIPv6StructResolve;
import com.a.eye.gemini.simulator.resolve.PcapTcpStructResolve;
import com.a.eye.gemini.simulator.resolve.PcapUDPStructResolve;
import com.a.eye.gemini.simulator.resolve.ResolverRegister;

public class PcapFileReaderTest {

	public static void main(String[] args) {
		long startTime = new Date().getTime();

		ResolverRegister.registeResolve(PcapFrameStructResolve.Name, PcapFrameStructResolve.inst);
		ResolverRegister.registeResolve(PcapEthernetStructResolve.Name, PcapEthernetStructResolve.inst);
		ResolverRegister.registeResolve(PcapIPv4StructResolve.Name, PcapIPv4StructResolve.inst);
		ResolverRegister.registeResolve(PcapTcpStructResolve.Name, PcapTcpStructResolve.inst);
		ResolverRegister.registeResolve(PcapUDPStructResolve.Name, PcapUDPStructResolve.inst);
		ResolverRegister.registeResolve(PcapDNSStructResolve.Name, PcapDNSStructResolve.inst);
		ResolverRegister.registeResolve(PcapDNSAnswerStructResolve.Name, PcapDNSAnswerStructResolve.inst);
		ResolverRegister.registeResolve(PcapARPStructResolve.Name, PcapARPStructResolve.inst);
		ResolverRegister.registeResolve(PcapIPv6StructResolve.Name, PcapIPv6StructResolve.inst);

		PcapFrameStructResolve.createInvertedAttrs();

		URL url = Thread.currentThread().getContextClassLoader().getResource("20161226222122_gemini_sniffer.pcap");
		InputStream in = null;

		try {
			in = new FileInputStream(url.getPath());

			// read(in, (66 + 40));

			int i = 1;
			while (true) {
				if (i > 1) {
					BitInputStream.resetLengthSum();
				}
				PcapFrameStructResolve.inst.resolve(PcapFrameStructResolve.Name, in, i);
				i++;
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

		long endTime = new Date().getTime();

		System.out.println("耗时：" + (endTime - startTime));
	}

	public static byte[] read(InputStream in, int length) throws IOException {
		byte[] byteread = new byte[length];
		in.read(byteread, 0, length);
		return byteread;
	}
}
