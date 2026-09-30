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
/** Generated Model for P_Insurance_LTD
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.4 2008/03/12 20:02:49 marmor01 Exp $ */
public class X_P_Insurance_LTD extends PO
{
/** Standard Constructor
@param ctx context
@param P_Insurance_LTD_ID id
@param trxName transaction
*/
public X_P_Insurance_LTD (Properties ctx, int P_Insurance_LTD_ID, String trxName)
{
super (ctx, P_Insurance_LTD_ID, trxName);
/** if (P_Insurance_LTD_ID == 0)
{
setEffectIn (new Timestamp(System.currentTimeMillis()));
setP_Insurance_LTD_ID (0);
setP_Profile_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Insurance_LTD (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100865 */
public static final int Table_ID=2100865;

/** TableName=P_Insurance_LTD */
public static final String Table_Name="P_Insurance_LTD";

protected static KeyNamePair Model = new KeyNamePair(2100865,"P_Insurance_LTD");

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
StringBuffer sb = new StringBuffer ("X_P_Insurance_LTD[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Effective Date.
@param EffectIn Effective Date */
public void setEffectIn (Timestamp EffectIn)
{
if (EffectIn == null) throw new IllegalArgumentException ("EffectIn is mandatory.");
set_Value ("EffectIn", EffectIn);
}
/** Get Effective Date.
@return Effective Date */
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
/** Set Layer 01.
@param Layer01 Layer 01 */
public void setLayer01 (BigDecimal Layer01)
{
set_Value ("Layer01", Layer01);
}
/** Get Layer 01.
@return Layer 01 */
public BigDecimal getLayer01() 
{
BigDecimal bd = (BigDecimal)get_Value("Layer01");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Layer 02.
@param Layer02 Layer 02 */
public void setLayer02 (BigDecimal Layer02)
{
set_Value ("Layer02", Layer02);
}
/** Get Layer 02.
@return Layer 02 */
public BigDecimal getLayer02() 
{
BigDecimal bd = (BigDecimal)get_Value("Layer02");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Insurable Salary Ceiling.
@param MaxInsurableSalary Insurable Salary Ceiling */
public void setMaxInsurableSalary (BigDecimal MaxInsurableSalary)
{
set_Value ("MaxInsurableSalary", MaxInsurableSalary);
}
/** Get Insurable Salary Ceiling.
@return Insurable Salary Ceiling */
public BigDecimal getMaxInsurableSalary() 
{
BigDecimal bd = (BigDecimal)get_Value("MaxInsurableSalary");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set P_Insurance_LTD_ID.
@param P_Insurance_LTD_ID P_Insurance_LTD_ID */
public void setP_Insurance_LTD_ID (int P_Insurance_LTD_ID)
{
if (P_Insurance_LTD_ID < 1) throw new IllegalArgumentException ("P_Insurance_LTD_ID is mandatory.");
set_ValueNoCheck ("P_Insurance_LTD_ID", new Integer(P_Insurance_LTD_ID));
}
/** Get P_Insurance_LTD_ID.
@return P_Insurance_LTD_ID */
public int getP_Insurance_LTD_ID() 
{
Integer ii = (Integer)get_Value("P_Insurance_LTD_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Profile_ID AD_Reference_ID=2000030 */
public static final int P_PROFILE_ID_AD_Reference_ID=2000030;
/** Set Profile.
@param P_Profile_ID Profile */
public void setP_Profile_ID (int P_Profile_ID)
{
if (P_Profile_ID < 1) throw new IllegalArgumentException ("P_Profile_ID is mandatory.");
set_Value ("P_Profile_ID", new Integer(P_Profile_ID));
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
/** Set Rate 02.
@param Rate02 Rate 02 */
public void setRate02 (BigDecimal Rate02)
{
set_Value ("Rate02", Rate02);
}
/** Get Rate 02.
@return Rate 02 */
public BigDecimal getRate02() 
{
BigDecimal bd = (BigDecimal)get_Value("Rate02");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Rate 03.
@param Rate03 Rate 03 */
public void setRate03 (BigDecimal Rate03)
{
set_Value ("Rate03", Rate03);
}
/** Get Rate 03.
@return Rate 03 */
public BigDecimal getRate03() 
{
BigDecimal bd = (BigDecimal)get_Value("Rate03");
if (bd == null) return Env.ZERO;
return bd;
}
}
