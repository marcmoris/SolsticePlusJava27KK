<%@ taglib uri="/WEB-INF/taglib.tld" prefix="mt" %>
<%@ page import="org.compiere.util.DB" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="solstice.utils.PgiUtil" %>
<%@ page import="solstice.model.P_Period" %>
<%@ page import="org.compiere.util.Env" %>
<%@ page import="org.compiere.util.DB" %>
<%@ page import="org.compiere.util.TimeUtil" %>

<mt:securityCheck id="livrbanklist">
<mt:pageTag id="livrbanklist" supportCalendar="true">

	<script language="JavaScript">
		function search()
		{
			if(document.mainForm.date.value != "")
			{
				document.mainForm.action.value = 'search';
				document.mainForm.submit();
			}
			else
			{
				alert("Vous devez obligatoirement saisir une date");
			}
		}
	</script> 

<%

P_Period PeriodLimit = P_Period.getOpenPeriod(Env.getCtx(), null);
String dateLimit = TimeUtil.addDays(PeriodLimit.getStartDate(),-1).toString().substring(0, 10);

%>		

	<form name="mainForm" method="post">
		<input type="hidden" name="action">
		<p class="sectitle">Recherche</p>
		<table width="100%" border="0" cellspacing="1" cellpadding="1">
			<tr>
				<td class="tdlabel">Employ&eacute;</td>
				<td class="tdlabel">Banque</td>
				<td class="tdlabel">Date</td>
			</tr>
			<tr>
				<td class="tdfield">
					<mt:comboBoxTag id="employeeId" getPostValue="true">
						select P_Employee_ID, ltrim(rtrim(Name)) + ' (' + ltrim(rtrim(Value)) + ')'
						from P_Employee
						where IsActive = 'Y'
						order by Name
					</mt:comboBoxTag>
				</td>
				<td class="tdfield">
					<mt:comboBoxTag id="banqueId" getPostValue="true">
						select P_Credits_ID, ltrim(rtrim(Name)) + ' (' + ltrim(rtrim(Value)) + ')'
						from P_Credits
						where IsActive = 'Y'
						order by Name
					</mt:comboBoxTag>
				</td>
				<td class="tdfield">
					<mt:simpleCalendar id="date" formName="mainForm" getPostValue="true"/>
				</td>
			</tr>
		</table>

<p class="sectitle2">Veuillez prend note que le solde de banque est en date du <%=dateLimit %> (soit la dernière période de paie traité)</p>
		
		<div align="right">
			<input type="button" onClick="search();" value="Lancer la recherche">
		</div>
		
			<p class="sectitle">R&eacute;sultats de la recherche</p>
<%

P_Period currentPeriod = null;
if(request.getParameter("periodId") != null && !request.getParameter("periodId").equals(""))
{
	currentPeriod = P_Period.get(Env.getCtx(), Integer.parseInt(request.getParameter("periodId")), null);
	if(currentPeriod == null)
	{
		System.out.println("Période ID invalide");
	}
}


%>			
			<mt:grid>
				<mt:queryBuilder checkMethod="solstice.custom.LivretTempsRecherche.conditionBanque" groupBy="Employee.Value, Employee.Name, Credits.Value, Credits.Name, UOM.Value" orderBy="Employee.Name, Credits.Name">
						select rtrim(Employee.Name) + ' (' + ltrim(rtrim(Employee.Value)) + ')' as Employee_Name, 
						rtrim(Credits.Name) + ' (' + ltrim(rtrim(Credits.Value)) + ')' as Credits_Name, 
						SUM( PMOVEMENTVARIATION ) Solde, UOM.Value as Unit
						from P_Employee Employee inner join (P_Credits_Movement Movement  inner join (P_Credits Credits inner join P_UOM UOM
						on Credits.P_UOM_ID = UOM.P_UOM_ID)
						on Movement.P_Credits_ID = Credits.P_Credits_ID)
						on Employee.P_Employee_ID = Movement.P_Employee_ID
					<mt:queryCondition id="employeeId">Employee.P_Employee_ID = <%= request.getParameter("employeeId") %></mt:queryCondition>
					<mt:queryCondition id="banqueId">Credits.P_Credits_ID = <%= request.getParameter("banqueId") %></mt:queryCondition>
					<mt:queryCondition id="date">PMovementDate <= <%= request.getParameter("date") != null ? DB.TO_DATE(new Timestamp(PgiUtil.stringToDateV2(request.getParameter("date")).getTimeInMillis())): "PMovementDate"%></mt:queryCondition>
					<mt:queryCondition id="startUp">PMovementDate <= <%=DB.TO_DATE(currentPeriod.getEndDate())%></mt:queryCondition>
				</mt:queryBuilder>
				
				<mt:field id="Employee_Name" name="Employ&eacute;" type="String"/>
				<mt:field id="Credits_Name" name="Banque" type="String"/>
				<mt:fieldGroup name="Solde">
					<mt:field id="Solde" type="Number" format="#0.00"/>
					<mt:field id="Unit" type="String"/>
				</mt:fieldGroup>
			</mt:grid>
	</form>

</mt:pageTag>
</mt:securityCheck>

