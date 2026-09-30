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
/** Generated Model for P_Group_Security
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Group_Security extends PO
{
/** Standard Constructor
@param ctx context
@param P_Group_Security_ID id
@param trxName transaction
*/
public X_P_Group_Security (Properties ctx, int P_Group_Security_ID, String trxName)
{
super (ctx, P_Group_Security_ID, trxName);
/** if (P_Group_Security_ID == 0)
{
setP_Group_ID (0);
setP_Group_Security_ID (0);
setP_Security_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Group_Security (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100812 */
public static final int Table_ID=2100812;

/** TableName=P_Group_Security */
public static final String Table_Name="P_Group_Security";

protected static KeyNamePair Model = new KeyNamePair(2100812,"P_Group_Security");

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
StringBuffer sb = new StringBuffer ("X_P_Group_Security[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Security groups.
@param P_Group_ID Security groups */
public void setP_Group_ID (int P_Group_ID)
{
if (P_Group_ID < 1) throw new IllegalArgumentException ("P_Group_ID is mandatory.");
set_Value ("P_Group_ID", new Integer(P_Group_ID));
}
/** Get Security groups.
@return Security groups */
public int getP_Group_ID() 
{
Integer ii = (Integer)get_Value("P_Group_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Security groups ans policies.
@param P_Group_Security_ID Security groups ans policies */
public void setP_Group_Security_ID (int P_Group_Security_ID)
{
if (P_Group_Security_ID < 1) throw new IllegalArgumentException ("P_Group_Security_ID is mandatory.");
set_ValueNoCheck ("P_Group_Security_ID", new Integer(P_Group_Security_ID));
}
/** Get Security groups ans policies.
@return Security groups ans policies */
public int getP_Group_Security_ID() 
{
Integer ii = (Integer)get_Value("P_Group_Security_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Security.
@param P_Security_ID Security */
public void setP_Security_ID (int P_Security_ID)
{
if (P_Security_ID < 1) throw new IllegalArgumentException ("P_Security_ID is mandatory.");
set_ValueNoCheck ("P_Security_ID", new Integer(P_Security_ID));
}
/** Get Security.
@return Security */
public int getP_Security_ID() 
{
Integer ii = (Integer)get_Value("P_Security_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
