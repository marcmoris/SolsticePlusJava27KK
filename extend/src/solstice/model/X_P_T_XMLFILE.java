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
/** Generated Model for P_T_XMLFILE
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_T_XMLFILE extends PO
{
/** Standard Constructor
@param ctx context
@param P_T_XMLFILE_ID id
@param trxName transaction
*/
public X_P_T_XMLFILE (Properties ctx, int P_T_XMLFILE_ID, String trxName)
{
super (ctx, P_T_XMLFILE_ID, trxName);
/** if (P_T_XMLFILE_ID == 0)
{
setAD_PInstance_ID (0);
setLine (0);
setP_T_XMLFILE_ID (0);
setTAGXML (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_T_XMLFILE (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100807 */
public static final int Table_ID=2100807;

/** TableName=P_T_XMLFILE */
public static final String Table_Name="P_T_XMLFILE";

protected static KeyNamePair Model = new KeyNamePair(2100807,"P_T_XMLFILE");

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
StringBuffer sb = new StringBuffer ("X_P_T_XMLFILE[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Process Instance.
@param AD_PInstance_ID Instance of the process */
public void setAD_PInstance_ID (int AD_PInstance_ID)
{
if (AD_PInstance_ID < 1) throw new IllegalArgumentException ("AD_PInstance_ID is mandatory.");
set_Value ("AD_PInstance_ID", new Integer(AD_PInstance_ID));
}
/** Get Process Instance.
@return Instance of the process */
public int getAD_PInstance_ID() 
{
Integer ii = (Integer)get_Value("AD_PInstance_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Line No.
@param Line Unique line for this document */
public void setLine (int Line)
{
set_Value ("Line", new Integer(Line));
}
/** Get Line No.
@return Unique line for this document */
public int getLine() 
{
Integer ii = (Integer)get_Value("Line");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_T_XMLFILE_ID.
@param P_T_XMLFILE_ID P_T_XMLFILE_ID */
public void setP_T_XMLFILE_ID (int P_T_XMLFILE_ID)
{
if (P_T_XMLFILE_ID < 1) throw new IllegalArgumentException ("P_T_XMLFILE_ID is mandatory.");
set_ValueNoCheck ("P_T_XMLFILE_ID", new Integer(P_T_XMLFILE_ID));
}
/** Get P_T_XMLFILE_ID.
@return P_T_XMLFILE_ID */
public int getP_T_XMLFILE_ID() 
{
Integer ii = (Integer)get_Value("P_T_XMLFILE_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set TAGXML.
@param TAGXML TAGXML */
public void setTAGXML (String TAGXML)
{
if (TAGXML == null) throw new IllegalArgumentException ("TAGXML is mandatory.");
if (TAGXML.length() > 100)
{
log.warning("Length > 100 - truncated");
TAGXML = TAGXML.substring(0,99);
}
set_Value ("TAGXML", TAGXML);
}
/** Get TAGXML.
@return TAGXML */
public String getTAGXML() 
{
return (String)get_Value("TAGXML");
}
}
