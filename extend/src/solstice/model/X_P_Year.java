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
/** Generated Model for P_Year
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Year extends PO
{
/** Standard Constructor
@param ctx context
@param P_Year_ID id
@param trxName transaction
*/
public X_P_Year (Properties ctx, int P_Year_ID, String trxName)
{
super (ctx, P_Year_ID, trxName);
/** if (P_Year_ID == 0)
{
setP_Calendar_ID (0);
setP_Year_ID (0);
setProcessing (false);	// N
setYear (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Year (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000019 */
public static final int Table_ID=2000019;

/** TableName=P_Year */
public static final String Table_Name="P_Year";

protected static KeyNamePair Model = new KeyNamePair(2000019,"P_Year");

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
StringBuffer sb = new StringBuffer ("X_P_Year[").append(get_ID()).append("]");
return sb.toString();
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
/** Set Default.
@param IsDefault Default value */
public void setIsDefault (boolean IsDefault)
{
set_Value ("IsDefault", new Boolean(IsDefault));
}
/** Get Default.
@return Default value */
public boolean isDefault() 
{
Object oo = get_Value("IsDefault");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Calendar.
@param P_Calendar_ID Payroll Calendar Name  */
public void setP_Calendar_ID (int P_Calendar_ID)
{
if (P_Calendar_ID < 1) throw new IllegalArgumentException ("P_Calendar_ID is mandatory.");
set_ValueNoCheck ("P_Calendar_ID", new Integer(P_Calendar_ID));
}
/** Get Calendar.
@return Payroll Calendar Name  */
public int getP_Calendar_ID() 
{
Integer ii = (Integer)get_Value("P_Calendar_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Year.
@param P_Year_ID Calendar Year */
public void setP_Year_ID (int P_Year_ID)
{
if (P_Year_ID < 1) throw new IllegalArgumentException ("P_Year_ID is mandatory.");
set_ValueNoCheck ("P_Year_ID", new Integer(P_Year_ID));
}
/** Get Year.
@return Calendar Year */
public int getP_Year_ID() 
{
Integer ii = (Integer)get_Value("P_Year_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Process Now.
@param Processing Process Now */
public void setProcessing (boolean Processing)
{
set_Value ("Processing", new Boolean(Processing));
}
/** Get Process Now.
@return Process Now */
public boolean isProcessing() 
{
Object oo = get_Value("Processing");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Year.
@param Year Calendar Year */
public void setYear (int Year)
{
set_Value ("Year", new Integer(Year));
}
/** Get Year.
@return Calendar Year */
public int getYear() 
{
Integer ii = (Integer)get_Value("Year");
if (ii == null) return 0;
return ii.intValue();
}
/** Get Record ID/ColumnName
@return ID/ColumnName pair */
public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), String.valueOf(getYear()));
}
}
