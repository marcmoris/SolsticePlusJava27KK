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
/** Generated Model for P_Assignment
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Assignment extends PO
{
/** Standard Constructor
@param ctx context
@param P_Assignment_ID id
@param trxName transaction
*/
public X_P_Assignment (Properties ctx, int P_Assignment_ID, String trxName)
{
super (ctx, P_Assignment_ID, trxName);
/** if (P_Assignment_ID == 0)
{
setAssignmentType (null);	// P
setIsIncumbent (false);
setP_Assignment_ID (0);
setP_Assignment_State_ID (0);
setP_Employee_ID (0);
setStartDate (new Timestamp(System.currentTimeMillis()));
setValue (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Assignment (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000085 */
public static final int Table_ID=2000085;

/** TableName=P_Assignment */
public static final String Table_Name="P_Assignment";

protected static KeyNamePair Model = new KeyNamePair(2000085,"P_Assignment");

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
StringBuffer sb = new StringBuffer ("X_P_Assignment[").append(get_ID()).append("]");
return sb.toString();
}

/** AssignmentType AD_Reference_ID=2000067 */
public static final int ASSIGNMENTTYPE_AD_Reference_ID=2000067;
/** Base = B */
public static final String ASSIGNMENTTYPE_Base = "B";
/** Primary = P */
public static final String ASSIGNMENTTYPE_Primary = "P";
/** Other = O */
public static final String ASSIGNMENTTYPE_Other = "O";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isAssignmentTypeValid (String test)
{
return test.equals("B") || test.equals("P") || test.equals("O");
}
/** Set Assignment Type.
@param AssignmentType Assignment Type */
public void setAssignmentType (String AssignmentType)
{
if (AssignmentType == null) throw new IllegalArgumentException ("AssignmentType is mandatory");
if (!isAssignmentTypeValid(AssignmentType))
throw new IllegalArgumentException ("AssignmentType Invalid value - " + AssignmentType + " - Reference_ID=2000067 - B - P - O");
if (AssignmentType.length() > 1)
{
log.warning("Length > 1 - truncated");
AssignmentType = AssignmentType.substring(0,0);
}
set_Value ("AssignmentType", AssignmentType);
}
/** Get Assignment Type.
@return Assignment Type */
public String getAssignmentType() 
{
return (String)get_Value("AssignmentType");
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
/** Set Expected Advance Date.
@param ExpectedAdvanceDate Expected Advance Date */
public void setExpectedAdvanceDate (Timestamp ExpectedAdvanceDate)
{
set_Value ("ExpectedAdvanceDate", ExpectedAdvanceDate);
}
/** Get Expected Advance Date.
@return Expected Advance Date */
public Timestamp getExpectedAdvanceDate() 
{
return (Timestamp)get_Value("ExpectedAdvanceDate");
}
/** Set Is Incumbent.
@param IsIncumbent Signal if the employee is incumbent of the post */
public void setIsIncumbent (boolean IsIncumbent)
{
set_Value ("IsIncumbent", new Boolean(IsIncumbent));
}
/** Get Is Incumbent.
@return Signal if the employee is incumbent of the post */
public boolean isIncumbent() 
{
Object oo = get_Value("IsIncumbent");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Weekly Hours.
@param JobTitleWeeklyHours Weekly Hours */
public void setJobTitleWeeklyHours (BigDecimal JobTitleWeeklyHours)
{
throw new IllegalArgumentException ("JobTitleWeeklyHours is virtual column");
}
/** Get Weekly Hours.
@return Weekly Hours */
public BigDecimal getJobTitleWeeklyHours() 
{
BigDecimal bd = (BigDecimal)get_Value("JobTitleWeeklyHours");
if (bd == null) return Env.ZERO;
return bd;
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

/** P_Assignment_State_ID AD_Reference_ID=2100400 */
public static final int P_ASSIGNMENT_STATE_ID_AD_Reference_ID=2100400;
/** Set Assignment State.
@param P_Assignment_State_ID Assignment State */
public void setP_Assignment_State_ID (int P_Assignment_State_ID)
{
if (P_Assignment_State_ID < 1) throw new IllegalArgumentException ("P_Assignment_State_ID is mandatory.");
set_Value ("P_Assignment_State_ID", new Integer(P_Assignment_State_ID));
}
/** Get Assignment State.
@return Assignment State */
public int getP_Assignment_State_ID() 
{
Integer ii = (Integer)get_Value("P_Assignment_State_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Collective_Labour_Agr_ID AD_Reference_ID=2000006 */
public static final int P_COLLECTIVE_LABOUR_AGR_ID_AD_Reference_ID=2000006;
/** Set Collective Labour Agreement.
@param P_Collective_Labour_Agr_ID Collective Labour Agreement */
public void setP_Collective_Labour_Agr_ID (int P_Collective_Labour_Agr_ID)
{
throw new IllegalArgumentException ("P_Collective_Labour_Agr_ID is virtual column");
}
/** Get Collective Labour Agreement.
@return Collective Labour Agreement */
public int getP_Collective_Labour_Agr_ID() 
{
Integer ii = (Integer)get_Value("P_Collective_Labour_Agr_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Employee_ID AD_Reference_ID=2000010 */
public static final int P_EMPLOYEE_ID_AD_Reference_ID=2000010;
/** Set Employee .
@param P_Employee_ID Employee */
public void setP_Employee_ID (int P_Employee_ID)
{
if (P_Employee_ID < 1) throw new IllegalArgumentException ("P_Employee_ID is mandatory.");
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

/** P_Post_ID AD_Reference_ID=2000004 */
public static final int P_POST_ID_AD_Reference_ID=2000004;
/** Set Post.
@param P_Post_ID Post */
public void setP_Post_ID (int P_Post_ID)
{
if (P_Post_ID <= 0) set_Value ("P_Post_ID", null);
 else 
set_Value ("P_Post_ID", new Integer(P_Post_ID));
}
/** Get Post.
@return Post */
public int getP_Post_ID() 
{
Integer ii = (Integer)get_Value("P_Post_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Workplace_ID AD_Reference_ID=2000005 */
public static final int P_WORKPLACE_ID_AD_Reference_ID=2000005;
/** Set Workplace (Base).
@param P_Workplace_ID Workplace (Base) */
public void setP_Workplace_ID (int P_Workplace_ID)
{
	set_Value ("P_Workplace_ID", new Integer(P_Workplace_ID));

//throw new IllegalArgumentException ("P_Workplace_ID is virtual column");
}
/** Get Workplace (Base).
@return Workplace (Base) */
public int getP_Workplace_ID() 
{
Integer ii = (Integer)get_Value("P_Workplace_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** PostActivity AD_Reference_ID=142 */
public static final int POSTACTIVITY_AD_Reference_ID=142;
/** Set Activity.
@param PostActivity Activity */
public void setPostActivity (int PostActivity)
{
throw new IllegalArgumentException ("PostActivity is virtual column");
}
/** Get Activity.
@return Activity */
public int getPostActivity() 
{
Integer ii = (Integer)get_Value("PostActivity");
if (ii == null) return 0;
return ii.intValue();
}

/** PostJobTitle AD_Reference_ID=2000025 */
public static final int POSTJOBTITLE_AD_Reference_ID=2000025;
/** Set Job Title.
@param PostJobTitle Job Title */
public void setPostJobTitle (int PostJobTitle)
{
throw new IllegalArgumentException ("PostJobTitle is virtual column");
}
/** Get Job Title.
@return Job Title */
public int getPostJobTitle() 
{
Integer ii = (Integer)get_Value("PostJobTitle");
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

/** Set Expected end of contract date.
@param ExpectedEndDate Expected end of contract date */
public void setExpectedEndDate (Timestamp ExpectedEndDate)
{
set_Value ("ExpectedEndDate", ExpectedEndDate);
}
/** Get Expected end of contract date.
@return Expected end of contract date */
public Timestamp getExpectedEndDate() 
{
return (Timestamp)get_Value("ExpectedEndDate");
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

/** Set Equity Code CNP.
@param EquityCodeCNP Equity Code CNP */
public void setEquityCodeCNP (String EquityCodeCNP)
{
if (EquityCodeCNP != null && EquityCodeCNP.length() > 4)
{
log.warning("Length > 4 - truncated");
EquityCodeCNP = EquityCodeCNP.substring(0,3);
}
set_Value ("EquityCodeCNP", EquityCodeCNP);
}
/** Get Equity Code CNP.
@return Equity Code CNP */
public String getEquityCodeCNP() 
{
return (String)get_Value("EquityCodeCNP");
}

}
