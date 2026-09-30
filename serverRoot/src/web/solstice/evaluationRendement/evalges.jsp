<%@ taglib uri="/WEB-INF/taglib.tld" prefix="mt" %>
<%@ page import="java.util.Hashtable" %>

<mt:securityCheck id="evaluationRendement_evalges">
<mt:erPage id="evaluationRendement_evalges" validationClass="solstice.custom.EvaluationRendement">

<%

   Hashtable evaluation = solstice.custom.EvaluationRendement.createMainForm(pageContext, "GES");

   String standardLock = "";
   String standardLock2 = "";
   boolean boolLock = false;
   if("true".equals(evaluation.get("isClosed")) || 
   	!((solstice.custom.IUserInfo)session.getAttribute("userInfo")).hasPolicy("evaluationRendement_evaluation"))
   {
	   standardLock2 = "disabled=\"disabled\"";
	   standardLock = "readonly=\"readonly\"";
	   boolLock = true;
   }
%>

	<script language="JavaScript">		
		function validAllDate()
		{
			var bError = 0;
			if(validDate(document.mainForm.startDate.value) == 1)
			{
				alert("La date de début de période de référence n'est pas valide.\nVeuillez utiliser le format YYYY-MM-JJ.");			
				bError = 1;
			}
			if(validDate(document.mainForm.endDate.value) == 1)
			{
				alert("La date de fin de période de référence n'est pas valide.\nVeuillez utiliser le format YYYY-MM-JJ.");			
				bError = 1;			
			}
			if(validDate(document.mainForm.BossExpectencySignoff.value) == 1)
			{
				alert("La date pour la signature des attentes n'est pas valide.\nVeuillez utiliser le format YYYY-MM-JJ.");			
				bError = 1;			
			}
			if(validDate(document.mainForm.EmployeeExpectencySignoff.value) == 1)
			{
				alert("La date pour la signature des attentes n'est pas valide.\nVeuillez utiliser le format YYYY-MM-JJ.");			
				bError = 1;			
			}
			if(validDate(document.mainForm.meeting_meetingDate0.value) == 1)
			{
				alert("La date pour la première rencontre de suivi n'est pas valide.\nVeuillez utiliser le format YYYY-MM-JJ.");			
				bError = 1;			
			}
			if(validDate(document.mainForm.meeting_meetingDate1.value) == 1)
			{
				alert("La date pour la deuxième rencontre de suivi n'est pas valide.\nVeuillez utiliser le format YYYY-MM-JJ.");			
				bError = 1;			
			}
			if(validDate(document.mainForm.BossEvaluationSignoff.value) == 1)
			{
				alert("La date pour la signature de l'évaluation n'est pas valide.\nVeuillez utiliser le format YYYY-MM-JJ.");			
				bError = 1;			
			}
			if(validDate(document.mainForm.EmployeeEvaluationSignoff.value) == 1)
			{
				alert("La date pour la signature de l'évaluation n'est pas valide.\nVeuillez utiliser le format YYYY-MM-JJ.");			
				bError = 1;			
			}
			if(validDate(document.mainForm.BossBossEvaluationSignoff.value) == 1)
			{
				alert("La date pour la signature de l'évaluation n'est pas valide.\nVeuillez utiliser le format YYYY-MM-JJ.");			
				bError = 1;			
			}
			
			return bError;
		}
		
		function saveForm()
		{
   		if(document.mainForm.managerId.value == "")
   		{
      		alert("Vous devez obligatoirement choisir un gestionnaire");
   		}
   		else
   		{
   			if(validAllDate() == 0)
   			{
   				if(confirm("Voulez vous vraiment sauvegarder l'évaluation?"))
   				{
                  document.mainForm.action.value = "save";
                  document.mainForm.scroll.value = "0";
                  document.mainForm.submit();
   				}
   			}
   		}
		}
		
		function codeRemChanged()
		{
			document.mainForm.isExpectationCompleted.checked = true;
			document.mainForm.isClosed.checked = true;
			document.getElementById("btnajouter").href = "#";
		}

		function changeIsComplete()
		{
			if(document.mainForm.isExpectationCompleted.checked == true)
			{
				if(document.mainForm.BossExpectencySignoff.value == "" ||
				document.mainForm.EmployeeExpectencySignoff.value == "")
				{
					document.mainForm.isExpectationCompleted.checked = false;
					alert("Les dates de signature des attentes doivent être saisies si on veut compléter les attentes.");
				}
			}
		}

		function changeIsClose()
		{
			if(document.mainForm.isClosed.checked == true)
			{
				if(document.mainForm.BossEvaluationSignoff.value == "" ||
				document.mainForm.EmployeeEvaluationSignoff.value == "" ||
				document.mainForm.BossBossEvaluationSignoff.value == "")
				{
					document.mainForm.isClosed.checked = false;
					alert("Les dates de signature de l'évaluation doivent être saisies si on veut fermer l'évaluation.");
				}
			}
		}
	</script>


	<input type="hidden" name="action">
	<input type="hidden" name="jobTitleId" value="<%= (String)evaluation.get("jobTitleId") %>">
	<input type="hidden" name="jobTypeId" value="<%= (String)evaluation.get("jobTypeId") %>">
	<input type="hidden" name="employeeId" value="<%= (String)evaluation.get("employeeId") %>">
	<input type="hidden" name="evaluationId" value="<%= (String)evaluation.get("evaluationId") %>">
	<input type="hidden" name="formId" value="<%= (String)evaluation.get("formId") %>">
	<input type="hidden" name="postId" value="<%= (String)evaluation.get("postId") %>">

	<TABLE id="TableH" cellSpacing="0" cellPadding="0" width="765" align="center" border="0">
		<tr>
			<td width="211"><IMG height="100" alt="text" src="images/LogoBAN.jpg" width="211">
			</td>
			<td bgColor="#003399"><IMG height="4" alt="" src="images/spacer.gif" width="1" border="0">
				<span class="font">Évaluation du rendement des gestionnaires</span>
			</td>
		</tr>
	</TABLE>
		
	<TABLE id="Table7" cellSpacing="0" cellPadding="0" width="765" align="center" border="0">
		<tr>
			<td align=right>
				<a href="index.jsp">retour au menu principal</a>
				<img src="images/spacer.gif" alt="" height="30" width="1" border="0">
			</td>
		</tr>
		<tr>
			<td>
				<img src="images/spacer.gif" alt="" height="4" width="1" border="0">
			</td>
		</tr>
		<tr>
		   <td align="right">
		      Statut : <input type="text" name="status" size="30" value="<%= (String)evaluation.get("status") %>" readonly>
		   </td>
		</tr>
		<tr>
			<td>
				<img src="images/spacer.gif" alt="" height="4" width="1" border="0">
			</td>
		</tr>
	</table>
	<!-- Section 1 -->
	<TABLE id="TableSection1" cellSpacing="0" cellPadding="0" width="765" align="center" border="0">
		<tr>
			<td bgColor="#003399" height="20"><IMG height="4" alt="" src="images/spacer.gif" width="1" border="0">
				<span class="titlefont">Section 1 - Identification </span>
			</td>
		</tr>
		<tr>
			<td><IMG height="10" alt="" src="images/spacer.gif" width="1" border="0">
				<table id="TableSection1" borderColor="black" height="0" cellSpacing="0" cellPadding="3"
					width="765" border="1">
					<tr>
						<td colSpan="2" class="boldtext">
						   Nom : <input type="text" name="employeeName" size="30" value="<%= (String)evaluation.get("employeeName") %>" readonly>
						</td>
						<td class="boldtext">
						   Titre de l'emploi : <input type="text" name="employeeJobTitle" size="35" value="<%= (String)evaluation.get("employeeJobTitle") %>" readonly>
						</td>
					</tr>
					<tr>
						<td class="boldtext">
						   No d'employé : <input type="text" name="employeeValue" value="<%= (String)evaluation.get("employeeValue") %>" readonly>
						</td>
						<td class="boldtext">
						   C.R. : <input type="text" name="cr" value="<%= (String)evaluation.get("cr") %>" readonly>
						</td>
						<td class="boldtext">
						   Unité administrative : <input type="text" name="administrativeUnit" size="30" value="<%= (String)evaluation.get("administrativeUnit") %>" readonly>
						</td>
					</tr>
					<tr>
						<td colSpan="2" height="76">
							<table height="57">
								<tr>
									<td colSpan="3"><span class="boldtext">Période de référence : </span></td>
								</tr>
								<tr>
									<td width="34%">
										<p class="boldtext">
<%if(((solstice.custom.IUserInfo)session.getAttribute("userInfo")).hasPolicy("evaluationRendement_multiple"))
{%>
  										   Du <input type="text" name="startDate" size="8" value="<%= (String)evaluation.get("startDate") %>" <%=standardLock%>/>
<%}else{%>
										   Du <input type="text" name="startDate" size="8" value="<%= (String)evaluation.get("startDate") %>" readonly>
<%}%>
										</p>
									</td>
									<td width="33%">
										<p class="boldtext">
										   au <input type="text" name="endDate" size="8" value="<%= (String)evaluation.get("endDate") %>" <%=standardLock%> />
										</p>
									</td>
									<td width="33%">
										<p class="boldtext">
										   Année <input type="text" name="year" size="4" value="<%= (String)evaluation.get("year") %>" readonly>
										</p>
									</td>
								</tr>
							</table>
						</td>
						<td height="76">
							<table id="Table2">
								<tr>
									<td class="boldtext">Gestionnaire immédiat : </td>
									<td>
<%if(((solstice.custom.IUserInfo)session.getAttribute("userInfo")).hasPolicy("evaluationRendement_multiple"))
{%>									
						<mt:comboBoxTag id="managerId" selectedValue="<%= (String)evaluation.get("managerId") %>" readOnly="<%=boolLock%>">
						select P_Employee.P_Employee_ID,
                                  rtrim(P_Employee.Name) + ' (' + rtrim(P_Employee.Value) + ')' as Name
                    	from P_Distribution_Booklet
                    		join P_Employee
                      		on P_Employee.P_Employee_ID = P_Distribution_Booklet.P_Employee_Gestionnaire_ID
                      	Group By P_Employee.P_Employee_ID, P_Employee.Name, P_Employee.Value	
                      	Order by P_Employee.Name				    
                      	</mt:comboBoxTag>
<%}else{%>					
									<input type="hidden" name="managerId" value="<%= (String)evaluation.get("managerId") %>">
									<input type="text" name="managerName" value="<%= (String)evaluation.get("managerName") %>" readonly>
<%}%>							
									</td>		
								</tr>
								<tr>
									<td class="boldtext">Fonction :</td>
									<td>
										<input type="text" name="managerJobTitle" value="<%= (String)evaluation.get("managerJobTitle") %>" readonly>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td colSpan="2">
							<p class="boldtext">
						         Date d'entrée en fonction : <input type="text" name="hiredDate" value="<%= (String)evaluation.get("hiredDate") %>" size="10" readonly>
							</p>
						</td>
						<td>
							<p class="boldtext">
					        	Date d'évaluation : <input type="text" name="evaluationDate" size="10" value="<%= (String)evaluation.get("evaluationDate") %>" readonly>
							</p>
						</td>
					</tr>
					<tr>
					   <td colspan="2">
      					<%
      					   if(((solstice.custom.IUserInfo)session.getAttribute("userInfo")).hasPolicy("eval_canSetGain"))
      					   {
      					%>
					      Code de rémunération
					      <mt:comboBoxTag id="gainId" selectedValue="<%= (String)evaluation.get("gainId") %>" readOnly="<%=boolLock%>" onChange="codeRemChanged()">
					         select P_Gain_ID,
					                rtrim(Value) + ' - ' + Name
					           from P_Gain
					          where IsActive = 'Y'
                           and exists ( select 1
                                          from P_Gain_GainInfo,
                                               P_GainInfo
                                         where P_Gain_GainInfo.P_GainInfo_ID = P_GainInfo.P_GainInfo_ID
                                           and P_Gain_GainInfo.P_Gain_ID = P_Gain.P_Gain_ID
                                           and P_GainInfo.Value = 'EVAL'
                                           and P_Gain_GainInfo.To_Consider = 'Y' )
					       order by Value
					      </mt:comboBoxTag>
                     <%
                        }
                        else
                        {
                           out.println("&nbsp;");
                        }
                     %>					      
					   </td>
					   <td>
					      <%
					      	if(((solstice.custom.IUserInfo)session.getAttribute("userInfo")).hasPolicy("eval_canChangeProbation"))
					      	{ %>
					            <input type="hidden" name="IsProbation" id="IsProbation" value="<%= (String)evaluation.get("IsProbation") %>" />   
					            <input type="checkbox" name="cIsProbation" id="cIsProbation" <%= "1".equals(evaluation.get("IsProbation")) ? "checked=\"checked\" " : "" %>onclick="document.getElementById('IsProbation').value = this.checked ? '1' : '0';" />
					            <label for="cIsProbation">Évaluation probatoire</label>
					      	<% }
					      	else
					      	{ %>
					            <input type="hidden" name="IsProbation" id="IsProbation" value="<%= (String)evaluation.get("IsProbation") %>" />   
					            <input type="checkbox" name="cIsProbation" id="cIsProbation" disabled="disabled" <%= "1".equals(evaluation.get("IsProbation")) ? "checked=\"checked\" " : "" %>/>
					            <label for="cIsProbation">Évaluation probatoire</label>
					      	<% }
					      %>
					   </td>
					</tr>
				</table>
				<IMG height="10" alt="" src="images/spacer.gif" width="1" border="0">
			</td>
		</tr>
	</TABLE>
	<!-- Section2 -->
	<TABLE id="TabSection2" cellSpacing="0" cellPadding="0" width="765" align="center" border="0">
		<tr>
			<td bgColor="#003399" height="20"><IMG height="4" alt="" src="images/spacer.gif" width="1" border="0">
				<span class="titlefont">Section 2 - Signatures des attentes </span>
			</td>
		</tr>
		<tr>
			<td><IMG height="10" alt="" src="images/spacer.gif" width="1" border="0">
				<table borderColor="black" height="0" cellSpacing="0" cellPadding="3" width="765" border="1">
					<tr>
						<td colSpan="2"><span class="boldtext">Communication des attentes </span></td>
					</tr>
					<tr>
						<td>Gestionnaire immédiat : </td>
						<td>Date : <input type="text" name="BossExpectencySignoff" size="10" value="<%= (String)evaluation.get("BossExpectencySignoff") %>" <%=standardLock%>/></td>
					</tr>
					<tr>
						<td>Employé :</td>
						<td>Date : <input type="text" name="EmployeeExpectencySignoff" size="10" value="<%= (String)evaluation.get("EmployeeExpectencySignoff") %>" <%=standardLock%>/></td>
					</tr>
				</table>
				<img src="images/spacer.gif" alt="" height="10" width="1" border="0">
			</td>
		</tr>
	</TABLE>
	<!-- Section 3 -->
	<TABLE id="TabSection3" cellSpacing="0" cellPadding="0" width="765" align="center" border="0">
		<tr>
			<td bgColor="#003399" height="20"><IMG height="4" alt="" src="images/spacer.gif" width="1" border="0">
				<span class="titlefont">Section 3 - Directives </span>
			</td>
		</tr>
		<tr>
			<td>
				<table borderColor="black" height="0" cellSpacing="0" cellPadding="3" width="765" border="1">
					<tr>
						<td><span class="boldtext">Directives générales</span></td>
					</tr>
					<tr>
						<br>
						<td width="765">
							<mt:dynamicStructure>
								<mt:customQuery orderBy="PNumber">
									select Text
									  from P_ER_Form_Struct
									 where P_ER_Form_ID = <%= (String)evaluation.get("formId") %>
									   and FieldType = 'dg'
								</mt:customQuery>
								
								<span class="desc"><mt:dynamicValue id="Text"/></span>
								
							</mt:dynamicStructure>
							<br>
							<br>
							<span class="boldtext"> Consulter le <span class="bigitalictext">Guide des gestionnaires avisés sur la gestion du rendement au travail</span> pour vous aider à chacune des étapes du
				      		processus. Quant au gestionnaire évalué, il peut consulter le <span class="bigitalictext">Guide des employés sur la gestion du rendement au travail </span>
				      		afin de se préparer en tant que personne évaluée.</span>
							<br>
							<br>
						</td>
					</tr>
				</table>
				<img src="images/spacer.gif" alt="" height="10" width="1" border="0">
			</td>
		</tr>
	</TABLE>
	<!-- Section 4 -->
	<TABLE id="TabSection4" cellSpacing="0" cellPadding="0" width="765" align="center" border="0">
		<tr>
			<td bgColor="#003399" height="20"><IMG height="4" alt="" src="images/spacer.gif" width="1" border="0">
				<span class="titlefont">Section 4 - Attentes </span>
			</td>
		</tr>
		<tr>
			<td>
				<IMG height="10" alt="" src="images/spacer.gif" width="1" border="0">
				<table borderColor="black" height="0" cellSpacing="0" cellPadding="3" width="765" border="1">
					<tr>
						<td><span class="boldtext">Cotes d'appréciation des résultats</span></td>
					</tr>
					<tr>
						<td>
							<table border="0" width="100%" height="48" ID="Table12">
								<mt:dynamicStructure>
									<mt:customQuery orderBy="PNumber">
										select Name, Description, Text
										  from P_ER_Form_Struct
										 where P_ER_Form_ID = <%= (String)evaluation.get("formId") %>
										   and FieldType = 'expectation'
									</mt:customQuery>
									<tr>
										<td width="5%" class="boldtext"><mt:dynamicValue id="Name"/></td>
										<td width="25%" class="boldtext"><mt:dynamicValue id="Description"/></td>
										<td width="70%"><mt:dynamicValue id="Text"/></td>
									</tr>
								</mt:dynamicStructure>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
			   <% { int ei = 0; %>
			   <mt:manipulableStructure 
			         id="expectations" 
			         formName="mainForm" 
			         fields="expectation_expectationId, expectation_expectedResult, expectation_appreciationElement, expectation_achievementAssessment, expectation_commentary, expectation_cote"
			         initializationMethod="solstice.custom.EvaluationRendement.initializeExpectation">
			   <%
			      { 
   			      Hashtable expectation = (Hashtable)pageContext.getAttribute("expectations"); 
   			      ei++;
			   %>
			   
			         <input type="hidden" name="expectation_expectationId" value="<%= expectation.get("expectation_expectationId") %>">
						<IMG height="10" alt="" src="images/spacer.gif" width="1" border="0">
						<table borderColor="black" height="0" cellSpacing="0" cellPadding="3" width="765" border="1">
							<tr>
								<td colspan="2"><span class="boldtext">ATTENTE <%= ei %></span></td>
							</tr> 
							<tr>
								<td colspan="2">Résultats attendus</td>
							</tr>
							<tr>
								<td colspan="2" height="150"><textarea name="expectation_expectedResult" rows="8" cols="91" <%=standardLock%>><%= (String)expectation.get("expectation_expectedResult") %></textarea></td>
							</tr>
							<tr>
								<td colspan="2">Éléments d'appréciation</td>
							</tr>
							<tr>
								<td colspan="2" height="150"><textarea name="expectation_appreciationElement" rows="8" cols="91" <%=standardLock%>><%= (String)expectation.get("expectation_appreciationElement") %></textarea></td>
							</tr>
							<tr>
								<td colspan="2">Bilan des réalisations par la personne évaluée</td>
							</tr>
							<tr>
								<td colspan="2" height="150"><textarea name="expectation_achievementAssessment" rows="8" cols="91" <%=standardLock%>><%= (String)expectation.get("expectation_achievementAssessment") %></textarea></td>
							</tr>
							<tr>
								<td colspan="2">Commentaires et évaluation du gestionnaire</td>
							</tr>
							<tr>
								<td colspan="2" height="150"><textarea name="expectation_commentary" rows="8" cols="91" <%=standardLock%>><%= (String)expectation.get("expectation_commentary") %></textarea></td>
							</tr>
							<tr>
								<td width="90%">
									Cote d'appréciation
								</td>
								<td width="10%">
								   <%= solstice.custom.EvaluationRendement.createExpectationComboBox(Integer.parseInt((String)evaluation.get("formId")), (String)expectation.get("expectation_cote"), boolLock) %>
								</td>
							</tr>
							<% if(!boolLock) { %>
							<tr>
							   <td align="right" colspan="2">
							      <mt:manipulableDeleteButton type="image" src="images/delete.gif" />
							   </td>
							</tr>
							<% } %>
						</table>
			   
            <% } %>			   
			   </mt:manipulableStructure>
			   <% } %>
			   <p align="right" style="border: 1px solid #000000">
			   <%
			      if(boolLock)
			      {%>
   	         <img src="images/Btnajouter.gif" border="0">
			      <%}
			      else
			      {%>
			      <a href="javascript:expectations_add()" id="btnajouter">
   	         <img src="images/Btnajouter.gif" border="0">
			      </a>
			      <%}
			   %>
			   </p>
				<IMG height="10" alt="" src="images/spacer.gif" width="1" border="0">
			</td>
		</tr>
		<tr>
			<td align="right">
		    	<input type="checkbox" name="isExpectationCompleted" <%= "true".equals(evaluation.get("isExpectationCompleted")) ? "checked" : "" %>  <%=standardLock2%> onClick="changeIsComplete()"> Attentes complétées
		    </td>
		</tr>
		<tr>
		    <td align="right">
		         	 <%
      					   if(!((solstice.custom.IUserInfo)session.getAttribute("userInfo")).hasPolicy("eval_canSetGain"))
      					   {
      				 %>
					      Code de rémunération
					      <input type="text" size="30" readonly="readonly" value="<%=evaluation.get("gainName")%>">
                     <%
                        }
                        else
                        {
                           out.println("&nbsp;");
                        }
                     %>					      
		    </td>
		</tr>
	</TABLE>
	<br><br>
	<!-- Section 5 -->
	<TABLE cellSpacing="0" cellPadding="0" width="765" align="center" border="0" height="323">
		<tr>
			<td bgColor="#003399" height="20"><IMG height="4" alt="" src="images/spacer.gif" width="1" border="0">
				<span class="titlefont">Section 5 - Profil des compétences </span>
			</td>
		</tr>
		<tr>
			<td>
				<IMG height="10" alt="" src="images/spacer.gif" width="1" border="0">
				<table borderColor="black" height="104" cellSpacing="0" cellPadding="3" border="1" width="100%"
					ID="TableCoteCompetence">
					<tr>
						<td colspan="2"><span class="boldtext">Cotes d'appréciation en fonction des exigences du poste</span></td>
					</tr>
					<tr>
						<td>
							<table border="0" width="100%" height="48" ID="Table8">
								<tr>
									<mt:dynamicStructure>
										<mt:customQuery orderBy="PNumber">
											select Name, Description
											  from P_ER_Form_Struct
											 where P_ER_Form_ID = <%= (String)evaluation.get("formId") %>
											   and FieldType = 'compCote'
										</mt:customQuery>
									
										<td class="boldtext">
											<mt:dynamicValue id="Name"/> = <mt:dynamicValue id="Description"/>
										</td>
									</mt:dynamicStructure>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				<img src="images/spacer.gif" alt="" height="10" width="1" border="0">
			</td>
		</tr>
		<tr>
			<td>
				<table borderColor="black" height="139" cellSpacing="0" cellPadding="3" border="1" ID="TableCompetences">
					<tr>
						<td width="679"><span class="boldtext">Compétences</span></td>
						<td width="389"><span class="boldtext">Faits significatifs / commentaires et objectifs</span></td>
						<td width="10%"><span class="boldtext">Cote</span></td>
					</tr>
					<%
					   Hashtable[] competences = (Hashtable[])evaluation.get("competences");
					   int i = 0;
					%>
					<mt:dynamicStructure>
						<mt:customQuery orderBy="PNumber">
							select Name, Description
							  from P_ER_Form_Struct
							 where P_ER_Form_ID = <%= (String)evaluation.get("formId") %>
							   and FieldType = 'competence'
						</mt:customQuery>
						
						   <input type="hidden" name="competence_competenceId" value="<%= (String)competences[i].get("competenceId") %>">
							<tr>
								<td width="679" align="left" valign="top" height="158">
									<span class="boldtext">
										<mt:dynamicValue id="Name"/>
									</span>
									<br>
									<mt:dynamicValue id="Description"/>
								</td>
								<td width="389" height="158">
									<textarea name="competence_commentary" rows="9" cols="48" <%=standardLock%>><%= (String)competences[i].get("commentary") %></textarea>
								</td>
								<td width="5%" height="158">
								   <%= solstice.custom.EvaluationRendement.createCompetenceComboBox(Integer.parseInt((String)evaluation.get("formId")), (String)competences[i].get("cote"), boolLock) %>
								</td>
							</tr>
							<% i++; %>
					</mt:dynamicStructure>
				</table>
				<img src="images/spacer.gif" alt="" height="10" width="1" border="0">
			</td>
		</tr>
	</TABLE>
	<!-- Section 6 -->
	<TABLE cellSpacing="0" cellPadding="0" width="765" align="center" border="0" height="323">
		<tr>
			<td bgColor="#003399" height="20"><IMG height="4" alt="" src="images/spacer.gif" width="1" border="0">
				<span class="titlefont">Section 6 - Appréciation globale du rendement </span>
			</td>
		</tr>
		<tr>
			<td>
				<IMG height="10" alt="" src="images/spacer.gif" width="1" border="0">
				<table borderColor="black" cellSpacing="0" cellPadding="3" border="1" width="765" ID="TabCoteCompetences"
					height="76">
					<tr>
						<td><span class="boldtext">Déterminez l'appréciation globale qui correspond le mieux au rendement fourni
					par l'évalué en tenant compte des attentes<br> signifiées et des compétences de gestion requises.</span></td>
					</tr>
					<tr>
						<td>
							<table border="0" width="100%" ID="TableCoteApp">
								<tr>
								<% i = 0; %>
									<mt:dynamicStructure>
										<mt:customQuery orderBy="PNumber">
											select Name, PNumber, Description
											  from P_ER_Form_Struct
											 where P_ER_Form_ID = <%= (String)evaluation.get("formId") %>
											   and FieldType = 'globalApp'
										</mt:customQuery>

										<td class="boldtext">
										   <table>
										      <tr>
      											<td class="boldtext"><mt:dynamicValue id="Name"/></td>
      											<td class="boldtext"><input value="<mt:dynamicValue id="PNumber" formatMethod="solstice.custom.EvaluationRendement.formatPNumber" />" type="radio" name="globalCote"<%= ((String)evaluation.get("globalCote")).trim().equals(String.valueOf(i)) ? " checked" : "" %> <%=standardLock2%>></td>
      											<td class="boldtext"><mt:dynamicValue id="Description"/></td>
										      </tr>
											</table>
										</td>
										<% i++; %>
									</mt:dynamicStructure>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table borderColor="black" width="100%" height="139" cellSpacing="0" cellPadding="3" border="1"
					ID="Table6">
					<tr>
						<td><span class="boldtext">Indiquez les principaux motifs à l'appui de l'appréciation globale retenue :</span></td>
					</tr>
					<tr>
						<td><textarea name="globalReason" rows="7" cols="91" <%=standardLock%>><%= (String)evaluation.get("globalReason") %></textarea></td>
					</tr>
				</table>
			</td>
		</tr>
	</TABLE>
	<!-- Section 7 -->
	<TABLE id="TabSection7" cellSpacing="0" cellPadding="0" width="765" align="center" border="0">
		<tr>
			<td bgColor="#003399" height="20"><IMG height="4" alt="" src="images/spacer.gif" width="1" border="0">
				<span class="titlefont">Section 7 - Plan de développement </span>
			</td>
		</tr>
		<tr>
		   <td><IMG height="20" alt="" src="images/spacer.gif" width="1" border="0"></td>
		</tr>
		<tr>
		   <td>
				<table borderColor="black" width="100%" height="139" cellSpacing="0" cellPadding="3" border="1">
				   <tr>
				      <td>
				         <mt:dynamicStructure>
				            <mt:customQuery>
                           select isnull((select convert(varchar(2000), Description)
                                            from P_ER_Form_Struct
                                           where P_ER_Form_ID = <%= (String)evaluation.get("formId") %>
                                             and FieldType = 'consultAct'), '#') as consultAct_Description,
                                  isnull((select convert(varchar(2000), Description)
                                            from P_ER_Form_Struct
                                           where P_ER_Form_ID = <%= (String)evaluation.get("formId") %>
                                             and FieldType = 'addAct'), '#') as addAct_Description,
                                  isnull((select convert(varchar(2000), Description)
                                            from P_ER_Form_Struct
                                           where P_ER_Form_ID = <%= (String)evaluation.get("formId") %>
                                             and FieldType = 'hyperlien'), '#') as hyperlien
				            </mt:customQuery>
				            <mt:body>
         				      Pour prioriser des besoins de formation à l'employé, cliquez sur <a href="<mt:dynamicValue id="consultAct_Description"/>">Inventaire des besoins de formation</a><br><br>
                           Pour consulter les activités de formation réalisées et à venir, cliquez sur <a href="<mt:dynamicValue id="addAct_Description"/>">Plan global de formation</a><br><br>
                           Pour valider les activités de formation réalisées, cliquez sur <a href="<mt:dynamicValue id="hyperlien" />">Atteintes des objectifs de formation</a>
				            </mt:body>
				         </mt:dynamicStructure>
                  </td>
               </tr>
            </table>
		   </td>
		</tr>
		<tr>
		   <td><IMG height="20" alt="" src="images/spacer.gif" width="1" border="0"></td>
		</tr>
	</TABLE>

	<!-- Section 8 -->
	<TABLE id="TabSection8" cellSpacing="0" cellPadding="0" width="765" align="center" border="0">
		<tr>
			<td bgColor="#003399" height="20"><IMG height="4" alt="" src="images/spacer.gif" width="1" border="0">
				<span class="titlefont">Section 8 - Commentaires du gestionnaire évalué </span>
			</td>
		</tr>
		<tr>
			<td>
				<IMG height="10" alt="" src="images/spacer.gif" width="1" border="0">
				<table borderColor="black" height="0" cellSpacing="0" cellPadding="3" width="765" border="1"
					ID="Table5">
					<tr>
						<td><span class="boldtext">Commentaires</span></td>
					</tr>
					<tr>
						<td><textarea name="employeeCommentary" rows="9" cols="91" <%=standardLock%>><%= (String)evaluation.get("employeeCommentary") %></textarea></td>
					</tr>
				</table>
				<IMG height="10" alt="" src="images/spacer.gif" width="1" border="0">
			</td>
		</tr>
	</TABLE>

	<!-- Section 9 -->
	<TABLE id="TabSection9" cellSpacing="0" cellPadding="0" width="765" align="center" border="0">
		<tr>
			<td bgColor="#003399" height="20"><IMG height="4" alt="" src="images/spacer.gif" width="1" border="0">
				<span class="titlefont">Section 9 - Suivi </span>
			</td>
		</tr>
		<tr>
			<td>
				<IMG height="10" alt="" src="images/spacer.gif" width="1" border="0">
				<table borderColor="black" height="0" cellSpacing="0" cellPadding="3" width="765" border="1"
					ID="TableRencontre">
					<tr>
						<td colspan="2"><SPAN class="boldtext">Rencontres de suivi : </SPAN></td>
					</tr>
					<%
					   Hashtable[] meetings = (Hashtable[])evaluation.get("meetings");
					%>
					<TR>
						<TD>
   						<input type="hidden" name="meeting_meetingId0" value="<%= (String)meetings[0].get("meetingId") %>">
							<p>Date : <input type="text" name="meeting_meetingDate0" value="<%= (String)meetings[0].get("meetingDate") %>"  <%=standardLock%> /></p>
						</TD>
						<td>
   						<input type="hidden" name="meeting_meetingId1" value="<%= (String)meetings[1].get("meetingId") %>">
							<p>Date : <input type="text" name="meeting_meetingDate1" value="<%= (String)meetings[1].get("meetingDate") %>" <%=standardLock%> /></p>
						</td>
					</TR>
				</table>
				<IMG height="4" alt="" src="images/spacer.gif" width="1" border="0">
			</td>
		</tr>
	</TABLE>
	<!--Autres réalisations -->
	<TABLE id="TabAutre" cellSpacing="0" cellPadding="3" width="765" align="center" border="1"
		borderColor="black">
		<tr>
			<td>
				<IMG height="4" alt="" src="images/spacer.gif" width="1" border="0">
				<span class="boldtext">Bilan des autres réalisations s'il y a lieu ( à remplir par le gestionnaire évalué ) : </span>
			</td>
		</tr>
		<tr>
			<td>
				<textarea name="otherAchievement" rows="9" cols="91" <%=standardLock%>><%= (String)evaluation.get("otherAchievement") %></textarea>
			</td>
		</tr>
	</TABLE>
	
	<br>
	<TABLE id="TabSectionSignature" cellSpacing="0" cellPadding="0" width="765" align="center" border="0">
		<tr>
			<td bgColor="#003399" height="20"><IMG height="4" alt="" src="images/spacer.gif" width="1" border="0">
				<span class="titlefont">Section 10 - Signatures  </span>
			</td>
		</tr>
		<tr>
			<td><IMG height="10" alt="" src="images/spacer.gif" width="1" border="0">
				<table borderColor="black" height="0" cellSpacing="0" cellPadding="3" width="765" border="1">
					<tr>
						<td>Gestionnaire immédiat : </td>
						<td>Date : <input type="text" name="BossEvaluationSignoff" size="10" value="<%= (String)evaluation.get("BossEvaluationSignoff") %>" <%=standardLock%>/></td>
					</tr>
					<tr>
						<td>Gestionnaire évalué : </td>
						<td>Date : <input type="text" name="EmployeeEvaluationSignoff" size="10" value="<%= (String)evaluation.get("EmployeeEvaluationSignoff") %>" <%=standardLock%>/></td>
					</tr>
					<tr>
						<td>Gestionnaire hiérarchique : </td>
						<td>Date : <input type="text" name="BossBossEvaluationSignoff" size="10" value="<%= (String)evaluation.get("BossBossEvaluationSignoff") %>" <%=standardLock%>/></td>
					</tr>
				</table>
				<img src="images/spacer.gif" alt="" height="10" width="1" border="0">
			</td>
		</tr>
	</TABLE>
	
	
	<!-- footer -->
	<TABLE cellSpacing="0" cellPadding="0" width="765" border="0" align="center" ID="TableF">
		<tr>
			<td>
				<img src="images/spacer.gif" alt="" height="4" width="1" border="0">
				<span class="boldtext">%%Organisation%%</span>
			</td>
		</tr>
		<tr>
			<td height="15" bgcolor="#003399" colspan="2"></td>
		</tr>
	</TABLE>
	<table cellSpacing="0" cellPadding="0" width="765" border="0" align="center" ID="TableSave">
		<tr>
			<td>
				<IMG height="4" alt="" src="images/spacer.gif" width="1" border="0">
			</td>
		</tr>
		<tr>
			<td align="right">
			<%
				  String lockCompleteField = "disabled=\"disabled\"";
			      if(((solstice.custom.IUserInfo)session.getAttribute("userInfo")).hasPolicy("evaluationRendement_reopen")
			            || boolLock == false)
			      {
			            lockCompleteField = "";
			      }
			%>
			   <input type="checkbox" name="isClosed"<%= "true".equals(evaluation.get("isClosed")) ? " checked" : "" %> <%=lockCompleteField%> onClick="changeIsClose()"> Évaluation complétée
			</td>
		</tr>
		<tr>
		    <td align="right">
		         	 <%
      					   if(!((solstice.custom.IUserInfo)session.getAttribute("userInfo")).hasPolicy("eval_canSetGain"))
      					   {
      				 %>
					      Code de rémunération
					      <input type="text" size="30" readonly="readonly" value="<%=evaluation.get("gainName")%>">
                     <%
                        }
                        else
                        {
                           out.println("&nbsp;");
                        }
                     %>					      
		    </td>
		</tr>
		<tr>
			<td>
			   <table width="100%" border="0" cellspacing="1" cellpadding="1">   
			      <tr>
			         <td width="100%">&nbsp</td>
			         <td>
         				<% if(!"-1".equals(evaluation.get("evaluationId"))) { %>
         				   <a target="_blank" href="launchreport.jsp?evaluationId=<%= (String)evaluation.get("evaluationId") %>&evaluationType=GES">
         				      <img src="images/btnImprimer.gif" border="0" />
         				   </a>
         				<% } %>
			         </td>
			         <td>
         			   <%
         			   		String previousPage = request.getHeader("REFERER");
         			   		previousPage = previousPage.substring(previousPage.length()-11, previousPage.length());
         			      if((!((solstice.custom.IUserInfo)session.getAttribute("userInfo")).hasPolicy("evaluationRendement_sauvegarde")
         			            && "true".equals(evaluation.get("isClosed"))) || previousPage.trim().toLowerCase().equals("moneval.jsp"))
         			      {
            			      %>
               	   		<img alt="Sauvegarde de l'évaluation" border="0" src="images/btnSave.gif">
            			      <%
         			      }
         			      else
         			      {
            			      %>
                  	      <a href="javascript:saveForm()">
                  	   		<img alt="Sauvegarde de l'évaluation" border="0" src="images/btnSave.gif">
                  	      </a>
            			      <%
         			      }
         			   %>
			         </td>
			      </tr>
			   </table>
			</td>
		</tr>
	</table>
	<TABLE id="Table9" cellSpacing="0" cellPadding="0" width="765" align="center" border="0">
		<tr>
			<td align="left"><%= "ER_" + solstice.custom.EvaluationRendementPTB.getVersionForm((String)evaluation.get("formId"))%></td>
		<td align="right"><a href="index.jsp">retour au menu principal</a><img src="images/spacer.gif" alt="" height="30" width="1" border="0"></td>
		</tr>
	</table>

</mt:erPage>
</mt:securityCheck>