<%@page import="es.sm2.openppm.core.model.impl.Milestones"%>
<%@page import="es.sm2.openppm.utils.StringPool"%>
<%@page import="es.sm2.openppm.core.logic.impl.ProjectLogic"%>
<%@page import="es.sm2.openppm.core.logic.impl.ProjectActivityLogic"%>
<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="es.sm2.openppm.front.utils.SettingUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="es.sm2.openppm.front.utils.OpenppmUtil"%>
<%@page import="es.sm2.openppm.core.common.Settings"%>
<%@page import="es.sm2.openppm.core.model.impl.Fundingsource"%>
<%@page import="es.sm2.openppm.core.model.impl.Geography"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectControlServlet"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectPlanServlet"%>
<%@page import="es.sm2.openppm.core.model.impl.Projectactivity"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.front.utils.SecurityUtil"%>
<%@page import="es.sm2.openppm.core.model.impl.Category"%>
<%@page import="es.sm2.openppm.core.model.impl.Project"%>
<%@page import="es.sm2.openppm.front.servlets.ProgramServlet"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectInitServlet"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>
	
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>
<%@taglib uri="Openppm" prefix="op" %>
<%@taglib uri="Visual" prefix="visual" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%--
  ~ Copyright (C) 2009-2015 SM2 SOFTWARE & SERVICES MANAGEMENT
  ~ This program is free software: you can redistribute it and/or modify
  ~ it under the terms of the GNU General Public License as published by
  ~ the Free Software Foundation, either version 3 of the License, or
  ~ (at your option) any later version.
  ~
  ~ This program has been created in the hope that it will be useful.
  ~ It is distributed WITHOUT ANY WARRANTY of any Kind,
  ~ without even the implied warranty of
  ~ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
  ~ See the GNU General Public License for more details.
  ~
  ~ You should have received a copy of the GNU General Public License
  ~ along with this program. If not, see http://www.gnu.org/licenses/.
  ~
  ~ For more information, please contact SM2 Software & Services Management.
  ~ Mail: info@talaia-openppm.com
  ~ Web: http://www.talaia-openppm.com
  ~
  ~ Module: front
  ~ File: colective_follow.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:57
  --%>

<fmt:message key="data_not_found" var="dataNotFound" />
<c:set var="infoPrint"><fmt:message key="info.print"/></c:set>

<fmt:setLocale value="${locale}" scope="request"/>


<script type="text/javascript">
<!--

<%
HashMap<String, String> settings = SettingUtil.getSettings(request);
%>

var programAjax = new AjaxCall('<%=ProgramServlet.REFERENCE%>','<fmt:message key="error" />');
var dataInfoTable;

/* SCHEDULE CHART */
function showChartGantt() {

	var params = {
		accion: "<%=ProgramServlet.JX_CONS_GANTT%>",
		ids: '${ids}',
		propiedad: '${propiedad}',
		orden: '${orden}',
		since: $('#since').val(),
		until: $('#until').val()	
	};
	
	programAjax.call(params, function(data) {
		if(typeof data === 'undefined'){
			$("#chart_gantt").html('${dataNotFound}');
			$("#legendGantt").html('');
		}else{
			var monthNames = ['<fmt:message key="month.min_1"/>','<fmt:message key="month.min_2"/>','<fmt:message key="month.min_3"/>',
			                  '<fmt:message key="month.min_4"/>','<fmt:message key="month.min_5"/>','<fmt:message key="month.min_6"/>',
			                  '<fmt:message key="month.min_7"/>','<fmt:message key="month.min_8"/>','<fmt:message key="month.min_9"/>',
			                  '<fmt:message key="month.min_10"/>','<fmt:message key="month.min_11"/>','<fmt:message key="month.min_12"/>'];
			
			var paramsGantt = {
	    		data: data.tasks,
	    		monthNames: monthNames,
	    		cellWidth:$('#numberOfMonths').val()
		    };
			
			if ($("#chart_gantt").html()) {
				$("#chart_gantt").empty();
				$("#chart_gantt").ganttView(paramsGantt);
			}else{
				$("#chart_gantt").ganttView(paramsGantt);
				createLegend('#legendGantt','<fmt:message key="cost.actual" />','#4567aa');
				createLegend('#legendGantt','<fmt:message key="actual.dependent" />','#FF8F35');
				createLegend('#legendGantt','<fmt:message key="percent_complete" />','#000000');
				createLegend('#legendGantt','<fmt:message key="cost.planned" />','#A9A9A9');
				createLegend('#legendGantt','<fmt:message key="milestone" />','url(images/gantt/milestone.png) no-repeat; font-size: 13px');
				createLegend('#legendGantt','<fmt:message key="project.status_date" />','url(images/gantt/statusDate.png) no-repeat');
				createLegend('#legendGantt','<fmt:message key="today" />',';border-right: 2px solid red; padding-right: 0px; margin-left: 15px;border-radius:0px');
			}
			
			// Styles for two years
			if ($('#numberOfMonths').val() == 1) {
				$("div.ganttview-blocks").css("margin-top", "42px");
				$("div.ganttview-vtheader").css("margin-top", "41px");
				$(".ganttview-hzheader-month").css("height", "40px");
			}
		}
		show('chart_valuechart_gantt');
	});
}

function resetDatesGantt() {
	$("#since").val('');
	$("#until").val('');
	showChartGantt();
}

/* BUBBLE CHART */
function showValueChart() {
	
	if (typeof dataInfoTable === 'undefined') { 
		loadDashboard = true; 
	}
	else {
		var params = {
			accion: "<%=ProgramServlet.CHART_BUBBLE%>",
			ids: dataInfoTable.ids,
			investiment: false,
			axesXType: $("#selectBubbleChart").val()
		};
		
		programAjax.call(params, function(data) {
			
			var legendContent = '<span style="background-color:#E3E3E3;">&nbsp;&nbsp;</span><fmt:message key="risk.undefined" />';
			
			legendContent += '<span style="background-color:#99CC00;">&nbsp;&nbsp;</span><fmt:message key="risk.low" />' +
			'<span style="background-color:#FFCC00;">&nbsp;&nbsp;</span><fmt:message key="risk.medium" />' +
			'<span style="background-color:#CC0000;">&nbsp;&nbsp;</span><fmt:message key="risk.high" />';
			
			//JQPlot parameters 
			var optionsObj;
			
			if ($("#selectBubbleChart").val() == '<%= Project.INITDATE %>' || $("#selectBubbleChart").val() == '<%= Project.ENDDATE %>') {
				optionsObj = {
					axes:{
						xaxis: {
							label: $("#selectBubbleChart :selected").text(),
							renderer: $.jqplot.DateAxisRenderer,
							tickRenderer: $.jqplot.CanvasAxisTickRenderer ,
							tickOptions:{
								formatString:"%d/%m/%Y",
								angle: -45
							}
						},
						yaxis: {
							label: '<fmt:message key="table.priority" />',
							labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
							labelOptions: {
								angle: -90
							}
						}
					}
				};
			}
			else {
				optionsObj = {
					labelX: $("#selectBubbleChart :selected").text(),
					labelY:'<fmt:message key="table.priority" />'
				};
			}
			
			if (typeof bubbleChart === 'undefined') {
				bubbleChart = createBubbleChart('bubble_chart', [data.chart], optionsObj, '${dataNotFound}', legendContent);
			}
			else {
				$("#legend-bubble_chart").remove();
				bubbleChart.destroy();
				bubbleChart = createBubbleChart('bubble_chart', [data.chart], optionsObj, '${dataNotFound}', legendContent);
			}
			
		});
	}
}

/* COOKIE OBJECT */
cookieObject = {
	printStatus:function(){
		var status = [];
		var statusCont = 0;
		if ($.cookie('projects.statusIni') == 'true') {
			status[statusCont] = '<fmt:message key="project_status.INITIATING" />';
			statusCont++;
		}
		if($.cookie('projects.statusPla') == 'true'){
			status[statusCont] = '<fmt:message key="project_status.PLANNING" />';
			statusCont++;
		}
		if($.cookie('projects.statusCon') == 'true'){
			status[statusCont] = '<fmt:message key="project_status.CONTROL" />';
			statusCont++;
		}
		if($.cookie('projects.statusClo') == 'true'){
			status[statusCont] = '<fmt:message key="project_status.CLOSED" />';
			statusCont++;
		}
		
		if(statusCont != 0){
			$("#status").html(status.join(", "));
		}
		else{
			$("tr #status").parent().remove();
		}
	},
	printRagStatus:function(){
		if($.cookie('ragStatus') == null || typeof $.cookie('ragStatus') === 'undefined' || $.cookie('ragStatus') == ''){
			$("#ragStatus").parent().remove();
		}
		else {
			
			$("#ragStatus").html('<span>');
			$("#ragStatus span").css("padding-right", "14px").css("margin-right", "4px");
			
			if($.cookie('ragStatus') == 'H'){
				$("#ragStatus span").attr('class', 'high_importance');
				$("#ragStatus").append('<fmt:message key="rag.high_importance"/>');
			}
			else if($.cookie('ragStatus') == 'M'){
				$("#ragStatus span").attr('class', 'medium_importance');
				$("#ragStatus").append('<fmt:message key="rag.medium_importance"/>');
			}
			else if($.cookie('ragStatus') == 'N'){
				$("#ragStatus span").attr('class', 'normal_importance');
				$("#ragStatus").append('<fmt:message key="rag.normal_importance"/>');
			}
			else if($.cookie('ragStatus') == 'L'){
				$("#ragStatus span").attr('class', 'low_importance');
				$("#ragStatus").append('<fmt:message key="rag.low_importance"/>');
			}
			
			
		}
	},
	printFilterInternal:function(){
		if($.cookie('projects.filterInternal') == null || typeof $.cookie('projects.filterInternal') === 'undefined' || $.cookie('projects.filterInternal') == ''){
			$("#filterInternal").parent().remove();
		}
		else {
			if($.cookie('projects.filterInternal') == true){
				$("#filterInternal").html('<fmt:message key="project.internal.only"/>');
			}
			else{
				$("#filterInternal").html('<fmt:message key="project.internal.not"/>');
			}
		}
	},
	printFilterPriority:function(){
		if($.cookie('filterPriority') == null || typeof $.cookie('filterPriority') === 'undefined' || $.cookie('filterPriority') == ''){
			$("#filterPriority").parent().remove();
		}
		else {
			if($.cookie('filterPriority') == "greaterEqual"){
				$("#filterPriority").prepend('<fmt:message key="filter.greater_equal"/>');
				$("#filterPriority #last").html($.cookie('last'));
			}
			else if($.cookie('filterPriority') == "lessEqual"){
				$("#filterPriority").prepend('<fmt:message key="filter.less_equal"/>');
				$("#filterPriority #last").html($.cookie('last'));
			}
			else if($.cookie('filterPriority') == "between"){
				$("#filterPriority").prepend('<fmt:message key="filter.between"/>');
				$("#filterPriority #first").html($.cookie('first'));
				$("#filterPriority #last").prepend('- ');
				$("#filterPriority #last").append($.cookie('last'));
			}
		}
	},
	printGeneric:function(id, nameCookie){
		if($.cookie(nameCookie) == null ||  typeof $.cookie(nameCookie) === 'undefined' || $.cookie(nameCookie) == ''){
			$("#" + id).parent().remove();	
		}
		else {
			$("#" + id).html($.cookie(nameCookie));
		}
	}
	
};

readyMethods.add(function () {
	
	/* FILTERS */
	filtersTable = $('#tb_filters').dataTable({
		"oLanguage": datatable_language,
		"bFilter": false,
		"bInfo": false,
		"bPaginate": false,
		"iDisplayLength": 50,
		"sPaginationType": "full_numbers",
		"aaSorting": [[ 1, "asc" ]],
		"aoColumns": [
		              { "sClass": "left", "bSortable": false },
		              { "sClass": "left", "bSortable": false }
		      		]
	});
	
	/* PROJECTS INFORMATION */
	projectsTable = $('#tb_projects').dataTable({
		"oLanguage": datatable_language,
		"bInfo": false,
		"iDisplayLength": 50,
		"sPaginationType": "full_numbers",
		"aoColumns": [ 
			            { "bVisible": false, "bSortable": false }, 
			            { "bVisible": <%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_PROJECT_COLUMN_RAG, Settings.DEFAULT_PROJECT_COLUMN_RAG))%>, "sClass": "center",   "sWidth": "4%"},
			            { "bVisible": <%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_PROJECT_COLUMN_START, Settings.DEFAULT_PROJECT_COLUMN_START))%>, "sClass": "center",   "sWidth": "6%"}, 
			            { "bVisible": <%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_PROJECT_COLUMN_ACCOUNTING_CODE, Settings.DEFAULT_PROJECT_COLUMN_ACCOUNTING_CODE))%>, "sClass": "left",   "sWidth": "2%"}, 
			            { "bVisible": <%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_PROJECT_COLUMN_NAME, Settings.DEFAULT_PROJECT_COLUMN_NAME))%>, "sClass": "left",   "sWidth": "18%" },
			            { "bVisible": <%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_PROJECT_COLUMN_SHORT_NAME, Settings.DEFAULT_PROJECT_COLUMN_SHORT_NAME))%>, "sClass": "left",   "sWidth": "8%" },
			            <% if (SecurityUtil.isUserInRole(request, Constants.ROLE_PORFM)) {%>
			            { "sClass": "left", "sWidth": "12%" },
			            <%}%>
			            { "bVisible": <%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_PROJECT_COLUMN_BUDGET, Settings.DEFAULT_PROJECT_COLUMN_BUDGET))%>, "sClass": "right",  "sWidth": "8%" },
			            { "bVisible": <%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_PROJECT_COLUMN_PRIORITY, Settings.DEFAULT_PROJECT_COLUMN_PRIORITY))%>, "sClass": "right",  "sWidth": "6%" },
			            { "bVisible": <%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_PROJECT_COLUMN_POC, Settings.DEFAULT_PROJECT_COLUMN_POC))%>, "sClass": "right",  "sWidth": "8%", "bSortable": false },
			            { "bVisible": <%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_PROJECT_COLUMN_BASELINE_START, Settings.DEFAULT_PROJECT_COLUMN_BASELINE_START))%>, "sClass": "center", "sWidth": "7%", "sType": "es_date" },
			            { "bVisible": <%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_PROJECT_COLUMN_BASELINE_FINISH, Settings.DEFAULT_PROJECT_COLUMN_BASELINE_FINISH))%>, "sClass": "center", "sWidth": "7%", "sType": "es_date" },
			            { "bVisible": <%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_PROJECT_COLUMN_START, Settings.DEFAULT_PROJECT_COLUMN_START))%>, "sClass": "center", "sWidth": "7%", "sType": "es_date", "bSortable": false },
			            { "bVisible": <%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_PROJECT_COLUMN_FINISH, Settings.DEFAULT_PROJECT_COLUMN_FINISH))%>, "sClass": "center", "sWidth": "7%", "sType": "es_date", "bSortable": false },
			            { "bVisible": <%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_PROJECT_COLUMN_ACTUAL_COST, Settings.DEFAULT_PROJECT_COLUMN_ACTUAL_COST))%>, "sClass": "right","sWidth": "3%", "bSortable": false },
			            { "bVisible": <%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_PROJECT_COLUMN_INTERNAL_EFFORT, Settings.DEFAULT_PROJECT_COLUMN_INTERNAL_EFFORT))%>, "sClass": "right","sWidth": "2%" },
			            { "bVisible": <%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_PROJECT_COLUMN_EXTERNAL_COST, Settings.DEFAULT_PROJECT_COLUMN_EXTERNAL_COST))%>, "sClass": "right", "sWidth": "7%", "bSortable": false }
   		],
		"bLengthChange": false,
		"bFilter": false,
		"bPaginate": false,
		"bSort": false
	});
	
	/* DATES SCHEDULE CHART */
	var sinceUntil = $('#since, #until').datepicker({
		dateFormat: '${datePickerPattern}',
		firstDay: 1,
		showOn: 'button',
		buttonImage: 'images/calendario.png',
		buttonImageOnly: true,
		numberOfMonths: ${numberOfMonths},
		changeMonth: true,
		onSelect: function(selectedDate) {
			var option = this.id == "since" ? "minDate" : "maxDate";
			var instance = $(this).data("datepicker");
			var date = $.datepicker.parseDate(instance.settings.dateFormat || $.datepicker._defaults.dateFormat, selectedDate, instance.settings);
			sinceUntil.not(this).datepicker("option", option, date);
		}
	});
	
	/* MILESTONES */
	$('#tb_milestones').dataTable({
		"sPaginationType": "full_numbers",
		"oLanguage": datatable_language,
		"bInfo": false,
		"aaSorting": [],
        "bLengthChange": false,
        "bFilter": false,
        "bPaginate": false,
        "bSort": false,
		"aoColumns": [
		              { "sClass": "left" },
		              { "sClass": "left" },
		              { "sClass": "left" },
		              { "sClass": "left" },
		              { "sClass": "left" },
		              { "sClass": "center", "sType": "es_date" },	
		              { "sClass": "center", "sType": "es_date" },
		              { "sClass": "center", "sType": "es_date" }	
		      		]
	});

    /* KPIs */
    $('#tb_kpis').dataTable({
        "sPaginationType": "full_numbers",
        "oLanguage": datatable_language,
        "bInfo": false,
        "aaSorting": [],
        "bLengthChange": false,
        "bFilter": false,
        "bPaginate": false,
        "bSort": false,
        "aoColumns": [
            { "bVisible": false },
            { "sClass": "left",  "sWidth":  "4%", "sSortDataType": "dom-div", "sType": "numeric"},
            { "sClass": "left",  "sWidth": "30%" },
            { "sClass": "left",  "sWidth": "26%" },
            { "sClass": "right", "sWidth": "10%" },
            { "sClass": "right", "sWidth": "10%" },
            { "sClass": "right", "sWidth": "10%" },
            { "sClass": "right", "sWidth": "10%" }
        ]
    });
	
	/* SHOW CHARTS */
	showChartGantt();
	
	$("#selectBubbleChart").change(function(){
		showValueChart();
	});
	showValueChart();
	
	/* Delete styles panel */
	$("#chart_valuechart").parent().css({'background':'none','border': 'none'}); 
	$("#chart_valuechart").prev().css({'display': 'none'});
	$('#chart_valuechart').show();
	
	dataInfoTable = {
			ids: "${ids}"
	};
	showValueChart();
	
	/* SHOW FILTERS */
	cookieObject.printStatus();
	cookieObject.printGeneric('<%=Project.PROJECTNAME %>','projects.<%=Project.PROJECTNAME %>');
	cookieObject.printRagStatus();
	cookieObject.printFilterPriority();
	cookieObject.printFilterInternal();
	cookieObject.printGeneric('budgetYear','projects.<%=Project.BUDGETYEAR %>');
	cookieObject.printGeneric('filter_start','projects.filter_start');
	cookieObject.printGeneric('filter_finish','projects.filter_finish');
	
	/* OPEN ALL PANELS */
	openCloseAllPanels(true);
	
	//Activate tooltip
	createBT('.btitle');
});

//-->
</script>
	<c:set var="controlStatus" value="<%=Constants.STATUS_CONTROL%>" ></c:set>
	<c:set var="styleRow" value="odd"></c:set>
	<c:set var="even" value="even"></c:set>
	<c:set var="blank" value="<%= StringPool.BLANK %>"></c:set>

	<!-- PRINT INFORMATION -->
	<div style="padding-right: 14px;" class="right hidePrint">
		<img class="btitle" title="" src="images/info.png" bt-xtitle="${infoPrint}">
	</div>

	<!-- FILTERS -->
	<fmt:message key="filter" var="titleFilters"/>
	<visual:panel id="filters" title="${titleFilters}"  >
		<table id="tb_filters" class="tabledata" cellspacing="1" width="100%">
			<thead>
				<tr>
					<th width="20%"><fmt:message key="filter" /></th>
					<th width="80%"><fmt:message key="values" /></th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td><fmt:message key="status" /></td>
					<td id="status"></td>
				</tr>
				<tr>
					<td><fmt:message key="search" /></td>
					<td id="projectName"></td>
				</tr>
				<tr>
					<td><fmt:message key="rag" /></td>
					<td id="ragStatus"></td>
				</tr>
				<tr>
					<td><fmt:message key="project.priority" /></td>
					<td id="filterPriority">
						<span id="first"></span>
						<span id="last"></span>
					</td>
				</tr>
				<tr>
					<td><fmt:message key="project.internal" /></td>
					<td id="filterInternal"></td>
				</tr>
				<c:if test="${fundingSource != blank}">
					<tr>
						<td><fmt:message key="funding_source" /></td>
						<td>${fundingSource}</td>
					</tr>
				</c:if>
				<tr>
					<td><fmt:message key="project.budget_year" /></td>
					<td id="budgetYear"></td>
				</tr>
				<tr>
					<td><fmt:message key="dates.since" /></td>
					<td id="filter_start"></td>
				</tr>
				<tr>
					<td><fmt:message key="dates.until" /></td>
					<td id="filter_finish"></td>
				</tr>
				<c:if test="${customerType != blank}">
					<tr>
						<td><fmt:message key="customer_type" /></td>
						<td>${customerType}</td>
					</tr>
				</c:if>
				<c:if test="${customer != blank}">
					<tr>
						<td><fmt:message key="customer" /></td>
						<td>${customer}</td>
					</tr>
				</c:if>
				<c:if test="${program != blank}">
					<tr>
						<td><fmt:message key="program" /></td>
						<td>${program}</td>
					</tr>
				</c:if>
				<c:if test="${PM != blank}">
					<tr>
						<td><fmt:message key="project_manager" /></td>
						<td>${PM}</td>
					</tr>
				</c:if>
				<c:if test="${category != blank}">
					<tr>
						<td><fmt:message key="category" /></td>
						<td>${category}</td>
					</tr>
				</c:if>
				<c:if test="${geography != blank}">
					<tr>
						<td><fmt:message key="geography" /></td>
						<td>${geography}</td>
					</tr>
				</c:if>
				<c:if test="${seller != blank}">
					<tr>
						<td><fmt:message key="seller" /></td>
						<td>${seller}</td>
					</tr>
				</c:if>
				<c:if test="${sponsor != blank}">
					<tr>
						<td><fmt:message key="sponsor" /></td>
						<td>${sponsor}</td>
					</tr>
				</c:if>
				<c:if test="${label != blank}">
					<tr>
						<td><fmt:message key="labels" /></td>
						<td>${label}</td>
					</tr>
				</c:if>
				<c:if test="${stageGate != blank}">
					<tr>
						<td><fmt:message key="stage_gates" /></td>
						<td>${stageGate}</td>
					</tr>
				</c:if>
			</tbody>
		</table>
	</visual:panel>

	<!-- INFORMATION OF PROJECTS -->
	<fmt:message key="projects" var="titleProjects"/>
	<visual:panel id="projectsInfo" title="${titleProjects}"  >
		<table id="tb_projects" class="tabledata" cellspacing="1" width="100%">
			<thead>
				<tr>
					<th>&nbsp;</th>
					<th><fmt:message key="rag" /></th>
					<th><fmt:message key="status" /></th>
					<th><fmt:message key="project.accounting_code" /></th>
					<th><fmt:message key="project" /></th>
					<th><fmt:message key="project.chart_label" /></th>
					<c:if test="<%=SecurityUtil.isUserInRole(request, Constants.ROLE_PORFM)%>">
						<th><fmt:message key="perf_organization" /></th>
					</c:if>
					<th>
						<fmt:message key="activity.budget" />
						<br><span id="sumBudget"></span>
					</th>
					<th><fmt:message key="table.priority" /></th>
					<th><fmt:message key="table.poc" /></th>
					<th><fmt:message key="baseline_start" /></th>
					<th><fmt:message key="baseline_finish" /></th>
					<th><fmt:message key="start" /></th>
					<th><fmt:message key="finish" /></th>
					<th><fmt:message key="followup.ac" /></th>
					<th><fmt:message key="internal_effort" /></th>
					<th><fmt:message key="external_costs" /></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="project" items="${projects}">
					<tr>
						<td>${project.idProject}</td>
						
						<c:set var="riskLow" value="<%= Constants.RISK_LOW %>"></c:set>
						<c:set var="riskMedium" value="<%= Constants.RISK_MEDIUM %>"></c:set>
						<c:set var="riskHigh" value="<%= Constants.RISK_HIGH %>"></c:set>
						<c:set var="riskNormal" value="<%= Constants.RISK_NORMAL %>"></c:set>
						<c:choose>
							<c:when test="${op:isCharEquals(project.rag, riskLow)}">
								<c:set var="rag" value="low_importance"></c:set>
							</c:when>
							<c:when test="${op:isCharEquals(project.rag, riskMedium)}">
								<c:set var="rag" value="medium_importance"></c:set>
							</c:when>
							<c:when test="${op:isCharEquals(project.rag, riskHigh)}">
								<c:set var="rag" value="high_importance"></c:set>
							</c:when>
							<c:when test="${op:isCharEquals(project.rag, riskNormal)}">
								<c:set var="rag" value="normal_importance"></c:set>
							</c:when>
							<c:otherwise>
								<c:set var="rag" value=""></c:set>
							</c:otherwise>
						</c:choose>
						<td><div class="${rag}">&nbsp;&nbsp;&nbsp;</div></td>
						
						<td><fmt:message key="project_status.${project.status}" /></td>
						<td>${project.accountingCode}</td>
						<td>${project.projectName}</td>
						<td>${project.chartLabel}</td>
						<c:if test="<%=SecurityUtil.isUserInRole(request, Constants.ROLE_PORFM)%>">
							<td>${project.performingOrg}</td>
						</c:if>
						<td>${tl:toCurrency(project.tcv)}</td>
						<td>${project.priority}</td>
						<td>${tl:toPercent(project.poc)}</td>
						<td class="center"><fmt:formatDate value="${project.plannedInitDate}" pattern="${datePattern}"/></td>
						<td class="center"><fmt:formatDate value="${project.plannedFinishDate}" pattern="${datePattern}"/></td>
						<td class="center"><fmt:formatDate value="${project.startDate}" pattern="${datePattern}"/></td>
						<td class="center"><fmt:formatDate value="${project.finishDate}" pattern="${datePattern}"/></td>
						<td>${tl:toCurrency(project.lastAc)}</td>
						<td>${project.effort}</td>
						<td>${tl:toCurrency(project.externalCost)}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</visual:panel>

	<%-- QUALITATIVE INFORMATION --%>
	<fmt:message key="status_report" var="titleStatusReport"/>
	<visual:panel id="statusReport" title="${titleStatusReport}"  >
		<table class="tabledata" cellspacing="1" width="100%">
			<thead>
				<tr>
					<th width="20%"><fmt:message key="project_name"/></th>
					<th width="5%"><fmt:message key="date"/></th>
					<th width="5%"><fmt:message key="status"/></th>
					<th width="10%"><fmt:message key="type"/></th>
					<th width="56%"><fmt:message key="desc"/></th>
				</tr>
			</thead>
			<tbody id="add_datacualitative">
				<c:forEach var="project" items="${projects}">
					<c:set var="followup" value="${project.lastFollowup}"></c:set>
					<c:if test="${op:isStringEquals(project.status, controlStatus) && followup != null}">
						<c:choose>
							<c:when test="${followup.generalFlag != null}">
								<c:set var="general_status" value="${followup.generalFlag}"/>
							</c:when>
							<c:otherwise>
								<c:set var="general_status" value=" "/>
							</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${followup.generalComments != null}">
								<c:set var="general_desc" value="${op:generateBreak(tl:escape(followup.generalComments))}"/>
							</c:when>
							<c:otherwise>
								<c:set var="general_desc" value=" "/>
							</c:otherwise>
						</c:choose>
						<c:choose>
						 	<c:when test="${followup.riskFlag != null}">
						 		<c:set var="risk_status" value="${followup.riskFlag}"/>
						 	</c:when>
						 	<c:otherwise>
						 		<c:set var="risk_status" value=" "/>
						 	</c:otherwise>
						 </c:choose>
					 	<c:choose>
						 	<c:when test="${followup.risksComments != null}">
						 		<c:set var="risk_desc" value="${op:generateBreak(tl:escape(followup.risksComments))}"/>
						 	</c:when>
						 	<c:otherwise>
						 		<c:set var="risk_desc" value=" "/>
						 	</c:otherwise>
						 </c:choose>
					  	<c:choose>
						 	<c:when test="${followup.scheduleFlag != null}">
						 		<c:set var="schedule_status" value="${followup.scheduleFlag}"/>
						 	</c:when>
						 	<c:otherwise>
						 		<c:set var="schedule_status" value=" "/>
						 	</c:otherwise>
						 </c:choose>
					 	<c:choose>
						 	<c:when test="${followup.scheduleComments != null}">
						 		<c:set var="schedule_desc" value="${op:generateBreak(tl:escape(followup.scheduleComments))}"/>
						 	</c:when>
						 	<c:otherwise>
						 		<c:set var="schedule_desc" value=" "/>
						 	</c:otherwise>
						 </c:choose>
					  	<c:choose>
						 	<c:when test="${followup.costFlag != null}">
						 		<c:set var="cost_status" value="${followup.costFlag}"/>
						 	</c:when>
						 	<c:otherwise>
						 		<c:set var="cost_status" value=" "/>
						 	</c:otherwise>
						 </c:choose>
					  	<c:choose>
						 	<c:when test="${followup.costComments != null}">
						 		<c:set var="cost_desc" value="${op:generateBreak(tl:escape(followup.costComments))}"/>
						 	</c:when>
						 	<c:otherwise>
						 		<c:set var="cost_desc" value=" "/>
						 	</c:otherwise>
						 </c:choose>
						<tr class="${styleRow}">
							<td class="left" rowspan="4">${project.projectName}</td>
							<td class="center" rowspan="4"><fmt:formatDate value="${followup.followupDate}" pattern="${datePattern}"/></td>
							<td class="left"><div class="${followup.classGeneral}">&nbsp;</div></td>
							<td class="left"><fmt:message key="general"/></td>
							<td class="left">${general_desc}</td>
						</tr>
						<tr class="${styleRow}">
							<td class="left"><div class="${followup.classRisk}">&nbsp;</div></td>
							<td class="left"><fmt:message key="risk"/></td>
							<td class="left">${risk_desc}</td>
						</tr>
						<tr class="${styleRow}">
							<td class="left"><div class="${followup.classSchedule}">&nbsp;</div></td>
							<td class="left"><fmt:message key="schedule"/></td>
							<td class="left">${schedule_desc}</td>
						</tr>
						<tr class="${styleRow}">
							<td class="left"><div class="${followup.classCost}">&nbsp;</div></td>
							<td class="left"><fmt:message key="cost"/></td>
							<td class="left">${cost_desc}</td>
						</tr>
						<c:choose>
							<c:when test="${styleRow == even}">
								<c:set var="styleRow" value="odd"></c:set>
							</c:when>
							<c:otherwise>
								<c:set var="styleRow" value="even"></c:set>
							</c:otherwise>
						</c:choose>
					</c:if>
				</c:forEach>
			</tbody>
		</table>
	</visual:panel>

	<%-- MILESTONES TABLE --%>
	<fmt:message key="milestones.list" var="titleMilestones"/>
	<visual:panel id="milestones" title="${titleMilestones}" >
	
		<div style="padding-top:10px;margin-bottom: 22px;">
			<%-- Filters dates --%>
			<div style="margin-right:5px; float: left; width: 95px;">
				<fmt:message key="dates.since"/>:&nbsp;
				<fmt:formatDate value="${milestoneDateSince}" pattern="${datePattern}" var="milestoneDateSinceParsed"/>
				${milestoneDateSinceParsed}
			</div>
		  	<div style="margin-right:15px; float: left; width: 95px;">
				<fmt:message key="dates.until"/>:&nbsp;
				<fmt:formatDate value="${milestoneDateUntil}" pattern="${datePattern}" var="milestoneDateUntilParsed"/>
				${milestoneDateUntilParsed}
			</div>
			<%-- Filter Milestone type --%>
			<div style="margin-right:15px; float: left;">
				<fmt:message key="milestone.type"/>:&nbsp;
				${milestonetype}
			</div>
			<%-- Filter Milestone category --%>
			<div style="margin-right:15px; float: left;">
				<fmt:message key="milestone.category"/>:&nbsp;
				${milestonecategory}
			</div>
			<%-- Filter Milestone pending --%>
			<div style="margin-right:15px; float: left;">
				<fmt:message key="milestone.pendings"/>:&nbsp;
				${milestonePending}
			</div>
		</div>
		
		<table id="tb_milestones" class="tabledata" cellspacing="1" style="width:100%">
			<thead>
				<tr>
					<th width="20%"><fmt:message key="project"/></th>
					<th width="10%"><fmt:message key="project.chart_label"/></th>
					<th width="10%"><fmt:message key="milestone.type"/></th>
					<th width="20%"><fmt:message key="milestone.name"/></th>
					<th width="25%"><fmt:message key="milestone.desc"/></th>
					<th width="5%"><fmt:message key="milestone.planned_date"/></th>	
					<th width="5%"><fmt:message key="milestone.due_date"/></th>	
					<th width="5%"><fmt:message key="milestone.actual_date"/></th>	
				</tr>
			</thead>
			<tbody>
				<c:forEach var="milestone" items="${milestones}">
					<tr>
						<td class="left">${milestone.project.projectName}</td>
						<td class="left">${milestone.project.chartLabel}</td>
						<td class="left">${milestone.milestonetype.name}</td>
						<td class="left">${milestone.name}</td>
						<td class="left">${milestone.description}</td>
						<td class="center"><fmt:formatDate value="${milestone.planned}" pattern="${datePattern}"/></td>
						<td class="center"><fmt:formatDate value="${milestone.estimatedDate}" pattern="${datePattern}"/></td>
						<td class="center"><fmt:formatDate value="${milestone.achieved}" pattern="${datePattern}"/></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</visual:panel>

    <%-- KPIs TABLE --%>
    <fmt:message key="kpi.name" var="titleKpi"/>
    <visual:panel id="kpis" title="${titleKpi}" >
        <table id="tb_kpis" class="tabledata" cellspacing="1" width="100%">
            <thead>
            <tr>
                <th>&nbsp;</th>
                <th><fmt:message key="rag" /></th>
                <th><fmt:message key="project" /></th>
                <th><fmt:message key="kpi.metric" /></th>
                <th><fmt:message key="kpi.upper_threshold" /></th>
                <th><fmt:message key="kpi.lower_threshold" /></th>
                <th><fmt:message key="kpi.weight" />&nbsp;%</th>
                <th><fmt:message key="kpi.value" /></th>
            </tr>
            </thead>
            <tbody>
                <c:forEach var="kpi" items="${kpis}">
                <tr>
                    <td class="left">${kpi.idProjectKpi}</td>
                    <td class="left"><div class="riskRating ${kpi.RAG}">&nbsp;</div></td>
                    <td class="left">${kpi.project.projectName}</td>
                    <td class="left">${kpi.metrickpi eq null ? kpi.specificKpi : kpi.metrickpi.name}</td>
                    <td class="left">${kpi.upperThreshold eq null ? tl:toCurrency(0) : tl:toCurrency(kpi.upperThreshold)}</td>
                    <td class="left">${kpi.lowerThreshold eq null ? tl:toCurrency(0) : tl:toCurrency(kpi.lowerThreshold)}</td>
                    <td class="left">${kpi.weight eq null ? tl:toCurrency(0) : tl:toCurrency(kpi.weight)}</td>
                    <td class="left">${kpi.value eq null ? tl:toCurrency(0) : tl:toCurrency(kpi.value)}</td>
                </tr>
                </c:forEach>
            </tbody>
        </table>
    </visual:panel>

	<%-- SCHEDULE CHART --%>
	<fmt:message key="schedule_chart" var="titleScheduleChart"/>
	<visual:panel id="scheduleChart" title="${titleScheduleChart}" cssClass="pageBreak">
		<div class="hidePrint" style="padding-top:10px;">
	     	<span style="margin-right:5px;">
				<fmt:message key="dates.since"/>:&nbsp;
				<input type="text" id="since" class="campo fecha alwaysEditable"/>
	       	</span>
	    		<span style="margin-right:5px;">
				<fmt:message key="dates.until"/>:&nbsp;
				<input type="text" id="until" class="campo fecha alwaysEditable"/>
			</span>
			<span style="margin-right:5px;">
				<fmt:message key="months"/>:&nbsp;
				<select id="numberOfMonths" class="campo alwaysEditable" style="width:45px">
					<option value="1">24</option>
					<option value="2" selected>12</option>
	  				<option value="4">6</option>
					<option value="6">4</option>
				  	<option value="8">3</option>
				</select>
			</span>
			<a href="javascript:showChartGantt();" class="boton"><fmt:message key="chart.refresh"/></a>
			<a href="javascript:resetDatesGantt();" class="boton"><fmt:message key="filter.default"/></a>
		</div>
		<div id="chart_gantt" style="margin: 20px auto;"></div>
		<div id="legendGantt" class="legendChart"></div>
	</visual:panel>
		
	<%-- BUBBLE CHART --%>
	<fmt:message key="dashboard" var="titleDashboard"/>
	<visual:panel id="chartDashboard" title="${titleDashboard}"  cssClass="pageBreak">
		<div class="hidePrint" style="padding-top:10px;">
			<div style="margin-bottom:10px;">
				<fmt:message key="chart.axis" />&nbsp;X:&nbsp;
				<select id="selectBubbleChart" class="campo alwaysEditable" style="width:120px">
					<option value="poc"><fmt:message key="table.poc" /></option>
		 			<option value="<%= Project.PROBABILITY %>" <c:if test="${investiment}">selected="selected"</c:if>><fmt:message key="table.probability" /></option>
					<option value="<%= Project.INITDATE %>"><fmt:message key="date"/>&nbsp;<fmt:message key="start" /></option>
				  	<option value="<%= Project.ENDDATE %>"><fmt:message key="date"/>&nbsp;<fmt:message key="finish" /></option>
				  	<option value="riskRating"><fmt:message key="riskRating" /></option>
				</select>
			</div>
		</div>
		<div id="bubble_chart" style="margin: 20px auto;"></div>
		<div style="position:absolute;z-index:99;display:none;" id="tooltipBubbleChart" class="tooltip"></div>
	</visual:panel>
