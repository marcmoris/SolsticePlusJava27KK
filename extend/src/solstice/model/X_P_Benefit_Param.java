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
/** Generated Model for P_Benefit_Param
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Benefit_Param extends PO
{
/** Standard Constructor
@param ctx context
@param P_Benefit_Param_ID id
@param trxName transaction
*/
public X_P_Benefit_Param (Properties ctx, int P_Benefit_Param_ID, String trxName)
{
super (ctx, P_Benefit_Param_ID, trxName);
/** if (P_Benefit_Param_ID == 0)
{
setC_Activity_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Benefit_Param (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100874 */
public static final int Table_ID=2100874;

/** TableName=P_Benefit_Param */
public static final String Table_Name="P_Benefit_Param";

protected static KeyNamePair Model = new KeyNamePair(2100874,"P_Benefit_Param");

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
StringBuffer sb = new StringBuffer ("X_P_Benefit_Param[").append(get_ID()).append("]");
return sb.toString();
}

/** ACCT_CREDIT AD_Reference_ID=132 */
public static final int ACCT_CREDIT_AD_Reference_ID=132;
/** Set Credit.
@param ACCT_CREDIT Credit */
public void setACCT_CREDIT (int ACCT_CREDIT)
{
set_Value ("ACCT_CREDIT", new Integer(ACCT_CREDIT));
}
/** Get Credit.
@return Credit */
public int getACCT_CREDIT() 
{
Integer ii = (Integer)get_Value("ACCT_CREDIT");
if (ii == null) return 0;
return ii.intValue();
}

/** ACCT_DEBIT AD_Reference_ID=132 */
public static final int ACCT_DEBIT_AD_Reference_ID=132;
/** Set Debit.
@param ACCT_DEBIT Debit */
public void setACCT_DEBIT (int ACCT_DEBIT)
{
set_Value ("ACCT_DEBIT", new Integer(ACCT_DEBIT));
}
/** Get Debit.
@return Debit */
public int getACCT_DEBIT() 
{
Integer ii = (Integer)get_Value("ACCT_DEBIT");
if (ii == null) return 0;
return ii.intValue();
}

/** C_Activity_ID AD_Reference_ID=142 */
public static final int C_ACTIVITY_ID_AD_Reference_ID=142;
/** Set Crew cost.
@param C_Activity_ID Crew cost */
public void setC_Activity_ID (int C_Activity_ID)
{
if (C_Activity_ID < 1) throw new IllegalArgumentException ("C_Activity_ID is mandatory.");
set_Value ("C_Activity_ID", new Integer(C_Activity_ID));
}
/** Get Crew cost.
@return Crew cost */
public int getC_Activity_ID() 
{
Integer ii = (Integer)get_Value("C_Activity_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Get Record ID/ColumnName
@return ID/ColumnName pair */
public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), String.valueOf(getC_Activity_ID()));
}
/** Set P_BENEFIT_PARAM_ID.
@param P_BENEFIT_PARAM_ID P_BENEFIT_PARAM_ID */
public void setP_BENEFIT_PARAM_ID (int P_BENEFIT_PARAM_ID)
{
if (P_BENEFIT_PARAM_ID <= 0) set_ValueNoCheck ("P_BENEFIT_PARAM_ID", null);
 else 
set_ValueNoCheck ("P_BENEFIT_PARAM_ID", new Integer(P_BENEFIT_PARAM_ID));
}
/** Get P_BENEFIT_PARAM_ID.
@return P_BENEFIT_PARAM_ID */
public int getP_BENEFIT_PARAM_ID() 
{
Integer ii = (Integer)get_Value("P_BENEFIT_PARAM_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Percentage.
@param Percentage Percent of the entire amount */
public void setPercentage (BigDecimal Percentage)
{
set_Value ("Percentage", Percentage);
}
/** Get Percentage.
@return Percent of the entire amount */
public BigDecimal getPercentage() 
{
BigDecimal bd = (BigDecimal)get_Value("Percentage");
if (bd == null) return Env.ZERO;
return bd;
}
}
