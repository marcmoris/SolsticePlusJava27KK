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
/** Generated Model for P_Equivalence_Factor_Param
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Equivalence_Factor_Param extends PO
{
/** Standard Constructor
@param ctx context
@param P_Equivalence_Factor_Param_ID id
@param trxName transaction
*/
public X_P_Equivalence_Factor_Param (Properties ctx, int P_Equivalence_Factor_Param_ID, String trxName)
{
super (ctx, P_Equivalence_Factor_Param_ID, trxName);
/** if (P_Equivalence_Factor_Param_ID == 0)
{
setAnnual_Base (Env.ZERO);
setCalculPercentage (Env.ZERO);
setCARRA (null);
setExemptionPercentage (Env.ZERO);
setMaximumAmountETC (Env.ZERO);
setMaximumAmountFE (Env.ZERO);
setP_Deduction_ID (0);
setP_Equivalence_Factor_Param_ID (0);
setP_Year_ID (0);
setRRSP (Env.ZERO);
setYMPE (Env.ZERO);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Equivalence_Factor_Param (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100784 */
public static final int Table_ID=2100784;

/** TableName=P_Equivalence_Factor_Param */
public static final String Table_Name="P_Equivalence_Factor_Param";

protected static KeyNamePair Model = new KeyNamePair(2100784,"P_Equivalence_Factor_Param");

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
StringBuffer sb = new StringBuffer ("X_P_Equivalence_Factor_Param[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Annual Base.
@param Annual_Base Annual Base */
public void setAnnual_Base (BigDecimal Annual_Base)
{
if (Annual_Base == null) throw new IllegalArgumentException ("Annual_Base is mandatory.");
set_Value ("Annual_Base", Annual_Base);
}
/** Get Annual Base.
@return Annual Base */
public BigDecimal getAnnual_Base() 
{
BigDecimal bd = (BigDecimal)get_Value("Annual_Base");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Percentage of calculation.
@param CalculPercentage Percentage of calculation */
public void setCalculPercentage (BigDecimal CalculPercentage)
{
if (CalculPercentage == null) throw new IllegalArgumentException ("CalculPercentage is mandatory.");
set_Value ("CalculPercentage", CalculPercentage);
}
/** Get Percentage of calculation.
@return Percentage of calculation */
public BigDecimal getCalculPercentage() 
{
BigDecimal bd = (BigDecimal)get_Value("CalculPercentage");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Employer's no CARRA.
@param CARRA Employer's no CARRA */
public void setCARRA (String CARRA)
{
if (CARRA == null) throw new IllegalArgumentException ("CARRA is mandatory.");
if (CARRA.length() > 50)
{
log.warning("Length > 50 - truncated");
CARRA = CARRA.substring(0,49);
}
set_Value ("CARRA", CARRA);
}
/** Get Employer's no CARRA.
@return Employer's no CARRA */
public String getCARRA() 
{
return (String)get_Value("CARRA");
}
/** Set Percentage of exemption.
@param ExemptionPercentage Percentage of exemption */
public void setExemptionPercentage (BigDecimal ExemptionPercentage)
{
if (ExemptionPercentage == null) throw new IllegalArgumentException ("ExemptionPercentage is mandatory.");
set_Value ("ExemptionPercentage", ExemptionPercentage);
}
/** Get Percentage of exemption.
@return Percentage of exemption */
public BigDecimal getExemptionPercentage() 
{
BigDecimal bd = (BigDecimal)get_Value("ExemptionPercentage");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Maximum amount E.T.C..
@param MaximumAmountETC Maximum amount E.T.C. */
public void setMaximumAmountETC (BigDecimal MaximumAmountETC)
{
if (MaximumAmountETC == null) throw new IllegalArgumentException ("MaximumAmountETC is mandatory.");
set_Value ("MaximumAmountETC", MaximumAmountETC);
}
/** Get Maximum amount E.T.C..
@return Maximum amount E.T.C. */
public BigDecimal getMaximumAmountETC() 
{
BigDecimal bd = (BigDecimal)get_Value("MaximumAmountETC");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Maximum amount FE.
@param MaximumAmountFE Maximum amount FE */
public void setMaximumAmountFE (BigDecimal MaximumAmountFE)
{
if (MaximumAmountFE == null) throw new IllegalArgumentException ("MaximumAmountFE is mandatory.");
set_Value ("MaximumAmountFE", MaximumAmountFE);
}
/** Get Maximum amount FE.
@return Maximum amount FE */
public BigDecimal getMaximumAmountFE() 
{
BigDecimal bd = (BigDecimal)get_Value("MaximumAmountFE");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Deduction.
@param P_Deduction_ID   */
public void setP_Deduction_ID (int P_Deduction_ID)
{
if (P_Deduction_ID < 1) throw new IllegalArgumentException ("P_Deduction_ID is mandatory.");
set_Value ("P_Deduction_ID", new Integer(P_Deduction_ID));
}
/** Get Deduction.
@return   */
public int getP_Deduction_ID() 
{
Integer ii = (Integer)get_Value("P_Deduction_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_Equivalence_Factor_Param_ID.
@param P_Equivalence_Factor_Param_ID P_Equivalence_Factor_Param_ID */
public void setP_Equivalence_Factor_Param_ID (int P_Equivalence_Factor_Param_ID)
{
if (P_Equivalence_Factor_Param_ID < 1) throw new IllegalArgumentException ("P_Equivalence_Factor_Param_ID is mandatory.");
set_ValueNoCheck ("P_Equivalence_Factor_Param_ID", new Integer(P_Equivalence_Factor_Param_ID));
}
/** Get P_Equivalence_Factor_Param_ID.
@return P_Equivalence_Factor_Param_ID */
public int getP_Equivalence_Factor_Param_ID() 
{
Integer ii = (Integer)get_Value("P_Equivalence_Factor_Param_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Year_ID AD_Reference_ID=2000012 */
public static final int P_YEAR_ID_AD_Reference_ID=2000012;
/** Set Year.
@param P_Year_ID Calendar Year */
public void setP_Year_ID (int P_Year_ID)
{
if (P_Year_ID < 1) throw new IllegalArgumentException ("P_Year_ID is mandatory.");
set_Value ("P_Year_ID", new Integer(P_Year_ID));
}
/** Get Year.
@return Calendar Year */
public int getP_Year_ID() 
{
Integer ii = (Integer)get_Value("P_Year_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Deduction for RRSP.
@param RRSP Deduction for RRSP */
public void setRRSP (BigDecimal RRSP)
{
if (RRSP == null) throw new IllegalArgumentException ("RRSP is mandatory.");
set_Value ("RRSP", RRSP);
}
/** Get Deduction for RRSP.
@return Deduction for RRSP */
public BigDecimal getRRSP() 
{
BigDecimal bd = (BigDecimal)get_Value("RRSP");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set YMPE.
@param YMPE YMPE */
public void setYMPE (BigDecimal YMPE)
{
if (YMPE == null) throw new IllegalArgumentException ("YMPE is mandatory.");
set_Value ("YMPE", YMPE);
}
/** Get YMPE.
@return YMPE */
public BigDecimal getYMPE() 
{
BigDecimal bd = (BigDecimal)get_Value("YMPE");
if (bd == null) return Env.ZERO;
return bd;
}
}
