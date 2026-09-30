<%@ taglib uri="/WEB-INF/taglib.tld" prefix="mt" %>
<%@ page import="java.sql.*" %>
<%@ page import="org.compiere.util.DB" %>
<%@ page import="solstice.custom.IUserInfo" %>
<%! private IUserInfo userInfo; %>
<% this.userInfo = (IUserInfo)session.getAttribute("userInfo"); %>

<%

	String bookletDetailId = "-1";
	String bookletDetailCuttingId = "-1";
	String speriodId = null;
	String employeeId = null;
	String distributionId = null;
	String periodId = null;
	String gainId = null;
	String day = null;
	String dayQty = "";
	String cutGainId = null;
	String OriginTime = "LIV";

	speriodId = request.getParameter("speriodId");
	employeeId = request.getParameter("employeeId");
	distributionId = request.getParameter("distributionId");
	
	boolean loaded = false;
	
	// Puisque ce formulaire peut servir autant pour créer une nouvelle correction
	// que pour en modifier une, on doit vérifier si on doit charger l'affichage
	// à partir de la base de données lors de la première ouverture
	if(!"false".equals(request.getParameter("firstTime"))
		&& request.getParameter("bookletDetailId") != null
		&& !request.getParameter("bookletDetailId").equals(""))
	{
		// On charge le contenue à partir de la base de données
		PreparedStatement stmt = DB.prepareStatement(
			"select bookletDetail.P_Booklet_Detail_ID," +
			"       bookletDetail.P_Period_ID," +
			"       bookletDetail.P_Gain_ID," +
			"       datediff( day, period.StartDate, bookletDetail.[Day] ) as \"Day\"," +
			"       bookletDetail.DayQty," +
			"       isnull( bookletDetailCutting.P_Booklet_Detail_ID, -1 ) as P_Booklet_Detail_Cutting_ID," +
			"       bookletDetailCutting.P_Gain_ID as P_Gain_Cutting_ID, " +
			"       bookletDetail.OriginTime " +
			"  from P_Booklet_Detail bookletDetail" +
			"       inner join P_Period period" +
			"               on period.P_Period_ID = bookletDetail.P_Period_ID" +
			"       left join P_Booklet_Detail bookletDetailCutting" +
			"              on bookletDetailCutting.P_Booklet_Detail_CPA_ID = bookletDetail.P_Booklet_Detail_ID" +
			" where bookletDetail.P_Booklet_Detail_ID = " + request.getParameter("bookletDetailId"));

		ResultSet rs = stmt.executeQuery();
		
		if(rs.next())
		{
			bookletDetailId = rs.getString("P_Booklet_Detail_ID");
			bookletDetailCuttingId = rs.getString("P_Booklet_Detail_Cutting_ID");
			periodId = rs.getString("P_Period_ID");
			gainId = rs.getString("P_Gain_ID");
			day = rs.getString("Day");
			dayQty = rs.getString("DayQty") == null ? "" : rs.getString("DayQty");
			cutGainId = rs.getString("P_Gain_Cutting_ID");
			OriginTime = rs.getString( "OriginTime" );
			loaded = true;
		}
		
	}
	
	if(!loaded)
	{
		// On charge le contenue à partir du request
		bookletDetailId = request.getParameter("bookletDetailId") == null || request.getParameter("bookletDetailId").equals("") ? "-1" : request.getParameter("bookletDetailId");
		bookletDetailCuttingId = request.getParameter("bookletDetailCuttingId") == null || request.getParameter("bookletDetailCuttingId").equals("") ? "-1" : request.getParameter("bookletDetailCuttingId");
		periodId = request.getParameter("periodId");
		gainId = request.getParameter("gainId");
		day = request.getParameter("day");
		dayQty = request.getParameter("dayQty") == null ? "" : request.getParameter("dayQty");
		cutGainId = request.getParameter("cutGainId");
		OriginTime = request.getParameter("OriginTime");
	}

%>

<html>
    <head>
        <title>Correction paie ant&eacute;rieure</title>
        <link rel="stylesheet" type="text/css" href="styles/siq.css">
        <script language="javascript">
            function doOK()
            {
                if(document.mainForm.periodId != null && document.mainForm.periodId.selectedIndex != null && document.mainForm.periodId.selectedIndex >= 0
                    && document.mainForm.day != null && document.mainForm.day.selectedIndex != null && document.mainForm.day.selectedIndex >= 0
                    && document.mainForm.gainId != null && document.mainForm.gainId.selectedIndex != null && document.mainForm.gainId.selectedIndex >= 0
                    && document.mainForm.dayQty.value != "")
                {
                   if(isNumeric(document.mainForm.dayQty.value))
                   {
                      var cutGain;
                      
                      if(document.mainForm.cutGainId != null && document.mainForm.cutGainId.selectedIndex >= 0)
                        cutGain = document.mainForm.cutGainId.options[document.mainForm.cutGainId.selectedIndex].value;
                      else
                        cutGain = "";
                      
                       opener.addPaymentCorrection(
                           document.mainForm.bookletDetailId.value,
                           document.mainForm.bookletDetailCuttingId.value,
                           document.mainForm.periodId.options[document.mainForm.periodId.selectedIndex].value,
                           document.mainForm.day.options[document.mainForm.day.selectedIndex].value,
                           document.mainForm.gainId.options[document.mainForm.gainId.selectedIndex].value,
                           document.mainForm.dayQty.value,
                           cutGain,
                           document.mainForm.OriginTime.value);
                           
                       self.close();
                    }
                    else
                    {
                       alert("Le nombre d'heures doit être un chiffre valide");
                    }
                }
                else
                {
                    alert("Vous devez remplir tous les champs");
                }
            }
            
            function isNumeric(v)
            {
                if(v.length == 0) return false;
                var validChars = "0123456789.-";
                var isNumber=true;
                var c;

                for (i = 0; i < v.length && isNumber == true; i++) 
                { 
                    c = v.charAt(i); 
                    if (validChars.indexOf(c) == -1) 
                    {
                        isNumber = false;
                    }  
                }
                return isNumber;
            }
        </script>
    </head>
    <body style="margin:0px; padding: 0px">
        <form name="mainForm" method="post">
        	<input type="hidden" name="firstTime" value="false" />
        	<input type="hidden" name="bookletDetailId" value="<%= bookletDetailId %>" />
        	<input type="hidden" name="bookletDetailCuttingId" value="<%= bookletDetailCuttingId %>" />
            <input type="hidden" name="speriodId" value="<%= speriodId %>" />
            <input type="hidden" name="employeeId" value="<%= employeeId %>" />
            <input type="hidden" name="distributionId" value="<%= distributionId %>" />
            <input type="hidden" name="OriginTime" value="<%= OriginTime %>" />
            <table width="100%" border="0" cellspacing="1" cellpadding="1">
                <tr>
                    <td class="tdlabel">P&eacute;riode</td>
                    <td class="tdfield">
                    <% if(speriodId != null && !speriodId.equals("") 
                           && employeeId != null && !employeeId.equals("")
                           && distributionId != null && !distributionId.equals("")) { %>

                    	<% if(userInfo.hasPolicy("editRemoveAbsence") || OriginTime == null || ! ( OriginTime.equals("PAR") || OriginTime.equals("CAL")) ) { %>
                        <mt:comboBoxTag formatMethod="solstice.custom.LivretTempsPrincipal.formatPeriodComboBox" allowNull="true" id="periodId" onChange="this.form.submit();" getPostValue="true" selectedValue="<%= periodId %>">
                           select top 78 P_Period_ID, Name, StartDate, EndDate
                             from P_Period period
                            where StartDate < (select StartDate from P_Period where P_Period_ID = <%= speriodId %>)
                              and P_Frequency_ID = ( Select P_Frequency_ID From P_Payment_Group, P_Employee Where P_Payment_Group.P_Payment_Group_ID = P_Employee.P_Payment_Group_ID and P_Employee.P_Employee_ID = <%= employeeId %> )
                              and exists ( select 1
                                             from P_Employee_Distribution_Booklet
                                            where P_Employee_ID = <%= employeeId %>
                                              -- and P_Distribution_Booklet_ID = <%= distributionId %>
                                              and IsLocked = 'N'
                                              and ( Start_Date between period.StartDate and period.EndDate or
                                                    ( Start_Date <= period.StartDate and ( isnull( End_Date, period.StartDate ) between period.StartDate and period.EndDate ) ) or
                                                    ( Start_Date <= period.StartDate and ( period.StartDate between Start_Date and isnull( End_Date, period.EndDate ) ) ) ) )
                            order by StartDate desc
                        </mt:comboBoxTag>
                     <% } else {%>
                        <mt:comboBoxTag formatMethod="solstice.custom.LivretTempsPrincipal.formatPeriodComboBox" allowNull="true" readOnly="true" id="periodId" onChange="this.form.submit();" getPostValue="true" selectedValue="<%= periodId %>">
                           select top 78 P_Period_ID, Name, StartDate, EndDate
                             from P_Period period
                            where StartDate < (select StartDate from P_Period where P_Period_ID = <%= speriodId %>)
                              and P_Frequency_ID = ( Select P_Frequency_ID From P_Payment_Group, P_Employee Where P_Payment_Group.P_Payment_Group_ID = P_Employee.P_Payment_Group_ID and P_Employee.P_Employee_ID = <%= employeeId %> )
                              and exists ( select 1
                                             from P_Employee_Distribution_Booklet
                                            where P_Employee_ID = <%= employeeId %>
                                              -- and P_Distribution_Booklet_ID = <%= distributionId %>
                                              and IsLocked = 'N'
                                              and ( Start_Date between period.StartDate and period.EndDate or
                                                    ( Start_Date <= period.StartDate and ( isnull( End_Date, period.StartDate ) between period.StartDate and period.EndDate ) ) or
                                                    ( Start_Date <= period.StartDate and ( period.StartDate between Start_Date and isnull( End_Date, period.EndDate ) ) ) ) )
                            order by StartDate desc
                        </mt:comboBoxTag>
                    <% } %>
                        
                    <% } else {%>
                        <select disabled>
                            <option selected>S&eacute;lection de la p&eacute;riode</option>
                        </select>
                    <% } %>
                    </td>
                </tr>
                <tr>
                    <td class="tdlabel">Jour</td>
                    <td class="tdfield">
                    <% if(periodId != null && !periodId.equals("")) { %>
                    
                    <% if(userInfo.hasPolicy("editRemoveAbsence") || OriginTime == null || ! ( OriginTime.equals("PAR") || OriginTime.equals("CAL"))) { %>
                    <select name="day" onChange="this.form.submit();">
                    <% } else {%>
                    <select name="day" disabled>
                    <% } %>
                    
                        <option></option>
                    <%
                        PreparedStatement stmt = DB.prepareStatement(
                              "exec dbo.sp_paymentCorrection " + employeeId + 
                              ", " + periodId + 
                              ", " + distributionId);
                              
                        ResultSet rs = stmt.executeQuery();
                        
                        while(rs.next())
                        {%>
                           <option value="<%= rs.getInt(1) %>"<%= day != null && day.equals(String.valueOf(rs.getInt(1))) ? " selected" : "" %>><%= solstice.custom.DayCounter.FormatDate(rs.getTimestamp(2)) %></option>
                        <%}
                        
                        rs.close();
                        stmt.close();
                    %>
                    </select>
                    <% } else {%>
                        <select disabled>
                            <option selected>S&eacute;lection de la journ&eacute;e</option>
                        </select>
                    <% } %>
                    </td>
                </tr>
                <tr>
                    <td class="tdlabel">Code de r&eacute;mun&eacute;ration</td>
                    <td class="tdfield">
                    	<% if(userInfo.hasPolicy("editRemoveAbsence") || OriginTime == null || ! ( OriginTime.equals("PAR") || OriginTime.equals("CAL"))) { %>
           				<% if(((solstice.custom.IUserInfo)session.getAttribute("userInfo")).hasPolicy("livradmingainlist")) { %>
                            <mt:comboBoxTag id="gainId" onChange="this.form.submit();" getPostValue="true" selectedValue="<%= gainId %>">
                                select P_Gain_ID, ltrim(rtrim(Value)) + ' - ' + ltrim(rtrim(Name))
                                from P_Gain
                                where IsActive = 'Y'
                                order by Value
                            </mt:comboBoxTag>
                        <%}else{%>
                            <mt:comboBoxTag id="gainId" getPostValue="true" selectedValue="<%= gainId %>">
                                select P_Gain_ID, ltrim(rtrim(Value)) + ' - ' + ltrim(rtrim(Name))
                                from P_Gain
                                where IsActive = 'Y'
                                and Accessible = 'Y'
                                order by Value
                            </mt:comboBoxTag>
						<%}%>
                        <%}else{%>
                            <mt:comboBoxTag id="gainId" getPostValue="true" readOnly="true" selectedValue="<%= gainId %>">
                                select P_Gain_ID, ltrim(rtrim(Value)) + ' - ' + ltrim(rtrim(Name))
                                from P_Gain
                                where IsActive = 'Y'
                                and Accessible = 'Y'
                                order by Value
                            </mt:comboBoxTag>
						<%}%>
                    </td>
                </tr>
                <tr>
                    <td class="tdlabel">Nombre d'heures</td>
                   	<% if(userInfo.hasPolicy("editRemoveAbsence") || OriginTime == null || ! ( OriginTime.equals("PAR") || OriginTime.equals("CAL"))) { %>
                    <td class="tdfield"><input type="text" name="dayQty" value="<%= dayQty %>"></td>
                    <%}else{%>
                    <td class="tdfield"><input type="text" readonly=true name="dayQty" value="<%= dayQty %>"></td>
					<%}%>
                </tr>
                <tr>
                    <td class="tdlabel">Code &agrave; couper</td>
                    <td class="tdfield">
                    <% if( periodId != null && !periodId.equals("")
                        && day != null && !day.equals("")
						) { %>
                    	
                    <mt:comboBoxTag id="cutGainId" getPostValue="true" selectedValue="<%= cutGainId %>">
						select gain.P_Gain_ID, rtrim(gain.Name) + ' (' + rtrim(gain.Value) + ')'
						from (P_Time_Sheet timeSheet inner join P_Time_Sheet_Detail timeSheetDetail
						on timeSheet.P_Time_Sheet_ID = timeSheetDetail.P_Time_Sheet_ID) inner join P_Gain gain
						on timeSheetDetail.P_Gain_ID = gain.P_Gain_ID
						where timeSheet.P_Employee_ID = <%= employeeId %>
						and timeSheetDetail.[day] = '<%= solstice.custom.LivretTempsPrincipal.getDay(periodId, day) %>'
						order by gain.Name
                    </mt:comboBoxTag>
                    <% } else {%>
                        <select disabled>
                            <option selected>S&eacute;lection de la journ&eacute;e</option>
                        </select>
                    <% } %>
                    </td>
                </tr>
                <tr>
                    <td class="tdfield" colspan="2" align="center">
                        <input type="button" value="Enregistrer" onClick="doOK();" />
                    </td>
                </tr>
            </table>
        </form>
    </body>
</html>