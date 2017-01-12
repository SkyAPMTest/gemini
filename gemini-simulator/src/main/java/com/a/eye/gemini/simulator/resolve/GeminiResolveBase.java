package com.a.eye.gemini.simulator.resolve;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.a.eye.gemini.common.util.StringUtils;
import com.a.eye.gemini.simulator.bit.BitInputStream;
import com.a.eye.gemini.simulator.resolve.Attribute.DataType;
import com.a.eye.gemini.simulator.util.FormatUtils;

public abstract class GeminiResolveBase extends ResolveBase {

	protected BitInputStream bitInputStream = new BitInputStream();

	@Override
	public void resolve(String name, InputStream in, long frameId) throws IOException {
		System.out.println(frameId);
		int skipLog = 0;
		if (frameId > skipLog) {
			System.out.println(name);
		}
		Map<String, Attribute> attrs = getAttrs().get(name);
		String attrName = attrs.get(Attribute.First).getNext();

		boolean hasNext = true;
		while (hasNext) {
			Attribute attr = attrs.get(attrName);
			attrName = attr.getNext();
			if (attr.getNext() == null || "".equals(attr.getNext())) {
				hasNext = false;
			}

			byte[] byteread = null;

			if (DataType.Data.equals(attr.getDataType())) {
				dataResolve(name, in, frameId);
				continue;
			}
			if (DataType.Custom.equals(attr.getDataType())) {
				custom(attr, frameId, in);
				continue;
			}
			if (DataType.Frame.equals(attr.getDataType())) {
				setValue(attr.getName(), frameId);
				continue;
			}
			if (attr.isSkipAfterFirstTime() && frameId != 1) {
				continue;
			}

			byteread = bitInputStream.readBit(in, attr.getLength());

			if (DataType.String.equals(attr.getDataType())) {
				setValue(attr.getName(), new String(byteread));
				if (frameId > skipLog) {
					System.out.printf("%s %s >> %s, %s, %sbit, %s, %s \n", frameId, name, attr.getName(), new String(byteread), attr.getLength(), attr.getDataType(), binaryToHexString(byteread));
				}
			} else {
				String strread = binaryToHexString(byteread);
				if (DataType.Integer.equals(attr.getDataType())) {
					setValue(attr.getName(), Integer.parseInt(strread, 16));
					if (frameId > skipLog) {
						System.out.printf("%s %s >> %s, %s, %sbit, %s, %s \n", frameId, name, attr.getName(), Integer.parseInt(strread, 16), attr.getLength(), attr.getDataType(), strread);
					}
				} else if (DataType.Long.equals(attr.getDataType())) {
					setValue(attr.getName(), Long.parseLong(strread, 16));
					if (frameId > skipLog) {
						System.out.printf("%s %s >> %s, %s, %sbit, %s, %s\n", frameId, name, attr.getName(), Long.parseLong(strread, 16), attr.getLength(), attr.getDataType(), strread);
					}
				} else if (DataType.Mac.equals(attr.getDataType())) {
					setValue(attr.getName(), FormatUtils.mac(byteread));
					if (frameId > skipLog) {
						System.out.printf("%s %s >> %s, %s, %sbit, %s, %s \n", frameId, name, attr.getName(), FormatUtils.mac(byteread), attr.getLength(), attr.getDataType(), strread);
					}
				} else if (DataType.IP.equals(attr.getDataType())) {
					setValue(attr.getName(), FormatUtils.ip(byteread));
					if (frameId > skipLog) {
						System.out.printf("%s %s >> %s, %s, %sbit, %s, %s \n", frameId, name, attr.getName(), FormatUtils.ip(byteread), attr.getLength(), attr.getDataType(), strread);
					}
				} else if (DataType.Hex.equals(attr.getDataType())) {
					setValue(attr.getName(), byteread);
					if (frameId > skipLog) {
						System.out.printf("%s %s >> %s, %s, %sbit, %s, %s \n", frameId, name, attr.getName(), StringUtils.BinaryToHexString(byteread), attr.getLength(), attr.getDataType(), strread);
					}
				} else {
				}
			}
		}
	}

	public abstract String binaryToHexString(byte[] bytes);

	public abstract void custom(Attribute attr, Long frameId, InputStream in) throws IOException;

	public static String mac(byte[] paramArrayOfByte) {
		return asStringZeroPad(paramArrayOfByte, ':', 16, 0, paramArrayOfByte.length);
	}

	public static String asStringZeroPad(byte[] paramArrayOfByte, char paramChar, int paramInt1, int paramInt2, int paramInt3) {
		StringBuilder localStringBuilder = new StringBuilder();
		for (int i = paramInt2; i < paramInt2 + paramInt3; ++i) {
			int j = paramArrayOfByte[i];
			if (localStringBuilder.length() != 0)
				localStringBuilder.append(paramChar);
			String str = Integer.toString((j < 0) ? j + 256 : j, paramInt1).toUpperCase();
			if (str.length() == 1)
				localStringBuilder.append('0');
			localStringBuilder.append(str);
		}
		return localStringBuilder.toString();
	}
}
