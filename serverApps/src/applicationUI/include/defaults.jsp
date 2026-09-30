<%--/******************************************************************************
     * Product: Compiere ERP & CRM Smart Business Solution                        *
     * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
     * This program is free software; you can redistribute it and/or modify it    *
     * under the terms version 2 of the GNU General Public License as published   *
     * by the Free Software Foundation. This program is distributed in the hope   *
     * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
     * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
     * See the GNU General Public License for more details.                       *
     * You should have received a copy of the GNU General Public License along    *
     * with this program; if not, write to the Free Software Foundation, Inc.,    *
     * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
     * For the text or an alternative of this public license, you may reach us    *
     * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
     * or via info@compiere.org or http://www.compiere.org/license.html           *
     ******************************************************************************/--%>

<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" 
   "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>

<f:verbatim>
	<table>
		<tr>
			<td>
</f:verbatim>
				<t:outputLabel value="Role"/>
<f:verbatim>
			</td>
			<td colspan="2">
</f:verbatim>
				<t:selectOneMenu value="#{loginBean.loginInformation.selectedRole}" styleClass="selectList"
									onchange="document.getElementById('theButton').click()">
					<f:selectItems value="#{loginBean.loginInformation.roleMap}"/>
				</t:selectOneMenu>
<f:verbatim>
			</td>
		</tr>
		<tr>
			<td>
</f:verbatim>
				<t:outputLabel value="Client"/>
<f:verbatim>
			</td>
			<td colspan="2">
</f:verbatim>
				<t:selectOneMenu value="#{loginBean.loginInformation.selectedClient}" styleClass="selectList"
									onchange="document.getElementById('theButton').click()">
					<f:selectItems value="#{loginBean.loginInformation.clientMap}"/>
				</t:selectOneMenu>
<f:verbatim>
			</td>
		</tr>
		<tr>
			<td>
</f:verbatim>
				<t:outputLabel value="Organization"/>
<f:verbatim>
			</td>
			<td colspan="2">
</f:verbatim>		
				<t:selectOneMenu value="#{loginBean.loginInformation.selectedOrg}" styleClass="selectList"
									onchange="document.getElementById('theButton').click()">
					<f:selectItems value="#{loginBean.loginInformation.orgMap}"/>
				</t:selectOneMenu>
<f:verbatim>
			</td>
		</tr>
		<tr>
			<td>
</f:verbatim>		
				<t:outputLabel value="Warehouse"/>
<f:verbatim>
			</td>
			<td colspan="2">
</f:verbatim>		
				<t:selectOneMenu value="#{loginBean.loginInformation.selectedWarehouse}" styleClass="selectList"
									onchange="document.getElementById('theButton').click()">
					<f:selectItems value="#{loginBean.loginInformation.warehouseMap}"/>
				</t:selectOneMenu>
<f:verbatim>
<%-- --%>
			</td>
		</tr>
		<tr>
			<td>
</f:verbatim>		
				<t:outputLabel value="Date"/>
<f:verbatim>
			</td>
			<td colspan="2">
</f:verbatim>		
				<t:inputText value="#{loginBean.loginInformation.date}" styleClass="textBox"/>
<f:verbatim>
			</td>
		</tr>
		<tr>
			<td>
</f:verbatim>		
				<t:outputLabel value="Printer"/>
<f:verbatim>
			</td>
			<td colspan="2">
</f:verbatim>		
				<t:selectOneMenu value="#{loginBean.loginInformation.selectedPrinter}" styleClass="selectList">
					<f:selectItems value="#{loginBean.loginInformation.printerMap}"/>
				</t:selectOneMenu>
<f:verbatim>
			</td>
		</tr>
	</table>	
</f:verbatim> 
<t:commandButton id="theButton" action="#{loginBean.refreshDropDownMenus}" forceId="true" style="visibility:hidden" />
<f:verbatim>
	<div align="center">
</f:verbatim>
	<t:panelGroup>
		<t:commandLink onclick="javascript:self.close()">
			<t:graphicImage value="/images/Cancel24.gif"/>
		</t:commandLink>
		<t:commandLink action="#{loginBean.defaultOkClicked}">
			<t:graphicImage value="/images/Ok24.gif"/>
		</t:commandLink>
	</t:panelGroup>
<f:verbatim>
</div>
</f:verbatim>