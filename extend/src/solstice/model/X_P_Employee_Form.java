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
/** Generated Model for P_Employee_Form
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Employee_Form extends PO
{
/** Standard Constructor
@param ctx context
@param P_Employee_Form_ID id
@param trxName transaction
*/
public X_P_Employee_Form (Properties ctx, int P_Employee_Form_ID, String trxName)
{
super (ctx, P_Employee_Form_ID, trxName);
/** if (P_Employee_Form_ID == 0)
{
setP_Employee_Form_ID (0);
setP_Employee_ID (0);
setP_Form_Template_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Employee_Form (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100685 */
public static final int Table_ID=2100685;

/** TableName=P_Employee_Form */
public static final String Table_Name="P_Employee_Form";

protected static KeyNamePair Model = new KeyNamePair(2100685,"P_Employee_Form");

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
StringBuffer sb = new StringBuffer ("X_P_Employee_Form[").append(get_ID()).append("]");
return sb.toString();
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
/** Set Form.
@param P_Employee_Form_ID Form */
public void setP_Employee_Form_ID (int P_Employee_Form_ID)
{
if (P_Employee_Form_ID < 1) throw new IllegalArgumentException ("P_Employee_Form_ID is mandatory.");
set_ValueNoCheck ("P_Employee_Form_ID", new Integer(P_Employee_Form_ID));
}
/** Get Form.
@return Form */
public int getP_Employee_Form_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Form_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Employee_ID AD_Reference_ID=2000010 */
public static final int P_EMPLOYEE_ID_AD_Reference_ID=2000010;
/** Set Employee .
@param P_Employee_ID Employee */
public void setP_Employee_ID (int P_Employee_ID)
{
if (P_Employee_ID < 1) throw new IllegalArgumentException ("P_Employee_ID is mandatory.");
set_ValueNoCheck ("P_Employee_ID", new Integer(P_Employee_ID));
}
/** Get Employee .
@return Employee */
public int getP_Employee_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Get Record ID/ColumnName
@return ID/ColumnName pair */
public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), String.valueOf(getP_Employee_ID()));
}

/** P_Form_Template_ID AD_Reference_ID=2100345 */
public static final int P_FORM_TEMPLATE_ID_AD_Reference_ID=2100345;
/** Set Form Template.
@param P_Form_Template_ID Form Template */
public void setP_Form_Template_ID (int P_Form_Template_ID)
{
if (P_Form_Template_ID < 1) throw new IllegalArgumentException ("P_Form_Template_ID is mandatory.");
set_ValueNoCheck ("P_Form_Template_ID", new Integer(P_Form_Template_ID));
}
/** Get Form Template.
@return Form Template */
public int getP_Form_Template_ID() 
{
Integer ii = (Integer)get_Value("P_Form_Template_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
