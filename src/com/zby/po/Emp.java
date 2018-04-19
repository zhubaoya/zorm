package com.zby.po;

import java.sql.*;
import java.util.*;

public class Emp{

	private Integer id;
	private java.sql.Date hiredate;
	private String empName;
	private Integer age;
	private Double bonus;
	private Double salary;
	private Integer deptId;


	public Integer getId(){
		return id;
	}

	public java.sql.Date getHiredate(){
		return hiredate;
	}

	public String getEmpName(){
		return empName;
	}

	public Integer getAge(){
		return age;
	}

	public Double getBonus(){
		return bonus;
	}

	public Double getSalary(){
		return salary;
	}

	public Integer getDeptId(){
		return deptId;
	}

	public void setId(Integer id){
		this.id=id;
	}

	public void setHiredate(java.sql.Date hiredate){
		this.hiredate=hiredate;
	}

	public void setEmpName(String empName){
		this.empName=empName;
	}

	public void setAge(Integer age){
		this.age=age;
	}

	public void setBonus(Double bonus){
		this.bonus=bonus;
	}

	public void setSalary(Double salary){
		this.salary=salary;
	}

	public void setDeptId(Integer deptId){
		this.deptId=deptId;
	}

}
