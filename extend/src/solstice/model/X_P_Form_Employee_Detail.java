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
/** Generated Model for P_Form_Employee_Detail
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Form_Employee_Detail extends PO
{
/** Standard Constructor
@param ctx context
@param P_Form_Employee_Detail_ID id
@param trxName transaction
*/
public X_P_Form_Employee_Detail (Properties ctx, int P_Form_Employee_Detail_ID, String trxName)
{
super (ctx, P_Form_Employee_Detail_ID, trxName);
/** if (P_Form_Employee_Detail_ID == 0)
{
setP_Form_Employee_Detail_ID (0);
setP_Form_Employee_ID (0);
setP_Form_Field_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Form_Employee_Detail (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100670 */
public static final int Table_ID=2100670;

/** TableName=P_Form_Employee_Detail */
public static final String Table_Name="P_Form_Employee_Detail";

protected static KeyNamePair Model = new KeyNamePair(2100670,"P_Form_Employee_Detail");

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
StringBuffer sb = new StringBuffer ("X_P_Form_Employee_Detail[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Amount.
@param FormFieldAmount Amount */
public void setFormFieldAmount (BigDecimal FormFieldAmount)
{
set_Value ("FormFieldAmount", FormFieldAmount);
}
/** Get Amount.
@return Amount */
public BigDecimal getFormFieldAmount() 
{
BigDecimal bd = (BigDecimal)get_Value("FormFieldAmount");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Text.
@param FormFieldText Text */
public void setFormFieldText (String FormFieldText)
{
if (FormFieldText != null && FormFieldText.length() > 255)
{
log.warning("Length > 255 - truncated");
FormFieldText = FormFieldText.substring(0,254);
}
set_Value ("FormFieldText", FormFieldText);
}
/** Get Text.
@return Text */
public String getFormFieldText() 
{
return (String)get_Value("FormFieldText");
}
/** Set Generated.
@param IsGenerated This Line is generated */
public void setIsGenerated (boolean IsGenerated)
{
throw new IllegalArgumentException ("IsGenerated is virtual column");
}
/** Get Generated.
@return This Line is generated */
public boolean isGenerated() 
{
Object oo = get_Value("IsGenerated");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Printed.
@param IsPrinted Indicates if this document / line is printed */
public void setIsPrinted (boolean IsPrinted)
{
throw new IllegalArgumentException ("IsPrinted is virtual column");
}
/** Get Printed.
@return Indicates if this document / line is printed */
public boolean isPrinted() 
{
Object oo = get_Value("IsPrinted");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set P_Form_Employee_Detail_ID.
@param P_Form_Employee_Detail_ID P_Form_Employee_Detail_ID */
public void setP_Form_Employee_Detail_ID (int P_Form_Employee_Detail_ID)
{
if (P_Form_Employee_Detail_ID < 1) throw new IllegalArgumentException ("P_Form_Employee_Detail_ID is mandatory.");
set_ValueNoCheck ("P_Form_Employee_Detail_ID", new Integer(P_Form_Employee_Detail_ID));
}
/** Get P_Form_Employee_Detail_ID.
@return P_Form_Employee_Detail_ID */
public int getP_Form_Employee_Detail_ID() 
{
Integer ii = (Integer)get_Value("P_Form_Employee_Detail_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Employee.
@param P_Form_Employee_ID Employee */
public void setP_Form_Employee_ID (int P_Form_Employee_ID)
{
if (P_Form_Employee_ID < 1) throw new IllegalArgumentException ("P_Form_Employee_ID is mandatory.");
set_Value ("P_Form_Employee_ID", new Integer(P_Form_Employee_ID));
}
/** Get Employee.
@return Employee */
public int getP_Form_Employee_ID() 
{
Integer ii = (Integer)get_Value("P_Form_Employee_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Form_Field_ID AD_Reference_ID=2100335 */
public static final int P_FORM_FIELD_ID_AD_Reference_ID=2100335;
/** Set Field.
@param P_Form_Field_ID Field */
public void setP_Form_Field_ID (int P_Form_Field_ID)
{
if (P_Form_Field_ID < 1) throw new IllegalArgumentException ("P_Form_Field_ID is mandatory.");
set_Value ("P_Form_Field_ID", new Integer(P_Form_Field_ID));
}
/** Get Field.
@return Field */
public int getP_Form_Field_ID() 
{
Integer ii = (Integer)get_Value("P_Form_Field_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Year.
@param Year Calendar Year */
public void setYear (String Year)
{
throw new IllegalArgumentException ("Year is virtual column");
}
/** Get Year.
@return Calendar Year */
public String getYear() 
{
return (String)get_Value("Year");
}
}
