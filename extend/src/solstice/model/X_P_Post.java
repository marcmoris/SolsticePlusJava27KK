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
/** Generated Model for P_Post
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Post extends PO
{
/** Standard Constructor
@param ctx context
@param P_Post_ID id
@param trxName transaction
*/
public X_P_Post (Properties ctx, int P_Post_ID, String trxName)
{
super (ctx, P_Post_ID, trxName);
/** if (P_Post_ID == 0)
{
setName (null);
setP_Collective_Labour_Agr_ID (0);
setP_Post_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Post (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000015 */
public static final int Table_ID=2000015;

/** TableName=P_Post */
public static final String Table_Name="P_Post";

protected static KeyNamePair Model = new KeyNamePair(2000015,"P_Post");

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
StringBuffer sb = new StringBuffer ("X_P_Post[").append(get_ID()).append("]");
return sb.toString();
}

/** C_Activity_ID AD_Reference_ID=142 */
public static final int C_ACTIVITY_ID_AD_Reference_ID=142;
/** Set Function.
@param C_Activity_ID Function */
public void setC_Activity_ID (int C_Activity_ID)
{
if (C_Activity_ID <= 0) set_Value ("C_Activity_ID", null);
 else 
set_Value ("C_Activity_ID", new Integer(C_Activity_ID));
}
/** Get Function.
@return Function */
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
if (Description != null && Description.length() > 2000)
{
log.warning("Length > 2000 - truncated");
Description = Description.substring(0,1999);
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
/** Set Equivalence  Full Time.
@param Equivalence_Full_Time   */
public void setEquivalence_Full_Time (BigDecimal Equivalence_Full_Time)
{
set_Value ("Equivalence_Full_Time", Equivalence_Full_Time);
}
/** Get Equivalence  Full Time.
@return   */
public BigDecimal getEquivalence_Full_Time() 
{
BigDecimal bd = (BigDecimal)get_Value("Equivalence_Full_Time");
if (bd == null) return Env.ZERO;
return bd;
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

/** P_Collective_Labour_Agr_ID AD_Reference_ID=2000006 */
public static final int P_COLLECTIVE_LABOUR_AGR_ID_AD_Reference_ID=2000006;
/** Set Collective Labour Agreement.
@param P_Collective_Labour_Agr_ID Collective Labour Agreement */
public void setP_Collective_Labour_Agr_ID (int P_Collective_Labour_Agr_ID)
{
if (P_Collective_Labour_Agr_ID < 1) throw new IllegalArgumentException ("P_Collective_Labour_Agr_ID is mandatory.");
set_Value ("P_Collective_Labour_Agr_ID", new Integer(P_Collective_Labour_Agr_ID));
}
/** Get Collective Labour Agreement.
@return Collective Labour Agreement */
public int getP_Collective_Labour_Agr_ID() 
{
Integer ii = (Integer)get_Value("P_Collective_Labour_Agr_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Job_Title_ID AD_Reference_ID=2000025 */
public static final int P_JOB_TITLE_ID_AD_Reference_ID=2000025;
/** Set Job Title.
@param P_Job_Title_ID   */
public void setP_Job_Title_ID (int P_Job_Title_ID)
{
if (P_Job_Title_ID <= 0) set_Value ("P_Job_Title_ID", null);
 else 
set_Value ("P_Job_Title_ID", new Integer(P_Job_Title_ID));
}
/** Get Job Title.
@return   */
public int getP_Job_Title_ID() 
{
Integer ii = (Integer)get_Value("P_Job_Title_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Occupation_Group_ID AD_Reference_ID=2000014 */
public static final int P_OCCUPATION_GROUP_ID_AD_Reference_ID=2000014;
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
/** Set Post.
@param P_Post_ID Post */
public void setP_Post_ID (int P_Post_ID)
{
if (P_Post_ID < 1) throw new IllegalArgumentException ("P_Post_ID is mandatory.");
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

/** P_Post_ID_1 AD_Reference_ID=2000004 */
public static final int P_POST_ID_1_AD_Reference_ID=2000004;
/** Set Post superior.
@param P_Post_ID_1   */
public void setP_Post_ID_1 (int P_Post_ID_1)
{
set_Value ("P_Post_ID_1", new Integer(P_Post_ID_1));
}
/** Get Post superior.
@return   */
public int getP_Post_ID_1() 
{
Integer ii = (Integer)get_Value("P_Post_ID_1");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Post_Struct_ID AD_Reference_ID=2100413 */
public static final int P_POST_STRUCT_ID_AD_Reference_ID=2100413;
/** Set Hierarchy code.
@param P_Post_Struct_ID Hierarchy code */
public void setP_Post_Struct_ID (int P_Post_Struct_ID)
{
if (P_Post_Struct_ID <= 0) set_Value ("P_Post_Struct_ID", null);
 else 
set_Value ("P_Post_Struct_ID", new Integer(P_Post_Struct_ID));
}
/** Get Hierarchy code.
@return Hierarchy code */
public int getP_Post_Struct_ID() 
{
Integer ii = (Integer)get_Value("P_Post_Struct_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Post_Type_ID AD_Reference_ID=2100368 */
public static final int P_POST_TYPE_ID_AD_Reference_ID=2100368;
/** Set Post Type.
@param P_Post_Type_ID Post Type */
public void setP_Post_Type_ID (int P_Post_Type_ID)
{
if (P_Post_Type_ID <= 0) set_Value ("P_Post_Type_ID", null);
 else 
set_Value ("P_Post_Type_ID", new Integer(P_Post_Type_ID));
}
/** Get Post Type.
@return Post Type */
public int getP_Post_Type_ID() 
{
Integer ii = (Integer)get_Value("P_Post_Type_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Salary_Class_ID AD_Reference_ID=2100354 */
public static final int P_SALARY_CLASS_ID_AD_Reference_ID=2100354;
/** Set Salary Class.
@param P_Salary_Class_ID Salary Class */
public void setP_Salary_Class_ID (int P_Salary_Class_ID)
{
if (P_Salary_Class_ID <= 0) set_Value ("P_Salary_Class_ID", null);
 else 
set_Value ("P_Salary_Class_ID", new Integer(P_Salary_Class_ID));
}
/** Get Salary Class.
@return Salary Class */
public int getP_Salary_Class_ID() 
{
Integer ii = (Integer)get_Value("P_Salary_Class_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Workplace_ID AD_Reference_ID=2000005 */
public static final int P_WORKPLACE_ID_AD_Reference_ID=2000005;
/** Set Workplace .
@param P_Workplace_ID Workplace  */
public void setP_Workplace_ID (int P_Workplace_ID)
{
if (P_Workplace_ID <= 0) set_Value ("P_Workplace_ID", null);
 else 
set_Value ("P_Workplace_ID", new Integer(P_Workplace_ID));
}
/** Get Workplace .
@return Workplace  */
public int getP_Workplace_ID() 
{
Integer ii = (Integer)get_Value("P_Workplace_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Phone.
@param Phone Identifies a telephone number */
public void setPhone (String Phone)
{
if (Phone != null && Phone.length() > 40)
{
log.warning("Length > 40 - truncated");
Phone = Phone.substring(0,39);
}
set_Value ("Phone", Phone);
}
/** Get Phone.
@return Identifies a telephone number */
public String getPhone() 
{
return (String)get_Value("Phone");
}
/** Set Extention.
@param PhoneExt Extention */
public void setPhoneExt (String PhoneExt)
{
if (PhoneExt != null && PhoneExt.length() > 10)
{
log.warning("Length > 10 - truncated");
PhoneExt = PhoneExt.substring(0,9);
}
set_Value ("PhoneExt", PhoneExt);
}
/** Get Extention.
@return Extention */
public String getPhoneExt() 
{
return (String)get_Value("PhoneExt");
}
/** Set Selection.
@param PostNameSelection Selection of the name */
public void setPostNameSelection (String PostNameSelection)
{
if (PostNameSelection != null && PostNameSelection.length() > 1)
{
log.warning("Length > 1 - truncated");
PostNameSelection = PostNameSelection.substring(0,0);
}
set_Value ("PostNameSelection", PostNameSelection);
}
/** Get Selection.
@return Selection of the name */
public String getPostNameSelection() 
{
return (String)get_Value("PostNameSelection");
}

/** PostState AD_Reference_ID=2100412 */
public static final int POSTSTATE_AD_Reference_ID=2100412;
/** Vacant = V */
public static final String POSTSTATE_Vacant = "V";
/** Vacant/Occupied = S */
public static final String POSTSTATE_VacantOccupied = "S";
/** Occupied = O */
public static final String POSTSTATE_Occupied = "O";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isPostStateValid (String test)
{
return test == null || test.equals("V") || test.equals("S") || test.equals("O");
}
/** Set Post State.
@param PostState Post State */
public void setPostState (String PostState)
{
if (!isPostStateValid(PostState))
throw new IllegalArgumentException ("PostState Invalid value - " + PostState + " - Reference_ID=2100412 - V - S - O");
throw new IllegalArgumentException ("PostState is virtual column");
}
/** Get Post State.
@return Post State */
public String getPostState() 
{
return (String)get_Value("PostState");
}
/** Set Resolution.
@param Resolution Resolution */
public void setResolution (String Resolution)
{
if (Resolution != null && Resolution.length() > 255)
{
log.warning("Length > 255 - truncated");
Resolution = Resolution.substring(0,254);
}
set_Value ("Resolution", Resolution);
}
/** Get Resolution.
@return Resolution */
public String getResolution() 
{
return (String)get_Value("Resolution");
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
if (Value != null && Value.length() > 30)
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
throw new IllegalArgumentException ("Weekly_Hours is virtual column");
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
