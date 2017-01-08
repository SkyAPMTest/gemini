package com.a.eye.gemini.simulator.util;

public class FormatUtils {
	/**
	 * Ip.
	 * 
	 * @param address
	 *            the address
	 * @return the string
	 */
	public static String ip(byte[] address) {
		if (address.length == 4) {
			return asString(address, '.', 10);
		} else {
			return asStringIp6(address, true);
		}
	}

	/**
	 * Mac.
	 * 
	 * @param address
	 *            the address
	 * @return the string
	 */
	public static String mac(byte[] address) {
		return asStringZeroPad(address, ':', 16, 0, address.length);
	}

	/**
	 * Handles various forms of ip6 addressing
	 * 
	 * <pre>
	 * 2001:0db8:0000:0000:0000:0000:1428:57ab
	 * 2001:0db8:0000:0000:0000::1428:57ab
	 * 2001:0db8:0:0:0:0:1428:57ab
	 * 2001:0db8:0:0::1428:57ab
	 * 2001:0db8::1428:57ab
	 * 2001:db8::1428:57ab
	 * </pre>
	 * 
	 * .
	 * 
	 * @param array
	 *            address array
	 * @param holes
	 *            if true holes are allowed
	 * @return formatted string
	 */
	public static String asStringIp6(byte[] array, boolean holes) {
		StringBuilder buf = new StringBuilder();

		int len = 0;
		int start = -1;
		/*
		 * Check for byte compression where sequential zeros are replaced with
		 * ::
		 */
		for (int i = 0; i < array.length && holes; i++) {
			if (array[i] == 0) {
				if (len == 0) {
					start = i;
				}

				len++;
			}

			/*
			 * Only the first sequence of 0s is compressed, so break out
			 */
			if (array[i] != 0 && len != 0) {
				break;
			}
		}

		/*
		 * Now round off to even length so that only pairs are compressed
		 */
		if (start != -1 && (start % 2) == 1) {
			start++;
			len--;
		}

		if (start != -1 && (len % 2) == 1) {
			len--;
		}

		for (int i = 0; i < array.length; i++) {
			if (i == start) {
				buf.append(':');
				i += len - 1;

				if (i == array.length - 1) {
					buf.append(':');
				}
				continue;
			}

			byte b = array[i];

			if (buf.length() != 0 && (i % 2) == 0) {
				buf.append(':');
			}
			if (b < 16) {
				buf.append('0');
			}
			buf.append(Integer.toHexString((b < 0) ? b + 256 : b).toUpperCase());
		}

		return buf.toString();
	}

	/**
	 * Converts the given byte array to a string using a default separator
	 * character.
	 * 
	 * @param array
	 *            array to convert
	 * @return the converted string
	 */
	public static String asString(byte[] array) {
		return asString(array, ':');
	}

	/**
	 * Convers the given byte array to a string using the supplied separator
	 * character.
	 * 
	 * @param array
	 *            array to convert
	 * @param separator
	 *            separator character to use in between array elements
	 * @return the converted string
	 */
	public static String asString(byte[] array, char separator) {
		return asString(array, separator, 16); // Default HEX
	}

	/**
	 * Converts the given byte array to a string using the supplied separator
	 * character and radix for conversion of the numerical component.
	 * 
	 * @param array
	 *            array to convert
	 * @param separator
	 *            separator character to use in between array elements
	 * @param radix
	 *            numerical radix to use for numbers
	 * @return the converted string
	 */
	public static String asString(byte[] array, char separator, int radix) {
		return asString(array, separator, radix, 0, array.length);
	}

	/**
	 * Convers the given byte array to a string using the supplied separator
	 * character.
	 * 
	 * @param array
	 *            array to convert
	 * @param separator
	 *            separator character to use in between array elements
	 * @param radix
	 *            the radix
	 * @param start
	 *            the start
	 * @param len
	 *            the len
	 * @return the converted string
	 */
	public static String asString(byte[] array, char separator, int radix, int start, int len) {

		final StringBuilder buf = new StringBuilder();

		for (int i = start; i < (start + len); i++) {
			byte b = array[i];
			if (buf.length() != 0) {
				buf.append(separator);
			}

			buf.append(Integer.toString((b < 0) ? b + 256 : b, radix).toUpperCase());
		}

		return buf.toString();
	}

	/**
	 * Convers the given byte array to a string using the supplied separator
	 * character.
	 * 
	 * @param array
	 *            array to convert
	 * @param separator
	 *            separator character to use in between array elements
	 * @param radix
	 *            the radix
	 * @param start
	 *            the start
	 * @param len
	 *            the len
	 * @return the converted string
	 */
	public static String asStringZeroPad(byte[] array, char separator, int radix, int start, int len) {

		final StringBuilder buf = new StringBuilder();

		for (int i = start; i < (start + len); i++) {
			byte b = array[i];
			if (buf.length() != 0) {
				buf.append(separator);
			}

			final String s = Integer.toString((b < 0) ? b + 256 : b, radix).toUpperCase();

			if (s.length() == 1) {
				buf.append('0');
			}

			buf.append(s);
		}

		return buf.toString();
	}
}
