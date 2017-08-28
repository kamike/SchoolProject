package com.pursuege.schoolproject.utils;

import java.lang.reflect.Field;

public class ReflectUtils {

	public static void setFieldValue(final Object obj, final String fieldName,
                                     final Object value) {
		Field field = getAccessibleField(obj, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("Could not find field ["
					+ fieldName + "] on target [" + obj + "]");
		}

		try {
			field.set(obj, value);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public static Field getAccessibleField(final Object obj,
                                           final String fieldName) {
		if (obj == null) {
			throw new IllegalArgumentException("object can't be null");
		}

		if (fieldName == null || fieldName.length() <= 0) {
			throw new IllegalArgumentException("fieldName can't be blank");
		}

		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				Field field = superClass.getDeclaredField(fieldName);
				field.setAccessible(true);
				return field;
			} catch (NoSuchFieldException e) {
				continue;
			}
		}
		return null;
	}
}
