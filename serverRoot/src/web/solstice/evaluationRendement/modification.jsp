<%@ taglib uri="/WEB-INF/taglib.tld" prefix="mt" %>
<%@ page import="java.sql.*" %>
<%@ page import="org.compiere.util.DB" %>
<%@ page import="java.text.SimpleDateFormat" %>

<mt:securityCheck id="evaluationRendement_modification">
<mt:pageTag id="evaluationRendement_modification" validationClass="solstice.custom.ERModificationValidation" supportCalendar="true">

<script language="JavaScript">
		function validAllDate()
		{
			var bError = 0;
			if(validDate(document.mainForm.formDate.value) == 1)
			{
				alert("La date de l'évaluation n'est pas valide.\nVeuillez utiliser le format YYYY-MM-JJ.");			
				bError = 1;
			}
			return bError;
		}

		function modForm()
		{
			if(validAllDate() == 0)
			{
				document.mainForm.action.value = 'modification'
			}
			else
			{
				return false;
			}
		}
</script>
	<form method="post" name="mainForm">
	
		<input type="hidden" name="action">
	
		<table width="100%" cellspacing="1" cellpadding="1" border="0">
		   <tr>
				<td class="tdlabel">Type de formulaire</td>
				<td class="tdfield">
         		<select name="formType" onChange="this.form.submit();">
         		   <option<%= request.getParameter("formType") == null || request.getParameter("formType").equals("") ? " selected" : "" %>></option>
         			<option value="GES"<%= request.getParameter("formType") != null && request.getParameter("formType").equals("GES") ? " selected" : "" %>>&Eacute;valuation des gestionnaires</option>
         			<option value="PTB"<%= request.getParameter("formType") != null && request.getParameter("formType").equals("PTB") ? " selected" : "" %>>&Eacute;valuation du personnel professionnel, technique et de bureau</option>
         			<option value="OUV"<%= request.getParameter("formType") != null && request.getParameter("formType").equals("OUV") ? " selected" : "" %>>&Eacute;valuation du personnel ouvrier</option>
         		</select>
				</td>
			</tr>
			<tr>
				<td class="tdlabel">Date d'entrée en vigueur</td>
				<td class="tdfield">
         		<input type="text" name="formDate" /><br>
				</td>
		   </tr>
		</table>
		
		<%
		   // On doit afficher la liste des formulaires pouvant être évalué selon la
		   // date de leur entrée en vigueur. Évidemment, cette section s'affiche
		   // seulement s'il y a un formulaire de sélectionné.
		   if(request.getParameter("formType") != null && !request.getParameter("formType").equals(""))
		   {
   		   String sql
   		      = "   select * "+
  			" from P_ER_Form f "+
 			" where f.FormType = '"+request.getParameter("formType")+"' "+
   			" and f.Version = (select max(P_ER_Form.Version) "+
                       	" from P_ER_Form "+
                      	" where P_ER_Form.FormType = '"+request.getParameter("formType")+"' "+
                        " and day(P_ER_Form.FormDate) = day(f.FormDate) "+
			" and month(P_ER_Form.FormDate) = month(f.FormDate) "+
			" and year(P_ER_Form.FormDate) = year(f.FormDate))"+
			" order by f.FormDate";
               
            PreparedStatement stmt = DB.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next())
            {
               SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
               %>
                  <table width="100%" cellspacing="1" cellpadding="1" border="0">
                     <tr>
                        <td class="tdlabel" colspan="2">
                           Liste des formulaires pouvant être modifiés
                        </td>
                     </tr>
                     <tr>
                        <td class="tdfield">
                           <input type="radio" name="formId" value="<%= rs.getInt("P_ER_Form_ID") %>" checked>
                        </td>
                        <td class="tdfield" width="100%">
                           <%= simpleDateFormat.format(rs.getTimestamp("FormDate")) %>
                           <input type="hidden" name="modifiedFormDate" value="<%= simpleDateFormat.format(rs.getTimestamp("FormDate")) %>">
                        </td>
                     </tr>
               <%
               
               while(rs.next())
               {
                  %>
                     <tr>
                        <td class="tdfield">
                           <input type="radio" name="formId" value="<%= rs.getInt("P_ER_Form_ID") %>">
                        </td>
                        <td class="tdfield" width="100%">
                           <%= simpleDateFormat.format(rs.getTimestamp("FormDate")) %>
                           <input type="hidden" name="modifiedFormDate" value="<%= simpleDateFormat.format(rs.getTimestamp("FormDate")) %>">
                        </td>
                     </tr>
                  <%
               }
               
               
               %>
                  </table>
               <%
            }
            
            rs.close();
            stmt.close();
		   }
		%>
		
		<p align="right">
		    <input type="submit" onClick="modForm();" value="Modifier l'&eacute;valuation">
		</p>

	</form>

</mt:pageTag>
</mt:securityCheck>
