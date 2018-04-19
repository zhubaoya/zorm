package com.zby.zorm.core;

/**
 * 负责将Java数据模型和数据库的数据类型互相转换
 * @author 祝宝亚
 *
 */
public interface TypeConvertor {
	
	/**
	 * 将数据库的类型转化为Java的数据类型
	 * @param columnType  数据库字段的数据类型
	 * @return Java的数据类型
	 */
	public String dbTypeConvertJavaType(String columnType);
	
	/**
	 * 将Java的数据类型转化为数据库的数据类型
	 * @param javaDataType  Java数据类型
	 * @return  数据库的数据类型
	 */
	public String javaTypeConvertdbType(String javaDataType);

}
