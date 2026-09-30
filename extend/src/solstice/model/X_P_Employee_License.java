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
/** Generated Model for P_Employee_License
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.4 2008/03/12 20:02:49 marmor01 Exp $ */
public class X_P_Employee_License extends PO
{
/** Standard Constructor
@param ctx context
@param P_Employee_License_ID id
@param trxName transaction
*/
public X_P_Employee_License (Properties ctx, int P_Employee_License_ID, String trxName)
{
super (ctx, P_Employee_License_ID, trxName);
/** if (P_Employee_License_ID == 0)
{
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Employee_License (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100895 */
public static final int Table_ID=2100895;

/** TableName=P_Employee_License */
public static final String Table_Name="P_Employee_License";

protected static KeyNamePair Model = new KeyNamePair(2100895,"P_Employee_License");

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
StringBuffer sb = new StringBuffer ("X_P_Employee_License[").append(get_ID()).append("]");
return sb.toString();
}
/** Set End Date.
@param EndDate Last effective date (inclusive) */
public void setEndDate (Timestamp EndDate)
{
set_Value ("EndDate", EndDate);
}
/** Get End Date.
@return Last effective date (inclusive) */
public Timestamp getEndDate() 
{
return (Timestamp)get_Value("EndDate");
}
/** Set License Type.
@param LicenseType License Type */
public void setLicenseType (String LicenseType)
{
if (LicenseType != null && LicenseType.length() > 60)
{
log.warning("Length > 60 - truncated");
LicenseType = LicenseType.substring(0,59);
}
set_Value ("LicenseType", LicenseType);
}
/** Get License Type.
@return License Type */
public String getLicenseType() 
{
return (String)get_Value("LicenseType");
}
/** Set Employee .
@param P_Employee_ID Employee */
public void setP_Employee_ID (int P_Employee_ID)
{
if (P_Employee_ID <= 0) set_Value ("P_Employee_ID", null);
 else 
set_Value ("P_Employee_ID", new Integer(P_Employee_ID));
}
/** Get Employee .
@return Employee */
public int getP_Employee_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_Employee_License_ID.
@param P_Employee_License_ID P_Employee_License_ID */
public void setP_Employee_License_ID (int P_Employee_License_ID)
{
if (P_Employee_License_ID <= 0) set_ValueNoCheck ("P_Employee_License_ID", null);
 else 
set_ValueNoCheck ("P_Employee_License_ID", new Integer(P_Employee_License_ID));
}
/** Get P_Employee_License_ID.
@return P_Employee_License_ID */
public int getP_Employee_License_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_License_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Start Date.
@param StartDate First effective day (inclusive) */
public void setStartDate (Timestamp StartDate)
{
set_Value ("StartDate", StartDate);
}
/** Get Start Date.
@return First effective day (inclusive) */
public Timestamp getStartDate() 
{
return (Timestamp)get_Value("StartDate");
}
}
