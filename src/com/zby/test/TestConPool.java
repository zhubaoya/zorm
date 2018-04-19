package com.zby.test;

import java.beans.FeatureDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import com.zby.empVO.EMPVO;
import com.zby.zorm.core.MySQLQuery;
import com.zby.zorm.core.QueryFactory;
import com.zby.zorm.core.TableContext;

public class TestConPool {

	public static void test() {
		MySQLQuery query = (MySQLQuery) QueryFactory.creteQuery();
		String sql = "SELECT e.id,e.empName,e.age,e.hiredate,e.salary+e.bonus as wages, d.dname,d.address FROM emp e  JOIN dept d ON e.deptId = d.id;";
		List<EMPVO> list = query.queryRows(sql, EMPVO.class, null);
		Iterator<EMPVO> it = list.iterator();
		while (it.hasNext()) {
			EMPVO emp = it.next();
			System.out.println(emp.getId() + "\t" + emp.getEmpName() + "\t"
					+ emp.getAge() + "\t" + emp.getAddress());
		}

	}
	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		for (int i = 0; i < 10000; i++) {
			test();
		}
		long end = System.currentTimeMillis();
		System.out.print("查询一万次所需要的时间：" + (double)((end-start ) / 1000) + "秒"); 
		//在不添加连接池的情况下，进行数据库查询10000次所需要的时间:25秒
		//在使用连接池的情况下，进行数据库查询10000次所需要的时间:5秒
	}

	public static void testReflect() {
		Class c = TableContext.class;
		System.out.println(c);
		try {
			Class c1 = Class.forName("com.zby.zorm.core.TableContext");
			Constructor[] con = c1.getDeclaredConstructors();
			for (Constructor<TableContext> cons : con) {
				System.out.println(cons);
			}

			Method[] m = c1.getDeclaredMethods();
			for (Method ms : m) {
				System.out.println(ms.getModifiers());
			}

			Field[] fields = c1.getDeclaredFields();
			for (Field f : fields) {
				System.out.println(f.getType());
			}

			System.out.println(c1.getDeclaredField("tables"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
