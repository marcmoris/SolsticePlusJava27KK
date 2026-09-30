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
/** Generated Model for I_Employee_Credits
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: X_I_Employee_Credits.java,v 1.1 2007/08/06 12:58:17 marmor01 Exp $ */
public class X_I_Employee_Credits extends PO
{
/** Standard Constructor
@param ctx context
@param I_Employee_Credits_ID id
@param trxName transaction
*/
public X_I_Employee_Credits (Properties ctx, int I_Employee_Credits_ID, String trxName)
{
super (ctx, I_Employee_Credits_ID, trxName);
/** if (I_Employee_Credits_ID == 0)
{
setI_Employee_Credits_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_I_Employee_Credits (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100861 */
public static final int Table_ID=2100861;

/** TableName=I_Employee_Credits */
public static final String Table_Name="I_Employee_Credits";

protected static KeyNamePair Model = new KeyNamePair(2100861,"I_Employee_Credits");

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
StringBuffer sb = new StringBuffer ("X_I_Employee_Credits[").append(get_ID()).append("]");
return sb.toString();
}
/** Set First name.
@param FirstName First name */
public void setFirstName (String FirstName)
{
if (FirstName != null && FirstName.length() > 30)
{
log.warning("Length > 30 - truncated");
FirstName = FirstName.substring(0,29);
}
set_Value ("FirstName", FirstName);
}
/** Get First name.
@return First name */
public String getFirstName() 
{
return (String)get_Value("FirstName");
}
/** Set I_Employee_Credits_ID.
@param I_Employee_Credits_ID I_Employee_Credits_ID */
public void setI_Employee_Credits_ID (int I_Employee_Credits_ID)
{
if (I_Employee_Credits_ID < 1) throw new IllegalArgumentException ("I_Employee_Credits_ID is mandatory.");
set_ValueNoCheck ("I_Employee_Credits_ID", new Integer(I_Employee_Credits_ID));
}
/** Get I_Employee_Credits_ID.
@return I_Employee_Credits_ID */
public int getI_Employee_Credits_ID() 
{
Integer ii = (Integer)get_Value("I_Employee_Credits_ID");
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
/** Set LastName.
@param LastName LastName */
public void setLastName (String LastName)
{
if (LastName != null && LastName.length() > 30)
{
log.warning("Length > 30 - truncated");
LastName = LastName.substring(0,29);
}
set_Value ("LastName", LastName);
}
/** Get LastName.
@return LastName */
public String getLastName() 
{
return (String)get_Value("LastName");
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
/** Set Variation.
@param PMovementVariation Variation */
public void setPMovementVariation (BigDecimal PMovementVariation)
{
set_Value ("PMovementVariation", PMovementVariation);
}
/** Get Variation.
@return Variation */
public BigDecimal getPMovementVariation() 
{
BigDecimal bd = (BigDecimal)get_Value("PMovementVariation");
if (bd == null) return Env.ZERO;
return bd;
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
/** Set Sin.
@param Sin Social Insurance NUMERIC  */
public void setSin (String Sin)
{
if (Sin != null && Sin.length() > 30)
{
log.warning("Length > 30 - truncated");
Sin = Sin.substring(0,29);
}
set_Value ("Sin", Sin);
}
/** Get Sin.
@return Social Insurance NUMERIC  */
public String getSin() 
{
return (String)get_Value("Sin");
}
}
