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
/** Generated Model for P_Employee_Insurance
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Employee_Insurance extends PO
{
/** Standard Constructor
@param ctx context
@param P_Employee_Insurance_ID id
@param trxName transaction
*/
public X_P_Employee_Insurance (Properties ctx, int P_Employee_Insurance_ID, String trxName)
{
super (ctx, P_Employee_Insurance_ID, trxName);
/** if (P_Employee_Insurance_ID == 0)
{
setEffectIn (new Timestamp(System.currentTimeMillis()));	// @SQL=SELECT StartDate AS DefaultValue FROM P_Period WHERE PeriodStatus='O' 
setGender (null);
setInsuranceParticipant (null);
setIsSmoker (false);	// N
setP_Deduction_ID (0);
setP_Employee_ID (0);
setP_Employee_Insurance_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Employee_Insurance (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000091 */
public static final int Table_ID=2000091;

/** TableName=P_Employee_Insurance */
public static final String Table_Name="P_Employee_Insurance";

protected static KeyNamePair Model = new KeyNamePair(2000091,"P_Employee_Insurance");

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
StringBuffer sb = new StringBuffer ("X_P_Employee_Insurance[").append(get_ID()).append("]");
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
/** Set Effect In.
@param EffectIn Date when information in effect  */
public void setEffectIn (Timestamp EffectIn)
{
if (EffectIn == null) throw new IllegalArgumentException ("EffectIn is mandatory.");
set_Value ("EffectIn", EffectIn);
}
/** Get Effect In.
@return Date when information in effect  */
public Timestamp getEffectIn() 
{
return (Timestamp)get_Value("EffectIn");
}
/** Set To.
@param EffectTo To */
public void setEffectTo (Timestamp EffectTo)
{
set_Value ("EffectTo", EffectTo);
}
/** Get To.
@return To */
public Timestamp getEffectTo() 
{
return (Timestamp)get_Value("EffectTo");
}

/** Gender AD_Reference_ID=2000048 */
public static final int GENDER_AD_Reference_ID=2000048;
/** Man = M */
public static final String GENDER_Man = "M";
/** Woman = F */
public static final String GENDER_Woman = "F";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isGenderValid (String test)
{
return test.equals("M") || test.equals("F");
}
/** Set Gender.
@param Gender Gender  */
public void setGender (String Gender)
{
if (Gender == null) throw new IllegalArgumentException ("Gender is mandatory");
if (!isGenderValid(Gender))
throw new IllegalArgumentException ("Gender Invalid value - " + Gender + " - Reference_ID=2000048 - M - F");
if (Gender.length() > 1)
{
log.warning("Length > 1 - truncated");
Gender = Gender.substring(0,0);
}
set_Value ("Gender", Gender);
}
/** Get Gender.
@return Gender  */
public String getGender() 
{
return (String)get_Value("Gender");
}

/** InsuranceParticipant AD_Reference_ID=2000040 */
public static final int INSURANCEPARTICIPANT_AD_Reference_ID=2000040;
/** Familly = 4 */
public static final String INSURANCEPARTICIPANT_Familly = "4";
/** Salaried = 1 */
public static final String INSURANCEPARTICIPANT_Salaried = "1";
/** Conjoint = 2 */
public static final String INSURANCEPARTICIPANT_Conjoint = "2";
/** Child = 3 */
public static final String INSURANCEPARTICIPANT_Child = "3";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isInsuranceParticipantValid (String test)
{
return test.equals("4") || test.equals("1") || test.equals("2") || test.equals("3");
}
/** Set Participant.
@param InsuranceParticipant Participant */
public void setInsuranceParticipant (String InsuranceParticipant)
{
if (InsuranceParticipant == null) throw new IllegalArgumentException ("InsuranceParticipant is mandatory");
if (!isInsuranceParticipantValid(InsuranceParticipant))
throw new IllegalArgumentException ("InsuranceParticipant Invalid value - " + InsuranceParticipant + " - Reference_ID=2000040 - 4 - 1 - 2 - 3");
if (InsuranceParticipant.length() > 1)
{
log.warning("Length > 1 - truncated");
InsuranceParticipant = InsuranceParticipant.substring(0,0);
}
set_Value ("InsuranceParticipant", InsuranceParticipant);
}
/** Get Participant.
@return Participant */
public String getInsuranceParticipant() 
{
return (String)get_Value("InsuranceParticipant");
}
/** Get Record ID/ColumnName
@return ID/ColumnName pair */
public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), String.valueOf(getInsuranceParticipant()));
}
/** Set Smoker.
@param IsSmoker Smoker */
public void setIsSmoker (boolean IsSmoker)
{
set_Value ("IsSmoker", new Boolean(IsSmoker));
}
/** Get Smoker.
@return Smoker */
public boolean isSmoker() 
{
Object oo = get_Value("IsSmoker");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Multiply Rate.
@param MultiplyRate Rate to multiple the source by to calculate the target. */
public void setMultiplyRate (BigDecimal MultiplyRate)
{
set_Value ("MultiplyRate", MultiplyRate);
}
/** Get Multiply Rate.
@return Rate to multiple the source by to calculate the target. */
public BigDecimal getMultiplyRate() 
{
BigDecimal bd = (BigDecimal)get_Value("MultiplyRate");
if (bd == null) return Env.ZERO;
return bd;
}

/** P_Deduction_ID AD_Reference_ID=2000039 */
public static final int P_DEDUCTION_ID_AD_Reference_ID=2000039;
/** Set Deduction.
@param P_Deduction_ID   */
public void setP_Deduction_ID (int P_Deduction_ID)
{
if (P_Deduction_ID < 1) throw new IllegalArgumentException ("P_Deduction_ID is mandatory.");
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

/** P_Employee_ID AD_Reference_ID=2000010 */
public static final int P_EMPLOYEE_ID_AD_Reference_ID=2000010;
/** Set Employee .
@param P_Employee_ID Employee */
public void setP_Employee_ID (int P_Employee_ID)
{
if (P_Employee_ID < 1) throw new IllegalArgumentException ("P_Employee_ID is mandatory.");
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
/** Set Employee Insurance ID.
@param P_Employee_Insurance_ID Employee Insurance ID */
public void setP_Employee_Insurance_ID (int P_Employee_Insurance_ID)
{
if (P_Employee_Insurance_ID < 1) throw new IllegalArgumentException ("P_Employee_Insurance_ID is mandatory.");
set_Value ("P_Employee_Insurance_ID", new Integer(P_Employee_Insurance_ID));
}
/** Get Employee Insurance ID.
@return Employee Insurance ID */
public int getP_Employee_Insurance_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Insurance_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Taxable_Benefit_ID AD_Reference_ID=2000046 */
public static final int P_TAXABLE_BENEFIT_ID_AD_Reference_ID=2000046;
/** Set Taxable Benefit.
@param P_Taxable_Benefit_ID Taxable Benefit */
public void setP_Taxable_Benefit_ID (int P_Taxable_Benefit_ID)
{
if (P_Taxable_Benefit_ID <= 0) set_Value ("P_Taxable_Benefit_ID", null);
 else 
set_Value ("P_Taxable_Benefit_ID", new Integer(P_Taxable_Benefit_ID));
}
/** Get Taxable Benefit.
@return Taxable Benefit */
public int getP_Taxable_Benefit_ID() 
{
Integer ii = (Integer)get_Value("P_Taxable_Benefit_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Participant Name.
@param Participant_Name Participant Name */
public void setParticipant_Name (String Participant_Name)
{
if (Participant_Name != null && Participant_Name.length() > 60)
{
log.warning("Length > 60 - truncated");
Participant_Name = Participant_Name.substring(0,59);
}
set_Value ("Participant_Name", Participant_Name);
}
/** Get Participant Name.
@return Participant Name */
public String getParticipant_Name() 
{
return (String)get_Value("Participant_Name");
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
/** Set Fixed Amount.
@param TaxB_Fixed_Amount Fixed Amount */
public void setTaxB_Fixed_Amount (BigDecimal TaxB_Fixed_Amount)
{
set_Value ("TaxB_Fixed_Amount", TaxB_Fixed_Amount);
}
/** Get Fixed Amount.
@return Fixed Amount */
public BigDecimal getTaxB_Fixed_Amount() 
{
BigDecimal bd = (BigDecimal)get_Value("TaxB_Fixed_Amount");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Rate.
@param TaxB_Rate Rate */
public void setTaxB_Rate (BigDecimal TaxB_Rate)
{
set_Value ("TaxB_Rate", TaxB_Rate);
}
/** Get Rate.
@return Rate */
public BigDecimal getTaxB_Rate() 
{
BigDecimal bd = (BigDecimal)get_Value("TaxB_Rate");
if (bd == null) return Env.ZERO;
return bd;
}
}
