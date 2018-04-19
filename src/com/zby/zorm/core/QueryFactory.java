package com.zby.zorm.core;

/**
 * ���ù������ģʽ�����������ļ�������query����
 *  ���õ���ģʽ������Ϳ�¡ģʽ,��ߴ���query�����Ч��
 * 
 * @author ף����
 * 
 */
public class QueryFactory {
	private static Query protoTypeQuery; // ԭ�Ͷ���

	private QueryFactory() {
	} // ˽�й�����

	static {
		try {
			Class clazz = Class.forName(DBManager.getConfiguration()
					.getQueryClass());
			protoTypeQuery = (Query) clazz.newInstance();
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public static Query creteQuery() {
		try {
			return (Query) protoTypeQuery.clone(); // ���ص���query����
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
}
