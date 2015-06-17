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
 * File: ConfigurationLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import es.sm2.openppm.core.dao.ConfigurationDAO;
import es.sm2.openppm.core.exceptions.NoDataFoundException;
import es.sm2.openppm.core.model.impl.Configuration;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;



/**
 * Logic object for domain model class Configuration
 * @see es.sm2.openppm.logic.Configuration
 * @author Hibernate Generator by Javier Hernandez
 *
 * IMPORTANT! Instantiate the class for use generic methods
 *
 */
public final class ConfigurationLogic extends AbstractGenericLogic<Configuration, Integer> {

	/**
	 * Find configurations by types
	 * 
	 * @param types
	 * @return
	 * @throws Exception 
	 */
	public HashMap<String, String> findByTypes(Employee user, String...types) throws Exception {
		
		if (types == null) {
			throw new NoDataFoundException();
		}
		
		HashMap<String, String> configurations = new HashMap<String, String>();
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			ConfigurationDAO configurationDAO = new ConfigurationDAO(session);
			
			List<Configuration> confs = configurationDAO.findByTypes(user, types);
			
			for (Configuration item : confs) {
			
				if (ValidateUtil.isNotNull(item.getValue())) {
					
					configurations.put(item.getName(), item.getValue());
				}
			}
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return configurations;
	}

	/**
	 * Save or update Configuration
	 *
	 * @param user
	 * @param type
	 * @param name
	 * @param value
	 * @throws Exception
	 */
	public void saveConfiguration(Employee user, String type, String name, String value)
			throws Exception {

		// Create configuration for call generic method
		HashMap<String, String> configuration = new HashMap<String, String>();
		configuration.put(name, value);

		// Save or update configuration
		saveConfigurations(user, configuration, type);
	}
	
	/**
	 * Save or update configurations
	 * 
	 * @param user
	 * @param configurations
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, String> saveConfigurations(Employee user, HashMap<String, String> configurations, String type) throws Exception {
		
		if (type == null
				|| configurations == null
				|| user == null) {
			throw new NoDataFoundException();
		}
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			ConfigurationDAO configurationDAO = new ConfigurationDAO(session);
			
			for (String name : configurations.keySet()) {
				
				// Find if exist
				Configuration configuration = configurationDAO.findByName(user, name, type);
				
				// If not exist initialize configuration
				if (configuration == null) {
					
					configuration = new Configuration();
					configuration.setContact(user.getContact());
					configuration.setName(name);
					configuration.setType(type);
				}
				
				// Set new value
				configuration.setValue(configurations.get(name));
				
				// Save configuration
				configurationDAO.makePersistent(configuration);
			}
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return configurations;
	}
}

