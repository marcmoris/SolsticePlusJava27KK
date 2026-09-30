<%@ taglib uri="/WEB-INF/taglib.tld" prefix="mt" %>

<mt:securityCheck id="evaluationRendement_moneval">
<mt:pageTag id="evaluationRendement_moneval" supportCalendar="true">	
	<%
	
	java.sql.Timestamp startDate = null;
	java.sql.Timestamp endDate = null;
	java.util.Calendar calendar = solstice.utils.PgiUtil.stringToDate(request.getParameter("dateDu"));
	if(calendar != null)
	   startDate = new java.sql.Timestamp(calendar.getTimeInMillis());
	   
	calendar = solstice.utils.PgiUtil.stringToDate(request.getParameter("dateAu"));
	if(calendar != null)
	   endDate = new java.sql.Timestamp(calendar.getTimeInMillis());
	
	%>
	
		<mt:grid>
			<mt:queryBuilder orderBy="Gest.Name, Emp.Name" checkMethod="solstice.custom.EvaluationRendementStaticMethod.useCondition">
					select ltrim(str(P_ER_Evaluation_ID)) as P_ER_Evaluation_ID,
					'<a href="' +
					(case Form.FormType
					  when 'GES' then 'evalges.jsp'
					  when 'PTB' then 'evalptb.jsp'
					  else 'evalout.jsp'
					end)
					+ '?evaluationId=' + ltrim(str(P_ER_Evaluation_ID)) + '">' + Emp.Name + '</a>' as Emp, Gest.Name as Gest, Eval.StartingRefPeriodDate, Eval.EndingRefPeriodDate
					from (P_ER_Form Form inner join (P_ER_Evaluation Eval inner join P_Employee Emp
					on Eval.P_Employee_ID = Emp.P_Employee_ID)
					on Form.P_ER_Form_ID = Eval.P_ER_Form_ID) inner join P_Employee Gest
					on Eval.P_Employee_Gest_ID = Gest.P_Employee_ID
				<mt:staticQueryCondition>Eval.P_Employee_ID = <%= ((solstice.custom.IUserInfo)session.getAttribute("userInfo")).getEmployeeId() %></mt:staticQueryCondition>
				<mt:staticQueryCondition>(Eval.Closed = 'Y' OR Eval.IsExpectationCompleted = 'Y')</mt:staticQueryCondition>
			</mt:queryBuilder>
			
			<mt:field id="P_ER_Evaluation_ID" name="&Eacute;valuation" type="Number"/>
			<mt:field id="Emp" name="Employ&eacute; &eacute;valu&eacute;" type="String"/>
			<mt:field id="Gest" name="Gestionnaire imm&eacute;diat" type="String"/>
			<mt:field id="StartingRefPeriodDate" name="D&eacute;but de la p&eacute;riode" type="Date" format="yyyy-MM-dd"/>
			<mt:field id="EndingRefPeriodDate" name="Fin de la p&eacute;riode" type="Date" format="yyyy-MM-dd"/>
		</mt:grid>

</mt:pageTag>

</mt:securityCheck>
