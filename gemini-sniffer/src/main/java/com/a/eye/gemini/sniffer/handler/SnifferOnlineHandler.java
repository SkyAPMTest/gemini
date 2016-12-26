package com.a.eye.gemini.sniffer.handler;

import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.a.eye.gemini.sniffer.matcher.PacketMatch;

@Component
public class SnifferOnlineHandler implements PcapPacketHandler<String> {// 抓到包后送去检测

	@Autowired
	private PacketMatch packetMatch;

	@Override
	public void nextPacket(PcapPacket packet, String obj) {
		try {
			packetMatch.handlePacket(packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
