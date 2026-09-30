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
/** Generated Model for P_Employee_RWT
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Employee_RWT extends PO
{
/** Standard Constructor
@param ctx context
@param P_Employee_RWT_ID id
@param trxName transaction
*/
public X_P_Employee_RWT (Properties ctx, int P_Employee_RWT_ID, String trxName)
{
super (ctx, P_Employee_RWT_ID, trxName);
/** if (P_Employee_RWT_ID == 0)
{
setisWorkingShortTime (false);	// N
setP_Employee_ID (0);
setP_Gain_ID (0);	// @SQL=SELECT P_Gain_ID AS DefaultValue FROM P_Gain WHERE IsRWT = 'Y'
setStartDate (new Timestamp(System.currentTimeMillis()));
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Employee_RWT (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100917 */
public static final int Table_ID=2100917;

/** TableName=P_Employee_RWT */
public static final String Table_Name="P_Employee_RWT";

protected static KeyNamePair Model = new KeyNamePair(2100917,"P_Employee_RWT");

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
StringBuffer sb = new StringBuffer ("X_P_Employee_RWT[").append(get_ID()).append("]");
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
/** Set ORG.
@param isWorkingShortTime ORG */
public void setisWorkingShortTime (boolean isWorkingShortTime)
{
set_Value ("isWorkingShortTime", new Boolean(isWorkingShortTime));
}
/** Get ORG.
@return ORG */
public boolean isWorkingShortTime() 
{
Object oo = get_Value("isWorkingShortTime");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}

/** P_Employee_ID AD_Reference_ID=2000010 */
public static final int P_EMPLOYEE_ID_AD_Reference_ID=2000010;
/** Set Employee.
@param P_Employee_ID Employee */
public void setP_Employee_ID (int P_Employee_ID)
{
if (P_Employee_ID < 1) throw new IllegalArgumentException ("P_Employee_ID is mandatory.");
set_Value ("P_Employee_ID", new Integer(P_Employee_ID));
}
/** Get Employee.
@return Employee */
public int getP_Employee_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Get Record ID/ColumnName
@return ID/ColumnName pair */
public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), String.valueOf(getP_Employee_ID()));
}
/** Set GROUP_NAME.
@param P_Employee_RWT_ID GROUP_NAME */
public void setP_Employee_RWT_ID (int P_Employee_RWT_ID)
{
if (P_Employee_RWT_ID <= 0) set_ValueNoCheck ("P_Employee_RWT_ID", null);
 else 
set_ValueNoCheck ("P_Employee_RWT_ID", new Integer(P_Employee_RWT_ID));
}
/** Get GROUP_NAME.
@return GROUP_NAME */
public int getP_Employee_RWT_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_RWT_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Gain_ID AD_Reference_ID=2100317 */
public static final int P_GAIN_ID_AD_Reference_ID=2100317;
/** Set Earnings.
@param P_Gain_ID Earnings */
public void setP_Gain_ID (int P_Gain_ID)
{
if (P_Gain_ID < 1) throw new IllegalArgumentException ("P_Gain_ID is mandatory.");
set_Value ("P_Gain_ID", new Integer(P_Gain_ID));
}
/** Get Earnings.
@return Earnings */
public int getP_Gain_ID() 
{
Integer ii = (Integer)get_Value("P_Gain_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set View Column.
@param RWTDetail Select column in View */
public void setRWTDetail (String RWTDetail)
{
if (RWTDetail != null && RWTDetail.length() > 1)
{
log.warning("Length > 1 - truncated");
RWTDetail = RWTDetail.substring(0,0);
}
set_Value ("RWTDetail", RWTDetail);
}
/** Get View Column.
@return Select column in View */
public String getRWTDetail() 
{
return (String)get_Value("RWTDetail");
}
/** Set Start Date.
@param StartDate First effective day (inclusive) */
public void setStartDate (Timestamp StartDate)
{
if (StartDate == null) throw new IllegalArgumentException ("StartDate is mandatory.");
set_Value ("StartDate", StartDate);
}
/** Get Start Date.
@return First effective day (inclusive) */
public Timestamp getStartDate() 
{
return (Timestamp)get_Value("StartDate");
}
}
