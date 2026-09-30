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
/** Generated Model for P_TimeCorrection
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_TimeCorrection extends PO
{
/** Standard Constructor
@param ctx context
@param P_TimeCorrection_ID id
@param trxName transaction
*/
public X_P_TimeCorrection (Properties ctx, int P_TimeCorrection_ID, String trxName)
{
super (ctx, P_TimeCorrection_ID, trxName);
/** if (P_TimeCorrection_ID == 0)
{
setP_TIMECORRECTION_ID (0);
setValue (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_TimeCorrection (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=3201209 */
public static final int Table_ID=3201209;

/** TableName=P_TimeCorrection */
public static final String Table_Name="P_TimeCorrection";

protected static KeyNamePair Model = new KeyNamePair(3201209,"P_TimeCorrection");

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
StringBuffer sb = new StringBuffer ("X_P_TimeCorrection[").append(get_ID()).append("]");
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
/** Set Affectation destination.
@param P_Assignment_Dst_ID Affectation destination */
public void setP_Assignment_Dst_ID (int P_Assignment_Dst_ID)
{
if (P_Assignment_Dst_ID <= 0) set_Value ("P_Assignment_Dst_ID", null);
 else 
set_Value ("P_Assignment_Dst_ID", new Integer(P_Assignment_Dst_ID));
}
/** Get Affectation destination.
@return Affectation destination */
public int getP_Assignment_Dst_ID() 
{
Integer ii = (Integer)get_Value("P_Assignment_Dst_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Affectation source.
@param P_Assignment_Src_ID Affectation source */
public void setP_Assignment_Src_ID (int P_Assignment_Src_ID)
{
if (P_Assignment_Src_ID <= 0) set_Value ("P_Assignment_Src_ID", null);
 else 
set_Value ("P_Assignment_Src_ID", new Integer(P_Assignment_Src_ID));
}
/** Get Affectation source.
@return Affectation source */
public int getP_Assignment_Src_ID() 
{
Integer ii = (Integer)get_Value("P_Assignment_Src_ID");
if (ii == null) return 0;
return ii.intValue();
}
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
/** Set Gain destination.
@param P_Gain_Dst_ID Gain destination */
public void setP_Gain_Dst_ID (int P_Gain_Dst_ID)
{
if (P_Gain_Dst_ID <= 0) set_Value ("P_Gain_Dst_ID", null);
 else 
set_Value ("P_Gain_Dst_ID", new Integer(P_Gain_Dst_ID));
}
/** Get Gain destination.
@return Gain destination */
public int getP_Gain_Dst_ID() 
{
Integer ii = (Integer)get_Value("P_Gain_Dst_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Gain source.
@param P_Gain_Src_ID Gain source */
public void setP_Gain_Src_ID (int P_Gain_Src_ID)
{
if (P_Gain_Src_ID <= 0) set_Value ("P_Gain_Src_ID", null);
 else 
set_Value ("P_Gain_Src_ID", new Integer(P_Gain_Src_ID));
}
/** Get Gain source.
@return Gain source */
public int getP_Gain_Src_ID() 
{
Integer ii = (Integer)get_Value("P_Gain_Src_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Ending Period.
@param P_Period_End_ID Ending Period */
public void setP_Period_End_ID (int P_Period_End_ID)
{
if (P_Period_End_ID <= 0) set_Value ("P_Period_End_ID", null);
 else 
set_Value ("P_Period_End_ID", new Integer(P_Period_End_ID));
}
/** Get Ending Period.
@return Ending Period */
public int getP_Period_End_ID() 
{
Integer ii = (Integer)get_Value("P_Period_End_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Starting Period.
@param P_Period_Start_ID Starting Period */
public void setP_Period_Start_ID (int P_Period_Start_ID)
{
if (P_Period_Start_ID <= 0) set_Value ("P_Period_Start_ID", null);
 else 
set_Value ("P_Period_Start_ID", new Integer(P_Period_Start_ID));
}
/** Get Starting Period.
@return Starting Period */
public int getP_Period_Start_ID() 
{
Integer ii = (Integer)get_Value("P_Period_Start_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_TIMECORRECTION_ID.
@param P_TIMECORRECTION_ID P_TIMECORRECTION_ID */
public void setP_TIMECORRECTION_ID (int P_TIMECORRECTION_ID)
{
if (P_TIMECORRECTION_ID < 1) throw new IllegalArgumentException ("P_TIMECORRECTION_ID is mandatory.");
set_ValueNoCheck ("P_TIMECORRECTION_ID", new Integer(P_TIMECORRECTION_ID));
}
/** Get P_TIMECORRECTION_ID.
@return P_TIMECORRECTION_ID */
public int getP_TimeCorrection_ID() 
{
Integer ii = (Integer)get_Value("P_TIMECORRECTION_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Process Now.
@param Processing Process Now */
public void setProcessing (boolean Processing)
{
set_Value ("Processing", new Boolean(Processing));
}
/** Get Process Now.
@return Process Now */
public boolean isProcessing() 
{
Object oo = get_Value("Processing");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
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
/** Set Search Key.
@param Value Search key for the record in the format required - must be unique */
public void setValue (String Value)
{
if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
if (Value.length() > 10)
{
log.warning("Length > 10 - truncated");
Value = Value.substring(0,9);
}
set_Value ("Value", Value);
}
/** Get Search Key.
@return Search key for the record in the format required - must be unique */
public String getValue() 
{
return (String)get_Value("Value");
}
}
