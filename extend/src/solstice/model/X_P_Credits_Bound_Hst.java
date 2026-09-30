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
/** Generated Model for P_Credits_Bound_Hst
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Credits_Bound_Hst extends PO
{
/** Standard Constructor
@param ctx context
@param P_Credits_Bound_Hst_ID id
@param trxName transaction
*/
public X_P_Credits_Bound_Hst (Properties ctx, int P_Credits_Bound_Hst_ID, String trxName)
{
super (ctx, P_Credits_Bound_Hst_ID, trxName);
/** if (P_Credits_Bound_Hst_ID == 0)
{
setEffectIn (new Timestamp(System.currentTimeMillis()));	// @#Date@
setLimitMin (Env.ZERO);
setP_Credits_Bound_Hst_ID (0);
setP_Credits_Bound_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Credits_Bound_Hst (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100687 */
public static final int Table_ID=2100687;

/** TableName=P_Credits_Bound_Hst */
public static final String Table_Name="P_Credits_Bound_Hst";

protected static KeyNamePair Model = new KeyNamePair(2100687,"P_Credits_Bound_Hst");

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
StringBuffer sb = new StringBuffer ("X_P_Credits_Bound_Hst[").append(get_ID()).append("]");
return sb.toString();
}

/** CreditsUnit AD_Reference_ID=2000051 */
public static final int CREDITSUNIT_AD_Reference_ID=2000051;
/** Amount = 6 */
public static final String CREDITSUNIT_Amount = "6";
/** Hours = 1 */
public static final String CREDITSUNIT_Hours = "1";
/** Days = 2 */
public static final String CREDITSUNIT_Days = "2";
/** Year = 3 */
public static final String CREDITSUNIT_Year = "3";
/** Week = 4 */
public static final String CREDITSUNIT_Week = "4";
/** Quantity = 5 */
public static final String CREDITSUNIT_Quantity = "5";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isCreditsUnitValid (String test)
{
return test == null || test.equals("6") || test.equals("1") || test.equals("2") || test.equals("3") || test.equals("4") || test.equals("5");
}
/** Set Unit.
@param CreditsUnit Unit */
public void setCreditsUnit (String CreditsUnit)
{
if (!isCreditsUnitValid(CreditsUnit))
throw new IllegalArgumentException ("CreditsUnit Invalid value - " + CreditsUnit + " - Reference_ID=2000051 - 6 - 1 - 2 - 3 - 4 - 5");
if (CreditsUnit != null && CreditsUnit.length() > 2)
{
log.warning("Length > 2 - truncated");
CreditsUnit = CreditsUnit.substring(0,1);
}
set_ValueNoCheck ("CreditsUnit", CreditsUnit);
}
/** Get Unit.
@return Unit */
public String getCreditsUnit() 
{
return (String)get_Value("CreditsUnit");
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
/** Set Limit minimum.
@param LimitMin Limit minimum */
public void setLimitMin (BigDecimal LimitMin)
{
if (LimitMin == null) throw new IllegalArgumentException ("LimitMin is mandatory.");
set_Value ("LimitMin", LimitMin);
}
/** Get Limit minimum.
@return Limit minimum */
public BigDecimal getLimitMin() 
{
BigDecimal bd = (BigDecimal)get_Value("LimitMin");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set P_Credits_Bound_Hst_ID.
@param P_Credits_Bound_Hst_ID P_Credits_Bound_Hst_ID */
public void setP_Credits_Bound_Hst_ID (int P_Credits_Bound_Hst_ID)
{
if (P_Credits_Bound_Hst_ID < 1) throw new IllegalArgumentException ("P_Credits_Bound_Hst_ID is mandatory.");
set_Value ("P_Credits_Bound_Hst_ID", new Integer(P_Credits_Bound_Hst_ID));
}
/** Get P_Credits_Bound_Hst_ID.
@return P_Credits_Bound_Hst_ID */
public int getP_Credits_Bound_Hst_ID() 
{
Integer ii = (Integer)get_Value("P_Credits_Bound_Hst_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_credits_bound_id.
@param P_Credits_Bound_ID P_credits_bound_id */
public void setP_Credits_Bound_ID (int P_Credits_Bound_ID)
{
if (P_Credits_Bound_ID < 1) throw new IllegalArgumentException ("P_Credits_Bound_ID is mandatory.");
set_Value ("P_Credits_Bound_ID", new Integer(P_Credits_Bound_ID));
}
/** Get P_credits_bound_id.
@return P_credits_bound_id */
public int getP_Credits_Bound_ID() 
{
Integer ii = (Integer)get_Value("P_Credits_Bound_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Parameter.
@param P_Credits_Param_ID   */
public void setP_Credits_Param_ID (int P_Credits_Param_ID)
{
if (P_Credits_Param_ID <= 0) set_Value ("P_Credits_Param_ID", null);
 else 
set_Value ("P_Credits_Param_ID", new Integer(P_Credits_Param_ID));
}
/** Get Parameter.
@return   */
public int getP_Credits_Param_ID() 
{
Integer ii = (Integer)get_Value("P_Credits_Param_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Variation.
@param Variation Variation */
public void setVariation (BigDecimal Variation)
{
set_Value ("Variation", Variation);
}
/** Get Variation.
@return Variation */
public BigDecimal getVariation() 
{
BigDecimal bd = (BigDecimal)get_Value("Variation");
if (bd == null) return Env.ZERO;
return bd;
}
}
