<%@ taglib uri="/WEB-INF/taglib.tld" prefix="mt" %>
<%@ page import="org.compiere.util.DB" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="solstice.utils.PgiUtil" %>
<%@ page import="solstice.model.P_Period" %>
<%@ page import="org.compiere.util.Env" %>
<%@ page import="org.compiere.util.DB" %>
<%@ page import="org.compiere.util.TimeUtil" %>

<%
   solstice.custom.IUserInfo userInfo = (solstice.custom.IUserInfo)session.getAttribute("userInfo");
%>

<mt:securityCheck id="remplir">
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

P_Period currentPeriod = P_Period.getOpenPeriod(Env.getCtx(), null);
String dateInner = (request.getParameter("date") != null)? DB.TO_DATE(new Timestamp(PgiUtil.stringToDateV2(request.getParameter("date")).getTimeInMillis())) : TimeUtil.addDays(currentPeriod.getStartDate(),-1).toString().substring(0, 10);
String dateLimit = TimeUtil.addDays(currentPeriod.getStartDate(),-1).toString().substring(0, 10);

%>		

	<form name="mainForm" method="post">
		<input type="hidden" name="action">
		<p class="sectitle">Recherche</p>
		<table width="100%" border="0" cellspacing="1" cellpadding="1">
			<tr>
				<td class="tdlabel">Date</td>
			</tr>
			<tr>
				<td class="tdfield">
					<mt:simpleCalendar id="date" formName="mainForm" getPostValue="true" value="<%=dateInner %>"/>
				</td>
			</tr>
		</table>

<p class="sectitle2">Veuillez prendre note que le solde de banque est en date du <%=dateLimit %> (soit la dernière période de paie traitée).</p>



		
		<div align="right">
			<input type="button" onClick="search();" value="Lancer la recherche">
		</div>
		
			<p class="sectitle">R&eacute;sultats de la recherche</p>
	
			<mt:grid>
				<mt:queryBuilder checkMethod="solstice.custom.LivretTempsRecherche.conditionEmployeeBanque" groupBy="Employee.Value, Employee.Name, Credits.Value, Credits.Name, UOM.Value" orderBy="Employee.Name, Credits.Name">
					select 
					rtrim(Employee.Name) + ' (' + ltrim(rtrim(Employee.Value)) + ')' as Employee_Name, 
					rtrim(Credits.Name) + ' (' + ltrim(rtrim(Credits.Value)) + ')' as Credits_Name, 
					SUM( PMOVEMENTVARIATION ) Solde, 
					UOM.Value as Unit
					from P_Employee Employee 
					inner join P_Credits_Movement Movement on (Employee.P_Employee_ID = Movement.P_Employee_ID) 
					inner join P_Credits Credits on (Movement.P_Credits_ID = Credits.P_Credits_ID)
					inner join P_UOM UOM on (Credits.P_UOM_ID = UOM.P_UOM_ID and ISPRINTED = 'Y')
					inner join P_Employee_Credits ECredits 
							   on (
										Employee.P_Employee_ID = ECredits.P_Employee_ID 
									AND Credits.P_Credits_ID = ECredits.P_Credits_ID
									AND ECredits.ISACTIVE = 'Y'
									AND ECredits.Effectin = (
																select max(Effectin) from P_Employee_Credits PEC
																where 
																	PEC.P_Employee_ID = ECredits.P_Employee_ID
																AND PEC.P_Credits_ID = ECredits.P_Credits_ID
																AND PEC.Effectin  <= <%= dateInner %>
															)
								  )
					<mt:queryCondition id="employeeId">Employee.P_Employee_ID = <%= userInfo.getEmployeeId() %></mt:queryCondition>
					<mt:queryCondition id="date">PMovementDate <= <%= request.getParameter("date") != null ? DB.TO_DATE(new Timestamp(PgiUtil.stringToDateV2(request.getParameter("date")).getTimeInMillis())): "PMovementDate"%></mt:queryCondition>
					<mt:queryCondition id="startUp">PMovementDate < <%=DB.TO_DATE(currentPeriod.getStartDate())%></mt:queryCondition>					
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
