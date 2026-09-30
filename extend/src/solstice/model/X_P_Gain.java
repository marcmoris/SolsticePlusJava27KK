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
/** Generated Model for P_Gain
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Gain extends PO
{
/** Standard Constructor
@param ctx context
@param P_Gain_ID id
@param trxName transaction
*/
public X_P_Gain (Properties ctx, int P_Gain_ID, String trxName)
{
super (ctx, P_Gain_ID, trxName);
/** if (P_Gain_ID == 0)
{
setEffectIn (new Timestamp(System.currentTimeMillis()));
setIsSummary (false);
setName (null);
setP_Gain_Family_ID (0);
setP_Gain_ID (0);
setP_Method_Gain_ID (0);
setP_UOM_ID (0);
setValue (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Gain (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000005 */
public static final int Table_ID=2000005;

/** TableName=P_Gain */
public static final String Table_Name="P_Gain";

protected static KeyNamePair Model = new KeyNamePair(2000005,"P_Gain");

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
StringBuffer sb = new StringBuffer ("X_P_Gain[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Accessible.
@param Accessible Accessible */
public void setAccessible (boolean Accessible)
{
set_Value ("Accessible", new Boolean(Accessible));
}
/** Get Accessible.
@return Accessible */
public boolean isAccessible() 
{
Object oo = get_Value("Accessible");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Bonus Amount.
@param Bonus_Amount Bonus Amount */
public void setBonus_Amount (BigDecimal Bonus_Amount)
{
set_Value ("Bonus_Amount", Bonus_Amount);
}
/** Get Bonus Amount.
@return Bonus Amount */
public BigDecimal getBonus_Amount() 
{
BigDecimal bd = (BigDecimal)get_Value("Bonus_Amount");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Color.
@param Color Color */
public void setColor (String Color)
{
if (Color != null && Color.length() > 11)
{
log.warning("Length > 11 - truncated");
Color = Color.substring(0,10);
}
set_Value ("Color", Color);
}
/** Get Color.
@return Color */
public String getColor() 
{
return (String)get_Value("Color");
}
/** Set Description.
@param Description Optional short description of the record */
public void setDescription (String Description)
{
if (Description != null && Description.length() > 2000)
{
log.warning("Length > 2000 - truncated");
Description = Description.substring(0,1999);
}
set_Value ("Description", Description);
}
/** Get Description.
@return Optional short description of the record */
public String getDescription() 
{
return (String)get_Value("Description");
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
/** Set Fixed Amount.
@param Fixed_Amount   */
public void setFixed_Amount (BigDecimal Fixed_Amount)
{
set_Value ("Fixed_Amount", Fixed_Amount);
}
/** Get Fixed Amount.
@return   */
public BigDecimal getFixed_Amount() 
{
BigDecimal bd = (BigDecimal)get_Value("Fixed_Amount");
if (bd == null) return Env.ZERO;
return bd;
}

/** Flag_CARRA AD_Reference_ID=2100451 */
public static final int FLAG_CARRA_AD_Reference_ID=2100451;
/** AIN - Jours invalidité non cotisé = AIN */
public static final String FLAG_CARRA_AIN_JoursInvaliditéNonCotisé = "AIN";
public static final String FLAG_CARRA_AIN_Non_ContributoryDisabilityDays  = "AIN";
/** AMA - Jour absence congé maternité = AMA */
public static final String FLAG_CARRA_AMA_JourAbsenceCongéMaternité = "AMA";
public static final String FLAG_CARRA_AMA_MaternityLeaveDaysOff  = "AMA";
/** ANP - Jours absence non payés = ANP */
public static final String FLAG_CARRA_ANP_JoursAbsenceNonPayés = "ANP";
public static final String FLAG_CARRA_ANP_Non_PaidDays = "ANP";
/** ATRD - Accumulation TD = ATRD */
public static final String FLAG_CARRA_ATRD_AccumulationTD = "ATRD";
/** CTRD - Congé traitement différé = CTRD */
public static final String FLAG_CARRA_CTRD_CongéTraitementDifféré = "CTRD";
/** RASS - Rétro. sur assurance salaire = RASS */
public static final String FLAG_CARRA_RASS_RétroSurAssuranceSalaire = "RASS";
public static final String FLAG_CARRA_RASS_InsuranceRetroOnSalary = "RASS";
/** RSAL - Retro. sur salaire = RSAL */
public static final String FLAG_CARRA_RSAL_RetroSurSalaire = "RSAL";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isFlag_CARRAValid (String test)
{
return test == null || test.equals("AIN") || test.equals("AMA") || test.equals("ANP") || test.equals("ATRD") || test.equals("CTRD") || test.equals("RASS") || test.equals("RSAL");
}
/** Set Flag CARRA.
@param Flag_CARRA Flag CARRA */
public void setFlag_CARRA (String Flag_CARRA)
{
if (!isFlag_CARRAValid(Flag_CARRA))
throw new IllegalArgumentException ("Flag_CARRA Invalid value - " + Flag_CARRA + " - Reference_ID=2100451 - AIN - AMA - ANP - ATRD - CTRD - RASS - RSAL");
if (Flag_CARRA != null && Flag_CARRA.length() > 10)
{
log.warning("Length > 10 - truncated");
Flag_CARRA = Flag_CARRA.substring(0,9);
}
set_Value ("Flag_CARRA", Flag_CARRA);
}
/** Get Flag CARRA.
@return Flag CARRA */
public String getFlag_CARRA() 
{
return (String)get_Value("Flag_CARRA");
}

/** GainUnit AD_Reference_ID=2100377 */
public static final int GAINUNIT_AD_Reference_ID=2100377;
/** Quantity = Q */
public static final String GAINUNIT_Quantity = "Q";
/** Amount = A */
public static final String GAINUNIT_Amount = "A";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isGainUnitValid (String test)
{
return test == null || test.equals("Q") || test.equals("A");
}
/** Set Unit.
@param GainUnit Unit */
public void setGainUnit (String GainUnit)
{
if (!isGainUnitValid(GainUnit))
throw new IllegalArgumentException ("GainUnit Invalid value - " + GainUnit + " - Reference_ID=2100377 - Q - A");
if (GainUnit != null && GainUnit.length() > 1)
{
log.warning("Length > 1 - truncated");
GainUnit = GainUnit.substring(0,0);
}
set_Value ("GainUnit", GainUnit);
}
/** Get Unit.
@return Unit */
public String getGainUnit() 
{
return (String)get_Value("GainUnit");
}
/** Set Hourly Rate.
@param Hourly_Rate   */
public void setHourly_Rate (BigDecimal Hourly_Rate)
{
set_Value ("Hourly_Rate", Hourly_Rate);
}
/** Get Hourly Rate.
@return   */
public BigDecimal getHourly_Rate() 
{
BigDecimal bd = (BigDecimal)get_Value("Hourly_Rate");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Absence.
@param IsAbsence Absence */
public void setIsAbsence (boolean IsAbsence)
{
set_Value ("IsAbsence", new Boolean(IsAbsence));
}
/** Get Absence.
@return Absence */
public boolean isAbsence() 
{
Object oo = get_Value("IsAbsence");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Detail on the registry.
@param IsDetail_Registry Detail on the registry */
public void setIsDetail_Registry (boolean IsDetail_Registry)
{
set_Value ("IsDetail_Registry", new Boolean(IsDetail_Registry));
}
/** Get Detail on the registry.
@return Detail on the registry */
public boolean isDetail_Registry() 
{
Object oo = get_Value("IsDetail_Registry");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Productive .
@param IsProductive this gain egal a productive ti */
public void setIsProductive (boolean IsProductive)
{
set_Value ("IsProductive", new Boolean(IsProductive));
}
/** Get Productive .
@return this gain egal a productive ti */
public boolean isProductive() 
{
Object oo = get_Value("IsProductive");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set IsRWT.
@param IsRWT IsRWT */
public void setIsRWT (boolean IsRWT)
{
set_Value ("IsRWT", new Boolean(IsRWT));
}
/** Get IsRWT.
@return IsRWT */
public boolean isRWT() 
{
Object oo = get_Value("IsRWT");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Summary Level.
@param IsSummary This is a summary entity */
public void setIsSummary (boolean IsSummary)
{
set_Value ("IsSummary", new Boolean(IsSummary));
}
/** Get Summary Level.
@return This is a summary entity */
public boolean isSummary() 
{
Object oo = get_Value("IsSummary");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Name .
@param Name Alphanumeric identifier of the entity */
public void setName (String Name)
{
if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
if (Name.length() > 120)
{
log.warning("Length > 120 - truncated");
Name = Name.substring(0,119);
}
set_Value ("Name", Name);
}
/** Get Name .
@return Alphanumeric identifier of the entity */
public String getName() 
{
return (String)get_Value("Name");
}

/** P_Advance_ID AD_Reference_ID=2000023 */
public static final int P_ADVANCE_ID_AD_Reference_ID=2000023;
/** Set Advance.
@param P_Advance_ID Advance */
public void setP_Advance_ID (int P_Advance_ID)
{
if (P_Advance_ID <= 0) set_Value ("P_Advance_ID", null);
 else 
set_Value ("P_Advance_ID", new Integer(P_Advance_ID));
}
/** Get Advance.
@return Advance */
public int getP_Advance_ID() 
{
Integer ii = (Integer)get_Value("P_Advance_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Column_Adm_ID AD_Reference_ID=2000000 */
public static final int P_COLUMN_ADM_ID_AD_Reference_ID=2000000;
/** Set Admissibility Column.
@param P_Column_Adm_ID Admissibility Column */
public void setP_Column_Adm_ID (int P_Column_Adm_ID)
{
if (P_Column_Adm_ID <= 0) set_Value ("P_Column_Adm_ID", null);
 else 
set_Value ("P_Column_Adm_ID", new Integer(P_Column_Adm_ID));
}
/** Get Admissibility Column.
@return Admissibility Column */
public int getP_Column_Adm_ID() 
{
Integer ii = (Integer)get_Value("P_Column_Adm_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Credits_ID AD_Reference_ID=2100318 */
public static final int P_CREDITS_ID_AD_Reference_ID=2100318;
/** Set Credits.
@param P_Credits_ID Credits */
public void setP_Credits_ID (int P_Credits_ID)
{
if (P_Credits_ID <= 0) set_Value ("P_Credits_ID", null);
 else 
set_Value ("P_Credits_ID", new Integer(P_Credits_ID));
}
/** Get Credits.
@return Credits */
public int getP_Credits_ID() 
{
Integer ii = (Integer)get_Value("P_Credits_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Deduction_ID AD_Reference_ID=2000039 */
public static final int P_DEDUCTION_ID_AD_Reference_ID=2000039;
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

/** P_Gain_Category_ID AD_Reference_ID=2100396 */
public static final int P_GAIN_CATEGORY_ID_AD_Reference_ID=2100396;
/** Set Gain Category.
@param P_Gain_Category_ID Gain Category */
public void setP_Gain_Category_ID (int P_Gain_Category_ID)
{
if (P_Gain_Category_ID <= 0) set_Value ("P_Gain_Category_ID", null);
 else 
set_Value ("P_Gain_Category_ID", new Integer(P_Gain_Category_ID));
}
/** Get Gain Category.
@return Gain Category */
public int getP_Gain_Category_ID() 
{
Integer ii = (Integer)get_Value("P_Gain_Category_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Gain_Compensation AD_Reference_ID=2100317 */
public static final int P_GAIN_COMPENSATION_AD_Reference_ID=2100317;
/** Set Gain to compense non business day (RWT, ALD).
@param P_Gain_Compensation Gain to compense non business day (RWT, ALD) */
public void setP_Gain_Compensation (int P_Gain_Compensation)
{
set_Value ("P_Gain_Compensation", new Integer(P_Gain_Compensation));
}
/** Get Gain to compense non business day (RWT, ALD).
@return Gain to compense non business day (RWT, ALD) */
public int getP_Gain_Compensation() 
{
Integer ii = (Integer)get_Value("P_Gain_Compensation");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Gain_Family_ID AD_Reference_ID=2000021 */
public static final int P_GAIN_FAMILY_ID_AD_Reference_ID=2000021;
/** Set Gain Family.
@param P_Gain_Family_ID Gain Family  */
public void setP_Gain_Family_ID (int P_Gain_Family_ID)
{
if (P_Gain_Family_ID < 1) throw new IllegalArgumentException ("P_Gain_Family_ID is mandatory.");
set_Value ("P_Gain_Family_ID", new Integer(P_Gain_Family_ID));
}
/** Get Gain Family.
@return Gain Family  */
public int getP_Gain_Family_ID() 
{
Integer ii = (Integer)get_Value("P_Gain_Family_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Gain_Group_ID AD_Reference_ID=2100378 */
public static final int P_GAIN_GROUP_ID_AD_Reference_ID=2100378;
/** Set Gain Group.
@param P_Gain_Group_ID Gain Group */
public void setP_Gain_Group_ID (int P_Gain_Group_ID)
{
if (P_Gain_Group_ID <= 0) set_Value ("P_Gain_Group_ID", null);
 else 
set_Value ("P_Gain_Group_ID", new Integer(P_Gain_Group_ID));
}
/** Get Gain Group.
@return Gain Group */
public int getP_Gain_Group_ID() 
{
Integer ii = (Integer)get_Value("P_Gain_Group_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Gain.
@param P_Gain_ID Gain */
public void setP_Gain_ID (int P_Gain_ID)
{
if (P_Gain_ID < 1) throw new IllegalArgumentException ("P_Gain_ID is mandatory.");
set_ValueNoCheck ("P_Gain_ID", new Integer(P_Gain_ID));
}
/** Get Gain.
@return Gain */
public int getP_Gain_ID() 
{
Integer ii = (Integer)get_Value("P_Gain_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Method_Gain_ID AD_Reference_ID=2000020 */
public static final int P_METHOD_GAIN_ID_AD_Reference_ID=2000020;
/** Set Method.
@param P_Method_Gain_ID Method */
public void setP_Method_Gain_ID (int P_Method_Gain_ID)
{
if (P_Method_Gain_ID < 1) throw new IllegalArgumentException ("P_Method_Gain_ID is mandatory.");
set_Value ("P_Method_Gain_ID", new Integer(P_Method_Gain_ID));
}
/** Get Method.
@return Method */
public int getP_Method_Gain_ID() 
{
Integer ii = (Integer)get_Value("P_Method_Gain_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_SpecialRate_ID AD_Reference_ID=2100391 */
public static final int P_SPECIALRATE_ID_AD_Reference_ID=2100391;
/** Set Special Rate.
@param P_SpecialRate_ID Special Rate */
public void setP_SpecialRate_ID (int P_SpecialRate_ID)
{
if (P_SpecialRate_ID <= 0) set_Value ("P_SpecialRate_ID", null);
 else 
set_Value ("P_SpecialRate_ID", new Integer(P_SpecialRate_ID));
}
/** Get Special Rate.
@return Special Rate */
public int getP_SpecialRate_ID() 
{
Integer ii = (Integer)get_Value("P_SpecialRate_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_UOM_ID AD_Reference_ID=2100342 */
public static final int P_UOM_ID_AD_Reference_ID=2100342;
/** Set Unit.
@param P_UOM_ID Unit of Measure */
public void setP_UOM_ID (int P_UOM_ID)
{
if (P_UOM_ID < 1) throw new IllegalArgumentException ("P_UOM_ID is mandatory.");
set_Value ("P_UOM_ID", new Integer(P_UOM_ID));
}
/** Get Unit.
@return Unit of Measure */
public int getP_UOM_ID() 
{
Integer ii = (Integer)get_Value("P_UOM_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Account Generation.
@param ProcessAccountGen Account Generation */
public void setProcessAccountGen (String ProcessAccountGen)
{
if (ProcessAccountGen != null && ProcessAccountGen.length() > 1)
{
log.warning("Length > 1 - truncated");
ProcessAccountGen = ProcessAccountGen.substring(0,0);
}
set_Value ("ProcessAccountGen", ProcessAccountGen);
}
/** Get Account Generation.
@return Account Generation */
public String getProcessAccountGen() 
{
return (String)get_Value("ProcessAccountGen");
}
/** Set ProcessAcountGen_H.
@param ProcessAcountGen_H ProcessAcountGen_H */
public void setProcessAcountGen_H (String ProcessAcountGen_H)
{
if (ProcessAcountGen_H != null && ProcessAcountGen_H.length() > 1)
{
log.warning("Length > 1 - truncated");
ProcessAcountGen_H = ProcessAcountGen_H.substring(0,0);
}
set_Value ("ProcessAcountGen_H", ProcessAcountGen_H);
}
/** Get ProcessAcountGen_H.
@return ProcessAcountGen_H */
public String getProcessAcountGen_H() 
{
return (String)get_Value("ProcessAcountGen_H");
}

/** Type_Rate AD_Reference_ID=2000022 */
public static final int TYPE_RATE_AD_Reference_ID=2000022;
/** Overtime = O */
public static final String TYPE_RATE_Overtime = "O";
/** Accumulated Days = D */
public static final String TYPE_RATE_AccumulatedDays = "D";
/** Short Term Disability = S */
public static final String TYPE_RATE_ShortTermDisability = "S";
/** Fixe Rate = F */
public static final String TYPE_RATE_FixedRate = "F";
/** Vacance taux employé = H */
public static final String TYPE_RATE_VacanceTauxEmploye = "H";
public static final String TYPE_RATE_VacanceTauxEmployé = "H";
/** CSST / WSIB = W */
public static final String TYPE_RATE_CSSTWSIB = "W";
/** Employee Affectation = A */
public static final String TYPE_RATE_EmployeeAffectation = "A";
/** Vacance selon banque          = V */
public static final String TYPE_RATE_VacanceSelonBanque = "V";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isType_RateValid (String test)
{
return test == null || test.equals("O") || test.equals("D") || test.equals("S") || test.equals("F") || test.equals("H") || test.equals("W") || test.equals("A") || test.equals("V");
}
/** Set Type Rate.
@param Type_Rate   */
public void setType_Rate (String Type_Rate)
{
if (!isType_RateValid(Type_Rate))
throw new IllegalArgumentException ("Type_Rate Invalid value - " + Type_Rate + " - Reference_ID=2000022 - O - D - S - F - H - W - A - V");
if (Type_Rate != null && Type_Rate.length() > 1)
{
log.warning("Length > 1 - truncated");
Type_Rate = Type_Rate.substring(0,0);
}
set_Value ("Type_Rate", Type_Rate);
}
/** Get Type Rate.
@return   */
public String getType_Rate() 
{
return (String)get_Value("Type_Rate");
}
/** Set Search Key.
@param Value Search key for the record in the format required - must be unique */
public void setValue (String Value)
{
if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
if (Value.length() > 80)
{
log.warning("Length > 80 - truncated");
Value = Value.substring(0,79);
}
set_Value ("Value", Value);
}
/** Get Search Key.
@return Search key for the record in the format required - must be unique */
public String getValue() 
{
return (String)get_Value("Value");
}
/** Get Record ID/ColumnName
@return ID/ColumnName pair */
public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), getValue());
}
/** Set Visible.
@param Visible Visible */
public void setVisible (boolean Visible)
{
set_Value ("Visible", new Boolean(Visible));
}
/** Get Visible.
@return Visible */
public boolean isVisible() 
{
Object oo = get_Value("Visible");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
}
