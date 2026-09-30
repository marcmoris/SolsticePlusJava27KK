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
/** Generated Model for P_ExpenseCorrection
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.4 2008/03/12 20:02:49 marmor01 Exp $ */
public class X_P_ExpenseCorrection extends PO
{
/** Standard Constructor
@param ctx context
@param P_ExpenseCorrection_ID id
@param trxName transaction
*/
public X_P_ExpenseCorrection (Properties ctx, int P_ExpenseCorrection_ID, String trxName)
{
super (ctx, P_ExpenseCorrection_ID, trxName);
/** if (P_ExpenseCorrection_ID == 0)
{
setDescription (null);
setP_ExpenseCorrection_ID (0);
setP_Period_End_ID (0);
setP_Period_Start_ID (0);
setValue (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_ExpenseCorrection (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100698 */
public static final int Table_ID=2100698;

/** TableName=P_ExpenseCorrection */
public static final String Table_Name="P_ExpenseCorrection";

protected static KeyNamePair Model = new KeyNamePair(2100698,"P_ExpenseCorrection");

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
StringBuffer sb = new StringBuffer ("X_P_ExpenseCorrection[").append(get_ID()).append("]");
return sb.toString();
}

/** AD_Org_Dst_ID AD_Reference_ID=322 */
public static final int AD_ORG_DST_ID_AD_Reference_ID=322;
/** Set Division.
@param AD_Org_Dst_ID Division */
public void setAD_Org_Dst_ID (int AD_Org_Dst_ID)
{
if (AD_Org_Dst_ID <= 0) set_Value ("AD_Org_Dst_ID", null);
 else 
set_Value ("AD_Org_Dst_ID", new Integer(AD_Org_Dst_ID));
}
/** Get Division.
@return Division */
public int getAD_Org_Dst_ID() 
{
Integer ii = (Integer)get_Value("AD_Org_Dst_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** AD_Org_Src_ID AD_Reference_ID=322 */
public static final int AD_ORG_SRC_ID_AD_Reference_ID=322;
/** Set Division.
@param AD_Org_Src_ID Division */
public void setAD_Org_Src_ID (int AD_Org_Src_ID)
{
if (AD_Org_Src_ID <= 0) set_Value ("AD_Org_Src_ID", null);
 else 
set_Value ("AD_Org_Src_ID", new Integer(AD_Org_Src_ID));
}
/** Get Division.
@return Division */
public int getAD_Org_Src_ID() 
{
Integer ii = (Integer)get_Value("AD_Org_Src_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Amount.
@param Amount Amount in a defined currency */
public void setAmount (BigDecimal Amount)
{
set_Value ("Amount", Amount);
}
/** Get Amount.
@return Amount in a defined currency */
public BigDecimal getAmount() 
{
BigDecimal bd = (BigDecimal)get_Value("Amount");
if (bd == null) return Env.ZERO;
return bd;
}

/** C_Activity_Dst_ID AD_Reference_ID=142 */
public static final int C_ACTIVITY_DST_ID_AD_Reference_ID=142;
/** Set Cost center.
@param C_Activity_Dst_ID Cost center */
public void setC_Activity_Dst_ID (int C_Activity_Dst_ID)
{
if (C_Activity_Dst_ID <= 0) set_Value ("C_Activity_Dst_ID", null);
 else 
set_Value ("C_Activity_Dst_ID", new Integer(C_Activity_Dst_ID));
}
/** Get Cost center.
@return Cost center */
public int getC_Activity_Dst_ID() 
{
Integer ii = (Integer)get_Value("C_Activity_Dst_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** C_Activity_Src_ID AD_Reference_ID=142 */
public static final int C_ACTIVITY_SRC_ID_AD_Reference_ID=142;
/** Set Cost center.
@param C_Activity_Src_ID Cost center */
public void setC_Activity_Src_ID (int C_Activity_Src_ID)
{
if (C_Activity_Src_ID <= 0) set_Value ("C_Activity_Src_ID", null);
 else 
set_Value ("C_Activity_Src_ID", new Integer(C_Activity_Src_ID));
}
/** Get Cost center.
@return Cost center */
public int getC_Activity_Src_ID() 
{
Integer ii = (Integer)get_Value("C_Activity_Src_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** C_ElementValue_Dst_ID AD_Reference_ID=182 */
public static final int C_ELEMENTVALUE_DST_ID_AD_Reference_ID=182;
/** Set Account Element.
@param C_ElementValue_Dst_ID Account Element */
public void setC_ElementValue_Dst_ID (int C_ElementValue_Dst_ID)
{
if (C_ElementValue_Dst_ID <= 0) set_Value ("C_ElementValue_Dst_ID", null);
 else 
set_Value ("C_ElementValue_Dst_ID", new Integer(C_ElementValue_Dst_ID));
}
/** Get Account Element.
@return Account Element */
public int getC_ElementValue_Dst_ID() 
{
Integer ii = (Integer)get_Value("C_ElementValue_Dst_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** C_ElementValue_MP_Dst_ID AD_Reference_ID=182 */
public static final int C_ELEMENTVALUE_MP_DST_ID_AD_Reference_ID=182;
/** Set Marginal Benefits Accounts.
@param C_ElementValue_MP_Dst_ID Marginal Benefits Accounts */
public void setC_ElementValue_MP_Dst_ID (int C_ElementValue_MP_Dst_ID)
{
if (C_ElementValue_MP_Dst_ID <= 0) set_Value ("C_ElementValue_MP_Dst_ID", null);
 else 
set_Value ("C_ElementValue_MP_Dst_ID", new Integer(C_ElementValue_MP_Dst_ID));
}
/** Get Marginal Benefits Accounts.
@return Marginal Benefits Accounts */
public int getC_ElementValue_MP_Dst_ID() 
{
Integer ii = (Integer)get_Value("C_ElementValue_MP_Dst_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** C_ElementValue_MP_Src_ID AD_Reference_ID=182 */
public static final int C_ELEMENTVALUE_MP_SRC_ID_AD_Reference_ID=182;
/** Set Marginal Benefits Accounts.
@param C_ElementValue_MP_Src_ID Marginal Benefits Accounts */
public void setC_ElementValue_MP_Src_ID (int C_ElementValue_MP_Src_ID)
{
if (C_ElementValue_MP_Src_ID <= 0) set_Value ("C_ElementValue_MP_Src_ID", null);
 else 
set_Value ("C_ElementValue_MP_Src_ID", new Integer(C_ElementValue_MP_Src_ID));
}
/** Get Marginal Benefits Accounts.
@return Marginal Benefits Accounts */
public int getC_ElementValue_MP_Src_ID() 
{
Integer ii = (Integer)get_Value("C_ElementValue_MP_Src_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** C_ElementValue_Src_ID AD_Reference_ID=182 */
public static final int C_ELEMENTVALUE_SRC_ID_AD_Reference_ID=182;
/** Set Account Element.
@param C_ElementValue_Src_ID Account Element */
public void setC_ElementValue_Src_ID (int C_ElementValue_Src_ID)
{
if (C_ElementValue_Src_ID <= 0) set_Value ("C_ElementValue_Src_ID", null);
 else 
set_Value ("C_ElementValue_Src_ID", new Integer(C_ElementValue_Src_ID));
}
/** Get Account Element.
@return Account Element */
public int getC_ElementValue_Src_ID() 
{
Integer ii = (Integer)get_Value("C_ElementValue_Src_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** C_Period_ID AD_Reference_ID=275 */
public static final int C_PERIOD_ID_AD_Reference_ID=275;
/** Set Period.
@param C_Period_ID Period of the Calendar */
public void setC_Period_ID (int C_Period_ID)
{
if (C_Period_ID <= 0) set_Value ("C_Period_ID", null);
 else 
set_Value ("C_Period_ID", new Integer(C_Period_ID));
}
/** Get Period.
@return Period of the Calendar */
public int getC_Period_ID() 
{
Integer ii = (Integer)get_Value("C_Period_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Base.
@param C_SalesRegion_Dst_ID Base */
public void setC_SalesRegion_Dst_ID (int C_SalesRegion_Dst_ID)
{
if (C_SalesRegion_Dst_ID <= 0) set_Value ("C_SalesRegion_Dst_ID", null);
 else 
set_Value ("C_SalesRegion_Dst_ID", new Integer(C_SalesRegion_Dst_ID));
}
/** Get Base.
@return Base */
public int getC_SalesRegion_Dst_ID() 
{
Integer ii = (Integer)get_Value("C_SalesRegion_Dst_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Base.
@param C_SalesRegion_Src_ID Base */
public void setC_SalesRegion_Src_ID (int C_SalesRegion_Src_ID)
{
if (C_SalesRegion_Src_ID <= 0) set_Value ("C_SalesRegion_Src_ID", null);
 else 
set_Value ("C_SalesRegion_Src_ID", new Integer(C_SalesRegion_Src_ID));
}
/** Get Base.
@return Base */
public int getC_SalesRegion_Src_ID() 
{
Integer ii = (Integer)get_Value("C_SalesRegion_Src_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Description.
@param Description Optional short description of the record */
public void setDescription (String Description)
{
if (Description == null) throw new IllegalArgumentException ("Description is mandatory.");
if (Description.length() > 255)
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
/** Set Marginal Benefits Accounts.
@param MPAccount_DST Marginal Benefits Accounts */
public void setMPAccount_DST (boolean MPAccount_DST)
{
set_Value ("MPAccount_DST", new Boolean(MPAccount_DST));
}
/** Get Marginal Benefits Accounts.
@return Marginal Benefits Accounts */
public boolean isMPAccount_DST() 
{
Object oo = get_Value("MPAccount_DST");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Marginal Benefits Accounts.
@param MPAccount_SRC Marginal Benefits Accounts */
public void setMPAccount_SRC (boolean MPAccount_SRC)
{
set_Value ("MPAccount_SRC", new Boolean(MPAccount_SRC));
}
/** Get Marginal Benefits Accounts.
@return Marginal Benefits Accounts */
public boolean isMPAccount_SRC() 
{
Object oo = get_Value("MPAccount_SRC");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Document Number.
@param P_Accounting_Entry_ID Document Number */
public void setP_Accounting_Entry_ID (int P_Accounting_Entry_ID)
{
if (P_Accounting_Entry_ID <= 0) set_Value ("P_Accounting_Entry_ID", null);
 else 
set_Value ("P_Accounting_Entry_ID", new Integer(P_Accounting_Entry_ID));
}
/** Get Document Number.
@return Document Number */
public int getP_Accounting_Entry_ID() 
{
Integer ii = (Integer)get_Value("P_Accounting_Entry_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Employee_ID AD_Reference_ID=2000010 */
public static final int P_EMPLOYEE_ID_AD_Reference_ID=2000010;
/** Set Employee.
@param P_Employee_ID Employee */
public void setP_Employee_ID (int P_Employee_ID)
{
if (P_Employee_ID <= 0) set_Value ("P_Employee_ID", null);
 else 
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
/** Set Expense Correction.
@param P_ExpenseCorrection_ID Expense Correction */
public void setP_ExpenseCorrection_ID (int P_ExpenseCorrection_ID)
{
if (P_ExpenseCorrection_ID < 1) throw new IllegalArgumentException ("P_ExpenseCorrection_ID is mandatory.");
set_Value ("P_ExpenseCorrection_ID", new Integer(P_ExpenseCorrection_ID));
}
/** Get Expense Correction.
@return Expense Correction */
public int getP_ExpenseCorrection_ID() 
{
Integer ii = (Integer)get_Value("P_ExpenseCorrection_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Gain_ID AD_Reference_ID=2100317 */
public static final int P_GAIN_ID_AD_Reference_ID=2100317;
/** Set Pay Codes.
@param P_Gain_ID Pay Codes */
public void setP_Gain_ID (int P_Gain_ID)
{
if (P_Gain_ID <= 0) set_Value ("P_Gain_ID", null);
 else 
set_Value ("P_Gain_ID", new Integer(P_Gain_ID));
}
/** Get Pay Codes.
@return Pay Codes */
public int getP_Gain_ID() 
{
Integer ii = (Integer)get_Value("P_Gain_ID");
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

/** P_Period_End_ID AD_Reference_ID=2000009 */
public static final int P_PERIOD_END_ID_AD_Reference_ID=2000009;
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

/** P_Period_Start_ID AD_Reference_ID=2000009 */
public static final int P_PERIOD_START_ID_AD_Reference_ID=2000009;
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
/** Set Rate.
@param Rate Rate or Tax or Exchange */
public void setRate (BigDecimal Rate)
{
set_Value ("Rate", Rate);
}
/** Get Rate.
@return Rate or Tax or Exchange */
public BigDecimal getRate() 
{
BigDecimal bd = (BigDecimal)get_Value("Rate");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Total Amount.
@param TotalAmt Total Amount */
public void setTotalAmt (BigDecimal TotalAmt)
{
throw new IllegalArgumentException ("TotalAmt is virtual column");
}
/** Get Total Amount.
@return Total Amount */
public BigDecimal getTotalAmt() 
{
BigDecimal bd = (BigDecimal)get_Value("TotalAmt");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Search Key.
@param Value Search key for the record in the format required - must be unique */
public void setValue (String Value)
{
if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
if (Value.length() > 10)
{
log.warning("Length > 10 - truncated");
Value = Value.substring(0,9);
}
set_Value ("Value", Value);
}
/** Get Search Key.
@return Search key for the record in the format required - must be unique */
public String getValue() 
{
return (String)get_Value("Value");
}
}
