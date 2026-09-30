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
/** Generated Model for P_Assignment_AssignmentReason
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Assignment_AssignmentReason extends PO
{
/** Standard Constructor
@param ctx context
@param P_Assignment_AssignmentReason_ID id
@param trxName transaction
*/
public X_P_Assignment_AssignmentReason (Properties ctx, int P_Assignment_AssignmentReason_ID, String trxName)
{
super (ctx, P_Assignment_AssignmentReason_ID, trxName);
/** if (P_Assignment_AssignmentReason_ID == 0)
{
setEndDate (new Timestamp(System.currentTimeMillis()));
setP_Assignment_AssignmentReason_ID (0);
setP_Assignment_ID (0);
setP_AssignmentReason_ID (0);
setStartDate (new Timestamp(System.currentTimeMillis()));
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Assignment_AssignmentReason (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100767 */
public static final int Table_ID=2100767;

/** TableName=P_Assignment_AssignmentReason */
public static final String Table_Name="P_Assignment_AssignmentReason";

protected static KeyNamePair Model = new KeyNamePair(2100767,"P_Assignment_AssignmentReason");

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
StringBuffer sb = new StringBuffer ("X_P_Assignment_AssignmentReason[").append(get_ID()).append("]");
return sb.toString();
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
if (EndDate == null) throw new IllegalArgumentException ("EndDate is mandatory.");
set_Value ("EndDate", EndDate);
}
/** Get End Date.
@return Last effective date (inclusive) */
public Timestamp getEndDate() 
{
return (Timestamp)get_Value("EndDate");
}
/** Set Assignment Reason.
@param P_Assignment_AssignmentReason_ID Assignment Reason */
public void setP_Assignment_AssignmentReason_ID (int P_Assignment_AssignmentReason_ID)
{
if (P_Assignment_AssignmentReason_ID < 1) throw new IllegalArgumentException ("P_Assignment_AssignmentReason_ID is mandatory.");
set_ValueNoCheck ("P_Assignment_AssignmentReason_ID", new Integer(P_Assignment_AssignmentReason_ID));
}
/** Get Assignment Reason.
@return Assignment Reason */
public int getP_Assignment_AssignmentReason_ID() 
{
Integer ii = (Integer)get_Value("P_Assignment_AssignmentReason_ID");
if (ii == null) return 0;
return ii.intValue();
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

/** P_AssignmentReason_ID AD_Reference_ID=2100405 */
public static final int P_ASSIGNMENTREASON_ID_AD_Reference_ID=2100405;
/** Set Assignment Reason.
@param P_AssignmentReason_ID Assignment Reason */
public void setP_AssignmentReason_ID (int P_AssignmentReason_ID)
{
if (P_AssignmentReason_ID < 1) throw new IllegalArgumentException ("P_AssignmentReason_ID is mandatory.");
set_Value ("P_AssignmentReason_ID", new Integer(P_AssignmentReason_ID));
}
/** Get Assignment Reason.
@return Assignment Reason */
public int getP_AssignmentReason_ID() 
{
Integer ii = (Integer)get_Value("P_AssignmentReason_ID");
if (ii == null) return 0;
return ii.intValue();
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
}
