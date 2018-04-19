package com.zby.zorm.bean;

import java.util.List;
import java.util.Map;

/**
 * 封装表结构的信息
 * @author 祝宝亚
 *
 */
public class TableInfo {

	/**
	 *数据库中的表名
	 */
	private String tName;
	
	/**
	 * 所有的字段信息
	 */
	private Map<String,ColumnInfo> colums;
	
	/**
	 * 唯一主键（目前我们只能处理表中有且只有一个主键）
	 */
	private ColumnInfo onlyPrimaryKey;
	
	/**
	 * 如果联合主键，则在这里存储
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
