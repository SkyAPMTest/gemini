package com.a.eye.gemini.simulator.resolve;

import com.a.eye.gemini.common.util.StringUtils;

public abstract class GeminiResolveHighBase<T> extends GeminiResolveBase<T> {

	public String binaryToHexString(byte[] bytes) {
		return StringUtils.BinaryToHexString(bytes);
	}
}
