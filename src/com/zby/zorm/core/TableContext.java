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
 * �����ȡ�������ݿ�����б�ṹ����ṹ�Ĺ�ϵ�������Ը��߱�ṹ������ṹ
 * 
 * @author ף����
 * 
 */
public class TableContext {

	/**
	 * ����Ϊkey������Ϣ����Ϊvalue
	 */
	public static Map<String, TableInfo> tables = new HashMap<String, TableInfo>();

	/**
	 * ͨ�����䣬��po��Class����ͱ���Ϣ�����������������ã�
	 */
	public static Map<Class, TableInfo> poClassTableMap = new HashMap<Class, TableInfo>();

	private TableContext() {
	};

	static {
		try {
			// ��ʼ����ñ����Ϣ
			Connection conn = DBManager.getConn();
			//���������ļ������ݿ���Ϣ����ȡĳ�����ݿ��ȫ����Ϣ��
			//���ݿ�����ȫ�������Ϣ�����е��ֶ������ֶε�ȫ������ֵ
			DatabaseMetaData dbmd = (DatabaseMetaData) conn.getMetaData();  

			//���ص���ResultSet�����
			ResultSet tableRs = dbmd.getTables(null, "%", "%",
					new String[] { "TABLE" });

			while (tableRs.next()) {
				String tableName = (String) tableRs.getObject("TABLE_NAME");

				TableInfo ti = new TableInfo(tableName,
						new HashMap<String, ColumnInfo>(),
						new ArrayList<ColumnInfo>());
				tables.put(tableName, ti);   //������������TableInfo������
				/*Set<String> setKey=tables.keySet();  ���Ա�
				Iterator<String> it=setKey.iterator();
				while(it.hasNext()){
					String key=it.next();
					System.out.println(key+"--->"+tables.get(key));*/
				
				// ��ѯ���е������ֶ�;���ص���ResultSet�����
				ResultSet fieldRs = dbmd.getColumns(null, "%", tableName, "%"); 
				while (fieldRs.next()) {
					ColumnInfo ci= new ColumnInfo(  //�ֶ���Ϣ�����
							fieldRs.getString("COLUMN_NAME"),  //�ֶ���
							fieldRs.getString("TYPE_NAME"), 0);  //�ֶε�������
					ti.getColums().put(fieldRs.getString("COLUMN_NAME"), ci);
				
				}
//				System.out.println(ci.getDataType()+"\t"+ci.getKeyType()+"\t"+ci.getName());

				//��ȡ���е����������ص���ResultSet�����
				ResultSet PKrs = dbmd.getPrimaryKeys(null, "%", tableName);
				while (PKrs.next()) {
					ColumnInfo ci2= ti.getColums().get(//�ֶ���Ϣ�����
							PKrs.getObject("COLUMN_NAME"));  //��ȡ����ֶ���
					ci2.setKeyType(1); // ����Ϊ��������
					ti.getPriKeys().add(ci2); //��ȡ�������
				}
//				System.out.println(ci2.getName()); 
//				for(ColumnInfo ci3:ti.getPriKeys()){
//					System.out.println(ci3.getName());
//				}
				if (ti.getPriKeys().size() > 0) { // ȡΨһ����������ʹ�á������������������Ϊ��
					ti.setOnlyPrimaryKey(ti.getPriKeys().get(0));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// ������ṹ
		updateJavaPOFile();
		
		//ͨ�����䣬����po���������
		loadPOTables();
	}
	
	/**
	 * ���ݱ�ṹ���������ö�po�������Java�� ʵ���˴ӱ�ṹ����ṹ
	 */
	public static void updateJavaPOFile() {
		Map<String, TableInfo> map = TableContext.tables;
		for (TableInfo ti : map.values()) {
			JavaFileUtils.createJavaPOFile(ti, new MySQLTypeConvertor());
		}
	}

	/**
	 * ����po���������
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
