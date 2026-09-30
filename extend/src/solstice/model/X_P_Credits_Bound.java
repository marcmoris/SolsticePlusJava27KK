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
/** Generated Model for P_Credits_Bound
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.4 2008/03/12 20:02:49 marmor01 Exp $ */
public class X_P_Credits_Bound extends PO
{
/** Standard Constructor
@param ctx context
@param P_Credits_Bound_ID id
@param trxName transaction
*/
public X_P_Credits_Bound (Properties ctx, int P_Credits_Bound_ID, String trxName)
{
super (ctx, P_Credits_Bound_ID, trxName);
/** if (P_Credits_Bound_ID == 0)
{
setLimitMin (Env.ZERO);
setP_Credits_Bound_ID (0);
setP_Credits_Param_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Credits_Bound (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000090 */
public static final int Table_ID=2000090;

/** TableName=P_Credits_Bound */
public static final String Table_Name="P_Credits_Bound";

protected static KeyNamePair Model = new KeyNamePair(2000090,"P_Credits_Bound");

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
StringBuffer sb = new StringBuffer ("X_P_Credits_Bound[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Additional Yearly Quantity.
@param CreditsAnnual Additional Yearly Quantity */
public void setCreditsAnnual (BigDecimal CreditsAnnual)
{
set_Value ("CreditsAnnual", CreditsAnnual);
}
/** Get Additional Yearly Quantity.
@return Additional Yearly Quantity */
public BigDecimal getCreditsAnnual() 
{
BigDecimal bd = (BigDecimal)get_Value("CreditsAnnual");
if (bd == null) return Env.ZERO;
return bd;
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
/** Set Calculation Basis.
@param CreditsUnit Calculation Basis */
public void setCreditsUnit (String CreditsUnit)
{
if (!isCreditsUnitValid(CreditsUnit))
throw new IllegalArgumentException ("CreditsUnit Invalid value - " + CreditsUnit + " - Reference_ID=2000051 - 6 - 1 - 2 - 3 - 4 - 5");
if (CreditsUnit != null && CreditsUnit.length() > 1)
{
log.warning("Length > 1 - truncated");
CreditsUnit = CreditsUnit.substring(0,0);
}
set_ValueNoCheck ("CreditsUnit", CreditsUnit);
}
/** Get Calculation Basis.
@return Calculation Basis */
public String getCreditsUnit() 
{
return (String)get_Value("CreditsUnit");
}
/** Set Start Range.
@param LimitMin Start Range */
public void setLimitMin (BigDecimal LimitMin)
{
if (LimitMin == null) throw new IllegalArgumentException ("LimitMin is mandatory.");
set_Value ("LimitMin", LimitMin);
}
/** Get Start Range.
@return Start Range */
public BigDecimal getLimitMin() 
{
BigDecimal bd = (BigDecimal)get_Value("LimitMin");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Maximum Variation By Year.
@param MaximumVariationByYear Maximum Variation By Year */
public void setMaximumVariationByYear (BigDecimal MaximumVariationByYear)
{
set_Value ("MaximumVariationByYear", MaximumVariationByYear);
}
/** Get Maximum Variation By Year.
@return Maximum Variation By Year */
public BigDecimal getMaximumVariationByYear() 
{
BigDecimal bd = (BigDecimal)get_Value("MaximumVariationByYear");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set P_Credits_Bound_ID.
@param P_Credits_Bound_ID P_Credits_Bound_ID */
public void setP_Credits_Bound_ID (int P_Credits_Bound_ID)
{
if (P_Credits_Bound_ID < 1) throw new IllegalArgumentException ("P_Credits_Bound_ID is mandatory.");
set_Value ("P_Credits_Bound_ID", new Integer(P_Credits_Bound_ID));
}
/** Get P_Credits_Bound_ID.
@return P_Credits_Bound_ID */
public int getP_Credits_Bound_ID() 
{
Integer ii = (Integer)get_Value("P_Credits_Bound_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Parameters.
@param P_Credits_Param_ID Parameters */
public void setP_Credits_Param_ID (int P_Credits_Param_ID)
{
if (P_Credits_Param_ID < 1) throw new IllegalArgumentException ("P_Credits_Param_ID is mandatory.");
set_Value ("P_Credits_Param_ID", new Integer(P_Credits_Param_ID));
}
/** Get Parameters.
@return Parameters */
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
