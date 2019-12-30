package com.gpnu.miaosha.redis;

public abstract class BasePrefix implements KeyPrefix {
	private int expireSeconds;
	private String prefix;
	
	public BasePrefix(String prefix) {//如果不输入过期时间默认为0
		this(0, prefix);
	}
	public BasePrefix(int expireSeconds, String prefix) {
		this.expireSeconds = expireSeconds;
		this.prefix = prefix;
	}

	public int expireSeconds() {//0代表永不过期
		return expireSeconds;
	}

	public String getPrefix() {
		String className = getClass().getSimpleName();
		return className+":"+prefix;
	}

}
