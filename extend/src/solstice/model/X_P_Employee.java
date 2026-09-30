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
/** Generated Model for P_Employee
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Employee extends PO
{
/** Standard Constructor
@param ctx context
@param P_Employee_ID id
@param trxName transaction
*/
public X_P_Employee (Properties ctx, int P_Employee_ID, String trxName)
{
super (ctx, P_Employee_ID, trxName);
/** if (P_Employee_ID == 0)
{
setBirthDate (new Timestamp(System.currentTimeMillis()));
setC_Activity_ID (0);
setDateHired (new Timestamp(System.currentTimeMillis()));
setDistribution_Booklet_ID (0);
setFirstName (null);
setGender (null);	// M
setName (null);
setP_Department_ID (0);
setP_Distribution_ID (0);
setP_Employee_ID (0);
setP_Employee_Manager_ID (0);
setP_Employer_ID (0);
setP_Holidays_Calendar_ID (0);
setP_Job_Type_ID (0);
setP_Language_ID (0);
setP_LongTermLeave_ID (0);
setP_Occupation_Group_ID (0);
setP_Payment_Group_ID (0);
setP_Workplace_ID (0);
setPaymentType (null);
setPrintStatementEarning (true);	// Y
setSin (null);
setSurname (null);
setTaxation_Region_ID (0);
setValue (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Employee (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100659 */
public static final int Table_ID=2100659;

/** TableName=P_Employee */
public static final String Table_Name="P_Employee";

protected static KeyNamePair Model = new KeyNamePair(2100659,"P_Employee");

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
StringBuffer sb = new StringBuffer ("X_P_Employee[").append(get_ID()).append("]");
return sb.toString();
}
/** Set User/Contact.
@param AD_User_ID User within the system - Internal or Business Partner Contact */
public void setAD_User_ID (int AD_User_ID)
{
if (AD_User_ID <= 0) set_Value ("AD_User_ID", null);
 else 
set_Value ("AD_User_ID", new Integer(AD_User_ID));
}
/** Get User/Contact.
@return User within the system - Internal or Business Partner Contact */
public int getAD_User_ID() 
{
Integer ii = (Integer)get_Value("AD_User_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Age.
@param Age Age */
public void setAge (int Age)
{
throw new IllegalArgumentException ("Age is virtual column");
}
/** Get Age.
@return Age */
public int getAge() 
{
Integer ii = (Integer)get_Value("Age");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Base Address.
@param BaseLocation Base Address */
public void setBaseLocation (int BaseLocation)
{
throw new IllegalArgumentException ("BaseLocation is virtual column");
}
/** Get Base Address.
@return Base Address */
public int getBaseLocation() 
{
Integer ii = (Integer)get_Value("BaseLocation");
if (ii == null) return 0;
return ii.intValue();
}
/** Set BirthDate.
@param BirthDate BirthDate   */
public void setBirthDate (Timestamp BirthDate)
{
if (BirthDate == null) throw new IllegalArgumentException ("BirthDate is mandatory.");
set_Value ("BirthDate", BirthDate);
}
/** Get BirthDate.
@return BirthDate   */
public Timestamp getBirthDate() 
{
return (Timestamp)get_Value("BirthDate");
}

/** C_Activity_ID AD_Reference_ID=142 */
public static final int C_ACTIVITY_ID_AD_Reference_ID=142;
/** Set Function.
@param C_Activity_ID Function */
public void setC_Activity_ID (int C_Activity_ID)
{
if (C_Activity_ID < 1) throw new IllegalArgumentException ("C_Activity_ID is mandatory.");
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

/** C_Location_ID AD_Reference_ID=133 */
public static final int C_LOCATION_ID_AD_Reference_ID=133;
/** Set Address.
@param C_Location_ID Location or Address */
public void setC_Location_ID (int C_Location_ID)
{
if (C_Location_ID <= 0) set_Value ("C_Location_ID", null);
 else 
set_Value ("C_Location_ID", new Integer(C_Location_ID));
}
/** Get Address.
@return Location or Address */
public int getC_Location_ID() 
{
Integer ii = (Integer)get_Value("C_Location_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set ChangeActivityStatus.
@param ChangeActivityStatus ChangeActivityStatus */
public void setChangeActivityStatus (boolean ChangeActivityStatus)
{
set_Value ("ChangeActivityStatus", new Boolean(ChangeActivityStatus));
}
/** Get ChangeActivityStatus.
@return ChangeActivityStatus */
public boolean isChangeActivityStatus() 
{
Object oo = get_Value("ChangeActivityStatus");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}

/** CivilStatus AD_Reference_ID=2100316 */
public static final int CIVILSTATUS_AD_Reference_ID=2100316;
/** Married = 2 */
public static final String CIVILSTATUS_Married = "2";
/** Single = 1 */
public static final String CIVILSTATUS_Single = "1";
/** Widowed = 4 */
public static final String CIVILSTATUS_Widowed = "4";
/** Legally Separated = 6 */
public static final String CIVILSTATUS_LegallySeparated = "6";
/** Annulled Marriage = 7 */
public static final String CIVILSTATUS_AnnulledMarriage = "7";
/** Divorced = 5 */
public static final String CIVILSTATUS_Divorced = "5";
/** Common law partner = 3 */
public static final String CIVILSTATUS_CommonLawPartner = "3";
/** Not Applicable  = 9 */
public static final String CIVILSTATUS_NotApplicable = "9";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isCivilStatusValid (String test)
{
return test == null || test.equals("2") || test.equals("1") || test.equals("4") || test.equals("6") || test.equals("7") || test.equals("5") || test.equals("3") || test.equals("9");
}
/** Set Civil Status.
@param CivilStatus Civil Status */
public void setCivilStatus (String CivilStatus)
{
if (!isCivilStatusValid(CivilStatus))
throw new IllegalArgumentException ("CivilStatus Invalid value - " + CivilStatus + " - Reference_ID=2100316 - 2 - 1 - 4 - 6 - 7 - 5 - 3 - 9");
if (CivilStatus != null && CivilStatus.length() > 1)
{
log.warning("Length > 1 - truncated");
CivilStatus = CivilStatus.substring(0,0);
}
set_Value ("CivilStatus", CivilStatus);
}
/** Get Civil Status.
@return Civil Status */
public String getCivilStatus() 
{
return (String)get_Value("CivilStatus");
}
/** Set Cost Center.
@param CostCenter Cost Center */
public void setCostCenter (String CostCenter)
{
throw new IllegalArgumentException ("CostCenter is virtual column");
}
/** Get Cost Center.
@return Cost Center */
public String getCostCenter() 
{
return (String)get_Value("CostCenter");
}
/** Set CustomFieldChar01.
@param CustomFieldChar01 CustomFieldChar01 */
public void setCustomFieldChar01 (String CustomFieldChar01)
{
if (CustomFieldChar01 != null && CustomFieldChar01.length() > 1000)
{
log.warning("Length > 1000 - truncated");
CustomFieldChar01 = CustomFieldChar01.substring(0,999);
}
set_Value ("CustomFieldChar01", CustomFieldChar01);
}
/** Get CustomFieldChar01.
@return CustomFieldChar01 */
public String getCustomFieldChar01() 
{
return (String)get_Value("CustomFieldChar01");
}
/** Set CustomFieldChar02.
@param CustomFieldChar02 CustomFieldChar02 */
public void setCustomFieldChar02 (String CustomFieldChar02)
{
if (CustomFieldChar02 != null && CustomFieldChar02.length() > 1000)
{
log.warning("Length > 1000 - truncated");
CustomFieldChar02 = CustomFieldChar02.substring(0,999);
}
set_Value ("CustomFieldChar02", CustomFieldChar02);
}
/** Get CustomFieldChar02.
@return CustomFieldChar02 */
public String getCustomFieldChar02() 
{
return (String)get_Value("CustomFieldChar02");
}
/** Set CustomFieldChar03.
@param CustomFieldChar03 CustomFieldChar03 */
public void setCustomFieldChar03 (String CustomFieldChar03)
{
if (CustomFieldChar03 != null && CustomFieldChar03.length() > 1000)
{
log.warning("Length > 1000 - truncated");
CustomFieldChar03 = CustomFieldChar03.substring(0,999);
}
set_Value ("CustomFieldChar03", CustomFieldChar03);
}
/** Get CustomFieldChar03.
@return CustomFieldChar03 */
public String getCustomFieldChar03() 
{
return (String)get_Value("CustomFieldChar03");
}
/** Set CustomFieldChar04.
@param CustomFieldChar04 CustomFieldChar04 */
public void setCustomFieldChar04 (String CustomFieldChar04)
{
if (CustomFieldChar04 != null && CustomFieldChar04.length() > 1000)
{
log.warning("Length > 1000 - truncated");
CustomFieldChar04 = CustomFieldChar04.substring(0,999);
}
set_Value ("CustomFieldChar04", CustomFieldChar04);
}
/** Get CustomFieldChar04.
@return CustomFieldChar04 */
public String getCustomFieldChar04() 
{
return (String)get_Value("CustomFieldChar04");
}
/** Set CustomFieldChar05.
@param CustomFieldChar05 CustomFieldChar05 */
public void setCustomFieldChar05 (String CustomFieldChar05)
{
if (CustomFieldChar05 != null && CustomFieldChar05.length() > 1000)
{
log.warning("Length > 1000 - truncated");
CustomFieldChar05 = CustomFieldChar05.substring(0,999);
}
set_Value ("CustomFieldChar05", CustomFieldChar05);
}
/** Get CustomFieldChar05.
@return CustomFieldChar05 */
public String getCustomFieldChar05() 
{
return (String)get_Value("CustomFieldChar05");
}
/** Set Manulif Executive Employee .
@param CustomFieldYesNo01 Manulif Executive Employee  */
public void setCustomFieldYesNo01 (boolean CustomFieldYesNo01)
{
set_Value ("CustomFieldYesNo01", new Boolean(CustomFieldYesNo01));
}
/** Get Manulif Executive Employee .
@return Manulif Executive Employee  */
public boolean isCustomFieldYesNo01() 
{
Object oo = get_Value("CustomFieldYesNo01");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Manulif  Terminate Employee Transferted.
@param CustomFieldYesNo02 Manulif  Terminate Employee Transferted */
public void setCustomFieldYesNo02 (boolean CustomFieldYesNo02)
{
set_Value ("CustomFieldYesNo02", new Boolean(CustomFieldYesNo02));
}
/** Get Manulif  Terminate Employee Transferted.
@return Manulif  Terminate Employee Transferted */
public boolean isCustomFieldYesNo02() 
{
Object oo = get_Value("CustomFieldYesNo02");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set CustomFieldYesNo03.
@param CustomFieldYesNo03 CustomFieldYesNo03 */
public void setCustomFieldYesNo03 (boolean CustomFieldYesNo03)
{
set_Value ("CustomFieldYesNo03", new Boolean(CustomFieldYesNo03));
}
/** Get CustomFieldYesNo03.
@return CustomFieldYesNo03 */
public boolean isCustomFieldYesNo03() 
{
Object oo = get_Value("CustomFieldYesNo03");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set CustomFieldYesNo04.
@param CustomFieldYesNo04 CustomFieldYesNo04 */
public void setCustomFieldYesNo04 (boolean CustomFieldYesNo04)
{
set_Value ("CustomFieldYesNo04", new Boolean(CustomFieldYesNo04));
}
/** Get CustomFieldYesNo04.
@return CustomFieldYesNo04 */
public boolean isCustomFieldYesNo04() 
{
Object oo = get_Value("CustomFieldYesNo04");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set CustomFieldYesNo05.
@param CustomFieldYesNo05 CustomFieldYesNo05 */
public void setCustomFieldYesNo05 (boolean CustomFieldYesNo05)
{
set_Value ("CustomFieldYesNo05", new Boolean(CustomFieldYesNo05));
}
/** Get CustomFieldYesNo05.
@return CustomFieldYesNo05 */
public boolean isCustomFieldYesNo05() 
{
Object oo = get_Value("CustomFieldYesNo05");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Date hired.
@param DateHired Date hired */
public void setDateHired (Timestamp DateHired)
{
if (DateHired == null) throw new IllegalArgumentException ("DateHired is mandatory.");
set_Value ("DateHired", DateHired);
}
/** Get Date hired.
@return Date hired */
public Timestamp getDateHired() 
{
return (Timestamp)get_Value("DateHired");
}
/** Set Date prime d'ancienneté.
@param DateHired2 Date prime d'ancienneté */
public void setDateHired2 (Timestamp DateHired2)
{
set_Value ("DateHired2", DateHired2);
}
/** Get Date prime d'ancienneté.
@return Date prime d'ancienneté */
public Timestamp getDateHired2() 
{
return (Timestamp)get_Value("DateHired2");
}
/** Set Comment (date hired).
@param DateHiredComment Comment (date hired) */
public void setDateHiredComment (String DateHiredComment)
{
if (DateHiredComment != null && DateHiredComment.length() > 255)
{
log.warning("Length > 255 - truncated");
DateHiredComment = DateHiredComment.substring(0,254);
}
set_Value ("DateHiredComment", DateHiredComment);
}
/** Get Comment (date hired).
@return Comment (date hired) */
public String getDateHiredComment() 
{
return (String)get_Value("DateHiredComment");
}
/** Set Date Layoff.
@param DateLayoff Date Layoff */
public void setDateLayoff (Timestamp DateLayoff)
{
set_Value ("DateLayoff", DateLayoff);
}
/** Get Date Layoff.
@return Date Layoff */
public Timestamp getDateLayoff() 
{
return (Timestamp)get_Value("DateLayoff");
}
/** Set Date Probationary.
@param DateProbationary Date de fin de la période de probation */
public void setDateProbationary (Timestamp DateProbationary)
{
set_Value ("DateProbationary", DateProbationary);
}
/** Get Date Probationary.
@return Date de fin de la période de probation */
public Timestamp getDateProbationary() 
{
return (Timestamp)get_Value("DateProbationary");
}
/** Set Date Rehired.
@param DateRehired Date Rehired */
public void setDateRehired (Timestamp DateRehired)
{
set_Value ("DateRehired", DateRehired);
}
/** Get Date Rehired.
@return Date Rehired */
public Timestamp getDateRehired() 
{
return (Timestamp)get_Value("DateRehired");
}
/** Set Comment (date rehired).
@param DateRehiredComment Comment (date rehired) */
public void setDateRehiredComment (String DateRehiredComment)
{
if (DateRehiredComment != null && DateRehiredComment.length() > 255)
{
log.warning("Length > 255 - truncated");
DateRehiredComment = DateRehiredComment.substring(0,254);
}
set_Value ("DateRehiredComment", DateRehiredComment);
}
/** Get Comment (date rehired).
@return Comment (date rehired) */
public String getDateRehiredComment() 
{
return (String)get_Value("DateRehiredComment");
}
/** Set Date Seniority.
@param DateSeniority Date Seniority */
public void setDateSeniority (Timestamp DateSeniority)
{
set_Value ("DateSeniority", DateSeniority);
}
/** Get Date Seniority.
@return Date Seniority */
public Timestamp getDateSeniority() 
{
return (Timestamp)get_Value("DateSeniority");
}

/** Distribution_Booklet_ID AD_Reference_ID=2100388 */
public static final int DISTRIBUTION_BOOKLET_ID_AD_Reference_ID=2100388;
/** Set Work team.
@param Distribution_Booklet_ID Work team */
public void setDistribution_Booklet_ID (int Distribution_Booklet_ID)
{
if (Distribution_Booklet_ID < 1) throw new IllegalArgumentException ("Distribution_Booklet_ID is mandatory.");
set_ValueNoCheck ("Distribution_Booklet_ID", new Integer(Distribution_Booklet_ID));
}
/** Get Work team.
@return Work team */
public int getDistribution_Booklet_ID() 
{
Integer ii = (Integer)get_Value("Distribution_Booklet_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Email.
@param EMail Electronic Mail Address */
public void setEMail (String EMail)
{
if (EMail != null && EMail.length() > 60)
{
log.warning("Length > 60 - truncated");
EMail = EMail.substring(0,59);
}
set_Value ("EMail", EMail);
}
/** Get Email.
@return Electronic Mail Address */
public String getEMail() 
{
return (String)get_Value("EMail");
}
/** Set Date Historic.
@param Employee_Hist Date Historic */
public void setEmployee_Hist (String Employee_Hist)
{
if (Employee_Hist != null && Employee_Hist.length() > 1)
{
log.warning("Length > 1 - truncated");
Employee_Hist = Employee_Hist.substring(0,0);
}
set_Value ("Employee_Hist", Employee_Hist);
}
/** Get Date Historic.
@return Date Historic */
public String getEmployee_Hist() 
{
return (String)get_Value("Employee_Hist");
}
/** Set End Date Recall List.
@param EndDateRecallList End Date Recall List */
public void setEndDateRecallList (Timestamp EndDateRecallList)
{
set_Value ("EndDateRecallList", EndDateRecallList);
}
/** Get End Date Recall List.
@return End Date Recall List */
public Timestamp getEndDateRecallList() 
{
return (Timestamp)get_Value("EndDateRecallList");
}
/** Set EndOfYearFormWebConfirmationDate.
@param EndOfYearFormWebConfirmationDate EndOfYearFormWebConfirmationDate */
public void setEndOfYearFormWebConfirmationDate (Timestamp EndOfYearFormWebConfirmationDate)
{
set_Value ("EndOfYearFormWebConfirmationDate", EndOfYearFormWebConfirmationDate);
}
/** Get EndOfYearFormWebConfirmationDate.
@return EndOfYearFormWebConfirmationDate */
public Timestamp getEndOfYearFormWebConfirmationDate() 
{
return (Timestamp)get_Value("EndOfYearFormWebConfirmationDate");
}
/** Set Expected end of contract date.
@param ExpectedEndDate Expected end of contract date */
public void setExpectedEndDate (Timestamp ExpectedEndDate)
{
throw new IllegalArgumentException ("ExpectedEndDate is virtual column");
}
/** Get Expected end of contract date.
@return Expected end of contract date */
public Timestamp getExpectedEndDate() 
{
return (Timestamp)get_Value("ExpectedEndDate");
}
/** Set First Letter.
@param FirstLetter First Letter */
public void setFirstLetter (String FirstLetter)
{
if (FirstLetter != null && FirstLetter.length() > 30)
{
log.warning("Length > 30 - truncated");
FirstLetter = FirstLetter.substring(0,29);
}
set_Value ("FirstLetter", FirstLetter);
}
/** Get First Letter.
@return First Letter */
public String getFirstLetter() 
{
return (String)get_Value("FirstLetter");
}
/** Set First name.
@param FirstName First name */
public void setFirstName (String FirstName)
{
if (FirstName == null) throw new IllegalArgumentException ("FirstName is mandatory.");
if (FirstName.length() > 30)
{
log.warning("Length > 30 - truncated");
FirstName = FirstName.substring(0,29);
}
set_Value ("FirstName", FirstName);
}
/** Get First name.
@return First name */
public String getFirstName() 
{
return (String)get_Value("FirstName");
}

/** Gender AD_Reference_ID=2000048 */
public static final int GENDER_AD_Reference_ID=2000048;
/** Man = M */
public static final String GENDER_Man = "M";
/** Woman = F */
public static final String GENDER_Woman = "F";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isGenderValid (String test)
{
return test.equals("M") || test.equals("F");
}
/** Set Gender.
@param Gender Gender  */
public void setGender (String Gender)
{
if (Gender == null) throw new IllegalArgumentException ("Gender is mandatory");
if (!isGenderValid(Gender))
throw new IllegalArgumentException ("Gender Invalid value - " + Gender + " - Reference_ID=2000048 - M - F");
if (Gender.length() > 1)
{
log.warning("Length > 1 - truncated");
Gender = Gender.substring(0,0);
}
set_Value ("Gender", Gender);
}
/** Get Gender.
@return Gender  */
public String getGender() 
{
return (String)get_Value("Gender");
}
/** Set Insurable Salary.
@param InsurableSalary Insurable Salary */
public void setInsurableSalary (BigDecimal InsurableSalary)
{
throw new IllegalArgumentException ("InsurableSalary is virtual column");
}
/** Get Insurable Salary.
@return Insurable Salary */
public BigDecimal getInsurableSalary() 
{
BigDecimal bd = (BigDecimal)get_Value("InsurableSalary");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Insurable Salary.
@param InsurableSalaryFix Insurable Salary */
public void setInsurableSalaryFix (BigDecimal InsurableSalaryFix)
{
set_Value ("InsurableSalaryFix", InsurableSalaryFix);
}
/** Get Insurable Salary.
@return Insurable Salary */
public BigDecimal getInsurableSalaryFix() 
{
BigDecimal bd = (BigDecimal)get_Value("InsurableSalaryFix");
if (bd == null) return Env.ZERO;
return bd;
}

/** Set Insurance No.
@param InsuranceNumber Employee Insurance No */
public void setInsuranceNumber (String InsuranceNumber)
{
if (InsuranceNumber != null && InsuranceNumber.length() > 5)
{
log.warning("Length > 5 - truncated");
InsuranceNumber = InsuranceNumber.substring(0,4);
}
set_ValueNoCheck ("InsuranceNumber", InsuranceNumber);
}
/** Get Insurance No.
@return Employee Insurance No */
public String getInsuranceNumber() 
{
return (String)get_Value("InsuranceNumber");
}


/** Set Insurance Start Date.
@param InsuranceStartDate Insurance Start Date */
public void setInsuranceStartDate (Timestamp InsuranceStartDate)
{
set_Value ("InsuranceStartDate", InsuranceStartDate);
}
/** Get Insurance Start Date.
@return Insurance Start Date */
public Timestamp getInsuranceStartDate() 
{
return (Timestamp)get_Value("InsuranceStartDate");
}
/** Set Right for comming back.
@param IsCommingBack Right for comming back */
public void setIsCommingBack (boolean IsCommingBack)
{
set_Value ("IsCommingBack", new Boolean(IsCommingBack));
}
/** Get Right for comming back.
@return Right for comming back */
public boolean isCommingBack() 
{
Object oo = get_Value("IsCommingBack");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set IsEndOfYearFormWebConfirmation.
@param IsEndOfYearFormWebConfirmation IsEndOfYearFormWebConfirmation */
public void setIsEndOfYearFormWebConfirmation (boolean IsEndOfYearFormWebConfirmation)
{
set_Value ("IsEndOfYearFormWebConfirmation", new Boolean(IsEndOfYearFormWebConfirmation));
}
/** Get IsEndOfYearFormWebConfirmation.
@return IsEndOfYearFormWebConfirmation */
public boolean isEndOfYearFormWebConfirmation() 
{
Object oo = get_Value("IsEndOfYearFormWebConfirmation");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Insurable Salary Fix.
@param IsInsurableSalaryFix Insurable Salary Fix */
public void setIsInsurableSalaryFix (boolean IsInsurableSalaryFix)
{
set_Value ("IsInsurableSalaryFix", new Boolean(IsInsurableSalaryFix));
}
/** Get Insurable Salary Fix.
@return Insurable Salary Fix */
public boolean isInsurableSalaryFix() 
{
Object oo = get_Value("IsInsurableSalaryFix");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set isMission.
@param isMission isMission */
public void setisMission (boolean isMission)
{
set_Value ("isMission", new Boolean(isMission));
}
/** Get isMission.
@return isMission */
public boolean isMission() 
{
Object oo = get_Value("isMission");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Print End Of Year Form.
@param isPrintEndOfYearForm Print End Of Year Form */
public void setisPrintEndOfYearForm (boolean isPrintEndOfYearForm)
{
set_Value ("isPrintEndOfYearForm", new Boolean(isPrintEndOfYearForm));
}
/** Get Print End Of Year Form.
@return Print End Of Year Form */
public boolean isPrintEndOfYearForm() 
{
Object oo = get_Value("isPrintEndOfYearForm");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Refund EI.
@param isRefundEIActive Refund EI */
public void setisRefundEIActive (boolean isRefundEIActive)
{
set_Value ("isRefundEIActive", new Boolean(isRefundEIActive));
}
/** Get Refund EI.
@return Refund EI */
public boolean isRefundEIActive() 
{
Object oo = get_Value("isRefundEIActive");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set on-reserve status Indian .
@param isStatusIndian on-reserve status Indian  */
public void setisStatusIndian (boolean isStatusIndian)
{
set_Value ("isStatusIndian", new Boolean(isStatusIndian));
}
/** Get on-reserve status Indian .
@return on-reserve status Indian  */
public boolean isStatusIndian() 
{
Object oo = get_Value("isStatusIndian");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set isUseExternalTimeSystem.
@param isUseExternalTimeSystem isUseExternalTimeSystem */
public void setisUseExternalTimeSystem (boolean isUseExternalTimeSystem)
{
set_Value ("isUseExternalTimeSystem", new Boolean(isUseExternalTimeSystem));
}
/** Get isUseExternalTimeSystem.
@return isUseExternalTimeSystem */
public boolean isUseExternalTimeSystem() 
{
Object oo = get_Value("isUseExternalTimeSystem");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Vacation Fixe Rate.
@param IsVacationFixeRate Vacation Fixe Rate */
public void setIsVacationFixeRate (boolean IsVacationFixeRate)
{
set_Value ("IsVacationFixeRate", new Boolean(IsVacationFixeRate));
}
/** Get Vacation Fixe Rate.
@return Vacation Fixe Rate */
public boolean isVacationFixeRate() 
{
Object oo = get_Value("IsVacationFixeRate");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set IsVacationFixeRateLock.
@param IsVacationFixeRateLock IsVacationFixeRateLock */
public void setIsVacationFixeRateLock (boolean IsVacationFixeRateLock)
{
set_Value ("IsVacationFixeRateLock", new Boolean(IsVacationFixeRateLock));
}
/** Get IsVacationFixeRateLock.
@return IsVacationFixeRateLock */
public boolean isVacationFixeRateLock() 
{
Object oo = get_Value("IsVacationFixeRateLock");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Effect In (Job Type).
@param JobTypeEffectIn Effect In (Job Type) */
public void setJobTypeEffectIn (Timestamp JobTypeEffectIn)
{
set_Value ("JobTypeEffectIn", JobTypeEffectIn);
}
/** Get Effect In (Job Type).
@return Effect In (Job Type) */
public Timestamp getJobTypeEffectIn() 
{
return (Timestamp)get_Value("JobTypeEffectIn");
}

/** LayoffCode AD_Reference_ID=2100332 */
public static final int LAYOFFCODE_AD_Reference_ID=2100332;
/** Compassionate Care = Z */
public static final String LAYOFFCODE_CompassionateCare = "Z";
/** A00 Manque de travail / Fin de saison ou de contrat = A00 */
public static final String LAYOFFCODE_A00ManqueDeTravailFinDeSaisonOuDeContrat = "A00";
/** A01 Faillite de l'employeur ou mise sous séquestre  = A01 */
public static final String LAYOFFCODE_A01FailliteDeLEmployeurOuMiseSousSéquestre = "A01";
/** B00 Grève ou lockout = B00 */
public static final String LAYOFFCODE_B00GrèveOuLockout = "B00";
/** D00 Maladie ou blessure = D00 */
public static final String LAYOFFCODE_D00MaladieOuBlessure = "D00";
/** E00 Départ volontaire = E00 */
public static final String LAYOFFCODE_E00DépartVolontaire = "E00";
/** E02 Départ volontaire / Pour accompagner un(e) conjoint(e)  = E02 */
public static final String LAYOFFCODE_E02DépartVolontairePourAccompagnerUnEConjointE = "E02";
/** Shortage of Work = A */
public static final String LAYOFFCODE_ShortageOfWork = "A";
/** Strike or Lockout = B */
public static final String LAYOFFCODE_StrikeOrLockout = "B";
/** Return to School  = C */
public static final String LAYOFFCODE_ReturnToSchool = "C";
/** Illness or Injury = D */
public static final String LAYOFFCODE_IllnessOrInjury = "D";
/** Quit   = E */
public static final String LAYOFFCODE_Quit = "E";
/** Maternity  = F */
public static final String LAYOFFCODE_Maternity = "F";
/** Retirement  = G */
public static final String LAYOFFCODE_Retirement = "G";
/** Work sharing = H */
public static final String LAYOFFCODE_WorkSharing = "H";
/** Apprentice Training = J */
public static final String LAYOFFCODE_ApprenticeTraining = "J";
/** Other = K */
public static final String LAYOFFCODE_Other = "K";
/** Dismissal = M */
public static final String LAYOFFCODE_Dismissal = "M";
/** Leave of Absence = N */
public static final String LAYOFFCODE_LeaveOfAbsence = "N";
/** Parental = P */
public static final String LAYOFFCODE_Parental = "P";
/** E03 Départ volontaire / Retour aux études = E03 */
public static final String LAYOFFCODE_E03DépartVolontaireRetourAuxÉtudes = "E03";
/** E04 Départ volontaire / Raisons médicales = E04 */
public static final String LAYOFFCODE_E04DépartVolontaireRaisonsMédicales = "E04";
/** E05 Départ volontaire / Retraite volontaire = E05 */
public static final String LAYOFFCODE_E05DépartVolontaireRetraiteVolontaire = "E05";
/** E06 Départ volontaire / Autre emploi  = E06 */
public static final String LAYOFFCODE_E06DépartVolontaireAutreEmploi = "E06";
/** E09 Départ volontaire / Déménagement de l'employeur = E09 */
public static final String LAYOFFCODE_E09DépartVolontaireDéménagementDeLEmployeur = "E09";
/** E10 Départ volontaire / Prendre soin d'une personne à charge = E10 */
public static final String LAYOFFCODE_E10DépartVolontairePrendreSoinDUnePersonneÀCharge = "E10";
/** E11 Départ volontaire / Pour devenir travailleur indépendant = E11 */
public static final String LAYOFFCODE_E11DépartVolontairePourDevenirTravailleurIndépendant = "E11";
/** F00 Maternité = F00 */
public static final String LAYOFFCODE_F00Maternité = "F00";
/** G07 Retraite Approuvée programme de compression du personnel = G07 */
public static final String LAYOFFCODE_G07RetraiteApprouvéeProgrammeDeCompressionDuPersonnel = "G07";
/** H00 Travail partagé = H00 */
public static final String LAYOFFCODE_H00TravailPartagé = "H00";
/** J00 Formation en apprentissage  = J00 */
public static final String LAYOFFCODE_J00FormationEnApprentissage = "J00";
/** K00 Autre = K00 */
public static final String LAYOFFCODE_K00Autre = "K00";
/** K12 Autre / Changement de la fréquence de paie  = K12 */
public static final String LAYOFFCODE_K12AutreChangementDeLaFréquenceDePaie = "K12";
/** K13 Autre / Changement de propriétaire  = K13 */
public static final String LAYOFFCODE_K13AutreChangementDePropriétaire = "K13";
/** K14 Autre / À la demande de l'assurance-emploi  = K14 */
public static final String LAYOFFCODE_K14AutreÀLaDemandeDeLAssurance_Emploi = "K14";
/** K15 Autre / Forces canadiennes - Ordonnances = K15 */
public static final String LAYOFFCODE_K15AutreForcesCanadiennes_Ordonnances = "K15";
/** K16 Autre / À la demande de l'employé(e)  = K16 */
public static final String LAYOFFCODE_K16AutreÀLaDemandeDeLEmployéE = "K16";
/** K17 Autre / Changement de fournisseur de service  = K17 */
public static final String LAYOFFCODE_K17AutreChangementDeFournisseurDeService = "K17";
/** M00 Congédiement  = M00 */
public static final String LAYOFFCODE_M00Congédiement = "M00";
/** M08 Congédiement avant la fin de la période de probation = M08 */
public static final String LAYOFFCODE_M08CongédiementAvantLaFinDeLaPériodeDeProbation = "M08";
/** N00 Congé = N00 */
public static final String LAYOFFCODE_N00Congé = "N00";
/** P00 Parental  = P00 */
public static final String LAYOFFCODE_P00Parental = "P00";
/** Z00 Congé de compassion = Z00 */
public static final String LAYOFFCODE_Z00CongéDeCompassion = "Z00";
/** G00 Retraite obligatoire  = G00 */
public static final String LAYOFFCODE_G00RetraiteObligatoire = "G00";
/** Administrative Dismissal = Q */
public static final String LAYOFFCODE_AdministrativeDismissal = "Q";
/** Decease = R */
public static final String LAYOFFCODE_Decease = "R";
/** Revocation (disability) = S */
public static final String LAYOFFCODE_RevocationDisability = "S";
/** Suspension (recall casual employee) = T */
public static final String LAYOFFCODE_SuspensionRecallCasualEmployee = "T";
/** Suspension (recall permanent employee) = U */
public static final String LAYOFFCODE_SuspensionRecallPermanentEmployee = "U";
/** Suspension (no recall) = V */
public static final String LAYOFFCODE_SuspensionNoRecall = "V";
/** Comeback Public Service  = W */
public static final String LAYOFFCODE_ComebackPublicService = "W";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isLayoffCodeValid (String test)
{
return test == null || test.equals("Z") || test.equals("A00") || test.equals("A01") || test.equals("B00") || test.equals("D00") || test.equals("E00") || test.equals("E02") || test.equals("A") || test.equals("B") || test.equals("C") || test.equals("D") || test.equals("E") || test.equals("F") || test.equals("G") || test.equals("H") || test.equals("J") || test.equals("K") || test.equals("M") || test.equals("N") || test.equals("P") || test.equals("E03") || test.equals("E04") || test.equals("E05") || test.equals("E06") || test.equals("E09") || test.equals("E10") || test.equals("E11") || test.equals("F00") || test.equals("G07") || test.equals("H00") || test.equals("J00") || test.equals("K00") || test.equals("K12") || test.equals("K13") || test.equals("K14") || test.equals("K15") || test.equals("K16") || test.equals("K17") || test.equals("M00") || test.equals("M08") || test.equals("N00") || test.equals("P00") || test.equals("Z00") || test.equals("G00") || test.equals("Q") || test.equals("R") || test.equals("S") || test.equals("T") || test.equals("U") || test.equals("V") || test.equals("W");
}
/** Set Layoff Description.
@param LayoffCode Layoff Description */
public void setLayoffCode (String LayoffCode)
{
if (!isLayoffCodeValid(LayoffCode))
throw new IllegalArgumentException ("LayoffCode Invalid value - " + LayoffCode + " - Reference_ID=2100332 - Z - A00 - A01 - B00 - D00 - E00 - E02 - A - B - C - D - E - F - G - H - J - K - M - N - P - E03 - E04 - E05 - E06 - E09 - E10 - E11 - F00 - G07 - H00 - J00 - K00 - K12 - K13 - K14 - K15 - K16 - K17 - M00 - M08 - N00 - P00 - Z00 - G00 - Q - R - S - T - U - V - W");
if (LayoffCode != null && LayoffCode.length() > 3)
{
log.warning("Length > 3 - truncated");
LayoffCode = LayoffCode.substring(0,2);
}
set_Value ("LayoffCode", LayoffCode);
}
/** Get Layoff Description.
@return Layoff Description */
public String getLayoffCode() 
{
return (String)get_Value("LayoffCode");
}
/** Set Comment.
@param LayoffComment Comment */
public void setLayoffComment (String LayoffComment)
{
if (LayoffComment != null && LayoffComment.length() > 255)
{
log.warning("Length > 255 - truncated");
LayoffComment = LayoffComment.substring(0,254);
}
set_Value ("LayoffComment", LayoffComment);
}
/** Get Comment.
@return Comment */
public String getLayoffComment() 
{
return (String)get_Value("LayoffComment");
}
/** Set Leave Startdate.
@param LongTermLeaveDate Leave Startdate */
public void setLongTermLeaveDate (Timestamp LongTermLeaveDate)
{
set_Value ("LongTermLeaveDate", LongTermLeaveDate);
}
/** Get Leave Startdate.
@return Leave Startdate */
public Timestamp getLongTermLeaveDate() 
{
return (Timestamp)get_Value("LongTermLeaveDate");
}
/** Set Manager’s email.
@param ManagerEmail Manager’s email */
public void setManagerEmail (String ManagerEmail)
{
throw new IllegalArgumentException ("ManagerEmail is virtual column");
}
/** Get Manager’s email.
@return Manager’s email */
public String getManagerEmail() 
{
return (String)get_Value("ManagerEmail");
}
/** Set Name .
@param Name Alphanumeric identifier of the entity */
public void setName (String Name)
{
if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
if (Name.length() > 65)
{
log.warning("Length > 65 - truncated");
Name = Name.substring(0,64);
}
set_Value ("Name", Name);
}
/** Get Name .
@return Alphanumeric identifier of the entity */
public String getName() 
{
return (String)get_Value("Name");
}
/** Set Overtime Rate.
@param OvertimeRate Overtime Rate */
public void setOvertimeRate (BigDecimal OvertimeRate)
{
throw new IllegalArgumentException ("OvertimeRate is virtual column");
}
/** Get Overtime Rate.
@return Overtime Rate */
public BigDecimal getOvertimeRate() 
{
BigDecimal bd = (BigDecimal)get_Value("OvertimeRate");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set P_Carra_PayCalendar_ID.
@param P_Carra_PayCalendar_ID P_Carra_PayCalendar_ID */
public void setP_Carra_PayCalendar_ID (int P_Carra_PayCalendar_ID)
{
if (P_Carra_PayCalendar_ID <= 0) set_Value ("P_Carra_PayCalendar_ID", null);
 else 
set_Value ("P_Carra_PayCalendar_ID", new Integer(P_Carra_PayCalendar_ID));
}
/** Get P_Carra_PayCalendar_ID.
@return P_Carra_PayCalendar_ID */
public int getP_Carra_PayCalendar_ID() 
{
Integer ii = (Integer)get_Value("P_Carra_PayCalendar_ID");
if (ii == null) return 0;
return ii.intValue();
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

/** P_Department_ID AD_Reference_ID=2100460 */
public static final int P_DEPARTMENT_ID_AD_Reference_ID=2100460;
/** Set Department.
@param P_Department_ID Department */
public void setP_Department_ID (int P_Department_ID)
{
if (P_Department_ID < 1) throw new IllegalArgumentException ("P_Department_ID is mandatory.");
set_Value ("P_Department_ID", new Integer(P_Department_ID));
}
/** Get Department.
@return Department */
public int getP_Department_ID() 
{
Integer ii = (Integer)get_Value("P_Department_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Distribution_Booklet_ID AD_Reference_ID=2100388 */
public static final int P_DISTRIBUTION_BOOKLET_ID_AD_Reference_ID=2100388;
/** Set Work team.
@param P_Distribution_Booklet_ID Work team */
public void setP_Distribution_Booklet_ID (int P_Distribution_Booklet_ID)
{
throw new IllegalArgumentException ("P_Distribution_Booklet_ID is virtual column");
}
/** Get Work team.
@return Work team */
public int getP_Distribution_Booklet_ID() 
{
Integer ii = (Integer)get_Value("P_Distribution_Booklet_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Distribution_ID AD_Reference_ID=2000053 */
public static final int P_DISTRIBUTION_ID_AD_Reference_ID=2000053;
/** Set Distribution.
@param P_Distribution_ID Code for the check Distributio */
public void setP_Distribution_ID (int P_Distribution_ID)
{
if (P_Distribution_ID < 1) throw new IllegalArgumentException ("P_Distribution_ID is mandatory.");
set_Value ("P_Distribution_ID", new Integer(P_Distribution_ID));
}
/** Get Distribution.
@return Code for the check Distributio */
public int getP_Distribution_ID() 
{
Integer ii = (Integer)get_Value("P_Distribution_ID");
if (ii == null) return 0;
return ii.intValue();
}
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
/** Set Manager.
@param P_Employee_Manager_ID Manager */
public void setP_Employee_Manager_ID (int P_Employee_Manager_ID)
{
if (P_Employee_Manager_ID < 1) throw new IllegalArgumentException ("P_Employee_Manager_ID is mandatory.");
set_Value ("P_Employee_Manager_ID", new Integer(P_Employee_Manager_ID));
}
/** Get Manager.
@return Manager */
public int getP_Employee_Manager_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Manager_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Employer_ID AD_Reference_ID=2100464 */
public static final int P_EMPLOYER_ID_AD_Reference_ID=2100464;
/** Set Employer.
@param P_Employer_ID Employer */
public void setP_Employer_ID (int P_Employer_ID)
{
if (P_Employer_ID < 1) throw new IllegalArgumentException ("P_Employer_ID is mandatory.");
set_Value ("P_Employer_ID", new Integer(P_Employer_ID));
}
/** Get Employer.
@return Employer */
public int getP_Employer_ID() 
{
Integer ii = (Integer)get_Value("P_Employer_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Holidays_Calendar_ID AD_Reference_ID=2000063 */
public static final int P_HOLIDAYS_CALENDAR_ID_AD_Reference_ID=2000063;
/** Set Calendar.
@param P_Holidays_Calendar_ID Calendar */
public void setP_Holidays_Calendar_ID (int P_Holidays_Calendar_ID)
{
if (P_Holidays_Calendar_ID < 1) throw new IllegalArgumentException ("P_Holidays_Calendar_ID is mandatory.");
set_Value ("P_Holidays_Calendar_ID", new Integer(P_Holidays_Calendar_ID));
}
/** Get Calendar.
@return Calendar */
public int getP_Holidays_Calendar_ID() 
{
Integer ii = (Integer)get_Value("P_Holidays_Calendar_ID");
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

/** P_Job_Type_ID AD_Reference_ID=2000054 */
public static final int P_JOB_TYPE_ID_AD_Reference_ID=2000054;
/** Set Job Type.
@param P_Job_Type_ID Job Type */
public void setP_Job_Type_ID (int P_Job_Type_ID)
{
if (P_Job_Type_ID < 1) throw new IllegalArgumentException ("P_Job_Type_ID is mandatory.");
set_Value ("P_Job_Type_ID", new Integer(P_Job_Type_ID));
}
/** Get Job Type.
@return Job Type */
public int getP_Job_Type_ID() 
{
Integer ii = (Integer)get_Value("P_Job_Type_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Language_ID AD_Reference_ID=2100385 */
public static final int P_LANGUAGE_ID_AD_Reference_ID=2100385;
/** Set Language.
@param P_Language_ID Language */
public void setP_Language_ID (int P_Language_ID)
{
if (P_Language_ID < 1) throw new IllegalArgumentException ("P_Language_ID is mandatory.");
set_Value ("P_Language_ID", new Integer(P_Language_ID));
}
/** Get Language.
@return Language */
public int getP_Language_ID() 
{
Integer ii = (Integer)get_Value("P_Language_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_LongTermLeave_ID AD_Reference_ID=2100372 */
public static final int P_LONGTERMLEAVE_ID_AD_Reference_ID=2100372;
/** Set Activity Status.
@param P_LongTermLeave_ID Activity Status */
public void setP_LongTermLeave_ID (int P_LongTermLeave_ID)
{
//if (P_LongTermLeave_ID < 1) throw new IllegalArgumentException ("P_LongTermLeave_ID is mandatory.");
set_Value ("P_LongTermLeave_ID", new Integer(P_LongTermLeave_ID));
}
/** Get Activity Status.
@return Activity Status */
public int getP_LongTermLeave_ID() 
{
Integer ii = (Integer)get_Value("P_LongTermLeave_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Occupation_Group_ID AD_Reference_ID=2000014 */
public static final int P_OCCUPATION_GROUP_ID_AD_Reference_ID=2000014;
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

/** P_Payment_Group_ID AD_Reference_ID=2000056 */
public static final int P_PAYMENT_GROUP_ID_AD_Reference_ID=2000056;
/** Set Payment Group.
@param P_Payment_Group_ID Payment Group */
public void setP_Payment_Group_ID (int P_Payment_Group_ID)
{
if (P_Payment_Group_ID < 1) throw new IllegalArgumentException ("P_Payment_Group_ID is mandatory.");
set_Value ("P_Payment_Group_ID", new Integer(P_Payment_Group_ID));
}
/** Get Payment Group.
@return Payment Group */
public int getP_Payment_Group_ID() 
{
Integer ii = (Integer)get_Value("P_Payment_Group_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Schedule.
@param P_Schedule_ID Répartition des heures de travail à l'intérieur d'une période donnée.  */
public void setP_Schedule_ID (int P_Schedule_ID)
{
if (P_Schedule_ID <= 0) set_Value ("P_Schedule_ID", null);
 else 
set_Value ("P_Schedule_ID", new Integer(P_Schedule_ID));
}
/** Get Schedule.
@return Répartition des heures de travail à l'intérieur d'une période donnée.  */
public int getP_Schedule_ID() 
{
Integer ii = (Integer)get_Value("P_Schedule_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Visible Minority.
@param P_VisibleMinorityInfo_ID Visible Minority */
public void setP_VisibleMinorityInfo_ID (int P_VisibleMinorityInfo_ID)
{
if (P_VisibleMinorityInfo_ID <= 0) set_Value ("P_VisibleMinorityInfo_ID", null);
 else 
set_Value ("P_VisibleMinorityInfo_ID", new Integer(P_VisibleMinorityInfo_ID));
}
/** Get Visible Minority.
@return Visible Minority */
public int getP_VisibleMinorityInfo_ID() 
{
Integer ii = (Integer)get_Value("P_VisibleMinorityInfo_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Workplace_Base_ID AD_Reference_ID=2000005 */
public static final int P_WORKPLACE_BASE_ID_AD_Reference_ID=2000005;
/** Set Real work place.
@param P_Workplace_Base_ID Real work place */
public void setP_Workplace_Base_ID (int P_Workplace_Base_ID)
{
if (P_Workplace_Base_ID <= 0) set_Value ("P_Workplace_Base_ID", null);
 else 
set_Value ("P_Workplace_Base_ID", new Integer(P_Workplace_Base_ID));
}
/** Get Real work place.
@return Real work place */
public int getP_Workplace_Base_ID() 
{
Integer ii = (Integer)get_Value("P_Workplace_Base_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Workplace_ID AD_Reference_ID=2000005 */
public static final int P_WORKPLACE_ID_AD_Reference_ID=2000005;
/** Set Workplace .
@param P_Workplace_ID Workplace  */
public void setP_Workplace_ID (int P_Workplace_ID)
{
if (P_Workplace_ID < 1) throw new IllegalArgumentException ("P_Workplace_ID is mandatory.");
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
/** Set Password.
@param Password Password of any length (case sensitive) */
public void setPassword (String Password)
{
if (Password != null && Password.length() > 80)
{
log.warning("Length > 80 - truncated");
Password = Password.substring(0,79);
}
set_Value ("Password", Password);
}
/** Get Password.
@return Password of any length (case sensitive) */
public String getPassword() 
{
return (String)get_Value("Password");
}

/** PaymentType AD_Reference_ID=2000055 */
public static final int PAYMENTTYPE_AD_Reference_ID=2000055;
/** Check = C */
public static final String PAYMENTTYPE_Check = "C";
/** Deposit = D */
public static final String PAYMENTTYPE_Deposit = "D";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isPaymentTypeValid (String test)
{
return test.equals("C") || test.equals("D");
}
/** Set Payment Type.
@param PaymentType Payment Type */
public void setPaymentType (String PaymentType)
{
if (PaymentType == null) throw new IllegalArgumentException ("PaymentType is mandatory");
if (!isPaymentTypeValid(PaymentType))
throw new IllegalArgumentException ("PaymentType Invalid value - " + PaymentType + " - Reference_ID=2000055 - C - D");
if (PaymentType.length() > 1)
{
log.warning("Length > 1 - truncated");
PaymentType = PaymentType.substring(0,0);
}
set_Value ("PaymentType", PaymentType);
}
/** Get Payment Type.
@return Payment Type */
public String getPaymentType() 
{
return (String)get_Value("PaymentType");
}
/** Set Personal Email.
@param PersonalEmail Personal Email */
public void setPersonalEmail (String PersonalEmail)
{
if (PersonalEmail != null && PersonalEmail.length() > 60)
{
log.warning("Length > 60 - truncated");
PersonalEmail = PersonalEmail.substring(0,59);
}
set_Value ("PersonalEmail", PersonalEmail);
}
/** Get Personal Email.
@return Personal Email */
public String getPersonalEmail() 
{
return (String)get_Value("PersonalEmail");
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
if (PhoneExt != null && PhoneExt.length() > 30)
{
log.warning("Length > 30 - truncated");
PhoneExt = PhoneExt.substring(0,29);
}
set_Value ("PhoneExt", PhoneExt);
}
/** Get Extention.
@return Extention */
public String getPhoneExt() 
{
return (String)get_Value("PhoneExt");
}
/** Set PhoneExt2.
@param PhoneExt2 PhoneExt2 */
public void setPhoneExt2 (String PhoneExt2)
{
if (PhoneExt2 != null && PhoneExt2.length() > 30)
{
log.warning("Length > 30 - truncated");
PhoneExt2 = PhoneExt2.substring(0,29);
}
set_Value ("PhoneExt2", PhoneExt2);
}
/** Get PhoneExt2.
@return PhoneExt2 */
public String getPhoneExt2() 
{
return (String)get_Value("PhoneExt2");
}
/** Set Print Statement Of Earnings.
@param PrintStatementEarning Print Statement Of Earnings */
public void setPrintStatementEarning (boolean PrintStatementEarning)
{
set_Value ("PrintStatementEarning", new Boolean(PrintStatementEarning));
}
/** Get Print Statement Of Earnings.
@return Print Statement Of Earnings */
public boolean isPrintStatementEarning() 
{
Object oo = get_Value("PrintStatementEarning");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Record of Employment.
@param ProcessRoe Record of Employment */
public void setProcessRoe (String ProcessRoe)
{
if (ProcessRoe != null && ProcessRoe.length() > 1)
{
log.warning("Length > 1 - truncated");
ProcessRoe = ProcessRoe.substring(0,0);
}
set_Value ("ProcessRoe", ProcessRoe);
}
/** Get Record of Employment.
@return Record of Employment */
public String getProcessRoe() 
{
return (String)get_Value("ProcessRoe");
}
/** Set Prepare ROE List.
@param ProcessRoeList Prepare and ship a list of ROEs depending on few parameters */
public void setProcessRoeList (String ProcessRoeList)
{
if (ProcessRoeList != null && ProcessRoeList.length() > 1)
{
log.warning("Length > 1 - truncated");
ProcessRoeList = ProcessRoeList.substring(0,0);
}
set_Value ("ProcessRoeList", ProcessRoeList);
}
/** Get Prepare ROE List.
@return Prepare and ship a list of ROEs depending on few parameters */
public String getProcessRoeList() 
{
return (String)get_Value("ProcessRoeList");
}
/** Set Launch Statement Earning.
@param ProcessStatementEarning Launch Statement Earning */
public void setProcessStatementEarning (String ProcessStatementEarning)
{
if (ProcessStatementEarning != null && ProcessStatementEarning.length() > 1)
{
log.warning("Length > 1 - truncated");
ProcessStatementEarning = ProcessStatementEarning.substring(0,0);
}
set_Value ("ProcessStatementEarning", ProcessStatementEarning);
}
/** Get Launch Statement Earning.
@return Launch Statement Earning */
public String getProcessStatementEarning() 
{
return (String)get_Value("ProcessStatementEarning");
}
/** Set Reactivation.
@param Reactivation Reactivation */
public void setReactivation (Timestamp Reactivation)
{
set_Value ("Reactivation", Reactivation);
}
/** Get Reactivation.
@return Reactivation */
public Timestamp getReactivation() 
{
return (Timestamp)get_Value("Reactivation");
}
/** Set Remunerate Holiday.
@param RemunerateHoliday Remunerate Holiday */
public void setRemunerateHoliday (boolean RemunerateHoliday)
{
set_Value ("RemunerateHoliday", new Boolean(RemunerateHoliday));
}
/** Get Remunerate Holiday.
@return Remunerate Holiday */
public boolean isRemunerateHoliday() 
{
Object oo = get_Value("RemunerateHoliday");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Seniority.
@param Seniority Seniority */
public void setSeniority (String Seniority)
{
if (Seniority != null && Seniority.length() > 20)
{
log.warning("Length > 20 - truncated");
Seniority = Seniority.substring(0,19);
}
set_Value ("Seniority", Seniority);
}
/** Get Seniority.
@return Seniority */
public String getSeniority() 
{
return (String)get_Value("Seniority");
}
/** Set Sin.
@param Sin Social Insurance Number  */
public void setSin (String Sin)
{
if (Sin == null) throw new IllegalArgumentException ("Sin is mandatory.");
if (Sin.length() > 11)
{
log.warning("Length > 11 - truncated");
Sin = Sin.substring(0,10);
}
set_Value ("Sin", Sin);
}
/** Get Sin.
@return Social Insurance Number  */
public String getSin() 
{
return (String)get_Value("Sin");
}
/** Set StatutoryHolidayGenerationType.
@param StatutoryHolidayGenerationType StatutoryHolidayGenerationType */
public void setStatutoryHolidayGenerationType (boolean StatutoryHolidayGenerationType)
{
set_Value ("StatutoryHolidayGenerationType", new Boolean(StatutoryHolidayGenerationType));
}
/** Get StatutoryHolidayGenerationType.
@return StatutoryHolidayGenerationType */
public boolean isStatutoryHolidayGenerationType() 
{
Object oo = get_Value("StatutoryHolidayGenerationType");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Supplier Number.
@param SupplierNumber Supplier Number */
public void setSupplierNumber (String SupplierNumber)
{
if (SupplierNumber != null && SupplierNumber.length() > 30)
{
log.warning("Length > 30 - truncated");
SupplierNumber = SupplierNumber.substring(0,29);
}
set_Value ("SupplierNumber", SupplierNumber);
}
/** Get Supplier Number.
@return Supplier Number */
public String getSupplierNumber() 
{
return (String)get_Value("SupplierNumber");
}
/** Set Surname.
@param Surname Surname */
public void setSurname (String Surname)
{
if (Surname == null) throw new IllegalArgumentException ("Surname is mandatory.");
if (Surname.length() > 30)
{
log.warning("Length > 30 - truncated");
Surname = Surname.substring(0,29);
}
set_Value ("Surname", Surname);
}
/** Get Surname.
@return Surname */
public String getSurname() 
{
return (String)get_Value("Surname");
}

/** Taxation_Region_ID AD_Reference_ID=2000026 */
public static final int TAXATION_REGION_ID_AD_Reference_ID=2000026;
/** Set Taxation Region.
@param Taxation_Region_ID Taxation Region */
public void setTaxation_Region_ID (int Taxation_Region_ID)
{
if (Taxation_Region_ID < 1) throw new IllegalArgumentException ("Taxation_Region_ID is mandatory.");
set_Value ("Taxation_Region_ID", new Integer(Taxation_Region_ID));
}
/** Get Taxation Region.
@return Taxation Region */
public int getTaxation_Region_ID() 
{
Integer ii = (Integer)get_Value("Taxation_Region_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set User code.
@param UserCode User code */
public void setUserCode (String UserCode)
{
if (UserCode != null && UserCode.length() > 30)
{
log.warning("Length > 30 - truncated");
UserCode = UserCode.substring(0,29);
}
set_Value ("UserCode", UserCode);
}
/** Get User code.
@return User code */
public String getUserCode() 
{
return (String)get_Value("UserCode");
}
/** Set Vacation Rate Fixe.
@param VacationFixeRate Vacation Rate Fixe */
public void setVacationFixeRate (BigDecimal VacationFixeRate)
{
set_Value ("VacationFixeRate", VacationFixeRate);
}
/** Get Vacation Rate Fixe.
@return Vacation Rate Fixe */
public BigDecimal getVacationFixeRate() 
{
BigDecimal bd = (BigDecimal)get_Value("VacationFixeRate");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Vacation hourly rate.
@param vacationHourlyRate Vacation hourly rate */
public void setvacationHourlyRate (BigDecimal vacationHourlyRate)
{
set_Value ("vacationHourlyRate", vacationHourlyRate);
}
/** Get Vacation hourly rate.
@return Vacation hourly rate */
public BigDecimal getvacationHourlyRate() 
{
BigDecimal bd = (BigDecimal)get_Value("vacationHourlyRate");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Vacation Rate.
@param VacationRate Vacation Rate */
public void setVacationRate (BigDecimal VacationRate)
{
throw new IllegalArgumentException ("VacationRate is virtual column");
}
/** Get Vacation Rate.
@return Vacation Rate */
public BigDecimal getVacationRate() 
{
BigDecimal bd = (BigDecimal)get_Value("VacationRate");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Search Key.
@param Value Search key for the record in the format required - must be unique */
public void setValue (String Value)
{
if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
if (Value.length() > 120)
{
log.warning("Length > 120 - truncated");
Value = Value.substring(0,119);
}
set_ValueNoCheck ("Value", Value);
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


/** StatutoryHolidayGenerationType AD_Reference_ID=2100492 */
public static final int STATUTORYHOLIDAYGENERATIONTYPE_AD_Reference_ID=2100492;
/** No statutory holiday generation = 0 */
public static final String STATUTORYHOLIDAYGENERATIONTYPE_NoStatutoryHolidayGeneration = "0";
/** Fixed nbr of hours = 1 */
public static final String STATUTORYHOLIDAYGENERATIONTYPE_FixedNbrOfHours = "1";
/** Nbr of hours as defined in the generic schedule = 2 */
public static final String STATUTORYHOLIDAYGENERATIONTYPE_NbrOfHoursAsDefinedInTheGenericSchedule = "2";
/** 1/20th of hours = 3 */
public static final String STATUTORYHOLIDAYGENERATIONTYPE_120thOfHours = "3";
/** 1/20th of hours and amount = 4 */
public static final String STATUTORYHOLIDAYGENERATIONTYPE_120thOfHoursAndAmount = "4";
/** 1/60th of hours and amount = A */
public static final String STATUTORYHOLIDAYGENERATIONTYPE_160thOfHoursAndAmount = "A";

/** 1/16th of hours = 5 */
public static final String STATUTORYHOLIDAYGENERATIONTYPE_116thOfHours = "5";
/** 1/16th of hours and amount  = 6 */
public static final String STATUTORYHOLIDAYGENERATIONTYPE_116thOfHoursAndAmount = "6";
/** 1/15th of hours = 7 */
public static final String STATUTORYHOLIDAYGENERATIONTYPE_115thOfHours = "7";
/** 1/10th of hours = 8 */
public static final String STATUTORYHOLIDAYGENERATIONTYPE_110thOfHoursAndAmount = "8";
/** 1/37th of hours = 9 */
public static final String STATUTORYHOLIDAYGENERATIONTYPE_MoyenSur37Hours = "9";

/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isStatutoryHolidayGenerationTypeValid (String test)
{
return test.equals("0") || test.equals("1") || test.equals("2") || test.equals("3") || test.equals("4") || test.equals("5") || test.equals("6") || test.equals("7") || test.equals("8") || test.equals("9") || test.equals("A") ;
}
/** Set Statutory Holiday Generation .
@param StatutoryHolidayGenerationType Statutory Holiday Generation  */
public void setStatutoryHolidayGenerationType (String StatutoryHolidayGenerationType)
{
if (StatutoryHolidayGenerationType == null) throw new IllegalArgumentException ("StatutoryHolidayGenerationType is mandatory");
if (!isStatutoryHolidayGenerationTypeValid(StatutoryHolidayGenerationType))
throw new IllegalArgumentException ("StatutoryHolidayGenerationType Invalid value - " + StatutoryHolidayGenerationType + " - Reference_ID=2100492 - 0 - 1 - 2 - 3 - 4 - 5 - 6 - 7 - 8 - 9 - A");
if (StatutoryHolidayGenerationType.length() > 1)
{
log.warning("Length > 1 - truncated");
StatutoryHolidayGenerationType = StatutoryHolidayGenerationType.substring(0,0);
}
set_Value ("StatutoryHolidayGenerationType", StatutoryHolidayGenerationType);
}
/** Get Statutory Holiday Generation .
@return Statutory Holiday Generation  */
public String getStatutoryHolidayGenerationType() 
{
return (String)get_Value("StatutoryHolidayGenerationType");
}

/** Set Pro Rata Holiday.
@param ProRataHoliday Pro Rata Holiday */
public void setProRataHoliday (BigDecimal ProRataHoliday)
{
set_Value ("ProRataHoliday", ProRataHoliday);
}
/** Get Pro Rata Holiday.
@return Pro Rata Holiday */
public BigDecimal getProRataHoliday() 
{
BigDecimal bd = (BigDecimal)get_Value("ProRataHoliday");
if (bd == null) return Env.ZERO;
return bd;
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

/** Set Sick Leave Hourly Rate.
@param SickLeaveHourlyRate Sick Leave Hourly Rate */
public void setSickLeaveHourlyRate (BigDecimal SickLeaveHourlyRate)
{
set_Value ("SickLeaveHourlyRate", SickLeaveHourlyRate);
}
/** Get Sick Leave Hourly Rate.
@return Sick Leave Hourly Rate */
public BigDecimal getSickLeaveHourlyRate() 
{
BigDecimal bd = (BigDecimal)get_Value("SickLeaveHourlyRate");
if (bd == null) return Env.ZERO;
return bd;
}

}
