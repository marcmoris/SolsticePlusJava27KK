<%@ taglib uri="/WEB-INF/taglib.tld" prefix="mt" %>
<%@ page import="solstice.custom.IUserInfo" %>
<%! private IUserInfo userInfo; %>
<mt:securityCheck id="livretTemps">
<mt:pageTag id="livretTemps" onLoad="scroll();" validationClass="solstice.custom.LivretTempsPrincipal">

<% this.userInfo = (IUserInfo)session.getAttribute("userInfo"); %>
   <style>
      .note, .note option {
         font-family: 'Courier New', monospace;
      }
   </style>
	<script language="JavaScript">
	
		var currentTimeout;
		var currentId;

    	function openPeriodList()
		{
            window.open('/solstice/listperiod.jsp','listPeriod', 'width=850, height=500, scrollbars=yes, toolbar=yes, resizable=yes');
    	}
	
		// On ajuste la valeur du scroll à chaque interval
		function scrollListener()
		{
			// On vérifie si on utilise ie ou un autre navigateur
			if(document.body.scrollTop != null)
				document.mainForm.scroll.value = document.body.scrollTop;
			else
				document.mainForm.scroll.value = window.pageYOffset;
		}
		
		// On récupère la position de l'ancien post et on part le listener
		function scroll()
		{
			// On vérifie si on utilise ie ou un autre navigateur
			if(document.body.scrollTop != null)
				document.body.scrollTop = <%= request.getParameter("scroll") != null && !request.getParameter("scroll").equals("") ? request.getParameter("scroll") : "0" %>;
			else
				window.pageYOffset = <%= request.getParameter("scroll") != null && !request.getParameter("scroll").equals("") ? request.getParameter("scroll") : "0" %>;
			setInterval("scrollListener()", 1);
		}
		
		function add(actionName)
		{
			document.mainForm.dayName.value = actionName;
			sendAction("add");
		}
		
		function deleteCode(actionName)
		{
			document.mainForm.dayName.value = actionName;
			var detailid = eval("document.mainForm."+actionName+"_selectedValue.value");
			var list = document.mainForm.listCheckForDelete.value;
			if(detailid.length > 0)
			{
			   if(list.indexOf(detailid+"|") >= 0 )
			   {
      			if(confirm("En supprimant ce code, aucun retour en arrière ne sera possible. Désirez-vous continuer?"))
      			{
         			sendAction("deleteCode");
         		}
      			
   		   }
   		   else
   		   {
      		   sendAction("deleteCode"); 
      		}
		   }

		}
		
		<mt:checkVisible checkMethod="solstice.custom.LivretTempsPrincipal.showEmployee">
		function showSoldes()
		{
			window.open("banque.jsp?employeeId=<%= request.getParameter("employeeId") %>&periodId=<%= request.getParameter("periodId") %>", "banklist", "left=20,top=20,width=900,height=600,toolbar=0,resizable=1,scrollbars=1");
		}
		
		function showGains()
		{
			window.open("listgain.jsp", "listgain", "left=20,top=20,width=900,height=600,toolbar=0,resizable=1,scrollbars=1");
		}
		
		
		</mt:checkVisible>
		
		function showAutorisationAbsence(emp,per)
		{
			window.open("../autorisationAbsence/admconsult.jsp?action=recherche&firstload=false&periodId="+per+"&cPeriod=on&employeeId="+emp+"&status='APPR', 'SIGN', 'SIGD', 'TRSF'", "admconsult", "left=20,top=20,width=990,height=600,toolbar=0,resizable=1,scrollbars=1");
			
		}
		
		function chatWindow(tab,rec)
		{
			window.open("chat_window.jsp?tableId="+tab+"&recordId="+rec, "chatWindow", "left=20,top=20,width=990,height=600,toolbar=0,resizable=1,scrollbars=1");
		}
		
		function sendAction(act)
		{
			document.mainForm.action.value = act;
			document.mainForm.submit();
		}
		
		function selectBooklet(value)
		{
        	// On place les valeurs du livert à charger
			document.mainForm.bookletValue.value = value;
			document.mainForm.bookletId.value = value.split("|")[0];
			document.mainForm.employeeId.value = value.split("|")[1];
			document.mainForm.timeSheetStatus.value = value.split("|")[2];
			sendAction("bookletChanged");
		}
		
		function resetBooklet()
		{
   		if(document.mainForm.bookletValue != null)
   		{
   			document.mainForm.bookletValue.value = "";
   			document.mainForm.bookletId.value = "";
   			document.mainForm.employeeId.value = "";
   			document.mainForm.timeSheetStatus.value = "";
   		}
		}
		
		function closeList()
		{
			document.getElementById(currentId).style.visibility = 'hidden';
			currentTimeout = null;
		}
		
		function gotFocus()
		{
			if(currentTimeout)
				clearTimeout(currentTimeout);
		}
		
		function lostFocus()
		{
			currentTimeout = setTimeout("closeList()", 2000);
		}
		
		function openList(id)
		{
			if(currentId && id != currentId)
			{
				if(currentTimeout)
					clearTimeout(currentTimeout);
				closeList();
			}
			currentId = id;
			document.getElementById(id).style.visibility = 'visible';
		}
		
		function addPaymentCorrection(bookletDetailId, bookletDetailCuttingId, periodId, day, gainId, dayQty, cutGainId, OriginTime)
		{
    		document.mainForm.cpaBookletDetailId.value = bookletDetailId;
    		document.mainForm.cpaBookletDetailCuttingId.value = bookletDetailCuttingId;
    		document.mainForm.cpaPeriodId.value = periodId;
    		document.mainForm.cpaDay.value = day;
    		document.mainForm.cpaGainId.value = gainId;
    		document.mainForm.cpaDayQty.value = dayQty;
    		document.mainForm.cpaCutGainId.value = cutGainId;
    		if ( OriginTime == null )
    			document.mainForm.cpaOriginTime.value = "LIV";
    		else
    			document.mainForm.cpaOriginTime.value = OriginTime;
    		sendAction("paymentCorrectionAdded");
		}
		
		function showPaymentCorrection()
		{
            window.open('payment_correction.jsp?speriodId=<%= request.getParameter("periodId") %>&employeeId=<%= request.getParameter("employeeId") %>&distributionId=<%= request.getParameter("distributionId") %>', 
                'payment_correction', 'width=500, height=200, scrollbars=0, toolbar=0');
    	}
    	
    	
    	function editCPA(id)
    	{
   	        window.open('payment_correction.jsp?speriodId=<%= request.getParameter("periodId") %>&employeeId=<%= request.getParameter("employeeId") %>&distributionId=<%= request.getParameter("distributionId") %>&bookletDetailId=' + id, 
        	        'payment_correction', 'width=500, height=200, scrollbars=0, toolbar=0');
    	}
    	
    	function saveBooklet()
    	{
        	// On force un reload de la période de paie sur 14 jours
        	sendAction("save");
    	}
    	
    	
         function promptUser()
         {
            var input = "";
            while(true)
            {
               input = prompt("Entrez une note (maximum 255 caractères)", input);
               if(input.length > 255)
               {
                  if(confirm("La note saisie fait " + input.length + " caractères alors que la limite est de 255 caractères. Voulez-vous qu'elle soit automatiquement tronquée?"))
                     return input.substring(0, 255);
               }
               else
               {
                  return input;
               }
            }
         }


    	
    	function removeCPA(id)
    	{
		if(document.mainForm.timeSheetStatus.value == 'A')
		{alert("Vous ne pouvez supprimer une correction paies antérieures sur un livret approuvé.");}

		//2011.03.02 ne pas permettre à l'utilisateur de supprimé les absences autorisé par un gestionnaire. 
  	    else if ( id == -10 )
			{alert("Vous devez contacter l'administrateur paie pour effectuer l'action demandée.");}

		else
		{
        	document.mainForm.cpaToDelete.value = id;
        	sendAction("removeCPA");
		}
    	}

		/*blurCode(theValue)
		{
		document.mainForm.dayCode.value = theValue;
		}*/
	</script>

	<form name="mainForm" method="post">
		<input type="hidden" name="scroll">
		<input type="hidden" name="action">
		<p class="sectitle">Identification</p>
	
		<table width="100%" border="0" cellspacing="1" cellpadding="1">
			<tr>
				<td colspan="2" class="tdlabel">P&eacute;riode de paie</td>
			</tr>
			<tr>
				<td class="tdfield">
<%
					String speriodId = null;
					speriodId = request.getParameter("periodId");
%>
					<mt:comboBoxTag id="periodId" getPostValue="true" allowNull="false" onChange="resetBooklet(); sendAction('periodChanged');" formatMethod="solstice.custom.LivretTempsPrincipal.formatPeriodComboBox"  selectedValue="<%= speriodId %>" >
						select P_Period_ID, Name, StartDate, EndDate
						from P_Period
						where PeriodStatus in ( 'C', 'O', 'P')
						order by StartDate desc
					</mt:comboBoxTag>
				</td>
				<td width="100%" class="tdfield">&nbsp;&nbsp;&nbsp; <input type="button" value="Liste des périodes" onClick="openPeriodList();" > </td>
			</tr>
		</table><br><br>
		
		<table width="100%" border="0" cellspacing="1" cellpadding="1">
			<tr>
				<td class="tdlabel">Gestionnaire</td>
			</tr>
			<tr>
				<td class="tdfield">
				
				   <mt:dynamicStructure>
				      <mt:customQuery>
                     select Gestionnaire
                     from (
                       select employee.Name as Gestionnaire
                       from P_Distribution_Booklet distributionBooklet inner join P_Employee employee
                       on distributionBooklet.P_Employee_ID = employee.P_Employee_ID
                       where distributionBooklet.P_Distribution_Booklet_ID = <%= request.getParameter("distributionId") != null && !request.getParameter("distributionId").equals("") ? request.getParameter("distributionId") : "0" %>
                     ) GestView
				      </mt:customQuery>
					  <mt:body>
				      <mt:dynamicValue id="Gestionnaire"/>
					  </mt:body>
				   </mt:dynamicStructure>
				
				</td>
			</tr>
		</table>
		<table width="100%" border="0" cellspacing="1" cellpadding="1">
			<tr>
				<td colspan="2" class="tdlabel">Distribution</td>
			</tr>
			<tr>
				<td class="tdfield">
				<%try{%>
					<mt:comboBoxTag id="distributionId" getPostValue="true" allowNull="true" onChange="resetBooklet(); sendAction('distributionChanged');">
						<%= this.userInfo.getDistributionWebSQL(new String[] {"P_Distribution_Booklet_ID", "rtrim(Value) + ' - ' + Name"}, true) %>
					</mt:comboBoxTag>
					<%}catch(Exception e){e.printStackTrace();}%>
				</td>
				<td class="tdfield">&nbsp;</td>
			</tr>
		</table>

		<mt:checkVisible checkMethod="solstice.custom.LivretTempsPrincipal.showLivret">
		<%
		//Création des bool de sécurité
		boolean[] lockDay = new boolean[solstice.custom.LivretTempsPrincipal.NUMDAY];
		lockDay = solstice.custom.LivretTempsPrincipal.getDayLock(pageContext);
		boolean isTimeSheetStatusEnabled = solstice.custom.LivretTempsPrincipal.isTimeSheetStatusEnabled(pageContext);
		boolean isTimeSheetTransferStatusAvailable = solstice.custom.LivretTempsPrincipal.isTimeSheetTransferStatusAvailable(pageContext);
		boolean isTimeSheetApprovedStatusAvailable = solstice.custom.LivretTempsPrincipal.isTimeSheetApprovedStatusAvailable(pageContext);
		boolean isTimeSheetCompletedStatusAvailable = solstice.custom.LivretTempsPrincipal.isTimeSheetCompletedStatusAvailable(pageContext);
		boolean isSaveTimeSheetEnabled = solstice.custom.LivretTempsPrincipal.isSaveTimeSheetEnabled(pageContext);
		boolean isTimeSheetEnabled = solstice.custom.LivretTempsPrincipal.isTimeSheetEnabled(pageContext);
		%>

		<p class="sectitle">Livrets</p>
		
		<input type="hidden" name="bookletId"<%= request.getParameter("bookletId") != null && !request.getParameter("bookletId").equals("") ? " value=\"" + request.getParameter("bookletId") + "\"" : "" %>>
		<input type="hidden" name="employeeId"<%= request.getParameter("employeeId") != null && !request.getParameter("employeeId").equals("") ? " value=\"" + request.getParameter("employeeId") + "\"" : "" %>>
		<input type="hidden" name="timeSheetStatus"<%= request.getParameter("timeSheetStatus") != null && !request.getParameter("timeSheetStatus").equals("") ? " value=\"" + request.getParameter("timeSheetStatus") + "\"" : "" %>>
		<input type="hidden" name="bookletValue"<%= request.getParameter("bookletValue") != null && !request.getParameter("bookletValue").equals("") ? " value=\"" + request.getParameter("bookletValue") + "\"" : "" %>>
		<input type="hidden" name="listCheckForDelete" value="<%=solstice.custom.LivretTempsPrincipal.printDependentsCodeList(request.getParameter("bookletId"))%>">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
					<table width="100%" border="0" cellspacing="1" cellpadding="1">
						<tr>
							<td class="tdlabel" align="center">Livrets en cours</td>
							<td class="tdlabel" align="center">Livrets compl&eacute;t&eacute;s</td>
						</tr>
						<tr>
							<td class="tdfield" align="center">
								<mt:comboBoxTag id="livertsCourrants" getPostValue="false" allowNull="false" size="5" style="width: 250px" onChange="selectBooklet(this.value);" selectedValue="<%= request.getParameter("bookletValue") %>">
                           select convert( varchar(10), booklet.P_Booklet_ID ) + '|' + 
                                  convert( varchar(10), employee.P_Employee_ID ) + '|I' as Clef, 
                                  employee.Name + ' (' + ltrim(rtrim(employee.Value)) + ')' as Display
                             from P_Employee employee
                             join P_Booklet booklet
                             join P_Booklet_Status_Histo statusHisto
                               on statusHisto.P_Booklet_ID = booklet.P_Booklet_ID and statusHisto.P_Distribution_Booklet_ID = <%= request.getParameter("distributionId") %>
                               on booklet.P_Employee_ID = employee.P_Employee_ID
                            where booklet.P_Period_ID = <%= request.getParameter("periodId") %>
                              and statusHisto.P_Distribution_Booklet_ID = <%= request.getParameter("distributionId") %>
                              and statusHisto.TimeSheetStatus = 'I'
                              and statusHisto.P_Booklet_Status_Histo_ID = ( select max( P_Booklet_Status_Histo_ID )
                                                            from P_Booklet_Status_Histo
                                                           where P_Booklet_ID = booklet.P_Booklet_ID
                                                             and P_Distribution_Booklet_ID = <%= request.getParameter("distributionId") %> )
                            order by employee.Name
								</mt:comboBoxTag>
							</td>
							<td class="tdfield" align="center">
								<mt:comboBoxTag id="livertsCompletes" getPostValue="false" allowNull="false" size="5" style="width: 250px" onChange="selectBooklet(this.value);" selectedValue="<%= request.getParameter("bookletValue") %>">
                           select convert( varchar(10), booklet.P_Booklet_ID ) + '|' + 
                                  convert( varchar(10), employee.P_Employee_ID ) + '|' +
                                  statusHisto.TimeSheetStatus as Clef,
                                  case statusHisto.TimeSheetStatus when 'C' then '(&Agrave; approuver) ' + employee.Name + ' (' + ltrim(rtrim(employee.Value)) + ')'
                                   							       when 'T' then '(&Agrave; approuver) ' + employee.Name + ' (' + ltrim(rtrim(employee.Value)) + ')'
                                                                   else          employee.Name + ' (' + ltrim(rtrim(employee.Value)) + ')'
                                  end as Display
                             from P_Employee employee
                             join P_Booklet booklet
                             join P_Booklet_Status_Histo statusHisto
                               on statusHisto.P_Booklet_ID = booklet.P_Booklet_ID and statusHisto.P_Distribution_Booklet_ID = <%= request.getParameter("distributionId") %> 
                               
                               on booklet.P_Employee_ID = employee.P_Employee_ID
                            where booklet.P_Period_ID = <%= request.getParameter("periodId") %>
                              and statusHisto.P_Distribution_Booklet_ID = <%= request.getParameter("distributionId") %>
                              and statusHisto.TimeSheetStatus in ('C', 'A', 'T')
                              and statusHisto.P_Booklet_Status_Histo_ID = ( select max( P_Booklet_Status_Histo_ID )
                                                            from P_Booklet_Status_Histo
                                                           where P_Booklet_ID = booklet.P_Booklet_ID and TimeSheetStatus in ('C', 'A', 'I' )
                                                             and P_Distribution_Booklet_ID = <%= request.getParameter("distributionId") %> )
                            order by employee.Name
 								</mt:comboBoxTag>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table width="100%" border="0" cellpadding="1" cellspacing="1">
						<tr>
							<td class="tdfieldalt" width="30%"><b>&Eacute;tat du livret de temps</b></td>
							<td class="tdfield">
								<select name="timeSheetStatuss" onChange="document.mainForm.timeSheetStatus.value = this.value;" <%=isTimeSheetStatusEnabled ? "" : "disabled"%>>
									<option value="I"<%= request.getParameter("timeSheetStatus") != null && request.getParameter("timeSheetStatus").equals("I") ? " selected" : ""%>>Initial</option>
									<%if (isTimeSheetCompletedStatusAvailable)
									{
									%>
									<option value="C"<%= request.getParameter("timeSheetStatus") != null && request.getParameter("timeSheetStatus").equals("C") ? " selected" : ""%>>Compl&eacute;t&eacute;</option>
									<%
									}
									if (isTimeSheetTransferStatusAvailable)
									{
									%>
									<option value="T"  selected>Transf&eacute;r&eacute;</option>
									<%
									}
									if (isTimeSheetApprovedStatusAvailable && ! isTimeSheetTransferStatusAvailable)
									{
									%>
									<option value="A" selected >Approuv&eacute;</option>
									<%
									}
									%>
								</select>
								<script>
								<%
									String strStateValue = "";
									if(request.getParameter("timeSheetStatus") != null && !request.getParameter("timeSheetStatus").equals(""))
										strStateValue = request.getParameter("timeSheetStatus");
								%>
									document.mainForm.timeSheetStatus.value = "<%=strStateValue%>";
								</script>
							</td>
							<td class="tdfield"><nobr>
							<%if (request.getParameter("bookletId") != null && !request.getParameter("bookletId").equals(""))
							{
							%>
								<mt:dynamicStructure>
									<mt:customQuery>
										select max(Created) as StatusCreated
										from P_Booklet_Status_Histo
										where P_Booklet_ID = <%= request.getParameter("bookletId") %>
										and P_Distribution_Booklet_ID = <%= request.getParameter("distributionId") %>
										and TimeSheetStatus in ('A', 'T')
									</mt:customQuery>
									<mt:dynamicValue id="StatusCreated" formatMethod="solstice.custom.LivretTempsPrincipal.formatDate"/>
								</mt:dynamicStructure>
							<%
							}
							%>
							&nbsp;</nobr>
							</td>
							<td class="tdfield" width="70%"><input type="button" value="Enregistrer" onClick="saveBooklet();" <%= isSaveTimeSheetEnabled ? "" : "disabled" %>>
							
							<input type="button" value="Actualiser" onClick="javascript:selectBooklet('<%=request.getParameter("bookletId")%>|<%=request.getParameter("employeeId")%>|<%=request.getParameter("timeSheetStatus")%>');"
							<%if (request.getParameter("bookletId") == null || request.getParameter("employeeId") == null || request.getParameter("timeSheetStatus")== null){%> disabled<%}%>>
							
							
							</td>
						</tr>
						<tr>
							<td class="tdfieldalt"><b>Transf&eacute;r&eacute; &agrave; la paie</b></td>
							<td class="tdfield" colspan="2">&nbsp;</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<br><br>
		
		<mt:checkVisible checkMethod="solstice.custom.LivretTempsPrincipal.showEmployee">
		<table width="100%" cellspacing="0" cellpadding="5">
			
    <tr> 
      <td style="border-right: solid 1px #000000" valign="top"> <p class="sectitle">Employ&eacute;</p>
        <mt:dynamicStructure> 
        <mt:customQuery>
			SELECT employeeValue,
			       employeeName, 
			       jobTitleValue,
			       jobTitleName,  
			       collectiveLabourValue, 
			       collectiveLabourName, 
			       jobTypeValue, 
			       jobTypeName, 
			       Weekly_Hours,
			       Day_Hours 
			FROM V_BOOKLET_HEADER
			WHERE P_Employee_ID = <%= request.getParameter("employeeId") %>         
			  and P_Booklet_ID = <%= request.getParameter("bookletId") %>
		</mt:customQuery> 
        <table width="100%" border="0" cellspacing="1" cellpadding="1">
          <tr> 
            <td class="tdlabel" colspan="2"><mt:dynamicValue id="employeeName"/> 
              (<mt:dynamicValue id="employeeValue"/>)</td>
          </tr>
          <tr> 
            <td class="tdfield"><b><mt:dynamicValue id="jobTitleValue"/></b></td>
            <td class="tdfield"><mt:dynamicValue id="jobTitleName"/></td>
          </tr>
          <tr> 
            <td class="tdfield"><b><mt:dynamicValue id="collectiveLabourValue"/></b></td>
            <td class="tdfield"><mt:dynamicValue id="collectiveLabourName"/></td>
          </tr>
          <tr> 
            <td class="tdfield"><b><mt:dynamicValue id="jobTypeValue"/></b></td>
            <td class="tdfield"><mt:dynamicValue id="jobTypeName"/></td>
          </tr>
          <tr> 
            <td class="tdfield" colspan="2"><mt:dynamicValue id="Weekly_Hours" formatMethod="solstice.custom.LivretTempsPrincipal.formatHours"/>&nbsp;/&nbsp;<mt:dynamicValue id="Day_Hours" formatMethod="solstice.custom.LivretTempsPrincipal.formatHours"/></td>
          </tr>
        </table>
        </mt:dynamicStructure> </td>
				<td style="border-right: solid 1px #000000" valign="top">
					<p class="sectitle">
					    <mt:dynamicStructure>
					        <mt:customQuery>
					            select EndDate from P_Period where P_Period_ID = <%= request.getParameter("periodId") %>
					        </mt:customQuery>
        					Soldes de Banques au <mt:dynamicValue id="EndDate" formatMethod="solstice.custom.LivretTempsPrincipal.formatDate"/>
					    </mt:dynamicStructure>
					</p>
					<% solstice.custom.LivretTempsPrincipal ltp = new solstice.custom.LivretTempsPrincipal();%>
						<%= ltp.printBankGrid(Integer.parseInt(request.getParameter("employeeId").toString()),
												Integer.parseInt(request.getParameter("periodId").toString()),
												Integer.parseInt(request.getParameter("bookletId").toString())) %>
					<% if(userInfo.hasPolicy("livradmingainbank")) { %>
						<p align="center">
							<input type="button" value="Autres soldes" style="width: 90%" onClick="showSoldes();">
						</p>
					<%}%>
				</td>
				<td valign="top">
					<p class="sectitle">Temps Sommaire paie courante</p>
					<mt:grid>
						<mt:customQuery>
							select gainGroup.Name as Description, sum(bookletDetail.DayQty) as Valeur
							from (P_Booklet booklet inner join P_Booklet_Detail bookletDetail
							on booklet.P_Booklet_ID = bookletDetail.P_Booklet_ID) inner join (P_Gain gain inner join P_Gain_Group gainGroup
							on gain.P_Gain_Group_ID = gainGroup.P_Gain_Group_ID)
							on bookletDetail.P_Gain_ID = gain.P_Gain_ID
						    where booklet.p_booklet_id in (select p_booklet_id from p_booklet where p_booklet.P_Employee_ID = <%= request.getParameter("employeeId") %> and p_booklet.p_period_id = <%= request.getParameter("periodId") %>)
							and gainGroup.IncludeInTotal = 'Y'
							group by gainGroup.Name
							union all
							select '<b>Total</b>' as Description, sum(P_Booklet_Detail.DayQty) as Valeur
							from P_Booklet booklet 
							  inner join P_Booklet_Detail on booklet.P_Booklet_ID = P_Booklet_Detail.P_Booklet_ID
							  inner join P_Gain  on P_Gain.P_Gain_ID = p_booklet_detail.P_Gain_ID
							  inner join P_Gain_Group on P_Gain.P_Gain_Group_ID = P_Gain_Group.P_Gain_Group_ID AND P_Gain_Group.IncludeInTotal = 'Y'
						    where booklet.p_booklet_id in (select p_booklet_id from p_booklet where p_booklet.P_Employee_ID = <%= request.getParameter("employeeId") %> and p_booklet.p_period_id = <%= request.getParameter("periodId") %>)
						union all
							select gainGroup.Name as Description, sum(bookletDetail.DayQty) as Valeur
							from (P_Booklet booklet inner join P_Booklet_Detail bookletDetail
							on booklet.P_Booklet_ID = bookletDetail.P_Booklet_ID) inner join (P_Gain gain inner join P_Gain_Group gainGroup
							on gain.P_Gain_Group_ID = gainGroup.P_Gain_Group_ID)
							on bookletDetail.P_Gain_ID = gain.P_Gain_ID
						    where booklet.p_booklet_id in (select p_booklet_id from p_booklet where p_booklet.P_Employee_ID = <%= request.getParameter("employeeId") %> and p_booklet.p_period_id = <%= request.getParameter("periodId") %>)
							and gainGroup.IncludeInTotal = 'N'
							group by gainGroup.Name
						</mt:customQuery>

						<mt:field id="Description" name="Description" type="String"/>
						<mt:field id="Valeur" name="Valeur" type="Number" format="#0.00"/>
					</mt:grid>
				</td>
			</tr>
		</table>
		<table width="100%">
		 <tr>
		  <td><p class="sectitle">P&eacute;riode de paie sur 14 jours <input type="button" value="Descriptif des codes de gain" onClick="showGains();"></p></td>
		  <td style="text-align:right"><input type="button" value="Autorisation d'absence" onClick="showAutorisationAbsence('<%=request.getParameter("employeeId")%>','<%=request.getParameter("periodId")%>');"></td>
		 </tr>
		</table>
		<input type="hidden" name="dayCode">
		<input type="hidden" name="dayHrs">
		<input type="hidden" name="dayName">

		<% solstice.custom.DayCounter counter = new solstice.custom.DayCounter(Integer.parseInt(request.getParameter("periodId"))); %>
		
		<table border="0" cellspacing="0" cellpadding="0" align="center">
			<tr>
				<td class="tdfield" align="center">
					<%= counter.nextDay() %>
					<input type="hidden" name="Jeudi1_date" value="<%= counter.getFormattedDate("yyyy-MM-dd") %>">
				</td>
				<td class="tdfield" align="center">
					<%= counter.nextDay() %>
					<input type="hidden" name="Vendredi1_date" value="<%= counter.getFormattedDate("yyyy-MM-dd") %>">
				</td>
				<td class="tdfield" align="center">
					<%= counter.nextDay() %>
					<input type="hidden" name="Samedi1_date" value="<%= counter.getFormattedDate("yyyy-MM-dd") %>">
				</td>
				<td class="tdfield" align="center">
					<%= counter.nextDay() %>
					<input type="hidden" name="Dimanche1_date" value="<%= counter.getFormattedDate("yyyy-MM-dd") %>">
				</td>
				<td class="tdfield" align="center">
					<%= counter.nextDay() %>
					<input type="hidden" name="Lundi1_date" value="<%= counter.getFormattedDate("yyyy-MM-dd") %>">
				</td>
				<td class="tdfield" align="center">
					<%= counter.nextDay() %>
					<input type="hidden" name="Mardi1_date" value="<%= counter.getFormattedDate("yyyy-MM-dd") %>">
				</td>
				<td class="tdfield" align="center">
					<%= counter.nextDay() %>
					<input type="hidden" name="Mercredi1_date" value="<%= counter.getFormattedDate("yyyy-MM-dd") %>">
				</td>
			</tr>
			<tr>
				<td class="tdlabel" align="center">Jeudi</td>
				<td class="tdlabel" align="center">Vendredi</td>
				<td class="tdfieldalt" align="center">Samedi</td>
				<td class="tdfieldalt" align="center">Dimanche</td>
				<td class="tdlabel" align="center">Lundi</td>
				<td class="tdlabel" align="center">Mardi</td>
				<td class="tdlabel" align="center">Mercredi</td>
			</tr>
			<tr>
				<td>
					<mt:listMultiColumn id="Jeudi1" autoReload="true"
						cssLabel="tdfield"
						cssField="tdsimple"
						columnsText="<%= new String[] {"Code", "hre"} %>"
						columnsWidth="<%= new int[] {65, 55}%>"
						height="50"
						initializeMethod="solstice.custom.LivretTempsPrincipal.jourInitializer"
						formName="mainForm"/>
				</td>
				<td>
					<mt:listMultiColumn id="Vendredi1" autoReload="true"
						cssLabel="tdsimple"
						cssField="tdsimple"
						columnsText="<%= new String[] {"Code", "hre"} %>"
						columnsWidth="<%= new int[] {65, 55}%>"
						height="50"
						initializeMethod="solstice.custom.LivretTempsPrincipal.jourInitializer"
						formName="mainForm"/>
				</td>
				<td>
					<mt:listMultiColumn id="Samedi1" autoReload="true"
						cssLabel="tdfieldalt"
						cssField="tdsimple"
						columnsText="<%= new String[] {"Code", "hre"} %>"
						columnsWidth="<%= new int[] {65, 55}%>"
						height="50"
						initializeMethod="solstice.custom.LivretTempsPrincipal.jourInitializer"
						formName="mainForm"/>
				</td>
				<td>
					<mt:listMultiColumn id="Dimanche1" autoReload="true"
						cssLabel="tdfieldalt"
						columnsText="<%= new String[] {"Code", "hre"} %>"
						cssField="tdsimple"
						columnsWidth="<%= new int[] {65, 55}%>"
						height="50"
						initializeMethod="solstice.custom.LivretTempsPrincipal.jourInitializer"
						formName="mainForm"/>
				</td>
				<td>
					<mt:listMultiColumn id="Lundi1" autoReload="true"
						cssField="tdsimple"
						cssLabel="tdfield"
						columnsText="<%= new String[] {"Code", "hre"} %>"
						columnsWidth="<%= new int[] {65, 55}%>"
						height="50"
						initializeMethod="solstice.custom.LivretTempsPrincipal.jourInitializer"
						formName="mainForm"/>
				</td>
				<td>
					<mt:listMultiColumn id="Mardi1" autoReload="true"
						cssField="tdsimple"
						cssLabel="tdsimple"
						columnsText="<%= new String[] {"Code", "hre"} %>"
						columnsWidth="<%= new int[] {65, 55}%>"
						height="50"
						initializeMethod="solstice.custom.LivretTempsPrincipal.jourInitializer"
						formName="mainForm"/>
				</td>
				<td>
					<mt:listMultiColumn id="Mercredi1" autoReload="true"
						cssField="tdsimple"
						cssLabel="tdfield"
						columnsText="<%= new String[] {"Code", "hre"} %>"
						columnsWidth="<%= new int[] {65, 55}%>"
						height="50"
						initializeMethod="solstice.custom.LivretTempsPrincipal.jourInitializer"
						formName="mainForm"/>
				</td>
			</tr>
			<tr>
				<% { 
						int count = 0; 
						String[] actions = new String[] {"Jeudi1", "Vendredi1", "Samedi1", "Dimanche1", "Lundi1", "Mardi1", "Mercredi1"};
				%>
				<mt:loop iterations="7">
				<td>
					<table border="0" cellspacing="0" cellpadding="0" width="100%">
						<tr>
							<td align="center"><input type="button" value=" + " onClick="openList('lista<%= count %>'); document.mainForm.cbCodea<%= count %>.focus();" <%= isTimeSheetEnabled && !lockDay[count] ? "" : "disabled" %>></td>
							<td align="center"><input type="button" value=" - " onClick="deleteCode('<%= actions[count] %>');" <%= isTimeSheetEnabled && !lockDay[count] ? "" : "disabled" %>></td>
						</tr>
						<tr>
							<td>
								<div style="position: absolute; visibility: hidden; width: 100%" id="lista<%= count %>">
									<table border="0" cellspacing="1" cellpadding="0">
										<tr>
											<td class="tdlabel">Code</td>
											<td class="tdlabel">hrs centile</td>
											<td class="tdlabel">&nbsp;</td>
										</tr>
										<tr>
											<td class="tdfield">
                                            <% if(userInfo.hasPolicy("livradmingainlist")) { %>
    												<mt:comboBoxTag id="<%= "cbCodea" + count %>" onFocus="gotFocus();" onBlur="lostFocus();document.mainForm.dayCode.value = this.value;" onChange="document.mainForm.dayCode.value = this.value;">
    													select P_Gain_ID, ltrim(rtrim(Value)) + ' - ' + ltrim(rtrim(Name))
    													from P_Gain
    													where IsActive = 'Y'
    													order by Value
    												</mt:comboBoxTag>
    										<%}else{%>
    												<mt:comboBoxTag id="<%= "cbCodea" + count %>" onFocus="gotFocus();" onBlur="lostFocus();document.mainForm.dayCode.value = this.value;" onChange="document.mainForm.dayCode.value = this.value;">
    													select P_Gain_ID, ltrim(rtrim(Value)) + ' - ' + ltrim(rtrim(Name))
    													from P_Gain
    													where IsActive = 'Y'
    													and Accessible = 'Y'
    													order by Value
    												</mt:comboBoxTag>
    										<%}%>
											</td>
											<td class="tdfield"><input type="text" size="5" name="txtHrs<%= count %>" value="" onFocus="gotFocus();" onBlur="lostFocus();" onChange="document.mainForm.dayHrs.value = this.value;"></td>
											<td class="tdfield"><input type="button" value="Confirmer" onFocus="gotFocus();" onBlur="lostFocus();" onClick="add('<%= actions[count] %>');"></td>
										</tr>
									</table>
								</div>
							</td>
						</tr>
					</table>
				</td>
				<% count++; %>
				</mt:loop>
				<% } %>
			</tr>
			<tr>
				<td colspan="7">&nbsp;<br>&nbsp;</td>
			</tr>
			<tr>
				<td class="tdfield" align="center">
					<%= counter.nextDay() %>
					<input type="hidden" name="Jeudi2_date" value="<%= counter.getFormattedDate("yyyy-MM-dd") %>">
				</td>
				<td class="tdfield" align="center">
					<%= counter.nextDay() %>
					<input type="hidden" name="Vendredi2_date" value="<%= counter.getFormattedDate("yyyy-MM-dd") %>">
				</td>
				<td class="tdfield" align="center">
					<%= counter.nextDay() %>
					<input type="hidden" name="Samedi2_date" value="<%= counter.getFormattedDate("yyyy-MM-dd") %>">
				</td>
				<td class="tdfield" align="center">
					<%= counter.nextDay() %>
					<input type="hidden" name="Dimanche2_date" value="<%= counter.getFormattedDate("yyyy-MM-dd") %>">
				</td>
				<td class="tdfield" align="center">
					<%= counter.nextDay() %>
					<input type="hidden" name="Lundi2_date" value="<%= counter.getFormattedDate("yyyy-MM-dd") %>">
				</td>
				<td class="tdfield" align="center">
					<%= counter.nextDay() %>
					<input type="hidden" name="Mardi2_date" value="<%= counter.getFormattedDate("yyyy-MM-dd") %>">
				</td>
				<td class="tdfield" align="center">
					<%= counter.nextDay() %>
					<input type="hidden" name="Mercredi2_date" value="<%= counter.getFormattedDate("yyyy-MM-dd") %>">
				</td>
			</tr>
			<tr>
				<td class="tdlabel" align="center">Jeudi</td>
				<td class="tdlabel" align="center">Vendredi</td>
				<td class="tdfieldalt" align="center">Samedi</td>
				<td class="tdfieldalt" align="center">Dimanche</td>
				<td class="tdlabel" align="center">Lundi</td>
				<td class="tdlabel" align="center">Mardi</td>
				<td class="tdlabel" align="center">Mercredi</td>
			</tr>
			<tr>
				<td>
					<mt:listMultiColumn id="Jeudi2" autoReload="true"
						cssField="tdsimple"
						cssLabel="tdfield"
						columnsText="<%= new String[] {"Code", "hre"} %>"
						columnsWidth="<%= new int[] {65, 55}%>"
						height="50"
						initializeMethod="solstice.custom.LivretTempsPrincipal.jourInitializer"
						formName="mainForm"/>
				</td>
				<td>
					<mt:listMultiColumn id="Vendredi2" autoReload="true"
						cssLabel="tdsimple"
						cssField="tdsimple"
						columnsText="<%= new String[] {"Code", "hre"} %>"
						columnsWidth="<%= new int[] {65, 55}%>"
						height="50"
						initializeMethod="solstice.custom.LivretTempsPrincipal.jourInitializer"
						formName="mainForm"/>
				</td>
				<td>
					<mt:listMultiColumn id="Samedi2" autoReload="true"
						cssLabel="tdfieldalt"
						cssField="tdsimple"
						columnsText="<%= new String[] {"Code", "hre"} %>"
						columnsWidth="<%= new int[] {65, 55}%>"
						height="50"
						initializeMethod="solstice.custom.LivretTempsPrincipal.jourInitializer"
						formName="mainForm"/>
				</td>
				<td>
					<mt:listMultiColumn id="Dimanche2" autoReload="true"
						cssLabel="tdfieldalt"
						cssField="tdsimple"
						columnsText="<%= new String[] {"Code", "hre"} %>"
						columnsWidth="<%= new int[] {65, 55}%>"
						height="50"
						initializeMethod="solstice.custom.LivretTempsPrincipal.jourInitializer"
						formName="mainForm"/>
				</td>
				<td>
					<mt:listMultiColumn id="Lundi2" autoReload="true"
						cssLabel="tdfield"
						cssField="tdsimple"
						columnsText="<%= new String[] {"Code", "hre"} %>"
						columnsWidth="<%= new int[] {65, 55}%>"
						height="50"
						initializeMethod="solstice.custom.LivretTempsPrincipal.jourInitializer"
						formName="mainForm"/>
				</td>
				<td>
					<mt:listMultiColumn id="Mardi2" autoReload="true"
						cssLabel="tdsimple"
						cssField="tdsimple"
						columnsText="<%= new String[] {"Code", "hre"} %>"
						columnsWidth="<%= new int[] {65, 55}%>"
						height="50"
						initializeMethod="solstice.custom.LivretTempsPrincipal.jourInitializer"
						formName="mainForm"/>
				</td>
				<td>
					<mt:listMultiColumn id="Mercredi2" autoReload="true"
						cssLabel="tdfield"
						cssField="tdsimple"
						columnsText="<%= new String[] {"Code", "hre"} %>"
						columnsWidth="<%= new int[] {65, 55}%>"
						height="50"
						initializeMethod="solstice.custom.LivretTempsPrincipal.jourInitializer"
						formName="mainForm"/>
				</td>
			</tr>
			<tr>
				<% { 
						int count = 0; 
						String[] actions = new String[] {"Jeudi2", "Vendredi2", "Samedi2", "Dimanche2", "Lundi2", "Mardi2", "Mercredi2"};
				%>
				<mt:loop iterations="7">
				<td>
					<table border="0" cellspacing="0" width="100%" cellpadding="0">
						<tr>
							<td align="center"><input type="button" width="50%" font = "courrier" value=" + " onClick="openList('listb<%= count %>'); document.mainForm.cbCodeb<%= count %>.focus();" <%= isTimeSheetEnabled && !lockDay[count+7] ? "" : "disabled" %>></td>
							<td align="center"><input type="button" width="50%" font = "courrier" value=" - " onClick="deleteCode('<%= actions[count] %>');" <%= isTimeSheetEnabled && !lockDay[count+7] ? "" : "disabled" %>></td>
						</tr>
						<tr>
							<td>
								<div style="position: absolute; visibility: hidden; width: 100%" id="listb<%= count %>">
									<table border="0" cellspacing="1" cellpadding="0">
										<tr>
											<td class="tdlabel">Code</td>
											<td class="tdlabel">hrs centile</td>
											<td class="tdlabel">&nbsp;</td>
										</tr>
										<tr>
											<td class="tdfield">
											<% if(userInfo.hasPolicy("livradmingainlist")) { %>
    												<mt:comboBoxTag id="<%= "cbCodeb" + count %>" onFocus="gotFocus();" onBlur="lostFocus();document.mainForm.dayCode.value = this.value;" onChange="document.mainForm.dayCode.value = this.value;">
    													select P_Gain_ID, ltrim(rtrim(Value)) + ' - ' + ltrim(rtrim(Name))
    													from P_Gain
    													where IsActive = 'Y'
    													order by Value
    												</mt:comboBoxTag>
											<%}else{%>
    												<mt:comboBoxTag id="<%= "cbCodeb" + count %>" onFocus="gotFocus();" onBlur="lostFocus();document.mainForm.dayCode.value = this.value;" onChange="document.mainForm.dayCode.value = this.value;">
    													select P_Gain_ID, ltrim(rtrim(Value)) + ' - ' + ltrim(rtrim(Name))
    													from P_Gain
    													where IsActive = 'Y'
    													and Accessible = 'Y'
    													order by Value
    												</mt:comboBoxTag>
											<%}%>
											</td>
											<td class="tdfield"><input type="text" size="5" name="dayHrsb<%= count %>" value="" onFocus="gotFocus();" onBlur="lostFocus();" onChange="document.mainForm.dayHrs.value = this.value;"></td>
											<td class="tdfield"><input type="button" value="Confirmer" onFocus="gotFocus();" onBlur="lostFocus();" onClick="add('<%= actions[count] %>');"></td>
										</tr>
									</table>
								</div>
							</td>
						</tr>
					</table>
				</td>
				<% count++; %>
				</mt:loop>
				<% } %>
			</tr>
		</table><br><br>
		<table width="100%" border="0" cellspacing="0" cellpadding="5">
			<tr>
				<td valign="top" style="border-right: solid 1px #000000" width="50%">
					<p class="sectitle">Correction paies ant&eacute;rieures</p>
					<input type="button" value="Ajouter" onClick="showPaymentCorrection();" <%= isTimeSheetEnabled ? "" : "disabled" %>>
					<input type="hidden" name="cpaBookletDetailId" />
					<input type="hidden" name="cpaBookletDetailCuttingId" />
					<input type="hidden" name="cpaPeriodId" />
					<input type="hidden" name="cpaDay" />
					<input type="hidden" name="cpaAssignmentId" />
					<input type="hidden" name="cpaGainId" />
					<input type="hidden" name="cpaDayQty" />
					<input type="hidden" name="cpaCutGainId" />
					<input type="hidden" name="cpaToDelete" />
					<input type="hidden" name="cpaOriginTime" />
					<mt:grid>
						<mt:customQuery>
                            select bookletDetail.P_Booklet_Detail_ID, 
                                   period.Name as period, 
                                   bookletDetail.Day, 
                                   assignment.Value as aff, 
                                   ltrim(rtrim(gain.Value)) + ' - ' + ltrim(rtrim(gain.Name)) as code, 
                                   dayqty,
                                   case when bookletDetail.P_Booklet_Detail_CPA_ID is null then bookletDetail.P_Booklet_Detail_ID
                                   	 else null
                                   end as Edit_ID,
                                   
                                   case when ( isnull( OriginTime, 'LIV' ) not in ('PAR', 'CAL') ) then
                                   		case when bookletDetail.P_Booklet_Detail_CPA_ID is null then bookletDetail.P_Booklet_Detail_ID
                                       		 else null
                                   		end 
                                   else -10
                                   end
                                   as Edit_ID2
                              from P_Period period
                                   inner join P_Assignment assignment
                                              inner join P_Gain gain
                                                         inner join P_Booklet_Detail bookletDetail
                                                                 on gain.P_Gain_ID = bookletDetail.P_Gain_ID
                                                      on assignment.P_Assignment_ID = bookletDetail.P_Assignment_ID
                                   on period.P_Period_ID = bookletDetail.P_Period_ID
                             where bookletDetail.P_Booklet_ID = <%= request.getParameter("bookletId") %>
                               and exists ( select 1
                                              from P_Booklet booklet
                                             where booklet.P_Booklet_ID = bookletDetail.P_Booklet_ID
                                               and booklet.P_Period_ID <> bookletDetail.P_Period_ID )
						</mt:customQuery>
						
						<mt:field id="period" name="P&eacute;riode" type="String"/>
						<mt:field id="day" name="Date" type="Date" format="yyyy-MM-dd"/>
						<mt:field id="aff" name="Aff." type="String"/>
						<mt:field id="code" name="Code" type="String"/>
						<mt:field id="dayqty" name="Hrs" type="Number" format="#0.00"/>
						<% if(isTimeSheetEnabled) { %>
						<% if(userInfo.hasPolicy("editRemoveAbsence")) { %>
						<mt:fieldGroup name="Action">
							<mt:field id="Edit_ID" type="Number" nullValue="&nbsp;">
							    <mt:fieldStyle alt="false">
							        <a href="javascript:editCPA(@@)">
							            <img src="images/edit.gif" border="0">
							        </a>
							    </mt:fieldStyle>
							</mt:field>
							<mt:field id="Edit_ID" type="Number" nullValue="&nbsp;">
							    <mt:fieldStyle alt="false">
							        <a href="javascript:removeCPA(@@)">
							            <img src="images/delete.gif" border="0">
							        </a>
							    </mt:fieldStyle>
							</mt:field>
						</mt:fieldGroup>
						<% } else { %>
							<mt:fieldGroup name="Action">
							<mt:field id="Edit_ID" type="Number" nullValue="&nbsp;">
							    <mt:fieldStyle alt="false">
							        <a href="javascript:editCPA(@@)">
							            <img src="images/edit.gif" border="0">
							        </a>
							    </mt:fieldStyle>
							</mt:field>
							<mt:field id="Edit_ID2" type="Number" nullValue="&nbsp;">
							    <mt:fieldStyle alt="false">
							        <a href="javascript:removeCPA(@@)">
							            <img src="images/delete.gif" border="0">
							        </a>
							    </mt:fieldStyle>
							</mt:field>
						</mt:fieldGroup>
						<% } %>
						<% } %>
					</mt:grid>
				</td>
				<td valign="top" width="50%">
					<p class="sectitle">Notes</p>
					<div align="right">
						<input type="button" value="Gestion des notes" onClick="chatWindow('2100735','<%=request.getParameter("bookletId")%>');" <%--= isTimeSheetEnabled ? "" : "disabled" --%>>
					</div>
					<iframe name="iframechat" id="iframechat" src="chat_window.jsp?tableId=2100735&recordId=<%=request.getParameter("bookletId")%>&compact=true" width="100%" frameborder="0" scrolling="auto"></iframe>
				</td>
			</tr>
		</table>
		
		</mt:checkVisible>
		</mt:checkVisible>
	</form>
	
</mt:pageTag>
</mt:securityCheck>
