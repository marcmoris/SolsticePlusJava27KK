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
/** Generated Model for P_Occupation_Group
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Occupation_Group extends PO
{
/** Standard Constructor
@param ctx context
@param P_Occupation_Group_ID id
@param trxName transaction
*/
public X_P_Occupation_Group (Properties ctx, int P_Occupation_Group_ID, String trxName)
{
super (ctx, P_Occupation_Group_ID, trxName);
/** if (P_Occupation_Group_ID == 0)
{
setName (null);
setP_Occupation_Group_ID (0);
setValue (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Occupation_Group (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000031 */
public static final int Table_ID=2000031;

/** TableName=P_Occupation_Group */
public static final String Table_Name="P_Occupation_Group";

protected static KeyNamePair Model = new KeyNamePair(2000031,"P_Occupation_Group");

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
StringBuffer sb = new StringBuffer ("X_P_Occupation_Group[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Activate Step Advance .
@param Activate_Step_Advance Activate Step Advance  */
public void setActivate_Step_Advance (boolean Activate_Step_Advance)
{
set_Value ("Activate_Step_Advance", new Boolean(Activate_Step_Advance));
}
/** Get Activate Step Advance .
@return Activate Step Advance  */
public boolean isActivate_Step_Advance() 
{
Object oo = get_Value("Activate_Step_Advance");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
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
/** Set Name .
@param Name Alphanumeric identifier of the entity */
public void setName (String Name)
{
if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
if (Name.length() > 60)
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
/** Set Occupation Group.
@param P_Occupation_Group_ID   */
public void setP_Occupation_Group_ID (int P_Occupation_Group_ID)
{
if (P_Occupation_Group_ID < 1) throw new IllegalArgumentException ("P_Occupation_Group_ID is mandatory.");
set_Value ("P_Occupation_Group_ID", new Integer(P_Occupation_Group_ID));
}
/** Get Occupation Group.
@return   */
public int getP_Occupation_Group_ID() 
{
Integer ii = (Integer)get_Value("P_Occupation_Group_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** Remuneration_Method AD_Reference_ID=2000016 */
public static final int REMUNERATION_METHOD_AD_Reference_ID=2000016;
/** daily = daily */
public static final String REMUNERATION_METHOD_Daily = "daily";
/** hourly = hourly */
public static final String REMUNERATION_METHOD_Hourly = "hourly";
/** annual = annual */
public static final String REMUNERATION_METHOD_Annual = "annual";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isRemuneration_MethodValid (String test)
{
return test == null || test.equals("daily") || test.equals("hourly") || test.equals("annual");
}
/** Set Remuneration Method.
@param Remuneration_Method   */
public void setRemuneration_Method (String Remuneration_Method)
{
if (!isRemuneration_MethodValid(Remuneration_Method))
throw new IllegalArgumentException ("Remuneration_Method Invalid value - " + Remuneration_Method + " - Reference_ID=2000016 - daily - hourly - annual");
if (Remuneration_Method != null && Remuneration_Method.length() > 30)
{
log.warning("Length > 30 - truncated");
Remuneration_Method = Remuneration_Method.substring(0,29);
}
set_Value ("Remuneration_Method", Remuneration_Method);
}
/** Get Remuneration Method.
@return   */
public String getRemuneration_Method() 
{
return (String)get_Value("Remuneration_Method");
}
/** Set Search Key.
@param Value Search key for the record in the format required - must be unique */
public void setValue (String Value)
{
if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
if (Value.length() > 30)
{
log.warning("Length > 30 - truncated");
Value = Value.substring(0,29);
}
set_Value ("Value", Value);
}
/** Get Search Key.
@return Search key for the record in the format required - must be unique */
public String getValue() 
{
return (String)get_Value("Value");
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
