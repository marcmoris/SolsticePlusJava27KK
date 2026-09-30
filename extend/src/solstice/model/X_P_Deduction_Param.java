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
/** Generated Model for P_Deduction_Param
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Deduction_Param extends PO
{
/** Standard Constructor
@param ctx context
@param P_Deduction_Param_ID id
@param trxName transaction
*/
public X_P_Deduction_Param (Properties ctx, int P_Deduction_Param_ID, String trxName)
{
super (ctx, P_Deduction_Param_ID, trxName);
/** if (P_Deduction_Param_ID == 0)
{
setEffectIn (new Timestamp(System.currentTimeMillis()));
setExemptionType (null);	// A
setMethod_Rounding (null);	// N
setP_Deduction_ID (0);
setP_Deduction_Param_ID (0);
setSalaryType (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Deduction_Param (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100661 */
public static final int Table_ID=2100661;

/** TableName=P_Deduction_Param */
public static final String Table_Name="P_Deduction_Param";

protected static KeyNamePair Model = new KeyNamePair(2100661,"P_Deduction_Param");

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
StringBuffer sb = new StringBuffer ("X_P_Deduction_Param[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Administration Fee.
@param AdministrationFee Administration Fee */
public void setAdministrationFee (BigDecimal AdministrationFee)
{
set_Value ("AdministrationFee", AdministrationFee);
}
/** Get Administration Fee.
@return Administration Fee */
public BigDecimal getAdministrationFee() 
{
BigDecimal bd = (BigDecimal)get_Value("AdministrationFee");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Annual Maximum employee.
@param AnnualMaximum Annual Maximum Amount for the employee */
public void setAnnualMaximum (BigDecimal AnnualMaximum)
{
set_Value ("AnnualMaximum", AnnualMaximum);
}
/** Get Annual Maximum employee.
@return Annual Maximum Amount for the employee */
public BigDecimal getAnnualMaximum() 
{
BigDecimal bd = (BigDecimal)get_Value("AnnualMaximum");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Annual Maximum Employer.
@param AnnualMaximumEmployer Annual Maximum Employer */
public void setAnnualMaximumEmployer (BigDecimal AnnualMaximumEmployer)
{
set_Value ("AnnualMaximumEmployer", AnnualMaximumEmployer);
}
/** Get Annual Maximum Employer.
@return Annual Maximum Employer */
public BigDecimal getAnnualMaximumEmployer() 
{
BigDecimal bd = (BigDecimal)get_Value("AnnualMaximumEmployer");
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
/** Set Copy From.
@param CopyFrom Copy From Record */
public void setCopyFrom (boolean CopyFrom)
{
set_Value ("CopyFrom", new Boolean(CopyFrom));
}
/** Get Copy From.
@return Copy From Record */
public boolean isCopyFrom() 
{
Object oo = get_Value("CopyFrom");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Day.
@param DeductionDay Day */
public void setDeductionDay (int DeductionDay)
{
set_Value ("DeductionDay", new Integer(DeductionDay));
}
/** Get Day.
@return Day */
public int getDeductionDay() 
{
Integer ii = (Integer)get_Value("DeductionDay");
if (ii == null) return 0;
return ii.intValue();
}

/** DeductionFrequency AD_Reference_ID=2100410 */
public static final int DEDUCTIONFREQUENCY_AD_Reference_ID=2100410;
/** 1 of the month = 1M */
public static final String DEDUCTIONFREQUENCY_1OfTheMonth = "1M";
/** 15 of the month = 15M */
public static final String DEDUCTIONFREQUENCY_15OfTheMonth = "15M";
/** End of the month = 30M */
public static final String DEDUCTIONFREQUENCY_EndOfTheMonth = "30M";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isDeductionFrequencyValid (String test)
{
return test == null || test.equals("1M") || test.equals("15M") || test.equals("30M");
}
/** Set Frequency.
@param DeductionFrequency Frequency */
public void setDeductionFrequency (String DeductionFrequency)
{
if (!isDeductionFrequencyValid(DeductionFrequency))
throw new IllegalArgumentException ("DeductionFrequency Invalid value - " + DeductionFrequency + " - Reference_ID=2100410 - 1M - 15M - 30M");
if (DeductionFrequency != null && DeductionFrequency.length() > 4)
{
log.warning("Length > 4 - truncated");
DeductionFrequency = DeductionFrequency.substring(0,3);
}
set_Value ("DeductionFrequency", DeductionFrequency);
}
/** Get Frequency.
@return Frequency */
public String getDeductionFrequency() 
{
return (String)get_Value("DeductionFrequency");
}

/** DeductionMonth AD_Reference_ID=2000035 */
public static final int DEDUCTIONMONTH_AD_Reference_ID=2000035;
/** January = 01 */
public static final String DEDUCTIONMONTH_January = "01";
/** February = 02 */
public static final String DEDUCTIONMONTH_February = "02";
/** Mars = 03 */
public static final String DEDUCTIONMONTH_Mars = "03";
/** April = 04 */
public static final String DEDUCTIONMONTH_April = "04";
/** May = 05 */
public static final String DEDUCTIONMONTH_May = "05";
/** June = 06 */
public static final String DEDUCTIONMONTH_June = "06";
/** July = 07 */
public static final String DEDUCTIONMONTH_July = "07";
/** August = 08 */
public static final String DEDUCTIONMONTH_August = "08";
/** September = 09 */
public static final String DEDUCTIONMONTH_September = "09";
/** October = 10 */
public static final String DEDUCTIONMONTH_October = "10";
/** November = 11 */
public static final String DEDUCTIONMONTH_November = "11";
/** December = 12 */
public static final String DEDUCTIONMONTH_December = "12";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isDeductionMonthValid (String test)
{
return test == null || test.equals("01") || test.equals("02") || test.equals("03") || test.equals("04") || test.equals("05") || test.equals("06") || test.equals("07") || test.equals("08") || test.equals("09") || test.equals("10") || test.equals("11") || test.equals("12");
}
/** Set Month.
@param DeductionMonth Month */
public void setDeductionMonth (String DeductionMonth)
{
if (!isDeductionMonthValid(DeductionMonth))
throw new IllegalArgumentException ("DeductionMonth Invalid value - " + DeductionMonth + " - Reference_ID=2000035 - 01 - 02 - 03 - 04 - 05 - 06 - 07 - 08 - 09 - 10 - 11 - 12");
if (DeductionMonth != null && DeductionMonth.length() > 30)
{
log.warning("Length > 30 - truncated");
DeductionMonth = DeductionMonth.substring(0,29);
}
set_Value ("DeductionMonth", DeductionMonth);
}
/** Get Month.
@return Month */
public String getDeductionMonth() 
{
return (String)get_Value("DeductionMonth");
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
/** Set Maximum employer amount period.
@param MaxEmployer Maximum Employer Amount for the period */
public void setMaxEmployer (BigDecimal MaxEmployer)
{
set_Value ("MaxEmployer", MaxEmployer);
}
/** Get Maximum employer amount period.
@return Maximum Employer Amount for the period */
public BigDecimal getMaxEmployer() 
{
BigDecimal bd = (BigDecimal)get_Value("MaxEmployer");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Maximum Age.
@param MaximumAge Maximum Age */
public void setMaximumAge (BigDecimal MaximumAge)
{
set_Value ("MaximumAge", MaximumAge);
}
/** Get Maximum Age.
@return Maximum Age */
public BigDecimal getMaximumAge() 
{
BigDecimal bd = (BigDecimal)get_Value("MaximumAge");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Maximum Allowable Earnings.
@param MaximumAllowableEarnings Maximum Amount for Allowable Earnings  */
public void setMaximumAllowableEarnings (BigDecimal MaximumAllowableEarnings)
{
set_Value ("MaximumAllowableEarnings", MaximumAllowableEarnings);
}
/** Get Maximum Allowable Earnings.
@return Maximum Amount for Allowable Earnings  */
public BigDecimal getMaximumAllowableEarnings() 
{
BigDecimal bd = (BigDecimal)get_Value("MaximumAllowableEarnings");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Maximum Amount To Withhold.
@param MaximumAmountToWithhold Maximum Amount To Withhold */
public void setMaximumAmountToWithhold (BigDecimal MaximumAmountToWithhold)
{
set_Value ("MaximumAmountToWithhold", MaximumAmountToWithhold);
}
/** Get Maximum Amount To Withhold.
@return Maximum Amount To Withhold */
public BigDecimal getMaximumAmountToWithhold() 
{
BigDecimal bd = (BigDecimal)get_Value("MaximumAmountToWithhold");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Maximum Backpayment.
@param MaximumBackpayment Maximum Backpayment allowed */
public void setMaximumBackpayment (BigDecimal MaximumBackpayment)
{
set_Value ("MaximumBackpayment", MaximumBackpayment);
}
/** Get Maximum Backpayment.
@return Maximum Backpayment allowed */
public BigDecimal getMaximumBackpayment() 
{
BigDecimal bd = (BigDecimal)get_Value("MaximumBackpayment");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Maximum Backpayment Accumulated.
@param MaximumBackpaymentAccumulated Maximum Backpayment Accumulated */
public void setMaximumBackpaymentAccumulated (BigDecimal MaximumBackpaymentAccumulated)
{
set_Value ("MaximumBackpaymentAccumulated", MaximumBackpaymentAccumulated);
}
/** Get Maximum Backpayment Accumulated.
@return Maximum Backpayment Accumulated */
public BigDecimal getMaximumBackpaymentAccumulated() 
{
BigDecimal bd = (BigDecimal)get_Value("MaximumBackpaymentAccumulated");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Maximum Employee Period.
@param MaximumEmployee Maximum Employee Period */
public void setMaximumEmployee (BigDecimal MaximumEmployee)
{
set_Value ("MaximumEmployee", MaximumEmployee);
}
/** Get Maximum Employee Period.
@return Maximum Employee Period */
public BigDecimal getMaximumEmployee() 
{
BigDecimal bd = (BigDecimal)get_Value("MaximumEmployee");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Maximum Hours.
@param MaximumHoursAllowable Maximum Hours */
public void setMaximumHoursAllowable (BigDecimal MaximumHoursAllowable)
{
set_Value ("MaximumHoursAllowable", MaximumHoursAllowable);
}
/** Get Maximum Hours.
@return Maximum Hours */
public BigDecimal getMaximumHoursAllowable() 
{
BigDecimal bd = (BigDecimal)get_Value("MaximumHoursAllowable");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Maximum Number of Period.
@param MaximumNumberPeriod Maximum Number of Period */
public void setMaximumNumberPeriod (int MaximumNumberPeriod)
{
set_Value ("MaximumNumberPeriod", new Integer(MaximumNumberPeriod));
}
/** Get Maximum Number of Period.
@return Maximum Number of Period */
public int getMaximumNumberPeriod() 
{
Integer ii = (Integer)get_Value("MaximumNumberPeriod");
if (ii == null) return 0;
return ii.intValue();
}

/** Method_Rounding AD_Reference_ID=2000061 */
public static final int METHOD_ROUNDING_AD_Reference_ID=2000061;
/** No Rounding = N */
public static final String METHOD_ROUNDING_NoRounding = "N";
/** Tausen 1000, 2000, .. = T */
public static final String METHOD_ROUNDING_Tausen10002000 = "T";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isMethod_RoundingValid (String test)
{
return test.equals("N") || test.equals("T");
}
/** Set Method Rounding.
@param Method_Rounding Method Rounding */
public void setMethod_Rounding (String Method_Rounding)
{
if (Method_Rounding == null) throw new IllegalArgumentException ("Method_Rounding is mandatory");
if (!isMethod_RoundingValid(Method_Rounding))
throw new IllegalArgumentException ("Method_Rounding Invalid value - " + Method_Rounding + " - Reference_ID=2000061 - N - T");
if (Method_Rounding.length() > 1)
{
log.warning("Length > 1 - truncated");
Method_Rounding = Method_Rounding.substring(0,0);
}
set_Value ("Method_Rounding", Method_Rounding);
}
/** Get Method Rounding.
@return Method Rounding */
public String getMethod_Rounding() 
{
return (String)get_Value("Method_Rounding");
}

/** MethodRecoveryBackPayment AD_Reference_ID=2100467 */
public static final int METHODRECOVERYBACKPAYMENT_AD_Reference_ID=2100467;
/** Pourcentage = 1 */
public static final String METHODRECOVERYBACKPAYMENT_Pourcentage = "1";
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
/** Set Minimum Amount To Withhold.
@param MinimumAmountToWithhold Minimum Amount To Withhold */
public void setMinimumAmountToWithhold (BigDecimal MinimumAmountToWithhold)
{
set_Value ("MinimumAmountToWithhold", MinimumAmountToWithhold);
}
/** Get Minimum Amount To Withhold.
@return Minimum Amount To Withhold */
public BigDecimal getMinimumAmountToWithhold() 
{
BigDecimal bd = (BigDecimal)get_Value("MinimumAmountToWithhold");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Minimum Hours.
@param MinimumHoursAllowable Minimum Hours */
public void setMinimumHoursAllowable (BigDecimal MinimumHoursAllowable)
{
set_Value ("MinimumHoursAllowable", MinimumHoursAllowable);
}
/** Get Minimum Hours.
@return Minimum Hours */
public BigDecimal getMinimumHoursAllowable() 
{
BigDecimal bd = (BigDecimal)get_Value("MinimumHoursAllowable");
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

/** P_Advance_ID AD_Reference_ID=2000023 */
public static final int P_ADVANCE_ID_AD_Reference_ID=2000023;
/** Set Advance.
@param P_Advance_ID Advance */
public void setP_Advance_ID (int P_Advance_ID)
{
if (P_Advance_ID <= 0) set_Value ("P_Advance_ID", null);
 else 
set_Value ("P_Advance_ID", new Integer(P_Advance_ID));
}
/** Get Advance.
@return Advance */
public int getP_Advance_ID() 
{
Integer ii = (Integer)get_Value("P_Advance_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Column_Adm_ID AD_Reference_ID=2000000 */
public static final int P_COLUMN_ADM_ID_AD_Reference_ID=2000000;
/** Set Admissibility Column.
@param P_Column_Adm_ID Admissibility Column */
public void setP_Column_Adm_ID (int P_Column_Adm_ID)
{
if (P_Column_Adm_ID <= 0) set_Value ("P_Column_Adm_ID", null);
 else 
set_Value ("P_Column_Adm_ID", new Integer(P_Column_Adm_ID));
}
/** Get Admissibility Column.
@return Admissibility Column */
public int getP_Column_Adm_ID() 
{
Integer ii = (Integer)get_Value("P_Column_Adm_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Credits.
@param P_Credits_ID Credits */
public void setP_Credits_ID (int P_Credits_ID)
{
if (P_Credits_ID <= 0) set_Value ("P_Credits_ID", null);
 else 
set_Value ("P_Credits_ID", new Integer(P_Credits_ID));
}
/** Get Credits.
@return Credits */
public int getP_Credits_ID() 
{
Integer ii = (Integer)get_Value("P_Credits_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Deduction.
@param P_Deduction_ID   */
public void setP_Deduction_ID (int P_Deduction_ID)
{
if (P_Deduction_ID < 1) throw new IllegalArgumentException ("P_Deduction_ID is mandatory.");
set_Value ("P_Deduction_ID", new Integer(P_Deduction_ID));
}
/** Get Deduction.
@return   */
public int getP_Deduction_ID() 
{
Integer ii = (Integer)get_Value("P_Deduction_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Get Record ID/ColumnName
@return ID/ColumnName pair */
public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), String.valueOf(getP_Deduction_ID()));
}
/** Set Deduction.
@param P_Deduction_Param_ID Deduction */
public void setP_Deduction_Param_ID (int P_Deduction_Param_ID)
{
if (P_Deduction_Param_ID < 1) throw new IllegalArgumentException ("P_Deduction_Param_ID is mandatory.");
set_ValueNoCheck ("P_Deduction_Param_ID", new Integer(P_Deduction_Param_ID));
}
/** Get Deduction.
@return Deduction */
public int getP_Deduction_Param_ID() 
{
Integer ii = (Integer)get_Value("P_Deduction_Param_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Employer_ID AD_Reference_ID=2000042 */
public static final int P_EMPLOYER_ID_AD_Reference_ID=2000042;
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
/** Set Method.
@param P_Method_Deduction_ID Method */
public void setP_Method_Deduction_ID (int P_Method_Deduction_ID)
{
if (P_Method_Deduction_ID <= 0) set_Value ("P_Method_Deduction_ID", null);
 else 
set_Value ("P_Method_Deduction_ID", new Integer(P_Method_Deduction_ID));
}
/** Get Method.
@return Method */
public int getP_Method_Deduction_ID() 
{
Integer ii = (Integer)get_Value("P_Method_Deduction_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_RWT_ID AD_Reference_ID=2100369 */
public static final int P_RWT_ID_AD_Reference_ID=2100369;
/** Set No. Regime.
@param P_RWT_ID No. Regime */
public void setP_RWT_ID (int P_RWT_ID)
{
if (P_RWT_ID <= 0) set_Value ("P_RWT_ID", null);
 else 
set_Value ("P_RWT_ID", new Integer(P_RWT_ID));
}
/** Get No. Regime.
@return No. Regime */
public int getP_RWT_ID() 
{
Integer ii = (Integer)get_Value("P_RWT_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** SalaryType AD_Reference_ID=2000060 */
public static final int SALARYTYPE_AD_Reference_ID=2000060;
/** 1 = 1 */
public static final String SALARYTYPE_1 = "1";
/** 2 = 2 */
public static final String SALARYTYPE_2 = "2";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isSalaryTypeValid (String test)
{
return test.equals("1") || test.equals("2");
}
/** Set Type.
@param SalaryType Type */
public void setSalaryType (String SalaryType)
{
if (SalaryType == null) throw new IllegalArgumentException ("SalaryType is mandatory");
if (!isSalaryTypeValid(SalaryType))
throw new IllegalArgumentException ("SalaryType Invalid value - " + SalaryType + " - Reference_ID=2000060 - 1 - 2");
if (SalaryType.length() > 1)
{
log.warning("Length > 1 - truncated");
SalaryType = SalaryType.substring(0,0);
}
set_Value ("SalaryType", SalaryType);
}
/** Get Type.
@return Type */
public String getSalaryType() 
{
return (String)get_Value("SalaryType");
}
/** Set Taxe Rate Quebec.
@param TaxeRate Taxe Rate Quebec */
public void setTaxeRate (BigDecimal TaxeRate)
{
set_Value ("TaxeRate", TaxeRate);
}
/** Get Taxe Rate Quebec.
@return Taxe Rate Quebec */
public BigDecimal getTaxeRate() 
{
BigDecimal bd = (BigDecimal)get_Value("TaxeRate");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Taxe Rate Manitoba.
@param TaxeRateManitoba Taxe Rate Manitoba */
public void setTaxeRateManitoba (BigDecimal TaxeRateManitoba)
{
set_Value ("TaxeRateManitoba", TaxeRateManitoba);
}
/** Get Taxe Rate Manitoba.
@return Taxe Rate Manitoba */
public BigDecimal getTaxeRateManitoba() 
{
BigDecimal bd = (BigDecimal)get_Value("TaxeRateManitoba");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Taxe Rate Ontario.
@param TaxeRateOntario Taxe Rate Ontario */
public void setTaxeRateOntario (BigDecimal TaxeRateOntario)
{
set_Value ("TaxeRateOntario", TaxeRateOntario);
}
/** Get Taxe Rate Ontario.
@return Taxe Rate Ontario */
public BigDecimal getTaxeRateOntario() 
{
BigDecimal bd = (BigDecimal)get_Value("TaxeRateOntario");
if (bd == null) return Env.ZERO;
return bd;
}

/** Set UnionDuesFixedAmt.
@param UnionDuesFixedAmt */
public void setUnionDuesFixedAmt (BigDecimal UnionDuesFixedAmt)
{
set_Value ("UnionDuesFixedAmt", UnionDuesFixedAmt);
}
/** Get UnionDuesFixedAmt.
@return UnionDuesFixedAmt */
public BigDecimal getUnionDuesFixedAmt() 
{
BigDecimal bd = (BigDecimal)get_Value("UnionDuesFixedAmt");
if (bd == null) return Env.ZERO;
return bd;
}


}


