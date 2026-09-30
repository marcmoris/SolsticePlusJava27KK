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
/** Generated Model for P_Employee_Immigration
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.4 2008/03/12 20:02:49 marmor01 Exp $ */
public class X_P_Employee_Immigration extends PO
{
/** Standard Constructor
@param ctx context
@param P_Employee_Immigration_ID id
@param trxName transaction
*/
public X_P_Employee_Immigration (Properties ctx, int P_Employee_Immigration_ID, String trxName)
{
super (ctx, P_Employee_Immigration_ID, trxName);
/** if (P_Employee_Immigration_ID == 0)
{
setDateOfExpiry (new Timestamp(System.currentTimeMillis()));
setIssuedDate (new Timestamp(System.currentTimeMillis()));
setPassportNo (null);
setPassportType (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Employee_Immigration (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100896 */
public static final int Table_ID=2100896;

/** TableName=P_Employee_Immigration */
public static final String Table_Name="P_Employee_Immigration";

protected static KeyNamePair Model = new KeyNamePair(2100896,"P_Employee_Immigration");

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
StringBuffer sb = new StringBuffer ("X_P_Employee_Immigration[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Citizenship.
@param Citizenship Citizenship */
public void setCitizenship (String Citizenship)
{
if (Citizenship != null && Citizenship.length() > 30)
{
log.warning("Length > 30 - truncated");
Citizenship = Citizenship.substring(0,29);
}
set_Value ("Citizenship", Citizenship);
}
/** Get Citizenship.
@return Citizenship */
public String getCitizenship() 
{
return (String)get_Value("Citizenship");
}
/** Set Date of expiry.
@param DateOfExpiry Date of expiry */
public void setDateOfExpiry (Timestamp DateOfExpiry)
{
if (DateOfExpiry == null) throw new IllegalArgumentException ("DateOfExpiry is mandatory.");
set_Value ("DateOfExpiry", DateOfExpiry);
}
/** Get Date of expiry.
@return Date of expiry */
public Timestamp getDateOfExpiry() 
{
return (Timestamp)get_Value("DateOfExpiry");
}
/** Set Issued date.
@param IssuedDate Issued date */
public void setIssuedDate (Timestamp IssuedDate)
{
if (IssuedDate == null) throw new IllegalArgumentException ("IssuedDate is mandatory.");
set_Value ("IssuedDate", IssuedDate);
}
/** Get Issued date.
@return Issued date */
public Timestamp getIssuedDate() 
{
return (Timestamp)get_Value("IssuedDate");
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
/** Set P_Employee_Immigration_ID.
@param P_Employee_Immigration_ID P_Employee_Immigration_ID */
public void setP_Employee_Immigration_ID (int P_Employee_Immigration_ID)
{
if (P_Employee_Immigration_ID <= 0) set_ValueNoCheck ("P_Employee_Immigration_ID", null);
 else 
set_ValueNoCheck ("P_Employee_Immigration_ID", new Integer(P_Employee_Immigration_ID));
}
/** Get P_Employee_Immigration_ID.
@return P_Employee_Immigration_ID */
public int getP_Employee_Immigration_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Immigration_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Passport / Visa No.
@param PassportNo Passport / Visa No */
public void setPassportNo (String PassportNo)
{
if (PassportNo == null) throw new IllegalArgumentException ("PassportNo is mandatory.");
if (PassportNo.length() > 30)
{
log.warning("Length > 30 - truncated");
PassportNo = PassportNo.substring(0,29);
}
set_Value ("PassportNo", PassportNo);
}
/** Get Passport / Visa No.
@return Passport / Visa No */
public String getPassportNo() 
{
return (String)get_Value("PassportNo");
}

/** PassportType AD_Reference_ID=2100472 */
public static final int PASSPORTTYPE_AD_Reference_ID=2100472;
/** Passport = Passport */
public static final String PASSPORTTYPE_Passport = "Passport";
/** Visa = Visa */
public static final String PASSPORTTYPE_Visa = "Visa";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isPassportTypeValid (String test)
{
return test.equals("Passport") || test.equals("Visa");
}
/** Set Passport / Visa.
@param PassportType Passport / Visa */
public void setPassportType (String PassportType)
{
if (PassportType == null) throw new IllegalArgumentException ("PassportType is mandatory");
if (!isPassportTypeValid(PassportType))
throw new IllegalArgumentException ("PassportType Invalid value - " + PassportType + " - Reference_ID=2100472 - Passport - Visa");
if (PassportType.length() > 30)
{
log.warning("Length > 30 - truncated");
PassportType = PassportType.substring(0,29);
}
set_Value ("PassportType", PassportType);
}
/** Get Passport / Visa.
@return Passport / Visa */
public String getPassportType() 
{
return (String)get_Value("PassportType");
}
}
