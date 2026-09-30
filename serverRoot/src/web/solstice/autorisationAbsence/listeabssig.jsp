<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.Enumeration" %>


<%@ page import="javax.servlet.jsp.PageContext" %>
<%@ page import="org.compiere.util.DB" %>
<%@ page import="org.compiere.util.Env" %>
<%@ page import="org.compiere.util.TimeUtil" %>
<%@ page import="solstice.custom.IUserInfo" %>
<%@ page import="solstice.web.PageWebSolstice" %>
<%@ page import="solstice.model.P_Employee" %>
<%@ page import="solstice.model.P_Absence" %>
<%@ page import="solstice.model.P_Absence_Detail" %>
<%@ page import="solstice.model.P_Absence_Status" %>
<%@ page import="solstice.model.P_Assignment" %>
<%@ page import="solstice.model.P_Post" %>
<%@ page import="org.compiere.model.MActivity" %>
<%@ page import="solstice.model.P_Period" %>

<%@ page import="solstice.utils.PgiUtil" %>
<%@ page import="java.io.*" %>
<%@ page import="javax.servlet.*" %>
<%@ page import="javax.servlet.http.*" %>

<%@ page extends="solstice.web.PageWebSolstice" %>

<%!	


//	String id = "consult";
	
	Timestamp startDate = null;
	Timestamp endDate = null;
	String status = null;
	
	boolean isSOTrx = true;

    
    
    /**
     * Cette méthode crée une table de hachage pour le relevé d'emploi passé en paramètre
     */
    private void loadRecord(Timestamp startDate, Timestamp endDate, String filtreEmployee, String demande)
    {
    	

    	String sql = "select employee.Name as EmployeeName, " 
                   + " absence.ADate, "
                   + "         case absence.DayTime WHEN 'Z' "
                   + "             then AD_Ref_List_Trl.Name + '  ('+absence.Begining_hour+' - '+absence.Ending_Hour+')' " 
                   + " Else AD_Ref_List_Trl.Name " 
                   + " END as Period, "
                   + " isnull(( select top 1 'Oui' " 
                   + " from P_Absence " 
                   + " inner join P_Absence_Detail on P_Absence.P_Absence_ID = P_Absence_Detail.P_Absence_ID "
                   + " left outer join P_Absence_Status on P_Absence_Status.P_Absence_Status_ID = P_Absence.P_Absence_Status_ID "
                   + " where P_Absence.P_Employee_ID = employee.P_Employee_ID "
                   + " and P_Absence_Status.value not in ('REFU','DSUP','DELE')"
                   + " and Year(P_Absence_Detail.AbsenceDate) = Year(absence.ADate) "
                   + " and Month(P_Absence_Detail.AbsenceDate) = Month(absence.ADate) "
                   + " and Day(P_Absence_Detail.AbsenceDate) = Day(absence.ADate) ), 'Non') as Demandee, replace(absence.Description, '''', '\''') as Description, " 
                   + " absence.P_Absence_Significant_ID " 
                   + " from P_Employee employee "
                   + " inner join AD_Reference on AD_Reference.Name = 'P_DayTime' "
                   + " inner join AD_Ref_List on AD_Reference.AD_Reference_ID = AD_Ref_List.AD_Reference_ID  "
                   + " inner join AD_Ref_List_Trl on AD_Ref_List_Trl.AD_Ref_List_ID = AD_Ref_List.AD_Ref_List_ID "  
                   + " inner join P_Absence_Significant absence  on absence.DayTime = AD_Ref_List.Value and employee.P_Employee_ID = absence.P_Employee_ID "
                   ;

//		sql += " Where absence.P_Employee_ID = " + ((solstice.custom.IUserInfo)session.getAttribute("userInfo")).getEmployeeId();
		
    	 sql += " Where dbo.fn_ReturnCurrentDistribution( employee.P_Employee_ID, absence.ADate ) in (" + PageWebSolstice.getDistributionWebSQLAbsence(pageContext,new String[] {"P_Distribution_Booklet_ID"}, false) + " ) ";

//        sql += " Where Employee.P_Distribution_Booklet_ID in ( " + solstice.custom.AutorisationAbsenceADM.getDistributionSQL(pageContext,new String[] {"P_Distribution_Booklet_ID"}, false) + " )" ;

        sql += " and AD_Reference.Name = 'P_DayTime' and AD_Ref_List_Trl.AD_Language = 'fr_CA'";

		if ( filtreEmployee != null && filtreEmployee.trim().length() != 0)
		{
			sql += " and employee.P_Employee_ID = " + filtreEmployee + " ";
		}

//		if ( demande != null )
		{
			if ( demande == null || demande.equals("N"))
				sql += " and not exists ( ";
			else
				sql += " and exists ( ";
			
			sql += " select 1 from P_Absence inner join P_Absence_Detail "
		        + " on P_Absence.P_Absence_ID = P_Absence_Detail.P_Absence_ID "
                + " left outer join P_Absence_Status on P_Absence_Status.P_Absence_Status_ID = P_Absence.P_Absence_Status_ID "
		        + " where P_Absence.P_Employee_ID = employee.P_Employee_ID "
                + " and P_Absence_Status.value not in ('REFU','DSUP','DELE')"
		        + " and Year(P_Absence_Detail.AbsenceDate) = Year(absence.ADate) "
		        + " and Month(P_Absence_Detail.AbsenceDate) = Month(absence.ADate) "
		        + " and Day(P_Absence_Detail.AbsenceDate) = Day(absence.ADate) "
		        + " ) ";
		}

	    if ( startDate != null ) 
	    {
	    	sql += " and absence.ADate >= " + DB.TO_DATE(startDate);
	    	if ( endDate != null )
	            sql += " and absence.ADate  <= " + DB.TO_DATE(endDate) ;
	    }
	    else if ( endDate != null )
	      sql += " and absence.ADate  <= " + DB.TO_DATE(endDate) ;

		sql += " order by employee.Name, absence.ADate Desc " ;

	    try
		{

			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			boolean alt = false;
			P_Absence Absence;
			while (rs.next ())
			{	
//				Absence = P_Absence.get( ctx, rs.getInt("P_Absence_ID"), null);
//				tpl.assign("P_Absence_ID" , rs.getInt("P_Absence_ID"));
		        tpl.assign("Name", rs.getString("EmployeeName"));
		        tpl.assign("Date", rs.getTimestamp("ADate"));
		        tpl.assign("PERIOD", rs.getString("Period"));
		        
		        tpl.assign("Demandee", rs.getString("Demandee") );
		        if ( rs.getString("Description") != null)
		        	
		        	tpl.assign("REM", "<a href=\"javascript:alert('Remarque sur l absence:\n" + rs.getString("Description").replaceAll("'", " ") + "')\"><img border=\"0\" src=\"images/remarque.gif\"></a>" );
		        else
		        	tpl.assign("REM", "<img src=\"images/remarqueg.gif\">" );

		        if ( rs.getInt("P_Absence_Significant_ID") != 0)
		        	tpl.assign("DELETE", "<a href=\"javascript:deleteAbsence(" + rs.getInt("P_Absence_Significant_ID") + ")\"> <img src=\"images/delete.gif\" border=\"0\"></a>" );
		        else
		        	tpl.assign("DELETE", "<img src=\"images/deleteg.gif\" border=\"0\">" );
		        
		        if ( alt)
			        tpl.assign("CELLCLASS", "tdfieldalt");
		        else
		            tpl.assign("CELLCLASS", "tdfield");
		        
				tpl.parse("main.line");
				alt = !alt;
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.out.println("* ERROR * Read P_Absence_Detail sql " + sql + "Exception :" + e);
		}


    }
   


    public String GeneratePage( ) throws Exception 
	{

		this.setPageId("listeabssig");
		this.setSecurityCheck( true );
		this.setTemplate("AutorisationAbsenceListSig.jtpl");

		if ( ! SecurityCheck() )
		{
			response.sendError(403); 
		}
		
    	String filtreEmployee = null;
    	String demande = null;

		boolean checkStartDate = false;
		boolean checkEndDate = false;
		
		if ( request.getParameter("startDate") != null && request.getParameter("startDate").trim().length() != 0 )
	          startDate = new Timestamp( PgiUtil.stringToDateV2( request.getParameter("startDate").trim()).getTimeInMillis() );
		else startDate = null;
		
		if ( request.getParameter("endDate") != null && request.getParameter("endDate").trim().length() != 0 )
	          endDate   = new Timestamp( PgiUtil.stringToDateV2( request.getParameter("endDate").trim() ).getTimeInMillis() );
		else endDate = null;

		if ( request.getParameter("demandee") != null )
			demande = request.getParameter("demandee");
		else demande = null;

		if ( request.getParameter("employeeId") != null && request.getParameter("employeeId").trim().length() != 0 )
			filtreEmployee = request.getParameter("employeeId");
		else filtreEmployee = null;

		String action = request.getParameter("action");
		if(action != null)
		{	
	        if(action.equals("delete"))
	        {
	            // On récupère d'abord l'absence et on évalue le statut actuel
	            if(request.getParameter("toRemove") != null && !request.getParameter("toRemove").equals(""))
	            {
		            String sql 
		            = "delete from P_Absence_Significant"
		                + " where P_Absence_Significant_ID = " + request.getParameter("toRemove");
		            
		            DB.executeUpdate(sql, null);
	            }

	        }
		}

		if ( firstLoad )
		{
			checkStartDate = false;
			checkEndDate = false;
			
		}
		else 
		{
            if ( request.getParameter("cStartDate") != null )
			{
				  checkStartDate = (request.getParameter("cStartDate").trim().length() != 0);
			}

            
			if ( request.getParameter("cEndDate") != null )
				checkEndDate = (request.getParameter("cEndDate").trim().length() != 0);

		}

		
//		this.pageContext = ;

		tpl.assign("TITLE","Liste des absences signifiées" );
		tpl.assign("SUBTITLE", "Autorisation d'absence" );  // Record of Employment
		tpl.assign("PAGETITLE", "Liste des absences signifiées" );

		tpl.assign("LabelEmployee", "No. et nom de l'employé");
		tpl.assign("LabelDate", "Date de l'absence" );	
		tpl.assign("LabelPeriod", "Période de l'absence");
		tpl.assign("Demande", "Demandée*" ); 	
		tpl.assign("Rem", "Rem." );	
		tpl.assign("LabelStatus", "Statut" 	);
		tpl.assign("LabelApprove", "Pour<br /> appro." );
		tpl.assign("LabelEdit", "Édit." );
		tpl.assign("LabelDelete", "Suppr." );
		tpl.assign("LabelStartDate", "Date de début:" );
		tpl.assign("LabelEndDate", "Date de fin:" );
	
		tpl.assign("LabelFilter","Filtres");

		if ( checkStartDate )
		{
			tpl.assign("cStartDate", "CHECKED");
			tpl.assign("SelStartDate", startDate );
		}
		else
		{
			tpl.assign("cStartDate", "");
			tpl.assign("SelStartDate", "" );
		}
		if ( checkEndDate )
		{
			tpl.assign("cEndDate", "CHECKED");
			tpl.assign("SelEndDate", endDate );
		}
		else 
		{
			tpl.assign("cEndDate", "");
			tpl.assign("SelEndDate", "" );
		}
		tpl.assign( "SelectEmployee", this.ComboBoxEmployee() );

		if ( demande == null || demande.equals("N") )
		{
			tpl.assign( "deSelectedO", "" );
			tpl.assign( "deSelectedN", "Selected" );
		}
		else
		{
			tpl.assign( "deSelectedO", "Selected" );
			tpl.assign( "deSelectedN", "" );
		}
	
		this.loadRecord(startDate, endDate, filtreEmployee, demande );
		
		tpl.parse("main");
		return (tpl.out());
	}
	
%>

