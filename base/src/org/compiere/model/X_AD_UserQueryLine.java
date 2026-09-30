/******************************************************************************
* Product: Solstice+ Payroll & Human Resources Management                    *
* Copyright (C) 2008 ProGestion Informatique, Inc. All Rights Reserved.      *
* This program is free software, you can redistribute it and/or modify it    *
* under the terms version 2 of the GNU General Public License as published   *
* by the Free Software Foundation. This program is distributed in the hope   *
* that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
* warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
* See the GNU General Public License for more details.                       *
* You should have received a copy of the GNU General Public License along    *
* with this program, if not, write to the Free Software Foundation, Inc.,    *
* 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
* For the text or an alternative of this public license, you may reach us    *
* ProGestion Informatique, 210-5300 Bld des Galerie, Quebec, G2K 2A2 Canada  *
* or via info@progestion.net or http://www.progestion.net/license.html       *
******************************************************************************/
package org.compiere.model;

/** Generated Model - DO NOT CHANGE */
import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;
/** Generated Model for AD_UserQueryLine
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_AD_UserQueryLine extends PO
{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/** Standard Constructor
@param ctx context
@param AD_UserQueryLine_ID id
@param trxName transaction
*/
public X_AD_UserQueryLine (Properties ctx, int AD_UserQueryLine_ID, String trxName)
{
super (ctx, AD_UserQueryLine_ID, trxName);
/** if (AD_UserQueryLine_ID == 0)
{
setAD_Tab_ID (0);
setAD_Table_ID (0);
setAD_UserQuery_ID (0);
setAD_UserQueryLine_ID (0);
setKeyName (null);
setKeyValue (null);
setName (null);
setOperator (null);
setValue1Name (null);
setValue1Value (null);
setValue2Name (null);
setValue2Value (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_AD_UserQueryLine (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=3201211 */
public static final int Table_ID=3201211;

/** TableName=AD_UserQueryLine */
public static final String Table_Name="AD_UserQueryLine";

protected static KeyNamePair Model = new KeyNamePair(3201211,"AD_UserQueryLine");

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
StringBuffer sb = new StringBuffer ("X_AD_UserQueryLine[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Tab.
@param AD_Tab_ID Tab within a Window */
public void setAD_Tab_ID (int AD_Tab_ID)
{
if (AD_Tab_ID < 1) throw new IllegalArgumentException ("AD_Tab_ID is mandatory.");
set_Value ("AD_Tab_ID", new Integer(AD_Tab_ID));
}
/** Get Tab.
@return Tab within a Window */
public int getAD_Tab_ID() 
{
Integer ii = (Integer)get_Value("AD_Tab_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Table.
@param AD_Table_ID Database Table information */
public void setAD_Table_ID (int AD_Table_ID)
{
if (AD_Table_ID < 1) throw new IllegalArgumentException ("AD_Table_ID is mandatory.");
set_Value ("AD_Table_ID", new Integer(AD_Table_ID));
}
/** Get Table.
@return Database Table information */
public int getAD_Table_ID() 
{
Integer ii = (Integer)get_Value("AD_Table_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set User/Contact.
@param AD_User_ID User within the system - Internal or Business Partner Contact */
public void setAD_User_ID (int AD_User_ID)
{
if (AD_User_ID <= 0) set_Value ("AD_User_ID", null);
 else 
set_Value ("AD_User_ID", new Integer(AD_User_ID));
}
/** Get User/Contact.
@return User within the system - Internal or Business Partner Contact */
public int getAD_User_ID() 
{
Integer ii = (Integer)get_Value("AD_User_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set User Query.
@param AD_UserQuery_ID Saved User Query */
public void setAD_UserQuery_ID (int AD_UserQuery_ID)
{
if (AD_UserQuery_ID < 1) throw new IllegalArgumentException ("AD_UserQuery_ID is mandatory.");
set_Value ("AD_UserQuery_ID", new Integer(AD_UserQuery_ID));
}
/** Get User Query.
@return Saved User Query */
public int getAD_UserQuery_ID() 
{
Integer ii = (Integer)get_Value("AD_UserQuery_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set AD_UserQueryLine_ID.
@param AD_UserQueryLine_ID AD_UserQueryLine_ID */
public void setAD_UserQueryLine_ID (int AD_UserQueryLine_ID)
{
if (AD_UserQueryLine_ID < 1) throw new IllegalArgumentException ("AD_UserQueryLine_ID is mandatory.");
set_ValueNoCheck ("AD_UserQueryLine_ID", new Integer(AD_UserQueryLine_ID));
}
/** Get AD_UserQueryLine_ID.
@return AD_UserQueryLine_ID */
public int getAD_UserQueryLine_ID() 
{
Integer ii = (Integer)get_Value("AD_UserQueryLine_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Code.
@param Code Code to execute or to validate */
public void setCode (String Code)
{
if (Code != null && Code.length() > 2000)
{
log.warning("Length > 2000 - truncated");
Code = Code.substring(0,1999);
}
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
/** Set IsAnd.
@param IsAnd IsAnd */
public void setIsAnd (boolean IsAnd)
{
set_Value ("IsAnd", new Boolean(IsAnd));
}
/** Get IsAnd.
@return IsAnd */
public boolean isAnd() 
{
Object oo = get_Value("IsAnd");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set KeyName.
@param KeyName KeyName */
public void setKeyName (String KeyName)
{
if (KeyName == null) throw new IllegalArgumentException ("KeyName is mandatory.");
if (KeyName.length() > 60)
{
log.warning("Length > 60 - truncated");
KeyName = KeyName.substring(0,59);
}
set_Value ("KeyName", KeyName);
}
/** Get KeyName.
@return KeyName */
public String getKeyName() 
{
return (String)get_Value("KeyName");
}
/** Set KeyValue.
@param KeyValue KeyValue */
public void setKeyValue (String KeyValue)
{
if (KeyValue == null) throw new IllegalArgumentException ("KeyValue is mandatory.");
if (KeyValue.length() > 255)
{
log.warning("Length > 255 - truncated");
KeyValue = KeyValue.substring(0,254);
}
set_Value ("KeyValue", KeyValue);
}
/** Get KeyValue.
@return KeyValue */
public String getKeyValue() 
{
return (String)get_Value("KeyValue");
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
/** Set Operator.
@param Operator Operator */
public void setOperator (String Operator)
{
if (Operator == null) throw new IllegalArgumentException ("Operator is mandatory.");
if (Operator.length() > 20)
{
log.warning("Length > 20 - truncated");
Operator = Operator.substring(0,19);
}
set_Value ("Operator", Operator);
}
/** Get Operator.
@return Operator */
public String getOperator() 
{
return (String)get_Value("Operator");
}
/** Set Sequence.
@param SeqNo Method of ordering elements;
 lowest NUMERIC comes first */
public void setSeqNo (int SeqNo)
{
set_Value ("SeqNo", new Integer(SeqNo));
}
/** Get Sequence.
@return Method of ordering elements;
 lowest NUMERIC comes first */
public int getSeqNo() 
{
Integer ii = (Integer)get_Value("SeqNo");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Value1Name.
@param Value1Name Value1Name */
public void setValue1Name (String Value1Name)
{
if (Value1Name == null) throw new IllegalArgumentException ("Value1Name is mandatory.");
if (Value1Name.length() > 255)
{
log.warning("Length > 255 - truncated");
Value1Name = Value1Name.substring(0,254);
}
set_Value ("Value1Name", Value1Name);
}
/** Get Value1Name.
@return Value1Name */
public String getValue1Name() 
{
return (String)get_Value("Value1Name");
}
/** Set Value1Value.
@param Value1Value Value1Value */
public void setValue1Value (String Value1Value)
{
if (Value1Value == null) throw new IllegalArgumentException ("Value1Value is mandatory.");
if (Value1Value.length() > 60)
{
log.warning("Length > 60 - truncated");
Value1Value = Value1Value.substring(0,59);
}
set_Value ("Value1Value", Value1Value);
}
/** Get Value1Value.
@return Value1Value */
public String getValue1Value() 
{
return (String)get_Value("Value1Value");
}
/** Set Value2Name.
@param Value2Name Value2Name */
public void setValue2Name (String Value2Name)
{
if (Value2Name == null) throw new IllegalArgumentException ("Value2Name is mandatory.");
if (Value2Name.length() > 255)
{
log.warning("Length > 255 - truncated");
Value2Name = Value2Name.substring(0,254);
}
set_Value ("Value2Name", Value2Name);
}
/** Get Value2Name.
@return Value2Name */
public String getValue2Name() 
{
return (String)get_Value("Value2Name");
}
/** Set Value2Value.
@param Value2Value Value2Value */
public void setValue2Value (String Value2Value)
{
if (Value2Value == null) throw new IllegalArgumentException ("Value2Value is mandatory.");
if (Value2Value.length() > 60)
{
log.warning("Length > 60 - truncated");
Value2Value = Value2Value.substring(0,59);
}
set_Value ("Value2Value", Value2Value);
}
/** Get Value2Value.
@return Value2Value */
public String getValue2Value() 
{
return (String)get_Value("Value2Value");
}
}
