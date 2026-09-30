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
package solstice.model;

/** Generated Model - DO NOT CHANGE */
import org.compiere.model.*;
import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;
/** Generated Model for P_Booklet_Detail
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Booklet_Detail extends PO
{
/** Standard Constructor
@param ctx context
@param P_Booklet_Detail_ID id
@param trxName transaction
*/
public X_P_Booklet_Detail (Properties ctx, int P_Booklet_Detail_ID, String trxName)
{
super (ctx, P_Booklet_Detail_ID, trxName);
/** if (P_Booklet_Detail_ID == 0)
{
setP_Assignment_ID (0);
setP_Booklet_Detail_ID (0);
setP_Booklet_ID (0);
setP_Gain_ID (0);
setP_Period_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Booklet_Detail (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100736 */
public static final int Table_ID=2100736;

/** TableName=P_Booklet_Detail */
public static final String Table_Name="P_Booklet_Detail";

protected static KeyNamePair Model = new KeyNamePair(2100736,"P_Booklet_Detail");

protected BigDecimal accessLevel = new BigDecimal(3);
/** AccessLevel
@return 3 - Client - Org 
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
StringBuffer sb = new StringBuffer ("X_P_Booklet_Detail[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Crew cost.
@param C_Activity_ID Crew cost */
public void setC_Activity_ID (int C_Activity_ID)
{
if (C_Activity_ID <= 0) set_Value ("C_Activity_ID", null);
 else 
set_Value ("C_Activity_ID", new Integer(C_Activity_ID));
}
/** Get Crew cost.
@return Crew cost */
public int getC_Activity_ID() 
{
Integer ii = (Integer)get_Value("C_Activity_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** C_Campaign_ID AD_Reference_ID=141 */
public static final int C_CAMPAIGN_ID_AD_Reference_ID=141;
/** Set Campaign.
@param C_Campaign_ID Marketing Campaign */
public void setC_Campaign_ID (int C_Campaign_ID)
{
if (C_Campaign_ID <= 0) set_Value ("C_Campaign_ID", null);
 else 
set_Value ("C_Campaign_ID", new Integer(C_Campaign_ID));
}
/** Get Campaign.
@return Marketing Campaign */
public int getC_Campaign_ID() 
{
Integer ii = (Integer)get_Value("C_Campaign_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** C_Project_ID AD_Reference_ID=141 */
public static final int C_PROJECT_ID_AD_Reference_ID=141;
/** Set Project.
@param C_Project_ID Financial Project */
public void setC_Project_ID (int C_Project_ID)
{
if (C_Project_ID <= 0) set_Value ("C_Project_ID", null);
 else 
set_Value ("C_Project_ID", new Integer(C_Project_ID));
}
/** Get Project.
@return Financial Project */
public int getC_Project_ID() 
{
Integer ii = (Integer)get_Value("C_Project_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Day.
@param Day Day */
public void setDay (Timestamp Day)
{
set_Value ("Day", Day);
}
/** Get Day.
@return Day */
public Timestamp getDay() 
{
return (Timestamp)get_Value("Day");
}
/** Set Quantity.
@param DayQty Quantity */
public void setDayQty (BigDecimal DayQty)
{
set_Value ("DayQty", DayQty);
}
/** Get Quantity.
@return Quantity */
public BigDecimal getDayQty() 
{
BigDecimal bd = (BigDecimal)get_Value("DayQty");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set End Date.
@param EndDate Last effective date (inclusive) */
public void setEndDate (Timestamp EndDate)
{
set_Value ("EndDate", EndDate);
}
/** Get End Date.
@return Last effective date (inclusive) */
public Timestamp getEndDate() 
{
return (Timestamp)get_Value("EndDate");
}
/** Set Hourly Rate.
@param Hourly_Rate   */
public void setHourly_Rate (BigDecimal Hourly_Rate)
{
set_Value ("Hourly_Rate", Hourly_Rate);
}
/** Get Hourly Rate.
@return   */
public BigDecimal getHourly_Rate() 
{
BigDecimal bd = (BigDecimal)get_Value("Hourly_Rate");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Origin.
@param OriginTime Origin */
public void setOriginTime (String OriginTime)
{
if (OriginTime != null && OriginTime.length() > 3)
{
log.warning("Length > 3 - truncated");
OriginTime = OriginTime.substring(0,2);
}
set_Value ("OriginTime", OriginTime);
}
/** Get Origin.
@return Origin */
public String getOriginTime() 
{
return (String)get_Value("OriginTime");
}

/** P_Assignment_ID AD_Reference_ID=2100364 */
public static final int P_ASSIGNMENT_ID_AD_Reference_ID=2100364;
/** Set Assignment.
@param P_Assignment_ID Assignment */
public void setP_Assignment_ID (int P_Assignment_ID)
{
if (P_Assignment_ID < 1) throw new IllegalArgumentException ("P_Assignment_ID is mandatory.");
set_Value ("P_Assignment_ID", new Integer(P_Assignment_ID));
}
/** Get Assignment.
@return Assignment */
public int getP_Assignment_ID() 
{
Integer ii = (Integer)get_Value("P_Assignment_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_Booklet_Detail_CPA_ID.
@param P_Booklet_Detail_CPA_ID P_Booklet_Detail_CPA_ID */
public void setP_Booklet_Detail_CPA_ID (int P_Booklet_Detail_CPA_ID)
{
if (P_Booklet_Detail_CPA_ID <= 0) set_Value ("P_Booklet_Detail_CPA_ID", null);
 else 
set_Value ("P_Booklet_Detail_CPA_ID", new Integer(P_Booklet_Detail_CPA_ID));
}
/** Get P_Booklet_Detail_CPA_ID.
@return P_Booklet_Detail_CPA_ID */
public int getP_Booklet_Detail_CPA_ID() 
{
Integer ii = (Integer)get_Value("P_Booklet_Detail_CPA_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Booklet_Detail.
@param P_Booklet_Detail_ID Booklet_Detail */
public void setP_Booklet_Detail_ID (int P_Booklet_Detail_ID)
{
if (P_Booklet_Detail_ID < 1) throw new IllegalArgumentException ("P_Booklet_Detail_ID is mandatory.");
set_ValueNoCheck ("P_Booklet_Detail_ID", new Integer(P_Booklet_Detail_ID));
}
/** Get Booklet_Detail.
@return Booklet_Detail */
public int getP_Booklet_Detail_ID() 
{
Integer ii = (Integer)get_Value("P_Booklet_Detail_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Booklet.
@param P_Booklet_ID Booklet */
public void setP_Booklet_ID (int P_Booklet_ID)
{
if (P_Booklet_ID < 1) throw new IllegalArgumentException ("P_Booklet_ID is mandatory.");
set_ValueNoCheck ("P_Booklet_ID", new Integer(P_Booklet_ID));
}
/** Get Booklet.
@return Booklet */
public int getP_Booklet_ID() 
{
Integer ii = (Integer)get_Value("P_Booklet_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_Employee_LongTermLeave_ID.
@param P_Employee_LongTermLeave_ID P_Employee_LongTermLeave_ID */
public void setP_Employee_LongTermLeave_ID (int P_Employee_LongTermLeave_ID)
{
if (P_Employee_LongTermLeave_ID <= 0) set_Value ("P_Employee_LongTermLeave_ID", null);
 else 
set_Value ("P_Employee_LongTermLeave_ID", new Integer(P_Employee_LongTermLeave_ID));
}
/** Get P_Employee_LongTermLeave_ID.
@return P_Employee_LongTermLeave_ID */
public int getP_Employee_LongTermLeave_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_LongTermLeave_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Gain_ID AD_Reference_ID=2100317 */
public static final int P_GAIN_ID_AD_Reference_ID=2100317;
/** Set Gain.
@param P_Gain_ID Gain */
public void setP_Gain_ID (int P_Gain_ID)
{
if (P_Gain_ID < 1) throw new IllegalArgumentException ("P_Gain_ID is mandatory.");
set_ValueNoCheck ("P_Gain_ID", new Integer(P_Gain_ID));
}
/** Get Gain.
@return Gain */
public int getP_Gain_ID() 
{
Integer ii = (Integer)get_Value("P_Gain_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Period.
@param P_Period_ID Period of the Calendar   */
public void setP_Period_ID (int P_Period_ID)
{
if (P_Period_ID < 1) throw new IllegalArgumentException ("P_Period_ID is mandatory.");
set_ValueNoCheck ("P_Period_ID", new Integer(P_Period_ID));
}
/** Get Period.
@return Period of the Calendar   */
public int getP_Period_ID() 
{
Integer ii = (Integer)get_Value("P_Period_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Schedule.
@param P_Schedule_ID Répartition des heures de travail à l'intérieur d'une période donnée.  */
public void setP_Schedule_ID (int P_Schedule_ID)
{
if (P_Schedule_ID <= 0) set_Value ("P_Schedule_ID", null);
 else 
set_Value ("P_Schedule_ID", new Integer(P_Schedule_ID));
}
/** Get Schedule.
@return Répartition des heures de travail à l'intérieur d'une période donnée.  */
public int getP_Schedule_ID() 
{
Integer ii = (Integer)get_Value("P_Schedule_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Salary Percentage.
@param SalaryPercentage Salary Percentage */
public void setSalaryPercentage (BigDecimal SalaryPercentage)
{
set_Value ("SalaryPercentage", SalaryPercentage);
}
/** Get Salary Percentage.
@return Salary Percentage */
public BigDecimal getSalaryPercentage() 
{
BigDecimal bd = (BigDecimal)get_Value("SalaryPercentage");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Start Date.
@param StartDate First effective day (inclusive) */
public void setStartDate (Timestamp StartDate)
{
set_Value ("StartDate", StartDate);
}
/** Get Start Date.
@return First effective day (inclusive) */
public Timestamp getStartDate() 
{
return (Timestamp)get_Value("StartDate");
}
/** Set Week Index.
@param WeekIndex Week Index */
public void setWeekIndex (int WeekIndex)
{
set_Value ("WeekIndex", new Integer(WeekIndex));
}
/** Get Week Index.
@return Week Index */
public int getWeekIndex() 
{
Integer ii = (Integer)get_Value("WeekIndex");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Time code.
@param P_Timecode_ID Time code */
public void setP_Timecode_ID (int P_Timecode_ID)
{
if (P_Timecode_ID <= 0) set_Value ("P_Timecode_ID", null);
 else 
set_Value ("P_Timecode_ID", new Integer(P_Timecode_ID));
}
/** Get Time code.
@return Time code */
public int getP_Timecode_ID() 
{
Integer ii = (Integer)get_Value("P_Timecode_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** Set Start Time.
@param StartTime Time started */
public void setStartTime (String StartTime)
{
if (StartTime != null && StartTime.length() > 10)
{
log.warning("Length > 10 - truncated");
StartTime = StartTime.substring(0,9);
}
set_Value ("StartTime", StartTime);
}
/** Get Start Time.
@return Time started */
public String getStartTime() 
{
return (String)get_Value("StartTime");
}

/** Set End Time.
@param EndTime End of the time span */
public void setEndTime (String EndTime)
{
if (EndTime != null && EndTime.length() > 10)
{
log.warning("Length > 10 - truncated");
EndTime = EndTime.substring(0,9);
}
set_Value ("EndTime", EndTime);
}
/** Get End Time.
@return End of the time span */
public String getEndTime() 
{
return (String)get_Value("EndTime");
}


}
