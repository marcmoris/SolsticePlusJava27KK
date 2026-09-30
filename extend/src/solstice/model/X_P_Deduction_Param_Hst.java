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
/** Generated Model for P_Deduction_Param_Hst
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Deduction_Param_Hst extends PO
{
/** Standard Constructor
@param ctx context
@param P_Deduction_Param_Hst_ID id
@param trxName transaction
*/
public X_P_Deduction_Param_Hst (Properties ctx, int P_Deduction_Param_Hst_ID, String trxName)
{
super (ctx, P_Deduction_Param_Hst_ID, trxName);
/** if (P_Deduction_Param_Hst_ID == 0)
{
setEffectIn (new Timestamp(System.currentTimeMillis()));
setEmployeeAmount (Env.ZERO);
setEmployeeRate (Env.ZERO);
setEmployerAmount (Env.ZERO);
setEmployerRate (Env.ZERO);
setMethod_Rounding (null);
setP_Advance_ID (0);
setP_Column_Adm_ID (0);
setP_Employer_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Deduction_Param_Hst (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100665 */
public static final int Table_ID=2100665;

/** TableName=P_Deduction_Param_Hst */
public static final String Table_Name="P_Deduction_Param_Hst";

protected static KeyNamePair Model = new KeyNamePair(2100665,"P_Deduction_Param_Hst");

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
StringBuffer sb = new StringBuffer ("X_P_Deduction_Param_Hst[").append(get_ID()).append("]");
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
/** Set Effect In.
@param EffectIn Date when information in effect  */
public void setEffectIn (Timestamp EffectIn)
{
if (EffectIn == null) throw new IllegalArgumentException ("EffectIn is mandatory.");
set_ValueNoCheck ("EffectIn", EffectIn);
}
/** Get Effect In.
@return Date when information in effect  */
public Timestamp getEffectIn() 
{
return (Timestamp)get_Value("EffectIn");
}
/** Set Employee amount.
@param EmployeeAmount Employee amount */
public void setEmployeeAmount (BigDecimal EmployeeAmount)
{
if (EmployeeAmount == null) throw new IllegalArgumentException ("EmployeeAmount is mandatory.");
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
if (EmployeeRate == null) throw new IllegalArgumentException ("EmployeeRate is mandatory.");
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
if (EmployerAmount == null) throw new IllegalArgumentException ("EmployerAmount is mandatory.");
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
if (EmployerRate == null) throw new IllegalArgumentException ("EmployerRate is mandatory.");
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
if (Method_Rounding.length() > 2)
{
log.warning("Length > 2 - truncated");
Method_Rounding = Method_Rounding.substring(0,1);
}
set_Value ("Method_Rounding", Method_Rounding);
}
/** Get Method Rounding.
@return Method Rounding */
public String getMethod_Rounding() 
{
return (String)get_Value("Method_Rounding");
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
if (P_Advance_ID < 1) throw new IllegalArgumentException ("P_Advance_ID is mandatory.");
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
if (P_Column_Adm_ID < 1) throw new IllegalArgumentException ("P_Column_Adm_ID is mandatory.");
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

/** P_Deduction_ID AD_Reference_ID=2000039 */
public static final int P_DEDUCTION_ID_AD_Reference_ID=2000039;
/** Set Deduction.
@param P_Deduction_ID   */
public void setP_Deduction_ID (int P_Deduction_ID)
{
if (P_Deduction_ID <= 0) set_Value ("P_Deduction_ID", null);
 else 
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
/** Set P_deduction_param_hst_ID.
@param P_Deduction_Param_Hst_ID P_deduction_param_hst_ID */
public void setP_Deduction_Param_Hst_ID (int P_Deduction_Param_Hst_ID)
{
if (P_Deduction_Param_Hst_ID <= 0) set_Value ("P_Deduction_Param_Hst_ID", null);
 else 
set_Value ("P_Deduction_Param_Hst_ID", new Integer(P_Deduction_Param_Hst_ID));
}
/** Get P_deduction_param_hst_ID.
@return P_deduction_param_hst_ID */
public int getP_Deduction_Param_Hst_ID() 
{
Integer ii = (Integer)get_Value("P_Deduction_Param_Hst_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Deduction.
@param P_Deduction_Param_ID Deduction */
public void setP_Deduction_Param_ID (int P_Deduction_Param_ID)
{
if (P_Deduction_Param_ID <= 0) set_Value ("P_Deduction_Param_ID", null);
 else 
set_Value ("P_Deduction_Param_ID", new Integer(P_Deduction_Param_ID));
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
return test == null || test.equals("1") || test.equals("2");
}
/** Set Type.
@param SalaryType Type */
public void setSalaryType (String SalaryType)
{
if (!isSalaryTypeValid(SalaryType))
throw new IllegalArgumentException ("SalaryType Invalid value - " + SalaryType + " - Reference_ID=2000060 - 1 - 2");
if (SalaryType != null && SalaryType.length() > 2)
{
log.warning("Length > 2 - truncated");
SalaryType = SalaryType.substring(0,1);
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
}
