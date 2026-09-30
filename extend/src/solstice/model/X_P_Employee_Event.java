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
/** Generated Model for P_Employee_Event
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Employee_Event extends PO
{
/** Standard Constructor
@param ctx context
@param P_Employee_Event_ID id
@param trxName transaction
*/
public X_P_Employee_Event (Properties ctx, int P_Employee_Event_ID, String trxName)
{
super (ctx, P_Employee_Event_ID, trxName);
/** if (P_Employee_Event_ID == 0)
{
setDate (new Timestamp(System.currentTimeMillis()));
setP_Employee_Event_ID (0);
setP_Employee_ID (0);
setP_EventType_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Employee_Event (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100711 */
public static final int Table_ID=2100711;

/** TableName=P_Employee_Event */
public static final String Table_Name="P_Employee_Event";

protected static KeyNamePair Model = new KeyNamePair(2100711,"P_Employee_Event");

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
StringBuffer sb = new StringBuffer ("X_P_Employee_Event[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Date.
@param Date Date of event */
public void setDate (Timestamp Date)
{
if (Date == null) throw new IllegalArgumentException ("Date is mandatory.");
set_Value ("Date", Date);
}
/** Get Date.
@return Date of event */
public Timestamp getDate() 
{
return (Timestamp)get_Value("Date");
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
/** Set Événement.
@param P_Employee_Event_ID Événement */
public void setP_Employee_Event_ID (int P_Employee_Event_ID)
{
if (P_Employee_Event_ID < 1) throw new IllegalArgumentException ("P_Employee_Event_ID is mandatory.");
set_ValueNoCheck ("P_Employee_Event_ID", new Integer(P_Employee_Event_ID));
}
/** Get Événement.
@return Événement */
public int getP_Employee_Event_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Event_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Employee_ID AD_Reference_ID=2000010 */
public static final int P_EMPLOYEE_ID_AD_Reference_ID=2000010;
/** Set Employee .
@param P_Employee_ID Employee */
public void setP_Employee_ID (int P_Employee_ID)
{
if (P_Employee_ID < 1) throw new IllegalArgumentException ("P_Employee_ID is mandatory.");
set_ValueNoCheck ("P_Employee_ID", new Integer(P_Employee_ID));
}
/** Get Employee .
@return Employee */
public int getP_Employee_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_EventType_ID AD_Reference_ID=2100370 */
public static final int P_EVENTTYPE_ID_AD_Reference_ID=2100370;
/** Set Event Type.
@param P_EventType_ID Event Type */
public void setP_EventType_ID (int P_EventType_ID)
{
if (P_EventType_ID < 1) throw new IllegalArgumentException ("P_EventType_ID is mandatory.");
set_ValueNoCheck ("P_EventType_ID", new Integer(P_EventType_ID));
}
/** Get Event Type.
@return Event Type */
public int getP_EventType_ID() 
{
Integer ii = (Integer)get_Value("P_EventType_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
