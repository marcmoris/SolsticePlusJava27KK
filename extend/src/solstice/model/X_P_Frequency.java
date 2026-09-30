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
/** Generated Model for P_Frequency
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Frequency extends PO
{
/** Standard Constructor
@param ctx context
@param P_Frequency_ID id
@param trxName transaction
*/
public X_P_Frequency (Properties ctx, int P_Frequency_ID, String trxName)
{
super (ctx, P_Frequency_ID, trxName);
/** if (P_Frequency_ID == 0)
{
setName (null);
setNumberOfPeriod (0);
setP_Frequency_ID (0);
setPayPeriodType (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Frequency (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000002 */
public static final int Table_ID=2000002;

/** TableName=P_Frequency */
public static final String Table_Name="P_Frequency";

protected static KeyNamePair Model = new KeyNamePair(2000002,"P_Frequency");

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
StringBuffer sb = new StringBuffer ("X_P_Frequency[").append(get_ID()).append("]");
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
/** Set Number of  period.
@param NumberOfPeriod Number of  period */
public void setNumberOfPeriod (int NumberOfPeriod)
{
set_Value ("NumberOfPeriod", new Integer(NumberOfPeriod));
}
/** Get Number of  period.
@return Number of  period */
public int getNumberOfPeriod() 
{
Integer ii = (Integer)get_Value("NumberOfPeriod");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Frequency.
@param P_Frequency_ID Frequency */
public void setP_Frequency_ID (int P_Frequency_ID)
{
if (P_Frequency_ID < 1) throw new IllegalArgumentException ("P_Frequency_ID is mandatory.");
set_Value ("P_Frequency_ID", new Integer(P_Frequency_ID));
}
/** Get Frequency.
@return Frequency */
public int getP_Frequency_ID() 
{
Integer ii = (Integer)get_Value("P_Frequency_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** PayPeriodType AD_Reference_ID=2000068 */
public static final int PAYPERIODTYPE_AD_Reference_ID=2000068;
/** Bi-weekly = B */
public static final String PAYPERIODTYPE_Bi_Weekly = "B";
/** Monthly = M */
public static final String PAYPERIODTYPE_Monthly = "M";
/** Monthly non-standard = O */
public static final String PAYPERIODTYPE_MonthlyNon_Standard = "O";
/** Semi-Monthly = S */
public static final String PAYPERIODTYPE_Semi_Monthly = "S";
/** Semi-Monthly non-standard = E */
public static final String PAYPERIODTYPE_Semi_MonthlyNon_Standard = "E";
/** Thirteen Per Year = H */
public static final String PAYPERIODTYPE_ThirteenPerYear = "H";
/** Weekly = W */
public static final String PAYPERIODTYPE_Weekly = "W";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isPayPeriodTypeValid (String test)
{
return test.equals("B") || test.equals("M") || test.equals("O") || test.equals("S") || test.equals("E") || test.equals("H") || test.equals("W");
}
/** Set Pay Period Type.
@param PayPeriodType Pay Period Type */
public void setPayPeriodType (String PayPeriodType)
{
if (PayPeriodType == null) throw new IllegalArgumentException ("PayPeriodType is mandatory");
if (!isPayPeriodTypeValid(PayPeriodType))
throw new IllegalArgumentException ("PayPeriodType Invalid value - " + PayPeriodType + " - Reference_ID=2000068 - B - M - O - S - E - H - W");
if (PayPeriodType.length() > 1)
{
log.warning("Length > 1 - truncated");
PayPeriodType = PayPeriodType.substring(0,0);
}
set_Value ("PayPeriodType", PayPeriodType);
}
/** Get Pay Period Type.
@return Pay Period Type */
public String getPayPeriodType() 
{
return (String)get_Value("PayPeriodType");
}
}
