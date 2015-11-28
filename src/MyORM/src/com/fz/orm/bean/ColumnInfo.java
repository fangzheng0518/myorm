package com.fz.orm.bean;

/**
 * 表中某一个字段的详细信息
 * <br><br><strong>时间:</strong><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;2015年10月29日 上午10:41:10<br>
 * @author FZ
 * @version 1.0
 */
public class ColumnInfo {
	/**
	 * 字段的名称
	 */
	private String name;
	/**
	 * 字段的数据类型
	 */
	private String dataType;
	/**
	 * 字段的类型（0:普通键	1:主键   2:外键）
	 */
	private int keyType;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public int getKeyType() {
		return keyType;
	}
	public void setKeyType(int keyType) {
		this.keyType = keyType;
	}
	public ColumnInfo() {
	}
	public ColumnInfo(String name, String dataType, int keyType) {
		super();
		this.name = name;
		this.dataType = dataType;
		this.keyType = keyType;
	}
	
	
}
