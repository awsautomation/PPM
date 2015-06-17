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
 * File: ProjectFollowupLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.utils.Info;
import es.sm2.openppm.utils.StringPool;
import org.hibernate.Session;
import org.hibernate.Transaction;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.dao.ProjectDAO;
import es.sm2.openppm.core.dao.ProjectFollowupDAO;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectfollowup;
import es.sm2.openppm.utils.DateUtil;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;

public class ProjectFollowupLogic extends AbstractGenericLogic<Projectfollowup, Integer> {

    private Map<String, String> settings;

    public ProjectFollowupLogic(Map<String, String> settings, ResourceBundle bundle) {
        super(bundle);
        this.settings = settings;
    }

    public ProjectFollowupLogic() {

    }
		
	/**
	 * Return Project followup
	 * @param idFollowup
	 * @return
	 * @throws Exception 
	 */
	public Projectfollowup consFollowup (Integer idFollowup) throws Exception {
		
		Projectfollowup followup = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			ProjectFollowupDAO followupDAO = new ProjectFollowupDAO(session);
			if (idFollowup != -1) {
				followup = followupDAO.findById(idFollowup, false);
				followup.getProject();
			} 
			tx.commit();
		}
		catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
		finally {
			SessionFactoryUtil.getInstance().close();
		}
		return followup;
	}	
	
	
	/**
	 * Find last followup with general flag
	 * @param project
	 * @return
	 * @throws Exception
	 */
	public Projectfollowup findLastByProject(Project project) throws Exception {
		
		Projectfollowup followup = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			ProjectFollowupDAO followupDAO = new ProjectFollowupDAO(session);
			followup = followupDAO.findLastByProject(project);
			
			tx.commit();
		}
		catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
		finally {
			SessionFactoryUtil.getInstance().close();
		}
		return followup;
	}	
	
	
	/**
	 * Find last followup
	 * @param project
	 * @return
	 * @throws Exception
	 */
	public Projectfollowup findLast(Project project) throws Exception {
		
		Projectfollowup followup = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			ProjectFollowupDAO followupDAO = new ProjectFollowupDAO(session);
			followup = followupDAO.findLast(project);
			
			tx.commit();
		}
		catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
		finally {
			SessionFactoryUtil.getInstance().close();
		}
		return followup;
	}
	
	
	/**
	 * Find last followup with AC
	 * @param project
	 * @return
	 * @throws Exception
	 */
	public Projectfollowup findLastByProjectWithAC(Project project) throws Exception {
		
		Projectfollowup followup = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			ProjectFollowupDAO followupDAO = new ProjectFollowupDAO(session);
			followup = followupDAO.findLastByProjectWithAC(project);
			
			tx.commit();
		}
		catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
		finally {
			SessionFactoryUtil.getInstance().close();
		}
		return followup;
	}	
	
	
	/**
	 * Find last followup
	 * @param project
	 * @return
	 * @throws Exception
	 */
	public Projectfollowup findLastWithDataByProject(Project project) throws Exception {
		
		Projectfollowup followup = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			ProjectFollowupDAO followupDAO = new ProjectFollowupDAO(session);
			followup = followupDAO.findLastWithDataByProject(project);
			
			tx.commit();
		}
		catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
		finally {
			SessionFactoryUtil.getInstance().close();
		}
		return followup;
	}
	
	
	/**
	 * List of project followups
	 * @param project
	 * @return
	 * @throws Exception 
	 */
	public List<Projectfollowup> consFollowups (Project project) throws Exception {
		
		List<Projectfollowup> followups = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			ProjectFollowupDAO followupDAO = new ProjectFollowupDAO(session);
			followups = followupDAO.findByProject(project, Constants.ASCENDENT);
			
			tx.commit();
		}
		catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
		finally {
			SessionFactoryUtil.getInstance().close();
		}
		return followups;
	}
	

	/**
	 * @param projectfollowup
	 * @return
	 * @throws Exception
	 */
	public Projectfollowup consFollowupWithProject(Projectfollowup projectfollowup) throws Exception {
		
		Projectfollowup followup = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			ProjectFollowupDAO followupDAO = new ProjectFollowupDAO(session);
			followup = followupDAO.consFollowupWithProject(projectfollowup);
			
			tx.commit();
		}
		catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
		finally {
			SessionFactoryUtil.getInstance().close();
		}
		return followup;
	}

	
	/**
	 * Find last followup of month
	 * @param project
	 * @param since
	 * @return
	 * @throws Exception 
	 */
	public Projectfollowup findLasInMonth(Project project, Date since) throws Exception {
		
		Projectfollowup followup = null;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			
			ProjectFollowupDAO followupDAO = new ProjectFollowupDAO(session);
			
			followup = followupDAO.findLasInMonth(
                    project,
                    DateUtil.getFirstMonthDay(since),
                    DateUtil.getLastMonthDay(since));
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return followup;
	}

	/**
	 * Find followup before date
	 * @param project
	 * @param date
	 * @param propertyOrder
	 * @param typeOrder
	 * @param joins
	 * @return
	 * @throws Exception 
	 */
	public List<Projectfollowup> findByProject(Project project, Date date, String propertyOrder, String typeOrder, List<String> joins) throws Exception {
		
		List<Projectfollowup> followups = null;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			
			ProjectFollowupDAO followupDAO = new ProjectFollowupDAO(session);
			
			followups = followupDAO.findByProject(project, date, propertyOrder, typeOrder, joins);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return followups;
		
	}

	
	/**
	 * Update RAG project
	 * @param project
	 * @throws Exception
	 */
	public void updateRAG(Project project) throws Exception {
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			ProjectFollowupDAO followupDAO	= new ProjectFollowupDAO(session);
			ProjectDAO projectDAO			= new ProjectDAO(session);
			
			Projectfollowup followup = followupDAO.findLastByProject(project);
			
			project.setRag(followup == null?null:followup.getGeneralFlag());
			
			projectDAO.makePersistent(project);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
	}

/**
 * followup returns before the date
 * @param project 
 * @param date
 * @return
 * @throws Exception
 */
	public Projectfollowup beforeFollowup(Project project, Date date) throws Exception {

		Projectfollowup followup = null;
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			ProjectFollowupDAO followupDAO = new ProjectFollowupDAO(session);
			
			followup = followupDAO.beforeFollowup(project, date);			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
			
		return followup;
	}

/**
 * followup returns after the date
 * @param project 
 * @param date
 * @return
 * @throws Exception
 */
	public Projectfollowup afterFollowup(Project project, Date date) throws Exception {
		
		Projectfollowup followup = null;
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			ProjectFollowupDAO followupDAO = new ProjectFollowupDAO(session);
			
			followup = followupDAO.afterFollowup(project, date);			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return followup;
	}

/**
 * 
 * @param project
 * @param followupdate
 * @param date
 * @return
 * @throws Exception
 */
	public Projectfollowup findByDate(Integer id,Project project, String followupdate, Date date) throws Exception {
		
		Projectfollowup followup = null;
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			ProjectFollowupDAO followupDAO = new ProjectFollowupDAO(session);
			
			followup = followupDAO.findBydDate(id, project, followupdate, date);			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return followup;
	}

    /**
     * Find follow ups for notify
     * @return
     * @throws Exception
     */
    public List<Projectfollowup> findForNotify() throws Exception {

        List<Projectfollowup> projectfollowups = null;

        Transaction tx = null;
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        try {
            tx = session.beginTransaction();

            ProjectFollowupDAO projectFollowupDAO = new ProjectFollowupDAO(session);
            projectfollowups = projectFollowupDAO.findForNotify();

            tx.commit();
        }
        catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
        finally { SessionFactoryUtil.getInstance().close(); }

        return projectfollowups;
    }

    /**
     * Save followup and update automatic kpis
     *
     * @param followup
     * @param user
     * @throws Exception
     */
    public List<Info> saveFollowup(Projectfollowup followup, Employee user) throws Exception {

        List<Info> infos = new ArrayList<Info>();

        Transaction tx = null;
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        try {
            tx = session.beginTransaction();

            // Declare DAOs
            ProjectFollowupDAO projectFollowupDAO   = new ProjectFollowupDAO(session);
            ProjectFollowupDAO followupDAO          = new ProjectFollowupDAO(session);

            // Update project followup
            projectFollowupDAO.makePersistent(followup);

            Info infoUpdateFollowup = new Info(StringPool.InfoType.SUCCESS, "msg.info.updated", "followup");

            infos.add(infoUpdateFollowup);


            // Update automatic kpis
            //

            // Select last informed followup
            //
            Calendar calendar = DateUtil.getCalendar();
            calendar.setTime(new Date());
            calendar.add(Calendar.YEAR, +1);

            List<Projectfollowup> followups = followupDAO.findByProject(followup.getProject(), calendar.getTime(), Projectfollowup.FOLLOWUPDATE, Constants.ASCENDENT, null);

            Projectfollowup lastInformedFollowup = null;
            for (Projectfollowup projectfollowup : followups) {

                if ((lastInformedFollowup == null) || (projectfollowup.getEv() != null && projectfollowup.getAc() != null)) {
                    lastInformedFollowup = projectfollowup;
                }
            }

            // Conditions to update kpis
            //
            Calendar followupDate = DateUtil.getCalendar();
            followupDate.setTime(followup.getFollowupDate());

            Calendar lastInformedFollowupDate = DateUtil.getCalendar();
            lastInformedFollowupDate.setTime(lastInformedFollowup.getFollowupDate());

            if (followupDate.equals(lastInformedFollowupDate) || followupDate.after(lastInformedFollowupDate)) {

                ProjectKpiLogic projectKpiLogic = new ProjectKpiLogic(settings, getBundle());

                // Update kpis
                infos.addAll(projectKpiLogic.updateAutomaticKPIs(session, followups, followup, user));
            }

            tx.commit();
        }
        catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
        finally { SessionFactoryUtil.getInstance().close(); }

        return infos;
    }
}