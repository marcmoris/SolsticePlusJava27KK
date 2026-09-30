<%--/**************************************************************************
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
 *****************************************************************************/--%>

<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" 
   "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>

<f:view>
	<t:document>
		<t:documentHead>
			<t:stylesheet path="/css/login.css" />
			<f:verbatim>
				<title>Compiere - Login</title>
			</f:verbatim>
		</t:documentHead>
		<t:documentBody>
			<h:form id="loginForm">
				<f:subview id="menuPane">
					<t:panelTabbedPane serverSideTabSwitch="true" selectedIndex="#{loginBean.selectedIndex}">
						<t:panelTab id="connection" label="Connection">
							<f:subview id="tab1">
								<jsp:include page="/include/connection.jsp"/>
							</f:subview>
		        		</t:panelTab>
		        		
			        	<t:panelTab id="defaultTab" label="Defaults" rendered="#{loginBean.showTab}">
							<f:subview id="tab2">
								<jsp:include page="include/defaults.jsp"/>
							</f:subview>
						</t:panelTab>
						<%--
		        		<t:panelTab id="Help" label="Help" rendered="#{loginBean.showTab}">
						</t:panelTab>
						--%>
					</t:panelTabbedPane>
				</f:subview>
			</h:form>
		</t:documentBody>		
	</t:document>
</f:view>
