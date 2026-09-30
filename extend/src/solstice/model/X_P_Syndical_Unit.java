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
/** Generated Model for P_Syndical_Unit
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Syndical_Unit extends PO
{
/** Standard Constructor
@param ctx context
@param P_Syndical_Unit_ID id
@param trxName transaction
*/
public X_P_Syndical_Unit (Properties ctx, int P_Syndical_Unit_ID, String trxName)
{
super (ctx, P_Syndical_Unit_ID, trxName);
/** if (P_Syndical_Unit_ID == 0)
{
setName (null);
setP_Syndical_Unit_ID (0);
setValue (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Syndical_Unit (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000016 */
public static final int Table_ID=2000016;

/** TableName=P_Syndical_Unit */
public static final String Table_Name="P_Syndical_Unit";

protected static KeyNamePair Model = new KeyNamePair(2000016,"P_Syndical_Unit");

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
StringBuffer sb = new StringBuffer ("X_P_Syndical_Unit[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Address.
@param C_Location_ID Location or Address */
public void setC_Location_ID (int C_Location_ID)
{
if (C_Location_ID <= 0) set_Value ("C_Location_ID", null);
 else 
set_Value ("C_Location_ID", new Integer(C_Location_ID));
}
/** Get Address.
@return Location or Address */
public int getC_Location_ID() 
{
Integer ii = (Integer)get_Value("C_Location_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Central.
@param Central Central */
public void setCentral (String Central)
{
if (Central != null && Central.length() > 120)
{
log.warning("Length > 120 - truncated");
Central = Central.substring(0,119);
}
set_Value ("Central", Central);
}
/** Get Central.
@return Central */
public String getCentral() 
{
return (String)get_Value("Central");
}
/** Set Fax.
@param Fax Facsimile number */
public void setFax (String Fax)
{
if (Fax != null && Fax.length() > 80)
{
log.warning("Length > 80 - truncated");
Fax = Fax.substring(0,79);
}
set_Value ("Fax", Fax);
}
/** Get Fax.
@return Facsimile number */
public String getFax() 
{
return (String)get_Value("Fax");
}
/** Set Name .
@param Name Alphanumeric identifier of the entity */
public void setName (String Name)
{
if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
if (Name.length() > 120)
{
log.warning("Length > 120 - truncated");
Name = Name.substring(0,119);
}
set_Value ("Name", Name);
}
/** Get Name .
@return Alphanumeric identifier of the entity */
public String getName() 
{
return (String)get_Value("Name");
}
/** Set Syndical Unit.
@param P_Syndical_Unit_ID    */
public void setP_Syndical_Unit_ID (int P_Syndical_Unit_ID)
{
if (P_Syndical_Unit_ID < 1) throw new IllegalArgumentException ("P_Syndical_Unit_ID is mandatory.");
set_Value ("P_Syndical_Unit_ID", new Integer(P_Syndical_Unit_ID));
}
/** Get Syndical Unit.
@return    */
public int getP_Syndical_Unit_ID() 
{
Integer ii = (Integer)get_Value("P_Syndical_Unit_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Phone.
@param Phone Identifies a telephone number */
public void setPhone (String Phone)
{
if (Phone != null && Phone.length() > 40)
{
log.warning("Length > 40 - truncated");
Phone = Phone.substring(0,39);
}
set_Value ("Phone", Phone);
}
/** Get Phone.
@return Identifies a telephone number */
public String getPhone() 
{
return (String)get_Value("Phone");
}
/** Set 2nd Phone.
@param Phone2 Identifies an alternate telephone number. */
public void setPhone2 (String Phone2)
{
if (Phone2 != null && Phone2.length() > 40)
{
log.warning("Length > 40 - truncated");
Phone2 = Phone2.substring(0,39);
}
set_Value ("Phone2", Phone2);
}
/** Get 2nd Phone.
@return Identifies an alternate telephone number. */
public String getPhone2() 
{
return (String)get_Value("Phone2");
}
/** Set Search Key.
@param Value Search key for the record in the format required - must be unique */
public void setValue (String Value)
{
if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
if (Value.length() > 60)
{
log.warning("Length > 60 - truncated");
Value = Value.substring(0,59);
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
