<%@ taglib uri="/WEB-INF/taglib.tld" prefix="mt" %>
<%@ page import="solstice.custom.IUserInfo" %>
<%@ page import="java.sql.*" %>
<%@ page import="org.compiere.util.DB" %>

<%
   // Puisqu'on doit faire une validation particulière pour la convention collective de
   // l'employé connecté, on ne peut pas utiliser le page tag pour générer automatiquement
   // cette page.
%>

<mt:securityCheck id="permisHorodateur_index">
	<html>
	<head>
		<meta name="vs_targetSchema" content="http://schemas.microsoft.com/intellisense/ie5">
		<link rel="stylesheet" type="text/css" href="styles/siq.css">
		<title>SIQ - Menu Principal</title>
	</head>
	<body >
		
		<table width="900" align="center" border="0" cellspacing="2" cellpadding="0">
			<tr>
				<td align="center" valign="middle">
					<img src="images/LogoBAN.jpg" width="211" height="100" border="0">
				</td>

				<td align="center" valign="middle">
					<img src="images/SIQBAN.GIF" width="450" height="57" border="0">
				</td>
			</tr>
			<tr>
				<td colspan="2" align="center" valign="middle">
					<div class="apptitle">Permis horodateur</div>
				</td>

			</tr>
			<tr>
				<td colspan="2" class="line"></td>
			</tr>
			<tr>
				<td colspan="2">
				   
					<a href="/solstice/index.jsp" class="path">Portail SIQ</a> &middot;&gt;<a class="path" href="index.jsp">Menu Principal</a>

				</td>
			</tr>
			<tr>
				<td colspan="2">
					<div class="pagetitle">Menu Principal</div>
				</td>
			</tr>
			<tr>

				<td colspan="2">
				   <ul>
				      <%
				         // Pour remplir un permis horodateur, l'utilisateur doit d'abord passer le
				         // check de sécurité de Solstice et doit ensuite passer un check en fonction
				         // de la convention collective.
				         IUserInfo userInfo = (IUserInfo)session.getAttribute("userInfo");
				         if(userInfo.hasPolicy("permisHorodateur_remplir"))
				         {
   				         boolean access = true;
   				         if(!userInfo.hasPolicy("permisHorodateur_afficherListeEmploye"))
   				         {
         				      PreparedStatement stmt = DB.prepareStatement(
         				         "select assignmentParam.P_Collective_Labour_Agr_ID" +
                              "  from P_Assignment assignment" +
                              "  join P_Assignment_Param assignmentParam" +
                              "    on assignmentParam.P_Assignment_ID = assignment.P_Assignment_ID" +
                              " where assignment.P_Employee_ID = " + userInfo.getEmployeeId() +
                              "   and assignment.IsActive = 'Y'" +
                              "   and assignmentParam.IsActive = 'Y'" +
                              "   and getdate() between isnull( assignment.StartDate, getdate() ) and isnull( assignment.EndDate, getdate() )" +
                              "   and getdate() between isnull( assignmentParam.EffectIn, getdate() ) and isnull( assignmentParam.EffectTo, getdate() )" +
                              "   order by AssignmentType");
         				         
         				      ResultSet rs = stmt.executeQuery();
         				      
         				      access = rs.next() ? solstice.model.P_PunchCard_Collective_Labour_Agr.canAccess(rs.getInt("P_Collective_Labour_Agr_ID")) : false;
         				      
         				      rs.close();
         				      stmt.close();
      				      }
      				      
      				      if(access)
      				         out.println("<li><a href=\"selectemployee.jsp\" class=\"menu\">Remplir un permis horodateur</a></li>");
				         }
				         
				         // Pour le reste des liens, on doit seulement vérifier la sécurité de Solstice
				         if(userInfo.hasPolicy("permisHorodateur_consulter"))
				            out.println("<li><a href=\"consulter.jsp\" class=\"menu\">Consulter la liste des permis horodateur</a></li>");
				            
				         if(userInfo.hasPolicy("permisHorodateur_consulteradm"))
				            out.println("<li><a href=\"consulteradm.jsp\" class=\"menu\">Consulter la liste des permis horodateur à approuver</a></li>");
				            
				         if(userInfo.hasPolicy("permisHorodateur_administration"))
				            out.println("<li><a href=\"administration.jsp\" class=\"menu\">Administration</a></li>");
				      %>
				   </ul>
				</td>
			</tr>
			<tr>
				<td colspan="2">

				</td>
			</tr>
			<tr>
				<td colspan="2">
					<div style="padding-top: 20px" class="siqname">%%Organisation%%</div>

				</td>
			</tr>
			<tr>
				<td colspan="2" class="line"> </td>
			</tr>
		</table>
	</body>
</html>

</mt:securityCheck>