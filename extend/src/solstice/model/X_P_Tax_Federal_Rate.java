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
/** Generated Model for P_Tax_Federal_Rate
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Tax_Federal_Rate extends PO
{
/** Standard Constructor
@param ctx context
@param P_Tax_Federal_Rate_ID id
@param trxName transaction
*/
public X_P_Tax_Federal_Rate (Properties ctx, int P_Tax_Federal_Rate_ID, String trxName)
{
super (ctx, P_Tax_Federal_Rate_ID, trxName);
/** if (P_Tax_Federal_Rate_ID == 0)
{
setP_Tax_Federal_Rate_ID (0);
setP_Tax_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Tax_Federal_Rate (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000047 */
public static final int Table_ID=2000047;

/** TableName=P_Tax_Federal_Rate */
public static final String Table_Name="P_Tax_Federal_Rate";

protected static KeyNamePair Model = new KeyNamePair(2000047,"P_Tax_Federal_Rate");

protected BigDecimal accessLevel = new BigDecimal(7);
/** AccessLevel
@return 7 - System - Client - Org 
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
StringBuffer sb = new StringBuffer ("X_P_Tax_Federal_Rate[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Constant K.
@param Federal_Constant_K Constant K */
public void setFederal_Constant_K (BigDecimal Federal_Constant_K)
{
set_Value ("Federal_Constant_K", Federal_Constant_K);
}
/** Get Constant K.
@return Constant K */
public BigDecimal getFederal_Constant_K() 
{
BigDecimal bd = (BigDecimal)get_Value("Federal_Constant_K");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Rate .
@param Federal_Tax_Rate Rate  */
public void setFederal_Tax_Rate (BigDecimal Federal_Tax_Rate)
{
set_Value ("Federal_Tax_Rate", Federal_Tax_Rate);
}
/** Get Rate .
@return Rate  */
public BigDecimal getFederal_Tax_Rate() 
{
BigDecimal bd = (BigDecimal)get_Value("Federal_Tax_Rate");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Limit Max.
@param Limit_Max Maximum  */
public void setLimit_Max (BigDecimal Limit_Max)
{
set_Value ("Limit_Max", Limit_Max);
}
/** Get Limit Max.
@return Maximum  */
public BigDecimal getLimit_Max() 
{
BigDecimal bd = (BigDecimal)get_Value("Limit_Max");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Limit Min.
@param Limit_Min Minimum  */
public void setLimit_Min (BigDecimal Limit_Min)
{
set_Value ("Limit_Min", Limit_Min);
}
/** Get Limit Min.
@return Minimum  */
public BigDecimal getLimit_Min() 
{
BigDecimal bd = (BigDecimal)get_Value("Limit_Min");
if (bd == null) return Env.ZERO;
return bd;
}
/** Get Record ID/ColumnName
@return ID/ColumnName pair */
public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), String.valueOf(getLimit_Min()));
}
/** Set P_Tax_Federal_Rate_ID.
@param P_Tax_Federal_Rate_ID P_Tax_Federal_Rate_ID */
public void setP_Tax_Federal_Rate_ID (int P_Tax_Federal_Rate_ID)
{
if (P_Tax_Federal_Rate_ID < 1) throw new IllegalArgumentException ("P_Tax_Federal_Rate_ID is mandatory.");
set_Value ("P_Tax_Federal_Rate_ID", new Integer(P_Tax_Federal_Rate_ID));
}
/** Get P_Tax_Federal_Rate_ID.
@return P_Tax_Federal_Rate_ID */
public int getP_Tax_Federal_Rate_ID() 
{
Integer ii = (Integer)get_Value("P_Tax_Federal_Rate_ID");
if (ii == null) return 0;
return ii.intValue();
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
