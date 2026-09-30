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
/** Generated Model for P_Audit_Trail_Insurance
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Audit_Trail_Insurance extends PO
{
/** Standard Constructor
@param ctx context
@param P_Audit_Trail_Insurance_ID id
@param trxName transaction
*/
public X_P_Audit_Trail_Insurance (Properties ctx, int P_Audit_Trail_Insurance_ID, String trxName)
{
super (ctx, P_Audit_Trail_Insurance_ID, trxName);
/** if (P_Audit_Trail_Insurance_ID == 0)
{
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Audit_Trail_Insurance (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100884 */
public static final int Table_ID=2100884;

/** TableName=P_Audit_Trail_Insurance */
public static final String Table_Name="P_Audit_Trail_Insurance";

protected static KeyNamePair Model = new KeyNamePair(2100884,"P_Audit_Trail_Insurance");

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
StringBuffer sb = new StringBuffer ("X_P_Audit_Trail_Insurance[").append(get_ID()).append("]");
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
/** Set Effect In.
@param EffectIn Date when information in effect  */
public void setEffectIn (Timestamp EffectIn)
{
set_Value ("EffectIn", EffectIn);
}
/** Get Effect In.
@return Date when information in effect  */
public Timestamp getEffectIn() 
{
return (Timestamp)get_Value("EffectIn");
}
/** Set To.
@param EffectTo To */
public void setEffectTo (Timestamp EffectTo)
{
set_Value ("EffectTo", EffectTo);
}
/** Get To.
@return To */
public Timestamp getEffectTo() 
{
return (Timestamp)get_Value("EffectTo");
}
/** Set New Value.
@param NewValue New field value */
public void setNewValue (String NewValue)
{
if (NewValue != null && NewValue.length() > 60)
{
log.warning("Length > 60 - truncated");
NewValue = NewValue.substring(0,59);
}
set_Value ("NewValue", NewValue);
}
/** Get New Value.
@return New field value */
public String getNewValue() 
{
return (String)get_Value("NewValue");
}
/** Set Old Value.
@param OldValue The old file data */
public void setOldValue (String OldValue)
{
if (OldValue != null && OldValue.length() > 60)
{
log.warning("Length > 60 - truncated");
OldValue = OldValue.substring(0,59);
}
set_Value ("OldValue", OldValue);
}
/** Get Old Value.
@return The old file data */
public String getOldValue() 
{
return (String)get_Value("OldValue");
}
/** Set P_Audit_Trail_Insurance_ID.
@param P_Audit_Trail_Insurance_ID P_Audit_Trail_Insurance_ID */
public void setP_Audit_Trail_Insurance_ID (int P_Audit_Trail_Insurance_ID)
{
if (P_Audit_Trail_Insurance_ID <= 0) set_ValueNoCheck ("P_Audit_Trail_Insurance_ID", null);
 else 
set_ValueNoCheck ("P_Audit_Trail_Insurance_ID", new Integer(P_Audit_Trail_Insurance_ID));
}
/** Get P_Audit_Trail_Insurance_ID.
@return P_Audit_Trail_Insurance_ID */
public int getP_Audit_Trail_Insurance_ID() 
{
Integer ii = (Integer)get_Value("P_Audit_Trail_Insurance_ID");
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
}
