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
/** Generated Model for P_Employee_Deduction
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Employee_Deduction extends PO
{
/** Standard Constructor
@param ctx context
@param P_Employee_Deduction_ID id
@param trxName transaction
*/
public X_P_Employee_Deduction (Properties ctx, int P_Employee_Deduction_ID, String trxName)
{
super (ctx, P_Employee_Deduction_ID, trxName);
/** if (P_Employee_Deduction_ID == 0)
{
setEffectIn (new Timestamp(System.currentTimeMillis()));	// @SQL=SELECT StartDate AS DefaultValue FROM P_Period WHERE PeriodStatus='O' 
setExemptionType (null);	// A
setP_Deduction_ID (0);
setP_Employee_Deduction_ID (0);
setP_Employee_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Employee_Deduction (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000077 */
public static final int Table_ID=2000077;

/** TableName=P_Employee_Deduction */
public static final String Table_Name="P_Employee_Deduction";

protected static KeyNamePair Model = new KeyNamePair(2000077,"P_Employee_Deduction");

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
StringBuffer sb = new StringBuffer ("X_P_Employee_Deduction[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Back Payment.
@param BackPayment Back Payment */
public void setBackPayment (BigDecimal BackPayment)
{
set_Value ("BackPayment", BackPayment);
}
/** Get Back Payment.
@return Back Payment */
public BigDecimal getBackPayment() 
{
BigDecimal bd = (BigDecimal)get_Value("BackPayment");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Back Payment by Period.
@param BackPaymentPeriod Back Payment by Period */
public void setBackPaymentPeriod (BigDecimal BackPaymentPeriod)
{
set_Value ("BackPaymentPeriod", BackPaymentPeriod);
}
/** Get Back Payment by Period.
@return Back Payment by Period */
public BigDecimal getBackPaymentPeriod() 
{
BigDecimal bd = (BigDecimal)get_Value("BackPaymentPeriod");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Back Payment Rate.
@param BackPaymentRate Back Payment Rate */
public void setBackPaymentRate (BigDecimal BackPaymentRate)
{
set_Value ("BackPaymentRate", BackPaymentRate);
}
/** Get Back Payment Rate.
@return Back Payment Rate */
public BigDecimal getBackPaymentRate() 
{
BigDecimal bd = (BigDecimal)get_Value("BackPaymentRate");
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
/** Set Date 35 Year.
@param Date35 Date ou l'employé atteint 35 ans d'année de service */
public void setDate35 (Timestamp Date35)
{
set_Value ("Date35", Date35);
}
/** Get Date 35 Year.
@return Date ou l'employé atteint 35 ans d'année de service */
public Timestamp getDate35() 
{
return (Timestamp)get_Value("Date35");
}
/** Set Effect In.
@param EffectIn Date when information in effect  */
public void setEffectIn (Timestamp EffectIn)
{
if (EffectIn == null) throw new IllegalArgumentException ("EffectIn is mandatory.");
set_Value ("EffectIn", EffectIn);
}
/** Get Effect In.
@return Date when information in effect  */
public Timestamp getEffectIn() 
{
return (Timestamp)get_Value("EffectIn");
}
/** Set To.
@param EffectTo To */
public void setEffectTo (Timestamp EffectTo)
{
set_Value ("EffectTo", EffectTo);
}
/** Get To.
@return To */
public Timestamp getEffectTo() 
{
return (Timestamp)get_Value("EffectTo");
}
/** Set Status.
@param Employee_Status Status of the Employee */
public void setEmployee_Status (boolean Employee_Status)
{
throw new IllegalArgumentException ("Employee_Status is virtual column");
}
/** Get Status.
@return Status of the Employee */
public boolean isEmployee_Status() 
{
Object oo = get_Value("Employee_Status");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Employee amount.
@param EmployeeAmount Employee amount */
public void setEmployeeAmount (BigDecimal EmployeeAmount)
{
set_Value ("EmployeeAmount", EmployeeAmount);
}
/** Get Employee amount.
@return Employee amount */
public BigDecimal getEmployeeAmount() 
{
BigDecimal bd = (BigDecimal)get_Value("EmployeeAmount");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Employee rate.
@param EmployeeRate Employee rate */
public void setEmployeeRate (BigDecimal EmployeeRate)
{
set_Value ("EmployeeRate", EmployeeRate);
}
/** Get Employee rate.
@return Employee rate */
public BigDecimal getEmployeeRate() 
{
BigDecimal bd = (BigDecimal)get_Value("EmployeeRate");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Employer amount.
@param EmployerAmount Employer amount */
public void setEmployerAmount (BigDecimal EmployerAmount)
{
set_Value ("EmployerAmount", EmployerAmount);
}
/** Get Employer amount.
@return Employer amount */
public BigDecimal getEmployerAmount() 
{
BigDecimal bd = (BigDecimal)get_Value("EmployerAmount");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Employer rate.
@param EmployerRate Employer rate */
public void setEmployerRate (BigDecimal EmployerRate)
{
set_Value ("EmployerRate", EmployerRate);
}
/** Get Employer rate.
@return Employer rate */
public BigDecimal getEmployerRate() 
{
BigDecimal bd = (BigDecimal)get_Value("EmployerRate");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Exemption Amount.
@param ExemptionAmount Exemption Amount */
public void setExemptionAmount (BigDecimal ExemptionAmount)
{
set_Value ("ExemptionAmount", ExemptionAmount);
}
/** Get Exemption Amount.
@return Exemption Amount */
public BigDecimal getExemptionAmount() 
{
BigDecimal bd = (BigDecimal)get_Value("ExemptionAmount");
if (bd == null) return Env.ZERO;
return bd;
}

/** ExemptionType AD_Reference_ID=2100455 */
public static final int EXEMPTIONTYPE_AD_Reference_ID=2100455;
/** Annual = A */
public static final String EXEMPTIONTYPE_Annual = "A";
/** Periodical = P */
public static final String EXEMPTIONTYPE_Periodical = "P";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isExemptionTypeValid (String test)
{
return test.equals("A") || test.equals("P");
}
/** Set ExemptionType.
@param ExemptionType ExemptionType */
public void setExemptionType (String ExemptionType)
{
if (ExemptionType == null) throw new IllegalArgumentException ("ExemptionType is mandatory");
if (!isExemptionTypeValid(ExemptionType))
throw new IllegalArgumentException ("ExemptionType Invalid value - " + ExemptionType + " - Reference_ID=2100455 - A - P");
if (ExemptionType.length() > 1)
{
log.warning("Length > 1 - truncated");
ExemptionType = ExemptionType.substring(0,0);
}
set_Value ("ExemptionType", ExemptionType);
}
/** Get ExemptionType.
@return ExemptionType */
public String getExemptionType() 
{
return (String)get_Value("ExemptionType");
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

/** InsuranceParticipant AD_Reference_ID=2000040 */
public static final int INSURANCEPARTICIPANT_AD_Reference_ID=2000040;
/** Salaried = 1 */
public static final String INSURANCEPARTICIPANT_Salaried = "1";
/** Conjoint = 2 */
public static final String INSURANCEPARTICIPANT_Conjoint = "2";
/** Child = 3 */
public static final String INSURANCEPARTICIPANT_Child = "3";
/** Familly = 4 */
public static final String INSURANCEPARTICIPANT_Familly = "4";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isInsuranceParticipantValid (String test)
{
return test == null || test.equals("1") || test.equals("2") || test.equals("3") || test.equals("4");
}
/** Set Participant.
@param InsuranceParticipant Participant */
public void setInsuranceParticipant (String InsuranceParticipant)
{
if (!isInsuranceParticipantValid(InsuranceParticipant))
throw new IllegalArgumentException ("InsuranceParticipant Invalid value - " + InsuranceParticipant + " - Reference_ID=2000040 - 1 - 2 - 3 - 4");
if (InsuranceParticipant != null && InsuranceParticipant.length() > 1)
{
log.warning("Length > 1 - truncated");
InsuranceParticipant = InsuranceParticipant.substring(0,0);
}
set_Value ("InsuranceParticipant", InsuranceParticipant);
}
/** Get Participant.
@return Participant */
public String getInsuranceParticipant() 
{
return (String)get_Value("InsuranceParticipant");
}
/** Set Insurance Package.
@param IsBaseInsurance Insurance Package */
public void setIsBaseInsurance (boolean IsBaseInsurance)
{
throw new IllegalArgumentException ("IsBaseInsurance is virtual column");
}
/** Get Insurance Package.
@return Insurance Package */
public boolean isBaseInsurance() 
{
Object oo = get_Value("IsBaseInsurance");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Exonerated.
@param IsExonerated Exonerated */
public void setIsExonerated (boolean IsExonerated)
{
set_Value ("IsExonerated", new Boolean(IsExonerated));
}
/** Get Exonerated.
@return Exonerated */
public boolean isExonerated() 
{
Object oo = get_Value("IsExonerated");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Smoker.
@param IsSmoker Smoker */
public void setIsSmoker (boolean IsSmoker)
{
set_Value ("IsSmoker", new Boolean(IsSmoker));
}
/** Get Smoker.
@return Smoker */
public boolean isSmoker() 
{
Object oo = get_Value("IsSmoker");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Max Yearly.
@param MaxYearly Max Yearly */
public void setMaxYearly (BigDecimal MaxYearly)
{
set_Value ("MaxYearly", MaxYearly);
}
/** Get Max Yearly.
@return Max Yearly */
public BigDecimal getMaxYearly() 
{
BigDecimal bd = (BigDecimal)get_Value("MaxYearly");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Max Yearly Employer.
@param MaxYearlyYeur Max Yearly Employer */
public void setMaxYearlyYeur (BigDecimal MaxYearlyYeur)
{
set_Value ("MaxYearlyYeur", MaxYearlyYeur);
}
/** Get Max Yearly Employer.
@return Max Yearly Employer */
public BigDecimal getMaxYearlyYeur() 
{
BigDecimal bd = (BigDecimal)get_Value("MaxYearlyYeur");
if (bd == null) return Env.ZERO;
return bd;
}

/** MethodRecoveryBackPayment AD_Reference_ID=2100467 */
public static final int METHODRECOVERYBACKPAYMENT_AD_Reference_ID=2100467;
/** Percentage = 1 */
public static final String METHODRECOVERYBACKPAYMENT_Percentage = "1";
/** Fix Amount = 2 */
public static final String METHODRECOVERYBACKPAYMENT_FixAmount = "2";
/** Total = 3 */
public static final String METHODRECOVERYBACKPAYMENT_Total = "3";
/** Double of the deduction = 4 */
public static final String METHODRECOVERYBACKPAYMENT_DoubleOfTheDeduction = "4";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isMethodRecoveryBackPaymentValid (String test)
{
return test == null || test.equals("1") || test.equals("2") || test.equals("3") || test.equals("4");
}
/** Set Method Recovery Back Payment.
@param MethodRecoveryBackPayment Method Recovery Back Payment */
public void setMethodRecoveryBackPayment (String MethodRecoveryBackPayment)
{
if (!isMethodRecoveryBackPaymentValid(MethodRecoveryBackPayment))
throw new IllegalArgumentException ("MethodRecoveryBackPayment Invalid value - " + MethodRecoveryBackPayment + " - Reference_ID=2100467 - 1 - 2 - 3 - 4");
if (MethodRecoveryBackPayment != null && MethodRecoveryBackPayment.length() > 1)
{
log.warning("Length > 1 - truncated");
MethodRecoveryBackPayment = MethodRecoveryBackPayment.substring(0,0);
}
set_Value ("MethodRecoveryBackPayment", MethodRecoveryBackPayment);
}
/** Get Method Recovery Back Payment.
@return Method Recovery Back Payment */
public String getMethodRecoveryBackPayment() 
{
return (String)get_Value("MethodRecoveryBackPayment");
}
/** Set Multiply Rate.
@param MultiplyRate Rate to multiple the source by to calculate the target. */
public void setMultiplyRate (BigDecimal MultiplyRate)
{
set_Value ("MultiplyRate", MultiplyRate);
}
/** Get Multiply Rate.
@return Rate to multiple the source by to calculate the target. */
public BigDecimal getMultiplyRate() 
{
BigDecimal bd = (BigDecimal)get_Value("MultiplyRate");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Non Annual Maximum.
@param NonAnnualMaximum All time maximum */
public void setNonAnnualMaximum (BigDecimal NonAnnualMaximum)
{
set_Value ("NonAnnualMaximum", NonAnnualMaximum);
}
/** Get Non Annual Maximum.
@return All time maximum */
public BigDecimal getNonAnnualMaximum() 
{
BigDecimal bd = (BigDecimal)get_Value("NonAnnualMaximum");
if (bd == null) return Env.ZERO;
return bd;
}

/** P_Deduction_ID AD_Reference_ID=2000039 */
public static final int P_DEDUCTION_ID_AD_Reference_ID=2000039;
/** Set Deduction.
@param P_Deduction_ID   */
public void setP_Deduction_ID (int P_Deduction_ID)
{
if (P_Deduction_ID < 1) throw new IllegalArgumentException ("P_Deduction_ID is mandatory.");
set_ValueNoCheck ("P_Deduction_ID", new Integer(P_Deduction_ID));
}
/** Get Deduction.
@return   */
public int getP_Deduction_ID() 
{
Integer ii = (Integer)get_Value("P_Deduction_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Deduction.
@param P_Employee_Deduction_ID Deduction */
public void setP_Employee_Deduction_ID (int P_Employee_Deduction_ID)
{
if (P_Employee_Deduction_ID < 1) throw new IllegalArgumentException ("P_Employee_Deduction_ID is mandatory.");
set_Value ("P_Employee_Deduction_ID", new Integer(P_Employee_Deduction_ID));
}
/** Get Deduction.
@return Deduction */
public int getP_Employee_Deduction_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Deduction_ID");
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
set_ValueNoCheck ("P_Employee_ID", new Integer(P_Employee_ID));
}
/** Get Employee .
@return Employee */
public int getP_Employee_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Method_Deduction_ID AD_Reference_ID=2000043 */
public static final int P_METHOD_DEDUCTION_ID_AD_Reference_ID=2000043;
/** Set Method.
@param P_Method_Deduction_ID Method */
public void setP_Method_Deduction_ID (int P_Method_Deduction_ID)
{
throw new IllegalArgumentException ("P_Method_Deduction_ID is virtual column");
}
/** Get Method.
@return Method */
public int getP_Method_Deduction_ID() 
{
Integer ii = (Integer)get_Value("P_Method_Deduction_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Participant Name.
@param Participant_Name Participant Name */
public void setParticipant_Name (String Participant_Name)
{
if (Participant_Name != null && Participant_Name.length() > 60)
{
log.warning("Length > 60 - truncated");
Participant_Name = Participant_Name.substring(0,59);
}
set_Value ("Participant_Name", Participant_Name);
}
/** Get Participant Name.
@return Participant Name */
public String getParticipant_Name() 
{
return (String)get_Value("Participant_Name");
}
/** Set Amount protection.
@param ProtectionAmount Amount protection */
public void setProtectionAmount (BigDecimal ProtectionAmount)
{
set_Value ("ProtectionAmount", ProtectionAmount);
}
/** Get Amount protection.
@return Amount protection */
public BigDecimal getProtectionAmount() 
{
BigDecimal bd = (BigDecimal)get_Value("ProtectionAmount");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Salary Eligible.
@param Salary_Eligible Salary Eligible */
public void setSalary_Eligible (BigDecimal Salary_Eligible)
{
set_Value ("Salary_Eligible", Salary_Eligible);
}
/** Get Salary Eligible.
@return Salary Eligible */
public BigDecimal getSalary_Eligible() 
{
BigDecimal bd = (BigDecimal)get_Value("Salary_Eligible");
if (bd == null) return Env.ZERO;
return bd;
}

/** TypeRate AD_Reference_ID=2200485 */
public static final int TYPERATE_AD_Reference_ID=2200485;
/** Individual = 1 */
public static final String TYPERATE_Individual = "1";
/** Single parent = 2 */
public static final String TYPERATE_SingleParent = "2";
/** Family = 3 */
public static final String TYPERATE_Family = "3";
/** Couple = 4 */
public static final String TYPERATE_Couple = "4";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isTypeRateValid (String test)
{
return test == null || test.equals("1") || test.equals("2") || test.equals("3") || test.equals("4");
}
/** Set Type Rate.
@param TypeRate Type Rate */
public void setTypeRate (String TypeRate)
{
if (!isTypeRateValid(TypeRate))
throw new IllegalArgumentException ("TypeRate Invalid value - " + TypeRate + " - Reference_ID=2200485 - 1 - 2 - 3 - 4");
if (TypeRate != null && TypeRate.length() > 1)
{
log.warning("Length > 1 - truncated");
TypeRate = TypeRate.substring(0,0);
}
set_Value ("TypeRate", TypeRate);
}
/** Get Type Rate.
@return Type Rate */
public String getTypeRate() 
{
return (String)get_Value("TypeRate");
}
}
