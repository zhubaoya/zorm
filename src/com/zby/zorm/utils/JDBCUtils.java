package com.zby.zorm.utils;

import java.sql.SQLException;

import com.mysql.jdbc.PreparedStatement;

/**
 * <p>
 * ��װ���õ�JDBC����
 * </p>
 * &nbsp;&nbsp;&nbsp;&nbsp;�������ݿ�</br> &nbsp;&nbsp;&nbsp;&nbsp;�ر����ݿ������</br>
 * &nbsp;&nbsp;&nbsp;&nbsp;......</br>
 * 
 * @author ף����
 * 
 */
public class JDBCUtils {

	/**
	 * ��SQL���
	 * 
	 * @param ps
	 *            Ԥ����SQL������
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
