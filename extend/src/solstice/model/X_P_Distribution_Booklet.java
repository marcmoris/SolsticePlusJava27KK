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
/** Generated Model for P_Distribution_Booklet
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Distribution_Booklet extends PO
{
/** Standard Constructor
@param ctx context
@param P_Distribution_Booklet_ID id
@param trxName transaction
*/
public X_P_Distribution_Booklet (Properties ctx, int P_Distribution_Booklet_ID, String trxName)
{
super (ctx, P_Distribution_Booklet_ID, trxName);
/** if (P_Distribution_Booklet_ID == 0)
{
setP_Distribution_Booklet_ID (0);
setValue (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Distribution_Booklet (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100749 */
public static final int Table_ID=2100749;

/** TableName=P_Distribution_Booklet */
public static final String Table_Name="P_Distribution_Booklet";

protected static KeyNamePair Model = new KeyNamePair(2100749,"P_Distribution_Booklet");

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
StringBuffer sb = new StringBuffer ("X_P_Distribution_Booklet[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Use WEB Booklet.
@param IsUsingWEB Indicate if this distribution uses the WEB Booklet */
public void setIsUsingWEB (boolean IsUsingWEB)
{
set_Value ("IsUsingWEB", new Boolean(IsUsingWEB));
}
/** Get Use WEB Booklet.
@return Indicate if this distribution uses the WEB Booklet */
public boolean isUsingWEB() 
{
Object oo = get_Value("IsUsingWEB");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Name .
@param Name Alphanumeric identifier of the entity */
public void setName (String Name)
{
if (Name != null && Name.length() > 255)
{
log.warning("Length > 255 - truncated");
Name = Name.substring(0,254);
}
set_Value ("Name", Name);
}
/** Get Name .
@return Alphanumeric identifier of the entity */
public String getName() 
{
return (String)get_Value("Name");
}
/** Set Booklet.
@param P_Distribution_Booklet_ID Booklet */
public void setP_Distribution_Booklet_ID (int P_Distribution_Booklet_ID)
{
if (P_Distribution_Booklet_ID < 1) throw new IllegalArgumentException ("P_Distribution_Booklet_ID is mandatory.");
set_ValueNoCheck ("P_Distribution_Booklet_ID", new Integer(P_Distribution_Booklet_ID));
}
/** Get Booklet.
@return Booklet */
public int getP_Distribution_Booklet_ID() 
{
Integer ii = (Integer)get_Value("P_Distribution_Booklet_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Coordinating.
@param P_Employee_Coordinating_ID Coordinating */
public void setP_Employee_Coordinating_ID (int P_Employee_Coordinating_ID)
{
if (P_Employee_Coordinating_ID <= 0) set_Value ("P_Employee_Coordinating_ID", null);
 else 
set_Value ("P_Employee_Coordinating_ID", new Integer(P_Employee_Coordinating_ID));
}
/** Get Coordinating.
@return Coordinating */
public int getP_Employee_Coordinating_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Coordinating_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Manager.
@param P_Employee_Gestionnaire_ID Manager */
public void setP_Employee_Gestionnaire_ID (int P_Employee_Gestionnaire_ID)
{
if (P_Employee_Gestionnaire_ID <= 0) set_Value ("P_Employee_Gestionnaire_ID", null);
 else 
set_Value ("P_Employee_Gestionnaire_ID", new Integer(P_Employee_Gestionnaire_ID));
}
/** Get Manager.
@return Manager */
public int getP_Employee_Gestionnaire_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Gestionnaire_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Employee .
@param P_Employee_ID Employee */
public void setP_Employee_ID (int P_Employee_ID)
{
if (P_Employee_ID <= 0) set_Value ("P_Employee_ID", null);
 else 
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
/** Set Secretary.
@param P_Employee_Secretary_ID Secretary */
public void setP_Employee_Secretary_ID (int P_Employee_Secretary_ID)
{
if (P_Employee_Secretary_ID <= 0) set_Value ("P_Employee_Secretary_ID", null);
 else 
set_Value ("P_Employee_Secretary_ID", new Integer(P_Employee_Secretary_ID));
}
/** Get Secretary.
@return Secretary */
public int getP_Employee_Secretary_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Secretary_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Process Now.
@param Processing Process Now */
public void setProcessing (boolean Processing)
{
set_Value ("Processing", new Boolean(Processing));
}
/** Get Process Now.
@return Process Now */
public boolean isProcessing() 
{
Object oo = get_Value("Processing");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Search Key.
@param Value Search key for the record in the format required - must be unique */
public void setValue (String Value)
{
if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
if (Value.length() > 30)
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
/** Get Record ID/ColumnName
@return ID/ColumnName pair */
public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), getValue());
}
}
