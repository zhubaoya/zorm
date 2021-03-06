package com.zby.zorm.bean;

/**
 * 封装配置文件信息
 * @author 祝宝亚
 *
 */
public class Configuration {

	/**
	 * 驱动类
	 */
	private String driver;
	
	/**
	 * jdbc的URL
	 */
	private String url;
	
	/**
	 * 数据库的用户名
	 */
	private String user;
	
	/**
	 * 数据库的密码
	 */
	private String pass;
	
	/**
	 * 正在使用的数据库
	 */
	private String usingDB;
	
	/**
	 * 项目的源码路径
	 */
	private String srcPath;
	
	/**
	 * 扫描生产Java类的包
	 */
	private String poPackage;
	
	/**
	 * 项目使用的查询类时哪一个类
	 */
	private String queryClass;
	
	/**
	 * Connection连接池的最小数
	 */
	private int poolMinSize;
	
	/**
	 * Connection连接池的最大数
	 */
	private int poolMaxSize;
	
	public Configuration(String driver, String url, String user, String pass,
			String usingDB, String srcPath, String poPackage) {
		super();
		this.driver = driver;
		this.url = url;
		this.user = user;
		this.pass = pass;
		this.usingDB = usingDB;
		this.srcPath = srcPath;
		this.poPackage = poPackage;
	}

	public Configuration() {
		super();
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getUsingDB() {
		return usingDB;
	}

	public void setUsingDB(String usingDB) {
		this.usingDB = usingDB;
	}

	public String getSrcPath() {
		return srcPath;
	}

	public void setSrcPath(String srcPath) {
		this.srcPath = srcPath;
	}

	public String getPoPackage() {
		return poPackage;
	}

	public void setPoPackage(String poPackage) {
		this.poPackage = poPackage;
	}

	public String getQueryClass() {
		return queryClass;
	}

	public void setQueryClass(String queryClass) {
		this.queryClass = queryClass;
	}

	public int getPoolMinSize() {
		return poolMinSize;
	}

	public void setPoolMinSize(int poolMinSize) {
		this.poolMinSize = poolMinSize;
	}

	public int getPoolMaxSize() {
		return poolMaxSize;
	}

	public void setPoolMaxSize(int poolMaxSize) {
		this.poolMaxSize = poolMaxSize;
	}
	
}
