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
 * ��תJavaԴ�����ļ�����
 * 
 * @author ף����
 * 
 */
public class JavaFileUtils {

	/**
	 * �����ֶ���Ϣ����Java������Ϣ���磺 </br> &nbsp;&nbsp;varchar userName-->private String
	 * userName;</br> ����Ӧ��get����: </br>&nbsp;&nbsp;public String getUserName(){
	 * </br>&nbsp;&nbsp;&nbsp;&nbsp;return userName;</br>&nbsp;&nbsp;}</br>
	 * ����Ӧ��set����:</br> &nbsp;&nbsp;public void setUserName(String
	 * userName){</br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;this.userName=userName;</br>&nbsp;&nbsp;}
	 * 
	 * @param columnInfo
	 *            �ֶ���Ϣ
	 * @param convertor
	 *            ����ת����
	 * @return Java���Ժ�set��get����
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

		// ����package���
		src.append("package " + DBManager.getConfiguration().getPoPackage()
				+ ";\n\n");

		// ����import���
		src.append("import java.sql.*;\n");
		src.append("import java.util.*;\n\n");

		// �������������
		src.append("public class "
				+ StringUtils.firstCharToUpperCase(tableInfo.gettName())
				+ "{\n\n");

		// ���������б�
		for (JavaFieldGetSet jfGetSet : javaFields) {
			src.append(jfGetSet.getFieldInfo());
		}
		src.append("\n\n");

		// ����get����
		for (JavaFieldGetSet jfGetSet : javaFields) {
			src.append(jfGetSet.getGetInfo());
		}

		// ����set����
		for (JavaFieldGetSet jfGetSet : javaFields) {
			src.append(jfGetSet.getSetInfo());
		}

		// ��������
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
		// System.out.println("�ļ�·����"+file.getAbsolutePath());

		if (!file.exists()) { // ���ָ��·�������ڣ�������û�����
			file.mkdirs();
		}
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile() + "/"
					+ StringUtils.firstCharToUpperCase(tableInfo.gettName())
					+ ".java"));
			bw.write(src);
			System.out.println("������"
					+ tableInfo.gettName()
					+ "��Ӧ��Java��"
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
