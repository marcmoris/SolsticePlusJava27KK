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
package solstice.model;

/** Generated Model - DO NOT CHANGE */
import org.compiere.model.*;
import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;
/** Generated Model for P_Punch_Time
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Punch_Time extends PO
{
/** Standard Constructor
@param ctx context
@param P_Punch_Time_ID id
@param trxName transaction
*/
public X_P_Punch_Time (Properties ctx, int P_Punch_Time_ID, String trxName)
{
super (ctx, P_Punch_Time_ID, trxName);
/** if (P_Punch_Time_ID == 0)
{
setP_Distribution_ID (0);
setP_Employee_ID (0);
setP_Gain_ID (0);
setP_Period_ID (0);
setP_PUNCH_TIME_ID (0);
setP_Schedule_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Punch_Time (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=3201196 */
public static final int Table_ID=3201196;

/** TableName=P_Punch_Time */
public static final String Table_Name="P_Punch_Time";

protected static KeyNamePair Model = new KeyNamePair(3201196,"P_Punch_Time");

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
StringBuffer sb = new StringBuffer ("X_P_Punch_Time[").append(get_ID()).append("]");
return sb.toString();
}
/** Set bizSubLabel.
@param bizSubLabel bizSubLabel */
public void setbizSubLabel (String bizSubLabel)
{
if (bizSubLabel != null && bizSubLabel.length() > 100)
{
log.warning("Length > 100 - truncated");
bizSubLabel = bizSubLabel.substring(0,99);
}
set_Value ("bizSubLabel", bizSubLabel);
}
/** Get bizSubLabel.
@return bizSubLabel */
public String getbizSubLabel() 
{
return (String)get_Value("bizSubLabel");
}
/** Set Day.
@param Day Day */
public void setDay (Timestamp Day)
{
set_ValueNoCheck ("Day", Day);
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
/** Set End Time.
@param EndTime End of the time span */
public void setEndTime (Timestamp EndTime)
{
set_ValueNoCheck ("EndTime", EndTime);
}
/** Get End Time.
@return End of the time span */
public Timestamp getEndTime() 
{
return (Timestamp)get_Value("EndTime");
}
/** Set HourlyRate.
@param HourlyRate HourlyRate */
public void setHourlyRate (BigDecimal HourlyRate)
{
set_Value ("HourlyRate", HourlyRate);
}
/** Get HourlyRate.
@return HourlyRate */
public BigDecimal getHourlyRate() 
{
BigDecimal bd = (BigDecimal)get_Value("HourlyRate");
if (bd == null) return Env.ZERO;
return bd;
}

/** P_Assignment_ID AD_Reference_ID=2000057 */
public static final int P_ASSIGNMENT_ID_AD_Reference_ID=2000057;
/** Set Assignment.
@param P_Assignment_ID Assignment */
public void setP_Assignment_ID (int P_Assignment_ID)
{
if (P_Assignment_ID <= 0) set_Value ("P_Assignment_ID", null);
 else 
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

/** P_Distribution_ID AD_Reference_ID=2000053 */
public static final int P_DISTRIBUTION_ID_AD_Reference_ID=2000053;
/** Set Distribution.
@param P_Distribution_ID Code for the check Distributio */
public void setP_Distribution_ID (int P_Distribution_ID)
{
if (P_Distribution_ID < 1) throw new IllegalArgumentException ("P_Distribution_ID is mandatory.");
set_Value ("P_Distribution_ID", new Integer(P_Distribution_ID));
}
/** Get Distribution.
@return Code for the check Distributio */
public int getP_Distribution_ID() 
{
Integer ii = (Integer)get_Value("P_Distribution_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Employee_ID AD_Reference_ID=2000010 */
public static final int P_EMPLOYEE_ID_AD_Reference_ID=2000010;
/** Set Employee .
@param P_Employee_ID Employee */
public void setP_Employee_ID (int P_Employee_ID)
{
if (P_Employee_ID < 1) throw new IllegalArgumentException ("P_Employee_ID is mandatory.");
set_ValueNoCheck ("P_Employee_ID", new Integer(P_Employee_ID));
}
/** Get Employee .
@return Employee */
public int getP_Employee_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_ID");
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
set_Value ("P_Gain_ID", new Integer(P_Gain_ID));
}
/** Get Gain.
@return Gain */
public int getP_Gain_ID() 
{
Integer ii = (Integer)get_Value("P_Gain_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Period_ID AD_Reference_ID=2000009 */
public static final int P_PERIOD_ID_AD_Reference_ID=2000009;
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

/** P_Post_ID AD_Reference_ID=2000004 */
public static final int P_POST_ID_AD_Reference_ID=2000004;
/** Set Post.
@param P_Post_ID Post */
public void setP_Post_ID (int P_Post_ID)
{
if (P_Post_ID <= 0) set_Value ("P_Post_ID", null);
 else 
set_Value ("P_Post_ID", new Integer(P_Post_ID));
}
/** Get Post.
@return Post */
public int getP_Post_ID() 
{
Integer ii = (Integer)get_Value("P_Post_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_PUNCH_TIME_ID.
@param P_PUNCH_TIME_ID P_PUNCH_TIME_ID */
public void setP_PUNCH_TIME_ID (int P_PUNCH_TIME_ID)
{
if (P_PUNCH_TIME_ID < 1) throw new IllegalArgumentException ("P_PUNCH_TIME_ID is mandatory.");
set_ValueNoCheck ("P_PUNCH_TIME_ID", new Integer(P_PUNCH_TIME_ID));
}
/** Get P_PUNCH_TIME_ID.
@return P_PUNCH_TIME_ID */
public int getP_PUNCH_TIME_ID() 
{
Integer ii = (Integer)get_Value("P_PUNCH_TIME_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Schedule_ID AD_Reference_ID=2000064 */
public static final int P_SCHEDULE_ID_AD_Reference_ID=2000064;
/** Set Schedule.
@param P_Schedule_ID Répartition des heures de travail à l'intérieur d'une période donnée.  */
public void setP_Schedule_ID (int P_Schedule_ID)
{
if (P_Schedule_ID < 1) throw new IllegalArgumentException ("P_Schedule_ID is mandatory.");
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
public void setStartTime (Timestamp StartTime)
{
set_ValueNoCheck ("StartTime", StartTime);
}
/** Get Start Time.
@return Time started */
public Timestamp getStartTime() 
{
return (Timestamp)get_Value("StartTime");
}
}
