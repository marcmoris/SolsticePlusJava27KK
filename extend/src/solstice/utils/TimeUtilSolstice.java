/******************************************************************************
 * Product: Solstice+ Payroll & Human Resources Management                    *
 * Copyright (C) 2008 ProGestion Informatique, Inc. All Rights Reserved.      *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ProGestion Informatique, 210-5300 Bld des Galerie, Quebec, G2K 2A2 Canada  *
 * or via info@progestion.net or http://www.progestion.net/license.html       *
 ******************************************************************************/
package solstice.utils;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

//import org.compiere.common.constants.DisplayTypeConstants;
import org.compiere.util.*;


/**
 *	Time Utilities
 *
 *  @author Marc Morissette
 * 	@version 	$Id: TimeUtil.java,v 1.3 2006/07/30 00:54:35 jjanke Exp $
 */
public class TimeUtilSolstice
{

	/**
	 * 	Is it the same day
	 * 	@param one day
	 * 	@param two compared day
	 * 	@return true if the same day
	 */
	static public boolean isSameDay (Timestamp one, Timestamp two)
	{
		GregorianCalendar calOne = new GregorianCalendar();
		if (one != null)
			calOne.setTimeInMillis(one.getTime());
		GregorianCalendar calTwo = new GregorianCalendar();
		if (two != null)
			calTwo.setTimeInMillis(two.getTime());
		if (calOne.get(Calendar.YEAR) == calTwo.get(Calendar.YEAR)
			&& calOne.get(Calendar.MONTH) == calTwo.get(Calendar.MONTH)
// Progestion / Solstice 2008-04-09
			&& calOne.get(Calendar.DAY_OF_MONTH) == calTwo.get(Calendar.DAY_OF_MONTH))
//			&& calOne.get(Calendar.DAY_OF_MONTH) == calTwo.get(Calendar.DAY_OF_YEAR))
			return true;
		return false;
	}	//	isSameDay

	
	//+ Progestion Inf / Solstice.
	/** 
	 * 	Return Day + offset (truncates)
	 * 	@param day Day
	 * 	@param offset day offset
	 * 	@return Day + offset at 00:00
	 */
	static public Timestamp addYear (Timestamp day, int offset)
	{
		if (day == null)
			day = new Timestamp(System.currentTimeMillis());
		//
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(day);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		if (offset == 0)
			return new Timestamp (cal.getTimeInMillis());
		cal.add(Calendar.YEAR, offset);			//	may have a problem with negative (before 1/1)
		return new Timestamp (cal.getTimeInMillis());
	}	//	addYear


	/** 
	 * 	Return Day of week
	 * 	@param day Day
	 * 	@return 
	 */
	static public Calendar getCalendar (Timestamp day)
	{
		if (day == null)
			day = new Timestamp(System.currentTimeMillis());
		//
	    GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(day);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		return cal;
	}	//	a

	/** 
	 * 	Return Year
	 * 	@param day Day
	 * 	@return 
	 */
	static public int getYear (Timestamp day)
	{
		if (day == null)
			day = new Timestamp(System.currentTimeMillis());
		//
	    GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(day);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		return cal.get( Calendar.YEAR);
	}	//	getYear

	/** 
	 * 	Return Month Jan = 1 and Dec = 12
	 * 	@param day Day
	 * 	@return 
	 */
	static public int getMonth (Timestamp day)
	{
		if (day == null)
			day = new Timestamp(System.currentTimeMillis());
		//
	    GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(day);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.get( Calendar.MONTH) + 1;
	}	//	getMonth

	/** 
	 * 	Return day of month
	 * 	@param day Day
	 * 	@return 
	 */
	static public int getDay_Of_Month (Timestamp day)
	{
		if (day == null)
			day = new Timestamp(System.currentTimeMillis());
		//
	    GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(day);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.get( Calendar.DAY_OF_MONTH) ;
	}	//	getDay_Of_Month

	
	/** 
	 * 	Return day of week
	 * 	@param day Day
	 * 	@return 
	 */
	static public int getDay_Of_Week (Timestamp day)
	{
		if (day == null)
			day = new Timestamp(System.currentTimeMillis());
		//
	    GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(day);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.get( Calendar.DAY_OF_WEEK) ;
	}	//	getDay_Of_Month
	
	
	
	/**
	 * 	Return Day + offset (truncates)
	 * 	@param day Day
	 * 	@param offset month offset
	 * 	@return Day + offset at 00:00
	 */
	static public Timestamp addMonth (Timestamp day, int offset)
	{
		if (day == null)
			day = new Timestamp(System.currentTimeMillis());
		//
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(day);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		if (offset == 0)
			return new Timestamp (cal.getTimeInMillis());
		cal.add(Calendar.MONTH, offset);		
		return new Timestamp (cal.getTimeInMillis());
	}	//	addMonth

	
	/**
	 * 	Get last date in month
	 *  @param day day
	 *  @return last day with 00:00
	 */
	static public Timestamp getMonthLastDay (Timestamp day)
	{
		if (day == null)
			day = new Timestamp(System.currentTimeMillis());
		GregorianCalendar cal = new GregorianCalendar(Language.getLoginLanguage().getLocale());
		cal.setTimeInMillis(day.getTime());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		//
		cal.add(Calendar.MONTH, 1);			//	next
		cal.set(Calendar.DAY_OF_MONTH, 1);	//	first
		cal.add(Calendar.DAY_OF_YEAR, -1);	//	previous
		return new Timestamp (cal.getTimeInMillis());
	}	//	getNextDay

	//- Progestion Inf / Solstice.


	private static SimpleDateFormat m_dateFormat;
	private static int			m_displayType = DisplayType.Date;  //DisplayTypeConstants.Date;
	/**
	 * 	Set Format
	 *  Required when Format/Locale changed
	 */
	public static  String setFormat( Timestamp d )
	{
		m_dateFormat = DisplayType.getDateFormat(m_displayType);
		return m_dateFormat.format( d );
	}

	public static Timestamp StringToTimestamp( String d )
	{
		if ( d == null || d.equals(""))
			return null;
		
//		Date l = null;
		Timestamp l = null;
		try 
		{ 
			d = d.replace( 'T', ' ');
			System.out.println( "*DEBUG* Date " + d );
//			l = m_dateFormat.parse( d );
			
			if (d.length() == 10) 
				d = d + " 00:00:00.000000000";

			if (d.length() == 19) 
				d = d + ".000000000";

			
			l = Timestamp.valueOf( d );
		}
		catch (Exception e)   	{System.out.println("Exception : " + d + " "+ e);    }    
	
		return l;
//		return new Timestamp( l.getTime() );

	}


    /**
     * Retourne le nom du mois en français
     */
    public static String monthName(int month)
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

	
}	//	TimeUtil
