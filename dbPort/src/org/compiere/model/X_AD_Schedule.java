/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2007 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software;
 you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program;
 if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.model;

/** Generated Model - DO NOT CHANGE */
import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;
/** Generated Model for AD_Schedule
 *  @author Jorg Janke (generated) 
 *  @version Release 2.6.1 - $Id: X_AD_Schedule.java,v 1.1 2007/07/18 15:00:53 marmor01 Exp $ */
public class X_AD_Schedule extends PO
{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/** Standard Constructor
@param ctx context
@param AD_Schedule_ID id
@param trxName transaction
*/
public X_AD_Schedule (Properties ctx, int AD_Schedule_ID, String trxName)
{
super (ctx, AD_Schedule_ID, trxName);
/** if (AD_Schedule_ID == 0)
{
setAD_Schedule_ID (0);
setName (null);
setOnFriday (true);	// Y
setOnMonday (true);	// Y
setOnSaturday (false);	// N
setOnSunday (false);	// N
setOnThursday (true);	// Y
setOnTuesday (true);	// Y
setOnWednesday (true);	// Y
setRunOnlySpecifiedTime (false);	// N
setScheduleType (null);	// F
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_AD_Schedule (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=916 */
public static final int Table_ID=916;

/** TableName=AD_Schedule */
public static final String Table_Name="AD_Schedule";

protected static KeyNamePair Model = new KeyNamePair(916,"AD_Schedule");

protected BigDecimal accessLevel = new BigDecimal(6);
/** AccessLevel
@return 6 - System - Client 
*/
protected int get_AccessLevel()
{
return accessLevel.intValue();
}
/** Load Meta Data
@param ctx context
@return PO Info
*/
protected POInfo initPO (Properties ctx)
{
POInfo poi = POInfo.getPOInfo (ctx, Table_ID);
return poi;
}
/** Info
@return info
*/
public String toString()
{
StringBuffer sb = new StringBuffer ("X_AD_Schedule[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Schedule.
@param AD_Schedule_ID Execution Schedule */
public void setAD_Schedule_ID (int AD_Schedule_ID)
{
if (AD_Schedule_ID < 1) throw new IllegalArgumentException ("AD_Schedule_ID is mandatory.");
set_ValueNoCheck ("AD_Schedule_ID", new Integer(AD_Schedule_ID));
}
/** Get Schedule.
@return Execution Schedule */
public int getAD_Schedule_ID() 
{
Integer ii = (Integer)get_Value("AD_Schedule_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Description.
@param Description Optional short description of the record */
public void setDescription (String Description)
{
if (Description != null && Description.length() > 255)
{
log.warning("Length > 255 - truncated");
Description = Description.substring(0,254);
}
set_Value ("Description", Description);
}
/** Get Description.
@return Optional short description of the record */
public String getDescription() 
{
return (String)get_Value("Description");
}
/** Set Frequency.
@param Frequency Frequency of events */
public void setFrequency (int Frequency)
{
set_Value ("Frequency", new Integer(Frequency));
}
/** Get Frequency.
@return Frequency of events */
public int getFrequency() 
{
Integer ii = (Integer)get_Value("Frequency");
if (ii == null) return 0;
return ii.intValue();
}

/** FrequencyType AD_Reference_ID=221 */
public static final int FREQUENCYTYPE_AD_Reference_ID=221;
/** Day = D */
public static final String FREQUENCYTYPE_Day = "D";
/** Hour = H */
public static final String FREQUENCYTYPE_Hour = "H";
/** Minute = M */
public static final String FREQUENCYTYPE_Minute = "M";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isFrequencyTypeValid (String test)
{
return test == null || test.equals("D") || test.equals("H") || test.equals("M");
}
/** Set Frequency Type.
@param FrequencyType Frequency of event */
public void setFrequencyType (String FrequencyType)
{
if (!isFrequencyTypeValid(FrequencyType))
throw new IllegalArgumentException ("FrequencyType Invalid value - " + FrequencyType + " - Reference_ID=221 - D - H - M");
if (FrequencyType != null && FrequencyType.length() > 1)
{
log.warning("Length > 1 - truncated");
FrequencyType = FrequencyType.substring(0,0);
}
set_Value ("FrequencyType", FrequencyType);
}
/** Get Frequency Type.
@return Frequency of event */
public String getFrequencyType() 
{
return (String)get_Value("FrequencyType");
}
/** Set Comment/Help.
@param Help Comment or Hint */
public void setHelp (String Help)
{
if (Help != null && Help.length() > 2000)
{
log.warning("Length > 2000 - truncated");
Help = Help.substring(0,1999);
}
set_Value ("Help", Help);
}
/** Get Comment/Help.
@return Comment or Hint */
public String getHelp() 
{
return (String)get_Value("Help");
}
/** Set Day of the Month.
@param MonthDay Day of the month 1 to 28/29/30/31 */
public void setMonthDay (int MonthDay)
{
set_Value ("MonthDay", new Integer(MonthDay));
}
/** Get Day of the Month.
@return Day of the month 1 to 28/29/30/31 */
public int getMonthDay() 
{
Integer ii = (Integer)get_Value("MonthDay");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Name.
@param Name Alphanumeric identifier of the entity */
public void setName (String Name)
{
if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
if (Name.length() > 120)
{
log.warning("Length > 120 - truncated");
Name = Name.substring(0,119);
}
set_Value ("Name", Name);
}
/** Get Name.
@return Alphanumeric identifier of the entity */
public String getName() 
{
return (String)get_Value("Name");
}
/** Get Record ID/ColumnName
@return ID/ColumnName pair */
public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), getName());
}
/** Set Friday.
@param OnFriday Available on Fridays */
public void setOnFriday (boolean OnFriday)
{
set_Value ("OnFriday", new Boolean(OnFriday));
}
/** Get Friday.
@return Available on Fridays */
public boolean isOnFriday() 
{
Object oo = get_Value("OnFriday");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Monday.
@param OnMonday Available on Mondays */
public void setOnMonday (boolean OnMonday)
{
set_Value ("OnMonday", new Boolean(OnMonday));
}
/** Get Monday.
@return Available on Mondays */
public boolean isOnMonday() 
{
Object oo = get_Value("OnMonday");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Saturday.
@param OnSaturday Available on Saturday */
public void setOnSaturday (boolean OnSaturday)
{
set_Value ("OnSaturday", new Boolean(OnSaturday));
}
/** Get Saturday.
@return Available on Saturday */
public boolean isOnSaturday() 
{
Object oo = get_Value("OnSaturday");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Sunday.
@param OnSunday Available on Sundays */
public void setOnSunday (boolean OnSunday)
{
set_Value ("OnSunday", new Boolean(OnSunday));
}
/** Get Sunday.
@return Available on Sundays */
public boolean isOnSunday() 
{
Object oo = get_Value("OnSunday");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Thursday.
@param OnThursday Available on Thursdays */
public void setOnThursday (boolean OnThursday)
{
set_Value ("OnThursday", new Boolean(OnThursday));
}
/** Get Thursday.
@return Available on Thursdays */
public boolean isOnThursday() 
{
Object oo = get_Value("OnThursday");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Tuesday.
@param OnTuesday Available on Tuesdays */
public void setOnTuesday (boolean OnTuesday)
{
set_Value ("OnTuesday", new Boolean(OnTuesday));
}
/** Get Tuesday.
@return Available on Tuesdays */
public boolean isOnTuesday() 
{
Object oo = get_Value("OnTuesday");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Wednesday.
@param OnWednesday Available on Wednesdays */
public void setOnWednesday (boolean OnWednesday)
{
set_Value ("OnWednesday", new Boolean(OnWednesday));
}
/** Get Wednesday.
@return Available on Wednesdays */
public boolean isOnWednesday() 
{
Object oo = get_Value("OnWednesday");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Only IP.
@param RunOnlyOnIP Run only on this IP address */
public void setRunOnlyOnIP (String RunOnlyOnIP)
{
if (RunOnlyOnIP != null && RunOnlyOnIP.length() > 40)
{
log.warning("Length > 40 - truncated");
RunOnlyOnIP = RunOnlyOnIP.substring(0,39);
}
set_Value ("RunOnlyOnIP", RunOnlyOnIP);
}
/** Get Only IP.
@return Run only on this IP address */
public String getRunOnlyOnIP() 
{
return (String)get_Value("RunOnlyOnIP");
}
/** Set Only Specified Time.
@param RunOnlySpecifiedTime Run the Process only at Specified Time */
public void setRunOnlySpecifiedTime (boolean RunOnlySpecifiedTime)
{
set_Value ("RunOnlySpecifiedTime", new Boolean(RunOnlySpecifiedTime));
}
/** Get Only Specified Time.
@return Run the Process only at Specified Time */
public boolean isRunOnlySpecifiedTime() 
{
Object oo = get_Value("RunOnlySpecifiedTime");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Tolerance Minutes.
@param RunOnlySpecifiedTolMin The tolerance in Minutes */
public void setRunOnlySpecifiedTolMin (int RunOnlySpecifiedTolMin)
{
set_Value ("RunOnlySpecifiedTolMin", new Integer(RunOnlySpecifiedTolMin));
}
/** Get Tolerance Minutes.
@return The tolerance in Minutes */
public int getRunOnlySpecifiedTolMin() 
{
Integer ii = (Integer)get_Value("RunOnlySpecifiedTolMin");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Target Hour.
@param ScheduleHour 24 hour target start time of the process */
public void setScheduleHour (int ScheduleHour)
{
set_Value ("ScheduleHour", new Integer(ScheduleHour));
}
/** Get Target Hour.
@return 24 hour target start time of the process */
public int getScheduleHour() 
{
Integer ii = (Integer)get_Value("ScheduleHour");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Target Minute.
@param ScheduleMinute Minute of process start time */
public void setScheduleMinute (int ScheduleMinute)
{
set_Value ("ScheduleMinute", new Integer(ScheduleMinute));
}
/** Get Target Minute.
@return Minute of process start time */
public int getScheduleMinute() 
{
Integer ii = (Integer)get_Value("ScheduleMinute");
if (ii == null) return 0;
return ii.intValue();
}

/** ScheduleType AD_Reference_ID=318 */
public static final int SCHEDULETYPE_AD_Reference_ID=318;
/** Frequency = F */
public static final String SCHEDULETYPE_Frequency = "F";
/** Month Day = M */
public static final String SCHEDULETYPE_MonthDay = "M";
/** Week Day = W */
public static final String SCHEDULETYPE_WeekDay = "W";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isScheduleTypeValid (String test)
{
return test.equals("F") || test.equals("M") || test.equals("W");
}
/** Set Schedule Type.
@param ScheduleType Type of schedule */
public void setScheduleType (String ScheduleType)
{
if (ScheduleType == null) throw new IllegalArgumentException ("ScheduleType is mandatory");
if (!isScheduleTypeValid(ScheduleType))
throw new IllegalArgumentException ("ScheduleType Invalid value - " + ScheduleType + " - Reference_ID=318 - F - M - W");
if (ScheduleType.length() > 1)
{
log.warning("Length > 1 - truncated");
ScheduleType = ScheduleType.substring(0,0);
}
set_Value ("ScheduleType", ScheduleType);
}
/** Get Schedule Type.
@return Type of schedule */
public String getScheduleType() 
{
return (String)get_Value("ScheduleType");
}

/** WeekDay AD_Reference_ID=167 */
public static final int WEEKDAY_AD_Reference_ID=167;
/** Monday = 1 */
public static final String WEEKDAY_Monday = "1";
/** Tuesday = 2 */
public static final String WEEKDAY_Tuesday = "2";
/** Wednesday = 3 */
public static final String WEEKDAY_Wednesday = "3";
/** Thursday = 4 */
public static final String WEEKDAY_Thursday = "4";
/** Friday = 5 */
public static final String WEEKDAY_Friday = "5";
/** Saturday = 6 */
public static final String WEEKDAY_Saturday = "6";
/** Sunday = 7 */
public static final String WEEKDAY_Sunday = "7";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isWeekDayValid (String test)
{
return test == null || test.equals("1") || test.equals("2") || test.equals("3") || test.equals("4") || test.equals("5") || test.equals("6") || test.equals("7");
}
/** Set Day of the Week.
@param WeekDay Day of the Week */
public void setWeekDay (String WeekDay)
{
if (!isWeekDayValid(WeekDay))
throw new IllegalArgumentException ("WeekDay Invalid value - " + WeekDay + " - Reference_ID=167 - 1 - 2 - 3 - 4 - 5 - 6 - 7");
if (WeekDay != null && WeekDay.length() > 1)
{
log.warning("Length > 1 - truncated");
WeekDay = WeekDay.substring(0,0);
}
set_Value ("WeekDay", WeekDay);
}
/** Get Day of the Week.
@return Day of the Week */
public String getWeekDay() 
{
return (String)get_Value("WeekDay");
}
}
