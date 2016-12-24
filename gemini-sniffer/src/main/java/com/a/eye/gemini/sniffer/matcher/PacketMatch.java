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
import com.a.eye.gemini.sniffer.util.CallCountUtil;
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

		onceJson.addProperty("eth_frame_number", packet.getFrameNumber());
		onceJson.addProperty("eth_source", FormatUtils.mac(eth.source()));
		onceJson.addProperty("eth_destination", FormatUtils.mac(eth.destination()));
		logger.debug("#%d Ethernet Json=%s", packet.getFrameNumber(), onceJson.toString());

		Tcp tcp = new Tcp();
		packet.getHeader(tcp);

		onceJson.addProperty("tcp_source", tcp.source());
		onceJson.addProperty("tcp_destination", tcp.destination());
		onceJson.addProperty("tcp_seq", tcp.seq());
		onceJson.addProperty("tcp_ack", tcp.ack());
		onceJson.addProperty("tcp_time", packet.getCaptureHeader().timestampInMillis());
		logger.debug("#%d Tcp Json=%s", packet.getFrameNumber(), onceJson.toString());

		Ip4 ip = new Ip4();
		packet.getHeader(ip);

		onceJson.addProperty("ip_source", FormatUtils.ip(ip.source()));
		onceJson.addProperty("ip_destination", FormatUtils.ip(ip.destination()));

		logger.debug("#%d Ip4 Json=%s", packet.getFrameNumber(), onceJson.toString());

		if (tcp.destination() == 17909 && (FormatUtils.ip(ip.destination()).equals("10.19.7.66") || FormatUtils.ip(ip.destination()).equals("10.19.7.67"))) {
			logger.info("地址被过滤：%s", FormatUtils.ip(ip.destination()));
			return;
		} else {
			logger.info("地址未过滤：%s, 端口：%s", FormatUtils.ip(ip.destination()), tcp.destination());
		}

		if (tcp.destination() != 443) {
			Http http = new Http();
			packet.getHeader(http);

			logger.debug("#%d isResponse=%s", packet.getFrameNumber(), http.isResponse());
			// if (ContentType.HTML.equals(http.contentTypeEnum()) ||
			// (http.hasField(Request.Accept) &&
			// http.fieldValue(Request.Accept).contains("text/html"))) {
			if (http.isResponse()) {
				logger.debug("#" + packet.getFrameNumber() + " Res  ");
				onceJson.addProperty("is_res", true);
				for (Response res : Response.values()) {
					if (http.hasField(res)) {
						logger.debug(res.toString() + ":" + http.fieldValue(res) + "  ");
						onceJson.addProperty("res_" + res.toString(), http.fieldValue(res));
					}
				}
			} else {
				logger.debug("#" + packet.getFrameNumber() + " Req  ");
				onceJson.addProperty("is_res", false);
				for (Request req : Request.values()) {
					if (http.hasField(req)) {
						logger.debug(req.toString() + ":" + http.fieldValue(req) + "  ");
						onceJson.addProperty("req_" + req.toString(), http.fieldValue(req));
					}
				}
			}

			logger.debug(packet.getCaptureHeader().timestampInMillis());

			CallCountUtil.increment();

			logger.debug(onceJson.toString());
			ProducerRecord<Long, String> record = new ProducerRecord<Long, String>("gemini-sniffer-topic", worker.nextId(), onceJson.toString());
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
