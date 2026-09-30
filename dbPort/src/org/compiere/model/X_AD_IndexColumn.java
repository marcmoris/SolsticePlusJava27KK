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
/** Generated Model for AD_IndexColumn
 *  @author Jorg Janke (generated) 
 *  @version Release 2.6.1 - $Id: X_AD_IndexColumn.java,v 1.1 2007/07/18 14:42:25 marmor01 Exp $ */
public class X_AD_IndexColumn extends PO
{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/** Standard Constructor
@param ctx context
@param AD_IndexColumn_ID id
@param trxName transaction
*/
public X_AD_IndexColumn (Properties ctx, int AD_IndexColumn_ID, String trxName)
{
super (ctx, AD_IndexColumn_ID, trxName);
/** if (AD_IndexColumn_ID == 0)
{
setAD_Column_ID (0);
setAD_IndexColumn_ID (0);
setAD_TableIndex_ID (0);
setSeqNo (0);	// @SQL=SELECT COALESCE(MAX(SeqNo),0)+10 AS DefaultValue FROM AD_IndexColumn WHERE AD_TableIndex_ID=@AD_TableIndex_ID@
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_AD_IndexColumn (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=910 */
public static final int Table_ID=910;

/** TableName=AD_IndexColumn */
public static final String Table_Name="AD_IndexColumn";

protected static KeyNamePair Model = new KeyNamePair(910,"AD_IndexColumn");

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
StringBuffer sb = new StringBuffer ("X_AD_IndexColumn[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Column.
@param AD_Column_ID Column in the table */
public void setAD_Column_ID (int AD_Column_ID)
{
if (AD_Column_ID < 1) throw new IllegalArgumentException ("AD_Column_ID is mandatory.");
set_Value ("AD_Column_ID", new Integer(AD_Column_ID));
}
/** Get Column.
@return Column in the table */
public int getAD_Column_ID() 
{
Integer ii = (Integer)get_Value("AD_Column_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Index Column.
@param AD_IndexColumn_ID Index Column */
public void setAD_IndexColumn_ID (int AD_IndexColumn_ID)
{
if (AD_IndexColumn_ID < 1) throw new IllegalArgumentException ("AD_IndexColumn_ID is mandatory.");
set_ValueNoCheck ("AD_IndexColumn_ID", new Integer(AD_IndexColumn_ID));
}
/** Get Index Column.
@return Index Column */
public int getAD_IndexColumn_ID() 
{
Integer ii = (Integer)get_Value("AD_IndexColumn_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Table Index.
@param AD_TableIndex_ID Table Index */
public void setAD_TableIndex_ID (int AD_TableIndex_ID)
{
if (AD_TableIndex_ID < 1) throw new IllegalArgumentException ("AD_TableIndex_ID is mandatory.");
set_ValueNoCheck ("AD_TableIndex_ID", new Integer(AD_TableIndex_ID));
}
/** Get Table Index.
@return Table Index */
public int getAD_TableIndex_ID() 
{
Integer ii = (Integer)get_Value("AD_TableIndex_ID");
if (ii == null) return 0;
return ii.intValue();
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
