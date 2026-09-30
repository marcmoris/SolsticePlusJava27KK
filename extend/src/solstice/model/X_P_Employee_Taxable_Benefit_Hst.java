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
/** Generated Model for P_Employee_Taxable_Benefit_Hst
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Employee_Taxable_Benefit_Hst extends PO
{
/** Standard Constructor
@param ctx context
@param P_Employee_Taxable_Benefit_Hst_ID id
@param trxName transaction
*/
public X_P_Employee_Taxable_Benefit_Hst (Properties ctx, int P_Employee_Taxable_Benefit_Hst_ID, String trxName)
{
super (ctx, P_Employee_Taxable_Benefit_Hst_ID, trxName);
/** if (P_Employee_Taxable_Benefit_Hst_ID == 0)
{
setEffectIn (new Timestamp(System.currentTimeMillis()));
setP_Employee_ID (0);
setP_Employee_Taxable_Benefit_Hst_ID (0);
setP_Employee_Taxable_Benefit_ID (0);
setP_Taxable_Benefit_ID (0);
setTaxB_Fixed_Amount (Env.ZERO);
setTaxB_Rate (Env.ZERO);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Employee_Taxable_Benefit_Hst (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100676 */
public static final int Table_ID=2100676;

/** TableName=P_Employee_Taxable_Benefit_Hst */
public static final String Table_Name="P_Employee_Taxable_Benefit_Hst";

protected static KeyNamePair Model = new KeyNamePair(2100676,"P_Employee_Taxable_Benefit_Hst");

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
StringBuffer sb = new StringBuffer ("X_P_Employee_Taxable_Benefit_Hst[").append(get_ID()).append("]");
return sb.toString();
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
/** Set Taxable Benefit.
@param P_Employee_Taxable_Benefit_Hst_ID Taxable Benefit */
public void setP_Employee_Taxable_Benefit_Hst_ID (int P_Employee_Taxable_Benefit_Hst_ID)
{
if (P_Employee_Taxable_Benefit_Hst_ID < 1) throw new IllegalArgumentException ("P_Employee_Taxable_Benefit_Hst_ID is mandatory.");
set_ValueNoCheck ("P_Employee_Taxable_Benefit_Hst_ID", new Integer(P_Employee_Taxable_Benefit_Hst_ID));
}
/** Get Taxable Benefit.
@return Taxable Benefit */
public int getP_Employee_Taxable_Benefit_Hst_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Taxable_Benefit_Hst_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Taxable Benefit.
@param P_Employee_Taxable_Benefit_ID Taxable Benefit */
public void setP_Employee_Taxable_Benefit_ID (int P_Employee_Taxable_Benefit_ID)
{
if (P_Employee_Taxable_Benefit_ID < 1) throw new IllegalArgumentException ("P_Employee_Taxable_Benefit_ID is mandatory.");
set_Value ("P_Employee_Taxable_Benefit_ID", new Integer(P_Employee_Taxable_Benefit_ID));
}
/** Get Taxable Benefit.
@return Taxable Benefit */
public int getP_Employee_Taxable_Benefit_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Taxable_Benefit_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Taxable_Benefit_ID AD_Reference_ID=2000046 */
public static final int P_TAXABLE_BENEFIT_ID_AD_Reference_ID=2000046;
/** Set Taxable Benefit.
@param P_Taxable_Benefit_ID Taxable Benefit */
public void setP_Taxable_Benefit_ID (int P_Taxable_Benefit_ID)
{
if (P_Taxable_Benefit_ID < 1) throw new IllegalArgumentException ("P_Taxable_Benefit_ID is mandatory.");
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
/** Set Fixed Amount.
@param TaxB_Fixed_Amount Fixed Amount */
public void setTaxB_Fixed_Amount (BigDecimal TaxB_Fixed_Amount)
{
if (TaxB_Fixed_Amount == null) throw new IllegalArgumentException ("TaxB_Fixed_Amount is mandatory.");
set_Value ("TaxB_Fixed_Amount", TaxB_Fixed_Amount);
}
/** Get Fixed Amount.
@return Fixed Amount */
public BigDecimal getTaxB_Fixed_Amount() 
{
BigDecimal bd = (BigDecimal)get_Value("TaxB_Fixed_Amount");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Rate.
@param TaxB_Rate Rate */
public void setTaxB_Rate (BigDecimal TaxB_Rate)
{
if (TaxB_Rate == null) throw new IllegalArgumentException ("TaxB_Rate is mandatory.");
set_Value ("TaxB_Rate", TaxB_Rate);
}
/** Get Rate.
@return Rate */
public BigDecimal getTaxB_Rate() 
{
BigDecimal bd = (BigDecimal)get_Value("TaxB_Rate");
if (bd == null) return Env.ZERO;
return bd;
}
}
