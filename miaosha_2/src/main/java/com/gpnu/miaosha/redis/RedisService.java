package com.gpnu.miaosha.redis;

import org.codehaus.groovy.classgen.ReturnAdder;
import org.hibernate.validator.internal.util.privilegedactions.GetResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
@Service
public class RedisService {
	@Autowired
	JedisPool jedisPool;
	/**
	 * 获取对象
	 */
	public <T> T get(KeyPrefix prefix,String key,Class<T> clazz) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String realKey = prefix.getPrefix()+key;//拼接key
			String str = jedis.get(realKey);
			T t = stringToBean(str,clazz);
			return t;
		} finally {
			returnToPool(jedis);  
		}
	}
	/**
	 * 设置对象
	 */
	public <T> Boolean set(KeyPrefix prefix,String key,T value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String str = beanToString(value);
			if(str==null||str.length()<=0)return false;
			String realKey = prefix.getPrefix()+key;//拼接key
			int seconds = prefix.expireSeconds();
			if(seconds<=0) {
				jedis.set(realKey,str);
			}else {
				jedis.setex(realKey, seconds, str);
			}
			return true;
		} finally {
			returnToPool(jedis);  
		}
	}
	/**
	 * 判断是否存在
	 */
	public boolean exists(KeyPrefix prefix,String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String realKey = prefix.getPrefix()+key;//拼接key
			return jedis.exists(realKey);
		} finally {
			returnToPool(jedis);  
		}
	}
	/**
	 * 原子自增
	 */
	public Long incr(KeyPrefix prefix,String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String realKey = prefix.getPrefix()+key;//拼接key
			return jedis.incr(realKey);
		} finally {
			returnToPool(jedis);  
		}
	}
	/**
	 * 原子自减
	 */
	public Long decr(KeyPrefix prefix,String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String realKey = prefix.getPrefix()+key;//拼接key
			return jedis.decr(realKey);
		} finally {
			returnToPool(jedis);  
		}
	}
	private <T> String beanToString(T value) {
		if(value ==null) {
			return null;
		}
		Class<?> clazz = value.getClass();
		if(clazz == int.class || clazz == Integer.class) {
			return ""+value;
		}else if(clazz == String.class) {
			return (String)value;
		}else {
			return JSON.toJSONString(value);
		}

	}
	private <T> T stringToBean(String str,Class<T> clazz) {
		if(str == null || str.length()<=0 || clazz == null) {
			return null;
		}
		if(clazz == int.class || clazz == Integer.class) {
			return (T) Integer.valueOf(str);
		}else if(clazz == String.class) {
			return (T)str;
		}else {
			return JSON.toJavaObject(JSON.parseObject(str), clazz);
		}
	}
	private void returnToPool(Jedis jedis) {//用完的jedis回到jedispool
		if(jedis!=null) {
			jedis.close();
		}
	}

}
