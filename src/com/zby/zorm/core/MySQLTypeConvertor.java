package com.zby.zorm.core;

public class MySQLTypeConvertor implements TypeConvertor {

	/**
	 * �����ݿ������ת��ΪJava����������<br />
	 * ����varchar --�� String
	 * 
	 * @param columnType
	 *            ���ݿ��ֶε���������
	 * @return Java����������
	 */
	@Override
	public String dbTypeConvertJavaType(String columnType) {
		if ("varchar".equalsIgnoreCase(columnType)
				|| "char".equalsIgnoreCase(columnType)) {
			return "String";
		} else if ("int".equalsIgnoreCase(columnType)
				|| "tinyint".equalsIgnoreCase(columnType)
				|| "smallint".equalsIgnoreCase(columnType)
				|| "integer".equalsIgnoreCase(columnType)) {
			return "Integer";
		} else if ("bigint".equalsIgnoreCase(columnType)) {
			return "Long";
		} else if ("double".equalsIgnoreCase(columnType)) {
			return "Double";
		} else if ("float".equalsIgnoreCase(columnType)) {
			return "Float";
		} else if ("clob".equalsIgnoreCase(columnType)) {
			return "java.sql.Clob";
		}else if ("blob".equalsIgnoreCase(columnType)) {
			return "java.sql.Blob";
		}else if ("date".equalsIgnoreCase(columnType)) {
			return "java.sql.Date";
		}else if ("time".equalsIgnoreCase(columnType)) {
			return "java.sql.Time";
		}else if ("timestamp".equalsIgnoreCase(columnType)) {
			return "java.sql.Timestamp";
		}
		return null;
	}

	@Override
	public String javaTypeConvertdbType(String javaDataType) {

		return null;
	}

}
