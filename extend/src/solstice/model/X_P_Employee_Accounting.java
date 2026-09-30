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
/** Generated Model for P_Employee_Accounting
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Employee_Accounting extends PO
{
/** Standard Constructor
@param ctx context
@param P_Employee_Accounting_ID id
@param trxName transaction
*/
public X_P_Employee_Accounting (Properties ctx, int P_Employee_Accounting_ID, String trxName)
{
super (ctx, P_Employee_Accounting_ID, trxName);
/** if (P_Employee_Accounting_ID == 0)
{
setFirstName (null);
setName (null);
setP_Employee_Accounting_ID (0);
setP_Employee_ID (0);
setPrintStatementEarning (false);
setRemunerateHoliday (false);
setSurname (null);
setValue (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Employee_Accounting (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100834 */
public static final int Table_ID=2100834;

/** TableName=P_Employee_Accounting */
public static final String Table_Name="P_Employee_Accounting";

protected static KeyNamePair Model = new KeyNamePair(2100834,"P_Employee_Accounting");

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
StringBuffer sb = new StringBuffer ("X_P_Employee_Accounting[").append(get_ID()).append("]");
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
@param BirthDate BirthDate  */
public void setBirthDate (Timestamp BirthDate)
{
set_Value ("BirthDate", BirthDate);
}
/** Get BirthDate.
@return BirthDate  */
public Timestamp getBirthDate() 
{
return (Timestamp)get_Value("BirthDate");
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
/** Set Civil Status.
@param CivilStatus Civil Status */
public void setCivilStatus (boolean CivilStatus)
{
set_Value ("CivilStatus", new Boolean(CivilStatus));
}
/** Get Civil Status.
@return Civil Status */
public boolean isCivilStatus() 
{
Object oo = get_Value("CivilStatus");
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
/** Set EMail.
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
/** Get EMail.
@return Electronic Mail Address */
public String getEMail() 
{
return (String)get_Value("EMail");
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
/** Set Gender.
@param Gender Gender  */
public void setGender (boolean Gender)
{
set_Value ("Gender", new Boolean(Gender));
}
/** Get Gender.
@return Gender  */
public boolean isGender() 
{
Object oo = get_Value("Gender");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
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
/** Set Layoff Description.
@param LayoffCode Layoff Description */
public void setLayoffCode (boolean LayoffCode)
{
set_Value ("LayoffCode", new Boolean(LayoffCode));
}
/** Get Layoff Description.
@return Layoff Description */
public boolean isLayoffCode() 
{
Object oo = get_Value("LayoffCode");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
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
/** Set MotorcycleClass.
@param MotorcycleClass List of the Motorcycle Classes of Driving Licence */
public void setMotorcycleClass (String MotorcycleClass)
{
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
/** Get Record ID/ColumnName
@return ID/ColumnName pair */
public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), getName());
}
/** Set Number of Driver's Licence.
@param NbDriverLicence Number of Driver's Licence */
public void setNbDriverLicence (int NbDriverLicence)
{
set_Value ("NbDriverLicence", new Integer(NbDriverLicence));
}
/** Get Number of Driver's Licence.
@return Number of Driver's Licence */
public int getNbDriverLicence() 
{
Integer ii = (Integer)get_Value("NbDriverLicence");
if (ii == null) return 0;
return ii.intValue();
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
/** Set Employee accounting.
@param P_Employee_Accounting_ID Employee accounting */
public void setP_Employee_Accounting_ID (int P_Employee_Accounting_ID)
{
if (P_Employee_Accounting_ID < 1) throw new IllegalArgumentException ("P_Employee_Accounting_ID is mandatory.");
set_ValueNoCheck ("P_Employee_Accounting_ID", new Integer(P_Employee_Accounting_ID));
}
/** Get Employee accounting.
@return Employee accounting */
public int getP_Employee_Accounting_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Accounting_ID");
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
/** Set Employer.
@param P_Employer_ID Employer */
public void setP_Employer_ID (int P_Employer_ID)
{
if (P_Employer_ID <= 0) set_Value ("P_Employer_ID", null);
 else 
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
/** Set Calendar.
@param P_Holidays_Calendar_ID Calendar */
public void setP_Holidays_Calendar_ID (int P_Holidays_Calendar_ID)
{
if (P_Holidays_Calendar_ID <= 0) set_Value ("P_Holidays_Calendar_ID", null);
 else 
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
/** Set Job Type.
@param P_Job_Type_ID Job Type */
public void setP_Job_Type_ID (int P_Job_Type_ID)
{
if (P_Job_Type_ID <= 0) set_Value ("P_Job_Type_ID", null);
 else 
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
/** Set Payment Group.
@param P_Payment_Group_ID Payment Group */
public void setP_Payment_Group_ID (int P_Payment_Group_ID)
{
if (P_Payment_Group_ID <= 0) set_Value ("P_Payment_Group_ID", null);
 else 
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
/** Set Payment Type.
@param PaymentType Payment Type */
public void setPaymentType (boolean PaymentType)
{
set_Value ("PaymentType", new Boolean(PaymentType));
}
/** Get Payment Type.
@return Payment Type */
public boolean isPaymentType() 
{
Object oo = get_Value("PaymentType");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Phone.
@param Phone Identifies a telephone number */
public void setPhone (String Phone)
{
if (Phone != null && Phone.length() > 60)
{
log.warning("Length > 60 - truncated");
Phone = Phone.substring(0,59);
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
if (PhoneExt != null && PhoneExt.length() > 50)
{
log.warning("Length > 50 - truncated");
PhoneExt = PhoneExt.substring(0,49);
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
if (PhoneExt2 != null && PhoneExt2.length() > 50)
{
log.warning("Length > 50 - truncated");
PhoneExt2 = PhoneExt2.substring(0,49);
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
public void setProcessRoe (boolean ProcessRoe)
{
set_Value ("ProcessRoe", new Boolean(ProcessRoe));
}
/** Get Record of Employment.
@return Record of Employment */
public boolean isProcessRoe() 
{
Object oo = get_Value("ProcessRoe");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Prepare ROE List.
@param ProcessRoeList Prepare and ship a list of ROEs depending on few parameters */
public void setProcessRoeList (boolean ProcessRoeList)
{
set_Value ("ProcessRoeList", new Boolean(ProcessRoeList));
}
/** Get Prepare ROE List.
@return Prepare and ship a list of ROEs depending on few parameters */
public boolean isProcessRoeList() 
{
Object oo = get_Value("ProcessRoeList");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Launch Statement Earning.
@param ProcessStatementEarning Launch Statement Earning */
public void setProcessStatementEarning (boolean ProcessStatementEarning)
{
set_Value ("ProcessStatementEarning", new Boolean(ProcessStatementEarning));
}
/** Get Launch Statement Earning.
@return Launch Statement Earning */
public boolean isProcessStatementEarning() 
{
Object oo = get_Value("ProcessStatementEarning");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
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
if (Sin != null && Sin.length() > 30)
{
log.warning("Length > 30 - truncated");
Sin = Sin.substring(0,29);
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
/** Set Taxation Region.
@param Taxation_Region_ID Taxation Region */
public void setTaxation_Region_ID (int Taxation_Region_ID)
{
if (Taxation_Region_ID <= 0) set_Value ("Taxation_Region_ID", null);
 else 
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
if (UserCode != null && UserCode.length() > 50)
{
log.warning("Length > 50 - truncated");
UserCode = UserCode.substring(0,49);
}
set_Value ("UserCode", UserCode);
}
/** Get User code.
@return User code */
public String getUserCode() 
{
return (String)get_Value("UserCode");
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
}
