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
/** Generated Model for P_Employee_RWT_Detail
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Employee_RWT_Detail extends PO
{
/** Standard Constructor
@param ctx context
@param P_Employee_RWT_Detail_ID id
@param trxName transaction
*/
public X_P_Employee_RWT_Detail (Properties ctx, int P_Employee_RWT_Detail_ID, String trxName)
{
super (ctx, P_Employee_RWT_Detail_ID, trxName);
/** if (P_Employee_RWT_Detail_ID == 0)
{
setWeekDay (null);
setWeekly_Hours (Env.ZERO);	// @SQL=select  WEEKLY_HOURS AS DefaultValue from p_assignment_param, p_assignment where assignmenttype = 'P' and p_assignment_param.p_assignment_id = p_assignment.p_assignment_Id and p_assignment_param.effectin = ( select max( effectin) from p_assignment_param m where m.p_assignment_id = p_assignment.p_assignment_id ) and P_Assignment.P_Employee_ID = @P_Employee_ID@  
setWeekNumber (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Employee_RWT_Detail (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100918 */
public static final int Table_ID=2100918;

/** TableName=P_Employee_RWT_Detail */
public static final String Table_Name="P_Employee_RWT_Detail";

protected static KeyNamePair Model = new KeyNamePair(2100918,"P_Employee_RWT_Detail");

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
StringBuffer sb = new StringBuffer ("X_P_Employee_RWT_Detail[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Daily Hours.
@param Day_Hours   */
public void setDay_Hours (BigDecimal Day_Hours)
{
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
/** Set RV_ChequesEmis_ID.
@param P_Employee_RWT_Detail_ID RV_ChequesEmis_ID */
public void setP_Employee_RWT_Detail_ID (int P_Employee_RWT_Detail_ID)
{
if (P_Employee_RWT_Detail_ID <= 0) set_ValueNoCheck ("P_Employee_RWT_Detail_ID", null);
 else 
set_ValueNoCheck ("P_Employee_RWT_Detail_ID", new Integer(P_Employee_RWT_Detail_ID));
}
/** Get RV_ChequesEmis_ID.
@return RV_ChequesEmis_ID */
public int getP_Employee_RWT_Detail_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_RWT_Detail_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set GROUP_NAME.
@param P_Employee_RWT_ID GROUP_NAME */
public void setP_Employee_RWT_ID (int P_Employee_RWT_ID)
{
if (P_Employee_RWT_ID <= 0) set_Value ("P_Employee_RWT_ID", null);
 else 
set_Value ("P_Employee_RWT_ID", new Integer(P_Employee_RWT_ID));
}
/** Get GROUP_NAME.
@return GROUP_NAME */
public int getP_Employee_RWT_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_RWT_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Quantity.
@param RwtQuantity Quantity */
public void setRwtQuantity (BigDecimal RwtQuantity)
{
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
/** Set Week Number.
@param WeekNumber Week Number */
public void setWeekNumber (int WeekNumber)
{
set_Value ("WeekNumber", new Integer(WeekNumber));
}
/** Get Week Number.
@return Week Number */
public int getWeekNumber() 
{
Integer ii = (Integer)get_Value("WeekNumber");
if (ii == null) return 0;
return ii.intValue();
}
}
