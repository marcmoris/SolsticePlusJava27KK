<%@ taglib uri="/WEB-INF/taglib.tld" prefix="mt" %>

<mt:securityCheck id="permisHorodateur_consulter">
	<mt:pageTag id="permisHorodateur_consulter" supportCalendar="true" validationClass="solstice.custom.HorodateurRemplir">
	
	   <script language="javascript">
         function search()
         {
            if(document.mainForm.cDatePunchCard.checked && document.mainForm.datePunchCard.value == "")
            {
               alert("Si vous cochez une date, vous devez obligatoirement en saisir une");
            }
            else
            {
            	document.mainForm.action.value = "search";
            	document.mainForm.submit();
            }
         }
         
         function askApprob(id)
         {
            document.mainForm.action.value = 'demanderApprobationEtEnregistrer';
            document.mainForm.punchCardId.value = id;
            document.mainForm.submit();
         }
         
         function deleteAA(id)
         {
            document.mainForm.action.value = 'effacer';
            document.mainForm.punchCardId.value = id;
            document.mainForm.submit();
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
	    
      <form name="mainForm" id="mainForm" method="post">
      
         <input type="hidden" name="action" id="action" />
         <input type="hidden" name="punchCardId" id="punchCardId" />

   	   <mt:grid>
   	      <mt:queryBuilder checkMethod="solstice.custom.AutorisationAbsence.checkCondition" orderBy="punchCard.DatePunchCard desc">
               select reason.Name + isnull('<br>' + replace(punchCard.Reason_Other, char(13) + char(10), '<br>'), '') as Reason,
                      punchCard.DatePunchCard,
                      punchCard.MorningStart,
                      punchCard.MorningEnd,
                      punchCard.AfternoonStart,
                      punchCard.AfternoonEnd,
                      status.Name as StatusName,
                      case status.Value
                        when 'INIT' then punchCard.P_PunchCard_ID
                        else null
                      end as AskApprob,
                      punchCard.P_PunchCard_ID as Edit,
                      case status.Value
                        when 'INIT' then punchCard.P_PunchCard_ID
                        when 'ATTE' then punchCard.P_PunchCard_ID
                        when 'APPR' then punchCard.P_PunchCard_ID
                        when 'SIGN' then punchCard.P_PunchCard_ID
                        when 'SIGD' then punchCard.P_PunchCard_ID
                        else null
                      end as Del
                 from P_PunchCard punchCard
                 join P_Absence_Status status
                   on status.P_Absence_Status_ID = punchCard.P_Absence_Statut_ID
                 join P_PunchCard_Reason reason
                   on reason.P_PunchCard_Reason_ID = punchCard.P_PunchCard_Reason_ID
                <mt:staticQueryCondition>P_Employee_ID = <%= ((solstice.custom.IUserInfo)session.getAttribute("userInfo")).getEmployeeId() %></mt:staticQueryCondition>
                <mt:queryCondition id="statusValue">status.Value in (<%= request.getParameter("statusValue") %>)</mt:queryCondition>
                <mt:queryCondition id="cDatePunchCard">
                   punchCard.DatePunchCard between convert(datetime, '<%= request.getParameter("datePunchCard") %>T00:00:00.000', 126)
                                               and convert(datetime, '<%= request.getParameter("datePunchCard") %>T23:59:59.998', 126)
                </mt:queryCondition>
             </mt:queryBuilder>
             
             <mt:field id="Reason" name="Motif du permis horodateur" type="String" />
             <mt:field id="DatePunchCard" name="Date" type="Date" format="yyyy-MM-dd" />
             <mt:field id="MorningStart" name="Arrivée" type="String" nullValue="&nbsp;" />
             <mt:field id="MorningEnd" name="Départ/dîner" type="String" nullValue="&nbsp;" />
             <mt:field id="AfternoonStart" name="Retour/dîner" type="String" nullValue="&nbsp;" />
             <mt:field id="AfternoonEnd" name="Départ" type="String" nullValue="&nbsp;" />
             <mt:field id="StatusName" name="Statut" type="String" />
             <mt:field id="AskApprob" name="Pour<br>appro." type="String" nullValue="<img src=\"images/approbaskg.gif\" border=\"0\">">
                 <mt:fieldStyle alt="false">
                     <a href="javascript:askApprob(@@)">
                         <img src="images/approbask.gif" border="0">
                     </a>
                 </mt:fieldStyle>
             </mt:field>
             <mt:field id="Edit" name="&Eacute;dit." type="String">
                 <mt:fieldStyle alt="false">
                     <a href="remplir.jsp?punchCardId=@@">
                         <img src="images/edit.gif" border="0">
                     </a>
                 </mt:fieldStyle>
             </mt:field>
             <mt:field id="Del" name="Suppr." type="String" nullValue="<img src=\"images/deleteg.gif\" border=\"0\">">
                 <mt:fieldStyle alt="false">
                     <a href="javascript:deleteAA(@@)">
                         <img src="images/delete.gif" border="0">
                     </a>
                 </mt:fieldStyle>
             </mt:field>
    	   </mt:grid>
    	   
         <br>
         
         <table width="100%" border="0" cellspacing="1" cellpadding="1">
            <tr>
               <td colspan="4" class="tdlabel">Filtres</td>
            </tr>
            <tr>
               <td class="tdfield" width="60%">
                  Statut
                  <mt:comboBoxTag id="statusValue" getPostValue="true" allowNull="false">
                     select '', 'Toutes les demandes' union all
                     select '''INIT'', ''ATTE'', ''DSUP''', 'Demandes en traitement' union all
                     select '''APPR'', ''SIGN'', ''SIGD'', ''TRSF''', 'Toutes approuvées' union all
                     select '''INIT'', ''ATTE'', ''REFU''', 'Toutes non approuvées' union all
                     select '', '-------------------------' union all
                     select '''' + absenceStatus.Value + '''', absenceStatus.Name
                     from P_Absence_Status absenceStatus
                     where Value <> 'TRSF'
                  </mt:comboBoxTag>
               </td>
               <td class="tdfield" width="40%">
                  Date
                  <input type="checkbox" name="cDatePunchCard" onClick="if(!this.checked) document.mainForm.datePunchCard.value = '';">
                  <mt:simpleCalendar id="datePunchCard" formName="mainForm" getPostValue="true"/>
               </td>
               <td class="tdfield">
                  <a href="javascript:search()">
                     <img src="images/refresh.gif" border="0">
                  </a>
               </td>
            </tr>
         </table>
         
         <p align="right"><input type="button" value="Nouveau" onClick="document.location = 'remplir.jsp';"></p>
    	</form>
	
      <script language="javascript">
         setInterval("dateListener()", 1);
      </script>
	</mt:pageTag>
</mt:securityCheck>