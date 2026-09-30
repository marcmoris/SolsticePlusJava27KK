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
package org.compiere.model;

/** Generated Model - DO NOT CHANGE */
import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;
/** Generated Model for AD_View
 *  @author Jorg Janke (generated) 
 *  @version Release 2.6.1 - $Id: X_AD_View.java,v 1.1 2007/07/18 14:42:13 marmor01 Exp $ */
public class X_AD_View extends PO
{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/** Standard Constructor
@param ctx context
@param AD_View_ID id
@param trxName transaction
*/
public X_AD_View (Properties ctx, int AD_View_ID, String trxName)
{
super (ctx, AD_View_ID, trxName);
/** if (AD_View_ID == 0)
{
setAD_Table_ID (0);
setAD_View_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_AD_View (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=920 */
public static final int Table_ID=920;

/** TableName=AD_View */
public static final String Table_Name="AD_View";

protected static KeyNamePair Model = new KeyNamePair(920,"AD_View");

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
StringBuffer sb = new StringBuffer ("X_AD_View[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Table.
@param AD_Table_ID Database Table information */
public void setAD_Table_ID (int AD_Table_ID)
{
if (AD_Table_ID < 1) throw new IllegalArgumentException ("AD_Table_ID is mandatory.");
set_ValueNoCheck ("AD_Table_ID", new Integer(AD_Table_ID));
}
/** Get Table.
@return Database Table information */
public int getAD_Table_ID() 
{
Integer ii = (Integer)get_Value("AD_Table_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Get Record ID/ColumnName
@return ID/ColumnName pair */
public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), String.valueOf(getAD_Table_ID()));
}
/** Set View.
@param AD_View_ID Database View */
public void setAD_View_ID (int AD_View_ID)
{
if (AD_View_ID < 1) throw new IllegalArgumentException ("AD_View_ID is mandatory.");
set_ValueNoCheck ("AD_View_ID", new Integer(AD_View_ID));
}
/** Get View.
@return Database View */
public int getAD_View_ID() 
{
Integer ii = (Integer)get_Value("AD_View_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Code.
@param Code Code to execute or to validate */
public void setCode (String Code)
{
set_Value ("Code", Code);
}
/** Get Code.
@return Code to execute or to validate */
public String getCode() 
{
return (String)get_Value("Code");
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
/** Set Comment/Help.
@param Help Comment or Hint */
public void setHelp (String Help)
{
if (Help != null && Help.length() > 2000)
{
log.warning("Length > 2000 - truncated");
Help = Help.substring(0,1999);
}
set_Value ("Help", Help);
}
/** Get Comment/Help.
@return Comment or Hint */
public String getHelp() 
{
return (String)get_Value("Help");
}
}
