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
/** Generated Model for P_Payment_Deduction_Excep
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.1 2007/07/18 14:43:46 marmor01 Exp $ */
public class X_P_Payment_Deduction_Excep extends PO
{
/** Standard Constructor
@param ctx context
@param P_Payment_Deduction_Excep_ID id
@param trxName transaction
*/
public X_P_Payment_Deduction_Excep (Properties ctx, int P_Payment_Deduction_Excep_ID, String trxName)
{
super (ctx, P_Payment_Deduction_Excep_ID, trxName);
/** if (P_Payment_Deduction_Excep_ID == 0)
{
setP_Employee_Deduction_ID (0);
setP_Payment_Deduction_Excep_ID (0);
setP_Period_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Payment_Deduction_Excep (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100757 */
public static final int Table_ID=2100757;

/** TableName=P_Payment_Deduction_Excep */
public static final String Table_Name="P_Payment_Deduction_Excep";

protected static KeyNamePair Model = new KeyNamePair(2100757,"P_Payment_Deduction_Excep");

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
StringBuffer sb = new StringBuffer ("X_P_Payment_Deduction_Excep[").append(get_ID()).append("]");
return sb.toString();
}

/** EmployeeAssessment AD_Reference_ID=2100351 */
public static final int EMPLOYEEASSESSMENT_AD_Reference_ID=2100351;
/** Relieved = 01 */
public static final String EMPLOYEEASSESSMENT_Relieved = "01";
/** Relieved + employer = 02 */
public static final String EMPLOYEEASSESSMENT_RelievedPlusEmployer = "02";
/** No = 03 */
public static final String EMPLOYEEASSESSMENT_No = "03";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isEmployeeAssessmentValid (String test)
{
return test == null || test.equals("01") || test.equals("02") || test.equals("03");
}
/** Set Employee Assessment.
@param EmployeeAssessment Employee Assessment */
public void setEmployeeAssessment (String EmployeeAssessment)
{
if (!isEmployeeAssessmentValid(EmployeeAssessment))
throw new IllegalArgumentException ("EmployeeAssessment Invalid value - " + EmployeeAssessment + " - Reference_ID=2100351 - 01 - 02 - 03");
if (EmployeeAssessment != null && EmployeeAssessment.length() > 2)
{
log.warning("Length > 2 - truncated");
EmployeeAssessment = EmployeeAssessment.substring(0,1);
}
set_Value ("EmployeeAssessment", EmployeeAssessment);
}
/** Get Employee Assessment.
@return Employee Assessment */
public String getEmployeeAssessment() 
{
return (String)get_Value("EmployeeAssessment");
}

/** EmployerAssessment AD_Reference_ID=2100352 */
public static final int EMPLOYERASSESSMENT_AD_Reference_ID=2100352;
/** Assess = 01 */
public static final String EMPLOYERASSESSMENT_Assess = "01";
/** Assess + employee = 02 */
public static final String EMPLOYERASSESSMENT_AssessPlusEmployee = "02";
/** No = 03 */
public static final String EMPLOYERASSESSMENT_No = "03";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isEmployerAssessmentValid (String test)
{
return test == null || test.equals("01") || test.equals("02") || test.equals("03");
}
/** Set Employer Assessment.
@param EmployerAssessment Employer Assessment */
public void setEmployerAssessment (String EmployerAssessment)
{
if (!isEmployerAssessmentValid(EmployerAssessment))
throw new IllegalArgumentException ("EmployerAssessment Invalid value - " + EmployerAssessment + " - Reference_ID=2100352 - 01 - 02 - 03");
if (EmployerAssessment != null && EmployerAssessment.length() > 2)
{
log.warning("Length > 2 - truncated");
EmployerAssessment = EmployerAssessment.substring(0,1);
}
set_Value ("EmployerAssessment", EmployerAssessment);
}
/** Get Employer Assessment.
@return Employer Assessment */
public String getEmployerAssessment() 
{
return (String)get_Value("EmployerAssessment");
}
/** Set Amount Employee.
@param ExceptionAmountEmployee Amount Employee */
public void setExceptionAmountEmployee (BigDecimal ExceptionAmountEmployee)
{
set_Value ("ExceptionAmountEmployee", ExceptionAmountEmployee);
}
/** Get Amount Employee.
@return Amount Employee */
public BigDecimal getExceptionAmountEmployee() 
{
BigDecimal bd = (BigDecimal)get_Value("ExceptionAmountEmployee");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Amount Employer.
@param ExceptionAmountEmployer Amount Employer */
public void setExceptionAmountEmployer (BigDecimal ExceptionAmountEmployer)
{
set_Value ("ExceptionAmountEmployer", ExceptionAmountEmployer);
}
/** Get Amount Employer.
@return Amount Employer */
public BigDecimal getExceptionAmountEmployer() 
{
BigDecimal bd = (BigDecimal)get_Value("ExceptionAmountEmployer");
if (bd == null) return Env.ZERO;
return bd;
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
/** Set P_Payment_Deduction_Excep_ID.
@param P_Payment_Deduction_Excep_ID P_Payment_Deduction_Excep_ID */
public void setP_Payment_Deduction_Excep_ID (int P_Payment_Deduction_Excep_ID)
{
if (P_Payment_Deduction_Excep_ID < 1) throw new IllegalArgumentException ("P_Payment_Deduction_Excep_ID is mandatory.");
set_ValueNoCheck ("P_Payment_Deduction_Excep_ID", new Integer(P_Payment_Deduction_Excep_ID));
}
/** Get P_Payment_Deduction_Excep_ID.
@return P_Payment_Deduction_Excep_ID */
public int getP_Payment_Deduction_Excep_ID() 
{
Integer ii = (Integer)get_Value("P_Payment_Deduction_Excep_ID");
if (ii == null) return 0;
return ii.intValue();
}
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

/** P_Period_ID AD_Reference_ID=2100320 */
public static final int P_PERIOD_ID_AD_Reference_ID=2100320;
/** Set Period.
@param P_Period_ID Period of the Calendar  */
public void setP_Period_ID (int P_Period_ID)
{
if (P_Period_ID < 1) throw new IllegalArgumentException ("P_Period_ID is mandatory.");
set_Value ("P_Period_ID", new Integer(P_Period_ID));
}
/** Get Period.
@return Period of the Calendar  */
public int getP_Period_ID() 
{
Integer ii = (Integer)get_Value("P_Period_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
