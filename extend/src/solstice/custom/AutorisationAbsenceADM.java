/*
 * Created on 2005-09-08
 */
package solstice.custom;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspWriter;

import org.compiere.util.DB;
import org.compiere.util.Env;

import solstice.model.P_Absence;
import solstice.model.P_Absence_Detail;
import solstice.model.P_Absence_Status;
import solstice.model.P_Employee_Calendar;
import solstice.model.P_Employee_NonBusinessDay;
import solstice.model.P_Particular_Sheet;
import solstice.model.P_Period;
import solstice.model.P_System_Parameters;

/**
 * @author frafor01
 *
 * Cette classe sert à valider la page autorisationAbsence/admconsult.jsp
 */
public class AutorisationAbsenceADM extends ValidationBase
{
    public static void main(String[] args)
    {
		org.compiere.Compiere.startupEnvironment(true);
        AutorisationAbsenceADM auto = new AutorisationAbsenceADM();
        auto.createAbsence(1000011);
    }
    
    public AutorisationAbsenceADM()
    {
        super();
    }

    /* (non-Javadoc)
     * @see solstice.custom.ValidationBase#doValidate()
     */
    protected String[] doValidate()
    {
        return null;
    }

    /* (non-Javadoc)
     * @see solstice.custom.ValidationBase#afterValidation(javax.servlet.jsp.JspWriter)
     */
    public boolean afterValidation(JspWriter out)
    {
        String action;
        String qualificatif;
        P_Absence absence;

        //2008-06-26 + connaitre l'usager qui a modifier ou approuver.
		IUserInfo userInfo = (IUserInfo)session.getAttribute("userInfo");
		Env.setContext(Env.getCtx(), "#AD_User_ID",  userInfo.getUserId());
        
        if((action = request.getParameter("action")) != null)
        {
            // Si l'utilisateur a choisi de supprimer, on doit faire passer
            // le statut de DSUP à DELE
            if(action.equals("delete"))
            {
                int absenceId = Integer.parseInt(request.getParameter("absenceId"));
                absence = new P_Absence(Env.getCtx(), absenceId, null);
                absence.setP_Absence_Status_ID(P_Absence_Status.getId("DELE"));
                
                // Si l'absence avait été créée dans le calendrier de l'employé, on
                // doit la supprimer
                if(absence.isAddedCalendar())
                    absence.setAddedCalendar(this.removeAbsence(absenceId));
                absence.save();
                qualificatif = "supprimé";
                envoyerMessage(absence,qualificatif);
                
                
            }
            // Si l'utilisateur a choisi de refuser une demande d'autorisation
            // d'absence, on doit faire passer le statut à REFU
            else if(action.equals("refus"))
            {
                int absenceId = Integer.parseInt(request.getParameter("absenceId"));
                absence = new P_Absence(Env.getCtx(), absenceId, null);
                absence.setP_Absence_Status_ID(P_Absence_Status.getId("REFU"));
                
                // Si l'absence avait été créée dans le calendrier de l'employé, on
                // doit la supprimer
                if(absence.isAddedCalendar())
                    absence.setAddedCalendar(this.removeAbsence(absenceId));
                absence.save();
                qualificatif = "refusé";
                envoyerMessage(absence,qualificatif);
            }
            // Si l'utilisateur a choisi d'accepter une demande d'autorisation
            // d'absence, on doit d'abord vérifier le rôle de l'utilisateur. Dans
            // le cas d'un signataire, on doit faire passer le statut à SIGN. Dans
            // le cas d'un délégué, on le fait passer à SIGD. Lorsqu'il s'agit
            // d'un approbateur, on le fait passer à APPR ou SIGD dépendemment
            // du niveau d'approbation
            else if(action.equals("accept"))
            {
                int absenceId = Integer.parseInt(request.getParameter("absenceId"));
                int absenceStatusId = 0;
                absence = new P_Absence(Env.getCtx(), absenceId, null);
                
                // S'il s'agit d'un administrateur ou d'un gestionnaire
                if(loggedUserHasPolicy(pageContext,"approve"))
                {
                    absenceStatusId = P_Absence_Status.getId("SIGN");
                    
                    // On doit maintenant ajouter cette absence au calendrier de
                    // l'employé si celle-ci n'exists pas déjà
                    if(!absence.isAddedCalendar())
                        absence.setAddedCalendar(this.createAbsence(absenceId));
                }
                // S'il s'agit d'une secrétaire ou d'un délégué
                else if(loggedUserHasPolicy(pageContext,"approveByDelegate"))
                {
                    absenceStatusId = P_Absence_Status.getId("SIGD");

                    // On doit maintenant ajouter cette absence au calendrier de
                    // l'employé si celle-ci n'exists pas déjà
                    if(!absence.isAddedCalendar())
                        absence.setAddedCalendar(this.createAbsence(absenceId));
            	}
                // Sinon, il s'agit d'un approbateur coordonnateur
                else if(loggedUserHasPolicy(pageContext,"approvePartially"))
                {
//                    // Si le coordonnateur a un niveau complet d'approbation, on fait passer le statut à SIGD
//                    if(getApprobation(Integer.parseInt(request.getParameter("absenceId"))) == 'C')
//                    {
//                        absenceStatusId = P_Absence_Status.getId("SIGD");
//
//                        // On doit maintenant ajouter cette absence au calendrier de
//                        // l'employé si celle-ci n'exists pas déjà
//                        if(!absence.isAddedCalendar())
//                            absence.setAddedCalendar(this.createAbsence(absenceId));
//                    }
//                    // Sinon on fait passer le statut à APPR
//                    else
//                    {
//                        absenceStatusId = P_Absence_Status.getId("APPR");
//                    }
                	absenceStatusId = P_Absence_Status.getId("APPR");
                }
                
                absence.setP_Absence_Status_ID(absenceStatusId);
                absence.save();
                qualificatif = "approuvé";
                envoyerMessage(absence,qualificatif);
            }
        }
        return true;
    }
    
	/**
	 * Envoie le e-mail et prépare le message
	 */
    private void envoyerMessage (P_Absence absence, String qualificatif)
    {
        String message = this.prepareMessage(absence.getP_Absence_ID(),qualificatif);
        try
        {
            EMailSender sender = new EMailSender(absence.getP_Employee_ID(), Env.getCtx());
            sender.sendEMail("Demande d'autorisation d'absence", message);
        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
        }
    }
	/**
	 * Prépare le e-mail selon les informations de la demande d'autorisation d'absence
	 * en qualifiant le message d'approuvé, refusé ou supprimé
	 */
	private String prepareMessage(int absenceId, String qualificatif)
	{
	    // On récupère le nom de l'employé et les dates d'absence
	    String sql
	    = "select employee.Name, min(absenceDetail.AbsenceDate) as StartDate, max(absenceDetail.AbsenceDate) as EndDate, employee.UserCode "
	        + " from P_Employee employee inner join (P_Absence absence inner join P_Absence_Detail absenceDetail"
	        + " on absence.P_Absence_ID = absenceDetail.P_Absence_ID)"
	        + " on employee.P_Employee_ID = absence.P_Employee_ID"
	        + " where absence.P_Absence_ID = " + absenceId
	        + " group by absence.P_Absence_ID, employee.Name, employee.UserCode";
	    
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
//Env.startBrowser(rs.getString(1) + "login.jsp?user=" + Env.getContext(Env.getCtx(), "#AD_User_Name") + "&redirection=%2FreleveEmploi%2Findex.jsp%3FemployeeId=" + m_employeeId);	    
//        return (getEmployeeName(((IUserInfo)session.getAttribute("userInfo")).getEmployeeId())+ " a "+qualificatif+" la demande d'autorisation d'absence. <br><br>"
  //              + " Absence demandée : " + startDate + " au " + endDate
    //            + " <br><br><a href=\"" + webAppUrl + "autorisationAbsence/index.jsp\">"
      //          + " Cliquer ici pour accéder à l'application d'autorisation d'absence</a>");
        return (getEmployeeName(((IUserInfo)session.getAttribute("userInfo")).getEmployeeId())+ " a "+qualificatif+" la demande d'autorisation d'absence. <br><br>"
                + " Absence demandée : " + startDate + " au " + endDate
                + " <br><br><a href=\"" + webAppUrl + "login.jsp?user="+strUserCode+"&redirection=%2FautorisationAbsence%2Findex.jsp\">"
                + " Cliquer ici pour accéder à l'application d'autorisation d'absence</a>");

	}
	
   
    /**
     * Cette méthode crée l'absence soit dans le calendrier de l'employé
     * ou soit dans le temps spécifique dépendemment de la date du congé.
     */
    private boolean createAbsence(int absenceId)
    {
        String sql
        = "select absence.P_Employee_ID, isnull(absence.Description, '') as Description, absence.P_Gain_ID, absenceDetail.P_Absence_Detail_ID, year(absenceDetail.AbsenceDate) as AbsenceYear, absenceDetail.AbsenceDate, absenceDetail.Duration"
            + " from P_Absence absence inner join P_Absence_Detail absenceDetail"
            + " on absence.P_Absence_ID = absenceDetail.P_Absence_ID"
            + " where absence.P_Absence_ID = " + absenceId
            + " order by absenceDetail.AbsenceDate";
        
        long currentPeriod = currentPeriodStartDate().getTime();
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            int lastYear = 0;
            int employeeCalendarId = 0;
            
            while(rs.next())
            {
                // On obtient d'abord le calendrier référant à la date de l'absence
                if(rs.getInt("AbsenceYear") != lastYear)
                {
                    employeeCalendarId = this.currentCalendarId(rs.getInt("P_Employee_ID"), rs.getInt("AbsenceYear"));
                    lastYear = rs.getInt("AbsenceYear");
                }
                
                // On doit maintenant vérifier la date de l'absence. Si celle-ci
                // appartient à la période courrante ou à une période future, on
                // ajoute l'absence dans le calendrier de l'employé. Sinon, on
                // l'ajoute dans le temps spécifique.
                P_Absence_Detail absenceDetail = new P_Absence_Detail(Env.getCtx(), rs.getInt("P_Absence_Detail_ID"), null);
                if(rs.getTimestamp("AbsenceDate").getTime() >= currentPeriod)
                {
                    String description = rs.getString("Description");
                    if(description.length() >= 60) description = description.substring(0, 60);
                    // Ajout au calendrier
                    int sourceID = this.setNonBusinessDay(rs.getInt("P_Absence_Detail_ID"), rs.getInt("P_Employee_ID"), rs.getTimestamp("AbsenceDate"), 
                            rs.getBigDecimal("Duration"), description, 
                            rs.getInt("P_Gain_ID"), employeeCalendarId);
                    if ( sourceID != 0 )
                    	absenceDetail.setSource_ID( sourceID );
                    absenceDetail.setSource("C");
                }
                else
                {
                    // Ajout au temps spécifique
                    absenceDetail.setSource_ID(
                            this.setParticularSheet(rs.getInt("P_Absence_Detail_ID"), rs.getInt("P_Employee_ID"), rs.getTimestamp("AbsenceDate"), 
                                    rs.getBigDecimal("Duration"), rs.getInt("P_Gain_ID")));
                    absenceDetail.setSource("T");
                }
                absenceDetail.save();
            }
            
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace(System.out);
            return false;
        }
        
        return true;
    }
    
    /**
     * Retourne la date de début de la période courrante
     */
    private Timestamp currentPeriodStartDate()
    {
        P_Period period = P_Period.getOpenPeriod(Env.getCtx(), null);
        return period.getStartDate();
    }
    
    /**
     * Cette méthode ajoute l'absence au calendrier de l'employé
     */
    private int setNonBusinessDay(int AbsenceDetailID, int employeeId, Timestamp absenceDate, BigDecimal duration, String description, int gainId, int employeeCalendarId)
    {
    	if ( duration.equals(Env.ZERO)  )
    		return 0;
    	
        // On crée une nouvelle entrée dans le calendrier
        P_Employee_NonBusinessDay employeeNonBusinessDay = new P_Employee_NonBusinessDay(Env.getCtx(), -1, null);
        employeeNonBusinessDay.setP_Employee_Calendar_ID(employeeCalendarId);
        employeeNonBusinessDay.setNonBusinessDate(absenceDate);
        employeeNonBusinessDay.setName(description);
        employeeNonBusinessDay.setDayQty(duration);
        employeeNonBusinessDay.setP_Gain_ID(gainId);
        employeeNonBusinessDay.setP_Absence_Detail_ID( AbsenceDetailID);
        employeeNonBusinessDay.save();
        
        return employeeNonBusinessDay.getP_Employee_NonBusinessDay_ID();
    }
    
    /**
     * Cette méthode ajoute l'absence au temps spécifique
     */
    private int setParticularSheet(int AbsenceDetailID, int employeeId, Timestamp absenceDate, BigDecimal duration, int gainId)
    {
        // On obtient d'abord la période du congé
        int periodId = getPeriodId(absenceDate);
        
        // On crée l'entrée dans la table P_Particular_Sheet
        P_Particular_Sheet particularSheet = new P_Particular_Sheet(Env.getCtx(), -1, null);
        particularSheet.setP_Employee_ID(employeeId);
        particularSheet.setDay(absenceDate);
        particularSheet.setDayQty(duration);
        particularSheet.setP_Gain_ID(gainId);
        particularSheet.setP_Period_ID(periodId);
        particularSheet.setP_Schedule_ID(1000000);
        particularSheet.setP_Absence_Detail_ID( AbsenceDetailID);
        particularSheet.save();

        return particularSheet.getP_Particular_Sheet_ID();
    }
    
    /**
     * Retourne la période englobant la date passée en paramètre
     */
    private int getPeriodId(Timestamp date)
    {
        int id = 0;
        String sql
        = "select P_Period_ID"
            + " from P_Period"
            + " where StartDate <= ?"
            + " and EndDate >= ?";
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            stmt.setTimestamp(1, date);
            stmt.setTimestamp(2, date);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next())
            {
                id = rs.getInt("P_Period_ID");
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

    /**
     * Cette méthode supprime l'absence
     * @return TRUE si l'absence figure toujours au calendrier
     */
    private boolean removeAbsence(int absenceId)
    {
        String sql
        = "select absence.P_Employee_ID, absence.P_Gain_ID, absenceDetail.AbsenceDate, absenceDetail.Duration, absenceDetail.Source, absenceDetail.Source_ID, absenceDetail.P_Absence_Detail_ID"
            + " from P_Absence absence inner join P_Absence_Detail absenceDetail"
            + " on absence.P_Absence_ID = absenceDetail.P_Absence_ID"
            + " where absence.P_Absence_ID = " + absenceId
            + " and absenceDetail.Source <> 'N'"
            + " and absenceDetail.Source_ID is not null";
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next())
            {
                // On vérifie la source de l'entrée
                if(rs.getString("Source").equals("C"))
                {
                    // On supprime l'entrée du calendrier
                    DB.executeUpdate("delete from P_Employee_NonBusinessDay where P_Employee_NonBusinessDay_ID = " + rs.getInt("Source_ID"), null);
                }
                else
                {
                    // On doit vérifier si l'absence existe toujours dans la table
                    // P_Particular_Sheet. Dans le cas contraire, on doit faire une
                    // entrée négative
                    if(DB.executeUpdate("delete from P_Particular_Sheet where P_Particular_Sheet_ID = " + rs.getInt("Source_ID"), null) == 0)
                    {
                        setParticularSheet(rs.getInt("P_Absence_Detail_ID"), rs.getInt("P_Employee_ID"), rs.getTimestamp("AbsenceDate"), rs.getBigDecimal("Duration").negate(), rs.getInt("P_Gain_ID"));
                    }
                }
            }
            
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace(System.out);
            return true;
        }
        return false;
    }
    
    /**
     * Cette méthode trouve le calendrier courrant de l'employé pour
     * la date de l'absence. Si celui-ci n'existe pas encore, on le
     * crée
     */
    private int currentCalendarId(int employeeId, int year)
    {
        int id = 0;
        String sql
        = "select P_Employee_Calendar_ID"
            + " from P_Employee_Calendar"
            + " where P_Employee_ID = " + employeeId
            + " and Year = '" + year + "'";
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            // Si le calendrier de l'employé existe, on concerve le id
            if(rs.next())
            {
                id = rs.getInt("P_Employee_Calendar_ID");
            }
            
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace(System.out);
        }
        
        // Si on n'a pas trouvé de calendrier pour l'employé, on doit
        // en créer un et retourner ce nouvel id
        if(id == 0)
        {
            P_Employee_Calendar employeeCalendar = new P_Employee_Calendar(Env.getCtx(), -1, null);
            employeeCalendar.setP_Employee_ID(employeeId);
            employeeCalendar.setYear(String.valueOf(year));
            employeeCalendar.save();
            return employeeCalendar.getP_Employee_Calendar_ID();
        }
        
        return id;
    }
    
	private String getEmployeeName (int noEmploye)
	{
	    // On récupère le nom de l'employé et les dates d'absence
	    String sql
	    = "select employee.FirstName, SurName"
	        + " from P_Employee employee"
	        + " where employee.p_employee_id = "+noEmploye;
	    String employeeName = null;
	    try
	    {
		    PreparedStatement stmt = DB.prepareStatement(sql, null);
		    ResultSet rs = stmt.executeQuery();
		    if(rs.next())
		    {
		        employeeName = rs.getString("FirstName")+" "+rs.getString("SurName");
		    }
		    
		    rs.close();
		    stmt.close();
		    
	    }
	    catch (SQLException e)
	    {
	        e.printStackTrace(System.out);
	    }
	    return employeeName;
	}

//    /**
//     * Retourne le niveau d'approbation du délégué pour le groupe d'occupation
//     * de l'employé référé par le numéro d'absence
//     */
//    private char getApprobation(int absenceId)
//    {
//        char approbationLevel = 0;
//        String sql
//        = "select P_Approbation_Level"
//            + " from P_Employee_Group_Approbation employeeGroupApprobation inner join P_Employee_Relation employeeRelation"
//            + " on employeeGroupApprobation.P_Employee_Relation_ID = employeeRelation.P_Employee_Relation_ID"
//            + " where employeeRelation.P_Employee_ID_In_Relation = " + ((IUserInfo)session.getAttribute("userInfo")).getEmployeeId()
//            + " and exists ("
//            + "   select 1"
//            + "   from P_Absence inner join (P_Employee inner join P_Distribution_Booklet"
//            + "   on P_Employee.P_Distribution_Booklet_ID = P_Distribution_Booklet.P_Distribution_Booklet_ID)"
//            + "   on P_Absence.P_Employee_ID = P_Employee.P_Employee_ID"
//            + "   where P_Distribution_Booklet.P_Employee_ID = employeeRelation.P_Employee_ID"
//            + "   and P_Absence.P_Absence_ID = " + absenceId
//            + " )";
//        
//        try
//        {
//            PreparedStatement stmt = DB.prepareStatement(sql, null);
//            ResultSet rs = stmt.executeQuery();
//            
//            if(rs.next())
//            {
//                approbationLevel = rs.getString("P_Approbation_Level").charAt(0);
//            }
//            
//            rs.close();
//            stmt.close();
//        }
//        catch(SQLException e)
//        {
//            e.printStackTrace(System.out);
//        }
//        
//        return approbationLevel;
//    }
    
    /**
     * Permet d'obtenir le cas du switch case sql qui permet d'approuver
     * @return le sql
     */
    public static String peutApprouver (PageContext pageContext)
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
    	else if (loggedUserHasPolicy(pageContext,"approveByDelegate"))
    	{
    		sql += " when 'ATTE' then absence.P_Absence_ID ";
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
    public static String peutRefuser (PageContext pageContext)
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
    public static String peutSupprimer (PageContext pageContext)
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
    
   public static String getDistributionSQL(PageContext pageContext,
		   String[] selectedFields, boolean orderByValue)
   {
	   return getUserInfo(pageContext).getDistributionSQL(selectedFields,orderByValue);
   }
  
   public static String getEmployeeSQL(PageContext pageContext)
   {
	   return getUserInfo(pageContext).getEmployeeSQL();
   }
    
}
