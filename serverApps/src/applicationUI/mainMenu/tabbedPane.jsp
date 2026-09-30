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
 
<%@ page import="java.math.BigDecimal,java.util.Date"%>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<html>
	<head>
		<title>Compiere Main Menu</title>
		<link rel="stylesheet" type="text/css" href="/newInterface/css/menu.css" />
	</head>
	<f:view>
		<body>
			<h:form>
				<f:subview id="menuPane">
				<!-- The menu uses MenuBean.java temporarily for visual effect.  A new bean will need to be built that better models the appropriate menu -->
					<t:jscookMenu layout="hbr" theme="ThemeCompiere" javascriptLocation="/js" styleLocation="/css" imageLocation="/images">
    					<t:navigationMenuItems value="#{menuBean.menu}" />
    				</t:jscookMenu>
	    			<t:panelTabbedPane serverSideTabSwitch="true" >
	    			
					<t:panelTab id="menu" label="Menu">
	        			<f:verbatim><table><tr><td width="300px" valign="top"></f:verbatim>
	        			<f:verbatim>
	        				<t:selectOneListbox styleClass="selectList">
                				<f:selectItems value="#{treeBacker.shortcutMap}"/>
            				</t:selectOneListbox>
	        			</f:verbatim>	
	        			</td><td width="350px" bgcolor="#FFFFFF" valign="top" colspan="2">
	        			<div class="menuDivStyle">
	        				<jsp:include page="mainMenu.jsp"></jsp:include>	
	        			</div></td></tr>
	        			<tr><td width="350px" >
							<t:selectBooleanCheckbox value="#{treeBacker.expandMenu}" title="Expand Tree" onchange="document.getElementById('menuButton').click()"/>
							<t:outputText value="Expand Tree" />
						</td><td width="100px" align="right">
							<t:outputText value="Lookup" />
						</td><td width="250px">
							<t:inputText/>
						</td></tr></table>
	        		</t:panelTab>
	        		
		        	<t:panelTab id="workflowActivities" label="Workflow Activities" >
					</t:panelTab>
					
	        		<t:panelTab id="workflow" label="Workflow" >
					</t:panelTab>
					
					<t:panelTab id="performance" label="Performance" >
					</t:panelTab>
					
					</t:panelTabbedPane>
					
				</f:subview>
				<t:commandButton id="menuButton" action="#{treeBacker.checkBoxChanged}" forceId="true" style="visibility:hidden" />
				<t:commandButton id="logoutButton" action="#{tabbedPaneBacker.logout}" value="Logout"/>
			</h:form>
		</body>
	</f:view>
</html>