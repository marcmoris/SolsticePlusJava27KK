<%@ taglib uri="/WEB-INF/taglib.tld" prefix="mt" %>

<mt:securityCheck id="livrsrcgain">
<mt:pageTag id="livrsrcgain" ref="<%= request.getRealPath("") + "/WEB-INF" %>" validationClass="solstice.custom.LivretTempsCodeSecurite">

	<script language="JavaScript">
		function save()
		{
			// Pour forcer le rechargement des listes
			document.mainForm.access_action.value = ''; 
			document.mainForm.action.value = 'save';
			document.mainForm.submit();
		}
	</script>

	<form name="mainForm" method="post">
		<input type="hidden" name="action">
		<mt:switch id="access" leftSufix="Non" rightSufix="Oui" leftTitle="Codes non accessibles" rightTitle="Codes accessibles" leftInitialize="solstice.custom.LivretTempsCodeSecurite.leftInitialize" rightInitialize="solstice.custom.LivretTempsCodeSecurite.rightInitialize"/>
		<div align="right"><input type="button" value="Sauvegarder" onClick="save();"></div>
	</form>

</mt:pageTag>
</mt:securityCheck>
