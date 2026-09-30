<%@ taglib uri="/WEB-INF/taglib.tld" prefix="mt" %>
<%@ page import="java.sql.*" %>
<%@ page import="org.compiere.util.DB" %>

<mt:securityCheck id="permisHorodateur_raison">
	<mt:pageTag id="permisHorodateur_raison" validationClass="solstice.custom.HorodateurAdministration">
	
	   <%
	   
	      // On doit d'abord vérifier si on doit créer un nouvel enregistrement
	      // ou si on doit charger un enregistrement déjà existant.
	      String punchCardReasonId = request.getParameter("punchCardReasonId");
	      String value = null;
	      String active = null;
	      String name = null;
	      boolean ready = false;
	      
	      if(punchCardReasonId != null && !punchCardReasonId.equals("") && !"nouveau".equals(request.getParameter("action")))
	      {
   	      // On doit charger l'enregistrement dans la base de données
   	      PreparedStatement stmt = DB.prepareStatement(
   	         "select P_PunchCard_Reason_ID," +
   	         "       Value," +
   	         "       IsActive," +
   	         "       Name" +
   	         "  from P_PunchCard_Reason" +
   	         " where P_PunchCard_Reason_ID = " + punchCardReasonId);
   	         
   	      ResultSet rs = stmt.executeQuery();
   	      
   	      if(rs.next())
   	      {
      	      punchCardReasonId = rs.getString("P_PunchCard_Reason_ID");
      	      value = rs.getString("Value");
      	      active = "Y".equals(rs.getString("IsActive")) ? " checked=\"checked\"" : "";
      	      name = rs.getString("Name");
      	      ready = true;
   	      }
   	      
   	      rs.close();
   	      stmt.close();
	      }
	      
	      if(!ready)
	      {
   	      // On doit préparer un nouvel enregistrement
   	      punchCardReasonId = "-1";
   	      value = "";
   	      active = " checked=\"checked\"";
   	      name = "";
	      }
	   
	   %>
	   
	   <script>

	      function deleteMotif(id)
	      {
   	      document.getElementById("punchCardReasonId").value = id;
   	      document.getElementById("action").value = "deleteMotif";
   	      document.mainForm.submit();
	      }
	   	   
	      function editMotif(id)
	      {
   	      document.getElementById("punchCardReasonId").value = id;
   	      document.mainForm.submit();
	      }
	      
	      function saveMotif()
	      {
   	      // On s'assure que tous les champs sont remplis
   	      if(document.getElementById("value").length != ""
   	         && document.getElementById("name").length != "")
	         {
   	         document.getElementById("action").value = "enregistrerMotif";
   	         document.mainForm.submit();
	         }
	         else
	         {
   	         alert("Vous devez remplir tous les champs");
	         }
	      }

	   </script>
	
	   <p class="sectitle">Section 1 - Saisie</p>
	   
	   <form name="mainForm" id="mainForm" method="post">
   	   
   	   <input type="hidden" name="punchCardReasonId" id="punchCardReasonId" value="<%= punchCardReasonId %>" />
   	   <input type="hidden" name="action" id="action" />
   	   
   	   <table cellspacing="1" cellpadding="1" border="0" width="100%">
   	      <tr>
   	         <td class="tdlabel">
   	            Clé de recherche
   	         </td>
   	         <td class="tdfield">
   	            <input type="text" name="value" id="value" value="<%= value %>" maxlength="5" size="5" />
   	         </td>
   	         <td class="tdfield">
   	            <input type="checkbox" name="active" id="active"<%= active %> />&nbsp;Actif
   	         </td>
   	      </tr>
   	      <tr>
   	         <td class="tdlabel">
   	            Nom
   	         </td>
   	         <td class="tdfield" colspan="2">
   	            <input type="text" name="name" id="name" value="<%= name %>" maxlength="100" size="100" />
   	         </td>
   	      </tr>
   	   </table>
   	   
   	   <p align="right">
   	      <input type="button" value="Nouveau" onclick="document.getElementById('action').value = 'nouveau'; document.mainForm.submit();" />
   	      <input type="button" value="Enregistrer" onclick="saveMotif();" />
   	   </p>
   	   
   	</form>
	   
	   <p class="sectitle">Section 2 - Liste des motifs</p>
	   
	   <mt:grid>
	      <mt:customQuery orderBy="Value">
            select P_PunchCard_Reason.Value,
                   P_PunchCard_Reason.Name,
                   case P_PunchCard_Reason.IsActive when 'Y' then ' checked="checked"'
                                                    else ''
                   end as IsActive,
                   P_PunchCard_Reason.P_PunchCard_Reason_ID
              from P_PunchCard_Reason
	      </mt:customQuery>
	      <mt:field id="Value" name="Clé de recherche" type="String" />
	      <mt:field id="Name" name="Nom" type="String" />
	      <mt:field id="IsActive" name="Actif" type="String">
            <mt:fieldStyle alt="false">
               <input type="checkbox" disabled="disabled" @@ />
            </mt:fieldStyle>
	      </mt:field>
	      <mt:fieldGroup name="Actions">
	         <mt:field id="P_PunchCard_Reason_ID" type="String">
               <mt:fieldStyle alt="false">
                  <a href="javascript:deleteMotif(@@)">
                     <img src="images/delete.gif" border="0" />
                  </a>
               </mt:fieldStyle>
	         </mt:field>
	         <mt:field id="P_PunchCard_Reason_ID" type="String">
               <mt:fieldStyle alt="false">
                  <a href="javascript:editMotif(@@)">
                     <img src="images/edit.gif" border="0" />
                  </a>
               </mt:fieldStyle>
	         </mt:field>
	      </mt:fieldGroup>
	   </mt:grid>
	
	</mt:pageTag>
</mt:securityCheck>