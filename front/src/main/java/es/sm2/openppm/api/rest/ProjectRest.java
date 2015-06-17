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
 * Module: front
 * File: ProjectRest.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:22
 */

/** -------------------------------------------------------------------------
*
* Desarrollado por SM2 Baleares S.A.
*
* -------------------------------------------------------------------------
* Este fichero solo podra ser copiado, distribuido y utilizado
* en su totalidad o en parte, de acuerdo con los terminos y
* condiciones establecidas en el acuerdo/contrato bajo el que se
* suministra.
* -------------------------------------------------------------------------
*
* Proyecto : OpenPPM
* Autor : Javier Hernandez
* Fecha : Lunes, 12 de Diciembre, 2011
*
* -------------------------------------------------------------------------
*/
package es.sm2.openppm.api.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.logic.impl.ProjectLogic;
import es.sm2.openppm.core.model.impl.Category;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Contracttype;
import es.sm2.openppm.core.model.impl.Customer;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Fundingsource;
import es.sm2.openppm.core.model.impl.Geography;
import es.sm2.openppm.core.model.impl.Performingorg;
import es.sm2.openppm.core.model.impl.Program;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectcalendar;
import es.sm2.openppm.front.utils.SecurityUtil;
import es.sm2.openppm.utils.exceptions.LogicException;
import es.sm2.openppm.utils.json.Exclusion;
import es.sm2.openppm.utils.json.JsonUtil;

public class ProjectRest {

	private static final Logger LOGGER = Logger.getLogger(ProjectRest.class);
	
	@Context
	private UriInfo uriInfo;
	@Context
	private Request request;
	private Integer id;
	
	public ProjectRest(UriInfo uriInfo, Request request, Integer id) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = id;
	}
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Project getById(
			@QueryParam("token") String token) throws Exception {
		
		LOGGER.debug("REST API CALL: getById");
		
		ProjectLogic projectLogic = new ProjectLogic(null, null);
		
		Employee user = SecurityUtil.consUserByToken(token);
		
		if (!projectLogic.hasPermission(new Project(id), user, Constants.TAB_INITIATION)) {
			throw new LogicException("You are not permission to see the project");
		}
		
		List<String> joins = new ArrayList<String>();
		joins.add(Project.EMPLOYEEBYSPONSOR);
		joins.add(Project.EMPLOYEEBYSPONSOR+"."+Employee.CONTACT);
		joins.add(Project.EMPLOYEEBYINVESTMENTMANAGER);
		joins.add(Project.EMPLOYEEBYINVESTMENTMANAGER+"."+Employee.CONTACT);
		joins.add(Project.EMPLOYEEBYFUNCTIONALMANAGER);
		joins.add(Project.EMPLOYEEBYFUNCTIONALMANAGER+"."+Employee.CONTACT);
		joins.add(Project.EMPLOYEEBYPROJECTMANAGER);
		joins.add(Project.EMPLOYEEBYPROJECTMANAGER+"."+Employee.CONTACT);
		
		Project project = projectLogic.findById(Integer.valueOf(id), joins);
		
		List<String> nEx = new ArrayList<String>();
		// TODO resourcepool
//		nEx.add(Employee.EMPLOYEE);
//		nEx.add(Employee.EMPLOYEES);
		
		Project item = (Project) JsonUtil.removeLazy(project, Project.class,
				new Exclusion(Employee.class, nEx),
				new Exclusion(Company.class),
				new Exclusion(Program.class),
				new Exclusion(Fundingsource.class),
				new Exclusion(Geography.class),
				new Exclusion(Performingorg.class),
				new Exclusion(Projectcalendar.class),
				new Exclusion(Customer.class),
				new Exclusion(Contracttype.class),
				new Exclusion(Category.class));
		
		if (item == null) throw new RuntimeException("Get: Project with " + id +  " not found");
		
		return item;
	}
	
}