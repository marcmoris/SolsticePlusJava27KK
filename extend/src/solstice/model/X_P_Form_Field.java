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
/** Generated Model for P_Form_Field
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Form_Field extends PO
{
/** Standard Constructor
@param ctx context
@param P_Form_Field_ID id
@param trxName transaction
*/
public X_P_Form_Field (Properties ctx, int P_Form_Field_ID, String trxName)
{
super (ctx, P_Form_Field_ID, trxName);
/** if (P_Form_Field_ID == 0)
{
setName (null);
setP_Form_Field_ID (0);
setP_Form_ID (0);
setValue (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Form_Field (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100671 */
public static final int Table_ID=2100671;

/** TableName=P_Form_Field */
public static final String Table_Name="P_Form_Field";

protected static KeyNamePair Model = new KeyNamePair(2100671,"P_Form_Field");

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
StringBuffer sb = new StringBuffer ("X_P_Form_Field[").append(get_ID()).append("]");
return sb.toString();
}

/** FormFieldType AD_Reference_ID=2100334 */
public static final int FORMFIELDTYPE_AD_Reference_ID=2100334;
/** Gain ( Quantity ) = 8 */
public static final String FORMFIELDTYPE_GainQuantity = "8";
/** Exception Deduction = 9 */
public static final String FORMFIELDTYPE_ExceptionDeduction = "9";
/** Deduction = 1 */
public static final String FORMFIELDTYPE_Deduction = "1";
/** Gain = 2 */
public static final String FORMFIELDTYPE_Gain = "2";
/** Taxable Benefit = 3 */
public static final String FORMFIELDTYPE_TaxableBenefit = "3";
/** Variable = 4 */
public static final String FORMFIELDTYPE_Variable = "4";
/** Texte = 5 */
public static final String FORMFIELDTYPE_Texte = "5";
/** Gain / Avantage Imposable = 6 */
public static final String FORMFIELDTYPE_GainAvantageImposable = "6";
/** Déduction / Avantage Imposable = 7 */
public static final String FORMFIELDTYPE_DéductionAvantageImposable = "7";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isFormFieldTypeValid (String test)
{
return test == null || test.equals("8") || test.equals("9") || test.equals("1") || test.equals("2") || test.equals("3") || test.equals("4") || test.equals("5") || test.equals("6") || test.equals("7");
}
/** Set Type.
@param FormFieldType Type */
public void setFormFieldType (String FormFieldType)
{
if (!isFormFieldTypeValid(FormFieldType))
throw new IllegalArgumentException ("FormFieldType Invalid value - " + FormFieldType + " - Reference_ID=2100334 - 8 - 9 - 1 - 2 - 3 - 4 - 5 - 6 - 7");
if (FormFieldType != null && FormFieldType.length() > 1)
{
log.warning("Length > 1 - truncated");
FormFieldType = FormFieldType.substring(0,0);
}
set_Value ("FormFieldType", FormFieldType);
}
/** Get Type.
@return Type */
public String getFormFieldType() 
{
return (String)get_Value("FormFieldType");
}
/** Set Default value.
@param FormFieldTypeText Default value */
public void setFormFieldTypeText (String FormFieldTypeText)
{
if (FormFieldTypeText != null && FormFieldTypeText.length() > 30)
{
log.warning("Length > 30 - truncated");
FormFieldTypeText = FormFieldTypeText.substring(0,29);
}
set_Value ("FormFieldTypeText", FormFieldTypeText);
}
/** Get Default value.
@return Default value */
public String getFormFieldTypeText() 
{
return (String)get_Value("FormFieldTypeText");
}

/** FormFieldTypeVar AD_Reference_ID=2100338 */
public static final int FORMFIELDTYPEVAR_AD_Reference_ID=2100338;
/** Employee no. = 1 */
public static final String FORMFIELDTYPEVAR_EmployeeNo = "1";
/** SIN = 2 */
public static final String FORMFIELDTYPEVAR_SIN = "2";
/** Employee BirthDate  = 3 */
public static final String FORMFIELDTYPEVAR_EmployeeBirthDate = "3";
/** Province = 4 */
public static final String FORMFIELDTYPEVAR_Province = "4";
/** No. Régime de pension = 5 */
public static final String FORMFIELDTYPEVAR_NoRégimeDePension = "5";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isFormFieldTypeVarValid (String test)
{
return test == null || test.equals("1") || test.equals("2") || test.equals("3") || test.equals("4") || test.equals("5");
}
/** Set Code Variable.
@param FormFieldTypeVar Code Variable */
public void setFormFieldTypeVar (String FormFieldTypeVar)
{
if (!isFormFieldTypeVarValid(FormFieldTypeVar))
throw new IllegalArgumentException ("FormFieldTypeVar Invalid value - " + FormFieldTypeVar + " - Reference_ID=2100338 - 1 - 2 - 3 - 4 - 5");
if (FormFieldTypeVar != null && FormFieldTypeVar.length() > 2)
{
log.warning("Length > 2 - truncated");
FormFieldTypeVar = FormFieldTypeVar.substring(0,1);
}
set_Value ("FormFieldTypeVar", FormFieldTypeVar);
}
/** Get Code Variable.
@return Code Variable */
public String getFormFieldTypeVar() 
{
return (String)get_Value("FormFieldTypeVar");
}
/** Set IsOtherInformation.
@param IsOtherInformation Other information */
public void setIsOtherInformation (boolean IsOtherInformation)
{
set_Value ("IsOtherInformation", new Boolean(IsOtherInformation));
}
/** Get IsOtherInformation.
@return Other information */
public boolean isOtherInformation() 
{
Object oo = get_Value("IsOtherInformation");
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
/** Set Field.
@param P_Form_Field_ID Field */
public void setP_Form_Field_ID (int P_Form_Field_ID)
{
if (P_Form_Field_ID < 1) throw new IllegalArgumentException ("P_Form_Field_ID is mandatory.");
set_ValueNoCheck ("P_Form_Field_ID", new Integer(P_Form_Field_ID));
}
/** Get Field.
@return Field */
public int getP_Form_Field_ID() 
{
Integer ii = (Integer)get_Value("P_Form_Field_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Form.
@param P_Form_ID Form */
public void setP_Form_ID (int P_Form_ID)
{
if (P_Form_ID < 1) throw new IllegalArgumentException ("P_Form_ID is mandatory.");
set_Value ("P_Form_ID", new Integer(P_Form_ID));
}
/** Get Form.
@return Form */
public int getP_Form_ID() 
{
Integer ii = (Integer)get_Value("P_Form_ID");
if (ii == null) return 0;
return ii.intValue();
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
