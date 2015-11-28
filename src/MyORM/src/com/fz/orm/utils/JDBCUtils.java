package com.fz.orm.utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 封装了JDBC的常用操作
 * <br><br><strong>时间:</strong><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;2015年10月29日 上午10:42:48<br>
 * @author FZ
 * @version 1.0
 */
public class JDBCUtils {
	/**
	 * 给sql设置参数
	 * @param ps		预编译sql对象
	 * @param params	参数
	 */
	public static void handlePrams(PreparedStatement ps ,Object[] params){
		if (params!=null && params.length>0) {
			for (int i = 0; i < params.length; i++) {
				try {
					ps.setObject(1+i, params[i]);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
