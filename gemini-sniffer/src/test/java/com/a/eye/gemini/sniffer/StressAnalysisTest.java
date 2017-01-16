package com.a.eye.gemini.sniffer;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import com.a.eye.gemini.sniffer.matcher.SnifferData;
import com.a.eye.gemini.sniffer.producer.GeminiProducer;
import com.a.eye.gemini.sniffer.util.IdWorker;

public class StressAnalysisTest {

	private static final IdWorker worker = new IdWorker(1, 1);

	private static long timezone = 8 * 60 * 60 * 1000;

	private static ProducerRecord<Long, byte[]> record;

	private static int i = 0;
	private static boolean isContinue = true;

	public static void main(String[] args) throws InterruptedException {
		int sleepTime = 200;
		if (args != null && args.length > 0) {
			sleepTime = Integer.valueOf(args[0]);
		}

		SnifferData SnifferData = new SnifferData();
		while (true) {
			System.out.println("#########################################");
			StressAnalysisTest.stressing(SnifferData, sleepTime);
			isContinue = true;
			i = 0;
		}
	}

	public static void stressing(SnifferData snifferData, int sleepTime) throws InterruptedException {
		long start = System.currentTimeMillis();
		while (isContinue) {
			long now = System.currentTimeMillis();
			long workId = worker.nextId();
			snifferData.setReqHost("home.asiainfo.com");
			snifferData.setReqRequesturl("/xiao_jun_0820/article/details/46911775");
			snifferData.setReqTcpSeq(String.valueOf(workId));
			snifferData.setReqTcpTime(String.valueOf(now - timezone));

			byte[] data = snifferData.encode();

			GeminiProducer producer = new GeminiProducer();
			record = new ProducerRecord<Long, byte[]>("gemini-sniffer-topic", workId, data);
			producer.getProducer().send(record, new Callback() {
				public void onCompletion(RecordMetadata metadata, Exception e) {
					if (e != null)
						e.printStackTrace();
				}
			});
			snifferData.clear();
			i++;
			if (i % 10000 == 0) {
				System.out.println(i);
				Thread.sleep(sleepTime);
			}
			if (i == 100000) {
				isContinue = false;
			}
		}
		long end = System.currentTimeMillis();
		System.out.println((end - start) / 1000);
	}
}
