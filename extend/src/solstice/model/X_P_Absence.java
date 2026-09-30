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
/** Generated Model for P_Absence
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: X_P_Absence.java,v 1.4 2008/11/06 18:22:58 marmor01 Exp $ */
public class X_P_Absence extends PO
{
/** Standard Constructor
@param ctx context
@param P_Absence_ID id
@param trxName transaction
*/
public X_P_Absence (Properties ctx, int P_Absence_ID, String trxName)
{
super (ctx, P_Absence_ID, trxName);
/** if (P_Absence_ID == 0)
{
setAddedCalendar (false);
setC_Activity_ID (0);
setDescription (null);
setP_Absence_ID (0);
setP_Absence_Status_ID (0);
setP_Employee_ID (0);
setP_Gain_ID (0);
setP_Job_Type_ID (0);
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
public X_P_Absence (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100758 */
public static final int Table_ID=2100758;

/** TableName=P_Absence */
public static final String Table_Name="P_Absence";

protected static KeyNamePair Model = new KeyNamePair(2100758,"P_Absence");

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
StringBuffer sb = new StringBuffer ("X_P_Absence[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Added to Calendar.
@param AddedCalendar Added to Calendar */
public void setAddedCalendar (boolean AddedCalendar)
{
set_Value ("AddedCalendar", new Boolean(AddedCalendar));
}
/** Get Added to Calendar.
@return Added to Calendar */
public boolean isAddedCalendar() 
{
Object oo = get_Value("AddedCalendar");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Crew cost.
@param C_Activity_ID Crew cost */
public void setC_Activity_ID (int C_Activity_ID)
{
if (C_Activity_ID < 1) throw new IllegalArgumentException ("C_Activity_ID is mandatory.");
set_Value ("C_Activity_ID", new Integer(C_Activity_ID));
}
/** Get Crew cost.
@return Crew cost */
public int getC_Activity_ID() 
{
Integer ii = (Integer)get_Value("C_Activity_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Description.
@param Description Optional short description of the record */
public void setDescription (String Description)
{
if (Description == null) throw new IllegalArgumentException ("Description is mandatory.");
if (Description.length() > 255)
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
/** Set isCallCenter.
@param isCallCenter isCallCenter */
public void setisCallCenter (boolean isCallCenter)
{
set_Value ("isCallCenter", new Boolean(isCallCenter));
}
/** Get isCallCenter.
@return isCallCenter */
public boolean isCallCenter() 
{
Object oo = get_Value("isCallCenter");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set IsFlexibleHours.
@param IsFlexibleHours IsFlexibleHours */
public void setIsFlexibleHours (boolean IsFlexibleHours)
{
set_Value ("IsFlexibleHours", new Boolean(IsFlexibleHours));
}
/** Get IsFlexibleHours.
@return IsFlexibleHours */
public boolean isFlexibleHours() 
{
Object oo = get_Value("IsFlexibleHours");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Absence.
@param P_Absence_ID Absence */
public void setP_Absence_ID (int P_Absence_ID)
{
if (P_Absence_ID < 1) throw new IllegalArgumentException ("P_Absence_ID is mandatory.");
set_ValueNoCheck ("P_Absence_ID", new Integer(P_Absence_ID));
}
/** Get Absence.
@return Absence */
public int getP_Absence_ID() 
{
Integer ii = (Integer)get_Value("P_Absence_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_Absence_Status_ID.
@param P_Absence_Status_ID P_Absence_Status_ID */
public void setP_Absence_Status_ID (int P_Absence_Status_ID)
{
if (P_Absence_Status_ID < 1) throw new IllegalArgumentException ("P_Absence_Status_ID is mandatory.");
set_Value ("P_Absence_Status_ID", new Integer(P_Absence_Status_ID));
}
/** Get P_Absence_Status_ID.
@return P_Absence_Status_ID */
public int getP_Absence_Status_ID() 
{
Integer ii = (Integer)get_Value("P_Absence_Status_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Employee.
@param P_Employee_ID Employee */
public void setP_Employee_ID (int P_Employee_ID)
{
if (P_Employee_ID < 1) throw new IllegalArgumentException ("P_Employee_ID is mandatory.");
set_Value ("P_Employee_ID", new Integer(P_Employee_ID));
}
/** Get Employee.
@return Employee */
public int getP_Employee_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Pay Codes.
@param P_Gain_ID Pay Codes */
public void setP_Gain_ID (int P_Gain_ID)
{
if (P_Gain_ID < 1) throw new IllegalArgumentException ("P_Gain_ID is mandatory.");
set_Value ("P_Gain_ID", new Integer(P_Gain_ID));
}
/** Get Pay Codes.
@return Pay Codes */
public int getP_Gain_ID() 
{
Integer ii = (Integer)get_Value("P_Gain_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Job Type.
@param P_Job_Type_ID   */
public void setP_Job_Type_ID (int P_Job_Type_ID)
{
if (P_Job_Type_ID < 1) throw new IllegalArgumentException ("P_Job_Type_ID is mandatory.");
set_Value ("P_Job_Type_ID", new Integer(P_Job_Type_ID));
}
/** Get Job Type.
@return   */
public int getP_Job_Type_ID() 
{
Integer ii = (Integer)get_Value("P_Job_Type_ID");
if (ii == null) return 0;
return ii.intValue();
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
/** Set Search Key.
@param Value Search key for the record in the format required - must be unique */
public void setValue (String Value)
{
if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
if (Value.length() > 50)
{
log.warning("Length > 50 - truncated");
Value = Value.substring(0,49);
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
