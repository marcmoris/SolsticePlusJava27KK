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
/** Generated Model for P_Gain_Parameter
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Gain_Parameter extends PO
{
/** Standard Constructor
@param ctx context
@param P_Gain_Parameter_ID id
@param trxName transaction
*/
public X_P_Gain_Parameter (Properties ctx, int P_Gain_Parameter_ID, String trxName)
{
super (ctx, P_Gain_Parameter_ID, trxName);
/** if (P_Gain_Parameter_ID == 0)
{
setMultiplyRate (Env.ZERO);
setP_Gain_ID (0);
setP_Gain_Parameter_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Gain_Parameter (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100739 */
public static final int Table_ID=2100739;

/** TableName=P_Gain_Parameter */
public static final String Table_Name="P_Gain_Parameter";

protected static KeyNamePair Model = new KeyNamePair(2100739,"P_Gain_Parameter");

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
StringBuffer sb = new StringBuffer ("X_P_Gain_Parameter[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Bonus Amount.
@param Bonus_Amount Bonus Amount */
public void setBonus_Amount (BigDecimal Bonus_Amount)
{
set_Value ("Bonus_Amount", Bonus_Amount);
}
/** Get Bonus Amount.
@return Bonus Amount */
public BigDecimal getBonus_Amount() 
{
BigDecimal bd = (BigDecimal)get_Value("Bonus_Amount");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Fixed Amount.
@param Fixed_Amount   */
public void setFixed_Amount (BigDecimal Fixed_Amount)
{
set_Value ("Fixed_Amount", Fixed_Amount);
}
/** Get Fixed Amount.
@return   */
public BigDecimal getFixed_Amount() 
{
BigDecimal bd = (BigDecimal)get_Value("Fixed_Amount");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Multiply Rate.
@param MultiplyRate Rate to multiple the source by to calculate the target. */
public void setMultiplyRate (BigDecimal MultiplyRate)
{
if (MultiplyRate == null) throw new IllegalArgumentException ("MultiplyRate is mandatory.");
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
if (P_Collective_Labour_Agr_ID <= 0) set_ValueNoCheck ("P_Collective_Labour_Agr_ID", null);
 else 
set_ValueNoCheck ("P_Collective_Labour_Agr_ID", new Integer(P_Collective_Labour_Agr_ID));
}
/** Get Collective Labour Agreement.
@return Collective Labour Agreement */
public int getP_Collective_Labour_Agr_ID() 
{
Integer ii = (Integer)get_Value("P_Collective_Labour_Agr_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Gain_ID AD_Reference_ID=2100317 */
public static final int P_GAIN_ID_AD_Reference_ID=2100317;
/** Set Gain.
@param P_Gain_ID Gain */
public void setP_Gain_ID (int P_Gain_ID)
{
if (P_Gain_ID < 1) throw new IllegalArgumentException ("P_Gain_ID is mandatory.");
set_ValueNoCheck ("P_Gain_ID", new Integer(P_Gain_ID));
}
/** Get Gain.
@return Gain */
public int getP_Gain_ID() 
{
Integer ii = (Integer)get_Value("P_Gain_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Get Record ID/ColumnName
@return ID/ColumnName pair */
public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), String.valueOf(getP_Gain_ID()));
}
/** Set Gain Parameter.
@param P_Gain_Parameter_ID Gain Parameter */
public void setP_Gain_Parameter_ID (int P_Gain_Parameter_ID)
{
if (P_Gain_Parameter_ID < 1) throw new IllegalArgumentException ("P_Gain_Parameter_ID is mandatory.");
set_ValueNoCheck ("P_Gain_Parameter_ID", new Integer(P_Gain_Parameter_ID));
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
