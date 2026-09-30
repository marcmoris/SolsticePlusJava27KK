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
/** Generated Model for P_Employee_Bonus
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Employee_Bonus extends PO
{
/** Standard Constructor
@param ctx context
@param P_Employee_Bonus_ID id
@param trxName transaction
*/
public X_P_Employee_Bonus (Properties ctx, int P_Employee_Bonus_ID, String trxName)
{
super (ctx, P_Employee_Bonus_ID, trxName);
/** if (P_Employee_Bonus_ID == 0)
{
setEffectIn (new Timestamp(System.currentTimeMillis()));	// @SQL=SELECT StartDate AS DefaultValue FROM P_Period WHERE PeriodStatus='O' 
setP_Bonus_ID (0);
setP_Employee_Bonus_ID (0);
setP_Employee_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Employee_Bonus (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100680 */
public static final int Table_ID=2100680;

/** TableName=P_Employee_Bonus */
public static final String Table_Name="P_Employee_Bonus";

protected static KeyNamePair Model = new KeyNamePair(2100680,"P_Employee_Bonus");

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
StringBuffer sb = new StringBuffer ("X_P_Employee_Bonus[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Effect In.
@param EffectIn Date when information in effect  */
public void setEffectIn (Timestamp EffectIn)
{
if (EffectIn == null) throw new IllegalArgumentException ("EffectIn is mandatory.");
set_Value ("EffectIn", EffectIn);
}
/** Get Effect In.
@return Date when information in effect  */
public Timestamp getEffectIn() 
{
return (Timestamp)get_Value("EffectIn");
}
/** Set To.
@param EffectTo To */
public void setEffectTo (Timestamp EffectTo)
{
set_Value ("EffectTo", EffectTo);
}
/** Get To.
@return To */
public Timestamp getEffectTo() 
{
return (Timestamp)get_Value("EffectTo");
}

/** P_Bonus_ID AD_Reference_ID=2100343 */
public static final int P_BONUS_ID_AD_Reference_ID=2100343;
/** Set Bonus.
@param P_Bonus_ID Bonus */
public void setP_Bonus_ID (int P_Bonus_ID)
{
if (P_Bonus_ID < 1) throw new IllegalArgumentException ("P_Bonus_ID is mandatory.");
set_Value ("P_Bonus_ID", new Integer(P_Bonus_ID));
}
/** Get Bonus.
@return Bonus */
public int getP_Bonus_ID() 
{
Integer ii = (Integer)get_Value("P_Bonus_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Bonus.
@param P_Employee_Bonus_ID Bonus */
public void setP_Employee_Bonus_ID (int P_Employee_Bonus_ID)
{
if (P_Employee_Bonus_ID < 1) throw new IllegalArgumentException ("P_Employee_Bonus_ID is mandatory.");
set_ValueNoCheck ("P_Employee_Bonus_ID", new Integer(P_Employee_Bonus_ID));
}
/** Get Bonus.
@return Bonus */
public int getP_Employee_Bonus_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Bonus_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Employee_ID AD_Reference_ID=2000010 */
public static final int P_EMPLOYEE_ID_AD_Reference_ID=2000010;
/** Set Employee .
@param P_Employee_ID Employee */
public void setP_Employee_ID (int P_Employee_ID)
{
if (P_Employee_ID < 1) throw new IllegalArgumentException ("P_Employee_ID is mandatory.");
set_ValueNoCheck ("P_Employee_ID", new Integer(P_Employee_ID));
}
/** Get Employee .
@return Employee */
public int getP_Employee_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
