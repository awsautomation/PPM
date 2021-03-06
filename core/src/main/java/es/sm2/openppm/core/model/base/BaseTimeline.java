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
 * File: BaseTimeline.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Timeline;


import java.util.Date;

 /**
 * Base Pojo object for domain model class Timeline
 * @see BaseTimeline
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Timeline
 */
public class BaseTimeline  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "timeline";
	
	public static final String IDTIMELINE = "idTimeline";
	public static final String PROJECT = "project";
	public static final String TIMELINEDATE = "timelineDate";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
    public static final String IMPORTANCE = "importance";

     private Integer idTimeline;
     private Project project;
     private Date timelineDate;
     private String name;
     private String description;
     private String importance;

    public BaseTimeline() {
    }
    
    public BaseTimeline(Integer idTimeline) {
    	this.idTimeline = idTimeline;
    }
   
    public Integer getIdTimeline() {
        return this.idTimeline;
    }
    
    public void setIdTimeline(Integer idTimeline) {
        this.idTimeline = idTimeline;
    }
    public Project getProject() {
        return this.project;
    }
    
    public void setProject(Project project) {
        this.project = project;
    }
    public Date getTimelineDate() {
        return this.timelineDate;
    }
    
    public void setTimelineDate(Date timelineDate) {
        this.timelineDate = timelineDate;
    }
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

     public String getImportance() {
         return importance;
     }

     public void setImportance(String importance) {
         this.importance = importance;
     }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Timeline) )  { result = false; }
		 else if (other != null) {
		 	Timeline castOther = (Timeline) other;
			if (castOther.getIdTimeline().equals(this.getIdTimeline())) { result = true; }
         }
		 return result;
   }



 }


