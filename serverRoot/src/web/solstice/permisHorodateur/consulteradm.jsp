<%@ taglib uri="/WEB-INF/taglib.tld" prefix="mt" %>

<mt:securityCheck id="permisHorodateur_consulteradm">
	<mt:pageTag id="permisHorodateur_consulteradm" supportCalendar="true" validationClass="solstice.custom.HorodateurConsulter">
	
	   <script>
	      
	      function sendAction(action, id)
	      {
            if(document.mainForm.cDatePunchCard.checked && document.mainForm.datePunchCard.value == "")
            {
               alert("Si vous cochez une date, vous devez obligatoirement en saisir une");
            }
            else
            {
               document.mainForm.action.value = action;
               document.mainForm.punchCardId.value = id;
               document.mainForm.submit();
            }
	      }
	   
         var datePunchCard = new String();

         function dateListener()
         {
            if(datePunchCard != document.mainForm.datePunchCard.value)
            {
               datePunchCard = document.mainForm.datePunchCard.value;
               document.mainForm.cDatePunchCard.checked = (document.mainForm.datePunchCard.value != "");
            }
         }
         
	   </script>
	
   	<table width="100%" border="0" cellspacing="1" cellpadding="1">
   	   <tr>
   	      <td class="tdlabel">
   	         Liste des demandes de permis horodateur à approuver par
   	      </td>
   	   </tr>
   	   <tr>
   	      <td class="tdfield">
   	         <%= ((solstice.custom.IUserInfo)session.getAttribute("userInfo")).getEmployeeName() %>
   	      </td>
   	   </tr>
   	</table>
   
   	<form method="post" name="mainForm" id="mainForm">
   	
   	   <input type="hidden" name="action" id="action" />
   	   <input type="hidden" name="punchCardId" id="punchCardId" />
   	
      	<mt:grid>
      	   <mt:queryBuilder checkMethod="solstice.custom.AutorisationAbsence.checkConditionStartup" orderBy="punchCard.DatePunchCard desc">
               select demandeur.Name as DemandeurName,
                      reason.Name + isnull('<br>' + replace(punchCard.Reason_Other, char(13) + char(10), '<br>'), '') as Reason,
                      punchCard.DatePunchCard,
                      punchCard.MorningStart,
                      punchCard.MorningEnd,
                      punchCard.AfternoonStart,
                      punchCard.AfternoonEnd,
                      status.Name as StatusValue,
                      case status.Value
                        <%= solstice.custom.HorodateurConsulter.peutApprouver(pageContext) %>
                        else null
                      end as Accept,
                      case status.Value
                        <%= solstice.custom.HorodateurConsulter.peutRefuser(pageContext) %>
                        else null
                      end as Refus, 
                      punchCard.P_PunchCard_ID as Edit,
                      case status.Value
                        <%= solstice.custom.HorodateurConsulter.peutSupprimer(pageContext) %>
                        else null
                      end as Suppr
                 from P_PunchCard punchCard
                 join P_Absence_Status status
                   on status.P_Absence_Status_ID = punchCard.P_Absence_Statut_ID
                 join P_PunchCard_Reason reason
                   on reason.P_PunchCard_Reason_ID = punchCard.P_PunchCard_Reason_ID
                 join P_Employee demandeur
                   on demandeur.P_Employee_ID = punchCard.P_Employee_ID
               <mt:queryCondition id="startup">status.Value in ('ATTE', 'DSUP')</mt:queryCondition>
               <mt:queryCondition id="distributionId">demandeur.P_Distribution_Booklet_ID = <%= request.getParameter("distributionId") %></mt:queryCondition>
               <mt:queryCondition id="employeeId">demandeur.P_Employee_ID = <%= request.getParameter("employeeId") %></mt:queryCondition>
               <mt:queryCondition id="status">status.Value in (<%= request.getParameter("status") %>)</mt:queryCondition>
               <mt:queryCondition id="cDatePunchCard">
                   punchCard.DatePunchCard between convert(datetime, '<%= request.getParameter("datePunchCard") %>T00:00:00.000', 126)
                                               and convert(datetime, '<%= request.getParameter("datePunchCard") %>T23:59:59.998', 126)
               </mt:queryCondition>
               <mt:staticQueryCondition>demandeur.IsActive = 'Y'</mt:staticQueryCondition>
               <mt:staticQueryCondition>exists (SELECT 1 FROM P_Employee_Distribution_Booklet edb Where edb.P_Employee_ID = demandeur.P_Employee_ID And punchCard.DatePunchCard between edb.Start_Date And IsNull(End_Date, punchCard.DatePunchCard) And edb.P_Distribution_Booklet_ID in (<%= solstice.custom.AutorisationAbsenceADM.getDistributionSQL(pageContext,new String[] {"P_Distribution_Booklet_ID"}, false) %>))</mt:staticQueryCondition>
      	   </mt:queryBuilder>
      	   
      	   <mt:field id="DemandeurName" name="Demandé par" type="String" />
      	   <mt:field id="Reason" name="Motif du permis horodateur" type="String" />
      	   <mt:field id="DatePunchCard" name="Date" type="Date" format="yyyy-MM-dd" />
      	   <mt:field id="MorningStart" name="Arrivée" type="String" nullValue="&nbsp;" />
      	   <mt:field id="MorningEnd" name="Départ/dîner" type="String" nullValue="&nbsp;" />
      	   <mt:field id="AfternoonStart" name="Retour/dîner" type="String" nullValue="&nbsp;" />
      	   <mt:field id="AfternoonEnd" name="Départ" type="String" nullValue="&nbsp;" />
      	   <mt:field id="StatusValue" name="Statut" type="String" />

            <mt:field id="Accept" name="Accept." type="String" nullValue="<img src=\"images/approbacceptg.gif\">">
                <mt:fieldStyle alt="false">
                    <a href="javascript:sendAction('accept', @@)">
                        <img src="images/approbaccept.gif" border="0">
                    </a>
                </mt:fieldStyle>
            </mt:field>
            <mt:field id="Refus" name="Refus." type="String" nullValue="<img src=\"images/approbrejectg.gif\">">
                <mt:fieldStyle alt="false">
                    <a href="javascript:sendAction('refus', @@)">
                        <img src="images/approbreject.gif" border="0">
                    </a>
                </mt:fieldStyle>
            </mt:field>
            <mt:field id="Edit" name="&Eacute;dit." type="String">
                <mt:fieldStyle alt="false">
                    <a  href="remplir.jsp?punchCardId=@@">
                        <img src="images/edit.gif" border="0">
                    </a>
                </mt:fieldStyle>
            </mt:field>
            <mt:field id="Suppr" name="Suppr." type="String" nullValue="<img src=\"images/deleteg.gif\">">
                <mt:fieldStyle alt="false">
                    <a href="javascript:sendAction('delete', @@)">
                        <img src="images/delete.gif" border="0">
                    </a>
                </mt:fieldStyle>
            </mt:field>
      	</mt:grid>
   
      	<br>
      	
         <table width="100%" border="0" cellspacing="1" cellpadding="1">
            <tr>
               <td colspan="3" class="tdlabel">Filtres</td>
            </tr>
            <tr>
               <td class="tdfield">
                  Distribution
                  <mt:comboBoxTag id="distributionId" getPostValue="true">
                     <%= solstice.custom.AutorisationAbsenceADM.getDistributionSQL(pageContext,new String[] {"P_Distribution_Booklet_ID, Value"},false) %>
                  </mt:comboBoxTag>
               </td>
               <td class="tdfield">
                  Employ&eacute;
                  <mt:comboBoxTag id="employeeId" getPostValue="true">
                     <%= solstice.custom.AutorisationAbsenceADM.getEmployeeSQL(pageContext) %>
                  </mt:comboBoxTag>
               </td>
               <td class="tdfield" rowspan="2">
                  <input type="image" src="images/refresh.gif" onClick="document.mainForm.action.value='recherche'">
               </td>
            </tr>
            <tr>
               <td class="tdfield">
                  Statut
                  <mt:comboBoxTag id="status" getPostValue="true" allowNull="false">
                     select '''ATTE'', ''DSUP''', 'En attente d''approbation' union all
                     select '''INIT'', ''ATTE'', ''DSUP'', ''APPR'', ''SIGN'', ''SIGD'', ''TRSF'', ''REFU'', ''DELE''', 'Toutes les demandes' union all
                     select '''INIT'', ''ATTE'', ''DSUP''', 'Demandes en traitement' union all
                     select '''APPR'', ''SIGN'', ''SIGD'', ''TRSF''', 'Toutes approuvées' union all
                     select '''INIT'', ''ATTE'', ''REFU''', 'Toutes non approuvées' union all
                     select '', '-------------------------' union all
                     select '''' + absenceStatus.Value + '''', absenceStatus.Name
                     from P_Absence_Status absenceStatus
                     where Value <> 'TRSF'
                  </mt:comboBoxTag>
               </td>
               <td class="tdfield">
                  Date de d&eacute;but
                  <input type="checkbox" name="cDatePunchCard" onClick="if(!this.checked) document.mainForm.datePunchCard.value = '';">
                  <mt:simpleCalendar id="datePunchCard" formName="mainForm" getPostValue="true" />
               </td>
            </tr>
         </table>
   	</form>
	
      <script language="javascript">
         setInterval("dateListener()", 1);
      </script>
	</mt:pageTag>
</mt:securityCheck>