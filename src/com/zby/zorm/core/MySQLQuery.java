package com.zby.zorm.core;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.PreparedStatement;
import com.zby.empVO.EMPVO;
import com.zby.po.Emp;
import com.zby.zorm.bean.ColumnInfo;
import com.zby.zorm.bean.TableInfo;
import com.zby.zorm.utils.JDBCUtils;
import com.zby.zorm.utils.ReflectUtils;

@SuppressWarnings(value = { "all" })
public class MySQLQuery extends Query {


	@Override
	public Object queryPagenate(int pageNum, int size) {
		// TODO Auto-generated method stub
		return null;
	}
}
