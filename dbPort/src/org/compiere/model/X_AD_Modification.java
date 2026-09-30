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
/** Generated Model for AD_Modification
 *  @author Jorg Janke (generated) 
 *  @version Release 2.6.1 - $Id: X_AD_Modification.java,v 1.1 2007/07/18 14:42:43 marmor01 Exp $ */
public class X_AD_Modification extends PO
{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/** Standard Constructor
@param ctx context
@param AD_Modification_ID id
@param trxName transaction
*/
public X_AD_Modification (Properties ctx, int AD_Modification_ID, String trxName)
{
super (ctx, AD_Modification_ID, trxName);
/** if (AD_Modification_ID == 0)
{
setAD_Modification_ID (0);
setAD_Version_ID (0);
setName (null);
setSeqNo (0);	// @SQL=SELECT COALESCE(MAX(SeqNo),0)+10 AS DefaultValue FROM AD_Modification WHERE EntityType='@EntityType@'
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_AD_Modification (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=883 */
public static final int Table_ID=883;

/** TableName=AD_Modification */
public static final String Table_Name="AD_Modification";

protected static KeyNamePair Model = new KeyNamePair(883,"AD_Modification");

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
StringBuffer sb = new StringBuffer ("X_AD_Modification[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Modification.
@param AD_Modification_ID System Modification or Extension */
public void setAD_Modification_ID (int AD_Modification_ID)
{
if (AD_Modification_ID < 1) throw new IllegalArgumentException ("AD_Modification_ID is mandatory.");
set_ValueNoCheck ("AD_Modification_ID", new Integer(AD_Modification_ID));
}
/** Get Modification.
@return System Modification or Extension */
public int getAD_Modification_ID() 
{
Integer ii = (Integer)get_Value("AD_Modification_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Entity Version.
@param AD_Version_ID Entity Version */
public void setAD_Version_ID (int AD_Version_ID)
{
if (AD_Version_ID < 1) throw new IllegalArgumentException ("AD_Version_ID is mandatory.");
set_ValueNoCheck ("AD_Version_ID", new Integer(AD_Version_ID));
}
/** Get Entity Version.
@return Entity Version */
public int getAD_Version_ID() 
{
Integer ii = (Integer)get_Value("AD_Version_ID");
if (ii == null) return 0;
return ii.intValue();
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
/** Set Name.
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
/** Get Name.
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
/** Set Sequence.
@param SeqNo Method of ordering records;
 lowest number comes first */
public void setSeqNo (int SeqNo)
{
set_Value ("SeqNo", new Integer(SeqNo));
}
/** Get Sequence.
@return Method of ordering records;
 lowest number comes first */
public int getSeqNo() 
{
Integer ii = (Integer)get_Value("SeqNo");
if (ii == null) return 0;
return ii.intValue();
}
}
