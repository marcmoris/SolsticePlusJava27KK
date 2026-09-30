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
/** Generated Model for P_Form_Field_Taxable_Benefit
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Form_Field_Taxable_Benefit extends PO
{
/** Standard Constructor
@param ctx context
@param P_Form_Field_Taxable_Benefit_ID id
@param trxName transaction
*/
public X_P_Form_Field_Taxable_Benefit (Properties ctx, int P_Form_Field_Taxable_Benefit_ID, String trxName)
{
super (ctx, P_Form_Field_Taxable_Benefit_ID, trxName);
/** if (P_Form_Field_Taxable_Benefit_ID == 0)
{
setP_Form_Field_ID (0);	// @P_Form_Field_ID@
setP_Form_Field_Taxable_Benefit_ID (0);
setP_Taxable_Benefit_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Form_Field_Taxable_Benefit (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100674 */
public static final int Table_ID=2100674;

/** TableName=P_Form_Field_Taxable_Benefit */
public static final String Table_Name="P_Form_Field_Taxable_Benefit";

protected static KeyNamePair Model = new KeyNamePair(2100674,"P_Form_Field_Taxable_Benefit");

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
StringBuffer sb = new StringBuffer ("X_P_Form_Field_Taxable_Benefit[").append(get_ID()).append("]");
return sb.toString();
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
/** Set P_Form_Field_Taxable_Benefit_ID.
@param P_Form_Field_Taxable_Benefit_ID P_Form_Field_Taxable_Benefit_ID */
public void setP_Form_Field_Taxable_Benefit_ID (int P_Form_Field_Taxable_Benefit_ID)
{
if (P_Form_Field_Taxable_Benefit_ID < 1) throw new IllegalArgumentException ("P_Form_Field_Taxable_Benefit_ID is mandatory.");
set_ValueNoCheck ("P_Form_Field_Taxable_Benefit_ID", new Integer(P_Form_Field_Taxable_Benefit_ID));
}
/** Get P_Form_Field_Taxable_Benefit_ID.
@return P_Form_Field_Taxable_Benefit_ID */
public int getP_Form_Field_Taxable_Benefit_ID() 
{
Integer ii = (Integer)get_Value("P_Form_Field_Taxable_Benefit_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Taxable_Benefit_ID AD_Reference_ID=2000046 */
public static final int P_TAXABLE_BENEFIT_ID_AD_Reference_ID=2000046;
/** Set Taxable Benefit.
@param P_Taxable_Benefit_ID Taxable Benefit */
public void setP_Taxable_Benefit_ID (int P_Taxable_Benefit_ID)
{
if (P_Taxable_Benefit_ID < 1) throw new IllegalArgumentException ("P_Taxable_Benefit_ID is mandatory.");
set_Value ("P_Taxable_Benefit_ID", new Integer(P_Taxable_Benefit_ID));
}
/** Get Taxable Benefit.
@return Taxable Benefit */
public int getP_Taxable_Benefit_ID() 
{
Integer ii = (Integer)get_Value("P_Taxable_Benefit_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
