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
/** Generated Model for P_Gain_Credits
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Gain_Credits extends PO
{
/** Standard Constructor
@param ctx context
@param P_Gain_Credits_ID id
@param trxName transaction
*/
public X_P_Gain_Credits (Properties ctx, int P_Gain_Credits_ID, String trxName)
{
super (ctx, P_Gain_Credits_ID, trxName);
/** if (P_Gain_Credits_ID == 0)
{
setCredits_Function (null);	// +
setP_Credits_ID (0);
setP_Gain_Credits_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Gain_Credits (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100662 */
public static final int Table_ID=2100662;

/** TableName=P_Gain_Credits */
public static final String Table_Name="P_Gain_Credits";

protected static KeyNamePair Model = new KeyNamePair(2100662,"P_Gain_Credits");

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
StringBuffer sb = new StringBuffer ("X_P_Gain_Credits[").append(get_ID()).append("]");
return sb.toString();
}

/** Credits_Function AD_Reference_ID=2000018 */
public static final int CREDITS_FUNCTION_AD_Reference_ID=2000018;
/** Increase = + */
public static final String CREDITS_FUNCTION_Increase = "+";
/** Decrease = - */
public static final String CREDITS_FUNCTION_Decrease = "-";
/** Reset to zero ( empty ) = = */
public static final String CREDITS_FUNCTION_ResetToZeroEmpty = "=";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isCredits_FunctionValid (String test)
{
return test.equals("+") || test.equals("-") || test.equals("=");
}
/** Set Function.
@param Credits_Function Function */
public void setCredits_Function (String Credits_Function)
{
if (Credits_Function == null) throw new IllegalArgumentException ("Credits_Function is mandatory");
if (!isCredits_FunctionValid(Credits_Function))
throw new IllegalArgumentException ("Credits_Function Invalid value - " + Credits_Function + " - Reference_ID=2000018 - + - - - =");
if (Credits_Function.length() > 1)
{
log.warning("Length > 1 - truncated");
Credits_Function = Credits_Function.substring(0,0);
}
set_Value ("Credits_Function", Credits_Function);
}
/** Get Function.
@return Function */
public String getCredits_Function() 
{
return (String)get_Value("Credits_Function");
}
/** Set Multiply Rate.
@param MultiplyRate Rate to multiple the source by to calculate the target. */
public void setMultiplyRate (BigDecimal MultiplyRate)
{
set_Value ("MultiplyRate", MultiplyRate);
}
/** Get Multiply Rate.
@return Rate to multiple the source by to calculate the target. */
public BigDecimal getMultiplyRate() 
{
BigDecimal bd = (BigDecimal)get_Value("MultiplyRate");
if (bd == null) return Env.ZERO;
return bd;
}

/** P_Collective_Labour_Agr_ID AD_Reference_ID=2000006 */
public static final int P_COLLECTIVE_LABOUR_AGR_ID_AD_Reference_ID=2000006;
/** Set Collective Labour Agreement.
@param P_Collective_Labour_Agr_ID Collective Labour Agreement */
public void setP_Collective_Labour_Agr_ID (int P_Collective_Labour_Agr_ID)
{
if (P_Collective_Labour_Agr_ID <= 0) set_Value ("P_Collective_Labour_Agr_ID", null);
 else 
set_Value ("P_Collective_Labour_Agr_ID", new Integer(P_Collective_Labour_Agr_ID));
}
/** Get Collective Labour Agreement.
@return Collective Labour Agreement */
public int getP_Collective_Labour_Agr_ID() 
{
Integer ii = (Integer)get_Value("P_Collective_Labour_Agr_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Credits_ID AD_Reference_ID=2100318 */
public static final int P_CREDITS_ID_AD_Reference_ID=2100318;
/** Set Credits.
@param P_Credits_ID Credits */
public void setP_Credits_ID (int P_Credits_ID)
{
//if (P_Credits_ID < 1) throw new IllegalArgumentException ("P_Credits_ID is mandatory.");
set_Value ("P_Credits_ID", new Integer(P_Credits_ID));
}
/** Get Credits.
@return Credits */
public int getP_Credits_ID() 
{
Integer ii = (Integer)get_Value("P_Credits_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Gain Accumulated.
@param P_Gain_Credits_ID   */
public void setP_Gain_Credits_ID (int P_Gain_Credits_ID)
{
if (P_Gain_Credits_ID < 1) throw new IllegalArgumentException ("P_Gain_Credits_ID is mandatory.");
set_Value ("P_Gain_Credits_ID", new Integer(P_Gain_Credits_ID));
}
/** Get Gain Accumulated.
@return   */
public int getP_Gain_Credits_ID() 
{
Integer ii = (Integer)get_Value("P_Gain_Credits_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Gain Parameter.
@param P_Gain_Parameter_ID Gain Parameter */
public void setP_Gain_Parameter_ID (int P_Gain_Parameter_ID)
{
if (P_Gain_Parameter_ID <= 0) set_Value ("P_Gain_Parameter_ID", null);
 else 
set_Value ("P_Gain_Parameter_ID", new Integer(P_Gain_Parameter_ID));
}
/** Get Gain Parameter.
@return Gain Parameter */
public int getP_Gain_Parameter_ID() 
{
Integer ii = (Integer)get_Value("P_Gain_Parameter_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
