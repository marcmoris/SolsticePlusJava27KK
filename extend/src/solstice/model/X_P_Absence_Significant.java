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
/** Generated Model for P_Absence_Significant
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Absence_Significant extends PO
{
/** Standard Constructor
@param ctx context
@param P_Absence_Significant_ID id
@param trxName transaction
*/
public X_P_Absence_Significant (Properties ctx, int P_Absence_Significant_ID, String trxName)
{
super (ctx, P_Absence_Significant_ID, trxName);
/** if (P_Absence_Significant_ID == 0)
{
setADATE (new Timestamp(System.currentTimeMillis()));
setDayTime (null);
setP_Absence_Significant_ID (0);
setP_Employee_ID (0);
setValue (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Absence_Significant (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100759 */
public static final int Table_ID=2100759;

/** TableName=P_Absence_Significant */
public static final String Table_Name="P_Absence_Significant";

protected static KeyNamePair Model = new KeyNamePair(2100759,"P_Absence_Significant");

protected BigDecimal accessLevel = new BigDecimal(4);
/** AccessLevel
@return 4 - System 
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
StringBuffer sb = new StringBuffer ("X_P_Absence_Significant[").append(get_ID()).append("]");
return sb.toString();
}
/** Set ADATE.
@param ADATE ADATE */
public void setADATE (Timestamp ADATE)
{
if (ADATE == null) throw new IllegalArgumentException ("ADATE is mandatory.");
set_Value ("ADATE", ADATE);
}
/** Get ADATE.
@return ADATE */
public Timestamp getADATE() 
{
return (Timestamp)get_Value("ADATE");
}
/** Set Begining_Hour.
@param Begining_Hour Begining_Hour */
public void setBegining_Hour (String Begining_Hour)
{
if (Begining_Hour != null && Begining_Hour.length() > 5)
{
log.warning("Length > 5 - truncated");
Begining_Hour = Begining_Hour.substring(0,4);
}
set_Value ("Begining_Hour", Begining_Hour);
}
/** Get Begining_Hour.
@return Begining_Hour */
public String getBegining_Hour() 
{
return (String)get_Value("Begining_Hour");
}

/** DayTime AD_Reference_ID=2100397 */
public static final int DAYTIME_AD_Reference_ID=2100397;
/** Avant-Midi = A */
public static final String DAYTIME_Avant_Midi = "A";
/** Après-Midi = P */
public static final String DAYTIME_Après_Midi = "P";
/** Journée = J */
public static final String DAYTIME_Journée = "J";
/** Autre = Z */
public static final String DAYTIME_Autre = "Z";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isDayTimeValid (String test)
{
return test.equals("A") || test.equals("P") || test.equals("J") || test.equals("Z");
}
/** Set DayTime.
@param DayTime DayTime */
public void setDayTime (String DayTime)
{
if (DayTime == null) throw new IllegalArgumentException ("DayTime is mandatory");
if (!isDayTimeValid(DayTime))
throw new IllegalArgumentException ("DayTime Invalid value - " + DayTime + " - Reference_ID=2100397 - A - P - J - Z");
if (DayTime.length() > 1)
{
log.warning("Length > 1 - truncated");
DayTime = DayTime.substring(0,0);
}
set_Value ("DayTime", DayTime);
}
/** Get DayTime.
@return DayTime */
public String getDayTime() 
{
return (String)get_Value("DayTime");
}
/** Set Description.
@param Description Optional short description of the record */
public void setDescription (String Description)
{
if (Description != null && Description.length() > 255)
{
log.warning("Length > 255 - truncated");
Description = Description.substring(0,254);
}
set_Value ("Description", Description);
}
/** Get Description.
@return Optional short description of the record */
public String getDescription() 
{
return (String)get_Value("Description");
}
/** Set Ending_Hour.
@param Ending_Hour Ending_Hour */
public void setEnding_Hour (String Ending_Hour)
{
if (Ending_Hour != null && Ending_Hour.length() > 5)
{
log.warning("Length > 5 - truncated");
Ending_Hour = Ending_Hour.substring(0,4);
}
set_Value ("Ending_Hour", Ending_Hour);
}
/** Get Ending_Hour.
@return Ending_Hour */
public String getEnding_Hour() 
{
return (String)get_Value("Ending_Hour");
}
/** Set P_Absence_Significant_ID.
@param P_Absence_Significant_ID P_Absence_Significant_ID */
public void setP_Absence_Significant_ID (int P_Absence_Significant_ID)
{
if (P_Absence_Significant_ID < 1) throw new IllegalArgumentException ("P_Absence_Significant_ID is mandatory.");
set_ValueNoCheck ("P_Absence_Significant_ID", new Integer(P_Absence_Significant_ID));
}
/** Get P_Absence_Significant_ID.
@return P_Absence_Significant_ID */
public int getP_Absence_Significant_ID() 
{
Integer ii = (Integer)get_Value("P_Absence_Significant_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Employee .
@param P_Employee_ID Employee */
public void setP_Employee_ID (int P_Employee_ID)
{
if (P_Employee_ID < 1) throw new IllegalArgumentException ("P_Employee_ID is mandatory.");
set_Value ("P_Employee_ID", new Integer(P_Employee_ID));
}
/** Get Employee .
@return Employee */
public int getP_Employee_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Search Key.
@param Value Search key for the record in the format required - must be unique */
public void setValue (String Value)
{
if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
if (Value.length() > 50)
{
log.warning("Length > 50 - truncated");
Value = Value.substring(0,49);
}
set_Value ("Value", Value);
}
/** Get Search Key.
@return Search key for the record in the format required - must be unique */
public String getValue() 
{
return (String)get_Value("Value");
}
}
