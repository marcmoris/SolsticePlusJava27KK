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
/** Generated Model for P_Employee_Contact
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Employee_Contact extends PO
{
/** Standard Constructor
@param ctx context
@param P_Employee_Contact_ID id
@param trxName transaction
*/
public X_P_Employee_Contact (Properties ctx, int P_Employee_Contact_ID, String trxName)
{
super (ctx, P_Employee_Contact_ID, trxName);
/** if (P_Employee_Contact_ID == 0)
{
setP_Employee_Contact_ID (0);
setP_Employee_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Employee_Contact (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100785 */
public static final int Table_ID=2100785;

/** TableName=P_Employee_Contact */
public static final String Table_Name="P_Employee_Contact";

protected static KeyNamePair Model = new KeyNamePair(2100785,"P_Employee_Contact");

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
StringBuffer sb = new StringBuffer ("X_P_Employee_Contact[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Description.
@param Description Optional short description of the record */
public void setDescription (String Description)
{
if (Description != null && Description.length() > 50)
{
log.warning("Length > 50 - truncated");
Description = Description.substring(0,49);
}
set_Value ("Description", Description);
}
/** Get Description.
@return Optional short description of the record */
public String getDescription() 
{
return (String)get_Value("Description");
}
/** Set Name .
@param Name Alphanumeric identifier of the entity */
public void setName (String Name)
{
if (Name != null && Name.length() > 50)
{
log.warning("Length > 50 - truncated");
Name = Name.substring(0,49);
}
set_Value ("Name", Name);
}
/** Get Name .
@return Alphanumeric identifier of the entity */
public String getName() 
{
return (String)get_Value("Name");
}
/** Get Record ID/ColumnName
@return ID/ColumnName pair */
public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), getName());
}
/** Set P_Employee_Contact_ID.
@param P_Employee_Contact_ID P_Employee_Contact_ID */
public void setP_Employee_Contact_ID (int P_Employee_Contact_ID)
{
if (P_Employee_Contact_ID < 1) throw new IllegalArgumentException ("P_Employee_Contact_ID is mandatory.");
set_ValueNoCheck ("P_Employee_Contact_ID", new Integer(P_Employee_Contact_ID));
}
/** Get P_Employee_Contact_ID.
@return P_Employee_Contact_ID */
public int getP_Employee_Contact_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Contact_ID");
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
/** Set Phone.
@param Phone Identifies a telephone number */
public void setPhone (String Phone)
{
if (Phone != null && Phone.length() > 50)
{
log.warning("Length > 50 - truncated");
Phone = Phone.substring(0,49);
}
set_Value ("Phone", Phone);
}
/** Get Phone.
@return Identifies a telephone number */
public String getPhone() 
{
return (String)get_Value("Phone");
}
}
