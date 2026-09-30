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
/** Generated Model for P_LongTermLeave_Gain
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_LongTermLeave_Gain extends PO
{
/** Standard Constructor
@param ctx context
@param P_LongTermLeave_Gain_ID id
@param trxName transaction
*/
public X_P_LongTermLeave_Gain (Properties ctx, int P_LongTermLeave_Gain_ID, String trxName)
{
super (ctx, P_LongTermLeave_Gain_ID, trxName);
/** if (P_LongTermLeave_Gain_ID == 0)
{
setLongtermLeaveMth (null);
setP_Gain_ID (0);
setP_LongTermLeave_Gain_ID (0);
setP_LongTermLeave_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_LongTermLeave_Gain (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100691 */
public static final int Table_ID=2100691;

/** TableName=P_LongTermLeave_Gain */
public static final String Table_Name="P_LongTermLeave_Gain";

protected static KeyNamePair Model = new KeyNamePair(2100691,"P_LongTermLeave_Gain");

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
StringBuffer sb = new StringBuffer ("X_P_LongTermLeave_Gain[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Day.
@param Day Day */
public void setDay (int Day)
{
set_Value ("Day", new Integer(Day));
}
/** Get Day.
@return Day */
public int getDay() 
{
Integer ii = (Integer)get_Value("Day");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Method.
@param LongtermLeaveMth Method */
public void setLongtermLeaveMth (String LongtermLeaveMth)
{
if (LongtermLeaveMth == null) throw new IllegalArgumentException ("LongtermLeaveMth is mandatory.");
if (LongtermLeaveMth.length() > 2)
{
log.warning("Length > 2 - truncated");
LongtermLeaveMth = LongtermLeaveMth.substring(0,1);
}
set_Value ("LongtermLeaveMth", LongtermLeaveMth);
}
/** Get Method.
@return Method */
public String getLongtermLeaveMth() 
{
return (String)get_Value("LongtermLeaveMth");
}
/** Set Month.
@param Month Month */
public void setMonth (int Month)
{
set_Value ("Month", new Integer(Month));
}
/** Get Month.
@return Month */
public int getMonth() 
{
Integer ii = (Integer)get_Value("Month");
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
/** Set Long Term Leave Profile Gain.
@param P_LongTermLeave_Gain_ID Long Term Leave Profile Gain */
public void setP_LongTermLeave_Gain_ID (int P_LongTermLeave_Gain_ID)
{
if (P_LongTermLeave_Gain_ID < 1) throw new IllegalArgumentException ("P_LongTermLeave_Gain_ID is mandatory.");
set_ValueNoCheck ("P_LongTermLeave_Gain_ID", new Integer(P_LongTermLeave_Gain_ID));
}
/** Get Long Term Leave Profile Gain.
@return Long Term Leave Profile Gain */
public int getP_LongTermLeave_Gain_ID() 
{
Integer ii = (Integer)get_Value("P_LongTermLeave_Gain_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Activity Status.
@param P_LongTermLeave_ID Activity Status */
public void setP_LongTermLeave_ID (int P_LongTermLeave_ID)
{
if (P_LongTermLeave_ID < 1) throw new IllegalArgumentException ("P_LongTermLeave_ID is mandatory.");
set_ValueNoCheck ("P_LongTermLeave_ID", new Integer(P_LongTermLeave_ID));
}
/** Get Activity Status.
@return Activity Status */
public int getP_LongTermLeave_ID() 
{
Integer ii = (Integer)get_Value("P_LongTermLeave_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
