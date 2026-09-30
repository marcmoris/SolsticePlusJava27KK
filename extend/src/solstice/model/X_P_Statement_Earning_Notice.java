/******************************************************************************
* Product: Solstice+ Payroll & Human Resources Management                    *
* Copyright (C) 2008 ProGestion Informatique, Inc. All Rights Reserved.      *
* This program is free software, you can redistribute it and/or modify it    *
* under the terms version 2 of the GNU General Public License as published   *
* by the Free Software Foundation. This program is distributed in the hope   *
* that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
* warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
* See the GNU General Public License for more details.                       *
* You should have received a copy of the GNU General Public License along    *
* with this program, if not, write to the Free Software Foundation, Inc.,    *
* 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
* For the text or an alternative of this public license, you may reach us    *
* ProGestion Informatique, 210-5300 Bld des Galerie, Quebec, G2K 2A2 Canada  *
* or via info@progestion.net or http://www.progestion.net/license.html       *
******************************************************************************/
package solstice.model;

/** Generated Model - DO NOT CHANGE */
import org.compiere.model.*;
import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;
/** Generated Model for P_Statement_Earning_Notice
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Statement_Earning_Notice extends PO
{
/** Standard Constructor
@param ctx context
@param P_Statement_Earning_Notice_ID id
@param trxName transaction
*/
public X_P_Statement_Earning_Notice (Properties ctx, int P_Statement_Earning_Notice_ID, String trxName)
{
super (ctx, P_Statement_Earning_Notice_ID, trxName);
/** if (P_Statement_Earning_Notice_ID == 0)
{
setNotice (null);
setP_Statement_Earning_Notice_ID (0);
setValue (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Statement_Earning_Notice (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2101103 */
public static final int Table_ID=2101103;

/** TableName=P_Statement_Earning_Notice */
public static final String Table_Name="P_Statement_Earning_Notice";

protected static KeyNamePair Model = new KeyNamePair(2101103,"P_Statement_Earning_Notice");

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
StringBuffer sb = new StringBuffer ("X_P_Statement_Earning_Notice[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Notice.
@param Notice Notice */
public void setNotice (String Notice)
{
if (Notice == null) throw new IllegalArgumentException ("Notice is mandatory.");
if (Notice.length() > 2000)
{
log.warning("Length > 2000 - truncated");
Notice = Notice.substring(0,1999);
}
set_Value ("Notice", Notice);
}
/** Get Notice.
@return Notice */
public String getNotice() 
{
return (String)get_Value("Notice");
}
/** Set P_Statement_Earning_ID.
@param P_Statement_Earning_ID P_Statement_Earning_ID */
public void setP_Statement_Earning_ID (int P_Statement_Earning_ID)
{
if (P_Statement_Earning_ID <= 0) set_Value ("P_Statement_Earning_ID", null);
 else 
set_Value ("P_Statement_Earning_ID", new Integer(P_Statement_Earning_ID));
}
/** Get P_Statement_Earning_ID.
@return P_Statement_Earning_ID */
public int getP_Statement_Earning_ID() 
{
Integer ii = (Integer)get_Value("P_Statement_Earning_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_Statement_Earning_Notice_ID.
@param P_Statement_Earning_Notice_ID P_Statement_Earning_Notice_ID */
public void setP_Statement_Earning_Notice_ID (int P_Statement_Earning_Notice_ID)
{
if (P_Statement_Earning_Notice_ID < 1) throw new IllegalArgumentException ("P_Statement_Earning_Notice_ID is mandatory.");
set_ValueNoCheck ("P_Statement_Earning_Notice_ID", new Integer(P_Statement_Earning_Notice_ID));
}
/** Get P_Statement_Earning_Notice_ID.
@return P_Statement_Earning_Notice_ID */
public int getP_Statement_Earning_Notice_ID() 
{
Integer ii = (Integer)get_Value("P_Statement_Earning_Notice_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Search Key.
@param Value Search key for the record in the format required - must be unique */
public void setValue (String Value)
{
if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
if (Value.length() > 30)
{
log.warning("Length > 30 - truncated");
Value = Value.substring(0,29);
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
