package com.zby.zorm.utils;

import java.sql.SQLException;

import com.mysql.jdbc.PreparedStatement;

/**
 * <p>
 * 封装常用的JDBC操作
 * </p>
 * &nbsp;&nbsp;&nbsp;&nbsp;连接数据库</br> &nbsp;&nbsp;&nbsp;&nbsp;关闭数据库的连接</br>
 * &nbsp;&nbsp;&nbsp;&nbsp;......</br>
 * 
 * @author 祝宝亚
 * 
 */
public class JDBCUtils {

	/**
	 * 给SQL设参
	 * 
	 * @param ps
	 *            预编译SQL语句对象
	 * @param params
	 */
	public static void handelParams(PreparedStatement ps, Object[] params) {
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				try {
					ps.setObject(1 + i, params[i]);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
