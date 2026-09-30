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
/** Generated Model for P_Credits_Family_Mth_Var
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Credits_Family_Mth_Var extends PO
{
/** Standard Constructor
@param ctx context
@param P_Credits_Family_Mth_Var_ID id
@param trxName transaction
*/
public X_P_Credits_Family_Mth_Var (Properties ctx, int P_Credits_Family_Mth_Var_ID, String trxName)
{
super (ctx, P_Credits_Family_Mth_Var_ID, trxName);
/** if (P_Credits_Family_Mth_Var_ID == 0)
{
setP_Credits_Family_ID (0);
setP_Credits_Family_Mth_Var_ID (0);
setP_Credits_Mth_Variation_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Credits_Family_Mth_Var (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000065 */
public static final int Table_ID=2000065;

/** TableName=P_Credits_Family_Mth_Var */
public static final String Table_Name="P_Credits_Family_Mth_Var";

protected static KeyNamePair Model = new KeyNamePair(2000065,"P_Credits_Family_Mth_Var");

protected BigDecimal accessLevel = new BigDecimal(7);
/** AccessLevel
@return 7 - System - Client - Org 
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
StringBuffer sb = new StringBuffer ("X_P_Credits_Family_Mth_Var[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Group.
@param P_Credits_Family_ID   */
public void setP_Credits_Family_ID (int P_Credits_Family_ID)
{
if (P_Credits_Family_ID < 1) throw new IllegalArgumentException ("P_Credits_Family_ID is mandatory.");
set_Value ("P_Credits_Family_ID", new Integer(P_Credits_Family_ID));
}
/** Get Group.
@return   */
public int getP_Credits_Family_ID() 
{
Integer ii = (Integer)get_Value("P_Credits_Family_ID");
if (ii == null) return 0;
return ii.intValue();
}
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
}
