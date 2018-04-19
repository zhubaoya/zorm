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
 * 根据配置信息，维持连接对象的管理（增加连接池功能）
 * 
 * @author 祝宝亚
 * 
 */
public class DBManager {

	/**
	 * 配置文件类的对象
	 */
	private static Configuration con;

	/**
	 * Connection连接池的对象
	 */
	private static DBConnectionPool pool ;

	/**
	 * 静态代码块，加载配置文件
	 */
	static { // 静态代码块
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

		// 加载tableContext类对象
		System.out.println(TableContext.class);
	}

	/**
	 * 获得数据库的连接
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
	 * 创建新的Connection对象
	 * 在DBConnectionPool类的initPool方法中使用
	 * @return
	 */
	public static Connection createConn() {
		try {
			Class.forName(con.getDriver()); // 加载驱动
			return DriverManager.getConnection(con.getUrl(), con.getUser(),
					con.getPass()); // 目前直接连接，后期增加连接池管理，提高效率
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 关闭数据库的连接 即关闭传入的Statement、ResultSet、Connection对象
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
	 * 关闭数据库的连接 即关闭传入的Statement、Connection对象
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
	 * 关闭数据库的连接池
	 * 
	 * @param connection
	 */
	public static void close(Connection connection) {
			pool.close(connection);
	}

	/**
	 * 返回Configuration对象
	 * 
	 * @return
	 */
	public static Configuration getConfiguration() {
		return con;
	}
}
