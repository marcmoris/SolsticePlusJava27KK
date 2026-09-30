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
/** Generated Model for P_Payment_Gain
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Payment_Gain extends PO
{
/** Standard Constructor
@param ctx context
@param P_Payment_Gain_ID id
@param trxName transaction
*/
public X_P_Payment_Gain (Properties ctx, int P_Payment_Gain_ID, String trxName)
{
super (ctx, P_Payment_Gain_ID, trxName);
/** if (P_Payment_Gain_ID == 0)
{
setLine (0);
setP_Assignment_ID (0);
setP_Collective_Labour_Agr_ID (0);
setP_Employee_ID (0);	// @P_Employee_ID@
setP_Gain_ID (0);
setP_Gain_Parameter_ID (0);
setP_Payment_Gain_ID (0);
setP_Payment_ID (0);
setP_UOM_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Payment_Gain (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000095 */
public static final int Table_ID=2000095;

/** TableName=P_Payment_Gain */
public static final String Table_Name="P_Payment_Gain";

protected static KeyNamePair Model = new KeyNamePair(2000095,"P_Payment_Gain");

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
StringBuffer sb = new StringBuffer ("X_P_Payment_Gain[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Amount calculed.
@param AmountCalc Amount calculed */
public void setAmountCalc (BigDecimal AmountCalc)
{
set_Value ("AmountCalc", AmountCalc);
}
/** Get Amount calculed.
@return Amount calculed */
public BigDecimal getAmountCalc() 
{
BigDecimal bd = (BigDecimal)get_Value("AmountCalc");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Bonus Amount.
@param Bonus_Amount Bonus Amount */
public void setBonus_Amount (BigDecimal Bonus_Amount)
{
set_Value ("Bonus_Amount", Bonus_Amount);
}
/** Get Bonus Amount.
@return Bonus Amount */
public BigDecimal getBonus_Amount() 
{
BigDecimal bd = (BigDecimal)get_Value("Bonus_Amount");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Function.
@param C_Activity_ID Function */
public void setC_Activity_ID (int C_Activity_ID)
{
if (C_Activity_ID <= 0) set_Value ("C_Activity_ID", null);
 else 
set_Value ("C_Activity_ID", new Integer(C_Activity_ID));
}
/** Get Function.
@return Function */
public int getC_Activity_ID() 
{
Integer ii = (Integer)get_Value("C_Activity_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Day.
@param Day Day */
public void setDay (Timestamp Day)
{
set_Value ("Day", Day);
}
/** Get Day.
@return Day */
public Timestamp getDay() 
{
return (Timestamp)get_Value("Day");
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
/** Set Fixed Amount.
@param Fixed_Amount   */
public void setFixed_Amount (BigDecimal Fixed_Amount)
{
set_Value ("Fixed_Amount", Fixed_Amount);
}
/** Get Fixed Amount.
@return   */
public BigDecimal getFixed_Amount() 
{
BigDecimal bd = (BigDecimal)get_Value("Fixed_Amount");
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
/** Set Line No.
@param Line Unique line for this document */
public void setLine (int Line)
{
set_Value ("Line", new Integer(Line));
}
/** Get Line No.
@return Unique line for this document */
public int getLine() 
{
Integer ii = (Integer)get_Value("Line");
if (ii == null) return 0;
return ii.intValue();
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
/** Set Origin.
@param OriginTime Origin */
public void setOriginTime (String OriginTime)
{
if (OriginTime != null && OriginTime.length() > 3)
{
log.warning("Length > 3 - truncated");
OriginTime = OriginTime.substring(0,2);
}
set_Value ("OriginTime", OriginTime);
}
/** Get Origin.
@return Origin */
public String getOriginTime() 
{
return (String)get_Value("OriginTime");
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

/** P_Assignment_ID AD_Reference_ID=2000057 */
public static final int P_ASSIGNMENT_ID_AD_Reference_ID=2000057;
/** Set Assignment.
@param P_Assignment_ID Assignment */
public void setP_Assignment_ID (int P_Assignment_ID)
{
if (P_Assignment_ID < 1) throw new IllegalArgumentException ("P_Assignment_ID is mandatory.");
set_Value ("P_Assignment_ID", new Integer(P_Assignment_ID));
}
/** Get Assignment.
@return Assignment */
public int getP_Assignment_ID() 
{
Integer ii = (Integer)get_Value("P_Assignment_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Assigment parameter.
@param P_Assignment_Param_ID Assigment parameter */
public void setP_Assignment_Param_ID (int P_Assignment_Param_ID)
{
if (P_Assignment_Param_ID <= 0) set_Value ("P_Assignment_Param_ID", null);
 else 
set_Value ("P_Assignment_Param_ID", new Integer(P_Assignment_Param_ID));
}
/** Get Assigment parameter.
@return Assigment parameter */
public int getP_Assignment_Param_ID() 
{
Integer ii = (Integer)get_Value("P_Assignment_Param_ID");
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
/** Set Long Term Leave.
@param P_Employee_LongTermLeave_ID Long Term Leave */
public void setP_Employee_LongTermLeave_ID (int P_Employee_LongTermLeave_ID)
{
if (P_Employee_LongTermLeave_ID <= 0) set_Value ("P_Employee_LongTermLeave_ID", null);
 else 
set_Value ("P_Employee_LongTermLeave_ID", new Integer(P_Employee_LongTermLeave_ID));
}
/** Get Long Term Leave.
@return Long Term Leave */
public int getP_Employee_LongTermLeave_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_LongTermLeave_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Gain.
@param P_Gain_ID Gain */
public void setP_Gain_ID (int P_Gain_ID)
{
if (P_Gain_ID < 1) throw new IllegalArgumentException ("P_Gain_ID is mandatory.");
set_Value ("P_Gain_ID", new Integer(P_Gain_ID));
}
/** Get Gain.
@return Gain */
public int getP_Gain_ID() 
{
Integer ii = (Integer)get_Value("P_Gain_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Gain Parameter.
@param P_Gain_Parameter_ID Gain Parameter */
public void setP_Gain_Parameter_ID (int P_Gain_Parameter_ID)
{
if (P_Gain_Parameter_ID < 1) throw new IllegalArgumentException ("P_Gain_Parameter_ID is mandatory.");
set_Value ("P_Gain_Parameter_ID", new Integer(P_Gain_Parameter_ID));
}
/** Get Gain Parameter.
@return Gain Parameter */
public int getP_Gain_Parameter_ID() 
{
Integer ii = (Integer)get_Value("P_Gain_Parameter_ID");
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
/** Set P_Payment_Gain_ID.
@param P_Payment_Gain_ID P_Payment_Gain_ID */
public void setP_Payment_Gain_ID (int P_Payment_Gain_ID)
{
if (P_Payment_Gain_ID < 1) throw new IllegalArgumentException ("P_Payment_Gain_ID is mandatory.");
set_Value ("P_Payment_Gain_ID", new Integer(P_Payment_Gain_ID));
}
/** Get P_Payment_Gain_ID.
@return P_Payment_Gain_ID */
public int getP_Payment_Gain_ID() 
{
Integer ii = (Integer)get_Value("P_Payment_Gain_ID");
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

/** P_Period_ID AD_Reference_ID=2000009 */
public static final int P_PERIOD_ID_AD_Reference_ID=2000009;
/** Set Period.
@param P_Period_ID Period of the Calendar   */
public void setP_Period_ID (int P_Period_ID)
{
if (P_Period_ID <= 0) set_Value ("P_Period_ID", null);
 else 
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

/** P_Post_ID AD_Reference_ID=2000004 */
public static final int P_POST_ID_AD_Reference_ID=2000004;
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
/** Set Salary Scale.
@param P_Salary_Scale_ID   */
public void setP_Salary_Scale_ID (int P_Salary_Scale_ID)
{
if (P_Salary_Scale_ID <= 0) set_Value ("P_Salary_Scale_ID", null);
 else 
set_Value ("P_Salary_Scale_ID", new Integer(P_Salary_Scale_ID));
}
/** Get Salary Scale.
@return   */
public int getP_Salary_Scale_ID() 
{
Integer ii = (Integer)get_Value("P_Salary_Scale_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_UOM_ID AD_Reference_ID=2100342 */
public static final int P_UOM_ID_AD_Reference_ID=2100342;
/** Set Unit.
@param P_UOM_ID Unit of Measure */
public void setP_UOM_ID (int P_UOM_ID)
{
if (P_UOM_ID < 1) throw new IllegalArgumentException ("P_UOM_ID is mandatory.");
set_Value ("P_UOM_ID", new Integer(P_UOM_ID));
}
/** Get Unit.
@return Unit of Measure */
public int getP_UOM_ID() 
{
Integer ii = (Integer)get_Value("P_UOM_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Year_ID AD_Reference_ID=2000012 */
public static final int P_YEAR_ID_AD_Reference_ID=2000012;
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
/** Set Quantity.
@param Quantity Quantity */
public void setQuantity (BigDecimal Quantity)
{
set_Value ("Quantity", Quantity);
}
/** Get Quantity.
@return Quantity */
public BigDecimal getQuantity() 
{
BigDecimal bd = (BigDecimal)get_Value("Quantity");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Quantity calculed.
@param QuantityCalc Quantity calculed */
public void setQuantityCalc (BigDecimal QuantityCalc)
{
set_Value ("QuantityCalc", QuantityCalc);
}
/** Get Quantity calculed.
@return Quantity calculed */
public BigDecimal getQuantityCalc() 
{
BigDecimal bd = (BigDecimal)get_Value("QuantityCalc");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Salary Percentage.
@param SalaryPercentage Salary Percentage */
public void setSalaryPercentage (BigDecimal SalaryPercentage)
{
set_Value ("SalaryPercentage", SalaryPercentage);
}
/** Get Salary Percentage.
@return Salary Percentage */
public BigDecimal getSalaryPercentage() 
{
BigDecimal bd = (BigDecimal)get_Value("SalaryPercentage");
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

/** Type_Rate AD_Reference_ID=2000022 */
public static final int TYPE_RATE_AD_Reference_ID=2000022;
/** Overtime = O */
public static final String TYPE_RATE_Overtime = "O";
/** Accumulated Days = D */
public static final String TYPE_RATE_AccumulatedDays = "D";
/** Short Term Disability = S */
public static final String TYPE_RATE_ShortTermDisability = "S";
/** Vacance taux employé = H */
public static final String TYPE_RATE_VacanceTauxEmploye = "H";
public static final String TYPE_RATE_VacanceTauxEmployé = "H";
/** CSST / WSIB = W */
public static final String TYPE_RATE_CSSTWSIB = "W";
/** Fixed Rate = F */
public static final String TYPE_RATE_FixedRate = "F";
/** Employee Affectation = A */
public static final String TYPE_RATE_EmployeeAffectation = "A";
/** Vacance selon banque          = V */
public static final String TYPE_RATE_VacanceSelonBanque = "V";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isType_RateValid (String test)
{
return test == null || test.equals("O") || test.equals("D") || test.equals("S") || test.equals("H") || test.equals("W") || test.equals("F") || test.equals("A") || test.equals("V");
}
/** Set Type Rate.
@param Type_Rate   */
public void setType_Rate (String Type_Rate)
{
if (!isType_RateValid(Type_Rate))
throw new IllegalArgumentException ("Type_Rate Invalid value - " + Type_Rate + " - Reference_ID=2000022 - O - D - S - H - W - F - A - V");
if (Type_Rate != null && Type_Rate.length() > 1)
{
log.warning("Length > 1 - truncated");
Type_Rate = Type_Rate.substring(0,0);
}
set_Value ("Type_Rate", Type_Rate);
}
/** Get Type Rate.
@return   */
public String getType_Rate() 
{
return (String)get_Value("Type_Rate");
}
/** Set Week Index.
@param WeekIndex Week Index */
public void setWeekIndex (int WeekIndex)
{
set_Value ("WeekIndex", new Integer(WeekIndex));
}
/** Get Week Index.
@return Week Index */
public int getWeekIndex() 
{
Integer ii = (Integer)get_Value("WeekIndex");
if (ii == null) return 0;
return ii.intValue();
}


/** Set Schedule.
@param P_Schedule_ID Répartition des heures de travail à l'intérieur d'une période donnée.  */
public void setP_Schedule_ID (int P_Schedule_ID)
{
if (P_Schedule_ID <= 0) set_Value ("P_Schedule_ID", null);
 else 
set_Value ("P_Schedule_ID", new Integer(P_Schedule_ID));
}
/** Get Schedule.
@return Répartition des heures de travail à l'intérieur d'une période donnée.  */
public int getP_Schedule_ID() 
{
Integer ii = (Integer)get_Value("P_Schedule_ID");
if (ii == null) return 0;
return ii.intValue();
}


}
