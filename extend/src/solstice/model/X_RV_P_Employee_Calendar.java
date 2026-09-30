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
/** Generated Model for RV_P_Employee_Calendar
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: X_RV_P_Employee_Calendar.java,v 1.1 2007/07/18 14:43:41 marmor01 Exp $ */
public class X_RV_P_Employee_Calendar extends PO
{
/** Standard Constructor
@param ctx context
@param RV_P_Employee_Calendar_ID id
@param trxName transaction
*/
public X_RV_P_Employee_Calendar (Properties ctx, int RV_P_Employee_Calendar_ID, String trxName)
{
super (ctx, RV_P_Employee_Calendar_ID, trxName);
/** if (RV_P_Employee_Calendar_ID == 0)
{
setNonBusinessDate (new Timestamp(System.currentTimeMillis()));
setP_Employee_Calendar_ID (0);
setP_Employee_NonBusinessDay_ID (0);
setP_Gain_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_RV_P_Employee_Calendar (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100804 */
public static final int Table_ID=2100804;

/** TableName=RV_P_Employee_Calendar */
public static final String Table_Name="RV_P_Employee_Calendar";

protected static KeyNamePair Model = new KeyNamePair(2100804,"RV_P_Employee_Calendar");

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
StringBuffer sb = new StringBuffer ("X_RV_P_Employee_Calendar[").append(get_ID()).append("]");
return sb.toString();
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
/** Set Name .
@param Name Alphanumeric identifier of the entity */
public void setName (String Name)
{
if (Name != null && Name.length() > 60)
{
log.warning("Length > 60 - truncated");
Name = Name.substring(0,59);
}
set_Value ("Name", Name);
}
/** Get Name .
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
/** Set Day.
@param NonBusinessDate Day */
public void setNonBusinessDate (Timestamp NonBusinessDate)
{
if (NonBusinessDate == null) throw new IllegalArgumentException ("NonBusinessDate is mandatory.");
set_Value ("NonBusinessDate", NonBusinessDate);
}
/** Get Day.
@return Day */
public Timestamp getNonBusinessDate() 
{
return (Timestamp)get_Value("NonBusinessDate");
}
/** Set Calendar.
@param P_Employee_Calendar_ID Calendar */
public void setP_Employee_Calendar_ID (int P_Employee_Calendar_ID)
{
if (P_Employee_Calendar_ID < 1) throw new IllegalArgumentException ("P_Employee_Calendar_ID is mandatory.");
set_Value ("P_Employee_Calendar_ID", new Integer(P_Employee_Calendar_ID));
}
/** Get Calendar.
@return Calendar */
public int getP_Employee_Calendar_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Calendar_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Employee_ID AD_Reference_ID=2000010 */
public static final int P_EMPLOYEE_ID_AD_Reference_ID=2000010;
/** Set Employee .
@param P_Employee_ID Employee */
public void setP_Employee_ID (int P_Employee_ID)
{
if (P_Employee_ID <= 0) set_Value ("P_Employee_ID", null);
 else 
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
/** Set P_Employee_NonBusinessDay_ID.
@param P_Employee_NonBusinessDay_ID P_Employee_NonBusinessDay_ID */
public void setP_Employee_NonBusinessDay_ID (int P_Employee_NonBusinessDay_ID)
{
if (P_Employee_NonBusinessDay_ID < 1) throw new IllegalArgumentException ("P_Employee_NonBusinessDay_ID is mandatory.");
set_Value ("P_Employee_NonBusinessDay_ID", new Integer(P_Employee_NonBusinessDay_ID));
}
/** Get P_Employee_NonBusinessDay_ID.
@return P_Employee_NonBusinessDay_ID */
public int getP_Employee_NonBusinessDay_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_NonBusinessDay_ID");
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
/** Set Year.
@param Year Calendar Year */
public void setYear (String Year)
{
if (Year != null && Year.length() > 10)
{
log.warning("Length > 10 - truncated");
Year = Year.substring(0,9);
}
set_Value ("Year", Year);
}
/** Get Year.
@return Calendar Year */
public String getYear() 
{
return (String)get_Value("Year");
}
}
