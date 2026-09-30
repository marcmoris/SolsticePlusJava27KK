/*
 * Created on 2005-09-03
 */
package solstice.custom;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.jsp.JspWriter;

import org.compiere.util.DB;
import org.compiere.util.Env;

import solstice.model.P_Absence_Significant;
import solstice.model.P_Employee;
import solstice.model.P_System_Parameters;

/**
 * @author frafor01
 *
 * Validation du formulaire autorisationAbsence/addabssig.jsp et autorisationAbsence/listabssig.jsp
 */
public class AbsenceSignifiee extends ValidationBase
{

    public AbsenceSignifiee()
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
        if((action = request.getParameter("action")) != null)
        {
	        // Si on a cliqué sur le boutton sauvegarder, on doit créer un enregistrement
	        // dans la table P_Absence_Significant pour sauvegarder les informations du formulaire
	        if(action.equals("save"))
	        {
	            P_Absence_Significant absenceSignificant = new P_Absence_Significant(Env.getCtx(), -1, null);
	            absenceSignificant.setADATE(createTimestamp(request.getParameter("absenceDate")));
	            absenceSignificant.setP_Employee_ID(Integer.parseInt(request.getParameter("employeeId")));
	            if(request.getParameter("remarque") != null && !request.getParameter("remarque").equals(""))
	            {
	                absenceSignificant.setDescription(request.getParameter("remarque").replaceAll("'", "\\'"));
	            }
	            absenceSignificant.setDayTime(request.getParameter("absencePeriod"));
	            
	            if(request.getParameter("absencePeriod").equals("Z"))
	            {
	                absenceSignificant.setBegining_Hour(request.getParameter("startHour"));
	                absenceSignificant.setEnding_Hour(request.getParameter("endHour"));
	            }
	            
	            absenceSignificant.save();
	            envoyerCourriel(getManager(absenceSignificant.getP_Employee_ID()),prepareMessageGestionnaire(absenceSignificant, Integer.parseInt(request.getParameter("employeeId"))),"Absence signifiée");
	            envoyerCourriel(absenceSignificant.getP_Employee_ID(),prepareMessageEmployee(absenceSignificant, Integer.parseInt(request.getParameter("employeeId"))),"Absence signifiée");
		        
	            // On redirige l'utilisateur vers la page d'administration
		        try
		        {
		            this.response.sendRedirect("admabssig.jsp");
		        }
		        catch (IOException e)
		        {
		            e.printStackTrace(System.out);
		        }
		        return false;
	        }
	        // Si on veut supprimer un enregistrement, on récupère le id et on effectue la suppression
	        else if(action.equals("delete"))
	        {
	            String sql 
	            = "delete from P_Absence_Significant"
	                + " where P_Absence_Significant_ID = " + request.getParameter("toRemove");
	            
	            DB.executeUpdate(sql, null);
	        }
        }
        return true;
    }
    
	/**
	 * Envoi d'un courriel
	 */
	private void envoyerCourriel(int receveur, String message, String sujet)
	{
        
        try
        {
            EMailSender sender = new EMailSender(receveur, Env.getCtx());
            sender.sendEMail(sujet, message);
        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
        }
	}

    
	/**
	 * Prépare le e-mail selon les informations de l'absence signifié
	 * pour l'employé
	 */
	private String prepareMessageEmployee(P_Absence_Significant absence, int iEmployeeId)
	{
	    // On récupère les dates d'absence
	    String suiteMessage = null;

	    P_Employee employee = P_Employee.get(Env.getCtx(), iEmployeeId, null);
	    String webAppUrl = null;
	    try
	    {
		    webAppUrl = (String)P_System_Parameters.getParameterValue("WEBAPPURL");
	    }
	    catch (SQLException e)
	    {
	        e.printStackTrace(System.out);
	    }
	    
	    //On formate le message dépendant du type d'absence signifiée
	    if (absence.getDayTime().equals("A"))
	    {
	    	suiteMessage = "pour l'avant-midi du";
	    }
	    else if (absence.getDayTime().equals("J"))
	    {
	    	suiteMessage = "pour la journée du";
	    }
	    else if (absence.getDayTime().equals("P"))
	    {
	    	suiteMessage = "pour l'après-midi du";
	    }
	    else if (absence.getDayTime().equals("Z"))
	    {
	    	suiteMessage = "pour la période de "+absence.getBegining_Hour()+" à "+absence.getEnding_Hour()+" du";
	    }
	    //Formatage de la date de l'absence
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    suiteMessage += " "+format.format(absence.getADATE());

//	  Env.startBrowser(rs.getString(1) + "login.jsp?user=" + Env.getContext(Env.getCtx(), "#AD_User_Name") + "&redirection=%2FreleveEmploi%2Findex.jsp%3FemployeeId=" + m_employeeId);	    
//        return ("Veuillez faire votre autorisation d'absence "+suiteMessage+"<br><br>"
//                + " <br><br><a href=\"" + webAppUrl + "autorisationAbsence/index.jsp\">"
//                + " Cliquer ici pour accéder à l'application d'autorisation d'absence</a>");
        return ("Veuillez faire votre autorisation d'absence "+suiteMessage+"<br><br>"
                + " <br><br><a href=\"" + webAppUrl + "login.jsp?user="+employee.getUserCode()+"&redirection=%2FautorisationAbsence%2Findex.jsp\">"
                + " Cliquer ici pour accéder à l'application d'autorisation d'absence</a>");

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

	/**
	 * Prépare le e-mail selon les informations de l'absence signifié
	 * pour le gestionnaire
	 */
	private String prepareMessageGestionnaire(P_Absence_Significant absence, int iEmployeeId)
	{
	    // On récupère le nom de l'employé et les dates d'absence
	    String employeeName = getEmployeeName(absence.getP_Employee_ID());
	    String webAppUrl = null;
	    String suiteMessage = null;
	    String remarque = "";
//	    P_Employee employee = P_Employee.get(Env.getCtx(), iEmployeeId, null);
	    P_Employee manager = P_Employee.get( Env.getCtx(), this.getManager(absence.getP_Employee_ID()), null );

	    try
	    {
		    webAppUrl = (String)P_System_Parameters.getParameterValue("WEBAPPURL");
	    }
	    catch (SQLException e)
	    {
	        e.printStackTrace(System.out);
	    }

	    //On formate le message dépendant du type d'absence signifiée
	    if (absence.getDayTime().equals("A"))
	    {
	    	suiteMessage = "pour l'avant-midi du";
	    }
	    else if (absence.getDayTime().equals("J"))
	    {
	    	suiteMessage = "pour la journée du";
	    }
	    else if (absence.getDayTime().equals("P"))
	    {
	    	suiteMessage = "pour l'après-midi du";
	    }
	    else if (absence.getDayTime().equals("Z"))
	    {
	    	suiteMessage = "pour la période de "+absence.getBegining_Hour()+" à "+absence.getEnding_Hour()+" du";
	    }
	    //Formatage de la date de l'absence
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    suiteMessage += " "+format.format(absence.getADATE());
	    if (absence.getDescription() != null)
	    {
	    	remarque = absence.getDescription();
	    }
//        return ("Veuillez prendre note que "+employeeName+" sera absent(e) "+suiteMessage+"<br><br>"
//        		+ "Remarque:"+remarque
//                + " <br><br><a href=\"" + webAppUrl + "autorisationAbsence/index.jsp\">"
//                + " Cliquer ici pour accéder à l'application d'autorisation d'absence</a>");
        return ("Veuillez prendre note que "+employeeName+" sera absent(e) "+suiteMessage+"<br><br>"
        		+ "Remarque:"+remarque
        		+ " <br><br><a href=\"" + webAppUrl + "login.jsp?user="+manager.getUserCode()+"&redirection=%2FautorisationAbsence%2Findex.jsp\">"  
                + " Cliquer ici pour accéder à l'application d'autorisation d'absence</a>");

	}
	
	
	
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
            	if  ( rs.getInt("P_Employee_Coordinating_ID") != 0 )
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


    
    /**
     * Retourne un timestamp à partir d'une date dans le format yyyy-MM-dd
     */
    public static Timestamp createTimestamp(String value)
    {
        int yyyy = Integer.parseInt(value.substring(0, 4));
        int mm = Integer.parseInt(value.substring(5,7));
        int dd = Integer.parseInt(value.substring(8));
        
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.YEAR, yyyy);
        calendar.set(Calendar.MONTH, mm-1);
        calendar.set(Calendar.DAY_OF_MONTH, dd);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /* (non-Javadoc)
     * @see solstice.custom.ValidationBase#getPageName()
     */
    protected String getPageName()
    {
        return null;
    }

}
