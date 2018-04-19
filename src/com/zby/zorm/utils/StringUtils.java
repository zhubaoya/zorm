package com.zby.zorm.utils;

/**
 * 封装常用字符串操作
 * 
 * @author 祝宝亚
 * 
 */
public class StringUtils {

	/**
	 * 将目标字符串首字母大写
	 * 
	 * @param str
	 *            目标字符串
	 * @return 首字母变为大写的字符串
	 */
	public static String firstCharToUpperCase(String str) {
		return str.toUpperCase().substring(0, 1) + str.substring(1);
	}
}
