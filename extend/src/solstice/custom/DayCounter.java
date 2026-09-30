/*
 * Created on 31-Aug-2005
 */
package solstice.custom;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.compiere.util.DB;

/**
 * @author frafor01
 *
 * Cette classe prend un numéro de période en paramètre
 * et affiche les jours selon le format dd-MMMM
 */
public class DayCounter
{
    /*
     * Members
     */
    private GregorianCalendar currentDate = null;
    private int periodId;
    
    /**
     * Constructeur
     */
    public DayCounter(int periodId)
    {
        this.periodId = periodId;
    }
    
    /**
     * Passe au prochain jour et renvoit le résultat formaté
     */
    public String nextDay()
    {
        // Si c'est la première fois qu'on utilise cette méthode,
        // le prochain jour c'est le premier jour de la période
        if(this.currentDate == null)
        {
            String sql = "select StartDate from P_Period where P_Period_ID = " + this.periodId;
            
            try
            {
                PreparedStatement stmt = DB.prepareStatement(sql, null);
                ResultSet rs = stmt.executeQuery();
                
                if(rs.next())
                {
                    Timestamp startDate = rs.getTimestamp("StartDate");
                    this.currentDate = (GregorianCalendar)GregorianCalendar.getInstance();
                    this.currentDate.setTime(startDate);
                }
                
                rs.close();
                stmt.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace(System.out);
            }
        }
        // Si on a déjà une date courrante, on doit passer au 
        // prochain jour
        else
        {
            this.currentDate.add(GregorianCalendar.DAY_OF_MONTH, 1);
        }
        
        // On affiche la date
        return formatDay(this.currentDate.get(GregorianCalendar.DAY_OF_MONTH)) + "-" 
        + monthName(this.currentDate.get(GregorianCalendar.MONTH));
    }
    
    public static String FormatDate(Timestamp ts)
    {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(ts.getTime());
        return formatDay(calendar.get(GregorianCalendar.DAY_OF_MONTH)) + "-" 
        + monthName(calendar.get(GregorianCalendar.MONTH));
    }
    
    /**
     * Renvoit la date courrante
     */
    public Timestamp getCurrentDate()
    {
        if(this.currentDate != null)
            return new Timestamp(this.currentDate.getTimeInMillis());
        return null;
    }
    
    /**
     * Renvoit la date courrante sous le format demandé
     */
    public String getFormattedDate(String format)
    {
        if(this.currentDate != null)
        {
	        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
	        Timestamp date = new Timestamp(this.currentDate.getTimeInMillis());
	        return dateFormat.format(date);
        }
        return "";
    }
    
    /**
     * Format la journée sur deux chiffres
     */
    private static String formatDay(int day)
    {
        if(day < 10)
            return "0" + day;
        return String.valueOf(day);
    }

    /**
     * Retourne le nom du mois en français
     */
    private static String monthName(int month)
    {
        switch(month)
        {
        case GregorianCalendar.JANUARY: return "Janvier";
        case GregorianCalendar.FEBRUARY: return "F&eacute;vrier";
        case GregorianCalendar.MARCH: return "Mars";
        case GregorianCalendar.APRIL: return "Avril";
        case GregorianCalendar.MAY: return "Mai";
        case GregorianCalendar.JUNE: return "Juin";
        case GregorianCalendar.JULY: return "Juillet";
        case GregorianCalendar.AUGUST: return "Ao&ucirc;t";
        case GregorianCalendar.SEPTEMBER: return "Septembre";
        case GregorianCalendar.OCTOBER: return "Octobre";
        case GregorianCalendar.NOVEMBER: return "Novembre";
        default: return "D&eacute;cembre";
        }
    }
}
