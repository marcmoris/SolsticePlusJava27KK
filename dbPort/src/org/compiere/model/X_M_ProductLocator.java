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
/** Generated Model for M_ProductLocator
 *  @author Jorg Janke (generated) 
 *  @version Release 2.6.1 - $Id: X_M_ProductLocator.java,v 1.1 2007/07/18 15:00:40 marmor01 Exp $ */
public class X_M_ProductLocator extends PO
{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/** Standard Constructor
@param ctx context
@param M_ProductLocator_ID id
@param trxName transaction
*/
public X_M_ProductLocator (Properties ctx, int M_ProductLocator_ID, String trxName)
{
super (ctx, M_ProductLocator_ID, trxName);
/** if (M_ProductLocator_ID == 0)
{
setM_Locator_ID (0);
setM_ProductLocator_ID (0);
setM_Product_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_M_ProductLocator (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=915 */
public static final int Table_ID=915;

/** TableName=M_ProductLocator */
public static final String Table_Name="M_ProductLocator";

protected static KeyNamePair Model = new KeyNamePair(915,"M_ProductLocator");

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
StringBuffer sb = new StringBuffer ("X_M_ProductLocator[").append(get_ID()).append("]");
return sb.toString();
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
/** Set Locator.
@param M_Locator_ID Warehouse Locator */
public void setM_Locator_ID (int M_Locator_ID)
{
if (M_Locator_ID < 1) throw new IllegalArgumentException ("M_Locator_ID is mandatory.");
set_Value ("M_Locator_ID", new Integer(M_Locator_ID));
}
/** Get Locator.
@return Warehouse Locator */
public int getM_Locator_ID() 
{
Integer ii = (Integer)get_Value("M_Locator_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Product Locator.
@param M_ProductLocator_ID The preferred Locators for a Product */
public void setM_ProductLocator_ID (int M_ProductLocator_ID)
{
if (M_ProductLocator_ID < 1) throw new IllegalArgumentException ("M_ProductLocator_ID is mandatory.");
set_ValueNoCheck ("M_ProductLocator_ID", new Integer(M_ProductLocator_ID));
}
/** Get Product Locator.
@return The preferred Locators for a Product */
public int getM_ProductLocator_ID() 
{
Integer ii = (Integer)get_Value("M_ProductLocator_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Product.
@param M_Product_ID Product, Service, Item */
public void setM_Product_ID (int M_Product_ID)
{
if (M_Product_ID < 1) throw new IllegalArgumentException ("M_Product_ID is mandatory.");
set_Value ("M_Product_ID", new Integer(M_Product_ID));
}
/** Get Product.
@return Product, Service, Item */
public int getM_Product_ID() 
{
Integer ii = (Integer)get_Value("M_Product_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Relative Priority.
@param PriorityNo Where inventory should be picked from first */
public void setPriorityNo (int PriorityNo)
{
throw new IllegalArgumentException ("PriorityNo is virtual column");
}
/** Get Relative Priority.
@return Where inventory should be picked from first */
public int getPriorityNo() 
{
Integer ii = (Integer)get_Value("PriorityNo");
if (ii == null) return 0;
return ii.intValue();
}
}
