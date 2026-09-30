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
<%@ page import="solstice.web.PageWebSolstice" %>
<%@ page import="solstice.model.P_Employee" %>
<%@ page import="solstice.model.P_Payment_Group" %>
<%@ page import="solstice.model.P_Statement_Earning" %>
<%@ page import="solstice.model.P_Frequency" %>
<%@ page import="solstice.model.P_Job_Title" %>
<%@ page import="solstice.model.P_Language" %>
<%@ page import="solstice.model.P_Period" %>
<%@ page import="solstice.model.P_Post" %>
<%@ page import="solstice.model.P_Assignment" %>

<%@ page import="org.compiere.model.MActivity" %>
<%@ page import="org.compiere.model.MLocation" %>
<%@ page import="org.compiere.model.MOrg" %>
<%@ page import="org.compiere.model.MClient" %>
<%@ page import="org.compiere.model.MUser" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page extends="solstice.web.PageWebSolstice" %>
<%!

	int roeID ;
	int employeeID ;

	boolean isSOTrx = true;


	private void GenerateDetailEarning( P_Statement_Earning Statement_Earning )
	{
		String sql = "select Description, UnitaryPeriod, UnitaryCum, AmountPeriod, AmountCumul from P_Statement_Earning_Gain where P_Statement_Earning_ID = " + Statement_Earning.getP_Statement_Earning_ID(); 
		try
		{
			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			boolean alt = false;

			while (rs.next ())
			{
				tpl.assign("Description", rs.getString("Description"));
				tpl.assign("UnitaryPeriod", rs.getBigDecimal("UnitaryPeriod"));
				tpl.assign("AmountPeriod", rs.getBigDecimal("AmountPeriod"));
				tpl.assign("UnitaryCum", rs.getBigDecimal("UnitaryCum"));
				tpl.assign("AmountCumul", rs.getBigDecimal("AmountCumul"));
				tpl.parse("main.line");
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.out.println("* ERROR * Read P_Statement_Earning_Gain sql " + sql + "Exception :" + e);
		}
	}

	private void GenerateDetailTaxable_Benefits( P_Statement_Earning Statement_Earning )
	{
		String sql = " select Description, AmountPeriod, AmountCumul from P_Statement_Earning_Taxable_Benefits where P_Statement_Earning_ID = " + Statement_Earning.getP_Statement_Earning_ID(); 
		try
		{
			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			boolean alt = false;

			while (rs.next ())
			{
				tpl.assign("Description", rs.getString("Description"));
				tpl.assign("AmountPeriod", rs.getBigDecimal("AmountPeriod"));
				tpl.assign("AmountCumul", rs.getBigDecimal("AmountCumul"));
				tpl.parse("main.line2");
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.out.println("* ERROR * Read P_Statement_Earning_Gain sql " + sql + "Exception :" + e);
		}
	}

	private void GenerateDetailDeduction( P_Statement_Earning Statement_Earning )
	{
		String sql = "select Description, AmountPeriod, AmountCumul, Balance "
		           + " from P_Statement_Earning_Deduction "
		           + " where P_Statement_Earning_ID = " + Statement_Earning.getP_Statement_Earning_ID()
		           + " and Insurance = 'N' order by Description " ; 
		try
		{
			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			boolean alt = false;

			while (rs.next ())
			{
				tpl.assign("Description", rs.getString("Description"));
				tpl.assign("AmountPeriod", rs.getBigDecimal("AmountPeriod"));
				tpl.assign("AmountCumul", rs.getBigDecimal("AmountCumul"));
				tpl.assign("Balance", rs.getBigDecimal("Balance"));
				tpl.parse("main.line3");
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.out.println("* ERROR * Read P_Statement_Earning_Gain sql " + sql + "Exception :" + e);
		}
	}

	private void GenerateDetailAcrualBank( P_Statement_Earning Statement_Earning )
	{
		String sql = " select Description, CalculUnit, Balance from P_Statement_Earning_Credits "
			       + " where P_Statement_Earning_ID = " + Statement_Earning.getP_Statement_Earning_ID(); 
		try
		{
			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			boolean alt = false;

			while (rs.next ())
			{
				tpl.assign("Description", rs.getString("Description"));
				tpl.assign("CalculUnit", rs.getString("CalculUnit"));
				tpl.assign("Balance", rs.getBigDecimal("Balance"));
				tpl.parse("main.line4");
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.out.println("* ERROR * Read P_Statement_Earning_Gain sql " + sql + "Exception :" + e);
		}
	}


    private int getPayStubCount( P_Employee Employee, P_Period Period)
    {
        int PayStubCount = 0;
    	P_Statement_Earning Statement_Earning;
        P_Employee employee;
        P_Frequency frequency;
        P_Job_Title jobTitle;
        P_Payment_Group PaymentGroup;

    	String sql = "select count(*) "
	           + " from P_Statement_Earning " 
	           + " WHERE P_Statement_Earning.P_Employee_ID = " + Employee.getP_Employee_ID()
	           + "   and P_Period_ID = " + Period.getP_Period_ID()
	           ;
		try
		{
			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				PayStubCount = rs.getInt( 1 );
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.out.println("* ERROR * Read payStub sql " + sql + "Exception :" + e);
		}
		
		return PayStubCount;
    }

    public String GeneratePage( ) throws Exception 
	{

    	
		this.setPageId("avisDepot_avis");
		this.setSecurityCheck( true );
		this.setTemplate("PayStub.jtpl");
		
		if ( ! SecurityCheck() )
		{
			response.sendError(403); 
		}

        
        P_Employee Employee = P_Employee.get( ctx, ((IUserInfo)session.getAttribute("userInfo")).getEmployeeId(), null);
        System.out.println("* DEBUG * payStub for " + Employee.getValue() + " " + Employee.getName() );
		P_Period Period = P_Period.get( ctx, Integer.parseInt((String)pageContext.getAttribute("periodId")), null);
            
//        P_Period Period = P_Period.getOpenPeriodWithCalendar(ctx, PaymentGroup.getP_Calendar_ID(), null);
            
        int P_Statement_Earning_ID = 0;    

        int PayStubCount = getPayStubCount( Employee, Period);
        
        if ( PayStubCount != 0 )
        {
            P_Statement_Earning Statement_Earning;
            
    		String sql = "select P_Statement_Earning_ID "
    	           + " from P_Statement_Earning " 
    	           + " WHERE P_Statement_Earning.P_Employee_ID = " + Employee.getP_Employee_ID()
    	           + "   and P_Period_ID = " + Period.getP_Period_ID()
    	           ;
    		PreparedStatement pstmt = null;
    		pstmt = DB.prepareStatement (sql, null);
    		ResultSet rs = pstmt.executeQuery ();
    		if (rs.next ())
    		{
    	        Statement_Earning = P_Statement_Earning.get(ctx, P_Statement_Earning_ID, null );
    		    
    	        tpl.assign("hiddenP_Statement_Earning_Id", Statement_Earning.getP_Statement_Earning_ID());
    			tpl.assign("hiddenP_Employee_ID", Employee.getP_Employee_ID());

    	        String Language = "fr_CA"; // this.getLanguage();
    			tpl.assign("TITLE", Msg.getMsg(Language, "PayStub", true ));  // Record of Employment
    			tpl.assign("sRedirect", "ICI" );

    			GenerateDetailEarning( Statement_Earning );
    			GenerateDetailTaxable_Benefits( Statement_Earning );
    			GenerateDetailDeduction( Statement_Earning );
    			GenerateDetailAcrualBank( Statement_Earning );
    		}
        }
        else
        {
			tpl.assign("MSG", "Il n'existe pas d'avis de d&eacute;pot pour cet employ&eacute; &agrave; cette p&eacute;riode" ); 
        }


        
		
		tpl.parse("main");
		return (tpl.out());
	}

    

%>