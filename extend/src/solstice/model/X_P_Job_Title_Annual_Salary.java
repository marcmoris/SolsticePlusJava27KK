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
/** Generated Model for P_Job_Title_Annual_Salary
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Job_Title_Annual_Salary extends PO
{
/** Standard Constructor
@param ctx context
@param P_Job_Title_Annual_Salary_ID id
@param trxName transaction
*/
public X_P_Job_Title_Annual_Salary (Properties ctx, int P_Job_Title_Annual_Salary_ID, String trxName)
{
super (ctx, P_Job_Title_Annual_Salary_ID, trxName);
/** if (P_Job_Title_Annual_Salary_ID == 0)
{
setP_Job_Title_Annual_Salary_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Job_Title_Annual_Salary (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100798 */
public static final int Table_ID=2100798;

/** TableName=P_Job_Title_Annual_Salary */
public static final String Table_Name="P_Job_Title_Annual_Salary";

protected static KeyNamePair Model = new KeyNamePair(2100798,"P_Job_Title_Annual_Salary");

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
StringBuffer sb = new StringBuffer ("X_P_Job_Title_Annual_Salary[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Base Salary.
@param BaseSalary Base Salary */
public void setBaseSalary (BigDecimal BaseSalary)
{
set_Value ("BaseSalary", BaseSalary);
}
/** Get Base Salary.
@return Base Salary */
public BigDecimal getBaseSalary() 
{
BigDecimal bd = (BigDecimal)get_Value("BaseSalary");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Contributory Days.
@param ContributoryDays Contributory Days */
public void setContributoryDays (BigDecimal ContributoryDays)
{
set_Value ("ContributoryDays", ContributoryDays);
}
/** Get Contributory Days.
@return Contributory Days */
public BigDecimal getContributoryDays() 
{
BigDecimal bd = (BigDecimal)get_Value("ContributoryDays");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Disability Days.
@param DisabilityDays Disability Days */
public void setDisabilityDays (BigDecimal DisabilityDays)
{
set_Value ("DisabilityDays", DisabilityDays);
}
/** Get Disability Days.
@return Disability Days */
public BigDecimal getDisabilityDays() 
{
BigDecimal bd = (BigDecimal)get_Value("DisabilityDays");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Leave Of Absence Without Pay.
@param LeaveOfAbsenceWithoutPay Leave Of Absence Without Pay */
public void setLeaveOfAbsenceWithoutPay (BigDecimal LeaveOfAbsenceWithoutPay)
{
set_Value ("LeaveOfAbsenceWithoutPay", LeaveOfAbsenceWithoutPay);
}
/** Get Leave Of Absence Without Pay.
@return Leave Of Absence Without Pay */
public BigDecimal getLeaveOfAbsenceWithoutPay() 
{
BigDecimal bd = (BigDecimal)get_Value("LeaveOfAbsenceWithoutPay");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Motherhood Days.
@param MotherHoodDays Motherhood Days */
public void setMotherHoodDays (BigDecimal MotherHoodDays)
{
set_Value ("MotherHoodDays", MotherHoodDays);
}
/** Get Motherhood Days.
@return Motherhood Days */
public BigDecimal getMotherHoodDays() 
{
BigDecimal bd = (BigDecimal)get_Value("MotherHoodDays");
if (bd == null) return Env.ZERO;
return bd;
}
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
/** Set P_Job_Title_Annual_Salary_ID.
@param P_Job_Title_Annual_Salary_ID P_Job_Title_Annual_Salary_ID */
public void setP_Job_Title_Annual_Salary_ID (int P_Job_Title_Annual_Salary_ID)
{
if (P_Job_Title_Annual_Salary_ID < 1) throw new IllegalArgumentException ("P_Job_Title_Annual_Salary_ID is mandatory.");
set_ValueNoCheck ("P_Job_Title_Annual_Salary_ID", new Integer(P_Job_Title_Annual_Salary_ID));
}
/** Get P_Job_Title_Annual_Salary_ID.
@return P_Job_Title_Annual_Salary_ID */
public int getP_Job_Title_Annual_Salary_ID() 
{
Integer ii = (Integer)get_Value("P_Job_Title_Annual_Salary_ID");
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
/** Set Year.
@param P_Year_ID Calendar Year */
public void setP_Year_ID (int P_Year_ID)
{
if (P_Year_ID <= 0) set_Value ("P_Year_ID", null);
 else 
set_Value ("P_Year_ID", new Integer(P_Year_ID));
}
/** Get Year.
@return Calendar Year */
public int getP_Year_ID() 
{
Integer ii = (Integer)get_Value("P_Year_ID");
if (ii == null) return 0;
return ii.intValue();
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
