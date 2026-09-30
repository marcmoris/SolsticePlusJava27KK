<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.math.BigDecimal" %>

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

<%@ page import="solstice.custom.EMailSender" %>

<%@ page import="solstice.utils.PgiUtil" %>
<%@ page import="java.io.*" %>
<%@ page import="javax.servlet.*" %>
<%@ page import="javax.servlet.http.*" %>



<%@ page extends="solstice.web.PageWebSolstice" %>

<%!	

	
	Timestamp startDate = null;
	Timestamp endDate = null;
	String status = null;
	
	boolean isSOTrx = true;

    
    
    /**
     * Cette méthode crée une table de hachage pour le relevé d'emploi passé en paramètre
     */
    private void loadRecord(Timestamp startDate, Timestamp endDate, String status)
    {
    	
		String sql = "select absence.P_Absence_ID, " 
				   + " count(*) NbrDay, "
			       + " rtrim(gain.Value) + ' - ' + gain.Name as Motif, min(absenceDetail.AbsenceDate) as StartDate, " 
                   + " max(absenceDetail.AbsenceDate) as EndDate, sum(absenceDetail.Duration) as Duration, absenceStatus.Name as Status, "
                   + " case absenceStatus.Value "
                   + " when 'INIT' then absence.P_Absence_ID "
                   + " else null "
                   + " end as AskApprob, absence.P_Absence_ID as Edit, "
                   + " case absenceStatus.Value "
                   + "  when 'INIT' then absence.P_Absence_ID "
                   + "  when 'ATTE' then absence.P_Absence_ID "
                   + "  when 'APPR' then absence.P_Absence_ID "
                   + "  when 'SIGN' then absence.P_Absence_ID "
                   + "  when 'SIGD' then absence.P_Absence_ID "
                   + "  else null "
                   + " end as Del "
                   + " from ((P_Absence absence inner join P_Absence_Detail absenceDetail "
                   + " on absence.P_Absence_ID = absenceDetail.P_Absence_ID) inner join P_Gain gain "
                   + " on absence.P_Gain_ID = gain.P_Gain_ID) inner join P_Absence_Status absenceStatus "
                   + " on absence.P_Absence_Status_ID = absenceStatus.P_Absence_Status_ID "
              ;

		sql += " Where isnull( absenceDetail.duration, 0) <> 0 and absence.P_Employee_ID = " + ((solstice.custom.IUserInfo)session.getAttribute("userInfo")).getEmployeeId();
		
		if ( status != null && status.trim().length() != 0)
		{
			sql += " and absenceStatus.Value in ( " + status + " )";
		}

		sql += " group By absence.P_Absence_ID, absenceStatus.Value, absenceStatus.Name, gain.Value, gain.Name ";

	    if ( startDate != null ) 
	    {
	    	sql += " having max(absenceDetail.AbsenceDate) >= " + DB.TO_DATE(startDate);
	    	if ( endDate != null )
	            sql += " and min(absenceDetail.AbsenceDate) <= " + DB.TO_DATE(endDate) ;
	    }
	    else if ( endDate != null )
	      sql += " having min(absenceDetail.AbsenceDate) <= " + DB.TO_DATE(endDate) ;

		sql += " order By min(absenceDetail.AbsenceDate) desc " ;

	    try
		{

			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			boolean alt = false;
			P_Absence Absence;
			while (rs.next ())
			{	
				Absence = P_Absence.get( ctx, rs.getInt("P_Absence_ID"), null);
				tpl.assign("P_Absence_ID" , rs.getInt("P_Absence_ID"));
		        tpl.assign("Reason", rs.getString("Motif"));
		        tpl.assign("StartDate", rs.getTimestamp("StartDate"));
		        tpl.assign("EndDate", rs.getTimestamp("EndDate"));
		        
		        tpl.assign("Time", Absence.displayDuration() );
		        String detail = Absence.displayDurationDetail();
		        if ( detail != null )
		        	tpl.assign("MouseOver", "onMouseOver=\"overlib('" + detail + "'); return true;\" onMouseOut=\"nd(); return true;\" ");
		        else
		        	tpl.assign("MouseOver", "" );
		        
	        	tpl.assign("Status", rs.getString("Status"));
		        if ( rs.getInt("AskApprob") != 0 )
		        	tpl.assign("APPROVE",   "<a href=\"javascript:askApprob(" + rs.getInt("AskApprob") + ")\"> <img src=\"images/approbask.gif\" border=\"0\"> </a> " );
		        else
		        	tpl.assign("APPROVE",   "<img src=\"images/approbaskg.gif\" border=\"0\"> " );

		        if ( rs.getInt("Edit") != 0 )
		        	tpl.assign("EDIT", "<a href=edit.jsp?absenceId=" + rs.getInt("Edit") + " target=\"_blank\" > <img src=\"images/edit.gif\" border=\"0\"> </a>" );
		        else
		        	tpl.assign("EDIT", "<img src=\"images/edit.gif\" border=\"0\">" );

		        if ( rs.getInt("Del") != 0)
		        	tpl.assign("DELETE", "<a href=\"javascript:deleteAbsence(" + rs.getInt("Del") + ")\"> <img src=\"images/delete.gif\" border=\"0\"></a>" );
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

		this.setPageId("consult");
		this.setSecurityCheck( true );
		this.setTemplate("AutorisationAbsenceList.jtpl");

		if ( ! SecurityCheck() )
		{
			response.sendError(403); 
		}
		
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
	          status = request.getParameter("status").trim();
		else status = "'INIT', 'ATTE', 'REFU', 'APPR'";
;
		
		String action = request.getParameter("action");
		if(action != null)
		{	
	        // Si l'utilisateur a choisi de sauvegarder, récupère les journées
	        // de congé durant la période sélectionnée
	        if(action.equals("save"))
	        {
	            P_Absence absence = sauvegarderAbsenceSaisie( request );
	            try
	            {
                  if(request.getParameter("source") != null
                          && request.getParameter("source").equals("edition"))
                  {
                      response.sendRedirect("edit.jsp?absenceId=" + absence.getP_Absence_ID());
                  }
                  else
                  {
                  	response.sendRedirect("consult.jsp");
//                      response.sendRedirect("remplir.jsp");
                  }
	            }
	            catch (IOException e)
	            {
	                e.printStackTrace(System.out);
	            }
	        }
	        // Lors d'une demande d'approbation, on doit envoyer un e-mail au gestionnaire et
	        // faire passer le statut à ATTE
	        else if(action.equals("askApprob"))
	        {
	        	demanderApprobation(Integer.parseInt(request.getParameter("absenceId")));
	        }
	        else if (action.equals("askApprobAndSave"))
	        {
	        	P_Absence absence = sauvegarderAbsenceSaisie( request );
	        	demanderApprobation(absence);
	            try
	            {
                 if(request.getParameter("source") != null
                         && request.getParameter("source").equals("edition"))
                 {
                    response.sendRedirect("edit.jsp?absenceId=" + absence.getP_Absence_ID());
                 }
                 else
                 {
                 	response.sendRedirect("consult.jsp");
//	                  response.sendRedirect("remplir.jsp");
                 }
	            }
	            catch (IOException e)
	            {
	               e.printStackTrace(System.out);
	            }
	        }
          // Lorsque l'utilisateur demande de supprimer une autorisation d'absence, on 
          // doit d'abord vérifier le statut de cette demande. Si celui-ci est à INIT ou ATTE,
          // on change simplement le statut pour DELE. Cependant, si le statut est à
          // APPR, SIGD ou  SIGN, on doit faire passer le statut à DSUP
	        else if(action.equals("delete"))
	        {
	            // On récupère d'abord l'absence et on évalue le statut actuel
	            if(request.getParameter("absenceId") != null && !request.getParameter("absenceId").equals(""))
	            {
	                P_Absence absence = new P_Absence(ctx, Integer.parseInt(request.getParameter("absenceId")), null);
	                String absenceStatus = P_Absence_Status.getValue(absence.getP_Absence_Status_ID());
	                if (   absenceStatus.equals("INIT") 
	                	|| absenceStatus.equals("ATTE"))
	                {
              	    absence.setP_Absence_Status_ID(P_Absence_Status.getId("DELE"));
	                }
	                else if(   absenceStatus.equals("APPR")
	                        || absenceStatus.equals("SIGN")
	                        || absenceStatus.equals("SIGD"))
	                {
              	    absence.setP_Absence_Status_ID(P_Absence_Status.getId("DSUP"));
              	    demandeSuppression( absence );
	                }
	                
	                // On sauvegarde le nouveau statut
	                absence.save();
	            }
	        }
		}

		P_Period Period = P_Period.getOpenPeriodWithPaymentGroup(ctx, this.getEmployeeConnected().getP_Payment_Group_ID(), null);
		if ( firstLoad )
		{
			checkPeriod = false;
			checkStartDate = false;
			checkEndDate = false;
			startDate = Period.getStartDate();
			
		}
		else 
		{
            if ( request.getParameter("cStartDate") != null )
			{
				  checkStartDate = (request.getParameter("cStartDate").trim().length() != 0);
		   		  if ( checkStartDate && ( request.getParameter("startDate") == null || request.getParameter("startDate").trim().length() == 0 ) )
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

		tpl.assign("TITLE","SIQ - Consulter la liste de vos absences" );
		tpl.assign("SUBTITLE", "Autorisation d'absence" );  // Record of Employment
		tpl.assign("PAGETITLE", "Consulter la liste de vos absences" );

		tpl.assign("LabelEmployeeName", "Nom de l'employé");
		tpl.assign("LabelEmployeeNo", "No. de l'employé");
		tpl.assign("LabelActivity", "Unité administrative");
		tpl.assign("LabelReason", "Motif de l'absence" ); 	
		tpl.assign("LabelStartDate", "Date de début" );	
		tpl.assign("LabelEndDate", "Date de fin" );
		tpl.assign("LabelTime", "Durée (h)" );	
		tpl.assign("LabelStatus", "Statut" 	);
		tpl.assign("LabelApprove", "Pour<br /> appro." );
		tpl.assign("LabelEdit", "Édit." );
		tpl.assign("LabelDelete", "Suppr." );


		
		P_Assignment Assignment = P_Assignment.get( ctx, this.getEmployeeConnected().GetAssignmentPrincipal(), null);
		P_Post Post = P_Post.get(ctx, Assignment.getP_Post_ID(), null);
		MActivity Activity = new MActivity( ctx, Post.getC_Activity_ID(), null);
		tpl.assign("EmployeeName", this.getEmployeeConnected().getName());
		tpl.assign("EmployeeNo", this.getEmployeeConnected().getValue());
		if ( Post.getC_Activity_ID() != 0)
			tpl.assign("Activity",  Activity.getValue() + " - " + Activity.getName() );

// 2008.10.20 add
		else
			tpl.assign("Activity",  "N/A" );
			
		tpl.assign("LabelFilter","Filtres");

		tpl.assign( "SelectStatus", this.ComboBoxStatus() );
		tpl.assign( "SelectDistribution" ,ComboBoxDistributionBooklet() );  

		tpl.assign( "LabelPeriod", "Période" 	);
		tpl.assign( "SelectPeriod" ,ComboBoxPeriod() );

		if ( checkPeriod )
		{
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
	
		this.loadRecord(startDate, endDate, status );
		
		tpl.parse("main");
		return (tpl.out());
	}
	
	/**
	 * Demande l'approbation et charger l'absence
	 */
	private void demanderApprobation(int absenceId)
	{
        P_Absence absence = new P_Absence(ctx, absenceId, null);
        this.demanderApprobation(absence);
 	}
	
	/**
	 * Demande l'approbation
	 */
	private void demanderApprobation(P_Absence absence)
	{
        absence.setP_Absence_Status_ID(P_Absence_Status.getId("ATTE"));
        
/*        String message = absence.prepareMessage( );
        int managerId = absence.getManager();
        
        try
        {
            EMailSender sender = new EMailSender(managerId, ctx);
            sender.sendEMail("Demande d'autorisation d'absence", message);

            //2009.03.17
            sender.sendEMailSubstituteManager(Env.getCtx(), "Demande d'autorisation d'absence", message, absence.getManagerDistribution(), absence.getStartDate(), absence.getEndDate());

        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
        }
  */      
        // On sauvegarde le nouveau statut
        absence.save();

	}

	private void demandeSuppression(P_Absence absence)
	{
        String message = absence.prepareMessage( );
        int managerId = absence.getManager();
        
        try
        {
            EMailSender sender = new EMailSender(managerId, ctx);
            sender.sendEMail("Demande de suppression", message);
        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
        }

	}

	/**
	 * Permet de sauvegarder l'absence couramment saisie
	 */
	private P_Absence sauvegarderAbsenceSaisie ( HttpServletRequest request )
	{
        // On crée d'abord l'absence
        P_Absence absence = new P_Absence(ctx, Integer.parseInt(request.getParameter("absenceId")), null);
        absence.setP_Employee_ID(((IUserInfo)session.getAttribute("userInfo")).getEmployeeId());
        absence.setC_Activity_ID(Integer.parseInt(request.getParameter("activityId")));
        absence.setP_Occupation_Group_ID(Integer.parseInt(request.getParameter("occupationGroupId")));
        absence.setP_Job_Type_ID(Integer.parseInt(request.getParameter("jobTypeId")));
        absence.setDescription(request.getParameter("detailDemande"));
        absence.setP_Gain_ID(Integer.parseInt(request.getParameter("gainId")));
        absence.setP_Absence_Status_ID(P_Absence_Status.getId("INIT"));
        boolean saveResult = absence.save();
        
        BigDecimal total = Env.ZERO;
        
        // Le reste du traitement s'applique seulement si on a fait afficher le calendrier
        if("true".equals(request.getParameter("calendarShown")))
        {
        
            Calendar current = PgiUtil.stringToDateV2(request.getParameter("startDate"));
            Calendar endDate = PgiUtil.stringToDateV2(request.getParameter("endDate"));
            
            //on supprime les anciens details avant de sauvegarder les nouveaux
            String strSqlDelete = "DELETE FROM P_Absence_Detail WHERE P_Absence_ID = "+Integer.parseInt(request.getParameter("absenceId"));
            DB.executeUpdate(strSqlDelete, null);
            // On passe chacune des journées de la période de congé demandée
            while(current.getTimeInMillis() <= endDate.getTimeInMillis())
            {
                String prefix 
                = "d" + String.valueOf(current.get(Calendar.YEAR))
                        + String.valueOf(current.get(Calendar.MONTH))
                        + String.valueOf(current.get(Calendar.DAY_OF_MONTH));
                
                // On regarde s'il existe une entrée pour la journée courrante
                if(request.getParameter(prefix + "_duration") != null)
                {
                    //BigDecimal duration = new BigDecimal(Double.parseDouble(request.getParameter(prefix + "_duration"))).setScale(2,BigDecimal.ROUND_HALF_UP);
    				BigDecimal duration = new BigDecimal("0");
    		 		String strDuration = request.getParameter(prefix + "_duration");
    				if(request.getParameter("timeFormat") != null && request.getParameter("timeFormat") != "")
    				{
    					String strTimeFormat = request.getParameter("timeFormat");
    					if(strTimeFormat.indexOf(":") != -1)
    					{
    						try
    						{
    							int indexOfColon = strDuration.indexOf(":");
    							String firstPart;
    							String secondPart;
    							if ( indexOfColon != 0 )
    							{
        							firstPart = strDuration.substring(0, indexOfColon);
        							secondPart = strDuration.substring(indexOfColon+1, strDuration.length());
    							}
    							else
    							{
        							firstPart = strDuration.substring(0, strDuration.length());
        							secondPart = "00";
    								
    							}
    
    							BigDecimal bdSecondPart = new BigDecimal(secondPart);
            	                bdSecondPart = bdSecondPart.multiply(new BigDecimal("100")).divide(new BigDecimal("60"), 0, BigDecimal.ROUND_HALF_UP);
    
            	                secondPart = bdSecondPart.toString();
    
            	                if(secondPart.length() == 0)
            	                	secondPart = "00";
            	                else if(secondPart.length() == 1)
            	                	secondPart = "0"+secondPart;
            	                if(firstPart.length() == 0)
            	                	firstPart = "0";
                          		strDuration = firstPart + "." + secondPart;
                          		duration = new BigDecimal(strDuration);
    						}
    						catch(Exception e)
    						{
    							e.printStackTrace();
    						}
    					}
    					else
    					{
    						duration = new BigDecimal(strDuration);
    					}
    				}
                   
    				if ( duration.compareTo( Env.ZERO) != 0 )
    				{
                        // On crée un enregistrement pour la journée
                        //P_Absence_Detail absenceDetail = new P_Absence_Detail(ctx, getAbsenceDetailId(current, absence.getP_Absence_ID()), null);
        				P_Absence_Detail absenceDetail = new P_Absence_Detail(ctx, -1, null);
                        absenceDetail.setP_Absence_ID(absence.getP_Absence_ID());
                        absenceDetail.setAbsenceDate(new Timestamp(current.getTimeInMillis()));
                        absenceDetail.setBegining_Hour(
                                request.getParameter(prefix + "_startHour") != null
                                && !request.getParameter(prefix + "_startHour").equals("")
                                ? request.getParameter(prefix + "_startHour") : null);
                        absenceDetail.setEnding_Hour(
                                request.getParameter(prefix + "_endHour") != null
                                && !request.getParameter(prefix + "_endHour").equals("")
                                ? request.getParameter(prefix + "_endHour") : null);
                        absenceDetail.setDuration(duration);
        
                        total = total.add(duration);
                        
                        if(!absenceDetail.save())
                        {
                            System.out.println("save failed" + request.getParameter(prefix + "_duration") + " " + duration.toString());
                        }
    					
    				}
    				
                }
                
                current.add(Calendar.DAY_OF_MONTH, 1);
            }
        
        }
        // Si c'est la création d'un nouveau record. et qu'il n'y a pas d'heures de saisie alors l'on détruit l'absence.
        // Si c'est une édition on ne permet pas de mettre 0 heures dans le détail.
        if ( total.equals( Env.ZERO ) && ! ( request.getParameter("source") != null && request.getParameter("source").equals("edition")))
        {
        	absence.delete(true);
            System.out.println("save failed" + " Total hours : " + total.toString());
        	return null;
        }
        return absence;
	}
	
	

	/*
	    *  Filtres des différents statuts possible 
	    */ 
	   private String ComboBoxStatus()
	   {
			String selected = ""; 
			if ( request != null && request.getParameter("status") != null)
				selected = request.getParameter("status").trim();
			else
				selected = "'INIT', 'ATTE', 'REFU'";

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
				if ( selected.equals("'ATTE', 'DSUP'") )
					sel1 = "selected";
				if ( selected.equals("'INIT', 'ATTE', 'DSUP', 'APPR', 'SIGN', 'SIGD', 'TRSF', 'REFU', 'DELE', 'TRPR'") )
					sel2 = "selected";
				if ( selected.equals("'INIT', 'ATTE', 'DSUP'") )
					sel3 = "selected";
				if ( selected.equals("'APPR', 'SIGN', 'SIGD', 'TRSF', 'TRPR'") )
					sel4 = "selected";
				if ( selected.equals("'INIT', 'ATTE', 'REFU'") )
					sel5 = "selected";
				
				result += "<option value=\"'ATTE', 'DSUP'\"" + sel1 + " >En attente d'approbation</option>\n";
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

%>