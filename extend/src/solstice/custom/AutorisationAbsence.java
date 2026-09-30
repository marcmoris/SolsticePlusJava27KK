/*
 * Created on Aug 30, 2005
 */
package solstice.custom;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.compiere.util.DB;
import org.compiere.util.Env;

import solstice.model.P_Absence;
import solstice.model.P_Absence_Detail;
import solstice.model.P_Absence_Status;
import solstice.process.PgiUtil;

public class AutorisationAbsence extends ValidationBase
{
    public static boolean checkCondition (PageContext pageContext, String condition)
    {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        return request.getParameter(condition) != null && !request.getParameter(condition).equals("");
    } 
    
    public static boolean checkConditionStartup (PageContext pageContext, String condition)
    {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        if(condition.equals("startup"))
        {
            return (request.getParameter("action") == null
                    || request.getParameter("action").equals(""));
        }
        return request.getParameter(condition) != null && !request.getParameter(condition).equals("");
    }
    
    public static boolean showAbsenceCalendar(PageContext pageContext)
    {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        return (request.getParameter("showAbsenceCalendar") != null 
                && request.getParameter("showAbsenceCalendar").equals("true"));
    }

	protected String[] doValidate()
	{
	    return null;
	}
	
	/*
	private int getManager(int employeeId)
	{
	    int id = 0;
        String sql
        = "select P_Distribution_Booklet.P_Employee_ID, P_Distribution_Booklet.P_Employee_Coordinating_ID"
            + " from P_Employee inner join P_Distribution_Booklet"
            + " on P_Employee.P_Distribution_Booklet_ID = P_Distribution_Booklet.P_Distribution_Booklet_ID"
            + " where P_Employee.P_Employee_ID = " + employeeId;
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next())
            {
            	// S'il y a un coordonnateur alors le email est envoyé au coordonnateur. sinon au signataire.
            	if  ( rs.getInt("P_Employee_Coordinating_ID") != 0 && employeeId != rs.getInt("P_Employee_Coordinating_ID") )
           			id =rs.getInt("P_Employee_Coordinating_ID");
            	else
            		id = rs.getInt("P_Employee_ID");
            }
            
            rs.close();
            stmt.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace(System.out);
        }
        
        return id;
	}
	*/
	
	/**
	 * Gestion des actions dans l'interface principal des autorisations d'absence
	 */
	public boolean afterValidation(JspWriter out)
	{
	    String action;
	    if((action = request.getParameter("action")) != null)
	    {
	        // Si l'utilisateur a choisi de sauvegarder, récupère les journées
	        // de congé durant la période sélectionnée
	        if(action.equals("save"))
	        {
	            P_Absence absence = sauvegarderAbsenceSaisie();
//	            if ( absence == null )
//	            	return false;
	            
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
//                        response.sendRedirect("remplir.jsp");
                    }
	            }
	            catch (IOException e)
	            {
	                e.printStackTrace(System.out);
	            }
	            return false;
	        }
	        // Lors d'une demande d'approbation, on doit envoyer un e-mail au gestionnaire et
	        // faire passer le statut à ATTE
	        else if(action.equals("askApprob"))
	        {
	        	demanderApprobation(Integer.parseInt(request.getParameter("absenceId")));
	        }
	        else if (action.equals("askApprobAndSave"))
	        {
	        	P_Absence absence = sauvegarderAbsenceSaisie();
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
	                P_Absence absence = new P_Absence(Env.getCtx(), Integer.parseInt(request.getParameter("absenceId")), null);
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
	    return true;
	}
	
	/**
	 * Demande l'approbation et charger l'absence
	 */
	private void demanderApprobation(int absenceId)
	{
        P_Absence absence = new P_Absence(Env.getCtx(), absenceId, null);
        this.demanderApprobation(absence);
 	}
	
	/**
	 * Demande l'approbation
	 */
	private void demanderApprobation(P_Absence absence)
	{
        absence.setP_Absence_Status_ID(P_Absence_Status.getId("ATTE"));
        
        String message = absence.prepareMessage( );
        int managerId = absence.getManager( );
        
        try
        {
            EMailSender sender = new EMailSender(managerId, Env.getCtx());
            sender.sendEMail("Demande d'autorisation d'absence", message);
        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
        }
        
        // On sauvegarde le nouveau statut
        absence.save();

	}

	private void demandeSuppression(P_Absence absence)
	{
        String message = absence.prepareMessage( );
        int managerId = absence.getManager();
        
        try
        {
            EMailSender sender = new EMailSender(managerId, Env.getCtx());
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
	private P_Absence sauvegarderAbsenceSaisie ()
	{
        // On crée d'abord l'absence
		System.out.println("debut sauvegarderAbsenceSaisie");
        P_Absence absence = new P_Absence(Env.getCtx(), Integer.parseInt(request.getParameter("absenceId")), null);
        absence.setP_Employee_ID(((IUserInfo)session.getAttribute("userInfo")).getEmployeeId());
        if ( request.getParameter("activityId").length() != 0)
        	absence.setC_Activity_ID(Integer.parseInt(request.getParameter("activityId")));
        if ( request.getParameter("occupationGroupId").length() != 0)
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
                if(request.getParameter(prefix + "_duration") != null && !request.getParameter(prefix + "_duration").equals(""))
                {
                    //BigDecimal duration = new BigDecimal(Double.parseDouble(request.getParameter(prefix + "_duration"))).setScale(2,BigDecimal.ROUND_HALF_UP);
    				BigDecimal duration = new BigDecimal("0");
    		 		String strDuration = request.getParameter(prefix + "_duration");
    		 		System.out.println("strDuration: " + strDuration);
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
                          		System.out.println("duration: " + duration.toString());
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
    				else
    					duration = new BigDecimal(0);
                   
    				if ( duration.compareTo( Env.ZERO) != 0 )
    				{
                        // On crée un enregistrement pour la journée
                        //P_Absence_Detail absenceDetail = new P_Absence_Detail(Env.getCtx(), getAbsenceDetailId(current, absence.getP_Absence_ID()), null);
        				P_Absence_Detail absenceDetail = new P_Absence_Detail(Env.getCtx(), -1, null);
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
        System.out.println("fin sauvegarderAbsenceSaisie");
        return absence;
	}
	
	
	/**
	 * Prépare le e-mail selon les informations de la demande d'autorisation d'absence
	 */
	/*
	private String prepareMessage(P_Absence absence)
	{
	    // On récupère le nom de l'employé et les dates d'absence
	    String sql
	    = "select (employee.FirstName+' '+employee.SurName) as Nom, min(absenceDetail.AbsenceDate) as StartDate, max(absenceDetail.AbsenceDate) as EndDate, employee.UserCode "
	        + " from P_Employee employee inner join (P_Absence absence inner join P_Absence_Detail absenceDetail"
	        + " on absence.P_Absence_ID = absenceDetail.P_Absence_ID)"
	        + " on employee.P_Employee_ID = absence.P_Employee_ID"
	        + " where absence.P_Absence_ID = " + absence.getP_Absence_ID()
	        + " group by absence.P_Absence_ID, (employee.FirstName+' '+employee.SurName), employee.UserCode ";
	    
	    String employeeName = null;
	    String startDate = null;
	    String endDate = null;
	    String webAppUrl = null;
	    String strUserCode = "";
	    
	    try
	    {
		    PreparedStatement stmt = DB.prepareStatement(sql, null);
		    ResultSet rs = stmt.executeQuery();
		    
		    if(rs.next())
		    {
		        employeeName = rs.getString("Nom");
		        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		        startDate = format.format(rs.getTimestamp("StartDate"));
		        endDate = format.format(rs.getTimestamp("EndDate"));
		        strUserCode = rs.getString("UserCode");
		    }
		    
		    rs.close();
		    stmt.close();
		    
		    webAppUrl = (String)P_System_Parameters.getParameterValue("WEBAPPURL");
	    }
	    catch (SQLException e)
	    {
	        e.printStackTrace(System.out);
	    }
	    
	    P_Employee manager = P_Employee.get( Env.getCtx(), this.getManager(absence.getP_Employee_ID()), null );

	    if ( absence.getP_Absence_Status_ID() == P_Absence_Status.getId("DSUP") )
	    {
	        return (employeeName + " a effectué(e) une demande de suppression d'absence. <br><br>"
	                + " Absence demandée : " + startDate + " au " + endDate
	                + " <br><br>Veuillez accéder à l'application d'autorisation des absences pour approuver ou refuser cette demande."
	                + " <br><br><a href=\"" + webAppUrl + "login.jsp?user="+manager.getUserCode()+"&redirection=%2FautorisationAbsence%2Findex.jsp\">"
	                + " Cliquer ici pour accéder à l'application d'autorisation d'absence</a>");
    	
	    }

//        return (employeeName + " a effectué(e) une demande d'autorisation d'absence. <br><br>"
//                + " Absence demandée : " + startDate + " au " + endDate
//                + " <br><br>Veuillez accéder à l'application d'autorisation des absences pour approuver ou refuser cette demande."
//                + " <br><br><a href=\"" + webAppUrl + "autorisationAbsence/index.jsp\">"
//                + " Cliquer ici pour accéder à l'application d'autorisation d'absence</a>");
        return (employeeName + " a effectué(e) une demande d'autorisation d'absence. <br><br>"
                + " Absence demandée : " + startDate + " au " + endDate
                + " <br><br>Veuillez accéder à l'application d'autorisation des absences pour approuver ou refuser cette demande."
                + " <br><br><a href=\"" + webAppUrl + "login.jsp?user="+manager.getUserCode()+"&redirection=%2FautorisationAbsence%2Findex.jsp\">"
                + " Cliquer ici pour accéder à l'application d'autorisation d'absence</a>");

	}

*/
	/**
	 * On vérifie s'il existe déjà un enregistrement pour cette journée d'absence. Si
	 * oui, on retourne l'id, sinon, on retourne -1.
	 */
	private int getAbsenceDetailId(Calendar date, int absenceId)
	{
	    int id = -1;
	    String sql
	    = "select P_Absence_Detail_ID"
	        + " from P_Absence_Detail"
	        + " where P_Absence_ID = " + absenceId
	        + " and AbsenceDate = ?";
	    
	    
	    try
	    {
	        PreparedStatement stmt = DB.prepareStatement(sql, null);
	        stmt.setTimestamp(1, new Timestamp(date.getTimeInMillis()));
	        ResultSet rs = stmt.executeQuery();
	        
	        if(rs.next())
	        {
	            id = rs.getInt("P_Absence_Detail_ID");
	        }
	        
	        rs.close();
	        stmt.close();
	    }
	    catch (SQLException e)
	    {
	        e.printStackTrace(System.out);
	    }
	    
	    return id;
	}
}
