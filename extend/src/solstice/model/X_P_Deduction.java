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
/** Generated Model for P_Deduction
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: X_P_Deduction.java,v 1.8 2008/11/13 19:32:28 marmor01 Exp $ */
public class X_P_Deduction extends PO
{
/** Standard Constructor
@param ctx context
@param P_Deduction_ID id
@param trxName transaction
*/
public X_P_Deduction (Properties ctx, int P_Deduction_ID, String trxName)
{
super (ctx, P_Deduction_ID, trxName);
/** if (P_Deduction_ID == 0)
{
setDeduction_AP_Acct (0);
setIsSinPrinted (false);
setIsSummary (false);
setName (null);
setP_Deduction_Family_ID (0);
setP_Deduction_ID (0);
setValue (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Deduction (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000153 */
public static final int Table_ID=2000153;

/** TableName=P_Deduction */
public static final String Table_Name="P_Deduction";

protected static KeyNamePair Model = new KeyNamePair(2000153,"P_Deduction");

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
StringBuffer sb = new StringBuffer ("X_P_Deduction[").append(get_ID()).append("]");
return sb.toString();
}

/** Back_Payment_Acct AD_Reference_ID=132 */
public static final int BACK_PAYMENT_ACCT_AD_Reference_ID=132;
/** Set Back Payment Acct.
@param Back_Payment_Acct Back Payment Acct */
public void setBack_Payment_Acct (int Back_Payment_Acct)
{
set_Value ("Back_Payment_Acct", new Integer(Back_Payment_Acct));
}
/** Get Back Payment Acct.
@return Back Payment Acct */
public int getBack_Payment_Acct() 
{
Integer ii = (Integer)get_Value("Back_Payment_Acct");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Country.
@param C_Country_ID Country  */
public void setC_Country_ID (int C_Country_ID)
{
if (C_Country_ID <= 0) set_Value ("C_Country_ID", null);
 else 
set_Value ("C_Country_ID", new Integer(C_Country_ID));
}
/** Get Country.
@return Country  */
public int getC_Country_ID() 
{
Integer ii = (Integer)get_Value("C_Country_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** C_Region_ID AD_Reference_ID=2000026 */
public static final int C_REGION_ID_AD_Reference_ID=2000026;
/** Set Region.
@param C_Region_ID Identifies a geographical Region */
public void setC_Region_ID (int C_Region_ID)
{
if (C_Region_ID <= 0) set_Value ("C_Region_ID", null);
 else 
set_Value ("C_Region_ID", new Integer(C_Region_ID));
}
/** Get Region.
@return Identifies a geographical Region */
public int getC_Region_ID() 
{
Integer ii = (Integer)get_Value("C_Region_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** Deduction_AP_Acct AD_Reference_ID=132 */
public static final int DEDUCTION_AP_ACCT_AD_Reference_ID=132;
/** Set AP Account.
@param Deduction_AP_Acct AP Account */
public void setDeduction_AP_Acct (int Deduction_AP_Acct)
{
set_Value ("Deduction_AP_Acct", new Integer(Deduction_AP_Acct));
}
/** Get AP Account.
@return AP Account */
public int getDeduction_AP_Acct() 
{
Integer ii = (Integer)get_Value("Deduction_AP_Acct");
if (ii == null) return 0;
return ii.intValue();
}

/** Deduction_Ap_Acct_Yeur AD_Reference_ID=132 */
public static final int DEDUCTION_AP_ACCT_YEUR_AD_Reference_ID=132;
/** Set AP Account Employer.
@param Deduction_Ap_Acct_Yeur AP Account Employer */
public void setDeduction_Ap_Acct_Yeur (int Deduction_Ap_Acct_Yeur)
{
set_Value ("Deduction_Ap_Acct_Yeur", new Integer(Deduction_Ap_Acct_Yeur));
}
/** Get AP Account Employer.
@return AP Account Employer */
public int getDeduction_Ap_Acct_Yeur() 
{
Integer ii = (Integer)get_Value("Deduction_Ap_Acct_Yeur");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Description.
@param Description Optional short description of the record */
public void setDescription (String Description)
{
if (Description != null && Description.length() > 255)
{
log.warning("Length > 255 - truncated");
Description = Description.substring(0,254);
}
set_Value ("Description", Description);
}
/** Get Description.
@return Optional short description of the record */
public String getDescription() 
{
return (String)get_Value("Description");
}
/** Set insurance group.
@param Group_insurance insurance group */
public void setGroup_insurance (String Group_insurance)
{
if (Group_insurance != null && Group_insurance.length() > 10)
{
log.warning("Length > 10 - truncated");
Group_insurance = Group_insurance.substring(0,9);
}
set_Value ("Group_insurance", Group_insurance);
}
/** Get insurance group.
@return insurance group */
public String getGroup_insurance() 
{
return (String)get_Value("Group_insurance");
}
/** Set IsAnnual.
@param IsAnnual Déduction annuel ou avec un maximum non annuel */
public void setIsAnnual (boolean IsAnnual)
{
set_Value ("IsAnnual", new Boolean(IsAnnual));
}
/** Get IsAnnual.
@return Déduction annuel ou avec un maximum non annuel */
public boolean isAnnual() 
{
Object oo = get_Value("IsAnnual");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Insurance Package.
@param IsBaseInsurance Insurance Package */
public void setIsBaseInsurance (boolean IsBaseInsurance)
{
set_Value ("IsBaseInsurance", new Boolean(IsBaseInsurance));
}
/** Get Insurance Package.
@return Insurance Package */
public boolean isBaseInsurance() 
{
Object oo = get_Value("IsBaseInsurance");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Mandatory Deduction.
@param IsDeductionMandatory Mandatory Deduction */
public void setIsDeductionMandatory (boolean IsDeductionMandatory)
{
set_Value ("IsDeductionMandatory", new Boolean(IsDeductionMandatory));
}
/** Get Mandatory Deduction.
@return Mandatory Deduction */
public boolean isDeductionMandatory() 
{
Object oo = get_Value("IsDeductionMandatory");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Mandatory Deduction Residence Region.
@param IsDeductionMandatoryResidenceRegion Mandatory Deduction Residence Region */
public void setIsDeductionMandatoryResidenceRegion (boolean IsDeductionMandatoryResidenceRegion)
{
set_Value ("IsDeductionMandatoryResidenceRegion", new Boolean(IsDeductionMandatoryResidenceRegion));
}
/** Get Mandatory Deduction Residence Region.
@return Mandatory Deduction Residence Region */
public boolean isDeductionMandatoryResidenceRegion() 
{
Object oo = get_Value("IsDeductionMandatoryResidenceRegion");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set SIN Printed .
@param IsSinPrinted SIN Printed  */
public void setIsSinPrinted (boolean IsSinPrinted)
{
set_Value ("IsSinPrinted", new Boolean(IsSinPrinted));
}
/** Get SIN Printed .
@return SIN Printed  */
public boolean isSinPrinted() 
{
Object oo = get_Value("IsSinPrinted");
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
if (Name.length() > 60)
{
log.warning("Length > 60 - truncated");
Name = Name.substring(0,59);
}
set_Value ("Name", Name);
}
/** Get Name .
@return Alphanumeric identifier of the entity */
public String getName() 
{
return (String)get_Value("Name");
}

/** P_Deduction_Category_ID AD_Reference_ID=2100395 */
public static final int P_DEDUCTION_CATEGORY_ID_AD_Reference_ID=2100395;
/** Set Deduction Category.
@param P_Deduction_Category_ID Deduction Category */
public void setP_Deduction_Category_ID (int P_Deduction_Category_ID)
{
if (P_Deduction_Category_ID <= 0) set_Value ("P_Deduction_Category_ID", null);
 else 
set_Value ("P_Deduction_Category_ID", new Integer(P_Deduction_Category_ID));
}
/** Get Deduction Category.
@return Deduction Category */
public int getP_Deduction_Category_ID() 
{
Integer ii = (Integer)get_Value("P_Deduction_Category_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Deduction_Family_ID AD_Reference_ID=2000038 */
public static final int P_DEDUCTION_FAMILY_ID_AD_Reference_ID=2000038;
/** Set Deduction Family.
@param P_Deduction_Family_ID Deduction Family */
public void setP_Deduction_Family_ID (int P_Deduction_Family_ID)
{
if (P_Deduction_Family_ID < 1) throw new IllegalArgumentException ("P_Deduction_Family_ID is mandatory.");
set_ValueNoCheck ("P_Deduction_Family_ID", new Integer(P_Deduction_Family_ID));
}
/** Get Deduction Family.
@return Deduction Family */
public int getP_Deduction_Family_ID() 
{
Integer ii = (Integer)get_Value("P_Deduction_Family_ID");
if (ii == null) return 0;
return ii.intValue();
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

/** P_Payment_Group_ID AD_Reference_ID=2000056 */
public static final int P_PAYMENT_GROUP_ID_AD_Reference_ID=2000056;
/** Set Payment Group.
@param P_Payment_Group_ID Payment Group */
public void setP_Payment_Group_ID (int P_Payment_Group_ID)
{
if (P_Payment_Group_ID <= 0) set_Value ("P_Payment_Group_ID", null);
 else 
set_Value ("P_Payment_Group_ID", new Integer(P_Payment_Group_ID));
}
/** Get Payment Group.
@return Payment Group */
public int getP_Payment_Group_ID() 
{
Integer ii = (Integer)get_Value("P_Payment_Group_ID");
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

/** P_Taxable_Benefit_Provincial_ID AD_Reference_ID=2000046 */
public static final int P_TAXABLE_BENEFIT_PROVINCIAL_ID_AD_Reference_ID=2000046;
/** Set Provincial Taxable Benefit .
@param P_Taxable_Benefit_Provincial_ID Provincial Taxable Benefit  */
public void setP_Taxable_Benefit_Provincial_ID (int P_Taxable_Benefit_Provincial_ID)
{
if (P_Taxable_Benefit_Provincial_ID <= 0) set_Value ("P_Taxable_Benefit_Provincial_ID", null);
 else 
set_Value ("P_Taxable_Benefit_Provincial_ID", new Integer(P_Taxable_Benefit_Provincial_ID));
}
/** Get Provincial Taxable Benefit .
@return Provincial Taxable Benefit  */
public int getP_Taxable_Benefit_Provincial_ID() 
{
Integer ii = (Integer)get_Value("P_Taxable_Benefit_Provincial_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** PlanTyp AD_Reference_ID=2100471 */
public static final int PLANTYP_AD_Reference_ID=2100471;
/** RRF = 3 */
public static final String PLANTYP_RRF = "3";
/** RRE = 4 */
public static final String PLANTYP_RRE = "4";
/** RRMSQ = 5 */
public static final String PLANTYP_RRMSQ = "5";
/** RRCHCN = 9 */
public static final String PLANTYP_RRCHCN = "9";
/** RRAPSC = 10 */
public static final String PLANTYP_RRAPSC = "10";
/** RRCJAJ = 17 */
public static final String PLANTYP_RRCJAJ = "17";
/** RRAS = 21 */
public static final String PLANTYP_RRAS = "21";
/** RREM = 28 */
public static final String PLANTYP_RREM = "28";
/** RREFQ = 30 */
public static final String PLANTYP_RREFQ = "30";
/** RRPE = 31 */
public static final String PLANTYP_RRPE = "31";
/** RRMAN = 36 */
public static final String PLANTYP_RRMAN = "36";
/** RRJCQM = 47 */
public static final String PLANTYP_RRJCQM = "47";
/** RRCE = 54 */
public static final String PLANTYP_RRCE = "54";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isPlanTypValid (String test)
{
return test == null || test.equals("3") || test.equals("4") || test.equals("5") || test.equals("9") || test.equals("10") || test.equals("17") || test.equals("21") || test.equals("28") || test.equals("30") || test.equals("31") || test.equals("36") || test.equals("47") || test.equals("54");
}
/** Set Plan Typ.
@param PlanTyp Plan Typ */
public void setPlanTyp (String PlanTyp)
{
if (!isPlanTypValid(PlanTyp))
throw new IllegalArgumentException ("PlanTyp Invalid value - " + PlanTyp + " - Reference_ID=2100471 - 3 - 4 - 5 - 9 - 10 - 17 - 21 - 28 - 30 - 31 - 36 - 47 - 54");
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

/** Residence_Region_ID AD_Reference_ID=2000026 */
public static final int RESIDENCE_REGION_ID_AD_Reference_ID=2000026;
/** Set Residence Region.
@param Residence_Region_ID Residence Region */
public void setResidence_Region_ID (int Residence_Region_ID)
{
if (Residence_Region_ID <= 0) set_Value ("Residence_Region_ID", null);
 else 
set_Value ("Residence_Region_ID", new Integer(Residence_Region_ID));
}
/** Get Residence Region.
@return Residence Region */
public int getResidence_Region_ID() 
{
Integer ii = (Integer)get_Value("Residence_Region_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set RRPNumber.
@param RRPNumber RRPNumber */
public void setRRPNumber (String RRPNumber)
{
if (RRPNumber != null && RRPNumber.length() > 30)
{
log.warning("Length > 30 - truncated");
RRPNumber = RRPNumber.substring(0,29);
}
set_Value ("RRPNumber", RRPNumber);
}
/** Get RRPNumber.
@return RRPNumber */
public String getRRPNumber() 
{
return (String)get_Value("RRPNumber");
}
/** Set Search Key.
@param Value Search key for the record in the format required - must be unique */
public void setValue (String Value)
{
if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
if (Value.length() > 30)
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
}
