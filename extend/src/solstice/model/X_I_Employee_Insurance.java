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
/** Generated Model for I_Employee_Insurance
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: X_I_Employee_Insurance.java,v 1.1 2007/07/18 14:43:13 marmor01 Exp $ */
public class X_I_Employee_Insurance extends PO
{
/** Standard Constructor
@param ctx context
@param I_Employee_Insurance_ID id
@param trxName transaction
*/
public X_I_Employee_Insurance (Properties ctx, int I_Employee_Insurance_ID, String trxName)
{
super (ctx, I_Employee_Insurance_ID, trxName);
/** if (I_Employee_Insurance_ID == 0)
{
setI_Employee_Insurance_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_I_Employee_Insurance (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100852 */
public static final int Table_ID=2100852;

/** TableName=I_Employee_Insurance */
public static final String Table_Name="I_Employee_Insurance";

protected static KeyNamePair Model = new KeyNamePair(2100852,"I_Employee_Insurance");

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
StringBuffer sb = new StringBuffer ("X_I_Employee_Insurance[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Employee.
@param Employee Employee */
public void setEmployee (String Employee)
{
if (Employee != null && Employee.length() > 10)
{
log.warning("Length > 10 - truncated");
Employee = Employee.substring(0,9);
}
set_Value ("Employee", Employee);
}
/** Get Employee.
@return Employee */
public String getEmployee() 
{
return (String)get_Value("Employee");
}
/** Set I_Employee_Insurance_ID.
@param I_Employee_Insurance_ID I_Employee_Insurance_ID */
public void setI_Employee_Insurance_ID (int I_Employee_Insurance_ID)
{
if (I_Employee_Insurance_ID < 1) throw new IllegalArgumentException ("I_Employee_Insurance_ID is mandatory.");
set_ValueNoCheck ("I_Employee_Insurance_ID", new Integer(I_Employee_Insurance_ID));
}
/** Get I_Employee_Insurance_ID.
@return I_Employee_Insurance_ID */
public int getI_Employee_Insurance_ID() 
{
Integer ii = (Integer)get_Value("I_Employee_Insurance_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Import Error Message.
@param I_ErrorMsg Messages generated from import process */
public void setI_ErrorMsg (String I_ErrorMsg)
{
if (I_ErrorMsg != null && I_ErrorMsg.length() > 2000)
{
log.warning("Length > 2000 - truncated");
I_ErrorMsg = I_ErrorMsg.substring(0,1999);
}
set_Value ("I_ErrorMsg", I_ErrorMsg);
}
/** Get Import Error Message.
@return Messages generated from import process */
public String getI_ErrorMsg() 
{
return (String)get_Value("I_ErrorMsg");
}
/** Set Imported.
@param I_IsImported Has this import been processed */
public void setI_IsImported (boolean I_IsImported)
{
set_Value ("I_IsImported", new Boolean(I_IsImported));
}
/** Get Imported.
@return Has this import been processed */
public boolean isI_IsImported() 
{
Object oo = get_Value("I_IsImported");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Insurance.
@param Insurance Insurance */
public void setInsurance (String Insurance)
{
if (Insurance != null && Insurance.length() > 10)
{
log.warning("Length > 10 - truncated");
Insurance = Insurance.substring(0,9);
}
set_Value ("Insurance", Insurance);
}
/** Get Insurance.
@return Insurance */
public String getInsurance() 
{
return (String)get_Value("Insurance");
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
/** Set Employee Profile.
@param P_Employee_Profile_ID Employee Profile */
public void setP_Employee_Profile_ID (int P_Employee_Profile_ID)
{
if (P_Employee_Profile_ID <= 0) set_Value ("P_Employee_Profile_ID", null);
 else 
set_Value ("P_Employee_Profile_ID", new Integer(P_Employee_Profile_ID));
}
/** Get Employee Profile.
@return Employee Profile */
public int getP_Employee_Profile_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Profile_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Profile.
@param P_Profile_ID Profile */
public void setP_Profile_ID (int P_Profile_ID)
{
if (P_Profile_ID <= 0) set_Value ("P_Profile_ID", null);
 else 
set_Value ("P_Profile_ID", new Integer(P_Profile_ID));
}
/** Get Profile.
@return Profile */
public int getP_Profile_ID() 
{
Integer ii = (Integer)get_Value("P_Profile_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Processed.
@param Processed The document has been processed */
public void setProcessed (boolean Processed)
{
set_Value ("Processed", new Boolean(Processed));
}
/** Get Processed.
@return The document has been processed */
public boolean isProcessed() 
{
Object oo = get_Value("Processed");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
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
}
