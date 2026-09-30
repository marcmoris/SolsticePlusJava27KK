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
/** Generated Model for P_Statement_Earning_InsDetail
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Statement_Earning_InsDetail extends PO
{
/** Standard Constructor
@param ctx context
@param P_Statement_Earning_InsDetail_ID id
@param trxName transaction
*/
public X_P_Statement_Earning_InsDetail (Properties ctx, int P_Statement_Earning_InsDetail_ID, String trxName)
{
super (ctx, P_Statement_Earning_InsDetail_ID, trxName);
/** if (P_Statement_Earning_InsDetail_ID == 0)
{
setName (null);
setP_Statement_Earning_Deduction_ID (0);
setP_Statement_Earning_InsDetail_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Statement_Earning_InsDetail (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100781 */
public static final int Table_ID=2100781;

/** TableName=P_Statement_Earning_InsDetail */
public static final String Table_Name="P_Statement_Earning_InsDetail";

protected static KeyNamePair Model = new KeyNamePair(2100781,"P_Statement_Earning_InsDetail");

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
StringBuffer sb = new StringBuffer ("X_P_Statement_Earning_InsDetail[").append(get_ID()).append("]");
return sb.toString();
}
/** Set DecimalValue.
@param DecimalValue DecimalValue */
public void setDecimalValue (BigDecimal DecimalValue)
{
set_Value ("DecimalValue", DecimalValue);
}
/** Get DecimalValue.
@return DecimalValue */
public BigDecimal getDecimalValue() 
{
BigDecimal bd = (BigDecimal)get_Value("DecimalValue");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Name .
@param Name Alphanumeric identifier of the entity */
public void setName (String Name)
{
if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
if (Name.length() > 255)
{
log.warning("Length > 255 - truncated");
Name = Name.substring(0,254);
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
/** Set Statement Earning Deduction.
@param P_Statement_Earning_Deduction_ID Statement Earning Deduction */
public void setP_Statement_Earning_Deduction_ID (int P_Statement_Earning_Deduction_ID)
{
if (P_Statement_Earning_Deduction_ID < 1) throw new IllegalArgumentException ("P_Statement_Earning_Deduction_ID is mandatory.");
set_Value ("P_Statement_Earning_Deduction_ID", new Integer(P_Statement_Earning_Deduction_ID));
}
/** Get Statement Earning Deduction.
@return Statement Earning Deduction */
public int getP_Statement_Earning_Deduction_ID() 
{
Integer ii = (Integer)get_Value("P_Statement_Earning_Deduction_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_Statement_Earning_InsDetail_ID.
@param P_Statement_Earning_InsDetail_ID P_Statement_Earning_InsDetail_ID */
public void setP_Statement_Earning_InsDetail_ID (int P_Statement_Earning_InsDetail_ID)
{
if (P_Statement_Earning_InsDetail_ID < 1) throw new IllegalArgumentException ("P_Statement_Earning_InsDetail_ID is mandatory.");
set_ValueNoCheck ("P_Statement_Earning_InsDetail_ID", new Integer(P_Statement_Earning_InsDetail_ID));
}
/** Get P_Statement_Earning_InsDetail_ID.
@return P_Statement_Earning_InsDetail_ID */
public int getP_Statement_Earning_InsDetail_ID() 
{
Integer ii = (Integer)get_Value("P_Statement_Earning_InsDetail_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
