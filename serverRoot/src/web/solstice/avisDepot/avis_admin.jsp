<%@page import="java.io.IOException"%>
<%@page import="org.compiere.util.DB"%>
<%@page import="org.compiere.db.CConnection"%>
<%@page import="java.sql.*"%>


<%
   solstice.custom.IUserInfo userInfo = (solstice.custom.IUserInfo)session.getAttribute("userInfo");

%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="mt" %>

<%
	response.setHeader("Cache-Control","no-cache");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader ("Expires", -1);

	String strTimeOut = "Select Value, Parameter from P_Web_Parameters where Parameter = 'AVISDEPOT_TIMEOUT'" + 
		" OR Parameter = 'AVISDEPOT_REDIRECTION'";

	PreparedStatement stmt = DB.prepareStatement(strTimeOut);
	ResultSet rs = stmt.executeQuery();
	String sRedirect = "";
	String sTimeout = "";
	while(rs.next())
	{
	 	if(rs.getString("Parameter").equals("AVISDEPOT_REDIRECTION"))
			sRedirect = rs.getString("value");
	 	else if(rs.getString("Parameter").equals("AVISDEPOT_TIMEOUT"))
			sTimeout = rs.getString("value");
	}
	rs.close();
	stmt.close();

    String periodId = request.getParameter("periodId");
    String Statement_Earning_ID = request.getParameter("Statement_Earning_ID");
    String employeeId = request.getParameter("employeeId");

	if ( request.getParameter("periodId") == null || periodId == null || periodId.length() == 0  )
	{ 
        System.out.println( "Avis Period : is null" );
		String sql = "Select max(P_Period_ID ) P_Period_ID FROM P_Period WHERE PeriodStatus =  'C'";
        System.out.println( "Avis sql : " + sql);

		stmt = DB.prepareStatement(sql);
		rs = stmt.executeQuery();
		if(rs.next())
		{
			periodId = rs.getString("P_Period_ID");
		}
		rs.close();
		stmt.close();
   }
   else
   {
		periodId = request.getParameter("periodId");
        System.out.println( "Avis Period : " +periodId  );

   }

	   if ( request.getParameter("Statement_Earning_ID") == null || Statement_Earning_ID == null || Statement_Earning_ID.length() == 0  )
	   { 
			String sql = "Select min(P_Statement_Earning_ID ) P_Statement_Earning_ID FROM P_Statement_Earning WHERE P_Period_ID = " +periodId + " and P_Employee_ID = " + request.getParameter("employeeId") ;
		        System.out.println( "Avis sql : " + sql);

			stmt = DB.prepareStatement(sql);
			rs = stmt.executeQuery();
			if(rs.next())
			{
				Statement_Earning_ID = rs.getString("P_Statement_Earning_ID");
			}
			rs.close();
			stmt.close();

	   }

   pageContext.setAttribute("periodId", periodId);
   pageContext.setAttribute("Statement_Earning_ID", Statement_Earning_ID);
   
   pageContext.setAttribute("employeeId", employeeId);

   
   System.out.println( "Avis Period context : " + (String)pageContext.getAttribute("periodId")   );
   System.out.println( "Avis Period context : " + (String)pageContext.getAttribute("Statement_Earning_ID")   );


%>
<mt:securityCheck id="avisDepot_avis">
<mt:pageTag id="avisDepot_avis" >

<html>
	<head>
		<title>Avis de d&eacute;p&ocirc;t bancaire</title>
		<link rel="stylesheet" type="text/css" href="styles/siq.css">
		<style type="text/css">
			.quelconque {font-family:Courier New;text-align: top;}
			.petitTexte { font-family:Courier New;text-align:center; FONT-SIZE: 8pt; background-color:#EBEEF5 }
			.petitTexteNonCentre { font-family:Courier New;FONT-SIZE: 8pt;  }
			.texteDate { font-family:Courier New;BACKGROUND-POSITION: right bottom; TEXT-ALIGN: center; FONT-SIZE: 8pt; }
			.bordureDate {font-family:Courier New;BACKGROUND-IMAGE: url(images/delimiteurDate.gif); BACKGROUND-REPEAT: no-repeat;}
			.enteteTableau { font-family:Arial;FONT-WEIGHT: bold; COLOR: white; BACKGROUND-COLOR:#253049; TEXT-ALIGN: center }
			.carreNoir { font-family:Courier New;BORDER-RIGHT: black 1px solid; BORDER-TOP: black 1px solid; BORDER-LEFT: black 1px solid; BORDER-BOTTOM: black 1px solid }
			.bordureDessus { font-family:Courier New;BORDER-TOP: black 1px solid }
			.bordureDessous { font-family:Courier New;BORDER-BOTTOM: black 1px solid }
			.bordureGauche { font-family:Courier New;BORDER-LEFT: black 1px solid }
			.bordureDroite { font-family:Courier New;FONT-SIZE: 8pt;BORDER-RIGHT: black 1px solid }
			.full {  font-family:Courier New;WIDTH: 100%; HEIGHT: 100% }
			.cellulesCentrales {  font-family:Courier New;FONT-SIZE: 8pt }
			.montant {  font-family:Courier New;Text-align: Right }
			.sousTableaux {  font-family:Courier New;FONT-SIZE: 8pt; }
			.celluleMontantPetites {  font-family:Courier New;Text-align: Right;}
			.infoemp{font-family:Courier New;FONT-SIZE: 8pt;}

			@media print
			{
				.printHidden{display: none}
			}
		</style>
	    <script language="javascript">
            	function changer()
		{
<%
	//response.setHeader("Cache-Control","no-cache");
	//response.setHeader("Pragma","no-cache");
	//response.setDateHeader ("Expires", -1);


	//HttpSession httpSession = request.getSession();
	//httpSession.removeAttribute("userInfo");
	//httpSession.invalidate();
%>
			window.location = "./post.jsp?redirect=<%=sRedirect%>";
		}
	    </script>
	</head>
	<body>
	<form name="mainForm" method="post">

      <TABLE id="TableH" class="printHidden" cellSpacing="0" cellPadding="0" width="750" align="center" border="0">
               <tr>
               <tr>
                  <td align="center" colspan="2"><span class="apptitle">Avis de dépôt bancaire</span>
                  </td>
               </tr>
               </tr>
			<tr>
                  <td align="center" > 
		            <span class="pathtext">Employé : </span>
                  </td>
				<td class="tdfield">
			   <%
			      // On doit vérifier si l'utilisateur a accès à tous les employés
			      if(userInfo.hasPolicy("avis_allemployee"))
			      {
			         %>
							<mt:comboBoxTag id="employeeId" allowNull="false" onChange="this.form.submit();" getPostValue="true" selectedValue="<% employeeId %> ">
								select P_Employee_ID, Name + ' (' + ltrim(rtrim(Value)) + ')'
								from P_Employee
								where IsActive = 'Y'
								order by Name
							</mt:comboBoxTag>
			         <%
		         }
			         %>
				</td>
			
                  <td align="center" > 
		            <span class="pathtext">Pour la période de paie : </span>
                  </td>
				<td class="tdfield">
			
							<mt:comboBoxTag id="periodId" formatMethod="solstice.custom.AvisDepotStatic.formatPeriodComboBox" allowNull="false" onChange="this.form.submit();" getPostValue="true" selectedValue="<% periodId %>">
								select P_Period_ID, Name, StartDate, EndDate
								from P_Period
								where StartDate <= (select top 1 StartDate from P_Period where PeriodStatus = 'O' )
								and name >= '2005-14'
								order by P_Frequency_ID, StartDate desc
							</mt:comboBoxTag>
				</td>
			</tr>
			<mt:if condition="solstice.custom.AvisDepotStatic.existsMultipleAvisAdmin">
            <mt:ifTrue>
			<tr>
                  <td align="center" colspan="2"> 
		            <span class="pathtext">Choisir l'avis de dépôt : </span>
							<mt:comboBoxTag id="Statement_Earning_ID" formatMethod="solstice.custom.AvisDepotStatic.formatComboBox" allowNull="false" onChange="this.form.submit();" getPostValue="true" selectedValue="<% Statement_Earning_ID %>" >
								select P_Statement_Earning_ID, PrePrintedNo as name
								from P_Statement_Earning
								where P_Employee_ID = <%= request.getParameter("employeeId") %>
								and P_Period_ID = <%= (String)pageContext.getAttribute("periodId") %>
								and PrePrintedNo like 'D%' 
								order by PrePrintedNo
							</mt:comboBoxTag>
				</td>
			</tr>
			</mt:ifTrue>
			</mt:if>
				
               
               </TABLE>
               <TABLE id="TableSection1" cellSpacing="0" cellPadding="0" width="750" align="center" border="0">
               
               <tr>
                  <td><!--<IMG height="30" alt="" src="images/spacer.gif" width="1" border="0">--><br>
                  </td>
               </tr>
               <tr class="printHidden">
                  <td width="790" class="line" height="4"></td>
               </tr>
               <tr class="printHidden">
                  <td><a class="path" href="../index.jsp">Menu de sélection</a>
                  <span class="pathtext">&middot;&gt; Avis de dépôt bancaire</span>
                  <IMG height="30" alt="" src="images/spacer.gif" width="1" border="0"><br>
                  </td>
               </tr>
              
               <tr>
                  <td align="left">
                  
                  <mt:if condition="solstice.custom.AvisDepotStatic.existsAvisAdmin">
                     <mt:ifTrue>

                     <table boder="0" width="87%"><tr>
<mt:if condition="solstice.custom.AvisDepotStatic.isSoquij">
<mt:ifTrue>
              <td colspan="2"><IMG alt="text" src="images/logo_SOQUIJ.jpg" ></td>
</mt:ifTrue>
</mt:if>
<mt:if condition="solstice.custom.AvisDepotStatic.isSiq">
<mt:ifTrue>
		<td><IMG height="100" alt="text" src="images/SQIipr.jpg" width="211" align="left"></td>
</mt:ifTrue>
</mt:if>
<mt:if condition="solstice.custom.AvisDepotStatic.isChl">
<mt:ifTrue>
		<td><IMG height="100" alt="text" src="images/logo_HNZ.jpg" width="211" align="left"></td>
</mt:ifTrue>
</mt:if>

		<td align="right">
                     <span class="pagetitle">Avis de dépôt bancaire</span>
                     </td></tr></table>
                  
						<mt:dynamicStructure id="header">
							<mt:customQuery>
								select top 1 Earning.P_Statement_Earning_ID, Employee.Value as EmpValue, Employee.Name as EmpName,  Distribution.Value as DistName, Period.Name as PerName,
								Earning.PrePrintedNo, year(Earning.PayFinishing) as PayYear, month(Earning.PayFinishing) as PayMonth, day(Earning.PayFinishing) as PayDay,
								year(Earning.PayStarting) as PayStartingYear, month(Earning.PayStarting) as PayStartingMonth, day(Earning.PayStarting) as PayStartingDay,
								year(Period.PayDate) as EmiYear, month(Period.PayDate) as EmiMonth, day(Period.PayDate) as EmiDay,
								JobTitle.Name as TE, Activity.Name as CR, SC.Value as Cl, SS.Step as Ech,
								Earning.Hourly_Rate, Earning.AnnualSalary,  Earning.Remuneration_Method, isnull( Earning.FederalTaxationAdd, 0) FederalTaxationAdd, isnull(Earning.ProvincialTaxationAdd , 0) ProvincialTaxationAdd,  Earning.FederalTaxationCredit, Earning.ProvincialTaxationCredit, Earning.FederalTaxationReduction,
								Earning.ProvincialTaxationReduction, Earning.PaidHourPer, Earning.PaidHourCum, Earning.NotPaidHourPer, Earning.NotPaidHourCum,
								Earning.GrossEarningsPeriod, Earning.TaxableBenefitsPeriod, Earning.DeductionPeriod, Earning.GrossEarningsCum,
								Earning.TaxableBenefitsCum, Earning.DeductionCum, Earning.GrossEarningsPeriod - Earning.DeductionPeriod as PeriodNetAmount, 
								Earning.GrossEarningsCum - Earning.DeductionCum as CumulativeNetAmount
								from P_Employee Employee inner join (P_Distribution Distribution inner join (P_Period Period 
								inner join ((((P_Statement_Earning Earning
								inner join P_Payment on P_Payment.P_Payment_ID = Earning.P_Payment_ID and PaymentType = 'D' 
								Left Outer join P_Job_Title JobTitle
								on Earning.P_Job_Title_ID = JobTitle.P_Job_Title_ID) left join C_Activity Activity
								on Activity.C_Activity_ID = Earning.C_Activity_ID) left join P_Salary_Class SC
								on Earning.P_Salary_Class_ID = SC.P_Salary_Class_ID) left join P_Salary_Scale_Detail SS
								on Earning.P_Salary_Scale_Detail_ID = SS.P_Salary_Scale_Detail_ID)
								on Period.P_Period_ID = Earning.P_Period_ID)
								on Distribution.P_Distribution_ID = Earning.P_Distribution_ID)
								on Employee.P_Employee_ID = Earning.P_Employee_ID
								where Employee.P_Employee_ID = <%= (String)pageContext.getAttribute("employeeId") %>
								and Period.P_Period_ID = <%= (String)pageContext.getAttribute("periodId") %>
								and P_Statement_Earning_ID = <%= (String)pageContext.getAttribute("Statement_Earning_ID") %> 
                  </mt:customQuery>
							<mt:body>
                  <!-- Tableau principal -->
                  <table border="0" cellSpacing="0" cellPadding="0" width="87%">
                     <tr>
                        <td class="bordureDessus bordureGauche BordureDessous" style="background-color:#253049;" rowSpan="7" align="center"><img src="images/Sommaire.gif"></td>
                        <td class="bordureDessus bordureDessous" style="VERTICAL-ALIGN: top" colSpan="3" rowSpan="1" height="100%">
                           <table border="0" cellSpacing="0" cellPadding="0" width="100%" height="100%">
                              <tr>
                                 <td>
                                    <!-- Tableau date -->
                                    <table height="100%" cellSpacing="0" cellPadding="0" border="0" width="100%">
                                       <tr>
                                          <td class="petitTexte bordureDroite bordureDessous" style="text-align: center; ">A</td>
                                          <td class="petitTexte bordureDroite bordureDessous" align="center">M</td>
                                          <td class="petitTexte bordureDessous" align="center">J</td>
                                       </tr>
                                       <tr>
                                          <td class="petitTexte" colSpan="3">Paie&nbsp;débutant&nbsp;le</td>
                                       </tr>
                                       <tr>
                                          <td class="texteDate bordureDate bordureDessous"><mt:dynamicValue nullValue="&nbsp;" id="PayStartingYear" formatMethod="solstice.custom.AvisDepotStatic.formatDate2"/></td>
                                          <td class="texteDate bordureDate bordureDessous"><mt:dynamicValue nullValue="&nbsp;" id="PayStartingMonth" formatMethod="solstice.custom.AvisDepotStatic.formatDate2"/></td>
                                          <td class="texteDate bordureDessous"><mt:dynamicValue nullValue="&nbsp;" id="PayStartingDay" formatMethod="solstice.custom.AvisDepotStatic.formatDate2"/></td>
                                       </tr>
                                       <tr>
                                          <td class="petitTexte" colSpan="3">Paie&nbsp;finissant&nbsp;le</td>
                                       </tr>
                                       <tr>
                                          <td class="texteDate bordureDate bordureDessous"><mt:dynamicValue nullValue="&nbsp;" id="PayYear" formatMethod="solstice.custom.AvisDepotStatic.formatDate2"/></td>
                                          <td class="texteDate bordureDate bordureDessous"><mt:dynamicValue nullValue="&nbsp;" id="PayMonth" formatMethod="solstice.custom.AvisDepotStatic.formatDate2"/></td>
                                          <td class="texteDate bordureDessous"><mt:dynamicValue nullValue="&nbsp;" id="PayDay" formatMethod="solstice.custom.AvisDepotStatic.formatDate2"/></td>
                                       </tr>
                                       <tr>
                                          <td class="petitTexte" colSpan="3">Date d'émission</td>
                                       </tr>
                                       <tr>
                                          <td class="texteDate bordureDate"><mt:dynamicValue nullValue="&nbsp;" id="EmiYear" formatMethod="solstice.custom.AvisDepotStatic.formatDate2"/></td>
                                          <td class="texteDate bordureDate"><mt:dynamicValue nullValue="&nbsp;" id="EmiMonth" formatMethod="solstice.custom.AvisDepotStatic.formatDate2"/></td>
                                          <td class="texteDate"><mt:dynamicValue nullValue="&nbsp;" id="EmiDay" formatMethod="solstice.custom.AvisDepotStatic.formatDate2"/></td>
                                       </tr>
                                    </table>
                                    <!-- Fin tableau date -->
                                    <div></div>
                                 </td>
                                 <td style="background-color:#253049;" align="center" width="10"><img src="images/Employe4.gif"><!-- Image employé --></td>
                                 <td style="vertical-align:top;" >
                        <mt:if condition="solstice.custom.AvisDepotStatic.isSoquij">
                        <mt:ifFalse>
                                    <!-- Tableau T.E C.R -->
                                    <table style="WIDTH: 100%; HEIGHT: 100%" border="0" cellSpacing="0" cellPadding="0">
                                       <tr>
                                          <td class="petitTexte" style="VERTICAL-ALIGN: left;" width="15px">T.E.&nbsp;&nbsp;</td>
                                          <td id="celluleTE" class="petitTexteNonCentre" style="VERTICAL-ALIGN: center;" ><mt:dynamicValue nullValue="&nbsp;" id="TE"/></td>
                                       </tr>
                                       <tr>
                                          <td class="petitTexte" style="VERTICAL-ALIGN: left;" width="15px">C.R.&nbsp;&nbsp;</td>
                                          <td id="celluleCR" class="petitTexteNonCentre" style="VERTICAL-ALIGN: center;" ><mt:dynamicValue nullValue="&nbsp;" id="CR"/></td>
                                       </tr>
                                    </table>
			</mt:ifFalse>
			  <mt:ifTrue>
                                    <!-- Tableau T.E C.R -->
                                    <table style="WIDTH: 100%; HEIGHT: 100%" border="0" cellSpacing="0" cellPadding="0">
                              <tr>
                                 <td class="petitTexte" style="FONT-WEIGHT: bold; VERTICAL-ALIGN: top; TEXT-ALIGN: center">&nbsp;</td>
                              </tr>

                                       <tr>
                                          <td id="celluleEM" class="petitTexteNonCentre" style="VERTICAL-ALIGN: center;" ><mt:dynamicValue nullValue="&nbsp;" id="EmpName"/></td>
                                       </tr>
                                       <tr>
                                          <td id="celluleEM" class="petitTexteNonCentre" style="VERTICAL-ALIGN: center;" >&nbsp;</td>
                                       </tr>

                                       <tr>
                                          <td id="celluleTE" class="petitTexteNonCentre" style="VERTICAL-ALIGN: center;" ><mt:dynamicValue nullValue="&nbsp;" id="TE"/></td>
                                       </tr>
                                       <tr>
                                          <td id="celluleCR" class="petitTexteNonCentre" style="VERTICAL-ALIGN: center;" ><mt:dynamicValue nullValue="&nbsp;" id="CR"/></td>
                                       </tr>
                                    </table>
		  </mt:ifTrue>
		</mt:if>
                                 
                                 </td>
                              </tr>
                           </table>
                        </td>
                        <td class="bordureDessous bordureDessus" style="vertical-align:top; BORDER-LEFT: black 1px solid " >
                           <!-- Tableau cl ech-->
                           <table style="WIDTH: 100%; Height:100%" cellSpacing="0" cellPadding="0" border="0">
                              <tr>
                                 <td class="petitTexte" style="FONT-WEIGHT: bold; VERTICAL-ALIGN: top; TEXT-ALIGN: center">Classe</td>
                              </tr>
                              <tr>
                                 <td id="celluleCl" class="petitTexteNonCentre montant" ><mt:dynamicValue nullValue="&nbsp;" id="Cl"/></td>
                              </tr>

                        <mt:if condition="solstice.custom.AvisDepotStatic.isSoquij">
                        <mt:ifFalse>
                              <tr>
                                 <td class="petitTexte" width="10" align="center">Éch.</td>
                                 <td id="celluleEch" class="petitTexteNonCentre montant" ><mt:dynamicValue nullValue="&nbsp;" id="Ech"/></td>
                              </tr>
						</mt:ifFalse>
						</mt:if>

                           </table>
                        </td>
                        <td style="vertical-align:top; BORDER-RIGHT: black 1px solid; BORDER-TOP: black 1px solid; BORDER-LEFT: black 1px solid; BORDER-BOTTOM: black 1px solid"
                           rowSpan="1" >
                           <table cellSpacing="0" cellPadding="0" width="100%" border="0" >
                              <tr>
                                 <td class="petitTexte" style="FONT-WEIGHT: bold; VERTICAL-ALIGN: top; TEXT-ALIGN: center">Taux</td>
                              </tr>
                              <tr>
                                 <td class="petitTexteNonCentre" style="text-align: right; style="text-align: center;"    id="celluleTaux" >
                                    <mt:selectCase dynamicValue="Remuneration_Method">
                                       <mt:case value="annual">
      								            <mt:dynamicValue nullValue="&nbsp;" id="AnnualSalary" formatMethod="solstice.custom.AvisDepotStatic.formatMontant"/>
                                       </mt:case>
                                       <mt:default>
      								            <mt:dynamicValue nullValue="&nbsp;" id="Hourly_Rate" formatMethod="solstice.custom.AvisDepotStatic.formatHourly_Rate"/>
                                       </mt:default>
                                    </mt:selectCase>
					         </td>
                              </tr>
                           </table>
                        </td>
                     </tr>
                     <tr>
                        <td rowSpan="6" class="bordureDroite bordureDessous" style="vertical-align:top;">
                           <!-- Tableau Crédits et Reductions -->
                           <table cellSpacing="0" cellPadding="0" border="0" width="100%" height="100%">
                           <mt:if condition="solstice.custom.AvisDepotStatic.isSoquij">
                           <mt:ifFalse>
                              <tr>
                                 <td>
                                    <!-- Tableau Crédit Impot -->
                                    <table class="bordureDessous" height="100%" width="100%" cellSpacing="0" border="0" cellPadding="0" >
                                       <tr>
                                          <td width="30%" class="petitTexte"></td>
                                          <td width="70%" class="petitTexte">Crédit d'impôt</td>
                                       </tr>
                                       <tr>
                                          <td class="petitTexte">Féd.</td>
                                          <td style="text-align: center;" class="petitTexteNonCentre" id="celluleCreditFed" >
															<mt:dynamicValue nullValue="&nbsp;" id="FederalTaxationCredit" formatMethod="solstice.custom.AvisDepotStatic.formatMontant"/>
														</td>
                                       </tr>
                                       <tr>
                                          <td class="petitTexte">Pro.</td>
                                          <td style="text-align: center;" class="petitTexteNonCentre" id="celluleCreditPro" >
															<mt:dynamicValue nullValue="&nbsp;" id="ProvincialTaxationCredit" formatMethod="solstice.custom.AvisDepotStatic.formatMontant"/>
														</td>
                                       </tr>
                                    </table>
                                    <!-- Fin Tableau Crédit Impot --></td>
                              </tr>
                              <tr>
                                 <td>
                                    <!-- Tableau Réduction Impot -->
                                    <table height="100%" cellSpacing="0" cellPadding="0" width="100%" border="0">
                                       <tr>
                                          <td width="30%" class="petitTexte"></td>
                                          <td width="70%" class="petitTexte" width="110">Réduction&nbsp;d'impôt</td>
                                       </tr>
                                       <tr>
                                          <td class="petitTexte">Féd.</td>
                                          <td style="text-align: center;" class="petitTexteNonCentre" id="celluleReductionFed" >
															<mt:dynamicValue nullValue="&nbsp;" id="FederalTaxationReduction" formatMethod="solstice.custom.AvisDepotStatic.formatMontant"/>
														</td>
                                       </tr>
                                       <tr>
                                          <td class="petitTexte">Pro.</td>
                                          <td style="text-align: center;" class="petitTexteNonCentre" id="celluleReductionPro" >
															<mt:dynamicValue nullValue="&nbsp;" id="ProvincialTaxationReduction" formatMethod="solstice.custom.AvisDepotStatic.formatMontant"/>
														</td>
                                       </tr>
                                    </table>
                                    <!-- Fin Tableau Réduction Impot --></td>
                              </tr>
                               </mt:ifFalse>
                               <mt:ifTrue>
                              <tr>
                                 <td>
                                    <!-- Tableau Crédit Impot -->
                                    <table class="bordureDessous" height="100%" width="100%" cellSpacing="0" border="0" cellPadding="0" >
                                       <tr>
                                          <td width="30%" class="petitTexte"></td>
                                          <td width="70%" class="petitTexte">Crédit d'impôt</td>
                                       </tr>
                                       <tr>
                                          <td class="petitTexte">Féd.</td>
                                          <td style="text-align: center;" class="petitTexteNonCentre" id="celluleCreditFed" >
															<mt:dynamicValue nullValue="&nbsp;" id="FederalTaxationCredit" formatMethod="solstice.custom.AvisDepotStatic.formatMontant"/>
														</td>
                                       </tr>
                                       <tr>
                                          <td class="petitTexte">Pro.</td>
                                          <td style="text-align: center;" class="petitTexteNonCentre" id="celluleCreditPro" >
															<mt:dynamicValue nullValue="&nbsp;" id="ProvincialTaxationCredit" formatMethod="solstice.custom.AvisDepotStatic.formatMontant"/>
														</td>
                                       </tr>
                                    </table>
                                    <!-- Fin Tableau Crédit Impot --></td>
                              </tr>
                               
                              <tr>
                                 <td>
                                    <!-- Tableau Impot add -->
                                    <table height="100%" cellSpacing="0" cellPadding="0" width="100%" border="0">
                                       <tr>
                                          <td width="100%" colspan="2" class="petitTexte" width="110">Impôts additionnels</td>
                                       </tr>
                                       <tr>
                                          <td class="petitTexte">Féd.</td>
                                          <td style="text-align: center;" class="petitTexteNonCentre" id="celluleReductionFed" >
					         <mt:dynamicValue nullValue="&nbsp;" id="FederalTaxationAdd" formatMethod="solstice.custom.AvisDepotStatic.formatMontant"/>
														</td>
                                       </tr>
                                       <tr>
                                          <td class="petitTexte">Pro.</td>
                                          <td style="text-align: center;" class="petitTexteNonCentre" id="celluleReductionPro" >
						<mt:dynamicValue nullValue="&nbsp;" id="ProvincialTaxationAdd" formatMethod="solstice.custom.AvisDepotStatic.formatMontant"/>
														</td>
                                       </tr>
                                    </table>
                                    <!-- Fin Tableau Impot Add -->
				</td>
                              </tr>
							 </mt:ifTrue>
							 </mt:if>
							 
                           </table> <!-- Fin tableau Crédits et Reductions --></td>
                        <td colSpan="2" class="petitTexte">&nbsp;<!-- Tableau Central vide -->
                        </td>
                        <td class="bordureGauche petitTexte" align="center">
                           <!-- Cellule Périodique -->Périodique</td>
                        <td class="bordureDroite bordureGauche petitTexte" align="center">
                           <!-- Cellule Cumulatif -->Cumulatif</td>
                     </tr>
                     <mt:if condition="solstice.custom.AvisDepotStatic.isSoquij">
                     <mt:ifFalse>
                     <tr>
                        <td class="cellulesCentrales" colspan="2" style="text-align: right;">Heures payées&nbsp;</td>
                        <td class="cellulesCentrales bordureGauche" style="text-align: right;" id="periodiqueHeuresPayees" >
									<mt:dynamicValue nullValue="&nbsp;" id="PaidHourPer" formatMethod="solstice.custom.AvisDepotStatic.formatMontant"/>
								</td>
                        <td class="cellulesCentrales bordureDroite bordureGauche" style="text-align: right;" id="cumulatifHeuresPayees">
									<mt:dynamicValue nullValue="&nbsp;" id="PaidHourCum" formatMethod="solstice.custom.AvisDepotStatic.formatMontant"/>
								</td>
                     </tr>
                     <tr>
                        <td class="cellulesCentrales" colspan="2" style="text-align: right;">Heures non payées&nbsp;</td>
                        <td class="cellulesCentrales bordureGauche" style="text-align: right;" id="periodiqueHeuresNonPayees" >
									<mt:dynamicValue nullValue="&nbsp;" id="NotPaidHourPer" formatMethod="solstice.custom.AvisDepotStatic.formatMontant"/>
								</td>
                        <td class="cellulesCentrales bordureDroite bordureGauche" style="text-align: right;" id="cumulatifHeuresNonPayees">
									<mt:dynamicValue nullValue="&nbsp;" id="NotPaidHourCum" formatMethod="solstice.custom.AvisDepotStatic.formatMontant"/>
                        </td>
                     </tr>
                     </mt:ifFalse>
  		     </mt:if>
                     
                     <tr>
                        <td class="cellulesCentrales" colspan="2" style="text-align: right;">Salaire brut&nbsp;</td>
                        <td class="cellulesCentrales bordureGauche" style="text-align: right;" id="periodiqueSalaireBrut" >
									<mt:dynamicValue nullValue="&nbsp;" id="GrossEarningsPeriod" formatMethod="solstice.custom.AvisDepotStatic.formatMontant"/>
								</td>
                        <td class="cellulesCentrales bordureDroite bordureGauche" style="text-align: right;" id="cumulatifSalaireBrut">
									<mt:dynamicValue nullValue="&nbsp;" id="GrossEarningsCum" formatMethod="solstice.custom.AvisDepotStatic.formatMontant"/>
								</td>
                     </tr>
                     <tr>
                        <td class="cellulesCentrales" colspan="2" style="text-align: right;">Avantages imposables&nbsp;</td>
                        <td class="cellulesCentrales bordureGauche" style="text-align: right;" id="periodiqueAvantagesImposables" >
									<mt:dynamicValue nullValue="&nbsp;" id="TaxableBenefitsPeriod" formatMethod="solstice.custom.AvisDepotStatic.formatMontant"/>
								</td>
                        <td class="cellulesCentrales bordureDroite bordureGauche" style="text-align: right;" id="cumulatifAvantagesImposables">
									<mt:dynamicValue nullValue="&nbsp;" id="TaxableBenefitsCum" formatMethod="solstice.custom.AvisDepotStatic.formatMontant"/>
								</td>
                     </tr>
                     <mt:if condition="solstice.custom.AvisDepotStatic.isSoquij">
                     <mt:ifFalse>

                     <tr>
                        <td class="cellulesCentrales bordureDessous" colspan="2" style="text-align: right;">Déductions&nbsp;</td>
                        <td class="cellulesCentrales bordureGauche bordureDessous" style="text-align: right;" id="periodiqueDeductions">
									<mt:dynamicValue nullValue="&nbsp;" id="DeductionPeriod" formatMethod="solstice.custom.AvisDepotStatic.formatMontant"/>
								</td>
                        <td class="cellulesCentrales bordureDroite bordureGauche bordureDessous" style="text-align: right;" id="cumulatifDeductions">
									<mt:dynamicValue nullValue="&nbsp;" id="DeductionCum" formatMethod="solstice.custom.AvisDepotStatic.formatMontant"/>
								</td>
                     </tr>
                     </mt:ifFalse>
                     <mt:ifTrue>
                     <tr>
                        <td class="cellulesCentrales " colspan="2" style="text-align: right;">Déductions&nbsp;</td>
                        <td class="cellulesCentrales bordureGauche " style="text-align: right;" id="periodiqueDeductions">
									<mt:dynamicValue nullValue="&nbsp;" id="DeductionPeriod" formatMethod="solstice.custom.AvisDepotStatic.formatMontant"/>
								</td>
                        <td class="cellulesCentrales bordureDroite bordureGauche " style="text-align: right;" id="cumulatifDeductions">
									<mt:dynamicValue nullValue="&nbsp;" id="DeductionCum" formatMethod="solstice.custom.AvisDepotStatic.formatMontant"/>
								</td>
                     </tr>

                     </mt:ifTrue>
		     </mt:if>

                     <mt:if condition="solstice.custom.AvisDepotStatic.isSoquij">
                     <mt:ifTrue>
                     <tr>
                        <td class="cellulesCentrales" colspan="2" style="text-align: right;">&nbsp;</td>
                        <td class="cellulesCentrales bordureGauche" style="text-align: right;" id="periodiqueAvantagesImposables" >&nbsp;</td>
                        <td class="cellulesCentrales bordureDroite bordureGauche" style="text-align: right;" id="cumulatifAvantagesImposables">&nbsp;</td>
								
                     </tr>
                     <tr>
                        <td class="cellulesCentrales bordureDessous" colspan="2" style="text-align: right;">&nbsp;</td>
                        <td class="cellulesCentrales bordureGauche bordureDessous" style="text-align: right;" id="periodiqueDeductions">&nbsp;</td>
                      <td class="cellulesCentrales bordureDroite bordureGauche bordureDessous" style="text-align: right;" id="cumulatifDeductions">&nbsp;</td>
								
                     </tr>
                     </mt:ifTrue>
		     </mt:if>
                     <!-- Rangées du bas (celles vide) -->
                     <tr>
                        <td width="25px">&nbsp;<!--Cellule vide --></td>
                        <td width="140px">&nbsp;<!--Cellule vide --></td>
                        <td width="200px">&nbsp;<!--Cellule vide --></td>
                        <td width="70px" class="bordureGauche bordureDessous" style="COLOR: white;font-family:Arial;font-size:12px; BACKGROUND-COLOR:#253049; text-align:center"><!-- Cellule NET -->
                           <b>N E T</b></td>
                        <td width="*" class="bordureGauche bordureDessous petitTexteNonCentre montant" id="periodiqueOmbrage"  align="right">
									<mt:dynamicValue nullValue="&nbsp;" id="PeriodNetAmount" formatMethod="solstice.custom.AvisDepotStatic.formatMontant"/>
								</td>
                        <td width="*" class="bordureGauche bordureDroite bordureDessous petitTexteNonCentre montant" id="cumulatifOmbrage"  style="text-align:right">
									<mt:dynamicValue nullValue="&nbsp;" id="CumulativeNetAmount" formatMethod="solstice.custom.AvisDepotStatic.formatMontant"/>
								</td>
                     </tr>
                  </table>
                  <!-- Fin tableau principal --></td>
            </tr>
            <tr>
               <td colSpan="3">
               <table cellSpacing="0" cellPadding="0" border="0" >
               <tr><td><br class="printHidden"></td></tr>
               <tr><td align="left">
                  <!-- Tableau Secondaire -->
                  <table id="as" cellSpacing="0" cellPadding="0" border="0" class="carreNoir" >
                     <tr>
                        <td class="petitTexte bordureDroite">No. d'employé</td>
<mt:if condition="solstice.custom.AvisDepotStatic.isSoquij">
<mt:ifFalse>
                        <td class="petitTexte bordureDroite">Distribution</td></mt:ifFalse>
</mt:if>
                        <td class="petitTexte bordureDroite">No. de paie</td>
                        <td class="petitTexte">No. du relevé</td>
                     </tr>
                     <tr>
                        <td id="celluleNumeroEmploye" class="bordureDroite" width="60px" >
									<mt:dynamicValue nullValue="&nbsp;" id="EmpValue"/>
								</td>
<mt:if condition="solstice.custom.AvisDepotStatic.isSoquij">
<mt:ifFalse>
                        <td id="celluleDistribution" class="bordureDroite" width="60px" >
									<mt:dynamicValue nullValue="&nbsp;" id="DistName"/>
								</td>
</mt:ifFalse>
</mt:if>

                        <td id="celluleNumeroPaie" class="bordureDroite" width="60px" >
									<mt:dynamicValue nullValue="&nbsp;" id="PerName"/>
								</td>
                        <td id="celluleNumeroReleve" style="font-size:8pt" width="60px" >
									<mt:dynamicValue nullValue="&nbsp;" id="PrePrintedNo"/>
								</td>
                     </tr>
                  </table>
                  <!-- Fin tableau secondaire -->
                  </td><td>
                  <!-- tableau tertiaire (info employe) -->
						<mt:dynamicStructure>
							<mt:customQuery>
								select employee.Name as Employee, location.address1 as Address, region.Name as Province,
								location.city as City, location.postal as POSTAL
								from P_Employee employee left join (C_Location location left join C_Region region
								on location.C_Region_ID = region.C_Region_ID)
								on employee.C_Location_ID = location.C_Location_ID
								where employee.P_Employee_ID = <%= (String)pageContext.getAttribute("employeeId") %>
							</mt:customQuery>
							<table id="as1" cellSpacing="0" cellPadding="0" border="0" class="infoemp" >
								<tr>
									<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
									<td id="celluleNomEmploye"  class="infoemp"><mt:dynamicValue nullValue="&nbsp;" id="Employee"/></td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td id="celluleAdrEmp"  class="infoemp"><mt:dynamicValue nullValue="&nbsp;" id="Address"/></td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td id="celluleVille_Prv"  class="infoemp">
										<mt:dynamicValue nullValue="&nbsp;" id="City"/>, 
										<mt:dynamicValue nullValue="&nbsp;" id="Province"/>
									</td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td id="celluleCP"  class="infoemp"><mt:dynamicValue nullValue="&nbsp;" id="POSTAL"/></td>
								</tr>
							</table>
						</mt:dynamicStructure>
               </td></tr>
               <tr><td><br class="printHidden"></td></tr>
               </table>
               </td>
</tr>

         <!-- Fin tableau entete -->

         <br>
<tr>
<td>
         <!-- Tableau rémunération -->
         <table class="carreNoir" id="tblRemuneration" width="87%" cellSpacing="0" cellPadding="0">
            <tr>
               <td class="bordureDessous enteteTableau" style="font-family:Arial;font-size:12px" colSpan="7">R É M U N É R A T I O N</td>
            </tr>
            <tr align="left">
               <td class="petitTexte">&nbsp;</td>
               <td class="petitTexte" colSpan="3" align="left">Périodique</td>
               <td class="petitTexte" colSpan="2" align="left">Cumulatif</td>
               <td class="petitTexte">&nbsp;</td>
            </tr>
            <tr>
               <td style="WIDTH: 300px" class="petitTexte"></td>
               <td class="petitTexte montant" style="WIDTH: 75px" align="left">Taux horaire</td>
               <td class="petitTexte montant" style="WIDTH: 75px" align="left">Unités</td>
               <td class="petitTexte montant" style="WIDTH: 75px" align="left">Montant</td>
               <td class="petitTexte montant" style="WIDTH: 75px" align="left">Unités</td>
               <td class="petitTexte montant" style="WIDTH: 75px" align="left">Montant</td>
               <td class="petitTexte" WIDTH="*" align="left">&nbsp;</td>
            </tr>
			<mt:dynamicStructure>
				<mt:customQuery>
					select Description, UnitaryPeriod, UnitaryCum, AmountPeriod, AmountCumul, Hourly_Rate
					from P_Statement_Earning_Gain
					where P_Statement_Earning_ID = <mt:dynamicValue id="P_Statement_Earning_ID" parentId="header"/>
				</mt:customQuery>
				<mt:body>
					<tr>
						<td class="sousTableaux"><mt:dynamicValue nullValue="&nbsp;" id="Description"/></td>
						<td class="sousTableaux montant"><mt:dynamicValue nullValue="&nbsp;" id="Hourly_Rate" formatMethod="solstice.custom.AvisDepotStatic.formatHourly_Rate"/></td>
						<td class="sousTableaux montant"><mt:dynamicValue nullValue="&nbsp;" id="UnitaryPeriod" formatMethod="solstice.custom.AvisDepotStatic.formatMontant"/></td>
						<td class="sousTableaux montant"><mt:dynamicValue nullValue="&nbsp;" id="AmountPeriod" formatMethod="solstice.custom.AvisDepotStatic.formatMontant"/></td>
						<td class="sousTableaux montant"><mt:dynamicValue nullValue="&nbsp;" id="UnitaryCum" formatMethod="solstice.custom.AvisDepotStatic.formatMontant"/></td>
						<td class="sousTableaux montant"><mt:dynamicValue nullValue="&nbsp;" id="AmountCumul" formatMethod="solstice.custom.AvisDepotStatic.formatMontant"/></td>
					</tr>
				</mt:body>
			</mt:dynamicStructure>
			<tr>
				<td colspan="5" class="sousTableaux" style="text-align: center; height: 15px">
					*************** Avantages imposables ***************
				</td>
			</tr>
			<mt:dynamicStructure>
				<mt:customQuery>
					select Description, AmountPeriod, AmountCumul
					from P_Statement_Earning_Taxable_Benefits
					where P_Statement_Earning_ID = <mt:dynamicValue id="P_Statement_Earning_ID" parentId="header"/>
				</mt:customQuery>
				<mt:body>
					<tr>
					
						<td class="sousTableaux"><mt:dynamicValue nullValue="&nbsp;" id="Description"/></td>
						<td></td>
						<td></td>
						<td class="sousTableaux montant"><mt:dynamicValue nullValue="&nbsp;" id="AmountPeriod" formatMethod="solstice.custom.AvisDepotStatic.formatMontant"/></td>
						<td></td>
						<td class="sousTableaux montant"><mt:dynamicValue nullValue="&nbsp;" id="AmountCumul" formatMethod="solstice.custom.AvisDepotStatic.formatMontant"/></td>
					</tr>
				</mt:body>
			</mt:dynamicStructure>
         </table>
         <!-- Fin Tableau rémunération -->

         <br>

         <!-- Tableau RETENUES -->
         <table class="carreNoir" id="tblRetenues" cellSpacing="0" cellPadding="0"  width="87%">
            <tr>
               <td class="bordureDessous enteteTableau" style="font-family:Arial;font-size:12px" colSpan="8">R E T E N U E S</td>
            </tr>
            <tr>
               <td style="WIDTH: 300px" class="petitTexte"></td>
               <td class="petitTexte montant" style="WIDTH: 75px">Périodique</td>
               <td class="petitTexte montant" style="WIDTH: 75px">Cumulatif</td>
               <td class="petitTexte montant" style="WIDTH: 75px">Solde à déduire</td>
               <td class="petitTexte" width="*" colspan="4">&nbsp;</td>
            </tr>
			<mt:dynamicStructure>
				<mt:customQuery>
                    select Description, AmountPeriod, AmountCumul, Balance
                    from P_Statement_Earning_Deduction
                    where P_Statement_Earning_ID = <mt:dynamicValue id="P_Statement_Earning_ID" parentId="header"/>
                    and Insurance = 'N' order by Description 
				</mt:customQuery>
				<mt:body>
				<tr>
					<td class="sousTableaux"><mt:dynamicValue nullValue="&nbsp;" id="Description"/></td>
					<td class="sousTableaux montant"><mt:dynamicValue nullValue="&nbsp;" id="AmountPeriod" formatMethod="solstice.custom.AvisDepotStatic.formatMontant"/></td>
					<td class="sousTableaux montant"><mt:dynamicValue nullValue="&nbsp;" id="AmountCumul" formatMethod="solstice.custom.AvisDepotStatic.formatMontant"/></td>
					<td class="sousTableaux montant"><mt:dynamicValue nullValue="&nbsp;" id="Balance" formatMethod="solstice.custom.AvisDepotStatic.formatMontant"/></td>
					<td class="sousTableaux"></td>
				</tr>
				</mt:body>
			</mt:dynamicStructure>
			
			<tr style="height: 20px">
				<td colspan="5" class="sousTableaux" style="text-align: center">****** Détail des retenues pour assurances ******</td>
			</tr>
			
			<mt:dynamicStructure id="insurance">
				<mt:customQuery>
					select P_Statement_Earning_Deduction_ID, Description, AmountPeriod, AmountCumul, Balance
					from P_Statement_Earning_Deduction
					where P_Statement_Earning_ID = <mt:dynamicValue id="P_Statement_Earning_ID" parentId="header"/>
					and Insurance = 'Y' order by Description
				</mt:customQuery>
				<mt:body>
   				<tr>
   					<td class="sousTableaux"><mt:dynamicValue nullValue="&nbsp;" id="Description"/></td>
   					<td class="sousTableaux montant"><mt:dynamicValue nullValue="&nbsp;" id="AmountPeriod" formatMethod="solstice.custom.AvisDepotStatic.formatMontant"/></td>
   					<td class="sousTableaux montant"><mt:dynamicValue nullValue="&nbsp;" id="AmountCumul" formatMethod="solstice.custom.AvisDepotStatic.formatMontant"/></td>
   					<td class="sousTableaux montant"><mt:dynamicValue nullValue="&nbsp;" id="Balance" formatMethod="solstice.custom.AvisDepotStatic.formatMontant"/></td>
   					<td class="sousTableaux"></td>
   				</tr>
   				
   				<mt:dynamicStructure id="detail">
   				   <mt:customQuery>
                     select Name, DecimalValue
                     from P_Statement_Earning_InsDetail
                     where P_Statement_Earning_Deduction_ID = <mt:dynamicValue id="P_Statement_Earning_Deduction_ID" parentId="insurance" />
   				   </mt:customQuery>
		      		<mt:body>
         				<tr>
         					<td class="sousTableaux"><mt:dynamicValue parentId="detail" nullValue="&nbsp;" id="Name"/></td>
         					<td class="sousTableaux montant"><mt:dynamicValue parentId="detail" nullValue="&nbsp;" id="DecimalValue" formatMethod="solstice.custom.AvisDepotStatic.formatMontant"/></td>
         					<td class="sousTableaux montant">&nbsp;</td>
         					<td class="sousTableaux montant">&nbsp;</td>
         					<td class="sousTableaux"></td>
         				</tr>
      				</mt:body>
   				</mt:dynamicStructure>
				</mt:body>
			</mt:dynamicStructure>

			<mt:dynamicStructure>
				<mt:customQuery>
					select AdvanceAmount
					from P_Employee_Advance_Permanent
					where P_Employee_ID = <%= (String)pageContext.getAttribute("employeeId") %>
				</mt:customQuery>
				<mt:body>
   			<tr style="height: 20px">
   				<td colspan="5" class="sousTableaux" style="text-align: center">****** Avance permanente ******</td>
   			</tr>
			
				<tr>
					<td class="sousTableaux">Avance permanente</td>
					<td class="sousTableaux montant"><mt:dynamicValue nullValue="&nbsp;" id="AdvanceAmount" formatMethod="solstice.custom.AvisDepotStatic.formatMontant"/></td>
					<td class="sousTableaux montant"></td>
					<td class="sousTableaux montant"></td>
					<td class="sousTableaux"></td>
				</tr>
				</mt:body>
			</mt:dynamicStructure>
         </table>
         <!-- Fin Tableau RETENUES -->

         <br>

<mt:if condition="solstice.custom.AvisDepotStatic.isAcrualBankExist">
<mt:ifTrue>
         <!-- Tableau banques -->
         <table class="carreNoir" cellpadding="0" cellspacing="0" width="87%">
            <tr>
               <td class="bordureDessous enteteTableau" style="font-family:Arial;font-size:12px" colSpan="4">B A N Q U E S</td>
            </tr>
            <tr>
               <td width="350" class="bordureDroite">
                  <table id="tblBanqueGauche" height="100%" cellSpacing="0" cellPadding="0" width="100%">
						<mt:dynamicStructure>
							<mt:customQuery>
								select Description, CalculUnit, Balance
								from P_Statement_Earning_Credits
								where P_Statement_Earning_ID = <mt:dynamicValue id="P_Statement_Earning_ID" parentId="header"/>
							</mt:customQuery>
							<mt:body>
							<tr>
								<td class="sousTableaux"><mt:dynamicValue nullValue="&nbsp;" id="Description"/></td>
								<td class="sousTableaux montant"><mt:dynamicValue nullValue="&nbsp;" id="CalculUnit"/></td>
								<td class="sousTableaux montant"><mt:dynamicValue nullValue="&nbsp;" id="Balance" formatMethod="solstice.custom.AvisDepotStatic.formatMontant"/></td>
							</tr>
							
							<mt:checkVisible checkMethod="solstice.custom.AvisDepotStatic.isMiddle">
						</table>
					</td>
					<td width="350">
						<table id="tblBanqueDroit" height="100%" cellSpacing="0" cellPadding="0" width="100%" >
							</mt:checkVisible>
							</mt:body>
						</mt:dynamicStructure>
                  </table>
               </td>
            </tr>
         </table>
         <!-- Fin Tableau banque -->
</mt:ifTrue>
</mt:if>
							</mt:body>
						</mt:dynamicStructure>
				     </mt:ifTrue>
				     <mt:ifFalse>
				     	Il n'existe pas d'avis de d&eacute;pot pour cet employ&eacute; &agrave; cette p&eacute;riode
				     </mt:ifFalse>
				  </mt:if>
         </td>
         </tr>
         <tr>
				<td class="printHidden"><br>

           	</td>
			</tr>
			<!-- Section Pied de page -->
			<tr>
				<td class="printHidden"><span class="siqname"></span>				</td>
				<td>
					<!--<IMG height="30" alt="" src="images/spacer.gif" width="1" border="0">--><br>
				</td>
			</tr>
			<tr  class="printHidden">
				<td class="line" width="790" height="4"></td>
			</tr>
			<!-- Fin section pied de page -->
		</TABLE>
	</form>
		
	    <script language="javascript">
<%
	if(!sTimeout.equals("0"))
	{
		// le temps est définie dans la table P_Web_parameters
		int iTimeout = Integer.parseInt(sTimeout);
		iTimeout = iTimeout * 60000;//minutes en milli secondes
%>
            	setInterval("changer()", <%=String.valueOf(iTimeout)%>);
<%
	}
%>
	    </script>

	</body>
</html>
</mt:pageTag>
</mt:securityCheck>