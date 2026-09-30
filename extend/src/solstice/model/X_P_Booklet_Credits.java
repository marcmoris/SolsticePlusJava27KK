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
/** Generated Model for P_Booklet_Credits
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Booklet_Credits extends PO
{
/** Standard Constructor
@param ctx context
@param P_Booklet_Credits_ID id
@param trxName transaction
*/
public X_P_Booklet_Credits (Properties ctx, int P_Booklet_Credits_ID, String trxName)
{
super (ctx, P_Booklet_Credits_ID, trxName);
/** if (P_Booklet_Credits_ID == 0)
{
setP_Booklet_Credits_ID (0);
setP_Booklet_ID (0);
setP_Credits_ID (0);
setP_Period_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Booklet_Credits (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100737 */
public static final int Table_ID=2100737;

/** TableName=P_Booklet_Credits */
public static final String Table_Name="P_Booklet_Credits";

protected static KeyNamePair Model = new KeyNamePair(2100737,"P_Booklet_Credits");

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
StringBuffer sb = new StringBuffer ("X_P_Booklet_Credits[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Booklet_Credits.
@param P_Booklet_Credits_ID Booklet_Credits */
public void setP_Booklet_Credits_ID (int P_Booklet_Credits_ID)
{
if (P_Booklet_Credits_ID < 1) throw new IllegalArgumentException ("P_Booklet_Credits_ID is mandatory.");
set_ValueNoCheck ("P_Booklet_Credits_ID", new Integer(P_Booklet_Credits_ID));
}
/** Get Booklet_Credits.
@return Booklet_Credits */
public int getP_Booklet_Credits_ID() 
{
Integer ii = (Integer)get_Value("P_Booklet_Credits_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Booklet.
@param P_Booklet_ID Booklet */
public void setP_Booklet_ID (int P_Booklet_ID)
{
if (P_Booklet_ID < 1) throw new IllegalArgumentException ("P_Booklet_ID is mandatory.");
set_ValueNoCheck ("P_Booklet_ID", new Integer(P_Booklet_ID));
}
/** Get Booklet.
@return Booklet */
public int getP_Booklet_ID() 
{
Integer ii = (Integer)get_Value("P_Booklet_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Credits_ID AD_Reference_ID=2100318 */
public static final int P_CREDITS_ID_AD_Reference_ID=2100318;
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
/** Set Period.
@param P_Period_ID Period of the Calendar   */
public void setP_Period_ID (int P_Period_ID)
{
if (P_Period_ID < 1) throw new IllegalArgumentException ("P_Period_ID is mandatory.");
set_ValueNoCheck ("P_Period_ID", new Integer(P_Period_ID));
}
/** Get Period.
@return Period of the Calendar   */
public int getP_Period_ID() 
{
Integer ii = (Integer)get_Value("P_Period_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Quantity.
@param Quantity Quantity */
public void setQuantity (BigDecimal Quantity)
{
set_Value ("Quantity", Quantity);
}
/** Get Quantity.
@return Quantity */
public BigDecimal getQuantity() 
{
BigDecimal bd = (BigDecimal)get_Value("Quantity");
if (bd == null) return Env.ZERO;
return bd;
}
}
