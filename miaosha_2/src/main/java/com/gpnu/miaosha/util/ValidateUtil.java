package com.gpnu.miaosha.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.druid.util.StringUtils;

public class ValidateUtil {
	private static final Pattern mobile_pattern = Pattern.compile("1\\d{10}");
	public static boolean isMobile(String src) {
			Matcher m = mobile_pattern.matcher(src);
			return m.matches();
	}
}
