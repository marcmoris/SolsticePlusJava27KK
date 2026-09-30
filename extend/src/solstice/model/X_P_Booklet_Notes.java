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
/** Generated Model for P_Booklet_Notes
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Booklet_Notes extends PO
{
/** Standard Constructor
@param ctx context
@param P_Booklet_Notes_ID id
@param trxName transaction
*/
public X_P_Booklet_Notes (Properties ctx, int P_Booklet_Notes_ID, String trxName)
{
super (ctx, P_Booklet_Notes_ID, trxName);
/** if (P_Booklet_Notes_ID == 0)
{
setNotes (null);
setP_Booklet_ID (0);
setP_Booklet_Notes_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Booklet_Notes (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100763 */
public static final int Table_ID=2100763;

/** TableName=P_Booklet_Notes */
public static final String Table_Name="P_Booklet_Notes";

protected static KeyNamePair Model = new KeyNamePair(2100763,"P_Booklet_Notes");

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
StringBuffer sb = new StringBuffer ("X_P_Booklet_Notes[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Notes.
@param Notes Notes */
public void setNotes (String Notes)
{
if (Notes == null) throw new IllegalArgumentException ("Notes is mandatory.");
if (Notes.length() > 255)
{
log.warning("Length > 255 - truncated");
Notes = Notes.substring(0,254);
}
set_Value ("Notes", Notes);
}
/** Get Notes.
@return Notes */
public String getNotes() 
{
return (String)get_Value("Notes");
}
/** Set Booklet.
@param P_Booklet_ID Booklet */
public void setP_Booklet_ID (int P_Booklet_ID)
{
if (P_Booklet_ID < 1) throw new IllegalArgumentException ("P_Booklet_ID is mandatory.");
set_Value ("P_Booklet_ID", new Integer(P_Booklet_ID));
}
/** Get Booklet.
@return Booklet */
public int getP_Booklet_ID() 
{
Integer ii = (Integer)get_Value("P_Booklet_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_Booklet_Notes_ID.
@param P_Booklet_Notes_ID P_Booklet_Notes_ID */
public void setP_Booklet_Notes_ID (int P_Booklet_Notes_ID)
{
if (P_Booklet_Notes_ID < 1) throw new IllegalArgumentException ("P_Booklet_Notes_ID is mandatory.");
set_ValueNoCheck ("P_Booklet_Notes_ID", new Integer(P_Booklet_Notes_ID));
}
/** Get P_Booklet_Notes_ID.
@return P_Booklet_Notes_ID */
public int getP_Booklet_Notes_ID() 
{
Integer ii = (Integer)get_Value("P_Booklet_Notes_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
