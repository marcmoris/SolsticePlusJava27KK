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
/** Generated Model for P_Standard_Time
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Standard_Time extends PO
{
/** Standard Constructor
@param ctx context
@param P_Standard_Time_ID id
@param trxName transaction
*/
public X_P_Standard_Time (Properties ctx, int P_Standard_Time_ID, String trxName)
{
super (ctx, P_Standard_Time_ID, trxName);
/** if (P_Standard_Time_ID == 0)
{
setP_Employee_ID (0);	// @P_Employee_ID@
setP_Gain_ID (0);
setP_Standard_Time_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Standard_Time (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000098 */
public static final int Table_ID=2000098;

/** TableName=P_Standard_Time */
public static final String Table_Name="P_Standard_Time";

protected static KeyNamePair Model = new KeyNamePair(2000098,"P_Standard_Time");

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
StringBuffer sb = new StringBuffer ("X_P_Standard_Time[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Effect In.
@param EffectIn Date when information in effect  */
public void setEffectIn (Timestamp EffectIn)
{
set_Value ("EffectIn", EffectIn);
}
/** Get Effect In.
@return Date when information in effect  */
public Timestamp getEffectIn() 
{
return (Timestamp)get_Value("EffectIn");
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

/** GainUnit AD_Reference_ID=2100377 */
public static final int GAINUNIT_AD_Reference_ID=2100377;
/** Quantity = Q */
public static final String GAINUNIT_Quantity = "Q";
/** Amount = A */
public static final String GAINUNIT_Amount = "A";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isGainUnitValid (String test)
{
return test == null || test.equals("Q") || test.equals("A");
}
/** Set Unit.
@param GainUnit Unit */
public void setGainUnit (String GainUnit)
{
if (!isGainUnitValid(GainUnit))
throw new IllegalArgumentException ("GainUnit Invalid value - " + GainUnit + " - Reference_ID=2100377 - Q - A");
if (GainUnit != null && GainUnit.length() > 1)
{
log.warning("Length > 1 - truncated");
GainUnit = GainUnit.substring(0,0);
}
set_Value ("GainUnit", GainUnit);
}
/** Get Unit.
@return Unit */
public String getGainUnit() 
{
return (String)get_Value("GainUnit");
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

/** P_Schedule_ID AD_Reference_ID=2000064 */
public static final int P_SCHEDULE_ID_AD_Reference_ID=2000064;
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

/** P_SelfFinancedLeave_ID AD_Reference_ID=2100371 */
public static final int P_SELFFINANCEDLEAVE_ID_AD_Reference_ID=2100371;
/** Set Self Financed Leave Plan.
@param P_SelfFinancedLeave_ID Self Financed Leave Plan */
public void setP_SelfFinancedLeave_ID (int P_SelfFinancedLeave_ID)
{
if (P_SelfFinancedLeave_ID <= 0) set_Value ("P_SelfFinancedLeave_ID", null);
 else 
set_Value ("P_SelfFinancedLeave_ID", new Integer(P_SelfFinancedLeave_ID));
}
/** Get Self Financed Leave Plan.
@return Self Financed Leave Plan */
public int getP_SelfFinancedLeave_ID() 
{
Integer ii = (Integer)get_Value("P_SelfFinancedLeave_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_Standard_Time_ID.
@param P_Standard_Time_ID P_Standard_Time_ID */
public void setP_Standard_Time_ID (int P_Standard_Time_ID)
{
if (P_Standard_Time_ID < 1) throw new IllegalArgumentException ("P_Standard_Time_ID is mandatory.");
set_ValueNoCheck ("P_Standard_Time_ID", new Integer(P_Standard_Time_ID));
}
/** Get P_Standard_Time_ID.
@return P_Standard_Time_ID */
public int getP_Standard_Time_ID() 
{
Integer ii = (Integer)get_Value("P_Standard_Time_ID");
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
/** Set Amount.
@param Standard_Time_Amount Amount */
public void setStandard_Time_Amount (BigDecimal Standard_Time_Amount)
{
set_Value ("Standard_Time_Amount", Standard_Time_Amount);
}
/** Get Amount.
@return Amount */
public BigDecimal getStandard_Time_Amount() 
{
BigDecimal bd = (BigDecimal)get_Value("Standard_Time_Amount");
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
if (WeekNumber != null && WeekNumber.length() > 4)
{
log.warning("Length > 4 - truncated");
WeekNumber = WeekNumber.substring(0,3);
}
set_Value ("WeekNumber", WeekNumber);
}
/** Get Week Number.
@return Week Number */
public String getWeekNumber() 
{
return (String)get_Value("WeekNumber");
}
}
