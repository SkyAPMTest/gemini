package com.a.eye.gemini.simulator;

public class PcapHead {
	private int magic; // 标识位：32位的，这个标识位的值是16进制的 0xa1b2c3d4
	private short version_major; // 主版本号：16位， 默认值为0x2
	private short version_minor; // 副版本号：16位，默认值为0x04
	private int thiszone; // 区域时间：32位，实际上该值并未使用，因此可以将该位设置为0
	private int sigfigs; // 精确时间戳：32位，实际上该值并未使用，因此可以将该值设置为0
	private int snaplen; // 数据包最大长度：32位，该值设置所抓获的数据包的最大长度，如果所有数据包都要抓获，将该值设置为65535；例如：想获取数据包的前64字节，可将该值设置为64。
	private int linktype; // 链路层类型：32位， 数据包的链路层包头决定了链路层的类型。

	public int getMagic() {
		return magic;
	}

	public void setMagic(int magic) {
		this.magic = magic;
	}

	public short getVersion_major() {
		return version_major;
	}

	public void setVersion_major(short version_major) {
		this.version_major = version_major;
	}

	public short getVersion_minor() {
		return version_minor;
	}

	public void setVersion_minor(short version_minor) {
		this.version_minor = version_minor;
	}

	public int getThiszone() {
		return thiszone;
	}

	public void setThiszone(int thiszone) {
		this.thiszone = thiszone;
	}

	public int getSigfigs() {
		return sigfigs;
	}

	public void setSigfigs(int sigfigs) {
		this.sigfigs = sigfigs;
	}

	public int getSnaplen() {
		return snaplen;
	}

	public void setSnaplen(int snaplen) {
		this.snaplen = snaplen;
	}

	public int getLinktype() {
		return linktype;
	}

	public void setLinktype(int linktype) {
		this.linktype = linktype;
	}
}
