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
/** Generated Model for P_PrintFormat_Credits
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_PrintFormat_Credits extends PO
{
/** Standard Constructor
@param ctx context
@param P_PrintFormat_Credits_ID id
@param trxName transaction
*/
public X_P_PrintFormat_Credits (Properties ctx, int P_PrintFormat_Credits_ID, String trxName)
{
super (ctx, P_PrintFormat_Credits_ID, trxName);
/** if (P_PrintFormat_Credits_ID == 0)
{
setP_Credits_ID (0);
setP_PrintFormat_Credits_ID (0);
setP_PrintFormat_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_PrintFormat_Credits (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100772 */
public static final int Table_ID=2100772;

/** TableName=P_PrintFormat_Credits */
public static final String Table_Name="P_PrintFormat_Credits";

protected static KeyNamePair Model = new KeyNamePair(2100772,"P_PrintFormat_Credits");

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
StringBuffer sb = new StringBuffer ("X_P_PrintFormat_Credits[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Credits.
@param P_Credits_ID Credits */
public void setP_Credits_ID (int P_Credits_ID)
{
if (P_Credits_ID < 1) throw new IllegalArgumentException ("P_Credits_ID is mandatory.");
set_Value ("P_Credits_ID", new Integer(P_Credits_ID));
}
/** Get Credits.
@return Credits */
public int getP_Credits_ID() 
{
Integer ii = (Integer)get_Value("P_Credits_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_PrintFormat_Credits_ID.
@param P_PrintFormat_Credits_ID P_PrintFormat_Credits_ID */
public void setP_PrintFormat_Credits_ID (int P_PrintFormat_Credits_ID)
{
if (P_PrintFormat_Credits_ID < 1) throw new IllegalArgumentException ("P_PrintFormat_Credits_ID is mandatory.");
set_Value ("P_PrintFormat_Credits_ID", new Integer(P_PrintFormat_Credits_ID));
}
/** Get P_PrintFormat_Credits_ID.
@return P_PrintFormat_Credits_ID */
public int getP_PrintFormat_Credits_ID() 
{
Integer ii = (Integer)get_Value("P_PrintFormat_Credits_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_PrintFormat_ID.
@param P_PrintFormat_ID P_PrintFormat_ID */
public void setP_PrintFormat_ID (int P_PrintFormat_ID)
{
if (P_PrintFormat_ID < 1) throw new IllegalArgumentException ("P_PrintFormat_ID is mandatory.");
set_Value ("P_PrintFormat_ID", new Integer(P_PrintFormat_ID));
}
/** Get P_PrintFormat_ID.
@return P_PrintFormat_ID */
public int getP_PrintFormat_ID() 
{
Integer ii = (Integer)get_Value("P_PrintFormat_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
