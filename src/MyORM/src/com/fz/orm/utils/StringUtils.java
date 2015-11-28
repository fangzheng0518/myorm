package com.fz.orm.utils;

/**
 * 封装了String类的常用操作
 * <br><br><strong>时间:</strong><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;2015年10月29日 上午10:42:57<br>
 * @author FZ
 * @version 1.0
 */
public class StringUtils {
	/**
	 * 将目标字符串的首字母大写
	 * @param str 目标字符串
	 * @return 首字母大写后的字符串
	 */
	public static String firstChar2UpperCase(String str){
		return str.toUpperCase().substring(0,1)+str.substring(1);
	}
}
