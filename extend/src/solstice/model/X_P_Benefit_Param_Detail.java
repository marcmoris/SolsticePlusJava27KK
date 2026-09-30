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
/** Generated Model for P_Benefit_Param_Detail
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Benefit_Param_Detail extends PO
{
/** Standard Constructor
@param ctx context
@param P_Benefit_Param_Detail_ID id
@param trxName transaction
*/
public X_P_Benefit_Param_Detail (Properties ctx, int P_Benefit_Param_Detail_ID, String trxName)
{
super (ctx, P_Benefit_Param_Detail_ID, trxName);
/** if (P_Benefit_Param_Detail_ID == 0)
{
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Benefit_Param_Detail (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100875 */
public static final int Table_ID=2100875;

/** TableName=P_Benefit_Param_Detail */
public static final String Table_Name="P_Benefit_Param_Detail";

protected static KeyNamePair Model = new KeyNamePair(2100875,"P_Benefit_Param_Detail");

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
StringBuffer sb = new StringBuffer ("X_P_Benefit_Param_Detail[").append(get_ID()).append("]");
return sb.toString();
}
/** Set P_BENEFIT_PARAM_DETAIL_ID.
@param P_BENEFIT_PARAM_DETAIL_ID P_BENEFIT_PARAM_DETAIL_ID */
public void setP_BENEFIT_PARAM_DETAIL_ID (int P_BENEFIT_PARAM_DETAIL_ID)
{
if (P_BENEFIT_PARAM_DETAIL_ID <= 0) set_ValueNoCheck ("P_BENEFIT_PARAM_DETAIL_ID", null);
 else 
set_ValueNoCheck ("P_BENEFIT_PARAM_DETAIL_ID", new Integer(P_BENEFIT_PARAM_DETAIL_ID));
}
/** Get P_BENEFIT_PARAM_DETAIL_ID.
@return P_BENEFIT_PARAM_DETAIL_ID */
public int getP_BENEFIT_PARAM_DETAIL_ID() 
{
Integer ii = (Integer)get_Value("P_BENEFIT_PARAM_DETAIL_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_BENEFIT_PARAM_ID.
@param P_BENEFIT_PARAM_ID P_BENEFIT_PARAM_ID */
public void setP_BENEFIT_PARAM_ID (int P_BENEFIT_PARAM_ID)
{
if (P_BENEFIT_PARAM_ID <= 0) set_Value ("P_BENEFIT_PARAM_ID", null);
 else 
set_Value ("P_BENEFIT_PARAM_ID", new Integer(P_BENEFIT_PARAM_ID));
}
/** Get P_BENEFIT_PARAM_ID.
@return P_BENEFIT_PARAM_ID */
public int getP_BENEFIT_PARAM_ID() 
{
Integer ii = (Integer)get_Value("P_BENEFIT_PARAM_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Gain.
@param P_Gain_ID Gain */
public void setP_Gain_ID (int P_Gain_ID)
{
if (P_Gain_ID <= 0) set_Value ("P_Gain_ID", null);
 else 
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
}
