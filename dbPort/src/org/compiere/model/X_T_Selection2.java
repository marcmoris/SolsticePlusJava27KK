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
/** Generated Model for T_Selection2
 *  @author Jorg Janke (generated) 
 *  @version Release 2.6.1 - $Id: X_T_Selection2.java,v 1.1 2007/07/18 14:42:17 marmor01 Exp $ */
public class X_T_Selection2 extends PO
{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/** Standard Constructor
@param ctx context
@param T_Selection2_ID id
@param trxName transaction
*/
public X_T_Selection2 (Properties ctx, int T_Selection2_ID, String trxName)
{
super (ctx, T_Selection2_ID, trxName);
/** if (T_Selection2_ID == 0)
{
setQuery_ID (0);
setT_Selection_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_T_Selection2 (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=918 */
public static final int Table_ID=918;

/** TableName=T_Selection2 */
public static final String Table_Name="T_Selection2";

protected static KeyNamePair Model = new KeyNamePair(918,"T_Selection2");

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
StringBuffer sb = new StringBuffer ("X_T_Selection2[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Query.
@param Query_ID Query */
public void setQuery_ID (int Query_ID)
{
if (Query_ID < 1) throw new IllegalArgumentException ("Query_ID is mandatory.");
set_Value ("Query_ID", new Integer(Query_ID));
}
/** Get Query.
@return Query */
public int getQuery_ID() 
{
Integer ii = (Integer)get_Value("Query_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Selection.
@param T_Selection_ID *** DO NOT USE *** */
public void setT_Selection_ID (int T_Selection_ID)
{
if (T_Selection_ID < 1) throw new IllegalArgumentException ("T_Selection_ID is mandatory.");
set_Value ("T_Selection_ID", new Integer(T_Selection_ID));
}
/** Get Selection.
@return *** DO NOT USE *** */
public int getT_Selection_ID() 
{
Integer ii = (Integer)get_Value("T_Selection_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
