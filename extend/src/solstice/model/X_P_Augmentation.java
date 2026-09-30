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
/** Generated Model for P_Augmentation
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Augmentation extends PO
{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/** Standard Constructor
@param ctx context
@param P_Augmentation_ID id
@param trxName transaction
*/
public X_P_Augmentation (Properties ctx, int P_Augmentation_ID, String trxName)
{
super (ctx, P_Augmentation_ID, trxName);
/** if (P_Augmentation_ID == 0)
{
setAugmentationDate (new Timestamp(System.currentTimeMillis()));
setAugmentationType (null);
setP_Augmentation_Gain_ID (0);
setP_Augmentation_ID (0);
setValue (null);	// @P_Augmentation_Security@=1
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Augmentation (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100787 */
public static final int Table_ID=2100787;

/** TableName=P_Augmentation */
public static final String Table_Name="P_Augmentation";

protected static KeyNamePair Model = new KeyNamePair(2100787,"P_Augmentation");

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
StringBuffer sb = new StringBuffer ("X_P_Augmentation[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Date of Increase.
@param AugmentationDate Date of Increase */
public void setAugmentationDate (Timestamp AugmentationDate)
{
if (AugmentationDate == null) throw new IllegalArgumentException ("AugmentationDate is mandatory.");
set_Value ("AugmentationDate", AugmentationDate);
}
/** Get Date of Increase.
@return Date of Increase */
public Timestamp getAugmentationDate() 
{
return (Timestamp)get_Value("AugmentationDate");
}

/** AugmentationEmployeeType AD_Reference_ID=2100415 */
public static final int AUGMENTATIONEMPLOYEETYPE_AD_Reference_ID=2100415;
/** No Restriction = A */
public static final String AUGMENTATIONEMPLOYEETYPE_NoRestriction = "A";
/** Exclude Listed Employees = E */
public static final String AUGMENTATIONEMPLOYEETYPE_ExcludeListedEmployees = "E";
/** Select Only Listed Employees = S */
public static final String AUGMENTATIONEMPLOYEETYPE_SelectOnlyListedEmployees = "S";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isAugmentationEmployeeTypeValid (String test)
{
return test == null || test.equals("A") || test.equals("E") || test.equals("S");
}
/** Set Employee.
@param AugmentationEmployeeType Employee */
public void setAugmentationEmployeeType (String AugmentationEmployeeType)
{
if (!isAugmentationEmployeeTypeValid(AugmentationEmployeeType))
throw new IllegalArgumentException ("AugmentationEmployeeType Invalid value - " + AugmentationEmployeeType + " - Reference_ID=2100415 - A - E - S");
if (AugmentationEmployeeType != null && AugmentationEmployeeType.length() > 1)
{
log.warning("Length > 1 - truncated");
AugmentationEmployeeType = AugmentationEmployeeType.substring(0,0);
}
set_Value ("AugmentationEmployeeType", AugmentationEmployeeType);
}
/** Get Employee.
@return Employee */
public String getAugmentationEmployeeType() 
{
return (String)get_Value("AugmentationEmployeeType");
}

/** AugmentationGroupType AD_Reference_ID=2100416 */
public static final int AUGMENTATIONGROUPTYPE_AD_Reference_ID=2100416;
/** No rRstriction = A */
public static final String AUGMENTATIONGROUPTYPE_NoRRstriction = "A";
/** Exclude Listed Job Title = E */
public static final String AUGMENTATIONGROUPTYPE_ExcludeListedJobTitle = "E";
/** Select Only Listed Job Title = S */
public static final String AUGMENTATIONGROUPTYPE_SelectOnlyListedJobTitle = "S";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isAugmentationGroupTypeValid (String test)
{
return test == null || test.equals("A") || test.equals("E") || test.equals("S");
}
/** Set Job title.
@param AugmentationGroupType Job title */
public void setAugmentationGroupType (String AugmentationGroupType)
{
if (!isAugmentationGroupTypeValid(AugmentationGroupType))
throw new IllegalArgumentException ("AugmentationGroupType Invalid value - " + AugmentationGroupType + " - Reference_ID=2100416 - A - E - S");
if (AugmentationGroupType != null && AugmentationGroupType.length() > 1)
{
log.warning("Length > 1 - truncated");
AugmentationGroupType = AugmentationGroupType.substring(0,0);
}
set_Value ("AugmentationGroupType", AugmentationGroupType);
}
/** Get Job title.
@return Job title */
public String getAugmentationGroupType() 
{
return (String)get_Value("AugmentationGroupType");
}

/** AugmentationType AD_Reference_ID=2100417 */
public static final int AUGMENTATIONTYPE_AD_Reference_ID=2100417;
/** For Only One Employee = E */
public static final String AUGMENTATIONTYPE_ForOnlyOneEmployee = "E";
/** For Only One Group = G */
public static final String AUGMENTATIONTYPE_ForOnlyOneGroup = "G";
/** For Only One Job Title = T */
public static final String AUGMENTATIONTYPE_ForOnlyOneJobTitle = "T";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isAugmentationTypeValid (String test)
{
return test.equals("E") || test.equals("G") || test.equals("T");
}
/** Set Reference type.
@param AugmentationType Reference type */
public void setAugmentationType (String AugmentationType)
{
if (AugmentationType == null) throw new IllegalArgumentException ("AugmentationType is mandatory");
if (!isAugmentationTypeValid(AugmentationType))
throw new IllegalArgumentException ("AugmentationType Invalid value - " + AugmentationType + " - Reference_ID=2100417 - E - G - T");
if (AugmentationType.length() > 1)
{
log.warning("Length > 1 - truncated");
AugmentationType = AugmentationType.substring(0,0);
}
set_Value ("AugmentationType", AugmentationType);
}
/** Get Reference type.
@return Reference type */
public String getAugmentationType() 
{
return (String)get_Value("AugmentationType");
}
/** Set % Increase.
@param AugPrcCad % Increase */
public void setAugPrcCad (BigDecimal AugPrcCad)
{
set_Value ("AugPrcCad", AugPrcCad);
}
/** Get % Increase.
@return % Increase */
public BigDecimal getAugPrcCad() 
{
BigDecimal bd = (BigDecimal)get_Value("AugPrcCad");
if (bd == null) return Env.ZERO;
return bd;
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

/** EmployeeStatus AD_Reference_ID=2100466 */
public static final int EMPLOYEESTATUS_AD_Reference_ID=2100466;
/** Active Employees Only = A */
public static final String EMPLOYEESTATUS_ActiveEmployeesOnly = "A";
/** Inactive Employees Only = I */
public static final String EMPLOYEESTATUS_InactiveEmployeesOnly = "I";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isEmployeeStatusValid (String test)
{
return test == null || test.equals("A") || test.equals("I");
}
/** Set Employee Status.
@param EmployeeStatus Employee Status */
public void setEmployeeStatus (String EmployeeStatus)
{
if (!isEmployeeStatusValid(EmployeeStatus))
throw new IllegalArgumentException ("EmployeeStatus Invalid value - " + EmployeeStatus + " - Reference_ID=2100466 - A - I");
if (EmployeeStatus != null && EmployeeStatus.length() > 1)
{
log.warning("Length > 1 - truncated");
EmployeeStatus = EmployeeStatus.substring(0,0);
}
set_Value ("EmployeeStatus", EmployeeStatus);
}
/** Get Employee Status.
@return Employee Status */
public String getEmployeeStatus() 
{
return (String)get_Value("EmployeeStatus");
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
/** Set Generate on TimeSheet.
@param IsAugmentationResult Generate on TimeSheet */
public void setIsAugmentationResult (boolean IsAugmentationResult)
{
throw new IllegalArgumentException ("IsAugmentationResult is virtual column");
}
/** Get Generate on TimeSheet.
@return Generate on TimeSheet */
public boolean isAugmentationResult() 
{
Object oo = get_Value("IsAugmentationResult");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Recipe.
@param P_Augmentation_Gain_ID Recipe */
public void setP_Augmentation_Gain_ID (int P_Augmentation_Gain_ID)
{
if (P_Augmentation_Gain_ID < 1) throw new IllegalArgumentException ("P_Augmentation_Gain_ID is mandatory.");
set_Value ("P_Augmentation_Gain_ID", new Integer(P_Augmentation_Gain_ID));
}
/** Get Recipe.
@return Recipe */
public int getP_Augmentation_Gain_ID() 
{
Integer ii = (Integer)get_Value("P_Augmentation_Gain_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Retroactivity.
@param P_Augmentation_ID Retroactivity */
public void setP_Augmentation_ID (int P_Augmentation_ID)
{
if (P_Augmentation_ID < 1) throw new IllegalArgumentException ("P_Augmentation_ID is mandatory.");
set_ValueNoCheck ("P_Augmentation_ID", new Integer(P_Augmentation_ID));
}
/** Get Retroactivity.
@return Retroactivity */
public int getP_Augmentation_ID() 
{
Integer ii = (Integer)get_Value("P_Augmentation_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_Retro_Security.
@param P_Augmentation_Security P_Retro_Security */
public void setP_Augmentation_Security (String P_Augmentation_Security)
{
throw new IllegalArgumentException ("P_Augmentation_Security is virtual column");
}
/** Get P_Retro_Security.
@return P_Retro_Security */
public String getP_Augmentation_Security() 
{
return (String)get_Value("P_Augmentation_Security");
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
/** Set Process Definition Copy.
@param ProcessDefinitionCopy Process Definition Copy */
public void setProcessDefinitionCopy (String ProcessDefinitionCopy)
{
if (ProcessDefinitionCopy != null && ProcessDefinitionCopy.length() > 1)
{
log.warning("Length > 1 - truncated");
ProcessDefinitionCopy = ProcessDefinitionCopy.substring(0,0);
}
set_Value ("ProcessDefinitionCopy", ProcessDefinitionCopy);
}
/** Get Process Definition Copy.
@return Process Definition Copy */
public String getProcessDefinitionCopy() 
{
return (String)get_Value("ProcessDefinitionCopy");
}
/** Set Delete Results.
@param ProcessDescription Delete Results */
public void setProcessDescription (String ProcessDescription)
{
if (ProcessDescription != null && ProcessDescription.length() > 1)
{
log.warning("Length > 1 - truncated");
ProcessDescription = ProcessDescription.substring(0,0);
}
set_Value ("ProcessDescription", ProcessDescription);
}
/** Get Delete Results.
@return Delete Results */
public String getProcessDescription() 
{
return (String)get_Value("ProcessDescription");
}
/** Set Generate Payments.
@param ProcessPaymentGeneration Generate Payments */
public void setProcessPaymentGeneration (String ProcessPaymentGeneration)
{
if (ProcessPaymentGeneration != null && ProcessPaymentGeneration.length() > 1)
{
log.warning("Length > 1 - truncated");
ProcessPaymentGeneration = ProcessPaymentGeneration.substring(0,0);
}
set_Value ("ProcessPaymentGeneration", ProcessPaymentGeneration);
}
/** Get Generate Payments.
@return Generate Payments */
public String getProcessPaymentGeneration() 
{
return (String)get_Value("ProcessPaymentGeneration");
}
/** Set Process Retroactive Payments.
@param ProcessRetro Process Retroactive Payments */
public void setProcessRetro (String ProcessRetro)
{
if (ProcessRetro != null && ProcessRetro.length() > 1)
{
log.warning("Length > 1 - truncated");
ProcessRetro = ProcessRetro.substring(0,0);
}
set_Value ("ProcessRetro", ProcessRetro);
}
/** Get Process Retroactive Payments.
@return Process Retroactive Payments */
public String getProcessRetro() 
{
return (String)get_Value("ProcessRetro");
}
/** Set Disactivation Date of Scale.
@param ScaleEndDate Disactivation Date of Scale */
public void setScaleEndDate (Timestamp ScaleEndDate)
{
set_Value ("ScaleEndDate", ScaleEndDate);
}
/** Get Disactivation Date of Scale.
@return Disactivation Date of Scale */
public Timestamp getScaleEndDate() 
{
return (Timestamp)get_Value("ScaleEndDate");
}
/** Set Scale Active Date.
@param ScaleStartDate Scale Active Date */
public void setScaleStartDate (Timestamp ScaleStartDate)
{
set_Value ("ScaleStartDate", ScaleStartDate);
}
/** Get Scale Active Date.
@return Scale Active Date */
public Timestamp getScaleStartDate() 
{
return (Timestamp)get_Value("ScaleStartDate");
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
if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
if (Value.length() > 6)
{
log.warning("Length > 6 - truncated");
Value = Value.substring(0,5);
}
set_Value ("Value", Value);
}
/** Get Search Key.
@return Search key for the record in the format required - must be unique */
public String getValue() 
{
return (String)get_Value("Value");
}
/** Get Record ID/ColumnName
@return ID/ColumnName pair */
public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), getValue());
}
}
