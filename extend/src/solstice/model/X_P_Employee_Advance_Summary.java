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
/** Generated Model for P_Employee_Advance_Summary
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Employee_Advance_Summary extends PO
{
/** Standard Constructor
@param ctx context
@param P_Employee_Advance_Summary_ID id
@param trxName transaction
*/
public X_P_Employee_Advance_Summary (Properties ctx, int P_Employee_Advance_Summary_ID, String trxName)
{
super (ctx, P_Employee_Advance_Summary_ID, trxName);
/** if (P_Employee_Advance_Summary_ID == 0)
{
setP_Advance_ID (0);
setP_Employee_Advance_ID (0);
setP_Employee_Advance_Summary_ID (0);
setP_Employee_ID (0);
setP_SpecialRate_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Employee_Advance_Summary (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100832 */
public static final int Table_ID=2100832;

/** TableName=P_Employee_Advance_Summary */
public static final String Table_Name="P_Employee_Advance_Summary";

protected static KeyNamePair Model = new KeyNamePair(2100832,"P_Employee_Advance_Summary");

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
StringBuffer sb = new StringBuffer ("X_P_Employee_Advance_Summary[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Amount.
@param AdvanceAmount Amount */
public void setAdvanceAmount (BigDecimal AdvanceAmount)
{
set_Value ("AdvanceAmount", AdvanceAmount);
}
/** Get Amount.
@return Amount */
public BigDecimal getAdvanceAmount() 
{
BigDecimal bd = (BigDecimal)get_Value("AdvanceAmount");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Balance.
@param AdvanceAmountBalance Balance */
public void setAdvanceAmountBalance (BigDecimal AdvanceAmountBalance)
{
set_Value ("AdvanceAmountBalance", AdvanceAmountBalance);
}
/** Get Balance.
@return Balance */
public BigDecimal getAdvanceAmountBalance() 
{
BigDecimal bd = (BigDecimal)get_Value("AdvanceAmountBalance");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Amount Refund.
@param AdvanceAmountRefund Amount Refund */
public void setAdvanceAmountRefund (BigDecimal AdvanceAmountRefund)
{
set_Value ("AdvanceAmountRefund", AdvanceAmountRefund);
}
/** Get Amount Refund.
@return Amount Refund */
public BigDecimal getAdvanceAmountRefund() 
{
BigDecimal bd = (BigDecimal)get_Value("AdvanceAmountRefund");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Advance.
@param P_Advance_ID Advance */
public void setP_Advance_ID (int P_Advance_ID)
{
if (P_Advance_ID < 1) throw new IllegalArgumentException ("P_Advance_ID is mandatory.");
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
/** Set Advance.
@param P_Employee_Advance_ID Advance */
public void setP_Employee_Advance_ID (int P_Employee_Advance_ID)
{
if (P_Employee_Advance_ID < 1) throw new IllegalArgumentException ("P_Employee_Advance_ID is mandatory.");
set_Value ("P_Employee_Advance_ID", new Integer(P_Employee_Advance_ID));
}
/** Get Advance.
@return Advance */
public int getP_Employee_Advance_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Advance_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_Employee_Advance_Summary_ID.
@param P_Employee_Advance_Summary_ID P_Employee_Advance_Summary_ID */
public void setP_Employee_Advance_Summary_ID (int P_Employee_Advance_Summary_ID)
{
if (P_Employee_Advance_Summary_ID < 1) throw new IllegalArgumentException ("P_Employee_Advance_Summary_ID is mandatory.");
set_ValueNoCheck ("P_Employee_Advance_Summary_ID", new Integer(P_Employee_Advance_Summary_ID));
}
/** Get P_Employee_Advance_Summary_ID.
@return P_Employee_Advance_Summary_ID */
public int getP_Employee_Advance_Summary_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Advance_Summary_ID");
if (ii == null) return 0;
return ii.intValue();
}
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
/** Get Record ID/ColumnName
@return ID/ColumnName pair */
public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), String.valueOf(getP_Employee_ID()));
}
/** Set Special Rate.
@param P_SpecialRate_ID Special Rate */
public void setP_SpecialRate_ID (int P_SpecialRate_ID)
{
if (P_SpecialRate_ID < 1) throw new IllegalArgumentException ("P_SpecialRate_ID is mandatory.");
set_Value ("P_SpecialRate_ID", new Integer(P_SpecialRate_ID));
}
/** Get Special Rate.
@return Special Rate */
public int getP_SpecialRate_ID() 
{
Integer ii = (Integer)get_Value("P_SpecialRate_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
