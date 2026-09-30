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
package org.compiere.model;

/** Generated Model - DO NOT CHANGE */
import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;
/** Generated Model for C_JobAssignment
 *  @author Jorg Janke (generated) 
 *  @version Release 2.6.1 - $Id: X_C_JobAssignment.java,v 1.1 2007/07/18 14:42:40 marmor01 Exp $ */
public class X_C_JobAssignment extends PO
{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/** Standard Constructor
@param ctx context
@param C_JobAssignment_ID id
@param trxName transaction
*/
public X_C_JobAssignment (Properties ctx, int C_JobAssignment_ID, String trxName)
{
super (ctx, C_JobAssignment_ID, trxName);
/** if (C_JobAssignment_ID == 0)
{
setAD_User_ID (0);
setC_JobAssignment_ID (0);
setC_Job_ID (0);
setJobAssignmentType (null);	// P
setValidFrom (new Timestamp(System.currentTimeMillis()));
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_C_JobAssignment (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=791 */
public static final int Table_ID=791;

/** TableName=C_JobAssignment */
public static final String Table_Name="C_JobAssignment";

protected static KeyNamePair Model = new KeyNamePair(791,"C_JobAssignment");

protected BigDecimal accessLevel = new BigDecimal(2);
/** AccessLevel
@return 2 - Client 
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
StringBuffer sb = new StringBuffer ("X_C_JobAssignment[").append(get_ID()).append("]");
return sb.toString();
}
/** Set User/Contact.
@param AD_User_ID User within the system - Internal or Business Partner Contact */
public void setAD_User_ID (int AD_User_ID)
{
if (AD_User_ID < 1) throw new IllegalArgumentException ("AD_User_ID is mandatory.");
set_ValueNoCheck ("AD_User_ID", new Integer(AD_User_ID));
}
/** Get User/Contact.
@return User within the system - Internal or Business Partner Contact */
public int getAD_User_ID() 
{
Integer ii = (Integer)get_Value("AD_User_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Position Assignment.
@param C_JobAssignment_ID Assignemt of Employee (User) to Job Position */
public void setC_JobAssignment_ID (int C_JobAssignment_ID)
{
if (C_JobAssignment_ID < 1) throw new IllegalArgumentException ("C_JobAssignment_ID is mandatory.");
set_ValueNoCheck ("C_JobAssignment_ID", new Integer(C_JobAssignment_ID));
}
/** Get Position Assignment.
@return Assignemt of Employee (User) to Job Position */
public int getC_JobAssignment_ID() 
{
Integer ii = (Integer)get_Value("C_JobAssignment_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Position.
@param C_Job_ID Job Position */
public void setC_Job_ID (int C_Job_ID)
{
if (C_Job_ID < 1) throw new IllegalArgumentException ("C_Job_ID is mandatory.");
set_ValueNoCheck ("C_Job_ID", new Integer(C_Job_ID));
}
/** Get Position.
@return Job Position */
public int getC_Job_ID() 
{
Integer ii = (Integer)get_Value("C_Job_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Get Record ID/ColumnName
@return ID/ColumnName pair */
public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), String.valueOf(getC_Job_ID()));
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

/** JobAssignmentType AD_Reference_ID=400 */
public static final int JOBASSIGNMENTTYPE_AD_Reference_ID=400;
/** Other = O */
public static final String JOBASSIGNMENTTYPE_Other = "O";
/** Primary = P */
public static final String JOBASSIGNMENTTYPE_Primary = "P";
/** Secondary = S */
public static final String JOBASSIGNMENTTYPE_Secondary = "S";
/** Temporary = T */
public static final String JOBASSIGNMENTTYPE_Temporary = "T";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isJobAssignmentTypeValid (String test)
{
return test.equals("O") || test.equals("P") || test.equals("S") || test.equals("T");
}
/** Set Assignment Type.
@param JobAssignmentType Job Assignment Type */
public void setJobAssignmentType (String JobAssignmentType)
{
if (JobAssignmentType == null) throw new IllegalArgumentException ("JobAssignmentType is mandatory");
if (!isJobAssignmentTypeValid(JobAssignmentType))
throw new IllegalArgumentException ("JobAssignmentType Invalid value - " + JobAssignmentType + " - Reference_ID=400 - O - P - S - T");
if (JobAssignmentType.length() > 1)
{
log.warning("Length > 1 - truncated");
JobAssignmentType = JobAssignmentType.substring(0,0);
}
set_Value ("JobAssignmentType", JobAssignmentType);
}
/** Get Assignment Type.
@return Job Assignment Type */
public String getJobAssignmentType() 
{
return (String)get_Value("JobAssignmentType");
}
/** Set Valid from.
@param ValidFrom Valid from including this date (first day) */
public void setValidFrom (Timestamp ValidFrom)
{
if (ValidFrom == null) throw new IllegalArgumentException ("ValidFrom is mandatory.");
set_Value ("ValidFrom", ValidFrom);
}
/** Get Valid from.
@return Valid from including this date (first day) */
public Timestamp getValidFrom() 
{
return (Timestamp)get_Value("ValidFrom");
}
/** Set Valid to.
@param ValidTo Valid to including this date (last day) */
public void setValidTo (Timestamp ValidTo)
{
set_Value ("ValidTo", ValidTo);
}
/** Get Valid to.
@return Valid to including this date (last day) */
public Timestamp getValidTo() 
{
return (Timestamp)get_Value("ValidTo");
}
}
