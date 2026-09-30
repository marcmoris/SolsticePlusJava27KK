<%@ taglib uri="/WEB-INF/taglib.tld" prefix="mt" %>

<mt:securityCheck id="addabssig">
	<mt:pageTag id="addabssig" supportCalendar="true" validationClass="solstice.custom.AbsenceSignifiee">
	
	    <script language="javascript">
	        var absencePeriod = null;
	    
	        function setHourReadOnly(value)
	        {
    	        absencePeriod = value;
    	        if(value == 'Z')
    	        {
        	        document.mainForm.startHour.readOnly = false;
        	        document.mainForm.endHour.readOnly = false;
    	        }
    	        else
    	        {
        	        document.mainForm.startHour.readOnly = true;
        	        document.mainForm.endHour.readOnly = true;
    	        }
	        }
	        
	        function saveAbsence()
	        {
    	        if(document.mainForm.employeeId.selectedIndex > 0
    	            && document.mainForm.absenceDate.value != ""
    	            && absencePeriod != null)
    	        {
    	        
        	        if(!document.mainForm.startHour.readOnly 
        	            && (!validTime(document.mainForm.startHour.value)
        	                || !validTime(document.mainForm.endHour.value)))
        	        {
            	        alert("Vous devez obligatoirement saisir des heures valides (hh:mm)");
        	        }
        	        else
        	        {
            	        document.mainForm.action.value = "save";
            	        document.mainForm.submit();
        	        }
        	        
    	        }
    	        else
    	        {
        	        alert("Vous devez obligatoirement saisir un employé, une date et une période de la journée");
    	        }
	        }
	        
	        function validTime(value)
	        {
    	        if(value.length == 5)
    	        {
        	        var numbers = new String("0123456789");
        	        return (numbers.indexOf(value.charAt(0)) >= 0
        	            && numbers.indexOf(value.charAt(1)) >= 0
        	            && value.charAt(2) == ':'
        	            && numbers.indexOf(value.charAt(3)) >= 0
        	            && numbers.indexOf(value.charAt(4)) >= 0);
    	        }
    	        return false;
	        }
	    </script>
	
	    <form name="mainForm" method="post">

	        <input type="hidden" name="action">
	    
	        <p class="sectitle">Section - Identification</p>
	        
	        <table width="100%" cellspacing="1" cellpadding="1" border="0">
	            <tr>
	                <td class="tdlabel">Nom et Num&eacute;ro de l'employ&eacute;</td>
	            </tr>
	            <tr>
	                <td class="tdfield">
	                    <mt:comboBoxTag id="employeeId" getPostValue="true">
	                       <%= solstice.custom.AutorisationAbsenceADM.getEmployeeSQL(pageContext) %>
	                    </mt:comboBoxTag>
	                </td>
	            </tr>
	            <tr>
	                <td class="tdlabel">Date de l'absence</td>
	            </tr>
	            <tr>
	                <td class="tdfield"><mt:simpleCalendar formName="mainForm" id="absenceDate"/></td>
	            </tr>
	            <tr>
	                <td class="tdlabel">P&eacute;riode de l'absence</td>
	            </tr>
	            <tr>
	                <td class="tdfield">
	                    <table width="100%" cellspacing="0" cellpadding="1" border="0">
	                        <tr>
	                            <td class="tdfield">
	                                <div>
    	                                <input type="radio" name="absencePeriod" value="A" onClick="setHourReadOnly('A');">
    	                                Avant-midi
    	                            </div>
	                            </td>
	                            <td class="tdfield">
	                                <div>
    	                                <input type="radio" name="absencePeriod" value="J" onClick="setHourReadOnly('J');">
    	                                Journ&eacute;e
    	                            </div>
	                            </td>
	                            <td>&nbsp;</td>
	                        </tr>
	                        <tr>
	                            <td class="tdfield">
	                                <div>
    	                                <input type="radio" name="absencePeriod" value="P" onClick="setHourReadOnly('P');">
    	                                Apr&egrave;s-midi
    	                            </div>
	                            </td>
	                            <td class="tdfield">
	                                <div>
    	                                <input type="radio" name="absencePeriod" value="Z" onClick="setHourReadOnly('Z');">
    	                                Autre
    	                            </div>
	                            </td>
	                            <td class="tdfield">
	                                <div>
	                                    Heure de début
	                                    <input type="text" name="startHour" readonly="true">
	                                    &agrave l'heure de fin
	                                    <input type="text" name="endHour" readonly="true">
	                                    <sub>(hh:mm)<sub>
	                                </div>
	                            </td>
	                        </tr>
	                    </table>
	                </td>
	            </tr>
	            <tr>
	                <td class="tdlabel">Remarque</td>
	            </tr>
	            <tr>
	                <td class="tdfield">
	                    <textarea name="remarque" rows="4" style="width: 100%"></textarea>
	                </td>
	            </tr>
	        </table><br>
	        <p align="right"><input type="button" value="Sauvegarder" onClick="saveAbsence();"></p>
	    	    
	    </form>  
	
	</mt:pageTag>
</mt:securityCheck>
