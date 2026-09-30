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
/** Generated Model for P_Absence_Status_Histo
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Absence_Status_Histo extends PO
{
/** Standard Constructor
@param ctx context
@param P_Absence_Status_Histo_ID id
@param trxName transaction
*/
public X_P_Absence_Status_Histo (Properties ctx, int P_Absence_Status_Histo_ID, String trxName)
{
super (ctx, P_Absence_Status_Histo_ID, trxName);
/** if (P_Absence_Status_Histo_ID == 0)
{
setP_Absence_ID (0);
setP_Absence_Status_Histo_ID (0);
setP_Absence_Status_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Absence_Status_Histo (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100778 */
public static final int Table_ID=2100778;

/** TableName=P_Absence_Status_Histo */
public static final String Table_Name="P_Absence_Status_Histo";

protected static KeyNamePair Model = new KeyNamePair(2100778,"P_Absence_Status_Histo");

protected BigDecimal accessLevel = new BigDecimal(4);
/** AccessLevel
@return 4 - System 
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
StringBuffer sb = new StringBuffer ("X_P_Absence_Status_Histo[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Absence.
@param P_Absence_ID Absence */
public void setP_Absence_ID (int P_Absence_ID)
{
if (P_Absence_ID < 1) throw new IllegalArgumentException ("P_Absence_ID is mandatory.");
set_Value ("P_Absence_ID", new Integer(P_Absence_ID));
}
/** Get Absence.
@return Absence */
public int getP_Absence_ID() 
{
Integer ii = (Integer)get_Value("P_Absence_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_Absence_Status_Histo_ID.
@param P_Absence_Status_Histo_ID P_Absence_Status_Histo_ID */
public void setP_Absence_Status_Histo_ID (int P_Absence_Status_Histo_ID)
{
if (P_Absence_Status_Histo_ID < 1) throw new IllegalArgumentException ("P_Absence_Status_Histo_ID is mandatory.");
set_ValueNoCheck ("P_Absence_Status_Histo_ID", new Integer(P_Absence_Status_Histo_ID));
}
/** Get P_Absence_Status_Histo_ID.
@return P_Absence_Status_Histo_ID */
public int getP_Absence_Status_Histo_ID() 
{
Integer ii = (Integer)get_Value("P_Absence_Status_Histo_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_Absence_Status_ID.
@param P_Absence_Status_ID P_Absence_Status_ID */
public void setP_Absence_Status_ID (int P_Absence_Status_ID)
{
if (P_Absence_Status_ID < 1) throw new IllegalArgumentException ("P_Absence_Status_ID is mandatory.");
set_Value ("P_Absence_Status_ID", new Integer(P_Absence_Status_ID));
}
/** Get P_Absence_Status_ID.
@return P_Absence_Status_ID */
public int getP_Absence_Status_ID() 
{
Integer ii = (Integer)get_Value("P_Absence_Status_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
