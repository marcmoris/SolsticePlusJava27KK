
<%@ page import="java.io.*" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>


<%@ page import="org.compiere.util.DB" %>
<%@ page import="org.compiere.util.Env" %>
<%@ page import="org.compiere.util.Msg" %>

<%@ page import="solstice.custom.IUserInfo" %>

<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.util.Calendar" %>

<%@ page import="solstice.utils.PgiUtil" %>
<%@ page import="solstice.model.P_Employee" %>
<%@ page import="org.compiere.model.MLocation" %>

<%@ page extends="solstice.web.PageWebSolstice" %>

<%!
    public String GeneratePage( ) throws Exception 
	{

		IUserInfo userInfo = null;
		String liste = "";
    	
		this.setPageId("Profile");
		this.setSecurityCheck( false );
		String client = PgiUtil.getSolsticeParameter( ctx, "Client").toLowerCase();
		this.setTemplate("Address.jtpl");

		if ( ! SecurityCheck() )
		{
			response.sendError(403); 

		}

		if ( this.session != null )
			userInfo = (IUserInfo)session.getAttribute("userInfo");
		
		String webAppUrl = PgiUtil.getSolsticeParameter(ctx, "WEBAPPURL");
		if ( userInfo == null )
		{
			response.sendRedirect( webAppUrl + "login.jsp");
		}

 	   P_Employee Employee = P_Employee.get( ctx, userInfo.getEmployeeId(), null);
       MLocation location = MLocation.get( ctx, Employee.getC_Location_ID(), null );
	   tpl.assign("FirstName", Employee.getFirstName());
	   tpl.assign("LastName", Employee.getSurname());
	   tpl.assign("Suffix", Employee.getSurname());
	   tpl.assign("StreetAddress", location.getAddress1());
	   tpl.assign("StreetAddressLine2", location.getAddress2());
	   tpl.assign("City", location.getCity());
	   tpl.assign("State", location.getRegionName());
	   tpl.assign("Postal", location.getPostal());
	   tpl.assign("Email", Employee.getPersonalEmail());
	   //450 625-8325
	   tpl.assign("Field35", Employee.getPhone().trim().substring(0,3) ); 
	   tpl.assign("Field351", Employee.getPhone().trim().substring(4,7) );
	   tpl.assign("Field352", Employee.getPhone().trim().substring(8,12) );
	   tpl.parse("main");
	   return (tpl.out());
	}
%>
