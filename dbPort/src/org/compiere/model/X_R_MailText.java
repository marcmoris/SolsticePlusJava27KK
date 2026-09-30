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
/** Generated Model for R_MailText
 *  @author Jorg Janke (generated) 
 *  @version Release 2.6.1 - $Id: X_R_MailText.java,v 1.1 2007/07/18 14:42:20 marmor01 Exp $ */
public class X_R_MailText extends PO
{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/** Standard Constructor
@param ctx context
@param R_MailText_ID id
@param trxName transaction
*/
public X_R_MailText (Properties ctx, int R_MailText_ID, String trxName)
{
super (ctx, R_MailText_ID, trxName);
/** if (R_MailText_ID == 0)
{
setIsHtml (false);
setMailText (null);
setName (null);
setR_MailText_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_R_MailText (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=416 */
public static final int Table_ID=416;

/** TableName=R_MailText */
public static final String Table_Name="R_MailText";

protected static KeyNamePair Model = new KeyNamePair(416,"R_MailText");

protected BigDecimal accessLevel = new BigDecimal(7);
/** AccessLevel
@return 7 - System - Client - Org 
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
StringBuffer sb = new StringBuffer ("X_R_MailText[").append(get_ID()).append("]");
return sb.toString();
}
/** Set HTML.
@param IsHtml Text has HTML tags */
public void setIsHtml (boolean IsHtml)
{
set_Value ("IsHtml", new Boolean(IsHtml));
}
/** Get HTML.
@return Text has HTML tags */
public boolean isHtml() 
{
Object oo = get_Value("IsHtml");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Subject.
@param MailHeader Mail Header (Subject) */
public void setMailHeader (String MailHeader)
{
if (MailHeader != null && MailHeader.length() > 2000)
{
log.warning("Length > 2000 - truncated");
MailHeader = MailHeader.substring(0,1999);
}
set_Value ("MailHeader", MailHeader);
}
/** Get Subject.
@return Mail Header (Subject) */
public String getMailHeader() 
{
return (String)get_Value("MailHeader");
}
/** Set Mail Text.
@param MailText Text used for Mail message */
public void setMailText (String MailText)
{
if (MailText == null) throw new IllegalArgumentException ("MailText is mandatory.");
if (MailText.length() > 2000)
{
log.warning("Length > 2000 - truncated");
MailText = MailText.substring(0,1999);
}
set_Value ("MailText", MailText);
}
/** Get Mail Text.
@return Text used for Mail message */
public String getMailText() 
{
return (String)get_Value("MailText");
}
/** Set Mail Text 2.
@param MailText2 Optional second text part used for Mail message */
public void setMailText2 (String MailText2)
{
if (MailText2 != null && MailText2.length() > 2000)
{
log.warning("Length > 2000 - truncated");
MailText2 = MailText2.substring(0,1999);
}
set_Value ("MailText2", MailText2);
}
/** Get Mail Text 2.
@return Optional second text part used for Mail message */
public String getMailText2() 
{
return (String)get_Value("MailText2");
}
/** Set Mail Text 3.
@param MailText3 Optional third text part used for Mail message */
public void setMailText3 (String MailText3)
{
if (MailText3 != null && MailText3.length() > 2000)
{
log.warning("Length > 2000 - truncated");
MailText3 = MailText3.substring(0,1999);
}
set_Value ("MailText3", MailText3);
}
/** Get Mail Text 3.
@return Optional third text part used for Mail message */
public String getMailText3() 
{
return (String)get_Value("MailText3");
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
/** Set Mail Template.
@param R_MailText_ID Text templates for mailings */
public void setR_MailText_ID (int R_MailText_ID)
{
if (R_MailText_ID < 1) throw new IllegalArgumentException ("R_MailText_ID is mandatory.");
set_ValueNoCheck ("R_MailText_ID", new Integer(R_MailText_ID));
}
/** Get Mail Template.
@return Text templates for mailings */
public int getR_MailText_ID() 
{
Integer ii = (Integer)get_Value("R_MailText_ID");
if (ii == null) return 0;
return ii.intValue();
}

public void setReportName (String ReportName)
{
if (ReportName == null) throw new IllegalArgumentException ("Name is mandatory.");
if (ReportName.length() > 60)
{
log.warning("Length > 60 - truncated");
ReportName = ReportName.substring(0,59);
}
set_Value ("ReportName", ReportName);
}
/** Get Name.
@return Alphanumeric identifier of the entity */
public String getReportName() 
{
return (String)get_Value("ReportName");
}

/** EmployeeType AD_Reference_ID=2100415 */
public static final int EMPLOYEETYPE_AD_Reference_ID=2100415;
/** No Restriction = A */
public static final String EMPLOYEETYPE_NoRestriction = "A";
/** Exclude Listed Employees = E */
public static final String EMPLOYEETYPE_ExcludeListedEmployees = "E";
/** Select Only Listed Employees = S */
public static final String EMPLOYEETYPE_SelectOnlyListedEmployees = "S";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isEmployeeTypeValid (String test)
{
return test.equals("A") || test.equals("E") || test.equals("S");
}
/** Set EmployeeType.
@param EmployeeType EmployeeType */
public void setEmployeeType (String EmployeeType)
{
if (EmployeeType == null) throw new IllegalArgumentException ("EmployeeType is mandatory");
if (!isEmployeeTypeValid(EmployeeType))
throw new IllegalArgumentException ("EmployeeType Invalid value - " + EmployeeType + " - Reference_ID=2100415 - A - E - S");
if (EmployeeType.length() > 1)
{
log.warning("Length > 1 - truncated");
EmployeeType = EmployeeType.substring(0,0);
}
set_Value ("EmployeeType", EmployeeType);
}
/** Get EmployeeType.
@return EmployeeType */
public String getEmployeeType() 
{
return (String)get_Value("EmployeeType");
}

/** GroupType AD_Reference_ID=2100416 */
public static final int GROUPTYPE_AD_Reference_ID=2100416;
/** No Restriction = A */
public static final String GROUPTYPE_NoRestriction = "A";
/** Exclude Listed Job Title = E */
public static final String GROUPTYPE_ExcludeListedJobTitle = "E";
/** Select Only Listed Job Title = S */
public static final String GROUPTYPE_SelectOnlyListedJobTitle = "S";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isGroupTypeValid (String test)
{
return test.equals("A") || test.equals("E") || test.equals("S");
}
/** Set GroupType.
@param GroupType GroupType */
public void setGroupType (String GroupType)
{
if (GroupType == null) throw new IllegalArgumentException ("GroupType is mandatory");
if (!isGroupTypeValid(GroupType))
throw new IllegalArgumentException ("GroupType Invalid value - " + GroupType + " - Reference_ID=2100416 - A - E - S");
if (GroupType.length() > 1)
{
log.warning("Length > 1 - truncated");
GroupType = GroupType.substring(0,0);
}
set_Value ("GroupType", GroupType);
}
/** Get GroupType.
@return GroupType */
public String getGroupType() 
{
return (String)get_Value("GroupType");
}

/** Set Is Web Mail.
@param IsWebMail Is Web Mail */
public void setIsWebMail (boolean IsWebMail)
{
set_Value ("IsWebMail", new Boolean(IsWebMail));
}
/** Get Is Web Mail.
@return Is Web Mail */
public boolean isWebMail() 
{
Object oo = get_Value("IsWebMail");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}

/** P_Collective_Labour_Agr_ID AD_Reference_ID=2000006 */
public static final int P_COLLECTIVE_LABOUR_AGR_ID_AD_Reference_ID=2000006;
/** Set Collective Labour Agreement.
@param P_Collective_Labour_Agr_ID Collective Labour Agreement */
public void setP_Collective_Labour_Agr_ID (int P_Collective_Labour_Agr_ID)
{
if (P_Collective_Labour_Agr_ID <= 0) set_Value ("P_Collective_Labour_Agr_ID", null);
 else 
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

/** P_Employee_ID AD_Reference_ID=2000010 */
public static final int P_EMPLOYEE_ID_AD_Reference_ID=2000010;
/** Set Employee.
@param P_Employee_ID Employee */
public void setP_Employee_ID (int P_Employee_ID)
{
if (P_Employee_ID <= 0) set_Value ("P_Employee_ID", null);
 else 
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

/** P_Job_Title_ID AD_Reference_ID=2000025 */
public static final int P_JOB_TITLE_ID_AD_Reference_ID=2000025;
/** Set Job Title.
@param P_Job_Title_ID Job Title */
public void setP_Job_Title_ID (int P_Job_Title_ID)
{
if (P_Job_Title_ID <= 0) set_Value ("P_Job_Title_ID", null);
 else 
set_Value ("P_Job_Title_ID", new Integer(P_Job_Title_ID));
}
/** Get Job Title.
@return Job Title */
public int getP_Job_Title_ID() 
{
Integer ii = (Integer)get_Value("P_Job_Title_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Occupation_Group_ID AD_Reference_ID=2000014 */
public static final int P_OCCUPATION_GROUP_ID_AD_Reference_ID=2000014;
/** Set Occupation Group.
@param P_Occupation_Group_ID Occupation Group */
public void setP_Occupation_Group_ID (int P_Occupation_Group_ID)
{
if (P_Occupation_Group_ID <= 0) set_Value ("P_Occupation_Group_ID", null);
 else 
set_Value ("P_Occupation_Group_ID", new Integer(P_Occupation_Group_ID));
}
/** Get Occupation Group.
@return Occupation Group */
public int getP_Occupation_Group_ID() 
{
Integer ii = (Integer)get_Value("P_Occupation_Group_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** SelectionType AD_Reference_ID=2100417 */
public static final int SELECTIONTYPE_AD_Reference_ID=2100417;
/** For Only One Employee = E */
public static final String SELECTIONTYPE_ForOnlyOneEmployee = "E";
/** For Only One Group = G */
public static final String SELECTIONTYPE_ForOnlyOneGroup = "G";
/** For Only One Job Title = T */
public static final String SELECTIONTYPE_ForOnlyOneJobTitle = "T";
/** Collective Labour Agr Only = C */
public static final String SELECTIONTYPE_CollectiveLabourAgrOnly = "C";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isSelectionTypeValid (String test)
{
return test.equals("E") || test.equals("G") || test.equals("T") || test.equals("C");
}

/** Set SelectionType.
@param SelectionType SelectionType */
public void setSelectionType (String SelectionType)
{
if (SelectionType == null) throw new IllegalArgumentException ("SelectionType is mandatory");
if (!isSelectionTypeValid(SelectionType))
throw new IllegalArgumentException ("SelectionType Invalid value - " + SelectionType + " - Reference_ID=2100417 - E - G - T - C");
if (SelectionType.length() > 1)
{
log.warning("Length > 1 - truncated");
SelectionType = SelectionType.substring(0,0);
}
set_Value ("SelectionType", SelectionType);
}
/** Get SelectionType.
@return SelectionType */
public String getSelectionType() 
{
return (String)get_Value("SelectionType");
}


}
