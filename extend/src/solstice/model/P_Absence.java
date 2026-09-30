/*
 * Created on 31-Aug-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package solstice.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CCache;
import org.compiere.util.CompiereUserError;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;
import java.util.Calendar;


import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * @author frafor01
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class P_Absence extends X_P_Absence
{
	/**
	 * 	Get absence
	 *	@param ctx context
	 * 	@param P_absence_ID id
	 *	@return absence
	 */
	public static P_Absence get (Properties ctx, int P_Absence_ID, String trxName)
	{
		Integer key = new Integer (P_Absence_ID);
		P_Absence absence = (P_Absence)s_cache.get(key);
		if (absence != null)
			return absence;
		absence = new P_Absence (ctx, P_Absence_ID, trxName);
		s_cache.put (key, absence);
		return absence;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer,P_Absence>	s_cache = new CCache<Integer,P_Absence>("P_Absence", 100);

	/**
     * @param ctx
     * @param P_Absence_ID
     * @param trxName
     */
    public P_Absence(Properties ctx, int P_Absence_ID, String trxName)
    {
        super(ctx, P_Absence_ID, trxName);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param ctx
     * @param rs
     * @param trxName
     */
    public P_Absence(Properties ctx, ResultSet rs, String trxName)
    {
        super(ctx, rs, trxName);
        // TODO Auto-generated constructor stub
    }

    public Timestamp getStartDate()
    {
    	Timestamp result = null; 
    	String sql = " Select Min(absencedate ) as startdate From P_Absence_Detail Where P_Absence_ID = " + this.getP_Absence_ID();
	    try
		{
			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				result = rs.getTimestamp("startdate");
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.out.println("* ERROR * Read getStartDate() sql " + sql + "Exception :" + e);
		}


    	return result;
    }

    public Timestamp getEndDate()
    {
    	Timestamp result = null; 
    	String sql = " Select max(absencedate ) as enddate From P_Absence_Detail Where P_Absence_ID = " + this.getP_Absence_ID();
	    try
		{
			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				result = rs.getTimestamp("enddate");
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.out.println("* ERROR * Read getEndDate() sql " + sql + "Exception :" + e);
		}


    	return result;
    }
    
    public String displayDuration(  )
    {
    	String display = null;
    	String sql = "Select AbsenceDate, Duration, Begining_Hour, Ending_Hour From P_Absence_Detail Where P_Absence_ID  = " + this.getP_Absence_ID() + " Order By AbsenceDate";
    	int nbrDay = 0;
    	BigDecimal Duration = Env.ZERO;
    	
    	try
		{
			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				nbrDay++;	
				Duration = Duration.add( rs.getBigDecimal("Duration") );
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.out.println("* ERROR * Read P_Absence_Detail sql " + sql + "Exception :" + e);
		}

		DecimalFormat 	quantityFormat;
		quantityFormat = DisplayType.getNumberFormat(DisplayType.Amount, Env.getLanguage(Env.getCtx() ));
		if ( nbrDay == 1)
			display = quantityFormat.format(Duration) ; 
		else
			display = quantityFormat.format(Duration) + " (" + nbrDay + " jours )" ; // + "<br />" + detail;

        return display;
    }

    // TODO Translation
//	private final String MONTH[] = { 
//		    "January", "February", "March", "April", "May", "June", 
//		    "July", "August", "September", "October", "November", "December" 
//	    };
	private final String MONTH[] = { 
		    "Janvier", "Février", "Mars", "Avril", "Mai", "Juin", 
		    "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre" 
	    };

    
    public String displayDurationDetail(  )
    {
    	String display = null;
    	String detail = "";
    	String sql = "Select AbsenceDate, Duration, Begining_Hour, Ending_Hour From P_Absence_Detail Where P_Absence_ID  = " + this.getP_Absence_ID() + " Order By AbsenceDate";
    	int nbrDay = 0;
    	BigDecimal Duration = Env.ZERO;

		DecimalFormat 	quantityFormat;
		quantityFormat = DisplayType.getNumberFormat(DisplayType.Amount, Env.getLanguage(Env.getCtx() ));

    	try
		{
			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (sql, null);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				nbrDay++;	
				Duration = Duration.add( rs.getBigDecimal("Duration") );

				Calendar date = TimeUtil.getCalendar(rs.getTimestamp("AbsenceDate"));
				// TODO Translation
				String month = MONTH[date.get(Calendar.MONTH)];
//				String month = Msg.translate(Env.getCtx(), MONTH[date.get(Calendar.MONTH)]);
				if ( rs.getString("Begining_Hour" ) != null )
					detail += "(le " + rs.getTimestamp("AbsenceDate").toString().substring(8, 11) + " " + month + " de " + rs.getString("Begining_Hour") + "h à " + rs.getString("Ending_Hour") + "h) " + quantityFormat.format(rs.getBigDecimal("Duration")) + "h <br />";

			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.out.println("* ERROR * Read P_Absence_Detail sql " + sql + "Exception :" + e);
		}

		if ( detail.length() != 0 )
			display = detail;
		else
			display = null;

        return display;
    }

    //2008-10-01
    private Timestamp getAbsenceDate()
    {
    	Timestamp AbsenceDate = null;

        String sql = " Select min(AbsenceDate) as StartDate "
        		   + " from P_Absence_Detail "
        		   + " where P_Absence_ID = " + this.getP_Absence_ID();
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next())
            {
            	AbsenceDate = rs.getTimestamp(1);	
            }
            rs.close();
            stmt.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace(System.out);
        }
        
    	return AbsenceDate;
    }
    	
    	
    
	public int getManager(  )
	{
	    int id = 0;
	    
	    Timestamp  AbsenceDate  = getAbsenceDate();
	    
        String sql
        = "select P_Distribution_Booklet.P_Employee_ID, P_Distribution_Booklet.P_Employee_Coordinating_ID"
            + " from P_Employee "
            + " inner join P_Employee_Distribution_Booklet on P_Employee_Distribution_Booklet.P_Employee_ID = P_Employee.P_Employee_ID "
            + " inner join P_Distribution_Booklet on P_Employee_Distribution_Booklet.P_Distribution_Booklet_ID = P_Distribution_Booklet.P_Distribution_Booklet_ID"
            + " where P_Employee.P_Employee_ID = " + this.getP_Employee_ID()
            + " And P_Employee_Distribution_Booklet.Start_Date <= " + DB.TO_DATE( AbsenceDate ) 
            + " And IsNull(End_Date, " + DB.TO_DATE( AbsenceDate ) + ") >= " + DB.TO_DATE( AbsenceDate ) 
                ;  
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next())
            {
            	// S'il y a un coordonnateur alors le email est envoyé au coordonnateur. sinon au signataire.
            	if  ( rs.getInt("P_Employee_Coordinating_ID") != 0 && this.getP_Employee_ID() != rs.getInt("P_Employee_Coordinating_ID") )
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
	 * Prépare le e-mail selon les informations de la demande d'autorisation d'absence
	 */
	public String prepareMessage( )
	{
	    // On récupère le nom de l'employé et les dates d'absence
	    String sql
	    = "select (employee.FirstName+' '+employee.SurName) as Nom, min(absenceDetail.AbsenceDate) as StartDate, max(absenceDetail.AbsenceDate) as EndDate, employee.UserCode "
	        + " from P_Employee employee inner join (P_Absence absence inner join P_Absence_Detail absenceDetail"
	        + " on absence.P_Absence_ID = absenceDetail.P_Absence_ID)"
	        + " on employee.P_Employee_ID = absence.P_Employee_ID"
	        + " where absence.P_Absence_ID = " + this.getP_Absence_ID()
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
	    
	    P_Employee manager = P_Employee.get( Env.getCtx(), this.getManager(), null );
	    if ( manager == null)
	    	throw new RuntimeException( "Manager not found");

	    if ( this.getP_Absence_Status_ID() == P_Absence_Status.getId("DSUP") )
	    {
	        return (employeeName + " a effectué(e) une demande de suppression d'absence. <br><br>"
	                + " Absence demandée : " + startDate + " au " + endDate
	                + " <br><br>Veuillez accéder à l'application d'autorisation des absences pour approuver ou refuser cette demande."
	                //+ " <br><br><a href=\"" + webAppUrl + "login.jsp?user="+manager.getUserCode()+"&redirection=%2FautorisationAbsence%2Findex.jsp\">"
	                + " <br><br><a href=\"" + webAppUrl + "login.jsp?user="+manager.getUserCode().trim()+"&redirection=/autorisationAbsence/admconsult.jsp\">"
	                + " Cliquer ici pour accéder à l'application d'autorisation d'absence</a>");
    	
	    }

	    String detail = this.displayDurationDetail();
	    if ( detail == null )
	        return (employeeName + " a effectué(e) une demande d'autorisation d'absence. <br><br>"
	                + " Absence demandée : " + startDate + " au " + endDate + " - " + this.displayDuration()  + "  "
	                + " <br><br>Veuillez accéder à l'application d'autorisation des absences pour approuver ou refuser cette demande."
	                //+ " <br><br><a href=\"" + webAppUrl + "login.jsp?user="+manager.getUserCode()+"&redirection=%2FautorisationAbsence%2Findex.jsp\">"
	                + " <br><br><a href=\"" + webAppUrl + "login.jsp?user="+manager.getUserCode().trim()+"&redirection=/autorisationAbsence/admconsult.jsp\">"
	                + " Cliquer ici pour accéder à l'application d'autorisation d'absence</a>");
	    else 
	    	return (employeeName + " a effectué(e) une demande d'autorisation d'absence. <br><br>"
                + " Absence demandée : " + startDate + " au " + endDate + " - " + this.displayDuration()  + "  <br>"
                + " Détail des absences partielles : <br>" + detail
                + " <br><br>Veuillez accéder à l'application d'autorisation des absences pour approuver ou refuser cette demande."
                //+ " <br><br><a href=\"" + webAppUrl + "login.jsp?user="+manager.getUserCode()+"&redirection=%2FautorisationAbsence%2Findex.jsp\">"
                + " <br><br><a href=\"" + webAppUrl + "login.jsp?user="+manager.getUserCode().trim()+"&redirection=/autorisationAbsence/admconsult.jsp\">"
                + " Cliquer ici pour accéder à l'application d'autorisation d'absence</a>");

	}

	protected boolean beforeSave (boolean newRecord)
	{
		//+ 2011.07.05 + sécurité par compagnie.
		P_Employee Employee = P_Employee.get(getCtx(), getP_Employee_ID(), this.get_TrxName());
		this.setAD_Org_ID( Employee.getAD_Org_ID());
		//- 2011.07.05 + sécurité par compagnie.
		return true;
	}
    /**
     * Lorsqu'on sauvegarde une absence, on doit s'assurer de garder un historique
     * des status. Pour ce faire, on utilise la table P_Absence_Status_Histo
     */
    protected boolean afterSave(boolean newRecord, boolean success)
    {
        // On vérifie d'abord si le statut a changé en sortant le dernier
        // statut enregistré pour ce livret
        boolean logChange = true;
        String sql
        = "select top 1 P_Absence_Status_ID"
            + " from P_Absence_Status_Histo"
            + " where P_Absence_ID = " + this.getP_Absence_ID()
            + " order by Created desc";
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next())
            {
                logChange = rs.getInt("P_Absence_Status_ID") != this.getP_Absence_Status_ID();
            }
            
            rs.close();
            stmt.close();
        }
        catch(SQLException e)
        {
            log.log(Level.SEVERE, "afterSave", e);
            return false;
        }

        
        // Si on doit concerver un historique, on crée un nouvel enregistrement dans la
        // bd. La date de création compte comme la date de l'historique
        if(logChange)
        {
            P_Absence_Status_Histo absenceStatusHisto = new P_Absence_Status_Histo(Env.getCtx(), -1, null);
            absenceStatusHisto.setP_Absence_ID(this.getP_Absence_ID());
            absenceStatusHisto.setP_Absence_Status_ID(this.getP_Absence_Status_ID());
            return absenceStatusHisto.save();
        }
        
        return true;
    }
}
