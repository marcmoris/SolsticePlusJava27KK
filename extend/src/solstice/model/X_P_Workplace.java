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
/** Generated Model for P_Workplace
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Workplace extends PO
{
/** Standard Constructor
@param ctx context
@param P_Workplace_ID id
@param trxName transaction
*/
public X_P_Workplace (Properties ctx, int P_Workplace_ID, String trxName)
{
super (ctx, P_Workplace_ID, trxName);
/** if (P_Workplace_ID == 0)
{
setName (null);
setP_Workplace_ID (0);
setValue (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Workplace (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000013 */
public static final int Table_ID=2000013;

/** TableName=P_Workplace */
public static final String Table_Name="P_Workplace";

protected static KeyNamePair Model = new KeyNamePair(2000013,"P_Workplace");

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
StringBuffer sb = new StringBuffer ("X_P_Workplace[").append(get_ID()).append("]");
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
/** Set Region.
@param C_Region_ID Identifies a geographical Region */
public void setC_Region_ID (int C_Region_ID)
{
if (C_Region_ID <= 0) set_Value ("C_Region_ID", null);
 else 
set_Value ("C_Region_ID", new Integer(C_Region_ID));
}
/** Get Region.
@return Identifies a geographical Region */
public int getC_Region_ID() 
{
Integer ii = (Integer)get_Value("C_Region_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Base.
@param C_SalesRegion_ID Base */
public void setC_SalesRegion_ID (int C_SalesRegion_ID)
{
if (C_SalesRegion_ID <= 0) set_Value ("C_SalesRegion_ID", null);
 else 
set_Value ("C_SalesRegion_ID", new Integer(C_SalesRegion_ID));
}
/** Get Base.
@return Base */
public int getC_SalesRegion_ID() 
{
Integer ii = (Integer)get_Value("C_SalesRegion_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Contact Name.
@param ContactName Business Partner Contact Name */
public void setContactName (String ContactName)
{
if (ContactName != null && ContactName.length() > 60)
{
log.warning("Length > 60 - truncated");
ContactName = ContactName.substring(0,59);
}
set_Value ("ContactName", ContactName);
}
/** Get Contact Name.
@return Business Partner Contact Name */
public String getContactName() 
{
return (String)get_Value("ContactName");
}
/** Set Contact Name 2.
@param ContactName2 Business Partner Contact Name */
public void setContactName2 (String ContactName2)
{
if (ContactName2 != null && ContactName2.length() > 60)
{
log.warning("Length > 60 - truncated");
ContactName2 = ContactName2.substring(0,59);
}
set_Value ("ContactName2", ContactName2);
}
/** Get Contact Name 2.
@return Business Partner Contact Name */
public String getContactName2() 
{
return (String)get_Value("ContactName2");
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
/** Set Establishment.
@param Establishment Establishment */
public void setEstablishment (String Establishment)
{
if (Establishment != null && Establishment.length() > 30)
{
log.warning("Length > 30 - truncated");
Establishment = Establishment.substring(0,29);
}
set_Value ("Establishment", Establishment);
}
/** Get Establishment.
@return Establishment */
public String getEstablishment() 
{
return (String)get_Value("Establishment");
}
/** Set Fax.
@param Fax Facsimile number */
public void setFax (String Fax)
{
if (Fax != null && Fax.length() > 40)
{
log.warning("Length > 40 - truncated");
Fax = Fax.substring(0,39);
}
set_Value ("Fax", Fax);
}
/** Get Fax.
@return Facsimile number */
public String getFax() 
{
return (String)get_Value("Fax");
}
/** Set Default.
@param IsDefault Default value */
public void setIsDefault (boolean IsDefault)
{
set_Value ("IsDefault", new Boolean(IsDefault));
}
/** Get Default.
@return Default value */
public boolean isDefault() 
{
Object oo = get_Value("IsDefault");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Legal Entity.
@param LegalEntity Legal Entity */
public void setLegalEntity (String LegalEntity)
{
if (LegalEntity != null && LegalEntity.length() > 30)
{
log.warning("Length > 30 - truncated");
LegalEntity = LegalEntity.substring(0,29);
}
set_Value ("LegalEntity", LegalEntity);
}
/** Get Legal Entity.
@return Legal Entity */
public String getLegalEntity() 
{
return (String)get_Value("LegalEntity");
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
/** Get Record ID/ColumnName
@return ID/ColumnName pair */
public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), getName());
}
/** Set Workplace (Base).
@param P_Workplace_ID Workplace (Base) */
public void setP_Workplace_ID (int P_Workplace_ID)
{
if (P_Workplace_ID < 1) throw new IllegalArgumentException ("P_Workplace_ID is mandatory.");
set_ValueNoCheck ("P_Workplace_ID", new Integer(P_Workplace_ID));
}
/** Get Workplace (Base).
@return Workplace (Base) */
public int getP_Workplace_ID() 
{
Integer ii = (Integer)get_Value("P_Workplace_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Region.
@param P_WorkRegion_ID Region */
public void setP_WorkRegion_ID (int P_WorkRegion_ID)
{
if (P_WorkRegion_ID <= 0) set_Value ("P_WorkRegion_ID", null);
 else 
set_Value ("P_WorkRegion_ID", new Integer(P_WorkRegion_ID));
}
/** Get Region.
@return Region */
public int getP_WorkRegion_ID() 
{
Integer ii = (Integer)get_Value("P_WorkRegion_ID");
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
/** Set Phone number.
@param PhoneNumberContactPerson Phone number Contact Person */
public void setPhoneNumberContactPerson (String PhoneNumberContactPerson)
{
if (PhoneNumberContactPerson != null && PhoneNumberContactPerson.length() > 40)
{
log.warning("Length > 40 - truncated");
PhoneNumberContactPerson = PhoneNumberContactPerson.substring(0,39);
}
set_Value ("PhoneNumberContactPerson", PhoneNumberContactPerson);
}
/** Get Phone number.
@return Phone number Contact Person */
public String getPhoneNumberContactPerson() 
{
return (String)get_Value("PhoneNumberContactPerson");
}
/** Set Phone 2.
@param PhoneNumberContactPerson2 Phone Contact Person */
public void setPhoneNumberContactPerson2 (String PhoneNumberContactPerson2)
{
if (PhoneNumberContactPerson2 != null && PhoneNumberContactPerson2.length() > 40)
{
log.warning("Length > 40 - truncated");
PhoneNumberContactPerson2 = PhoneNumberContactPerson2.substring(0,39);
}
set_Value ("PhoneNumberContactPerson2", PhoneNumberContactPerson2);
}
/** Get Phone 2.
@return Phone Contact Person */
public String getPhoneNumberContactPerson2() 
{
return (String)get_Value("PhoneNumberContactPerson2");
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

/** Get isTaxable.
@return isTaxable */
public boolean isTaxable() 
{
Object oo = get_Value("isTaxable");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
 return false;
}

}
