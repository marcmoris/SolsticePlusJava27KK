/******************************************************************************
* Product: Solstice+ Payroll & Human Resources Management                    *
* Copyright (C) 2008 ProGestion Informatique, Inc. All Rights Reserved.      *
* This program is free software, you can redistribute it and/or modify it    *
* under the terms version 2 of the GNU General Public License as published   *
* by the Free Software Foundation. This program is distributed in the hope   *
* that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
* warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
* See the GNU General Public License for more details.                       *
* You should have received a copy of the GNU General Public License along    *
* with this program, if not, write to the Free Software Foundation, Inc.,    *
* 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
* For the text or an alternative of this public license, you may reach us    *
* ProGestion Informatique, 210-5300 Bld des Galerie, Quebec, G2K 2A2 Canada  *
* or via info@progestion.net or http://www.progestion.net/license.html       *
******************************************************************************/
package solstice.model;

/** Generated Model - DO NOT CHANGE */
import org.compiere.model.*;
import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;
/** Generated Model for P_PayProcessorLog
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.2 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_PayProcessorLog extends PO
{
/** Standard Constructor
@param ctx context
@param P_PayProcessorLog_ID id
@param trxName transaction
*/
public X_P_PayProcessorLog (Properties ctx, int P_PayProcessorLog_ID, String trxName)
{
super (ctx, P_PayProcessorLog_ID, trxName);
/** if (P_PayProcessorLog_ID == 0)
{
setIsError (false);
setP_PayProcessor_ID (0);
setP_PayProcessorLog_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_PayProcessorLog (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=3101150 */
public static final int Table_ID=3101166;

/** TableName=P_PayProcessorLog */
public static final String Table_Name="P_PayProcessorLog";

protected static KeyNamePair Model = new KeyNamePair(3101166,"P_PayProcessorLog");

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
StringBuffer sb = new StringBuffer ("X_P_PayProcessorLog[").append(get_ID()).append("]");
return sb.toString();
}
/** Set BinaryData.
@param BinaryData Binary Data */
public void setBinaryData (byte[] BinaryData)
{
set_Value ("BinaryData", BinaryData);
}
/** Get BinaryData.
@return Binary Data */
public byte[] getBinaryData() 
{
return (byte[])get_Value("BinaryData");
}
/** Set Description.
@param Description Optional short description of the record */
public void setDescription (String Description)
{
if (Description != null && Description.length() > 255)
{
log.warning("Length > 255 - truncated");
Description = Description.substring(0,254);
}
set_Value ("Description", Description);
}
/** Get Description.
@return Optional short description of the record */
public String getDescription() 
{
return (String)get_Value("Description");
}
/** Set Error.
@param IsError An Error that has occurred in the execution */
public void setIsError (boolean IsError)
{
set_Value ("IsError", new Boolean(IsError));
}
/** Get Error.
@return An Error that has occurred in the execution */
public boolean isError() 
{
Object oo = get_Value("IsError");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set P_PayProcessor_ID.
@param P_PayProcessor_ID P_PayProcessor_ID */
public void setP_PayProcessor_ID (int P_PayProcessor_ID)
{
//if (P_PayProcessor_ID < 1) throw new IllegalArgumentException ("P_PayProcessor_ID is mandatory.");
set_Value ("P_PayProcessor_ID", new Integer(P_PayProcessor_ID));
}
/** Get P_PayProcessor_ID.
@return P_PayProcessor_ID */
public int getP_PayProcessor_ID() 
{
Integer ii = (Integer)get_Value("P_PayProcessor_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_PayProcessorLog_ID.
@param P_PayProcessorLog_ID P_PayProcessorLog_ID */
public void setP_PayProcessorLog_ID (int P_PayProcessorLog_ID)
{
if (P_PayProcessorLog_ID < 1) throw new IllegalArgumentException ("P_PayProcessorLog_ID is mandatory.");
set_ValueNoCheck ("P_PayProcessorLog_ID", new Integer(P_PayProcessorLog_ID));
}
/** Get P_PayProcessorLog_ID.
@return P_PayProcessorLog_ID */
public int getP_PayProcessorLog_ID() 
{
Integer ii = (Integer)get_Value("P_PayProcessorLog_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Reference.
@param Reference Reference for this record */
public void setReference (String Reference)
{
if (Reference != null && Reference.length() > 60)
{
log.warning("Length > 60 - truncated");
Reference = Reference.substring(0,59);
}
set_Value ("Reference", Reference);
}
/** Get Reference.
@return Reference for this record */
public String getReference() 
{
return (String)get_Value("Reference");
}
/** Set Summary.
@param Summary Textual summary of this request */
public void setSummary (String Summary)
{
if (Summary != null && Summary.length() > 2000)
{
log.warning("Length > 2000 - truncated");
Summary = Summary.substring(0,1999);
}
set_Value ("Summary", Summary);
}
/** Get Summary.
@return Textual summary of this request */
public String getSummary() 
{
return (String)get_Value("Summary");
}
/** Set Text Message.
@param TextMsg Text Message */
public void setTextMsg (String TextMsg)
{
if (TextMsg != null && TextMsg.length() > 2000)
{
log.warning("Length > 2000 - truncated");
TextMsg = TextMsg.substring(0,1999);
}
set_Value ("TextMsg", TextMsg);
}
/** Get Text Message.
@return Text Message */
public String getTextMsg() 
{
return (String)get_Value("TextMsg");
}
}
