package com.fz.orm.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * 用于回调的接口
 * <br><br><strong>时间:</strong><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;2015年10月29日 上午10:41:47<br>
 * @author FZ
 * @version 1.0
 */
public interface CallBack {
	/**
	 * 执行
	 * @param conn
	 * @param ps
	 * @param rs
	 */
	public Object doExecute(Connection conn,PreparedStatement ps ,ResultSet rs);
}
