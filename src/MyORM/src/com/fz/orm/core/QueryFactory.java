package com.fz.orm.core;

/**
 * 查询总工厂
 * <br><br><strong>时间:</strong><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;2015年10月29日 上午10:42:16<br>
 * @author FZ
 * @version 1.0
 */
@SuppressWarnings("all")
public class QueryFactory {
	private static QueryFactory queryFactory = new QueryFactory();
	private QueryFactory(){}
	private static Query protopyteObj;//原型对象，用于克隆
	static{
		try {
			Class c = Class.forName(DBManager.getConf().getQueryCalss());//加载所配置的正在使用的查询类
			protopyteObj = (Query) c.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 通过克隆创建Query对象
	 * @return 克隆的新对象
	 */
	public static Query createQuery(){
		try {
			return (Query) protopyteObj.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
}
