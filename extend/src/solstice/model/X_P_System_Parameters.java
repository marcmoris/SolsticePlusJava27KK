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
/** Generated Model for P_System_Parameters
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_System_Parameters extends PO
{
/** Standard Constructor
@param ctx context
@param P_System_Parameters_ID id
@param trxName transaction
*/
public X_P_System_Parameters (Properties ctx, int P_System_Parameters_ID, String trxName)
{
super (ctx, P_System_Parameters_ID, trxName);
/** if (P_System_Parameters_ID == 0)
{
setP_NoProductive_Gain_ID (0);
setP_Productive_Gain_ID (0);
setP_System_Parameters_ID (0);
setTransfertPath (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_System_Parameters (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100667 */
public static final int Table_ID=2100667;

/** TableName=P_System_Parameters */
public static final String Table_Name="P_System_Parameters";

protected static KeyNamePair Model = new KeyNamePair(2100667,"P_System_Parameters");

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
StringBuffer sb = new StringBuffer ("X_P_System_Parameters[").append(get_ID()).append("]");
return sb.toString();
}

/** P_NoProductive_Gain_ID AD_Reference_ID=2100317 */
public static final int P_NOPRODUCTIVE_GAIN_ID_AD_Reference_ID=2100317;
/** Set No Productive Gain.
@param P_NoProductive_Gain_ID No Productive Gain */
public void setP_NoProductive_Gain_ID (int P_NoProductive_Gain_ID)
{
if (P_NoProductive_Gain_ID < 1) throw new IllegalArgumentException ("P_NoProductive_Gain_ID is mandatory.");
set_Value ("P_NoProductive_Gain_ID", new Integer(P_NoProductive_Gain_ID));
}
/** Get No Productive Gain.
@return No Productive Gain */
public int getP_NoProductive_Gain_ID() 
{
Integer ii = (Integer)get_Value("P_NoProductive_Gain_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Productive_Gain_ID AD_Reference_ID=2100317 */
public static final int P_PRODUCTIVE_GAIN_ID_AD_Reference_ID=2100317;
/** Set Productive Gain.
@param P_Productive_Gain_ID Productive Gain */
public void setP_Productive_Gain_ID (int P_Productive_Gain_ID)
{
if (P_Productive_Gain_ID < 1) throw new IllegalArgumentException ("P_Productive_Gain_ID is mandatory.");
set_Value ("P_Productive_Gain_ID", new Integer(P_Productive_Gain_ID));
}
/** Get Productive Gain.
@return Productive Gain */
public int getP_Productive_Gain_ID() 
{
Integer ii = (Integer)get_Value("P_Productive_Gain_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_System_Parameters_ID.
@param P_System_Parameters_ID P_System_Parameters_ID */
public void setP_System_Parameters_ID (int P_System_Parameters_ID)
{
if (P_System_Parameters_ID < 1) throw new IllegalArgumentException ("P_System_Parameters_ID is mandatory.");
set_Value ("P_System_Parameters_ID", new Integer(P_System_Parameters_ID));
}
/** Get P_System_Parameters_ID.
@return P_System_Parameters_ID */
public int getP_System_Parameters_ID() 
{
Integer ii = (Integer)get_Value("P_System_Parameters_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Report Path.
@param ReportPath Report Path */
public void setReportPath (String ReportPath)
{
if (ReportPath != null && ReportPath.length() > 150)
{
log.warning("Length > 150 - truncated");
ReportPath = ReportPath.substring(0,149);
}
set_Value ("ReportPath", ReportPath);
}
/** Get Report Path.
@return Report Path */
public String getReportPath() 
{
return (String)get_Value("ReportPath");
}
/** Set Report Server.
@param ReportServer Report Server */
public void setReportServer (String ReportServer)
{
if (ReportServer != null && ReportServer.length() > 50)
{
log.warning("Length > 50 - truncated");
ReportServer = ReportServer.substring(0,49);
}
set_Value ("ReportServer", ReportServer);
}
/** Get Report Server.
@return Report Server */
public String getReportServer() 
{
return (String)get_Value("ReportServer");
}
/** Set Transfert Path .
@param TransfertPath Transfert Path  */
public void setTransfertPath (String TransfertPath)
{
if (TransfertPath == null) throw new IllegalArgumentException ("TransfertPath is mandatory.");
if (TransfertPath.length() > 120)
{
log.warning("Length > 120 - truncated");
TransfertPath = TransfertPath.substring(0,119);
}
set_Value ("TransfertPath", TransfertPath);
}
/** Get Transfert Path .
@return Transfert Path  */
public String getTransfertPath() 
{
return (String)get_Value("TransfertPath");
}
/** Set Web App URL.
@param WebAppURL Web App URL */
public void setWebAppURL (String WebAppURL)
{
if (WebAppURL != null && WebAppURL.length() > 100)
{
log.warning("Length > 100 - truncated");
WebAppURL = WebAppURL.substring(0,99);
}
set_Value ("WebAppURL", WebAppURL);
}
/** Get Web App URL.
@return Web App URL */
public String getWebAppURL() 
{
return (String)get_Value("WebAppURL");
}
/** Set XMLReportPath.
@param XMLReportPath XMLReportPath */
public void setXMLReportPath (String XMLReportPath)
{
if (XMLReportPath != null && XMLReportPath.length() > 50)
{
log.warning("Length > 50 - truncated");
XMLReportPath = XMLReportPath.substring(0,49);
}
set_Value ("XMLReportPath", XMLReportPath);
}
/** Get XMLReportPath.
@return XMLReportPath */
public String getXMLReportPath() 
{
return (String)get_Value("XMLReportPath");
}
}
