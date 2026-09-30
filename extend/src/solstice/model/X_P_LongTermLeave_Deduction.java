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
/** Generated Model for P_LongTermLeave_Deduction
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_LongTermLeave_Deduction extends PO
{
/** Standard Constructor
@param ctx context
@param P_LongTermLeave_Deduction_ID id
@param trxName transaction
*/
public X_P_LongTermLeave_Deduction (Properties ctx, int P_LongTermLeave_Deduction_ID, String trxName)
{
super (ctx, P_LongTermLeave_Deduction_ID, trxName);
/** if (P_LongTermLeave_Deduction_ID == 0)
{
setEmployeeAssessment (null);
setEmployerAssessment (null);
setP_Deduction_ID (0);
setP_LongTermLeave_Deduction_ID (0);
setP_LongTermLeave_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_LongTermLeave_Deduction (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100692 */
public static final int Table_ID=2100692;

/** TableName=P_LongTermLeave_Deduction */
public static final String Table_Name="P_LongTermLeave_Deduction";

protected static KeyNamePair Model = new KeyNamePair(2100692,"P_LongTermLeave_Deduction");

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
StringBuffer sb = new StringBuffer ("X_P_LongTermLeave_Deduction[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Day.
@param Day Day */
public void setDay (int Day)
{
set_Value ("Day", new Integer(Day));
}
/** Get Day.
@return Day */
public int getDay() 
{
Integer ii = (Integer)get_Value("Day");
if (ii == null) return 0;
return ii.intValue();
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
return test.equals("01") || test.equals("02") || test.equals("03");
}
/** Set Employee Assessment.
@param EmployeeAssessment Employee Assessment */
public void setEmployeeAssessment (String EmployeeAssessment)
{
if (EmployeeAssessment == null) throw new IllegalArgumentException ("EmployeeAssessment is mandatory");
if (!isEmployeeAssessmentValid(EmployeeAssessment))
throw new IllegalArgumentException ("EmployeeAssessment Invalid value - " + EmployeeAssessment + " - Reference_ID=2100351 - 01 - 02 - 03");
if (EmployeeAssessment.length() > 22)
{
log.warning("Length > 22 - truncated");
EmployeeAssessment = EmployeeAssessment.substring(0,21);
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
return test.equals("01") || test.equals("02") || test.equals("03");
}
/** Set Employer Assessment.
@param EmployerAssessment Employer Assessment */
public void setEmployerAssessment (String EmployerAssessment)
{
if (EmployerAssessment == null) throw new IllegalArgumentException ("EmployerAssessment is mandatory");
if (!isEmployerAssessmentValid(EmployerAssessment))
throw new IllegalArgumentException ("EmployerAssessment Invalid value - " + EmployerAssessment + " - Reference_ID=2100352 - 01 - 02 - 03");
if (EmployerAssessment.length() > 20)
{
log.warning("Length > 20 - truncated");
EmployerAssessment = EmployerAssessment.substring(0,19);
}
set_Value ("EmployerAssessment", EmployerAssessment);
}
/** Get Employer Assessment.
@return Employer Assessment */
public String getEmployerAssessment() 
{
return (String)get_Value("EmployerAssessment");
}
/** Set Month.
@param Month Month */
public void setMonth (int Month)
{
set_Value ("Month", new Integer(Month));
}
/** Get Month.
@return Month */
public int getMonth() 
{
Integer ii = (Integer)get_Value("Month");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Deduction_ID AD_Reference_ID=2000039 */
public static final int P_DEDUCTION_ID_AD_Reference_ID=2000039;
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
/** Set Long Term Leave Deduction.
@param P_LongTermLeave_Deduction_ID Long Term Leave Deduction */
public void setP_LongTermLeave_Deduction_ID (int P_LongTermLeave_Deduction_ID)
{
if (P_LongTermLeave_Deduction_ID < 1) throw new IllegalArgumentException ("P_LongTermLeave_Deduction_ID is mandatory.");
set_ValueNoCheck ("P_LongTermLeave_Deduction_ID", new Integer(P_LongTermLeave_Deduction_ID));
}
/** Get Long Term Leave Deduction.
@return Long Term Leave Deduction */
public int getP_LongTermLeave_Deduction_ID() 
{
Integer ii = (Integer)get_Value("P_LongTermLeave_Deduction_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Activity Status.
@param P_LongTermLeave_ID Activity Status */
public void setP_LongTermLeave_ID (int P_LongTermLeave_ID)
{
if (P_LongTermLeave_ID < 1) throw new IllegalArgumentException ("P_LongTermLeave_ID is mandatory.");
set_ValueNoCheck ("P_LongTermLeave_ID", new Integer(P_LongTermLeave_ID));
}
/** Get Activity Status.
@return Activity Status */
public int getP_LongTermLeave_ID() 
{
Integer ii = (Integer)get_Value("P_LongTermLeave_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
