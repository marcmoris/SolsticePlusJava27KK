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
/** Generated Model for P_Employee_Hst
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Employee_Hst extends PO
{
/** Standard Constructor
@param ctx context
@param P_Employee_Hst_ID id
@param trxName transaction
*/
public X_P_Employee_Hst (Properties ctx, int P_Employee_Hst_ID, String trxName)
{
super (ctx, P_Employee_Hst_ID, trxName);
/** if (P_Employee_Hst_ID == 0)
{
setBirthDate (new Timestamp(System.currentTimeMillis()));
setFirstName (null);
setGender (null);	// M
setName (null);
setP_Employee_ID (0);
setP_Employer_ID (0);
setP_Holidays_Calendar_ID (0);
setP_Job_Type_ID (0);
setP_Occupation_Group_ID (0);
setP_Payment_Group_ID (0);
setPaymentType (null);	// D
setSurname (null);
setTaxation_Region_ID (0);	// 166

setValue (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Employee_Hst (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100682 */
public static final int Table_ID=2100682;

/** TableName=P_Employee_Hst */
public static final String Table_Name="P_Employee_Hst";

protected static KeyNamePair Model = new KeyNamePair(2100682,"P_Employee_Hst");

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
StringBuffer sb = new StringBuffer ("X_P_Employee_Hst[").append(get_ID()).append("]");
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
/** Set Crew cost.
@param C_Activity_ID Crew cost */
public void setC_Activity_ID (int C_Activity_ID)
{
if (C_Activity_ID <= 0) set_Value ("C_Activity_ID", null);
 else 
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
if (CivilStatus != null && CivilStatus.length() > 2)
{
log.warning("Length > 2 - truncated");
CivilStatus = CivilStatus.substring(0,1);
}
set_Value ("CivilStatus", CivilStatus);
}
/** Get Civil Status.
@return Civil Status */
public String getCivilStatus() 
{
return (String)get_Value("CivilStatus");
}
/** Set Date hired.
@param DateHired Date hired */
public void setDateHired (Timestamp DateHired)
{
set_Value ("DateHired", DateHired);
}
/** Get Date hired.
@return Date hired */
public Timestamp getDateHired() 
{
return (Timestamp)get_Value("DateHired");
}
/** Set Date Hired 2.
@param DateHired2 Date Hired 2 */
public void setDateHired2 (Timestamp DateHired2)
{
set_Value ("DateHired2", DateHired2);
}
/** Get Date Hired 2.
@return Date Hired 2 */
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

/** Set Email.
@param Email Electronic Mail Address */
public void setEmail (String Email)
{
if (Email != null && Email.length() > 120)
{
log.warning("Length > 120 - truncated");
Email = Email.substring(0,119);
}
set_Value ("Email", Email);
}
/** Get Email.
@return Electronic Mail Address */
public String getEmail() 
{
return (String)get_Value("Email");
}
/** Set First Letter.
@param FirstLetter First Letter */
public void setFirstLetter (String FirstLetter)
{
if (FirstLetter != null && FirstLetter.length() > 60)
{
log.warning("Length > 60 - truncated");
FirstLetter = FirstLetter.substring(0,59);
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
if (Gender.length() > 2)
{
log.warning("Length > 2 - truncated");
Gender = Gender.substring(0,1);
}
set_Value ("Gender", Gender);
}
/** Get Gender.
@return Gender  */
public String getGender() 
{
return (String)get_Value("Gender");
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
return test == null || test.equals("A") || test.equals("B") || test.equals("C") || test.equals("D") || test.equals("E") || test.equals("F") || test.equals("G") || test.equals("H") || test.equals("J") || test.equals("K") || test.equals("M") || test.equals("N") || test.equals("P") || test.equals("Q") || test.equals("R") || test.equals("S") || test.equals("T") || test.equals("U") || test.equals("V") || test.equals("W") || test.equals("Z");
}
/** Set Layoff Description.
@param LayoffCode Layoff Description */
public void setLayoffCode (String LayoffCode)
{
if (LayoffCode != null && LayoffCode.length() > 3)
{
log.warning("Length > 3- truncated");
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

/** MotorcycleClass AD_Reference_ID=2100375 */
public static final int MOTORCYCLECLASS_AD_Reference_ID=2100375;
/** 6A - All Motorcycles = 6A */
public static final String MOTORCYCLECLASS_6A_AllMotorcycles = "6A";
/** 6B - Cylinder Size of 400 cc or less = 6B */
public static final String MOTORCYCLECLASS_6B_CylinderSizeOf400CcOrLess = "6B";
/** 6C - Cylinder Size of 125 cc or less = 6C */
public static final String MOTORCYCLECLASS_6C_CylinderSizeOf125CcOrLess = "6C";
/** 6D - Moped or Motorized Scooter = 6D */
public static final String MOTORCYCLECLASS_6D_MopedOrMotorizedScooter = "6D";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isMotorcycleClassValid (String test)
{
return test == null || test.equals("6A") || test.equals("6B") || test.equals("6C") || test.equals("6D");
}
/** Set MotorcycleClass.
@param MotorcycleClass List of the Motorcycle Classes of Driving Licence */
public void setMotorcycleClass (String MotorcycleClass)
{
if (!isMotorcycleClassValid(MotorcycleClass))
throw new IllegalArgumentException ("MotorcycleClass Invalid value - " + MotorcycleClass + " - Reference_ID=2100375 - 6A - 6B - 6C - 6D");
if (MotorcycleClass != null && MotorcycleClass.length() > 2)
{
log.warning("Length > 2 - truncated");
MotorcycleClass = MotorcycleClass.substring(0,1);
}
set_Value ("MotorcycleClass", MotorcycleClass);
}
/** Get MotorcycleClass.
@return List of the Motorcycle Classes of Driving Licence */
public String getMotorcycleClass() 
{
return (String)get_Value("MotorcycleClass");
}
/** Set Name .
@param Name Alphanumeric identifier of the entity */
public void setName (String Name)
{
if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
if (Name.length() > 120)
{
log.warning("Length > 120 - truncated");
Name = Name.substring(0,119);
}
set_Value ("Name", Name);
}
/** Get Name .
@return Alphanumeric identifier of the entity */
public String getName() 
{
return (String)get_Value("Name");
}
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
/** Set Department.
@param P_Department_ID Department */
public void setP_Department_ID (int P_Department_ID)
{
if (P_Department_ID <= 0) set_Value ("P_Department_ID", null);
 else 
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
/** Set Booklet.
@param P_Distribution_Booklet_ID Booklet */
public void setP_Distribution_Booklet_ID (int P_Distribution_Booklet_ID)
{
if (P_Distribution_Booklet_ID <= 0) set_Value ("P_Distribution_Booklet_ID", null);
 else 
set_Value ("P_Distribution_Booklet_ID", new Integer(P_Distribution_Booklet_ID));
}
/** Get Booklet.
@return Booklet */
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
if (P_Distribution_ID <= 0) set_Value ("P_Distribution_ID", null);
 else 
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
/** Set P_employee_hst_id.
@param P_Employee_Hst_ID P_employee_hst_id */
public void setP_Employee_Hst_ID (int P_Employee_Hst_ID)
{
if (P_Employee_Hst_ID <= 0) set_Value ("P_Employee_Hst_ID", null);
 else 
set_Value ("P_Employee_Hst_ID", new Integer(P_Employee_Hst_ID));
}
/** Get P_employee_hst_id.
@return P_employee_hst_id */
public int getP_Employee_Hst_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Hst_ID");
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

/** P_Employer_ID AD_Reference_ID=2000042 */
public static final int P_EMPLOYER_ID_AD_Reference_ID=2000042;
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
if (P_Language_ID <= 0) set_Value ("P_Language_ID", null);
 else 
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

/** P_Workplace_ID AD_Reference_ID=2000005 */
public static final int P_WORKPLACE_ID_AD_Reference_ID=2000005;
/** Set Workplace (Base).
@param P_Workplace_ID Workplace (Base) */
public void setP_Workplace_ID (int P_Workplace_ID)
{
if (P_Workplace_ID <= 0) set_Value ("P_Workplace_ID", null);
 else 
set_Value ("P_Workplace_ID", new Integer(P_Workplace_ID));
}
/** Get Workplace (Base).
@return Workplace (Base) */
public int getP_Workplace_ID() 
{
Integer ii = (Integer)get_Value("P_Workplace_ID");
if (ii == null) return 0;
return ii.intValue();
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
if (PaymentType.length() > 2)
{
log.warning("Length > 2 - truncated");
PaymentType = PaymentType.substring(0,1);
}
set_Value ("PaymentType", PaymentType);
}
/** Get Payment Type.
@return Payment Type */
public String getPaymentType() 
{
return (String)get_Value("PaymentType");
}
/** Set Phone.
@param Phone Identifies a telephone number */
public void setPhone (String Phone)
{
if (Phone != null && Phone.length() > 120)
{
log.warning("Length > 120 - truncated");
Phone = Phone.substring(0,119);
}
set_Value ("Phone", Phone);
}
/** Get Phone.
@return Identifies a telephone number */
public String getPhone() 
{
return (String)get_Value("Phone");
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
/** Set Sin.
@param Sin Social Insurance Number  */
public void setSin (String Sin)
{
if (Sin != null && Sin.length() > 60)
{
log.warning("Length > 60 - truncated");
Sin = Sin.substring(0,59);
}
set_Value ("Sin", Sin);
}
/** Get Sin.
@return Social Insurance Number  */
public String getSin() 
{
return (String)get_Value("Sin");
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
/** Set Search Key.
@param Value Search key for the record in the format required - must be unique */
public void setValue (String Value)
{
if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
if (Value.length() > 60)
{
log.warning("Length > 60 - truncated");
Value = Value.substring(0,59);
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
