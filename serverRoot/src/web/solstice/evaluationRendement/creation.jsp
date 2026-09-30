<%@ taglib uri="/WEB-INF/taglib.tld" prefix="mt" %>
<%@ page import="java.sql.*" %>
<%@ page import="org.compiere.util.DB" %>

<mt:securityCheck id="evaluationRendement_creation">
<mt:pageTag id="evaluationRendement_creation" supportCalendar="true" validationClass="solstice.custom.ERCreationValidation">

	<form name="mainForm" method="post">
      <input type="hidden" name="action">
		<table border="0" cellspacing="2" cellpadding="0" width="100%">
			<tr>
				<td class="tdlabel">Type de formulaire</td>
				<td class="tdfield" colspan="2">
					<select name="formType" onChange="this.form.action.value = 'formTypeChanged'; this.form.submit();">
					   <option <%= request.getParameter("formType") == null || request.getParameter("formType").equals("") ? "selected" : "" %>></option>
						<option value="GES" <%= request.getParameter("formType") != null && request.getParameter("formType").equals("GES") ? "selected" : "" %>>&Eacute;valuation des gestionnaires</option>
						<option value="PTB" <%= request.getParameter("formType") != null && request.getParameter("formType").equals("PTB") ? "selected" : "" %>>&Eacute;valuation du personnel professionnel, technique et de bureau</option>
						<option value="OUV" <%= request.getParameter("formType") != null && request.getParameter("formType").equals("OUV") ? "selected" : "" %>>&Eacute;valuation du personnel ouvrier</option>
					</select>
				</td>
			</tr>
			<tr>
				<td class="tdlabel">Nom de l'employ&eacute; &eacute;valu&eacute;</td>
				<td class="tdfield" colspan="2">
				   <%
				      if(request.getParameter("formType") != null
				         && !request.getParameter("formType").equals(""))
				      { 
   				      %>
      					<mt:comboBoxTag id="employeeId" allowNull="true" getPostValue="<%= request.getParameter("action") != null && request.getParameter("action").equals("employeeChanged") %>">
                        select distinct *
                        from (
                        select distinct P_Employee.P_Employee_ID,
                                        rtrim(P_Employee.Name) + ' (' + rtrim(P_Employee.Value) + ')' as Name
                          from P_Occupation_Group
                          join P_Employee
                          join P_Employee_Distribution_Booklet a
                          join P_Distribution_Booklet
                            on P_Distribution_Booklet.P_Distribution_Booklet_ID = a.P_Distribution_Booklet_ID
                            on P_Employee.P_Employee_ID = a.P_Employee_ID
                            on P_Employee.P_Occupation_Group_ID = P_Occupation_Group.P_Occupation_Group_ID
                         where P_Employee.IsActive = 'Y'
                           and P_Occupation_Group.EvaluationType = '<%= request.getParameter("formType") %>'
                           and dateadd(day, 0, datediff(day,0,a.Start_Date)) <= getdate()
                           and getdate() between dateadd(day, 0, datediff(day,0,a.Start_Date)) and isnull(dateadd(day, 0, datediff(day,0,a.End_Date)), getdate())
                           and (P_Employee.datelayoff is null or
								P_Employee.datelayoff < isnull(P_Employee.daterehired, P_Employee.datehired))                           
                        <%
                           // On doit vérifier si l'utilisateur a accès à la liste de tous les employés
                           if(!((solstice.custom.IUserInfo)session.getAttribute("userInfo")).hasPolicy("eval_allEmployees"))
                           {
                              out.println("and P_Distribution_Booklet.P_Distribution_Booklet_ID in (" + ((solstice.custom.IUserInfo)session.getAttribute("userInfo")).getDistributionSQLFromER(new String[] {"P_Distribution_Booklet_ID"}, false) + ")");
                           }
                        %>
                        union all
                        select distinct P_Employee.P_Employee_ID,
                                        rtrim(P_Employee.Name) + ' (' + rtrim(P_Employee.Value) + ')' as Name
                          from P_Employee
                          join P_ER_Evaluation
                            on P_ER_Evaluation.P_Employee_ID = P_Employee.P_Employee_ID
                         where P_ER_Evaluation.Closed = 'N'
                         AND P_ER_Evaluation.EvaluationType = '<%= request.getParameter("formType") %>' 
                        <%
                           // On doit vérifier si l'utilisateur a accès à la liste de tous les employés
                           if(!((solstice.custom.IUserInfo)session.getAttribute("userInfo")).hasPolicy("eval_allEmployees"))
                           {
                              out.println("and P_ER_Evaluation.P_Employee_Gest_ID in ( " + ((solstice.custom.IUserInfo)session.getAttribute("userInfo")).getDistributionSQLFromER(new String[] {"P_Employee_ID"}, false) + ")");
                           }
                        %>
                        ) Employees
                        order by Name
      					</mt:comboBoxTag>
      					<%
				      }
				      else
				      {
   				      %>
   				      <select disabled><option>S&eacute;lection de l'employ&eacute;</option></select>
   				      <%
				      }
				   %>
				</td>
			</tr>
			<tr>
				<td class="tdlabel">Copi&eacute; &eacute;valuation de l'ann&eacute;e pr&eacute;c&eacute;dente</td>
				<td class="tdfield" colspan="2">
		            <input type="checkbox" name="cIsCopy" id="cIsCopy"  />
				</td>
			</tr>
			<tr>
				<td colspan="3" align="right"><input type="submit" onClick="this.form.action.value = 'create';" value="&Eacute;valuer l'employ&eacute;"></td>
			</tr>
		</table>
		
	</form>

</mt:pageTag>

</mt:securityCheck>
