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
/** Generated Model for P_PrintFormat
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_PrintFormat extends PO
{
/** Standard Constructor
@param ctx context
@param P_PrintFormat_ID id
@param trxName transaction
*/
public X_P_PrintFormat (Properties ctx, int P_PrintFormat_ID, String trxName)
{
super (ctx, P_PrintFormat_ID, trxName);
/** if (P_PrintFormat_ID == 0)
{
setName (null);
setP_PrintFormat_ID (0);
setValue (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_PrintFormat (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100770 */
public static final int Table_ID=2100770;

/** TableName=P_PrintFormat */
public static final String Table_Name="P_PrintFormat";

protected static KeyNamePair Model = new KeyNamePair(2100770,"P_PrintFormat");

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
StringBuffer sb = new StringBuffer ("X_P_PrintFormat[").append(get_ID()).append("]");
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
/** Set IsErrorList.
@param IsErrorList IsErrorList */
public void setIsErrorList (boolean IsErrorList)
{
set_Value ("IsErrorList", new Boolean(IsErrorList));
}
/** Get IsErrorList.
@return IsErrorList */
public boolean isErrorList() 
{
Object oo = get_Value("IsErrorList");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set IsTimeSheetDetailed.
@param IsTimeSheetDetailed IsTimeSheetDetailed */
public void setIsTimeSheetDetailed (boolean IsTimeSheetDetailed)
{
set_Value ("IsTimeSheetDetailed", new Boolean(IsTimeSheetDetailed));
}
/** Get IsTimeSheetDetailed.
@return IsTimeSheetDetailed */
public boolean isTimeSheetDetailed() 
{
Object oo = get_Value("IsTimeSheetDetailed");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set IsTimeSheetMissed.
@param IsTimeSheetMissed IsTimeSheetMissed */
public void setIsTimeSheetMissed (boolean IsTimeSheetMissed)
{
set_Value ("IsTimeSheetMissed", new Boolean(IsTimeSheetMissed));
}
/** Get IsTimeSheetMissed.
@return IsTimeSheetMissed */
public boolean isTimeSheetMissed() 
{
Object oo = get_Value("IsTimeSheetMissed");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set IsUseTableCredits.
@param IsUseTableCredits IsUseTableCredits */
public void setIsUseTableCredits (boolean IsUseTableCredits)
{
set_Value ("IsUseTableCredits", new Boolean(IsUseTableCredits));
}
/** Get IsUseTableCredits.
@return IsUseTableCredits */
public boolean isUseTableCredits() 
{
Object oo = get_Value("IsUseTableCredits");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set IsUseTableGain.
@param IsUseTableGain IsUseTableGain */
public void setIsUseTableGain (boolean IsUseTableGain)
{
set_Value ("IsUseTableGain", new Boolean(IsUseTableGain));
}
/** Get IsUseTableGain.
@return IsUseTableGain */
public boolean isUseTableGain() 
{
Object oo = get_Value("IsUseTableGain");
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
/** Set P_PrintFormat_ID.
@param P_PrintFormat_ID P_PrintFormat_ID */
public void setP_PrintFormat_ID (int P_PrintFormat_ID)
{
if (P_PrintFormat_ID < 1) throw new IllegalArgumentException ("P_PrintFormat_ID is mandatory.");
set_ValueNoCheck ("P_PrintFormat_ID", new Integer(P_PrintFormat_ID));
}
/** Get P_PrintFormat_ID.
@return P_PrintFormat_ID */
public int getP_PrintFormat_ID() 
{
Integer ii = (Integer)get_Value("P_PrintFormat_ID");
if (ii == null) return 0;
return ii.intValue();
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
}
