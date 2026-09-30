<%@ taglib uri="/WEB-INF/taglib.tld" prefix="mt" %>

<%
   solstice.custom.IUserInfo userInfo = (solstice.custom.IUserInfo)session.getAttribute("userInfo");
%>



<mt:securityCheck id="avisDepot_index">

<%
	// if not a admin redirect to avis for employee
    if( ! userInfo.hasPolicy("avis_allemployee"))
    {
		response.sendRedirect("avis.jsp");
    }
    else
    {
		response.sendRedirect("avis_admin.jsp");
    }
%>
</mt:securityCheck>

