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
/** Generated Model for P_Gain_Join
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: X_P_Gain_Join.java,v 1.4 2008/11/11 16:06:39 stenad01 Exp $ */
public class X_P_Gain_Join extends PO
{
/** Standard Constructor
@param ctx context
@param P_Gain_Join_ID id
@param trxName transaction
*/
public X_P_Gain_Join (Properties ctx, int P_Gain_Join_ID, String trxName)
{
super (ctx, P_Gain_Join_ID, trxName);
/** if (P_Gain_Join_ID == 0)
{
setP_Gain_ID (0);
setP_Gain_ID_Affected (0);
setP_Gain_Join_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Gain_Join (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100751 */
public static final int Table_ID=2100751;

/** TableName=P_Gain_Join */
public static final String Table_Name="P_Gain_Join";

protected static KeyNamePair Model = new KeyNamePair(2100751,"P_Gain_Join");

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
StringBuffer sb = new StringBuffer ("X_P_Gain_Join[").append(get_ID()).append("]");
return sb.toString();
}
/** Set ISNEGATIVE.
@param ISNEGATIVE ISNEGATIVE */
public void setISNEGATIVE (boolean ISNEGATIVE)
{
set_Value ("ISNEGATIVE", new Boolean(ISNEGATIVE));
}
/** Get ISNEGATIVE.
@return ISNEGATIVE */
public boolean isNEGATIVE() 
{
Object oo = get_Value("ISNEGATIVE");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}

/** P_Gain_ID AD_Reference_ID=2100317 */
public static final int P_GAIN_ID_AD_Reference_ID=2100317;
/** Set Pay Codes.
@param P_Gain_ID Pay Codes */
public void setP_Gain_ID (int P_Gain_ID)
{
if (P_Gain_ID < 1) throw new IllegalArgumentException ("P_Gain_ID is mandatory.");
set_Value ("P_Gain_ID", new Integer(P_Gain_ID));
}
/** Get Pay Codes.
@return Pay Codes */
public int getP_Gain_ID() 
{
Integer ii = (Integer)get_Value("P_Gain_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Gain_ID_Affected AD_Reference_ID=2100317 */
public static final int P_GAIN_ID_AFFECTED_AD_Reference_ID=2100317;
/** Set Supercides Regular Earning Pay Codes Group.
@param P_Gain_ID_Affected Supercides Regular Earning Pay Codes Group */
public void setP_Gain_ID_Affected (int P_Gain_ID_Affected)
{
set_Value ("P_Gain_ID_Affected", new Integer(P_Gain_ID_Affected));
}
/** Get Supercides Regular Earning Pay Codes Group.
@return Supercides Regular Earning Pay Codes Group */
public int getP_Gain_ID_Affected() 
{
Integer ii = (Integer)get_Value("P_Gain_ID_Affected");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Gain Join.
@param P_Gain_Join_ID Gain Join */
public void setP_Gain_Join_ID (int P_Gain_Join_ID)
{
if (P_Gain_Join_ID < 1) throw new IllegalArgumentException ("P_Gain_Join_ID is mandatory.");
set_ValueNoCheck ("P_Gain_Join_ID", new Integer(P_Gain_Join_ID));
}
/** Get Gain Join.
@return Gain Join */
public int getP_Gain_Join_ID() 
{
Integer ii = (Integer)get_Value("P_Gain_Join_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
