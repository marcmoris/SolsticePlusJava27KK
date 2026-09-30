<%@ taglib uri="/WEB-INF/taglib.tld" prefix="mt" %>

<mt:securityCheck id="livrsommaire">
<mt:pageTag id="livrsommaire" >

	<script language="JavaScript">
		function search()
		{
			document.mainForm.action.value = 'search';
			document.mainForm.submit();
		}
	</script> 

	<form name="mainForm" method="post">
		<input type="hidden" name="action">
		<p class="sectitle">Recherche</p>
		<table width="100%" border="0" cellspacing="1" cellpadding="1">
			<tr>
				<td class="tdlabel">Distribution</td>
				<td class="tdlabel">Employ&eacute;</td>
				<td class="tdlabel">P&eacute;riode</td>
			</tr>
			<tr>
				<td class="tdfield">
					<mt:comboBoxTag id="distributionId" getPostValue="true" allowNull="true" onChange="this.form.submit();">
						<%= ((solstice.custom.IUserInfo)session.getAttribute("userInfo")).getDistributionWebSQL(new String[] {"P_Distribution_Booklet_ID", "rtrim(Value) + ' - ' + Name"}, false) %>
					</mt:comboBoxTag>
				</td>
				<td class="tdfield">
					<mt:comboBoxTag id="employeeId" getPostValue="true" allowNull="true">
						select P_Employee_ID, ltrim(rtrim(Name)) + ' (' + ltrim(rtrim(Value)) + ')'
						from P_Employee
						where IsActive = 'Y'
						<%= request.getParameter("distributionId") != null && !request.getParameter("distributionId").equals("") ? "and P_Distribution_Booklet_ID = " + request.getParameter("distributionId") : "" %>
						order by Name
					</mt:comboBoxTag>
				</td>
				<td class="tdfield">
					<mt:comboBoxTag id="periodId" getPostValue="true" allowNull="false">
						select P_Period_ID, Name
						from P_Period
						where StartDate <= (select min(StartDate) from P_Period where PeriodStatus = 'O')
						and IsActive = 'Y'
						order by StartDate desc
					</mt:comboBoxTag>
				</td>
			</tr>
		</table>
		<div align="right">
			<input type="button" onClick="search();" value="Lancer la recherche">
		</div>
		
		<mt:checkVisible checkMethod="solstice.custom.LivretTempsRecherche.searchBanque">
		
			<p class="sectitle">R&eacute;sultats de la recherche</p>
			
			<mt:grid>
				<mt:queryBuilder checkMethod="solstice.custom.LivretTempsRecherche.conditionBanque" orderBy="distributionBooklet.Value, employee.Name" groupBy="distributionBooklet.Value, booklet.P_Booklet_ID, employee.Value, employee.Name, gainGroup.Value, gainGroup.Name">
					select distributionBooklet.Value as Distr, rtrim(employee.Name) + ' (' + rtrim(employee.Value) + ') </td><td class="tdfont"> <b>Total : ' +
					replace(cast(convert(decimal(12,2), (
					  select sum(P_Booklet_Detail.DayQty)
					  from P_Booklet_Detail 
					  inner join P_Gain  on P_Booklet_Detail.P_Gain_ID = P_Gain.P_Gain_ID
					  inner join P_Gain_Group gainGroup on P_gain.P_Gain_Group_ID = gainGroup.P_Gain_Group_ID
					  where P_Booklet_Detail.P_Booklet_ID = booklet.P_Booklet_ID
					  and gainGroup.IncludeInTotal = 'Y'
					  and P_Gain.P_Gain_Group_ID in (
					    select g.P_Gain_Group_ID
					    from P_Booklet_Detail bd inner join P_Gain g
					    on bd.P_Gain_ID = g.P_Gain_ID
					    where bd.P_Booklet_ID = P_Booklet_Detail.P_Booklet_ID 
					  )
					)) as nvarchar(15)), '.', ',') + '</b>' as Name,
					rtrim(gainGroup.Name)  as "Group", sum(bookletDetail.DayQty) as Solde
					from ((P_Booklet booklet inner join P_Booklet_Detail bookletDetail
					on booklet.P_Booklet_ID = bookletDetail.P_Booklet_ID) inner join (P_Employee employee inner join P_Distribution_Booklet distributionBooklet
					on employee.P_Distribution_Booklet_ID = distributionBooklet.P_Distribution_Booklet_ID)
					on booklet.P_Employee_ID = employee.P_Employee_ID) inner join (P_Gain gain inner join P_Gain_Group gainGroup
					on gain.P_Gain_Group_ID = gainGroup.P_Gain_Group_ID)
					on bookletDetail.P_Gain_ID = gain.P_Gain_ID
					<mt:queryCondition id="distributionId">distributionBooklet.P_Distribution_Booklet_ID = <%= request.getParameter("distributionId") %></mt:queryCondition>
					<mt:queryCondition id="employeeId">employee.P_Employee_ID = <%= request.getParameter("employeeId") %></mt:queryCondition>
					<mt:queryCondition id="periodId">booklet.P_Period_ID = '<%= request.getParameter("periodId") %>'</mt:queryCondition>
				</mt:queryBuilder>
				
				<mt:field id="Distr" group="true" name="Distr." type="String"/>
				<mt:field id="Name" group="true" name="Nom" type="String">
					<mt:fieldStyle alt="false">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="75%" class="tdfont">
									@@
								</td>
							</tr>
						</table>
					</mt:fieldStyle>
				</mt:field>
				<mt:field id="Group" name="Groupe" type="String"/>
				<mt:field id="Solde" name="Total" type="Number" format="#0.00"/>
			</mt:grid>
			
		</mt:checkVisible>
	</form>

</mt:pageTag>
</mt:securityCheck>
