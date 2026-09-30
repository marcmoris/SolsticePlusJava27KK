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
/** Generated Model for P_Proposed_Adv_Date
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: X_P_Proposed_Adv_Date.java,v 1.3 2008/05/21 13:03:18 marmor01 Exp $ */
public class X_P_Proposed_Adv_Date extends PO
{
/** Standard Constructor
@param ctx context
@param P_Proposed_Adv_Date_ID id
@param trxName transaction
*/
public X_P_Proposed_Adv_Date (Properties ctx, int P_Proposed_Adv_Date_ID, String trxName)
{
super (ctx, P_Proposed_Adv_Date_ID, trxName);
/** if (P_Proposed_Adv_Date_ID == 0)
{
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Proposed_Adv_Date (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100881 */
public static final int Table_ID=2100898;

/** TableName=P_Proposed_Adv_Date */
public static final String Table_Name="P_Proposed_Adv_Date";

protected static KeyNamePair Model = new KeyNamePair(2100898,"P_Proposed_Adv_Date");

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
StringBuffer sb = new StringBuffer ("X_P_Proposed_Adv_Date[").append(get_ID()).append("]");
return sb.toString();
}
/** Set excpectedAdvanceDate.
@param excpectedAdvanceDate excpectedAdvanceDate */
public void setexcpectedAdvanceDate (Timestamp excpectedAdvanceDate)
{
set_Value ("excpectedAdvanceDate", excpectedAdvanceDate);
}
/** Get excpectedAdvanceDate.
@return excpectedAdvanceDate */
public Timestamp getexcpectedAdvanceDate() 
{
return (Timestamp)get_Value("excpectedAdvanceDate");
}
/** Set Last Advance Date.
@param LastAdvanceDate Last Advance Date */
public void setLastAdvanceDate (Timestamp LastAdvanceDate)
{
set_Value ("LastAdvanceDate", LastAdvanceDate);
}
/** Get Last Advance Date.
@return Last Advance Date */
public Timestamp getLastAdvanceDate() 
{
return (Timestamp)get_Value("LastAdvanceDate");
}
/** Set newExpectedAdvanceDate.
@param newExpectedAdvanceDate newExpectedAdvanceDate */
public void setnewExpectedAdvanceDate (Timestamp newExpectedAdvanceDate)
{
set_Value ("newExpectedAdvanceDate", newExpectedAdvanceDate);
}
/** Get newExpectedAdvanceDate.
@return newExpectedAdvanceDate */
public Timestamp getnewExpectedAdvanceDate() 
{
return (Timestamp)get_Value("newExpectedAdvanceDate");
}

/** P_Assignment_ID AD_Reference_ID=2100364 */
public static final int P_ASSIGNMENT_ID_AD_Reference_ID=2100364;
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

/** P_LongTermLeave_ID AD_Reference_ID=2100372 */
public static final int P_LONGTERMLEAVE_ID_AD_Reference_ID=2100372;
/** Set Long Term Leave Profile.
@param P_LongTermLeave_ID Long Term Leave Profile */
public void setP_LongTermLeave_ID (int P_LongTermLeave_ID)
{
if (P_LongTermLeave_ID <= 0) set_Value ("P_LongTermLeave_ID", null);
 else 
set_Value ("P_LongTermLeave_ID", new Integer(P_LongTermLeave_ID));
}
/** Get Long Term Leave Profile.
@return Long Term Leave Profile */
public int getP_LongTermLeave_ID() 
{
Integer ii = (Integer)get_Value("P_LongTermLeave_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Occupation_Group_ID AD_Reference_ID=2100363 */
public static final int P_OCCUPATION_GROUP_ID_AD_Reference_ID=2100363;
/** Set Occupation Group.
@param P_Occupation_Group_ID   */
public void setP_Occupation_Group_ID (int P_Occupation_Group_ID)
{
if (P_Occupation_Group_ID <= 0) set_Value ("P_Occupation_Group_ID", null);
 else 
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
/** Set P_Proposed_Adv_Date_ID.
@param P_Proposed_Adv_Date_ID P_Proposed_Adv_Date_ID */
public void setP_Proposed_Adv_Date_ID (int P_Proposed_Adv_Date_ID)
{
if (P_Proposed_Adv_Date_ID <= 0) set_ValueNoCheck ("P_Proposed_Adv_Date_ID", null);
 else 
set_ValueNoCheck ("P_Proposed_Adv_Date_ID", new Integer(P_Proposed_Adv_Date_ID));
}
/** Get P_Proposed_Adv_Date_ID.
@return P_Proposed_Adv_Date_ID */
public int getP_Proposed_Adv_Date_ID() 
{
Integer ii = (Integer)get_Value("P_Proposed_Adv_Date_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
