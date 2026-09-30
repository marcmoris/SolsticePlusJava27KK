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
/** Generated Model for P_Augmentation_Gain
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Augmentation_Gain extends PO
{
/** Standard Constructor
@param ctx context
@param P_Augmentation_Gain_ID id
@param trxName transaction
*/
public X_P_Augmentation_Gain (Properties ctx, int P_Augmentation_Gain_ID, String trxName)
{
super (ctx, P_Augmentation_Gain_ID, trxName);
/** if (P_Augmentation_Gain_ID == 0)
{
setP_Augmentation_Gain_ID (0);
setP_Augmentation_ID (0);
setP_Gain_Current_ID (0);
setP_Gain_Old_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Augmentation_Gain (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100791 */
public static final int Table_ID=2100791;

/** TableName=P_Augmentation_Gain */
public static final String Table_Name="P_Augmentation_Gain";

protected static KeyNamePair Model = new KeyNamePair(2100791,"P_Augmentation_Gain");

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
StringBuffer sb = new StringBuffer ("X_P_Augmentation_Gain[").append(get_ID()).append("]");
return sb.toString();
}
/** Set P_Augmentation_Gain_ID.
@param P_Augmentation_Gain_ID P_Augmentation_Gain_ID */
public void setP_Augmentation_Gain_ID (int P_Augmentation_Gain_ID)
{
if (P_Augmentation_Gain_ID < 1) throw new IllegalArgumentException ("P_Augmentation_Gain_ID is mandatory.");
set_ValueNoCheck ("P_Augmentation_Gain_ID", new Integer(P_Augmentation_Gain_ID));
}
/** Get P_Augmentation_Gain_ID.
@return P_Augmentation_Gain_ID */
public int getP_Augmentation_Gain_ID() 
{
Integer ii = (Integer)get_Value("P_Augmentation_Gain_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Retroactivity.
@param P_Augmentation_ID Retroactivity */
public void setP_Augmentation_ID (int P_Augmentation_ID)
{
if (P_Augmentation_ID < 1) throw new IllegalArgumentException ("P_Augmentation_ID is mandatory.");
set_Value ("P_Augmentation_ID", new Integer(P_Augmentation_ID));
}
/** Get Retroactivity.
@return Retroactivity */
public int getP_Augmentation_ID() 
{
Integer ii = (Integer)get_Value("P_Augmentation_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Gain_Current_ID AD_Reference_ID=2100317 */
public static final int P_GAIN_CURRENT_ID_AD_Reference_ID=2100317;
/** Set Current gain.
@param P_Gain_Current_ID Current gain */
public void setP_Gain_Current_ID (int P_Gain_Current_ID)
{
if (P_Gain_Current_ID < 1) throw new IllegalArgumentException ("P_Gain_Current_ID is mandatory.");
set_Value ("P_Gain_Current_ID", new Integer(P_Gain_Current_ID));
}
/** Get Current gain.
@return Current gain */
public int getP_Gain_Current_ID() 
{
Integer ii = (Integer)get_Value("P_Gain_Current_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Gain_Old_ID AD_Reference_ID=2100317 */
public static final int P_GAIN_OLD_ID_AD_Reference_ID=2100317;
/** Set Last year gain.
@param P_Gain_Old_ID Last year gain */
public void setP_Gain_Old_ID (int P_Gain_Old_ID)
{
if (P_Gain_Old_ID < 1) throw new IllegalArgumentException ("P_Gain_Old_ID is mandatory.");
set_Value ("P_Gain_Old_ID", new Integer(P_Gain_Old_ID));
}
/** Get Last year gain.
@return Last year gain */
public int getP_Gain_Old_ID() 
{
Integer ii = (Integer)get_Value("P_Gain_Old_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
