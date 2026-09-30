<%@ taglib uri="/WEB-INF/taglib.tld" prefix="mt" %>
<%@ page import="java.sql.*" %>
<%@ page import="org.compiere.util.DB" %>

<mt:securityCheck id="permisHorodateur_remplir">
	<mt:pageTag id="permisHorodateur_remplir" supportCalendar="true" validationClass="solstice.custom.HorodateurRemplir">
	
   	<%
   	   
   	   int employeeId;
   	   if(request.getParameter("employeeId") != null && !"".equals(request.getParameter("employeeId")))
   	      employeeId = Integer.parseInt(request.getParameter("employeeId"));
   	   else
      	   employeeId = ((solstice.custom.IUserInfo)session.getAttribute("userInfo")).getEmployeeId();
      	   
   	   String punchCardId = request.getParameter("punchCardId");
   	   Timestamp datePunchCard = null;
   	   String reasonId = null;
   	   String morningStart = null;
   	   String morningEnd = null;
   	   String afternoonStart = null;
   	   String afternoonEnd = null;
   	   String reasonOther = null;
   	   
   	   boolean readOnly = false;
   	   boolean ready = false;
   	
   	   // On doit d'abord vérifier s'il s'agit d'un nouvel enregistrement
   	   // ou si on doit charger un enregistrement existant
   	   if(!"nouveau".equals(request.getParameter("action"))
   	      && punchCardId != null && !"".equals(punchCardId))
         {
            PreparedStatement stmt = DB.prepareStatement(
               "select punchCard.P_PunchCard_ID," +
               "       punchCard.DatePunchCard," +
               "       isnull(punchCard.MorningStart, '') as MorningStart," +
               "       isnull(punchCard.MorningEnd, '') as MorningEnd," +
               "       isnull(punchCard.AfternoonStart, '') as AfternoonStart," +
               "       isnull(punchCard.AfternoonEnd, '') as AfternoonEnd," +
               "       punchCard.P_PunchCard_Reason_ID," +
               "       isnull(punchCard.Reason_Other, '') as Reason_Other," +
               "       absenceStatus.Value as Status," +
               "       punchCard.P_Employee_ID" +
               "  from P_PunchCard punchCard" +
               "  join P_Absence_Status absenceStatus" +
               "    on absenceStatus.P_Absence_Status_ID = punchCard.P_Absence_Statut_ID" +
               " where punchCard.P_PunchCard_ID = " + punchCardId);
               
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next())
            {
               // On a trouvé l'enregistrement, on prépare donc les variables
               datePunchCard = rs.getTimestamp("DatePunchCard");
               reasonId = rs.getString("P_PunchCard_Reason_ID");
               morningStart = rs.getString("MorningStart").trim();
               morningEnd = rs.getString("MorningEnd").trim();
               afternoonStart = rs.getString("AfternoonStart").trim();
               afternoonEnd = rs.getString("AfternoonEnd").trim();
               reasonOther = rs.getString("Reason_Other");
               employeeId = rs.getInt("P_Employee_ID");
               
               readOnly = !"INIT".equals(rs.getString("Status"));
               ready = true;
            }
            
            rs.close();
            stmt.close();
         }
         
         if(!ready)
         {
            // On doit préparer la saisie d'un nouvel enregistrement
            punchCardId = "-1";
            datePunchCard = null;
            reasonId = "";
            morningStart = "";
            morningEnd = "";
            afternoonStart = "";
            afternoonEnd = "";
            reasonOther = "";
         }
   	   
   	%>
   	
   	<script>
   	
   	   function demanderApprobation()
   	   {
      	   var erreurValidation = validateForm();
      	   if(erreurValidation == null)
      	   {
         	   document.getElementById("action").value = "demanderApprobation";
         	   document.mainForm.submit();
      	   }
      	   else
      	   {
         	   alert(erreurValidation);
      	   }
   	   }
   	   
   	   function enregistrer()
   	   {
      	   var erreurValidation = validateForm();
      	   if(erreurValidation == null)
      	   {
         	   document.getElementById("action").value = "enregistrer";
         	   document.mainForm.submit();
      	   }
      	   else
      	   {
         	   alert(erreurValidation);
      	   }
   	   }
   	   
   	   function nouveau()
   	   {
      	   document.getElementById("action").value = "nouveau";
      	   document.mainForm.submit();
   	   }
   	   
   	   function validateForm()
   	   {
      	   // On valide d'abord qu'il y ait une date de saisie
      	   if(document.getElementById("datePunchCard").value == "")
      	      return "La date de la demande de permis horodateur est obligatoire";
      	      
      	   // On valide ensuite que les heures saisies respecte le format HH:mm
      	   if((document.getElementById("morningStart").value != ""
      	      && validHeure(document.getElementById("morningStart").value) == false)
      	      || (document.getElementById("morningEnd").value != ""
      	      && validHeure(document.getElementById("morningEnd").value) == false)
      	      || (document.getElementById("afternoonStart").value != ""
      	      && validHeure(document.getElementById("afternoonStart").value) == false)
      	      || (document.getElementById("afternoonEnd").value != ""
      	      && validHeure(document.getElementById("afternoonEnd").value) == false))
      	   {
         	   return "Vérifier le format des heures (HH:mm)";
      	   }
      	   
      	   // On doit vérifier si on a saisi un motif
      	   if(document.getElementById("reasonId").value == null
      	      || document.getElementById("reasonId").value == "")
      	   {
         	   return "Vous devez obligatoirement saisir un motif pour cette demande de permis horodateur.";
      	   }
      	   
      	   // Il reste à valider que si on a pris le choix Autre comme
      	   // motif qu'on ait saisie obligatoirement une raison
      	   if(document.getElementById("reasonId").value == <%= compiere.model.P_PunchCard_Reason.getID("AUTRE") %>
      	      && document.getElementById("reasonOther").value == "")
      	   {
      	      return "Si vous sélectionnez 'Autre' comme motif, vous devez obligatoirement fournir une explication.";
   	      }
      	   
   	      // On valide ensuite qu'on ait saisi au moins une heure
   	      if(document.getElementById("morningStart").value == ""
   	         && document.getElementById("morningEnd").value == ""
   	         && document.getElementById("afternoonStart").value == ""
   	         && document.getElementById("afternoonEnd").value == "")
   	      {
      	      return "Vous devez remplir au moins une case de temps";
   	      }
   	      
      	   return null;
   	   }
   	
         function validHeure(inputValue)
         {
            if(inputValue.length == 5)
            {
               // On doit s'assurer d'avoir une heure
               // dans le format HH:mm
               if(inputValue.charAt(0) >= '0' && inputValue.charAt(0) <= '9'
                  && inputValue.charAt(1) >= '0' && inputValue.charAt(1) <= '9'
                  && inputValue.charAt(2) == ':'
                  && inputValue.charAt(3) >= '0' && inputValue.charAt(3) <= '9'
                  && inputValue.charAt(4) >= '0' && inputValue.charAt(4) <= '9')
               {
                  // Il reste à vérifier que les minutes sont entre 0 et 59 et
                  // que les heures sont entre 0 et 23
                  var minute = parseInt("" + inputValue.charAt(3) + inputValue.charAt(4));
                  var heure = parseInt("" + inputValue.charAt(0) + inputValue.charAt(1));
                  if(minute >= 0 && minute <= 59 && heure >= 0 && heure <= 23)
                     return true;
               }
            }
            else if(inputValue.length == 4)
            {
               // On doit s'assurer d'avoir une heure
               // dans le format H:mm
               if(inputValue.charAt(0) >= '0' && inputValue.charAt(0) <= '9'
                  && inputValue.charAt(1) == ':'
                  && inputValue.charAt(2) >= '0' && inputValue.charAt(3) <= '9'
                  && inputValue.charAt(3) >= '0' && inputValue.charAt(4) <= '9')
               {
                  // Il reste à vérifier que les minutes sont entre 0 et 59
                  var minute = parseInt("" + inputValue.charAt(2) + inputValue.charAt(3));
                  if(minute >= 0 && minute <= 59)
                     return true;
               }
            }
            return false;
         }
   	</script>
   	
   	<form name="mainForm" id="mainForm" method="post">
      	
      	<input type="hidden" name="action" id="action" />
      	<input type="hidden" name="punchCardId" id="punchCardId" value="<%= punchCardId %>" />
      	<input type="hidden" name="employeeId" id="employeeId" value="<%= employeeId %>" />
   	
         <p class="sectitle">Section 1 - Identification</p>
         
         <mt:dynamicStructure>
            <mt:customQuery>
                 select top 1 employee.Name, employee.Value, isnull(ltrim(rtrim(activity.Value)) + ' - ' + activity.Name, 'N/A') as Activity,
                 isnull(occupationGroup.Name, 'N/A') as OccupationGroup, isnull(jobType.Name, 'N/A') as JobType,
                 jobType.P_Job_Type_ID, occupationGroup.P_Occupation_Group_ID, activity.C_Activity_ID
                 from ((P_Employee employee left join P_Job_Type jobType
                 on employee.P_Job_Type_ID = jobType.P_Job_Type_ID) left join P_Occupation_Group occupationGroup
                 on employee.P_Occupation_Group_ID = occupationGroup.P_Occupation_Group_ID) 
                 left join (P_Assignment assignment inner join (P_Post post inner join C_Activity activity
                 on post.C_Activity_ID = activity.C_Activity_ID)
                 on assignment.P_Post_ID = post.P_Post_ID)
                 on employee.P_Employee_ID = assignment.P_Employee_ID
                 where isnull(assignment.AssignmentType, 'P') = 'P'
                 and employee.P_Employee_ID = <%= employeeId %>
            </mt:customQuery>
            
            <mt:body>
                 <table width="100%" border="0" cellspacing="1" cellpadding="1">
                     <tr>
                         <td class="tdlabel">Nom de l'employ&eacute;</td>
                         <td class="tdlabel">No. d'employ&eacute;</td>
                     </tr>
                     <tr>
                         <td class="tdfield"><mt:dynamicValue id="Name"/></td>
                         <td class="tdfield"><mt:dynamicValue id="Value"/></td>
                     </tr>
                     <tr>
                         <td class="tdlabel" colspan="2">Unit&eacute; administrative</td>
                     </tr>
                     <tr>
                         <td class="tdfield" colspan="2">
                             <mt:dynamicValue id="Activity"/>
                             <input type="hidden" name="activityId" value="<mt:dynamicValue id="C_Activity_ID"/>">
                         </td>
                     </tr>
                     <tr>
                         <td class="tdlabel">Cat&eacute;gorie de personnel</td>
                         <td class="tdlabel">Statut</td>
                     </tr>
                     <tr>
                         <td class="tdfield">
                             <mt:dynamicValue id="OccupationGroup"/>
                             <input type="hidden" name="occupationGroupId" value="<mt:dynamicValue id="P_Occupation_Group_ID"/>">
                         </td>
                         <td class="tdfield">
                             <mt:dynamicValue id="JobType"/>
                             <input type="hidden" name="jobTypeId" value="<mt:dynamicValue id="P_Job_Type_ID"/>">
                         </td>
                     </tr>
                 </table>
            </mt:body>
         </mt:dynamicStructure>
         
         <p class="sectitle">Section 2 - Demande de l'employé</p>
         
         <table width="100%" border="0" cellspacing="1" cellpadding="1">
            <tr>
               <td colspan="5" class="tdlabel">Motif</td>
            </tr>
            <tr>
               <td colspan="5" class="tdfield">
                  <mt:comboBoxTag id="reasonId" selectedValue="<%= reasonId %>" readOnly="<%= readOnly %>">
                     select P_PunchCard_Reason_ID,
                            rtrim(Name) + ' (' + rtrim(Value) + ')'
                       from P_PunchCard_Reason
                      where IsActive = 'Y'
                  </mt:comboBoxTag>
               </td>
            </tr>
            <tr>
               <td colspan="5" class="tdlabel">Autre (s'il y a lieu)</td>
            </tr>
            <tr>
               <td colspan="5" class="tdfield">
                  <textarea name="reasonOther" id="reasonOther" rows="4" style="width: 100%" maxlength="255" <% if(readOnly) out.print("readonly=\"readonly\""); %>><%= reasonOther %></textarea>
               </td>
            </tr>
            <tr>
               <td class="tdlabel">Date</td>
               <td class="tdlabel">Arrivée</td>
               <td class="tdlabel">Départ/dîner</td>
               <td class="tdlabel">Retour/dîner</td>
               <td class="tdlabel">Départ</td>
            </tr>
            <tr>
               <td class="tdfield"><mt:simpleCalendar id="datePunchCard" formName="mainForm" value="<%= datePunchCard %>" readOnly="<%= readOnly %>" /></td>
               <td class="tdfield"><input type="text" size="5" maxlength="5" id="morningStart" name="morningStart" value="<%= morningStart %>" <% if(readOnly) out.print("readonly=\"readonly\""); %> /></td>
               <td class="tdfield"><input type="text" size="5" maxlength="5" id="morningEnd" name="morningEnd" value="<%= morningEnd %>" <% if(readOnly) out.print("readonly=\"readonly\""); %> /></td>
               <td class="tdfield"><input type="text" size="5" maxlength="5" id="afternoonStart" name="afternoonStart" value="<%= afternoonStart %>" <% if(readOnly) out.print("readonly=\"readonly\""); %> /></td>
               <td class="tdfield"><input type="text" size="5" maxlength="5" id="afternoonEnd" name="afternoonEnd" value="<%= afternoonEnd %>" <% if(readOnly) out.print("readonly=\"readonly\""); %> /></td>
            </tr>
         </table>
         
         <p align="right">
            <input type="button" value="Demander approbation" onclick="demanderApprobation();" <% if(readOnly) out.print("disabled=\"disabled\""); %> />
         </p>
   	
         <p align="right">
            <input type="button" value="Enregistrer" onclick="enregistrer();" <% if(readOnly) out.print("disabled=\"disabled\""); %> />
            <input type="button" value="Liste des permis horodateur" onclick="document.location = 'consulter.jsp';" />
            <input type="button" value="Nouveau" onclick="nouveau();" />
         </p>
         
      </form>

	</mt:pageTag>
</mt:securityCheck>