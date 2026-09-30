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
/** Generated Model for P_Employee_LongTermLeave
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Employee_LongTermLeave extends PO
{
/** Standard Constructor
@param ctx context
@param P_Employee_LongTermLeave_ID id
@param trxName transaction
*/
public X_P_Employee_LongTermLeave (Properties ctx, int P_Employee_LongTermLeave_ID, String trxName)
{
super (ctx, P_Employee_LongTermLeave_ID, trxName);
/** if (P_Employee_LongTermLeave_ID == 0)
{
setLongtermLeaveMth (null);
setP_Employee_ID (0);
setP_Employee_LongTermLeave_ID (0);
setP_LongTermLeave_ID (0);
setStartDate (new Timestamp(System.currentTimeMillis()));
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Employee_LongTermLeave (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100713 */
public static final int Table_ID=2100713;

/** TableName=P_Employee_LongTermLeave */
public static final String Table_Name="P_Employee_LongTermLeave";

protected static KeyNamePair Model = new KeyNamePair(2100713,"P_Employee_LongTermLeave");

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
StringBuffer sb = new StringBuffer ("X_P_Employee_LongTermLeave[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Amount.
@param Amount Amount in a defined currency */
public void setAmount (BigDecimal Amount)
{
set_Value ("Amount", Amount);
}
/** Get Amount.
@return Amount in a defined currency */
public BigDecimal getAmount() 
{
BigDecimal bd = (BigDecimal)get_Value("Amount");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Comments.
@param Comments Comments or additional information */
public void setComments (String Comments)
{
if (Comments != null && Comments.length() > 255)
{
log.warning("Length > 255 - truncated");
Comments = Comments.substring(0,254);
}
set_Value ("Comments", Comments);
}
/** Get Comments.
@return Comments or additional information */
public String getComments() 
{
return (String)get_Value("Comments");
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
/** Set Friday.
@param Friday Friday */
public void setFriday (BigDecimal Friday)
{
set_Value ("Friday", Friday);
}
/** Get Friday.
@return Friday */
public BigDecimal getFriday() 
{
BigDecimal bd = (BigDecimal)get_Value("Friday");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Method.
@param LongtermLeaveMth Method */
public void setLongtermLeaveMth (String LongtermLeaveMth)
{
if (LongtermLeaveMth == null) throw new IllegalArgumentException ("LongtermLeaveMth is mandatory.");
if (LongtermLeaveMth.length() > 2)
{
log.warning("Length > 2 - truncated");
LongtermLeaveMth = LongtermLeaveMth.substring(0,1);
}
set_Value ("LongtermLeaveMth", LongtermLeaveMth);
}
/** Get Method.
@return Method */
public String getLongtermLeaveMth() 
{
return (String)get_Value("LongtermLeaveMth");
}
/** Set Monday.
@param Monday Monday */
public void setMonday (BigDecimal Monday)
{
set_Value ("Monday", Monday);
}
/** Get Monday.
@return Monday */
public BigDecimal getMonday() 
{
BigDecimal bd = (BigDecimal)get_Value("Monday");
if (bd == null) return Env.ZERO;
return bd;
}

/** P_AccidentReport_ID AD_Reference_ID=2100373 */
public static final int P_ACCIDENTREPORT_ID_AD_Reference_ID=2100373;
/** Set Report of Accident.
@param P_AccidentReport_ID CSST's Report of Accident */
public void setP_AccidentReport_ID (int P_AccidentReport_ID)
{
if (P_AccidentReport_ID <= 0) set_Value ("P_AccidentReport_ID", null);
 else 
set_Value ("P_AccidentReport_ID", new Integer(P_AccidentReport_ID));
}
/** Get Report of Accident.
@return CSST's Report of Accident */
public int getP_AccidentReport_ID() 
{
Integer ii = (Integer)get_Value("P_AccidentReport_ID");
if (ii == null) return 0;
return ii.intValue();
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

/** P_Employee_ID AD_Reference_ID=2000010 */
public static final int P_EMPLOYEE_ID_AD_Reference_ID=2000010;
/** Set Employee .
@param P_Employee_ID Employee */
public void setP_Employee_ID (int P_Employee_ID)
{
if (P_Employee_ID < 1) throw new IllegalArgumentException ("P_Employee_ID is mandatory.");
set_Value ("P_Employee_ID", new Integer(P_Employee_ID));
}
/** Get Employee .
@return Employee */
public int getP_Employee_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_Employee_LongTermLeave_ID.
@param P_Employee_LongTermLeave_ID P_Employee_LongTermLeave_ID */
public void setP_Employee_LongTermLeave_ID (int P_Employee_LongTermLeave_ID)
{
if (P_Employee_LongTermLeave_ID < 1) throw new IllegalArgumentException ("P_Employee_LongTermLeave_ID is mandatory.");
set_ValueNoCheck ("P_Employee_LongTermLeave_ID", new Integer(P_Employee_LongTermLeave_ID));
}
/** Get P_Employee_LongTermLeave_ID.
@return P_Employee_LongTermLeave_ID */
public int getP_Employee_LongTermLeave_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_LongTermLeave_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_LongTermLeave_ID AD_Reference_ID=2100372 */
public static final int P_LONGTERMLEAVE_ID_AD_Reference_ID=2100372;
/** Set Activity Status.
@param P_LongTermLeave_ID Activity Status */
public void setP_LongTermLeave_ID (int P_LongTermLeave_ID)
{
if (P_LongTermLeave_ID < 1) throw new IllegalArgumentException ("P_LongTermLeave_ID is mandatory.");
set_Value ("P_LongTermLeave_ID", new Integer(P_LongTermLeave_ID));
}
/** Get Activity Status.
@return Activity Status */
public int getP_LongTermLeave_ID() 
{
Integer ii = (Integer)get_Value("P_LongTermLeave_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Saturday.
@param Saturday Saturday */
public void setSaturday (BigDecimal Saturday)
{
set_Value ("Saturday", Saturday);
}
/** Get Saturday.
@return Saturday */
public BigDecimal getSaturday() 
{
BigDecimal bd = (BigDecimal)get_Value("Saturday");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Start Date.
@param StartDate First effective day (inclusive) */
public void setStartDate (Timestamp StartDate)
{
if (StartDate == null) throw new IllegalArgumentException ("StartDate is mandatory.");
set_Value ("StartDate", StartDate);
}
/** Get Start Date.
@return First effective day (inclusive) */
public Timestamp getStartDate() 
{
return (Timestamp)get_Value("StartDate");
}
/** Set Sunday.
@param Sunday Sunday */
public void setSunday (BigDecimal Sunday)
{
set_Value ("Sunday", Sunday);
}
/** Get Sunday.
@return Sunday */
public BigDecimal getSunday() 
{
BigDecimal bd = (BigDecimal)get_Value("Sunday");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Thursday.
@param Thursday Thursday */
public void setThursday (BigDecimal Thursday)
{
set_Value ("Thursday", Thursday);
}
/** Get Thursday.
@return Thursday */
public BigDecimal getThursday() 
{
BigDecimal bd = (BigDecimal)get_Value("Thursday");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Tuesday.
@param Tuesday Tuesday */
public void setTuesday (BigDecimal Tuesday)
{
set_Value ("Tuesday", Tuesday);
}
/** Get Tuesday.
@return Tuesday */
public BigDecimal getTuesday() 
{
BigDecimal bd = (BigDecimal)get_Value("Tuesday");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Wednesday.
@param Wednesday Wednesday */
public void setWednesday (BigDecimal Wednesday)
{
set_Value ("Wednesday", Wednesday);
}
/** Get Wednesday.
@return Wednesday */
public BigDecimal getWednesday() 
{
BigDecimal bd = (BigDecimal)get_Value("Wednesday");
if (bd == null) return Env.ZERO;
return bd;
}

/** WeekNumber AD_Reference_ID=2100379 */
public static final int WEEKNUMBER_AD_Reference_ID=2100379;
/** 1 = 1 */
public static final String WEEKNUMBER_1 = "1";
/** 2 = 2 */
public static final String WEEKNUMBER_2 = "2";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isWeekNumberValid (String test)
{
return test == null || test.equals("1") || test.equals("2");
}
/** Set Week Number.
@param WeekNumber Week Number */
public void setWeekNumber (String WeekNumber)
{
if (!isWeekNumberValid(WeekNumber))
throw new IllegalArgumentException ("WeekNumber Invalid value - " + WeekNumber + " - Reference_ID=2100379 - 1 - 2");
if (WeekNumber != null && WeekNumber.length() > 10)
{
log.warning("Length > 10 - truncated");
WeekNumber = WeekNumber.substring(0,9);
}
set_Value ("WeekNumber", WeekNumber);
}
/** Get Week Number.
@return Week Number */
public String getWeekNumber() 
{
return (String)get_Value("WeekNumber");
}

/** Set Activity Status.
@param P_LongTermLeave_ID Activity Status */
public void setP_Method_LongTermLeave_ID (int P_Method_LongTermLeave_ID)
{
if (P_Method_LongTermLeave_ID < 1) throw new IllegalArgumentException ("P_Method_LongTermLeave_ID is mandatory.");
set_Value ("P_Method_LongTermLeave_ID", new Integer(P_Method_LongTermLeave_ID));
}
/** Get Activity Status.
@return Activity Status */
public int getP_Method_LongTermLeave_ID() 
{
Integer ii = (Integer)get_Value("P_Method_LongTermLeave_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
