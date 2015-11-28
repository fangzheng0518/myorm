package com.fz.orm.pool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.fz.orm.core.DBManager;

/**
 * 数据库连接池
 * <br><br><strong>时间:</strong><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;2015年10月29日 上午10:42:34<br>
 * @author FZ
 * @version 1.0
 */
public class DBConnPool {
	/**
	 * 连接池容器
	 */
	private List<Connection> pool;
	/**
	 * 连接池中最大连接数
	 */
	private final static int POOL_MAX_SIZE=DBManager.getConf().getPoolMaxSize();
	/**
	 * 连接池中最小连接数
	 */
	private final static int POOL_MIN_SIZE=DBManager.getConf().getPoolMinSize();

	public DBConnPool() {
		initPool();//初始化连接
//		System.out.println(DBManager.class);
		try {
			Class.forName(DBManager.class.getName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 初始化连接池
	 */
	public void initPool(){
		if (pool ==null) {
			pool = new ArrayList<Connection>();
		}
		//如果连接池中的连接小于最小连接数，则创建
		while (pool.size() < DBConnPool.POOL_MIN_SIZE) {
			pool.add(DBManager.createConn());
		}
		System.out.println("初始化连接池，连接池中连接数量为："+pool.size());
	}
	/**
	 * 从连接池中获取一个链接
	 * @return 连接池中已存在的一个数据库连接
	 */
	public synchronized Connection getConnection(){
		int last_index = pool.size()-1;
		Connection conn = pool.get(last_index);
		pool.remove(last_index);
		return conn;
	}
	/**
	 * 将用完的连接放入到连接池中
	 * @param conn
	 */
	public synchronized void close(Connection conn){
		//如果连接池已满，则直接关闭连接。否则就放入连接池
		if (pool.size()>=POOL_MAX_SIZE) {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			pool.add(conn);
		}
	}
	
}
