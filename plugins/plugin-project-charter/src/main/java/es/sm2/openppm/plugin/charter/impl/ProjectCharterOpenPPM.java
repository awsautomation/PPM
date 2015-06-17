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
 * Module: plugin-project-charter
 * File: OpenPPMProjectCharter.java
 * Create User: javier.hernandez
 * Create Date: 21/03/2015 10:39:36
 */

package es.sm2.openppm.plugin.charter.impl;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.dao.ChargescostsDAO;
import es.sm2.openppm.core.dao.MilestoneDAO;
import es.sm2.openppm.core.dao.ProjectDAO;
import es.sm2.openppm.core.dao.StakeholderDAO;
import es.sm2.openppm.core.dao.WorkingcostsDAO;
import es.sm2.openppm.core.logic.impl.DocumentprojectLogic;
import es.sm2.openppm.core.model.impl.Chargescosts;
import es.sm2.openppm.core.model.impl.Documentproject;
import es.sm2.openppm.core.model.impl.Milestones;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectcharter;
import es.sm2.openppm.core.model.impl.Stakeholder;
import es.sm2.openppm.core.model.impl.Workingcosts;
import es.sm2.openppm.core.reports.GenerateReportInterface;
import es.sm2.openppm.core.reports.ReportDataSource;
import es.sm2.openppm.core.reports.annotations.ReportConfiguration;
import es.sm2.openppm.core.reports.annotations.ReportType;
import es.sm2.openppm.core.reports.beans.AprobacioReport;
import es.sm2.openppm.core.reports.beans.MilestoneReport;
import es.sm2.openppm.core.reports.beans.NombreReport;
import es.sm2.openppm.core.reports.beans.ProjectReport;
import es.sm2.openppm.core.reports.beans.ReportFile;
import es.sm2.openppm.core.reports.beans.ReportParams;
import es.sm2.openppm.core.reports.beans.StakeholderReport;
import es.sm2.openppm.core.reports.beans.WorkReport;
import es.sm2.openppm.core.reports.exceptions.ReportException;
import es.sm2.openppm.core.utils.DataUtil;
import es.sm2.openppm.utils.DateUtil;
import es.sm2.openppm.utils.DocumentUtils;
import es.sm2.openppm.utils.LogManager;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.StringUtil;
import es.sm2.openppm.utils.functions.HtmlUtil;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by javier.hernandez on 21/03/2015.
 */
@ReportConfiguration(type = ReportType.PROJECT_CHARTER)
public class ProjectCharterOpenPPM implements GenerateReportInterface {

    private static final String PATH_REPORT 			= "/es/sm2/openppm/plugin/charter/jasper/";
    private static final String JASPERFILE				= "ProjectCharterOpenPPM.jasper";
    private static final String LOGOFILE				= "headOpenPPM.jpg";

    @Override
    public ReportFile generate(ReportParams reportParams) throws ReportException {


        LogManager.getLog(this.getClass()).debug("GENERATE PROJECT CHARTER " + this.getClass());

        ReportFile reportFile = null;
        Transaction tx 		= null;
        Session session;

        try {

            session = SessionFactoryUtil.getInstance().getCurrentSession();
            tx = session.beginTransaction();

            final ReportConfiguration configuration = this.getClass().getAnnotation(ReportConfiguration.class);


            ProjectDAO projectDAO			= new ProjectDAO(session);
            MilestoneDAO milestoneDAO		= new MilestoneDAO(session);
            StakeholderDAO stakeholderDAO	= new StakeholderDAO(session);

            Project project = projectDAO.findById(reportParams.getProject().getIdProject());

            // Get project charter from project
            Projectcharter projectCharter = project.getProjectCharter();

            // Datos generales
            ProjectReport projectReport = new ProjectReport();
            projectReport.setCode(ValidateUtil.isNullCh(project.getChartLabel(), StringPool.BLANK));
            projectReport.setProjectName(ValidateUtil.isNullCh(project.getProjectName(), StringPool.BLANK));
            projectReport.setAutor(ValidateUtil.isNullCh(reportParams.getUser().getContact().getFullName(), StringPool.BLANK));
            projectReport.setProgram(ValidateUtil.isNullCh(project.getProgram().getProgramName(), StringPool.BLANK));
            projectReport.setPo(ValidateUtil.isNullCh(project.getPerformingorg().getName(), StringPool.BLANK));
            projectReport.setBudgetYear(project.getBudgetYear() != null ? project.getBudgetYear().toString() : StringPool.BLANK);
            projectReport.setAccountingCode(project.getAccountingCode() != null ? project.getAccountingCode() : StringPool.BLANK);

            projectReport.setDateDocument(DateUtil.format(reportParams.getBundle(), new Date()));

            projectReport.setBusinessNeed(ValidateUtil.isNullCh(projectCharter.getBusinessNeed(), StringPool.BLANK));
            projectReport.setDescripcion(ValidateUtil.isNullCh(projectCharter.getSucessCriteria(), StringPool.BLANK));
            projectReport.setObjetivos(ValidateUtil.isNullCh(projectCharter.getProjectObjectives(), StringPool.BLANK));
            projectReport.setSupuestos(ValidateUtil.isNullCh(projectCharter.getMainAssumptions(), StringPool.BLANK));
            projectReport.setRestricciones(ValidateUtil.isNullCh(projectCharter.getMainConstraints(), StringPool.BLANK));

            projectReport.setTotalValue(ValidateUtil.toCurrency(project.getTcv() == null?0:project.getTcv()));
            projectReport.setNetValue(ValidateUtil.toCurrency(project.getNetIncome() == null? 0:project.getNetIncome()));
            projectReport.setPrincipalesRiesgos(ValidateUtil.isNullCh(projectCharter.getMainRisks(), StringPool.BLANK));
            projectReport.setEntregables(ValidateUtil.isNullCh(projectCharter.getMainDeliverables(), StringPool.BLANK));
            projectReport.setExclusiones(ValidateUtil.isNullCh(projectCharter.getExclusions(), StringPool.BLANK));

            if (project.getCategory() != null) {
                projectReport.setCategory(project.getCategory().getName());
            }
            else { projectReport.setCategory(StringPool.BLANK); }

            if (project.getCustomer() != null) {

                String customer = (project.getCustomer().getCustomertype() != null?project.getCustomer().getCustomertype().getName()+" - ":"");
                projectReport.setCustomer(customer+project.getCustomer().getName());
            }
            else { projectReport.setCustomer(StringPool.BLANK); }

            if (project.getContracttype() != null) {
                projectReport.setContractType(project.getContracttype().getDescription());
            }
            else { projectReport.setContractType(StringPool.BLANK); }

            // Fechas
            String fechas = DateUtil.format(reportParams.getBundle(), project.getPlannedInitDate())+
                    " - "+DateUtil.format(reportParams.getBundle(), project.getPlannedFinishDate());

            projectReport.setFechas(fechas);
            projectReport.setPlannedStartDate(DateUtil.format(reportParams.getBundle(), project.getPlannedInitDate()));
            projectReport.setPlannedFinishDate(DateUtil.format(reportParams.getBundle(), project.getPlannedFinishDate()));
            projectReport.setDuration(project.getDuration() != null ? project.getDuration().toString() : StringPool.BLANK);
            projectReport.setPriority(project.getPriority()+StringPool.BLANK);
            projectReport.setTotalValue(project.getTcv() == null?StringPool.BLANK:ValidateUtil.toCurrency(project.getTcv()));

            // Aprobaciones
            List<AprobacioReport> aprobaciones = new ArrayList<AprobacioReport>();

            // PM
            if (project.getEmployeeByProjectManager() != null) {

                projectReport.setProjectManager(project.getEmployeeByProjectManager().getContact().getFullName());

                AprobacioReport aprobacio = new AprobacioReport();
                aprobacio.setNombre(project.getEmployeeByProjectManager().getContact().getFullName());
                aprobacio.setResponsabilidad(ValidateUtil.isNullCh(project.getEmployeeByProjectManager().getContact().getJobTitle(), StringPool.BLANK));
                aprobacio.setRol(reportParams.getBundle().getString("project_manager"));
                aprobacio.setFecha(StringPool.BLANK);
                aprobacio.setAprobado(StringPool.BLANK);
                aprobaciones.add(aprobacio);
            }
            else { projectReport.setProjectManager(StringPool.BLANK); }

            // FM
            if (project.getEmployeeByFunctionalManager() != null) {

                AprobacioReport aprobacio = new AprobacioReport();
                aprobacio.setNombre(project.getEmployeeByFunctionalManager().getContact().getFullName());
                aprobacio.setResponsabilidad(ValidateUtil.isNullCh(project.getEmployeeByFunctionalManager().getContact().getJobTitle(), StringPool.BLANK));
                aprobacio.setRol(reportParams.getBundle().getString("functional_manager"));
                aprobacio.setFecha(StringPool.BLANK);
                aprobacio.setAprobado(StringPool.BLANK);
                aprobaciones.add(aprobacio);
            }

            // IM
            if (project.getEmployeeByInvestmentManager() != null) {
                projectReport.setInvestmentManager(ValidateUtil.isNullCh(project.getEmployeeByInvestmentManager().getContact().getFullName(), StringPool.BLANK));
            }

            // Sponsor
            if (project.getEmployeeBySponsor() != null) {

                projectReport.setSponsor(project.getEmployeeBySponsor().getContact().getFullName());

                AprobacioReport aprobacio = new AprobacioReport();
                aprobacio.setNombre(project.getEmployeeBySponsor().getContact().getFullName());
                aprobacio.setResponsabilidad(ValidateUtil.isNullCh(project.getEmployeeBySponsor().getContact().getJobTitle(), StringPool.BLANK));
                aprobacio.setRol(reportParams.getBundle().getString("sponsor"));
                aprobacio.setFecha(StringPool.BLANK);
                aprobacio.setAprobado(StringPool.BLANK);
                aprobaciones.add(aprobacio);
            }
            else { projectReport.setSponsor(StringPool.BLANK); }

            projectReport.setAprobaciones(new ReportDataSource<AprobacioReport>(aprobaciones));

            // Milestones
            List<MilestoneReport> milestones = new ArrayList<MilestoneReport>();
            for (Milestones item : milestoneDAO.findByRelation(Milestones.PROJECT, project,Milestones.PLANNED, Constants.ASCENDENT)) {

                milestones.add(new MilestoneReport(
                        ValidateUtil.isNullCh(item.getName(), StringPool.BLANK),
                        DateUtil.format(reportParams.getBundle(), item.getPlanned())));
            }
            projectReport.setMilestones(new ReportDataSource<MilestoneReport>(milestones));

            // Stakeholders
            List<StakeholderReport> stakeholders = new ArrayList<StakeholderReport>();
            for (Stakeholder item : stakeholderDAO.findByRelation(Stakeholder.PROJECT, project,Stakeholder.ORDERTOSHOW, Constants.ASCENDENT)) {

                StakeholderReport stakeholder = new StakeholderReport();
                stakeholder.setRol(ValidateUtil.isNullCh(item.getProjectRole(), StringPool.BLANK));
                stakeholder.setInfluencia(ValidateUtil.isNullCh(item.getInfluence(), StringPool.BLANK));
                stakeholder.setCargo(ValidateUtil.isNullCh(item.getDepartment(), StringPool.BLANK));

                String name = "";

                if (item.getEmployee() == null) {
                    name = ValidateUtil.isNullCh(item.getContactName(), StringPool.BLANK);
                }
                else {
                    name = ValidateUtil.isNullCh(item.getEmployee().getContact().getFullName(), StringPool.BLANK);
                }

                stakeholder.setNombre(name);
                stakeholders.add(stakeholder);
            }
            if (ValidateUtil.isNull(projectReport.getStakeholder())) { projectReport.setStakeholder(StringPool.BLANK); }

            projectReport.setStakeholders(new ReportDataSource<StakeholderReport>(stakeholders));

            // Leads and dependents 
            //
            List<NombreReport> leadsAndDependents = new ArrayList<NombreReport>();

            int sizeLeadsAndDependents = project.getProjectassociationsForDependent().size();

            if ( project.getProjectassociationsForLead().size() > sizeLeadsAndDependents) {
                sizeLeadsAndDependents = project.getProjectassociationsForLead().size();
            }

            // Sellers - externals
            //
            ChargescostsDAO chargescostsDAO = new ChargescostsDAO(session);

            List<Chargescosts> sellersCosts = chargescostsDAO.consChargescostsByProject(project, Constants.SELLER_CHARGE_COST);

            double totalSellersCost = 0;

            List<NombreReport> sellers = new ArrayList<NombreReport>();

            for (Chargescosts sellerCost : sellersCosts) {

                sellers.add(new NombreReport(
                        HtmlUtil.escape(sellerCost.getName()),
                        ValidateUtil.toCurrency(sellerCost.getCost())));

                totalSellersCost += sellerCost.getCost();
            }

            if (ValidateUtil.isNotNull(sellers)) {
                projectReport.setSellers(new ReportDataSource<NombreReport>(sellers));
            }

            projectReport.setSellersCost(ValidateUtil.toCurrency(Double.toString(totalSellersCost)) );

            // Expenses - Coste de infraestructura
            //
            List<Chargescosts> infrastructureCosts = chargescostsDAO.consChargescostsByProject(project, Constants.INFRASTRUCTURE_CHARGE_COST);

            List<NombreReport> infrastructures = new ArrayList<NombreReport>();

            double totalInfraestructureCost = 0;

            for (Chargescosts infraCost : infrastructureCosts) {

                infrastructures.add(new NombreReport(
                        HtmlUtil.escape(infraCost.getName()),
                        ValidateUtil.toCurrency(infraCost.getCost()) ));

                totalInfraestructureCost += infraCost.getCost();
            }

            if (ValidateUtil.isNotNull(infrastructures)) {
                projectReport.setInfrastructures(new ReportDataSource<NombreReport>(infrastructures));
            }

            projectReport.setInfrastructuresCost(ValidateUtil.toCurrency(Double.toString(totalInfraestructureCost)) );

            // Expenses - Licencias. 
            //
            List<Chargescosts> licensesCosts = chargescostsDAO.consChargescostsByProject(project, Constants.LICENSE_CHARGE_COST);

            List<NombreReport> licenses = new ArrayList<NombreReport>();

            double totalLicensesCost = 0;

            for (Chargescosts licenseCost : licensesCosts) {

                totalLicensesCost += licenseCost.getCost();

                licenses.add(new NombreReport(
                        HtmlUtil.escape(licenseCost.getName()),
                        ValidateUtil.toCurrency(licenseCost.getCost()) ));
            }

            if (ValidateUtil.isNotNull(licenses)) {
                projectReport.setLicenses(new ReportDataSource<NombreReport>(licenses));
            }
            projectReport.setLicensesCost(ValidateUtil.toCurrency(Double.toString(totalLicensesCost)) );


            double expenses = totalSellersCost + totalInfraestructureCost + totalLicensesCost;

            projectReport.setExpenses(ValidateUtil.toCurrency(expenses));

            // Personal interno asignado
            //
            int totalEffort = 0;
            double totalWorkCost = 0;

            List<WorkReport> works = new ArrayList<WorkReport>();

            WorkingcostsDAO workingcostsDAO = new WorkingcostsDAO(session);
            List<Workingcosts> workingCostList = workingcostsDAO.consByProject(project);

            for (Workingcosts workingcost : workingCostList) {

                WorkReport work = new WorkReport();

                work.setName(HtmlUtil.escape(workingcost.getResourceName()));
                work.setDepartment(HtmlUtil.escape(workingcost.getResourceDepartment()));
                work.setEffort(Integer.toString(workingcost.getEffort()));
                work.setRate(ValidateUtil.toCurrency(workingcost.getRate()) );
                work.setWorkCost(ValidateUtil.toCurrency(workingcost.getWorkCost()) );

                works.add(work);

                totalEffort 	+= workingcost.getEffort();
                totalWorkCost 	+= workingcost.getWorkCost();
            }

            if (ValidateUtil.isNotNull(works)) {
                projectReport.setWorks(new ReportDataSource<WorkReport>(works));
            }

            projectReport.setEffort(Integer.toString(totalEffort));

            projectReport.setWorkCosts(ValidateUtil.toCurrency(totalWorkCost) );

            projectReport.setBudget(ValidateUtil.toCurrency(expenses + totalWorkCost) );

            // Documents
            DocumentprojectLogic documentprojectLogic = new DocumentprojectLogic();
            List<Documentproject> documents = documentprojectLogic.getDocuments(session, reportParams.getSettings(), project, Constants.DOCUMENT_INITIATING);

            List<NombreReport> docs = new ArrayList<NombreReport>();

            if (ValidateUtil.isNotNull(documents)) {

                for (Documentproject document : documents) {

                    docs.add(new NombreReport(
                            ValidateUtil.isNotNull(document.getType()) ? reportParams.getBundle().getString("documentation." + document.getType()) : StringPool.BLANK,
                            ValidateUtil.isNotNull(document.getMime()) ? document.getName(): document.getLink()
                    ));
                }
            }

            // Add docs to data source
            if (ValidateUtil.isNotNull(docs)) {

                projectReport.setDocuments(new ReportDataSource<NombreReport>(docs));
            }

            // Parameters
            Map<String, Object> parameters = new HashMap<String, Object>();

            // Find files in Jar
            InputStream logoFile	= this.getClass().getResourceAsStream(PATH_REPORT + LOGOFILE);
            InputStream jasperFile  = null; // ReportUtil.getInstance().getDeveloperInputStream("D://iReport/" + JASPERFILE);

            // Load production mode
            if (jasperFile == null) {
                jasperFile = this.getClass().getResourceAsStream(PATH_REPORT + JASPERFILE);
            }

            projectReport.setLogo(logoFile);

            // Data source
            ReportDataSource<ProjectReport> dataSource = new ReportDataSource<ProjectReport>(DataUtil.toList(projectReport));

            // Create report
            JasperPrint print = JasperFillManager.fillReport(jasperFile, parameters, dataSource);

            // Close inputs
            jasperFile.close();
            logoFile.close();

            String fileName = StringUtil.clearSlashes(project.getProjectName() + StringPool.BLANK_DASH + project.getChartLabel());

            File file = new File(Settings.TEMP_DIR_GENERATE_DOCS +
                    Constants.PATH_SEPARATOR + StringUtil.clearSlashes(project.getProjectName() + StringPool.BLANK_DASH + project.getChartLabel()) + configuration.extension().data());

            // ODT
            //
            JROdtExporter exporter = new JROdtExporter();
            exporter.setParameter(net.sf.jasperreports.engine.JRExporterParameter.JASPER_PRINT, print);
            exporter.setParameter(net.sf.jasperreports.engine.JRExporterParameter.OUTPUT_FILE, file);
            exporter.exportReport();

            // Create response file
            reportFile = new ReportFile();
            reportFile.setExtension(configuration.extension().data());
            reportFile.setFile(DocumentUtils.getBytesFromFile(file));
            reportFile.setName(fileName);

            // delete temporal file
            DocumentUtils.deleteFile(file);

            tx.commit();
        }
        catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new ReportException(e);
        }
        finally {
            SessionFactoryUtil.getInstance().close();
        }

        return reportFile;
    }
}
