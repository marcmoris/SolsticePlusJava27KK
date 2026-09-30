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
/** Generated Model for P_Augmentation_Gain_Detail
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Augmentation_Gain_Detail extends PO
{
/** Standard Constructor
@param ctx context
@param P_Augmentation_Gain_Detail_ID id
@param trxName transaction
*/
public X_P_Augmentation_Gain_Detail (Properties ctx, int P_Augmentation_Gain_Detail_ID, String trxName)
{
super (ctx, P_Augmentation_Gain_Detail_ID, trxName);
/** if (P_Augmentation_Gain_Detail_ID == 0)
{
setGainType (null);
setIsIgnoringScale (false);
setP_Augmentation_Gain_Detail_ID (0);
setP_Augmentation_Gain_ID (0);
setP_Gain_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Augmentation_Gain_Detail (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100792 */
public static final int Table_ID=2100792;

/** TableName=P_Augmentation_Gain_Detail */
public static final String Table_Name="P_Augmentation_Gain_Detail";

protected static KeyNamePair Model = new KeyNamePair(2100792,"P_Augmentation_Gain_Detail");

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
StringBuffer sb = new StringBuffer ("X_P_Augmentation_Gain_Detail[").append(get_ID()).append("]");
return sb.toString();
}

/** GainType AD_Reference_ID=2100414 */
public static final int GAINTYPE_AD_Reference_ID=2100414;
/** % Bonus = 1 */
public static final String GAINTYPE_Bonus = "1";
/** Fixed Amount Bonus = 2 */
public static final String GAINTYPE_FixedAmountBonus = "2";
/** Regular Gain = 3 */
public static final String GAINTYPE_RegularGain = "3";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isGainTypeValid (String test)
{
return test.equals("1") || test.equals("2") || test.equals("3");
}
/** Set Gain type.
@param GainType Gain type */
public void setGainType (String GainType)
{
if (GainType == null) throw new IllegalArgumentException ("GainType is mandatory");
if (!isGainTypeValid(GainType))
throw new IllegalArgumentException ("GainType Invalid value - " + GainType + " - Reference_ID=2100414 - 1 - 2 - 3");
if (GainType.length() > 1)
{
log.warning("Length > 1 - truncated");
GainType = GainType.substring(0,0);
}
set_Value ("GainType", GainType);
}
/** Get Gain type.
@return Gain type */
public String getGainType() 
{
return (String)get_Value("GainType");
}
/** Set Ignoring scale.
@param IsIgnoringScale Ignoring scale */
public void setIsIgnoringScale (boolean IsIgnoringScale)
{
set_Value ("IsIgnoringScale", new Boolean(IsIgnoringScale));
}
/** Get Ignoring scale.
@return Ignoring scale */
public boolean isIgnoringScale() 
{
Object oo = get_Value("IsIgnoringScale");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set New percent.
@param NewPercent New percent */
public void setNewPercent (BigDecimal NewPercent)
{
set_Value ("NewPercent", NewPercent);
}
/** Get New percent.
@return New percent */
public BigDecimal getNewPercent() 
{
BigDecimal bd = (BigDecimal)get_Value("NewPercent");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Old percent.
@param OldPercent Old percent */
public void setOldPercent (BigDecimal OldPercent)
{
set_Value ("OldPercent", OldPercent);
}
/** Get Old percent.
@return Old percent */
public BigDecimal getOldPercent() 
{
BigDecimal bd = (BigDecimal)get_Value("OldPercent");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set P_Augmentation_Gain_Detail_ID.
@param P_Augmentation_Gain_Detail_ID P_Augmentation_Gain_Detail_ID */
public void setP_Augmentation_Gain_Detail_ID (int P_Augmentation_Gain_Detail_ID)
{
if (P_Augmentation_Gain_Detail_ID < 1) throw new IllegalArgumentException ("P_Augmentation_Gain_Detail_ID is mandatory.");
set_ValueNoCheck ("P_Augmentation_Gain_Detail_ID", new Integer(P_Augmentation_Gain_Detail_ID));
}
/** Get P_Augmentation_Gain_Detail_ID.
@return P_Augmentation_Gain_Detail_ID */
public int getP_Augmentation_Gain_Detail_ID() 
{
Integer ii = (Integer)get_Value("P_Augmentation_Gain_Detail_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_Augmentation_Gain_ID.
@param P_Augmentation_Gain_ID P_Augmentation_Gain_ID */
public void setP_Augmentation_Gain_ID (int P_Augmentation_Gain_ID)
{
if (P_Augmentation_Gain_ID < 1) throw new IllegalArgumentException ("P_Augmentation_Gain_ID is mandatory.");
set_Value ("P_Augmentation_Gain_ID", new Integer(P_Augmentation_Gain_ID));
}
/** Get P_Augmentation_Gain_ID.
@return P_Augmentation_Gain_ID */
public int getP_Augmentation_Gain_ID() 
{
Integer ii = (Integer)get_Value("P_Augmentation_Gain_ID");
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
set_Value ("P_Gain_ID", new Integer(P_Gain_ID));
}
/** Get Gain.
@return Gain */
public int getP_Gain_ID() 
{
Integer ii = (Integer)get_Value("P_Gain_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Scale percent.
@param ScalePercent Scale percent */
public void setScalePercent (BigDecimal ScalePercent)
{
set_Value ("ScalePercent", ScalePercent);
}
/** Get Scale percent.
@return Scale percent */
public BigDecimal getScalePercent() 
{
BigDecimal bd = (BigDecimal)get_Value("ScalePercent");
if (bd == null) return Env.ZERO;
return bd;
}
}
