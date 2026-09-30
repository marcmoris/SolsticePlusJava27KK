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
/** Generated Model for P_Credits_Param_Hst
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Credits_Param_Hst extends PO
{
/** Standard Constructor
@param ctx context
@param P_Credits_Param_Hst_ID id
@param trxName transaction
*/
public X_P_Credits_Param_Hst (Properties ctx, int P_Credits_Param_Hst_ID, String trxName)
{
super (ctx, P_Credits_Param_Hst_ID, trxName);
/** if (P_Credits_Param_Hst_ID == 0)
{
setEffectIn (new Timestamp(System.currentTimeMillis()));	// @#Date@
setGainAnnual (0);
setGainParting (0);
setMethod_Annual (null);
setMethod_Parting (null);
setP_Credits_Family_Mth_Eval_ID (0);
setP_Credits_Family_Mth_Var_ID (0);
setP_Credits_ID (0);
setP_Credits_Mth_Evaluation_ID (0);
setP_Credits_Mth_Variation_ID (0);
setP_Credits_Param_Hst_ID (0);
setP_Method_Credits_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Credits_Param_Hst (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100688 */
public static final int Table_ID=2100688;

/** TableName=P_Credits_Param_Hst */
public static final String Table_Name="P_Credits_Param_Hst";

protected static KeyNamePair Model = new KeyNamePair(2100688,"P_Credits_Param_Hst");

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
StringBuffer sb = new StringBuffer ("X_P_Credits_Param_Hst[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Affected By RWT.
@param AffectedByRWT Affected By RWT */
public void setAffectedByRWT (boolean AffectedByRWT)
{
set_Value ("AffectedByRWT", new Boolean(AffectedByRWT));
}
/** Get Affected By RWT.
@return Affected By RWT */
public boolean isAffectedByRWT() 
{
Object oo = get_Value("AffectedByRWT");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Quantity Annual.
@param CreditsAnnual   */
public void setCreditsAnnual (BigDecimal CreditsAnnual)
{
set_Value ("CreditsAnnual", CreditsAnnual);
}
/** Get Quantity Annual.
@return   */
public BigDecimal getCreditsAnnual() 
{
BigDecimal bd = (BigDecimal)get_Value("CreditsAnnual");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Initial Credits.
@param CreditsInitial Initial Credits */
public void setCreditsInitial (BigDecimal CreditsInitial)
{
set_Value ("CreditsInitial", CreditsInitial);
}
/** Get Initial Credits.
@return Initial Credits */
public BigDecimal getCreditsInitial() 
{
BigDecimal bd = (BigDecimal)get_Value("CreditsInitial");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Variation.
@param CreditsVariation Variation */
public void setCreditsVariation (BigDecimal CreditsVariation)
{
set_Value ("CreditsVariation", CreditsVariation);
}
/** Get Variation.
@return Variation */
public BigDecimal getCreditsVariation() 
{
BigDecimal bd = (BigDecimal)get_Value("CreditsVariation");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Cumulated Hours.
@param CumulatedHours Cumulated Hours */
public void setCumulatedHours (boolean CumulatedHours)
{
set_Value ("CumulatedHours", new Boolean(CumulatedHours));
}
/** Get Cumulated Hours.
@return Cumulated Hours */
public boolean isCumulatedHours() 
{
Object oo = get_Value("CumulatedHours");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Description.
@param Description Optional short description of the record */
public void setDescription (String Description)
{
if (Description != null && Description.length() > 255)
{
log.warning("Length > 255 - truncated");
Description = Description.substring(0,254);
}
set_Value ("Description", Description);
}
/** Get Description.
@return Optional short description of the record */
public String getDescription() 
{
return (String)get_Value("Description");
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

/** GainAnnual AD_Reference_ID=2100317 */
public static final int GAINANNUAL_AD_Reference_ID=2100317;
/** Set Gain.
@param GainAnnual Gain */
public void setGainAnnual (int GainAnnual)
{
set_Value ("GainAnnual", new Integer(GainAnnual));
}
/** Get Gain.
@return Gain */
public int getGainAnnual() 
{
Integer ii = (Integer)get_Value("GainAnnual");
if (ii == null) return 0;
return ii.intValue();
}

/** GainParting AD_Reference_ID=2100317 */
public static final int GAINPARTING_AD_Reference_ID=2100317;
/** Set Gain.
@param GainParting Gain */
public void setGainParting (int GainParting)
{
set_Value ("GainParting", new Integer(GainParting));
}
/** Get Gain.
@return Gain */
public int getGainParting() 
{
Integer ii = (Integer)get_Value("GainParting");
if (ii == null) return 0;
return ii.intValue();
}

/** Method_Annual AD_Reference_ID=2000032 */
public static final int METHOD_ANNUAL_AD_Reference_ID=2000032;
/** Solde is reseted = 0 */
public static final String METHOD_ANNUAL_SoldeIsReseted = "0";
/** Sold is preserved = 1 */
public static final String METHOD_ANNUAL_SoldIsPreserved = "1";
/** Sold is transferred in a others accumulated credits = 2 */
public static final String METHOD_ANNUAL_SoldIsTransferredInAOthersAccumulatedCredits = "2";
/** Sold is pay  = 3 */
public static final String METHOD_ANNUAL_SoldIsPay = "3";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isMethod_AnnualValid (String test)
{
return test.equals("0") || test.equals("1") || test.equals("2") || test.equals("3");
}
/** Set Annual Method.
@param Method_Annual Annual Method */
public void setMethod_Annual (String Method_Annual)
{
if (Method_Annual == null) throw new IllegalArgumentException ("Method_Annual is mandatory");
if (!isMethod_AnnualValid(Method_Annual))
throw new IllegalArgumentException ("Method_Annual Invalid value - " + Method_Annual + " - Reference_ID=2000032 - 0 - 1 - 2 - 3");
if (Method_Annual.length() > 2)
{
log.warning("Length > 2 - truncated");
Method_Annual = Method_Annual.substring(0,1);
}
set_Value ("Method_Annual", Method_Annual);
}
/** Get Annual Method.
@return Annual Method */
public String getMethod_Annual() 
{
return (String)get_Value("Method_Annual");
}

/** Method_Parting AD_Reference_ID=2000033 */
public static final int METHOD_PARTING_AD_Reference_ID=2000033;
/** Sold is reseted = 0 */
public static final String METHOD_PARTING_SoldIsReseted = "0";
/** Sold is pay = 1 */
public static final String METHOD_PARTING_SoldIsPay = "1";
/** Sold is preserved = 2 */
public static final String METHOD_PARTING_SoldIsPreserved = "2";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isMethod_PartingValid (String test)
{
return test.equals("0") || test.equals("1") || test.equals("2");
}
/** Set Method.
@param Method_Parting   */
public void setMethod_Parting (String Method_Parting)
{
if (Method_Parting == null) throw new IllegalArgumentException ("Method_Parting is mandatory");
if (!isMethod_PartingValid(Method_Parting))
throw new IllegalArgumentException ("Method_Parting Invalid value - " + Method_Parting + " - Reference_ID=2000033 - 0 - 1 - 2");
if (Method_Parting.length() > 2)
{
log.warning("Length > 2 - truncated");
Method_Parting = Method_Parting.substring(0,1);
}
set_Value ("Method_Parting", Method_Parting);
}
/** Get Method.
@return   */
public String getMethod_Parting() 
{
return (String)get_Value("Method_Parting");
}

/** P_Credits_Family_Mth_Eval_ID AD_Reference_ID=2000037 */
public static final int P_CREDITS_FAMILY_MTH_EVAL_ID_AD_Reference_ID=2000037;
/** Set Evaluation.
@param P_Credits_Family_Mth_Eval_ID Evaluation */
public void setP_Credits_Family_Mth_Eval_ID (int P_Credits_Family_Mth_Eval_ID)
{
if (P_Credits_Family_Mth_Eval_ID < 1) throw new IllegalArgumentException ("P_Credits_Family_Mth_Eval_ID is mandatory.");
set_Value ("P_Credits_Family_Mth_Eval_ID", new Integer(P_Credits_Family_Mth_Eval_ID));
}
/** Get Evaluation.
@return Evaluation */
public int getP_Credits_Family_Mth_Eval_ID() 
{
Integer ii = (Integer)get_Value("P_Credits_Family_Mth_Eval_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Credits_Family_Mth_Var_ID AD_Reference_ID=2000036 */
public static final int P_CREDITS_FAMILY_MTH_VAR_ID_AD_Reference_ID=2000036;
/** Set Variation.
@param P_Credits_Family_Mth_Var_ID Variation */
public void setP_Credits_Family_Mth_Var_ID (int P_Credits_Family_Mth_Var_ID)
{
if (P_Credits_Family_Mth_Var_ID < 1) throw new IllegalArgumentException ("P_Credits_Family_Mth_Var_ID is mandatory.");
set_Value ("P_Credits_Family_Mth_Var_ID", new Integer(P_Credits_Family_Mth_Var_ID));
}
/** Get Variation.
@return Variation */
public int getP_Credits_Family_Mth_Var_ID() 
{
Integer ii = (Integer)get_Value("P_Credits_Family_Mth_Var_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Credits_ID AD_Reference_ID=2100318 */
public static final int P_CREDITS_ID_AD_Reference_ID=2100318;
/** Set Credits.
@param P_Credits_ID Credits */
public void setP_Credits_ID (int P_Credits_ID)
{
if (P_Credits_ID < 1) throw new IllegalArgumentException ("P_Credits_ID is mandatory.");
set_ValueNoCheck ("P_Credits_ID", new Integer(P_Credits_ID));
}
/** Get Credits.
@return Credits */
public int getP_Credits_ID() 
{
Integer ii = (Integer)get_Value("P_Credits_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Credits_Join AD_Reference_ID=2100318 */
public static final int P_CREDITS_JOIN_AD_Reference_ID=2100318;
/** Set Credit Join.
@param P_Credits_Join    */
public void setP_Credits_Join (int P_Credits_Join)
{
set_Value ("P_Credits_Join", new Integer(P_Credits_Join));
}
/** Get Credit Join.
@return    */
public int getP_Credits_Join() 
{
Integer ii = (Integer)get_Value("P_Credits_Join");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Credits_Mth_Evaluation_ID AD_Reference_ID=2000037 */
public static final int P_CREDITS_MTH_EVALUATION_ID_AD_Reference_ID=2000037;
/** Set Evaluation Method.
@param P_Credits_Mth_Evaluation_ID Evaluation Method */
public void setP_Credits_Mth_Evaluation_ID (int P_Credits_Mth_Evaluation_ID)
{
if (P_Credits_Mth_Evaluation_ID < 1) throw new IllegalArgumentException ("P_Credits_Mth_Evaluation_ID is mandatory.");
set_Value ("P_Credits_Mth_Evaluation_ID", new Integer(P_Credits_Mth_Evaluation_ID));
}
/** Get Evaluation Method.
@return Evaluation Method */
public int getP_Credits_Mth_Evaluation_ID() 
{
Integer ii = (Integer)get_Value("P_Credits_Mth_Evaluation_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Credits_Mth_Variation_ID AD_Reference_ID=2000036 */
public static final int P_CREDITS_MTH_VARIATION_ID_AD_Reference_ID=2000036;
/** Set Variation Method.
@param P_Credits_Mth_Variation_ID Variation Method */
public void setP_Credits_Mth_Variation_ID (int P_Credits_Mth_Variation_ID)
{
if (P_Credits_Mth_Variation_ID < 1) throw new IllegalArgumentException ("P_Credits_Mth_Variation_ID is mandatory.");
set_Value ("P_Credits_Mth_Variation_ID", new Integer(P_Credits_Mth_Variation_ID));
}
/** Get Variation Method.
@return Variation Method */
public int getP_Credits_Mth_Variation_ID() 
{
Integer ii = (Integer)get_Value("P_Credits_Mth_Variation_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_Credits_Param_Hst_ID.
@param P_Credits_Param_Hst_ID P_Credits_Param_Hst_ID */
public void setP_Credits_Param_Hst_ID (int P_Credits_Param_Hst_ID)
{
if (P_Credits_Param_Hst_ID < 1) throw new IllegalArgumentException ("P_Credits_Param_Hst_ID is mandatory.");
set_Value ("P_Credits_Param_Hst_ID", new Integer(P_Credits_Param_Hst_ID));
}
/** Get P_Credits_Param_Hst_ID.
@return P_Credits_Param_Hst_ID */
public int getP_Credits_Param_Hst_ID() 
{
Integer ii = (Integer)get_Value("P_Credits_Param_Hst_ID");
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

/** P_Credits_Target AD_Reference_ID=2100318 */
public static final int P_CREDITS_TARGET_AD_Reference_ID=2100318;
/** Set Accumulated Target.
@param P_Credits_Target Accumulated Target */
public void setP_Credits_Target (int P_Credits_Target)
{
set_Value ("P_Credits_Target", new Integer(P_Credits_Target));
}
/** Get Accumulated Target.
@return Accumulated Target */
public int getP_Credits_Target() 
{
Integer ii = (Integer)get_Value("P_Credits_Target");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Method_Credits_ID AD_Reference_ID=2100392 */
public static final int P_METHOD_CREDITS_ID_AD_Reference_ID=2100392;
/** Set Method.
@param P_Method_Credits_ID Method */
public void setP_Method_Credits_ID (int P_Method_Credits_ID)
{
if (P_Method_Credits_ID < 1) throw new IllegalArgumentException ("P_Method_Credits_ID is mandatory.");
set_Value ("P_Method_Credits_ID", new Integer(P_Method_Credits_ID));
}
/** Get Method.
@return Method */
public int getP_Method_Credits_ID() 
{
Integer ii = (Integer)get_Value("P_Method_Credits_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
