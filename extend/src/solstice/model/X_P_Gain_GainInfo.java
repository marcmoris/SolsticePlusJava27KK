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
/** Generated Model for P_Gain_GainInfo
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Gain_GainInfo extends PO
{
/** Standard Constructor
@param ctx context
@param P_Gain_GainInfo_ID id
@param trxName transaction
*/
public X_P_Gain_GainInfo (Properties ctx, int P_Gain_GainInfo_ID, String trxName)
{
super (ctx, P_Gain_GainInfo_ID, trxName);
/** if (P_Gain_GainInfo_ID == 0)
{
setP_Gain_GainInfo_ID (0);
setP_Gain_ID (0);
setP_GainInfo_ID (0);
setTo_Consider (false);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Gain_GainInfo (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100762 */
public static final int Table_ID=2100762;

/** TableName=P_Gain_GainInfo */
public static final String Table_Name="P_Gain_GainInfo";

protected static KeyNamePair Model = new KeyNamePair(2100762,"P_Gain_GainInfo");

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
StringBuffer sb = new StringBuffer ("X_P_Gain_GainInfo[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Gain Information.
@param P_Gain_GainInfo_ID Gain Information */
public void setP_Gain_GainInfo_ID (int P_Gain_GainInfo_ID)
{
if (P_Gain_GainInfo_ID < 1) throw new IllegalArgumentException ("P_Gain_GainInfo_ID is mandatory.");
set_ValueNoCheck ("P_Gain_GainInfo_ID", new Integer(P_Gain_GainInfo_ID));
}
/** Get Gain Information.
@return Gain Information */
public int getP_Gain_GainInfo_ID() 
{
Integer ii = (Integer)get_Value("P_Gain_GainInfo_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Gain_ID AD_Reference_ID=2100317 */
public static final int P_GAIN_ID_AD_Reference_ID=2100317;
/** Set Gain.
@param P_Gain_ID Gain */
public void setP_Gain_ID (int P_Gain_ID)
{
if (P_Gain_ID < 1) throw new IllegalArgumentException ("P_Gain_ID is mandatory.");
set_ValueNoCheck ("P_Gain_ID", new Integer(P_Gain_ID));
}
/** Get Gain.
@return Gain */
public int getP_Gain_ID() 
{
Integer ii = (Integer)get_Value("P_Gain_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_GainInfo_ID AD_Reference_ID=2100398 */
public static final int P_GAININFO_ID_AD_Reference_ID=2100398;
/** Set Gain Information.
@param P_GainInfo_ID Gain Information */
public void setP_GainInfo_ID (int P_GainInfo_ID)
{
if (P_GainInfo_ID < 1) throw new IllegalArgumentException ("P_GainInfo_ID is mandatory.");
set_ValueNoCheck ("P_GainInfo_ID", new Integer(P_GainInfo_ID));
}
/** Get Gain Information.
@return Gain Information */
public int getP_GainInfo_ID() 
{
Integer ii = (Integer)get_Value("P_GainInfo_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set To Consider.
@param To_Consider To Consider */
public void setTo_Consider (boolean To_Consider)
{
set_Value ("To_Consider", new Boolean(To_Consider));
}
/** Get To Consider.
@return To Consider */
public boolean isTo_Consider() 
{
Object oo = get_Value("To_Consider");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
}
