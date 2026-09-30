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
<%@ page import="org.compiere.model.MRole" %>
<%@ page import="org.compiere.util.DB" %>
<%@ page import="solstice.utils.PgiUtil" %>
<%@ page import="java.io.*" %>
<%@ page import="javax.servlet.*" %>
<%@ page import="javax.servlet.http.*" %>
<%@ page import="org.compiere.model.MLocation" %>
<%@ page extends="solstice.web.PageWebSolstice" %>

<%!
	String p_employee_ID = null;

	private void GenerateDetail()
	{
		String sql = " select P_Employee_ROE_ID, FirstDayWorked, LastDayForWhichPaid, FinalPayPeriodEndingDate, P_Period_ID "
                   + " from P_Employee_ROE "
                   + " where P_Employee_ROE.IsActive = 'Y' ";
	try
		{

//			if ( this.p_employee_ID != null && this.p_employee_ID.length() != 0)	
			sql += " And P_Employee_ROE.P_Employee_ID = " + this.p_employee_ID;
			
		    sql += " order By FirstDayWorked Desc ";
		    
			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			boolean alt = false;

			while (rs.next ())
			{
				if ( rs.getInt( "P_Period_ID" ) > 1002141 ) {
					tpl.assign("RELEVE", "releve");	
				}
				else {
					tpl.assign("RELEVE", "releve2015");	
				}

				tpl.assign("NOSERIE", rs.getString("P_Employee_ROE_ID"));
				tpl.assign("DATE1", rs.getTimestamp("FirstDayWorked"));
				tpl.assign("DATE2", rs.getTimestamp("LastDayForWhichPaid"));
				tpl.assign("DATE3", rs.getTimestamp("FinalPayPeriodEndingDate"));
		        if ( alt)
			        tpl.assign("CELLCLASS", "tdfieldalt");
		        else
		            tpl.assign("CELLCLASS", "tdfield");
				alt = !alt;

				tpl.parse("main.line");
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.out.println("* ERROR * Read ROE sql " + sql + "Exception :" + e);
		}
	}
	

	public String GeneratePage()  throws Exception  
	{
	    this.setPageId( "releveEmploi_index" );
		this.setSecurityCheck( true );
		this.setTemplate("ListRoe.jtpl");

		if ( request != null )
	    {
	        if ( request.getParameter("employeeId") != null)
	        	p_employee_ID = request.getParameter("employeeId").trim();
	        else p_employee_ID = null;
	    }

        
		if ( SecurityCheck() )
		{
			this.tpl.assign("TITLE", "Liste des relevés d'emplois");

			this.tpl.assign( "ComboBoxEmployee", ComboBoxEmployeeRoe() );
			this.tpl.assign( "EMPLOYEE_ID", p_employee_ID );

			GenerateDetail();
			
			if ( p_employee_ID != null )
				tpl.parse("main.new");
			
			this.tpl.parse("main");
			return (this.tpl.out());
			
		}
		return null;
	}

	public String ComboBoxEmployeeRoe()  
	{
		String selected = ""; 
		if ( request != null && request.getParameter("employeeId") != null)
			selected = request.getParameter("employeeId").trim();
		
		String result  = null;
		String sql = "Select P_Employee_ID, Name From P_Employee ";

		sql += MRole.getDefault().addAccessSQL (sql, "P_Employee", true, false); 

		try
		{

			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();

			result  = "<select name=\"employeeId\" id=\"employeeId\"  onKeyPress=\"autoSelect(this);\">";
			result += "<option></option> \n";

			while (rs.next ())
			{
				if ( rs.getString(1).equals(selected ) )
					result += "<option value=\"" + rs.getString(1) + "\" selected > " + rs.getString(2) + "</option> \n";
				else
					result += "<option value=\"" + rs.getString(1) + "\" > " + rs.getString(2) + "</option> \n";
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;

			result += "</select>";

		}
		catch (Exception e)
		{
			System.out.println("* ERROR * Read Employee " + sql + "Exception :" + e);
		}
	
		return result;
	}
%>