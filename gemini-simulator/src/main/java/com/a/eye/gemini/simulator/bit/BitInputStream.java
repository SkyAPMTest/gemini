package com.a.eye.gemini.simulator.bit;

import java.io.IOException;
import java.io.InputStream;

public class BitInputStream {

	private static int length_sum = -(40 * 8);

	private byte stream = 0;

	private int offset = 0;

	public static void resetLengthSum() {
		length_sum = 0;
	}

	public int getLengthSum() {
		return length_sum;
	}

	public byte[] readBit(InputStream in, int len) throws IOException {
		length_sum = length_sum + len;

		int arrayLen = 0;
		if (8 - offset >= len) {
			arrayLen = 1;
		} else {
			arrayLen = (int) Math.ceil((len - (8 - offset)) / 8) + 1;
		}

		int bitLenread = 0;
		byte[] byteread = new byte[arrayLen];
		for (int i = 0; i < arrayLen; i++) {
			if (i == 0) {
				bitLenread = bitLenread + 8 - offset;
				if (arrayLen == 1) {
					byteread[i] = readOneByteBits(in, len);
				} else {
					byteread[i] = readOneByteBits(in, 8 - offset);
				}
			} else if (i == arrayLen - 1) {
				byteread[i] = readOneByteBits(in, len - bitLenread);
			} else {
				bitLenread = bitLenread + 8;
				byteread[i] = readOneByteBits(in, 8);
			}
		}

		return byteread;
	}

	public byte readOneByteBits(InputStream in, int len) throws IOException {
		byte[] byteread = new byte[1];
		if (offset == 0) {
			in.read(byteread, 0, 1);
			stream = byteread[0];
			offset = 0;
		}
		// System.out.println("offset: " + offset + ", len: " + len);

		switch (len) {
		case 1:
			byteread[0] = (byte) ((stream >> (8 - offset - len)) & 0x1);
			break;
		case 2:
			byteread[0] = (byte) ((stream >> (8 - offset - len)) & 0x3);
			break;
		case 3:
			byteread[0] = (byte) ((stream >> (8 - offset - len)) & 0x7);
			break;
		case 4:
			byteread[0] = (byte) ((stream >> (8 - offset - len)) & 0xf);
			break;
		case 5:
			byteread[0] = (byte) ((stream >> (8 - offset - len)) & 0x1f);
			break;
		case 6:
			byteread[0] = (byte) ((stream >> (8 - offset - len)) & 0x3f);
			break;
		case 7:
			byteread[0] = (byte) ((stream >> (8 - offset - len)) & 0x7f);
			break;
		case 8:
			byteread[0] = (byte) ((stream >> (8 - offset - len)) & 0xff);
			break;
		}

		offset = offset + len;
		if (offset == 8) {
			offset = 0;
		}
		return byteread[0];
	}

	public byte[] readByte(InputStream in, byte[] b, int len) throws IOException {
		byte[] byteread = new byte[len];
		in.read(byteread, 0, len);
		return byteread;
	}
}
