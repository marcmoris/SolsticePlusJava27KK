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
/** Generated Model for P_Employee_Form_Detail
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Employee_Form_Detail extends PO
{
/** Standard Constructor
@param ctx context
@param P_Employee_Form_Detail_ID id
@param trxName transaction
*/
public X_P_Employee_Form_Detail (Properties ctx, int P_Employee_Form_Detail_ID, String trxName)
{
super (ctx, P_Employee_Form_Detail_ID, trxName);
/** if (P_Employee_Form_Detail_ID == 0)
{
setP_Employee_Form_Detail_ID (0);
setP_Employee_Form_ID (0);
setP_Form_Template_Detail_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Employee_Form_Detail (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100686 */
public static final int Table_ID=2100686;

/** TableName=P_Employee_Form_Detail */
public static final String Table_Name="P_Employee_Form_Detail";

protected static KeyNamePair Model = new KeyNamePair(2100686,"P_Employee_Form_Detail");

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
StringBuffer sb = new StringBuffer ("X_P_Employee_Form_Detail[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Default.
@param DefaultTaxAmount Valeur par défaut provenant des paramètres de la rémunérations */
public void setDefaultTaxAmount (String DefaultTaxAmount)
{
throw new IllegalArgumentException ("DefaultTaxAmount is virtual column");
}
/** Get Default.
@return Valeur par défaut provenant des paramètres de la rémunérations */
public String getDefaultTaxAmount() 
{
return (String)get_Value("DefaultTaxAmount");
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

/** FormTypeField AD_Reference_ID=2100347 */
public static final int FORMTYPEFIELD_AD_Reference_ID=2100347;
/** Text = 2 */
public static final String FORMTYPEFIELD_Text = "2";
/** Amount = 1 */
public static final String FORMTYPEFIELD_Amount = "1";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isFormTypeFieldValid (String test)
{
return test == null || test.equals("2") || test.equals("1");
}
/** Set Type Field.
@param FormTypeField Type Field */
public void setFormTypeField (String FormTypeField)
{
if (!isFormTypeFieldValid(FormTypeField))
throw new IllegalArgumentException ("FormTypeField Invalid value - " + FormTypeField + " - Reference_ID=2100347 - 2 - 1");
if (FormTypeField != null && FormTypeField.length() > 1)
{
log.warning("Length > 1 - truncated");
FormTypeField = FormTypeField.substring(0,0);
}
set_Value ("FormTypeField", FormTypeField);
}
/** Get Type Field.
@return Type Field */
public String getFormTypeField() 
{
return (String)get_Value("FormTypeField");
}
/** Set Description.
@param P_Employee_Form_Detail_ID Description */
public void setP_Employee_Form_Detail_ID (int P_Employee_Form_Detail_ID)
{
if (P_Employee_Form_Detail_ID < 1) throw new IllegalArgumentException ("P_Employee_Form_Detail_ID is mandatory.");
set_ValueNoCheck ("P_Employee_Form_Detail_ID", new Integer(P_Employee_Form_Detail_ID));
}
/** Get Description.
@return Description */
public int getP_Employee_Form_Detail_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Form_Detail_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Form.
@param P_Employee_Form_ID Form */
public void setP_Employee_Form_ID (int P_Employee_Form_ID)
{
if (P_Employee_Form_ID < 1) throw new IllegalArgumentException ("P_Employee_Form_ID is mandatory.");
set_Value ("P_Employee_Form_ID", new Integer(P_Employee_Form_ID));
}
/** Get Form.
@return Form */
public int getP_Employee_Form_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Form_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Form_Template_Detail_ID AD_Reference_ID=2100346 */
public static final int P_FORM_TEMPLATE_DETAIL_ID_AD_Reference_ID=2100346;
/** Set Description.
@param P_Form_Template_Detail_ID Description */
public void setP_Form_Template_Detail_ID (int P_Form_Template_Detail_ID)
{
if (P_Form_Template_Detail_ID < 1) throw new IllegalArgumentException ("P_Form_Template_Detail_ID is mandatory.");
set_Value ("P_Form_Template_Detail_ID", new Integer(P_Form_Template_Detail_ID));
}
/** Get Description.
@return Description */
public int getP_Form_Template_Detail_ID() 
{
Integer ii = (Integer)get_Value("P_Form_Template_Detail_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
