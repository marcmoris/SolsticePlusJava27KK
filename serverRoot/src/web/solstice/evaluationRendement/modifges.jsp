<%@ taglib uri="/WEB-INF/taglib.tld" prefix="mt" %>
<%@ page import="java.util.Hashtable" %>

<mt:securityCheck id="evaluationRendement_modifges">
<mt:erPage id="modifges" validationClass="solstice.custom.EvaluationRendementModification">

<%

   // On doit récupérer le formulaire
   Hashtable formulaire = solstice.custom.EvaluationRendementModification.createMainForm(pageContext, "GES");

%>

	<script language="JavaScript">
		
		function saveForm()
		{
			if(confirm("Voulez vous vraiment sauvegarder le formulaire?"))
			{
				document.mainForm.action.value = "save";
   			document.mainForm.submit();
			}
		}

	</script>
	
	<input type="hidden" name="action">
	<input type="hidden" name="formType" value="<%= (String)formulaire.get("formType") %>">
	<input type="hidden" name="formDate" value="<%= (String)formulaire.get("formDate") %>">
	<input type="hidden" name="originalVersion" value="<%= (String)formulaire.get("originalVersion") %>">
	<input type="hidden" name="originalFormId" value="<%= (String)formulaire.get("originalFormId") %>">
		
	
	<!-- header -->
	<TABLE id="TableH" cellSpacing="0" cellPadding="0" width="765" align="center" border="0">
		<tr>
			<td width="211"><IMG height="100" alt="%%Organisation%%" src="images/LogoBAN.jpg" width="211">
			</td>
			<td bgcolor="#990000">
				<img src="images/spacer.gif" alt="" height="4" width="1" border="0">
			    <span class="font">========== MODIFICATION ==========</span><br>
			    <IMG height="4" alt="" src="images/spacer.gif" width="1" border="0">
				<span class="font">Évaluation du rendement des gestionnaires</span>
			</td>
		</tr>
	</TABLE>
	<TABLE id="Table14" cellSpacing="0" cellPadding="0" width="765" align="center" border="0">
		<tr>
			<td align=right>
				<a href="index.jsp">retour au menu principal</a>
				<img src="images/spacer.gif" alt="" height="30" width="1" border="0">
			</td>
		</tr>
	</table>
	<!-- Section1 -->
	<TABLE id="TableSection1" cellSpacing="0" cellPadding="0" width="765" align="center" border="0">
		<tr>
			<td bgColor="#003399" height="20"><IMG height="4" alt="" src="images/spacer.gif" width="1" border="0">
				<span class="titlefont">Section 1 - Identification </span>
			</td>
		</tr>
		<tr>
			<td><IMG height="10" alt="" src="images/spacer.gif" width="1" border="0">
				<table id="Table2" borderColor="black" height="0" cellSpacing="0" cellPadding="3" width="765"
					border="1">
					<tr>
						<td colSpan="2"><span class="boldtext">Nom : </span></td>
						<td><span class="boldtext">Titre de l'emploi : </span></td>
					</tr>
					<tr>
						<td><span class="boldtext">No d'employé : </span></td>
						<td><span class="boldtext">C.R. :</span></td>
						<td><span class="boldtext">Unité administrative : </span></td>
					</tr>
					<tr>
						<td colSpan="2">
							<table ID="Table3">
								<tr>
									<td colSpan="2"><span class="boldtext">Période de référence : </span></td>
								</tr>
								<tr>
									<td width="50%"><span class="boldtext">Du </span></td>
									<td width="50%"><span class="boldtext">au </span></td>
								</tr>
							</table>
						</td>
						<td>
							<table id="Table4">
								<tr>
									<td><span class="boldtext">Gestionnaire immédiat : </span></td>
								</tr>
								<tr>
									<td><span class="boldtext">Fonction : </span></td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td colSpan="2"><span class="boldtext"><span class="boldtext">Date d'entrée en fonction : </span>
							</span></td>
						<td><span class="boldtext"><span class="boldtext">Date d'évaluation : </span>
							</span></td>
					</tr>
				</table>
				<IMG height="10" alt="" src="images/spacer.gif" width="1" border="0">
			</td>
		</tr>
	</TABLE>
	<!-- Section 2 -->
	<TABLE id="TabSection2" cellSpacing="0" cellPadding="0" width="765" align="center" border="0">
		<tr>
			<td bgColor="#003399" height="20"><IMG height="4" alt="" src="images/spacer.gif" width="1" border="0">
				<span class="titlefont">Section 2 - Signatures des attentes </span>
			</td>
		</tr>
		<tr>
			<td><IMG height="10" alt="" src="images/spacer.gif" width="1" border="0">
				<table borderColor="black" height="0" cellSpacing="0" cellPadding="3" width="765" border="1"
					ID="Table5">
					<tr>
						<td colSpan="2"><span class="boldtext">Communication des attentes </span></td>
					</tr>
					<tr>
						<td>Gestionnaire immédiat : </td>
						<td>Date : </td>
					</tr>
					<tr>
						<td>Employé :&nbsp; </SPAN></td>
						<td>Date : </SPAN></td>
					</tr>
				</table>
				<img src="images/spacer.gif" alt="" height="10" width="1" border="0">
			</td>
		</tr>
	</TABLE>
	<!-- Section 3 -->
	<TABLE id="TabSection3" cellSpacing="0" cellPadding="0" width="765" align="center" border="0">
		<tr>
			<td bgColor="#990000" height="20"><IMG height="4" alt="" src="images/spacer.gif" width="1" border="0">
				<span class="titlefont">Section 3 - Directives </span>
			</td>
		</tr>
		<tr>
			<td>
				<IMG height="10" alt="" src="images/spacer.gif" width="1" border="0">
				<table borderColor="black" height="0" cellSpacing="0" cellPadding="3" width="765" border="1" ID="Table6">
					<tr>
						<td><span class="boldtext">Directives générales</span></td>
					</tr>
					<tr>
						<td>
						   <textarea name="dg_Text" rows="6" cols="92"><%= (String)formulaire.get("dg_Text") %></textarea>
						</td>
					</tr>
					<tr>
					   <td><span class="boldtext"> Consulter le <span class="bigitalictext">Guide des gestionnaires avisés sur la gestion du rendement au travail</span> pour vous aider à chacune des étapes du
					      processus. Quant au gestionnaire évalué, il peut consulter le <span class="bigitalictext">Guide des employés sur la gestion du rendement au travail </span>
					      afin de se préparer en tant que personne évaluée.</span>
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
			<td bgColor="#990000" height="20"><IMG height="4" alt="" src="images/spacer.gif" width="1" border="0">
				<span class="titlefont">Section 4 - Attentes </span>
			</td>
		</tr>
		<tr>
			<td>
				<IMG height="10" alt="" src="images/spacer.gif" width="1" border="0">
				<table borderColor="black" height="0" cellSpacing="0" cellPadding="3" width="765" border="1" ID="Table7">
					<tr>
						<td><span class="boldtext">Cotes d'appréciation des résultats</span></td>
					</tr>
					<tr>
						<td>
							<table border="0" width="100%" height="48" ID="Table12">
								<% 
 								   Hashtable[] expectations = (Hashtable[])formulaire.get("expectations");
 								   for(int i = 0; i < expectations.length; i++)
 								   {
								%>
									<tr>
										<td width="5%"><input type="text" name="expectation_Name" size="1" class="boldtext" value="<%= (String)expectations[i].get("Name") %>"></td>
										<td width="25%">&nbsp;=&nbsp;<input type="text" name="expectation_Description" size="20" class="boldtext" value="<%= (String)expectations[i].get("Description") %>"></td>
										<td width="70%"><input type="text" name="expectation_Text" size="75" value="<%= (String)expectations[i].get("Text") %>"></td>
									</tr>
								<%
								   }
								%>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<IMG height="10" alt="" src="images/spacer.gif" width="1" border="0">
				<table borderColor="black" height="0" cellSpacing="0" cellPadding="3" width="765" border="1"
					ID="Table13">
					<tr>
						<td colspan="2"><span class="boldtext">ATTENTE 1</span></td>
					</tr>
					<tr>
						<td colspan="2">Résultats attendus</td>
					</tr>
					<tr>
						<td colspan="2" height="150">&nbsp;</td>
					</tr>
					<tr>
						<td colspan="2">Éléments d'appréciation</td>
					</tr>
					<tr>
						<td colspan="2" height="150">&nbsp;</td>
					</tr>
					<tr>
						<td colspan="2">Bilan des réalisations par la personne évaluée</td>
					</tr>
					<tr>
						<td colspan="2" height="150">&nbsp;</td>
					</tr>
					<tr>
						<td colspan="2">Commentaires et évaluation du gestionnaire</td>
					</tr>
					<tr>
						<td colspan="2" height="150">&nbsp;</td>
					</tr>
					<tr>
						<td width="90%">
							Cote d'appréciation
						</td>
						<td width="10%">&nbsp;
						   
						</td>
					</tr>
				</table>
				<IMG height="10" alt="" src="images/spacer.gif" width="1" border="0">
			</td>
		</tr>
	</TABLE>
	<!-- Section 5 -->
	<TABLE id="TabSection5" cellSpacing="0" cellPadding="0" width="765" align="center" border="0"
		height="323">
		<tr>
			<td bgColor="#990000" height="20"><IMG height="4" alt="" src="images/spacer.gif" width="1" border="0">
				<span class="titlefont">Section 5 - Profil des compétences </span>
			</td>
		</tr>
		<tr>
			<td>
				<IMG height="10" alt="" src="images/spacer.gif" width="1" border="0">
				<table borderColor="black" height="104" cellSpacing="0" cellPadding="3" border="1" width="100%"
					ID="TableCoteCompetence">
					<tr>
					<%
					   Hashtable[] compCotes = (Hashtable[])formulaire.get("compCotes");
					%>
						<td colspan="2"><span class="boldtext">Cotes d'appréciation en fonction des exigences du poste</span></td>
					</tr>
					<tr>
						<td>
							<table border="0" width="100%" height="48" ID="Table8">
								<tr>
									<td >
										<input type="text" name="compCote_Name" size="1" class="boldtext" value="<%= (String)compCotes[0].get("Name") %>">&nbsp;=&nbsp;
										<input type="text" name="compCote_Description" size="20" class="boldtext" value="<%= (String)compCotes[0].get("Description") %>">
									</td>
									<td >
										<input type="text" name="compCote_Name" size="1" class="boldtext" value="<%= (String)compCotes[1].get("Name") %>">&nbsp;=&nbsp;
										<input type="text" name="compCote_Description" size="20" class="boldtext" value="<%= (String)compCotes[1].get("Description") %>">
									</td>
								</tr>
								<tr>
									<td >
										<input type="text" name="compCote_Name" size="1" class="boldtext" value="<%= (String)compCotes[2].get("Name") %>">&nbsp;=&nbsp;
										<input type="text" name="compCote_Description" size="20" class="boldtext" value="<%= (String)compCotes[2].get("Description") %>">
									</td>
									<td >
										<input type="text" name="compCote_Name" size="1" class="boldtext" value="<%= (String)compCotes[3].get("Name") %>">&nbsp;=&nbsp;
										<input type="text" name="compCote_Description" size="20" class="boldtext" value="<%= (String)compCotes[3].get("Description") %>">
									</td>
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
						<td width="25%"><span class="boldtext">Compétences</span></td>
						<td width="479"><span class="boldtext">Commentaires / faits significatifs</span></td>
						<td width="10%"><span class="boldtext">Cote*</span></td>
						<td>&nbsp;</td>
					</tr>
               <mt:manipulableStructure id="competences" formName="mainForm" initializationMethod="solstice.custom.EvaluationRendementModification.initialCompetence" fields="competence_Name, competence_Description">
                  <tr>
                     <td>
                        <input type="text" name="competence_Name" maxLength="50" value="<%= (String)((Hashtable)pageContext.getAttribute("competences")).get("competence_Name") %>" style="width: 244px" class="boldtext"><br>
                        <textarea name="competence_Description" maxLength="1000" style="width: 244px; height: 100px"><%= (String)((Hashtable)pageContext.getAttribute("competences")).get("competence_Description") %></textarea>
                     </td>
                     <td>&nbsp;</td>
                     <td>&nbsp;</td>
                     <td align="center" valign="middle">
                        <mt:manipulableDeleteButton type="image" src="images/delete.gif"/>
                     </td>
                  </tr>
               </mt:manipulableStructure>
               <tr>
                  <td colspan="3">
                     <a href="javascript: competences_reset()"><img src="images/Btnrecommencer.gif" border="0"></a>
                  </td>
                  <td>
                     <a href="javascript: competences_add()"><img src="images/Btnajouter.gif" border="0"></a>
                  </td>
               </tr>
				</table>
				<img src="images/spacer.gif" alt="" height="10" width="1" border="0">
			</td>
		</tr>
	</TABLE>
	<!-- Section 6 -->
	<TABLE id="TabSection6" cellSpacing="0" cellPadding="0" width="765" align="center" border="0"
		height="323">
		<tr>
			<td bgColor="#990000" height="20"><IMG height="4" alt="" src="images/spacer.gif" width="1" border="0">
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
						<%
						   Hashtable[] globalApp = (Hashtable[])formulaire.get("globalApp");
						%>
							<table border="0" width="100%" ID="TableCoteApp">
								<tr>
									<td class="boldtext">
									   <input type="text" name="globalApp_Name" size="1" value="<%= (String)globalApp[0].get("Name") %>" class="boldtext">&nbsp;&nbsp;
									   <IMG height="12" alt="" src="images/carre.jpg" width="12" border="0" align="middle">&nbsp;
									   <input type="text" name="globalApp_Description" size="20" value="<%= (String)globalApp[0].get("Description") %>">
									</td>
									<td class="boldtext">
									   <input type="text" name="globalApp_Name" size="1" value="<%= (String)globalApp[1].get("Name") %>" class="boldtext">&nbsp;&nbsp;
									   <IMG height="12" alt="" src="images/carre.jpg" width="12" border="0" align="middle">&nbsp;
									   <input type="text" name="globalApp_Description" size="20" value="<%= (String)globalApp[1].get("Description") %>">
									</td>
							    </tr>
								<tr>
									<td class="boldtext">
									   <input type="text" name="globalApp_Name" size="1" value="<%= (String)globalApp[2].get("Name") %>" class="boldtext">&nbsp;&nbsp;
									   <IMG height="12" alt="" src="images/carre.jpg" width="12" border="0" align="middle">&nbsp;
									   <input type="text" name="globalApp_Description" size="20" value="<%= (String)globalApp[2].get("Description") %>">
									</td>
									<td class="boldtext">
									   <input type="text" name="globalApp_Name" size="1" value="<%= (String)globalApp[3].get("Name") %>" class="boldtext">&nbsp;&nbsp;
									   <IMG height="12" alt="" src="images/carre.jpg" width="12" border="0" align="middle">&nbsp;
									   <input type="text" name="globalApp_Description" size="20" value="<%= (String)globalApp[3].get("Description") %>">
									</td>
								</tr>
								<tr>
									<td class="boldtext">
									   <input type="text" name="globalApp_Name" size="1" value="<%= (String)globalApp[4].get("Name") %>" class="boldtext">&nbsp;&nbsp;
									   <IMG height="12" alt="" src="images/carre.jpg" width="12" border="0" align="middle">&nbsp;
									   <input type="text" name="globalApp_Description" size="20" value="<%= (String)globalApp[4].get("Description") %>">
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<IMG height="4" alt="" src="images/spacer.gif" width="1" border="0">
				<table borderColor="black" width="100%" height="139" cellSpacing="0" cellPadding="3" border="1"
					ID="Table9">
					<tr>
						<td><span class="boldtext">Indiquez les principaux motifs à l'appui de l'appréciation globale retenue :</span></td>
					</tr>
					<tr>
						<td height=120>&nbsp;</td>
					</tr>
				</table>
				<IMG height="4" alt="" src="images/spacer.gif" width="1" border="0">
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
			<td>
				<IMG height="10" alt="" src="images/spacer.gif" width="1" border="0">
				<table borderColor="black" height="0" cellSpacing="0" cellPadding="3" width="765" border="1">
					<tr>
						<td width="360" class="boldtext">
						   Pour prioriser des besoins de formation à l'employé, cliquez sur <font color="blue"><u>Inventaire des besoins de formation</u></font><br>
						   <textarea name="consultAct_Description" cols="91" rows="3"><%= (String)formulaire.get("consultAct_Description") %></textarea>
						   <br><br>
                     Pour consulter les activités de formation réalisées et à venir, cliquez sur <font color="blue"><u>Plan global de formation</u></font><br>
						   <textarea name="addAct_Description" cols="91" rows="3"><%= (String)formulaire.get("addAct_Description") %></textarea>
						   <br><br>
						   Pour valider les activités de formation réalisées, cliquez sur <font color="blue"><u>Atteintes des objectifs de formation</u></font><br>
						   <textarea name="hyperlien" cols="91" rows="3"><%= (String)formulaire.get("hyperlien") %></textarea>
						</td>
				   </tr>
				</table>
				<IMG height="10" alt="" src="images/spacer.gif" width="1" border="0">
			</td>
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
					ID="Table11">
					<tr>
						<td><span class="boldtext">Commentaires</span></td>
					</tr>
					<tr>
						<td height=100>&nbsp;</td>
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
						<td colspan=2 class="boldtext">Rencontres de suivi : </td>
					</tr>
					<tr>
						<td >Date : </td>
						<td >Date : </td>
					</tr>
				</table>
				<IMG height="4" alt="" src="images/spacer.gif" width="1" border="0">
			</td>
		</tr>
	</TABLE>
	<!--Autres réalisations -->
	<TABLE id="TabAutre" cellSpacing="0" cellPadding="3" width="765" align="center" border="1" borderColor="black">
		<tr>
			<td >
			    <IMG height="4" alt="" src="images/spacer.gif" width="1" border="0">
				<span class="boldtext">Bilan des autres réalisations s'il y a lieu (à remplir par le gestionnaire évalué ) : </span>
			</td>
		</tr>
		<tr>
			<td height=100>&nbsp;
			
			</td>
		</tr>
	</TABLE>
	
		<br>
	<TABLE id="TabSectionSignature" cellSpacing="0" cellPadding="0" width="765" align="center" border="0">
		<tr>
			<td bgColor="#003399" height="20"><IMG height="4" alt="" src="images/spacer.gif" width="1" border="0">
				<span class="titlefont">Section 10 - Signatures : </span>
			</td>
		</tr>
		<tr>
			<td><IMG height="10" alt="" src="images/spacer.gif" width="1" border="0">
				<table borderColor="black" height="0" cellSpacing="0" cellPadding="3" width="765" border="1">
					<tr>
						<td>Gestionnaire immédiat : </td>
						<td>Date : </td>
					</tr>
					<tr>
						<td>Gestionnaire évalué : </td>
						<td>Date : </td>
					</tr>
					<tr>
						<td>Gestionnaire hiérarchique : </td>
						<td>Date : </td>
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
	      <IMG height="4" alt="" src="images/spacer.gif" width="1" border="0">
	   </td>
	   </tr>
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
					<img src="images/spacer.gif" alt="" height="4" width="1" border="0">
			</td>
		</tr>
	   <tr>
	   <td>

         	      <a href="javascript:saveForm()">
         	   		<img alt="Sauvegarde du formulaire" align="right" border="0" src="images\btnSave.gif">
         	      </a>

	   </td>
	   </tr>
	</table>
	<TABLE id="Table15" cellSpacing="0" cellPadding="0" width="765" align="center" border="0">
		<tr>
		<td align=right><a href="index.jsp">retour au menu principal</a><img src="images/spacer.gif" alt="" height="30" width="1" border="0"></td>
		</tr>
	</table>

</mt:erPage>
</mt:securityCheck>
