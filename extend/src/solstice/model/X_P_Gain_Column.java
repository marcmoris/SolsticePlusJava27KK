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
/** Generated Model for P_Gain_Column
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Gain_Column extends PO
{
/** Standard Constructor
@param ctx context
@param P_Gain_Column_ID id
@param trxName transaction
*/
public X_P_Gain_Column (Properties ctx, int P_Gain_Column_ID, String trxName)
{
super (ctx, P_Gain_Column_ID, trxName);
/** if (P_Gain_Column_ID == 0)
{
setIsAdmissible (false);	// N
setP_Column_Adm_ID (0);
setP_Gain_Parameter_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Gain_Column (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000011 */
public static final int Table_ID=2000011;

/** TableName=P_Gain_Column */
public static final String Table_Name="P_Gain_Column";

protected static KeyNamePair Model = new KeyNamePair(2000011,"P_Gain_Column");

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
StringBuffer sb = new StringBuffer ("X_P_Gain_Column[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Is Admissible.
@param IsAdmissible   */
public void setIsAdmissible (boolean IsAdmissible)
{
set_Value ("IsAdmissible", new Boolean(IsAdmissible));
}
/** Get Is Admissible.
@return   */
public boolean isAdmissible() 
{
Object oo = get_Value("IsAdmissible");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}

/** P_Column_Adm_ID AD_Reference_ID=2000000 */
public static final int P_COLUMN_ADM_ID_AD_Reference_ID=2000000;
/** Set Admissibility Column.
@param P_Column_Adm_ID Admissibility Column */
public void setP_Column_Adm_ID (int P_Column_Adm_ID)
{
if (P_Column_Adm_ID < 1) throw new IllegalArgumentException ("P_Column_Adm_ID is mandatory.");
set_ValueNoCheck ("P_Column_Adm_ID", new Integer(P_Column_Adm_ID));
}
/** Get Admissibility Column.
@return Admissibility Column */
public int getP_Column_Adm_ID() 
{
Integer ii = (Integer)get_Value("P_Column_Adm_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_Gain_Column_ID.
@param P_Gain_Column_ID P_Gain_Column_ID */
public void setP_Gain_Column_ID (int P_Gain_Column_ID)
{
if (P_Gain_Column_ID <= 0) set_Value ("P_Gain_Column_ID", null);
 else 
set_Value ("P_Gain_Column_ID", new Integer(P_Gain_Column_ID));
}
/** Get P_Gain_Column_ID.
@return P_Gain_Column_ID */
public int getP_Gain_Column_ID() 
{
Integer ii = (Integer)get_Value("P_Gain_Column_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Gain Parameter.
@param P_Gain_Parameter_ID Gain Parameter */
public void setP_Gain_Parameter_ID (int P_Gain_Parameter_ID)
{
if (P_Gain_Parameter_ID < 1) throw new IllegalArgumentException ("P_Gain_Parameter_ID is mandatory.");
set_ValueNoCheck ("P_Gain_Parameter_ID", new Integer(P_Gain_Parameter_ID));
}
/** Get Gain Parameter.
@return Gain Parameter */
public int getP_Gain_Parameter_ID() 
{
Integer ii = (Integer)get_Value("P_Gain_Parameter_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
