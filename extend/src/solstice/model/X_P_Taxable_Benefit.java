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
/** Generated Model for P_Taxable_Benefit
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Taxable_Benefit extends PO
{
/** Standard Constructor
@param ctx context
@param P_Taxable_Benefit_ID id
@param trxName transaction
*/
public X_P_Taxable_Benefit (Properties ctx, int P_Taxable_Benefit_ID, String trxName)
{
super (ctx, P_Taxable_Benefit_ID, trxName);
/** if (P_Taxable_Benefit_ID == 0)
{
setName (null);
setP_Taxable_Benefit_ID (0);
setValue (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Taxable_Benefit (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000071 */
public static final int Table_ID=2000071;

/** TableName=P_Taxable_Benefit */
public static final String Table_Name="P_Taxable_Benefit";

protected static KeyNamePair Model = new KeyNamePair(2000071,"P_Taxable_Benefit");

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
StringBuffer sb = new StringBuffer ("X_P_Taxable_Benefit[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Country.
@param C_Country_ID Country  */
public void setC_Country_ID (int C_Country_ID)
{
if (C_Country_ID <= 0) set_Value ("C_Country_ID", null);
 else 
set_Value ("C_Country_ID", new Integer(C_Country_ID));
}
/** Get Country.
@return Country  */
public int getC_Country_ID() 
{
Integer ii = (Integer)get_Value("C_Country_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** C_Region_ID AD_Reference_ID=2000026 */
public static final int C_REGION_ID_AD_Reference_ID=2000026;
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

/** IsProvincial AD_Reference_ID=2100469 */
public static final int ISPROVINCIAL_AD_Reference_ID=2100469;
/** Provincial = Y */
public static final String ISPROVINCIAL_Provincial = "Y";
/** Federal = N */
public static final String ISPROVINCIAL_Federal = "N";
/** Federal and Provincial = O */
public static final String ISPROVINCIAL_FederalAndProvincial = "O";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isIsProvincialValid (String test)
{
return test == null || test.equals("Y") || test.equals("N") || test.equals("O");
}
/** Set Type.
@param IsProvincial Type */
public void setIsProvincial (String IsProvincial)
{
if (!isIsProvincialValid(IsProvincial))
throw new IllegalArgumentException ("IsProvincial Invalid value - " + IsProvincial + " - Reference_ID=2100469 - Y - N - O");
if (IsProvincial != null && IsProvincial.length() > 1)
{
log.warning("Length > 1 - truncated");
IsProvincial = IsProvincial.substring(0,0);
}
set_Value ("IsProvincial", IsProvincial);
}
/** Get Type.
@return Type */
public String getIsProvincial() 
{
return (String)get_Value("IsProvincial");
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
/** Set Taxable Benefit.
@param P_Taxable_Benefit_ID Taxable Benefit */
public void setP_Taxable_Benefit_ID (int P_Taxable_Benefit_ID)
{
if (P_Taxable_Benefit_ID < 1) throw new IllegalArgumentException ("P_Taxable_Benefit_ID is mandatory.");
set_ValueNoCheck ("P_Taxable_Benefit_ID", new Integer(P_Taxable_Benefit_ID));
}
/** Get Taxable Benefit.
@return Taxable Benefit */
public int getP_Taxable_Benefit_ID() 
{
Integer ii = (Integer)get_Value("P_Taxable_Benefit_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Fixed Amount.
@param TaxB_Fixed_Amount Fixed Amount */
public void setTaxB_Fixed_Amount (BigDecimal TaxB_Fixed_Amount)
{
set_Value ("TaxB_Fixed_Amount", TaxB_Fixed_Amount);
}
/** Get Fixed Amount.
@return Fixed Amount */
public BigDecimal getTaxB_Fixed_Amount() 
{
BigDecimal bd = (BigDecimal)get_Value("TaxB_Fixed_Amount");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Taxe Rate Quebec.
@param TaxeRate Taxe Rate Quebec */
public void setTaxeRate (BigDecimal TaxeRate)
{
set_Value ("TaxeRate", TaxeRate);
}
/** Get Taxe Rate Quebec.
@return Taxe Rate Quebec */
public BigDecimal getTaxeRate() 
{
BigDecimal bd = (BigDecimal)get_Value("TaxeRate");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Taxe Rate Ontario.
@param TaxeRateOntario Taxe Rate Ontario */
public void setTaxeRateOntario (BigDecimal TaxeRateOntario)
{
set_Value ("TaxeRateOntario", TaxeRateOntario);
}
/** Get Taxe Rate Ontario.
@return Taxe Rate Ontario */
public BigDecimal getTaxeRateOntario() 
{
BigDecimal bd = (BigDecimal)get_Value("TaxeRateOntario");
if (bd == null) return Env.ZERO;
return bd;
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
