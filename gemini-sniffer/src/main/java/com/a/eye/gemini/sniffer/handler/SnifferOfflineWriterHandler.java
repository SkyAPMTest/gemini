package com.a.eye.gemini.sniffer.handler;

import org.jnetpcap.JBufferHandler;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapDumper;
import org.jnetpcap.PcapHeader;
import org.jnetpcap.nio.JBuffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.a.eye.gemini.sniffer.matcher.PacketMatch;

@Component
public class SnifferOfflineWriterHandler implements JBufferHandler<Pcap> {// 抓到包后送去检测

	@Autowired
	private PacketMatch packetMatch;

	private PcapDumper dumper;

	private long interval;

	@Override
	public void nextPacket(PcapHeader header, JBuffer buffer, Pcap pcap) {
		dumper.dump(header, buffer);
		if (System.currentTimeMillis() > interval) {
			pcap.breakloop();
		}
	}

	public void setDumper(PcapDumper dumper) {
		this.dumper = dumper;
	}

	public void setInterval(long interval) {
		this.interval = interval;
	}
}
