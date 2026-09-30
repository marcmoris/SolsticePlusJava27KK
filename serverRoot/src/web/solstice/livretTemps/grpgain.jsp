<%@ taglib uri="/WEB-INF/taglib.tld" prefix="mt" %>
<%@ page import="java.sql.*"%>
<%@ page import="org.compiere.util.DB"%>

<mt:securityCheck id="livrgrpgain">
<mt:pageTag id="livrgrpgain" validationClass="solstice.custom.LivretTempsGroupeCode">

	<script language="JavaScript">
		function ajouter()
		{
			var result = prompt('Veuillez entrer le nom du nouveau groupe', '');
			if(result != '')
			{
				document.mainForm.newGroup.value = result;
				document.mainForm.action.value = 'ajouter';
				document.mainForm.submit();
			}
		}
		
		function detruire()
		{
			if(confirm('Voulez-vous vraiment supprimer ce groupe?'))
			{
				document.mainForm.group_action.value = '';
				document.mainForm.action.value = 'detruire';
				document.mainForm.submit();
			}
		}
		
		function changeGroup()
		{
			// Relance l'initialisation
			document.mainForm.group_action.value = '';
			document.mainForm.submit();
		}
		
		function save()
		{
			// Pour forcer le rechargement des listes
			document.mainForm.group_action.value = ''; 
			document.mainForm.action.value = 'save';
			document.mainForm.submit();
		}
	</script>

	<form name="mainForm" method="post">
		<input type="hidden" name="action">
		<input type="hidden" name="newGroup">
		<table width="100%" border="0" cellpadding="1" cellspacing="1">
			<tr>
				<td class="tdlabel">Groupe</td>
				<td class="tdlabel" colspan="2" align="center">Actions</td>
			</tr>
			<tr>
				<td class="tdfield" width="100%">
					<mt:comboBoxTag id="groupId" onChange="changeGroup();" selectedValue="<%= request.getParameter("groupId") != null ? request.getParameter("groupId") : "" %>">
						Select P_Gain_Group_ID, ltrim(rtrim(Name)) 
						FROM P_Gain_Group
						where IsActive = 'Y'
						ORDER BY Name
					</mt:comboBoxTag>
				</td>
				<td class="tdfield">
					<input type="button" onClick="ajouter();" value="Ajouter">
				</td>
				<td class="tdfield">
<%
	boolean canDelete = false;
	if(request.getParameter("groupId") != null && !request.getParameter("groupId").equals(""))
	{
		canDelete = true;
		String strSql = "SELECT TOP 1 1 FROM P_Gain WHERE P_Gain_Group_ID = "+request.getParameter("groupId");
		PreparedStatement stmt = DB.prepareStatement(strSql);
		ResultSet rs = stmt.executeQuery();
		if(rs.next())
		{
			canDelete = false;
		}
		if(request.getParameter("action") != null && !request.getParameter("action").equals(""))
		{
			if(request.getParameter("action").equals("detruire"))
			{
				canDelete = false;
			}
		}
	}
%>
					<input type="button" onClick="detruire();" value="Détruire" <%=canDelete ? "" : "disabled=\"disabled\""%>>
				</td>
			</tr>
		</table><br><br>
		<mt:switch id="group" leftSufix="Non" rightSufix="Oui" leftInitialize="solstice.custom.LivretTempsGroupeCode.nonAssignes" rightInitialize="solstice.custom.LivretTempsGroupeCode.assignes" leftTitle="Code de gains non regroupés" rightTitle="Code de gains groupés"/>
		<div align="right">
			<input type="button" value="Sauvegarder" onClick="save();">
		</div>
	</form>

</mt:pageTag>
</mt:securityCheck>

