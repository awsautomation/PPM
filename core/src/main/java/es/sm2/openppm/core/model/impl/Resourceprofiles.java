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
 * File: Resourceprofiles.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:57
 */

package es.sm2.openppm.core.model.impl;

import es.sm2.openppm.core.model.base.BaseResourceprofiles;



/**
 * Model class Resourceprofiles.
 * @author Hibernate Generator by Javier Hernandez
 */
public class Resourceprofiles extends BaseResourceprofiles {

	private static final long serialVersionUID = 1L;

    public Resourceprofiles() {
		super();
    }
    public Resourceprofiles(int idProfile) {
		super(idProfile);
    }

    
    public enum Profile {
    	RESOURCE("RESOURCE"),
    	PROJECT_MANAGER("PROJECT_MANAGER"),
    	INVESTMENT_MANAGER("INVESTMENT_MANAGER"),
    	FUNCTIONAL_MANAGER("FUNCTIONAL_MANAGER"),
    	PROGRAM_MANAGER("PROGRAM_MANAGER"),
    	RESOURCE_MANAGER("RESOURCE_MANAGER"),
    	PMO("PMO"),
    	SPONSOR("SPONSOR"),
    	PORFOLIO_MANAGER("PORFOLIO_MANAGER"),
    	ADMIN("ADMIN"),
    	STAKEHOLDER("STAKEHOLDER");
    	
    	private String profileName;
    	
    	private Profile(String profileName) {
    		this.profileName = profileName;
    	}

		/**
		 * @return the mode
		 */
		public String getProfileName() {
			return profileName;
		}

        /**
         * @return the number of profile equals bbdd
         */
        public int getID() {
            return this.ordinal() + 1;
        }
    }
}

