package com.a.eye.gemini.sniffer.core;

import org.jnetpcap.Pcap;
import org.jnetpcap.protocol.JProtocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.a.eye.gemini.sniffer.handler.SnifferOnlineHandler;

@Component
public class SnifferCoreOnline extends SnifferCoreBase {

	@Autowired
	private SnifferOnlineHandler handler;

	@Override
	protected void loop(Pcap pcap) {
		pcap.loop(0, JProtocol.ETHERNET_ID, handler, "gemini");
	}

}
