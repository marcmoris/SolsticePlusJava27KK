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
/** Generated Model for P_ER_Form
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_ER_Form extends PO
{
/** Standard Constructor
@param ctx context
@param P_ER_Form_ID id
@param trxName transaction
*/
public X_P_ER_Form (Properties ctx, int P_ER_Form_ID, String trxName)
{
super (ctx, P_ER_Form_ID, trxName);
/** if (P_ER_Form_ID == 0)
{
setFormDate (new Timestamp(System.currentTimeMillis()));
setFormType (null);
setP_ER_Form_ID (0);
setVersion (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_ER_Form (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100719 */
public static final int Table_ID=2100719;

/** TableName=P_ER_Form */
public static final String Table_Name="P_ER_Form";

protected static KeyNamePair Model = new KeyNamePair(2100719,"P_ER_Form");

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
StringBuffer sb = new StringBuffer ("X_P_ER_Form[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Form date.
@param FormDate Form date */
public void setFormDate (Timestamp FormDate)
{
if (FormDate == null) throw new IllegalArgumentException ("FormDate is mandatory.");
set_Value ("FormDate", FormDate);
}
/** Get Form date.
@return Form date */
public Timestamp getFormDate() 
{
return (Timestamp)get_Value("FormDate");
}
/** Set Form Type.
@param FormType Form Type */
public void setFormType (String FormType)
{
if (FormType == null) throw new IllegalArgumentException ("FormType is mandatory.");
if (FormType.length() > 3)
{
log.warning("Length > 3 - truncated");
FormType = FormType.substring(0,2);
}
set_Value ("FormType", FormType);
}
/** Get Form Type.
@return Form Type */
public String getFormType() 
{
return (String)get_Value("FormType");
}
/** Set Work performance evaluation form.
@param P_ER_Form_ID Work performance evaluation form */
public void setP_ER_Form_ID (int P_ER_Form_ID)
{
if (P_ER_Form_ID < 1) throw new IllegalArgumentException ("P_ER_Form_ID is mandatory.");
set_ValueNoCheck ("P_ER_Form_ID", new Integer(P_ER_Form_ID));
}
/** Get Work performance evaluation form.
@return Work performance evaluation form */
public int getP_ER_Form_ID() 
{
Integer ii = (Integer)get_Value("P_ER_Form_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Version.
@param Version Version of the table definition */
public void setVersion (int Version)
{
set_Value ("Version", new Integer(Version));
}
/** Get Version.
@return Version of the table definition */
public int getVersion() 
{
Integer ii = (Integer)get_Value("Version");
if (ii == null) return 0;
return ii.intValue();
}
}
