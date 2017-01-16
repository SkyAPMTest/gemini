package com.a.eye.gemini.sniffer.matcher;

public class SnifferData {
	private String reqHost;
	private String reqRequesturl;
	private String reqTcpSeq;
	private String reqTcpTime;

	private int offset = 0;

	public void setReqHost(String reqHost) {
		this.reqHost = reqHost;
	}

	public void setReqRequesturl(String reqRequesturl) {
		this.reqRequesturl = reqRequesturl;
	}

	public void setReqTcpSeq(String reqTcpSeq) {
		this.reqTcpSeq = reqTcpSeq;
	}

	public void setReqTcpTime(String reqTcpTime) {
		this.reqTcpTime = reqTcpTime;
	}

	public byte[] encode() {
		byte[] _1 = reqHost.getBytes();
		byte[] _2 = reqRequesturl.getBytes();
		byte[] _3 = reqTcpSeq.getBytes();
		byte[] _4 = reqTcpTime.getBytes();
		byte[] data = new byte[_1.length + _2.length + _3.length + _4.length + 4];

		arrayCopy(_1, data);
		arrayCopy(_2, data);
		arrayCopy(_3, data);
		arrayCopy(_4, data);

		return data;
	}

	public void decode(byte[] data) {
		int offset = 0;
		while (offset != data.length) {
			int length = data[offset];
			offset += 1;
			byte[] value = new byte[length];
			System.arraycopy(data, offset, value, 0, length);
			offset += length;

			System.out.println(new String(value));
		}
	}

	public void clear() {
		reqHost = null;
		reqRequesturl = null;
		reqTcpSeq = null;
		reqTcpTime = null;
		offset = 0;
	}

	private void arrayCopy(byte[] src, byte[] dest) {
		dest[offset] = (byte) src.length;
		offset += 1;
		System.arraycopy(src, 0, dest, offset, src.length);
		offset += src.length;
	}
}
