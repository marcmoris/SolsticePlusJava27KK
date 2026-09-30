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
/** Generated Model for P_Bank
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.1 2007/07/18 14:43:46 marmor01 Exp $ */
public class X_P_Bank extends PO
{
/** Standard Constructor
@param ctx context
@param P_Bank_ID id
@param trxName transaction
*/
public X_P_Bank (Properties ctx, int P_Bank_ID, String trxName)
{
super (ctx, P_Bank_ID, trxName);
/** if (P_Bank_ID == 0)
{
setName (null);
setP_Bank_ID (0);
setRoutingNo (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Bank (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000041 */
public static final int Table_ID=2000041;

/** TableName=P_Bank */
public static final String Table_Name="P_Bank";

protected static KeyNamePair Model = new KeyNamePair(2000041,"P_Bank");

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
StringBuffer sb = new StringBuffer ("X_P_Bank[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Address.
@param C_Location_ID Location or Address */
public void setC_Location_ID (int C_Location_ID)
{
if (C_Location_ID <= 0) set_Value ("C_Location_ID", null);
 else 
set_Value ("C_Location_ID", new Integer(C_Location_ID));
}
/** Get Address.
@return Location or Address */
public int getC_Location_ID() 
{
Integer ii = (Integer)get_Value("C_Location_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set File Name.
@param FileName Name of the local file or URL */
public void setFileName (String FileName)
{
if (FileName != null && FileName.length() > 30)
{
log.warning("Length > 30 - truncated");
FileName = FileName.substring(0,29);
}
set_Value ("FileName", FileName);
}
/** Get File Name.
@return Name of the local file or URL */
public String getFileName() 
{
return (String)get_Value("FileName");
}
/** Set Name .
@param Name Alphanumeric identifier of the entity */
public void setName (String Name)
{
if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
if (Name.length() > 120)
{
log.warning("Length > 120 - truncated");
Name = Name.substring(0,119);
}
set_Value ("Name", Name);
}
/** Get Name .
@return Alphanumeric identifier of the entity */
public String getName() 
{
return (String)get_Value("Name");
}
/** Get Record ID/ColumnName
@return ID/ColumnName pair */
public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), getName());
}
/** Set No Usager.
@param NoUsager No Usager */
public void setNoUsager (String NoUsager)
{
if (NoUsager != null && NoUsager.length() > 30)
{
log.warning("Length > 30 - truncated");
NoUsager = NoUsager.substring(0,29);
}
set_Value ("NoUsager", NoUsager);
}
/** Get No Usager.
@return No Usager */
public String getNoUsager() 
{
return (String)get_Value("NoUsager");
}
/** Set Bank.
@param P_Bank_ID   */
public void setP_Bank_ID (int P_Bank_ID)
{
if (P_Bank_ID < 1) throw new IllegalArgumentException ("P_Bank_ID is mandatory.");
set_Value ("P_Bank_ID", new Integer(P_Bank_ID));
}
/** Get Bank.
@return   */
public int getP_Bank_ID() 
{
Integer ii = (Integer)get_Value("P_Bank_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Routing No.
@param RoutingNo Bank Routing Number */
public void setRoutingNo (String RoutingNo)
{
if (RoutingNo == null) throw new IllegalArgumentException ("RoutingNo is mandatory.");
if (RoutingNo.length() > 40)
{
log.warning("Length > 40 - truncated");
RoutingNo = RoutingNo.substring(0,39);
}
set_Value ("RoutingNo", RoutingNo);
}
/** Get Routing No.
@return Bank Routing Number */
public String getRoutingNo() 
{
return (String)get_Value("RoutingNo");
}
/** Set Swift code.
@param SwiftCode Swift Code or BIC */
public void setSwiftCode (String SwiftCode)
{
if (SwiftCode != null && SwiftCode.length() > 40)
{
log.warning("Length > 40 - truncated");
SwiftCode = SwiftCode.substring(0,39);
}
set_Value ("SwiftCode", SwiftCode);
}
/** Get Swift code.
@return Swift Code or BIC */
public String getSwiftCode() 
{
return (String)get_Value("SwiftCode");
}
}
