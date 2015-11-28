package com.fz.orm.utils;

import java.lang.reflect.Method;

/**
 * 封装了反射的常用操作
 * <br><br><strong>时间:</strong><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;2015年10月29日 上午10:42:52<br>
 * @author FZ
 * @version 1.0
 */
@SuppressWarnings("all")
public class ReflectUtils {
	/**
	 * 调用obj对象的fieldName属性的get方法
	 * @param fieldName 要调用的属性名称
	 * @param obj		要执行get方法的对象
	 * @return get方法的返回值
	 */
	public static Object invokeGet(String fieldName,Object obj){
		try {
			Method m = obj.getClass().getMethod("get"+StringUtils.firstChar2UpperCase(fieldName), null);
			return m.invoke(obj, null);//调用get方法后返回的值
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 调用obj对象columnName属性的set方法
	 * @param obj			要调用的对象
	 * @param columnName	要调用对象set方法的属性
	 * @param columnValue	要调用set方法属性的参数值
	 */
	public static void invokeSet(Object obj,String columnName,Object columnValue){
		try {
			Method m = obj.getClass().getDeclaredMethod("set"+StringUtils.firstChar2UpperCase(columnName),
					columnValue!=null?columnValue.getClass():null);
			m.invoke(obj, columnValue);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
