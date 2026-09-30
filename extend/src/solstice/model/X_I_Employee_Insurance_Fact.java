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
/** Generated Model for I_Employee_Insurance_Fact
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: X_I_Employee_Insurance_Fact.java,v 1.1 2007/09/23 17:35:20 marmor01 Exp $ */
public class X_I_Employee_Insurance_Fact extends PO
{
/** Standard Constructor
@param ctx context
@param I_Employee_Insurance_Fact_ID id
@param trxName transaction
*/
public X_I_Employee_Insurance_Fact (Properties ctx, int I_Employee_Insurance_Fact_ID, String trxName)
{
super (ctx, I_Employee_Insurance_Fact_ID, trxName);
/** if (I_Employee_Insurance_Fact_ID == 0)
{
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_I_Employee_Insurance_Fact (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100873 */
public static final int Table_ID=2100873;

/** TableName=I_Employee_Insurance_Fact */
public static final String Table_Name="I_Employee_Insurance_Fact";

protected static KeyNamePair Model = new KeyNamePair(2100873,"I_Employee_Insurance_Fact");

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
StringBuffer sb = new StringBuffer ("X_I_Employee_Insurance_Fact[").append(get_ID()).append("]");
return sb.toString();
}
/** Set BirthDate.
@param BirthDate BirthDate   */
public void setBirthDate (Timestamp BirthDate)
{
set_Value ("BirthDate", BirthDate);
}
/** Get BirthDate.
@return BirthDate   */
public Timestamp getBirthDate() 
{
return (Timestamp)get_Value("BirthDate");
}
/** Set DEDUCTION.
@param DEDUCTION DEDUCTION */
public void setDEDUCTION (String DEDUCTION)
{
if (DEDUCTION != null && DEDUCTION.length() > 30)
{
log.warning("Length > 30 - truncated");
DEDUCTION = DEDUCTION.substring(0,29);
}
set_Value ("DEDUCTION", DEDUCTION);
}
/** Get DEDUCTION.
@return DEDUCTION */
public String getDEDUCTION() 
{
return (String)get_Value("DEDUCTION");
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
/** Set Gender.
@param Gender Gender  */
public void setGender (String Gender)
{
if (Gender != null && Gender.length() > 10)
{
log.warning("Length > 10 - truncated");
Gender = Gender.substring(0,9);
}
set_Value ("Gender", Gender);
}
/** Get Gender.
@return Gender  */
public String getGender() 
{
return (String)get_Value("Gender");
}
/** Set GenderParticipant.
@param GenderParticipant GenderParticipant */
public void setGenderParticipant (String GenderParticipant)
{
if (GenderParticipant != null && GenderParticipant.length() > 10)
{
log.warning("Length > 10 - truncated");
GenderParticipant = GenderParticipant.substring(0,9);
}
set_Value ("GenderParticipant", GenderParticipant);
}
/** Get GenderParticipant.
@return GenderParticipant */
public String getGenderParticipant() 
{
return (String)get_Value("GenderParticipant");
}
/** Set I_Employee_Insurance_Fact_ID.
@param I_Employee_Insurance_Fact_ID I_Employee_Insurance_Fact_ID */
public void setI_Employee_Insurance_Fact_ID (int I_Employee_Insurance_Fact_ID)
{
if (I_Employee_Insurance_Fact_ID <= 0) set_ValueNoCheck ("I_Employee_Insurance_Fact_ID", null);
 else 
set_ValueNoCheck ("I_Employee_Insurance_Fact_ID", new Integer(I_Employee_Insurance_Fact_ID));
}
/** Get I_Employee_Insurance_Fact_ID.
@return I_Employee_Insurance_Fact_ID */
public int getI_Employee_Insurance_Fact_ID() 
{
Integer ii = (Integer)get_Value("I_Employee_Insurance_Fact_ID");
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
/** Set InsuranceParticipantFristName.
@param InsuranceParticipantFristName InsuranceParticipantFristName */
public void setInsuranceParticipantFristName (String InsuranceParticipantFristName)
{
if (InsuranceParticipantFristName != null && InsuranceParticipantFristName.length() > 30)
{
log.warning("Length > 30 - truncated");
InsuranceParticipantFristName = InsuranceParticipantFristName.substring(0,29);
}
set_Value ("InsuranceParticipantFristName", InsuranceParticipantFristName);
}
/** Get InsuranceParticipantFristName.
@return InsuranceParticipantFristName */
public String getInsuranceParticipantFristName() 
{
return (String)get_Value("InsuranceParticipantFristName");
}
/** Set InsuranceParticipantLastName.
@param InsuranceParticipantLastName InsuranceParticipantLastName */
public void setInsuranceParticipantLastName (String InsuranceParticipantLastName)
{
if (InsuranceParticipantLastName != null && InsuranceParticipantLastName.length() > 30)
{
log.warning("Length > 30 - truncated");
InsuranceParticipantLastName = InsuranceParticipantLastName.substring(0,29);
}
set_Value ("InsuranceParticipantLastName", InsuranceParticipantLastName);
}
/** Get InsuranceParticipantLastName.
@return InsuranceParticipantLastName */
public String getInsuranceParticipantLastName() 
{
return (String)get_Value("InsuranceParticipantLastName");
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
/** Set NoParticipant.
@param NoParticipant NoParticipant */
public void setNoParticipant (String NoParticipant)
{
if (NoParticipant != null && NoParticipant.length() > 30)
{
log.warning("Length > 30 - truncated");
NoParticipant = NoParticipant.substring(0,29);
}
set_Value ("NoParticipant", NoParticipant);
}
/** Get NoParticipant.
@return NoParticipant */
public String getNoParticipant() 
{
return (String)get_Value("NoParticipant");
}
/** Set Deduction.
@param P_Deduction_ID   */
public void setP_Deduction_ID (int P_Deduction_ID)
{
if (P_Deduction_ID <= 0) set_Value ("P_Deduction_ID", null);
 else 
set_Value ("P_Deduction_ID", new Integer(P_Deduction_ID));
}
/** Get Deduction.
@return   */
public int getP_Deduction_ID() 
{
Integer ii = (Integer)get_Value("P_Deduction_ID");
if (ii == null) return 0;
return ii.intValue();
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
/** Set Prime.
@param Prime Prime */
public void setPrime (BigDecimal Prime)
{
set_Value ("Prime", Prime);
}
/** Get Prime.
@return Prime */
public BigDecimal getPrime() 
{
BigDecimal bd = (BigDecimal)get_Value("Prime");
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
/** Set Amount protection.
@param ProtectionAmount Amount protection */
public void setProtectionAmount (BigDecimal ProtectionAmount)
{
set_Value ("ProtectionAmount", ProtectionAmount);
}
/** Get Amount protection.
@return Amount protection */
public BigDecimal getProtectionAmount() 
{
BigDecimal bd = (BigDecimal)get_Value("ProtectionAmount");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Situation_familiale.
@param Situation_familiale Situation_familiale */
public void setSituation_familiale (String Situation_familiale)
{
if (Situation_familiale != null && Situation_familiale.length() > 30)
{
log.warning("Length > 30 - truncated");
Situation_familiale = Situation_familiale.substring(0,29);
}
set_Value ("Situation_familiale", Situation_familiale);
}
/** Get Situation_familiale.
@return Situation_familiale */
public String getSituation_familiale() 
{
return (String)get_Value("Situation_familiale");
}
/** Set Smoker.
@param Smoker Smoker */
public void setSmoker (String Smoker)
{
if (Smoker != null && Smoker.length() > 3)
{
log.warning("Length > 3 - truncated");
Smoker = Smoker.substring(0,2);
}
set_Value ("Smoker", Smoker);
}
/** Get Smoker.
@return Smoker */
public String getSmoker() 
{
return (String)get_Value("Smoker");
}
/** Set SmokerParticipant.
@param SmokerParticipant SmokerParticipant */
public void setSmokerParticipant (String SmokerParticipant)
{
if (SmokerParticipant != null && SmokerParticipant.length() > 3)
{
log.warning("Length > 3 - truncated");
SmokerParticipant = SmokerParticipant.substring(0,2);
}
set_Value ("SmokerParticipant", SmokerParticipant);
}
/** Get SmokerParticipant.
@return SmokerParticipant */
public String getSmokerParticipant() 
{
return (String)get_Value("SmokerParticipant");
}
/** Set TaxeAmount.
@param TaxeAmount TaxeAmount */
public void setTaxeAmount (BigDecimal TaxeAmount)
{
set_Value ("TaxeAmount", TaxeAmount);
}
/** Get TaxeAmount.
@return TaxeAmount */
public BigDecimal getTaxeAmount() 
{
BigDecimal bd = (BigDecimal)get_Value("TaxeAmount");
if (bd == null) return Env.ZERO;
return bd;
}
}
