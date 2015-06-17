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
 * File: Incomes.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.impl;

import java.util.Date;

import es.sm2.openppm.core.logic.impl.ProjectLogic;
import es.sm2.openppm.core.model.base.BaseIncomes;
import es.sm2.openppm.utils.functions.ValidateUtil;



/**
 * Model class Incomes.
 * @author Hibernate Generator by Javier Hernandez
 */
public class Incomes extends BaseIncomes {

	private static final long serialVersionUID = 1L;
	private Integer planDaysToDate;
	private Integer actDaysToDate;
	
    public Incomes() {
		super();
    }
    public Incomes(Integer idIncome) {
		super(idIncome);
    }

    public Integer getPlanDaysToDate() throws Exception {
    	
    	if (planDaysToDate == null) {
    		planDaysToDate = calcDaysToDate(getPlannedBillDate()); 
    	}
    	return planDaysToDate;
    }
    
    public Integer getActDaysToDate() throws Exception {
    	
    	if (actDaysToDate == null) {
    		actDaysToDate = calcDaysToDate(getActualBillDate()); 
    	}
    	return actDaysToDate;
    }
    
    private Integer calcDaysToDate(Date date) throws Exception {
    	
    	ProjectLogic projectLogic = new ProjectLogic(null, null);
    	
    	Project project			= projectLogic.findByIncome(this);
    	Projectactivity rootAct = project.getRootActivity();
    	
    	Integer daysToDate = null;
    	
    	if (rootAct.getActualInitDate() != null) {
    		daysToDate = ValidateUtil.calculateWorkDays(rootAct.getActualInitDate(), date);
    	}
    	else if (rootAct.getPlanInitDate() != null) {
    		daysToDate = ValidateUtil.calculateWorkDays(rootAct.getPlanInitDate(), date);
    	}
    	else {
    		daysToDate = ValidateUtil.calculateWorkDays(project.getPlannedInitDate(), date);
    	}
    	
    	return daysToDate;
    }
}

