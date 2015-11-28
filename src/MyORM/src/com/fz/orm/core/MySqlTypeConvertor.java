package com.fz.orm.core;

/**
 * mysql数据类型和java数据类型的转换
 * <br><br><strong>时间:</strong><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;2015年10月29日 上午10:42:01<br>
 * @author FZ
 * @version 1.0
 */
public class MySqlTypeConvertor implements TypeConvertor{
	/**
	 * 将mysql数据库中类型转换成java的数据类型
	 */
	@Override
	public String databaseType2JavaType(String columnType) {
		//varchar,char--->String
		if ("varchar".equalsIgnoreCase(columnType) || "char".equalsIgnoreCase(columnType)) {
			return "String";
		}else if("int".equalsIgnoreCase(columnType)
				|| "tinyint".equalsIgnoreCase(columnType)
				|| "smallint".equalsIgnoreCase(columnType)
				|| "integer".equalsIgnoreCase(columnType)
				){
			return "Integer";
		}else if("bigint".equalsIgnoreCase(columnType)){
			return "Long";
		}else if("double".equalsIgnoreCase(columnType)||"float".equalsIgnoreCase(columnType)){
			return "Double";
		}else if("clob".equalsIgnoreCase(columnType)){
			return "java.sql.CLob";
		}else if("blob".equalsIgnoreCase(columnType)){
			return "java.sql.BLob";
		}else if("date".equalsIgnoreCase(columnType)){
			return "java.sql.Date";
		}else if("time".equalsIgnoreCase(columnType)){
			return "java.sql.Time";
		}else if("timestamp".equalsIgnoreCase(columnType)){
			return "java.sql.TimeStamp";
		}

		return null;
	}
	/**
	 * 将java类型转换为mysql数据库类型
	 */
	@Override
	public String javaType2DatabaseType(String javaType) {
		// TODO Auto-generated method stub
		return null;
	}

}
