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
/** Generated Model for P_Deduction_Param_Ympe
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Deduction_Param_Ympe extends PO
{
/** Standard Constructor
@param ctx context
@param P_Deduction_Param_Ympe_ID id
@param trxName transaction
*/
public X_P_Deduction_Param_Ympe (Properties ctx, int P_Deduction_Param_Ympe_ID, String trxName)
{
super (ctx, P_Deduction_Param_Ympe_ID, trxName);
/** if (P_Deduction_Param_Ympe_ID == 0)
{
setP_Deduction_Param_ID (0);
setP_Deduction_Param_Ympe_ID (0);
setYmpeMin (0);
setYmpeRate (Env.ZERO);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Deduction_Param_Ympe (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000068 */
public static final int Table_ID=2000068;

/** TableName=P_Deduction_Param_Ympe */
public static final String Table_Name="P_Deduction_Param_Ympe";

protected static KeyNamePair Model = new KeyNamePair(2000068,"P_Deduction_Param_Ympe");

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
StringBuffer sb = new StringBuffer ("X_P_Deduction_Param_Ympe[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Deduction.
@param P_Deduction_Param_ID Deduction */
public void setP_Deduction_Param_ID (int P_Deduction_Param_ID)
{
if (P_Deduction_Param_ID < 1) throw new IllegalArgumentException ("P_Deduction_Param_ID is mandatory.");
set_Value ("P_Deduction_Param_ID", new Integer(P_Deduction_Param_ID));
}
/** Get Deduction.
@return Deduction */
public int getP_Deduction_Param_ID() 
{
Integer ii = (Integer)get_Value("P_Deduction_Param_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set YMPE.
@param P_Deduction_Param_Ympe_ID year's maximum pensionable ear */
public void setP_Deduction_Param_Ympe_ID (int P_Deduction_Param_Ympe_ID)
{
if (P_Deduction_Param_Ympe_ID < 1) throw new IllegalArgumentException ("P_Deduction_Param_Ympe_ID is mandatory.");
set_Value ("P_Deduction_Param_Ympe_ID", new Integer(P_Deduction_Param_Ympe_ID));
}
/** Get YMPE.
@return year's maximum pensionable ear */
public int getP_Deduction_Param_Ympe_ID() 
{
Integer ii = (Integer)get_Value("P_Deduction_Param_Ympe_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Employer Rate.
@param YmpeEmployerRate Employer Rate */
public void setYmpeEmployerRate (BigDecimal YmpeEmployerRate)
{
set_Value ("YmpeEmployerRate", YmpeEmployerRate);
}
/** Get Employer Rate.
@return Employer Rate */
public BigDecimal getYmpeEmployerRate() 
{
BigDecimal bd = (BigDecimal)get_Value("YmpeEmployerRate");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Minimum.
@param YmpeMin Minimum */
public void setYmpeMin (int YmpeMin)
{
set_Value ("YmpeMin", new Integer(YmpeMin));
}
/** Get Minimum.
@return Minimum */
public int getYmpeMin() 
{
Integer ii = (Integer)get_Value("YmpeMin");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Rate.
@param YmpeRate Rate */
public void setYmpeRate (BigDecimal YmpeRate)
{
if (YmpeRate == null) throw new IllegalArgumentException ("YmpeRate is mandatory.");
set_Value ("YmpeRate", YmpeRate);
}
/** Get Rate.
@return Rate */
public BigDecimal getYmpeRate() 
{
BigDecimal bd = (BigDecimal)get_Value("YmpeRate");
if (bd == null) return Env.ZERO;
return bd;
}
}
