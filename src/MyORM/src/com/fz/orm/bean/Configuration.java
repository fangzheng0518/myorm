package com.fz.orm.bean;

/**
 * 管理配置信息
 * <br><br><strong>时间:</strong><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;2015年10月29日 上午10:41:26<br>
 * @author FZ
 * @version 1.0
 */
public class Configuration {
	/**
	 * 驱动类
	 */
	private String driver;
	/**
	 * jdbc的url
	 */
	private String url;
	/**
	 * 用户名
	 */
	private String user;
	/**
	 * 密码
	 */
	private String pwd;
	/**
	 * 正在使用的数据库
	 */
	private String usingDB;
	/**
	 * 项目源码路径
	 */
	private String srcPath;
	/**
	 * 扫描生成java类的包（po：persistence object 持久化对象）
	 */
	private String poPackage;
	/**
	 * 项目使用的查询是哪一个类
	 */
	private String queryCalss;
	/**
	 * 连接池中最大连接数
	 */
	private int poolMaxSize;
	/**
	 * 连接池中最小连接数
	 */
	private int poolMinSize;
	
	
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
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
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
	public String getQueryCalss() {
		return queryCalss;
	}
	public void setQueryCalss(String queryCalss) {
		this.queryCalss = queryCalss;
	}
	public int getPoolMaxSize() {
		return poolMaxSize;
	}
	public void setPoolMaxSize(int poolMaxSize) {
		this.poolMaxSize = poolMaxSize;
	}
	public int getPoolMinSize() {
		return poolMinSize;
	}
	public void setPoolMinSize(int poolMinSize) {
		this.poolMinSize = poolMinSize;
	}
	
	public Configuration(String driver, String url, String user, String pwd, String usingDB, String srcPath,
			String poPackage, String queryCalss, int poolMaxSize, int poolMinSize) {
		super();
		this.driver = driver;
		this.url = url;
		this.user = user;
		this.pwd = pwd;
		this.usingDB = usingDB;
		this.srcPath = srcPath;
		this.poPackage = poPackage;
		this.queryCalss = queryCalss;
		this.poolMaxSize = poolMaxSize;
		this.poolMinSize = poolMinSize;
	}
	public Configuration() {
	}
}
