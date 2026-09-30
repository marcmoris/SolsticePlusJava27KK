<%@ taglib uri="/WEB-INF/taglib.tld" prefix="mt" %>

<mt:securityCheck id="livrapproblist">
	<mt:pageTag id="livrapproblist" validationClass="solstice.custom.LivretTempsApprobationList">
	
	    <script language="javascript">
	    
	        function sendAction(action)
	        {
    	        document.mainForm.action.value = action;
    	        document.mainForm.submit();
	        }
	        
			function checkChanged(id, checked)
			{
				var op = checked ? "+" : "-";
				document.mainForm.checkList.value = document.mainForm.checkList.value + op + id;
			}
			
			function saveapprobation()
			{
    	        document.mainForm.action.value = "";
    	        document.mainForm.action_save.value = "save";
    	        document.mainForm.submit();
			}	

	    </script>
	
	    <form name="mainForm" method="post">
	
			<input type="hidden" name="checkList" value="">
	        <input type="hidden" name="action" value="idle">
	        <input type="hidden" name="action_save">

	        <table width="100%" border="0" cellspacing="1" cellpadding="1">
	            <tr>
	                <td colspan="3" class="tdlabel">Filtres</td>
	            </tr>
	            <tr>
	                <td class="tdfield" width="50%">
	                    P&eacute;riode de paie
	                    <mt:comboBoxTag id="periodId" getPostValue="true">
	                        select period.P_Period_ID, period.Name + ' du ' + dbo.formatDate2(period.StartDate) + ' au ' + dbo.formatDate2(period.EndDate)
	                        from P_Period period
							where PeriodStatus in ( 'C', 'O', 'P')
	                        order by period.StartDate desc
	                    </mt:comboBoxTag>
	                </td>
	                <td class="tdfield" width="50%">
	                    Code de distribution
	                    <mt:comboBoxTag id="distributionBookletId" getPostValue="true">
<%= ((solstice.custom.IUserInfo)session.getAttribute("userInfo")).getDistributionWebSQLApp(new String[] {"P_Distribution_Booklet_ID", "rtrim(Value) + ' - ' + Name"}, true) %>
	                    </mt:comboBoxTag>
	                </td>
	                <td class="tdfield">
	                    <a href="javascript:sendAction('search')">
	                        <img src="images/refresh.gif" border="0">
	                    </a>
	                </td>
	            </tr>
	        </table>
	        
	        <mt:grid>
	            <mt:queryBuilder orderBy="period.StartDate, distributionBooklet.Value, employee.Name" checkMethod="solstice.custom.AutorisationAbsence.checkConditionStartup">
                    select period.Name + '<br>' + dbo.formatDate2(period.StartDate) + ' au ' + dbo.formatDate2(period.EndDate) as period, 
                    distributionBooklet.Value as distributionBooklet, 
                    '<a href="livret.jsp?employeeId=' + cast(employee.p_employee_id  as varchar)  + '&periodId=' + cast(period.P_Period_ID as varchar)  + '&distributionId=' + cast(distributionBooklet.P_Distribution_Booklet_ID as varchar)  + '&bookletId=' + cast( booklet.P_Booklet_ID as varchar) + '&timeSheetStatus=' + booklet.timeSheetStatus + '&bookletValue=' + cast( booklet.P_Booklet_ID as varchar) + '|' + cast(employee.p_employee_id  as varchar) + '|' + statusHisto.TimeSheetStatus + '&action=bookletChanged" class="path" target="_blank">' + rtrim(employee.Name) + ' (' + employee.Value + ') </a>'  as employee,
                    dbo.P_GenerateTimeSummariesTable(employee.P_Employee_ID, period.P_Period_ID) as Summaries, (
                      case [dbo].[FN_Booklet_Status]( booklet.P_Booklet_ID, distributionBooklet.P_Distribution_Booklet_ID)
                        when 'A' then ' checked disabled'
<%
	if(((solstice.custom.IUserInfo)session.getAttribute("userInfo")).hasPolicy("livrapprobation"))
	{
%>
                        when 'C' then ' onClick="checkChanged(''' + cast(booklet.P_Booklet_ID as nvarchar(10)) + '|' + distributionBooklet.Value + ''', this.checked);"'
                        when 'T' then ' onClick="checkChanged(''' + cast(booklet.P_Booklet_ID as nvarchar(10)) + '|' + distributionBooklet.Value + ''', this.checked);"'
<%
	}
	else
	{
%>
                        when 'C' then ' disabled'
                        when 'T' then ' disabled'
<%
	}
%>

                        else ' disabled'
                      end) as Status, 
                      ( SELECT MAX(CREATED) FROM P_Booklet_Status_Histo WHERE P_Booklet_Status_Histo.P_Booklet_ID = Booklet.P_Booklet_ID AND P_Booklet_Status_Histo.TimeSheetStatus = 'A' and P_Booklet_Status_Histo.P_Distribution_Booklet_ID = distributionBooklet.P_Distribution_Booklet_ID) as ApprobDate,
                      ( SELECT TOP 1 NAME FROM AD_USER, P_Booklet_Status_Histo WHERE AD_USER.AD_USER_ID = P_Booklet_Status_Histo.AD_USER_ID AND P_Booklet_Status_Histo.P_Booklet_ID = Booklet.P_Booklet_ID AND P_Booklet_Status_Histo.TimeSheetStatus = 'A' and P_Booklet_Status_Histo.P_Distribution_Booklet_ID = distributionBooklet.P_Distribution_Booklet_ID order by P_Booklet_Status_Histo.CREATED desc ) as AppBy
                    from P_Employee employee
                    join P_Booklet booklet on booklet.P_Employee_ID = employee.P_Employee_ID
                    join P_Booklet_Status_Histo statusHisto on statusHisto.P_Booklet_ID = booklet.P_Booklet_ID and statusHisto.TimeSheetStatus not in ('I')
                    join P_Distribution_Booklet distributionBooklet
                      on distributionBooklet.P_Distribution_Booklet_ID = statusHisto.P_Distribution_Booklet_ID
                    join P_Period period 
                      on period.P_Period_ID = booklet.P_Period_ID
                    <mt:staticQueryCondition>statusHisto.P_Booklet_Status_Histo_ID = ( select max(P_Booklet_Status_Histo_ID)
                                                                       from P_Booklet_Status_Histo
                                                                      where P_Booklet_ID = booklet.P_Booklet_ID
                                                                        and P_Distribution_Booklet_ID = distributionBooklet.P_Distribution_Booklet_ID )</mt:staticQueryCondition>
                    <mt:queryCondition id="startup">statusHisto.TimeSheetStatus in ('C', 'T') AND NOT EXISTS( SELECT 1 FROM P_Booklet_Status_Histo WHERE P_Booklet_Status_Histo.P_Booklet_ID = Booklet.P_Booklet_ID AND P_Booklet_Status_Histo.TimeSheetStatus = 'A' and P_Booklet_Status_Histo.P_Distribution_Booklet_ID = distributionBooklet.P_Distribution_Booklet_ID ) </mt:queryCondition>
                    
                    <mt:queryCondition id="periodId">period.P_Period_ID = <%= request.getParameter("periodId") %></mt:queryCondition>
		    		<mt:queryCondition id="distributionBookletId">distributionBooklet.P_Distribution_Booklet_ID = <%= request.getParameter("distributionBookletId") %></mt:queryCondition>
		    		<mt:staticQueryCondition>distributionBooklet.P_Distribution_Booklet_ID in ( <%= ((solstice.custom.IUserInfo)session.getAttribute("userInfo")).getDistributionWebSQLApp(new String[] {"P_Distribution_Booklet_ID"}, false) %> ) </mt:staticQueryCondition>
	            </mt:queryBuilder>
	            
	            <mt:field id="period" name="P&eacute;riode de paie" type="String"/>
	            <mt:field id="distributionBooklet" name="Distribution" type="String"/>
	            <mt:field id="employee" name="Employ&eacute;" type="String"/>
	            <mt:field id="Summaries" name="Temps sommaire" type="String"/>
	            <mt:fieldGroup name="Approbation par<br>le gestionnaire">
	                <mt:field id="Status" type="String">
	                    <mt:fieldStyle alt="false">
                            <input type="checkbox" @@>
	                    </mt:fieldStyle>
	                </mt:field>
                <mt:field id="ApprobDate" type="Date" format="yyyy-MM-dd"/>
                <mt:field id="AppBy" type="String" />
	            </mt:fieldGroup>
	        </mt:grid>
	        
	        <p align="center"><input type="button" value="CONFIRMER APPROBATION" onClick="saveapprobation();"></p>
	        
	    
	    </form>
	
	</mt:pageTag>
</mt:securityCheck>
