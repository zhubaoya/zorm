package com.zby.zorm.core;

/**
 * ����Java����ģ�ͺ����ݿ���������ͻ���ת��
 * @author ף����
 *
 */
public interface TypeConvertor {
	
	/**
	 * �����ݿ������ת��ΪJava����������
	 * @param columnType  ���ݿ��ֶε���������
	 * @return Java����������
	 */
	public String dbTypeConvertJavaType(String columnType);
	
	/**
	 * ��Java����������ת��Ϊ���ݿ����������
	 * @param javaDataType  Java��������
	 * @return  ���ݿ����������
	 */
	public String javaTypeConvertdbType(String javaDataType);

}
