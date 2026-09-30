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
/** Generated Model for P_Employee_Deduction_Hst
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Employee_Deduction_Hst extends PO
{
/** Standard Constructor
@param ctx context
@param P_Employee_Deduction_Hst_ID id
@param trxName transaction
*/
public X_P_Employee_Deduction_Hst (Properties ctx, int P_Employee_Deduction_Hst_ID, String trxName)
{
super (ctx, P_Employee_Deduction_Hst_ID, trxName);
/** if (P_Employee_Deduction_Hst_ID == 0)
{
setEffectIn (new Timestamp(System.currentTimeMillis()));
setP_Deduction_ID (0);
setP_Employee_Deduction_Hst_ID (0);
setP_Employee_Deduction_ID (0);
setP_Employee_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Employee_Deduction_Hst (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000156 */
public static final int Table_ID=2000156;

/** TableName=P_Employee_Deduction_Hst */
public static final String Table_Name="P_Employee_Deduction_Hst";

protected static KeyNamePair Model = new KeyNamePair(2000156,"P_Employee_Deduction_Hst");

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
StringBuffer sb = new StringBuffer ("X_P_Employee_Deduction_Hst[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Back Payment.
@param BackPayment Back Payment */
public void setBackPayment (BigDecimal BackPayment)
{
set_Value ("BackPayment", BackPayment);
}
/** Get Back Payment.
@return Back Payment */
public BigDecimal getBackPayment() 
{
BigDecimal bd = (BigDecimal)get_Value("BackPayment");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Back Payment by Period.
@param BackPaymentPeriod Back Payment by Period */
public void setBackPaymentPeriod (BigDecimal BackPaymentPeriod)
{
set_Value ("BackPaymentPeriod", BackPaymentPeriod);
}
/** Get Back Payment by Period.
@return Back Payment by Period */
public BigDecimal getBackPaymentPeriod() 
{
BigDecimal bd = (BigDecimal)get_Value("BackPaymentPeriod");
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
/** Set Employee amount.
@param EmployeeAmount Employee amount */
public void setEmployeeAmount (BigDecimal EmployeeAmount)
{
set_Value ("EmployeeAmount", EmployeeAmount);
}
/** Get Employee amount.
@return Employee amount */
public BigDecimal getEmployeeAmount() 
{
BigDecimal bd = (BigDecimal)get_Value("EmployeeAmount");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Employee rate.
@param EmployeeRate Employee rate */
public void setEmployeeRate (BigDecimal EmployeeRate)
{
set_Value ("EmployeeRate", EmployeeRate);
}
/** Get Employee rate.
@return Employee rate */
public BigDecimal getEmployeeRate() 
{
BigDecimal bd = (BigDecimal)get_Value("EmployeeRate");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Employer amount.
@param EmployerAmount Employer amount */
public void setEmployerAmount (BigDecimal EmployerAmount)
{
set_Value ("EmployerAmount", EmployerAmount);
}
/** Get Employer amount.
@return Employer amount */
public BigDecimal getEmployerAmount() 
{
BigDecimal bd = (BigDecimal)get_Value("EmployerAmount");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Employer rate.
@param EmployerRate Employer rate */
public void setEmployerRate (BigDecimal EmployerRate)
{
set_Value ("EmployerRate", EmployerRate);
}
/** Get Employer rate.
@return Employer rate */
public BigDecimal getEmployerRate() 
{
BigDecimal bd = (BigDecimal)get_Value("EmployerRate");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Exemption Amount.
@param ExemptionAmount Exemption Amount */
public void setExemptionAmount (BigDecimal ExemptionAmount)
{
set_Value ("ExemptionAmount", ExemptionAmount);
}
/** Get Exemption Amount.
@return Exemption Amount */
public BigDecimal getExemptionAmount() 
{
BigDecimal bd = (BigDecimal)get_Value("ExemptionAmount");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Max Yearly.
@param MaxYearly Max Yearly */
public void setMaxYearly (BigDecimal MaxYearly)
{
set_Value ("MaxYearly", MaxYearly);
}
/** Get Max Yearly.
@return Max Yearly */
public BigDecimal getMaxYearly() 
{
BigDecimal bd = (BigDecimal)get_Value("MaxYearly");
if (bd == null) return Env.ZERO;
return bd;
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
/** Set P_Employee_Deduction_Hst_ID.
@param P_Employee_Deduction_Hst_ID P_Employee_Deduction_Hst_ID */
public void setP_Employee_Deduction_Hst_ID (int P_Employee_Deduction_Hst_ID)
{
if (P_Employee_Deduction_Hst_ID < 1) throw new IllegalArgumentException ("P_Employee_Deduction_Hst_ID is mandatory.");
set_Value ("P_Employee_Deduction_Hst_ID", new Integer(P_Employee_Deduction_Hst_ID));
}
/** Get P_Employee_Deduction_Hst_ID.
@return P_Employee_Deduction_Hst_ID */
public int getP_Employee_Deduction_Hst_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Deduction_Hst_ID");
if (ii == null) return 0;
return ii.intValue();
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

/** P_Employee_ID AD_Reference_ID=2000010 */
public static final int P_EMPLOYEE_ID_AD_Reference_ID=2000010;
/** Set Employee .
@param P_Employee_ID Employee */
public void setP_Employee_ID (int P_Employee_ID)
{
if (P_Employee_ID < 1) throw new IllegalArgumentException ("P_Employee_ID is mandatory.");
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
}
