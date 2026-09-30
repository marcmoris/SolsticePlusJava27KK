<%@ taglib uri="/WEB-INF/taglib.tld" prefix="mt" %>

<mt:securityCheck id="permisHorodateur_convention">
	<mt:pageTag id="permisHorodateur_convention" validationClass="solstice.custom.HorodateurAdministration">
	
	   <form method="post" name="mainForm">
	
	   <mt:switch id="conventions" leftSufix="L" rightSufix="R"
	      leftInitialize="solstice.custom.HorodateurAdministration.getNotGrantedConvention"
	      rightInitialize="solstice.custom.HorodateurAdministration.getGrantedConvention"
	      leftTitle="Conventions collectives"
	      rightTitle="Accès au permis horodateur" />
	
	   <input type="hidden" name="action" id="action" />
	   
	   <p align="right">
	      <input type="submit" value="Enregistrer" onclick="document.getElementById('action').value = 'enregistrerAcces';" />
	   </p>
	      
	   </form>
	   
	</mt:pageTag>
</mt:securityCheck>