package com.zby.zorm.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 封装常用反射操作
 * 
 * @author 祝宝亚
 * 
 */
@SuppressWarnings(value = { "all" })
public class ReflectUtils {

	/**
	 * 调用Object对象对应其属性fieldName的get方法
	 * 
	 * @param FieldName
	 * @param obj
	 * @return
	 */
	public static Object InvokeGet(String FieldName, Object obj) {
		// 通过反射机制，调用属性对应的get方法
		try {
			Class clazz = obj.getClass();
			Method method = clazz.getDeclaredMethod(
					"get" + StringUtils.firstCharToUpperCase(FieldName), null); // get方法，比如getId
			return method.invoke(obj, null);  
			/*public Object invoke(Object obj,
                     Object... args)
              throws IllegalAccessException,
                     IllegalArgumentException,
                     InvocationTargetException
			obj - 从中调用底层方法的对象，比如 Emp对象 emp，Student对象stu
			args - 用于该方法调用的参数     比如调用emp的getName方法，而getName方法的没有形参，所以agrs为null*/
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
	 * 通过反射机制，调用属性对应的set方法
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
