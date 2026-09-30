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

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

	    		<t:tree2 id="clientTree" value="#{treeBacker.treeData}" var="node" varNodeToggler="t" showRootNode="false" clientSideToggle="true" binding = "#{treeBacker.tree}">
			        <f:facet name="parent">
			            <h:panelGroup>
			                <f:facet name="expand">
			                    <t:graphicImage value="/images/mOpen.gif" rendered="#{t.nodeExpanded}" border="0"/>
			                </f:facet>
			                <f:facet name="collapse">
			                    <t:graphicImage value="/images/mClosed.gif" rendered="#{!t.nodeExpanded}" border="0"/>
			                </f:facet>
			                <h:outputText value="#{node.description}" styleClass="nodeFolder"/>
			                <h:outputText value="(#{node.childCount})" styleClass="childCount" rendered="#{!empty node.children}"/>
			            </h:panelGroup>
			        </f:facet>
			        <f:facet name="document">
			            <h:panelGroup>
			                <h:commandLink immediate="true" styleClass="document" actionListener="#{t.setNodeSelected}">
			                    <t:graphicImage value="/images/mReport.gif" border="0"/>
			                    <h:outputText value="#{node.description}"/>
			                    <f:param name="docId" value="#{node.identifier}"/>
			                </h:commandLink>
			            </h:panelGroup>
			        </f:facet>
			        <f:facet name="window">
			        	<h:panelGroup>
			        		<h:commandLink styleClass="#{t.nodeSelected ? 'documentSelected':'window'}" actionListener="#{t.setNodeSelected}"
			        					   onclick="window.open('#{treeBacker.url}')">
			        			<t:graphicImage value="/images/mWindow.gif" border="0"/>
			        			<h:outputText value="#{node.description}"/>
			        			<f:param name="windowId" value="#{node.identifier}" binding="#{treeBacker.param}" />
			        		</h:commandLink>
			        	</h:panelGroup>
			        </f:facet>
			        <f:facet name="process">
			        	<h:panelGroup>
			        		<h:commandLink immediate="true" styleClass="#{t.nodeSelected ? 'documentSelected':'process'}" actionListener="#{t.setNodeSelected}">
			        			<t:graphicImage value="/images/mProcess.gif" border="0"/>
			        			<h:outputText value="#{node.description}"/>
			        			<f:param name="processId" value="#{node.identifier}"/>
			        		</h:commandLink>
			        	</h:panelGroup>
			        </f:facet>
			        <f:facet name="workflow">
			        	<h:panelGroup>
			        		<h:commandLink immediate="true" styleClass="#{t.nodeSelected ? 'documentSelected':'workflow'}" actionListener="#{t.setNodeSelected}">
			        			<t:graphicImage value="/images/mWorkFlow.gif" border="0"/>
			        			<h:outputText value="#{node.description}"/>
			        			<f:param name="workflowId" value="#{node.identifier}"/>
			        		</h:commandLink>
			        	</h:panelGroup>
			        </f:facet>
			        <f:facet name="form">
			        	<h:panelGroup>
			        		<h:commandLink immediate="true" styleClass="#{t.nodeSelected ? 'documentSelected':'workflow'}" actionListener="#{t.setNodeSelected}">
			        			<t:graphicImage value="/images/mWindow.gif" border="0"/>
			        			<h:outputText value="#{node.description}"/>
			        			<f:param name="formId" value="#{node.identifier}"/>
			        		</h:commandLink>
			        	</h:panelGroup>
			        </f:facet>
			        <f:facet name="workbench">
			        	<h:panelGroup>
			        		<h:commandLink immediate="true" styleClass="#{t.nodeSelected ? 'documentSelected':'workbench'}" actionListener="#{t.setNodeSelected}">
			        			<t:graphicImage value="/images/mWorkBench.gif" border="0"/>
			        			<h:outputText value="#{node.description}"/>
			        			<f:param name="workbenchId" value="#{node.identifier}"/>
			        		</h:commandLink>
			        	</h:panelGroup>
			        </f:facet>
			        <!--  include to handle other types until implemented -->
			        <f:facet name="other">
			        	<h:panelGroup>
			        		<h:commandLink immediate="true" styleClass="#{t.nodeSelected ? 'documentSelected':'workbench'}" actionListener="#{t.setNodeSelected}">
			        			<t:graphicImage value="/images/mUserChoice.gif" border="0"/>
			        			<h:outputText value="#{node.description}"/>
			        			<f:param name="otherId" value="#{node.identifier}"/>
			        		</h:commandLink>
			        	</h:panelGroup>
			        </f:facet>
			    </t:tree2>
		 