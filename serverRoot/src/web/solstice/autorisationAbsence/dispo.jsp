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
<%@ page import="solstice.model.P_Gain" %>
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

	P_Absence AbsenceParam;
	

	
	public static int nbrDisponibilite( P_Absence AbsenceParam )
	{
		P_Employee EmployeeParam = P_Employee.get( Env.getCtx(), AbsenceParam.getP_Employee_ID(), null);

		int nbr = 0;
		String sql = "select count(*) "
			       + " from p_employee" 
			       + "  inner join p_absence on p_absence.p_employee_id = p_employee.p_employee_id " 
			       + "  inner join p_absence_detail on p_absence.p_absence_id = p_absence_detail.p_absence_id and p_absence_detail.absencedate between " + DB.TO_DATE(AbsenceParam.getStartDate()) + " and " + DB.TO_DATE(AbsenceParam.getEndDate())
			       + "  left outer join P_Absence_Status on p_absence.P_Absence_Status_ID = P_Absence_Status.P_Absence_Status_ID "
		           + " where p_employee.p_employee_id <> " + AbsenceParam.getP_Employee_ID()
		           + " and p_employee.p_distribution_booklet_id = " + EmployeeParam.getP_Distribution_Booklet_ID() + "  "
		           ;
		
    	//System.out.println("* DEBUG * Read nbrDisponibilite " + sql);

	    try
		{

			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			boolean alt = false;
			if (rs.next ())
			{	
				nbr = rs.getInt(1);
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.out.println("* ERROR * Read Disponibility sql " + sql + "Exception :" + e);
		}
	
		return nbr;
	}

	private void loadRecord()
	{

		if ( request == null || request.getParameter("absenceId") == null )
			return;
		
		P_Employee EmployeeParam = P_Employee.get( ctx, AbsenceParam.getP_Employee_ID(), null);

		//TODO distribution booklet with the date effectin
		/*
		String sql = "select distinct p_employee.value, p_employee.name, p_absence.p_employee_id, P_Absence.P_Absence_ID, " 
			       + "       p_absence_detail.absencedate, " 
			       + "       p_absence_detail.begining_hour, " 
			       + "       p_absence_detail.ending_hour, " 
			       + "       p_absence.p_gain_id, " 
			       + "       P_Absence_Status.Name as Status "
			       + " from p_employee" 
			       + "  inner join p_absence on p_absence.p_employee_id = p_employee.p_employee_id "   
			       + "  inner join p_absence_detail on p_absence.p_absence_id = p_absence_detail.p_absence_id and p_absence_detail.absencedate between " + DB.TO_DATE(AbsenceParam.getStartDate()) + " and " + DB.TO_DATE(AbsenceParam.getEndDate())
			       + "  left outer join P_Absence_Status on p_absence.P_Absence_Status_ID = P_Absence_Status.P_Absence_Status_ID "
		           + " where p_employee.p_employee_id <> " + AbsenceParam.getP_Employee_ID()
		           + " and p_employee.p_distribution_booklet_id = " + EmployeeParam.getP_Distribution_Booklet_ID() + "  "
		           ;*/
		
		String sql = "select p_employee.value, p_employee.name, p_absence.p_employee_id, P_Absence.P_Absence_ID, " 
//		       + "       p_absence_detail.absencedate, " 
//		       + "       p_absence_detail.begining_hour, " 
//		       + "       p_absence_detail.ending_hour, " 
		       + "       p_absence.p_gain_id, " 
		       + "       P_Absence_Status.Name as Status "
		       + " from p_employee" 
		       + "  inner join p_absence on p_absence.p_employee_id = p_employee.p_employee_id "   
		       + "  inner join p_absence_detail on p_absence.p_absence_id = p_absence_detail.p_absence_id and p_absence_detail.absencedate between " + DB.TO_DATE(AbsenceParam.getStartDate()) + " and " + DB.TO_DATE(AbsenceParam.getEndDate())
		       + "  left outer join P_Absence_Status on p_absence.P_Absence_Status_ID = P_Absence_Status.P_Absence_Status_ID "
	           + " where p_employee.p_employee_id <> " + AbsenceParam.getP_Employee_ID()
	           + " and p_employee.p_distribution_booklet_id = " + EmployeeParam.getP_Distribution_Booklet_ID() + "  "
		       + " group by p_absence.p_absence_id, p_absence.p_employee_id, p_employee.value, p_employee.name, p_absence.p_gain_id,P_Absence_Status.Name";
		
	    try
		{

			//System.out.println("* DEBUG * Read Disponibility sql " + sql );

			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			boolean alt = false;
			while (rs.next ())
			{	
				P_Absence Absence = P_Absence.get( ctx, rs.getInt( "P_Absence_ID"), null);
				P_Gain Gain = P_Gain.get( ctx, rs.getInt( "P_Gain_ID") , null );
				tpl.assign("P_Absence_ID" , rs.getInt("P_Absence_ID"));
				tpl.assign("Applicant", rs.getString("Name"));
				if ( Gain.getValue() != null )
					tpl.assign("Reason",  Gain.getValue() + " - " + Gain.getName() );
				else
					tpl.assign("Reason",  "&nbsp;" );
		        tpl.assign("StartDate", Absence.getStartDate());
		        tpl.assign("EndDate", Absence.getEndDate());
		        tpl.assign("Time", Absence.displayDuration() );
		        
		        String detail = Absence.displayDurationDetail(); 
		        if ( detail != null )
		        	tpl.assign("MouseOver", "onMouseOver=\"overlib('" + detail + "'); return true;\" onMouseOut=\"nd(); return true;\" ");
		        else
		        	tpl.assign("MouseOver", "" );

		        tpl.assign("Status", rs.getString("Status"));

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
			System.out.println("* ERROR * Read Disponibility sql " + sql + "Exception :" + e);
		}

	}
	
	public String GeneratePage( ) throws Exception 
	{

		this.setPageId("admconsult");
		this.setSecurityCheck( true );
		this.setTemplate("AutorisationAbsenceDispo.jtpl");
		
		if ( ! SecurityCheck() )
		{
			response.sendError(403); 
		}

		
		tpl.assign("TITLE","SIQ - Consulter la disponibilité" );
		tpl.assign("SUBTITLE", "Autorisation d'absence" );  // Record of Employment
		tpl.assign("PAGETITLE", "Consulter la disponibilité" );
		
		tpl.assign("LabelApplicant", "Demandé par");
		tpl.assign("LabelEmployeeName", "Nom de l'employé");
		tpl.assign("LabelEmployeeNo", "No. de l'employé");
		tpl.assign("LabelEmployee", "Employé");
		tpl.assign("LabelActivity", "Unité administrative");
		tpl.assign("LabelReason", "Motif de l'absence" ); 	
		tpl.assign("LabelStartDate", "Date de début" );	
		tpl.assign("LabelEndDate", "Date de fin" );
		tpl.assign("LabelTime", "Durée (h)" );	
		tpl.assign("LabelStatus", "Statut" 	);
		tpl.assign("LabelApprove", "Pour<br /> appro." );
		tpl.assign("LabelDistribution","Distribution");
		tpl.assign("LabelFilter","Filtres");
		tpl.assign("Manager", ((solstice.custom.IUserInfo)session.getAttribute("userInfo")).getEmployeeName() );

		AbsenceParam = P_Absence.get( ctx, Integer.parseInt(request.getParameter("absenceId")), null);

	
		if ( AbsenceParam.getStartDate().compareTo( AbsenceParam.getEndDate()) != 0 )
			tpl.assign("LabelManager", "Liste des employés absent entre le " +  dateFormat.format( AbsenceParam.getStartDate() ) + " et " +  dateFormat.format( AbsenceParam.getEndDate() ));
		else
			tpl.assign("LabelManager", "Liste des employés absent le " +  dateFormat.format( AbsenceParam.getStartDate() ) );

		this.loadRecord( );

		

		tpl.parse("main");
		return (tpl.out());


	}

%>