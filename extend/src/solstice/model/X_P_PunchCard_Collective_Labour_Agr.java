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
/** Generated Model for P_PunchCard_Collective_Labour_Agr
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_PunchCard_Collective_Labour_Agr extends PO
{
/** Standard Constructor
@param ctx context
@param P_PunchCard_Collective_Labour_Agr_ID id
@param trxName transaction
*/
public X_P_PunchCard_Collective_Labour_Agr (Properties ctx, int P_PunchCard_Collective_Labour_Agr_ID, String trxName)
{
super (ctx, P_PunchCard_Collective_Labour_Agr_ID, trxName);
/** if (P_PunchCard_Collective_Labour_Agr_ID == 0)
{
setP_Collective_Labour_Agr_ID (0);
setP_PunchCard_Collective_Labour_Agr_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_PunchCard_Collective_Labour_Agr (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100831 */
public static final int Table_ID=2100831;

/** TableName=P_PunchCard_Collective_Labour_Agr */
public static final String Table_Name="P_PunchCard_Collective_Labour_Agr";

protected static KeyNamePair Model = new KeyNamePair(2100831,"P_PunchCard_Collective_Labour_Agr");

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
StringBuffer sb = new StringBuffer ("X_P_PunchCard_Collective_Labour_Agr[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Collective Labour Agreement.
@param P_Collective_Labour_Agr_ID Collective Labour Agreement */
public void setP_Collective_Labour_Agr_ID (int P_Collective_Labour_Agr_ID)
{
if (P_Collective_Labour_Agr_ID < 1) throw new IllegalArgumentException ("P_Collective_Labour_Agr_ID is mandatory.");
set_Value ("P_Collective_Labour_Agr_ID", new Integer(P_Collective_Labour_Agr_ID));
}
/** Get Collective Labour Agreement.
@return Collective Labour Agreement */
public int getP_Collective_Labour_Agr_ID() 
{
Integer ii = (Integer)get_Value("P_Collective_Labour_Agr_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_PunchCard_Collective_Labour_Agr_ID.
@param P_PunchCard_Collective_Labour_Agr_ID P_PunchCard_Collective_Labour_Agr_ID */
public void setP_PunchCard_Collective_Labour_Agr_ID (int P_PunchCard_Collective_Labour_Agr_ID)
{
if (P_PunchCard_Collective_Labour_Agr_ID < 1) throw new IllegalArgumentException ("P_PunchCard_Collective_Labour_Agr_ID is mandatory.");
set_ValueNoCheck ("P_PunchCard_Collective_Labour_Agr_ID", new Integer(P_PunchCard_Collective_Labour_Agr_ID));
}
/** Get P_PunchCard_Collective_Labour_Agr_ID.
@return P_PunchCard_Collective_Labour_Agr_ID */
public int getP_PunchCard_Collective_Labour_Agr_ID() 
{
Integer ii = (Integer)get_Value("P_PunchCard_Collective_Labour_Agr_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
