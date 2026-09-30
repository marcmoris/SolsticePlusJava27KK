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
/** Generated Model for P_Payment
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: X_P_Payment.java,v 1.6 2008/10/29 14:59:44 marmor01 Exp $ */
public class X_P_Payment extends PO
{
/** Standard Constructor
@param ctx context
@param P_Payment_ID id
@param trxName transaction
*/
public X_P_Payment (Properties ctx, int P_Payment_ID, String trxName)
{
super (ctx, P_Payment_ID, trxName);
/** if (P_Payment_ID == 0)
{
setIsError (null);
setP_Employee_ID (0);
setP_Employer_ID (0);
setP_Frequency_ID (0);
setP_Payment_Group_ID (0);
setP_Payment_ID (0);
setP_Period_ID (0);
setP_Year_ID (0);
setTimeSheetStatus (null);	// I
setValue (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Payment (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000092 */
public static final int Table_ID=2000092;

/** TableName=P_Payment */
public static final String Table_Name="P_Payment";

protected static KeyNamePair Model = new KeyNamePair(2000092,"P_Payment");

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
StringBuffer sb = new StringBuffer ("X_P_Payment[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Annual salary.
@param AnnualSalary Annual salary */
public void setAnnualSalary (BigDecimal AnnualSalary)
{
set_ValueNoCheck ("AnnualSalary", AnnualSalary);
}
/** Get Annual salary.
@return Annual salary */
public BigDecimal getAnnualSalary() 
{
BigDecimal bd = (BigDecimal)get_Value("AnnualSalary");
if (bd == null) return Env.ZERO;
return bd;
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
/** Set Date of Transfer.
@param DateTransfer Date of Transfer */
public void setDateTransfer (Timestamp DateTransfer)
{
set_ValueNoCheck ("DateTransfer", DateTransfer);
}
/** Get Date of Transfer.
@return Date of Transfer */
public Timestamp getDateTransfer() 
{
return (Timestamp)get_Value("DateTransfer");
}
/** Set Description.
@param Description Optional short description of the record */
public void setDescription (String Description)
{
if (Description != null && Description.length() > 255)
{
log.warning("Length > 255 - truncated");
Description = Description.substring(0,254);
}
set_Value ("Description", Description);
}
/** Get Description.
@return Optional short description of the record */
public String getDescription() 
{
return (String)get_Value("Description");
}
/** Set Gross Earnings.
@param GrossEarnings Gross Earnings */
public void setGrossEarnings (BigDecimal GrossEarnings)
{
set_ValueNoCheck ("GrossEarnings", GrossEarnings);
}
/** Get Gross Earnings.
@return Gross Earnings */
public BigDecimal getGrossEarnings() 
{
BigDecimal bd = (BigDecimal)get_Value("GrossEarnings");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Cancelled.
@param IsCancelled The transaction was cancelled */
public void setIsCancelled (String IsCancelled)
{
if (IsCancelled != null && IsCancelled.length() > 1)
{
log.warning("Length > 1 - truncated");
IsCancelled = IsCancelled.substring(0,0);
}
set_Value ("IsCancelled", IsCancelled);
}
/** Get Cancelled.
@return The transaction was cancelled */
public String getIsCancelled() 
{
return (String)get_Value("IsCancelled");
}
/** Set Error.
@param IsError An Error occured in the execution */
public void setIsError (String IsError)
{
if (IsError == null) throw new IllegalArgumentException ("IsError is mandatory.");
if (IsError.length() > 2)
{
log.warning("Length > 2 - truncated");
IsError = IsError.substring(0,1);
}
set_Value ("IsError", IsError);
}
/** Get Error.
@return An Error occured in the execution */
public String getIsError() 
{
return (String)get_Value("IsError");
}
/** Set Reconciled.
@param IsReconciled Payment is reconciled with bank statement */
public void setIsReconciled (boolean IsReconciled)
{
set_ValueNoCheck ("IsReconciled", new Boolean(IsReconciled));
}
/** Get Reconciled.
@return Payment is reconciled with bank statement */
public boolean isReconciled() 
{
Object oo = get_Value("IsReconciled");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set IsSelfFinancedLeave.
@param IsSelfFinancedLeave IsSelfFinancedLeave */
public void setIsSelfFinancedLeave (boolean IsSelfFinancedLeave)
{
set_Value ("IsSelfFinancedLeave", new Boolean(IsSelfFinancedLeave));
}
/** Get IsSelfFinancedLeave.
@return IsSelfFinancedLeave */
public boolean isSelfFinancedLeave() 
{
Object oo = get_Value("IsSelfFinancedLeave");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Transferred.
@param IsTransfer Transferred */
public void setIsTransfer (boolean IsTransfer)
{
set_ValueNoCheck ("IsTransfer", new Boolean(IsTransfer));
}
/** Get Transferred.
@return Transferred */
public boolean isTransfer() 
{
Object oo = get_Value("IsTransfer");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Name .
@param Name Alphanumeric identifier of the entity */
public void setName (String Name)
{
if (Name != null && Name.length() > 60)
{
log.warning("Length > 60 - truncated");
Name = Name.substring(0,59);
}
set_Value ("Name", Name);
}
/** Get Name .
@return Alphanumeric identifier of the entity */
public String getName() 
{
return (String)get_Value("Name");
}
/** Set Net Pay.
@param NetPay Net Pay */
public void setNetPay (BigDecimal NetPay)
{
set_ValueNoCheck ("NetPay", NetPay);
}
/** Get Net Pay.
@return Net Pay */
public BigDecimal getNetPay() 
{
BigDecimal bd = (BigDecimal)get_Value("NetPay");
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
/** Set TimeSheets.
@param P_Distribution_Booklet_ID TimeSheets */
public void setP_Distribution_Booklet_ID (int P_Distribution_Booklet_ID)
{
if (P_Distribution_Booklet_ID <= 0) set_Value ("P_Distribution_Booklet_ID", null);
 else 
set_Value ("P_Distribution_Booklet_ID", new Integer(P_Distribution_Booklet_ID));
}
/** Get TimeSheets.
@return TimeSheets */
public int getP_Distribution_Booklet_ID() 
{
Integer ii = (Integer)get_Value("P_Distribution_Booklet_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Distribution_ID AD_Reference_ID=2000053 */
public static final int P_DISTRIBUTION_ID_AD_Reference_ID=2000053;
/** Set Distribution.
@param P_Distribution_ID Distribution */
public void setP_Distribution_ID (int P_Distribution_ID)
{
if (P_Distribution_ID <= 0) set_Value ("P_Distribution_ID", null);
 else 
set_Value ("P_Distribution_ID", new Integer(P_Distribution_ID));
}
/** Get Distribution.
@return Distribution */
public int getP_Distribution_ID() 
{
Integer ii = (Integer)get_Value("P_Distribution_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Employee_ID AD_Reference_ID=2000010 */
public static final int P_EMPLOYEE_ID_AD_Reference_ID=2000010;
/** Set Employee.
@param P_Employee_ID Employee */
public void setP_Employee_ID (int P_Employee_ID)
{
if (P_Employee_ID < 1) throw new IllegalArgumentException ("P_Employee_ID is mandatory.");
set_ValueNoCheck ("P_Employee_ID", new Integer(P_Employee_ID));
}
/** Get Employee.
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

/** P_Frequency_ID AD_Reference_ID=2000007 */
public static final int P_FREQUENCY_ID_AD_Reference_ID=2000007;
/** Set Frequency.
@param P_Frequency_ID Frequency */
public void setP_Frequency_ID (int P_Frequency_ID)
{
if (P_Frequency_ID < 1) throw new IllegalArgumentException ("P_Frequency_ID is mandatory.");
set_Value ("P_Frequency_ID", new Integer(P_Frequency_ID));
}
/** Get Frequency.
@return Frequency */
public int getP_Frequency_ID() 
{
Integer ii = (Integer)get_Value("P_Frequency_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Job_Title_ID AD_Reference_ID=2000025 */
public static final int P_JOB_TITLE_ID_AD_Reference_ID=2000025;
/** Set Position Title.
@param P_Job_Title_ID Position Title */
public void setP_Job_Title_ID (int P_Job_Title_ID)
{
if (P_Job_Title_ID <= 0) set_Value ("P_Job_Title_ID", null);
 else 
set_Value ("P_Job_Title_ID", new Integer(P_Job_Title_ID));
}
/** Get Position Title.
@return Position Title */
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

/** P_Payment_Group_ID AD_Reference_ID=2000056 */
public static final int P_PAYMENT_GROUP_ID_AD_Reference_ID=2000056;
/** Set Payment Group.
@param P_Payment_Group_ID Payment Group */
public void setP_Payment_Group_ID (int P_Payment_Group_ID)
{
if (P_Payment_Group_ID < 1) throw new IllegalArgumentException ("P_Payment_Group_ID is mandatory.");
set_ValueNoCheck ("P_Payment_Group_ID", new Integer(P_Payment_Group_ID));
}
/** Get Payment Group.
@return Payment Group */
public int getP_Payment_Group_ID() 
{
Integer ii = (Integer)get_Value("P_Payment_Group_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Payment.
@param P_Payment_ID Payment */
public void setP_Payment_ID (int P_Payment_ID)
{
if (P_Payment_ID < 1) throw new IllegalArgumentException ("P_Payment_ID is mandatory.");
set_ValueNoCheck ("P_Payment_ID", new Integer(P_Payment_ID));
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
@param P_Period_ID Period */
public void setP_Period_ID (int P_Period_ID)
{
if (P_Period_ID < 1) throw new IllegalArgumentException ("P_Period_ID is mandatory.");
set_Value ("P_Period_ID", new Integer(P_Period_ID));
}
/** Get Period.
@return Period */
public int getP_Period_ID() 
{
Integer ii = (Integer)get_Value("P_Period_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Workplace_ID AD_Reference_ID=2000005 */
public static final int P_WORKPLACE_ID_AD_Reference_ID=2000005;
/** Set Workplace.
@param P_Workplace_ID Workplace */
public void setP_Workplace_ID (int P_Workplace_ID)
{
if (P_Workplace_ID <= 0) set_Value ("P_Workplace_ID", null);
 else 
set_Value ("P_Workplace_ID", new Integer(P_Workplace_ID));
}
/** Get Workplace.
@return Workplace */
public int getP_Workplace_ID() 
{
Integer ii = (Integer)get_Value("P_Workplace_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Year_ID AD_Reference_ID=2000012 */
public static final int P_YEAR_ID_AD_Reference_ID=2000012;
/** Set Calendar Year.
@param P_Year_ID Calendar Year */
public void setP_Year_ID (int P_Year_ID)
{
if (P_Year_ID < 1) throw new IllegalArgumentException ("P_Year_ID is mandatory.");
set_Value ("P_Year_ID", new Integer(P_Year_ID));
}
/** Get Calendar Year.
@return Calendar Year */
public int getP_Year_ID() 
{
Integer ii = (Integer)get_Value("P_Year_ID");
if (ii == null) return 0;
return ii.intValue();
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

/** PaymentType AD_Reference_ID=2000055 */
public static final int PAYMENTTYPE_AD_Reference_ID=2000055;
/** Cheque = C */
public static final String PAYMENTTYPE_Cheque = "C";
/** Deposit = D */
public static final String PAYMENTTYPE_Deposit = "D";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isPaymentTypeValid (String test)
{
return test == null || test.equals("C") || test.equals("D");
}
/** Set Payment Method.
@param PaymentType Payment Method */
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
/** Get Payment Method.
@return Payment Method */
public String getPaymentType() 
{
return (String)get_Value("PaymentType");
}

/** PaymentTypeDoc AD_Reference_ID=2000058 */
public static final int PAYMENTTYPEDOC_AD_Reference_ID=2000058;
/** Regular = Regular */
public static final String PAYMENTTYPEDOC_Regular = "Regular";
/** Adjustement = Adjustement */
public static final String PAYMENTTYPEDOC_Adjustement = "Adjustement";
/** Annulation+ = Annulation+ */
public static final String PAYMENTTYPEDOC_AnnulationPlus = "Annulation+";
/** Annulation- = Annulation- */
public static final String PAYMENTTYPEDOC_Annulation_ = "Annulation-";
/** Advance = Advance */
public static final String PAYMENTTYPEDOC_Advance = "Advance";
/** Complementary = Complementary */
public static final String PAYMENTTYPEDOC_Complementary = "Complementary";
/** Separation pay = Separation */
public static final String PAYMENTTYPEDOC_SeparationPay = "Separation";
/** Expense Account = ExpenseAccount */
public static final String PAYMENTTYPEDOC_ExpenseAccount = "ExpenseAccount";
/** Holiday Payment = HolidayPayment */
public static final String PAYMENTTYPEDOC_HolidayPayment = "HolidayPayment";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isPaymentTypeDocValid (String test)
{
return test == null || test.equals("Regular") || test.equals("Adjustement") || test.equals("Annulation+") || test.equals("Annulation-") || test.equals("Advance") || test.equals("Complementary") || test.equals("Separation") || test.equals("ExpenseAccount") || test.equals("HolidayPayment");
}
/** Set Type doccument.
@param PaymentTypeDoc Type doccument */
public void setPaymentTypeDoc (String PaymentTypeDoc)
{
if (!isPaymentTypeDocValid(PaymentTypeDoc))
throw new IllegalArgumentException ("PaymentTypeDoc Invalid value - " + PaymentTypeDoc + " - Reference_ID=2000058 - Regular - Adjustement - Annulation+ - Annulation- - Advance - Complementary - Separation - ExpenseAccount - HolidayPayment");
if (PaymentTypeDoc != null && PaymentTypeDoc.length() > 30)
{
log.warning("Length > 30 - truncated");
PaymentTypeDoc = PaymentTypeDoc.substring(0,29);
}
set_ValueNoCheck ("PaymentTypeDoc", PaymentTypeDoc);
}
/** Get Type doccument.
@return Type doccument */
public String getPaymentTypeDoc() 
{
return (String)get_Value("PaymentTypeDoc");
}

/** Set Preprinted Number.
@param PrePrintedNo Preprinted Number */
public void setPrePrintedNo (String PrePrintedNo)
{
if (PrePrintedNo != null && PrePrintedNo.length() > 30)
{
log.warning("Length > 30 - truncated");
PrePrintedNo = PrePrintedNo.substring(0,29);
}
set_ValueNoCheck ("PrePrintedNo", PrePrintedNo);
}
/** Get Preprinted Number.
@return Preprinted Number */
public String getPrePrintedNo() 
{
return (String)get_Value("PrePrintedNo");
}
/** Set Reconciliation Bank Date.
@param ReconciliationDate Reconciliation Bank Date */
public void setReconciliationDate (Timestamp ReconciliationDate)
{
set_Value ("ReconciliationDate", ReconciliationDate);
}
/** Get Reconciliation Bank Date.
@return Reconciliation Bank Date */
public Timestamp getReconciliationDate() 
{
return (Timestamp)get_Value("ReconciliationDate");
}
/** Set Deffered Salary %.
@param SalaryPercentage Deffered Salary % */
public void setSalaryPercentage (BigDecimal SalaryPercentage)
{
set_Value ("SalaryPercentage", SalaryPercentage);
}
/** Get Deffered Salary %.
@return Deffered Salary % */
public BigDecimal getSalaryPercentage() 
{
BigDecimal bd = (BigDecimal)get_Value("SalaryPercentage");
if (bd == null) return Env.ZERO;
return bd;
}

/** Taxation_Region_ID AD_Reference_ID=2000026 */
public static final int TAXATION_REGION_ID_AD_Reference_ID=2000026;
/** Set Taxation Region.
@param Taxation_Region_ID Taxation Region */
public void setTaxation_Region_ID (int Taxation_Region_ID)
{
if (Taxation_Region_ID <= 0) set_ValueNoCheck ("Taxation_Region_ID", null);
 else 
set_ValueNoCheck ("Taxation_Region_ID", new Integer(Taxation_Region_ID));
}
/** Get Taxation Region.
@return Taxation Region */
public int getTaxation_Region_ID() 
{
Integer ii = (Integer)get_Value("Taxation_Region_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** TimeSheetStatus AD_Reference_ID=2000078 */
public static final int TIMESHEETSTATUS_AD_Reference_ID=2000078;
/** Initial = I */
public static final String TIMESHEETSTATUS_Initial = "I";
/** Validated = V */
public static final String TIMESHEETSTATUS_Validated = "V";
/** Approved = A */
public static final String TIMESHEETSTATUS_Approved = "A";
/** Calculated = C */
public static final String TIMESHEETSTATUS_Calculated = "C";
/** Issued = E */
public static final String TIMESHEETSTATUS_Issued = "E";
/** Transferred = T */
public static final String TIMESHEETSTATUS_Transferred = "T";
/** Cancelled = Z */
public static final String TIMESHEETSTATUS_Cancelled = "Z";
/** Error = F */
public static final String TIMESHEETSTATUS_Error = "F";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isTimeSheetStatusValid (String test)
{
return test.equals("I") || test.equals("V") || test.equals("A") || test.equals("C") || test.equals("E") || test.equals("T") || test.equals("Z") || test.equals("F");
}
/** Set Status.
@param TimeSheetStatus Status */
public void setTimeSheetStatus (String TimeSheetStatus)
{
if (TimeSheetStatus == null) throw new IllegalArgumentException ("TimeSheetStatus is mandatory");
if (!isTimeSheetStatusValid(TimeSheetStatus))
throw new IllegalArgumentException ("TimeSheetStatus Invalid value - " + TimeSheetStatus + " - Reference_ID=2000078 - I - V - A - C - E - T - Z - F");
if (TimeSheetStatus.length() > 120)
{
log.warning("Length > 120 - truncated");
TimeSheetStatus = TimeSheetStatus.substring(0,119);
}
set_Value ("TimeSheetStatus", TimeSheetStatus);
}
/** Get Status.
@return Status */
public String getTimeSheetStatus() 
{
return (String)get_Value("TimeSheetStatus");
}
/** Set Total Deductions.
@param TotalDeduction Total Deductions */
public void setTotalDeduction (BigDecimal TotalDeduction)
{
set_ValueNoCheck ("TotalDeduction", TotalDeduction);
}
/** Get Total Deductions.
@return Total Deductions */
public BigDecimal getTotalDeduction() 
{
BigDecimal bd = (BigDecimal)get_Value("TotalDeduction");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Total Taxable Benefits.
@param TotalTaxableBenefit Total Taxable Benefits */
public void setTotalTaxableBenefit (BigDecimal TotalTaxableBenefit)
{
set_ValueNoCheck ("TotalTaxableBenefit", TotalTaxableBenefit);
}
/** Get Total Taxable Benefits.
@return Total Taxable Benefits */
public BigDecimal getTotalTaxableBenefit() 
{
BigDecimal bd = (BigDecimal)get_Value("TotalTaxableBenefit");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Bank Transfer Number.
@param TransfertNumber Bank Transfer Number */
public void setTransfertNumber (String TransfertNumber)
{
if (TransfertNumber != null && TransfertNumber.length() > 30)
{
log.warning("Length > 30 - truncated");
TransfertNumber = TransfertNumber.substring(0,29);
}
set_Value ("TransfertNumber", TransfertNumber);
}
/** Get Bank Transfer Number.
@return Bank Transfer Number */
public String getTransfertNumber() 
{
return (String)get_Value("TransfertNumber");
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

/** Set Bank Account.
@param P_BankAccount_ID Bank Account */
public void setP_BankAccount_ID (int P_BankAccount_ID)
{
if (P_BankAccount_ID <= 0) set_Value ("P_BankAccount_ID", null);
 else 
set_Value ("P_BankAccount_ID", new Integer(P_BankAccount_ID));
}
/** Get Bank Account.
@return Bank Account */
public int getP_BankAccount_ID() 
{
Integer ii = (Integer)get_Value("P_BankAccount_ID");
if (ii == null) return 0;
return ii.intValue();
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

/** Set Transferred.
@param IsTransferred Transferred to General Ledger (i.e. accounted) */
public void setIsTransferred (boolean IsTransferred)
{
set_Value ("IsTransferred", new Boolean(IsTransferred));
}
/** Get Transferred.
@return Transferred to General Ledger (i.e. accounted) */
public boolean isTransferred() 
{
Object oo = get_Value("IsTransferred");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set IsTransferredToBank.
@param IsTransferredToBank IsTransferredToBank */
public void setIsTransferredToBank (boolean IsTransferredToBank)
{
set_Value ("IsTransferredToBank", new Boolean(IsTransferredToBank));
}
/** Get IsTransferredToBank.
@return IsTransferredToBank */
public boolean isTransferredToBank() 
{
Object oo = get_Value("IsTransferredToBank");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}



}
