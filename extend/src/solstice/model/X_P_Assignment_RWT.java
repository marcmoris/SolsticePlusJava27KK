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
/** Generated Model for P_Assignment_RWT
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Assignment_RWT extends PO
{
/** Standard Constructor
@param ctx context
@param P_Assignment_RWT_ID id
@param trxName transaction
*/
public X_P_Assignment_RWT (Properties ctx, int P_Assignment_RWT_ID, String trxName)
{
super (ctx, P_Assignment_RWT_ID, trxName);
/** if (P_Assignment_RWT_ID == 0)
{
setDay_Hours (Env.ZERO);
setP_Assignment_ID (0);
setP_Assignment_RWT_ID (0);
setP_Gain_ID (0);	// @SQL=SELECT P_Gain_ID AS DefaultValue FROM P_Gain WHERE IsRWT = 'Y'
setRwtQuantity (Env.ZERO);
setStartDate (new Timestamp(System.currentTimeMillis()));
setWeekDay (null);
setWeekly_Hours (Env.ZERO);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Assignment_RWT (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100695 */
public static final int Table_ID=2100695;

/** TableName=P_Assignment_RWT */
public static final String Table_Name="P_Assignment_RWT";

protected static KeyNamePair Model = new KeyNamePair(2100695,"P_Assignment_RWT");

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
StringBuffer sb = new StringBuffer ("X_P_Assignment_RWT[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Daily Hours.
@param Day_Hours   */
public void setDay_Hours (BigDecimal Day_Hours)
{
if (Day_Hours == null) throw new IllegalArgumentException ("Day_Hours is mandatory.");
set_Value ("Day_Hours", Day_Hours);
}
/** Get Daily Hours.
@return   */
public BigDecimal getDay_Hours() 
{
BigDecimal bd = (BigDecimal)get_Value("Day_Hours");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Employee.
@param EmployeeName Employee */
public void setEmployeeName (String EmployeeName)
{
throw new IllegalArgumentException ("EmployeeName is virtual column");
}
/** Get Employee.
@return Employee */
public String getEmployeeName() 
{
return (String)get_Value("EmployeeName");
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

/** P_Assignment_ID AD_Reference_ID=2000057 */
public static final int P_ASSIGNMENT_ID_AD_Reference_ID=2000057;
/** Set Assignment.
@param P_Assignment_ID Assignment */
public void setP_Assignment_ID (int P_Assignment_ID)
{
if (P_Assignment_ID < 1) throw new IllegalArgumentException ("P_Assignment_ID is mandatory.");
set_ValueNoCheck ("P_Assignment_ID", new Integer(P_Assignment_ID));
}
/** Get Assignment.
@return Assignment */
public int getP_Assignment_ID() 
{
Integer ii = (Integer)get_Value("P_Assignment_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_Assignment_RWT_ID.
@param P_Assignment_RWT_ID P_Assignment_RWT_ID */
public void setP_Assignment_RWT_ID (int P_Assignment_RWT_ID)
{
if (P_Assignment_RWT_ID < 1) throw new IllegalArgumentException ("P_Assignment_RWT_ID is mandatory.");
set_ValueNoCheck ("P_Assignment_RWT_ID", new Integer(P_Assignment_RWT_ID));
}
/** Get P_Assignment_RWT_ID.
@return P_Assignment_RWT_ID */
public int getP_Assignment_RWT_ID() 
{
Integer ii = (Integer)get_Value("P_Assignment_RWT_ID");
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
/** Set Quantity.
@param RwtQuantity Quantity */
public void setRwtQuantity (BigDecimal RwtQuantity)
{
if (RwtQuantity == null) throw new IllegalArgumentException ("RwtQuantity is mandatory.");
set_Value ("RwtQuantity", RwtQuantity);
}
/** Get Quantity.
@return Quantity */
public BigDecimal getRwtQuantity() 
{
BigDecimal bd = (BigDecimal)get_Value("RwtQuantity");
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

/** WeekDay AD_Reference_ID=167 */
public static final int WEEKDAY_AD_Reference_ID=167;
/** Sunday = 7 */
public static final String WEEKDAY_Sunday = "7";
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
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isWeekDayValid (String test)
{
return test.equals("7") || test.equals("1") || test.equals("2") || test.equals("3") || test.equals("4") || test.equals("5") || test.equals("6");
}
/** Set Day of the Week.
@param WeekDay Day of the Week */
public void setWeekDay (String WeekDay)
{
if (WeekDay == null) throw new IllegalArgumentException ("WeekDay is mandatory");
if (!isWeekDayValid(WeekDay))
throw new IllegalArgumentException ("WeekDay Invalid value - " + WeekDay + " - Reference_ID=167 - 7 - 1 - 2 - 3 - 4 - 5 - 6");
if (WeekDay.length() > 10)
{
log.warning("Length > 10 - truncated");
WeekDay = WeekDay.substring(0,9);
}
set_Value ("WeekDay", WeekDay);
}
/** Get Day of the Week.
@return Day of the Week */
public String getWeekDay() 
{
return (String)get_Value("WeekDay");
}
/** Set Weekly Hours.
@param Weekly_Hours   */
public void setWeekly_Hours (BigDecimal Weekly_Hours)
{
if (Weekly_Hours == null) throw new IllegalArgumentException ("Weekly_Hours is mandatory.");
set_Value ("Weekly_Hours", Weekly_Hours);
}
/** Get Weekly Hours.
@return   */
public BigDecimal getWeekly_Hours() 
{
BigDecimal bd = (BigDecimal)get_Value("Weekly_Hours");
if (bd == null) return Env.ZERO;
return bd;
}
}
