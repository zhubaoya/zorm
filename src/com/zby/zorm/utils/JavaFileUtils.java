package com.zby.zorm.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zby.zorm.bean.ColumnInfo;
import com.zby.zorm.bean.JavaFieldGetSet;
import com.zby.zorm.bean.TableInfo;
import com.zby.zorm.core.DBManager;
import com.zby.zorm.core.TableContext;
import com.zby.zorm.core.TypeConvertor;

/**
 * 疯转Java源代码文件操作
 * 
 * @author 祝宝亚
 * 
 */
public class JavaFileUtils {

	/**
	 * 根据字段信息生成Java属性信息，如： </br> &nbsp;&nbsp;varchar userName-->private String
	 * userName;</br> 及相应的get方法: </br>&nbsp;&nbsp;public String getUserName(){
	 * </br>&nbsp;&nbsp;&nbsp;&nbsp;return userName;</br>&nbsp;&nbsp;}</br>
	 * 及相应的set方法:</br> &nbsp;&nbsp;public void setUserName(String
	 * userName){</br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;this.userName=userName;</br>&nbsp;&nbsp;}
	 * 
	 * @param columnInfo
	 *            字段信息
	 * @param convertor
	 *            类型转化器
	 * @return Java属性和set、get方法
	 */
	public static JavaFieldGetSet createFieldGetSetSRC(ColumnInfo columnInfo,
			TypeConvertor convertor) {
		JavaFieldGetSet fieldGetSet = new JavaFieldGetSet();
		String javaFieldType = convertor.dbTypeConvertJavaType(columnInfo
				.getDataType());

		fieldGetSet.setFieldInfo("\tprivate " + javaFieldType + " "
				+ columnInfo.getName() + ";\n");

		// public String getUserName(){return userName;}
		StringBuilder getSrc = new StringBuilder();
		getSrc.append("\tpublic " + javaFieldType + " get"
				+ StringUtils.firstCharToUpperCase(columnInfo.getName())
				+ "(){\n");
		getSrc.append("\t\treturn " + columnInfo.getName() + ";\n");
		getSrc.append("\t}\n\n");
		fieldGetSet.setGetInfo(getSrc.toString());

		// public void setUserName(String userName){this.userName=userName;}
		StringBuilder setSrc = new StringBuilder();
		setSrc.append("\tpublic void set"
				+ StringUtils.firstCharToUpperCase(columnInfo.getName()) + "(");
		setSrc.append(javaFieldType + " " + columnInfo.getName() + "){\n");
		setSrc.append("\t\tthis." + columnInfo.getName() + "="
				+ columnInfo.getName() + ";\n");
		setSrc.append("\t}\n\n");
		fieldGetSet.setSetInfo(setSrc.toString());

		return fieldGetSet;
	}

	public static String createJavaSrc(TableInfo tableInfo,
			TypeConvertor typeConvertor) {
		StringBuilder src = new StringBuilder();
		Map<String, ColumnInfo> columns = tableInfo.getColums();
		List<JavaFieldGetSet> javaFields = new ArrayList<JavaFieldGetSet>();

		for (ColumnInfo ci : columns.values()) {
			javaFields.add(createFieldGetSetSRC(ci, typeConvertor));
		}

		// 生成package语句
		src.append("package " + DBManager.getConfiguration().getPoPackage()
				+ ";\n\n");

		// 生成import语句
		src.append("import java.sql.*;\n");
		src.append("import java.util.*;\n\n");

		// 生成类声明语句
		src.append("public class "
				+ StringUtils.firstCharToUpperCase(tableInfo.gettName())
				+ "{\n\n");

		// 生成属性列表
		for (JavaFieldGetSet jfGetSet : javaFields) {
			src.append(jfGetSet.getFieldInfo());
		}
		src.append("\n\n");

		// 生成get方法
		for (JavaFieldGetSet jfGetSet : javaFields) {
			src.append(jfGetSet.getGetInfo());
		}

		// 生成set方法
		for (JavaFieldGetSet jfGetSet : javaFields) {
			src.append(jfGetSet.getSetInfo());
		}

		// 生成类结果
		src.append("}\n");
		// System.out.println(src);
		return src.toString();
	}

	public static void createJavaPOFile(TableInfo tableInfo,
			TypeConvertor typeConvertor) {
		String src = createJavaSrc(tableInfo, typeConvertor);

		String srcPath = DBManager.getConfiguration().getSrcPath() + "\\";
		String packagePath = DBManager.getConfiguration().getPoPackage()
				.replaceAll("\\.", "\\\\");
		File file = new File(srcPath + packagePath);
		// System.out.println("文件路径："+file.getAbsolutePath());

		if (!file.exists()) { // 如果指定路径不存在，则帮助用户建立
			file.mkdirs();
		}
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile() + "/"
					+ StringUtils.firstCharToUpperCase(tableInfo.gettName())
					+ ".java"));
			bw.write(src);
			System.out.println("建立表"
					+ tableInfo.gettName()
					+ "对应的Java类"
					+ StringUtils.firstCharToUpperCase(tableInfo.gettName()
							+ ".java"));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
