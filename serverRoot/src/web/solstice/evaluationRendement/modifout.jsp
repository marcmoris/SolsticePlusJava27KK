<%@ taglib uri="/WEB-INF/taglib.tld" prefix="mt" %>
<%@ page import="java.util.Hashtable" %>

<mt:securityCheck id="evaluationRendement_modifouv">
<mt:erPage id="modifges" validationClass="solstice.custom.EvaluationOUVModification">

<%

   // On doit récupérer le formulaire
   Hashtable formulaire = solstice.custom.EvaluationOUVModification.createMainForm(pageContext);

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
	<input type="hidden" name="formType" value="OUV">
	<input type="hidden" name="formDate" value="<%= (String)formulaire.get("formDate") %>">
	<input type="hidden" name="originalVersion" value="<%= (String)formulaire.get("originalVersion") %>">
	<input type="hidden" name="originalFormId" value="<%= (String)formulaire.get("originalFormId") %>">
				
	<!-- header -->
	<TABLE cellSpacing="0" cellPadding="0" width="765" border="0" align="center" ID="TableH">
		<tr>
			<td width="211">
				<img src="images/LogoBAN.jpg" alt="text" width="211" height="100">
			</td>
			<td bgcolor="#990000">
				<img src="images/spacer.gif" alt="" height="4" width="1" border="0">
				<span class="font">========== MODIFICATION ==========</span><br>
				<img src="images/spacer.gif" alt="" height="4" width="1" border="0">
				<span class="font">Évaluation du rendement du personnel ouvrier</span>
			</td>
		</tr>
	</TABLE>
	<TABLE id="Table8" cellSpacing="0" cellPadding="0" width="765" align="center" border="0">
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
			<td bgColor="#990000" height="20"><IMG height="4" alt="" src="images/spacer.gif" width="1" border="0">
				<span class="titlefont">Section 2 - Rendement</span>
			</td>
		</tr>
		<tr>
			<td><IMG height="10" alt="" src="images/spacer.gif" width="1" border="0">
				<table id="TabRendement1" borderColor="black" height="0" cellSpacing="0" cellPadding="3" width="765" border="1">
					<tr>
						<td><span class="boldtext">&nbsp;X&nbsp;&nbsp;Cochez l'énoncé qui correspond le mieux au rendement de l'employé et ajoutez vos commentaires s'il y a lieu</span></td>
					</tr>
				</table>
				<%
				   int rendementIterator = 0;
				   int rendementDetailIterator = 0;
				   Hashtable[] rendements = (Hashtable[])formulaire.get("rendements");
				   Hashtable[] rendementDetails = (Hashtable[])formulaire.get("rendementDetails");
				   for(int i = 0; i < 6; i++)
				   { 
				%>
					
						<IMG height="10" alt="" src="images/spacer.gif" width="1" border="0">
						<table borderColor="black" height="0" cellSpacing="0" cellPadding="3" width="765" border="1">
							<tr>
								<td width=50% class="boldtext">
									<input type="text" name="rendement_Description" size="55" value="<%= (String)rendements[rendementIterator++].get("Description") %>" class="boldtext">
								</td>
								<td width=50% class="boldtext">
									<input type="text" name="rendement_Description" size="55" value="<%= (String)rendements[rendementIterator++].get("Description") %>" class="boldtext">
								</td>
							</tr>
							<tr>
								<td width=50%>
										&nbsp;&nbsp;<IMG height="12" alt="" src="images/carre.jpg" width="12" border="0" align="middle">&nbsp;
										<textarea name="rendementDetail_Description" rows="2" cols="38"><%= (String)rendementDetails[rendementDetailIterator++].get("Description") %></textarea><br>
										&nbsp;&nbsp;<IMG height="12" alt="" src="images/carre.jpg" width="12" border="0" align="middle">&nbsp;
										<textarea name="rendementDetail_Description" rows="2" cols="38"><%= (String)rendementDetails[rendementDetailIterator++].get("Description") %></textarea><br>
										&nbsp;&nbsp;<IMG height="12" alt="" src="images/carre.jpg" width="12" border="0" align="middle">&nbsp;
										<textarea name="rendementDetail_Description" rows="2" cols="38"><%= (String)rendementDetails[rendementDetailIterator++].get("Description") %></textarea><br>
										&nbsp;&nbsp;<IMG height="12" alt="" src="images/carre.jpg" width="12" border="0" align="middle">&nbsp;
										<textarea name="rendementDetail_Description" rows="2" cols="38"><%= (String)rendementDetails[rendementDetailIterator++].get("Description") %></textarea><br>
								</td>
								<td width=50%>
										&nbsp;&nbsp;<IMG height="12" alt="" src="images/carre.jpg" width="12" border="0" align="middle">&nbsp;
										<textarea name="rendementDetail_Description" rows="2" cols="38"><%= (String)rendementDetails[rendementDetailIterator++].get("Description") %></textarea><br>
										&nbsp;&nbsp;<IMG height="12" alt="" src="images/carre.jpg" width="12" border="0" align="middle">&nbsp;
										<textarea name="rendementDetail_Description" rows="2" cols="38"><%= (String)rendementDetails[rendementDetailIterator++].get("Description") %></textarea><br>
										&nbsp;&nbsp;<IMG height="12" alt="" src="images/carre.jpg" width="12" border="0" align="middle">&nbsp;
										<textarea name="rendementDetail_Description" rows="2" cols="38"><%= (String)rendementDetails[rendementDetailIterator++].get("Description") %></textarea><br>
										&nbsp;&nbsp;<IMG height="12" alt="" src="images/carre.jpg" width="12" border="0" align="middle">&nbsp;
										<textarea name="rendementDetail_Description" rows="2" cols="38"><%= (String)rendementDetails[rendementDetailIterator++].get("Description") %></textarea><br>
								</td>
							</tr>
							<tr>
								<td height="150" align="left" valign="top"  width=50%>Commentaires :<br>
								<td height="150" align="left" valign="top"  width=50%>Commentaires :<br>
							</tr>
						</table>
				<% } %>
				<IMG height="10" alt="" src="images/spacer.gif" width="1" border="0">
			</td>
		</tr>
	</TABLE>
	<!-- Section 3 !-->
	<TABLE id="TabSection4" cellSpacing="0" cellPadding="0" width="765" align="center" border="0"
		height="323">
		<tr>
			<td bgColor="#990000" height="20"><IMG height="4" alt="" src="images/spacer.gif" width="1" border="0">
				<span class="titlefont">Section 3 - Appréciation globale</span>
			</td>
		</tr>
		<tr>
			<td>
				<IMG height="10" alt="" src="images/spacer.gif" width="1" border="0">
				<table borderColor="black" cellSpacing="0" cellPadding="3" border="1" width="765" ID="TabCoteCompetences"
					height="76">
					<tr>
						<td><span class="boldtext">Le rendement de l'employé est :</span></td>
					</tr>
					<tr>
						<td>
							<table border="0" width="100%" ID="TableCoteApp">
								<%
								   Hashtable[] globalApps = (Hashtable[])formulaire.get("globalApps");
								   for(int i = 0; i < 5; i++)
								   {
							   %>
										<tr>
											<td width="2%"></td>
											<td width="20%">
												&nbsp;&nbsp;<IMG height="12" alt="" src="images/carre.jpg" width="12" border="0" align="middle">&nbsp;
												<input type="text" size="25" name="globalApp_Name" value="<%= (String)globalApps[i].get("Name") %>" class="boldtext">
											</td>
											<td>
												<input type="text" size="25" name="globalApp_Description" value="<%= (String)globalApps[i].get("Description") %>">
											</td>
										</tr>
								<% } %>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<IMG height="10" alt="" src="images/spacer.gif" width="1" border="0">
				<table borderColor="black" width="100%" height="139" cellSpacing="0" cellPadding="3" border="1"
					ID="Table6">
					<tr>
						<td><span class="boldtext">Expliquez votre appréciation en mettant en évidence les points forts et les points à améliorer de la personne.</span></td>
					</tr>
					<tr>
						<td height="120"></td>
					</tr>
				</table>
				<IMG height="4" alt="" src="images/spacer.gif" width="1" border="0">
			</td>
		</tr>
	</TABLE>
	<!-- Section 4 !-->
	<TABLE id="Table5" cellSpacing="0" cellPadding="0" width="765" align="center" border="0">
		<tr>
			<td bgColor="#003399" height="20"><IMG height="4" alt="" src="images/spacer.gif" width="1" border="0">
				<span class="titlefont">Section 4 - Recommandation pour l'employé en probation </span>
			</td>
		</tr>
		<tr>
			<td>
				<IMG height="10" alt="" src="images/spacer.gif" width="1" border="0">
				<table borderColor="black" height="0" cellSpacing="0" cellPadding="3" width="765" border="1"
					ID="TableRecommendation">
					<tr>
						<td>&nbsp;&nbsp;<IMG height="12" alt="" src="images/carre.jpg" width="12" border="0" align="middle">&nbsp;
							Maintenir en emploi</td>
						<td>&nbsp;&nbsp;<IMG height="12" alt="" src="images/carre.jpg" width="12" border="0" align="middle">&nbsp;
							Mettre fin à l'emploi</td>
					</tr>
				</table>
				<IMG height="4" alt="" src="images/spacer.gif" width="1" border="0">
			</td>
		</tr>
	</TABLE>
	<!-- Section 5 !-->
	<TABLE id="TabSection7" cellSpacing="0" cellPadding="0" width="765" align="center" border="0">
		<tr>
			<td bgColor="#003399" height="20"><IMG height="4" alt="" src="images/spacer.gif" width="1" border="0">
				<span class="titlefont">Section 5 - Plan de développement </span>
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
	<!-- Section 6 !-->
	<TABLE id="TabSection8" cellSpacing="0" cellPadding="0" width="765" align="center" border="0">
		<tr>
			<td bgColor="#003399" height="20"><IMG height="4" alt="" src="images/spacer.gif" width="1" border="0">
				<span class="titlefont">Section 6 - Commentaires de l'employé </span>
			</td>
		</tr>
		<tr>
			<td>
				<IMG height="10" alt="" src="images/spacer.gif" width="1" border="0">
				<table borderColor="black" height="0" cellSpacing="0" cellPadding="3" width="765" border="1"
					ID="TableComment">
					<tr>
						<td><span class="boldtext">Commentaires :</span></td>
					</tr>
					<tr>
						<td height="140"></td>
					</tr>
				</table>
				<IMG height="10" alt="" src="images/spacer.gif" width="1" border="0">
			</td>
		</tr>
	</TABLE>
	
		<TABLE id="TabSectionSignature" cellSpacing="0" cellPadding="0" width="765" align="center" border="0">
		<tr>
			<td bgColor="#003399" height="20"><IMG height="4" alt="" src="images/spacer.gif" width="1" border="0">
				<span class="titlefont">Section 7 - Signatures : </span>
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
						<td>Employé : </td>
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
	  <td>
	      <a href="javascript:saveForm()">
	   		<img alt="Sauvegarde du formulaire" align="right" border="0" src="images\btnSave.gif">
	      </a>
	  </td>
	 </tr>
   </table>
   <TABLE id="Table9" cellSpacing="0" cellPadding="0" width="765" align="center" border="0">
		<tr>
		<td align=right><a href=index.jsp>retour au menu principal</a><img src="images/spacer.gif" alt="" height="30" width="1" border="0"></td>
		</tr>
	</table>

</mt:erPage>
</mt:securityCheck>
