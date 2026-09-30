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
/** Generated Model for P_Insurance_STD
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Insurance_STD extends PO
{
/** Standard Constructor
@param ctx context
@param P_Insurance_STD_ID id
@param trxName transaction
*/
public X_P_Insurance_STD (Properties ctx, int P_Insurance_STD_ID, String trxName)
{
super (ctx, P_Insurance_STD_ID, trxName);
/** if (P_Insurance_STD_ID == 0)
{
setP_Insurance_STD_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Insurance_STD (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=3201190 */
public static final int Table_ID=3201190;

/** TableName=P_Insurance_STD */
public static final String Table_Name="P_Insurance_STD";

protected static KeyNamePair Model = new KeyNamePair(3201190,"P_Insurance_STD");

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
StringBuffer sb = new StringBuffer ("X_P_Insurance_STD[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Effect In.
@param EffectIn Date when information in effect  */
public void setEffectIn (Timestamp EffectIn)
{
set_Value ("EffectIn", EffectIn);
}
/** Get Effect In.
@return Date when information in effect  */
public Timestamp getEffectIn() 
{
return (Timestamp)get_Value("EffectIn");
}
/** Set To.
@param EffectTo To */
public void setEffectTo (Timestamp EffectTo)
{
set_Value ("EffectTo", EffectTo);
}
/** Get To.
@return To */
public Timestamp getEffectTo() 
{
return (Timestamp)get_Value("EffectTo");
}
/** Set Max Insurable Salary.
@param MaxInsurableSalary Max Insurable Salary */
public void setMaxInsurableSalary (BigDecimal MaxInsurableSalary)
{
set_Value ("MaxInsurableSalary", MaxInsurableSalary);
}
/** Get Max Insurable Salary.
@return Max Insurable Salary */
public BigDecimal getMaxInsurableSalary() 
{
BigDecimal bd = (BigDecimal)get_Value("MaxInsurableSalary");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set P_Insurance_STD_ID.
@param P_Insurance_STD_ID P_Insurance_STD_ID */
public void setP_Insurance_STD_ID (int P_Insurance_STD_ID)
{
if (P_Insurance_STD_ID < 1) throw new IllegalArgumentException ("P_Insurance_STD_ID is mandatory.");
set_ValueNoCheck ("P_Insurance_STD_ID", new Integer(P_Insurance_STD_ID));
}
/** Get P_Insurance_STD_ID.
@return P_Insurance_STD_ID */
public int getP_Insurance_STD_ID() 
{
Integer ii = (Integer)get_Value("P_Insurance_STD_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Profile_ID AD_Reference_ID=2000030 */
public static final int P_PROFILE_ID_AD_Reference_ID=2000030;
/** Set Profile.
@param P_Profile_ID Profile */
public void setP_Profile_ID (int P_Profile_ID)
{
if (P_Profile_ID <= 0) set_ValueNoCheck ("P_Profile_ID", null);
 else 
set_ValueNoCheck ("P_Profile_ID", new Integer(P_Profile_ID));
}
/** Get Profile.
@return Profile */
public int getP_Profile_ID() 
{
Integer ii = (Integer)get_Value("P_Profile_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Rate 01.
@param Rate01 Rate 01 */
public void setRate01 (BigDecimal Rate01)
{
set_Value ("Rate01", Rate01);
}
/** Get Rate 01.
@return Rate 01 */
public BigDecimal getRate01() 
{
BigDecimal bd = (BigDecimal)get_Value("Rate01");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set SalaryRate.
@param SalaryRate SalaryRate */
public void setSalaryRate (BigDecimal SalaryRate)
{
set_Value ("SalaryRate", SalaryRate);
}
/** Get SalaryRate.
@return SalaryRate */
public BigDecimal getSalaryRate() 
{
BigDecimal bd = (BigDecimal)get_Value("SalaryRate");
if (bd == null) return Env.ZERO;
return bd;
}
}
