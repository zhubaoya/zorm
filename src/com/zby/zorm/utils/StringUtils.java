package com.zby.zorm.utils;

/**
 * ��װ�����ַ�������
 * 
 * @author ף����
 * 
 */
public class StringUtils {

	/**
	 * ��Ŀ���ַ�������ĸ��д
	 * 
	 * @param str
	 *            Ŀ���ַ���
	 * @return ����ĸ��Ϊ��д���ַ���
	 */
	public static String firstCharToUpperCase(String str) {
		return str.toUpperCase().substring(0, 1) + str.substring(1);
	}
}
