/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.
 * This program is free software; you can redistribute it and/or modify it
 * under the terms version 2 of the GNU General Public License as published
 * by the Free Software Foundation. This program is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along 
 * with this program; if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 * You may reach us at: ComPiere, Inc. - http://www.compiere.org/license.html
 * 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA or info@compiere.org 
 *****************************************************************************/
package org.compiere.model;

import java.net.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.*;
import org.compiere.util.*;


/**
 *	Schedule Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MSchedule.java,v 1.1 2007/07/18 14:39:23 marmor01 Exp $
 */
public class MSchedule extends X_AD_Schedule
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Schedule from Cache
	 *	@param ctx context
	 *	@param AD_Schedule_ID id
	 *	@return MSchedule
	 */
	public static MSchedule get(Properties ctx, int AD_Schedule_ID)
	{
		Integer key = new Integer (AD_Schedule_ID);
		MSchedule retValue = (MSchedule)s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MSchedule (ctx, AD_Schedule_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**	Cache						*/
	private static CCache<Integer, MSchedule> s_cache 
		= new CCache<Integer, MSchedule> ("AD_Schedule", 20);
	
	
	/**************************************************************************
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param AD_Schedule_ID id
	 *	@param trxName trx
	 */
	public MSchedule(Properties ctx, int AD_Schedule_ID, String trxName)
	{
		super (ctx, AD_Schedule_ID, trxName);
		if (AD_Schedule_ID == 0)
		{
		//	setName (null);
			setScheduleType (SCHEDULETYPE_Frequency);
			setFrequencyType (FREQUENCYTYPE_Day);
			setFrequency (1);
			//
			setOnMonday (true);	// Y
			setOnTuesday (true);	// Y
			setOnWednesday (true);	// Y
			setOnThursday (true);	// Y
			setOnFriday (true);	// Y
			setOnSaturday (false);	// N
			setOnSunday (false);	// N
			//
			setRunOnlySpecifiedTime (false);	// N
		}	}	//	MSchedule

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MSchedule(Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MSchedule

	
	/**
	 * 	Get Month Day
	 *	@return 1 .. 31
	 */
	public int getMonthDay()
	{
		int day = super.getMonthDay();
		if (day < 1)
			day = 1;
		else if (day > 31)
			day = 31;
		return day;
	}	//	getMonthDay
	
	/**
	 * 	Get WeekDay
	 *	@return WeekDay
	 */
	public String getWeekDay()
	{
		String wd = super.getWeekDay();
		if (wd == null || isWeekDayValid(wd))
			wd = WEEKDAY_Monday;
		return wd;
	}	//	getWeekDay
	
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave(boolean newRecord) 
	{
		if (getScheduleType() == null)
			setScheduleType (SCHEDULETYPE_Frequency);
		
		//	Set Schedule Type & Frequencies
		if (SCHEDULETYPE_Frequency.equals(getScheduleType()))
		{
			if (getFrequencyType() == null)
				setFrequencyType(FREQUENCYTYPE_Day);
			if (getFrequency() < 1)
				setFrequency(1);
		}
		else if (SCHEDULETYPE_MonthDay.equals(getScheduleType()))
		{
			if (super.getMonthDay() < 1 || super.getMonthDay() > 31)
				setMonthDay(1);
		}
		else	//	SCHEDULETYPE_WeekDay
		{
			if (getScheduleType() == null)
				setScheduleType(SCHEDULETYPE_WeekDay);
			if (super.getWeekDay() == null)
				setWeekDay(WEEKDAY_Monday);
		}
		//	Hour/Minute
		if (getScheduleHour() > 23 || getScheduleHour() < 0)
			setScheduleHour(0);
		if (getScheduleMinute() > 59 || getScheduleMinute() < 0)
			setScheduleMinute(0);
		return true;
	}	//	beforeSave

	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer ("MSchedule[");
		sb.append (get_ID()).append ("-").append (getName());
		String scheduleType = getScheduleType();
		sb.append (",Type=").append(scheduleType);
		if (SCHEDULETYPE_Frequency.equals(scheduleType))
		{
			sb.append (",Frequency=").append(getFrequencyType())
				.append("*").append(getFrequency());
			if (isOnMonday())
				sb.append(",Mo");
			if (isOnTuesday())
				sb.append(",Tu");
			if (isOnWednesday())
				sb.append(",We");
			if (isOnThursday())
				sb.append(",Th");
			if (isOnFriday())
				sb.append(",Fr");
			if (isOnSaturday())
				sb.append(",Sa");
			if (isOnSunday())
				sb.append(",Su");
		}
		else if (SCHEDULETYPE_MonthDay.equals(scheduleType))
			sb.append(",Day=").append(getMonthDay());
		else
			sb.append(",Day=").append(getWeekDay());
		//
		sb.append (",HH=").append(getScheduleHour())
			.append (",MM=").append(getScheduleMinute());
		//
		sb.append ("]");
		return sb.toString ();
	}	//	toString
	
	/**
	 * 	Is it OK to Run process On IP of this box
	 *	@return
	 */
	public boolean isOKtoRunOnIP()
	{
		String ipOnly = getRunOnlyOnIP();
		if (ipOnly == null || ipOnly.length() == 0)
			return true;
		
		try
		{
			InetAddress box = InetAddress.getLocalHost();
			String ip = box.getHostAddress();
			if (ipOnly.indexOf(ip) != -1)
			{
				log.warning ("Not allowed here - IP=" + ip + " does not match " + ipOnly);
				return false;
			}
			log.fine ("Allowed here - IP=" + ip + " matches " + ipOnly);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
			return false;
		}
		return true;
	}	//	isOKtoRunOnIP
	
	
	/**
	 * 	Get Next Run
	 *	@param last in MS
	 *	@return next run in MS
	 */
	public long getNextRunMS (long last)
	{
		Calendar calNow = GregorianCalendar.getInstance();
		calNow.setTimeInMillis (last);
		//
		Calendar calNext = GregorianCalendar.getInstance();
		calNext.setTimeInMillis (last);
		calNext.set (Calendar.SECOND, 0);
		calNext.set (Calendar.MILLISECOND, 0);
		//
		int hour = getScheduleHour();
		int minute = getScheduleMinute();
		//
		String scheduleType = getScheduleType();
		if (SCHEDULETYPE_Frequency.equals(scheduleType))
		{
			String frequencyType = getFrequencyType();
			int frequency = getFrequency();
			
			/*****	DAY		******/
			if (X_R_RequestProcessor.FREQUENCYTYPE_Day.equals(frequencyType))
			{
				calNext.set (Calendar.HOUR_OF_DAY, hour);
				calNext.set (Calendar.MINUTE, minute);
				calNext.add(Calendar.DAY_OF_YEAR, frequency);
			}	//	Day
			
			/*****	HOUR	******/
			else if (X_R_RequestProcessor.FREQUENCYTYPE_Hour.equals(frequencyType))
			{
				calNext.set (Calendar.MINUTE, minute);
				calNext.add (Calendar.HOUR_OF_DAY, frequency);
			}	//	Hour
			
			/*****	MINUTE	******/
			else if (X_R_RequestProcessor.FREQUENCYTYPE_Minute.equals(frequencyType))
			{
				calNext.add(Calendar.MINUTE, frequency);
			}	//	Minute
		}
		
		/*****	MONTH	******/
		else if (SCHEDULETYPE_MonthDay.equals(scheduleType))
		{
			calNext.set (Calendar.HOUR, hour);
			calNext.set (Calendar.MINUTE, minute);
			//
			int day = getMonthDay();
			int dd = calNext.get(Calendar.DAY_OF_MONTH);
			int max = calNext.getActualMaximum (Calendar.DAY_OF_MONTH);
			int dayUsed = Math.min (day, max);
			//	Same Month
			if (dd < dayUsed)
				calNext.set (Calendar.DAY_OF_MONTH, dayUsed);
			else
			{
				if (calNext.get (Calendar.MONTH) == Calendar.DECEMBER)
				{
					calNext.add (Calendar.YEAR, 1);
					calNext.set (Calendar.MONTH, Calendar.JANUARY);
				}
				else
					calNext.add (Calendar.MONTH, 1);
				max = calNext.getActualMaximum (Calendar.DAY_OF_MONTH);
				dayUsed = Math.min (day, max);
				calNext.set (Calendar.DAY_OF_MONTH, dayUsed);
			}
		}	//	month
		
		/*****	WEEK	******/
		else // if (SCHEDULETYPE_WeekDay.equals(scheduleType))
		{
			String weekDay = getWeekDay();
			int dayOfWeek = 0;
			if (WEEKDAY_Monday.equals(weekDay))
				dayOfWeek = Calendar.MONDAY;
			else if (WEEKDAY_Tuesday.equals(weekDay))
				dayOfWeek = Calendar.TUESDAY;
			else if (WEEKDAY_Wednesday.equals(weekDay))
				dayOfWeek = Calendar.WEDNESDAY;
			else if (WEEKDAY_Thursday.equals(weekDay))
				dayOfWeek = Calendar.THURSDAY;
			else if (WEEKDAY_Friday.equals(weekDay))
				dayOfWeek = Calendar.FRIDAY;
			else if (WEEKDAY_Saturday.equals(weekDay))
				dayOfWeek = Calendar.SATURDAY;
			else if (WEEKDAY_Sunday.equals(weekDay))
				dayOfWeek = Calendar.SUNDAY;
			calNext.set (Calendar.DAY_OF_WEEK, dayOfWeek);
			calNext.set (Calendar.HOUR, hour);
			calNext.set (Calendar.MINUTE, minute);
			calNext.set (Calendar.SECOND, 0);
			calNext.set (Calendar.MILLISECOND, 0);
			//
			if (!calNext.after(calNow))
			{
				calNext.add (Calendar.WEEK_OF_YEAR, 1);
			}
		}	//	week
		
		long delta = calNext.getTimeInMillis() - calNow.getTimeInMillis();
		String info = "Now=" + calNow.getTime().toString() 
			+ ", Next=" + calNext.getTime().toString() 
			+ ", Delta=" + delta
			+ " " + toString();
		if (delta < 0)
		{
			log.warning(info);
		}
		else
			log.info (info);

		return calNext.getTimeInMillis();
	}	//	getNextRunMS

	/**
	 * 	Get Next
	 *	@param start start time
	 *	@param iterations no iterations
	 *	@return array of next
	 */
	public Timestamp[] getNext (Timestamp start, int iterations)
	{
		Timestamp[]nexts = new Timestamp[iterations];
		long startMS = start.getTime(); 
		for (int i = 0; i < nexts.length; i++)
		{
			Timestamp next = new Timestamp (getNextRunMS (startMS));
			startMS = next.getTime();
			nexts[i] = next;
		}
		return nexts;
	}	//	getNext
	
	
	/**************************************************************************
	 * 	Test
	 *	@param args
	 */
	public static void main(String[] args)
	{
		Compiere.startup (true);
		CLogMgt.setLevel (Level.FINE);
		MSchedule s = null;
		Timestamp start = TimeUtil.getDay (2007, 12, 01);
		
		/**	Test Case - Day		**/
		s = new MSchedule(Env.getCtx (), 0, null);
		s.log.info ("*** Day 2 ***");
		s.setScheduleType (SCHEDULETYPE_Frequency);
		s.setFrequencyType (FREQUENCYTYPE_Day);
		s.setFrequency (2);
	//	start = new Timestamp(System.currentTimeMillis()); 
		s.getNext (start, 10);

		/**	Test Case - Hour 	**/
		s = new MSchedule(Env.getCtx (), 0, null);
		s.log.info ("*** Hour 5 ***");
		s.setScheduleType (SCHEDULETYPE_Frequency);
		s.setFrequencyType (FREQUENCYTYPE_Hour);
		s.setFrequency (5);
	//	start = new Timestamp(System.currentTimeMillis()); 
		s.getNext (start, 10);

		/**	Test Case - Minute	**/
		s = new MSchedule(Env.getCtx (), 0, null);
		s.log.info ("*** Minute 15 ***");
		s.setScheduleType (SCHEDULETYPE_Frequency);
		s.setFrequencyType (FREQUENCYTYPE_Minute);
		s.setFrequency (15);
	//	start = new Timestamp(System.currentTimeMillis()); 
		s.getNext (start, 10);

		/**	Test Case - WeekDay	**/
		s = new MSchedule(Env.getCtx (), 0, null);
		s.log.info ("*** WeekDay Mo ***");
		s.setScheduleType (SCHEDULETYPE_WeekDay);
		s.setWeekDay (WEEKDAY_Monday);
	//	start = new Timestamp(System.currentTimeMillis()); 
		s.getNext (start, 92);
		
		/**	Test Case - Month	**/
		s = new MSchedule(Env.getCtx (), 0, null);
		s.log.info ("*** MonthDay 31 ***");
		s.setScheduleType (SCHEDULETYPE_MonthDay);
		s.setMonthDay (31);
		//	start = new Timestamp(System.currentTimeMillis()); 
		s.getNext (start, 14);
		/** **/
		
	}	//	main
	
}	//	MSchedule
