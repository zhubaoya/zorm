package com.zby.pool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.zby.zorm.core.DBManager;

/**
 * �����������ݿ�����ӳ�
 * 
 * @author ף����
 * 
 */
public class DBConnectionPool {

	/**
	 * ���ӳض���
	 */
	private List<Connection> pool;

	/**
	 * ������ӳ�
	 */
	private static final int POOL_MAX_SIZE = DBManager.getConfiguration().getPoolMaxSize();

	/**
	 * ��С���ӳ�
	 */
	private static final int POOL_MIN_SIZE = DBManager.getConfiguration().getPoolMinSize();

	/**
	 * ��ʼ�����ӳأ�ʹ���е��������ﵽ��Сֵ
	 * �����ӳص���ĿС�����ӳص���Сֵʱ
	 * ��ô�����أ�list����������Ӷ���
	 * ֱ�����ӳص���Ŀ�������ӳص���Сֵ </br>
	 * 
	 * createConn()�����µ�Connection����
	 * ����DBManager.createConn()��������Connection����
	 * �����ж��Connection����(�������ݿ�Ķ���)</br>
	 * 
	 * ����while��䣬�����Ϊ��ʱ��
	 * ִ��while��body����,ֱ������Ϊ��
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
	 * �����ӳ���ȡ��һ������,�����ǻ�����ӳ������һ��Connection����</br>
	 * ��Ҫ���synchronized����֤�̰߳�ȫ����ֹ�����ȡ��ͬһ������
	 * 
	 * @return coon Connection����
	 */
	public synchronized Connection getConnection() {
		int last_index = pool.size() - 1;
		Connection coon = pool.get(last_index);
		pool.remove(last_index);
		return coon;

	}

	/**
	 * �ر����ӳأ���ʵ�������������ϵĹر� 
	 * ���ǽ����ӷŻص����ӳ��У���ֹ��Դ���˷�</br>
	 * ��������ӳص���ֵ���ڵ������ӳص������
	 * ��ô�������������Ϲر�connection����
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
