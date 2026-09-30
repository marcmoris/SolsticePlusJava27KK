<%@ taglib uri="/WEB-INF/taglib.tld" prefix="mt" %>

<mt:securityCheck id="livrvisgain">
<mt:pageTag id="livrvisgain" ref="<%= request.getRealPath("") + "/WEB-INF" %>" validationClass="solstice.custom.LivretTempsCodeVisibility">

	<script language="JavaScript">
		function save()
		{
			// Pour forcer le rechargement des listes
			document.mainForm.visible_action.value = ''; 
			document.mainForm.action.value = 'save';
			document.mainForm.submit();
		}
	</script>

	<form name="mainForm" method="post">
		<input type="hidden" name="action">
		<mt:switch id="visible" leftSufix="Non" rightSufix="Oui" leftTitle="Codes non visibles" rightTitle="Codes visibles" leftInitialize="solstice.custom.LivretTempsCodeVisibility.leftInitialize" rightInitialize="solstice.custom.LivretTempsCodeVisibility.rightInitialize"/>
		<div align="right"><input type="button" value="Sauvegarder" onClick="save();"></div>
	</form>

</mt:pageTag>
</mt:securityCheck>
