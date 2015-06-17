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
 * File: ContactDAO.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:44
 */

package es.sm2.openppm.core.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Contact;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Performingorg;
import es.sm2.openppm.core.model.impl.Resourceprofiles;
import es.sm2.openppm.core.model.impl.Security;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;

public class ContactDAO extends AbstractGenericHibernateDAO<Contact, Integer> {

	public ContactDAO(Session session) {
		super(session);
	}
	
	/**
	 * Return contact with company
	 * @param contact
	 * @return
	 */
	public Contact findByIdContact(Contact contact) {
		Contact cont = null;
		if (contact.getIdContact() != null) {
			Criteria crit = getSession().createCriteria(getPersistentClass());
			crit.setFetchMode("company", FetchMode.JOIN);
			crit.add(Restrictions.eq("idContact", contact.getIdContact()));
			cont = (Contact) crit.uniqueResult();
		}
		return cont;
	}

	
	/**
	 * Search Contacts by filter
	 * @param fullName
	 * @param fileAs
	 * @param performingorg
	 * @param company 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Contact> searchByFilter(String fullName, String fileAs,
			Performingorg performingorg, Company company) {
		
		Contact example = new Contact();
		example.setFileAs(fileAs);
		example.setFullName(fullName);
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
			.add(Example.create(example)
					.ignoreCase()
					.enableLike(MatchMode.ANYWHERE)
				)
			.add(Restrictions.or(
				Restrictions.isNull(Contact.DISABLE),
				Restrictions.ne(Contact.DISABLE, true))
			);
		
		crit.createCriteria(Contact.COMPANY)
			.add(Restrictions.idEq(company.getIdCompany()))
			.add(Restrictions.or(
					Restrictions.isNull(Company.DISABLE),
					Restrictions.ne(Company.DISABLE, true))
				);
		
		if (performingorg.getIdPerfOrg() != -1) {
			crit.createCriteria(Contact.EMPLOYEES)
				.createCriteria(Employee.PERFORMINGORG)
					.add(Restrictions.idEq(performingorg.getIdPerfOrg()));
		}
		
		return crit.list();
	}

	/**
	 * Find by login user name
	 * @param remoteUser
	 * @return
	 */
	public Contact findByUser(String remoteUser) {
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.createCriteria(Contact.SECURITIES)
				.add(Restrictions.eq(Security.LOGIN, remoteUser));
		
		return (Contact)crit.uniqueResult();
	}
	
	/**
	 * These contact has profile in company
	 * @param company
	 * @param contact
	 * @param profile
	 * @return
	 */
	public boolean hasProfile (Performingorg perfOrg, Contact contact, Resourceprofiles profile) {
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setProjection(Projections.rowCount())
			.add(Restrictions.idEq(contact.getIdContact()));
			
		crit.createCriteria(Contact.EMPLOYEES)
			.add(Restrictions.eq(Employee.RESOURCEPROFILES, profile))
			.add(Restrictions.eq(Employee.PERFORMINGORG, perfOrg));	
		
		return ((Integer)crit.uniqueResult() > 0);
	}
}
