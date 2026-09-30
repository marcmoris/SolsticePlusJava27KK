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
/** Generated Model for P_ER_Form_Struct
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_ER_Form_Struct extends PO
{
/** Standard Constructor
@param ctx context
@param P_ER_Form_Struct_ID id
@param trxName transaction
*/
public X_P_ER_Form_Struct (Properties ctx, int P_ER_Form_Struct_ID, String trxName)
{
super (ctx, P_ER_Form_Struct_ID, trxName);
/** if (P_ER_Form_Struct_ID == 0)
{
setFieldType (null);
setName (null);
setP_ER_Form_ID (0);
setP_ER_Form_Struct_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_ER_Form_Struct (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100720 */
public static final int Table_ID=2100720;

/** TableName=P_ER_Form_Struct */
public static final String Table_Name="P_ER_Form_Struct";

protected static KeyNamePair Model = new KeyNamePair(2100720,"P_ER_Form_Struct");

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
StringBuffer sb = new StringBuffer ("X_P_ER_Form_Struct[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Description.
@param Description Optional short description of the record */
public void setDescription (String Description)
{
if (Description != null && Description.length() > 2000)
{
log.warning("Length > 2000 - truncated");
Description = Description.substring(0,1999);
}
set_Value ("Description", Description);
}
/** Get Description.
@return Optional short description of the record */
public String getDescription() 
{
return (String)get_Value("Description");
}
/** Set Field Type.
@param FieldType Field Type */
public void setFieldType (String FieldType)
{
if (FieldType == null) throw new IllegalArgumentException ("FieldType is mandatory.");
if (FieldType.length() > 20)
{
log.warning("Length > 20 - truncated");
FieldType = FieldType.substring(0,19);
}
set_Value ("FieldType", FieldType);
}
/** Get Field Type.
@return Field Type */
public String getFieldType() 
{
return (String)get_Value("FieldType");
}
/** Set Name .
@param Name Alphanumeric identifier of the entity */
public void setName (String Name)
{
if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
if (Name.length() > 100)
{
log.warning("Length > 100 - truncated");
Name = Name.substring(0,99);
}
set_Value ("Name", Name);
}
/** Get Name .
@return Alphanumeric identifier of the entity */
public String getName() 
{
return (String)get_Value("Name");
}
/** Set Work performance evaluation form.
@param P_ER_Form_ID Work performance evaluation form */
public void setP_ER_Form_ID (int P_ER_Form_ID)
{
if (P_ER_Form_ID < 1) throw new IllegalArgumentException ("P_ER_Form_ID is mandatory.");
set_Value ("P_ER_Form_ID", new Integer(P_ER_Form_ID));
}
/** Get Work performance evaluation form.
@return Work performance evaluation form */
public int getP_ER_Form_ID() 
{
Integer ii = (Integer)get_Value("P_ER_Form_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Form structure.
@param P_ER_Form_Struct_ID Form structure */
public void setP_ER_Form_Struct_ID (int P_ER_Form_Struct_ID)
{
if (P_ER_Form_Struct_ID < 1) throw new IllegalArgumentException ("P_ER_Form_Struct_ID is mandatory.");
set_ValueNoCheck ("P_ER_Form_Struct_ID", new Integer(P_ER_Form_Struct_ID));
}
/** Get Form structure.
@return Form structure */
public int getP_ER_Form_Struct_ID() 
{
Integer ii = (Integer)get_Value("P_ER_Form_Struct_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set PNumber.
@param PNumber PNumber */
public void setPNumber (int PNumber)
{
set_Value ("PNumber", new Integer(PNumber));
}
/** Get PNumber.
@return PNumber */
public int getPNumber() 
{
Integer ii = (Integer)get_Value("PNumber");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Text.
@param Text Text */
public void setText (String Text)
{
if (Text != null && Text.length() > 2000)
{
log.warning("Length > 2000 - truncated");
Text = Text.substring(0,1999);
}
set_Value ("Text", Text);
}
/** Get Text.
@return Text */
public String getText() 
{
return (String)get_Value("Text");
}
}
