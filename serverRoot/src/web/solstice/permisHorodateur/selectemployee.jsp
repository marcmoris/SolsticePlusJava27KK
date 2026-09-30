<%@ taglib uri="/WEB-INF/taglib.tld" prefix="mt" %>

<mt:securityCheck id="permisHorodateur_remplir">
<%
   /*
    * Ce formulaire sert uniquement de transition pour sélectionner
    * un autre employé que celui connecté.
    */
    
   solstice.custom.IUserInfo userInfo = (solstice.custom.IUserInfo)session.getAttribute("userInfo");
   if(userInfo.hasPolicy("permisHorodateur_afficherListeEmploye"))
   {
%>
	<mt:pageTag id="permisHorodateur_remplir">
	
	   <form method="post" action="remplir.jsp">
	
   	   <table width="100%" border="0" cellspacing="1" cellpadding="1">
   	      <tr>
   	         <td class="tdlabel">Employé</td>
   	         <td class="tdfield">
            	   <mt:comboBoxTag id="employeeId" selectedValue="<%= String.valueOf(userInfo.getEmployeeId()) %>">
            	      <%= userInfo.getEmployeeSQL(
	            	      	"( select top 1 assignmentParam.P_Collective_Labour_Agr_ID" +
							"    from P_Assignment assignment" +
							"    join P_Assignment_Param assignmentParam" +
							"      on assignmentParam.P_Assignment_ID = assignment.P_Assignment_ID" +
							"   where assignment.P_Employee_ID = P_Employee.P_Employee_ID" +
							"     and assignment.IsActive = 'Y'" +
							"     and assignmentParam.IsActive = 'Y'" +
							"     and getdate() between isnull( assignment.StartDate, getdate() ) and isnull( assignment.EndDate, getdate() )" +
							"     and getdate() between isnull( assignmentParam.EffectIn, getdate() ) and isnull( assignmentParam.EffectTo, getdate() )" +
							"   order by AssignmentType ) in ( select P_Collective_Labour_Agr_ID from P_PunchCard_Collective_Labour_Agr )") %>
            	   </mt:comboBoxTag>
   	         </td>
   	      </tr>
   	   </table>
   	   
   	   <p align="right">
   	      <input type="submit" value="Remplir une demande" />
   	   </p>
   	   
	   </form>
	
   </mt:pageTag>
<%

   }
   else
   {
      response.sendRedirect("remplir.jsp");
   }

%>
</mt:securityCheck>