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
/** Generated Model for P_Assignment_SpecialRate
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Assignment_SpecialRate extends PO
{
/** Standard Constructor
@param ctx context
@param P_Assignment_SpecialRate_ID id
@param trxName transaction
*/
public X_P_Assignment_SpecialRate (Properties ctx, int P_Assignment_SpecialRate_ID, String trxName)
{
super (ctx, P_Assignment_SpecialRate_ID, trxName);
/** if (P_Assignment_SpecialRate_ID == 0)
{
setP_Assignment_ID (0);
setP_Assignment_SpecialRate_ID (0);
setP_SpecialRate_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Assignment_SpecialRate (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100754 */
public static final int Table_ID=2100754;

/** TableName=P_Assignment_SpecialRate */
public static final String Table_Name="P_Assignment_SpecialRate";

protected static KeyNamePair Model = new KeyNamePair(2100754,"P_Assignment_SpecialRate");

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
StringBuffer sb = new StringBuffer ("X_P_Assignment_SpecialRate[").append(get_ID()).append("]");
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
/** Set To.
@param EffectTo To */
public void setEffectTo (Timestamp EffectTo)
{
set_Value ("EffectTo", EffectTo);
}
/** Get To.
@return To */
public Timestamp getEffectTo() 
{
return (Timestamp)get_Value("EffectTo");
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
/** Set Hourly Rate.
@param Hourly_Rate   */
public void setHourly_Rate (BigDecimal Hourly_Rate)
{
throw new IllegalArgumentException ("Hourly_Rate is virtual column");
}
/** Get Hourly Rate.
@return   */
public BigDecimal getHourly_Rate() 
{
BigDecimal bd = (BigDecimal)get_Value("Hourly_Rate");
if (bd == null) return Env.ZERO;
return bd;
}

/** P_Assignment_ID AD_Reference_ID=2000057 */
public static final int P_ASSIGNMENT_ID_AD_Reference_ID=2000057;
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
/** Set P_ASSIGMENT_SPECIALRATE_ID.
@param P_Assignment_SpecialRate_ID P_ASSIGMENT_SPECIALRATE_ID */
public void setP_Assignment_SpecialRate_ID (int P_Assignment_SpecialRate_ID)
{
if (P_Assignment_SpecialRate_ID < 1) throw new IllegalArgumentException ("P_Assignment_SpecialRate_ID is mandatory.");
set_ValueNoCheck ("P_Assignment_SpecialRate_ID", new Integer(P_Assignment_SpecialRate_ID));
}
/** Get P_ASSIGMENT_SPECIALRATE_ID.
@return P_ASSIGMENT_SPECIALRATE_ID */
public int getP_Assignment_SpecialRate_ID() 
{
Integer ii = (Integer)get_Value("P_Assignment_SpecialRate_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_SpecialRate_ID AD_Reference_ID=2100391 */
public static final int P_SPECIALRATE_ID_AD_Reference_ID=2100391;
/** Set Special Rate.
@param P_SpecialRate_ID Special Rate */
public void setP_SpecialRate_ID (int P_SpecialRate_ID)
{
if (P_SpecialRate_ID < 1) throw new IllegalArgumentException ("P_SpecialRate_ID is mandatory.");
set_Value ("P_SpecialRate_ID", new Integer(P_SpecialRate_ID));
}
/** Get Special Rate.
@return Special Rate */
public int getP_SpecialRate_ID() 
{
Integer ii = (Integer)get_Value("P_SpecialRate_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Weekly Amount.
@param Weekly_Amount Weekly Amount */
public void setWeekly_Amount (BigDecimal Weekly_Amount)
{
set_Value ("Weekly_Amount", Weekly_Amount);
}
/** Get Weekly Amount.
@return Weekly Amount */
public BigDecimal getWeekly_Amount() 
{
BigDecimal bd = (BigDecimal)get_Value("Weekly_Amount");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Weekly Hours.
@param Weekly_Hours   */
public void setWeekly_Hours (BigDecimal Weekly_Hours)
{
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
