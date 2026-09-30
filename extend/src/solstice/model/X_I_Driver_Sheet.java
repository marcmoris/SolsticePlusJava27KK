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
/** Generated Model for I_Driver_Sheet
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_I_Driver_Sheet extends PO
{
/** Standard Constructor
@param ctx context
@param I_Driver_Sheet_ID id
@param trxName transaction
*/
public X_I_Driver_Sheet (Properties ctx, int I_Driver_Sheet_ID, String trxName)
{
super (ctx, I_Driver_Sheet_ID, trxName);
/** if (I_Driver_Sheet_ID == 0)
{
setI_Driver_Sheet_ID (0);
setP_Assignment_ID (0);
setP_Employee_ID (0);
setP_Post_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_I_Driver_Sheet (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=3201194 */
public static final int Table_ID=3201194;

/** TableName=I_Driver_Sheet */
public static final String Table_Name="I_Driver_Sheet";

protected static KeyNamePair Model = new KeyNamePair(3201194,"I_Driver_Sheet");

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
StringBuffer sb = new StringBuffer ("X_I_Driver_Sheet[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Amt.
@param Amt Amount */
public void setAmt (BigDecimal Amt)
{
set_Value ("Amt", Amt);
}
/** Get Amt.
@return Amount */
public BigDecimal getAmt() 
{
BigDecimal bd = (BigDecimal)get_Value("Amt");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Earning.
@param Earning Earning */
public void setEarning (String Earning)
{
if (Earning != null && Earning.length() > 30)
{
log.warning("Length > 30 - truncated");
Earning = Earning.substring(0,29);
}
set_Value ("Earning", Earning);
}
/** Get Earning.
@return Earning */
public String getEarning() 
{
return (String)get_Value("Earning");
}
/** Set Employé.
@param Employé Employé */
public void setEmployé (String Employé)
{
if (Employé != null && Employé.length() > 10)
{
log.warning("Length > 10 - truncated");
Employé = Employé.substring(0,9);
}
set_Value ("Employé", Employé);
}
/** Get Employé.
@return Employé */
public String getEmployé() 
{
return (String)get_Value("Employé");
}
/** Set Employee.
@param Employee Employee */
public void setEmployee (String Employee)
{
if (Employee != null && Employee.length() > 30)
{
log.warning("Length > 30 - truncated");
Employee = Employee.substring(0,29);
}
set_Value ("Employee", Employee);
}
/** Get Employee.
@return Employee */
public String getEmployee() 
{
return (String)get_Value("Employee");
}
/** Set I_Driver_Sheet_ID.
@param I_Driver_Sheet_ID I_Driver_Sheet_ID */
public void setI_Driver_Sheet_ID (int I_Driver_Sheet_ID)
{
if (I_Driver_Sheet_ID < 1) throw new IllegalArgumentException ("I_Driver_Sheet_ID is mandatory.");
set_ValueNoCheck ("I_Driver_Sheet_ID", new Integer(I_Driver_Sheet_ID));
}
/** Get I_Driver_Sheet_ID.
@return I_Driver_Sheet_ID */
public int getI_Driver_Sheet_ID() 
{
Integer ii = (Integer)get_Value("I_Driver_Sheet_ID");
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
/** Set Montant.
@param Montant Montant */
public void setMontant (BigDecimal Montant)
{
set_Value ("Montant", Montant);
}
/** Get Montant.
@return Montant */
public BigDecimal getMontant() 
{
BigDecimal bd = (BigDecimal)get_Value("Montant");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Name .
@param Name Alphanumeric identifier of the entity */
public void setName (String Name)
{
if (Name != null && Name.length() > 100)
{
log.warning("Length > 100 - truncated");
Name = Name.substring(0,99);
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
/** Set nom.
@param nom nom */
public void setnom (String nom)
{
if (nom != null && nom.length() > 60)
{
log.warning("Length > 60 - truncated");
nom = nom.substring(0,59);
}
set_Value ("nom", nom);
}
/** Get nom.
@return nom */
public String getnom() 
{
return (String)get_Value("nom");
}

/** P_Assignment_ID AD_Reference_ID=2000057 */
public static final int P_ASSIGNMENT_ID_AD_Reference_ID=2000057;
/** Set Assignment.
@param P_Assignment_ID Assignment */
public void setP_Assignment_ID (int P_Assignment_ID)
{
if (P_Assignment_ID < 1) throw new IllegalArgumentException ("P_Assignment_ID is mandatory.");
set_ValueNoCheck ("P_Assignment_ID", new Integer(P_Assignment_ID));
}
/** Get Assignment.
@return Assignment */
public int getP_Assignment_ID() 
{
Integer ii = (Integer)get_Value("P_Assignment_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Employee_ID AD_Reference_ID=2000010 */
public static final int P_EMPLOYEE_ID_AD_Reference_ID=2000010;
/** Set Employee .
@param P_Employee_ID Employee */
public void setP_Employee_ID (int P_Employee_ID)
{
if (P_Employee_ID < 1) throw new IllegalArgumentException ("P_Employee_ID is mandatory.");
set_ValueNoCheck ("P_Employee_ID", new Integer(P_Employee_ID));
}
/** Get Employee .
@return Employee */
public int getP_Employee_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Gain.
@param P_Gain_ID Gain */
public void setP_Gain_ID (int P_Gain_ID)
{
if (P_Gain_ID <= 0) set_Value ("P_Gain_ID", null);
 else 
set_Value ("P_Gain_ID", new Integer(P_Gain_ID));
}
/** Get Gain.
@return Gain */
public int getP_Gain_ID() 
{
Integer ii = (Integer)get_Value("P_Gain_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Particular Sheet.
@param P_Particular_Sheet_ID Particular Sheet */
public void setP_Particular_Sheet_ID (int P_Particular_Sheet_ID)
{
if (P_Particular_Sheet_ID <= 0) set_Value ("P_Particular_Sheet_ID", null);
 else 
set_Value ("P_Particular_Sheet_ID", new Integer(P_Particular_Sheet_ID));
}
/** Get Particular Sheet.
@return Particular Sheet */
public int getP_Particular_Sheet_ID() 
{
Integer ii = (Integer)get_Value("P_Particular_Sheet_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Post_ID AD_Reference_ID=2000004 */
public static final int P_POST_ID_AD_Reference_ID=2000004;
/** Set Post.
@param P_Post_ID Post */
public void setP_Post_ID (int P_Post_ID)
{
if (P_Post_ID < 1) throw new IllegalArgumentException ("P_Post_ID is mandatory.");
set_ValueNoCheck ("P_Post_ID", new Integer(P_Post_ID));
}
/** Get Post.
@return Post */
public int getP_Post_ID() 
{
Integer ii = (Integer)get_Value("P_Post_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Position.
@param Position Position */
public void setPosition (String Position)
{
if (Position != null && Position.length() > 30)
{
log.warning("Length > 30 - truncated");
Position = Position.substring(0,29);
}
set_Value ("Position", Position);
}
/** Get Position.
@return Position */
public String getPosition() 
{
return (String)get_Value("Position");
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

/** Set Day.
@param Day Day */
public void setDay (Timestamp Day)
{
set_Value ("Day", Day);
}
/** Get Day.
@return Day */
public Timestamp getDay() 
{
return (Timestamp)get_Value("Day");
}

}
