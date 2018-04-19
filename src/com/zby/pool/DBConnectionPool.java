package com.zby.pool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.zby.zorm.core.DBManager;

/**
 * 创建连接数据库的连接池
 * 
 * @author 祝宝亚
 * 
 */
public class DBConnectionPool {

	/**
	 * 连接池对象
	 */
	private List<Connection> pool;

	/**
	 * 最大连接池
	 */
	private static final int POOL_MAX_SIZE = DBManager.getConfiguration().getPoolMaxSize();

	/**
	 * 最小连接池
	 */
	private static final int POOL_MIN_SIZE = DBManager.getConfiguration().getPoolMinSize();

	/**
	 * 初始化连接池，使池中的连接数达到最小值
	 * 当连接池的数目小于连接池的最小值时
	 * 那么就往池（list）中添加连接对象
	 * 直到连接池的数目大于连接池的最小值 </br>
	 * 
	 * createConn()创建新的Connection对象
	 * 调用DBManager.createConn()向池中添加Connection对象
	 * 池中有多个Connection对象(连接数据库的对象)</br>
	 * 
	 * 对于while语句，当添加为真时，
	 * 执行while的body内容,直到条件为假
	 */
	public void initPool() {
		if (pool == null) {
			pool = new ArrayList<Connection>();
		}
		while (pool.size() < this.POOL_MIN_SIZE) {
			pool.add(DBManager.createConn());   
		}
	}

	/**
	 * 从连接池中取出一个连接,往往是获得连接池中最后一个Connection对象</br>
	 * 需要添加synchronized，保证线程安全，防止多个人取到同一个对象
	 * 
	 * @return coon Connection对象
	 */
	public synchronized Connection getConnection() {
		int last_index = pool.size() - 1;
		Connection coon = pool.get(last_index);
		pool.remove(last_index);
		return coon;

	}

	/**
	 * 关闭连接池，其实不是真正意义上的关闭 
	 * 而是将连接放回到连接池中，防止资源的浪费</br>
	 * 如果当连接池的数值大于等于连接池的最大数
	 * 那么就是真正意义上关闭connection对象
	 * 
	 * @param conn
	 */
	public synchronized void close(Connection conn) {
		if (pool.size() >= this.POOL_MAX_SIZE) {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		pool.add(conn);
	}

	public DBConnectionPool() {
		initPool();
	}
	
	public static void main(String[] args) {
		System.out.print(new DBConnectionPool().POOL_MAX_SIZE);
	}
}
