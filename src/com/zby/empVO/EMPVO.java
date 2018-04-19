package com.zby.empVO;

import java.sql.Date;

/**
 * ���ж���ѯ���ֵ���󣬱��磺</br> &nbsp; &nbsp; SELECT</br> &nbsp; &nbsp;&nbsp;
 * &nbsp;e.id,e.empName,e.hiredate,e.salary+e.bonus as wages,
 * d.address,d.dname</br> &nbsp; &nbsp;FROM</br> &nbsp; &nbsp;&nbsp; &nbsp;emp e
 * LEFT JOIN dept d ON e.deptId = d.id;</br></br>
 * ��ѯ���Ϊ: &nbsp;&nbsp;
 * <table>
 * <tr>
 * <td>1</td>
 * <td>ף����</td>
 * <td>1997-07-26</td>
 * <td>1400</td>
 * <td>���ڽ�</td>
 * <td>����</td>
 * </tr>
 * </table>
 * 
 * @author ף����
 * 
 */
public class EMPVO {

	private Integer id;
	private Integer age;
	private String empName;
	private Date hiredate;
	private Double wages;
	private String Dname;
	private String address;

	public EMPVO() {
		super();
	}

	public EMPVO(Integer id, String empName, Date hiredate, Double wages,
			String deptName, String address) {
		super();
		this.id = id;
		this.empName = empName;
		this.hiredate = hiredate;
		this.wages = wages;
		this.Dname = deptName;
		this.address = address;
	}
	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public Date getHiredate() {
		return hiredate;
	}

	public void setHiredate(Date hiredate) {
		this.hiredate = hiredate;
	}

	public Double getWages() {
		return wages;
	}

	public void setWages(Double wages) {
		this.wages = wages;
	}

	public String getDname() {
		return Dname;
	}

	public void setDname(String dname) {
		Dname = dname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
