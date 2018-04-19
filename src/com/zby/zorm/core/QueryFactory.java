package com.zby.zorm.core;

/**
 * 采用工厂设计模式，根据配置文件，创建query对象
 *  利用单例模式、反射和克隆模式,提高创建query对象的效率
 * 
 * @author 祝宝亚
 * 
 */
public class QueryFactory {
	private static Query protoTypeQuery; // 原型对象

	private QueryFactory() {
	} // 私有构造器

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
			return (Query) protoTypeQuery.clone(); // 返回的是query对象
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
}
