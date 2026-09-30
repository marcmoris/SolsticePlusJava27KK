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
/** Generated Model for P_Credits_Mth_Evaluation
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Credits_Mth_Evaluation extends PO
{
/** Standard Constructor
@param ctx context
@param P_Credits_Mth_Evaluation_ID id
@param trxName transaction
*/
public X_P_Credits_Mth_Evaluation (Properties ctx, int P_Credits_Mth_Evaluation_ID, String trxName)
{
super (ctx, P_Credits_Mth_Evaluation_ID, trxName);
/** if (P_Credits_Mth_Evaluation_ID == 0)
{
setName (null);
setP_Credits_Mth_Evaluation_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Credits_Mth_Evaluation (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000062 */
public static final int Table_ID=2000062;

/** TableName=P_Credits_Mth_Evaluation */
public static final String Table_Name="P_Credits_Mth_Evaluation";

protected static KeyNamePair Model = new KeyNamePair(2000062,"P_Credits_Mth_Evaluation");

protected BigDecimal accessLevel = new BigDecimal(7);
/** AccessLevel
@return 7 - System - Client - Org 
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
StringBuffer sb = new StringBuffer ("X_P_Credits_Mth_Evaluation[").append(get_ID()).append("]");
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
/** Get Record ID/ColumnName
@return ID/ColumnName pair */
public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), getName());
}
/** Set Evaluation Method.
@param P_Credits_Mth_Evaluation_ID Evaluation Method */
public void setP_Credits_Mth_Evaluation_ID (int P_Credits_Mth_Evaluation_ID)
{
if (P_Credits_Mth_Evaluation_ID < 1) throw new IllegalArgumentException ("P_Credits_Mth_Evaluation_ID is mandatory.");
set_Value ("P_Credits_Mth_Evaluation_ID", new Integer(P_Credits_Mth_Evaluation_ID));
}
/** Get Evaluation Method.
@return Evaluation Method */
public int getP_Credits_Mth_Evaluation_ID() 
{
Integer ii = (Integer)get_Value("P_Credits_Mth_Evaluation_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Search Key.
@param Value Search key for the record in the format required - must be unique */
public void setValue (String Value)
{
if (Value != null && Value.length() > 30)
{
log.warning("Length > 30 - truncated");
Value = Value.substring(0,29);
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
