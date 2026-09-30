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
/** Generated Model for P_Statement_Earning
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Statement_Earning extends PO
{
/** Standard Constructor
@param ctx context
@param P_Statement_Earning_ID id
@param trxName transaction
*/
public X_P_Statement_Earning (Properties ctx, int P_Statement_Earning_ID, String trxName)
{
super (ctx, P_Statement_Earning_ID, trxName);
/** if (P_Statement_Earning_ID == 0)
{
setP_Employee_ID (0);
setP_Payment_ID (0);
setP_Period_ID (0);
setP_Statement_Earning_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Statement_Earning (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100730 */
public static final int Table_ID=2100730;

/** TableName=P_Statement_Earning */
public static final String Table_Name="P_Statement_Earning";

protected static KeyNamePair Model = new KeyNamePair(2100730,"P_Statement_Earning");

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
StringBuffer sb = new StringBuffer ("X_P_Statement_Earning[").append(get_ID()).append("]");
return sb.toString();
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
/** Set Deduction Cumulative.
@param DeductionCum Deduction Cumulative */
public void setDeductionCum (BigDecimal DeductionCum)
{
set_Value ("DeductionCum", DeductionCum);
}
/** Get Deduction Cumulative.
@return Deduction Cumulative */
public BigDecimal getDeductionCum() 
{
BigDecimal bd = (BigDecimal)get_Value("DeductionCum");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Deduction Period.
@param DeductionPeriod Deduction Period */
public void setDeductionPeriod (BigDecimal DeductionPeriod)
{
set_Value ("DeductionPeriod", DeductionPeriod);
}
/** Get Deduction Period.
@return Deduction Period */
public BigDecimal getDeductionPeriod() 
{
BigDecimal bd = (BigDecimal)get_Value("DeductionPeriod");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Federal Taxation Credit.
@param FederalTaxationCredit Federal Taxation Credit */
public void setFederalTaxationCredit (BigDecimal FederalTaxationCredit)
{
set_Value ("FederalTaxationCredit", FederalTaxationCredit);
}
/** Get Federal Taxation Credit.
@return Federal Taxation Credit */
public BigDecimal getFederalTaxationCredit() 
{
BigDecimal bd = (BigDecimal)get_Value("FederalTaxationCredit");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Federal Taxation Reduction.
@param FederalTaxationReduction Federal Taxation Reduction */
public void setFederalTaxationReduction (BigDecimal FederalTaxationReduction)
{
set_Value ("FederalTaxationReduction", FederalTaxationReduction);
}
/** Get Federal Taxation Reduction.
@return Federal Taxation Reduction */
public BigDecimal getFederalTaxationReduction() 
{
BigDecimal bd = (BigDecimal)get_Value("FederalTaxationReduction");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gross earnings cumulative.
@param GrossEarningsCum Gross earnings cumulative */
public void setGrossEarningsCum (BigDecimal GrossEarningsCum)
{
set_Value ("GrossEarningsCum", GrossEarningsCum);
}
/** Get Gross earnings cumulative.
@return Gross earnings cumulative */
public BigDecimal getGrossEarningsCum() 
{
BigDecimal bd = (BigDecimal)get_Value("GrossEarningsCum");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gross EarningsPeriod.
@param GrossEarningsPeriod Gross EarningsPeriod */
public void setGrossEarningsPeriod (BigDecimal GrossEarningsPeriod)
{
set_Value ("GrossEarningsPeriod", GrossEarningsPeriod);
}
/** Get Gross EarningsPeriod.
@return Gross EarningsPeriod */
public BigDecimal getGrossEarningsPeriod() 
{
BigDecimal bd = (BigDecimal)get_Value("GrossEarningsPeriod");
if (bd == null) return Env.ZERO;
return bd;
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
/** Set Not Paid Hour Cumulative.
@param NotPaidHourCum Not Paid Hour Cumulative */
public void setNotPaidHourCum (BigDecimal NotPaidHourCum)
{
set_Value ("NotPaidHourCum", NotPaidHourCum);
}
/** Get Not Paid Hour Cumulative.
@return Not Paid Hour Cumulative */
public BigDecimal getNotPaidHourCum() 
{
BigDecimal bd = (BigDecimal)get_Value("NotPaidHourCum");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Not Paid Hour Period.
@param NotPaidHourPer Not Paid Hour Period */
public void setNotPaidHourPer (BigDecimal NotPaidHourPer)
{
set_Value ("NotPaidHourPer", NotPaidHourPer);
}
/** Get Not Paid Hour Period.
@return Not Paid Hour Period */
public BigDecimal getNotPaidHourPer() 
{
BigDecimal bd = (BigDecimal)get_Value("NotPaidHourPer");
if (bd == null) return Env.ZERO;
return bd;
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
/** Set Payment.
@param P_Payment_ID Payment */
public void setP_Payment_ID (int P_Payment_ID)
{
if (P_Payment_ID < 1) throw new IllegalArgumentException ("P_Payment_ID is mandatory.");
set_Value ("P_Payment_ID", new Integer(P_Payment_ID));
}
/** Get Payment.
@return Payment */
public int getP_Payment_ID() 
{
Integer ii = (Integer)get_Value("P_Payment_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Period.
@param P_Period_ID Period of the Calendar   */
public void setP_Period_ID (int P_Period_ID)
{
if (P_Period_ID < 1) throw new IllegalArgumentException ("P_Period_ID is mandatory.");
set_Value ("P_Period_ID", new Integer(P_Period_ID));
}
/** Get Period.
@return Period of the Calendar   */
public int getP_Period_ID() 
{
Integer ii = (Integer)get_Value("P_Period_ID");
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
/** Set Step.
@param P_Salary_Scale_Detail_ID   */
public void setP_Salary_Scale_Detail_ID (int P_Salary_Scale_Detail_ID)
{
if (P_Salary_Scale_Detail_ID <= 0) set_Value ("P_Salary_Scale_Detail_ID", null);
 else 
set_Value ("P_Salary_Scale_Detail_ID", new Integer(P_Salary_Scale_Detail_ID));
}
/** Get Step.
@return   */
public int getP_Salary_Scale_Detail_ID() 
{
Integer ii = (Integer)get_Value("P_Salary_Scale_Detail_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_statement_earning_id.
@param P_Statement_Earning_ID P_statement_earning_id */
public void setP_Statement_Earning_ID (int P_Statement_Earning_ID)
{
if (P_Statement_Earning_ID < 1) throw new IllegalArgumentException ("P_Statement_Earning_ID is mandatory.");
set_ValueNoCheck ("P_Statement_Earning_ID", new Integer(P_Statement_Earning_ID));
}
/** Get P_statement_earning_id.
@return P_statement_earning_id */
public int getP_Statement_Earning_ID() 
{
Integer ii = (Integer)get_Value("P_Statement_Earning_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Paid Hour Cumulative.
@param PaidHourCum Paid Hour Cumulative */
public void setPaidHourCum (BigDecimal PaidHourCum)
{
set_Value ("PaidHourCum", PaidHourCum);
}
/** Get Paid Hour Cumulative.
@return Paid Hour Cumulative */
public BigDecimal getPaidHourCum() 
{
BigDecimal bd = (BigDecimal)get_Value("PaidHourCum");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Paid Hour Period.
@param PaidHourPer Paid Hour Period */
public void setPaidHourPer (BigDecimal PaidHourPer)
{
set_Value ("PaidHourPer", PaidHourPer);
}
/** Get Paid Hour Period.
@return Paid Hour Period */
public BigDecimal getPaidHourPer() 
{
BigDecimal bd = (BigDecimal)get_Value("PaidHourPer");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Payment date.
@param PayDate Date Payment made */
public void setPayDate (Timestamp PayDate)
{
set_Value ("PayDate", PayDate);
}
/** Get Payment date.
@return Date Payment made */
public Timestamp getPayDate() 
{
return (Timestamp)get_Value("PayDate");
}
/** Set Pay Finishing Date.
@param PayFinishing Pay Finishing Date */
public void setPayFinishing (Timestamp PayFinishing)
{
set_Value ("PayFinishing", PayFinishing);
}
/** Get Pay Finishing Date.
@return Pay Finishing Date */
public Timestamp getPayFinishing() 
{
return (Timestamp)get_Value("PayFinishing");
}
/** Set Preprinted no.
@param PrePrintedNo Preprinted no */
public void setPrePrintedNo (String PrePrintedNo)
{
if (PrePrintedNo != null && PrePrintedNo.length() > 60)
{
log.warning("Length > 60 - truncated");
PrePrintedNo = PrePrintedNo.substring(0,59);
}
set_Value ("PrePrintedNo", PrePrintedNo);
}
/** Get Preprinted no.
@return Preprinted no */
public String getPrePrintedNo() 
{
return (String)get_Value("PrePrintedNo");
}
/** Set Provincial Taxation Credit.
@param ProvincialTaxationCredit Provincial Taxation Credit */
public void setProvincialTaxationCredit (BigDecimal ProvincialTaxationCredit)
{
set_Value ("ProvincialTaxationCredit", ProvincialTaxationCredit);
}
/** Get Provincial Taxation Credit.
@return Provincial Taxation Credit */
public BigDecimal getProvincialTaxationCredit() 
{
BigDecimal bd = (BigDecimal)get_Value("ProvincialTaxationCredit");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Provincial Taxation Reduction.
@param ProvincialTaxationReduction Provincial Taxation Reduction */
public void setProvincialTaxationReduction (BigDecimal ProvincialTaxationReduction)
{
set_Value ("ProvincialTaxationReduction", ProvincialTaxationReduction);
}
/** Get Provincial Taxation Reduction.
@return Provincial Taxation Reduction */
public BigDecimal getProvincialTaxationReduction() 
{
BigDecimal bd = (BigDecimal)get_Value("ProvincialTaxationReduction");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Remuneration Method.
@param Remuneration_Method   */
public void setRemuneration_Method (String Remuneration_Method)
{
if (Remuneration_Method != null && Remuneration_Method.length() > 30)
{
log.warning("Length > 30 - truncated");
Remuneration_Method = Remuneration_Method.substring(0,29);
}
set_Value ("Remuneration_Method", Remuneration_Method);
}
/** Get Remuneration Method.
@return   */
public String getRemuneration_Method() 
{
return (String)get_Value("Remuneration_Method");
}
/** Set Taxable Benefits Cum.
@param TaxableBenefitsCum Taxable Benefits Cum */
public void setTaxableBenefitsCum (BigDecimal TaxableBenefitsCum)
{
set_Value ("TaxableBenefitsCum", TaxableBenefitsCum);
}
/** Get Taxable Benefits Cum.
@return Taxable Benefits Cum */
public BigDecimal getTaxableBenefitsCum() 
{
BigDecimal bd = (BigDecimal)get_Value("TaxableBenefitsCum");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Taxable Benefits Period.
@param TaxableBenefitsPeriod Taxable Benefits Period */
public void setTaxableBenefitsPeriod (BigDecimal TaxableBenefitsPeriod)
{
set_Value ("TaxableBenefitsPeriod", TaxableBenefitsPeriod);
}
/** Get Taxable Benefits Period.
@return Taxable Benefits Period */
public BigDecimal getTaxableBenefitsPeriod() 
{
BigDecimal bd = (BigDecimal)get_Value("TaxableBenefitsPeriod");
if (bd == null) return Env.ZERO;
return bd;
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

}
