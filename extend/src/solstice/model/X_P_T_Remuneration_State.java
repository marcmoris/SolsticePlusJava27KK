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
/** Generated Model for P_T_Remuneration_State
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_T_Remuneration_State extends PO
{
/** Standard Constructor
@param ctx context
@param P_T_Remuneration_State_ID id
@param trxName transaction
*/
public X_P_T_Remuneration_State (Properties ctx, int P_T_Remuneration_State_ID, String trxName)
{
super (ctx, P_T_Remuneration_State_ID, trxName);
/** if (P_T_Remuneration_State_ID == 0)
{
setAD_PInstance_ID (0);
setEndDate (new Timestamp(System.currentTimeMillis()));
setLine (0);
setP_Job_Title_ID (0);
setP_Period_End_ID (0);
setP_Period_Start_ID (0);
setP_T_Remuneration_State_ID (0);
setStartDate (new Timestamp(System.currentTimeMillis()));
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_T_Remuneration_State (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100715 */
public static final int Table_ID=2100715;

/** TableName=P_T_Remuneration_State */
public static final String Table_Name="P_T_Remuneration_State";

protected static KeyNamePair Model = new KeyNamePair(2100715,"P_T_Remuneration_State");

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
StringBuffer sb = new StringBuffer ("X_P_T_Remuneration_State[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Process Instance.
@param AD_PInstance_ID Instance of the process */
public void setAD_PInstance_ID (int AD_PInstance_ID)
{
if (AD_PInstance_ID < 1) throw new IllegalArgumentException ("AD_PInstance_ID is mandatory.");
set_Value ("AD_PInstance_ID", new Integer(AD_PInstance_ID));
}
/** Get Process Instance.
@return Instance of the process */
public int getAD_PInstance_ID() 
{
Integer ii = (Integer)get_Value("AD_PInstance_ID");
if (ii == null) return 0;
return ii.intValue();
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
/** Set Employee Part.
@param Employee_Part Employee Part */
public void setEmployee_Part (BigDecimal Employee_Part)
{
set_Value ("Employee_Part", Employee_Part);
}
/** Get Employee Part.
@return Employee Part */
public BigDecimal getEmployee_Part() 
{
BigDecimal bd = (BigDecimal)get_Value("Employee_Part");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Employer Part.
@param Employer_Part Employer Part */
public void setEmployer_Part (BigDecimal Employer_Part)
{
set_Value ("Employer_Part", Employer_Part);
}
/** Get Employer Part.
@return Employer Part */
public BigDecimal getEmployer_Part() 
{
BigDecimal bd = (BigDecimal)get_Value("Employer_Part");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set End Date.
@param EndDate Last effective date (inclusive) */
public void setEndDate (Timestamp EndDate)
{
if (EndDate == null) throw new IllegalArgumentException ("EndDate is mandatory.");
set_Value ("EndDate", EndDate);
}
/** Get End Date.
@return Last effective date (inclusive) */
public Timestamp getEndDate() 
{
return (Timestamp)get_Value("EndDate");
}
/** Set Hours Eligible.
@param Hours_Eligible Hours Eligible */
public void setHours_Eligible (BigDecimal Hours_Eligible)
{
set_Value ("Hours_Eligible", Hours_Eligible);
}
/** Get Hours Eligible.
@return Hours Eligible */
public BigDecimal getHours_Eligible() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours_Eligible");
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
/** Set Line Type.
@param LineType Line Type */
public void setLineType (String LineType)
{
if (LineType != null && LineType.length() > 1)
{
log.warning("Length > 1 - truncated");
LineType = LineType.substring(0,0);
}
set_Value ("LineType", LineType);
}
/** Get Line Type.
@return Line Type */
public String getLineType() 
{
return (String)get_Value("LineType");
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

/** P_Gain_ID AD_Reference_ID=2100317 */
public static final int P_GAIN_ID_AD_Reference_ID=2100317;
/** Set Gain.
@param P_Gain_ID Gain */
public void setP_Gain_ID (int P_Gain_ID)
{
if (P_Gain_ID <= 0) set_Value ("P_Gain_ID", null);
 else 
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

/** P_Job_Title_ID AD_Reference_ID=2000025 */
public static final int P_JOB_TITLE_ID_AD_Reference_ID=2000025;
/** Set Job Title.
@param P_Job_Title_ID   */
public void setP_Job_Title_ID (int P_Job_Title_ID)
{
if (P_Job_Title_ID < 1) throw new IllegalArgumentException ("P_Job_Title_ID is mandatory.");
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
/** Set Ending Period.
@param P_Period_End_ID Ending Period */
public void setP_Period_End_ID (int P_Period_End_ID)
{
if (P_Period_End_ID < 1) throw new IllegalArgumentException ("P_Period_End_ID is mandatory.");
set_Value ("P_Period_End_ID", new Integer(P_Period_End_ID));
}
/** Get Ending Period.
@return Ending Period */
public int getP_Period_End_ID() 
{
Integer ii = (Integer)get_Value("P_Period_End_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Starting Period.
@param P_Period_Start_ID Starting Period */
public void setP_Period_Start_ID (int P_Period_Start_ID)
{
if (P_Period_Start_ID < 1) throw new IllegalArgumentException ("P_Period_Start_ID is mandatory.");
set_Value ("P_Period_Start_ID", new Integer(P_Period_Start_ID));
}
/** Get Starting Period.
@return Starting Period */
public int getP_Period_Start_ID() 
{
Integer ii = (Integer)get_Value("P_Period_Start_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Remuneration State.
@param P_T_Remuneration_State_ID Remuneration State */
public void setP_T_Remuneration_State_ID (int P_T_Remuneration_State_ID)
{
if (P_T_Remuneration_State_ID < 1) throw new IllegalArgumentException ("P_T_Remuneration_State_ID is mandatory.");
set_ValueNoCheck ("P_T_Remuneration_State_ID", new Integer(P_T_Remuneration_State_ID));
}
/** Get Remuneration State.
@return Remuneration State */
public int getP_T_Remuneration_State_ID() 
{
Integer ii = (Integer)get_Value("P_T_Remuneration_State_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Taxable_Benefit_ID AD_Reference_ID=2000046 */
public static final int P_TAXABLE_BENEFIT_ID_AD_Reference_ID=2000046;
/** Set Taxable Benefit.
@param P_Taxable_Benefit_ID Taxable Benefit */
public void setP_Taxable_Benefit_ID (int P_Taxable_Benefit_ID)
{
if (P_Taxable_Benefit_ID <= 0) set_Value ("P_Taxable_Benefit_ID", null);
 else 
set_Value ("P_Taxable_Benefit_ID", new Integer(P_Taxable_Benefit_ID));
}
/** Get Taxable Benefit.
@return Taxable Benefit */
public int getP_Taxable_Benefit_ID() 
{
Integer ii = (Integer)get_Value("P_Taxable_Benefit_ID");
if (ii == null) return 0;
return ii.intValue();
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
/** Set Salary Eligible.
@param Salary_Eligible Salary Eligible */
public void setSalary_Eligible (BigDecimal Salary_Eligible)
{
set_Value ("Salary_Eligible", Salary_Eligible);
}
/** Get Salary Eligible.
@return Salary Eligible */
public BigDecimal getSalary_Eligible() 
{
BigDecimal bd = (BigDecimal)get_Value("Salary_Eligible");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Start Date.
@param StartDate First effective day (inclusive) */
public void setStartDate (Timestamp StartDate)
{
if (StartDate == null) throw new IllegalArgumentException ("StartDate is mandatory.");
set_Value ("StartDate", StartDate);
}
/** Get Start Date.
@return First effective day (inclusive) */
public Timestamp getStartDate() 
{
return (Timestamp)get_Value("StartDate");
}
/** Set Amount.
@param Taxable_Benefit_Amount Amount */
public void setTaxable_Benefit_Amount (BigDecimal Taxable_Benefit_Amount)
{
set_Value ("Taxable_Benefit_Amount", Taxable_Benefit_Amount);
}
/** Get Amount.
@return Amount */
public BigDecimal getTaxable_Benefit_Amount() 
{
BigDecimal bd = (BigDecimal)get_Value("Taxable_Benefit_Amount");
if (bd == null) return Env.ZERO;
return bd;
}
}
