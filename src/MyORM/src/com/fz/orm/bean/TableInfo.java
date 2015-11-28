package com.fz.orm.bean;

import java.util.List;
import java.util.Map;

/**
 * 表信息
 * <br><br><strong>时间:</strong><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;2015年10月29日 上午10:41:38<br>
 * @author FZ
 * @version 1.0
 */
public class TableInfo {
	/**
	 * 表名
	 */
	private String tname;
	/**
	 * 表中所有字段的信息
	 */
	private Map<String,ColumnInfo> columns;
	/**
	 * 唯一主键，目前只能处理表中有且只有一个主键的情况
	 */
	private ColumnInfo onlyPrikey;
	/**
	 * 联合主键
	 */
	private List<ColumnInfo> priKeys;
	public String getTname() {
		return tname;
	}
	public void setTname(String tname) {
		this.tname = tname;
	}
	public Map<String, ColumnInfo> getColumns() {
		return columns;
	}
	public void setColumns(Map<String, ColumnInfo> columns) {
		this.columns = columns;
	}
	public ColumnInfo getOnlyPrikey() {
		return onlyPrikey;
	}
	public void setOnlyPrikey(ColumnInfo onlyPrikey) {
		this.onlyPrikey = onlyPrikey;
	}
	public List<ColumnInfo> getPriKeys() {
		return priKeys;
	}
	public void setPriKeys(List<ColumnInfo> priKeys) {
		this.priKeys = priKeys;
	}
	public TableInfo(String tname, Map<String, ColumnInfo> columns, ColumnInfo onlyPrikey) {
		super();
		this.tname = tname;
		this.columns = columns;
		this.onlyPrikey = onlyPrikey;
		
	}
	public TableInfo() {
	}
	public TableInfo(String tname, List<ColumnInfo> priKeys, Map<String, ColumnInfo> columns) {
		super();
		this.tname = tname;
		this.columns = columns;
		this.priKeys = priKeys;
	}
}
