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
/** Generated Model for P_Carra_Retro
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: X_P_Carra_Retro.java,v 1.1 2008/11/06 18:22:59 marmor01 Exp $ */
public class X_P_Carra_Retro extends PO
{
/** Standard Constructor
@param ctx context
@param P_Carra_Retro_ID id
@param trxName transaction
*/
public X_P_Carra_Retro (Properties ctx, int P_Carra_Retro_ID, String trxName)
{
super (ctx, P_Carra_Retro_ID, trxName);
/** if (P_Carra_Retro_ID == 0)
{
setP_Year_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Carra_Retro (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100904 */
public static final int Table_ID=2100904;

/** TableName=P_Carra_Retro */
public static final String Table_Name="P_Carra_Retro";

protected static KeyNamePair Model = new KeyNamePair(2100904,"P_Carra_Retro");

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
StringBuffer sb = new StringBuffer ("X_P_Carra_Retro[").append(get_ID()).append("]");
return sb.toString();
}
/** Set P_Carra_ID.
@param P_Carra_ID P_Carra_ID */
public void setP_Carra_ID (int P_Carra_ID)
{
if (P_Carra_ID <= 0) set_Value ("P_Carra_ID", null);
 else 
set_Value ("P_Carra_ID", new Integer(P_Carra_ID));
}
/** Get P_Carra_ID.
@return P_Carra_ID */
public int getP_Carra_ID() 
{
Integer ii = (Integer)get_Value("P_Carra_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_Carra_Retro_ID.
@param P_Carra_Retro_ID P_Carra_Retro_ID */
public void setP_Carra_Retro_ID (int P_Carra_Retro_ID)
{
if (P_Carra_Retro_ID <= 0) set_ValueNoCheck ("P_Carra_Retro_ID", null);
 else 
set_ValueNoCheck ("P_Carra_Retro_ID", new Integer(P_Carra_Retro_ID));
}
/** Get P_Carra_Retro_ID.
@return P_Carra_Retro_ID */
public int getP_Carra_Retro_ID() 
{
Integer ii = (Integer)get_Value("P_Carra_Retro_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Year_ID AD_Reference_ID=2000012 */
public static final int P_YEAR_ID_AD_Reference_ID=2000012;
/** Set Calendar Year.
@param P_Year_ID Calendar Year */
public void setP_Year_ID (int P_Year_ID)
{
if (P_Year_ID < 1) throw new IllegalArgumentException ("P_Year_ID is mandatory.");
set_Value ("P_Year_ID", new Integer(P_Year_ID));
}
/** Get Calendar Year.
@return Calendar Year */
public int getP_Year_ID() 
{
Integer ii = (Integer)get_Value("P_Year_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set RetroAmt.
@param RetroAmt RetroAmt */
public void setRetroAmt (BigDecimal RetroAmt)
{
set_Value ("RetroAmt", RetroAmt);
}
/** Get RetroAmt.
@return RetroAmt */
public BigDecimal getRetroAmt() 
{
BigDecimal bd = (BigDecimal)get_Value("RetroAmt");
if (bd == null) return Env.ZERO;
return bd;
}
}
