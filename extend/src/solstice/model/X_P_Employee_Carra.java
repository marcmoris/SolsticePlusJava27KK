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
/** Generated Model for P_Employee_Carra
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Employee_Carra extends PO
{
/** Standard Constructor
@param ctx context
@param P_Employee_Carra_ID id
@param trxName transaction
*/
public X_P_Employee_Carra (Properties ctx, int P_Employee_Carra_ID, String trxName)
{
super (ctx, P_Employee_Carra_ID, trxName);
/** if (P_Employee_Carra_ID == 0)
{
setP_Employee_CARRA_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Employee_Carra (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100799 */
public static final int Table_ID=2100799;

/** TableName=P_Employee_Carra */
public static final String Table_Name="P_Employee_Carra";

protected static KeyNamePair Model = new KeyNamePair(2100799,"P_Employee_Carra");

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
StringBuffer sb = new StringBuffer ("X_P_Employee_Carra[").append(get_ID()).append("]");
return sb.toString();
}
/** Set AdjustedLeaveOfAbsenceDays.
@param AdjustedLeaveOfAbsenceDays AdjustedLeaveOfAbsenceDays */
public void setAdjustedLeaveOfAbsenceDays (BigDecimal AdjustedLeaveOfAbsenceDays)
{
set_Value ("AdjustedLeaveOfAbsenceDays", AdjustedLeaveOfAbsenceDays);
}
/** Get AdjustedLeaveOfAbsenceDays.
@return AdjustedLeaveOfAbsenceDays */
public BigDecimal getAdjustedLeaveOfAbsenceDays() 
{
BigDecimal bd = (BigDecimal)get_Value("AdjustedLeaveOfAbsenceDays");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Adjustment Factor.
@param AdjustmentFactor Adjustment Factor */
public void setAdjustmentFactor (BigDecimal AdjustmentFactor)
{
set_Value ("AdjustmentFactor", AdjustmentFactor);
}
/** Get Adjustment Factor.
@return Adjustment Factor */
public BigDecimal getAdjustmentFactor() 
{
BigDecimal bd = (BigDecimal)get_Value("AdjustmentFactor");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Adjustment Factor Amount.
@param AdjustmentFactorAmount Adjustment Factor Amount */
public void setAdjustmentFactorAmount (BigDecimal AdjustmentFactorAmount)
{
set_Value ("AdjustmentFactorAmount", AdjustmentFactorAmount);
}
/** Get Adjustment Factor Amount.
@return Adjustment Factor Amount */
public BigDecimal getAdjustmentFactorAmount() 
{
BigDecimal bd = (BigDecimal)get_Value("AdjustmentFactorAmount");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Adjustment Factor STD Amount.
@param AdjustmentFactorSTDAmount Adjustment Factor STD Amount */
public void setAdjustmentFactorSTDAmount (BigDecimal AdjustmentFactorSTDAmount)
{
set_Value ("AdjustmentFactorSTDAmount", AdjustmentFactorSTDAmount);
}
/** Get Adjustment Factor STD Amount.
@return Adjustment Factor STD Amount */
public BigDecimal getAdjustmentFactorSTDAmount() 
{
BigDecimal bd = (BigDecimal)get_Value("AdjustmentFactorSTDAmount");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set AssignmentQty.
@param AssignmentQty AssignmentQty */
public void setAssignmentQty (BigDecimal AssignmentQty)
{
set_Value ("AssignmentQty", AssignmentQty);
}
/** Get AssignmentQty.
@return AssignmentQty */
public BigDecimal getAssignmentQty() 
{
BigDecimal bd = (BigDecimal)get_Value("AssignmentQty");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Base Salary.
@param BaseSalary Base Salary */
public void setBaseSalary (BigDecimal BaseSalary)
{
set_Value ("BaseSalary", BaseSalary);
}
/** Get Base Salary.
@return Base Salary */
public BigDecimal getBaseSalary() 
{
BigDecimal bd = (BigDecimal)get_Value("BaseSalary");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Processed Calcul FE.
@param CalculFE Processed Calcul FE */
public void setCalculFE (String CalculFE)
{
set_Value ("CalculFE", CalculFE);
}
/** Get Processed Calcul FE.
@return Processed Calcul FE */
public String getCalculFE() 
{
return (String)get_Value("CalculFE");
}
/** Set Computed Service.
@param ComputedService Computed Service */
public void setComputedService (BigDecimal ComputedService)
{
set_Value ("ComputedService", ComputedService);
}
/** Get Computed Service.
@return Computed Service */
public BigDecimal getComputedService() 
{
BigDecimal bd = (BigDecimal)get_Value("ComputedService");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Contributory Days.
@param ContributoryDays Contributory Days */
public void setContributoryDays (BigDecimal ContributoryDays)
{
set_Value ("ContributoryDays", ContributoryDays);
}
/** Get Contributory Days.
@return Contributory Days */
public BigDecimal getContributoryDays() 
{
BigDecimal bd = (BigDecimal)get_Value("ContributoryDays");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Contributory Salary.
@param ContributorySalary Contributory Salary */
public void setContributorySalary (BigDecimal ContributorySalary)
{
set_Value ("ContributorySalary", ContributorySalary);
}
/** Get Contributory Salary.
@return Contributory Salary */
public BigDecimal getContributorySalary() 
{
BigDecimal bd = (BigDecimal)get_Value("ContributorySalary");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Credited Service.
@param CreditedService Credited Service */
public void setCreditedService (BigDecimal CreditedService)
{
set_Value ("CreditedService", CreditedService);
}
/** Get Credited Service.
@return Credited Service */
public BigDecimal getCreditedService() 
{
BigDecimal bd = (BigDecimal)get_Value("CreditedService");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Deductible Salary.
@param DeductibleSalary Deductible Salary */
public void setDeductibleSalary (BigDecimal DeductibleSalary)
{
set_Value ("DeductibleSalary", DeductibleSalary);
}
/** Get Deductible Salary.
@return Deductible Salary */
public BigDecimal getDeductibleSalary() 
{
BigDecimal bd = (BigDecimal)get_Value("DeductibleSalary");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Disability Days.
@param DisabilityDays Disability Days */
public void setDisabilityDays (BigDecimal DisabilityDays)
{
set_Value ("DisabilityDays", DisabilityDays);
}
/** Get Disability Days.
@return Disability Days */
public BigDecimal getDisabilityDays() 
{
BigDecimal bd = (BigDecimal)get_Value("DisabilityDays");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Effect In.
@param EffectIn Date when information in effect  */
public void setEffectIn (Timestamp EffectIn)
{
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
/** Set End Date Carra.
@param EffectToCarra End Date Carra */
public void setEffectToCarra (String EffectToCarra)
{
throw new IllegalArgumentException ("EffectToCarra is virtual column");
}
/** Get End Date Carra.
@return End Date Carra */
public String getEffectToCarra() 
{
return (String)get_Value("EffectToCarra");
}
/** Set Employee Part.
@param Employee_Part Employee Part */
public void setEmployee_Part (BigDecimal Employee_Part)
{
set_Value ("Employee_Part", Employee_Part);
}
/** Get Employee Part.
@return Employee Part */
public BigDecimal getEmployee_Part() 
{
BigDecimal bd = (BigDecimal)get_Value("Employee_Part");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Exonerated Salary.
@param ExoneratedSalary Exonerated Salary */
public void setExoneratedSalary (BigDecimal ExoneratedSalary)
{
set_Value ("ExoneratedSalary", ExoneratedSalary);
}
/** Get Exonerated Salary.
@return Exonerated Salary */
public BigDecimal getExoneratedSalary() 
{
BigDecimal bd = (BigDecimal)get_Value("ExoneratedSalary");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Final version.
@param IsDone Final version */
public void setIsDone (boolean IsDone)
{
set_Value ("IsDone", new Boolean(IsDone));
}
/** Get Final version.
@return Final version */
public boolean isDone() 
{
Object oo = get_Value("IsDone");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Leave Of Absence Without Pay.
@param LeaveOfAbsenceWithoutPay Leave Of Absence Without Pay */
public void setLeaveOfAbsenceWithoutPay (BigDecimal LeaveOfAbsenceWithoutPay)
{
set_Value ("LeaveOfAbsenceWithoutPay", LeaveOfAbsenceWithoutPay);
}
/** Get Leave Of Absence Without Pay.
@return Leave Of Absence Without Pay */
public BigDecimal getLeaveOfAbsenceWithoutPay() 
{
BigDecimal bd = (BigDecimal)get_Value("LeaveOfAbsenceWithoutPay");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Missing Retro.
@param ManualRetro Missing Retro */
public void setManualRetro (BigDecimal ManualRetro)
{
set_Value ("ManualRetro", ManualRetro);
}
/** Get Missing Retro.
@return Missing Retro */
public BigDecimal getManualRetro() 
{
BigDecimal bd = (BigDecimal)get_Value("ManualRetro");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Motherhood Days.
@param MotherHoodDays Motherhood Days */
public void setMotherHoodDays (BigDecimal MotherHoodDays)
{
set_Value ("MotherHoodDays", MotherHoodDays);
}
/** Get Motherhood Days.
@return Motherhood Days */
public BigDecimal getMotherHoodDays() 
{
BigDecimal bd = (BigDecimal)get_Value("MotherHoodDays");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Motherhood Salary.
@param MotherHoodSalary Motherhood Salary */
public void setMotherHoodSalary (BigDecimal MotherHoodSalary)
{
set_Value ("MotherHoodSalary", MotherHoodSalary);
}
/** Get Motherhood Salary.
@return Motherhood Salary */
public BigDecimal getMotherHoodSalary() 
{
BigDecimal bd = (BigDecimal)get_Value("MotherHoodSalary");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Non Contributory Salary.
@param NonContributorySalary Non Contributory Salary */
public void setNonContributorySalary (BigDecimal NonContributorySalary)
{
set_Value ("NonContributorySalary", NonContributorySalary);
}
/** Get Non Contributory Salary.
@return Non Contributory Salary */
public BigDecimal getNonContributorySalary() 
{
BigDecimal bd = (BigDecimal)get_Value("NonContributorySalary");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Number Of Day.
@param NumberOfDay Number Of Day */
public void setNumberOfDay (BigDecimal NumberOfDay)
{
set_Value ("NumberOfDay", NumberOfDay);
}
/** Get Number Of Day.
@return Number Of Day */
public BigDecimal getNumberOfDay() 
{
BigDecimal bd = (BigDecimal)get_Value("NumberOfDay");
if (bd == null) return Env.ZERO;
return bd;
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
/** Set P_Employee_CARRA_ID.
@param P_Employee_CARRA_ID P_Employee_CARRA_ID */
public void setP_Employee_CARRA_ID (int P_Employee_CARRA_ID)
{
if (P_Employee_CARRA_ID < 1) throw new IllegalArgumentException ("P_Employee_CARRA_ID is mandatory.");
set_ValueNoCheck ("P_Employee_CARRA_ID", new Integer(P_Employee_CARRA_ID));
}
/** Get P_Employee_CARRA_ID.
@return P_Employee_CARRA_ID */
public int getP_Employee_CARRA_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_CARRA_ID");
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
/** Set Job Type.
@param P_Job_Type_ID   */
public void setP_Job_Type_ID (int P_Job_Type_ID)
{
if (P_Job_Type_ID <= 0) set_Value ("P_Job_Type_ID", null);
 else 
set_Value ("P_Job_Type_ID", new Integer(P_Job_Type_ID));
}
/** Get Job Type.
@return   */
public int getP_Job_Type_ID() 
{
Integer ii = (Integer)get_Value("P_Job_Type_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Period.
@param P_Period_ID Period of the Calendar  */
public void setP_Period_ID (int P_Period_ID)
{
if (P_Period_ID <= 0) set_Value ("P_Period_ID", null);
 else 
set_Value ("P_Period_ID", new Integer(P_Period_ID));
}
/** Get Period.
@return Period of the Calendar  */
public int getP_Period_ID() 
{
Integer ii = (Integer)get_Value("P_Period_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_RWT_ID AD_Reference_ID=2100369 */
public static final int P_RWT_ID_AD_Reference_ID=2100369;
/** Set No. Regime.
@param P_RWT_ID No. Regime */
public void setP_RWT_ID (int P_RWT_ID)
{
if (P_RWT_ID <= 0) set_Value ("P_RWT_ID", null);
 else 
set_Value ("P_RWT_ID", new Integer(P_RWT_ID));
}
/** Get No. Regime.
@return No. Regime */
public int getP_RWT_ID() 
{
Integer ii = (Integer)get_Value("P_RWT_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Year_ID AD_Reference_ID=2000012 */
public static final int P_YEAR_ID_AD_Reference_ID=2000012;
/** Set Year.
@param P_Year_ID Calendar Year */
public void setP_Year_ID (int P_Year_ID)
{
if (P_Year_ID <= 0) set_Value ("P_Year_ID", null);
 else 
set_Value ("P_Year_ID", new Integer(P_Year_ID));
}
/** Get Year.
@return Calendar Year */
public int getP_Year_ID() 
{
Integer ii = (Integer)get_Value("P_Year_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Record Type.
@param RecordType Record Type */
public void setRecordType (String RecordType)
{
if (RecordType != null && RecordType.length() > 1)
{
log.warning("Length > 1 - truncated");
RecordType = RecordType.substring(0,0);
}
set_Value ("RecordType", RecordType);
}
/** Get Record Type.
@return Record Type */
public String getRecordType() 
{
return (String)get_Value("RecordType");
}
/** Set Self Financed Leave Days.
@param SelfFinancedLeaveDays Self Financed Leave Days */
public void setSelfFinancedLeaveDays (BigDecimal SelfFinancedLeaveDays)
{
set_Value ("SelfFinancedLeaveDays", SelfFinancedLeaveDays);
}
/** Get Self Financed Leave Days.
@return Self Financed Leave Days */
public BigDecimal getSelfFinancedLeaveDays() 
{
BigDecimal bd = (BigDecimal)get_Value("SelfFinancedLeaveDays");
if (bd == null) return Env.ZERO;
return bd;
}

/** Status AD_Reference_ID=2100447 */
public static final int STATUS_AD_Reference_ID=2100447;
/** Employé non visé = 1 */
public static final String STATUS_EmployéNonVisé = "1";
/** Employé en libération syndicale = 5 */
public static final String STATUS_EmployéEnLibérationSyndicale = "5";
/** Enseignant pré-retraite = 6 */
public static final String STATUS_EnseignantPré_Retraite = "6";
/** Congé sabatique, traitement différé = 7 */
public static final String STATUS_CongéSabatiqueTraitementDifféré = "7";
/** Enseignant mis en disponibilité = 8 */
public static final String STATUS_EnseignantMisEnDisponibilité = "8";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isStatusValid (String test)
{
return test == null || test.equals("1") || test.equals("5") || test.equals("6") || test.equals("7") || test.equals("8");
}
/** Set Status.
@param Status Status */
public void setStatus (String Status)
{
if (!isStatusValid(Status))
throw new IllegalArgumentException ("Status Invalid value - " + Status + " - Reference_ID=2100447 - 1 - 5 - 6 - 7 - 8");
if (Status != null && Status.length() > 1)
{
log.warning("Length > 1 - truncated");
Status = Status.substring(0,0);
}
set_Value ("Status", Status);
}
/** Get Status.
@return Status */
public String getStatus() 
{
return (String)get_Value("Status");
}
/** Set Theorical Retro.
@param TheoricalRetro Theorical Retro */
public void setTheoricalRetro (BigDecimal TheoricalRetro)
{
set_Value ("TheoricalRetro", TheoricalRetro);
}
/** Get Theorical Retro.
@return Theorical Retro */
public BigDecimal getTheoricalRetro() 
{
BigDecimal bd = (BigDecimal)get_Value("TheoricalRetro");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Worked Time Percentage.
@param WorkedTimePercentage Worked Time Percentage */
public void setWorkedTimePercentage (BigDecimal WorkedTimePercentage)
{
set_Value ("WorkedTimePercentage", WorkedTimePercentage);
}
/** Get Worked Time Percentage.
@return Worked Time Percentage */
public BigDecimal getWorkedTimePercentage() 
{
BigDecimal bd = (BigDecimal)get_Value("WorkedTimePercentage");
if (bd == null) return Env.ZERO;
return bd;
}
}
