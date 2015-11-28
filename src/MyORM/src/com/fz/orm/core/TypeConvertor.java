package com.fz.orm.core;

/**
 * 负责类型转换接口（java数据类型和数据库中的数据类型相互转换）
 * <br><br><strong>时间:</strong><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;2015年10月29日 上午10:42:26<br>
 * @author FZ
 * @version 1.0
 */
public interface TypeConvertor {
	/**
	 * 将数据库数据类型转换为java的数据类型
	 * @param columnType	数据库字段的数据类型
	 * @return				转换过的java数据类型
	 */
	public String databaseType2JavaType(String columnType);
	/**
	 * 将java数据类型转换为数据库数据类型
	 * @param javaType	java数据类型
	 * @return			数据库中的数据类型
	 */
	public String javaType2DatabaseType(String javaType);
}
