package com.zby.test;

import java.sql.Date;
import java.util.List;

import com.zby.empVO.EMPVO;
import com.zby.po.Emp;
import com.zby.zorm.core.MySQLQuery;
import com.zby.zorm.core.QueryFactory;

public class TestQuery {

	public static void testDML() {
		Emp emp = new Emp();
		emp.setId(4);
		emp.setEmpName("∞≤»ÙÍÿ");
		emp.setAge(19);
		emp.setHiredate(new Date(System.currentTimeMillis()));
		emp.setSalary(1200.00);
		emp.setBonus((double) 200);
		emp.setDeptId(2);
		
		MySQLQuery query = (MySQLQuery) QueryFactory.creteQuery();
//		query.insert(emp);
	
		query.update(emp, new String[] { "bonus", "deptId" });
	}

	public static void testSelect() {
		MySQLQuery query = (MySQLQuery) QueryFactory.creteQuery();
		String sql = "SELECT e.id,e.empName,e.age,e.hiredate,e.salary+e.bonus as wages, d.dname,d.address FROM emp e  JOIN dept d ON e.deptId = d.id;";
		// List<Emp> list=new
		List<Emp> list = query.queryRows(
				"select id,empName,age from emp where id>? and age>?",
				Emp.class, new Object[] { 1, 17 });
		// List<EMPVO> list = new MySQLQuery().queryRows(sql, EMPVO.class,
		// null);
		for (Emp e : list) {
			System.out.print(e.getId() + "\t" + e.getEmpName() + "\t"
					+ e.getAge() + "\n");
		}

		// EMPVO e=(EMPVO) new MySQLQuery().queryUniqueRow(sql, EMPVO.class,
		// null);
		// System.out.print(e.getId() + "\t" + e.getEmpName() + "\t"
		// + e.getAge() + "\t" + e.getDname() + "\n");

		Object obj = query.queryValue(
				"select count(*) from emp where salary>?",
				new Object[] { 1100 });
		System.out.println(obj);
	}

	public static void main(String[] args) {
		//testDML();
		System.out.println(new MySQLQuery().queryById(Emp.class,2));
	}
}
