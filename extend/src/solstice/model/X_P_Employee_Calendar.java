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
/** Generated Model for P_Employee_Calendar
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Employee_Calendar extends PO
{
/** Standard Constructor
@param ctx context
@param P_Employee_Calendar_ID id
@param trxName transaction
*/
public X_P_Employee_Calendar (Properties ctx, int P_Employee_Calendar_ID, String trxName)
{
super (ctx, P_Employee_Calendar_ID, trxName);
/** if (P_Employee_Calendar_ID == 0)
{
setP_Employee_Calendar_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Employee_Calendar (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=1000000 */
public static final int Table_ID=1000000;

/** TableName=P_Employee_Calendar */
public static final String Table_Name="P_Employee_Calendar";

protected static KeyNamePair Model = new KeyNamePair(1000000,"P_Employee_Calendar");

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
StringBuffer sb = new StringBuffer ("X_P_Employee_Calendar[").append(get_ID()).append("]");
return sb.toString();
}
/** Set CallCalendar.
@param CallCalendar CallCalendar */
public void setCallCalendar (boolean CallCalendar)
{
set_Value ("CallCalendar", new Boolean(CallCalendar));
}
/** Get CallCalendar.
@return CallCalendar */
public boolean isCallCalendar() 
{
Object oo = get_Value("CallCalendar");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Calendar.
@param P_Employee_Calendar_ID Calendar */
public void setP_Employee_Calendar_ID (int P_Employee_Calendar_ID)
{
if (P_Employee_Calendar_ID < 1) throw new IllegalArgumentException ("P_Employee_Calendar_ID is mandatory.");
set_ValueNoCheck ("P_Employee_Calendar_ID", new Integer(P_Employee_Calendar_ID));
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
/** Get Record ID/ColumnName
@return ID/ColumnName pair */
public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), String.valueOf(getP_Employee_ID()));
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
