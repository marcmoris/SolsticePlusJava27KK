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
/** Generated Model for P_LongTermLeave_Credits
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_LongTermLeave_Credits extends PO
{
/** Standard Constructor
@param ctx context
@param P_LongTermLeave_Credits_ID id
@param trxName transaction
*/
public X_P_LongTermLeave_Credits (Properties ctx, int P_LongTermLeave_Credits_ID, String trxName)
{
super (ctx, P_LongTermLeave_Credits_ID, trxName);
/** if (P_LongTermLeave_Credits_ID == 0)
{
setP_Credits_ID (0);
setP_LongTermLeave_Credits_ID (0);
setP_LongTermLeave_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_LongTermLeave_Credits (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100693 */
public static final int Table_ID=2100693;

/** TableName=P_LongTermLeave_Credits */
public static final String Table_Name="P_LongTermLeave_Credits";

protected static KeyNamePair Model = new KeyNamePair(2100693,"P_LongTermLeave_Credits");

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
StringBuffer sb = new StringBuffer ("X_P_LongTermLeave_Credits[").append(get_ID()).append("]");
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

/** P_Credits_ID AD_Reference_ID=2100318 */
public static final int P_CREDITS_ID_AD_Reference_ID=2100318;
/** Set Credits.
@param P_Credits_ID Credits */
public void setP_Credits_ID (int P_Credits_ID)
{
if (P_Credits_ID < 1) throw new IllegalArgumentException ("P_Credits_ID is mandatory.");
set_Value ("P_Credits_ID", new Integer(P_Credits_ID));
}
/** Get Credits.
@return Credits */
public int getP_Credits_ID() 
{
Integer ii = (Integer)get_Value("P_Credits_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_LongTermLeave_Credits_ID AD_Reference_ID=113 */
public static final int P_LONGTERMLEAVE_CREDITS_ID_AD_Reference_ID=113;
/** Set Long Term Leave Credits.
@param P_LongTermLeave_Credits_ID Long Term Leave Credits */
public void setP_LongTermLeave_Credits_ID (int P_LongTermLeave_Credits_ID)
{
if (P_LongTermLeave_Credits_ID < 1) throw new IllegalArgumentException ("P_LongTermLeave_Credits_ID is mandatory.");
set_ValueNoCheck ("P_LongTermLeave_Credits_ID", new Integer(P_LongTermLeave_Credits_ID));
}
/** Get Long Term Leave Credits.
@return Long Term Leave Credits */
public int getP_LongTermLeave_Credits_ID() 
{
Integer ii = (Integer)get_Value("P_LongTermLeave_Credits_ID");
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
