package com.a.eye.gemini.simulator.resolve;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import com.a.eye.gemini.simulator.bit.BitInputStream;
import com.a.eye.gemini.simulator.resolve.Attribute.DataType;
import com.a.eye.gemini.simulator.util.FormatUtils;

public abstract class GeminiResolveBase<T> extends ResolveBase<T> {

	protected BitInputStream bitInputStream = new BitInputStream();

	@Override
	public void resolve(InputStream in, long frameId) throws IOException {
		System.out.println("######################" + this.getClass().getSimpleName() + "#######################");
		Collections.sort(attrList);

		for (Attribute<T> attr : attrList) {
			byte[] byteread = null;

			if (DataType.Data.equals(attr.getDataType())) {
				ResolverRegister.getResolve(getDataResolve()).resolve(in, frameId);
				continue;
			}
			if (DataType.Custom.equals(attr.getDataType())) {
				custom(attr.getName(), in);
				continue;
			}
			if (DataType.Frame.equals(attr.getDataType())) {
				setValue(attr.getName(), frameId);
				System.out.println(attr.getName() + " : " + frameId);
				continue;
			}
			if (attr.isSkipAfterFirstTime() && frameId != 1) {
				continue;
			}

			byteread = bitInputStream.readBit(in, attr.getLength());

			if (DataType.String.equals(attr.getDataType())) {
				setValue(attr.getName(), new String(byteread));
				System.out.println(attr.getName() + " : " + new String(byteread) + ", length=" + attr.getLength() + "bit");
			} else {
				String strread = binaryToHexString(byteread);
				if (DataType.Integer.equals(attr.getDataType())) {
					setValue(attr.getName(), Integer.parseInt(strread, 16));
					System.out.println(attr.getName() + " : " + Integer.parseInt(strread, 16) + ", length=" + attr.getLength() + "bit, hex=" + strread);
				} else if (DataType.Long.equals(attr.getDataType())) {
					setValue(attr.getName(), Long.parseLong(strread, 16));
					System.out.println(attr.getName() + " : " + Long.parseLong(strread, 16) + ", length=" + attr.getLength() + "bit, hex=" + strread);
				} else if (DataType.Mac.equals(attr.getDataType())) {
					setValue(attr.getName(), FormatUtils.mac(byteread));
					System.out.println(attr.getName() + " : " + FormatUtils.mac(byteread) + ", length=" + attr.getLength() + "bit, hex=" + strread);
				} else if (DataType.IP.equals(attr.getDataType())) {
					setValue(attr.getName(), FormatUtils.ip(byteread));
					System.out.println(attr.getName() + " : " + FormatUtils.ip(byteread) + ", length=" + attr.getLength() + "bit, hex=" + strread);
				} else if (DataType.Hex.equals(attr.getDataType())) {
					setValue(attr.getName(), byteread);
					System.out.println(attr.getName() + " : " + strread + ", length=" + attr.getLength() + "bit, hex=" + strread);
				} else {
					System.out.println(attr.getName() + " : " + strread + ", length=" + attr.getLength() + "bit, hex=" + strread);
				}
			}
		}
	}

	public abstract String binaryToHexString(byte[] bytes);

	public abstract void custom(T name, InputStream in) throws IOException;

	public void addAttr(Attribute<T> attr) {
		attrList.add(attr);
	}

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
