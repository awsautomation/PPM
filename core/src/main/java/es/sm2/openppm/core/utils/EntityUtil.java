/*
 * Copyright (C) 2009-2015 SM2 SOFTWARE & SERVICES MANAGEMENT
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program has been created in the hope that it will be useful.
 * It is distributed WITHOUT ANY WARRANTY of any Kind,
 * without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see http://www.gnu.org/licenses/.
 *
 * For more information, please contact SM2 Software & Services Management.
 * Mail: info@talaia-openppm.com
 * Web: http://www.talaia-openppm.com
 *
 * Module: core
 * File: EntityUtil.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:58
 */

package es.sm2.openppm.core.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class EntityUtil {
	
	private boolean removeLocal;
	private boolean override;
	private boolean recursive;
	
	public <S, T> T copyEntity(S source, T target) {
		
		Field[] fields = source.getClass().getDeclaredFields();
		
		for (Field field : fields) {
			
			String nameField = null;
			
			if (isRemoveLocal() && field.getName().contains("local")) {
				
				nameField = field.getName().substring(5, field.getName().length());
				nameField = String.valueOf(nameField.charAt(0)).toLowerCase() + nameField.substring(1);
			}
			else { nameField = field.getName(); }
			
			try {
				if (!"serialVersionUID".equals(nameField) && target.getClass().getDeclaredField(nameField) != null) {
					
					Object value = getFieldValue(source, nameField);
					
					
					if (isOverride()) {
						setFieldValue(target, nameField, value, field.getType());
					}
					else {
						Object targetValue = getFieldValue(target, nameField);
						
						if (targetValue == null) {
							setFieldValue(target, nameField, value, field.getType());
						}
					}
				}
			} catch (Exception e) { }
			
		}
		
		return target;
	}
	
	public Object getFieldValue(Object source, String field) {
		
		Object value = null; 
		
		try {
			Method getter = source.getClass().getMethod("get" +
                String.valueOf(field.charAt(0)).toUpperCase() +
                field.substring(1));

			value = getter.invoke(source, new Object[0]);
		} catch (Exception e) { }
		return value;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setFieldValue(Object target, String field, Object arg, Class<?> typeArg) {
		
		try {
			Method setter = target.getClass().getMethod("set" +
                String.valueOf(field.charAt(0)).toUpperCase() +
                field.substring(1), typeArg);
			
			if (isRecursive() && arg instanceof List) {
				
				List list = new ArrayList();
				
				for (Object item : ((List)arg)) {
					
					Object newItem = createContents(item.getClass());
					
					newItem = copyEntity(item, newItem);
					
					list.add(newItem);
				}
				
				setter.invoke(target, list);
			}
			else {
				
				setter.invoke(target, arg);
			}
			
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	private <R> R createContents(Class<R> typeArg) throws InstantiationException, IllegalAccessException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException {
		
        return typeArg.getConstructor().newInstance();
    }

	/**
	 * @return the removeLocal
	 */
	public boolean isRemoveLocal() {
		return removeLocal;
	}

	/**
	 * @param removeLocal the removeLocal to set
	 */
	public void setRemoveLocal(boolean removeLocal) {
		this.removeLocal = removeLocal;
	}

	/**
	 * @return the override
	 */
	public boolean isOverride() {
		return override;
	}

	/**
	 * @param override the override to set
	 */
	public void setOverride(boolean override) {
		this.override = override;
	}

	/**
	 * @return the recursive
	 */
	public boolean isRecursive() {
		return recursive;
	}

	/**
	 * @param recursive the recursive to set
	 */
	public void setRecursive(boolean recursive) {
		this.recursive = recursive;
	}
}
