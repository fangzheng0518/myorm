package com.fz.orm.core;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import com.fz.orm.bean.Configuration;
import com.fz.orm.pool.DBConnPool;


/**
 * 根据配置信息，维持数据库连接对象的管理（连接池等）
 * <br><br><strong>时间:</strong><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;2015年10月29日 上午10:41:51<br>
 * @author FZ
 * @version 1.0
 */
public class DBManager {
	/**
	 * 管理配置信息
	 */
	private static Configuration conf;
	/**
	 * 连接池对象
	 */
	private static DBConnPool pool;
	
	/**
	 * 静态代码块，用于初始化数据库的配置信息
	 */
	static{
		Properties pros = new Properties();
		try {
			pros.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		conf = new Configuration();
		conf.setDriver(pros.getProperty("driver"));
		conf.setUrl(pros.getProperty("url"));
		conf.setUser(pros.getProperty("user"));
		conf.setPwd(pros.getProperty("pwd"));
		conf.setUsingDB(pros.getProperty("mysql"));
		conf.setPoPackage(pros.getProperty("poPackage"));
		conf.setQueryCalss(pros.getProperty("queryCalss"));
		conf.setPoolMaxSize(Integer.parseInt(pros.getProperty("poolMaxSize")));
		conf.setPoolMinSize(Integer.parseInt(pros.getProperty("poolMinSize")));
		
	}
	/**
	 * 获取数据库连接
	 * @return 从连接池中获取的数据库连接
	 */
	public static Connection getConn(){
		if (pool==null) {
			pool = new DBConnPool();
		}
		return pool.getConnection();
	}
	/**
	 * 创建数据库连接
	 * @return	一个新的数据库连接
	 */
	public static Connection createConn(){
		try {
			Class.forName(conf.getDriver());
			return DriverManager.getConnection(conf.getUrl(), conf.getUser(), conf.getPwd());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 关闭连接
	 * @param rs
	 * @param ps
	 * @param conn
	 */
	public static void close(ResultSet rs,Statement ps ,Connection conn){
		try {
			if (rs != null ) {
				rs.close();
			}
			if (ps != null ) {
				ps.close();
			}
			if (conn != null ) {
				pool.close(conn);//从连接池中关闭
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 关闭连接
	 * @param conn
	 */
	public static void close(Connection conn){
		pool.close(conn);
	}
	/**
	 * 返回管理配置信息对象
	 * @return 返回Configuration对象
	 */
	public static Configuration getConf(){
		return conf;
	}
	
}
