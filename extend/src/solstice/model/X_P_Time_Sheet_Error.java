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
/** Generated Model for P_Time_Sheet_Error
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Time_Sheet_Error extends PO
{
/** Standard Constructor
@param ctx context
@param P_Time_Sheet_Error_ID id
@param trxName transaction
*/
public X_P_Time_Sheet_Error (Properties ctx, int P_Time_Sheet_Error_ID, String trxName)
{
super (ctx, P_Time_Sheet_Error_ID, trxName);
/** if (P_Time_Sheet_Error_ID == 0)
{
setAD_Message_ID (0);
setAlert_Severity_Level (null);	// E
setP_Time_Sheet_Error_ID (0);
setP_Time_Sheet_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Time_Sheet_Error (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000159 */
public static final int Table_ID=2000159;

/** TableName=P_Time_Sheet_Error */
public static final String Table_Name="P_Time_Sheet_Error";

protected static KeyNamePair Model = new KeyNamePair(2000159,"P_Time_Sheet_Error");

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
StringBuffer sb = new StringBuffer ("X_P_Time_Sheet_Error[").append(get_ID()).append("]");
return sb.toString();
}

/** AD_Message_ID AD_Reference_ID=102 */
public static final int AD_MESSAGE_ID_AD_Reference_ID=102;
/** Set Message.
@param AD_Message_ID System Message */
public void setAD_Message_ID (int AD_Message_ID)
{
if (AD_Message_ID < 1) throw new IllegalArgumentException ("AD_Message_ID is mandatory.");
set_Value ("AD_Message_ID", new Integer(AD_Message_ID));
}
/** Get Message.
@return System Message */
public int getAD_Message_ID() 
{
Integer ii = (Integer)get_Value("AD_Message_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Message.
@param Alert_Message Message */
public void setAlert_Message (String Alert_Message)
{
if (Alert_Message != null && Alert_Message.length() > 2000)
{
log.warning("Length > 2000 - truncated");
Alert_Message = Alert_Message.substring(0,1999);
}
set_Value ("Alert_Message", Alert_Message);
}
/** Get Message.
@return Message */
public String getAlert_Message() 
{
return (String)get_Value("Alert_Message");
}

/** Alert_Severity_Level AD_Reference_ID=2100348 */
public static final int ALERT_SEVERITY_LEVEL_AD_Reference_ID=2100348;
/** Error = E */
public static final String ALERT_SEVERITY_LEVEL_Error = "E";
/** Information = I */
public static final String ALERT_SEVERITY_LEVEL_Information = "I";
/** Warning = W */
public static final String ALERT_SEVERITY_LEVEL_Warning = "W";
/** Chain = C */
public static final String ALERT_SEVERITY_LEVEL_Chain = "C";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isAlert_Severity_LevelValid (String test)
{
return test.equals("E") || test.equals("I") || test.equals("W") || test.equals("C");
}
/** Set Severity Level.
@param Alert_Severity_Level Severity Level */
public void setAlert_Severity_Level (String Alert_Severity_Level)
{
if (Alert_Severity_Level == null) throw new IllegalArgumentException ("Alert_Severity_Level is mandatory");
if (!isAlert_Severity_LevelValid(Alert_Severity_Level))
throw new IllegalArgumentException ("Alert_Severity_Level Invalid value - " + Alert_Severity_Level + " - Reference_ID=2100348 - E - I - W - C");
if (Alert_Severity_Level.length() > 1)
{
log.warning("Length > 1 - truncated");
Alert_Severity_Level = Alert_Severity_Level.substring(0,0);
}
set_Value ("Alert_Severity_Level", Alert_Severity_Level);
}
/** Get Severity Level.
@return Severity Level */
public String getAlert_Severity_Level() 
{
return (String)get_Value("Alert_Severity_Level");
}

/** P_Credits_ID AD_Reference_ID=2100318 */
public static final int P_CREDITS_ID_AD_Reference_ID=2100318;
/** Set Credits.
@param P_Credits_ID Credits */
public void setP_Credits_ID (int P_Credits_ID)
{
if (P_Credits_ID <= 0) set_Value ("P_Credits_ID", null);
 else 
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
/** Set Feuille Detail.
@param P_Time_Sheet_Detail_ID   */
public void setP_Time_Sheet_Detail_ID (int P_Time_Sheet_Detail_ID)
{
if (P_Time_Sheet_Detail_ID <= 0) set_Value ("P_Time_Sheet_Detail_ID", null);
 else 
set_Value ("P_Time_Sheet_Detail_ID", new Integer(P_Time_Sheet_Detail_ID));
}
/** Get Feuille Detail.
@return   */
public int getP_Time_Sheet_Detail_ID() 
{
Integer ii = (Integer)get_Value("P_Time_Sheet_Detail_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_Time_Sheet_Error_ID.
@param P_Time_Sheet_Error_ID P_Time_Sheet_Error_ID */
public void setP_Time_Sheet_Error_ID (int P_Time_Sheet_Error_ID)
{
if (P_Time_Sheet_Error_ID < 1) throw new IllegalArgumentException ("P_Time_Sheet_Error_ID is mandatory.");
set_Value ("P_Time_Sheet_Error_ID", new Integer(P_Time_Sheet_Error_ID));
}
/** Get P_Time_Sheet_Error_ID.
@return P_Time_Sheet_Error_ID */
public int getP_Time_Sheet_Error_ID() 
{
Integer ii = (Integer)get_Value("P_Time_Sheet_Error_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Time Sheet No..
@param P_Time_Sheet_ID   */
public void setP_Time_Sheet_ID (int P_Time_Sheet_ID)
{
if (P_Time_Sheet_ID < 1) throw new IllegalArgumentException ("P_Time_Sheet_ID is mandatory.");
set_Value ("P_Time_Sheet_ID", new Integer(P_Time_Sheet_ID));
}
/** Get Time Sheet No..
@return   */
public int getP_Time_Sheet_ID() 
{
Integer ii = (Integer)get_Value("P_Time_Sheet_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
