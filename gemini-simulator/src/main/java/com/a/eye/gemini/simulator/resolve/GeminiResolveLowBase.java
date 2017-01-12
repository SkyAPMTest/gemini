package com.a.eye.gemini.simulator.resolve;

import com.a.eye.gemini.common.util.StringUtils;

public abstract class GeminiResolveLowBase extends GeminiResolveBase {

	public String binaryToHexString(byte[] bytes) {
		return StringUtils.bytesToHexString(bytes);
	}
}
