package com.zby.zorm.bean;

import java.util.List;
import java.util.Map;

/**
 * ��װ��ṹ����Ϣ
 * @author ף����
 *
 */
public class TableInfo {

	/**
	 *���ݿ��еı���
	 */
	private String tName;
	
	/**
	 * ���е��ֶ���Ϣ
	 */
	private Map<String,ColumnInfo> colums;
	
	/**
	 * Ψһ������Ŀǰ����ֻ�ܴ����������ֻ��һ��������
	 */
	private ColumnInfo onlyPrimaryKey;
	
	/**
	 * ���������������������洢
	 */
	private List<ColumnInfo> priKeys;

	public String gettName() {
		return tName;
	}

	public void settName(String tName) {
		this.tName = tName;
	}

	public Map<String, ColumnInfo> getColums() {
		return colums;
	}

	public void setColums(Map<String, ColumnInfo> colums) {
		this.colums = colums;
	}

	public ColumnInfo getOnlyPrimaryKey() {
		return onlyPrimaryKey;
	}

	public void setOnlyPrimaryKey(ColumnInfo onlyPrimaryKey) {
		this.onlyPrimaryKey = onlyPrimaryKey;
	}

	public List<ColumnInfo> getPriKeys() {
		return priKeys;
	}

	public void setPriKeys(List<ColumnInfo> priKeys) {
		this.priKeys = priKeys;
	}

	public TableInfo(String tName, Map<String, ColumnInfo> colums,
			ColumnInfo onlyPrimaryKey) {
		super();
		this.tName = tName;
		this.colums = colums;
		this.onlyPrimaryKey = onlyPrimaryKey;
	}

	public TableInfo() {
		super();
	}

	public TableInfo(String tName, Map<String, ColumnInfo> colums,
			List<ColumnInfo> priKeys) {
		super();
		this.tName = tName;
		this.colums = colums;
		this.priKeys = priKeys;
	}
	
}
