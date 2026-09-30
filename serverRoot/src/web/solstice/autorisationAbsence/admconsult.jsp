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
	public static final String BLACK_BERRY_TEMPLATE = "AutorisationAbsenceAdminBB.jtpl";
	public static final String PC_BROWSER_TEMPLATE =  "AutorisationAbsenceAdmin.jtpl";
	//boolean isSOTrx = true;
	boolean isBlackBerry = false; //Flag to check out if the request come from a BlackBerry

    
    /**
     * Cette méthode crée une table de hachage pour le relevé d'emploi passé en paramètre
     * sql += " absence.P_Employee_ID = " + ((solstice.custom.IUserInfo)session.getAttribute("userInfo")).getEmployeeId();
		
     */
    private void loadRecord(Timestamp startDate, Timestamp endDate, String filtreStatus, String filtreEmployee, String filtreDistribution, String filtreEarning)
    {
    	String sql = " select TOP 200 absence.P_Absence_ID, employee.Name as Employee, rtrim(gain.Value) + ' - ' + gain.Name as Motif, "
                   + " min(absenceDetail.AbsenceDate) as StartDate, max(absenceDetail.AbsenceDate) as EndDate, sum(absenceDetail.Duration) as Duration, "
                   + " absenceStatus.Name as Status, " 
                   + "( case absenceStatus.Value " + this.peutApprouver(pageContext) + " else null end) as Accept, " 
                   + "( case absenceStatus.Value " + this.peutRefuser(pageContext)   + " else null end) as Refus,  "
                   + " absence.P_Absence_ID as Edit," 
                   + "( case absenceStatus.Value " + this.peutSupprimer(pageContext) + " else null end) as Del ,"
                   + " isFlexibleHours, isCallCenter, "
                   + " (SELECT count(*) FROM CM_CHATENTRY WHERE CM_CHAT_ID = (select CM_CHAT_ID FROM CM_CHAT  WHERE AD_TABLE_ID = 2100758 AND RECORD_ID = absence.p_absence_id)) as notecount, "

                   + " dbo.getEmployeeDisponibility( absence.p_employee_id, employee.P_Distribution_Booklet_ID,  min(absenceDetail.AbsenceDate) , max(absenceDetail.AbsenceDate) ) as disponibility, "
                   + " absence.P_Absence_Status_ID, "
                   + " ad_user.name as ApprovedBy "
		          
                   + " from P_Absence absence "
                   + " inner join P_Absence_Status absenceStatus on absence.P_Absence_Status_ID = absenceStatus.P_Absence_Status_ID "
                   + " inner join P_Absence_Detail absenceDetail on absence.P_Absence_ID = absenceDetail.P_Absence_ID "
                   + " inner join P_Gain gain on absence.P_Gain_ID = gain.P_Gain_ID "
                   + " inner join P_Employee employee on absence.P_Employee_ID = employee.P_Employee_ID "
                   + " left outer join AD_USER on AD_User.AD_User_ID = ( select top 1 createdby  from p_absence_status_histo where p_absence_status_histo.P_Absence_ID = absence.P_Absence_ID and p_absence_status_histo.P_Absence_Status_ID = absence.P_Absence_Status_ID order by created ) "
                   + " left outer join AD_WF_NODE ON AD_WF_NODE.AD_WF_NODE_ID = absence.AD_WF_NODE_ID "
                   + " left outer join P_Distribution_Booklet on P_Distribution_Booklet.P_Distribution_Booklet_ID = dbo.fn_ReturnCurrentDistribution( employee.P_Employee_ID, absenceDetail.AbsenceDate ) "
                   ;
//employee.IsActive = 'Y' and 

		sql += " Where isnull( absenceDetail.duration, 0) <> 0 ";
		if ( filtreStatus != null && filtreStatus.trim().length() != 0)
		{
			sql += " and absenceStatus.Value in ( " + filtreStatus + " )";
		}

		if ( filtreEmployee != null && filtreEmployee.trim().length() != 0)
		{
			sql += " and employee.P_Employee_ID = " + filtreEmployee + " ";
		}

		if ( filtreEarning != null && filtreEarning.trim().length() != 0)
		{
			sql += " and absence.P_Gain_ID = " + filtreEarning + " ";
		}

		if ( filtreDistribution != null && filtreDistribution.trim().length() != 0)
		{
			
			sql += " and dbo.fn_ReturnCurrentDistribution( employee.P_Employee_ID, absenceDetail.AbsenceDate ) = " + filtreDistribution + " ";
//			sql += " and employee.P_Distribution_Booklet_ID = " + filtreDistribution + " ";
		}


		sql += " And ( (dbo.fn_ReturnCurrentDistribution( employee.P_Employee_ID, absenceDetail.AbsenceDate ) in (" + PageWebSolstice.getDistributionWebSQLAbsence(pageContext,new String[] {"P_Distribution_Booklet_ID"}, false) + " ) ";
		
//+ 2011.05.18 Ajout autorisation par workflow.
		if ( ! ((solstice.custom.IUserInfo)session.getAttribute("userInfo")).hasPolicy("avis_allemployee") == true  )
	    {
			sql += " AND  absence.AD_WF_NODE_ID IS NULL and gain.ad_workflow_id is null ) "
//Gestionnaire 			
			+ "      OR  ( ( absence.AD_WF_NODE_ID IS NOT NULL AND EMAILRECIPIENT = 'G' AND ((" + ((IUserInfo)session.getAttribute("userInfo")).getUserId() + " = absence.ApprovedBy ) "
			;

//			sql += "  OR " + ((IUserInfo)session.getAttribute("userInfo")).getEmployeeId() + " = P_Distribution_Booklet.P_Employee_Secretary_ID  ))"  
			sql += "  OR " + ((IUserInfo)session.getAttribute("userInfo")).getEmployeeId() + " in ( select P_Distribution_Booklet.P_Employee_Secretary_ID union select P_Employee_ID From P_Distribution_Booklet_Replacement  Where P_Distribution_Booklet_Replacement.P_Distribution_Booklet_ID=P_Distribution_Booklet.P_Distribution_Booklet_ID and ReplacementType = 'S' and StartDate <= getdate() and ( EndDate is null or EndDate >= getdate() ) )))"  
			; // D.R.H

	    }

//DRH			   
            if(loggedUserHasPolicy(pageContext,"seeAllDistributionAndEmployees"))
//        	if ( ((solstice.custom.IUserInfo)session.getAttribute("userInfo")).hasPolicy("seeAllDistributionAndEmployees") == true  )
        	{
				sql += "  OR   ( absence.AD_WF_NODE_ID IS NOT NULL AND EMAILRECIPIENT = 'H' AND absence.ApprovedBy = 1000 )  " ; // D.R.H
        	}			 
//Vice-président			
		if ( ! ((solstice.custom.IUserInfo)session.getAttribute("userInfo")).hasPolicy("avis_allemployee") == true  )
	    {
			sql += " OR ( absence.AD_WF_NODE_ID IS NOT NULL AND EMAILRECIPIENT = 'V' AND " + ((IUserInfo)session.getAttribute("userInfo")).getUserId() + " = absence.ApprovedBy )  "
             ;
        }			
        sql += "  )) ";
//- 2011.05.18		
		sql += " group By absence.P_Absence_ID, absence.p_employee_id, employee.P_Distribution_Booklet_ID, employee.Name, absenceStatus.Value, absenceStatus.Name, gain.Value, gain.Name, isFlexibleHours, isCallCenter, absence.P_Absence_Status_ID, ad_user.name ";

	    if ( startDate != null ) 
	    {
	    	sql += " having max(absenceDetail.AbsenceDate) >= " + DB.TO_DATE(startDate);
	    	if ( endDate != null )
	            sql += " and min(absenceDetail.AbsenceDate) <= " + DB.TO_DATE(endDate) ;
	    }
	    else if ( endDate != null )
	      sql += " having min(absenceDetail.AbsenceDate) <= " + DB.TO_DATE(endDate) ;

		sql += " order By employee.Name, min(absenceDetail.AbsenceDate) desc " ;

//		System.out.println("* DEBUG * Read absence sql " + sql );


	    try
		{

			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			boolean alt = false;
			P_Absence Absence;
			System.out.println("* DEBUG * Read absence SQL " + sql );
			while (rs.next ())
			{		
				Absence = P_Absence.get( ctx, rs.getInt("P_Absence_ID"), null);

//				System.out.println("* DEBUG * Read absence_id " + rs.getInt("P_Absence_ID") );

				tpl.assign("P_Absence_ID" , rs.getInt("P_Absence_ID"));
				tpl.assign("Applicant", rs.getString("Employee"));
		        tpl.assign("Reason", rs.getString("Motif"));
		        
		        if(rs.getString("isFlexibleHours").equals("Y"))
		        	tpl.assign("cHV", " checked");
		        else
		        	tpl.assign("cHV", "");
		        
		        if(rs.getString("isCallCenter").equals("Y"))
		        	tpl.assign("cCA", " checked");
		        else
		        	tpl.assign("cCA", "");
		        
		        tpl.assign("StartDate", rs.getTimestamp("StartDate"));
		        tpl.assign("EndDate", rs.getTimestamp("EndDate"));
		        tpl.assign("Time", Absence.displayDuration( ) );
		        String detail = Absence.displayDurationDetail();
		        if ( detail != null )
		        	tpl.assign("MouseOver", "onMouseOver=\"overlib('" + detail + "'); return true;\" onMouseOut=\"nd(); return true;\" ");
		        else
		        	tpl.assign("MouseOver", "" );

			    if ( rs.getInt("P_Absence_Status_ID") == P_Absence_Status.getId("APPR") )
			    	tpl.assign("Status", rs.getString("Status") + " par " + rs.getString( "ApprovedBy" ) );
			    else
			    	tpl.assign("Status", rs.getString("Status"));
			    
		        if ( rs.getInt("Accept") != 0 ) 
		        {
		        	
		        	if (isBlackBerry) 
		    
		        		tpl.assign("ACCEPT",   "<input type=\"button\" value=\"Accepter\" onClick=\"sendAction('accept'," + rs.getInt("Accept") + ");\">" );	
		        	else
		        		tpl.assign("ACCEPT",   "<a href=\"javascript:sendAction('accept', " + rs.getInt("Accept") + ")\"> <img src=\"images/approbaccept.gif\" border=\"0\"> </a>" );
		        }
		        else 
		        {
		        	if (isBlackBerry) 
		        		tpl.assign("ACCEPT",   "<input type=\"button\" value=\"Accepter\" >");
		        	else
		        		tpl.assign("ACCEPT",   "<img src=\"images/approbacceptg.gif\" border=\"0\"> " );
		        }
		        	
		        if ( rs.getInt("Refus") != 0 )
		        {
		        	if (isBlackBerry)
		        	{
		        		tpl.assign("REFUS",   "<input type=\"button\" value=\"Refuser\" onClick=\"sendAction('refus'," + rs.getInt("Refus") + ");\">" );
		        	}
		        	else
		        		tpl.assign("REFUS",   "<a href=\"javascript:sendAction('refus', " + rs.getInt("Refus") + ")\"> <img src=\"images/approbreject.gif\" border=\"0\"></a>" );
		        }
		        	
		        else
		        {
		        	if (isBlackBerry)
		        		tpl.assign("REFUS",   "<input type=\"button\" value=\"Refuser\" >");
		        	else
		        		tpl.assign("REFUS",   "<img src=\"images/approbrejectg.gif\" border=\"0\"> " );
		        }
		        	
//		        if ( Disponibility.nbrDisponibilite( Absence ) != 0)
		        if ( rs.getInt("Disponibility") != 0)
		        	tpl.assign("DISPO", "<a href=dispo.jsp?absenceId=" + rs.getInt("P_Absence_ID") + " target=\"_blank\" > <img src=\"images/dispo.gif\" border=\"0\"> </a>" );
		        else 
		        	tpl.assign("DISPO", "&nbsp;" );
		        
		        if(rs.getInt("notecount")==0)
	        		tpl.assign("Notes","noteg");
	        	else
	        		tpl.assign("Notes","note");
		        
		        if ( rs.getInt("Edit") != 0 )
		        	tpl.assign("EDIT", "<a href=edit.jsp?absenceId=" + rs.getInt("Edit") + " target=\"_blank\" > <img src=\"images/edit.gif\" border=\"0\"> </a>" );
		        else
		        	tpl.assign("EDIT", "<img src=\"images/edit.gif\" border=\"0\">" );



		        if ( rs.getInt("Del") != 0 )
	        	tpl.assign("DELETE", "<a href=\"javascript:deleteAbsence(" + rs.getInt("Del") + ")\"> <img src=\"images/delete.gif\" border=\"0\"> </a>" );
//	        	tpl.assign("DELETE", "<a href=\"javascript:sendAction('delete'," + rs.getInt("Del") + ")\"> <img src=\"images/delete.gif\" border=\"0\"> </a>" );
		        
		        else
		        	tpl.assign("DELETE", "<img src=\"images/deleteg.gif\" border=\"0\">" );

		        if ( alt)
			        tpl.assign("CELLCLASS", "tdfieldalt");
		        else
		            tpl.assign("CELLCLASS", "tdfield");
		        
		        if ( rs.getInt("Accept") != 0 || rs.getInt("Refus") != 0 || rs.getInt("Del") != 0 )
			        tpl.assign("GLOBALACTION", "<input type=\"checkbox\" name=\"OptionBatch[" + rs.getInt("P_Absence_ID") + "]\">");
		        else
			        tpl.assign("GLOBALACTION", "&nbsp;");

		        tpl.assign("G_ACCEPT", "<a href=\"javascript:sendGlobalAction('GlobalAccept')\"> <img src=\"images/approbaccept.gif\" border=\"0\"></a>" );
	        	tpl.assign("G_REFUS",  "<a href=\"javascript:sendGlobalAction('GlobalRefus') \"> <img src=\"images/approbreject.gif\" border=\"0\"></a>" );
	        	tpl.assign("G_DELETE", "<a href=\"javascript:sendGlobalAction('GlobalDelete')\"> <img src=\"images/delete.gif\" border=\"0\"> </a>" );

	        	tpl.assign("G_ACCEPT", "<input type=\"button\" value=\"Accepter\" onClick=\"sendGlobalAction('GlobalAccept');\">" );
	        	tpl.assign("G_REFUS",  "<input type=\"button\" value=\"Refuser\"  onClick=\"sendGlobalAction('GlobalRefus');\">" );
	        	tpl.assign("G_DELETE", "<input type=\"button\" value=\"Supprimer\"  onClick=\"sendGlobalAction('GlobalDelete');\">" );

	        	if ( rs.getString( "isFlexibleHours").equals("Y"))
	        		tpl.assign("cHV[" + rs.getInt("P_Absence_ID") + "]", "CHECKED");
	        	
	        	if ( rs.getString( "isCallCenter").equals("Y"))
	        		tpl.assign("cCA[" + rs.getInt("P_Absence_ID") + "]", "CHECKED");

	        	
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

    private void Process ( String action, int absenceId )
    {
        
    	P_Absence absence;
    	String qualificatif;
    	
    	
        // Si l'utilisateur a choisi de supprimer, on doit faire passer
        // le statut de DSUP à DELE
    	if(action.equals("delete") || action.equals("GlobalDelete"))
        {
    		
            absence = new P_Absence(ctx, absenceId, null);
            if ( ! this.peutSupprimer(absence) )
            	return;
            
            absence.setP_Absence_Status_ID(P_Absence_Status.getId("DELE"));
            
            // Si l'absence avait été créée dans le calendrier de l'employé, on
            // doit la supprimer
//            if(absence.isAddedCalendar())
//                absence.setAddedCalendar(this.removeAbsence(absenceId));
            
            absence.save();
//            qualificatif = "supprimé";
//            envoyerMessage(absence,qualificatif);
            
            
        }
        // Si l'utilisateur a choisi de refuser une demande d'autorisation
        // d'absence, on doit faire passer le statut à REFU
        else if(action.equals("refus") || action.equals("GlobalRefus") )
        {
        	absence = new P_Absence(ctx, absenceId, null);
            if ( ! this.peutRefuser(absence))
            	return;
            absence.setP_Absence_Status_ID(P_Absence_Status.getId("REFU"));
            
            // Si l'absence avait été créée dans le calendrier de l'employé, on
            // doit la supprimer
//            if(absence.isAddedCalendar())
//                absence.setAddedCalendar(this.removeAbsence(absenceId));
            absence.save();
//            qualificatif = "refusé";
//            envoyerMessage(absence,qualificatif);
        }
        // Si l'utilisateur a choisi d'accepter une demande d'autorisation
        // d'absence, on doit d'abord vérifier le rôle de l'utilisateur. Dans
        // le cas d'un signataire, on doit faire passer le statut à SIGN. Dans
        // le cas d'un délégué, on le fait passer à SIGD. Lorsqu'il s'agit
        // d'un approbateur, on le fait passer à APPR ou SIGD dépendemment
        // du niveau d'approbation
        else if(action.equals("accept") || action.equals("GlobalAccept") )
        {
            int absenceStatusId = 0;
            absence = new P_Absence(ctx, absenceId, null);
            if ( ! this.peutApprouver(absence) )
                	return;
            
            // S'il s'agit d'un administrateur ou d'un gestionnaire
            if(loggedUserHasPolicy(pageContext,"approve"))
            {
                absenceStatusId = P_Absence_Status.getId("SIGN");

                // On doit maintenant ajouter cette absence au calendrier de
                // l'employé si celle-ci n'exists pas déjà
//                if(!absence.isAddedCalendar())
//                    absence.setAddedCalendar(this.createAbsence(absenceId));
            }
            // S'il s'agit d'une secrétaire ou d'un délégué
            else if(loggedUserHasPolicy(pageContext,"approveByDelegate"))
            {
                absenceStatusId = P_Absence_Status.getId("SIGD");

                // On doit maintenant ajouter cette absence au calendrier de
                // l'employé si celle-ci n'exists pas déjà
 //               if(!absence.isAddedCalendar())
 //                   absence.setAddedCalendar(this.createAbsence(absenceId));
        	}
            // Sinon, il s'agit d'un approbateur coordonnateur
            else if(loggedUserHasPolicy(pageContext,"approvePartially"))
            {
            	absenceStatusId = P_Absence_Status.getId("APPR");
            }
            
            absence.setP_Absence_Status_ID(absenceStatusId);            
            absence.save();
//            qualificatif = "approuvé";
//            envoyerMessage(absence,qualificatif);
        }

    }
    
    public String GeneratePage( ) throws Exception 
	{

    	Timestamp startDate = null;
    	Timestamp endDate = null;
    	String filtreStatus = null;
    	String filtreEmployee = null;
    	String filtreDistribution = null;
    	String filtreEarning = null;
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

    	boolean checkStartDate = false;
		boolean checkEndDate = false;
		boolean checkPeriod = false;


		if ( request.getParameter("startDate") != null && request.getParameter("startDate").trim().length() != 0 )
	          startDate = new Timestamp( PgiUtil.stringToDateV2( request.getParameter("startDate").trim()).getTimeInMillis() );
		else startDate = null;
		
		if ( request.getParameter("endDate") != null && request.getParameter("endDate").trim().length() != 0 )
	          endDate   = new Timestamp( PgiUtil.stringToDateV2( request.getParameter("endDate").trim() ).getTimeInMillis() );
		else endDate = null;
		
		if ( request.getParameter("status") != null && request.getParameter("status").trim().length() != 0 )
			filtreStatus = request.getParameter("status").trim();
		else filtreStatus = "'ATTE', 'DSUP', 'APPR'";

		if ( request.getParameter("employeeId") != null && request.getParameter("employeeId").trim().length() != 0 )
			filtreEmployee = request.getParameter("employeeId");
		else filtreEmployee = null;

		if ( request.getParameter("distributionId") != null && request.getParameter("distributionId").trim().length() != 0 )
			filtreDistribution = request.getParameter("distributionId");
		
		if ( request.getParameter("earningId") != null && request.getParameter("earningId").trim().length() != 0 )
			filtreEarning = request.getParameter("earningId");
		else filtreEarning = null;

		
		if (isBlackBerry)
			 action = request.getParameter("act");	
		else
			 action = request.getParameter("action");
        if(action != null)
        {
        	if ( request.getParameter("absenceId") != null && request.getParameter("absenceId").trim().length() != 0 )
        		Process( action, Integer.parseInt(request.getParameter("absenceId")));
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
			filtreStatus = "'ATTE', 'DSUP', 'APPR'";
		}

		P_Period Period = P_Period.getOpenPeriodWithPaymentGroup(ctx, this.getEmployeeConnected().getP_Payment_Group_ID(), null);
		if ( request.getParameter("firstLoad") != null )
		{
			firstLoad = (request.getParameter("firstLoad").equals("true"));
		}
		
		if ( firstLoad )
		{
			checkPeriod = false;
			checkStartDate = false;
			checkEndDate = false;
			startDate = Period.getStartDate();
			//TODO MOdif régle bogue affichage
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
			tpl.assign("TITLE","SIQ - Consulter la liste des absences de vos employés à approuver" );
			tpl.assign("SUBTITLE", "Autorisation d'absence" );  // Record of Employment
			tpl.assign("PAGETITLE", "Consulter la liste des absences de vos employés à approuver" );
			tpl.assign("LabelManager", "Liste des absences à approuver par :" );
			if (isBlackBerry)
				tpl.assign("LabelApplicant", "Nom:");
			else
				tpl.assign("LabelApplicant", "Demandé par:");
			
			tpl.assign("LabelEmployeeName", "Nom de l'employé");
			tpl.assign("LabelEmployeeNo", "No. de l'employé");
			tpl.assign("LabelEmployee", "Employé");
			tpl.assign("LabelActivity", "Unité administrative");
			tpl.assign("LabelReason", "Motif:" );
			if (isBlackBerry)
			{
				tpl.assign("LabelStartDate", "Début:" );
				tpl.assign("LabelEndDate", "Fin:" );
			}
			else
			{
				tpl.assign("LabelStartDate", "Date de début:" );
				tpl.assign("LabelEndDate", "Date de fin:" );
			}
			tpl.assign("LabelTime", "Durée (hre:cent):" );	
			tpl.assign("LabelStatus", "Statut" 	);
			tpl.assign("LabelEarning", "Motif" 	);
			tpl.assign("LabelApprove", "Pour<br /> appro." );
			tpl.assign("LabelAccept", "Accept.");
			tpl.assign("LabelRevoke", "Refus.");
			tpl.assign("LabelDispo", "Dispo.");
			tpl.assign("LabelNote", "Note" );
			tpl.assign("LabelEdit", "Édit." );
			tpl.assign("LabelDelete", "Suppr." );
			tpl.assign("LabelDistribution","Distribution");
			tpl.assign("LabelFilter","Filtres");

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
			tpl.assign( "SelectEarning", this.ComboBoxEarning() );
			
			tpl.assign( "LabelPeriod", "Période" 	);
			tpl.assign( "SelectPeriod" ,ComboBoxPeriod() );
			
			tpl.assign("LabelHV", "HV" ); 	
			tpl.assign("LabelCA", "CA" ); 	

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
			
			 
			this.loadRecord(startDate, endDate, filtreStatus, filtreEmployee, filtreDistribution, filtreEarning);

        	/*tpl.assign("G_ACCEPT", "<a href=\"javascript:sendGlobalAction('GlobalAccept')\"> <img src=\"images/approbaccept.gif\" border=\"0\"></a>" );
        	tpl.assign("G_REFUS",  "<a href=\"javascript:sendGlobalAction('GlobalRefus') \"> <img src=\"images/approbreject.gif\" border=\"0\"></a>" );
        	tpl.assign("G_DELETE", "<a href=\"javascript:sendGlobalAction('GlobalDelete')\"> <img src=\"images/delete.gif\" border=\"0\"> </a>" );

        	tpl.assign("G_ACCEPT", "<input type=\"button\" value=\"Accepter\" onClick=\"sendGlobalAction('GlobalAccept');\">" );
        	tpl.assign("G_REFUS",  "<input type=\"button\" value=\"Refuser\"  onClick=\"sendGlobalAction('GlobalRefus');\">" );
        	tpl.assign("G_DELETE", "<input type=\"button\" value=\"Supprimer\"  onClick=\"sendGlobalAction('GlobalDelete');\">" );
*/
        	tpl.parse("main");
			return (tpl.out());
			
		}
		return null;
	}
	

	private boolean peutApprouver (P_Absence Absence)
    {
        P_Gain Gain = P_Gain.get( ctx, Absence.getP_Gain_ID(), null);
		
    	if (loggedUserHasPolicy(pageContext,"approve") && P_Absence_Status.getId("ATTE") == Absence.getP_Absence_Status_ID() )
    	{
    		return true;
    	}
    	else if (loggedUserHasPolicy(pageContext,"approvePartially") && P_Absence_Status.getId("ATTE") == Absence.getP_Absence_Status_ID())
    	{
    		return true;
    	}
    	else if (loggedUserHasPolicy(pageContext,"approveByDelegate") && P_Absence_Status.getId("ATTE") == Absence.getP_Absence_Status_ID() && Gain.getAD_Workflow_ID() == 0 )
    	{
    		return true;
    	}
    	if (loggedUserHasPolicy(pageContext,"approveApprovedByDelegate") && P_Absence_Status.getId("SIGD") == Absence.getP_Absence_Status_ID() )
    	{
    		return true;
    	}
    	if (loggedUserHasPolicy(pageContext,"approveApprovedPartially") && P_Absence_Status.getId("APPR") == Absence.getP_Absence_Status_ID())
    	{
    		return true;
    	}
    	return false;
    }


    private boolean peutRefuser ( P_Absence Absence)
    {
    	if (loggedUserHasPolicy(pageContext,"refuseAbsence") && P_Absence_Status.getId("ATTE") == Absence.getP_Absence_Status_ID())
    	{
    		return true;
    	}
    	if (loggedUserHasPolicy(pageContext,"refuseApprovedPartially") && P_Absence_Status.getId("APPR") == Absence.getP_Absence_Status_ID())
    	{
    		return true;
    	}
    	return false;
   }

    private boolean peutSupprimer (P_Absence Absence)
    {
       	// Permet de détruire toutes les absences ( pour administrateur ).
    	if (loggedUserHasPolicy(pageContext,"deleteAllAbsence"))
    	{
        	return true;
    	}
    	
    	if (loggedUserHasPolicy(pageContext,"deleteAbsence") && P_Absence_Status.getId("ATTE") == Absence.getP_Absence_Status_ID())
    	{
    		return true;
    	}
    	if (loggedUserHasPolicy(pageContext,"askApprovedPartiallyDeletion") && P_Absence_Status.getId("APPR") == Absence.getP_Absence_Status_ID())
    	{
    		return true;
    	}
    	if (loggedUserHasPolicy(pageContext,"askApprovedDeletion") && P_Absence_Status.getId("SIGN") == Absence.getP_Absence_Status_ID())
    	{
    		return true;
    	}
    	if (loggedUserHasPolicy(pageContext,"deleteAskDeletion") && P_Absence_Status.getId("DSUP") == Absence.getP_Absence_Status_ID())
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
    	if (loggedUserHasPolicy(pageContext,"approve"))
    	{
    		sql += " when 'ATTE' then absence.P_Absence_ID ";
    	}
    	else if (loggedUserHasPolicy(pageContext,"approvePartially"))
    	{
    		sql += " when 'ATTE' then absence.P_Absence_ID ";
    	}
    	else if (loggedUserHasPolicy(pageContext,"approveByDelegate")  )
    	{
    		sql += " when 'ATTE' then case when max( isnull( Gain.AD_Workflow_ID,0)) = 0 then absence.P_Absence_ID else null end ";
    	}
    	if (loggedUserHasPolicy(pageContext,"approveApprovedByDelegate"))
    	{
    		sql += " when 'SIGD' then absence.P_Absence_ID ";
    	}
    	if (loggedUserHasPolicy(pageContext,"approveApprovedPartially"))
    	{
    		sql += " when 'APPR' then absence.P_Absence_ID ";
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
    	if (loggedUserHasPolicy(pageContext,"refuseAbsence"))
    	{
    		sql += " when 'ATTE' then absence.P_Absence_ID";
    	}
    	if (loggedUserHasPolicy(pageContext,"refuseApprovedPartially"))
    	{
    		sql += " when 'APPR' then absence.P_Absence_ID";
    	}
    	if(sql.equals(""))
    		sql += " when 'ZZZZ' then '' ";

    	return sql;
   }
    
    /**
     * Permet d'obtenir le cas du switch case sql qui permet de supprimer
     * @return le sql
     */
    private String peutSupprimer (PageContext pageContext)
    {
       	String sql = "";
       	// Permet de détruire toutes les absences ( pour administrateur ).
    	if (loggedUserHasPolicy(pageContext,"deleteAllAbsence"))
    	{
    		sql += " when absenceStatus.Value then absence.P_Absence_ID";
        	return sql;
    	}
    	
    	if (loggedUserHasPolicy(pageContext,"deleteAbsence"))
    	{
    		sql += " when 'ATTE' then absence.P_Absence_ID";
    	}
    	if (loggedUserHasPolicy(pageContext,"askApprovedPartiallyDeletion"))
    	{
    		sql += " when 'APPR' then absence.P_Absence_ID";
    	}
    	if (loggedUserHasPolicy(pageContext,"askApprovedDeletion"))
    	{
    		sql += " when 'SIGN' then absence.P_Absence_ID";
    	}
    	if (loggedUserHasPolicy(pageContext,"deleteAskDeletion"))
    	{
    		sql += " when 'DSUP' then absence.P_Absence_ID";
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
			selected = "'ATTE', 'DSUP', 'APPR'";


		String result  = null;
		String sql = "select '''' + absenceStatus.Value + '''', absenceStatus.Name from P_Absence_Status absenceStatus";
		try
		{

			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();

			result  = "<select name=\"status\" id=\"status\"  onKeyPress=\"autoSelect(this);\">";

			String sel1 = "";
			String sel2 = "";
			String sel3 = "";
			String sel4 = "";
			String sel5 = "";
			if ( selected.equals("'ATTE', 'DSUP', 'APPR'") )
				sel1 = "selected";
			if ( selected.equals("'INIT', 'ATTE', 'DSUP', 'APPR', 'SIGN', 'SIGD', 'TRSF', 'REFU', 'DELE', 'TRPR'") )
				sel2 = "selected";
			if ( selected.equals("'INIT', 'ATTE', 'DSUP'") )
				sel3 = "selected";
			if ( selected.equals("'APPR', 'SIGN', 'SIGD', 'TRSF', 'TRPR'") )
				sel4 = "selected";
			if ( selected.equals("'INIT', 'ATTE', 'REFU'") )
				sel5 = "selected";
			
			result += "<option value=\"'ATTE', 'DSUP', 'APPR'\"" + sel1 + " >En attente d'approbation</option>\n";
			result += "<option value=\"'INIT', 'ATTE', 'DSUP', 'APPR', 'SIGN', 'SIGD', 'TRSF', 'REFU', 'DELE', 'TRPR'\"" + sel2 + " >Toutes les absences</option>\n";
			result += "<option value=\"'INIT', 'ATTE', 'DSUP'\"" + sel3 + " >Absences en traitement</option>\n";
			result += "<option value=\"'APPR', 'SIGN', 'SIGD', 'TRSF', 'TRPR'\"" + sel4 + " >Toutes approuvées</option>\n";
			result += "<option value=\"'INIT', 'ATTE', 'REFU'\"" + sel5 + " >Toutes non approuvées</option>\n";
			result += "<option value=\"\" >-------------------------</option>\n";


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
			System.out.println("* ERROR * Read AbsenceStatus " + sql + "Exception :" + e);
		}
		
		return result;

   }

   
	public String ComboBoxEarning( )  
	{
		String selected = ""; 
		if ( request != null && request.getParameter("earningId") != null)
			selected = request.getParameter("earningId").trim();
		
		String result  = null;
		String sql =  " select P_Gain_ID, ltrim(rtrim(Name)) + ' (' + ltrim(rtrim(Value)) + ')' as description  from P_Gain "
                   + " where IsActive = 'Y' "
                   + " and exists ( "
                   + " select * "
                   + " from P_Gain_GainInfo inner join P_GainInfo "
                   + " on P_Gain_GainInfo.P_GainInfo_ID = P_GainInfo.P_GainInfo_ID "
                   + " where P_GainInfo.Value = 'MotifAbsen' "
                   + " and P_Gain_GainInfo.P_Gain_ID = P_Gain.P_Gain_ID "
                   + " and P_Gain_GainInfo.To_Consider = 'Y' "
                   + " ) "
                   + " order by  2 asc ";
;
		try
		{

			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();

			result  = "<select name=\"earningId\" id=\"earningId\"  onKeyPress=\"autoSelect(this);\">";
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
			System.out.println("* ERROR * Read earning " + sql + "Exception :" + e);
		}
		
		return result;
	}

%>