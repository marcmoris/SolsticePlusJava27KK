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
/** Generated Model for I_Employee_NonBusinessDay
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_I_Employee_NonBusinessDay extends PO
{
/** Standard Constructor
@param ctx context
@param I_Employee_NonBusinessDay_ID id
@param trxName transaction
*/
public X_I_Employee_NonBusinessDay (Properties ctx, int I_Employee_NonBusinessDay_ID, String trxName)
{
super (ctx, I_Employee_NonBusinessDay_ID, trxName);
/** if (I_Employee_NonBusinessDay_ID == 0)
{
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_I_Employee_NonBusinessDay (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=3201208 */
public static final int Table_ID=3201208;

/** TableName=I_Employee_NonBusinessDay */
public static final String Table_Name="I_Employee_NonBusinessDay";

protected static KeyNamePair Model = new KeyNamePair(3201208,"I_Employee_NonBusinessDay");

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
StringBuffer sb = new StringBuffer ("X_I_Employee_NonBusinessDay[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Validation code.
@param Code Validation Code */
public void setCode (String Code)
{
if (Code != null && Code.length() > 10)
{
log.warning("Length > 10 - truncated");
Code = Code.substring(0,9);
}
set_Value ("Code", Code);
}
/** Get Validation code.
@return Validation Code */
public String getCode() 
{
return (String)get_Value("Code");
}
/** Set Dimanche.
@param CP_DIM Dimanche */
public void setCP_DIM (int CP_DIM)
{
set_Value ("CP_DIM", new Integer(CP_DIM));
}
/** Get Dimanche.
@return Dimanche */
public int getCP_DIM() 
{
Integer ii = (Integer)get_Value("CP_DIM");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Jeudi.
@param CP_JEU Jeudi */
public void setCP_JEU (int CP_JEU)
{
set_Value ("CP_JEU", new Integer(CP_JEU));
}
/** Get Jeudi.
@return Jeudi */
public int getCP_JEU() 
{
Integer ii = (Integer)get_Value("CP_JEU");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Lundi.
@param CP_LUN Lundi */
public void setCP_LUN (int CP_LUN)
{
set_Value ("CP_LUN", new Integer(CP_LUN));
}
/** Get Lundi.
@return Lundi */
public int getCP_LUN() 
{
Integer ii = (Integer)get_Value("CP_LUN");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Mardi.
@param CP_MAR Mardi */
public void setCP_MAR (int CP_MAR)
{
set_Value ("CP_MAR", new Integer(CP_MAR));
}
/** Get Mardi.
@return Mardi */
public int getCP_MAR() 
{
Integer ii = (Integer)get_Value("CP_MAR");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Mercredi.
@param CP_MER Mercredi */
public void setCP_MER (int CP_MER)
{
set_Value ("CP_MER", new Integer(CP_MER));
}
/** Get Mercredi.
@return Mercredi */
public int getCP_MER() 
{
Integer ii = (Integer)get_Value("CP_MER");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Samedi.
@param CP_SAM Samedi */
public void setCP_SAM (int CP_SAM)
{
set_Value ("CP_SAM", new Integer(CP_SAM));
}
/** Get Samedi.
@return Samedi */
public int getCP_SAM() 
{
Integer ii = (Integer)get_Value("CP_SAM");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Vendredi.
@param CP_VEN Vendredi */
public void setCP_VEN (int CP_VEN)
{
set_Value ("CP_VEN", new Integer(CP_VEN));
}
/** Get Vendredi.
@return Vendredi */
public int getCP_VEN() 
{
Integer ii = (Integer)get_Value("CP_VEN");
if (ii == null) return 0;
return ii.intValue();
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
/** Set Gain.
@param Gain Gain */
public void setGain (String Gain)
{
if (Gain != null && Gain.length() > 10)
{
log.warning("Length > 10 - truncated");
Gain = Gain.substring(0,9);
}
set_Value ("Gain", Gain);
}
/** Get Gain.
@return Gain */
public String getGain() 
{
return (String)get_Value("Gain");
}
/** Set GainCode.
@param GainCode GainCode */
public void setGainCode (String GainCode)
{
if (GainCode != null && GainCode.length() > 10)
{
log.warning("Length > 10 - truncated");
GainCode = GainCode.substring(0,9);
}
set_Value ("GainCode", GainCode);
}
/** Get GainCode.
@return GainCode */
public String getGainCode() 
{
return (String)get_Value("GainCode");
}
/** Set I_Employee_NonBusinessDay_ID.
@param I_Employee_NonBusinessDay_ID I_Employee_NonBusinessDay_ID */
public void setI_Employee_NonBusinessDay_ID (int I_Employee_NonBusinessDay_ID)
{
if (I_Employee_NonBusinessDay_ID <= 0) set_ValueNoCheck ("I_Employee_NonBusinessDay_ID", null);
 else 
set_ValueNoCheck ("I_Employee_NonBusinessDay_ID", new Integer(I_Employee_NonBusinessDay_ID));
}
/** Get I_Employee_NonBusinessDay_ID.
@return I_Employee_NonBusinessDay_ID */
public int getI_Employee_NonBusinessDay_ID() 
{
Integer ii = (Integer)get_Value("I_Employee_NonBusinessDay_ID");
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
/** Set Jeudi.
@param Jeudi Jeudi */
public void setJeudi (int Jeudi)
{
set_Value ("Jeudi", new Integer(Jeudi));
}
/** Get Jeudi.
@return Jeudi */
public int getJeudi() 
{
Integer ii = (Integer)get_Value("Jeudi");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Lundi.
@param Lundi Lundi */
public void setLundi (int Lundi)
{
set_Value ("Lundi", new Integer(Lundi));
}
/** Get Lundi.
@return Lundi */
public int getLundi() 
{
Integer ii = (Integer)get_Value("Lundi");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Mardi.
@param Mardi Mardi */
public void setMardi (int Mardi)
{
set_Value ("Mardi", new Integer(Mardi));
}
/** Get Mardi.
@return Mardi */
public int getMardi() 
{
Integer ii = (Integer)get_Value("Mardi");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Mercredi.
@param Mercredi Mercredi */
public void setMercredi (int Mercredi)
{
set_Value ("Mercredi", new Integer(Mercredi));
}
/** Get Mercredi.
@return Mercredi */
public int getMercredi() 
{
Integer ii = (Integer)get_Value("Mercredi");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Name .
@param Name Alphanumeric identifier of the entity */
public void setName (String Name)
{
if (Name != null && Name.length() > 65)
{
log.warning("Length > 65 - truncated");
Name = Name.substring(0,64);
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
/** Set Period.
@param P_Period_ID Period of the Calendar   */
public void setP_Period_ID (int P_Period_ID)
{
if (P_Period_ID <= 0) set_Value ("P_Period_ID", null);
 else 
set_Value ("P_Period_ID", new Integer(P_Period_ID));
}
/** Get Period.
@return Period of the Calendar   */
public int getP_Period_ID() 
{
Integer ii = (Integer)get_Value("P_Period_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Time Sheet No..
@param P_Time_Sheet_ID   */
public void setP_Time_Sheet_ID (int P_Time_Sheet_ID)
{
if (P_Time_Sheet_ID <= 0) set_Value ("P_Time_Sheet_ID", null);
 else 
set_Value ("P_Time_Sheet_ID", new Integer(P_Time_Sheet_ID));
}
/** Get Time Sheet No..
@return   */
public int getP_Time_Sheet_ID() 
{
Integer ii = (Integer)get_Value("P_Time_Sheet_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Period.
@param Period Period */
public void setPeriod (String Period)
{
if (Period != null && Period.length() > 60)
{
log.warning("Length > 60 - truncated");
Period = Period.substring(0,59);
}
set_Value ("Period", Period);
}
/** Get Period.
@return Period */
public String getPeriod() 
{
return (String)get_Value("Period");
}
/** Set Période.
@param Période Période */
public void setPériode (String Période)
{
if (Période != null && Période.length() > 10)
{
log.warning("Length > 10 - truncated");
Période = Période.substring(0,9);
}
set_Value ("Période", Période);
}
/** Get Période.
@return Période */
public String getPériode() 
{
return (String)get_Value("Période");
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
/** Set Quantity.
@param Quantity Quantity */
public void setQuantity (BigDecimal Quantity)
{
set_Value ("Quantity", Quantity);
}
/** Get Quantity.
@return Quantity */
public BigDecimal getQuantity() 
{
BigDecimal bd = (BigDecimal)get_Value("Quantity");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Search Key.
@param Value Search key for the record in the format required - must be unique */
public void setValue (String Value)
{
if (Value != null && Value.length() > 30)
{
log.warning("Length > 30 - truncated");
Value = Value.substring(0,29);
}
set_Value ("Value", Value);
}
/** Get Search Key.
@return Search key for the record in the format required - must be unique */
public String getValue() 
{
return (String)get_Value("Value");
}
/** Set Vendredi.
@param Vendredi Vendredi */
public void setVendredi (int Vendredi)
{
set_Value ("Vendredi", new Integer(Vendredi));
}
/** Get Vendredi.
@return Vendredi */
public int getVendredi() 
{
Integer ii = (Integer)get_Value("Vendredi");
if (ii == null) return 0;
return ii.intValue();
}
}
