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
 * File: JobcatemployeeDAO.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Jobcategory;
import es.sm2.openppm.core.model.impl.Jobcatemployee;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;



/**
 * DAO object for domain model class Jobcatemployee
 * @see es.sm2.openppm.core.dao.Jobcatemployee
 * @author Hibernate Generator by Javier Hernandez
 */
public class JobcatemployeeDAO extends AbstractGenericHibernateDAO<Jobcatemployee, Integer> {


	public JobcatemployeeDAO(Session session) {
		super(session);
	}

	/**
	 * Find jobs categories by employee
	 * @param employee
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Jobcatemployee> findByEmployee(Employee employee) {
		Criteria crit = getSession().createCriteria(getPersistentClass());
		
		crit.add(Restrictions.eq(Jobcatemployee.EMPLOYEE, employee));
		
		crit.createCriteria(Jobcatemployee.JOBCATEGORY)
			.addOrder(Order.asc(Jobcategory.NAME));
		
		return crit.list();
	}

}

