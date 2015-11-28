package com.fz.orm.core;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.fz.orm.bean.ColumnInfo;
import com.fz.orm.bean.TableInfo;
import com.fz.orm.utils.JDBCUtils;
import com.fz.orm.utils.ReflectUtils;

/**
 * 查询接口（对外提供服务）
 * <br><br><strong>时间:</strong><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;2015年10月29日 上午10:42:10<br>
 * @author FZ
 * @version 1.0
 */
@SuppressWarnings("all")
public abstract class Query implements Cloneable {
	/**
	 * 采用模板方法将jdbc的查询封装成模板，便于重用
	 * @param sql		要查询的sql
	 * @param params	sql的参数
	 * @param clazz		记录要封装到的java类
	 * @param back		CallBack的实现类，实现回调
	 * @return 返回查询结果
	 */
	public Object executeQueryTemplate(String sql,Object[] params,Class clazz,CallBack back){
		Connection conn = DBManager.getConn();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			JDBCUtils.handlePrams(ps, params);
			System.out.println(ps);
			rs = ps.executeQuery();
			//开始处理查询
			return back.doExecute(conn, ps, rs);
			//结束处理查询
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally{
			DBManager.close(null, ps, conn);
		}
	}
	/**
	 * 执行一个DML语句
	 * @param sql	要执行的sql
	 * @param params sql中的参数
	 * @return	执行sql后影响的条数，失败则为0
	 */
	public int executeDML(String sql, Object[] params) {
		Connection conn = DBManager.getConn();
		int count =0;//影响的记录条数
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			//给sql设置参数
			JDBCUtils.handlePrams(ps, params);
			System.out.println(ps);
			//执行sql
			count = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally{
			DBManager.close(null, ps, conn);
		}
		return count;
	}
	/**
	 * 将一个对象存储到数据库中
	 * @param obj	要存储的对象
	 */
	public void insert(Object obj) {
		Class c = obj.getClass();
		//得到具体表信息对象
		TableInfo tableInfo = TableContext.poClassTabMap.get(c);
		List<Object> params = new ArrayList<Object>();//参数的值
		int countNotNullField = 0;//不为空属性的个数，用于填充?使用
		StringBuilder sql = new StringBuilder("insert into "+tableInfo.getTname()+" (");
		Field[] fs = c.getDeclaredFields();
		for (Field f : fs) {
			//属性名称
			String fieldName = f.getName();
			//属性的值
			Object fieldValue =ReflectUtils.invokeGet(fieldName, obj);
			if (fieldValue !=null) {
				countNotNullField++;
				sql.append(fieldName+",");
				params.add(fieldValue);
			}
		}
		sql.setCharAt(sql.length()-1,')');//将insert的()中最后一个,转换为)
		sql.append(" values (");
		for (int i = 0; i < countNotNullField; i++) {
			sql.append("?,");
		}
		sql.setCharAt(sql.length()-1,')');//将values的()中最后一个,转换为)
		//执行sql语句
		executeDML(sql.toString(), params.toArray());
	}
	/**
	 * 根据id删除记录
	 * @param clazz	需要删除表对于的class
	 * @param id	需要删除的id
	 */
	public void delete(Class clazz, Object id) {
		//通过传入的clazz获取对应的tableInfo
		TableInfo tableInfo = TableContext.poClassTabMap.get(clazz);
		//获取唯一主键
		ColumnInfo onlyPrikey = tableInfo.getOnlyPrikey();
		//拼接sql语句
		String sql = "delete from "+tableInfo.getTname()+" where "+onlyPrikey.getName()+"=? ";
		//执行查询
		executeDML(sql, new Object[]{id});
	}
	/**
	 * 根据对象删除记录
	 * @param obj
	 */
	public void delete(Object obj) {
		Class c = obj.getClass();
		//通过传入的clazz获取对应的tableInfo
		TableInfo tableInfo = TableContext.poClassTabMap.get(c);
		//获取唯一主键信息
		ColumnInfo onlyPrikey = tableInfo.getOnlyPrikey();
		//通过反射调用要删除对象主键的get方法，查看主键是否为空
		Object onlyPriKeyValue = ReflectUtils.invokeGet(onlyPrikey.getName(), obj);
		//调用通过主键删除的方法
		delete(c, onlyPriKeyValue);
	}
	/**
	 * 更新对象对应的记录，并且只更新指定字段的值
	 * @param obj			所要更新的对象
	 * @param fieldNames	所要更新的属性列表
	 * @return	执行sql语句后影响的记录条数
	 */
	public int update(Object obj, String[] fieldNames) {
		Class c = obj.getClass();
		List<Object> params = new ArrayList<Object>();
		TableInfo tableInfo = TableContext.poClassTabMap.get(c);//得到具体表信息
		ColumnInfo onlyPrikey = tableInfo.getOnlyPrikey();//获取唯一主键
		StringBuilder sql = new StringBuilder("update "+tableInfo.getTname()+" set ");
		//遍历需要更新的属性列表
		for (String fname : fieldNames) {
			Object fieldValue = ReflectUtils.invokeGet(fname, obj);//调用每个需要更新属性的get方法，获取值
			if (fieldValue!=null) {
				params.add(fieldValue);//将属性值设置到参数list中
				sql.append(fname+"=?,");
			}
		}
		sql.setCharAt(sql.length()-1, ' ');//将set后面最后一个，替换成空格
		sql.append(" where ");
		sql.append(onlyPrikey.getName()+"=?");
		params.add(ReflectUtils.invokeGet(onlyPrikey.getName(), obj));//设置主键的值
		//执行sql
		return executeDML(sql.toString(), params.toArray());
	}
	/**
	 * 查询返回多行记录(多行多列)，并将每行记录封装为clazz对象，结果为list<clazz>
	 * @param sql		要查询的sql
	 * @param clazz		需要封装结果的对象类型
	 * @param params	sql的参数
	 * @return			封装后的结果list
	 */
	public List findRows(String sql, final Class clazz, Object[] params) {
		//调用查询的模板
		return (List)executeQueryTemplate(sql, params, clazz, new CallBack() {
			@Override
			public Object doExecute(Connection conn, PreparedStatement ps, ResultSet rs) {
				List list = null;//查询结果
				try {
					ResultSetMetaData metaData = rs.getMetaData();//获取查询结果元信息，用于获取列名，列个数
					while(rs.next()){//遍历行
						if (list == null) {
							list = new ArrayList<>();
						}
						Object rowObj = clazz.newInstance();//调用无参构造函数，构造新对象，用于向结果list中设置
						//遍历列
						for (int i = 0; i < metaData.getColumnCount(); i++) {
							String columnName = metaData.getColumnLabel(i+1);//获取列的别名
							Object columnValue = rs.getObject(i+1);//获取每一列的值
							if (columnValue!=null) {
								//调用rowObj的set方法，将返回结果的值设置进去。
								ReflectUtils.invokeSet(rowObj, columnName, columnValue);
							}
						}
						//将rowobj对象设置到返回结果list中
						list.add(rowObj);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return list;
			}
		});
		
	}
	/**
	 * 查询返回一行记录(一行多列)，并将每行记录封装为clazz对象，结果为Object
	 * @param sql		要查询的sql
	 * @param clazz		需要封装结果的对象类型
	 * @param params	sql的参数
	 * @return			查询结果
	 */
	public Object findUniqueRows(String sql, Class clazz, Object[] params) {
		List list = findRows(sql, clazz, params);
		return ((list !=null)&&(list.size()>0)?list.get(0):null);
	}
	/**
	 * 根据主键查询对象信息
	 * @param clazz	要查询的对象
	 * @param id	主键值
	 * @return		主键对应的唯一一条记录
	 */
	public List findById(Class clazz ,Object id){
		//通过传入的clazz获取对应的tableInfo
		TableInfo tableInfo = TableContext.poClassTabMap.get(clazz);
		//获取唯一主键
		ColumnInfo onlyPrikey = tableInfo.getOnlyPrikey();
		//拼接sql语句
		String sql = "select * from "+tableInfo.getTname()+" where "+onlyPrikey.getName()+"=? ";
		return findRows(sql, clazz, new Object[]{id}); 
	}
	/**
	 * 查询返回一个值(一行一列)
	 * @param sql		要查询的sql
	 * @param params	要查询sql的参数
	 * @return			查询结果
	 */
	public Object findValue(String sql, Object[] params) {
		return executeQueryTemplate(sql, params, null, new CallBack() {
			@Override
			public Object doExecute(Connection conn, PreparedStatement ps, ResultSet rs) {
				Object value = null;
				try {
					while(rs.next()){
						value = rs.getObject(1);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return value;
			}
		});
	}
	/**
	 * 查询返回一个数字(一行一列)
	 * @param sql		要查询的sql
	 * @param params	要查询sql的参数
	 * @return			查询结果
	 */
	public Number findNumber(String sql, Object[] params) {
		return (Number)findValue(sql, params);
	}
	/**
	 * 分页查询
	 * @param pageNum	第几页
	 * @param size		每页多少条
	 * @return 返回分页查询结果
	 */
	public abstract Object findPagenate(int pageNum,int size);
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
}
