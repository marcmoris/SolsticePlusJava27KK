<%@ taglib uri="/WEB-INF/taglib.tld" prefix="mt" %>

<mt:securityCheck id="evaluationRendement_recherche">
<mt:pageTag id="evaluationRendement_recherche" supportCalendar="true">

	<script language="JavaScript">
		function validAllDate()
		{
			var bError = 0;
			if(validDate(document.mainForm.dateDu.value) == 1)
			{
				alert("La date de début n'est pas valide.\nVeuillez utiliser le format YYYY-MM-JJ.");			
				bError = 1;
			}
			if(validDate(document.mainForm.dateAu.value) == 1)
			{
				alert("La date de fin n'est pas valide.\nVeuillez utiliser le format YYYY-MM-JJ.");			
				bError = 1;			
			}
			return bError;
		}
		function search()
		{
			if(validAllDate() == 0)
			{
				if(!document.mainForm.cFormType.checked && !document.mainForm.cEmployeeId.checked
					&& !document.mainForm.cGestionnaireId.checked && !document.mainForm.cDate.checked)
				{
					alert("Vous devez obligatoirement choisir au moins une option de recherche");
				}
				else if(document.mainForm.cFormType.checked && (document.mainForm.formType.value == null || document.mainForm.formType.value == ""))
				{
					alert("En cochant l'option de recherche pour le type de formulaire, vous devez obligatoirement en sélectionner un");
				}
				else if(document.mainForm.cEmployeeId.checked && (document.mainForm.employeeId.value == null || document.mainForm.employeeId.value == ""))
				{
					alert("En cochant l'option de recherche pour l'employé évalué, vous devez obligatoirement en sélectionner un");
				}
				else if(document.mainForm.cGestionnaireId.checked && (document.mainForm.gestionnaireId.value == null || document.mainForm.gestionnaireId.value == ""))
				{
					alert("En cochant l'option de recherche pour le gestionnaire immédiat, vous devez obligatoirement en sélectionner un");
				}
				else if(document.mainForm.cDate.checked && (document.mainForm.dateDu.value == "" || document.mainForm.dateAu.value == ""))
				{
					alert("En cochant l'option de recherche pour la période de référence, vous devez obligatoirement choisir une date de début et une date de fin");
				}
				else
				{
					document.mainForm.submit();
				}
			}
		}
		
		var dateDu = new String();
		var dateAu = new String();
		function dateListener()
		{
			if(dateDu != document.mainForm.dateDu.value || dateAu != document.mainForm.dateAu.value)
			{
				dateDu = document.mainForm.dateDu.value;
				dateAu = document.mainForm.dateAu.value;
				onDateChanged();
			}
		}
		
		function onDateChanged()
		{
			document.mainForm.cDate.checked = (document.mainForm.dateDu.value != "" && document.mainForm.dateAu.value != "");
		}
	</script>

	<form name="mainForm" method="post">
		<input type="hidden" name="action" value="recherche">
		<table border="0" cellspacing="2" cellpadding="1" width="100%">
			<tr>
				<td class="tdlabel"><input type="checkbox" name="cFormType"></td>
				<td class="tdlabel">Type de formulaire</td>
				<td class="tdfield" colspan="2">
					<select name="formType" onChange="this.form.cFormType.checked = (this.value != null && this.value != '');">
						<option></option>
						<option value="GES">&Eacute;valuation des gestionnaires</option>
						<option value="PTB">&Eacute;valuation du personnel professionnal, technique et de bureau</option>
						<option value="OUV">&Eacute;valuation du personnel ouvrier</option>
					</select>
				</td>
			</tr>
			<tr>
				<td class="tdlabel"><input type="checkbox" name="cEmployeeId"></td>
				<td class="tdlabel">Nom de l'employ&eacute; &eacute;valu&eacute;</td>
				<td class="tdfield" colspan="2">
					<mt:comboBoxTag id="employeeId" allowNull="true" onChange="this.form.cEmployeeId.checked = (this.value != null && this.value != '');">
<%       if(!((solstice.custom.IUserInfo)session.getAttribute("userInfo")).hasPolicy("eval_allEmployees")) { %>
                  select distinct P_Employee.P_Employee_ID,
                                  rtrim(P_Employee.Name) + ' (' + rtrim(P_Employee.Value) + ')' as Name
                    from P_Employee
                    join P_ER_Evaluation
                      on P_ER_Evaluation.P_Employee_ID = P_Employee.P_Employee_ID
                   where P_ER_Evaluation.P_Employee_Gest_ID in (<%= String.valueOf(((solstice.custom.IUserInfo)session.getAttribute("userInfo")).getDistributionSQLFromER(new String[] {"P_Employee_ID"}, false)) %>)
                     and P_Employee.IsActive = 'Y'
                   order by Name
<%       } else { %>
                  select distinct P_Employee.P_Employee_ID,
                                  rtrim(P_Employee.Name) + ' (' + rtrim(P_Employee.Value) + ')' as Name
                    from P_Employee
                    join P_ER_Evaluation
                      on P_ER_Evaluation.P_Employee_ID = P_Employee.P_Employee_ID
                   where P_Employee.IsActive = 'Y'
                   order by Name
<%       } %>
					</mt:comboBoxTag>
				</td>
			</tr>
			<tr>
				<td class="tdlabel"><input type="checkbox" name="cGestionnaireId"></td>
				<td class="tdlabel">Nom du gestionnaire imm&eacute;diat</td>
				<td class="tdfield" colspan="2">
					<mt:comboBoxTag id="gestionnaireId" allowNull="true" onChange="this.form.cGestionnaireId.checked = (this.value != null && this.value != '');">
                  select distinct P_Employee.P_Employee_ID,
                                  rtrim(P_Employee.Name) + ' (' + rtrim(P_Employee.Value) + ')' as Name
                    from P_Distribution_Booklet
                    join P_Employee
                      on P_Employee.P_Employee_ID = P_Distribution_Booklet.P_Employee_Gestionnaire_ID
                     <%
                        // On doit vérifier si l'utilisateur a accès à la liste de tous les employés
                        if(!((solstice.custom.IUserInfo)session.getAttribute("userInfo")).hasPolicy("eval_allEmployees"))
                        {
                           out.println("where P_Distribution_Booklet.P_Distribution_Booklet_ID in ( " + ((solstice.custom.IUserInfo)session.getAttribute("userInfo")).getDistributionSQLFromER(new String[] {"P_Distribution_Booklet_ID"}, false) + ")");
                        }
                     %>
                   order by rtrim(P_Employee.Name) + ' (' + rtrim(P_Employee.Value) + ')'
					</mt:comboBoxTag>
				</td>
			</tr>
			<tr>
				<td class="tdlabel"><input type="checkbox" name="cDate"></td>
				<td class="tdlabel">P&eacute;riode de r&eacute;f&eacute;rence</td>
				<td class="tdfield">Du <input type="text" name="dateDu" /></td>
				<td class="tdfield">au <input type="text" name="dateAu" /></td>
			</tr>
			<tr>
				<td class="tdlabel" colspan="4" align="center"><input type="button" onClick="search();" value="Rechercher"></td>
			</tr>
		</table>
	</form>
	
	<mt:checkVisible checkMethod="solstice.custom.EvaluationRendementStaticMethod.showGrid">
	
	<%
	
	java.sql.Timestamp startDate = null;
	java.sql.Timestamp endDate = null;
	java.util.Calendar calendar = solstice.utils.PgiUtil.stringToDate(request.getParameter("dateDu"));
	if(calendar != null)
	   startDate = new java.sql.Timestamp(calendar.getTimeInMillis());
	   
	calendar = solstice.utils.PgiUtil.stringToDate(request.getParameter("dateAu"));
	if(calendar != null)
	   endDate = new java.sql.Timestamp(calendar.getTimeInMillis());
	
	%>
	
		<mt:grid>
			<mt:queryBuilder orderBy="Gest.Name, Emp.Name" checkMethod="solstice.custom.EvaluationRendementStaticMethod.useCondition">
					select ltrim(str(P_ER_Evaluation_ID)) as P_ER_Evaluation_ID,
					'<a href="' +
					(case Form.FormType
					  when 'GES' then 'evalges.jsp'
					  when 'PTB' then 'evalptb.jsp'
					  else 'evalout.jsp'
					end)
					+ '?evaluationId=' + ltrim(str(P_ER_Evaluation_ID)) + '">' + Emp.Name + '</a>' as Emp, Gest.Name as Gest, Eval.StartingRefPeriodDate, Eval.EndingRefPeriodDate
					from (P_ER_Form Form inner join (P_ER_Evaluation Eval inner join P_Employee Emp
					on Eval.P_Employee_ID = Emp.P_Employee_ID)
					on Form.P_ER_Form_ID = Eval.P_ER_Form_ID) inner join P_Employee Gest
					on Eval.P_Employee_Gest_ID = Gest.P_Employee_ID
				<mt:queryCondition id="cFormType">Form.FormType = '<%= request.getParameter("formType") %>'</mt:queryCondition>
				<mt:queryCondition id="cEmployeeId">Eval.P_Employee_ID = <%= request.getParameter("employeeId") %></mt:queryCondition>
				<mt:queryCondition id="cGestionnaire">Eval.P_Employee_Gest_ID = <%= request.getParameter("gestionnaireId") %></mt:queryCondition>
				<mt:queryCondition id="cDate">Eval.StartingRefPeriodDate >= <%= org.compiere.util.DB.TO_DATE(startDate) %></mt:queryCondition>
				<mt:queryCondition id="cDate">Eval.EndingRefPeriodDate <= <%= org.compiere.util.DB.TO_DATE(endDate) %></mt:queryCondition>
				<mt:staticQueryCondition>
				         <%
                        // On doit vérifier si l'utilisateur a accès à la liste de tous les employés
                        if(!((solstice.custom.IUserInfo)session.getAttribute("userInfo")).hasPolicy("eval_allEmployees"))
                        {
                           out.println("Eval.P_Employee_Gest_ID in (" + ((solstice.custom.IUserInfo)session.getAttribute("userInfo")).getDistributionSQLFromER(new String[] {"P_Employee_ID"}, false) + ")");
                        }
                        else
                        {
                           out.println("1 = 1");
                        }
                     %>
				</mt:staticQueryCondition>
			</mt:queryBuilder>
			
			<mt:field id="P_ER_Evaluation_ID" name="&Eacute;valuation" type="Number"/>
			<mt:field id="Emp" name="Employ&eacute; &eacute;valu&eacute;" type="String"/>
			<mt:field id="Gest" name="Gestionnaire imm&eacute;diat" type="String"/>
			<mt:field id="StartingRefPeriodDate" name="D&eacute;but de la p&eacute;riode" type="Date" format="yyyy-MM-dd"/>
			<mt:field id="EndingRefPeriodDate" name="Fin de la p&eacute;riode" type="Date" format="yyyy-MM-dd"/>
		</mt:grid>
	
	</mt:checkVisible>

</mt:pageTag>

<script language="JavaScript">
	setInterval('dateListener()', 1);
</script>
</mt:securityCheck>
