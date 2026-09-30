<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="javax.servlet.jsp.PageContext" %>
<%@ page import="org.compiere.util.DB" %>
<%@ page import="org.compiere.util.Env" %>
<%@ page import="org.compiere.util.TimeUtil" %>
<%@ page import="solstice.custom.IUserInfo" %>
<%@ page import="solstice.web.PageWebSolstice" %>
<%@ page import="solstice.model.P_Employee" %>
<%@ page import="solstice.model.P_Workplace" %>
<%@ page import="solstice.utils.PgiUtil" %>
<%@ page import="java.io.*" %>
<%@ page import="javax.servlet.*" %>
<%@ page import="javax.servlet.http.*" %>
<%@ page import="org.compiere.model.MLocation" %>
<%@ page extends="solstice.web.PageWebSolstice" %>

<%!

public String GeneratePage( ) throws Exception 
{

	this.setPageId("Profile");
	this.setSecurityCheck( true );
	this.setTemplate("EmergencyContact.jtpl");

	if ( ! SecurityCheck() )
	{
		response.sendError(403); 
	}

	tpl.assign("TITLE","Solstice - Emergency Contact" );
	tpl.assign("SUBTITLE", "Emergency Contact" );  // Record of Employment
	tpl.assign("PAGETITLE", "Solstice - Emergency Contact" );

    P_Employee Employee = this.getEmployeeConnected();

    tpl.assign( "EmployeeID",  Employee.getP_Employee_ID() );

    tpl.assign( "EmployeeValue",  Employee.getValue() );
    tpl.assign( "EmployeeName",  Employee.getName() );
    tpl.assign( "EmployeeEmail",  Employee.getEMail() );
    tpl.assign( "EmployeePhone",  Employee.getPhone() );
    tpl.assign( "EmployeePhoneExt",  Employee.getPhoneExt() );

    P_Workplace Workplace = P_Workplace.get(ctx, Employee.getP_Workplace_ID(), null);
    if ( Workplace != null )
    	tpl.assign( "Workplace",  Workplace.getName() );

    MLocation Location = MLocation.get(ctx, Employee.getC_Location_ID(), null);
    if ( Location != null )
    {
    	tpl.assign( "EmployeeAdress1",  Location.getAddress1() );
        tpl.assign( "EmployeeAdress2",  Location.getAddress2() );
        tpl.assign( "EmployeeAdress3",  Location.getAddress3() );
        tpl.assign( "EmployeeAdress4",  Location.getAddress4() );
        
        tpl.assign( "EmployeePostal",  Location.getPostal() );
        tpl.assign( "EmployeeCity"  ,  Location.getCity() );
        tpl.assign( "EmployeeRegion"  ,  Location.getRegionName() );
        tpl.assign( "EmployeeCountry"  ,  Location.getCountryName() );
    }
    	
   
   tpl.parse("main");
   return (tpl.out());
//	return "test";
}

%>