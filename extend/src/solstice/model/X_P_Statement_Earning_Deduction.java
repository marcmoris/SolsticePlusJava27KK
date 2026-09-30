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
/** Generated Model for P_Statement_Earning_Deduction
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.4 2008/03/12 20:02:49 marmor01 Exp $ */
public class X_P_Statement_Earning_Deduction extends PO
{
/** Standard Constructor
@param ctx context
@param P_Statement_Earning_Deduction_ID id
@param trxName transaction
*/
public X_P_Statement_Earning_Deduction (Properties ctx, int P_Statement_Earning_Deduction_ID, String trxName)
{
super (ctx, P_Statement_Earning_Deduction_ID, trxName);
/** if (P_Statement_Earning_Deduction_ID == 0)
{
setInsurance (false);
setP_Deduction_ID (0);
setP_Statement_Earning_Deduction_ID (0);
setP_Statement_Earning_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Statement_Earning_Deduction (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100731 */
public static final int Table_ID=2100731;

/** TableName=P_Statement_Earning_Deduction */
public static final String Table_Name="P_Statement_Earning_Deduction";

protected static KeyNamePair Model = new KeyNamePair(2100731,"P_Statement_Earning_Deduction");

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
StringBuffer sb = new StringBuffer ("X_P_Statement_Earning_Deduction[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Cumulative Amount.
@param Amountcumul Cumulative Amount */
public void setAmountcumul (BigDecimal Amountcumul)
{
set_Value ("Amountcumul", Amountcumul);
}
/** Get Cumulative Amount.
@return Cumulative Amount */
public BigDecimal getAmountcumul() 
{
BigDecimal bd = (BigDecimal)get_Value("Amountcumul");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Periodic Amount.
@param AmountPeriod Periodic Amount */
public void setAmountPeriod (BigDecimal AmountPeriod)
{
set_Value ("AmountPeriod", AmountPeriod);
}
/** Get Periodic Amount.
@return Periodic Amount */
public BigDecimal getAmountPeriod() 
{
BigDecimal bd = (BigDecimal)get_Value("AmountPeriod");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Balance.
@param Balance Balance */
public void setBalance (BigDecimal Balance)
{
set_Value ("Balance", Balance);
}
/** Get Balance.
@return Balance */
public BigDecimal getBalance() 
{
BigDecimal bd = (BigDecimal)get_Value("Balance");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Description.
@param Description Optional short description of the record */
public void setDescription (String Description)
{
if (Description != null && Description.length() > 255)
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
/** Set Insurance.
@param Insurance Insurance */
public void setInsurance (boolean Insurance)
{
set_Value ("Insurance", new Boolean(Insurance));
}
/** Get Insurance.
@return Insurance */
public boolean isInsurance() 
{
Object oo = get_Value("Insurance");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Annual Deductions.
@param IsAnnual Annual Deductions */
public void setIsAnnual (boolean IsAnnual)
{
set_Value ("IsAnnual", new Boolean(IsAnnual));
}
/** Get Annual Deductions.
@return Annual Deductions */
public boolean isAnnual() 
{
Object oo = get_Value("IsAnnual");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Group Insurance.
@param IsBaseInsurance Group Insurance */
public void setIsBaseInsurance (boolean IsBaseInsurance)
{
set_Value ("IsBaseInsurance", new Boolean(IsBaseInsurance));
}
/** Get Group Insurance.
@return Group Insurance */
public boolean isBaseInsurance() 
{
Object oo = get_Value("IsBaseInsurance");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
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
/** Set Earnings Statement - Deductions.
@param P_Statement_Earning_Deduction_ID Earnings Statement - Deductions */
public void setP_Statement_Earning_Deduction_ID (int P_Statement_Earning_Deduction_ID)
{
if (P_Statement_Earning_Deduction_ID < 1) throw new IllegalArgumentException ("P_Statement_Earning_Deduction_ID is mandatory.");
set_ValueNoCheck ("P_Statement_Earning_Deduction_ID", new Integer(P_Statement_Earning_Deduction_ID));
}
/** Get Earnings Statement - Deductions.
@return Earnings Statement - Deductions */
public int getP_Statement_Earning_Deduction_ID() 
{
Integer ii = (Integer)get_Value("P_Statement_Earning_Deduction_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_statement_earning_id.
@param P_Statement_Earning_ID P_statement_earning_id */
public void setP_Statement_Earning_ID (int P_Statement_Earning_ID)
{
if (P_Statement_Earning_ID < 1) throw new IllegalArgumentException ("P_Statement_Earning_ID is mandatory.");
set_ValueNoCheck ("P_Statement_Earning_ID", new Integer(P_Statement_Earning_ID));
}
/** Get P_statement_earning_id.
@return P_statement_earning_id */
public int getP_Statement_Earning_ID() 
{
Integer ii = (Integer)get_Value("P_Statement_Earning_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
