/******************************************************************************
* Product: Solstice+ Payroll & Human Resources Management                    *
* Copyright (C) 2008 ProGestion Informatique, Inc. All Rights Reserved.      *
* This program is free software, you can redistribute it and/or modify it    *
* under the terms version 2 of the GNU General Public License as published   *
* by the Free Software Foundation. This program is distributed in the hope   *
* that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
* warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
* See the GNU General Public License for more details.                       *
* You should have received a copy of the GNU General Public License along    *
* with this program, if not, write to the Free Software Foundation, Inc.,    *
* 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
* For the text or an alternative of this public license, you may reach us    *
* ProGestion Informatique, 210-5300 Bld des Galerie, Quebec, G2K 2A2 Canada  *
* or via info@progestion.net or http://www.progestion.net/license.html       *
******************************************************************************/
package solstice.model;

/** Generated Model - DO NOT CHANGE */
import org.compiere.model.*;
import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;
/** Generated Model for P_Gain_Account_360
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Gain_Account_360 extends PO
{
/** Standard Constructor
@param ctx context
@param P_Gain_Account_360_ID id
@param trxName transaction
*/
public X_P_Gain_Account_360 (Properties ctx, int P_Gain_Account_360_ID, String trxName)
{
super (ctx, P_Gain_Account_360_ID, trxName);
/** if (P_Gain_Account_360_ID == 0)
{
setP_Gain_Account_360_ID (0);
setP_Gain_Acct (0);
setP_Gain_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Gain_Account_360 (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=3201220 */
public static final int Table_ID=3201220;

/** TableName=P_Gain_Account_360 */
public static final String Table_Name="P_Gain_Account_360";

protected static KeyNamePair Model = new KeyNamePair(3201220,"P_Gain_Account_360");

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
StringBuffer sb = new StringBuffer ("X_P_Gain_Account_360[").append(get_ID()).append("]");
return sb.toString();
}

/** C_AcctSchema_ID AD_Reference_ID=136 */
public static final int C_ACCTSCHEMA_ID_AD_Reference_ID=136;
/** Set Accounting Schema.
@param C_AcctSchema_ID Rules for accounting */
public void setC_AcctSchema_ID (int C_AcctSchema_ID)
{
if (C_AcctSchema_ID <= 0) set_Value ("C_AcctSchema_ID", null);
 else 
set_Value ("C_AcctSchema_ID", new Integer(C_AcctSchema_ID));
}
/** Get Accounting Schema.
@return Rules for accounting */
public int getC_AcctSchema_ID() 
{
Integer ii = (Integer)get_Value("C_AcctSchema_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** C_Activity_ID AD_Reference_ID=142 */
public static final int C_ACTIVITY_ID_AD_Reference_ID=142;
/** Set Function.
@param C_Activity_ID Function */
public void setC_Activity_ID (int C_Activity_ID)
{
if (C_Activity_ID <= 0) set_Value ("C_Activity_ID", null);
 else 
set_Value ("C_Activity_ID", new Integer(C_Activity_ID));
}
/** Get Function.
@return Function */
public int getC_Activity_ID() 
{
Integer ii = (Integer)get_Value("C_Activity_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Code main d'oeuvre.
@param C_SalesRegion_ID Code main d'oeuvre */
public void setC_SalesRegion_ID (int C_SalesRegion_ID)
{
if (C_SalesRegion_ID <= 0) set_Value ("C_SalesRegion_ID", null);
 else 
set_Value ("C_SalesRegion_ID", new Integer(C_SalesRegion_ID));
}
/** Get Code main d'oeuvre.
@return Code main d'oeuvre */
public int getC_SalesRegion_ID() 
{
Integer ii = (Integer)get_Value("C_SalesRegion_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_Gain_Account_360_ID.
@param P_Gain_Account_360_ID P_Gain_Account_360_ID */
public void setP_Gain_Account_360_ID (int P_Gain_Account_360_ID)
{
if (P_Gain_Account_360_ID < 1) throw new IllegalArgumentException ("P_Gain_Account_360_ID is mandatory.");
set_ValueNoCheck ("P_Gain_Account_360_ID", new Integer(P_Gain_Account_360_ID));
}
/** Get P_Gain_Account_360_ID.
@return P_Gain_Account_360_ID */
public int getP_Gain_Account_360_ID() 
{
Integer ii = (Integer)get_Value("P_Gain_Account_360_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Gain_Acct AD_Reference_ID=182 */
public static final int P_GAIN_ACCT_AD_Reference_ID=182;
/** Set Expense.
@param P_Gain_Acct Expense Account */
public void setP_Gain_Acct (int P_Gain_Acct)
{
set_Value ("P_Gain_Acct", new Integer(P_Gain_Acct));
}
/** Get Expense.
@return Expense Account */
public int getP_Gain_Acct() 
{
Integer ii = (Integer)get_Value("P_Gain_Acct");
if (ii == null) return 0;
return ii.intValue();
}
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
/** Set Occupation Group.
@param P_Occupation_Group_ID   */
public void setP_Occupation_Group_ID (int P_Occupation_Group_ID)
{
if (P_Occupation_Group_ID <= 0) set_Value ("P_Occupation_Group_ID", null);
 else 
set_Value ("P_Occupation_Group_ID", new Integer(P_Occupation_Group_ID));
}
/** Get Occupation Group.
@return   */
public int getP_Occupation_Group_ID() 
{
Integer ii = (Integer)get_Value("P_Occupation_Group_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
