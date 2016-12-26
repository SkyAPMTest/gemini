package com.a.eye.gemini.sniffer.core;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapDumper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.a.eye.gemini.sniffer.handler.SnifferOfflineWriterHandler;
import com.a.eye.gemini.sniffer.setting.GeminiSettings;

@Component
public class SnifferCoreOfflineWriter extends SnifferCoreBase {

	public static final String DATE_FORMAT_NOW = "yyyyMMddHHmmss";

	@Autowired
	private GeminiSettings settings;

	@Autowired
	private SnifferOfflineWriterHandler handler;

	@Override
	protected void loop(Pcap pcap) {
		while (true) {
			// 10 seconds
			final int CAPTURE_INTERVAL = Integer.parseInt(settings.getIntervalTime()) * 1000;
			final long interval = System.currentTimeMillis() + CAPTURE_INTERVAL;
			String ofile = settings.getFilePath() + "/" + now().toString() + "_gemini_sniffer.pcap";
			final PcapDumper dumper = pcap.dumpOpen(ofile);
			handler.setDumper(dumper);
			handler.setInterval(interval);
			pcap.loop(Pcap.LOOP_INFINITE, handler, pcap);
			dumper.close();
		}
	}

	private static String now() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT_NOW);
		return df.format(cal.getTime());
	}
}
