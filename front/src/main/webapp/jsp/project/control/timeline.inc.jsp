<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>
<%@taglib uri="Openppm" prefix="op" %>

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
  ~ File: timeline.inc.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:59
  --%>

<script type="text/javascript">

var timelineTable;


readyMethods.add(function() {
	
	timelineTable = $('#timelineTable').dataTable({
		"oLanguage": datatable_language,
		"iDisplayLength": 25,
		"aaSorting": [[2,'asc']],
		"bFilter": true,
		"sPaginationType": "full_numbers",
		"bAutoWidth": false,
		"aoColumns": [
		              { "bVisible": false },
		              { "bVisible": false },
		              { "sClass": "center", "sType": "es_date" },
                      { "bVisible": false },
                      { "sSortDataType": "dom-div", "sType": "numeric" },
		              { "sClass": "left"},
		              { "sClass": "left"},	
		              { "sClass": "center", "bSortable": false,  "bVisible": ${op:hasPermission(user,project.status,tab)} }
		      		]
	});
	
	$('#timelineTable tbody tr').live('click', function (event) {		
		timelineTable.fnSetSelectable(this,'selected_internal');
	});

    //Create an array with the values of all the div boxes in a column
    $.fn.dataTableExt.afnSortData['dom-div'] = function  ( oSettings, iColumn )
    {
        var aData 	= [];
        var cont 	= 0;

        for (var i = 0; i < iColumn; i++) {

            if (oSettings.aoColumns[i].bVisible == false) {
                cont += 1;
            }
        }

        $( 'td:eq('+(iColumn-cont)+') .importance', oSettings.oApi._fnGetTrNodes(oSettings) ).each( function () {
            aData.push( $(this).html() );
        } );

        return aData;
    };

    createLegend('#legendImportance','<fmt:message key="imp.very_high_importance"/>','#F00');
    createLegend('#legendImportance','<fmt:message key="imp.high_importance"/>','#FF0');
    createLegend('#legendImportance','<fmt:message key="imp.normal_importance"/>','#0F0');
});
</script>

<table id="timelineTable" class="tabledata" cellspacing="1" width="100%">
	<thead>
		<tr>
			<th width="0%">&nbsp;</th> <%-- idTimeline --%>
			<th width="0%">&nbsp;</th> <%-- idProject --%>
            <th width="10%"><fmt:message key="date"/></th>
            <th width="0%"><fmt:message key="imp"/></th>
            <th width="10%"><fmt:message key="imp"/></th>
			<th width="25%"><fmt:message key="name"/></th>
			<th width="57%"><fmt:message key="description"/></th>
			<th width="8%">
				<c:if test="${op:hasPermission(user,project.status,tab)}">
					<img id="addTimeline" title="<fmt:message key="add"/>" class="link" src="images/add.png">
				</c:if>							
			</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="timeline" items="${timelines}">
			<tr>
				<td>${timeline.idTimeline}</td>
				<td>${timeline.project.idProject}</td>
				<td><fmt:formatDate value="${timeline.timelineDate}" pattern="${datePattern}"/></td>
                <td>${timeline.importance}</td>
                <td>
                    <c:if test="${not empty timeline.importanceEnum}">
                        <div style="margin: 0px auto; width: 80px;">
                            <div style="display:none;" class="importance">${timeline.importanceEnum.order}</div>
                            <div class="${timeline.importanceEnum.classHtml}" title="<fmt:message key="${timeline.importanceEnum.label}"/>">&nbsp;</div>
                        </div>
                    </c:if>
                </td>
				<td>${tl:escape(timeline.name)}</td>
				<td>${tl:escape(timeline.description)}</td>
				<td>
					<c:if test="${op:hasPermission(user,project.status,tab)}">
						<nobr>
							<img title="<fmt:message key="view"/>" class="link viewTimeline" src="images/view.png">
                            &nbsp;&nbsp;
							<img title="<fmt:message key="delete"/>" class="link removeTimeline" src="images/delete.jpg">
						</nobr>
					</c:if>	
				</td>								
			</tr>
		</c:forEach>
		</tbody>
	</table>

    <div id="legendImportance" class="legendChart"></div>