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
/** Generated Model for RV_P_Employee_Deduction_2
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: X_RV_P_Employee_Deduction_2.java,v 1.1 2007/07/18 14:43:31 marmor01 Exp $ */
public class X_RV_P_Employee_Deduction_2 extends PO
{
/** Standard Constructor
@param ctx context
@param RV_P_Employee_Deduction_2_ID id
@param trxName transaction
*/
public X_RV_P_Employee_Deduction_2 (Properties ctx, int RV_P_Employee_Deduction_2_ID, String trxName)
{
super (ctx, RV_P_Employee_Deduction_2_ID, trxName);
/** if (RV_P_Employee_Deduction_2_ID == 0)
{
setEffectIn (new Timestamp(System.currentTimeMillis()));
setP_Deduction_ID (0);
setP_Employee_ID (0);
setRV_P_Employee_Deduction_2_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_RV_P_Employee_Deduction_2 (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100822 */
public static final int Table_ID=2100822;

/** TableName=RV_P_Employee_Deduction_2 */
public static final String Table_Name="RV_P_Employee_Deduction_2";

protected static KeyNamePair Model = new KeyNamePair(2100822,"RV_P_Employee_Deduction_2");

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
StringBuffer sb = new StringBuffer ("X_RV_P_Employee_Deduction_2[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Back Payment.
@param BackPayment Back Payment */
public void setBackPayment (BigDecimal BackPayment)
{
set_Value ("BackPayment", BackPayment);
}
/** Get Back Payment.
@return Back Payment */
public BigDecimal getBackPayment() 
{
BigDecimal bd = (BigDecimal)get_Value("BackPayment");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Back Payment by Period.
@param BackPaymentPeriod Back Payment by Period */
public void setBackPaymentPeriod (BigDecimal BackPaymentPeriod)
{
set_Value ("BackPaymentPeriod", BackPaymentPeriod);
}
/** Get Back Payment by Period.
@return Back Payment by Period */
public BigDecimal getBackPaymentPeriod() 
{
BigDecimal bd = (BigDecimal)get_Value("BackPaymentPeriod");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Date 35 Year.
@param Date35 Date ou l'employé atteint 35 ans d'année de service */
public void setDate35 (Timestamp Date35)
{
set_Value ("Date35", Date35);
}
/** Get Date 35 Year.
@return Date ou l'employé atteint 35 ans d'année de service */
public Timestamp getDate35() 
{
return (Timestamp)get_Value("Date35");
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
/** Set Employee amount.
@param EmployeeAmount Employee amount */
public void setEmployeeAmount (BigDecimal EmployeeAmount)
{
set_Value ("EmployeeAmount", EmployeeAmount);
}
/** Get Employee amount.
@return Employee amount */
public BigDecimal getEmployeeAmount() 
{
BigDecimal bd = (BigDecimal)get_Value("EmployeeAmount");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Employee rate.
@param EmployeeRate Employee rate */
public void setEmployeeRate (BigDecimal EmployeeRate)
{
set_Value ("EmployeeRate", EmployeeRate);
}
/** Get Employee rate.
@return Employee rate */
public BigDecimal getEmployeeRate() 
{
BigDecimal bd = (BigDecimal)get_Value("EmployeeRate");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Employer amount.
@param EmployerAmount Employer amount */
public void setEmployerAmount (BigDecimal EmployerAmount)
{
set_Value ("EmployerAmount", EmployerAmount);
}
/** Get Employer amount.
@return Employer amount */
public BigDecimal getEmployerAmount() 
{
BigDecimal bd = (BigDecimal)get_Value("EmployerAmount");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Employer rate.
@param EmployerRate Employer rate */
public void setEmployerRate (BigDecimal EmployerRate)
{
set_Value ("EmployerRate", EmployerRate);
}
/** Get Employer rate.
@return Employer rate */
public BigDecimal getEmployerRate() 
{
BigDecimal bd = (BigDecimal)get_Value("EmployerRate");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Exemption Amount.
@param ExemptionAmount Exemption Amount */
public void setExemptionAmount (BigDecimal ExemptionAmount)
{
set_Value ("ExemptionAmount", ExemptionAmount);
}
/** Get Exemption Amount.
@return Exemption Amount */
public BigDecimal getExemptionAmount() 
{
BigDecimal bd = (BigDecimal)get_Value("ExemptionAmount");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Gender.
@param Gender Gender  */
public void setGender (boolean Gender)
{
set_Value ("Gender", new Boolean(Gender));
}
/** Get Gender.
@return Gender  */
public boolean isGender() 
{
Object oo = get_Value("Gender");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Participant.
@param InsuranceParticipant Participant */
public void setInsuranceParticipant (boolean InsuranceParticipant)
{
set_Value ("InsuranceParticipant", new Boolean(InsuranceParticipant));
}
/** Get Participant.
@return Participant */
public boolean isInsuranceParticipant() 
{
Object oo = get_Value("InsuranceParticipant");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Exonerated.
@param IsExonerated Exonerated */
public void setIsExonerated (boolean IsExonerated)
{
set_Value ("IsExonerated", new Boolean(IsExonerated));
}
/** Get Exonerated.
@return Exonerated */
public boolean isExonerated() 
{
Object oo = get_Value("IsExonerated");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
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
/** Set Max Yearly.
@param MaxYearly Max Yearly */
public void setMaxYearly (BigDecimal MaxYearly)
{
set_Value ("MaxYearly", MaxYearly);
}
/** Get Max Yearly.
@return Max Yearly */
public BigDecimal getMaxYearly() 
{
BigDecimal bd = (BigDecimal)get_Value("MaxYearly");
if (bd == null) return Env.ZERO;
return bd;
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
/** Set Non Annual Maximum.
@param NonAnnualMaximum All time maximum */
public void setNonAnnualMaximum (BigDecimal NonAnnualMaximum)
{
set_Value ("NonAnnualMaximum", NonAnnualMaximum);
}
/** Get Non Annual Maximum.
@return All time maximum */
public BigDecimal getNonAnnualMaximum() 
{
BigDecimal bd = (BigDecimal)get_Value("NonAnnualMaximum");
if (bd == null) return Env.ZERO;
return bd;
}
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
/** Set RV_P_Employee_Deduction_2_ID.
@param RV_P_Employee_Deduction_2_ID RV_P_Employee_Deduction_2_ID */
public void setRV_P_Employee_Deduction_2_ID (int RV_P_Employee_Deduction_2_ID)
{
if (RV_P_Employee_Deduction_2_ID < 1) throw new IllegalArgumentException ("RV_P_Employee_Deduction_2_ID is mandatory.");
set_ValueNoCheck ("RV_P_Employee_Deduction_2_ID", new Integer(RV_P_Employee_Deduction_2_ID));
}
/** Get RV_P_Employee_Deduction_2_ID.
@return RV_P_Employee_Deduction_2_ID */
public int getRV_P_Employee_Deduction_2_ID() 
{
Integer ii = (Integer)get_Value("RV_P_Employee_Deduction_2_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
