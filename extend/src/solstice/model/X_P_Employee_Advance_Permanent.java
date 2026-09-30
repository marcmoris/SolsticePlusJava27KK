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
/** Generated Model for P_Employee_Advance_Permanent
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Employee_Advance_Permanent extends PO
{
/** Standard Constructor
@param ctx context
@param P_Employee_Advance_Permanent_ID id
@param trxName transaction
*/
public X_P_Employee_Advance_Permanent (Properties ctx, int P_Employee_Advance_Permanent_ID, String trxName)
{
super (ctx, P_Employee_Advance_Permanent_ID, trxName);
/** if (P_Employee_Advance_Permanent_ID == 0)
{
setAdvanceAmount (Env.ZERO);
setP_Employee_Advance_Permanent_ID (0);
setP_Employee_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Employee_Advance_Permanent (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100704 */
public static final int Table_ID=2100704;

/** TableName=P_Employee_Advance_Permanent */
public static final String Table_Name="P_Employee_Advance_Permanent";

protected static KeyNamePair Model = new KeyNamePair(2100704,"P_Employee_Advance_Permanent");

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
StringBuffer sb = new StringBuffer ("X_P_Employee_Advance_Permanent[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Amount.
@param AdvanceAmount Amount */
public void setAdvanceAmount (BigDecimal AdvanceAmount)
{
if (AdvanceAmount == null) throw new IllegalArgumentException ("AdvanceAmount is mandatory.");
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
/** Set Permanent Advance.
@param P_Employee_Advance_Permanent_ID Permanent Advance */
public void setP_Employee_Advance_Permanent_ID (int P_Employee_Advance_Permanent_ID)
{
if (P_Employee_Advance_Permanent_ID < 1) throw new IllegalArgumentException ("P_Employee_Advance_Permanent_ID is mandatory.");
set_Value ("P_Employee_Advance_Permanent_ID", new Integer(P_Employee_Advance_Permanent_ID));
}
/** Get Permanent Advance.
@return Permanent Advance */
public int getP_Employee_Advance_Permanent_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Advance_Permanent_ID");
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
}
