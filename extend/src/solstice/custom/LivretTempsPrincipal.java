
/*
 * Created on 9 août 2005
 */

package solstice.custom;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;
import org.compiere.util.Trx;

import solstice.model.P_Booklet;
import solstice.model.P_Booklet_Detail;
import solstice.model.P_Booklet_Notes;
import solstice.model.P_Credits;
import solstice.model.P_Credits_Alert;
import solstice.model.P_Employee;
import solstice.model.P_Gain;
import solstice.model.P_Period;
import solstice.process.Calcul_Credits;
import solstice.process.PgiUtil;

/**
 * @author frafor01
 *
 * Interface principal du livret de temps
 */
public class LivretTempsPrincipal extends ValidationBase
{
    private final BigDecimal ZERO = new BigDecimal(0);
    private ArrayList<String> m_nameG = new ArrayList<String>();
    private ArrayList<BigDecimal> m_soldeG = new ArrayList<BigDecimal>();
    private ArrayList<String> m_unitG = new ArrayList<String>();
    private ArrayList<String> m_valueG = new ArrayList<String>();
    private ArrayList<Integer> m_idG = new ArrayList<Integer>();
    private ArrayList<String> name = new ArrayList<String>();
    private ArrayList<Object> solde = new ArrayList<Object>();
    private ArrayList<String> unit = new ArrayList<String>();
    private ArrayList<String> value = new ArrayList<String>();


    
    /**
     * Cette méthode utilise les parametres periodId et day tel qu'utilisé
     * dans le fichier /livretTemps/payment_correction.jsp pour trouver la
     * journée sélectionnée. On retourne cette journée en String pour l'insérer
     * dans une requête sql.
     */
    public static String getDay(HttpServletRequest request)
    {
        // On trouve d'abord la date. Puisque le paramètre day donne le délais
        // en jour entre la date de début de période et la date sélectionnée, on
        // boucle selon ce délais
        DayCounter counter = new DayCounter(Integer.parseInt(request.getParameter("periodId")));
        int i = Integer.parseInt(request.getParameter("day"));
        do
        {
            counter.nextDay();
        } while(i-- > 0);
        
        // On retourne maintenant la date dans le format yyyyMMdd
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return simpleDateFormat.format(counter.getCurrentDate());
    }
   
    /**
     * Cette méthode utilise les parametres periodId et day tel qu'utilisé
     * dans le fichier /livretTemps/payment_correction.jsp pour trouver la
     * journée sélectionnée. On retourne cette journée en String pour l'insérer
     * dans une requête sql.
     */
    public static String getDay(String periodId, String day)
    {
        // On trouve d'abord la date. Puisque le paramètre day donne le délais
        // en jour entre la date de début de période et la date sélectionnée, on
        // boucle selon ce délais
        DayCounter counter = new DayCounter(Integer.parseInt(periodId));
        int i = Integer.parseInt(day);
        do
        {
            counter.nextDay();
        } while(i-- > 0);
        
        // On retourne maintenant la date dans le format yyyyMMdd
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return simpleDateFormat.format(counter.getCurrentDate());
    }
   
    /**
     * Formatage des périodes dans le comboBox
     */
    public static String formatPeriodComboBox(ResultSet rs) throws SQLException
    {
         SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        return rs.getString("Name") + " - " + "Du " + f.format(rs.getTimestamp("StartDate")) + " au " + f.format(rs.getTimestamp("EndDate"));
    }
   
    /**
     * Indique si le statut de la feuille de temps est transferé
     */
    public static boolean isTimeSheetTransfered(PageContext pageContext)
    {
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        boolean transfered = false;
        // On doit vérifier dans la bd si le statut du livret de temps
        // est transferé
        if (request.getParameter("bookletId") != null && !request.getParameter("bookletId").equals(""))
        {
	        String sql
	        = "select top 1 1"
            + "  from P_Booklet_Status_Histo statusHisto"
            + " where statusHisto.P_Booklet_ID = " + request.getParameter("bookletId")
            + "   and statusHisto.P_Distribution_Booklet_ID = " + request.getParameter("distributionId")
            + "   and statusHisto.TimeSheetStatus = 'T'"
            + "   and statusHisto.Created = ( select max(Created)"
            + "                                 from P_Booklet_Status_Histo"
            + "                                where P_Booklet_ID = statusHisto.P_Booklet_ID"
            + "                                  and P_Distribution_Booklet_ID = statusHisto.P_Distribution_Booklet_ID )";
	        
	        try
	        {
	            PreparedStatement stmt = DB.prepareStatement(sql, null);
	            ResultSet rs = stmt.executeQuery();
	            
	            // Si on a trouvé un enregistrement, c'est que le statut est transferé
	            transfered = rs.next();
	            
	            rs.close();
	            stmt.close();
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace(System.out);
	        }
        }
        return transfered;
    }
    
    /**
     * Indique si le statut de la feuille de temps est initial
     */
    public static boolean isTimeSheetInitial(PageContext pageContext)
    {
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        boolean initial = false;
        // On doit vérifier dans la bd si le statut du livret de temps
        // est initial
        if (request.getParameter("bookletId") != null && !request.getParameter("bookletId").equals(""))
        {
	        String sql
            = "select top 1 1"
                + "  from P_Booklet_Status_Histo statusHisto"
                + " where statusHisto.P_Booklet_ID = " + request.getParameter("bookletId")
                + "   and statusHisto.P_Distribution_Booklet_ID = " + request.getParameter("distributionId")
                + "   and statusHisto.TimeSheetStatus = 'I'"
                + "   and statusHisto.Created = ( select max(Created)"
                + "                                 from P_Booklet_Status_Histo"
                + "                                where P_Booklet_ID = statusHisto.P_Booklet_ID"
                + "                                  and P_Distribution_Booklet_ID = statusHisto.P_Distribution_Booklet_ID )";
	        
	        try
	        {
	            PreparedStatement stmt = DB.prepareStatement(sql, null);
	            ResultSet rs = stmt.executeQuery();
	            
	            // Si on a trouvé un enregistrement, c'est que le statut est transferé
	            initial = rs.next();
	            
	            rs.close();
	            stmt.close();
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace(System.out);
	        }
        }
        return initial;
    }
    
    
    /**
     * Indique si le statut de la feuille de temps est approuvé
     */
    public static boolean isTimeSheetApproved(PageContext pageContext)
    {
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        boolean approved = false;
        // On doit vérifier dans la bd si le statut du livret de temps
        // est approuvé
        if (request.getParameter("bookletId") != null && !request.getParameter("bookletId").equals(""))
        {
	        String sql
            = "select top 1 1"
                + "  from P_Booklet_Status_Histo statusHisto"
                + " where statusHisto.P_Booklet_ID = " + request.getParameter("bookletId")
                + "   and statusHisto.P_Distribution_Booklet_ID = " + request.getParameter("distributionId")
                + "   and statusHisto.TimeSheetStatus = 'A'"
                + "   and statusHisto.Created = ( select max(Created)"
                + "                                 from P_Booklet_Status_Histo"
                + "                                where P_Booklet_ID = statusHisto.P_Booklet_ID"
                + "                                  and P_Distribution_Booklet_ID = statusHisto.P_Distribution_Booklet_ID )";
	        
	        try
	        {
	            PreparedStatement stmt = DB.prepareStatement(sql, null);
	            ResultSet rs = stmt.executeQuery();
	            
	            // Si on a trouvé un enregistrement, c'est que le statut est transferé
	            approved = rs.next();
	            
	            rs.close();
	            stmt.close();
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace(System.out);
	        }
        }
        return approved;
    }

    /**
     * Indique si le statut de la feuille de temps est approuvé
     */
    public static boolean isTimeSheetCompleted(PageContext pageContext)
    {
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        boolean completed = false;
        // On doit vérifier dans la bd si le statut du livret de temps
        // est approuvé
        if (request.getParameter("bookletId") != null && !request.getParameter("bookletId").equals(""))
        {
	        String sql
            = "select top 1 1"
                + "  from P_Booklet_Status_Histo statusHisto"
                + " where statusHisto.P_Booklet_ID = " + request.getParameter("bookletId")
                + "   and statusHisto.P_Distribution_Booklet_ID = " + request.getParameter("distributionId")
                + "   and statusHisto.TimeSheetStatus = 'C'"
                + "   and statusHisto.Created = ( select max(Created)"
                + "                                 from P_Booklet_Status_Histo"
                + "                                where P_Booklet_ID = statusHisto.P_Booklet_ID"
                + "                                  and P_Distribution_Booklet_ID = statusHisto.P_Distribution_Booklet_ID )";
	        
	        try
	        {
	            PreparedStatement stmt = DB.prepareStatement(sql, null);
	            ResultSet rs = stmt.executeQuery();
	            
	            // Si on a trouvé un enregistrement, c'est que le statut est transferé
	            completed = rs.next();
	            
	            rs.close();
	            stmt.close();
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace(System.out);
	        }
        }
        return completed;
    }


    /**
     * Indique si la feuille de temps a été créé
     */
    public static boolean isTimeSheetCreated(PageContext pageContext)
    {
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        boolean isCreated = false;

        if (request.getParameter("employeeId") != null && !request.getParameter("employeeId").equals("")
        	&& request.getParameter("periodId") != null && !request.getParameter("periodId").equals(""))
        {
	        String sql = "select 1 from p_time_sheet "
	        + " where p_employee_id = " +request.getParameter("employeeId")
	        + " and p_period_id = "+request.getParameter("periodId")
	        + " and sheettype = 'Regular'";	        
	        try
	        {
	            PreparedStatement stmt = DB.prepareStatement(sql, null);
	            ResultSet rs = stmt.executeQuery();
	            
	            // Si on a trouvé un enregistrement, c'est que la feuille de temps est créée
	            isCreated = rs.next();
	            
	            rs.close();
	            stmt.close();
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace(System.out);
	        }
        }
        return isCreated;
    }
    
   
    /**
     * Indique si le status du livret de temps est activé
     */
    public static boolean isTimeSheetStatusEnabled(PageContext pageContext) throws JspException
    {
    	return LivretTempsPrincipal.isSaveTimeSheetEnabled(pageContext);
    }

    /**
     * Indique si le status transferé est disponible
     */
    public static boolean isTimeSheetTransferStatusAvailable(PageContext pageContext)
    {
    	return LivretTempsPrincipal.isTimeSheetTransfered(pageContext);
    }

    
    /**
     * Indique si le status approuvé est disponible
     */
    public static boolean isTimeSheetApprovedStatusAvailable(PageContext pageContext)
    {
    	return LivretTempsPrincipal.isTimeSheetApproved(pageContext);
    }

    /**
     * Indique si le status complété est disponible
     */
    public static boolean isTimeSheetCompletedStatusAvailable(PageContext pageContext)
    {
    	return (LivretTempsPrincipal.isTimeSheetInitial(pageContext) || LivretTempsPrincipal.isTimeSheetCompleted(pageContext));
    }

    /**
     * Indique si le bouton enregistrer est activé
     */
    public static boolean isSaveTimeSheetEnabled(PageContext pageContext) throws JspException
    {
    	try
    	{
            HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();

            int P_Period_ID   = Integer.parseInt(request.getParameter("periodId"));
            P_Period Period = P_Period.get( Env.getCtx(), P_Period_ID, null);
            if (Period.getPeriodStatus().equals(P_Period.PERIODSTATUS_Closed))
            	return false;

            if (LivretTempsPrincipal.isTimeSheetTransfered(pageContext))
	    	{
	    		if (LivretTempsPrincipal.loggedUserHasPolicy(pageContext,"changeTransferedStatus"))
	    		{
	    			if (LivretTempsPrincipal.isTimeSheetCreated(pageContext))
	    			{
	    				return false;
	    			}
	    			else
	    			{
	    				return true;
	    			}
	    		}
	    		else
	    		{
	    			return false;
	    		}
	    	}
	    	else if (LivretTempsPrincipal.isTimeSheetApproved(pageContext))
	    	{
    			
	    		if (LivretTempsPrincipal.loggedUserHasPolicy(pageContext,"changeApprovedStatus"))
	    		{
		    		// 2008-07-22
	    			if (LivretTempsPrincipal.isTimeSheetCreated(pageContext))
	    			{
	    				return false;
	    			}
	    			else
	    			{
	    				return true;
	    			}
	    		}
	    		else
	    		{
	    			return false;
	    		}
	    	}
	    	else
	    	{
	    		return true;
	    	}
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	return false;
    }
    
    /**
     * Indique si la feuille de temps est activé (donc modifiable)
     * Les boutons d'ajouts ou de suppression de gains, ajout de notes et
     * ajout d'une correction de paie antérieure
     */
    public static boolean isTimeSheetEnabled(PageContext pageContext)
    {
    	return LivretTempsPrincipal.isTimeSheetInitial(pageContext);
    }


    /**
     * Vérifie si la sélection des livrets peut être affichée i.e. qu'un période
     * et une distribution ont été sélectionnés
     */
    public static boolean showLivret (PageContext pageContext)
    {
    	HttpServletRequest request = null;
    	try
    	{
        request = (HttpServletRequest)pageContext.getRequest();
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
        return request.getParameter("periodId") != null
        && !request.getParameter("periodId").equals("")
        && request.getParameter("distributionId") != null
        && !request.getParameter("distributionId").equals("");

    }
    
    /**
     * Vérifie si on peut afficher le reste du formulaire, i.e. si on a
     * choisi un employé et un livret de temps
     */
    public static boolean showEmployee (PageContext pageContext)
    {
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        if (request.getParameter("action") != null)
        {
	        if (request.getParameter("action").equals("periodChanged") || request.getParameter("action").equals("distributionChanged"))
	        {
	        	return false;
	        }
	        else
	        {
	        	return request.getParameter("employeeId") != null && !request.getParameter("employeeId").equals("");
	        }
        }
        else
        {
        	return false;
        }
    }
    
    /**
     * Formatage des heures dans la section employé
     */
    public static String formatHours(Object value)
    {
    	if ( value == null )
      	  return "";

    	if(value instanceof Number)
        {
            DecimalFormat format = new DecimalFormat("#0.00");
            return format.format(((Number)value).doubleValue());
        }
        return value.toString();
    }
    
    /**
     * Formatage des dates selon yyyy-MM-dd
     */
    public static String formatDate(Object value)
    {
        if(value != null)
        {
	        if(value instanceof Date)
	        {
	            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	            return format.format(value);
	        }
	        return value.toString();
        }
        return "&nbsp;";
    }

    /**
     * Remplit les listes pour la période de paie sur 14 jours
     */
    public static Enumeration jourInitializer (PageContext pageContext) throws JspException
    {
        Vector<ListMultiColumnItem> list = new Vector<ListMultiColumnItem>();
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        
        // On récupère d'abord la liste des gains pour la journée courrante
        String sql
        = "select bookletDetail.P_Booklet_Detail_ID, gain.Value, bookletDetail.DayQty, bookletDetail.OriginTime "
            + " from P_Booklet booklet inner join (P_Booklet_Detail bookletDetail inner join P_Gain gain"
            + " on bookletDetail.P_Gain_ID = gain.P_Gain_ID)"
            + " on booklet.P_Booklet_ID = bookletDetail.P_Booklet_ID"
            + " and booklet.P_Period_ID = bookletDetail.P_Period_ID"
            + " where booklet.P_Period_ID = " + request.getParameter("periodId")
            + " and booklet.P_Employee_ID = " + request.getParameter("employeeId")
            + " and bookletDetail.day = ?";
        
        if(!LivretTempsPrincipal.loggedUserHasPolicy(pageContext,"viewNonVisibleGain"))
        {
            sql += " and Visible = 'Y'";
        }
        
        // Pour obtenir la date de la journée courrante, on doit passer par
        // le DayCounter passé en attribut de page.
        Timestamp day;
        // Si c'est la première fois que la méthode est appelé, on doit définir l'attribut
        // de page DayCounter pour la période courrante
        if(pageContext.getAttribute("day") == null)
        {
            DayCounter counter = new DayCounter(Integer.parseInt(request.getParameter("periodId")));
            counter.nextDay();
            day = counter.getCurrentDate();
            pageContext.setAttribute("day", counter);
        }
        // Sinon, on ne fait que passer au jours suivant
        else
        {
            DayCounter counter = (DayCounter)pageContext.getAttribute("day");
            counter.nextDay();
            day = counter.getCurrentDate();
        }

        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            stmt.setTimestamp(1, day);
            ResultSet rs = stmt.executeQuery();
            
            // On ajoute ensuite chaque élément du select au vecteur de ListMultiColumnItem
            while(rs.next())
            {
            	ListMultiColumnItem lmci= new ListMultiColumnItem(
                        rs.getString("P_Booklet_Detail_ID"),
                        new String[] {rs.getString("Value"), rs.getString("DayQty")});
            	
            	if(rs.getString("OriginTime").equals("LIV"))
            		lmci.setProperty("color", PgiUtil.getSolsticeParameter(Env.getCtx(), "BookletOriginColor"));
            	else
            		lmci.setProperty("color", "#000000");
            	
                list.add(lmci);
            }
            
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace(System.out);
        }
        
        return list.elements();
    }
	
    /**
     * Cette méthode retoune un resultSet contenant le solde théorique des
     * banques selon un employé et une période
     */
	public static ResultSet getBankTheoricSummaries(int employeeId, int periodId, boolean all)
	{
	    return null;
	}
	
    /* (non-Javadoc)
     * @see com.compiere.custom.ValidationBase#doValidate()
     */

	String erreurs = null;

	protected String[] doValidate()
    {
        Trx trx = Trx.get(Trx.createTrxName(), true);
        String action = this.request.getParameter("action");
        if(action != null)
        {
            // Si on ajoute une journée dans la période de paie des 14 jours...
            if(action.equals("add"))
            {
				if(request.getParameter("dayCode") == null || request.getParameter("dayCode").equals(""))
				{
					System.out.println("Aucun code saisi.");
				}
				else if(request.getParameter("dayHrs") == null || request.getParameter("dayHrs").equals(""))
				{
					System.out.println("Aucune heure saisie.");
				}
				else
				{
					//on va chercher les erreurs de parse (saisi)
					try
					{
						BigDecimal bdtemp = this.parseQty(request.getParameter("dayHrs"));
					}
					catch(Exception e)
					{
						return new String[] {"Vous avez saisie une valeur erronée pour le nombre d'heure. Le format de saisi doit être '9.99'."};
					}

	                P_Booklet booklet = new P_Booklet(Env.getCtx(), Integer.parseInt(request.getParameter("bookletId")), null);

	                int P_Employee_ID = Integer.parseInt(request.getParameter("employeeId"));
	                int P_Period_ID   = Integer.parseInt(request.getParameter("periodId"));
	                int P_Booklet_ID  = Integer.parseInt(request.getParameter("bookletId"));
	                int P_Gain_ID     = Integer.parseInt(request.getParameter("dayCode"));
	                BigDecimal var    = this.parseQty(request.getParameter("dayHrs"));
	                String dayName = request.getParameter("dayName");
	                
	                Timestamp ts      = new Timestamp(PgiUtil.stringToDateV2(request.getParameter(dayName + "_date")).getTimeInMillis() );
	                
	                // Le validation ne s'applique pas pour les administrateurs.
	                try
	                {
    	                if ( ! LivretTempsPrincipal.loggedUserHasPolicy(pageContext,"livradmin") )
    	                	erreurs = validateCredits(P_Employee_ID, P_Period_ID, P_Booklet_ID, P_Gain_ID, var );
    					System.out.println("**DEBUG** booklet.validate : " + erreurs);
					}
					catch(Exception e)
					{
						return new String[] {"Erreur lors de la validation des banques."};
					}
/*	                	erreurs += booklet.validate( P_Employee_ID, 
												var, 
												P_Gain_ID,
												P_Booklet_ID,
												dayName, 
												ts, 
												trx.getTrxName()
											  );
*/											  
				    
					if(erreurs != null && !erreurs.equals(""))
	                {
	                    System.out.println("Erreur dans la validation des banques : " + erreurs);
	                    trx.rollback();
	                    trx.close();
	                    return new String[] {erreurs};
	                }
				}
            }
            // Si on ajoute une correction de paie antérieure...
            else if(action.equals("paymentCorrectionAdded"))
            {
                // ...on effectue le même traitement que le bloc précédent
                
                // Le champ cpaDay retourne un entier indiquand le delta entre la date de début
                // de la période et le jour choisi
                DayCounter counter = new DayCounter(Integer.parseInt(request.getParameter("cpaPeriodId")));
                int i = Integer.parseInt(request.getParameter("cpaDay"));
                do
                {
                    counter.nextDay();
                } while(i-- > 0);
                
                Calcul_Credits calculCredits = new Calcul_Credits(trx.getTrxName());
                
                try
                {
	                erreurs = calculCredits.CreditsVariationFromWebV2(
	                        Integer.parseInt(request.getParameter("employeeId")),
	                        counter.getCurrentDate(),
	                        this.parseQty(request.getParameter("cpaDayQty")),
	                        Integer.parseInt(request.getParameter("cpaGainId")),
	                        Integer.parseInt(request.getParameter("bookletId")));
                }
                catch (SQLException e)
                {
                    e.printStackTrace(System.out);
                    trx.rollback();
                    trx.close();
                    return new String[] {e.getMessage()};
                }
                
                if(erreurs != null && !erreurs.equals(""))
                {
                    System.out.println("Erreur dans la validation des banques : " + erreurs);
                    trx.rollback();
                    trx.close();
                    return new String[] {erreurs};
                }
                
            }
            // Si on doit sauvegarder tout le document
            else if(action.equals("save"))
            {
            	//
            	// On valide le solde des banques, la validation ce fait a 2 places, au moment de la saisie d'un gain supplémentaire et
            	// a la sauvegarde, car les gains peuvent avoir été générer.
            	//
                if ( request.getParameter("timeSheetStatus").equals( "C" ))
                {
                    System.out.println("**DEBUG** TimeSheetStatus = C");

	                P_Booklet booklet = new P_Booklet(Env.getCtx(), Integer.parseInt(request.getParameter("bookletId")), null);
	                int P_Employee_ID = Integer.parseInt(request.getParameter("employeeId"));
	                int P_Period_ID   = Integer.parseInt(request.getParameter("periodId"));
	                int P_Booklet_ID  = Integer.parseInt(request.getParameter("bookletId"));
	                boolean isVisible = !LivretTempsPrincipal.loggedUserHasPolicy(pageContext,"viewNonVisibleGain");
					try
					{
		                // Le validation ne s'applique pas pour les administrateurs.
		                if ( ! LivretTempsPrincipal.loggedUserHasPolicy(pageContext,"livradmin") )
		                	erreurs = validateCredits(P_Employee_ID, P_Period_ID, P_Booklet_ID, 0, null );
	                    System.out.println("**DEBUG** Erreurs = " + erreurs);
					}
					catch(Exception e)
					{
						return new String[] {"Erreur lors de la validation des banques."};
					}

	                	
                }
                if(erreurs != null && !erreurs.equals(""))
                {
                    System.out.println("Erreur dans la validation des banques : " + erreurs);
                    trx.rollback();
                    trx.close();
                    return new String[] {erreurs};
                }
            	
            }

        }
        trx.commit();
        trx.close();
        return null;
    }

    /* (non-Javadoc)
     * @see com.compiere.custom.ValidationBase#afterValidation(javax.servlet.jsp.JspWriter)
     */
    public boolean afterValidation(JspWriter out)
    {
		try
		{
	        String action;
            System.out.println("**DEBUG** AfterValidation  " );

	        if((action = this.request.getParameter("action")) != null)
	        {
	            // Si on doit sauvegarder tout le document
	            if(action.equals("save"))
	            {
                    System.out.println("**DEBUG** Erreurs = " + erreurs);
            	
	                if(erreurs != null && !erreurs.equals(""))
	            	{
	                    System.out.println("Sauvegarde echouee");
	            	}
	            	else
	            	{
		                P_Booklet booklet = new P_Booklet(Env.getCtx(), Integer.parseInt(request.getParameter("bookletId")), null);
		                
	                    booklet.setTimeSheetStatus(request.getParameter("timeSheetStatus"));
	                    booklet.setP_Distribution_Booklet_ID(Integer.parseInt(request.getParameter("distributionId")));
	                    booklet.save();
	            	}
	            }
	            // Lorsqu'on ajoute une correction de paiement, on récupère l'information dans les champs
	            // cpaPeriodId, cpaDay, cpaAssignmentId, cpaGainId et cpaDayQty
	            else if(action.equals("paymentCorrectionAdded"))
	            {
	                int id = Integer.parseInt(request.getParameter("cpaBookletDetailId"));
	                P_Booklet_Detail bookletDetail = new P_Booklet_Detail(Env.getCtx(), id, null);
	                bookletDetail.setP_Period_ID(Integer.parseInt(request.getParameter("cpaPeriodId")));
	                
	                // Le champ cpaDay retourne un entier indiquand le delta entre la date de début
	                // de la période et le jour choisi
	                DayCounter counter = new DayCounter(Integer.parseInt(request.getParameter("cpaPeriodId")));
	                int i = Integer.parseInt(request.getParameter("cpaDay"));
	                do
	                {
	                    counter.nextDay();
	                } while(i-- > 0);
	                bookletDetail.setDay(counter.getCurrentDate());
	                
	                bookletDetail.setP_Booklet_ID(Integer.parseInt(request.getParameter("bookletId")));
	                bookletDetail.setP_Gain_ID(Integer.parseInt(request.getParameter("cpaGainId")));
	                bookletDetail.setDayQty(new BigDecimal(Double.parseDouble(request.getParameter("cpaDayQty"))).setScale(6,BigDecimal.ROUND_HALF_UP));
//	                bookletDetail.setP_Assignment_ID(Integer.parseInt(request.getParameter("cpaAssignmentId")));
	                bookletDetail.setOriginTime("LIV");
	                bookletDetail.setWeekIndex(this.getWeekIndex(counter.getCurrentDate()));
	                bookletDetail.setP_Booklet_Detail_CPA_ID(0);
	                
	                // On doit maintenant chercher le P_Schedule_ID à partir de l'assignation
//	                bookletDetail.setP_Schedule_ID(this.findScheduleId(request.getParameter("cpaAssignmentId"), bookletDetail.getP_Period_ID()));
	                
        	        this.setMainAssignmentSchedule(Integer.parseInt(request.getParameter("employeeId")), bookletDetail, bookletDetail.getDay());

        	   
        	        
	                if(!bookletDetail.save())
	                {
	                    System.out.println("Sauvegarde echouee");
	                }
	                else
	                {
	                    System.out.println("Sauvegarde reussie");
	                }
	                
	                // Si on a choisi de couper un gain, on refait un autre enregistrement pour
	                // enregistrer cette coupure
	                if(request.getParameter("cpaCutGainId") != null
	                        && !request.getParameter("cpaCutGainId").equals(""))
	                {
	                    id = Integer.parseInt(request.getParameter("cpaBookletDetailCuttingId"));
	                    P_Booklet_Detail bookletDetailCutting = new P_Booklet_Detail(Env.getCtx(), id, null);
	                    bookletDetailCutting.setP_Period_ID(Integer.parseInt(request.getParameter("cpaPeriodId")));
	                    bookletDetailCutting.setDay(counter.getCurrentDate());
	                    bookletDetailCutting.setP_Booklet_ID(Integer.parseInt(request.getParameter("bookletId")));
	                    
	                    // On utilise le code de gain coupé
	                    bookletDetailCutting.setP_Gain_ID(Integer.parseInt(request.getParameter("cpaCutGainId")));
	                    
	                    // On rend le nombre d'heures négatif pour effectuer la coupure
	                    bookletDetailCutting.setDayQty(new BigDecimal(Double.parseDouble(request.getParameter("cpaDayQty"))).negate().setScale(6,BigDecimal.ROUND_HALF_UP));
//	                    bookletDetailCutting.setP_Assignment_ID(Integer.parseInt(request.getParameter("cpaAssignmentId")));
	                    bookletDetailCutting.setOriginTime("LIV");
	                    bookletDetailCutting.setWeekIndex(this.getWeekIndex(counter.getCurrentDate()));
//	                    bookletDetailCutting.setP_Schedule_ID(this.findScheduleId(request.getParameter("cpaAssignmentId"), bookletDetail.getP_Period_ID()));
	                    bookletDetailCutting.setP_Booklet_Detail_CPA_ID(bookletDetail.getP_Booklet_Detail_ID());
	        	        this.setMainAssignmentSchedule2(Integer.parseInt(request.getParameter("employeeId")), bookletDetailCutting, bookletDetailCutting.getDay());
	                    
	                    if(!bookletDetailCutting.save())
	                    {
	                        System.out.println("Sauvegarde de la coupure echouee");
	                    }
	                    else
	                    {
	                        System.out.println("Sauvegarde de la coupure reussie");
	                    }
	                    
	//                    // On doit maitenant lier les deux entrées au livret
	//                    bookletDetail.setP_Booklet_Detail_CPA_ID(bookletDetailCutting.getP_Booklet_Detail_ID());
	//                    if(!bookletDetail.save())
	//                        System.out.println("Erreur dans la sauvegarde du lien entre la coupure et la correction de paie antérieure");
	                }
	            }
	            // Supprime une note
	            else if(action.equals("removeNote"))
	            {
	                String sql = "delete from P_Booklet_Notes where P_Booklet_Notes_ID = " + request.getParameter("message");
	                DB.executeUpdate(sql, null);
	            }
	            // Supprime une correction de paie antérieure
	            else if(action.equals("removeCPA"))
	            {
	                String sql = "delete from P_Booklet_Detail where P_Booklet_Detail_ID = " + request.getParameter("cpaToDelete") + " or P_Booklet_Detail_CPA_ID = " + request.getParameter("cpaToDelete");
	                DB.executeUpdate(sql, null);
	            }
	            // Ajoute une note
	            else if(action.equals("addNote"))
	            {
	                P_Booklet_Notes bookletNotes = new P_Booklet_Notes(Env.getCtx(), -1, null);
	                bookletNotes.setP_Booklet_ID(Integer.parseInt(request.getParameter("bookletId")));
	                bookletNotes.setNotes(request.getParameter("newMessage"));
	                bookletNotes.save();
	            }
	            // Ajouter une journée dans la période de paie sur 14 jours
	            else if(action.equals("add"))
	            {
					try
					{	
						if(request.getParameter("dayCode") == null || request.getParameter("dayCode").equals(""))
						{
							System.out.println("Aucun code saisi.");
						}
						else if(request.getParameter("dayHrs") == null || request.getParameter("dayHrs").equals(""))
						{
							System.out.println("Aucune heure saisie.");
						}
						else
						{
			                String dayName = request.getParameter("dayName");
		                
		        	        Timestamp day = new Timestamp(PgiUtil.stringToDateV2(request.getParameter(dayName + "_date")).getTimeInMillis());
		                	P_Booklet_Detail bookletDetail = new P_Booklet_Detail(Env.getCtx(), -1, null);
			                bookletDetail.setP_Period_ID(Integer.parseInt(request.getParameter("periodId")));
		        	        bookletDetail.setDay(day);
		                	bookletDetail.setP_Booklet_ID(Integer.parseInt(request.getParameter("bookletId")));
			                bookletDetail.setP_Gain_ID(Integer.parseInt(request.getParameter("dayCode")));
		        	        bookletDetail.setDayQty(this.parseQty(request.getParameter("dayHrs")).setScale(6,BigDecimal.ROUND_HALF_UP));
		                	bookletDetail.setOriginTime("LIV");
			                bookletDetail.setWeekIndex(this.getWeekIndex(day));
		        	        this.setMainAssignmentSchedule(Integer.parseInt(request.getParameter("employeeId")), bookletDetail, day);
		        	        try
		        	        
		        	        {
		                    	if(!bookletDetail.save())
		    	                {
		            	            System.out.println("Sauvegarde du livret detail échouée");
		                    	}
		    	                else
		            	        {
		                    	    System.out.println("Sauvegarde du livret detail réussie");
		    	                    Cut_Gain(bookletDetail);
		            	        }
		        	        }
		        	        catch(Exception e)
		        	        {
		        	        	e.printStackTrace();
		        	        }
						}
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
	            }
	            // Supprimer une journée dans la période de paie sur 14 jours
	            else if(action.equals("deleteCode"))
	            {
	                String dayName = request.getParameter("dayName");
	                String sql
	                = "delete from P_Booklet_Detail"
	                    + " where P_Booklet_Detail_ID = " + request.getParameter(dayName + "_selectedValue");
	                DB.executeUpdate(sql, null);
	            }
	        }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
/*
        // Le validation ne s'applique pas pour les administrateurs.
        try
        {
            if ( ! LivretTempsPrincipal.loggedUserHasPolicy(pageContext,"livradmin") )
            {
                int P_Employee_ID = Integer.parseInt(request.getParameter("employeeId"));
                int P_Period_ID   = Integer.parseInt(request.getParameter("periodId"));
                int P_Booklet_ID  = Integer.parseInt(request.getParameter("bookletId"));
                int P_Gain_ID     = Integer.parseInt(request.getParameter("dayCode"));
                BigDecimal var    = this.parseQty(request.getParameter("dayHrs"));
               	erreurs = validateCredits(P_Employee_ID, P_Period_ID, P_Booklet_ID, P_Gain_ID, var );
    			System.out.println("**DEBUG** booklet.validate : " + erreurs);
            }
		}
		catch(Exception e)
		{
			return false;
		}
*/
        return true;
    }
    
    /**
     * Retourne le numéro de la semaine de la période à laquelle appartient la date (1 ou 2)
     */
    private int getWeekIndex(Timestamp date)
    {
        int weekIndex = 0;
        // On recherche d'abord date de début de la période à laquelle la date appartient
        String sql
        = "select top 1 StartDate"
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
                Timestamp startDate = rs.getTimestamp("StartDate");
                
                // On calcule maintenant combien de jours il y a entre
                // la date passée en paramètre et la date de début
                // de la période. Ensuite, on fait une division pour trouver
                // la semaine.
                int days = TimeUtil.getDaysBetween(startDate, date);
                weekIndex = (int)Math.floor(days / 7.0d) + 1;
            }
            
            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace(System.out);
        }
        
        return weekIndex;
    }
    
    /**
     * Trouve le schedule_ID à partir du id de l'affectation
     */
    private int findScheduleId(String assignmentId, int periodId)
    {
        String sql 
        = "select isnull(("
            + "   select top 1 assignmentParam.P_Schedule_ID"
            + "   from P_Assignment_Param assignmentParam"
            + "   where assignmentParam.P_Assignment_ID = " + assignmentId
            + "   and EffectIn <= (select StartDate from P_Period where P_Period_ID = " + periodId + ")"
            + "   order by EffectIn desc"
            + " ), ("
            + "   select P_Schedule_ID"
            + "   from P_Schedule"
            + "   where IsDefault = 'Y'"
            + " )) as P_Schedule_ID";
        
        int id = 0;
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next())
            {
                id = rs.getInt("P_Schedule_ID");
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
     * Convertit le string en BigDecimal
     */
    private BigDecimal parseQty(String value)
    {
        double qty = Double.parseDouble(value);
        return new BigDecimal(qty).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
    
    /**
     * Set l'affectation principal et le schedule_id pour l'employé passé en paramètre
     */
    private void setMainAssignmentSchedule(int employeeId, P_Booklet_Detail bookletDetail, Timestamp day)
    {
		String sql = null;
		sql = "Select P_Assignment.P_Assignment_ID, P_Assignment_Param.P_Schedule_ID "; 
		sql += " From P_Assignment, P_Assignment_Param ";
		sql += " WHERE P_Assignment.IsActive='Y' ";
		sql += " AND P_Assignment_Param.IsActive='Y' ";
		sql += " AND P_Assignment_Param.P_Assignment_ID = P_Assignment.P_Assignment_ID";
		sql += " AND P_Assignment.P_Employee_ID=" + employeeId;
//		sql += " AND P_Assignment.AssignmentType = '" + P_Assignment.ASSIGNMENTTYPE_Other + "'";
		sql += " AND P_Assignment_Param.EffectIn <= " + DB.TO_DATE( day );
		sql += " AND " + DB.TO_DATE( day ) + " between isnull( P_Assignment.startdate, " + DB.TO_DATE( day ) + ") and isnull( P_Assignment.enddate, "+ DB.TO_DATE( day ) + ")";
		sql += " ORDER BY P_Assignment.StartDate Desc, P_Assignment.AssignmentType ";

        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next())
            {
                bookletDetail.setP_Assignment_ID(rs.getInt("P_Assignment_ID"));
                bookletDetail.setP_Schedule_ID(rs.getInt("P_Schedule_ID"));
            }
            
            rs.close();
            stmt.close();
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
    }

    /**
     * Set l'affectation du gain qui coupe un autre gain.
     */
    private void setMainAssignmentSchedule2(int employeeId, P_Booklet_Detail bookletDetail, Timestamp day)
    {
    	
		String sql = null;

		sql = "select P_Time_Sheet_Detail.P_Assignment_ID, P_Time_Sheet_Detail.P_Schedule_ID ";
		sql += " from (P_Time_Sheet ";
		sql += "	inner join P_Time_Sheet_Detail on P_Time_Sheet.P_Time_Sheet_ID = P_Time_Sheet_Detail.P_Time_Sheet_ID) ";
		sql += "    inner join P_Gain gain on P_Time_Sheet_Detail.P_Gain_ID = gain.P_Gain_ID";
		sql += " where P_Time_Sheet.P_Employee_ID = " + employeeId;
		sql += " and P_Time_Sheet_Detail.[day] = " + DB.TO_DATE(day);
		sql += " and P_Time_Sheet_Detail.P_Gain_ID = " + bookletDetail.getP_Gain_ID();
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql, null);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next())
            {
                bookletDetail.setP_Assignment_ID(rs.getInt("P_Assignment_ID"));
                bookletDetail.setP_Schedule_ID(rs.getInt("P_Schedule_ID"));
            }
            
            rs.close();
            stmt.close();
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
    }

    /**
     * Concatène la liste d'id à partir d'un input de code de gain
     */
    private String getIds(String[] values)
    {
        System.out.println("**** getIds()");
        if(values.length > 0)
        {
	        String ids = "";
	        for(int i = 0; i < values.length - 1; i++)
	        {
	            String v = PgiUtil.split(values[i], '|')[0];
	            if(v.charAt(0) != '+')
	                ids += v + ", ";
	        }
	        String v = PgiUtil.split(values[values.length-1], '|')[0];
	        if(v.charAt(0) == '+')
	            v = "0";
            ids += v;
	        return ids;
        }
        return "0";
    }

    /*
     * Implémentation des méthodes à Marc pour les coupures de gain
     */
    
	private void Cut_Gain(P_Booklet_Detail bookletDetail)
	{
//		System.out.println("Cut_Gain");
	    P_Booklet_Detail Cut;
        boolean isCut = false;
        BigDecimal qtyCut;
        
		String query = "Select P_Gain_ID_Affected FROM P_Gain_Join, P_Gain "
			         + " WHERE P_Gain_Join.P_Gain_ID = ? "
					 + "   AND P_Gain_Join.P_Gain_ID = P_Gain.P_Gain_ID "
					 + "  ORDER BY P_Gain.value ";
					 ;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (query, null);
			pstmt.setInt(1, bookletDetail.getP_Gain_ID() );
			
			ResultSet rs = pstmt.executeQuery ();
			P_Gain Gain_Affected;
			BigDecimal qty = bookletDetail.getDayQty();
			while (rs.next () && ! isCut )
			{
				Gain_Affected = P_Gain.get( Env.getCtx(), rs.getInt("P_Gain_ID_Affected"), null);
				Cut = getBookletDetail(bookletDetail.getP_Booklet_ID(), bookletDetail.getDay(), rs.getInt("P_Gain_ID_Affected"));
				
				if ( Cut != null && Cut.getP_Booklet_Detail_ID() != bookletDetail.getP_Booklet_Detail_ID() )
				{
					isCut = isCut(Cut.getDayQty() , qty);
				    qtyCut = cut( Cut.getDayQty() , qty ) ;
					Cut.setDayQty( Cut.getDayQty().add( qtyCut).setScale(6,BigDecimal.ROUND_HALF_UP));
					qty = qty.add( qtyCut );
					Cut.save();
					
					if ( Cut.getDayQty().compareTo( ZERO ) == 0 )
					{
						Cut.delete( true, null);
					}
				}
				
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
		    System.out.println("* ERROR * Cut_gain error  - " + query);
		    e.printStackTrace(System.out);
		}

	}
	
	/**
	 * Cette méthode retourne un détail de livret de temps s'il en existe un pour
	 * les paramètres suivants : P_Booklet_ID, day et P_Gain_ID
	 */
	private P_Booklet_Detail getBookletDetail(int bookletId, Timestamp day, int gainId)
	{
	    P_Booklet_Detail bookletDetail = null;
	    String sql
	    = "select P_Booklet_Detail_ID"
	        + " from P_Booklet_Detail"
	        + " where P_Booklet_ID = " + bookletId
	        + " and Day = ?"
	        + " and P_Gain_ID = " + gainId;
	    
	    try
	    {
	        PreparedStatement stmt = DB.prepareStatement(sql, null);
	        stmt.setTimestamp(1, day);
	        ResultSet rs = stmt.executeQuery();
	        
	        if(rs.next())
	        {
	            bookletDetail = new P_Booklet_Detail(Env.getCtx(), rs.getInt("P_Booklet_Detail_ID"), null);
	        }
	        
	        rs.close();
	        stmt.close();
	    }
	    catch (SQLException e)
	    {
	        e.printStackTrace(System.out);
	    }
	    
	    return bookletDetail;
	}

	private boolean isCut( BigDecimal QtyVo, BigDecimal QtySubtract )
	{
 	    if ( QtyVo.compareTo( QtySubtract) < 0)
		   return false;
		return true;
	}
	
	private BigDecimal cut( BigDecimal QtyVo, BigDecimal QtySubtract )
	{
	   if ( QtyVo.compareTo( QtySubtract) < 0)
	       return QtyVo.negate();
	   return QtySubtract.negate();
	}
	
		
	final public static int NUMDAY = 14;
	public static boolean[] getDayLock(PageContext pageContext) throws JspException
	{
		//init
		boolean[] retValue = new boolean[NUMDAY];
		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		
		for(int idx=0;idx<NUMDAY;idx++)
		{
			retValue[idx] = true;
		}
		
		if(request.getParameter("periodId") != null && !request.getParameter("periodId").equals("") &&
				request.getParameter("employeeId") != null && !request.getParameter("employeeId").equals("") &&
				request.getParameter("distributionId") != null && !request.getParameter("distributionId").equals(""))
		{
			int periodID = Integer.parseInt(request.getParameter("periodId"));
			P_Period period = P_Period.get(Env.getCtx(), periodID, null);
			if(period != null)
			{

				
				
				String strSelect = "SELECT dateadd(day, 0, datediff(day,0,Start_Date)) as Start_Date, dateadd(day, 0, datediff(day,0,End_Date)) as End_Date "
					+ " FROM P_Employee_Distribution_Booklet "
					+ " WHERE P_Employee_ID ="+request.getParameter("employeeId")
					+ " AND P_Distribution_Booklet_ID="+request.getParameter("distributionId")
					+ " AND ( Start_Date <= ? )"
					+ " AND ( End_Date is null OR End_Date > ? )"
					;
/*
					+ " AND ( (dateadd(day, 0, datediff(day,0,Start_Date)) BETWEEN dateadd(day, 0, datediff(day,0,?)) AND dateadd(day, 0, datediff(day,0,?))) "
					+ " OR (dateadd(day, 0, datediff(day,0,isNull(End_Date, ?))) BETWEEN dateadd(day, 0, datediff(day,0,?)) AND dateadd(day, 0, datediff(day,0,?))) "
					+ " OR (dateadd(day, 0, datediff(day,0,Start_Date)) <= ? AND dateadd(day, 0, datediff(day,0,isNull(End_Date, ?))) >= dateadd(day, 0, datediff(day,0,?))))";
*/
//				System.out.println("Reqete SQL aDebugguer :"+strSelect);
				PreparedStatement pstm = DB.prepareStatement(strSelect, null);
				try
				{
					pstm.setTimestamp(1, period.getEndDate());
					pstm.setTimestamp(2, period.getStartDate());
/*					pstm.setTimestamp(1, period.getStartDate());
					pstm.setTimestamp(2, period.getEndDate());
					pstm.setTimestamp(3, period.getEndDate());
					pstm.setTimestamp(4, period.getStartDate());
					pstm.setTimestamp(5, period.getEndDate());
					pstm.setTimestamp(6, period.getStartDate());
					pstm.setTimestamp(7, period.getEndDate());
					pstm.setTimestamp(8, period.getEndDate());
*/					
					ResultSet rs = pstm.executeQuery();
					while(rs.next())
					{
						//4 cas possibles
						
						//1 - date de debut avant periode, date de fin dans periode
						if(rs.getTimestamp("Start_Date").compareTo(period.getStartDate()) < 0 &&
								(rs.getTimestamp("End_Date") != null && rs.getTimestamp("End_Date").compareTo(period.getEndDate()) <= 0))
						{
							//on dé vérouille tout entre la date de début de période et la date de fin de distrib
							int iNumdays = getNumDaysBetweenTimestamp(period.getStartDate(), rs.getTimestamp("End_Date"));
							for(int idx1=0;idx1<iNumdays;idx1++)
							{
								retValue[idx1] = false;
							}
							
						}
						//2 - date de debut dans periode, date de fin dans periode
						else if(rs.getTimestamp("Start_Date").compareTo(period.getStartDate()) >= 0 &&
								(rs.getTimestamp("End_Date") != null && rs.getTimestamp("End_Date").compareTo(period.getEndDate()) <= 0))
						{
							//on dé vérouille tout entre la date de début de distrib et la date de fin de distrib
							//on commence par aller chercher notre index de tableau representant
							//la date de debut de distrib
							int iNumdays1 = getNumDaysBetweenTimestamp(period.getStartDate(), rs.getTimestamp("Start_Date")) -1;
							//-1 parce qu'on met ca dans un index de tableau
							//ensuite, on va chercher le nombre de jour entre la date de debut de distrib
							//et la date de fin de distrib
							int iNumdays2 = getNumDaysBetweenTimestamp(period.getStartDate(), rs.getTimestamp("End_Date")) -1;
							//-1 parce qu'on met ca dans un index de tableau
							for(int idx1=iNumdays1;idx1<=iNumdays2;idx1++)//on inclus toujours la journée de fin
							{
								retValue[idx1] = false;
							}
						}
						//3 - date de debut avant periode, date de fin apres periode
						else if(rs.getTimestamp("Start_Date").compareTo(period.getStartDate()) < 0 &&
								(rs.getTimestamp("End_Date") == null || (rs.getTimestamp("End_Date") != null && rs.getTimestamp("End_Date").compareTo(period.getEndDate()) > 0)))
						{
							//tout est non vérouillé
							for(int idx1=0;idx1<NUMDAY;idx1++)
							{
								retValue[idx1] = false;
							}
						}
						//4 - date de debut dans periode, date de fin apres periode
						else if(rs.getTimestamp("Start_Date").compareTo(period.getStartDate()) >= 0 &&
								(rs.getTimestamp("End_Date") == null || (rs.getTimestamp("End_Date") != null && rs.getTimestamp("End_Date").compareTo(period.getEndDate()) > 0)))
						{
							//on dé vérouille tout entre la date de début de distrib et la date de fin de période
							//on commence par aller chercher notre index de tableau representant
							//la date de debut de distrib
							int iNumdays = getNumDaysBetweenTimestamp(period.getStartDate(), rs.getTimestamp("Start_Date")) -1;
							//-1 parce qu'on met ca dans un index de tableau
							for(int idx1=iNumdays;idx1<NUMDAY;idx1++)
							{
								retValue[idx1] = false;
							}
						}
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
					throw new JspException("Erreur SQL sur la période ou sur l'employé.\nVeuillez contacter votre administrateur système.");
				}
			}
			else
			{
				throw new JspException("Erreur sur la période, sur la distribution ou sur l'employé.\nVeuillez contacter votre administrateur système.");
			}
		}
		return retValue;
	}
	
	private static int getNumDaysBetweenTimestamp(Timestamp date1, Timestamp date2)
	{
		String errorMsg = null;
		int retValue = 0;
		
		int yearDateDebut, yearDateFin, nbDayDateDebut, nbDayDateFin;

		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis(date1.getTime());
		yearDateDebut = cal.get(Calendar.YEAR);
		nbDayDateDebut = cal.get(Calendar.DAY_OF_YEAR);
		cal.setTimeInMillis(date2.getTime());
		yearDateFin = cal.get(Calendar.YEAR);
		nbDayDateFin = cal.get(Calendar.DAY_OF_YEAR)+1; //+1 pour inclure la journée de fin
		retValue = (yearDateFin - yearDateDebut)*365;
		retValue += (nbDayDateFin - nbDayDateDebut);

	      
		return retValue;
	}


	public String validateCredits(int employeeId, int periodId, int bookletId, int GainID, BigDecimal var) throws JspException
	{
//        System.out.println("**DEBUG** Validate Credits employee " + employeeId + "period " +periodId + " Booklet" + bookletId  );
		String errorMsg = null;
		name = new ArrayList<String>();
		solde = new ArrayList<Object>();
		unit = new ArrayList<String>();
		value = new ArrayList<String>();
		
		String strMovement = "select Credits.Name as Credits_Name, "+ 
			"	SUM( PMOVEMENTVARIATION ) Solde, "+ 
			"   UOM.Value as Unit, "+ 
			"   Credits.Value "+
		  "from P_Employee Employee, "+
		  "     P_Credits_Movement Movement, "+
			"   P_Credits Credits, "+
			"   P_UOM UOM, "+
			"   P_EMPLOYEE_CREDITS employeeCredits "+
		"where Credits.P_UOM_ID = UOM.P_UOM_ID "+
		"and Movement.P_Credits_ID = Credits.P_Credits_ID "+
		"and Employee.p_employee_id = employeeCredits.P_employee_id "+
		"and credits.p_credits_id = employeeCredits.p_credits_id "+
		"and employeeCredits.IsActive = 'Y' "+
		"and Employee.P_Employee_ID = Movement.P_Employee_ID "+
		"and Movement.PMovementDate < (select StartDate from P_Period where P_Period_ID = "+periodId+") "+
		"and Employee.P_Employee_ID = "+employeeId+" "+
		"and Credits.IsPrinted = 'Y'		 "+				
		"group by Credits.Value, UOM.Value, Credits.Name ";
		
		PreparedStatement pstmMovement = DB.prepareStatement(strMovement, null);
		ResultSet rsMovement;
		try
		{
			rsMovement = pstmMovement.executeQuery();
			while(rsMovement.next())
			{
				name.add(rsMovement.getString("Credits_Name"));
				solde.add(rsMovement.getBigDecimal("Solde"));
				unit.add(rsMovement.getString("Unit"));
				value.add(rsMovement.getString("Value"));
			}
			rsMovement.close();
			pstmMovement.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new JspException("Erreur SQL sur la période ou sur l'employé.\nVeuillez contacter votre administrateur système.");			
		}

		String strGain = "Select P_Credits.Name as Credits_Name, "+ 
		"		SUM(CASE WHEN Credits_Function = '+'  "+
		"		THEN P_Booklet_Detail.DayQty * P_Gain_Credits.MultiplyRate  "+
		"		ELSE P_Booklet_Detail.DayQty * P_Gain_Credits.MultiplyRate * -1  "+
		"   	END) as Solde,  "+
		"	P_UOM.Value as Unit,  "+
		"   P_Credits.Value,  "+
		"	P_Credits.P_Credits_ID, "+
		"	P_Employee_Credits.P_Method_Credits_ID "+
		"  From P_Employee_Credits,  "+
		"	   P_Gain_Credits,  "+
		"	   P_Credits,  "+
		"	   P_Gain_Parameter,  "+
		"	   P_Booklet_Detail,  "+
		"	   P_employee ,   "+
		"	   P_UOM  "+
		" Where P_Gain_Credits.IsActive = 'Y'  "+
		"   And P_Credits.IsActive = 'Y'  "+
		"   And P_Credits.IsPrinted = 'Y' "+
		"   And P_Gain_Credits.P_Credits_ID = P_Credits.P_Credits_ID  "+
		"   AND P_Gain_Parameter.P_Gain_Parameter_ID = P_Gain_Credits.P_Gain_Parameter_ID  "+
		"   And P_Gain_Parameter.P_Gain_ID = P_Booklet_Detail.P_Gain_ID  "+
		"   And P_Credits.P_UOM_ID = P_UOM.P_UOM_ID  "+
		"   And P_Booklet_Detail.P_Booklet_ID = "+bookletId+
		"   And P_Employee_Credits.P_Employee_ID = "+employeeId+
		"   And P_Employee_Credits.IsActive = 'Y'  "+
		"   And P_Employee_Credits.P_Credits_ID = P_Credits.P_Credits_ID  "+
		"   And P_Employee.p_employee_id = p_employee_credits.p_employee_id  "+
		"   And ISNULL(P_Gain_Credits.P_Collective_Labour_Agr_ID,0) in   "+
		"	   (SELECT ISNULL(Max (P_Collective_Labour_Agr_ID),0)  "+
		"		  FROM P_Gain_Credits credits "+
		"		 WHERE credits.p_gain_parameter_id = p_gain_credits.p_gain_parameter_id  "+
		"		   and credits.p_credits_id = p_gain_credits.p_credits_id  "+
		"		   and credits.credits_function = p_gain_credits.credits_function "+
		"		   and (Credits.P_Collective_Labour_Agr_ID is null  "+
		"			or Credits.P_Collective_Labour_Agr_ID = P_employee.P_Collective_Labour_Agr_ID) ) "+ 
		"GROUP BY P_Credits.Name, P_Credits.Value, P_Credits.P_Credits_ID, P_UOM.Value, P_Employee_Credits.P_Method_Credits_ID  ";

		PreparedStatement pstmGain = DB.prepareStatement(strGain, null);
		ResultSet rsGain;

		try
		{
			rsGain = pstmGain.executeQuery();
			while(rsGain.next())
			{
				m_nameG.add(rsGain.getString("Credits_Name"));
				m_soldeG.add(rsGain.getBigDecimal("Solde"));
				m_unitG.add(rsGain.getString("Unit"));
				m_valueG.add(rsGain.getString("Value"));
				m_idG.add(new Integer(rsGain.getInt("P_Credits_ID")));
				
				
				errorMsg = recursiveCheckBanksAlerts(rsGain.getInt("P_Method_Credits_ID"), employeeId);
				if ( errorMsg != null)
				{
					rsGain.close();
					pstmGain.close();
//			        System.out.println("**DEBUG** Validate Credits = " + errorMsg);
					return errorMsg;
				}
				for(int i=0;i<m_nameG.size();i++)
				{
					name.add(m_nameG.get(i));
					solde.add(m_soldeG.get(i));
					unit.add(m_unitG.get(i));
					value.add(m_valueG.get(i));
				}
				m_nameG.clear();
				m_soldeG.clear();
				m_unitG.clear();
				m_valueG.clear();
				m_idG.clear();
			}
			rsGain.close();
			pstmGain.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new JspException("Erreur SQL sur la période ou sur l'employé.\nVeuillez contacter votre administrateur système.");			
		}
		
		name = null;
		solde = null;
		unit = null;
		value = null;

  //      System.out.println("**DEBUG** Validate Credits = " + errorMsg);

		return errorMsg;
	}


	public String printBankGrid(int employeeId, int periodId, int bookletId) throws JspException
	{
		String retValue = "";
		String errorMsg = null;
		name = new ArrayList<String>();
		solde = new ArrayList<Object>();
		unit = new ArrayList<String>();
		value = new ArrayList<String>();
		
		String strMovement = "select Credits.Name as Credits_Name, "+ 
			"	SUM( PMOVEMENTVARIATION ) Solde, "+ 
			"   UOM.Value as Unit, "+ 
			"   Credits.Value "+
		  "from P_Employee Employee, "+
		  "     P_Credits_Movement Movement, "+
			"   P_Credits Credits, "+
			"   P_UOM UOM, "+
			"   P_EMPLOYEE_CREDITS employeeCredits "+
		"where Credits.P_UOM_ID = UOM.P_UOM_ID "+
		"and Movement.P_Credits_ID = Credits.P_Credits_ID "+
		"and Employee.p_employee_id = employeeCredits.P_employee_id "+
		"and credits.p_credits_id = employeeCredits.p_credits_id "+
		"and employeeCredits.IsActive = 'Y' "+
		"and Employee.P_Employee_ID = Movement.P_Employee_ID "+
		"and Movement.PMovementDate < (select StartDate from P_Period where P_Period_ID = "+periodId+") "+
		"and Employee.P_Employee_ID = "+employeeId+" "+
		"and Credits.IsPrinted = 'Y'		 "+				
		"group by Credits.Value, UOM.Value, Credits.Name ";
		
		PreparedStatement pstmMovement = DB.prepareStatement(strMovement, null);
		ResultSet rsMovement;
		try
		{
			rsMovement = pstmMovement.executeQuery();
			while(rsMovement.next())
			{
				name.add(rsMovement.getString("Credits_Name"));
				solde.add(rsMovement.getBigDecimal("Solde"));
				unit.add(rsMovement.getString("Unit"));
				value.add(rsMovement.getString("Value"));
			}
			rsMovement.close();
			pstmMovement.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new JspException("Erreur SQL sur la période ou sur l'employé.\nVeuillez contacter votre administrateur système.");			
		}


		String strGain = "Select P_Credits.Name as Credits_Name, "+ 
		"		SUM(CASE WHEN Credits_Function = '+'  "+
		"		THEN P_Booklet_Detail.DayQty * P_Gain_Credits.MultiplyRate  "+
		"		ELSE P_Booklet_Detail.DayQty * P_Gain_Credits.MultiplyRate * -1  "+
		"   	END) as Solde,  "+
		"	P_UOM.Value as Unit,  "+
		"   P_Credits.Value,  "+
		"	P_Credits.P_Credits_ID, "+
		"	P_Employee_Credits.P_Method_Credits_ID "+
		"  From P_Employee_Credits,  "+
		"	   P_Gain_Credits,  "+
		"	   P_Credits,  "+
		"	   P_Gain_Parameter,  "+
		"	   P_Booklet_Detail,  "+
		"	   P_employee ,   "+
		"	   P_UOM  "+
		" Where P_Gain_Credits.IsActive = 'Y'  "+
		"   And P_Credits.IsActive = 'Y'  "+
		"   And P_Credits.IsPrinted = 'Y' "+
		"   And P_Gain_Credits.P_Credits_ID = P_Credits.P_Credits_ID  "+
		"   AND P_Gain_Parameter.P_Gain_Parameter_ID = P_Gain_Credits.P_Gain_Parameter_ID  "+
		"   And P_Gain_Parameter.P_Gain_ID = P_Booklet_Detail.P_Gain_ID  "+
		"   And P_Credits.P_UOM_ID = P_UOM.P_UOM_ID  "+
		"   And P_Booklet_Detail.P_Booklet_ID = "+bookletId+
		"   And P_Employee_Credits.P_Employee_ID = "+employeeId+
		"   And P_Employee_Credits.IsActive = 'Y'  "+
		"   And P_Employee_Credits.P_Credits_ID = P_Credits.P_Credits_ID  "+
		"   And P_Employee.p_employee_id = p_employee_credits.p_employee_id  "+
		"   And ISNULL(P_Gain_Credits.P_Collective_Labour_Agr_ID,0) in   "+
		"	   (SELECT ISNULL(Max (P_Collective_Labour_Agr_ID),0)  "+
		"		  FROM P_Gain_Credits credits "+
		"		 WHERE credits.p_gain_parameter_id = p_gain_credits.p_gain_parameter_id  "+
		"		   and credits.p_credits_id = p_gain_credits.p_credits_id  "+
		"		   and credits.credits_function = p_gain_credits.credits_function "+
		"		   and (Credits.P_Collective_Labour_Agr_ID is null  "+
		"			or Credits.P_Collective_Labour_Agr_ID = P_employee.P_Collective_Labour_Agr_ID) ) "+ 
		"GROUP BY P_Credits.Name, P_Credits.Value, P_Credits.P_Credits_ID, P_UOM.Value, P_Employee_Credits.P_Method_Credits_ID  ";

		PreparedStatement pstmGain = DB.prepareStatement(strGain, null);
		ResultSet rsGain;

		try
		{
			rsGain = pstmGain.executeQuery();
			while(rsGain.next())
			{
				m_nameG.add(rsGain.getString("Credits_Name"));
				m_soldeG.add(rsGain.getBigDecimal("Solde"));
				m_unitG.add(rsGain.getString("Unit"));
				m_valueG.add(rsGain.getString("Value"));
				m_idG.add(new Integer(rsGain.getInt("P_Credits_ID")));
//				errorMsg = recursiveCheckBanksAlerts(rsGain.getInt("P_Method_Credits_ID"), employeeId);
				for(int i=0;i<m_nameG.size();i++)
				{
					name.add(m_nameG.get(i));
					solde.add(m_soldeG.get(i));
					unit.add(m_unitG.get(i));
					value.add(m_valueG.get(i));
				}
				m_nameG.clear();
				m_soldeG.clear();
				m_unitG.clear();
				m_valueG.clear();
				m_idG.clear();
			}
			rsGain.close();
			pstmGain.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new JspException("Erreur SQL sur la période ou sur l'employé.\nVeuillez contacter votre administrateur système.");			
		}
		
		//consolidation des mouvements
		for(int idx=0;idx<value.size();idx++)
		{
			for(int idx2=idx+1;idx2<value.size();idx2++)
			{
				//si deux mouvement viennent de la meme banque, on met le stock dans le premier, met le 2eme a 0
				if(value.get(idx).toString().equals(value.get(idx2).toString()))
				{
					BigDecimal firstSolde = (BigDecimal)solde.get(idx);
					BigDecimal secondSolde = (BigDecimal)solde.get(idx2);
					
					solde.set(idx, firstSolde.add(secondSolde));
					solde.set(idx2, "DUPLICATA");
				}
			}
		}
		

		retValue = "<table border=\"0\" width=\"100%\">";
		retValue += "<tr>";
		retValue += "<td class=\"tdlabel\">Banque</td>";
		retValue += "<td class=\"tdlabel\" align=\"right\">Solde</td>";
		retValue += "<td class=\"tdlabel\" colspan=\"2\" align=\"center\">&nbsp;</td>";
		retValue += "</tr>";
		for(int idx=0;idx<name.size();idx++)
		{
			if(!solde.get(idx).toString().equals("DUPLICATA"))
			{
				BigDecimal bdValue = (BigDecimal)solde.get(idx);
				retValue += "<tr>";
				retValue += "<td class=\"tdfield\">"+name.get(idx)+"</td>";
				retValue += "<td class=\"tdfield\" align=\"right\">"+bdValue.setScale(4)+"</td>";
				retValue += "<td class=\"tdfield\">"+unit.get(idx)+"</td><td class=\"tdfield\">"+value.get(idx)+"</td>";
				retValue += "</tr>";
			}
		}
		retValue += "</table>";
		
		name = null;
		solde = null;
		unit = null;
		value = null;

		return retValue;
	}


	private String recursiveCheckBanksAlerts(int methodCreditsID, int employeeID )
	{
		String errorMsg = null;
		String condition = new String( "=" );
		BigDecimal reminder = new BigDecimal("0");
		P_Employee employee = P_Employee.get(Env.getCtx(), employeeID, null);
		
		String strAlert = "Select P_Credits_Alert_ID From P_Credits_Alert WHERE IsActive='Y' ";
		strAlert += " AND P_Credits_ID=" +m_idG.get(m_idG.size()-1);
		strAlert += " AND P_Method_Credits_ID in(100," + methodCreditsID +")";

		
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;

		BigDecimal variation = (BigDecimal)(m_soldeG.get(m_soldeG.size()-1));
		String currentValue = m_valueG.get(m_valueG.size()-1).toString();
		BigDecimal previousSolde = Env.ZERO;
		
		for(int idx=0;idx<value.size();idx++)
		{
			if(value.get(idx).equals(currentValue))
			{
				previousSolde = previousSolde.add((BigDecimal)solde.get(idx));
			}
		}
		
		try
		{
			pstmt = DB.prepareStatement (strAlert, null);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				P_Credits_Alert creditsAlert = P_Credits_Alert.get( Env.getCtx (), rs.getInt(1), null);
			    switch ( Integer.parseInt( creditsAlert.getAlert_Condition() ) ) 
				{ 
					case 1: condition = "<";
							if( variation.add(previousSolde).compareTo(creditsAlert.getAlert_Limit()) == -1)
							{
								reminder = creditsAlert.getAlert_Limit().add(variation.add(previousSolde));
								//le reste de l'opération ne peut être plus gros que l'opération elle meme
							}
					break;
					case 2: condition = "<=";
							if(variation.add(previousSolde).compareTo(creditsAlert.getAlert_Limit()) == -1 || variation.add(previousSolde).compareTo(creditsAlert.getAlert_Limit()) == 0)
								reminder = creditsAlert.getAlert_Limit().add(variation.add(previousSolde));			
					break;
					case 3: condition = "<>";
						if(variation.add(previousSolde).compareTo(creditsAlert.getAlert_Limit()) != 0)
						{
							reminder = creditsAlert.getAlert_Limit().subtract(variation.add(previousSolde));
							//si remider est negatif, on le ramene en positif
							if(reminder.compareTo(Env.ZERO) == -1)
							{
								reminder.multiply(new BigDecimal(-1));
							}
						}
					break;
					case 4: condition = "=";
						if(variation.add(previousSolde).compareTo(creditsAlert.getAlert_Limit()) == 0)
							reminder = Env.ZERO;			
					break;
					case 5: condition = ">";
						if(variation.add(previousSolde).compareTo(creditsAlert.getAlert_Limit()) == 1)
							reminder = variation.add(previousSolde).subtract(creditsAlert.getAlert_Limit());			
					break;
					case 6: condition = ">=";
						if(variation.add(previousSolde).compareTo(creditsAlert.getAlert_Limit()) == 1 || variation.add(previousSolde).compareTo(creditsAlert.getAlert_Limit()) == 0)
							reminder = variation.add(previousSolde).subtract(creditsAlert.getAlert_Limit());			
					break;
				}
				String strEmpCheck = "Select 1 From P_Employee "  
				      +  "Where  P_Employee_ID = " + employee.getP_Employee_ID() + " And " + variation.add(previousSolde) + " " + condition + " " + creditsAlert.getAlert_Limit() ;
				
				if ( creditsAlert.getAlert_Option() != null )
				{
					switch ( Integer.parseInt( creditsAlert.getAlert_Option() ) )
					{ 
						case 1: strEmpCheck = strEmpCheck + " and P_Job_Type_ID = " + employee.getP_Job_Type_ID();
						break;
						case 2: strEmpCheck = strEmpCheck + " and P_Job_Title_ID = " + employee.getP_Job_Title_ID();
						break;
						case 3: strEmpCheck = strEmpCheck + " and P_Occupation_Group_ID = " + employee.getP_Occupation_Group_ID();
						break;
						case 4: strEmpCheck = strEmpCheck + " and P_Payment_Group_ID = " + employee.getP_Payment_Group_ID();
						break;
						case 5: strEmpCheck = strEmpCheck + " and P_Workplace_ID = " + employee.getP_Workplace_ID();
						break;
	     			}
				}
				    
				try
				{
					pstmt2 = DB.prepareStatement (strEmpCheck, null);
					System.out.println("Valide l'alert : "+strEmpCheck);
					ResultSet rs2 = pstmt2.executeQuery ();
					if ( rs2.next () )
					{

						// 2008-07-22 ajouts condition pour le Erreur avec banque liers ( vacance)
						if(creditsAlert.getAlert_Severity_Level().equals("C") || (creditsAlert.getAlert_Severity_Level().equals(P_Credits_Alert.ALERT_SEVERITY_LEVEL_Error) && creditsAlert.getP_Credits_Join_ID() != 0)  )
						{
								P_Credits credits_Alert_join = P_Credits.get(Env.getCtx (), creditsAlert.getP_Credits_Join_ID(), null );
								
								m_nameG.add(credits_Alert_join.getName());
								m_soldeG.add(reminder);
								m_unitG.add(m_unitG.get(m_unitG.size()-1));
								m_valueG.add(credits_Alert_join.getValue());
								m_idG.add(new Integer(credits_Alert_join.getP_Credits_ID()));

								
								m_soldeG.set(m_soldeG.size()-2, variation.subtract(reminder));
								System.out.println("DBGK2 "+credits_Alert_join.getName()+" | "+reminder.toString());
								errorMsg = recursiveCheckBanksAlerts(methodCreditsID, employeeID);
						}

						if(creditsAlert.getAlert_Severity_Level().equals( P_Credits_Alert.ALERT_SEVERITY_LEVEL_Error ) && creditsAlert.getP_Credits_Join_ID() == 0 ) // && actionSave
						{
							errorMsg = creditsAlert.getAlert_Message() + " | " +reminder.toString();
						}

						if ( errorMsg != null )
						{
							System.out.println("**DEBUG** errorMsg: " + errorMsg );
							rs2.close ();
							pstmt2.close ();
							rs.close ();
							pstmt.close ();
							return errorMsg;
						}
						
					}
					rs2.close ();
					pstmt2.close ();
					pstmt2 = null;
				}
				catch (Exception e)
				{
					System.out.println( "** ERROR ** " + e.toString());
					e.printStackTrace();
				}
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			System.out.println( "** ERROR ** " + e.toString());
			e.printStackTrace();
		}
		
		System.out.println("**DEBUG** Validation Credits return msg : " + errorMsg );
		return errorMsg;
		
	}
	
	
}
