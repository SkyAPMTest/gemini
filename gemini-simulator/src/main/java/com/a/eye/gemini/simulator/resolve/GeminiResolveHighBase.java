package com.a.eye.gemini.simulator.resolve;

import com.a.eye.gemini.common.util.StringUtils;

public abstract class GeminiResolveHighBase extends GeminiResolveBase {

	public String binaryToHexString(byte[] bytes) {
		return StringUtils.BinaryToHexString(bytes);
	}
}
