package com.zby.zorm.core;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.mysql.jdbc.DatabaseMetaData;
import com.zby.zorm.bean.ColumnInfo;
import com.zby.zorm.bean.TableInfo;
import com.zby.zorm.utils.JavaFileUtils;
import com.zby.zorm.utils.StringUtils;

/**
 * 负责获取管理数据库的所有表结构和类结构的关系，并可以更具表结构生成类结构
 * 
 * @author 祝宝亚
 * 
 */
public class TableContext {

	/**
	 * 表名为key，表信息对象为value
	 */
	public static Map<String, TableInfo> tables = new HashMap<String, TableInfo>();

	/**
	 * 通过反射，将po的Class对象和表信息关联起来，便于重用！
	 */
	public static Map<Class, TableInfo> poClassTableMap = new HashMap<Class, TableInfo>();

	private TableContext() {
	};

	static {
		try {
			// 初始化获得表的信息
			Connection conn = DBManager.getConn();
			//根据配置文件的数据库信息，获取某张数据库的全部信息：
			//数据库名、全部表的信息、表中的字段名、字段的全部属性值
			DatabaseMetaData dbmd = (DatabaseMetaData) conn.getMetaData();  

			//返回的是ResultSet结果集
			ResultSet tableRs = dbmd.getTables(null, "%", "%",
					new String[] { "TABLE" });

			while (tableRs.next()) {
				String tableName = (String) tableRs.getObject("TABLE_NAME");

				TableInfo ti = new TableInfo(tableName,
						new HashMap<String, ColumnInfo>(),
						new ArrayList<ColumnInfo>());
				tables.put(tableName, ti);   //将表名放置在TableInfo的类中
				/*Set<String> setKey=tables.keySet();  测试表
				Iterator<String> it=setKey.iterator();
				while(it.hasNext()){
					String key=it.next();
					System.out.println(key+"--->"+tables.get(key));*/
				
				// 查询表中的所有字段;返回的是ResultSet结果集
				ResultSet fieldRs = dbmd.getColumns(null, "%", tableName, "%"); 
				while (fieldRs.next()) {
					ColumnInfo ci= new ColumnInfo(  //字段信息类对象
							fieldRs.getString("COLUMN_NAME"),  //字段名
							fieldRs.getString("TYPE_NAME"), 0);  //字段的类型名
					ti.getColums().put(fieldRs.getString("COLUMN_NAME"), ci);
				
				}
//				System.out.println(ci.getDataType()+"\t"+ci.getKeyType()+"\t"+ci.getName());

				//获取表中的主键；返回的是ResultSet结果集
				ResultSet PKrs = dbmd.getPrimaryKeys(null, "%", tableName);
				while (PKrs.next()) {
					ColumnInfo ci2= ti.getColums().get(//字段信息类对象
							PKrs.getObject("COLUMN_NAME"));  //获取表的字段名
					ci2.setKeyType(1); // 设置为主键类型
					ti.getPriKeys().add(ci2); //获取多个主键
				}
//				System.out.println(ci2.getName()); 
//				for(ColumnInfo ci3:ti.getPriKeys()){
//					System.out.println(ci3.getName());
//				}
				if (ti.getPriKeys().size() > 0) { // 取唯一主键，方便使用。如果是联合主键，则为空
					ti.setOnlyPrimaryKey(ti.getPriKeys().get(0));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 更新类结构
		updateJavaPOFile();
		
		//通过反射，加载po包下面的类
		loadPOTables();
	}
	
	/**
	 * 根据表结构，更新配置额po包下面的Java类 实现了从表结构到类结构
	 */
	public static void updateJavaPOFile() {
		Map<String, TableInfo> map = TableContext.tables;
		for (TableInfo ti : map.values()) {
			JavaFileUtils.createJavaPOFile(ti, new MySQLTypeConvertor());
		}
	}

	/**
	 * 加载po包下面的类
	 */
	public static void loadPOTables() {
		for (TableInfo tbs : tables.values()) {
			try {
				Class clazz = Class.forName(DBManager.getConfiguration()
						.getPoPackage()
						+ "."
						+ StringUtils.firstCharToUpperCase(tbs.gettName()));
				poClassTableMap.put(clazz, tbs);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}
