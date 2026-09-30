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
/** Generated Model for P_Carra
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: X_P_Carra.java,v 1.2 2008/11/24 16:00:50 marmor01 Exp $ */
public class X_P_Carra extends PO
{
/** Standard Constructor
@param ctx context
@param P_Carra_ID id
@param trxName transaction
*/
public X_P_Carra (Properties ctx, int P_Carra_ID, String trxName)
{
super (ctx, P_Carra_ID, trxName);
/** if (P_Carra_ID == 0)
{
setP_Deduction_ID (0);
setP_Employee_ID (0);
setP_Year_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Carra (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100901 */
public static final int Table_ID=2100901;

/** TableName=P_Carra */
public static final String Table_Name="P_Carra";

protected static KeyNamePair Model = new KeyNamePair(2100901,"P_Carra");

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
StringBuffer sb = new StringBuffer ("X_P_Carra[").append(get_ID()).append("]");
return sb.toString();
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
/** Set Amt Retro Non Contributory.
@param AmtRetroNonContributory Amt Retro Non Contributory */
public void setAmtRetroNonContributory (BigDecimal AmtRetroNonContributory)
{
set_Value ("AmtRetroNonContributory", AmtRetroNonContributory);
}
/** Get Amt Retro Non Contributory.
@return Amt Retro Non Contributory */
public BigDecimal getAmtRetroNonContributory() 
{
BigDecimal bd = (BigDecimal)get_Value("AmtRetroNonContributory");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Amt Retro RREM.
@param AmtRetroRREM Amt Retro RREM */
public void setAmtRetroRREM (BigDecimal AmtRetroRREM)
{
set_Value ("AmtRetroRREM", AmtRetroRREM);
}
/** Get Amt Retro RREM.
@return Amt Retro RREM */
public BigDecimal getAmtRetroRREM() 
{
BigDecimal bd = (BigDecimal)get_Value("AmtRetroRREM");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Processed Calcul FE.
@param CalculFE Processed Calcul FE */
public void setCalculFE (boolean CalculFE)
{
set_Value ("CalculFE", new Boolean(CalculFE));
}
/** Get Processed Calcul FE.
@return Processed Calcul FE */
public boolean isCalculFE() 
{
Object oo = get_Value("CalculFE");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
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
/** Set Contribution.
@param Contribution Contribution */
public void setContribution (BigDecimal Contribution)
{
set_Value ("Contribution", Contribution);
}
/** Get Contribution.
@return Contribution */
public BigDecimal getContribution() 
{
BigDecimal bd = (BigDecimal)get_Value("Contribution");
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
/** Set Contributory Earnings.
@param ContributoryEarnings Contributory Earnings */
public void setContributoryEarnings (BigDecimal ContributoryEarnings)
{
set_Value ("ContributoryEarnings", ContributoryEarnings);
}
/** Get Contributory Earnings.
@return Contributory Earnings */
public BigDecimal getContributoryEarnings() 
{
BigDecimal bd = (BigDecimal)get_Value("ContributoryEarnings");
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
/** Set Date Retro Pay.
@param DateRetroPay Date Retro Pay */
public void setDateRetroPay (Timestamp DateRetroPay)
{
set_Value ("DateRetroPay", DateRetroPay);
}
/** Get Date Retro Pay.
@return Date Retro Pay */
public Timestamp getDateRetroPay() 
{
return (Timestamp)get_Value("DateRetroPay");
}

/** DAType AD_Reference_ID=2100474 */
public static final int DATYPE_AD_Reference_ID=2100474;
/** 1- Employé non visé = 1 */
public static final String DATYPE_1_EmployéNonVisé = "1";
/** 2- Employé libéré pour activités syndicales = 2 */
public static final String DATYPE_2_EmployéLibéréPourActivitésSyndicales = "2";
/** 3- Employé libéré sans salaire = 3 */
public static final String DATYPE_3_EmployéLibéréSansSalaire = "3";
/** N/A =   */
public static final String DATYPE_NA = " ";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isDATypeValid (String test)
{
return test == null || test.equals("1") || test.equals("2") || test.equals("3") || test.equals(" ");
}
/** Set DA Type.
@param DAType DA Type */
public void setDAType (String DAType)
{
if (!isDATypeValid(DAType))
throw new IllegalArgumentException ("DAType Invalid value - " + DAType + " - Reference_ID=2100474 - 1 - 2 - 3 -  ");
if (DAType != null && DAType.length() > 1)
{
log.warning("Length > 1 - truncated");
DAType = DAType.substring(0,0);
}
set_Value ("DAType", DAType);
}
/** Get DA Type.
@return DA Type */
public String getDAType() 
{
return (String)get_Value("DAType");
}

/** DayFactorVal AD_Reference_ID=2100475 */
public static final int DAYFACTORVAL_AD_Reference_ID=2100475;
/** Facteur quotidien sur base de 200 jours = 200 */
public static final String DAYFACTORVAL_FacteurQuotidienSurBaseDe200Jours = "200";
/** Facteur quotidien sur base de 240 jours = 240 */
public static final String DAYFACTORVAL_FacteurQuotidienSurBaseDe240Jours = "240";
/** Facteur quotidien sur base de 260 jours = 260 */
public static final String DAYFACTORVAL_FacteurQuotidienSurBaseDe260Jours = "260";
/** Facteur quotidien sur base de 260.9 jours = 2609 */
public static final String DAYFACTORVAL_FacteurQuotidienSurBaseDe2609Jours = "2609";
/** Facteur quotidien sur base de 270 jours = 270 */
public static final String DAYFACTORVAL_FacteurQuotidienSurBaseDe270Jours = "270";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isDayFactorValValid (String test)
{
return test == null || test.equals("200") || test.equals("240") || test.equals("260") || test.equals("2609") || test.equals("270");
}
/** Set Day Factor.
@param DayFactorVal Day Factor */
public void setDayFactorVal (String DayFactorVal)
{
if (!isDayFactorValValid(DayFactorVal))
throw new IllegalArgumentException ("DayFactorVal Invalid value - " + DayFactorVal + " - Reference_ID=2100475 - 200 - 240 - 260 - 2609 - 270");
if (DayFactorVal != null && DayFactorVal.length() > 5)
{
log.warning("Length > 5 - truncated");
DayFactorVal = DayFactorVal.substring(0,4);
}
set_Value ("DayFactorVal", DayFactorVal);
}
/** Get Day Factor.
@return Day Factor */
public String getDayFactorVal() 
{
return (String)get_Value("DayFactorVal");
}
/** Set Employer Contribution.
@param EmployerContribution Employer Contribution */
public void setEmployerContribution (boolean EmployerContribution)
{
set_Value ("EmployerContribution", new Boolean(EmployerContribution));
}
/** Get Employer Contribution.
@return Employer Contribution */
public boolean isEmployerContribution() 
{
Object oo = get_Value("EmployerContribution");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set End Employment Date.
@param EndEmploymentDate End Employment Date */
public void setEndEmploymentDate (Timestamp EndEmploymentDate)
{
set_Value ("EndEmploymentDate", EndEmploymentDate);
}
/** Get End Employment Date.
@return End Employment Date */
public Timestamp getEndEmploymentDate() 
{
return (Timestamp)get_Value("EndEmploymentDate");
}
/** Set Final Version.
@param IsDone Final Version */
public void setIsDone (boolean IsDone)
{
set_Value ("IsDone", new Boolean(IsDone));
}
/** Get Final Version.
@return Final Version */
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
/** Set No Employment.
@param NoEmployment No Employment */
public void setNoEmployment (BigDecimal NoEmployment)
{
set_Value ("NoEmployment", NoEmployment);
}
/** Get No Employment.
@return No Employment */
public BigDecimal getNoEmployment() 
{
BigDecimal bd = (BigDecimal)get_Value("NoEmployment");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set No Pay Calendar.
@param NoPayCalendar No Pay Calendar */
public void setNoPayCalendar (BigDecimal NoPayCalendar)
{
set_Value ("NoPayCalendar", NoPayCalendar);
}
/** Get No Pay Calendar.
@return No Pay Calendar */
public BigDecimal getNoPayCalendar() 
{
BigDecimal bd = (BigDecimal)get_Value("NoPayCalendar");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Carra.
@param P_Carra_ID Carra */
public void setP_Carra_ID (int P_Carra_ID)
{
if (P_Carra_ID <= 0) set_ValueNoCheck ("P_Carra_ID", null);
 else 
set_ValueNoCheck ("P_Carra_ID", new Integer(P_Carra_ID));
}
/** Get Carra.
@return Carra */
public int getP_Carra_ID() 
{
Integer ii = (Integer)get_Value("P_Carra_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Deduction_ID AD_Reference_ID=2100477 */
public static final int P_DEDUCTION_ID_AD_Reference_ID=2100477;
/** Set Deduction.
@param P_Deduction_ID Deduction */
public void setP_Deduction_ID (int P_Deduction_ID)
{
if (P_Deduction_ID < 1) throw new IllegalArgumentException ("P_Deduction_ID is mandatory.");
set_Value ("P_Deduction_ID", new Integer(P_Deduction_ID));
}
/** Get Deduction.
@return Deduction */
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
/** Get Record ID/ColumnName
@return ID/ColumnName pair */
public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), String.valueOf(getP_Employee_ID()));
}

/** P_Occupation_Group_ID AD_Reference_ID=2000014 */
public static final int P_OCCUPATION_GROUP_ID_AD_Reference_ID=2000014;
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

/** P_Year_ID AD_Reference_ID=2000012 */
public static final int P_YEAR_ID_AD_Reference_ID=2000012;
/** Set Year.
@param P_Year_ID Calendar Year */
public void setP_Year_ID (int P_Year_ID)
{
if (P_Year_ID < 1) throw new IllegalArgumentException ("P_Year_ID is mandatory.");
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
/** Set Part Time.
@param PartTime Part Time */
public void setPartTime (BigDecimal PartTime)
{
set_Value ("PartTime", PartTime);
}
/** Get Part Time.
@return Part Time */
public BigDecimal getPartTime() 
{
BigDecimal bd = (BigDecimal)get_Value("PartTime");
if (bd == null) return Env.ZERO;
return bd;
}

/** PlanGroup AD_Reference_ID=2100472 */
public static final int PLANGROUP_AD_Reference_ID=2100472;
/** RE - Ancien participant du RRE = RE */
public static final String PLANGROUP_RE_AncienParticipantDuRRE = "RE";
/** NS - Participant occupant un emploi non syndicable = NS */
public static final String PLANGROUP_NS_ParticipantOccupantUnEmploiNonSyndicable = "NS";
/** QP - Employé qualifié au RRAPSC (emploi visé RREGOP ou RRPE) = QP */
public static final String PLANGROUP_QP_EmployéQualifiéAuRRAPSCEmploiViséRREGOPOuRRPE = "QP";
/** R1 - Rente viagère supplémentaire = R1 */
public static final String PLANGROUP_R1_RenteViagèreSupplémentaire = "R1";
/** R2 - Rente viagère  supplémentaire pour une catégorie emp. = R2 */
public static final String PLANGROUP_R2_RenteViagèreSupplémentairePourUneCatégorieEmp = "R2";
/** 1P - Taux de cotisation de 1% = 1P */
public static final String PLANGROUP_1P_TauxDeCotisationDe1 = "1P";
/** 1S - Taux de cotisation de 1% (rente viagère) = 1S */
public static final String PLANGROUP_1S_TauxDeCotisationDe1RenteViagère = "1S";
/** 7S - Taux de cotisation de 7% (rente viagère) = 7S */
public static final String PLANGROUP_7S_TauxDeCotisationDe7RenteViagère = "7S";
/** FC - Les  cotisations sont versées au Fonds consolidé  = FC */
public static final String PLANGROUP_FC_LesCotisationsSontVerséesAuFondsConsolidé = "FC";
/** N/A = -- */
public static final String PLANGROUP_NA = "--";
/** RF - Ancien participant du RRF = RF */
public static final String PLANGROUP_RF_AncienParticipantDuRRF = "RF";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isPlanGroupValid (String test)
{
return test == null || test.equals("RE") || test.equals("NS") || test.equals("QP") || test.equals("R1") || test.equals("R2") || test.equals("1P") || test.equals("1S") || test.equals("7S") || test.equals("FC") || test.equals("--") || test.equals("RF");
}
/** Set Plan Group.
@param PlanGroup Plan Group */
public void setPlanGroup (String PlanGroup)
{
if (!isPlanGroupValid(PlanGroup))
throw new IllegalArgumentException ("PlanGroup Invalid value - " + PlanGroup + " - Reference_ID=2100472 - RE - NS - QP - R1 - R2 - 1P - 1S - 7S - FC - -- - RF");
if (PlanGroup != null && PlanGroup.length() > 2)
{
log.warning("Length > 2 - truncated");
PlanGroup = PlanGroup.substring(0,1);
}
set_Value ("PlanGroup", PlanGroup);
}
/** Get Plan Group.
@return Plan Group */
public String getPlanGroup() 
{
return (String)get_Value("PlanGroup");
}

/** PlanTyp AD_Reference_ID=2100471 */
public static final int PLANTYP_AD_Reference_ID=2100471;
/** 01 - RREGOP = 01 */
public static final String PLANTYP_01_RREGOP = "01";
/** 03 - RRF = 03 */
public static final String PLANTYP_03_RRF = "03";
/** 04 - RRE = 04 */
public static final String PLANTYP_04_RRE = "04";
/** 05 - RRMSQ = 05 */
public static final String PLANTYP_05_RRMSQ = "05";
/** 09 - RRCHCN = 09 */
public static final String PLANTYP_09_RRCHCN = "09";
/** 10 - RRAPSC = 10 */
public static final String PLANTYP_10_RRAPSC = "10";
/** 17 - RRCJAJ = 17 */
public static final String PLANTYP_17_RRCJAJ = "17";
/** 21 - RRAS = 21 */
public static final String PLANTYP_21_RRAS = "21";
/** 28 - RREM = 28 */
public static final String PLANTYP_28_RREM = "28";
/** 30 - RREFQ = 30 */
public static final String PLANTYP_30_RREFQ = "30";
/** 31- RRPE = 31 */
public static final String PLANTYP_31_RRPE = "31";
/** 36 - RRMAN = 36 */
public static final String PLANTYP_36_RRMAN = "36";
/** 47 - RRJCQM = 47 */
public static final String PLANTYP_47_RRJCQM = "47";
/** 54 - RRCE = 54 */
public static final String PLANTYP_54_RRCE = "54";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isPlanTypValid (String test)
{
return test == null || test.equals("01") || test.equals("03") || test.equals("04") || test.equals("05") || test.equals("09") || test.equals("10") || test.equals("17") || test.equals("21") || test.equals("28") || test.equals("30") || test.equals("31") || test.equals("36") || test.equals("47") || test.equals("54");
}
/** Set Plan Typ.
@param PlanTyp Plan Typ */
public void setPlanTyp (String PlanTyp)
{
if (!isPlanTypValid(PlanTyp))
throw new IllegalArgumentException ("PlanTyp Invalid value - " + PlanTyp + " - Reference_ID=2100471 - 01 - 03 - 04 - 05 - 09 - 10 - 17 - 21 - 28 - 30 - 31 - 36 - 47 - 54");
if (PlanTyp != null && PlanTyp.length() > 3)
{
log.warning("Length > 3 - truncated");
PlanTyp = PlanTyp.substring(0,2);
}
set_Value ("PlanTyp", PlanTyp);
}
/** Get Plan Typ.
@return Plan Typ */
public String getPlanTyp() 
{
return (String)get_Value("PlanTyp");
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

/** RemunerationBase AD_Reference_ID=2100476 */
public static final int REMUNERATIONBASE_AD_Reference_ID=2100476;
/** Rémunération sur une base de 200 jours = 200 */
public static final String REMUNERATIONBASE_RémunérationSurUneBaseDe200Jours = "200";
/** Rémunération sur 260 jours = 260 */
public static final String REMUNERATIONBASE_RémunérationSur260Jours = "260";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isRemunerationBaseValid (String test)
{
return test == null || test.equals("200") || test.equals("260");
}
/** Set Remuneration Base.
@param RemunerationBase Remuneration Base */
public void setRemunerationBase (String RemunerationBase)
{
if (!isRemunerationBaseValid(RemunerationBase))
throw new IllegalArgumentException ("RemunerationBase Invalid value - " + RemunerationBase + " - Reference_ID=2100476 - 200 - 260");
if (RemunerationBase != null && RemunerationBase.length() > 3)
{
log.warning("Length > 3 - truncated");
RemunerationBase = RemunerationBase.substring(0,2);
}
set_Value ("RemunerationBase", RemunerationBase);
}
/** Get Remuneration Base.
@return Remuneration Base */
public String getRemunerationBase() 
{
return (String)get_Value("RemunerationBase");
}
/** Set Salary35Year.
@param Salary35Year Salary35Year */
public void setSalary35Year (BigDecimal Salary35Year)
{
set_Value ("Salary35Year", Salary35Year);
}
/** Get Salary35Year.
@return Salary35Year */
public BigDecimal getSalary35Year() 
{
BigDecimal bd = (BigDecimal)get_Value("Salary35Year");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Salary Annual Base.
@param SalaryAnnualBase Salary Annual Base */
public void setSalaryAnnualBase (BigDecimal SalaryAnnualBase)
{
set_Value ("SalaryAnnualBase", SalaryAnnualBase);
}
/** Get Salary Annual Base.
@return Salary Annual Base */
public BigDecimal getSalaryAnnualBase() 
{
BigDecimal bd = (BigDecimal)get_Value("SalaryAnnualBase");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Salary Pay ( Only for RREM).
@param SalaryPay Salary Pay ( Only for RREM) */
public void setSalaryPay (BigDecimal SalaryPay)
{
set_Value ("SalaryPay", SalaryPay);
}
/** Get Salary Pay ( Only for RREM).
@return Salary Pay ( Only for RREM) */
public BigDecimal getSalaryPay() 
{
BigDecimal bd = (BigDecimal)get_Value("SalaryPay");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Start Employment Date.
@param StartEmploymentDate Start Employment Date */
public void setStartEmploymentDate (Timestamp StartEmploymentDate)
{
set_Value ("StartEmploymentDate", StartEmploymentDate);
}
/** Get Start Employment Date.
@return Start Employment Date */
public Timestamp getStartEmploymentDate() 
{
return (Timestamp)get_Value("StartEmploymentDate");
}
/** Set Weighted.
@param Weighted Weighted */
public void setWeighted (boolean Weighted)
{
set_Value ("Weighted", new Boolean(Weighted));
}
/** Get Weighted.
@return Weighted */
public boolean isWeighted() 
{
Object oo = get_Value("Weighted");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
}
