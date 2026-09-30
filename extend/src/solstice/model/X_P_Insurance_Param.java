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
/** Generated Model for P_Insurance_Param
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Insurance_Param extends PO
{
/** Standard Constructor
@param ctx context
@param P_Insurance_Param_ID id
@param trxName transaction
*/
public X_P_Insurance_Param (Properties ctx, int P_Insurance_Param_ID, String trxName)
{
super (ctx, P_Insurance_Param_ID, trxName);
/** if (P_Insurance_Param_ID == 0)
{
setP_Insurance_Param_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Insurance_Param (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100864 */
public static final int Table_ID=2100864;

/** TableName=P_Insurance_Param */
public static final String Table_Name="P_Insurance_Param";

protected static KeyNamePair Model = new KeyNamePair(2100864,"P_Insurance_Param");

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
StringBuffer sb = new StringBuffer ("X_P_Insurance_Param[").append(get_ID()).append("]");
return sb.toString();
}
/** Set MinimumSalaryInsurable.
@param MinimumSalaryInsurable MinimumSalaryInsurable */
public void setMinimumSalaryInsurable (BigDecimal MinimumSalaryInsurable)
{
set_Value ("MinimumSalaryInsurable", MinimumSalaryInsurable);
}
/** Get MinimumSalaryInsurable.
@return MinimumSalaryInsurable */
public BigDecimal getMinimumSalaryInsurable() 
{
BigDecimal bd = (BigDecimal)get_Value("MinimumSalaryInsurable");
if (bd == null) return Env.ZERO;
return bd;
}

/** P_Department_ID AD_Reference_ID=2100460 */
public static final int P_DEPARTMENT_ID_AD_Reference_ID=2100460;
/** Set Department.
@param P_Department_ID Department */
public void setP_Department_ID (int P_Department_ID)
{
if (P_Department_ID <= 0) set_Value ("P_Department_ID", null);
 else 
set_Value ("P_Department_ID", new Integer(P_Department_ID));
}
/** Get Department.
@return Department */
public int getP_Department_ID() 
{
Integer ii = (Integer)get_Value("P_Department_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_Insurance_Param_ID.
@param P_Insurance_Param_ID P_Insurance_Param_ID */
public void setP_Insurance_Param_ID (int P_Insurance_Param_ID)
{
if (P_Insurance_Param_ID < 1) throw new IllegalArgumentException ("P_Insurance_Param_ID is mandatory.");
set_ValueNoCheck ("P_Insurance_Param_ID", new Integer(P_Insurance_Param_ID));
}
/** Get P_Insurance_Param_ID.
@return P_Insurance_Param_ID */
public int getP_Insurance_Param_ID() 
{
Integer ii = (Integer)get_Value("P_Insurance_Param_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Program.
@param P_Insurance_Program_ID Program */
public void setP_Insurance_Program_ID (int P_Insurance_Program_ID)
{
if (P_Insurance_Program_ID <= 0) set_Value ("P_Insurance_Program_ID", null);
 else 
set_Value ("P_Insurance_Program_ID", new Integer(P_Insurance_Program_ID));
}
/** Get Program.
@return Program */
public int getP_Insurance_Program_ID() 
{
Integer ii = (Integer)get_Value("P_Insurance_Program_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
