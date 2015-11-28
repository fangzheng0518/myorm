package com.fz.orm.core;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fz.orm.bean.ColumnInfo;
import com.fz.orm.bean.TableInfo;
import com.fz.orm.utils.JavaFileUtils;
import com.fz.orm.utils.StringUtils;

/**
 * 负责获取管理数据库所有表结构和类结构的关系，并可以根据表结构生成类
 * <br><br><strong>时间:</strong><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;2015年10月29日 上午10:42:21<br>
 * @author FZ
 * @version 1.0
 */
@SuppressWarnings("all")
public class TableContext {
	/**
	 * 表名为key，表信息为value
	 */
	public static Map<String,TableInfo> tables = new HashMap<>();
	/**
	 * 将po对象和数据库中的表关联起来
	 */
	public static Map<Class,TableInfo> poClassTabMap = new HashMap<Class,TableInfo>();
	public TableContext() {
	}
	
	static{
		try {
			Connection conn = DBManager.getConn();
			DatabaseMetaData dbmd = conn.getMetaData();//DatabaseMetaData接口：关于数据库的整体综合信息
			//获取所有表
			ResultSet tableRet = dbmd.getTables(null, "%", "%", new String[]{"TABLE"});
			while (tableRet.next()) {
				//获取所有表名
				String tableName = (String) tableRet.getObject("TABLE_NAME");
				TableInfo ti = new TableInfo(tableName, new ArrayList<ColumnInfo>(), new HashMap<String, ColumnInfo>());
				tables.put(tableName, ti);
				
				//获取表中所有字段
				ResultSet columnSet = dbmd.getColumns(null, "%", tableName, "%");
				while (columnSet.next()) {
					ColumnInfo ci = new ColumnInfo(columnSet.getString("COLUMN_NAME"), columnSet.getString("TYPE_NAME"), 0);
					//设置列信息到表信息对象中
					ti.getColumns().put(columnSet.getString("COLUMN_NAME"), ci);
				}
				
				//获取表中主键
				ResultSet primarySet = dbmd.getPrimaryKeys(null, "%", tableName);
				while (primarySet.next()) {
					//获取主键对应的列
					ColumnInfo ci2 = ti.getColumns().get(primarySet.getString("COLUMN_NAME"));
					ci2.setKeyType(1);//设置为主键类型
					ti.getPriKeys().add(ci2);
				}
				
				if (ti.getPriKeys().size() > 0) {
					ti.setOnlyPrikey(ti.getPriKeys().get(0));
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		//TableContext类被加载时根据表结构生成对应的java源代码
		updateJavaPoFile();
		//加载po包下所有类，并和tableInfo绑定
		loadPOTables();
	}
	/**
	 * 获取表信息
	 * @return 返回一个用map存储的所有表信息
	 */
	public static Map<String,TableInfo> getTableInfo(){
		return tables;
	}
	/**
	 * 根据数据库中表结构生成对应的java源代码
	 * 项目启动时应该调用此方法
	 */
	public static void updateJavaPoFile(){
		//将数据库中所有表信息生成java类文件
		Map<String,TableInfo> map = TableContext.tables;
		for (TableInfo t : map.values()) {
			JavaFileUtils.createJavaPOFile(t,new MySqlTypeConvertor());
		}
	}
	/**
	 * 加载po包下的类对象，并和tableInfo绑定起来
	 */
	public static void loadPOTables(){
		String poPath = DBManager.getConf().getPoPackage();
		for (TableInfo tableInfo : tables.values()) {
			try {
				Class c = Class.forName(poPath+"."+StringUtils.firstChar2UpperCase(tableInfo.getTname()));
				poClassTabMap.put(c, tableInfo);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
}
 