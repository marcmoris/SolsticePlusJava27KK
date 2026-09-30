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
/** Generated Model for P_Time_Sheet
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: X_P_Time_Sheet.java,v 1.6 2008/11/06 18:22:59 marmor01 Exp $ */
public class X_P_Time_Sheet extends PO
{
/** Standard Constructor
@param ctx context
@param P_Time_Sheet_ID id
@param trxName transaction
*/
public X_P_Time_Sheet (Properties ctx, int P_Time_Sheet_ID, String trxName)
{
super (ctx, P_Time_Sheet_ID, trxName);
/** if (P_Time_Sheet_ID == 0)
{
setIsComplete (false);
setIsError (false);
setIsWarning (false);	// N
setP_Collective_Labour_Agr_ID (0);
setP_Employee_ID (0);
setP_Frequency_ID (0);
setP_Occupation_Group_ID (0);
setP_Period_ID (0);
setP_Time_Sheet_ID (0);
setP_Year_ID (0);
setSheetType (null);
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
public X_P_Time_Sheet (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000112 */
public static final int Table_ID=2000112;

/** TableName=P_Time_Sheet */
public static final String Table_Name="P_Time_Sheet";

protected static KeyNamePair Model = new KeyNamePair(2000112,"P_Time_Sheet");

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
StringBuffer sb = new StringBuffer ("X_P_Time_Sheet[").append(get_ID()).append("]");
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
/** Set Complete.
@param IsComplete It is complete */
public void setIsComplete (boolean IsComplete)
{
set_Value ("IsComplete", new Boolean(IsComplete));
}
/** Get Complete.
@return It is complete */
public boolean isComplete() 
{
Object oo = get_Value("IsComplete");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Error.
@param IsError An Error occured in the execution */
public void setIsError (boolean IsError)
{
set_ValueNoCheck ("IsError", new Boolean(IsError));
}
/** Get Error.
@return An Error occured in the execution */
public boolean isError() 
{
Object oo = get_Value("IsError");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Warning.
@param IsWarning Warning */
public void setIsWarning (boolean IsWarning)
{
set_ValueNoCheck ("IsWarning", new Boolean(IsWarning));
}
/** Get Warning.
@return Warning */
public boolean isWarning() 
{
Object oo = get_Value("IsWarning");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set TimeSheet.
@param P_Booklet_ID TimeSheet */
public void setP_Booklet_ID (int P_Booklet_ID)
{
if (P_Booklet_ID <= 0) set_Value ("P_Booklet_ID", null);
 else 
set_Value ("P_Booklet_ID", new Integer(P_Booklet_ID));
}
/** Get TimeSheet.
@return TimeSheet */
public int getP_Booklet_ID() 
{
Integer ii = (Integer)get_Value("P_Booklet_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Collective_Labour_Agr_ID AD_Reference_ID=2000006 */
public static final int P_COLLECTIVE_LABOUR_AGR_ID_AD_Reference_ID=2000006;
/** Set Collective Labour Agreement.
@param P_Collective_Labour_Agr_ID Collective Labour Agreement */
public void setP_Collective_Labour_Agr_ID (int P_Collective_Labour_Agr_ID)
{
if (P_Collective_Labour_Agr_ID < 1) throw new IllegalArgumentException ("P_Collective_Labour_Agr_ID is mandatory.");
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

/** P_Payment_ID AD_Reference_ID=2000059 */
public static final int P_PAYMENT_ID_AD_Reference_ID=2000059;
/** Set Payment.
@param P_Payment_ID Payment */
public void setP_Payment_ID (int P_Payment_ID)
{
if (P_Payment_ID <= 0) set_Value ("P_Payment_ID", null);
 else 
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

/** P_Period_ID AD_Reference_ID=2000009 */
public static final int P_PERIOD_ID_AD_Reference_ID=2000009;
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
/** Set Time Sheet No..
@param P_Time_Sheet_ID   */
public void setP_Time_Sheet_ID (int P_Time_Sheet_ID)
{
if (P_Time_Sheet_ID < 1) throw new IllegalArgumentException ("P_Time_Sheet_ID is mandatory.");
set_Value ("P_Time_Sheet_ID", new Integer(P_Time_Sheet_ID));
}
/** Get Time Sheet No..
@return   */
public int getP_Time_Sheet_ID() 
{
Integer ii = (Integer)get_Value("P_Time_Sheet_ID");
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

/** SheetType AD_Reference_ID=2100376 */
public static final int SHEETTYPE_AD_Reference_ID=2100376;
/** Regular = Regular */
public static final String SHEETTYPE_Regular = "Regular";
/** Annulation+ = Annulation+ */
public static final String SHEETTYPE_AnnulationPlus = "Annulation+";
public static final String SHEETTYPE_AnnulPlus = "Annulation+";



/** Adjustement = Adjustement */
public static final String SHEETTYPE_Adjustment = "Adjustement";
/** Annulation- = Annulation- */
public static final String SHEETTYPE_Annulation_ = "Annulation-";
public static final String SHEETTYPE_AnnulMoins = "Annulation-";

/** Adjustement arrearage = Adjustement arrearage */
public static final String SHEETTYPE_AdjustementArrearage = "Adjustement arrearage";
/** Complementary = Complementary */
public static final String SHEETTYPE_Complementary = "Complementary";
/** Advance = Advance */
public static final String SHEETTYPE_Advance = "Advance";
/** Separation Pay = Separation */
public static final String SHEETTYPE_SeparationPay = "Separation";
/** Expense Account = ExpenseAccount */
public static final String SHEETTYPE_ExpenseAccount = "ExpenseAccount";
/** Holiday Payment = HolidayPayment */
public static final String SHEETTYPE_HolidayPayment = "HolidayPayment";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isSheetTypeValid (String test)
{
return test.equals("Regular") || test.equals("Annulation+") || test.equals("Adjustement") || test.equals("Annulation-") || test.equals("Adjustement arrearage") || test.equals("Complementary") || test.equals("Advance") || test.equals("Separation") || test.equals("ExpenseAccount") || test.equals("HolidayPayment");
}
/** Set Time Sheet Type.
@param SheetType   */
public void setSheetType (String SheetType)
{
if (SheetType == null) throw new IllegalArgumentException ("SheetType is mandatory");
if (!isSheetTypeValid(SheetType))
throw new IllegalArgumentException ("SheetType Invalid value - " + SheetType + " - Reference_ID=2100376 - Regular - Annulation+ - Adjustement - Annulation- - Adjustement arrearage - Complementary - Advance - Separation - ExpenseAccount - HolidayPayment");
if (SheetType.length() > 30)
{
log.warning("Length > 30 - truncated");
SheetType = SheetType.substring(0,29);
}
set_Value ("SheetType", SheetType);
}
/** Get Time Sheet Type.
@return   */
public String getSheetType() 
{
return (String)get_Value("SheetType");
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
if (TimeSheetStatus.length() > 1)
{
log.warning("Length > 1 - truncated");
TimeSheetStatus = TimeSheetStatus.substring(0,0);
}
set_Value ("TimeSheetStatus", TimeSheetStatus);
}
/** Get Status.
@return Status */
public String getTimeSheetStatus() 
{
return (String)get_Value("TimeSheetStatus");
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

/** P_Employer_ID AD_Reference_ID=2100464 */
public static final int P_EMPLOYER_ID_AD_Reference_ID=2100464;
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

}
