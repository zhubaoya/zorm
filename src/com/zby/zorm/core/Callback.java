package com.zby.zorm.core;

import java.sql.Connection;
import java.sql.ResultSet;

import com.mysql.jdbc.PreparedStatement;

public interface Callback {
	
	public Object doExecute(Connection connection,PreparedStatement pStatement,ResultSet rs);

}
