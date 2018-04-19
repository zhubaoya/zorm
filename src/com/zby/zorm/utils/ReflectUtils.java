package com.zby.zorm.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * ��װ���÷������
 * 
 * @author ף����
 * 
 */
@SuppressWarnings(value = { "all" })
public class ReflectUtils {

	/**
	 * ����Object�����Ӧ������fieldName��get����
	 * 
	 * @param FieldName
	 * @param obj
	 * @return
	 */
	public static Object InvokeGet(String FieldName, Object obj) {
		// ͨ��������ƣ��������Զ�Ӧ��get����
		try {
			Class clazz = obj.getClass();
			Method method = clazz.getDeclaredMethod(
					"get" + StringUtils.firstCharToUpperCase(FieldName), null); // get����������getId
			return method.invoke(obj, null);  
			/*public Object invoke(Object obj,
                     Object... args)
              throws IllegalAccessException,
                     IllegalArgumentException,
                     InvocationTargetException
			obj - ���е��õײ㷽���Ķ��󣬱��� Emp���� emp��Student����stu
			args - ���ڸ÷������õĲ���     �������emp��getName��������getName������û���βΣ�����agrsΪnull*/
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
		return obj;
	}

	/**
	 * ͨ��������ƣ��������Զ�Ӧ��set����
	 * @param obj
	 * @param columnName
	 * @param columnValue
	 */
	public static void invokeSet(Object obj, String columnName,
			Object columnValue) {
		Class clazz = obj.getClass();
		try {
			Method method = clazz.getDeclaredMethod(
					"set" + StringUtils.firstCharToUpperCase(columnName),
					columnValue.getClass());
			method.invoke(obj, columnValue);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
