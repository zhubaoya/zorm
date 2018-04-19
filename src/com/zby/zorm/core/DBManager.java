package com.zby.zorm.core;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.mysql.jdbc.Statement;
import com.zby.pool.DBConnectionPool;
import com.zby.zorm.bean.Configuration;

/**
 * ����������Ϣ��ά�����Ӷ���Ĺ����������ӳع��ܣ�
 * 
 * @author ף����
 * 
 */
public class DBManager {

	/**
	 * �����ļ���Ķ���
	 */
	private static Configuration con;

	/**
	 * Connection���ӳصĶ���
	 */
	private static DBConnectionPool pool ;

	/**
	 * ��̬����飬���������ļ�
	 */
	static { // ��̬�����
		Properties pro = new Properties();
		try {
			pro.load(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("db.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		con = new Configuration();

		con.setDriver(pro.getProperty("driver"));
		con.setUrl(pro.getProperty("url"));
		con.setUser(pro.getProperty("user"));
		con.setPass(pro.getProperty("pass"));
		con.setUsingDB(pro.getProperty("usingDB"));
		con.setPoPackage(pro.getProperty("poPackage"));
		con.setSrcPath(pro.getProperty("srcPath"));
		con.setQueryClass(pro.getProperty("queryClass"));
		con.setPoolMinSize(Integer.valueOf((String) pro.get("poolMinSize")));
		con.setPoolMaxSize(Integer.valueOf((String) pro.get("poolMaxSize")));

		// ����tableContext�����
		System.out.println(TableContext.class);
	}

	/**
	 * ������ݿ������
	 * 
	 * @return
	 */
	public static Connection getConn() {
		if(pool==null){
			pool=new DBConnectionPool();
		}
		return pool.getConnection();
	}

	/**
	 * �����µ�Connection����
	 * ��DBConnectionPool���initPool������ʹ��
	 * @return
	 */
	public static Connection createConn() {
		try {
			Class.forName(con.getDriver()); // ��������
			return DriverManager.getConnection(con.getUrl(), con.getUser(),
					con.getPass()); // Ŀǰֱ�����ӣ������������ӳع������Ч��
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * �ر����ݿ������ ���رմ����Statement��ResultSet��Connection����
	 * 
	 * @param statement
	 * @param resultSet
	 * @param connection
	 */
	public static void close(Statement statement, ResultSet resultSet,
			Connection connection) {

		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
			pool.close(connection);
	}

	/**
	 * �ر����ݿ������ ���رմ����Statement��Connection����
	 * 
	 * @param statement
	 * @param connection
	 */
	public static void close(Statement statement, Connection connection) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
			pool.close(connection);
	}

	/**
	 * �ر����ݿ�����ӳ�
	 * 
	 * @param connection
	 */
	public static void close(Connection connection) {
			pool.close(connection);
	}

	/**
	 * ����Configuration����
	 * 
	 * @return
	 */
	public static Configuration getConfiguration() {
		return con;
	}
}
