package com.a.eye.gemini.sniffer.matcher;

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

import com.a.eye.gemini.sniffer.GeminiProducer;
import com.a.eye.gemini.sniffer.util.IdWorker;
import com.google.gson.JsonObject;

@Component
public class PacketMatch {

	private Logger logger = LogManager.getFormatterLogger(PacketMatch.class.getName());

	private static final IdWorker worker = new IdWorker(1, 1);

	@Autowired
	private GeminiProducer producer;

	public void handlePacket(PcapPacket packet) {
		if (packet.hasHeader(new Http())) {
			this.handleHttp(packet);
		}
	}

	public void handleHttp(PcapPacket packet) {
		JsonObject onceJson = new JsonObject();

		Ethernet eth = new Ethernet();
		packet.getHeader(eth);

		JsonObject macJson = new JsonObject();
		macJson.addProperty("frameNumber", packet.getFrameNumber());
		macJson.addProperty("source", FormatUtils.mac(eth.source()));
		macJson.addProperty("destination", FormatUtils.mac(eth.destination()));
		onceJson.add("mac", macJson);
		logger.debug("#%d Ethernet Json=%s", packet.getFrameNumber(), macJson.toString());

		Tcp tcp = new Tcp();
		packet.getHeader(tcp);

		JsonObject tcpJson = new JsonObject();
		tcpJson.addProperty("source", tcp.source());
		tcpJson.addProperty("destination", tcp.destination());
		tcpJson.addProperty("seq", tcp.seq());
		tcpJson.addProperty("ack", tcp.ack());
		tcpJson.addProperty("time", packet.getCaptureHeader().timestampInMillis());
		onceJson.add("tcp", tcpJson);
		logger.debug("#%d Tcp Json=%s", packet.getFrameNumber(), tcpJson.toString());

		Ip4 ip = new Ip4();
		packet.getHeader(ip);

		JsonObject ipJson = new JsonObject();
		ipJson.addProperty("frameNumber", packet.getFrameNumber());
		ipJson.addProperty("source", FormatUtils.ip(ip.source()));
		ipJson.addProperty("destination", FormatUtils.ip(ip.destination()));
		onceJson.add("ip", ipJson);

		logger.debug("#%d Ip4 Json=%s", packet.getFrameNumber(), ipJson.toString());

		if (tcp.destination() != 443) {
			Http http = new Http();
			packet.getHeader(http);

			logger.debug("#%d isResponse=%s", packet.getFrameNumber(), http.isResponse());
			// if (ContentType.HTML.equals(http.contentTypeEnum()) ||
			// (http.hasField(Request.Accept) &&
			// http.fieldValue(Request.Accept).contains("text/html"))) {
			if (http.isResponse()) {
				logger.debug("#" + packet.getFrameNumber() + " Res  ");
				JsonObject resJson = new JsonObject();
				for (Response res : Response.values()) {
					if (http.hasField(res)) {
						logger.debug(res.toString() + ":" + http.fieldValue(res) + "  ");
						resJson.addProperty(res.toString(), http.fieldValue(res));
					}
				}
				onceJson.add("res", resJson);
			} else {
				logger.debug("#" + packet.getFrameNumber() + " Req  ");
				JsonObject reqJson = new JsonObject();
				for (Request req : Request.values()) {
					if (http.hasField(req)) {
						logger.debug(req.toString() + ":" + http.fieldValue(req) + "  ");
						reqJson.addProperty(req.toString(), http.fieldValue(req));
					}
				}
				onceJson.add("req", reqJson);
			}

			logger.debug(packet.getCaptureHeader().timestampInMillis());

			logger.debug(onceJson.toString());
			ProducerRecord<Long, String> record = new ProducerRecord<Long, String>("sniffer-recevier-topic", worker.nextId(), onceJson.toString());
			producer.getProducer().send(record, new Callback() {
				public void onCompletion(RecordMetadata metadata, Exception e) {
					if (e != null)
						e.printStackTrace();
				}
			});
		}
		// }
		logger.debug("");
	}
}
