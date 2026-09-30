<%@ taglib uri="/WEB-INF/taglib.tld" prefix="mt" %>

<mt:securityCheck id="livravancement">
<mt:pageTag id="livravancement" >

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
				<td class="tdlabel">P&eacute;riode</td>
				<td class="tdlabel">Code de distribution</td>
				<td class="tdlabel">Statut</td>
			</tr>
			<tr>
				<td class="tdfield">
					<mt:comboBoxTag id="periodId" getPostValue="true" allowNull="false">
						select P_Period_ID, ltrim(rtrim(Name))
						from P_Period
						where IsActive = 'Y'
						and StartDate <= (select min(StartDate) from P_Period where PeriodStatus = 'O')
						order by StartDate desc
					</mt:comboBoxTag>
				</td>
				<td class="tdfield">
					<mt:comboBoxTag id="distributionId" getPostValue="true" allowNull="true">
						<%= ((solstice.custom.IUserInfo)session.getAttribute("userInfo")).getDistributionWebSQL(new String[] {"P_Distribution_Booklet_ID", "rtrim(Value) + ' - ' + Name"}, false) %>
					</mt:comboBoxTag>
				</td>
				<td class="tdfield">
					<mt:comboBoxTag id="statusId" getPostValue="true" allowNull="false" selectedValue="I">
					    select '', '' union all
						select AD_Ref_List.Value, rtrim(AD_Ref_List_TRL.Name) + ' (' + rtrim(AD_Ref_List.Value) + ')'
						from AD_Ref_List 
						inner join AD_Reference on AD_Ref_List.AD_Reference_ID = AD_Reference.AD_Reference_ID
						inner join AD_Ref_List_TRL on AD_Ref_List.AD_Ref_List_ID = AD_Ref_List_TRL.AD_Ref_List_ID
						where AD_Reference.Name like 'P_BookletStatus'
						and AD_Ref_List_TRL.AD_Language = 'fr_CA'
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
				<mt:queryBuilder orderBy="distributionBooklet.Value, employee.Name" checkMethod="solstice.custom.LivretTempsRecherche.conditionBanque">
					select rtrim(distributionBooklet.Value) + ' - ' + distributionBooklet.Name as Distr,  secretary.name secretary, p_post.phone + ' poste : ' + p_post.phoneExt as phone, (
						select rtrim(AD_Ref_List_TRL.Name) + ' (' + rtrim(AD_Ref_List.Value) + ')'
						from (AD_Ref_List inner join AD_Reference
						on AD_Ref_List.AD_Reference_ID = AD_Reference.AD_Reference_ID) inner join AD_Ref_List_TRL
						on AD_Ref_List.AD_Ref_List_ID = AD_Ref_List_TRL.AD_Ref_List_ID
						where AD_Reference.Name like 'P_BookletStatus'
						and AD_Ref_List_TRL.AD_Language = 'fr_CA'
						and AD_Ref_List.Value = booklet.TimeSheetStatus
					) as TimeSheetStatus, rtrim(employee.Name) + ' (' + rtrim(employee.Value) + ')' as Employee
					from P_Distribution_Booklet distributionBooklet 
					inner join (P_Employee employee 
					inner join P_Booklet booklet on employee.P_Employee_ID = booklet.P_Employee_ID) on distributionBooklet.P_Distribution_Booklet_ID = employee.P_Distribution_Booklet_ID
                    left outer join P_Employee secretary on  distributionBooklet.P_Employee_Secretary_id = Secretary.p_employee_id
                    left outer join p_assignment on p_assignment.assignmenttype = 'P' and  p_assignment.p_employee_id = Secretary.p_employee_id
                    left outer join p_post on p_post.p_post_id = p_assignment.p_post_id 

					<mt:staticQueryCondition>distributionBooklet.P_Distribution_Booklet_ID in (<%= ((solstice.custom.IUserInfo)session.getAttribute("userInfo")).getDistributionSQL(new String[] {"P_Distribution_Booklet_ID"}, false) %>)</mt:staticQueryCondition>
					<mt:queryCondition id="periodId">booklet.P_Period_ID = <%= request.getParameter("periodId") %></mt:queryCondition>
					<mt:queryCondition id="distributionId">distributionBooklet.P_Distribution_Booklet_ID = <%= request.getParameter("distributionId") %></mt:queryCondition>
					<mt:queryCondition id="statusId">booklet.TimeSheetStatus = '<%= request.getParameter("statusId") %>'</mt:queryCondition>
					
				</mt:queryBuilder>
				
				<mt:field id="Distr" name="Distribution" type="String"/>
				<mt:field id="secretary" name="Secrétaire" type="String"/>
				<mt:field id="phone" name="Téléphone" type="String"/>
				<mt:field id="TimeSheetStatus" name="Statut" type="String"/>
				<mt:field id="Employee" name="Employé" type="String"/>
			</mt:grid>
			
		</mt:checkVisible>
	</form>
	
</mt:pageTag>
</mt:securityCheck>
