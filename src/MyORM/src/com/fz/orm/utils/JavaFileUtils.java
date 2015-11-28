package com.fz.orm.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fz.orm.bean.ColumnInfo;
import com.fz.orm.bean.JavaFieldGetSet;
import com.fz.orm.bean.TableInfo;
import com.fz.orm.core.DBManager;
import com.fz.orm.core.TypeConvertor;

/**
 * 封装了生成java源文件的常用操作
 * <br><br><strong>时间:</strong><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;2015年10月29日 上午10:42:42<br>
 * @author FZ
 * @version 1.0
 */
public class JavaFileUtils {
	/**
	 * 将数据库中信息生成java的属性和get，set方法。
	 * 如：varchar username-->private String username;
	 *    public String getUsername(){return username;}
	 *    public void   setUsername(String username){this.username = username}
	 * @param columnInfo 列信息
	 * @param convertor  类型转换器
	 * @return 返回JavaFieldGetSet对象
	 */
	public static JavaFieldGetSet createJavaFieldGetSetSRC(ColumnInfo columnInfo,TypeConvertor convertor){
		JavaFieldGetSet jfgs = new JavaFieldGetSet();
		//拼接属性
		String javaType = convertor.databaseType2JavaType(columnInfo.getDataType());
		jfgs.setFieldInfo("\tprivate "+javaType+" "+columnInfo.getName()+";\n");
		//拼接get方法:public String getUsername(){return username;}
		StringBuilder getInfo = new StringBuilder();
		getInfo.append("\tpublic "+javaType+" get"+StringUtils.firstChar2UpperCase(columnInfo.getName())+"(){\n");
		getInfo.append("\t\treturn "+columnInfo.getName()+";\n");
		getInfo.append("\t}\n");
		jfgs.setGetInfo(getInfo.toString());
		//拼接set方法：public void setUsername(String username){this.username = username}
		StringBuilder setInfo = new StringBuilder();
		setInfo.append("\tpublic void set"+StringUtils.firstChar2UpperCase(columnInfo.getName())+"(");
		setInfo.append(javaType+" "+columnInfo.getName()+"){\n");
		setInfo.append("\t\tthis."+columnInfo.getName()+"="+columnInfo.getName()+";\n");
		setInfo.append("\t}\n");
		jfgs.setSetInfo(setInfo.toString());
		
		return jfgs;
	}
	
	/**
	 * 根据数据库表信息生成java类源码
	 * @param tableInfo 数据库表信息
	 * @param convertor 类型转换器
	 * @return java源码
	 */
	public static String createJavaSrc(TableInfo tableInfo,TypeConvertor convertor){
		//处理所有属性和get、set信息
		Map<String,ColumnInfo> columns = tableInfo.getColumns();
		List<JavaFieldGetSet> javaFields = new ArrayList<JavaFieldGetSet>();
		for (ColumnInfo c : columns.values()) {
			javaFields.add(createJavaFieldGetSetSRC(c,convertor));
		}
		
		StringBuilder javaSrc = new StringBuilder();
		//生成package语句
		javaSrc.append("package "+DBManager.getConf().getPoPackage()+";\n\n");
		//生成import语句
		javaSrc.append("import java.sql.*;\n");
		javaSrc.append("import java.util.*;\n\n");
		//生成声明类语句
		javaSrc.append("public class "+StringUtils.firstChar2UpperCase(tableInfo.getTname())+" {\n");
		//生成属性
		for (JavaFieldGetSet f : javaFields) {
			javaSrc.append(f.getFieldInfo());
		}
		javaSrc.append("\n\n");
		//生成get语句
		for (JavaFieldGetSet f : javaFields) {
			javaSrc.append(f.getGetInfo());
		}
		//生成set语句
		for (JavaFieldGetSet f : javaFields) {
			javaSrc.append(f.getSetInfo());
		}
		//生成类结束符合
		javaSrc.append("}\n");
		return javaSrc.toString();
	}
	
	/**
	 * 生成具体的java文件
	 * @param tableInfo	表信息
	 * @param convertor	类型转换器
	 */
	public static void createJavaPOFile(TableInfo tableInfo,TypeConvertor convertor){
		String src = createJavaSrc(tableInfo, convertor);
		//拼接java文件存放路径
//		String srcPath = DBManager.getConf().getSrcPath()+"/";//使用此方法src目录拒绝访问
		String srcPath = System.getProperty("user.dir") + "\\src\\";
		String packagePath = DBManager.getConf().getPoPackage().replaceAll("\\.", "\\\\");
		File f = new File(srcPath+packagePath);
		if (!f.exists()) {
			f.mkdirs();
		}
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(f.getAbsoluteFile()+"/"+StringUtils.firstChar2UpperCase(tableInfo.getTname()+".java")));
			bw.write(src);
			System.out.println(tableInfo.getTname()+"表对应的"+StringUtils.firstChar2UpperCase(tableInfo.getTname()+".java")+"创建成功！");
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				if (bw != null) {
					bw.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
//		JavaFieldGetSet f = JavaFileUtils.createJavaFieldGetSetSRC(new ColumnInfo("id","int",0), new MySqlTypeConvertor());
//		System.out.println(f);
		
		
		
	}
}
