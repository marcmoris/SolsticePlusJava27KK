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
/** Generated Model for P_PunchCard_Reason
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_PunchCard_Reason extends PO
{
/** Standard Constructor
@param ctx context
@param P_PunchCard_Reason_ID id
@param trxName transaction
*/
public X_P_PunchCard_Reason (Properties ctx, int P_PunchCard_Reason_ID, String trxName)
{
super (ctx, P_PunchCard_Reason_ID, trxName);
/** if (P_PunchCard_Reason_ID == 0)
{
setName (null);
setP_PunchCard_Reason_ID (0);
setValue (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_PunchCard_Reason (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100830 */
public static final int Table_ID=2100830;

/** TableName=P_PunchCard_Reason */
public static final String Table_Name="P_PunchCard_Reason";

protected static KeyNamePair Model = new KeyNamePair(2100830,"P_PunchCard_Reason");

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
StringBuffer sb = new StringBuffer ("X_P_PunchCard_Reason[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Name .
@param Name Alphanumeric identifier of the entity */
public void setName (String Name)
{
if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
if (Name.length() > 100)
{
log.warning("Length > 100 - truncated");
Name = Name.substring(0,99);
}
set_Value ("Name", Name);
}
/** Get Name .
@return Alphanumeric identifier of the entity */
public String getName() 
{
return (String)get_Value("Name");
}
/** Get Record ID/ColumnName
@return ID/ColumnName pair */
public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), getName());
}
/** Set P_PunchCard_Reason_ID.
@param P_PunchCard_Reason_ID P_PunchCard_Reason_ID */
public void setP_PunchCard_Reason_ID (int P_PunchCard_Reason_ID)
{
if (P_PunchCard_Reason_ID < 1) throw new IllegalArgumentException ("P_PunchCard_Reason_ID is mandatory.");
set_ValueNoCheck ("P_PunchCard_Reason_ID", new Integer(P_PunchCard_Reason_ID));
}
/** Get P_PunchCard_Reason_ID.
@return P_PunchCard_Reason_ID */
public int getP_PunchCard_Reason_ID() 
{
Integer ii = (Integer)get_Value("P_PunchCard_Reason_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Search Key.
@param Value Search key for the record in the format required - must be unique */
public void setValue (String Value)
{
if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
if (Value.length() > 5)
{
log.warning("Length > 5 - truncated");
Value = Value.substring(0,4);
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
