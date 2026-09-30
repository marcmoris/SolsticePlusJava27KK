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
/** Generated Model for P_NonBusinessDay
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_NonBusinessDay extends PO
{
/** Standard Constructor
@param ctx context
@param P_NonBusinessDay_ID id
@param trxName transaction
*/
public X_P_NonBusinessDay (Properties ctx, int P_NonBusinessDay_ID, String trxName)
{
super (ctx, P_NonBusinessDay_ID, trxName);
/** if (P_NonBusinessDay_ID == 0)
{
setName (null);
setNonBusinessDate (new Timestamp(System.currentTimeMillis()));
setP_Gain_ID (0);
setP_Holidays_Year_ID (0);
setP_NonBusinessDay_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_NonBusinessDay (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000108 */
public static final int Table_ID=2000108;

/** TableName=P_NonBusinessDay */
public static final String Table_Name="P_NonBusinessDay";

protected static KeyNamePair Model = new KeyNamePair(2000108,"P_NonBusinessDay");

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
StringBuffer sb = new StringBuffer ("X_P_NonBusinessDay[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Name .
@param Name Alphanumeric identifier of the entity */
public void setName (String Name)
{
if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
if (Name.length() > 60)
{
log.warning("Length > 60 - truncated");
Name = Name.substring(0,59);
}
set_Value ("Name", Name);
}
/** Get Name .
@return Alphanumeric identifier of the entity */
public String getName() 
{
return (String)get_Value("Name");
}
/** Set Day.
@param NonBusinessDate Day */
public void setNonBusinessDate (Timestamp NonBusinessDate)
{
if (NonBusinessDate == null) throw new IllegalArgumentException ("NonBusinessDate is mandatory.");
set_Value ("NonBusinessDate", NonBusinessDate);
}
/** Get Day.
@return Day */
public Timestamp getNonBusinessDate() 
{
return (Timestamp)get_Value("NonBusinessDate");
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

/** P_Holidays_Calendar_ID AD_Reference_ID=2000063 */
public static final int P_HOLIDAYS_CALENDAR_ID_AD_Reference_ID=2000063;
/** Set Calendar.
@param P_Holidays_Calendar_ID Calendar */
public void setP_Holidays_Calendar_ID (int P_Holidays_Calendar_ID)
{
throw new IllegalArgumentException ("P_Holidays_Calendar_ID is virtual column");
}
/** Get Calendar.
@return Calendar */
public int getP_Holidays_Calendar_ID() 
{
Integer ii = (Integer)get_Value("P_Holidays_Calendar_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Year.
@param P_Holidays_Year_ID Year */
public void setP_Holidays_Year_ID (int P_Holidays_Year_ID)
{
if (P_Holidays_Year_ID < 1) throw new IllegalArgumentException ("P_Holidays_Year_ID is mandatory.");
set_Value ("P_Holidays_Year_ID", new Integer(P_Holidays_Year_ID));
}
/** Get Year.
@return Year */
public int getP_Holidays_Year_ID() 
{
Integer ii = (Integer)get_Value("P_Holidays_Year_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Non Business Day.
@param P_NonBusinessDay_ID Non Business Day */
public void setP_NonBusinessDay_ID (int P_NonBusinessDay_ID)
{
if (P_NonBusinessDay_ID < 1) throw new IllegalArgumentException ("P_NonBusinessDay_ID is mandatory.");
set_Value ("P_NonBusinessDay_ID", new Integer(P_NonBusinessDay_ID));
}
/** Get Non Business Day.
@return Non Business Day */
public int getP_NonBusinessDay_ID() 
{
Integer ii = (Integer)get_Value("P_NonBusinessDay_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
