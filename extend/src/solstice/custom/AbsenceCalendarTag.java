/*
 * Created on 2005-09-06
 */
package solstice.custom;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.compiere.util.DB;
import org.compiere.util.TimeUtil;

import solstice.process.PgiUtil;
import solstice.model.P_Employee;
import solstice.model.P_NonBusinessDay;

import org.compiere.util.Env;

/**
 * @author frafor01
 *
 * Crée le calendrier d'absence et effectue les validations
 * selon le temps standard
 */
public class AbsenceCalendarTag extends TagSupport
{
    /**
     * @author frafor01
     *
     * Cette classe dessine une journée dans le calendrier d'absence. Pour
     * effectuer ce traitement, on doit vérifier, dans la base de données,
     * si l'employé passé en paramètre doit travailler ou non à la date 
     * indiquée. Si oui, on vérifie si l'utilisateur a déjà une absence
     * pour cette journée. On prépare ensuite les validations avec le temps
     * standard.
     */
    private class MyDay
    {
        /*
         * Members
         */
        private int employeeId;
        private Calendar day;
        private boolean inRange;
        private int index;
        private boolean readOnly;
        private int absenceId;
        
        /**
         * Constructeur
         * 
         * @param employeeId L'employé à évaluer
         * @param day La journée dans le calendrier
         * @param inRange Indique si la journée fait partie du congé demandé
         */
        public MyDay(int employeeId, Calendar day, boolean inRange, int index, boolean readOnly, int absenceId)
        {
            this.employeeId = employeeId;
            this.day = day;
            this.inRange = inRange;
            this.index = index;
            this.readOnly = readOnly;
            this.absenceId = absenceId;
        }
        
        /**
         * Retourne la date sous le format dd MMMM
         */
        private String formatDate()
        {
            return this.formatDay(this.day.get(Calendar.DAY_OF_MONTH)) + " " + this.monthName(this.day.get(Calendar.MONTH));
        }
        
        /**
         * Format la journée sur deux chiffres
         */
        private String formatDay(int day)
        {
            if(day < 10)
                return "0" + day;
            return String.valueOf(day);
        }

        /**
         * Retourne le nom du mois en français
         */
        private String monthName(int month)
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
        
        public String getDay()
        {
            switch(this.day.get(Calendar.DAY_OF_WEEK))
            {
            case Calendar.SUNDAY: return "Sunday";
            case Calendar.MONDAY: return "Monday";
            case Calendar.TUESDAY: return "Tuesday";
            case Calendar.WEDNESDAY: return "Wednesday";
            case Calendar.THURSDAY: return "Thursday";
            case Calendar.FRIDAY: return "Friday";
            default: return "Saturday";
            }
        }
        
        /**
         * Retourne le nombre d'heure que l'employé a déjà pris en congé pour
         * la date courrante
         */
        private BigDecimal existingTime(int iAbsenceId)
        {
            BigDecimal time = null;
            String sql
            = "select isnull(sum(absenceDetail.Duration), 0) as Duration"
                + " from (P_Absence absence inner join P_Absence_Detail absenceDetail"
                + " on absence.P_Absence_ID = absenceDetail.P_Absence_ID)"
                + " inner join p_absence_status absenceStatus"
                + " on absence.p_absence_status_id = absenceStatus.p_absence_status_id"
                + " where absence.P_Employee_ID = " + this.employeeId
                + " and absenceDetail.AbsenceDate = ?"
		+ " and absence.P_Absence_ID <> "+iAbsenceId
                + " and absenceStatus.value not in ('REFU','DELE')"
                + " having sum(absenceDetail.Duration) is not null";
            
            try
            {
                PreparedStatement stmt = DB.prepareStatement(sql);
                stmt.setTimestamp(1, new Timestamp(this.day.getTimeInMillis()));
                ResultSet rs = stmt.executeQuery();
                
                // L'employé a déjà pris un congé cette date
                if(rs.next())
                {
                    time = rs.getBigDecimal("Duration");
                }
                
                rs.close();
                stmt.close();
                    
            }
            catch(SQLException e)
            {
                e.printStackTrace(System.out);
            }
            
            //
            // Jour férié
            //
            Timestamp NBDay = new Timestamp(this.day.getTimeInMillis());
            P_Employee Employee = P_Employee.get( Env.getCtx(), this.employeeId, null );
            P_NonBusinessDay NonBusinessDay = P_NonBusinessDay.get(Env.getCtx(), Employee.getP_Holidays_Calendar_ID(),  NBDay , null);
            
            if ( NonBusinessDay != null)
            	time = standardTime();
            
            //
            // Artt
            // TODO definir les informations ARTT sour l'employée
            //
	        GregorianCalendar cal = new GregorianCalendar();
	        cal.setTime( (Date)NBDay );
			if ( Employee.isRWT( NBDay ) && Employee.dayRWT( NBDay ) == cal.get(Calendar.DAY_OF_WEEK) )
			{
//				P_Assignment_RWT AssignmentRWT = Employee.GetAssignment_RWT( NBDay );
				time = standardTime(); // AssignmentRWT.getRwtQuantity();
			}
            
            return time == null ? new BigDecimal(0) : time;
        }
                /**
         * Retourne le nombre d'heure que l'employé a déjà pris en congé pour
         * la date courrante
         */
        private BigDecimal existingTime()
        {
            BigDecimal time = null;
            String sql
            = "select isnull(sum(absenceDetail.Duration), 0) as Duration"
                + " from (P_Absence absence inner join P_Absence_Detail absenceDetail"
                + " on absence.P_Absence_ID = absenceDetail.P_Absence_ID)"
                + " inner join p_absence_status absenceStatus"
                + " on absence.p_absence_status_id = absenceStatus.p_absence_status_id"
                + " where absence.P_Employee_ID = " + this.employeeId
                + " and absenceDetail.AbsenceDate = ?"
                + " and absenceStatus.value not in ('REFU','DELE')"
                + " having sum(absenceDetail.Duration) is not null";
            
            try
            {
                PreparedStatement stmt = DB.prepareStatement(sql);
                stmt.setTimestamp(1, new Timestamp(this.day.getTimeInMillis()));
                ResultSet rs = stmt.executeQuery();
                
                // L'employé a déjà pris un congé cette date
                if(rs.next())
                {
                    time = rs.getBigDecimal("Duration");
                }
                
                rs.close();
                stmt.close();
                    
            }
            catch(SQLException e)
            {
                e.printStackTrace(System.out);
            }

            //
            // Jour férié
            //
            P_Employee Employee = P_Employee.get( Env.getCtx(), this.employeeId, null );
            P_NonBusinessDay NonBusinessDay = P_NonBusinessDay.get(Env.getCtx(), Employee.getP_Holidays_Calendar_ID(),  new Timestamp(this.day.getTimeInMillis()), null);
            
            if ( NonBusinessDay != null)
            	time = standardTime();

            return time == null ? new BigDecimal(0) : time;
        }
        /**
         * Retourne le temps standard pour l'employé à cette journée
         */
        private BigDecimal standardTime()
        {
        	Timestamp time = new Timestamp(day.getTimeInMillis());
            BigDecimal standardTime = null;
            String sql
            = "select isnull(sum(" + this.getDay() + "), 0) as StandardTime"
                + " from P_Standard_Time"
                + " where P_Employee_ID = " + this.employeeId
             	+ " and isActive = 'Y'"
//Add 2008.10.20
             	+ " and exists(" 
             	+ "   select 1 from P_Gain_GainInfo, P_GainInfo "
             	+ "   where P_Gain_GainInfo.P_GainInfo_ID = P_GainInfo.P_GainInfo_ID "
             	+ "   AND P_GainInfo.Value = 'STD' "
             	+ "   AND P_Gain_GainInfo.To_Consider = 'Y' "
             	+ "   AND P_Gain_GainInfo.P_Gain_ID = P_Standard_Time.P_Gain_ID "
             	+ " )"
            	+ " and "+DB.TO_DATE(time)+" between startDate and isnull(endDate,"+DB.TO_DATE(time)+")";
            
            try
            {
                PreparedStatement stmt = DB.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                
                if(rs.next())
                {
                    standardTime = rs.getBigDecimal("StandardTime");
                }
                
                rs.close();
                stmt.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace(System.out);
            }
            
            return standardTime == null ? new BigDecimal(0) : standardTime;
        }
        
        /**
         * Dessine la cellule incluant les tag TD
         */
        public String drawTD() throws JspException
        {
            // Si la journée fait partie de la période d'absence demandée, on vérifie si
            // l'employé a déjà pris une absence pour cette journée. Si oui, on affiche
            // une case jaune et on soustrait du temps stantard la période de congé existante
            if(this.inRange)
            {
		
		int isMinute = 0;
		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
	        if(request.getParameter("timeFormat") != null
	                && !request.getParameter("timeFormat").equals(""))
		{
			String formatValue = request.getParameter("timeFormat");
			if(formatValue.indexOf(":") != -1)
			{
				isMinute = 1;
			}
		}


                String css = "tdfullselectcal";
                String autre = "&nbsp;";
                BigDecimal alreadyOccupied = this.existingTime(this.absenceId);
                
                // Si l'employé a déjà pris un congé pour cette date
                if(alreadyOccupied.doubleValue() != 0)
                {
                    css = "tdautreselectcal";
                    autre = "<b>Autre:</b> " + alreadyOccupied;
                }

                // On sélectionne maintenant le temps standard pour l'employé
                BigDecimal standardTime = this.standardTime();

                // Si l'employé ne devait pas travaille cette journée, on affiche
                // une journée verte

         	     // Modifier pour les employés qui n'ont pas de temps standard - 29 septembre 2008
           	 //

/*                if(standardTime.doubleValue() == 0)
                {
                    return "<td valign=\"top\" class=\"tdselectcal\">" + this.formatDate() + "</td>\n";
                }
*/
                
                // On charge les valeurs à afficher dans les texts
                String duration = null;
                String startHour = null;
                String endHour = null;
                String durCentile = "";
                
                if(this.absenceId <= 0)
                {
	                duration = this.getTime(standardTime.subtract(alreadyOccupied));
	                durCentile = duration;
	                if(isMinute == 1)
	                {
	                	duration = getHourInStandardFormat(duration);
	                }


	                startHour = "";
	                endHour = "";
                }
                else
                {
                    String sql
                    = "select convert(nvarchar(10), cast(Duration as decimal(10,2))) as Duration, "
                        + " isnull(Begining_Hour, '') as StartHour, isnull(Ending_Hour, '') as EndHour"
                        + " from P_Absence_Detail"
                        + " where P_Absence_ID = " + this.absenceId
                    	+ " and AbsenceDate = "+DB.TO_DATE(new Timestamp(this.day.getTimeInMillis()));
                    
                    try
                    {
                        PreparedStatement stmt = DB.prepareStatement(sql);
                        ResultSet rs = stmt.executeQuery();
                        if(rs.next())
                        {
                            duration = rs.getString("Duration");
                            durCentile = duration;
                            if(isMinute == 1)
                            {
                            	duration = getHourInStandardFormat(duration);
                            }
                            startHour = rs.getString("StartHour");
                            endHour = rs.getString("EndHour");
                        }
                        
                        rs.close();
                        stmt.close();
                    }
                    catch(SQLException e)
                    {
                        e.printStackTrace(System.out);
                    }
                    if(duration == null)
                    {
    	                duration = this.getTime(standardTime.subtract(alreadyOccupied));
    	                durCentile = duration;
    	                if(isMinute == 1)
    	                {
    	                	duration = getHourInStandardFormat(duration);
    	                }  
    	                startHour = "";
    	                endHour = "";
                    }
                }
                
                // Sinon, on dessine la cellule pour la saisie du congé
                String html = "<td valign=\"top\" class=\"" + css + "\">" + this.formatDate() + "\n"
	                    + "<table width=\"100%\" cellspacing=\"0\" cellpadding=\"1\" border=\"0\">\n"
		                + "<tr>\n"
		                + "<td><b>Dur&eacute;e</b></td>\n";
                if(readOnly)
                    html += "<td><input type=\"hidden\" name=\"" + this.getPrefix() + "_duration_cent\" id=\"" + this.getPrefix() + "_duration_cent\" value=\"" + durCentile + "\"><input type=\"hidden\" name=\"dateJournee\" value=\"" + this.getPrefix() + "_duration\"><input name=\"" + this.getPrefix() + "_duration\" size=\"4\" value=\"" + duration + "\" readonly=\"readonly\"></td>\n";
                else
              	     // Modifier pour les employés qui n'ont pas de temps standard - 29 septembre 2008
                	 //
                	if ( standardTime.compareTo( Env.ZERO ) == 0)
                		html += "<td><input type=\"hidden\" name=\"" + this.getPrefix() + "_duration_cent\" id=\"" + this.getPrefix() + "_duration_cent\" value=\"" + durCentile + "\"><input type=\"hidden\" name=\"dateJournee\" value=\"" + this.getPrefix() + "_duration\"><input onChange=\"updateCentile(this.name, this.value);\" type=\"text\" name=\"" + this.getPrefix() + "_duration" + "\" size=\"4\" value=\"" + duration + "\"></td>\n";
                	else
                		html += "<td><input type=\"hidden\" name=\"" + this.getPrefix() + "_duration_cent\" id=\"" + this.getPrefix() + "_duration_cent\" value=\"" + durCentile + "\"><input type=\"hidden\" name=\"dateJournee\" value=\"" + this.getPrefix() + "_duration\"><input onChange=\"this.style.background = (validDuration(this.value, " + this.getTime(standardTime.subtract(alreadyOccupied)) + ", " + this.index + ") ? 'white' : 'red');updateCentile(this.name, this.value);\" type=\"text\" name=\"" + this.getPrefix() + "_duration" + "\" size=\"4\" value=\"" + duration + "\"></td>\n";
                
		        html += "</tr>\n"
		                + "<tr>\n"
		                + "<td colspan=\"2\">" + autre + "</td>\n"
		                + "</tr>\n"
		                + "<tr>\n"
		                + "<td colspan=\"2\" align=\"right\">" + this.getHeureFromString(duration, isMinute) + "</td>\n"
		                + "</tr>\n"
		                + "<tr>\n"
		                + "<td><b>D&eacute;but</b></td>\n"
		                + "<td><b>Fin</b></td>\n"
		                + "</tr>\n"
		                + "<tr>\n";
		        if(readOnly)
		        {
		            html += "<td width=\"50%\" align=\"center\"><input id=\"" + this.getPrefix() + "_startHour" + "\" type=\"text\" size=\"4\" value=\"" + startHour + "\" readonly=\"readonly\"></td>\n";
		            html += "<td width=\"50%\" align=\"center\"><input id=\"" + this.getPrefix() + "_endHour" + "\" type=\"text\" size=\"4\" value=\"" + endHour + "\" readonly=\"readonly\"></td>\n";
		        }
		        else
		        {
	                html += "<td width=\"50%\" align=\"center\"><input id=\"" + this.getPrefix() + "_startHour" + "\" name=\"" + this.getPrefix() + "_startHour" + "\" value=\"" + startHour + "\" onChange=\"validTime(this.value, " + this.index + ");\" type=\"text\" size=\"4\"></td>\n";
	                html += "<td width=\"50%\" align=\"center\"><input id=\"" + this.getPrefix() + "_endHour" + "\" name=\"" + this.getPrefix() + "_endHour" + "\" value=\"" + endHour + "\" onChange=\"validTime(this.value, " + this.index + ");\" type=\"text\" size=\"4\"></td>\n";
		        }
		        html += "</tr>\n"
		                + "</table>\n"
		                + "</td>\n";
		        return html;
            }
            // Sinon, on doit vérifier si l'employé devait travailler ou non cette journée.
            // Si l'employé a pris un congé dans une autre jounée, on affiche simplement 
            // une case jaune
            String css;
            String autre;
            BigDecimal alreadyOccupied = this.existingTime(this.absenceId);
            // L'employé a déjà pris un congé
            if(alreadyOccupied.doubleValue() > 0)
            {
                css = "tdautreselectcal";
                autre = "<br><b>Autre :</b> " + alreadyOccupied.doubleValue();
            }
            // Il s'agit d'une journée travaillée
            else if(this.standardTime().doubleValue() > 0)
            {
                css = "tdcal";
                autre = "&nbsp;";
            }
            else
            {
                css = "tdselectcal";
                autre = "&nbsp;";
            }
            
            return "<td valign=\"top\" class=\"" + css + "\">" + this.formatDate() + autre + "</td>\n";
        }

        /**
         * Cette méthode retourne un script de validation pour obliger l'utilisateur à saisir des heures
         * de début et de fin si le temps saisi est plus petit que le temps standard pour une journée. Ce
         * script doit être intégré à partir d'une fonction de validation pour tout le calendrier.
         * @return Le script
         */
        public String GetValidationScript()
        {
        	if(!this.readOnly && this.inRange  )
        	{
	        	// On récupère d'abord le temps standard
	        	BigDecimal tempsStandard = this.standardTime();
	        	DecimalFormat formatter = new DecimalFormat("#0.000");
	        	
	        	// On écrit le script
	        	StringBuffer buffer = new StringBuffer();
	        	
	        	
	        	if ( tempsStandard.doubleValue() != 0 )
	        	{
		        	buffer.append("if(parseFloat(document.getElementById('" + this.getPrefix() + "_duration_cent').value) < " + formatter.format(tempsStandard.doubleValue()).replace(',', '.'));
		        	buffer.append("      && trim(document.getElementById('" + this.getPrefix() + "_startHour').value).length == 0");
		        	buffer.append("      && trim(document.getElementById('" + this.getPrefix() + "_endHour').value).length == 0");
		        	//+ 4 aout 2008 valide que la journé ne soit pas à 0 pour effectuer la validation
		        	buffer.append(" && parseFloat(document.getElementById('" + this.getPrefix() + "_duration_cent').value) != 0)");
		        	//-
		        	buffer.append("   return false;");
	        	}
	        	return buffer.toString();
        	}
        	return null;
        }
        
        /**
         * Retourne le préfix unique pour la journée
         */
        private String getPrefix()
        {
            return ("d" + String.valueOf(this.day.get(Calendar.YEAR))
                    + String.valueOf(this.day.get(Calendar.MONTH))
                    + String.valueOf(this.day.get(Calendar.DAY_OF_MONTH)));
        }
        
        private String getTime(BigDecimal value)
        {
            DecimalFormat format = new DecimalFormat("#0.00");
            return format.format(value.doubleValue()).replace(',','.');
        }
        
        private String getHeure(BigDecimal value)
        {
            double dvalue = value.doubleValue();
            DecimalFormat format = new DecimalFormat("00");
            String buffer = format.format(Math.floor(dvalue)) + " hre. ";
            buffer += format.format((dvalue - Math.floor(dvalue)) * 100) + " min.";
            return buffer;
        }
	private String getHeureFromString(String strParam, int isInMinute)
	{
		String strDuration = "";
		String retValue = "";
		if(isInMinute == 0)
		{					
			strDuration = getHourInStandardFormat(strParam);
		}
		else
		{
			strDuration = strParam;
		}

		retValue = strDuration.substring(0, strDuration.indexOf(":"));
		retValue += " hre. ";
		retValue += strDuration.substring(strDuration.indexOf(":")+1, strDuration.length());
		retValue += " min.";

		return retValue;
	}

	private String getHourInStandardFormat(String strHourInDecimal)
	{
		String retValue = "";
		BigDecimal bdT = new BigDecimal(strHourInDecimal);
	        BigDecimal bdSecondPart = bdT.subtract(bdT.setScale(0, BigDecimal.ROUND_DOWN)).multiply(new BigDecimal("100"));
 	        bdSecondPart = bdSecondPart.multiply(new BigDecimal("60")).divide(new BigDecimal("100"), 0, BigDecimal.ROUND_HALF_UP);
		String secondPart = bdSecondPart.toString();
		if(secondPart.length() == 0)
			secondPart = "00";
		else if(secondPart.length() == 1)
			secondPart = "0" + secondPart;
             	int indexOfDot = strHourInDecimal.indexOf(".");
                retValue = strHourInDecimal.substring(0, indexOfDot);
		if(retValue.length() == 0)
			retValue = "00";
		else if(retValue.length() == 1)
			retValue = "0" + retValue;
                retValue += ":" + secondPart;
		return retValue;
	}
    }

    /*
     * Attributs
     */
    private int employeeId;
    private String startDate;
    private String endDate;
    
    /*
     * Setters
     */
    public void setEmployeeId(int employeeId) {this.employeeId = employeeId;}
    public void setStartDate(String startDate) {this.startDate = startDate;}
    public void setEndDate(String endDate) {this.endDate = endDate;}
    
    /**
     * Constructeur
     */
    public AbsenceCalendarTag()
    {
        super();
    }
    
    /**
     * Vérifie si le statut de l'absence nous permet de
     * modifier le calendrier
     */
    private boolean getIsReadOnly(int absenceId)
    {
        boolean readOnly = false;
        String sql
        = "select 1"
            + " from P_Absence inner join P_Absence_Status"
            + " on P_Absence.P_Absence_Status_ID = P_Absence_Status.P_Absence_Status_ID"
            + " where P_Absence_Status.Value <> 'INIT'"
            + " and P_Absence.P_Absence_ID = " + absenceId;
        
        try
        {
            PreparedStatement stmt = DB.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            // Si le statut ne nous permet pas de modifier le calendrier
            if(rs.next())
            {
                readOnly = true;
            }
            
            rs.close();
            stmt.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace(System.out);
        }
        return readOnly;
    }
    
    /**
     * Dessine le calendrier
     */
    public int doEndTag() throws JspException
    {
        // On vérifie d'abord s'il y a une absence de passé en paramètre
        int absenceId = 0;
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        if(request.getParameter("absenceId") != null
                && !request.getParameter("absenceId").equals(""))
        {
            absenceId = Integer.parseInt(request.getParameter("absenceId"));
        }
        
        
        // On vérifie ensuite si le statut de l'absence nous permet de
        // modifier le calendrier
        boolean readOnly = this.getIsReadOnly(absenceId);
        
        JspWriter out = pageContext.getOut();
        
        try
        {
        
	        // On prépare les dates
	        Calendar debut = PgiUtil.stringToDateV2(this.startDate);
	        Calendar fin = PgiUtil.stringToDateV2(this.endDate);
	        Calendar current = GregorianCalendar.getInstance();
	        current.set(Calendar.MILLISECOND, debut.get(Calendar.MILLISECOND));
	        current.set(Calendar.SECOND, debut.get(Calendar.SECOND));
	        current.set(Calendar.MINUTE, debut.get(Calendar.MINUTE));
	        current.set(Calendar.HOUR, debut.get(Calendar.HOUR));
	        current.set(Calendar.DAY_OF_MONTH, debut.get(Calendar.DAY_OF_MONTH));
	        current.set(Calendar.MONTH, debut.get(Calendar.MONTH));
	        current.set(Calendar.YEAR, debut.get(Calendar.YEAR));
	        current.set(Calendar.AM_PM, debut.get(Calendar.AM_PM));
	        
	        // Le calendrier doit partir sur un dimanche
	        while(current.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
	            current.add(Calendar.DAY_OF_MONTH, -1);
	        
	        
	        out.println("<table border=\"0\" cellspacing=\"1\" cellpadding=\"1\" width=\"90%\">");
	        out.println("<tr>");
            out.println("<td width=\"15%\" align=\"center\" class=\"tdlabelcal\">Dimanche</td>");
            out.println("<td width=\"14%\" align=\"center\" class=\"tdlabelcal\">Lundi</td>");
            out.println("<td width=\"14%\" align=\"center\" class=\"tdlabelcal\">Mardi</td>");
            out.println("<td width=\"14%\" align=\"center\" class=\"tdlabelcal\">Mercredi</td>");
            out.println("<td width=\"14%\" align=\"center\" class=\"tdlabelcal\">Jeudi</td>");
            out.println("<td width=\"14%\" align=\"center\" class=\"tdlabelcal\">Vendredi</td>");
            out.println("<td width=\"15%\" align=\"center\" class=\"tdlabelcal\">Samedi</td>");
            out.println("</tr>");
	        
            Vector days = new Vector();
            int index = 0;
	        // Le calendrier doit se terminer sur le samedi suivant la fin de la période de congé
 	        while(current.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
	                || current.getTimeInMillis() < fin.getTimeInMillis())
	        {
	            if(current.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
	                out.println("<tr>");
	            
	            // On crée la journée
	            MyDay day = new MyDay(
	                    this.employeeId, 
	                    (Calendar)current.clone(),
	                    current.getTimeInMillis() >= debut.getTimeInMillis()
	                    && current.getTimeInMillis() <= fin.getTimeInMillis(), index++, readOnly, absenceId);
	            
	            out.println(day.drawTD());
	            
	            days.add(day);
	            
	            if(current.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
	                out.println("</tr>");

	            // On passe au prochain jour
	            current.add(Calendar.DAY_OF_MONTH, 1);
	        }
            // On crée le dernier samedi
            MyDay day = new MyDay(
                    this.employeeId, 
                    (Calendar)current.clone(),
                    current.getTimeInMillis() >= debut.getTimeInMillis()
                    && current.getTimeInMillis() <= fin.getTimeInMillis(), index, readOnly, absenceId);
            
            out.println(day.drawTD());
            days.add(day);
            
            out.println("</tr>");
            out.println("</table>");
            
            // On doit ajouter un script pour valider que si l'utilisateur entre un temps plus petit
            // que le temps standard pour une journée, il soit obligé de saisir une heure de début
            // et une heure de fin pour son congé
            out.println("<script>");
            out.println("function absenceCalendar_validerTempsStandard() {");
            Enumeration daysEnumeration = days.elements();
            while(daysEnumeration.hasMoreElements())
            {
            	//TODO Modif ARTT 
            	MyDay d = (MyDay)daysEnumeration.nextElement();

            	Timestamp ts = new Timestamp(d.day.getTimeInMillis());
            	int liDay = TimeUtil.getDay_Of_Week(ts);
            	P_Employee Employee = P_Employee.get( Env.getCtx(), this.employeeId, null );
            	int liDayRWT = Employee.isDayRWT(ts);
            
	        	if (liDayRWT != liDay )
	        	{
	        		
	        		if ( d.GetValidationScript() != null)
	        			out.println(d.GetValidationScript());
	        	}

            }	
            
            out.println("return true;");
            out.println("}");
            out.println("</script>");
	        
	        return EVAL_PAGE;
        }
        
        catch (IOException e)
        {
            throw new JspException(e);
        }
    }
}
