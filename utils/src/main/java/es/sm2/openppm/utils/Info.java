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
 * Module: utils
 * File: Info.java
 * Create User: javier.hernandez
 * Create Date: 06/03/2015 14:35:37
 */

package es.sm2.openppm.utils;

import es.sm2.openppm.utils.StringPool.InfoType;

/**
 * @author jordi.ripoll
 *
 */
public class Info {

	private String type;
	private String key;
	private Object[] args;
	
	public Info(InfoType type, String key) {
		this.type = type.getName();
		this.key = key;
	}
	
	public Info(InfoType type, String key, Object...args) {
		this.type = type.getName();
		this.key = key;
		this.args = args;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return the message
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return the args
	 */
	public Object[] getArgs() {
		return args;
	}
}
