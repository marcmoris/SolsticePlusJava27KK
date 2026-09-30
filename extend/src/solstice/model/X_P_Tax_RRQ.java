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
/** Generated Model for P_Tax_RRQ
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Tax_RRQ extends PO
{
/** Standard Constructor
@param ctx context
@param P_Tax_RRQ_ID id
@param trxName transaction
*/
public X_P_Tax_RRQ (Properties ctx, int P_Tax_RRQ_ID, String trxName)
{
super (ctx, P_Tax_RRQ_ID, trxName);
/** if (P_Tax_RRQ_ID == 0)
{
setP_Tax_ID (0);
setP_Tax_Rrq_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Tax_RRQ (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000157 */
public static final int Table_ID=2000157;

/** TableName=P_Tax_RRQ */
public static final String Table_Name="P_Tax_RRQ";

protected static KeyNamePair Model = new KeyNamePair(2000157,"P_Tax_RRQ");

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
StringBuffer sb = new StringBuffer ("X_P_Tax_RRQ[").append(get_ID()).append("]");
return sb.toString();
}

/** AD_Message_ID AD_Reference_ID=102 */
public static final int AD_MESSAGE_ID_AD_Reference_ID=102;
/** Set Message.
@param AD_Message_ID System Message */
public void setAD_Message_ID (int AD_Message_ID)
{
if (AD_Message_ID <= 0) set_ValueNoCheck ("AD_Message_ID", null);
 else 
set_ValueNoCheck ("AD_Message_ID", new Integer(AD_Message_ID));
}
/** Get Message.
@return System Message */
public int getAD_Message_ID() 
{
Integer ii = (Integer)get_Value("AD_Message_ID");
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
/** Set Tax.
@param P_Tax_ID   */
public void setP_Tax_ID (int P_Tax_ID)
{
if (P_Tax_ID < 1) throw new IllegalArgumentException ("P_Tax_ID is mandatory.");
set_Value ("P_Tax_ID", new Integer(P_Tax_ID));
}
/** Get Tax.
@return   */
public int getP_Tax_ID() 
{
Integer ii = (Integer)get_Value("P_Tax_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_Tax_Rrq_ID.
@param P_Tax_Rrq_ID P_Tax_Rrq_ID */
public void setP_Tax_Rrq_ID (int P_Tax_Rrq_ID)
{
if (P_Tax_Rrq_ID < 1) throw new IllegalArgumentException ("P_Tax_Rrq_ID is mandatory.");
set_ValueNoCheck ("P_Tax_Rrq_ID", new Integer(P_Tax_Rrq_ID));
}
/** Get P_Tax_Rrq_ID.
@return P_Tax_Rrq_ID */
public int getP_Tax_Rrq_ID() 
{
Integer ii = (Integer)get_Value("P_Tax_Rrq_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Search Key.
@param Value Search key for the record in the format required - must be unique */
public void setValue (String Value)
{
if (Value != null && Value.length() > 60)
{
log.warning("Length > 60 - truncated");
Value = Value.substring(0,59);
}
set_ValueNoCheck ("Value", Value);
}
/** Get Search Key.
@return Search key for the record in the format required - must be unique */
public String getValue() 
{
return (String)get_Value("Value");
}
}
