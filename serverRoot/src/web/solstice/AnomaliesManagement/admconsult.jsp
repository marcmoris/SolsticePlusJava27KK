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
<%@ page import="solstice.model.P_Anomalies_Management" %>
<%@ page import="solstice.model.P_Assignment" %>
<%@ page import="solstice.model.P_Distribution_Booklet" %>
<%@ page import="solstice.model.P_Post" %>
<%@ page import="solstice.model.P_Gain" %>
<%@ page import="org.compiere.model.MActivity" %>
<%@ page import="solstice.model.P_Period" %>

<%@ page import="solstice.utils.PgiUtil" %>
<%@ page import="java.io.*" %>
<%@ page import="javax.servlet.*" %>
<%@ page import="javax.servlet.http.*" %>

<%@ page extends="solstice.web.PageWebSolstice" %>

<%!	


	public static final String USERAGENT_BLACKBERRY = "BlackBerry";
	public static final String BLACK_BERRY_TEMPLATE = "AnomaliesManagementAdminBB.jtpl";
	public static final String PC_BROWSER_TEMPLATE =  "AnomaliesManagementAdmin.jtpl";
	//boolean isSOTrx = true;
	boolean isBlackBerry = false; //Flag to check out if the request come from a BlackBerry

    
    /**
     * Cette méthode crée une table de hachage pour le relevé d'emploi passé en paramètre
     * sql += " absence.P_Employee_ID = " + ((solstice.custom.IUserInfo)session.getAttribute("userInfo")).getEmployeeId();
		
     */
    private void loadRecord(Timestamp startDate, Timestamp endDate, String filtreStatus, String filtreEmployee, String filtreDistribution, String filtreAnomalies)
    {
//		P_Anomalies_Management.loadAnomalies( ctx );
    	 
    	String sql = " select P_Anomalies_Management.P_Anomalies_Management_ID, P_Employee.Name as Employee, Description  as Anomalies, P_Gain_ID, "
                   + " AnomaliesDate, AnomaliesDuration, P_Anomalies_Management.P_Anomalies_ID, "
                   + "( SELECT AD_REF_LIST_TRL.Name from AD_Reference inner join AD_REF_LIST on AD_REF_LIST.AD_REFERENCE_ID = AD_REFERENCE.AD_REFERENCE_ID "
                   + "  left outer join AD_REF_LIST_TRL on AD_REF_LIST.AD_REF_LIST_ID = AD_REF_LIST_TRL.AD_REF_LIST_ID "
                   + " where AD_Reference.AD_Reference_ID=2101508 and AD_REF_LIST.Value  = P_Anomalies_Management.AnomaliesStatus  ) as AnomaliesStatus, " 
                   + "( case  P_Anomalies_Management.AnomaliesStatus " + this.peutApprouver(pageContext) + " else null end) as Accept, " 
                   + "( case  P_Anomalies_Management.AnomaliesStatus " + this.peutRefuser(pageContext)   + " else null end) as Refus,  "
                   + " (SELECT count(*) FROM CM_CHATENTRY WHERE CM_CHAT_ID = (select CM_CHAT_ID FROM CM_CHAT  WHERE AD_TABLE_ID = " + P_Anomalies_Management.Table_ID + " AND RECORD_ID = P_Anomalies_Management.P_Anomalies_Management_id)) as notecount "
		          
                   + " from P_Anomalies_Management "
                   + " inner join P_Employee on P_Anomalies_Management.P_Employee_ID = P_Employee.P_Employee_ID "
                   + " left outer join P_Distribution_Booklet on P_Distribution_Booklet.P_Distribution_Booklet_ID = dbo.fn_ReturnCurrentDistribution( P_Employee.P_Employee_ID, P_Anomalies_Management.AnomaliesDate ) "
                   ;
//employee.IsActive = 'Y' and 

		sql += " Where 1=1";
		
		if ( filtreStatus != null && filtreStatus.trim().length() != 0)
		{
			sql += " and ( P_Anomalies_Management.AnomaliesStatus is null or P_Anomalies_Management.AnomaliesStatus in ( " + filtreStatus + " ))";
		}

		if ( filtreAnomalies != null && filtreAnomalies.trim().length() != 0)
		{
			sql += " and P_Anomalies_Management.P_Anomalies_ID = " + filtreAnomalies + " ";
		}
		
		if ( filtreEmployee != null && filtreEmployee.trim().length() != 0)
		{
			sql += " and P_Anomalies_Management.P_Employee_ID = " + filtreEmployee + " ";
		}

		if ( filtreDistribution != null && filtreDistribution.trim().length() != 0)
		{
			
			sql += " and dbo.fn_ReturnCurrentDistribution( P_Anomalies_Management.P_Employee_ID, P_Anomalies_Management.AnomaliesDate ) = " + filtreDistribution + " ";
//			sql += " and employee.P_Distribution_Booklet_ID = " + filtreDistribution + " ";
		}


		sql += " And ( (dbo.fn_ReturnCurrentDistribution( P_Anomalies_Management.P_Employee_ID, P_Anomalies_Management.AnomaliesDate ) in (" + PageWebSolstice.getDistributionWebSQLAbsence(pageContext,new String[] {"P_Distribution_Booklet_ID"}, false) + " ) ";

	    if ( startDate != null ) 
	    {
	    	sql += " AND ( AnomaliesDate >= " + DB.TO_DATE(startDate) + " AND AnomaliesDate <= " + DB.TO_DATE(endDate) + ")"; // + " OR AnomaliesStatus = 'ATTE'"
	    }

        sql += "  )) ";
		sql += " order By case when P_Gain_ID is not null then 1 else 2 end, P_Employee.Name,  P_Anomalies_Management.AnomaliesDate desc " ;

//		System.out.println("* DEBUG * Read absence sql " + sql );


	    try
		{

			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			boolean alt = false;
			P_Anomalies_Management Anomalies;
			System.out.println("* DEBUG * Read Anomalies SQL " + sql );
			while (rs.next ())
			{		
				Anomalies = P_Anomalies_Management.get( ctx, rs.getInt("P_Anomalies_Management_ID"), null);

//				System.out.println("* DEBUG * Read P_Anomalies_Management_id " + rs.getInt("P_Anomalies_Management_ID") );

				tpl.assign("P_Anomalies_Management_ID" , rs.getInt("P_Anomalies_Management_ID"));
				tpl.assign("Applicant", rs.getString("Employee"));
		        tpl.assign("Anomalies", rs.getString("Anomalies"));
		        if ( rs.getString("AnomaliesStatus") != null )
		        	tpl.assign("Status", rs.getString("AnomaliesStatus"));
		        else
		        	tpl.assign("Status", "&nbsp;" );
		        
		        
		        tpl.assign("AnomaliesDate", rs.getTimestamp("AnomaliesDate"));
		        tpl.assign("Time", rs.getBigDecimal("AnomaliesDuration") );

//		        if ( rs.getInt("P_Anomalies_ID")  != 0 && ( rs.getInt("P_Anomalies_ID") == 5 || rs.getInt("P_Anomalies_ID") == 6 || rs.getInt("P_Anomalies_ID") == 7 || rs.getInt("P_Anomalies_ID") == 8 || rs.getInt("P_Gain_ID") == 0  ) )
		        if ( rs.getInt("P_Gain_ID") == 0   )
		        {
	        		tpl.assign("ACCEPT",   "&nbsp;");
	        		tpl.assign("REFUS",   "&nbsp;" );
			        tpl.assign("GLOBALACTION", "&nbsp;");
		        }
		        else
		        {
			        if ( rs.getInt("Accept") != 0 ) 
			        {
			        	
			        	if (isBlackBerry) 
			    
			        		tpl.assign("ACCEPT",   "<input type=\"button\" value=\"Accepter\" onClick=\"sendAction('accept'," + rs.getInt("Accept") + ");\">" );	
			        	else
			        		tpl.assign("ACCEPT",   "<a href=\"javascript:sendAction('accept', " + rs.getInt("Accept") + ")\"> <img src=\"../images/approbaccept.gif\" border=\"0\"> </a>" );
			        }
			        else 
			        {
			        	if (isBlackBerry) 
			        		tpl.assign("ACCEPT",   "<input type=\"button\" value=\"Accepter\" >");
			        	else
			        		tpl.assign("ACCEPT",   "<img src=\"../images/approbacceptg.gif\" border=\"0\"> " );
			        }
			        	
			        if ( rs.getInt("Refus") != 0 )
			        {
			        	if (isBlackBerry)
			        	{
			        		tpl.assign("REFUS",   "<input type=\"button\" value=\"Refuser\" onClick=\"sendAction('refus'," + rs.getInt("Refus") + ");\">" );
			        	}
			        	else
			        		tpl.assign("REFUS",   "<a href=\"javascript:sendAction('refus', " + rs.getInt("Refus") + ")\"> <img src=\"../images/approbreject.gif\" border=\"0\"></a>" );
			        }
			        	
			        else
			        {
			        	if (isBlackBerry)
			        		tpl.assign("REFUS",   "<input type=\"button\" value=\"Refuser\" >");
			        	else
			        		tpl.assign("REFUS",   "<img src=\"../images/approbrejectg.gif\" border=\"0\"> " );
			        }
		        	
			        if ( rs.getInt("Accept") != 0 || rs.getInt("Refus") != 0 )
				        tpl.assign("GLOBALACTION", "<input type=\"checkbox\" name=\"OptionBatch[" + rs.getInt("P_Anomalies_Management_ID") + "]\">");
			        else
				        tpl.assign("GLOBALACTION", "&nbsp;");

		        }
	        
		        if(rs.getInt("notecount")==0)
	        		tpl.assign("Notes","noteg");
	        	else
	        		tpl.assign("Notes","note");

		        if ( alt)
			        tpl.assign("CELLCLASS", "tdfieldalt");
		        else
		            tpl.assign("CELLCLASS", "tdfield");
		        

		        tpl.assign("G_ACCEPT", "<a href=\"javascript:sendGlobalAction('GlobalAccept')\"> <img src=\"../images/approbaccept.gif\" border=\"0\"></a>" );
	        	tpl.assign("G_REFUS",  "<a href=\"javascript:sendGlobalAction('GlobalRefus') \"> <img src=\"../images/approbreject.gif\" border=\"0\"></a>" );

	        	tpl.assign("G_ACCEPT", "<input type=\"button\" value=\"Accepter\" onClick=\"sendGlobalAction('GlobalAccept');\">" );
	        	tpl.assign("G_REFUS",  "<input type=\"button\" value=\"Refuser\"  onClick=\"sendGlobalAction('GlobalRefus');\">" );

				tpl.parse("main.line");
				alt = !alt;
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.out.println("* ERROR * Read P_Anomalies_Management_Detail sql " + sql + "Exception :" + e);
		}


    }

    private void Process ( String action, int AnomalyId )
    {
        
    	P_Anomalies_Management Anomalies_Management;
    	String qualificatif;
    	
    	
        // Si l'utilisateur a choisi de refuser 
        if(action.equals("refus") || action.equals("GlobalRefus") )
        {
        	Anomalies_Management = new P_Anomalies_Management(ctx, AnomalyId, null);
            if ( ! this.peutRefuser( Anomalies_Management))
            	return;
            Anomalies_Management.setAnomaliesStatus("REFU");
            Anomalies_Management.save();
        }
        // Si l'utilisateur a choisi d'accepter 
        else if(action.equals("accept") || action.equals("GlobalAccept") )
        {
            int AnomaliesStatusId = 0;
            Anomalies_Management = new P_Anomalies_Management(ctx, AnomalyId, null);
            if ( ! this.peutApprouver( Anomalies_Management ) )
                	return;
            
            // S'il s'agit d'un administrateur ou d'un gestionnaire
            if(loggedUserHasPolicy(pageContext,"approveAnomalies"))
            {
                Anomalies_Management.setAnomaliesStatus("SIGN");
                Anomalies_Management.save();

            }
        }
    }
    
    public String GeneratePage( ) throws Exception 
	{

    	Timestamp startDate = null;
    	Timestamp endDate = null;
    	String filtreStatus = null;
    	String filtreEmployee = null;
    	String filtreDistribution = null;
    	String filtreAnomalies = null;
    	String action = null;
		this.setPageId("admconsult");
		this.setSecurityCheck( true );
		
		if ( ! SecurityCheck() )
		{
			response.sendError(403); 
		}
		
		//Check out wich browser type sent the request and set the appropriate template
		String agentType = request.getHeader("user-agent");
		String typeAgent = agentType.substring(0,10);
		if (typeAgent.compareTo(USERAGENT_BLACKBERRY) == 0) {
			this.setTemplate(BLACK_BERRY_TEMPLATE);
			isBlackBerry = true;
		}	
		else
		{
			this.setTemplate(PC_BROWSER_TEMPLATE);
			isBlackBerry = false;
		}
		//ici
//		isBlackBerry = true;

    	boolean checkStartDate = true;
		boolean checkEndDate = true;
		boolean checkPeriod = true;


		if ( request.getParameter("startDate") != null && request.getParameter("startDate").trim().length() != 0 )
	          startDate = new Timestamp( PgiUtil.stringToDateV2( request.getParameter("startDate").trim()).getTimeInMillis() );
		else startDate = null;
		
		if ( request.getParameter("endDate") != null && request.getParameter("endDate").trim().length() != 0 )
	          endDate   = new Timestamp( PgiUtil.stringToDateV2( request.getParameter("endDate").trim() ).getTimeInMillis() );
		else endDate = null;
		
		if ( request.getParameter("status") != null && request.getParameter("status").trim().length() != 0 )
			filtreStatus = request.getParameter("status").trim();
		else filtreStatus = "'ATTE'";

		if ( request.getParameter("employeeId") != null && request.getParameter("employeeId").trim().length() != 0 )
			filtreEmployee = request.getParameter("employeeId");
		else filtreEmployee = null;

		if ( request.getParameter("distributionId") != null && request.getParameter("distributionId").trim().length() != 0 )
			filtreDistribution = request.getParameter("distributionId");
		
		if ( request.getParameter("FAnomaliesId") != null && request.getParameter("FAnomaliesId").trim().length() != 0 )
			filtreAnomalies = request.getParameter("FAnomaliesId");
		else filtreAnomalies = null;

		
		if (isBlackBerry)
			 action = request.getParameter("act");	
		else
			 action = request.getParameter("action");
        if(action != null)
        {
        	if ( request.getParameter("AnomaliesId") != null && request.getParameter("AnomaliesId").trim().length() != 0 )
        		Process( action, Integer.parseInt(request.getParameter("AnomaliesId")));
        }
        
		Enumeration NomsParam = request.getParameterNames();

		//
		//
		//
		String debug = "";
		while(NomsParam.hasMoreElements()) 
		{
			String NomParam = (String)NomsParam.nextElement();
			String[] ValeursParam = request.getParameterValues(NomParam);
			if ( NomParam.startsWith("OptionBatch")  ) // && ValeursParam.equals("on")
			{
				Process( action, Integer.parseInt( NomParam.substring( NomParam.indexOf('[') + 1, NomParam.indexOf(']')) ) );
						
				System.out.println("* DEBUG * call Process( action, " + NomParam.substring( NomParam.indexOf('[') + 1, NomParam.indexOf(']'))+ " )\n" );
				System.out.println("* DEBUG * ValeursParam " +  ValeursParam[0] + "- test : " + ValeursParam.equals("on") );
			}

			for(int i=0; i < ValeursParam.length; i++) {
				debug = debug + " NomParam  " + NomParam  + " - "+ ValeursParam[i]  + "\n" ;
			}
		}
		System.out.println("* DEBUG * NomParam  " + debug  + "\n" );
		//
		//
		//
        

		if(request.getParameter("status") != null && !request.getParameter("status").equals(""))
		{
			filtreStatus = request.getParameter("status");
		}
		else
		{
			filtreStatus = "'ATTE'";
		}

		P_Period Period = P_Period.getOpenPeriodWithPaymentGroup(ctx, this.getEmployeeConnected().getP_Payment_Group_ID(), null);
		if ( request.getParameter("firstLoad") != null )
		{
			firstLoad = (request.getParameter("firstLoad").equals("true"));
		}
		
		if ( firstLoad )
		{
			checkPeriod = true;
			checkStartDate = true;
			checkEndDate = true;
			startDate = Period.getStartDate();
			endDate   = Period.getEndDate();
			filtreDistribution = null;
		}
		else 
		{
			if ( request.getParameter("cStartDate") != null )
			{
				  checkStartDate = (request.getParameter("cStartDate").trim().length() != 0);
				  if ( checkStartDate && ( request.getParameter("startDate") == null || request.getParameter("startDate").trim().length() == 0 ))
						startDate = Period.getStartDate();
			}
			
			if ( request.getParameter("cEndDate") != null )
				checkEndDate = (request.getParameter("cEndDate").trim().length() != 0);

            if ( request.getParameter("cPeriod") != null )
            	checkPeriod = true;
            else
            	checkPeriod = false;
            	
            if ( request.getParameter("cPeriod") != null && request.getParameter("periodId") != null && request.getParameter("periodId").trim().length() != 0)
            {
            	checkPeriod = true;
            	Period = P_Period.get(ctx, Integer.parseInt(request.getParameter("periodId")) , null);
    			startDate = Period.getStartDate();
    			endDate   = Period.getEndDate();
    			checkStartDate = false;
    			checkEndDate = false;
            }

		}

//		this.pageContext = ;

		if ( SecurityCheck() )
		{
			tpl.assign("TITLE","Gestion des anomalies - Approbation des coupures de salaire et reports de temps" );
			tpl.assign("SUBTITLE", "Gestion des anomalies" );  // Record of Employment
			tpl.assign("PAGETITLE", "Gestion des anomalies - Approbation des coupures de salaire et reports de temps" );
			tpl.assign("LabelManager", "Liste des coupures de salaire et reports de temps à approuver par :" );
			if (isBlackBerry)
				tpl.assign("LabelApplicant", "Nom:");
			else
				tpl.assign("LabelApplicant", "Demandé par:");
			
			tpl.assign("LabelEmployeeName", "Nom de l'employé");
			tpl.assign("LabelEmployeeNo", "No. de l'employé");
			tpl.assign("LabelEmployee", "Employé");
			tpl.assign("LabelActivity", "Unité administrative");
			tpl.assign("LabelAnomalies", "Anomalies:" );
			tpl.assign("LabelDate", "Date:" );
			tpl.assign("LabelTime", "Durée (h):" );	
			tpl.assign("LabelStatus", "Statut" 	);
			tpl.assign("LabelAnomaliesId", "Anomalie" 	);
			tpl.assign("LabelApprove", "Pour<br /> appro." );
			tpl.assign("LabelAccept", "Accept.");
			tpl.assign("LabelRevoke", "Refus.");
			tpl.assign("LabelDispo", "Dispo.");
			tpl.assign("LabelNote", "Note" );
			tpl.assign("LabelEdit", "Édit." );
			tpl.assign("LabelDelete", "Suppr." );
			tpl.assign("LabelDistribution","Distribution");
			tpl.assign("LabelFilter","Filtres");
			tpl.assign("LabelAnomalies","Anomalies");

			tpl.assign("Manager", ((solstice.custom.IUserInfo)session.getAttribute("userInfo")).getEmployeeName() );
			
			P_Assignment Assignment = P_Assignment.get( ctx, this.getEmployeeConnected().GetAssignmentPrincipal(), null);
			P_Post Post = P_Post.get(ctx, Assignment.getP_Post_ID(), null);
			MActivity Activity = new MActivity( ctx, Post.getC_Activity_ID(), null);
			tpl.assign("EmployeeName", this.getEmployeeConnected().getName());
			tpl.assign("EmployeeNo", this.getEmployeeConnected().getValue());
			tpl.assign("Activity", Activity.getValue() + " - " + Activity.getName() );

			tpl.assign( "SelectEmployee", this.ComboBoxEmployee() );
			tpl.assign( "SelectStatus", this.ComboBoxStatus() );
			tpl.assign( "SelectDistribution" ,ComboBoxDistributionBooklet() );
			tpl.assign( "SelectAnomalies", this.ComboBoxAnomalies() );
			
			tpl.assign( "LabelPeriod", "Période" 	);
			tpl.assign( "SelectPeriod" ,ComboBoxPeriod() );

			if ( checkPeriod )
			{
				checkStartDate = false;
				checkEndDate = false;
				tpl.assign("cPeriod", "CHECKED");
			}
			else
			{
				tpl.assign("cPeriod", "");
			}


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
			
			 
			this.loadRecord(startDate, endDate, filtreStatus, filtreEmployee, filtreDistribution, filtreAnomalies);

        	tpl.parse("main");
			return (tpl.out());
			
		}
		return null;
	}
	
    
    /**
     * Retourne la date de début de la période courrante
     */
    private Timestamp currentPeriodStartDate( int P_Employee_ID)
    {
    	P_Employee Employee = P_Employee.get( ctx, P_Employee_ID, null);
        P_Period period = P_Period.getOpenPeriodWithPaymentGroup(ctx, Employee.getP_Payment_Group_ID(), null);
        return period.getStartDate();
    }
    

	private String getEmployeeName (int noEmploye)
	{
		P_Employee Employee = P_Employee.get( ctx, noEmploye, null);
		String employeeName = Employee.getFirstName() + " " + Employee.getSurname();
	    return employeeName;
	}

	private boolean peutApprouver (P_Anomalies_Management Anomalies_Management )
    {
//        P_Gain Gain = P_Gain.get( ctx, Absence.getP_Gain_ID(), null);
		
    	if (loggedUserHasPolicy(pageContext,"approveAnomalies") && Anomalies_Management.getAnomaliesStatus().equals( "ATTE" ) )
    	{
    		return true;
    	}
    	return false;
    }


    private boolean peutRefuser ( P_Anomalies_Management Anomalies_Management)
    {
    	if (loggedUserHasPolicy(pageContext,"refuseAnomalies") && Anomalies_Management.getAnomaliesStatus().equals( "ATTE" ) )
    	{
    		return true;
    	}
    	return false;
   }

	
    /**
     * Permet d'obtenir le cas du switch case sql qui permet d'approuver
     * @return le sql
     */

	private String peutApprouver (PageContext pageContext)
    {
    
    	String sql = "";
    	if (loggedUserHasPolicy(pageContext,"approveAnomalies"))
    	{
    		sql += " when 'ATTE' then P_Anomalies_Management.P_Anomalies_Management_ID ";
    	}

    	if(sql.equals(""))
    		sql += " when 'ZZZZ' then '' ";
    	return sql;
    }
    
    /**
     * Permet d'obtenir le cas du switch case sql qui permet de refuser
     * @return le sql
     */
    private String peutRefuser (PageContext pageContext)
    {
    	String sql = "";
    	if (loggedUserHasPolicy(pageContext,"refuseAnomalies"))
    	{
    		sql += " when 'ATTE' then P_Anomalies_Management.P_Anomalies_Management_ID";
    	}

    	if(sql.equals(""))
    		sql += " when 'ZZZZ' then '' ";

    	return sql;
   }
    
    
   /*
    *  Filtres des différents statuts possible 
    */ 
   private String ComboBoxStatus( )
   {

		String selected = ""; 
		if ( request != null && request.getParameter("status") != null)
			selected = request.getParameter("status").trim();
		else
			selected = "'ATTE'";


		String result  = null;
/*		String sql = "select AD_REF_LIST.Value, AD_REF_LIST_TRL.NAME from AD_Reference "
			       + " inner join AD_REF_LIST on AD_REF_LIST.AD_REFERENCE_ID = AD_REFERENCE.AD_REFERENCE_ID "
			       + " inner join AD_REF_LIST_TRL on AD_REF_LIST.AD_REF_LIST_ID = AD_REF_LIST_TRL.AD_REF_LIST_ID "
			       + " where AD_Reference.AD_Reference_ID=2101508"
			       ;
		try
		{

			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
*/
			result  = "<select name=\"status\" id=\"status\"  onKeyPress=\"autoSelect(this);\">";

			String sel1 = "";
			String sel2 = "";
			String sel3 = "";
			String sel4 = "";
			String sel5 = "";
			if ( selected.equals("'ATTE'") )
				sel1 = "selected";
			if ( selected.equals("'ATTE', 'SIGN', 'REFU'") )
				sel2 = "selected";
			if ( selected.equals("'SIGN'") )
				sel3 = "selected";
			if ( selected.equals("'ATTE', 'REFU'") )
				sel4 = "selected";
			if ( selected.equals("'REFU'") )
				sel5 = "selected";			
			result += "<option value=\"'ATTE'\"" + sel1 + " >En attente d'approbation</option>\n";
			result += "<option value=\"'ATTE', 'SIGN', 'REFU'\"" + sel2 + " >Toutes les anomalies</option>\n";
			result += "<option value=\"'SIGN'\"" + sel3 + " >Toutes approuvées</option>\n";
			result += "<option value=\"'ATTE', 'REFU'\"" + sel4 + " >Toutes non approuvées</option>\n";
			result += "<option value=\"'REFU'\"" + sel5 + " >Toutes refusées</option>\n";
//			result += "<option value=\"\" >-------------------------</option>\n";

/*
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
*/
			result += "</select>";

/*		}
		catch (Exception e)
		{
			System.out.println("* ERROR * Read AnomaliesStatus " + sql + "Exception :" + e);
		}
	*/	
		return result;

   }

   
	public String ComboBoxAnomalies( )  
	{
		String selected = ""; 
		if ( request != null && request.getParameter("FAnomaliesId") != null)
			selected = request.getParameter("FAnomaliesId").trim();
		
		String result  = null;
		String sql =  " select P_Anomalies_ID, ltrim(rtrim(Name)) + ' (' + ltrim(rtrim(Value)) + ')' as description  from P_Anomalies "
                   + " where IsActive = 'Y' "
                   + " order by  2 asc ";
;
		try
		{

			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();

			result  = "<select name=\"FAnomaliesId\" id=\"FAnomaliesId\"  onKeyPress=\"autoSelect(this);\">";
			result += "<option ></option> \n";

			while (rs.next ())
			{
				if ( rs.getString(1).equals(selected ) )
					result += "<option value=\"" + rs.getString(1) + "\" selected > " + rs.getString(2)  + "</option> \n";
				else
					result += "<option value=\"" + rs.getString(1) + "\" > " + rs.getString(2)  + "</option> \n";
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;

			result += "</select>";

		}
		catch (Exception e)
		{
			System.out.println("* ERROR * Read Anomalies " + sql + "Exception :" + e);
		}
		
		return result;
	}

%>