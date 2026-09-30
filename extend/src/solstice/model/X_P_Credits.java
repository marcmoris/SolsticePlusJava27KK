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
/** Generated Model for P_Credits
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Credits extends PO
{
/** Standard Constructor
@param ctx context
@param P_Credits_ID id
@param trxName transaction
*/
public X_P_Credits (Properties ctx, int P_Credits_ID, String trxName)
{
super (ctx, P_Credits_ID, trxName);
/** if (P_Credits_ID == 0)
{
setCreditsContents (null);
setCreditsDay (0);
setCreditsMonth (null);
setCreditsTypeDate (null);	// 1
setName (null);
setP_Credits_Family_ID (0);
setP_Credits_ID (0);
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
public X_P_Credits (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000056 */
public static final int Table_ID=2000056;

/** TableName=P_Credits */
public static final String Table_Name="P_Credits";

protected static KeyNamePair Model = new KeyNamePair(2000056,"P_Credits");

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
StringBuffer sb = new StringBuffer ("X_P_Credits[").append(get_ID()).append("]");
return sb.toString();
}

/** Credits_Acct AD_Reference_ID=132 */
public static final int CREDITS_ACCT_AD_Reference_ID=132;
/** Set Provision Account.
@param Credits_Acct Provision Account */
public void setCredits_Acct (int Credits_Acct)
{
set_Value ("Credits_Acct", new Integer(Credits_Acct));
}
/** Get Provision Account.
@return Provision Account */
public int getCredits_Acct() 
{
Integer ii = (Integer)get_Value("Credits_Acct");
if (ii == null) return 0;
return ii.intValue();
}

/** CreditsContents AD_Reference_ID=2000029 */
public static final int CREDITSCONTENTS_AD_Reference_ID=2000029;
/** Days convert to Hours = 3 */
public static final String CREDITSCONTENTS_DaysConvertToHours = "3";
/** Amount = 2 */
public static final String CREDITSCONTENTS_Amount = "2";
/** Hours convert to days = 4 */
public static final String CREDITSCONTENTS_HoursConvertToDays = "4";
/** Quantity = 1 */
public static final String CREDITSCONTENTS_Quantity = "1";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isCreditsContentsValid (String test)
{
return test.equals("3") || test.equals("2") || test.equals("4") || test.equals("1");
}
/** Set Content.
@param CreditsContents Content */
public void setCreditsContents (String CreditsContents)
{
if (CreditsContents == null) throw new IllegalArgumentException ("CreditsContents is mandatory");
if (!isCreditsContentsValid(CreditsContents))
throw new IllegalArgumentException ("CreditsContents Invalid value - " + CreditsContents + " - Reference_ID=2000029 - 3 - 2 - 4 - 1");
if (CreditsContents.length() > 30)
{
log.warning("Length > 30 - truncated");
CreditsContents = CreditsContents.substring(0,29);
}
set_Value ("CreditsContents", CreditsContents);
}
/** Get Content.
@return Content */
public String getCreditsContents() 
{
return (String)get_Value("CreditsContents");
}
/** Set Renewal day.
@param CreditsDay   */
public void setCreditsDay (int CreditsDay)
{
set_Value ("CreditsDay", new Integer(CreditsDay));
}
/** Get Renewal day.
@return   */
public int getCreditsDay() 
{
Integer ii = (Integer)get_Value("CreditsDay");
if (ii == null) return 0;
return ii.intValue();
}

/** CreditsMonth AD_Reference_ID=2000035 */
public static final int CREDITSMONTH_AD_Reference_ID=2000035;
/** May = 05 */
public static final String CREDITSMONTH_May = "05";
/** June = 06 */
public static final String CREDITSMONTH_June = "06";
/** July = 07 */
public static final String CREDITSMONTH_July = "07";
/** August = 08 */
public static final String CREDITSMONTH_August = "08";
/** September = 09 */
public static final String CREDITSMONTH_September = "09";
/** November = 11 */
public static final String CREDITSMONTH_November = "11";
/** January = 01 */
public static final String CREDITSMONTH_January = "01";
/** February = 02 */
public static final String CREDITSMONTH_February = "02";
/** Mars = 03 */
public static final String CREDITSMONTH_Mars = "03";
/** April = 04 */
public static final String CREDITSMONTH_April = "04";
/** October = 10 */
public static final String CREDITSMONTH_October = "10";
/** December = 12 */
public static final String CREDITSMONTH_December = "12";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isCreditsMonthValid (String test)
{
return test.equals("05") || test.equals("06") || test.equals("07") || test.equals("08") || test.equals("09") || test.equals("11") || test.equals("01") || test.equals("02") || test.equals("03") || test.equals("04") || test.equals("10") || test.equals("12");
}
/** Set Renewal month.
@param CreditsMonth Renewal month */
public void setCreditsMonth (String CreditsMonth)
{
if (CreditsMonth == null) throw new IllegalArgumentException ("CreditsMonth is mandatory");
if (!isCreditsMonthValid(CreditsMonth))
throw new IllegalArgumentException ("CreditsMonth Invalid value - " + CreditsMonth + " - Reference_ID=2000035 - 05 - 06 - 07 - 08 - 09 - 11 - 01 - 02 - 03 - 04 - 10 - 12");
if (CreditsMonth.length() > 22)
{
log.warning("Length > 22 - truncated");
CreditsMonth = CreditsMonth.substring(0,21);
}
set_Value ("CreditsMonth", CreditsMonth);
}
/** Get Renewal month.
@return Renewal month */
public String getCreditsMonth() 
{
return (String)get_Value("CreditsMonth");
}

/** CreditsTypeDate AD_Reference_ID=2100340 */
public static final int CREDITSTYPEDATE_AD_Reference_ID=2100340;
/** Date of the credits = 1 */
public static final String CREDITSTYPEDATE_DateOfTheCredits = "1";
/** Date Seniority = 2 */
public static final String CREDITSTYPEDATE_DateSeniority = "2";
/** Date hired = 3 */
public static final String CREDITSTYPEDATE_DateHired = "3";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isCreditsTypeDateValid (String test)
{
return test.equals("1") || test.equals("2") || test.equals("3");
}
/** Set Renewal based date.
@param CreditsTypeDate Renewal based date */
public void setCreditsTypeDate (String CreditsTypeDate)
{
if (CreditsTypeDate == null) throw new IllegalArgumentException ("CreditsTypeDate is mandatory");
if (!isCreditsTypeDateValid(CreditsTypeDate))
throw new IllegalArgumentException ("CreditsTypeDate Invalid value - " + CreditsTypeDate + " - Reference_ID=2100340 - 1 - 2 - 3");
if (CreditsTypeDate.length() > 2)
{
log.warning("Length > 2 - truncated");
CreditsTypeDate = CreditsTypeDate.substring(0,1);
}
set_Value ("CreditsTypeDate", CreditsTypeDate);
}
/** Get Renewal based date.
@return Renewal based date */
public String getCreditsTypeDate() 
{
return (String)get_Value("CreditsTypeDate");
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

/** DisplayUOM AD_Reference_ID=2100452 */
public static final int DISPLAYUOM_AD_Reference_ID=2100452;
/** Month = Mo */
public static final String DISPLAYUOM_Month = "Mo";
/** Amounts = A */
public static final String DISPLAYUOM_Amounts = "A";
/** Days = J */
public static final String DISPLAYUOM_Days = "J";
/** Year / Days = A/J */
public static final String DISPLAYUOM_YearDays = "A/J";
/** Hours = H */
public static final String DISPLAYUOM_Hours = "H";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isDisplayUOMValid (String test)
{
return test == null || test.equals("Mo") || test.equals("A") || test.equals("J") || test.equals("A/J") || test.equals("H");
}
/** Set Display UOM.
@param DisplayUOM Display UOM */
public void setDisplayUOM (String DisplayUOM)
{
if (!isDisplayUOMValid(DisplayUOM))
throw new IllegalArgumentException ("DisplayUOM Invalid value - " + DisplayUOM + " - Reference_ID=2100452 - Mo - A - J - A/J - H");
if (DisplayUOM != null && DisplayUOM.length() > 5)
{
log.warning("Length > 5 - truncated");
DisplayUOM = DisplayUOM.substring(0,4);
}
set_Value ("DisplayUOM", DisplayUOM);
}
/** Get Display UOM.
@return Display UOM */
public String getDisplayUOM() 
{
return (String)get_Value("DisplayUOM");
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
/** Set End Date.
@param EndDate Last effective date (inclusive) */
public void setEndDate (Timestamp EndDate)
{
set_Value ("EndDate", EndDate);
}
/** Get End Date.
@return Last effective date (inclusive) */
public Timestamp getEndDate() 
{
return (Timestamp)get_Value("EndDate");
}
/** Set Displayed.
@param IsDisplayedCredits Determines, if this credits is displayed */
public void setIsDisplayedCredits (boolean IsDisplayedCredits)
{
set_Value ("IsDisplayedCredits", new Boolean(IsDisplayedCredits));
}
/** Get Displayed.
@return Determines, if this credits is displayed */
public boolean isDisplayedCredits() 
{
Object oo = get_Value("IsDisplayedCredits");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Printed.
@param IsPrinted Indicates if this document / line is printed */
public void setIsPrinted (boolean IsPrinted)
{
set_Value ("IsPrinted", new Boolean(IsPrinted));
}
/** Get Printed.
@return Indicates if this document / line is printed */
public boolean isPrinted() 
{
Object oo = get_Value("IsPrinted");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Reporting.
@param IsReporting   */
public void setIsReporting (boolean IsReporting)
{
set_Value ("IsReporting", new Boolean(IsReporting));
}
/** Get Reporting.
@return   */
public boolean isReporting() 
{
Object oo = get_Value("IsReporting");
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

/** P_Credits_Family_ID AD_Reference_ID=2000028 */
public static final int P_CREDITS_FAMILY_ID_AD_Reference_ID=2000028;
/** Set Group.
@param P_Credits_Family_ID   */
public void setP_Credits_Family_ID (int P_Credits_Family_ID)
{
if (P_Credits_Family_ID < 1) throw new IllegalArgumentException ("P_Credits_Family_ID is mandatory.");
set_ValueNoCheck ("P_Credits_Family_ID", new Integer(P_Credits_Family_ID));
}
/** Get Group.
@return   */
public int getP_Credits_Family_ID() 
{
Integer ii = (Integer)get_Value("P_Credits_Family_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Credits.
@param P_Credits_ID Credits */
public void setP_Credits_ID (int P_Credits_ID)
{
if (P_Credits_ID < 1) throw new IllegalArgumentException ("P_Credits_ID is mandatory.");
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
/** Set Priority.
@param Priority Indicates if this request is of a high, medium or low priority. */
public void setPriority (int Priority)
{
set_Value ("Priority", new Integer(Priority));
}
/** Get Priority.
@return Indicates if this request is of a high, medium or low priority. */
public int getPriority() 
{
Integer ii = (Integer)get_Value("Priority");
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
/** Set UseRateEmployeeWhenExists.
@param UseRateEmployeeWhenExists UseRateEmployeeWhenExists */
public void setUseRateEmployeeWhenExists (boolean UseRateEmployeeWhenExists)
{
set_Value ("UseRateEmployeeWhenExists", new Boolean(UseRateEmployeeWhenExists));
}
/** Get UseRateEmployeeWhenExists.
@return UseRateEmployeeWhenExists */
public boolean isUseRateEmployeeWhenExists() 
{
Object oo = get_Value("UseRateEmployeeWhenExists");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
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
/** Get Record ID/ColumnName
@return ID/ColumnName pair */
public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), getValue());
}

/** Set isVirtualAccrualBank.
@param isVirtualAccrualBank isVirtualAccrualBank */
public void setisVirtualAccrualBank (boolean isVirtualAccrualBank)
{
set_Value ("isVirtualAccrualBank", new Boolean(isVirtualAccrualBank));
}
/** Get isVirtualAccrualBank.
@return isVirtualAccrualBank */
public boolean isVirtualAccrualBank() 
{
Object oo = get_Value("isVirtualAccrualBank");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}

/** Type_Rate AD_Reference_ID=2000022 */
public static final int TYPE_RATE_AD_Reference_ID=2000022;
/** Employee Affectation = A */
public static final String TYPE_RATE_EmployeeAffectation = "A";
/** Vacance selon banque          = V */
public static final String TYPE_RATE_VacanceSelonBanque = "V";
/** Overtime = O */
public static final String TYPE_RATE_Overtime = "O";
/** Fixed Rate = F */
public static final String TYPE_RATE_FixedRate = "F";
/** Accumulated Days = D */
public static final String TYPE_RATE_AccumulatedDays = "D";
/** Short Term Disability = S */
public static final String TYPE_RATE_ShortTermDisability = "S";
/** CSST / WSIB = W */
public static final String TYPE_RATE_CSSTWSIB = "W";
/** Vacance taux employé = H */
public static final String TYPE_RATE_VacanceTauxEmploye = "H";
public static final String TYPE_RATE_VacanceTauxEmployé = "H";
/** Is test a valid value.

/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isType_RateValid (String test)
{
return test == null || test.equals("A") || test.equals("V") || test.equals("O") || test.equals("F") || test.equals("D") || test.equals("S") || test.equals("W") || test.equals("H");
}
/** Set Type Rate.
@param Type_Rate   */
public void setType_Rate (String Type_Rate)
{
if (!isType_RateValid(Type_Rate))
throw new IllegalArgumentException ("Type_Rate Invalid value - " + Type_Rate + " - Reference_ID=2000022 - A - V - O - F - D - S - W - H");
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

/** Credits_Acct_360 AD_Reference_ID=132 */
public static final int CREDITS_ACCT_360_AD_Reference_ID=132;
/** Set Credits_Acct_360.
@param Credits_Acct_360 Credits_Acct_360 */
public void setCredits_Acct_360 (int Credits_Acct_360)
{
set_Value ("Credits_Acct_360", new Integer(Credits_Acct_360));
}
/** Get Credits_Acct_360.
@return Credits_Acct_360 */
public int getCredits_Acct_360() 
{
Integer ii = (Integer)get_Value("Credits_Acct_360");
if (ii == null) return 0;
return ii.intValue();
}


}
