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
/** Generated Model for I_Employee
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_I_Employee extends PO
{
/** Standard Constructor
@param ctx context
@param I_Employee_ID id
@param trxName transaction
*/
public X_I_Employee (Properties ctx, int I_Employee_ID, String trxName)
{
super (ctx, I_Employee_ID, trxName);
/** if (I_Employee_ID == 0)
{
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_I_Employee (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100847 */
public static final int Table_ID=2100847;

/** TableName=I_Employee */
public static final String Table_Name="I_Employee";

protected static KeyNamePair Model = new KeyNamePair(2100847,"I_Employee");

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
StringBuffer sb = new StringBuffer ("X_I_Employee[").append(get_ID()).append("]");
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
/** Set Annual Salary.
@param Annual_Salary   */
public void setAnnual_Salary (BigDecimal Annual_Salary)
{
set_Value ("Annual_Salary", Annual_Salary);
}
/** Get Annual Salary.
@return   */
public BigDecimal getAnnual_Salary() 
{
BigDecimal bd = (BigDecimal)get_Value("Annual_Salary");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set BirthDate.
@param BirthDate BirthDate   */
public void setBirthDate (Timestamp BirthDate)
{
set_Value ("BirthDate", BirthDate);
}
/** Get BirthDate.
@return BirthDate   */
public Timestamp getBirthDate() 
{
return (Timestamp)get_Value("BirthDate");
}
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
/** Set Country.
@param C_Country_ID Country  */
public void setC_Country_ID (int C_Country_ID)
{
if (C_Country_ID <= 0) set_Value ("C_Country_ID", null);
 else 
set_Value ("C_Country_ID", new Integer(C_Country_ID));
}
/** Get Country.
@return Country  */
public int getC_Country_ID() 
{
Integer ii = (Integer)get_Value("C_Country_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Province / Region.
@param C_Region_ID Identifies a geographical Region */
public void setC_Region_ID (int C_Region_ID)
{
if (C_Region_ID <= 0) set_Value ("C_Region_ID", null);
 else 
set_Value ("C_Region_ID", new Integer(C_Region_ID));
}
/** Get Province / Region.
@return Identifies a geographical Region */
public int getC_Region_ID() 
{
Integer ii = (Integer)get_Value("C_Region_ID");
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
/** Set Delete old/existing records.
@param DeleteOld Otherwise records will be added */
public void setDeleteOld (boolean DeleteOld)
{
set_Value ("DeleteOld", new Boolean(DeleteOld));
}
/** Get Delete old/existing records.
@return Otherwise records will be added */
public boolean isDeleteOld() 
{
Object oo = get_Value("DeleteOld");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set First name.
@param FirstName First name */
public void setFirstName (String FirstName)
{
if (FirstName != null && FirstName.length() > 30)
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
return test == null || test.equals("M") || test.equals("F");
}
/** Set Gender.
@param Gender Gender  */
public void setGender (String Gender)
{
if (!isGenderValid(Gender))
throw new IllegalArgumentException ("Gender Invalid value - " + Gender + " - Reference_ID=2000048 - M - F");
if (Gender != null && Gender.length() > 1)
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
/** Set Hourly Rate.
@param Hourly_Rate   */
public void setHourly_Rate (BigDecimal Hourly_Rate)
{
set_Value ("Hourly_Rate", Hourly_Rate);
}
/** Get Hourly Rate.
@return   */
public BigDecimal getHourly_Rate() 
{
BigDecimal bd = (BigDecimal)get_Value("Hourly_Rate");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set I_EMPLOYEE_ID.
@param I_EMPLOYEE_ID I_EMPLOYEE_ID */
public void setI_EMPLOYEE_ID (int I_EMPLOYEE_ID)
{
if (I_EMPLOYEE_ID <= 0) set_ValueNoCheck ("I_EMPLOYEE_ID", null);
 else 
set_ValueNoCheck ("I_EMPLOYEE_ID", new Integer(I_EMPLOYEE_ID));
}
/** Get I_EMPLOYEE_ID.
@return I_EMPLOYEE_ID */
public int getI_EMPLOYEE_ID() 
{
Integer ii = (Integer)get_Value("I_EMPLOYEE_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Import Error Message.
@param I_ErrorMsg Messages generated from import process */
public void setI_ErrorMsg (String I_ErrorMsg)
{
if (I_ErrorMsg != null && I_ErrorMsg.length() > 2000)
{
log.warning("Length > 2000 - truncated");
I_ErrorMsg = I_ErrorMsg.substring(0,1999);
}
set_Value ("I_ErrorMsg", I_ErrorMsg);
}
/** Get Import Error Message.
@return Messages generated from import process */
public String getI_ErrorMsg() 
{
return (String)get_Value("I_ErrorMsg");
}
/** Set Imported.
@param I_IsImported Has this import been processed */
public void setI_IsImported (boolean I_IsImported)
{
set_Value ("I_IsImported", new Boolean(I_IsImported));
}
/** Get Imported.
@return Has this import been processed */
public boolean isI_IsImported() 
{
Object oo = get_Value("I_IsImported");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Layoff Description.
@param LayoffCode Layoff Description */
public void setLayoffCode (String LayoffCode)
{
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
/** Set Team.
@param P_Distribution_Booklet_ID Team */
public void setP_Distribution_Booklet_ID (int P_Distribution_Booklet_ID)
{
if (P_Distribution_Booklet_ID <= 0) set_Value ("P_Distribution_Booklet_ID", null);
 else 
set_Value ("P_Distribution_Booklet_ID", new Integer(P_Distribution_Booklet_ID));
}
/** Get Team.
@return Team */
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
/** Set Employee .
@param P_Employee_ID Employee */
public void setP_Employee_ID (int P_Employee_ID)
{
if (P_Employee_ID <= 0) set_Value ("P_Employee_ID", null);
 else 
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
/** Set Financial Institution.
@param P_Financial_Institution_ID Financial Institution */
public void setP_Financial_Institution_ID (int P_Financial_Institution_ID)
{
if (P_Financial_Institution_ID <= 0) set_Value ("P_Financial_Institution_ID", null);
 else 
set_Value ("P_Financial_Institution_ID", new Integer(P_Financial_Institution_ID));
}
/** Get Financial Institution.
@return Financial Institution */
public int getP_Financial_Institution_ID() 
{
Integer ii = (Integer)get_Value("P_Financial_Institution_ID");
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
/** Set PAY_CD_PROV.
@param PAY_CD_PROV PAY_CD_PROV */
public void setPAY_CD_PROV (String PAY_CD_PROV)
{
if (PAY_CD_PROV != null && PAY_CD_PROV.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_CD_PROV = PAY_CD_PROV.substring(0,199);
}
set_Value ("PAY_CD_PROV", PAY_CD_PROV);
}
/** Get PAY_CD_PROV.
@return PAY_CD_PROV */
public String getPAY_CD_PROV() 
{
return (String)get_Value("PAY_CD_PROV");
}
/** Set PAY_CHQ_DEPOT.
@param PAY_CHQ_DEPOT PAY_CHQ_DEPOT */
public void setPAY_CHQ_DEPOT (String PAY_CHQ_DEPOT)
{
if (PAY_CHQ_DEPOT != null && PAY_CHQ_DEPOT.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_CHQ_DEPOT = PAY_CHQ_DEPOT.substring(0,199);
}
set_Value ("PAY_CHQ_DEPOT", PAY_CHQ_DEPOT);
}
/** Get PAY_CHQ_DEPOT.
@return PAY_CHQ_DEPOT */
public String getPAY_CHQ_DEPOT() 
{
return (String)get_Value("PAY_CHQ_DEPOT");
}
/** Set PAY_CIE_NO.
@param PAY_CIE_NO PAY_CIE_NO */
public void setPAY_CIE_NO (String PAY_CIE_NO)
{
if (PAY_CIE_NO != null && PAY_CIE_NO.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_CIE_NO = PAY_CIE_NO.substring(0,199);
}
set_Value ("PAY_CIE_NO", PAY_CIE_NO);
}
/** Get PAY_CIE_NO.
@return PAY_CIE_NO */
public String getPAY_CIE_NO() 
{
return (String)get_Value("PAY_CIE_NO");
}
/** Set PAY_CSS_NO.
@param PAY_CSS_NO PAY_CSS_NO */
public void setPAY_CSS_NO (String PAY_CSS_NO)
{
if (PAY_CSS_NO != null && PAY_CSS_NO.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_CSS_NO = PAY_CSS_NO.substring(0,199);
}
set_Value ("PAY_CSS_NO", PAY_CSS_NO);
}
/** Get PAY_CSS_NO.
@return PAY_CSS_NO */
public String getPAY_CSS_NO() 
{
return (String)get_Value("PAY_CSS_NO");
}
/** Set PAY_DEPT_NO.
@param PAY_DEPT_NO PAY_DEPT_NO */
public void setPAY_DEPT_NO (String PAY_DEPT_NO)
{
if (PAY_DEPT_NO != null && PAY_DEPT_NO.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_DEPT_NO = PAY_DEPT_NO.substring(0,199);
}
set_Value ("PAY_DEPT_NO", PAY_DEPT_NO);
}
/** Get PAY_DEPT_NO.
@return PAY_DEPT_NO */
public String getPAY_DEPT_NO() 
{
return (String)get_Value("PAY_DEPT_NO");
}
/** Set PAY_DSS_NO.
@param PAY_DSS_NO PAY_DSS_NO */
public void setPAY_DSS_NO (String PAY_DSS_NO)
{
if (PAY_DSS_NO != null && PAY_DSS_NO.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_DSS_NO = PAY_DSS_NO.substring(0,199);
}
set_Value ("PAY_DSS_NO", PAY_DSS_NO);
}
/** Get PAY_DSS_NO.
@return PAY_DSS_NO */
public String getPAY_DSS_NO() 
{
return (String)get_Value("PAY_DSS_NO");
}
/** Set PAY_ECH_ECHELON.
@param PAY_ECH_ECHELON PAY_ECH_ECHELON */
public void setPAY_ECH_ECHELON (String PAY_ECH_ECHELON)
{
if (PAY_ECH_ECHELON != null && PAY_ECH_ECHELON.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_ECH_ECHELON = PAY_ECH_ECHELON.substring(0,199);
}
set_Value ("PAY_ECH_ECHELON", PAY_ECH_ECHELON);
}
/** Get PAY_ECH_ECHELON.
@return PAY_ECH_ECHELON */
public String getPAY_ECH_ECHELON() 
{
return (String)get_Value("PAY_ECH_ECHELON");
}
/** Set PAY_ECH_PERMIS_COND.
@param PAY_ECH_PERMIS_COND PAY_ECH_PERMIS_COND */
public void setPAY_ECH_PERMIS_COND (String PAY_ECH_PERMIS_COND)
{
if (PAY_ECH_PERMIS_COND != null && PAY_ECH_PERMIS_COND.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_ECH_PERMIS_COND = PAY_ECH_PERMIS_COND.substring(0,199);
}
set_Value ("PAY_ECH_PERMIS_COND", PAY_ECH_PERMIS_COND);
}
/** Get PAY_ECH_PERMIS_COND.
@return PAY_ECH_PERMIS_COND */
public String getPAY_ECH_PERMIS_COND() 
{
return (String)get_Value("PAY_ECH_PERMIS_COND");
}
/** Set PAY_EMP_ACTIF.
@param PAY_EMP_ACTIF PAY_EMP_ACTIF */
public void setPAY_EMP_ACTIF (String PAY_EMP_ACTIF)
{
if (PAY_EMP_ACTIF != null && PAY_EMP_ACTIF.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_ACTIF = PAY_EMP_ACTIF.substring(0,199);
}
set_Value ("PAY_EMP_ACTIF", PAY_EMP_ACTIF);
}
/** Get PAY_EMP_ACTIF.
@return PAY_EMP_ACTIF */
public String getPAY_EMP_ACTIF() 
{
return (String)get_Value("PAY_EMP_ACTIF");
}
/** Set PAY_EMP_ADR1.
@param PAY_EMP_ADR1 PAY_EMP_ADR1 */
public void setPAY_EMP_ADR1 (String PAY_EMP_ADR1)
{
if (PAY_EMP_ADR1 != null && PAY_EMP_ADR1.length() > 30)
{
log.warning("Length > 30 - truncated");
PAY_EMP_ADR1 = PAY_EMP_ADR1.substring(0,29);
}
set_Value ("PAY_EMP_ADR1", PAY_EMP_ADR1);
}
/** Get PAY_EMP_ADR1.
@return PAY_EMP_ADR1 */
public String getPAY_EMP_ADR1() 
{
return (String)get_Value("PAY_EMP_ADR1");
}
/** Set PAY_EMP_ADR2.
@param PAY_EMP_ADR2 PAY_EMP_ADR2 */
public void setPAY_EMP_ADR2 (String PAY_EMP_ADR2)
{
if (PAY_EMP_ADR2 != null && PAY_EMP_ADR2.length() > 30)
{
log.warning("Length > 30 - truncated");
PAY_EMP_ADR2 = PAY_EMP_ADR2.substring(0,29);
}
set_Value ("PAY_EMP_ADR2", PAY_EMP_ADR2);
}
/** Get PAY_EMP_ADR2.
@return PAY_EMP_ADR2 */
public String getPAY_EMP_ADR2() 
{
return (String)get_Value("PAY_EMP_ADR2");
}
/** Set PAY_EMP_ADR3.
@param PAY_EMP_ADR3 PAY_EMP_ADR3 */
public void setPAY_EMP_ADR3 (String PAY_EMP_ADR3)
{
if (PAY_EMP_ADR3 != null && PAY_EMP_ADR3.length() > 30)
{
log.warning("Length > 30 - truncated");
PAY_EMP_ADR3 = PAY_EMP_ADR3.substring(0,29);
}
set_Value ("PAY_EMP_ADR3", PAY_EMP_ADR3);
}
/** Get PAY_EMP_ADR3.
@return PAY_EMP_ADR3 */
public String getPAY_EMP_ADR3() 
{
return (String)get_Value("PAY_EMP_ADR3");
}
/** Set PAY_EMP_AVA_IMP.
@param PAY_EMP_AVA_IMP PAY_EMP_AVA_IMP */
public void setPAY_EMP_AVA_IMP (String PAY_EMP_AVA_IMP)
{
if (PAY_EMP_AVA_IMP != null && PAY_EMP_AVA_IMP.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_AVA_IMP = PAY_EMP_AVA_IMP.substring(0,199);
}
set_Value ("PAY_EMP_AVA_IMP", PAY_EMP_AVA_IMP);
}
/** Get PAY_EMP_AVA_IMP.
@return PAY_EMP_AVA_IMP */
public String getPAY_EMP_AVA_IMP() 
{
return (String)get_Value("PAY_EMP_AVA_IMP");
}
/** Set PAY_EMP_CELL.
@param PAY_EMP_CELL PAY_EMP_CELL */
public void setPAY_EMP_CELL (String PAY_EMP_CELL)
{
if (PAY_EMP_CELL != null && PAY_EMP_CELL.length() > 30)
{
log.warning("Length > 30 - truncated");
PAY_EMP_CELL = PAY_EMP_CELL.substring(0,29);
}
set_Value ("PAY_EMP_CELL", PAY_EMP_CELL);
}
/** Get PAY_EMP_CELL.
@return PAY_EMP_CELL */
public String getPAY_EMP_CELL() 
{
return (String)get_Value("PAY_EMP_CELL");
}
/** Set PAY_EMP_CPOSTAL.
@param PAY_EMP_CPOSTAL PAY_EMP_CPOSTAL */
public void setPAY_EMP_CPOSTAL (String PAY_EMP_CPOSTAL)
{
if (PAY_EMP_CPOSTAL != null && PAY_EMP_CPOSTAL.length() > 30)
{
log.warning("Length > 30 - truncated");
PAY_EMP_CPOSTAL = PAY_EMP_CPOSTAL.substring(0,29);
}
set_Value ("PAY_EMP_CPOSTAL", PAY_EMP_CPOSTAL);
}
/** Get PAY_EMP_CPOSTAL.
@return PAY_EMP_CPOSTAL */
public String getPAY_EMP_CPOSTAL() 
{
return (String)get_Value("PAY_EMP_CPOSTAL");
}
/** Set PAY_EMP_CPT_BQ.
@param PAY_EMP_CPT_BQ PAY_EMP_CPT_BQ */
public void setPAY_EMP_CPT_BQ (String PAY_EMP_CPT_BQ)
{
if (PAY_EMP_CPT_BQ != null && PAY_EMP_CPT_BQ.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_CPT_BQ = PAY_EMP_CPT_BQ.substring(0,199);
}
set_Value ("PAY_EMP_CPT_BQ", PAY_EMP_CPT_BQ);
}
/** Get PAY_EMP_CPT_BQ.
@return PAY_EMP_CPT_BQ */
public String getPAY_EMP_CPT_BQ() 
{
return (String)get_Value("PAY_EMP_CPT_BQ");
}
/** Set PAY_EMP_DA_ANC.
@param PAY_EMP_DA_ANC PAY_EMP_DA_ANC */
public void setPAY_EMP_DA_ANC (String PAY_EMP_DA_ANC)
{
if (PAY_EMP_DA_ANC != null && PAY_EMP_DA_ANC.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_DA_ANC = PAY_EMP_DA_ANC.substring(0,199);
}
set_Value ("PAY_EMP_DA_ANC", PAY_EMP_DA_ANC);
}
/** Get PAY_EMP_DA_ANC.
@return PAY_EMP_DA_ANC */
public String getPAY_EMP_DA_ANC() 
{
return (String)get_Value("PAY_EMP_DA_ANC");
}
/** Set PAY_EMP_DA_ANC_0A.
@param PAY_EMP_DA_ANC_0A PAY_EMP_DA_ANC_0A */
public void setPAY_EMP_DA_ANC_0A (String PAY_EMP_DA_ANC_0A)
{
if (PAY_EMP_DA_ANC_0A != null && PAY_EMP_DA_ANC_0A.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_DA_ANC_0A = PAY_EMP_DA_ANC_0A.substring(0,199);
}
set_Value ("PAY_EMP_DA_ANC_0A", PAY_EMP_DA_ANC_0A);
}
/** Get PAY_EMP_DA_ANC_0A.
@return PAY_EMP_DA_ANC_0A */
public String getPAY_EMP_DA_ANC_0A() 
{
return (String)get_Value("PAY_EMP_DA_ANC_0A");
}
/** Set PAY_EMP_DA_CAL.
@param PAY_EMP_DA_CAL PAY_EMP_DA_CAL */
public void setPAY_EMP_DA_CAL (String PAY_EMP_DA_CAL)
{
if (PAY_EMP_DA_CAL != null && PAY_EMP_DA_CAL.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_DA_CAL = PAY_EMP_DA_CAL.substring(0,199);
}
set_Value ("PAY_EMP_DA_CAL", PAY_EMP_DA_CAL);
}
/** Get PAY_EMP_DA_CAL.
@return PAY_EMP_DA_CAL */
public String getPAY_EMP_DA_CAL() 
{
return (String)get_Value("PAY_EMP_DA_CAL");
}
/** Set PAY_EMP_DA_EMB.
@param PAY_EMP_DA_EMB PAY_EMP_DA_EMB */
public void setPAY_EMP_DA_EMB (String PAY_EMP_DA_EMB)
{
if (PAY_EMP_DA_EMB != null && PAY_EMP_DA_EMB.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_DA_EMB = PAY_EMP_DA_EMB.substring(0,199);
}
set_Value ("PAY_EMP_DA_EMB", PAY_EMP_DA_EMB);
}
/** Get PAY_EMP_DA_EMB.
@return PAY_EMP_DA_EMB */
public String getPAY_EMP_DA_EMB() 
{
return (String)get_Value("PAY_EMP_DA_EMB");
}
/** Set PAY_EMP_DA_MAP.
@param PAY_EMP_DA_MAP PAY_EMP_DA_MAP */
public void setPAY_EMP_DA_MAP (String PAY_EMP_DA_MAP)
{
if (PAY_EMP_DA_MAP != null && PAY_EMP_DA_MAP.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_DA_MAP = PAY_EMP_DA_MAP.substring(0,199);
}
set_Value ("PAY_EMP_DA_MAP", PAY_EMP_DA_MAP);
}
/** Get PAY_EMP_DA_MAP.
@return PAY_EMP_DA_MAP */
public String getPAY_EMP_DA_MAP() 
{
return (String)get_Value("PAY_EMP_DA_MAP");
}
/** Set PAY_EMP_DA_NAIS.
@param PAY_EMP_DA_NAIS PAY_EMP_DA_NAIS */
public void setPAY_EMP_DA_NAIS (String PAY_EMP_DA_NAIS)
{
if (PAY_EMP_DA_NAIS != null && PAY_EMP_DA_NAIS.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_DA_NAIS = PAY_EMP_DA_NAIS.substring(0,199);
}
set_Value ("PAY_EMP_DA_NAIS", PAY_EMP_DA_NAIS);
}
/** Get PAY_EMP_DA_NAIS.
@return PAY_EMP_DA_NAIS */
public String getPAY_EMP_DA_NAIS() 
{
return (String)get_Value("PAY_EMP_DA_NAIS");
}
/** Set PAY_EMP_DA_PREV_SAL.
@param PAY_EMP_DA_PREV_SAL PAY_EMP_DA_PREV_SAL */
public void setPAY_EMP_DA_PREV_SAL (String PAY_EMP_DA_PREV_SAL)
{
if (PAY_EMP_DA_PREV_SAL != null && PAY_EMP_DA_PREV_SAL.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_DA_PREV_SAL = PAY_EMP_DA_PREV_SAL.substring(0,199);
}
set_Value ("PAY_EMP_DA_PREV_SAL", PAY_EMP_DA_PREV_SAL);
}
/** Get PAY_EMP_DA_PREV_SAL.
@return PAY_EMP_DA_PREV_SAL */
public String getPAY_EMP_DA_PREV_SAL() 
{
return (String)get_Value("PAY_EMP_DA_PREV_SAL");
}
/** Set PAY_EMP_DA_RET.
@param PAY_EMP_DA_RET PAY_EMP_DA_RET */
public void setPAY_EMP_DA_RET (String PAY_EMP_DA_RET)
{
if (PAY_EMP_DA_RET != null && PAY_EMP_DA_RET.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_DA_RET = PAY_EMP_DA_RET.substring(0,199);
}
set_Value ("PAY_EMP_DA_RET", PAY_EMP_DA_RET);
}
/** Get PAY_EMP_DA_RET.
@return PAY_EMP_DA_RET */
public String getPAY_EMP_DA_RET() 
{
return (String)get_Value("PAY_EMP_DA_RET");
}
/** Set PAY_EMP_DA_RETRAITE.
@param PAY_EMP_DA_RETRAITE PAY_EMP_DA_RETRAITE */
public void setPAY_EMP_DA_RETRAITE (String PAY_EMP_DA_RETRAITE)
{
if (PAY_EMP_DA_RETRAITE != null && PAY_EMP_DA_RETRAITE.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_DA_RETRAITE = PAY_EMP_DA_RETRAITE.substring(0,199);
}
set_Value ("PAY_EMP_DA_RETRAITE", PAY_EMP_DA_RETRAITE);
}
/** Get PAY_EMP_DA_RETRAITE.
@return PAY_EMP_DA_RETRAITE */
public String getPAY_EMP_DA_RETRAITE() 
{
return (String)get_Value("PAY_EMP_DA_RETRAITE");
}
/** Set PAY_EMP_DA_REV_SAL.
@param PAY_EMP_DA_REV_SAL PAY_EMP_DA_REV_SAL */
public void setPAY_EMP_DA_REV_SAL (String PAY_EMP_DA_REV_SAL)
{
if (PAY_EMP_DA_REV_SAL != null && PAY_EMP_DA_REV_SAL.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_DA_REV_SAL = PAY_EMP_DA_REV_SAL.substring(0,199);
}
set_Value ("PAY_EMP_DA_REV_SAL", PAY_EMP_DA_REV_SAL);
}
/** Get PAY_EMP_DA_REV_SAL.
@return PAY_EMP_DA_REV_SAL */
public String getPAY_EMP_DA_REV_SAL() 
{
return (String)get_Value("PAY_EMP_DA_REV_SAL");
}
/** Set PAY_EMP_DERN_PEP.
@param PAY_EMP_DERN_PEP PAY_EMP_DERN_PEP */
public void setPAY_EMP_DERN_PEP (String PAY_EMP_DERN_PEP)
{
if (PAY_EMP_DERN_PEP != null && PAY_EMP_DERN_PEP.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_DERN_PEP = PAY_EMP_DERN_PEP.substring(0,199);
}
set_Value ("PAY_EMP_DERN_PEP", PAY_EMP_DERN_PEP);
}
/** Get PAY_EMP_DERN_PEP.
@return PAY_EMP_DERN_PEP */
public String getPAY_EMP_DERN_PEP() 
{
return (String)get_Value("PAY_EMP_DERN_PEP");
}
/** Set PAY_EMP_DIVA1.
@param PAY_EMP_DIVA1 PAY_EMP_DIVA1 */
public void setPAY_EMP_DIVA1 (String PAY_EMP_DIVA1)
{
if (PAY_EMP_DIVA1 != null && PAY_EMP_DIVA1.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_DIVA1 = PAY_EMP_DIVA1.substring(0,199);
}
set_Value ("PAY_EMP_DIVA1", PAY_EMP_DIVA1);
}
/** Get PAY_EMP_DIVA1.
@return PAY_EMP_DIVA1 */
public String getPAY_EMP_DIVA1() 
{
return (String)get_Value("PAY_EMP_DIVA1");
}
/** Set PAY_EMP_DIVA2.
@param PAY_EMP_DIVA2 PAY_EMP_DIVA2 */
public void setPAY_EMP_DIVA2 (String PAY_EMP_DIVA2)
{
if (PAY_EMP_DIVA2 != null && PAY_EMP_DIVA2.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_DIVA2 = PAY_EMP_DIVA2.substring(0,199);
}
set_Value ("PAY_EMP_DIVA2", PAY_EMP_DIVA2);
}
/** Get PAY_EMP_DIVA2.
@return PAY_EMP_DIVA2 */
public String getPAY_EMP_DIVA2() 
{
return (String)get_Value("PAY_EMP_DIVA2");
}
/** Set PAY_EMP_DIVA3.
@param PAY_EMP_DIVA3 PAY_EMP_DIVA3 */
public void setPAY_EMP_DIVA3 (String PAY_EMP_DIVA3)
{
if (PAY_EMP_DIVA3 != null && PAY_EMP_DIVA3.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_DIVA3 = PAY_EMP_DIVA3.substring(0,199);
}
set_Value ("PAY_EMP_DIVA3", PAY_EMP_DIVA3);
}
/** Get PAY_EMP_DIVA3.
@return PAY_EMP_DIVA3 */
public String getPAY_EMP_DIVA3() 
{
return (String)get_Value("PAY_EMP_DIVA3");
}
/** Set PAY_EMP_DIVA4.
@param PAY_EMP_DIVA4 PAY_EMP_DIVA4 */
public void setPAY_EMP_DIVA4 (String PAY_EMP_DIVA4)
{
if (PAY_EMP_DIVA4 != null && PAY_EMP_DIVA4.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_DIVA4 = PAY_EMP_DIVA4.substring(0,199);
}
set_Value ("PAY_EMP_DIVA4", PAY_EMP_DIVA4);
}
/** Get PAY_EMP_DIVA4.
@return PAY_EMP_DIVA4 */
public String getPAY_EMP_DIVA4() 
{
return (String)get_Value("PAY_EMP_DIVA4");
}
/** Set PAY_EMP_DIVA5.
@param PAY_EMP_DIVA5 PAY_EMP_DIVA5 */
public void setPAY_EMP_DIVA5 (String PAY_EMP_DIVA5)
{
if (PAY_EMP_DIVA5 != null && PAY_EMP_DIVA5.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_DIVA5 = PAY_EMP_DIVA5.substring(0,199);
}
set_Value ("PAY_EMP_DIVA5", PAY_EMP_DIVA5);
}
/** Get PAY_EMP_DIVA5.
@return PAY_EMP_DIVA5 */
public String getPAY_EMP_DIVA5() 
{
return (String)get_Value("PAY_EMP_DIVA5");
}
/** Set PAY_EMP_DIVA6.
@param PAY_EMP_DIVA6 PAY_EMP_DIVA6 */
public void setPAY_EMP_DIVA6 (String PAY_EMP_DIVA6)
{
if (PAY_EMP_DIVA6 != null && PAY_EMP_DIVA6.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_DIVA6 = PAY_EMP_DIVA6.substring(0,199);
}
set_Value ("PAY_EMP_DIVA6", PAY_EMP_DIVA6);
}
/** Get PAY_EMP_DIVA6.
@return PAY_EMP_DIVA6 */
public String getPAY_EMP_DIVA6() 
{
return (String)get_Value("PAY_EMP_DIVA6");
}
/** Set PAY_EMP_DIVA7.
@param PAY_EMP_DIVA7 PAY_EMP_DIVA7 */
public void setPAY_EMP_DIVA7 (String PAY_EMP_DIVA7)
{
if (PAY_EMP_DIVA7 != null && PAY_EMP_DIVA7.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_DIVA7 = PAY_EMP_DIVA7.substring(0,199);
}
set_Value ("PAY_EMP_DIVA7", PAY_EMP_DIVA7);
}
/** Get PAY_EMP_DIVA7.
@return PAY_EMP_DIVA7 */
public String getPAY_EMP_DIVA7() 
{
return (String)get_Value("PAY_EMP_DIVA7");
}
/** Set PAY_EMP_DIVA8.
@param PAY_EMP_DIVA8 PAY_EMP_DIVA8 */
public void setPAY_EMP_DIVA8 (String PAY_EMP_DIVA8)
{
if (PAY_EMP_DIVA8 != null && PAY_EMP_DIVA8.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_DIVA8 = PAY_EMP_DIVA8.substring(0,199);
}
set_Value ("PAY_EMP_DIVA8", PAY_EMP_DIVA8);
}
/** Get PAY_EMP_DIVA8.
@return PAY_EMP_DIVA8 */
public String getPAY_EMP_DIVA8() 
{
return (String)get_Value("PAY_EMP_DIVA8");
}
/** Set PAY_EMP_DIVA9.
@param PAY_EMP_DIVA9 PAY_EMP_DIVA9 */
public void setPAY_EMP_DIVA9 (String PAY_EMP_DIVA9)
{
if (PAY_EMP_DIVA9 != null && PAY_EMP_DIVA9.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_DIVA9 = PAY_EMP_DIVA9.substring(0,199);
}
set_Value ("PAY_EMP_DIVA9", PAY_EMP_DIVA9);
}
/** Get PAY_EMP_DIVA9.
@return PAY_EMP_DIVA9 */
public String getPAY_EMP_DIVA9() 
{
return (String)get_Value("PAY_EMP_DIVA9");
}
/** Set PAY_EMP_DIVN1.
@param PAY_EMP_DIVN1 PAY_EMP_DIVN1 */
public void setPAY_EMP_DIVN1 (String PAY_EMP_DIVN1)
{
if (PAY_EMP_DIVN1 != null && PAY_EMP_DIVN1.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_DIVN1 = PAY_EMP_DIVN1.substring(0,199);
}
set_Value ("PAY_EMP_DIVN1", PAY_EMP_DIVN1);
}
/** Get PAY_EMP_DIVN1.
@return PAY_EMP_DIVN1 */
public String getPAY_EMP_DIVN1() 
{
return (String)get_Value("PAY_EMP_DIVN1");
}
/** Set PAY_EMP_DIVN2.
@param PAY_EMP_DIVN2 PAY_EMP_DIVN2 */
public void setPAY_EMP_DIVN2 (String PAY_EMP_DIVN2)
{
if (PAY_EMP_DIVN2 != null && PAY_EMP_DIVN2.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_DIVN2 = PAY_EMP_DIVN2.substring(0,199);
}
set_Value ("PAY_EMP_DIVN2", PAY_EMP_DIVN2);
}
/** Get PAY_EMP_DIVN2.
@return PAY_EMP_DIVN2 */
public String getPAY_EMP_DIVN2() 
{
return (String)get_Value("PAY_EMP_DIVN2");
}
/** Set PAY_EMP_DIVN3.
@param PAY_EMP_DIVN3 PAY_EMP_DIVN3 */
public void setPAY_EMP_DIVN3 (String PAY_EMP_DIVN3)
{
if (PAY_EMP_DIVN3 != null && PAY_EMP_DIVN3.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_DIVN3 = PAY_EMP_DIVN3.substring(0,199);
}
set_Value ("PAY_EMP_DIVN3", PAY_EMP_DIVN3);
}
/** Get PAY_EMP_DIVN3.
@return PAY_EMP_DIVN3 */
public String getPAY_EMP_DIVN3() 
{
return (String)get_Value("PAY_EMP_DIVN3");
}
/** Set PAY_EMP_DIVN4.
@param PAY_EMP_DIVN4 PAY_EMP_DIVN4 */
public void setPAY_EMP_DIVN4 (String PAY_EMP_DIVN4)
{
if (PAY_EMP_DIVN4 != null && PAY_EMP_DIVN4.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_DIVN4 = PAY_EMP_DIVN4.substring(0,199);
}
set_Value ("PAY_EMP_DIVN4", PAY_EMP_DIVN4);
}
/** Get PAY_EMP_DIVN4.
@return PAY_EMP_DIVN4 */
public String getPAY_EMP_DIVN4() 
{
return (String)get_Value("PAY_EMP_DIVN4");
}
/** Set PAY_EMP_DIVN5.
@param PAY_EMP_DIVN5 PAY_EMP_DIVN5 */
public void setPAY_EMP_DIVN5 (String PAY_EMP_DIVN5)
{
if (PAY_EMP_DIVN5 != null && PAY_EMP_DIVN5.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_DIVN5 = PAY_EMP_DIVN5.substring(0,199);
}
set_Value ("PAY_EMP_DIVN5", PAY_EMP_DIVN5);
}
/** Get PAY_EMP_DIVN5.
@return PAY_EMP_DIVN5 */
public String getPAY_EMP_DIVN5() 
{
return (String)get_Value("PAY_EMP_DIVN5");
}
/** Set PAY_EMP_DIVN6.
@param PAY_EMP_DIVN6 PAY_EMP_DIVN6 */
public void setPAY_EMP_DIVN6 (String PAY_EMP_DIVN6)
{
if (PAY_EMP_DIVN6 != null && PAY_EMP_DIVN6.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_DIVN6 = PAY_EMP_DIVN6.substring(0,199);
}
set_Value ("PAY_EMP_DIVN6", PAY_EMP_DIVN6);
}
/** Get PAY_EMP_DIVN6.
@return PAY_EMP_DIVN6 */
public String getPAY_EMP_DIVN6() 
{
return (String)get_Value("PAY_EMP_DIVN6");
}
/** Set PAY_EMP_DIVN7.
@param PAY_EMP_DIVN7 PAY_EMP_DIVN7 */
public void setPAY_EMP_DIVN7 (String PAY_EMP_DIVN7)
{
if (PAY_EMP_DIVN7 != null && PAY_EMP_DIVN7.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_DIVN7 = PAY_EMP_DIVN7.substring(0,199);
}
set_Value ("PAY_EMP_DIVN7", PAY_EMP_DIVN7);
}
/** Get PAY_EMP_DIVN7.
@return PAY_EMP_DIVN7 */
public String getPAY_EMP_DIVN7() 
{
return (String)get_Value("PAY_EMP_DIVN7");
}
/** Set PAY_EMP_DIVN8.
@param PAY_EMP_DIVN8 PAY_EMP_DIVN8 */
public void setPAY_EMP_DIVN8 (String PAY_EMP_DIVN8)
{
if (PAY_EMP_DIVN8 != null && PAY_EMP_DIVN8.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_DIVN8 = PAY_EMP_DIVN8.substring(0,199);
}
set_Value ("PAY_EMP_DIVN8", PAY_EMP_DIVN8);
}
/** Get PAY_EMP_DIVN8.
@return PAY_EMP_DIVN8 */
public String getPAY_EMP_DIVN8() 
{
return (String)get_Value("PAY_EMP_DIVN8");
}
/** Set PAY_EMP_DIVN9.
@param PAY_EMP_DIVN9 PAY_EMP_DIVN9 */
public void setPAY_EMP_DIVN9 (String PAY_EMP_DIVN9)
{
if (PAY_EMP_DIVN9 != null && PAY_EMP_DIVN9.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_DIVN9 = PAY_EMP_DIVN9.substring(0,199);
}
set_Value ("PAY_EMP_DIVN9", PAY_EMP_DIVN9);
}
/** Get PAY_EMP_DIVN9.
@return PAY_EMP_DIVN9 */
public String getPAY_EMP_DIVN9() 
{
return (String)get_Value("PAY_EMP_DIVN9");
}
/** Set PAY_EMP_FAX.
@param PAY_EMP_FAX PAY_EMP_FAX */
public void setPAY_EMP_FAX (String PAY_EMP_FAX)
{
if (PAY_EMP_FAX != null && PAY_EMP_FAX.length() > 30)
{
log.warning("Length > 30 - truncated");
PAY_EMP_FAX = PAY_EMP_FAX.substring(0,29);
}
set_Value ("PAY_EMP_FAX", PAY_EMP_FAX);
}
/** Get PAY_EMP_FAX.
@return PAY_EMP_FAX */
public String getPAY_EMP_FAX() 
{
return (String)get_Value("PAY_EMP_FAX");
}
/** Set PAY_EMP_FED_E.
@param PAY_EMP_FED_E PAY_EMP_FED_E */
public void setPAY_EMP_FED_E (String PAY_EMP_FED_E)
{
if (PAY_EMP_FED_E != null && PAY_EMP_FED_E.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_FED_E = PAY_EMP_FED_E.substring(0,199);
}
set_Value ("PAY_EMP_FED_E", PAY_EMP_FED_E);
}
/** Get PAY_EMP_FED_E.
@return PAY_EMP_FED_E */
public String getPAY_EMP_FED_E() 
{
return (String)get_Value("PAY_EMP_FED_E");
}
/** Set PAY_EMP_FED_E1.
@param PAY_EMP_FED_E1 PAY_EMP_FED_E1 */
public void setPAY_EMP_FED_E1 (String PAY_EMP_FED_E1)
{
if (PAY_EMP_FED_E1 != null && PAY_EMP_FED_E1.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_FED_E1 = PAY_EMP_FED_E1.substring(0,199);
}
set_Value ("PAY_EMP_FED_E1", PAY_EMP_FED_E1);
}
/** Get PAY_EMP_FED_E1.
@return PAY_EMP_FED_E1 */
public String getPAY_EMP_FED_E1() 
{
return (String)get_Value("PAY_EMP_FED_E1");
}
/** Set PAY_EMP_FED_F1.
@param PAY_EMP_FED_F1 PAY_EMP_FED_F1 */
public void setPAY_EMP_FED_F1 (String PAY_EMP_FED_F1)
{
if (PAY_EMP_FED_F1 != null && PAY_EMP_FED_F1.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_FED_F1 = PAY_EMP_FED_F1.substring(0,199);
}
set_Value ("PAY_EMP_FED_F1", PAY_EMP_FED_F1);
}
/** Get PAY_EMP_FED_F1.
@return PAY_EMP_FED_F1 */
public String getPAY_EMP_FED_F1() 
{
return (String)get_Value("PAY_EMP_FED_F1");
}
/** Set PAY_EMP_FED_L.
@param PAY_EMP_FED_L PAY_EMP_FED_L */
public void setPAY_EMP_FED_L (String PAY_EMP_FED_L)
{
if (PAY_EMP_FED_L != null && PAY_EMP_FED_L.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_FED_L = PAY_EMP_FED_L.substring(0,199);
}
set_Value ("PAY_EMP_FED_L", PAY_EMP_FED_L);
}
/** Get PAY_EMP_FED_L.
@return PAY_EMP_FED_L */
public String getPAY_EMP_FED_L() 
{
return (String)get_Value("PAY_EMP_FED_L");
}
/** Set PAY_EMP_FED_TAUX_COM.
@param PAY_EMP_FED_TAUX_COM PAY_EMP_FED_TAUX_COM */
public void setPAY_EMP_FED_TAUX_COM (String PAY_EMP_FED_TAUX_COM)
{
if (PAY_EMP_FED_TAUX_COM != null && PAY_EMP_FED_TAUX_COM.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_FED_TAUX_COM = PAY_EMP_FED_TAUX_COM.substring(0,199);
}
set_Value ("PAY_EMP_FED_TAUX_COM", PAY_EMP_FED_TAUX_COM);
}
/** Get PAY_EMP_FED_TAUX_COM.
@return PAY_EMP_FED_TAUX_COM */
public String getPAY_EMP_FED_TAUX_COM() 
{
return (String)get_Value("PAY_EMP_FED_TAUX_COM");
}
/** Set PAY_EMP_FL_REGIS_ANC.
@param PAY_EMP_FL_REGIS_ANC PAY_EMP_FL_REGIS_ANC */
public void setPAY_EMP_FL_REGIS_ANC (String PAY_EMP_FL_REGIS_ANC)
{
if (PAY_EMP_FL_REGIS_ANC != null && PAY_EMP_FL_REGIS_ANC.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_FL_REGIS_ANC = PAY_EMP_FL_REGIS_ANC.substring(0,199);
}
set_Value ("PAY_EMP_FL_REGIS_ANC", PAY_EMP_FL_REGIS_ANC);
}
/** Get PAY_EMP_FL_REGIS_ANC.
@return PAY_EMP_FL_REGIS_ANC */
public String getPAY_EMP_FL_REGIS_ANC() 
{
return (String)get_Value("PAY_EMP_FL_REGIS_ANC");
}
/** Set PAY_EMP_FLAG_REGIS.
@param PAY_EMP_FLAG_REGIS PAY_EMP_FLAG_REGIS */
public void setPAY_EMP_FLAG_REGIS (String PAY_EMP_FLAG_REGIS)
{
if (PAY_EMP_FLAG_REGIS != null && PAY_EMP_FLAG_REGIS.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_FLAG_REGIS = PAY_EMP_FLAG_REGIS.substring(0,199);
}
set_Value ("PAY_EMP_FLAG_REGIS", PAY_EMP_FLAG_REGIS);
}
/** Get PAY_EMP_FLAG_REGIS.
@return PAY_EMP_FLAG_REGIS */
public String getPAY_EMP_FLAG_REGIS() 
{
return (String)get_Value("PAY_EMP_FLAG_REGIS");
}
/** Set PAY_EMP_GAI_IMP.
@param PAY_EMP_GAI_IMP PAY_EMP_GAI_IMP */
public void setPAY_EMP_GAI_IMP (String PAY_EMP_GAI_IMP)
{
if (PAY_EMP_GAI_IMP != null && PAY_EMP_GAI_IMP.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_GAI_IMP = PAY_EMP_GAI_IMP.substring(0,199);
}
set_Value ("PAY_EMP_GAI_IMP", PAY_EMP_GAI_IMP);
}
/** Get PAY_EMP_GAI_IMP.
@return PAY_EMP_GAI_IMP */
public String getPAY_EMP_GAI_IMP() 
{
return (String)get_Value("PAY_EMP_GAI_IMP");
}
/** Set PAY_EMP_HORS_ECH.
@param PAY_EMP_HORS_ECH PAY_EMP_HORS_ECH */
public void setPAY_EMP_HORS_ECH (String PAY_EMP_HORS_ECH)
{
if (PAY_EMP_HORS_ECH != null && PAY_EMP_HORS_ECH.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_HORS_ECH = PAY_EMP_HORS_ECH.substring(0,199);
}
set_Value ("PAY_EMP_HORS_ECH", PAY_EMP_HORS_ECH);
}
/** Get PAY_EMP_HORS_ECH.
@return PAY_EMP_HORS_ECH */
public String getPAY_EMP_HORS_ECH() 
{
return (String)get_Value("PAY_EMP_HORS_ECH");
}
/** Set PAY_EMP_HRE_JOUR.
@param PAY_EMP_HRE_JOUR PAY_EMP_HRE_JOUR */
public void setPAY_EMP_HRE_JOUR (String PAY_EMP_HRE_JOUR)
{
if (PAY_EMP_HRE_JOUR != null && PAY_EMP_HRE_JOUR.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_HRE_JOUR = PAY_EMP_HRE_JOUR.substring(0,199);
}
set_Value ("PAY_EMP_HRE_JOUR", PAY_EMP_HRE_JOUR);
}
/** Get PAY_EMP_HRE_JOUR.
@return PAY_EMP_HRE_JOUR */
public String getPAY_EMP_HRE_JOUR() 
{
return (String)get_Value("PAY_EMP_HRE_JOUR");
}
/** Set PAY_EMP_HRE_SEM.
@param PAY_EMP_HRE_SEM PAY_EMP_HRE_SEM */
public void setPAY_EMP_HRE_SEM (String PAY_EMP_HRE_SEM)
{
if (PAY_EMP_HRE_SEM != null && PAY_EMP_HRE_SEM.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_HRE_SEM = PAY_EMP_HRE_SEM.substring(0,199);
}
set_Value ("PAY_EMP_HRE_SEM", PAY_EMP_HRE_SEM);
}
/** Get PAY_EMP_HRE_SEM.
@return PAY_EMP_HRE_SEM */
public String getPAY_EMP_HRE_SEM() 
{
return (String)get_Value("PAY_EMP_HRE_SEM");
}
/** Set PAY_EMP_HRE_SERV_AN.
@param PAY_EMP_HRE_SERV_AN PAY_EMP_HRE_SERV_AN */
public void setPAY_EMP_HRE_SERV_AN (String PAY_EMP_HRE_SERV_AN)
{
if (PAY_EMP_HRE_SERV_AN != null && PAY_EMP_HRE_SERV_AN.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_HRE_SERV_AN = PAY_EMP_HRE_SERV_AN.substring(0,199);
}
set_Value ("PAY_EMP_HRE_SERV_AN", PAY_EMP_HRE_SERV_AN);
}
/** Get PAY_EMP_HRE_SERV_AN.
@return PAY_EMP_HRE_SERV_AN */
public String getPAY_EMP_HRE_SERV_AN() 
{
return (String)get_Value("PAY_EMP_HRE_SERV_AN");
}
/** Set PAY_EMP_HRE_SERV_TOT.
@param PAY_EMP_HRE_SERV_TOT PAY_EMP_HRE_SERV_TOT */
public void setPAY_EMP_HRE_SERV_TOT (String PAY_EMP_HRE_SERV_TOT)
{
if (PAY_EMP_HRE_SERV_TOT != null && PAY_EMP_HRE_SERV_TOT.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_HRE_SERV_TOT = PAY_EMP_HRE_SERV_TOT.substring(0,199);
}
set_Value ("PAY_EMP_HRE_SERV_TOT", PAY_EMP_HRE_SERV_TOT);
}
/** Get PAY_EMP_HRE_SERV_TOT.
@return PAY_EMP_HRE_SERV_TOT */
public String getPAY_EMP_HRE_SERV_TOT() 
{
return (String)get_Value("PAY_EMP_HRE_SERV_TOT");
}
/** Set PAY_EMP_INCAPACITE.
@param PAY_EMP_INCAPACITE PAY_EMP_INCAPACITE */
public void setPAY_EMP_INCAPACITE (String PAY_EMP_INCAPACITE)
{
if (PAY_EMP_INCAPACITE != null && PAY_EMP_INCAPACITE.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_INCAPACITE = PAY_EMP_INCAPACITE.substring(0,199);
}
set_Value ("PAY_EMP_INCAPACITE", PAY_EMP_INCAPACITE);
}
/** Get PAY_EMP_INCAPACITE.
@return PAY_EMP_INCAPACITE */
public String getPAY_EMP_INCAPACITE() 
{
return (String)get_Value("PAY_EMP_INCAPACITE");
}
/** Set PAY_EMP_IND_COM.
@param PAY_EMP_IND_COM PAY_EMP_IND_COM */
public void setPAY_EMP_IND_COM (String PAY_EMP_IND_COM)
{
if (PAY_EMP_IND_COM != null && PAY_EMP_IND_COM.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_IND_COM = PAY_EMP_IND_COM.substring(0,199);
}
set_Value ("PAY_EMP_IND_COM", PAY_EMP_IND_COM);
}
/** Get PAY_EMP_IND_COM.
@return PAY_EMP_IND_COM */
public String getPAY_EMP_IND_COM() 
{
return (String)get_Value("PAY_EMP_IND_COM");
}
/** Set PAY_EMP_INDIC_A1.
@param PAY_EMP_INDIC_A1 PAY_EMP_INDIC_A1 */
public void setPAY_EMP_INDIC_A1 (String PAY_EMP_INDIC_A1)
{
if (PAY_EMP_INDIC_A1 != null && PAY_EMP_INDIC_A1.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_INDIC_A1 = PAY_EMP_INDIC_A1.substring(0,199);
}
set_Value ("PAY_EMP_INDIC_A1", PAY_EMP_INDIC_A1);
}
/** Get PAY_EMP_INDIC_A1.
@return PAY_EMP_INDIC_A1 */
public String getPAY_EMP_INDIC_A1() 
{
return (String)get_Value("PAY_EMP_INDIC_A1");
}
/** Set PAY_EMP_JR_SERV_TOT.
@param PAY_EMP_JR_SERV_TOT PAY_EMP_JR_SERV_TOT */
public void setPAY_EMP_JR_SERV_TOT (String PAY_EMP_JR_SERV_TOT)
{
if (PAY_EMP_JR_SERV_TOT != null && PAY_EMP_JR_SERV_TOT.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_JR_SERV_TOT = PAY_EMP_JR_SERV_TOT.substring(0,199);
}
set_Value ("PAY_EMP_JR_SERV_TOT", PAY_EMP_JR_SERV_TOT);
}
/** Get PAY_EMP_JR_SERV_TOT.
@return PAY_EMP_JR_SERV_TOT */
public String getPAY_EMP_JR_SERV_TOT() 
{
return (String)get_Value("PAY_EMP_JR_SERV_TOT");
}
/** Set PAY_EMP_MIN_HRE_SUPP.
@param PAY_EMP_MIN_HRE_SUPP PAY_EMP_MIN_HRE_SUPP */
public void setPAY_EMP_MIN_HRE_SUPP (String PAY_EMP_MIN_HRE_SUPP)
{
if (PAY_EMP_MIN_HRE_SUPP != null && PAY_EMP_MIN_HRE_SUPP.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_MIN_HRE_SUPP = PAY_EMP_MIN_HRE_SUPP.substring(0,199);
}
set_Value ("PAY_EMP_MIN_HRE_SUPP", PAY_EMP_MIN_HRE_SUPP);
}
/** Get PAY_EMP_MIN_HRE_SUPP.
@return PAY_EMP_MIN_HRE_SUPP */
public String getPAY_EMP_MIN_HRE_SUPP() 
{
return (String)get_Value("PAY_EMP_MIN_HRE_SUPP");
}
/** Set PAY_EMP_NAS.
@param PAY_EMP_NAS PAY_EMP_NAS */
public void setPAY_EMP_NAS (String PAY_EMP_NAS)
{
if (PAY_EMP_NAS != null && PAY_EMP_NAS.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_NAS = PAY_EMP_NAS.substring(0,199);
}
set_Value ("PAY_EMP_NAS", PAY_EMP_NAS);
}
/** Get PAY_EMP_NAS.
@return PAY_EMP_NAS */
public String getPAY_EMP_NAS() 
{
return (String)get_Value("PAY_EMP_NAS");
}
/** Set PAY_EMP_NO.
@param PAY_EMP_NO PAY_EMP_NO */
public void setPAY_EMP_NO (String PAY_EMP_NO)
{
if (PAY_EMP_NO != null && PAY_EMP_NO.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_NO = PAY_EMP_NO.substring(0,199);
}
set_Value ("PAY_EMP_NO", PAY_EMP_NO);
}
/** Get PAY_EMP_NO.
@return PAY_EMP_NO */
public String getPAY_EMP_NO() 
{
return (String)get_Value("PAY_EMP_NO");
}
/** Set PAY_EMP_NO_BQ.
@param PAY_EMP_NO_BQ PAY_EMP_NO_BQ */
public void setPAY_EMP_NO_BQ (String PAY_EMP_NO_BQ)
{
if (PAY_EMP_NO_BQ != null && PAY_EMP_NO_BQ.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_NO_BQ = PAY_EMP_NO_BQ.substring(0,199);
}
set_Value ("PAY_EMP_NO_BQ", PAY_EMP_NO_BQ);
}
/** Get PAY_EMP_NO_BQ.
@return PAY_EMP_NO_BQ */
public String getPAY_EMP_NO_BQ() 
{
return (String)get_Value("PAY_EMP_NO_BQ");
}
/** Set PAY_EMP_NO_FTQ.
@param PAY_EMP_NO_FTQ PAY_EMP_NO_FTQ */
public void setPAY_EMP_NO_FTQ (String PAY_EMP_NO_FTQ)
{
if (PAY_EMP_NO_FTQ != null && PAY_EMP_NO_FTQ.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_NO_FTQ = PAY_EMP_NO_FTQ.substring(0,199);
}
set_Value ("PAY_EMP_NO_FTQ", PAY_EMP_NO_FTQ);
}
/** Get PAY_EMP_NO_FTQ.
@return PAY_EMP_NO_FTQ */
public String getPAY_EMP_NO_FTQ() 
{
return (String)get_Value("PAY_EMP_NO_FTQ");
}
/** Set PAY_EMP_NO_RAMQ.
@param PAY_EMP_NO_RAMQ PAY_EMP_NO_RAMQ */
public void setPAY_EMP_NO_RAMQ (String PAY_EMP_NO_RAMQ)
{
if (PAY_EMP_NO_RAMQ != null && PAY_EMP_NO_RAMQ.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_NO_RAMQ = PAY_EMP_NO_RAMQ.substring(0,199);
}
set_Value ("PAY_EMP_NO_RAMQ", PAY_EMP_NO_RAMQ);
}
/** Get PAY_EMP_NO_RAMQ.
@return PAY_EMP_NO_RAMQ */
public String getPAY_EMP_NO_RAMQ() 
{
return (String)get_Value("PAY_EMP_NO_RAMQ");
}
/** Set PAY_EMP_NOM.
@param PAY_EMP_NOM PAY_EMP_NOM */
public void setPAY_EMP_NOM (String PAY_EMP_NOM)
{
if (PAY_EMP_NOM != null && PAY_EMP_NOM.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_NOM = PAY_EMP_NOM.substring(0,199);
}
set_Value ("PAY_EMP_NOM", PAY_EMP_NOM);
}
/** Get PAY_EMP_NOM.
@return PAY_EMP_NOM */
public String getPAY_EMP_NOM() 
{
return (String)get_Value("PAY_EMP_NOM");
}
/** Set PAY_EMP_PC_CL1.
@param PAY_EMP_PC_CL1 PAY_EMP_PC_CL1 */
public void setPAY_EMP_PC_CL1 (String PAY_EMP_PC_CL1)
{
if (PAY_EMP_PC_CL1 != null && PAY_EMP_PC_CL1.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_PC_CL1 = PAY_EMP_PC_CL1.substring(0,199);
}
set_Value ("PAY_EMP_PC_CL1", PAY_EMP_PC_CL1);
}
/** Get PAY_EMP_PC_CL1.
@return PAY_EMP_PC_CL1 */
public String getPAY_EMP_PC_CL1() 
{
return (String)get_Value("PAY_EMP_PC_CL1");
}
/** Set PAY_EMP_PC_CL2.
@param PAY_EMP_PC_CL2 PAY_EMP_PC_CL2 */
public void setPAY_EMP_PC_CL2 (String PAY_EMP_PC_CL2)
{
if (PAY_EMP_PC_CL2 != null && PAY_EMP_PC_CL2.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_PC_CL2 = PAY_EMP_PC_CL2.substring(0,199);
}
set_Value ("PAY_EMP_PC_CL2", PAY_EMP_PC_CL2);
}
/** Get PAY_EMP_PC_CL2.
@return PAY_EMP_PC_CL2 */
public String getPAY_EMP_PC_CL2() 
{
return (String)get_Value("PAY_EMP_PC_CL2");
}
/** Set PAY_EMP_PC_CL3.
@param PAY_EMP_PC_CL3 PAY_EMP_PC_CL3 */
public void setPAY_EMP_PC_CL3 (String PAY_EMP_PC_CL3)
{
if (PAY_EMP_PC_CL3 != null && PAY_EMP_PC_CL3.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_PC_CL3 = PAY_EMP_PC_CL3.substring(0,199);
}
set_Value ("PAY_EMP_PC_CL3", PAY_EMP_PC_CL3);
}
/** Get PAY_EMP_PC_CL3.
@return PAY_EMP_PC_CL3 */
public String getPAY_EMP_PC_CL3() 
{
return (String)get_Value("PAY_EMP_PC_CL3");
}
/** Set PAY_EMP_PC_CL4.
@param PAY_EMP_PC_CL4 PAY_EMP_PC_CL4 */
public void setPAY_EMP_PC_CL4 (String PAY_EMP_PC_CL4)
{
if (PAY_EMP_PC_CL4 != null && PAY_EMP_PC_CL4.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_PC_CL4 = PAY_EMP_PC_CL4.substring(0,199);
}
set_Value ("PAY_EMP_PC_CL4", PAY_EMP_PC_CL4);
}
/** Get PAY_EMP_PC_CL4.
@return PAY_EMP_PC_CL4 */
public String getPAY_EMP_PC_CL4() 
{
return (String)get_Value("PAY_EMP_PC_CL4");
}
/** Set PAY_EMP_PC_CL5.
@param PAY_EMP_PC_CL5 PAY_EMP_PC_CL5 */
public void setPAY_EMP_PC_CL5 (String PAY_EMP_PC_CL5)
{
if (PAY_EMP_PC_CL5 != null && PAY_EMP_PC_CL5.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_PC_CL5 = PAY_EMP_PC_CL5.substring(0,199);
}
set_Value ("PAY_EMP_PC_CL5", PAY_EMP_PC_CL5);
}
/** Get PAY_EMP_PC_CL5.
@return PAY_EMP_PC_CL5 */
public String getPAY_EMP_PC_CL5() 
{
return (String)get_Value("PAY_EMP_PC_CL5");
}
/** Set PAY_EMP_PC_CL6.
@param PAY_EMP_PC_CL6 PAY_EMP_PC_CL6 */
public void setPAY_EMP_PC_CL6 (String PAY_EMP_PC_CL6)
{
if (PAY_EMP_PC_CL6 != null && PAY_EMP_PC_CL6.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_PC_CL6 = PAY_EMP_PC_CL6.substring(0,199);
}
set_Value ("PAY_EMP_PC_CL6", PAY_EMP_PC_CL6);
}
/** Get PAY_EMP_PC_CL6.
@return PAY_EMP_PC_CL6 */
public String getPAY_EMP_PC_CL6() 
{
return (String)get_Value("PAY_EMP_PC_CL6");
}
/** Set PAY_EMP_PC_CL7.
@param PAY_EMP_PC_CL7 PAY_EMP_PC_CL7 */
public void setPAY_EMP_PC_CL7 (String PAY_EMP_PC_CL7)
{
if (PAY_EMP_PC_CL7 != null && PAY_EMP_PC_CL7.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_PC_CL7 = PAY_EMP_PC_CL7.substring(0,199);
}
set_Value ("PAY_EMP_PC_CL7", PAY_EMP_PC_CL7);
}
/** Get PAY_EMP_PC_CL7.
@return PAY_EMP_PC_CL7 */
public String getPAY_EMP_PC_CL7() 
{
return (String)get_Value("PAY_EMP_PC_CL7");
}
/** Set PAY_EMP_PC_CL8.
@param PAY_EMP_PC_CL8 PAY_EMP_PC_CL8 */
public void setPAY_EMP_PC_CL8 (String PAY_EMP_PC_CL8)
{
if (PAY_EMP_PC_CL8 != null && PAY_EMP_PC_CL8.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_PC_CL8 = PAY_EMP_PC_CL8.substring(0,199);
}
set_Value ("PAY_EMP_PC_CL8", PAY_EMP_PC_CL8);
}
/** Get PAY_EMP_PC_CL8.
@return PAY_EMP_PC_CL8 */
public String getPAY_EMP_PC_CL8() 
{
return (String)get_Value("PAY_EMP_PC_CL8");
}
/** Set PAY_EMP_PC_CL9.
@param PAY_EMP_PC_CL9 PAY_EMP_PC_CL9 */
public void setPAY_EMP_PC_CL9 (String PAY_EMP_PC_CL9)
{
if (PAY_EMP_PC_CL9 != null && PAY_EMP_PC_CL9.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_PC_CL9 = PAY_EMP_PC_CL9.substring(0,199);
}
set_Value ("PAY_EMP_PC_CL9", PAY_EMP_PC_CL9);
}
/** Get PAY_EMP_PC_CL9.
@return PAY_EMP_PC_CL9 */
public String getPAY_EMP_PC_CL9() 
{
return (String)get_Value("PAY_EMP_PC_CL9");
}
/** Set PAY_EMP_PC_COND1.
@param PAY_EMP_PC_COND1 PAY_EMP_PC_COND1 */
public void setPAY_EMP_PC_COND1 (String PAY_EMP_PC_COND1)
{
if (PAY_EMP_PC_COND1 != null && PAY_EMP_PC_COND1.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_PC_COND1 = PAY_EMP_PC_COND1.substring(0,199);
}
set_Value ("PAY_EMP_PC_COND1", PAY_EMP_PC_COND1);
}
/** Get PAY_EMP_PC_COND1.
@return PAY_EMP_PC_COND1 */
public String getPAY_EMP_PC_COND1() 
{
return (String)get_Value("PAY_EMP_PC_COND1");
}
/** Set PAY_EMP_PC_COND2.
@param PAY_EMP_PC_COND2 PAY_EMP_PC_COND2 */
public void setPAY_EMP_PC_COND2 (String PAY_EMP_PC_COND2)
{
if (PAY_EMP_PC_COND2 != null && PAY_EMP_PC_COND2.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_PC_COND2 = PAY_EMP_PC_COND2.substring(0,199);
}
set_Value ("PAY_EMP_PC_COND2", PAY_EMP_PC_COND2);
}
/** Get PAY_EMP_PC_COND2.
@return PAY_EMP_PC_COND2 */
public String getPAY_EMP_PC_COND2() 
{
return (String)get_Value("PAY_EMP_PC_COND2");
}
/** Set PAY_EMP_PC_COND3.
@param PAY_EMP_PC_COND3 PAY_EMP_PC_COND3 */
public void setPAY_EMP_PC_COND3 (String PAY_EMP_PC_COND3)
{
if (PAY_EMP_PC_COND3 != null && PAY_EMP_PC_COND3.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_PC_COND3 = PAY_EMP_PC_COND3.substring(0,199);
}
set_Value ("PAY_EMP_PC_COND3", PAY_EMP_PC_COND3);
}
/** Get PAY_EMP_PC_COND3.
@return PAY_EMP_PC_COND3 */
public String getPAY_EMP_PC_COND3() 
{
return (String)get_Value("PAY_EMP_PC_COND3");
}
/** Set PAY_EMP_PC_DA_DEB_SU.
@param PAY_EMP_PC_DA_DEB_SU PAY_EMP_PC_DA_DEB_SU */
public void setPAY_EMP_PC_DA_DEB_SU (String PAY_EMP_PC_DA_DEB_SU)
{
if (PAY_EMP_PC_DA_DEB_SU != null && PAY_EMP_PC_DA_DEB_SU.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_PC_DA_DEB_SU = PAY_EMP_PC_DA_DEB_SU.substring(0,199);
}
set_Value ("PAY_EMP_PC_DA_DEB_SU", PAY_EMP_PC_DA_DEB_SU);
}
/** Get PAY_EMP_PC_DA_DEB_SU.
@return PAY_EMP_PC_DA_DEB_SU */
public String getPAY_EMP_PC_DA_DEB_SU() 
{
return (String)get_Value("PAY_EMP_PC_DA_DEB_SU");
}
/** Set PAY_EMP_PC_DA_FIN_SU.
@param PAY_EMP_PC_DA_FIN_SU PAY_EMP_PC_DA_FIN_SU */
public void setPAY_EMP_PC_DA_FIN_SU (String PAY_EMP_PC_DA_FIN_SU)
{
if (PAY_EMP_PC_DA_FIN_SU != null && PAY_EMP_PC_DA_FIN_SU.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_PC_DA_FIN_SU = PAY_EMP_PC_DA_FIN_SU.substring(0,199);
}
set_Value ("PAY_EMP_PC_DA_FIN_SU", PAY_EMP_PC_DA_FIN_SU);
}
/** Get PAY_EMP_PC_DA_FIN_SU.
@return PAY_EMP_PC_DA_FIN_SU */
public String getPAY_EMP_PC_DA_FIN_SU() 
{
return (String)get_Value("PAY_EMP_PC_DA_FIN_SU");
}
/** Set PAY_EMP_PC_NO.
@param PAY_EMP_PC_NO PAY_EMP_PC_NO */
public void setPAY_EMP_PC_NO (String PAY_EMP_PC_NO)
{
if (PAY_EMP_PC_NO != null && PAY_EMP_PC_NO.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_PC_NO = PAY_EMP_PC_NO.substring(0,199);
}
set_Value ("PAY_EMP_PC_NO", PAY_EMP_PC_NO);
}
/** Get PAY_EMP_PC_NO.
@return PAY_EMP_PC_NO */
public String getPAY_EMP_PC_NO() 
{
return (String)get_Value("PAY_EMP_PC_NO");
}
/** Set PAY_EMP_PRENOM.
@param PAY_EMP_PRENOM PAY_EMP_PRENOM */
public void setPAY_EMP_PRENOM (String PAY_EMP_PRENOM)
{
if (PAY_EMP_PRENOM != null && PAY_EMP_PRENOM.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_PRENOM = PAY_EMP_PRENOM.substring(0,199);
}
set_Value ("PAY_EMP_PRENOM", PAY_EMP_PRENOM);
}
/** Get PAY_EMP_PRENOM.
@return PAY_EMP_PRENOM */
public String getPAY_EMP_PRENOM() 
{
return (String)get_Value("PAY_EMP_PRENOM");
}
/** Set PAY_EMP_PRO_DED.
@param PAY_EMP_PRO_DED PAY_EMP_PRO_DED */
public void setPAY_EMP_PRO_DED (String PAY_EMP_PRO_DED)
{
if (PAY_EMP_PRO_DED != null && PAY_EMP_PRO_DED.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_PRO_DED = PAY_EMP_PRO_DED.substring(0,199);
}
set_Value ("PAY_EMP_PRO_DED", PAY_EMP_PRO_DED);
}
/** Get PAY_EMP_PRO_DED.
@return PAY_EMP_PRO_DED */
public String getPAY_EMP_PRO_DED() 
{
return (String)get_Value("PAY_EMP_PRO_DED");
}
/** Set PAY_EMP_PRO_E.
@param PAY_EMP_PRO_E PAY_EMP_PRO_E */
public void setPAY_EMP_PRO_E (String PAY_EMP_PRO_E)
{
if (PAY_EMP_PRO_E != null && PAY_EMP_PRO_E.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_PRO_E = PAY_EMP_PRO_E.substring(0,199);
}
set_Value ("PAY_EMP_PRO_E", PAY_EMP_PRO_E);
}
/** Get PAY_EMP_PRO_E.
@return PAY_EMP_PRO_E */
public String getPAY_EMP_PRO_E() 
{
return (String)get_Value("PAY_EMP_PRO_E");
}
/** Set PAY_EMP_PRO_L.
@param PAY_EMP_PRO_L PAY_EMP_PRO_L */
public void setPAY_EMP_PRO_L (String PAY_EMP_PRO_L)
{
if (PAY_EMP_PRO_L != null && PAY_EMP_PRO_L.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_PRO_L = PAY_EMP_PRO_L.substring(0,199);
}
set_Value ("PAY_EMP_PRO_L", PAY_EMP_PRO_L);
}
/** Get PAY_EMP_PRO_L.
@return PAY_EMP_PRO_L */
public String getPAY_EMP_PRO_L() 
{
return (String)get_Value("PAY_EMP_PRO_L");
}
/** Set PAY_EMP_PRO_TAUX_COM.
@param PAY_EMP_PRO_TAUX_COM PAY_EMP_PRO_TAUX_COM */
public void setPAY_EMP_PRO_TAUX_COM (String PAY_EMP_PRO_TAUX_COM)
{
if (PAY_EMP_PRO_TAUX_COM != null && PAY_EMP_PRO_TAUX_COM.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_PRO_TAUX_COM = PAY_EMP_PRO_TAUX_COM.substring(0,199);
}
set_Value ("PAY_EMP_PRO_TAUX_COM", PAY_EMP_PRO_TAUX_COM);
}
/** Get PAY_EMP_PRO_TAUX_COM.
@return PAY_EMP_PRO_TAUX_COM */
public String getPAY_EMP_PRO_TAUX_COM() 
{
return (String)get_Value("PAY_EMP_PRO_TAUX_COM");
}
/** Set PAY_EMP_REV_SAL_DEL.
@param PAY_EMP_REV_SAL_DEL PAY_EMP_REV_SAL_DEL */
public void setPAY_EMP_REV_SAL_DEL (String PAY_EMP_REV_SAL_DEL)
{
if (PAY_EMP_REV_SAL_DEL != null && PAY_EMP_REV_SAL_DEL.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_REV_SAL_DEL = PAY_EMP_REV_SAL_DEL.substring(0,199);
}
set_Value ("PAY_EMP_REV_SAL_DEL", PAY_EMP_REV_SAL_DEL);
}
/** Get PAY_EMP_REV_SAL_DEL.
@return PAY_EMP_REV_SAL_DEL */
public String getPAY_EMP_REV_SAL_DEL() 
{
return (String)get_Value("PAY_EMP_REV_SAL_DEL");
}
/** Set PAY_EMP_REV_SAL_DUR.
@param PAY_EMP_REV_SAL_DUR PAY_EMP_REV_SAL_DUR */
public void setPAY_EMP_REV_SAL_DUR (String PAY_EMP_REV_SAL_DUR)
{
if (PAY_EMP_REV_SAL_DUR != null && PAY_EMP_REV_SAL_DUR.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_REV_SAL_DUR = PAY_EMP_REV_SAL_DUR.substring(0,199);
}
set_Value ("PAY_EMP_REV_SAL_DUR", PAY_EMP_REV_SAL_DUR);
}
/** Get PAY_EMP_REV_SAL_DUR.
@return PAY_EMP_REV_SAL_DUR */
public String getPAY_EMP_REV_SAL_DUR() 
{
return (String)get_Value("PAY_EMP_REV_SAL_DUR");
}
/** Set PAY_EMP_REV_SAL_IND.
@param PAY_EMP_REV_SAL_IND PAY_EMP_REV_SAL_IND */
public void setPAY_EMP_REV_SAL_IND (String PAY_EMP_REV_SAL_IND)
{
if (PAY_EMP_REV_SAL_IND != null && PAY_EMP_REV_SAL_IND.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_REV_SAL_IND = PAY_EMP_REV_SAL_IND.substring(0,199);
}
set_Value ("PAY_EMP_REV_SAL_IND", PAY_EMP_REV_SAL_IND);
}
/** Get PAY_EMP_REV_SAL_IND.
@return PAY_EMP_REV_SAL_IND */
public String getPAY_EMP_REV_SAL_IND() 
{
return (String)get_Value("PAY_EMP_REV_SAL_IND");
}
/** Set PAY_EMP_RUE.
@param PAY_EMP_RUE PAY_EMP_RUE */
public void setPAY_EMP_RUE (String PAY_EMP_RUE)
{
if (PAY_EMP_RUE != null && PAY_EMP_RUE.length() > 30)
{
log.warning("Length > 30 - truncated");
PAY_EMP_RUE = PAY_EMP_RUE.substring(0,29);
}
set_Value ("PAY_EMP_RUE", PAY_EMP_RUE);
}
/** Get PAY_EMP_RUE.
@return PAY_EMP_RUE */
public String getPAY_EMP_RUE() 
{
return (String)get_Value("PAY_EMP_RUE");
}
/** Set PAY_EMP_SAL_ANNUEL.
@param PAY_EMP_SAL_ANNUEL PAY_EMP_SAL_ANNUEL */
public void setPAY_EMP_SAL_ANNUEL (String PAY_EMP_SAL_ANNUEL)
{
if (PAY_EMP_SAL_ANNUEL != null && PAY_EMP_SAL_ANNUEL.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_SAL_ANNUEL = PAY_EMP_SAL_ANNUEL.substring(0,199);
}
set_Value ("PAY_EMP_SAL_ANNUEL", PAY_EMP_SAL_ANNUEL);
}
/** Get PAY_EMP_SAL_ANNUEL.
@return PAY_EMP_SAL_ANNUEL */
public String getPAY_EMP_SAL_ANNUEL() 
{
return (String)get_Value("PAY_EMP_SAL_ANNUEL");
}
/** Set PAY_EMP_SAL_HOR_AJU.
@param PAY_EMP_SAL_HOR_AJU PAY_EMP_SAL_HOR_AJU */
public void setPAY_EMP_SAL_HOR_AJU (String PAY_EMP_SAL_HOR_AJU)
{
if (PAY_EMP_SAL_HOR_AJU != null && PAY_EMP_SAL_HOR_AJU.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_SAL_HOR_AJU = PAY_EMP_SAL_HOR_AJU.substring(0,199);
}
set_Value ("PAY_EMP_SAL_HOR_AJU", PAY_EMP_SAL_HOR_AJU);
}
/** Get PAY_EMP_SAL_HOR_AJU.
@return PAY_EMP_SAL_HOR_AJU */
public String getPAY_EMP_SAL_HOR_AJU() 
{
return (String)get_Value("PAY_EMP_SAL_HOR_AJU");
}
/** Set PAY_EMP_SAL_HOR_PRE.
@param PAY_EMP_SAL_HOR_PRE PAY_EMP_SAL_HOR_PRE */
public void setPAY_EMP_SAL_HOR_PRE (String PAY_EMP_SAL_HOR_PRE)
{
if (PAY_EMP_SAL_HOR_PRE != null && PAY_EMP_SAL_HOR_PRE.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_SAL_HOR_PRE = PAY_EMP_SAL_HOR_PRE.substring(0,199);
}
set_Value ("PAY_EMP_SAL_HOR_PRE", PAY_EMP_SAL_HOR_PRE);
}
/** Get PAY_EMP_SAL_HOR_PRE.
@return PAY_EMP_SAL_HOR_PRE */
public String getPAY_EMP_SAL_HOR_PRE() 
{
return (String)get_Value("PAY_EMP_SAL_HOR_PRE");
}
/** Set PAY_EMP_SAL_HOR_VAC.
@param PAY_EMP_SAL_HOR_VAC PAY_EMP_SAL_HOR_VAC */
public void setPAY_EMP_SAL_HOR_VAC (String PAY_EMP_SAL_HOR_VAC)
{
if (PAY_EMP_SAL_HOR_VAC != null && PAY_EMP_SAL_HOR_VAC.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_SAL_HOR_VAC = PAY_EMP_SAL_HOR_VAC.substring(0,199);
}
set_Value ("PAY_EMP_SAL_HOR_VAC", PAY_EMP_SAL_HOR_VAC);
}
/** Get PAY_EMP_SAL_HOR_VAC.
@return PAY_EMP_SAL_HOR_VAC */
public String getPAY_EMP_SAL_HOR_VAC() 
{
return (String)get_Value("PAY_EMP_SAL_HOR_VAC");
}
/** Set PAY_EMP_SAL_HORAIRE.
@param PAY_EMP_SAL_HORAIRE PAY_EMP_SAL_HORAIRE */
public void setPAY_EMP_SAL_HORAIRE (String PAY_EMP_SAL_HORAIRE)
{
if (PAY_EMP_SAL_HORAIRE != null && PAY_EMP_SAL_HORAIRE.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_SAL_HORAIRE = PAY_EMP_SAL_HORAIRE.substring(0,199);
}
set_Value ("PAY_EMP_SAL_HORAIRE", PAY_EMP_SAL_HORAIRE);
}
/** Get PAY_EMP_SAL_HORAIRE.
@return PAY_EMP_SAL_HORAIRE */
public String getPAY_EMP_SAL_HORAIRE() 
{
return (String)get_Value("PAY_EMP_SAL_HORAIRE");
}
/** Set PAY_EMP_SAL_JOUR.
@param PAY_EMP_SAL_JOUR PAY_EMP_SAL_JOUR */
public void setPAY_EMP_SAL_JOUR (String PAY_EMP_SAL_JOUR)
{
if (PAY_EMP_SAL_JOUR != null && PAY_EMP_SAL_JOUR.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_SAL_JOUR = PAY_EMP_SAL_JOUR.substring(0,199);
}
set_Value ("PAY_EMP_SAL_JOUR", PAY_EMP_SAL_JOUR);
}
/** Get PAY_EMP_SAL_JOUR.
@return PAY_EMP_SAL_JOUR */
public String getPAY_EMP_SAL_JOUR() 
{
return (String)get_Value("PAY_EMP_SAL_JOUR");
}
/** Set PAY_EMP_SEXE.
@param PAY_EMP_SEXE PAY_EMP_SEXE */
public void setPAY_EMP_SEXE (String PAY_EMP_SEXE)
{
if (PAY_EMP_SEXE != null && PAY_EMP_SEXE.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_SEXE = PAY_EMP_SEXE.substring(0,199);
}
set_Value ("PAY_EMP_SEXE", PAY_EMP_SEXE);
}
/** Get PAY_EMP_SEXE.
@return PAY_EMP_SEXE */
public String getPAY_EMP_SEXE() 
{
return (String)get_Value("PAY_EMP_SEXE");
}
/** Set PAY_EMP_TAUX_VAC.
@param PAY_EMP_TAUX_VAC PAY_EMP_TAUX_VAC */
public void setPAY_EMP_TAUX_VAC (String PAY_EMP_TAUX_VAC)
{
if (PAY_EMP_TAUX_VAC != null && PAY_EMP_TAUX_VAC.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_TAUX_VAC = PAY_EMP_TAUX_VAC.substring(0,199);
}
set_Value ("PAY_EMP_TAUX_VAC", PAY_EMP_TAUX_VAC);
}
/** Get PAY_EMP_TAUX_VAC.
@return PAY_EMP_TAUX_VAC */
public String getPAY_EMP_TAUX_VAC() 
{
return (String)get_Value("PAY_EMP_TAUX_VAC");
}
/** Set PAY_EMP_TEL1.
@param PAY_EMP_TEL1 PAY_EMP_TEL1 */
public void setPAY_EMP_TEL1 (String PAY_EMP_TEL1)
{
if (PAY_EMP_TEL1 != null && PAY_EMP_TEL1.length() > 30)
{
log.warning("Length > 30 - truncated");
PAY_EMP_TEL1 = PAY_EMP_TEL1.substring(0,29);
}
set_Value ("PAY_EMP_TEL1", PAY_EMP_TEL1);
}
/** Get PAY_EMP_TEL1.
@return PAY_EMP_TEL1 */
public String getPAY_EMP_TEL1() 
{
return (String)get_Value("PAY_EMP_TEL1");
}
/** Set PAY_EMP_TEL2.
@param PAY_EMP_TEL2 PAY_EMP_TEL2 */
public void setPAY_EMP_TEL2 (String PAY_EMP_TEL2)
{
if (PAY_EMP_TEL2 != null && PAY_EMP_TEL2.length() > 30)
{
log.warning("Length > 30 - truncated");
PAY_EMP_TEL2 = PAY_EMP_TEL2.substring(0,29);
}
set_Value ("PAY_EMP_TEL2", PAY_EMP_TEL2);
}
/** Get PAY_EMP_TEL2.
@return PAY_EMP_TEL2 */
public String getPAY_EMP_TEL2() 
{
return (String)get_Value("PAY_EMP_TEL2");
}
/** Set PAY_EMP_TRANSIT_BQ.
@param PAY_EMP_TRANSIT_BQ PAY_EMP_TRANSIT_BQ */
public void setPAY_EMP_TRANSIT_BQ (String PAY_EMP_TRANSIT_BQ)
{
if (PAY_EMP_TRANSIT_BQ != null && PAY_EMP_TRANSIT_BQ.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EMP_TRANSIT_BQ = PAY_EMP_TRANSIT_BQ.substring(0,199);
}
set_Value ("PAY_EMP_TRANSIT_BQ", PAY_EMP_TRANSIT_BQ);
}
/** Get PAY_EMP_TRANSIT_BQ.
@return PAY_EMP_TRANSIT_BQ */
public String getPAY_EMP_TRANSIT_BQ() 
{
return (String)get_Value("PAY_EMP_TRANSIT_BQ");
}
/** Set PAY_EMP_VILLE.
@param PAY_EMP_VILLE PAY_EMP_VILLE */
public void setPAY_EMP_VILLE (String PAY_EMP_VILLE)
{
if (PAY_EMP_VILLE != null && PAY_EMP_VILLE.length() > 30)
{
log.warning("Length > 30 - truncated");
PAY_EMP_VILLE = PAY_EMP_VILLE.substring(0,29);
}
set_Value ("PAY_EMP_VILLE", PAY_EMP_VILLE);
}
/** Get PAY_EMP_VILLE.
@return PAY_EMP_VILLE */
public String getPAY_EMP_VILLE() 
{
return (String)get_Value("PAY_EMP_VILLE");
}
/** Set PAY_EQT_NO.
@param PAY_EQT_NO PAY_EQT_NO */
public void setPAY_EQT_NO (String PAY_EQT_NO)
{
if (PAY_EQT_NO != null && PAY_EQT_NO.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_EQT_NO = PAY_EQT_NO.substring(0,199);
}
set_Value ("PAY_EQT_NO", PAY_EQT_NO);
}
/** Get PAY_EQT_NO.
@return PAY_EQT_NO */
public String getPAY_EQT_NO() 
{
return (String)get_Value("PAY_EQT_NO");
}
/** Set PAY_FLAG_CCQ.
@param PAY_FLAG_CCQ PAY_FLAG_CCQ */
public void setPAY_FLAG_CCQ (String PAY_FLAG_CCQ)
{
if (PAY_FLAG_CCQ != null && PAY_FLAG_CCQ.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_FLAG_CCQ = PAY_FLAG_CCQ.substring(0,199);
}
set_Value ("PAY_FLAG_CCQ", PAY_FLAG_CCQ);
}
/** Get PAY_FLAG_CCQ.
@return PAY_FLAG_CCQ */
public String getPAY_FLAG_CCQ() 
{
return (String)get_Value("PAY_FLAG_CCQ");
}
/** Set PAY_FON_CODE.
@param PAY_FON_CODE PAY_FON_CODE */
public void setPAY_FON_CODE (String PAY_FON_CODE)
{
if (PAY_FON_CODE != null && PAY_FON_CODE.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_FON_CODE = PAY_FON_CODE.substring(0,199);
}
set_Value ("PAY_FON_CODE", PAY_FON_CODE);
}
/** Get PAY_FON_CODE.
@return PAY_FON_CODE */
public String getPAY_FON_CODE() 
{
return (String)get_Value("PAY_FON_CODE");
}
/** Set PAY_POS_NO.
@param PAY_POS_NO PAY_POS_NO */
public void setPAY_POS_NO (String PAY_POS_NO)
{
if (PAY_POS_NO != null && PAY_POS_NO.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_POS_NO = PAY_POS_NO.substring(0,199);
}
set_Value ("PAY_POS_NO", PAY_POS_NO);
}
/** Get PAY_POS_NO.
@return PAY_POS_NO */
public String getPAY_POS_NO() 
{
return (String)get_Value("PAY_POS_NO");
}
/** Set PAY_ST_CIV.
@param PAY_ST_CIV PAY_ST_CIV */
public void setPAY_ST_CIV (String PAY_ST_CIV)
{
if (PAY_ST_CIV != null && PAY_ST_CIV.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_ST_CIV = PAY_ST_CIV.substring(0,199);
}
set_Value ("PAY_ST_CIV", PAY_ST_CIV);
}
/** Get PAY_ST_CIV.
@return PAY_ST_CIV */
public String getPAY_ST_CIV() 
{
return (String)get_Value("PAY_ST_CIV");
}
/** Set PAY_STE_CODE.
@param PAY_STE_CODE PAY_STE_CODE */
public void setPAY_STE_CODE (String PAY_STE_CODE)
{
if (PAY_STE_CODE != null && PAY_STE_CODE.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_STE_CODE = PAY_STE_CODE.substring(0,199);
}
set_Value ("PAY_STE_CODE", PAY_STE_CODE);
}
/** Get PAY_STE_CODE.
@return PAY_STE_CODE */
public String getPAY_STE_CODE() 
{
return (String)get_Value("PAY_STE_CODE");
}
/** Set PAY_SYN_NO.
@param PAY_SYN_NO PAY_SYN_NO */
public void setPAY_SYN_NO (String PAY_SYN_NO)
{
if (PAY_SYN_NO != null && PAY_SYN_NO.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_SYN_NO = PAY_SYN_NO.substring(0,199);
}
set_Value ("PAY_SYN_NO", PAY_SYN_NO);
}
/** Get PAY_SYN_NO.
@return PAY_SYN_NO */
public String getPAY_SYN_NO() 
{
return (String)get_Value("PAY_SYN_NO");
}
/** Set PAY_TYPE_SAL.
@param PAY_TYPE_SAL PAY_TYPE_SAL */
public void setPAY_TYPE_SAL (String PAY_TYPE_SAL)
{
if (PAY_TYPE_SAL != null && PAY_TYPE_SAL.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_TYPE_SAL = PAY_TYPE_SAL.substring(0,199);
}
set_Value ("PAY_TYPE_SAL", PAY_TYPE_SAL);
}
/** Get PAY_TYPE_SAL.
@return PAY_TYPE_SAL */
public String getPAY_TYPE_SAL() 
{
return (String)get_Value("PAY_TYPE_SAL");
}
/** Set PAY_UNA_NO.
@param PAY_UNA_NO PAY_UNA_NO */
public void setPAY_UNA_NO (String PAY_UNA_NO)
{
if (PAY_UNA_NO != null && PAY_UNA_NO.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_UNA_NO = PAY_UNA_NO.substring(0,199);
}
set_Value ("PAY_UNA_NO", PAY_UNA_NO);
}
/** Get PAY_UNA_NO.
@return PAY_UNA_NO */
public String getPAY_UNA_NO() 
{
return (String)get_Value("PAY_UNA_NO");
}
/** Set PAY_UNP_CODE.
@param PAY_UNP_CODE PAY_UNP_CODE */
public void setPAY_UNP_CODE (String PAY_UNP_CODE)
{
if (PAY_UNP_CODE != null && PAY_UNP_CODE.length() > 200)
{
log.warning("Length > 200 - truncated");
PAY_UNP_CODE = PAY_UNP_CODE.substring(0,199);
}
set_Value ("PAY_UNP_CODE", PAY_UNP_CODE);
}
/** Get PAY_UNP_CODE.
@return PAY_UNP_CODE */
public String getPAY_UNP_CODE() 
{
return (String)get_Value("PAY_UNP_CODE");
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
return test == null || test.equals("C") || test.equals("D");
}
/** Set Payment Type.
@param PaymentType Payment Type */
public void setPaymentType (String PaymentType)
{
if (!isPaymentTypeValid(PaymentType))
throw new IllegalArgumentException ("PaymentType Invalid value - " + PaymentType + " - Reference_ID=2000055 - C - D");
if (PaymentType != null && PaymentType.length() > 1)
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
/** Set Processed.
@param Processed The document has been processed */
public void setProcessed (boolean Processed)
{
set_Value ("Processed", new Boolean(Processed));
}
/** Get Processed.
@return The document has been processed */
public boolean isProcessed() 
{
Object oo = get_Value("Processed");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Process Now.
@param Processing Process Now */
public void setProcessing (boolean Processing)
{
set_Value ("Processing", new Boolean(Processing));
}
/** Get Process Now.
@return Process Now */
public boolean isProcessing() 
{
Object oo = get_Value("Processing");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set PROJET.
@param PROJET PROJET */
public void setPROJET (String PROJET)
{
if (PROJET != null && PROJET.length() > 200)
{
log.warning("Length > 200 - truncated");
PROJET = PROJET.substring(0,199);
}
set_Value ("PROJET", PROJET);
}
/** Get PROJET.
@return PROJET */
public String getPROJET() 
{
return (String)get_Value("PROJET");
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
/** Set SPK_NO.
@param SPK_NO SPK_NO */
public void setSPK_NO (String SPK_NO)
{
if (SPK_NO != null && SPK_NO.length() > 200)
{
log.warning("Length > 200 - truncated");
SPK_NO = SPK_NO.substring(0,199);
}
set_Value ("SPK_NO", SPK_NO);
}
/** Get SPK_NO.
@return SPK_NO */
public String getSPK_NO() 
{
return (String)get_Value("SPK_NO");
}
/** Set Surname.
@param Surname Surname */
public void setSurname (String Surname)
{
if (Surname != null && Surname.length() > 30)
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
set_Value ("Weekly_Hours", Weekly_Hours);
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
