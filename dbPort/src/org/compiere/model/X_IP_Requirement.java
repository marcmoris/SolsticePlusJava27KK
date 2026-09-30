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
/** Generated Model for IP_Requirement
 *  @author Jorg Janke (generated) 
 *  @version Release 2.6.1 - $Id: X_IP_Requirement.java,v 1.1 2007/07/18 14:42:34 marmor01 Exp $ */
public class X_IP_Requirement extends PO
{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/** Standard Constructor
@param ctx context
@param IP_Requirement_ID id
@param trxName transaction
*/
public X_IP_Requirement (Properties ctx, int IP_Requirement_ID, String trxName)
{
super (ctx, IP_Requirement_ID, trxName);
/** if (IP_Requirement_ID == 0)
{
setIP_Requirement_ID (0);
setIsSummary (false);
setName (null);
setRequirementType (null);	// O
setStandardQty (Env.ZERO);
setTaskType (null);	// O
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_IP_Requirement (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=906 */
public static final int Table_ID=906;

/** TableName=IP_Requirement */
public static final String Table_Name="IP_Requirement";

protected static KeyNamePair Model = new KeyNamePair(906,"IP_Requirement");

protected BigDecimal accessLevel = new BigDecimal(4);
/** AccessLevel
@return 4 - System 
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
StringBuffer sb = new StringBuffer ("X_IP_Requirement[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Process.
@param AD_Process_ID Process or Report */
public void setAD_Process_ID (int AD_Process_ID)
{
if (AD_Process_ID <= 0) set_Value ("AD_Process_ID", null);
 else 
set_Value ("AD_Process_ID", new Integer(AD_Process_ID));
}
/** Get Process.
@return Process or Report */
public int getAD_Process_ID() 
{
Integer ii = (Integer)get_Value("AD_Process_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Table.
@param AD_Table_ID Database Table information */
public void setAD_Table_ID (int AD_Table_ID)
{
if (AD_Table_ID <= 0) set_Value ("AD_Table_ID", null);
 else 
set_Value ("AD_Table_ID", new Integer(AD_Table_ID));
}
/** Get Table.
@return Database Table information */
public int getAD_Table_ID() 
{
Integer ii = (Integer)get_Value("AD_Table_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Window.
@param AD_Window_ID Data entry or display window */
public void setAD_Window_ID (int AD_Window_ID)
{
if (AD_Window_ID <= 0) set_Value ("AD_Window_ID", null);
 else 
set_Value ("AD_Window_ID", new Integer(AD_Window_ID));
}
/** Get Window.
@return Data entry or display window */
public int getAD_Window_ID() 
{
Integer ii = (Integer)get_Value("AD_Window_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** C_ProjectPhaseReq_ID AD_Reference_ID=405 */
public static final int C_PROJECTPHASEREQ_ID_AD_Reference_ID=405;
/** Set Requirement Phase.
@param C_ProjectPhaseReq_ID Project Requirements Phase */
public void setC_ProjectPhaseReq_ID (int C_ProjectPhaseReq_ID)
{
if (C_ProjectPhaseReq_ID <= 0) set_Value ("C_ProjectPhaseReq_ID", null);
 else 
set_Value ("C_ProjectPhaseReq_ID", new Integer(C_ProjectPhaseReq_ID));
}
/** Get Requirement Phase.
@return Project Requirements Phase */
public int getC_ProjectPhaseReq_ID() 
{
Integer ii = (Integer)get_Value("C_ProjectPhaseReq_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Project.
@param C_Project_ID Financial Project */
public void setC_Project_ID (int C_Project_ID)
{
if (C_Project_ID <= 0) set_Value ("C_Project_ID", null);
 else 
set_Value ("C_Project_ID", new Integer(C_Project_ID));
}
/** Get Project.
@return Financial Project */
public int getC_Project_ID() 
{
Integer ii = (Integer)get_Value("C_Project_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Consequences.
@param Consequences Consequences of the entry */
public void setConsequences (String Consequences)
{
if (Consequences != null && Consequences.length() > 2000)
{
log.warning("Length > 2000 - truncated");
Consequences = Consequences.substring(0,1999);
}
set_Value ("Consequences", Consequences);
}
/** Get Consequences.
@return Consequences of the entry */
public String getConsequences() 
{
return (String)get_Value("Consequences");
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
/** Set Comment/Help.
@param Help Comment or Hint */
public void setHelp (String Help)
{
if (Help != null && Help.length() > 2000)
{
log.warning("Length > 2000 - truncated");
Help = Help.substring(0,1999);
}
set_Value ("Help", Help);
}
/** Get Comment/Help.
@return Comment or Hint */
public String getHelp() 
{
return (String)get_Value("Help");
}
/** Set Requirement.
@param IP_Requirement_ID Business Requirement */
public void setIP_Requirement_ID (int IP_Requirement_ID)
{
if (IP_Requirement_ID < 1) throw new IllegalArgumentException ("IP_Requirement_ID is mandatory.");
set_ValueNoCheck ("IP_Requirement_ID", new Integer(IP_Requirement_ID));
}
/** Get Requirement.
@return Business Requirement */
public int getIP_Requirement_ID() 
{
Integer ii = (Integer)get_Value("IP_Requirement_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Summary Level.
@param IsSummary This is a summary entity */
public void setIsSummary (boolean IsSummary)
{
set_Value ("IsSummary", new Boolean(IsSummary));
}
/** Get Summary Level.
@return This is a summary entity */
public boolean isSummary() 
{
Object oo = get_Value("IsSummary");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Name.
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
/** Get Name.
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
/** Set Prerequisites.
@param Prerequisites Prerequisites of this entity */
public void setPrerequisites (String Prerequisites)
{
if (Prerequisites != null && Prerequisites.length() > 2000)
{
log.warning("Length > 2000 - truncated");
Prerequisites = Prerequisites.substring(0,1999);
}
set_Value ("Prerequisites", Prerequisites);
}
/** Get Prerequisites.
@return Prerequisites of this entity */
public String getPrerequisites() 
{
return (String)get_Value("Prerequisites");
}

/** RequirementType AD_Reference_ID=407 */
public static final int REQUIREMENTTYPE_AD_Reference_ID=407;
/** Other = O */
public static final String REQUIREMENTTYPE_Other = "O";
/** Determine Scope = S */
public static final String REQUIREMENTTYPE_DetermineScope = "S";
/** Implementation Task = T */
public static final String REQUIREMENTTYPE_ImplementationTask = "T";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isRequirementTypeValid (String test)
{
return test.equals("O") || test.equals("S") || test.equals("T");
}
/** Set Requirement Type.
@param RequirementType Requirement Type */
public void setRequirementType (String RequirementType)
{
if (RequirementType == null) throw new IllegalArgumentException ("RequirementType is mandatory");
if (!isRequirementTypeValid(RequirementType))
throw new IllegalArgumentException ("RequirementType Invalid value - " + RequirementType + " - Reference_ID=407 - O - S - T");
if (RequirementType.length() > 1)
{
log.warning("Length > 1 - truncated");
RequirementType = RequirementType.substring(0,0);
}
set_Value ("RequirementType", RequirementType);
}
/** Get Requirement Type.
@return Requirement Type */
public String getRequirementType() 
{
return (String)get_Value("RequirementType");
}
/** Set Standard Quantity.
@param StandardQty Standard Quantity */
public void setStandardQty (BigDecimal StandardQty)
{
if (StandardQty == null) throw new IllegalArgumentException ("StandardQty is mandatory.");
set_Value ("StandardQty", StandardQty);
}
/** Get Standard Quantity.
@return Standard Quantity */
public BigDecimal getStandardQty() 
{
BigDecimal bd = (BigDecimal)get_Value("StandardQty");
if (bd == null) return Env.ZERO;
return bd;
}

/** TaskType AD_Reference_ID=408 */
public static final int TASKTYPE_AD_Reference_ID=408;
/** Personal Activity = A */
public static final String TASKTYPE_PersonalActivity = "A";
/** Delegation = D */
public static final String TASKTYPE_Delegation = "D";
/** Other = O */
public static final String TASKTYPE_Other = "O";
/** Research = R */
public static final String TASKTYPE_Research = "R";
/** Test/Verify = T */
public static final String TASKTYPE_TestVerify = "T";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isTaskTypeValid (String test)
{
return test.equals("A") || test.equals("D") || test.equals("O") || test.equals("R") || test.equals("T");
}
/** Set Task Type.
@param TaskType Type of Project Task */
public void setTaskType (String TaskType)
{
if (TaskType == null) throw new IllegalArgumentException ("TaskType is mandatory");
if (!isTaskTypeValid(TaskType))
throw new IllegalArgumentException ("TaskType Invalid value - " + TaskType + " - Reference_ID=408 - A - D - O - R - T");
if (TaskType.length() > 1)
{
log.warning("Length > 1 - truncated");
TaskType = TaskType.substring(0,0);
}
set_Value ("TaskType", TaskType);
}
/** Get Task Type.
@return Type of Project Task */
public String getTaskType() 
{
return (String)get_Value("TaskType");
}
}
