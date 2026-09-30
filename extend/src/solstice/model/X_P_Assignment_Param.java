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
/** Generated Model for P_Assignment_Param
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Assignment_Param extends PO
{
/** Standard Constructor
@param ctx context
@param P_Assignment_Param_ID id
@param trxName transaction
*/
public X_P_Assignment_Param (Properties ctx, int P_Assignment_Param_ID, String trxName)
{
super (ctx, P_Assignment_Param_ID, trxName);
/** if (P_Assignment_Param_ID == 0)
{
setEffectIn (new Timestamp(System.currentTimeMillis()));	// @#Date@
setIsOut_Of_Range (false);
setIsOut_Of_Rate (false);
setP_Assignment_ID (0);
setP_Assignment_Param_ID (0);
setP_Collective_Labour_Agr_ID (0);	// @SQL=Select P_Collective_Labour_Agr_ID As DefaultValue From P_Employee Where P_Employee_ID = ( Select P_Employee_ID From P_Assignment Where P_Assignment_ID =@P_Assignment_ID@ )
setP_Occupation_Group_ID (0);	// @SQL=Select P_Occupation_Group_ID AS DefaultValue From P_Employee Where P_Employee_ID = ( Select P_Employee_ID From P_Assignment Where P_Assignment_ID = @P_Assignment_ID@ )
setP_Salary_Scale_Detail_ID (0);
setP_Salary_Scale_ID (0);
setRemuneration_Method (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Assignment_Param (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100776 */
public static final int Table_ID=2100776;

/** TableName=P_Assignment_Param */
public static final String Table_Name="P_Assignment_Param";

protected static KeyNamePair Model = new KeyNamePair(2100776,"P_Assignment_Param");

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
StringBuffer sb = new StringBuffer ("X_P_Assignment_Param[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Annual Salary.
@param Annual_Salary   */
public void setAnnual_Salary (BigDecimal Annual_Salary)
{
set_Value ("Annual_Salary", Annual_Salary);
}
/** Get Annual Salary.
@return   */
public BigDecimal getAnnual_Salary() 
{
BigDecimal bd = (BigDecimal)get_Value("Annual_Salary");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Annual Salary ( Out of Rate).
@param Annual_Salary_Out_Of_Rate Annual Salary ( Out of Rate) */
public void setAnnual_Salary_Out_Of_Rate (BigDecimal Annual_Salary_Out_Of_Rate)
{
throw new IllegalArgumentException ("Annual_Salary_Out_Of_Rate is virtual column");
}
/** Get Annual Salary ( Out of Rate).
@return Annual Salary ( Out of Rate) */
public BigDecimal getAnnual_Salary_Out_Of_Rate() 
{
BigDecimal bd = (BigDecimal)get_Value("Annual_Salary_Out_Of_Rate");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Salaru Scale by born.
@param CheckBorn Champs invisible que nous indique si l'affectation est par borne ou de type hors échelle */
public void setCheckBorn (String CheckBorn)
{
throw new IllegalArgumentException ("CheckBorn is virtual column");
}
/** Get Salaru Scale by born.
@return Champs invisible que nous indique si l'affectation est par borne ou de type hors échelle */
public String getCheckBorn() 
{
return (String)get_Value("CheckBorn");
}
/** Set CompaRatio.
@param CompaRatio CompaRatios are used to compare an individual's current rate of pay to the midpoint of the salary range.  */
public void setCompaRatio (BigDecimal CompaRatio)
{
throw new IllegalArgumentException ("CompaRatio is virtual column");
}
/** Get CompaRatio.
@return CompaRatios are used to compare an individual's current rate of pay to the midpoint of the salary range.  */
public BigDecimal getCompaRatio() 
{
BigDecimal bd = (BigDecimal)get_Value("CompaRatio");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Cumulated Hours.
@param CumulatedHours Cumulated Hours */
public void setCumulatedHours (BigDecimal CumulatedHours)
{
set_Value ("CumulatedHours", CumulatedHours);
}
/** Get Cumulated Hours.
@return Cumulated Hours */
public BigDecimal getCumulatedHours() 
{
BigDecimal bd = (BigDecimal)get_Value("CumulatedHours");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Daily Salary.
@param Daily_Salary Daily Salary */
public void setDaily_Salary (BigDecimal Daily_Salary)
{
set_Value ("Daily_Salary", Daily_Salary);
}
/** Get Daily Salary.
@return Daily Salary */
public BigDecimal getDaily_Salary() 
{
BigDecimal bd = (BigDecimal)get_Value("Daily_Salary");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Daily Hours.
@param Day_Hours   */
public void setDay_Hours (BigDecimal Day_Hours)
{
set_Value ("Day_Hours", Day_Hours);
}
/** Get Daily Hours.
@return   */
public BigDecimal getDay_Hours() 
{
BigDecimal bd = (BigDecimal)get_Value("Day_Hours");
if (bd == null) return Env.ZERO;
return bd;
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
/** Set Employee.
@param EmployeeName Employee */
public void setEmployeeName (String EmployeeName)
{
throw new IllegalArgumentException ("EmployeeName is virtual column");
}
/** Get Employee.
@return Employee */
public String getEmployeeName() 
{
return (String)get_Value("EmployeeName");
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
/** Set Hourly Rate.
@param Hourly_Rate_Out_Of_Rate Hourly Rate */
public void setHourly_Rate_Out_Of_Rate (BigDecimal Hourly_Rate_Out_Of_Rate)
{
throw new IllegalArgumentException ("Hourly_Rate_Out_Of_Rate is virtual column");
}
/** Get Hourly Rate.
@return Hourly Rate */
public BigDecimal getHourly_Rate_Out_Of_Rate() 
{
BigDecimal bd = (BigDecimal)get_Value("Hourly_Rate_Out_Of_Rate");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours Per Pay.
@param HoursPerPay Hours Per Pay */
public void setHoursPerPay (BigDecimal HoursPerPay)
{
throw new IllegalArgumentException ("HoursPerPay is virtual column");
}
/** Get Hours Per Pay.
@return Hours Per Pay */
public BigDecimal getHoursPerPay() 
{
BigDecimal bd = (BigDecimal)get_Value("HoursPerPay");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Out Of Range.
@param IsOut_Of_Range   */
public void setIsOut_Of_Range (boolean IsOut_Of_Range)
{
set_Value ("IsOut_Of_Range", new Boolean(IsOut_Of_Range));
}
/** Get Out Of Range.
@return   */
public boolean isOut_Of_Range() 
{
Object oo = get_Value("IsOut_Of_Range");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Out Of Rate.
@param IsOut_Of_Rate Out Of Rate */
public void setIsOut_Of_Rate (boolean IsOut_Of_Rate)
{
set_Value ("IsOut_Of_Rate", new Boolean(IsOut_Of_Rate));
}
/** Get Out Of Rate.
@return Out Of Rate */
public boolean isOut_Of_Rate() 
{
Object oo = get_Value("IsOut_Of_Rate");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Out Of Rate Contractual.
@param Out_Of_Rate_Contractual Out Of Rate Contractual */
public void setOut_Of_Rate_Contractual (BigDecimal Out_Of_Rate_Contractual)
{
set_Value ("Out_Of_Rate_Contractual", Out_Of_Rate_Contractual);
}
/** Get Out Of Rate Contractual.
@return Out Of Rate Contractual */
public BigDecimal getOut_Of_Rate_Contractual() 
{
BigDecimal bd = (BigDecimal)get_Value("Out_Of_Rate_Contractual");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Out Of Rate Salary.
@param Out_Of_Rate_Salary Out Of Rate Salary */
public void setOut_Of_Rate_Salary (BigDecimal Out_Of_Rate_Salary)
{
set_Value ("Out_Of_Rate_Salary", Out_Of_Rate_Salary);
}
/** Get Out Of Rate Salary.
@return Out Of Rate Salary */
public BigDecimal getOut_Of_Rate_Salary() 
{
BigDecimal bd = (BigDecimal)get_Value("Out_Of_Rate_Salary");
if (bd == null) return Env.ZERO;
return bd;
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
if (P_Assignment_Param_ID < 1) throw new IllegalArgumentException ("P_Assignment_Param_ID is mandatory.");
set_ValueNoCheck ("P_Assignment_Param_ID", new Integer(P_Assignment_Param_ID));
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
//if (P_Collective_Labour_Agr_ID < 1) throw new IllegalArgumentException ("P_Collective_Labour_Agr_ID is mandatory.");
set_ValueNoCheck ("P_Collective_Labour_Agr_ID", new Integer(P_Collective_Labour_Agr_ID));
}
/** Get Collective Labour Agreement.
@return Collective Labour Agreement */
public int getP_Collective_Labour_Agr_ID() 
{
Integer ii = (Integer)get_Value("P_Collective_Labour_Agr_ID");
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

/** P_Occupation_Group_ID AD_Reference_ID=2000014 */
public static final int P_OCCUPATION_GROUP_ID_AD_Reference_ID=2000014;
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

/** P_Salary_Class_ID AD_Reference_ID=2100354 */
public static final int P_SALARY_CLASS_ID_AD_Reference_ID=2100354;
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
if (P_Salary_Scale_Detail_ID < 1) throw new IllegalArgumentException ("P_Salary_Scale_Detail_ID is mandatory.");
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
if (P_Salary_Scale_ID < 1) throw new IllegalArgumentException ("P_Salary_Scale_ID is mandatory.");
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

/** P_Schedule_ID AD_Reference_ID=2000064 */
public static final int P_SCHEDULE_ID_AD_Reference_ID=2000064;
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

/** Remuneration_Method AD_Reference_ID=2000016 */
public static final int REMUNERATION_METHOD_AD_Reference_ID=2000016;
/** daily = daily */
public static final String REMUNERATION_METHOD_Daily = "daily";
/** hourly = hourly */
public static final String REMUNERATION_METHOD_Hourly = "hourly";
/** annual = annual */
public static final String REMUNERATION_METHOD_Annual = "annual";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isRemuneration_MethodValid (String test)
{
return test.equals("daily") || test.equals("hourly") || test.equals("annual");
}
/** Set Remuneration Method.
@param Remuneration_Method   */
public void setRemuneration_Method (String Remuneration_Method)
{
if (Remuneration_Method == null) throw new IllegalArgumentException ("Remuneration_Method is mandatory");
if (!isRemuneration_MethodValid(Remuneration_Method))
throw new IllegalArgumentException ("Remuneration_Method Invalid value - " + Remuneration_Method + " - Reference_ID=2000016 - daily - hourly - annual");
if (Remuneration_Method.length() > 30)
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
/** Set Salary Per Pay.
@param SalaryPerPay Salary Per Pay */
public void setSalaryPerPay (BigDecimal SalaryPerPay)
{
throw new IllegalArgumentException ("SalaryPerPay is virtual column");
}
/** Get Salary Per Pay.
@return Salary Per Pay */
public BigDecimal getSalaryPerPay() 
{
BigDecimal bd = (BigDecimal)get_Value("SalaryPerPay");
if (bd == null) return Env.ZERO;
return bd;
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

/** Get Off-Scale Hourly Rate.
@return Off-Scale Hourly Rate */
public BigDecimal getOut_Of_Rate_Hourly_Rate() 
{
BigDecimal bd = (BigDecimal)get_Value("Out_Of_Rate_Hourly_Rate");
if (bd == null) return Env.ZERO;
return bd;
}



}
