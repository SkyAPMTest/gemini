package com.a.eye.gemini.simulator;

public class ByteBitTest {

	public static void main(String[] args) {
		for (int x = 0; x < 8; x++) {
			String a = "";
			for (int y = 0; y < 8; y++) {
				if (y <= x) {
					a = a + "1";
				} else {
					a = "0" + a;
				}
			}
			System.out.println(a);

			int aa = Integer.parseInt(a, 2);
			System.out.println(Integer.toHexString(aa));
		}
	}
}
