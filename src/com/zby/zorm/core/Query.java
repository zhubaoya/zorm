package com.zby.zorm.core;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.PreparedStatement;
import com.zby.zorm.bean.ColumnInfo;
import com.zby.zorm.bean.TableInfo;
import com.zby.zorm.utils.JDBCUtils;
import com.zby.zorm.utils.ReflectUtils;

/**
 * query�ĳ����࣬�����ѯ�������ṩ����ĺ����ࣩ
 * 
 * @author ף����
 * 
 */
@SuppressWarnings(value = "all")
public abstract class Query implements Cloneable{

	/**
	 * ��ѯ����ģ�巽������Ϊ��ͬ�Ĳ�ѯ���в�ͬ��ʵ��Ч�������磺</br>
	 * &nbsp; &nbsp; &nbsp; &nbsp;queryValue(sql,)��������ѯĳ��ֵ</br>
	 * &nbsp; &nbsp; &nbsp; &nbsp;queryRows(sql,)������  ��ѯSQL�Ľ����</br>
	 * 
	 * ��Щͨ��Callback�ӿڣ�ͨ������connection��PreparedStatement��ResultSet������ʵ�֣�
	 * ��Щ������excuteQueryTemplate�������Ѿ�����������ִ�в�ѯʱ����Ӧ���������ص�����
	 * 
	 * @param sql  SQL���
	 * @param params  SQL����
	 * @param clazz  //��¼Ҫ��װ��Java��
	 * @param cb �ص��Ľӿ�
	 * @return
	 */
	public Object excuteQueryTemplate(String sql, Object[] params, Class clazz,
			Callback cb) {
		Connection conn = DBManager.getConn();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = (PreparedStatement) conn.prepareStatement(sql);
			JDBCUtils.handelParams(ps, params);
			rs = ps.executeQuery();
			System.out.println(ps);
			return cb.doExecute(conn, ps, rs); // �����object��ֵ���ظ�excuteQueryTemplate()����
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			DBManager.close(ps, rs, conn);
		}
	}

	/**
	 * ִ��SQL���ݣ�Ҳ�������ݲ��ݲ� DML��Data manipulation layer
	 * 
	 * @param sql
	 *            sql���
	 * @param params
	 *            ����
	 * @return ִ��SQL����Ӱ�������
	 */
	public int executeDML(String sql, Object[] params) {
		Connection connection = DBManager.getConn();
		int count = 0;

		PreparedStatement ps = null;
		try {
			ps = (PreparedStatement) connection.prepareStatement(sql);
			// ��SQL���
			JDBCUtils.handelParams(ps, params);
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBManager.close(ps, connection);
		}
		return count;
	}

	/**
	 * ��һ������洢�������� �Ѷ����в�Ϊnull�����������ݿ��д洢���������Ϊnull��Ϊ0</br>
	 * obj���Ǵ����ݿ��е�ĳ�ű���ӳ���������</br> �������������ݿ���е��ֶ���
	 * 
	 * @param obj
	 */
	public void insert(Object obj) {
		List<Object> params = new ArrayList<Object>(); // ����洢SQL�Ĳ�������

		Class clazz = obj.getClass();  //ͨ��������ص�ǰ�������
		TableInfo tableInfo = TableContext.poClassTableMap.get(clazz);

		StringBuilder sql = new StringBuilder("insert into "
				+ tableInfo.gettName() + " (");

		int countNotNullField = 0; // ���㲻Ϊnull������ֵ

		Field[] fs = clazz.getDeclaredFields();  //ͨ�������ȡ��ǰ���������
		for (Field f : fs) {
			String FieldName = f.getName();  //��ȡ��ǰ�������Ե����Ե�����
			Object fieldValue = ReflectUtils.InvokeGet(FieldName, obj);
			if (fieldValue != null) {
				countNotNullField++;
				sql.append(FieldName + ",");
				params.add(fieldValue);
			}
		}

		sql.setCharAt(sql.length() - 1, ')');
		sql.append(" values (");
		for (int i = 0; i < countNotNullField; i++) {
			sql.append("?,");
		}
		sql.setCharAt(sql.length() - 1, ')');

		executeDML(sql.toString(), params.toArray());
	}

	/**
	 * <p>
	 * ɾ����clazz��ʾ���Ӧ�ı��м�¼��ָ������ֵid�ļ�¼��
	 * </p>
	 * <p>
	 * id��ʾ����:</br>
	 * <p>
	 * &nbsp;&nbsp;����delete from emp where id=1
	 * </p>
	 * </p>
	 * 
	 * @param clazz
	 *            �����Ӧ���class����
	 * @param obj ɾ��ĳ�еĲ���ֵ 
	 * @return
	 */
	public void delete(Class clazz, Object obj) {
		// ͨ�����伯Class������TableInfo
		TableInfo info = TableContext.poClassTableMap.get(clazz);
		// �������
		ColumnInfo onlyPK = info.getOnlyPrimaryKey();
		String sql = "delete from " + info.gettName() + " where "
				+ onlyPK.getName() + "=?;";
		executeDML(sql, new Object[] { obj });
	}

	/**
	 * ɾ�����������ݿ��ж�Ӧ�ļ�¼���������ڵ����Ӧ���������������Ӧ����¼��
	 * 
	 * @param obj
	 * @return
	 */
	public void delete(Object obj) {
		Class clazz = obj.getClass();
		TableInfo tableInfo = TableContext.poClassTableMap.get(clazz);
		ColumnInfo onlyPK = tableInfo.getOnlyPrimaryKey();

		// ͨ��������ƣ���ȡ���Ե�set��get����
		Object priKeyValue = ReflectUtils.InvokeGet(onlyPK.getName(), obj);

		delete(clazz, priKeyValue);
	}

	/**
	 * ���¶����Ӧ�ļ�¼������ֻ����ָ�����ֶε�ֵ</br> &nbsp;&nbsp;&nbsp;&nbsp;����update user set
	 * uname=? pwd=?
	 * 
	 * @param obj
	 *            �����µĶ���
	 * @param fieldNames
	 *            ���µ������б�
	 * @return ִ��SQL����Ӱ�������
	 */
	public int update(Object obj, String[] fieldNames) {
		List<Object> params = new ArrayList<Object>(); // ����洢SQL�Ĳ�������

		Class clazz = obj.getClass();
		TableInfo tableInfo = TableContext.poClassTableMap.get(clazz);
		ColumnInfo onlyPk = tableInfo.getOnlyPrimaryKey(); // ���Ψһ����

		StringBuilder sql = new StringBuilder("update " + tableInfo.gettName()
				+ " set ");

		for (String fieldName : fieldNames) {
			Object fieldValue = ReflectUtils.InvokeGet(fieldName, obj);
			params.add(fieldValue);
			sql.append(fieldName + "=?,");
		}
		sql.setCharAt(sql.length() - 1, ' ');
		sql.append(" where ");
		sql.append(onlyPk.getName() + "=? ");
		params.add(ReflectUtils.InvokeGet(onlyPk.getName(), obj)); // ������ֵ
		return executeDML(sql.toString(), params.toArray());
	}

	/**
	 * ��ѯ���ض��м�¼������ÿ�м�¼��װ��clazzָ�����������</br>
	 * ���������ģ�巽��excuteQueryTemplate(),�ڷ���excuteQueryTemplate()
	 * ��ʵ�ֽӿ�Callback��Ҳ���Ƿ����ڲ��࣬������Android�����еļ�����
	 * 
	 * @param sql
	 *            ִ�е�SQL���
	 * @param clazz
	 *            ��װ���ݵ�Javabean���Class����
	 * @param params
	 *            SQL����
	 * @return ��ѯ���Ľ����װ��list��
	 */
	public List queryRows(String sql, final Class clazz, Object[] params) {

		//��������ظ�queryRows����
		return (List) excuteQueryTemplate(sql, params, clazz, new Callback() {

			@Override
			public Object doExecute(Connection connection,
					PreparedStatement pStatement, ResultSet rs) {
				List list = null;// �洢��ѯ����������� �����ڲ�������Ա����ǳ���

				try {
					ResultSetMetaData metaData = rs.getMetaData();
					System.out.println(metaData.getColumnLabel(2));
					while (rs.next()) {
						if (list == null) {
							list = new ArrayList();
						}
						Object rowObj = clazz.newInstance(); // ����Javabean���޲ι�����
						// ���� select * from emp where id>? and age>?
						for (int i = 0; i < metaData.getColumnCount(); i++) {
							// ��������Ӧ�����ݿ�ı���ֶ�����getColumnLabel(i + 1)��ʾ���ݿ��ĵڼ����ֶ���
							String columnName = metaData.getColumnLabel(i + 1); 
							Object columnValue = rs.getObject(i + 1); // ����ֵ��Ӧ�����ݵ��ֶ�ֵgetObject(i + 1)
							// ����rowObj�����setUserName��������columnValue���ú�
							ReflectUtils.invokeSet(rowObj, columnName,
									columnValue);
						}
						list.add(rowObj);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return list;
			}
		});
	}

	/**
	 * ��ѯ����һ�м�¼������ÿ�м�¼��װ��clazzָ�����������
	 * 
	 * @param sql
	 *            ִ�е�SQL���
	 * @param clazz
	 *            ��װ���ݵ�Javabean���Class����
	 * @param params
	 *            SQL����
	 * @return ��ѯ���Ľ��
	 */
	public Object queryUniqueRow(String sql, Class clazz, Object[] params) {
		List list = queryRows(sql, clazz, params);
		return (list != null && list.size() > 0) ? list.get(0) : null;
	}

	/**
	 * ����������ֵ���Ҷ�Ӧ�Ķ���
	 * 
	 * @param clazz
	 * @param obj
	 * @return ����
	 */
	public Object queryById(Class clazz, Object obj) {
		// ͨ�����伯Class������TableInfo
		TableInfo info = TableContext.poClassTableMap.get(clazz);
		// �������
		ColumnInfo onlyPK = info.getOnlyPrimaryKey();
		String sql = "select * from " + info.gettName() + " where "
				+ onlyPK.getName() + "=?;";
		return queryUniqueRow(sql, clazz, new Object[] { obj });
	}
	
	/**
	 * ��ѯ����һ��ֵ��Ҳ����һ��һ�У������ظ�ֵ,���磺
	 *</br> select count(*) from emp where salary>?
	 * 
	 * @param sql
	 *            ִ�е�SQL���
	 * @param params
	 *            SQL����
	 * @return ��ѯ���Ľ��
	 */
	public Object queryValue(String sql, Object[] params) {
		//��������ظ�queryValue����
		return excuteQueryTemplate(sql, params, null, new Callback() {

			@Override
			public Object doExecute(Connection connection,
					PreparedStatement pStatement, ResultSet rs) {
				Object value = null;
				try {
					while (rs.next()) {
						// select Count(*) from emp
						value = rs.getObject(1);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return value;  //��������ظ�excuteQueryTemplate()
			}
		});
	}

	/**
	 * ��ѯ����һ�����֣�Ҳ����һ��һ�У������ظ�ֵ
	 * 
	 * @param sql
	 *            ִ�е�SQL���
	 * @param params
	 *            SQL����
	 * @return ��ѯ���Ľ��
	 */
	public Number queryNumber(String sql, Object[] params) {
		return (Number) queryValue(sql, params);
	}

	/**
	 * ��Ϊ������oracle���ݿ⻹��MySQL���ݿ⣬��׼����ɾ�Ĳ��ǲ���
	 * Ȼ�������Եķ�ҳ�ǲ���ͬ�ģ�������÷���Ϊ���󷽷�����������ʵ��</br> �÷����ǽ��з�ҳ�������ӵڼ�ҳ���ڼ�ҳ��ѯ����ѯ����ҳ��
	 * 
	 * @param pageNum
	 *            �ڼ�ҳ����
	 * @param size
	 *            ÿҳ��ʾ���ټ�¼
	 * @return
	 */
	public abstract Object queryPagenate(int pageNum, int size);

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	
	
}
