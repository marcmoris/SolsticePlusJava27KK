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
/** Generated Model for I_Employee_PayScale
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: X_I_Employee_PayScale.java,v 1.1 2007/07/31 17:50:09 marmor01 Exp $ */
public class X_I_Employee_PayScale extends PO
{
/** Standard Constructor
@param ctx context
@param I_Employee_PayScale_ID id
@param trxName transaction
*/
public X_I_Employee_PayScale (Properties ctx, int I_Employee_PayScale_ID, String trxName)
{
super (ctx, I_Employee_PayScale_ID, trxName);
/** if (I_Employee_PayScale_ID == 0)
{
setI_Employee_PayScale_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_I_Employee_PayScale (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100853 */
public static final int Table_ID=2100853;

/** TableName=I_Employee_PayScale */
public static final String Table_Name="I_Employee_PayScale";

protected static KeyNamePair Model = new KeyNamePair(2100853,"I_Employee_PayScale");

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
StringBuffer sb = new StringBuffer ("X_I_Employee_PayScale[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Anniversary.
@param Anniversary Anniversary */
public void setAnniversary (Timestamp Anniversary)
{
set_Value ("Anniversary", Anniversary);
}
/** Get Anniversary.
@return Anniversary */
public Timestamp getAnniversary() 
{
return (Timestamp)get_Value("Anniversary");
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
/** Set I_Employee_PayScale_ID.
@param I_Employee_PayScale_ID I_Employee_PayScale_ID */
public void setI_Employee_PayScale_ID (int I_Employee_PayScale_ID)
{
if (I_Employee_PayScale_ID < 1) throw new IllegalArgumentException ("I_Employee_PayScale_ID is mandatory.");
set_ValueNoCheck ("I_Employee_PayScale_ID", new Integer(I_Employee_PayScale_ID));
}
/** Get I_Employee_PayScale_ID.
@return I_Employee_PayScale_ID */
public int getI_Employee_PayScale_ID() 
{
Integer ii = (Integer)get_Value("I_Employee_PayScale_ID");
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
/** Set Level.
@param Level Level */
public void setLevel (String Level)
{
if (Level != null && Level.length() > 30)
{
log.warning("Length > 30 - truncated");
Level = Level.substring(0,29);
}
set_Value ("Level", Level);
}
/** Get Level.
@return Level */
public String getLevel() 
{
return (String)get_Value("Level");
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
/** Set Occupation Group.
@param P_Occupation_Group_ID   */
public void setP_Occupation_Group_ID (int P_Occupation_Group_ID)
{
if (P_Occupation_Group_ID <= 0) set_Value ("P_Occupation_Group_ID", null);
 else 
set_Value ("P_Occupation_Group_ID", new Integer(P_Occupation_Group_ID));
}
/** Get Occupation Group.
@return   */
public int getP_Occupation_Group_ID() 
{
Integer ii = (Integer)get_Value("P_Occupation_Group_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Step.
@param P_Salary_Scale_Detail_ID   */
public void setP_Salary_Scale_Detail_ID (int P_Salary_Scale_Detail_ID)
{
if (P_Salary_Scale_Detail_ID <= 0) set_Value ("P_Salary_Scale_Detail_ID", null);
 else 
set_Value ("P_Salary_Scale_Detail_ID", new Integer(P_Salary_Scale_Detail_ID));
}
/** Get Step.
@return   */
public int getP_Salary_Scale_Detail_ID() 
{
Integer ii = (Integer)get_Value("P_Salary_Scale_Detail_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Salary Scale.
@param P_Salary_Scale_ID   */
public void setP_Salary_Scale_ID (int P_Salary_Scale_ID)
{
if (P_Salary_Scale_ID <= 0) set_Value ("P_Salary_Scale_ID", null);
 else 
set_Value ("P_Salary_Scale_ID", new Integer(P_Salary_Scale_ID));
}
/** Get Salary Scale.
@return   */
public int getP_Salary_Scale_ID() 
{
Integer ii = (Integer)get_Value("P_Salary_Scale_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set PayScale.
@param PayScale PayScale */
public void setPayScale (String PayScale)
{
if (PayScale != null && PayScale.length() > 30)
{
log.warning("Length > 30 - truncated");
PayScale = PayScale.substring(0,29);
}
set_Value ("PayScale", PayScale);
}
/** Get PayScale.
@return PayScale */
public String getPayScale() 
{
return (String)get_Value("PayScale");
}
/** Set Politique_Salariale.
@param Politique_Salariale Politique_Salariale */
public void setPolitique_Salariale (String Politique_Salariale)
{
if (Politique_Salariale != null && Politique_Salariale.length() > 30)
{
log.warning("Length > 30 - truncated");
Politique_Salariale = Politique_Salariale.substring(0,29);
}
set_Value ("Politique_Salariale", Politique_Salariale);
}
/** Get Politique_Salariale.
@return Politique_Salariale */
public String getPolitique_Salariale() 
{
return (String)get_Value("Politique_Salariale");
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
