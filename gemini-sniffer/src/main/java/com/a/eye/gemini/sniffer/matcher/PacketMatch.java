package com.a.eye.gemini.sniffer.matcher;

import java.util.Map;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Http;
import org.jnetpcap.protocol.tcpip.Http.Request;
import org.jnetpcap.protocol.tcpip.Http.Response;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.a.eye.gemini.sniffer.producer.GeminiProducer;
import com.a.eye.gemini.sniffer.util.CallCountUtil;
import com.a.eye.gemini.sniffer.util.IdWorker;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Component
public class PacketMatch {

	private long timezone = 8 * 60 * 60 * 1000;

	private long sum = 0;

	private Logger logger = LogManager.getFormatterLogger(this.getClass().getName());

	@Autowired
	private PacketReqCache cache;

	private ProducerRecord<Long, String> record;

	private static final IdWorker worker = new IdWorker(1, 1);

	private Ethernet eth = new Ethernet();

	private Tcp tcp = new Tcp();

	private Ip4 ip = new Ip4();

	private Http http = new Http();

	private JsonObject onceJson;

	private long lastTime = 0;

	private String reqOrRes = "";

	@Autowired
	private GeminiProducer producer;

	public void handlePacket(PcapPacket packet) {
		if (packet.hasHeader(new Http())) {
			this.handleHttp(packet);
		}
	}

	public void handleHttp(PcapPacket packet) {
		onceJson = new JsonObject();
		packet.getHeader(http);

		logger.debug("#%d isResponse=%s", packet.getFrameNumber(), http.isResponse());
		if (http.isResponse()) {
			reqOrRes = "res_";

			logger.debug("#" + packet.getFrameNumber() + " Res  ");
			for (Response res : Response.values()) {
				if (http.hasField(res)) {
					logger.debug(res.toString() + ":" + http.fieldValue(res) + "  ");
					onceJson.addProperty(reqOrRes + res.toString().toLowerCase(), http.fieldValue(res));
				}
			}
		} else {
			reqOrRes = "req_";

			logger.debug("#" + packet.getFrameNumber() + " Req  ");
			for (Request req : Request.values()) {
				if (http.hasField(req)) {
					logger.debug(req.toString() + ":" + http.fieldValue(req) + "  ");
					onceJson.addProperty(reqOrRes + req.toString().toLowerCase(), http.fieldValue(req));
				}
			}
		}

		packet.getHeader(eth);

		onceJson.addProperty(reqOrRes + "eth_frame_number", packet.getFrameNumber());
		onceJson.addProperty(reqOrRes + "eth_source", FormatUtils.mac(eth.source()));
		onceJson.addProperty(reqOrRes + "eth_destination", FormatUtils.mac(eth.destination()));
		logger.debug("#%d Ethernet Json=%s", packet.getFrameNumber(), onceJson.toString());

		packet.getHeader(tcp);

		onceJson.addProperty(reqOrRes + "tcp_source", tcp.source());
		onceJson.addProperty(reqOrRes + "tcp_destination", tcp.destination());
		long seq = tcp.seq();
		long ack = tcp.ack();
		onceJson.addProperty(reqOrRes + "tcp_seq", tcp.seq());
		onceJson.addProperty(reqOrRes + "tcp_ack", tcp.ack());
		onceJson.addProperty(reqOrRes + "tcp_time", packet.getCaptureHeader().timestampInMillis() - timezone);
		logger.debug("#%d Tcp Json=%s", packet.getFrameNumber(), onceJson.toString());
		logger.debug("#%d, seq=%s, ack=%s", packet.getFrameNumber(), tcp.seq(), tcp.ack());

		packet.getHeader(ip);

		onceJson.addProperty(reqOrRes + "ip_source", FormatUtils.ip(ip.source()));
		onceJson.addProperty(reqOrRes + "ip_destination", FormatUtils.ip(ip.destination()));

		logger.debug("#%d Ip4 Json=%s", packet.getFrameNumber(), onceJson.toString());

		logger.debug(packet.getCaptureHeader().timestampInMillis());

		CallCountUtil.increment();

		if (!http.isResponse()) {
			cache.cacheData(ack, onceJson);
		} else {
			JsonObject reqJson = cache.findPair(seq);
			if (reqJson != null) {
				logger.debug(onceJson.toString());
				for (Map.Entry<String, JsonElement> entry : reqJson.entrySet()) {
					onceJson.addProperty(entry.getKey(), entry.getValue().getAsString());
				}
				logger.debug(onceJson.toString());

				long workId = worker.nextId();
				record = new ProducerRecord<Long, String>("gemini-sniffer-topic", workId, onceJson.toString());
//				producer.getProducer().send(record, new Callback() {
//					public void onCompletion(RecordMetadata metadata, Exception e) {
//						record = null;
//						if (e != null)
//							e.printStackTrace();
//					}
//				});
			}
			reqJson = null;
		}
		onceJson = null;

		sum++;
		if (sum % 1000 == 0) {
			long currentTime = System.currentTimeMillis() - timezone;
			if (lastTime != 0) {
				logger.info("sniffer interval = %s second, count = %s", (currentTime - lastTime) / 1000, sum);
			} else {
				logger.info("sniffer time = %s, count = %s", currentTime, sum);
			}
			lastTime = currentTime;
		}
	}
}
