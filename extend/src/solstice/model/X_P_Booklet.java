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
/** Generated Model for P_Booklet
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Booklet extends PO
{
/** Standard Constructor
@param ctx context
@param P_Booklet_ID id
@param trxName transaction
*/
public X_P_Booklet (Properties ctx, int P_Booklet_ID, String trxName)
{
super (ctx, P_Booklet_ID, trxName);
/** if (P_Booklet_ID == 0)
{
setIsComplete (false);	// N
setIsError (false);	// N
setIsWarning (false);	// N
setP_Booklet_ID (0);
setP_Collective_Labour_Agr_ID (0);
setP_Employee_ID (0);
setP_Frequency_ID (0);
setP_Job_Title_ID (0);
setP_Occupation_Group_ID (0);
setP_Payment_Group_ID (0);
setP_Payment_ID (0);
setP_Period_ID (0);	// @P_Period_ID@
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
public X_P_Booklet (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100735 */
public static final int Table_ID=2100735;

/** TableName=P_Booklet */
public static final String Table_Name="P_Booklet";

protected static KeyNamePair Model = new KeyNamePair(2100735,"P_Booklet");

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
StringBuffer sb = new StringBuffer ("X_P_Booklet[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Activity.
@param C_Activity_ID Business Activity */
public void setC_Activity_ID (int C_Activity_ID)
{
if (C_Activity_ID <= 0) set_Value ("C_Activity_ID", null);
 else 
set_Value ("C_Activity_ID", new Integer(C_Activity_ID));
}
/** Get Activity.
@return Business Activity */
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
if (Description != null && Description.length() > 2000)
{
log.warning("Length > 2000 - truncated");
Description = Description.substring(0,1999);
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
@param IsError An Error that has occurred in the execution */
public void setIsError (boolean IsError)
{
set_Value ("IsError", new Boolean(IsError));
}
/** Get Error.
@return An Error that has occurred in the execution */
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
set_Value ("IsWarning", new Boolean(IsWarning));
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
if (P_Booklet_ID < 1) throw new IllegalArgumentException ("P_Booklet_ID is mandatory.");
set_ValueNoCheck ("P_Booklet_ID", new Integer(P_Booklet_ID));
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
/** Set Supervisor.
@param P_Distribution_Booklet_ID Supervisor */
public void setP_Distribution_Booklet_ID (int P_Distribution_Booklet_ID)
{
if (P_Distribution_Booklet_ID <= 0) set_Value ("P_Distribution_Booklet_ID", null);
 else 
set_Value ("P_Distribution_Booklet_ID", new Integer(P_Distribution_Booklet_ID));
}
/** Get Supervisor.
@return Supervisor */
public int getP_Distribution_Booklet_ID() 
{
Integer ii = (Integer)get_Value("P_Distribution_Booklet_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Distribution Method.
@param P_Distribution_ID Distribution Method */
public void setP_Distribution_ID (int P_Distribution_ID)
{
if (P_Distribution_ID <= 0) set_Value ("P_Distribution_ID", null);
 else 
set_Value ("P_Distribution_ID", new Integer(P_Distribution_ID));
}
/** Get Distribution Method.
@return Distribution Method */
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
/** Set Job Title.
@param P_Job_Title_ID   */
public void setP_Job_Title_ID (int P_Job_Title_ID)
{
//if (P_Job_Title_ID < 1) throw new IllegalArgumentException ("P_Job_Title_ID is mandatory.");
set_ValueNoCheck ("P_Job_Title_ID", new Integer(P_Job_Title_ID));
}
/** Get Job Title.
@return   */
public int getP_Job_Title_ID() 
{
Integer ii = (Integer)get_Value("P_Job_Title_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Work Status.
@param P_Job_Type_ID Work Status */
public void setP_Job_Type_ID (int P_Job_Type_ID)
{
if (P_Job_Type_ID <= 0) set_Value ("P_Job_Type_ID", null);
 else 
set_Value ("P_Job_Type_ID", new Integer(P_Job_Type_ID));
}
/** Get Work Status.
@return Work Status */
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
/** Set Payroll Group.
@param P_Payment_Group_ID Payroll Group */
public void setP_Payment_Group_ID (int P_Payment_Group_ID)
{
if (P_Payment_Group_ID < 1) throw new IllegalArgumentException ("P_Payment_Group_ID is mandatory.");
set_Value ("P_Payment_Group_ID", new Integer(P_Payment_Group_ID));
}
/** Get Payroll Group.
@return Payroll Group */
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

/** P_Year_ID AD_Reference_ID=2000012 */
public static final int P_YEAR_ID_AD_Reference_ID=2000012;
/** Set Calendar Year.
@param P_Year_ID Calendar Year */
public void setP_Year_ID (int P_Year_ID)
{
if (P_Year_ID < 1) throw new IllegalArgumentException ("P_Year_ID is mandatory.");
set_ValueNoCheck ("P_Year_ID", new Integer(P_Year_ID));
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
if (PaymentType != null && PaymentType.length() > 2)
{
log.warning("Length > 2 - truncated");
PaymentType = PaymentType.substring(0,1);
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
/** Adjustment = Adjustement */
public static final String SHEETTYPE_Adjustment = "Adjustement";
/** Adjustement arrearage = Adjustement arrearage */
public static final String SHEETTYPE_AdjustmentArrearage = "Adjustement arrearage";
/** Advance = Advance */
public static final String SHEETTYPE_Advance = "Advance";
/** Annulation- = Annulation- */
public static final String SHEETTYPE_Annulation_ = "Annulation-";
/** Annulation+ = Annulation+ */
public static final String SHEETTYPE_AnnulationPlus = "Annulation+";
/** Complementary = Complementary */
public static final String SHEETTYPE_Complementary = "Complementary";
/** Regular = Regular */
public static final String SHEETTYPE_Regular = "Regular";
/** Separation Pay = Separation */
public static final String SHEETTYPE_SeparationPay = "Separation";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isSheetTypeValid (String test)
{
return test.equals("Adjustement") || test.equals("Adjustement arrearage") || test.equals("Advance") || test.equals("Annulation-") || test.equals("Annulation+") || test.equals("Complementary") || test.equals("Regular") || test.equals("Separation");
}
/** Set Time Sheet Type.
@param SheetType   */
public void setSheetType (String SheetType)
{
if (SheetType == null) throw new IllegalArgumentException ("SheetType is mandatory");
if (!isSheetTypeValid(SheetType))
throw new IllegalArgumentException ("SheetType Invalid value - " + SheetType + " - Reference_ID=2100376 - Adjustement - Adjustement arrearage - Advance - Annulation- - Annulation+ - Complementary - Regular - Separation");
if (SheetType.length() > 16)
{
log.warning("Length > 16 - truncated");
SheetType = SheetType.substring(0,15);
}
set_Value ("SheetType", SheetType);
}
/** Get Time Sheet Type.
@return   */
public String getSheetType() 
{
return (String)get_Value("SheetType");
}

/** Taxation_Region_ID AD_Reference_ID=2000026 */
public static final int TAXATION_REGION_ID_AD_Reference_ID=2000026;
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
/** Error = F */
public static final String TIMESHEETSTATUS_Error = "F";
/** Cancelled = Z */
public static final String TIMESHEETSTATUS_Cancelled = "Z";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isTimeSheetStatusValid (String test)
{
return test.equals("I") || test.equals("V") || test.equals("A") || test.equals("C") || test.equals("E") || test.equals("T") || test.equals("F") || test.equals("Z");
}
/** Set Status.
@param TimeSheetStatus Status */
public void setTimeSheetStatus (String TimeSheetStatus)
{
if (TimeSheetStatus == null) throw new IllegalArgumentException ("TimeSheetStatus is mandatory");
if (!isTimeSheetStatusValid(TimeSheetStatus))
throw new IllegalArgumentException ("TimeSheetStatus Invalid value - " + TimeSheetStatus + " - Reference_ID=2000078 - I - V - A - C - E - T - F - Z");
if (TimeSheetStatus.length() > 2)
{
log.warning("Length > 2 - truncated");
TimeSheetStatus = TimeSheetStatus.substring(0,1);
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

}
