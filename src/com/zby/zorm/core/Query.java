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
 * query的抽象类，负责查询（对外提供服务的核心类）
 * 
 * @author 祝宝亚
 * 
 */
@SuppressWarnings(value = "all")
public abstract class Query implements Cloneable{

	/**
	 * 查询语句的模板方法，因为不同的查询，有不同的实现效果，比如：</br>
	 * &nbsp; &nbsp; &nbsp; &nbsp;queryValue(sql,)方法：查询某个值</br>
	 * &nbsp; &nbsp; &nbsp; &nbsp;queryRows(sql,)方法：  查询SQL的结果集</br>
	 * 
	 * 这些通过Callback接口，通过传入connection、PreparedStatement、ResultSet对象来实现，
	 * 这些对象在excuteQueryTemplate方法中已经声明，用以执行查询时的响应操作，即回调函数
	 * 
	 * @param sql  SQL语句
	 * @param params  SQL参数
	 * @param clazz  //记录要封装的Java类
	 * @param cb 回调的接口
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
			return cb.doExecute(conn, ps, rs); // 将结果object的值返回给excuteQueryTemplate()方法
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			DBManager.close(ps, rs, conn);
		}
	}

	/**
	 * 执行SQL数据，也就是数据操纵层 DML即Data manipulation layer
	 * 
	 * @param sql
	 *            sql语句
	 * @param params
	 *            参数
	 * @return 执行SQL语句后影响的行数
	 */
	public int executeDML(String sql, Object[] params) {
		Connection connection = DBManager.getConn();
		int count = 0;

		PreparedStatement ps = null;
		try {
			ps = (PreparedStatement) connection.prepareStatement(sql);
			// 给SQL设参
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
	 * 将一个对象存储到数据中 把对象中不为null的属性往数据库中存储，如果数字为null就为0</br>
	 * obj就是从数据库中的某张表名映射出的类名</br> 属性名就是数据库表中的字段名
	 * 
	 * @param obj
	 */
	public void insert(Object obj) {
		List<Object> params = new ArrayList<Object>(); // 请求存储SQL的参数对象

		Class clazz = obj.getClass();  //通过反射加载当前对象的类
		TableInfo tableInfo = TableContext.poClassTableMap.get(clazz);

		StringBuilder sql = new StringBuilder("insert into "
				+ tableInfo.gettName() + " (");

		int countNotNullField = 0; // 计算不为null的属性值

		Field[] fs = clazz.getDeclaredFields();  //通过反射获取当前对象的属性
		for (Field f : fs) {
			String FieldName = f.getName();  //获取当前对象属性的属性的名字
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
	 * 删除，clazz表示类对应的表中记录（指定主键值id的记录）
	 * </p>
	 * <p>
	 * id表示主键:</br>
	 * <p>
	 * &nbsp;&nbsp;比如delete from emp where id=1
	 * </p>
	 * </p>
	 * 
	 * @param clazz
	 *            跟表对应类的class对象
	 * @param obj 删除某行的参数值 
	 * @return
	 */
	public void delete(Class clazz, Object obj) {
		// 通过反射集Class对象找TableInfo
		TableInfo info = TableContext.poClassTableMap.get(clazz);
		// 获得主键
		ColumnInfo onlyPK = info.getOnlyPrimaryKey();
		String sql = "delete from " + info.gettName() + " where "
				+ onlyPK.getName() + "=?;";
		executeDML(sql, new Object[] { obj });
	}

	/**
	 * 删除对象在数据库中对应的记录（对象所在的类对应到表，对象的主键对应到记录）
	 * 
	 * @param obj
	 * @return
	 */
	public void delete(Object obj) {
		Class clazz = obj.getClass();
		TableInfo tableInfo = TableContext.poClassTableMap.get(clazz);
		ColumnInfo onlyPK = tableInfo.getOnlyPrimaryKey();

		// 通过反射机制，调取属性的set或get方法
		Object priKeyValue = ReflectUtils.InvokeGet(onlyPK.getName(), obj);

		delete(clazz, priKeyValue);
	}

	/**
	 * 更新对象对应的记录，并且只更新指定的字段的值</br> &nbsp;&nbsp;&nbsp;&nbsp;比如update user set
	 * uname=? pwd=?
	 * 
	 * @param obj
	 *            所更新的对象
	 * @param fieldNames
	 *            更新的属性列表
	 * @return 执行SQL语句后影响的行数
	 */
	public int update(Object obj, String[] fieldNames) {
		List<Object> params = new ArrayList<Object>(); // 请求存储SQL的参数对象

		Class clazz = obj.getClass();
		TableInfo tableInfo = TableContext.poClassTableMap.get(clazz);
		ColumnInfo onlyPk = tableInfo.getOnlyPrimaryKey(); // 获得唯一主键

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
		params.add(ReflectUtils.InvokeGet(onlyPk.getName(), obj)); // 主键的值
		return executeDML(sql.toString(), params.toArray());
	}

	/**
	 * 查询返回多行记录，并将每行记录封装到clazz指定的类对象中</br>
	 * 调用上面的模板方法excuteQueryTemplate(),在方法excuteQueryTemplate()
	 * 内实现接口Callback，也就是方法内部类，类似于Android开发中的监听器
	 * 
	 * @param sql
	 *            执行的SQL语句
	 * @param clazz
	 *            封装数据的Javabean类的Class对象
	 * @param params
	 *            SQL参数
	 * @return 查询到的结果封装到list中
	 */
	public List queryRows(String sql, final Class clazz, Object[] params) {

		//将结果返回给queryRows方法
		return (List) excuteQueryTemplate(sql, params, clazz, new Callback() {

			@Override
			public Object doExecute(Connection connection,
					PreparedStatement pStatement, ResultSet rs) {
				List list = null;// 存储查询结果的容器； 方法内部类的属性必须是常量

				try {
					ResultSetMetaData metaData = rs.getMetaData();
					System.out.println(metaData.getColumnLabel(2));
					while (rs.next()) {
						if (list == null) {
							list = new ArrayList();
						}
						Object rowObj = clazz.newInstance(); // 调用Javabean的无参构造器
						// 多列 select * from emp where id>? and age>?
						for (int i = 0; i < metaData.getColumnCount(); i++) {
							// 属性名对应着数据库的表的字段名，getColumnLabel(i + 1)表示数据库表的第几个字段名
							String columnName = metaData.getColumnLabel(i + 1); 
							Object columnValue = rs.getObject(i + 1); // 属性值对应着数据的字段值getObject(i + 1)
							// 调用rowObj对象的setUserName方法，将columnValue设置好
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
	 * 查询返回一行记录，并将每行记录封装到clazz指定的类对象中
	 * 
	 * @param sql
	 *            执行的SQL语句
	 * @param clazz
	 *            封装数据的Javabean类的Class对象
	 * @param params
	 *            SQL参数
	 * @return 查询到的结果
	 */
	public Object queryUniqueRow(String sql, Class clazz, Object[] params) {
		List list = queryRows(sql, clazz, params);
		return (list != null && list.size() > 0) ? list.get(0) : null;
	}

	/**
	 * 根据主键的值查找对应的对象
	 * 
	 * @param clazz
	 * @param obj
	 * @return 对象
	 */
	public Object queryById(Class clazz, Object obj) {
		// 通过反射集Class对象找TableInfo
		TableInfo info = TableContext.poClassTableMap.get(clazz);
		// 获得主键
		ColumnInfo onlyPK = info.getOnlyPrimaryKey();
		String sql = "select * from " + info.gettName() + " where "
				+ onlyPK.getName() + "=?;";
		return queryUniqueRow(sql, clazz, new Object[] { obj });
	}
	
	/**
	 * 查询返回一个值，也就是一行一列，并返回该值,比如：
	 *</br> select count(*) from emp where salary>?
	 * 
	 * @param sql
	 *            执行的SQL语句
	 * @param params
	 *            SQL参数
	 * @return 查询到的结果
	 */
	public Object queryValue(String sql, Object[] params) {
		//将结果返回给queryValue方法
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
				return value;  //将结果返回给excuteQueryTemplate()
			}
		});
	}

	/**
	 * 查询返回一个数字，也就是一行一列，并返回该值
	 * 
	 * @param sql
	 *            执行的SQL语句
	 * @param params
	 *            SQL参数
	 * @return 查询到的结果
	 */
	public Number queryNumber(String sql, Object[] params) {
		return (Number) queryValue(sql, params);
	}

	/**
	 * 因为不论是oracle数据库还是MySQL数据库，标准的增删改查是差不多的
	 * 然而，各自的分页是不相同的，因而，该方法为抽象方法，供其子类实现</br> 该方法是进行分页操作，从第几页到第几页查询，查询多少页等
	 * 
	 * @param pageNum
	 *            第几页数据
	 * @param size
	 *            每页显示多少记录
	 * @return
	 */
	public abstract Object queryPagenate(int pageNum, int size);

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	
	
}
