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
/** Generated Model for P_Statement_Earning_Taxable_Benefits
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Statement_Earning_Taxable_Benefits extends PO
{
/** Standard Constructor
@param ctx context
@param P_Statement_Earning_Taxable_Benefits_ID id
@param trxName transaction
*/
public X_P_Statement_Earning_Taxable_Benefits (Properties ctx, int P_Statement_Earning_Taxable_Benefits_ID, String trxName)
{
super (ctx, P_Statement_Earning_Taxable_Benefits_ID, trxName);
/** if (P_Statement_Earning_Taxable_Benefits_ID == 0)
{
setP_Statement_Earning_ID (0);
setP_Statement_Earning_Taxable_Benefits_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Statement_Earning_Taxable_Benefits (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100733 */
public static final int Table_ID=2100733;

/** TableName=P_Statement_Earning_Taxable_Benefits */
public static final String Table_Name="P_Statement_Earning_Taxable_Benefits";

protected static KeyNamePair Model = new KeyNamePair(2100733,"P_Statement_Earning_Taxable_Benefits");

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
StringBuffer sb = new StringBuffer ("X_P_Statement_Earning_Taxable_Benefits[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Amountcumul.
@param Amountcumul Amountcumul */
public void setAmountcumul (BigDecimal Amountcumul)
{
set_Value ("Amountcumul", Amountcumul);
}
/** Get Amountcumul.
@return Amountcumul */
public BigDecimal getAmountcumul() 
{
BigDecimal bd = (BigDecimal)get_Value("Amountcumul");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Amount Period.
@param AmountPeriod Amount Period */
public void setAmountPeriod (BigDecimal AmountPeriod)
{
set_Value ("AmountPeriod", AmountPeriod);
}
/** Get Amount Period.
@return Amount Period */
public BigDecimal getAmountPeriod() 
{
BigDecimal bd = (BigDecimal)get_Value("AmountPeriod");
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
/** Set P_statement_earning_id.
@param P_Statement_Earning_ID P_statement_earning_id */
public void setP_Statement_Earning_ID (int P_Statement_Earning_ID)
{
if (P_Statement_Earning_ID < 1) throw new IllegalArgumentException ("P_Statement_Earning_ID is mandatory.");
set_Value ("P_Statement_Earning_ID", new Integer(P_Statement_Earning_ID));
}
/** Get P_statement_earning_id.
@return P_statement_earning_id */
public int getP_Statement_Earning_ID() 
{
Integer ii = (Integer)get_Value("P_Statement_Earning_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Taxable Benefits ID.
@param P_Statement_Earning_Taxable_Benefits_ID Taxable Benefits ID */
public void setP_Statement_Earning_Taxable_Benefits_ID (int P_Statement_Earning_Taxable_Benefits_ID)
{
if (P_Statement_Earning_Taxable_Benefits_ID < 1) throw new IllegalArgumentException ("P_Statement_Earning_Taxable_Benefits_ID is mandatory.");
set_ValueNoCheck ("P_Statement_Earning_Taxable_Benefits_ID", new Integer(P_Statement_Earning_Taxable_Benefits_ID));
}
/** Get Taxable Benefits ID.
@return Taxable Benefits ID */
public int getP_Statement_Earning_Taxable_Benefits_ID() 
{
Integer ii = (Integer)get_Value("P_Statement_Earning_Taxable_Benefits_ID");
if (ii == null) return 0;
return ii.intValue();
}
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
}
