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
/** Generated Model for P_Augmentation_Result
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Augmentation_Result extends PO
{
/** Standard Constructor
@param ctx context
@param P_Augmentation_Result_ID id
@param trxName transaction
*/
public X_P_Augmentation_Result (Properties ctx, int P_Augmentation_Result_ID, String trxName)
{
super (ctx, P_Augmentation_Result_ID, trxName);
/** if (P_Augmentation_Result_ID == 0)
{
setIsAugmentationResult (false);
setP_Augmentation_ID (0);
setP_Augmentation_Result_ID (0);
setP_Employee_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Augmentation_Result (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100793 */
public static final int Table_ID=2100793;

/** TableName=P_Augmentation_Result */
public static final String Table_Name="P_Augmentation_Result";

protected static KeyNamePair Model = new KeyNamePair(2100793,"P_Augmentation_Result");

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
StringBuffer sb = new StringBuffer ("X_P_Augmentation_Result[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Amount calculed.
@param AmountCalc Amount calculed */
public void setAmountCalc (BigDecimal AmountCalc)
{
throw new IllegalArgumentException ("AmountCalc is virtual column");
}
/** Get Amount calculed.
@return Amount calculed */
public BigDecimal getAmountCalc() 
{
BigDecimal bd = (BigDecimal)get_Value("AmountCalc");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Modified amount.
@param AmountModified Modified amount */
public void setAmountModified (BigDecimal AmountModified)
{
throw new IllegalArgumentException ("AmountModified is virtual column");
}
/** Get Modified amount.
@return Modified amount */
public BigDecimal getAmountModified() 
{
BigDecimal bd = (BigDecimal)get_Value("AmountModified");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Generate on time sheet.
@param IsAugmentationResult Generate on time sheet */
public void setIsAugmentationResult (boolean IsAugmentationResult)
{
set_Value ("IsAugmentationResult", new Boolean(IsAugmentationResult));
}
/** Get Generate on time sheet.
@return Generate on time sheet */
public boolean isAugmentationResult() 
{
Object oo = get_Value("IsAugmentationResult");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Retroactivity.
@param P_Augmentation_ID Retroactivity */
public void setP_Augmentation_ID (int P_Augmentation_ID)
{
if (P_Augmentation_ID < 1) throw new IllegalArgumentException ("P_Augmentation_ID is mandatory.");
set_Value ("P_Augmentation_ID", new Integer(P_Augmentation_ID));
}
/** Get Retroactivity.
@return Retroactivity */
public int getP_Augmentation_ID() 
{
Integer ii = (Integer)get_Value("P_Augmentation_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_Augmentation_Result_ID.
@param P_Augmentation_Result_ID P_Augmentation_Result_ID */
public void setP_Augmentation_Result_ID (int P_Augmentation_Result_ID)
{
if (P_Augmentation_Result_ID < 1) throw new IllegalArgumentException ("P_Augmentation_Result_ID is mandatory.");
set_ValueNoCheck ("P_Augmentation_Result_ID", new Integer(P_Augmentation_Result_ID));
}
/** Get P_Augmentation_Result_ID.
@return P_Augmentation_Result_ID */
public int getP_Augmentation_Result_ID() 
{
Integer ii = (Integer)get_Value("P_Augmentation_Result_ID");
if (ii == null) return 0;
return ii.intValue();
}
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
/** Set Time Sheet No..
@param P_Time_Sheet_ID   */
public void setP_Time_Sheet_ID (String P_Time_Sheet_ID)
{
throw new IllegalArgumentException ("P_Time_Sheet_ID is virtual column");
}
/** Get Time Sheet No..
@return   */
public String getP_Time_Sheet_ID() 
{
return (String)get_Value("P_Time_Sheet_ID");
}
/** Set Payment amount.
@param PayAmt Amount being paid */
public void setPayAmt (BigDecimal PayAmt)
{
throw new IllegalArgumentException ("PayAmt is virtual column");
}
/** Get Payment amount.
@return Amount being paid */
public BigDecimal getPayAmt() 
{
BigDecimal bd = (BigDecimal)get_Value("PayAmt");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Quantity calculed.
@param QuantityCalc Quantity calculed */
public void setQuantityCalc (BigDecimal QuantityCalc)
{
throw new IllegalArgumentException ("QuantityCalc is virtual column");
}
/** Get Quantity calculed.
@return Quantity calculed */
public BigDecimal getQuantityCalc() 
{
BigDecimal bd = (BigDecimal)get_Value("QuantityCalc");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Retroactivity Amount.
@param RetroAmount Retroactivity Amount */
public void setRetroAmount (BigDecimal RetroAmount)
{
throw new IllegalArgumentException ("RetroAmount is virtual column");
}
/** Get Retroactivity Amount.
@return Retroactivity Amount */
public BigDecimal getRetroAmount() 
{
BigDecimal bd = (BigDecimal)get_Value("RetroAmount");
if (bd == null) return Env.ZERO;
return bd;
}
}
