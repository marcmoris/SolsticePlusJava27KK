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
/** Generated Model for P_Tax
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Tax extends PO
{
/** Standard Constructor
@param ctx context
@param P_Tax_ID id
@param trxName transaction
*/
public X_P_Tax (Properties ctx, int P_Tax_ID, String trxName)
{
super (ctx, P_Tax_ID, trxName);
/** if (P_Tax_ID == 0)
{
setEffectIn (new Timestamp(System.currentTimeMillis()));
setP_Tax_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Tax (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000024 */
public static final int Table_ID=2000024;

/** TableName=P_Tax */
public static final String Table_Name="P_Tax";

protected static KeyNamePair Model = new KeyNamePair(2000024,"P_Tax");

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
StringBuffer sb = new StringBuffer ("X_P_Tax[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Copy From.
@param CopyFrom Copy From Record */
public void setCopyFrom (String CopyFrom)
{
if (CopyFrom != null && CopyFrom.length() > 1)
{
log.warning("Length > 1 - truncated");
CopyFrom = CopyFrom.substring(0,0);
}
set_Value ("CopyFrom", CopyFrom);
}
/** Get Copy From.
@return Copy From Record */
public String getCopyFrom() 
{
return (String)get_Value("CopyFrom");
}
/** Set Effect In.
@param EffectIn Date when information in effect  */
public void setEffectIn (Timestamp EffectIn)
{
if (EffectIn == null) throw new IllegalArgumentException ("EffectIn is mandatory.");
set_ValueNoCheck ("EffectIn", EffectIn);
}
/** Get Effect In.
@return Date when information in effect  */
public Timestamp getEffectIn() 
{
return (Timestamp)get_Value("EffectIn");
}
/** Get Record ID/ColumnName
@return ID/ColumnName pair */
public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), String.valueOf(getEffectIn()));
}
/** Set Effect In.
@param EffectinStr Effect In */
public void setEffectinStr (String EffectinStr)
{
if (EffectinStr != null && EffectinStr.length() > 10)
{
log.warning("Length > 10 - truncated");
EffectinStr = EffectinStr.substring(0,9);
}
set_Value ("EffectinStr", EffectinStr);
}
/** Get Effect In.
@return Effect In */
public String getEffectinStr() 
{
return (String)get_Value("EffectinStr");
}
/** Set Tax.
@param P_Tax_ID   */
public void setP_Tax_ID (int P_Tax_ID)
{
if (P_Tax_ID < 1) throw new IllegalArgumentException ("P_Tax_ID is mandatory.");
set_ValueNoCheck ("P_Tax_ID", new Integer(P_Tax_ID));
}
/** Get Tax.
@return   */
public int getP_Tax_ID() 
{
Integer ii = (Integer)get_Value("P_Tax_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
