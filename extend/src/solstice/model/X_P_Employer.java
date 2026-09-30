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
/** Generated Model for P_Employer
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Employer extends PO
{
/** Standard Constructor
@param ctx context
@param P_Employer_ID id
@param trxName transaction
*/
public X_P_Employer (Properties ctx, int P_Employer_ID, String trxName)
{
super (ctx, P_Employer_ID, trxName);
/** if (P_Employer_ID == 0)
{
setC_Location_ID (0);
setCanadaRevenueBusinessNumber (null);
setCommunicationPreferredIn (null);	// F
setEmployerPayrollReferenceNbr (null);
setName (null);
setP_Employer_ID (0);
setPrintLanguageRoe (null);	// F
setType (null);
setValue (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Employer (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000003 */
public static final int Table_ID=2000003;

/** TableName=P_Employer */
public static final String Table_Name="P_Employer";

protected static KeyNamePair Model = new KeyNamePair(2000003,"P_Employer");

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
StringBuffer sb = new StringBuffer ("X_P_Employer[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Address.
@param C_Location_ID Location or Address */
public void setC_Location_ID (int C_Location_ID)
{
if (C_Location_ID < 1) throw new IllegalArgumentException ("C_Location_ID is mandatory.");
set_Value ("C_Location_ID", new Integer(C_Location_ID));
}
/** Get Address.
@return Location or Address */
public int getC_Location_ID() 
{
Integer ii = (Integer)get_Value("C_Location_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Canada Revenue Agency Business number.
@param CanadaRevenueBusinessNumber Canada Revenue Agency Business */
public void setCanadaRevenueBusinessNumber (String CanadaRevenueBusinessNumber)
{
if (CanadaRevenueBusinessNumber == null) throw new IllegalArgumentException ("CanadaRevenueBusinessNumber is mandatory.");
if (CanadaRevenueBusinessNumber.length() > 15)
{
log.warning("Length > 15 - truncated");
CanadaRevenueBusinessNumber = CanadaRevenueBusinessNumber.substring(0,14);
}
set_Value ("CanadaRevenueBusinessNumber", CanadaRevenueBusinessNumber);
}
/** Get Canada Revenue Agency Business number.
@return Canada Revenue Agency Business */
public String getCanadaRevenueBusinessNumber() 
{
return (String)get_Value("CanadaRevenueBusinessNumber");
}

/** CommunicationPreferredIn AD_Reference_ID=2000075 */
public static final int COMMUNICATIONPREFERREDIN_AD_Reference_ID=2000075;
/** English = E */
public static final String COMMUNICATIONPREFERREDIN_English = "E";
/** French = F */
public static final String COMMUNICATIONPREFERREDIN_French = "F";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isCommunicationPreferredInValid (String test)
{
return test.equals("E") || test.equals("F");
}
/** Set Communication Preferred In.
@param CommunicationPreferredIn Communication Preferred In */
public void setCommunicationPreferredIn (String CommunicationPreferredIn)
{
if (CommunicationPreferredIn == null) throw new IllegalArgumentException ("CommunicationPreferredIn is mandatory");
if (!isCommunicationPreferredInValid(CommunicationPreferredIn))
throw new IllegalArgumentException ("CommunicationPreferredIn Invalid value - " + CommunicationPreferredIn + " - Reference_ID=2000075 - E - F");
if (CommunicationPreferredIn.length() > 1)
{
log.warning("Length > 1 - truncated");
CommunicationPreferredIn = CommunicationPreferredIn.substring(0,0);
}
set_Value ("CommunicationPreferredIn", CommunicationPreferredIn);
}
/** Get Communication Preferred In.
@return Communication Preferred In */
public String getCommunicationPreferredIn() 
{
return (String)get_Value("CommunicationPreferredIn");
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
/** Set Ref. Cnt.
@param Employer_No_Cnt Commission des normes du trava */
public void setEmployer_No_Cnt (String Employer_No_Cnt)
{
if (Employer_No_Cnt != null && Employer_No_Cnt.length() > 30)
{
log.warning("Length > 30 - truncated");
Employer_No_Cnt = Employer_No_Cnt.substring(0,29);
}
set_Value ("Employer_No_Cnt", Employer_No_Cnt);
}
/** Get Ref. Cnt.
@return Commission des normes du trava */
public String getEmployer_No_Cnt() 
{
return (String)get_Value("Employer_No_Cnt");
}
/** Set Ref. Csst.
@param Employer_No_Csst Commission santé sécurité au travail */
public void setEmployer_No_Csst (String Employer_No_Csst)
{
if (Employer_No_Csst != null && Employer_No_Csst.length() > 30)
{
log.warning("Length > 30 - truncated");
Employer_No_Csst = Employer_No_Csst.substring(0,29);
}
set_Value ("Employer_No_Csst", Employer_No_Csst);
}
/** Get Ref. Csst.
@return Commission santé sécurité au travail */
public String getEmployer_No_Csst() 
{
return (String)get_Value("Employer_No_Csst");
}
/** Set Employer’s payroll no.
@param EmployerPayrollReferenceNbr Employer’s payroll reference n */
public void setEmployerPayrollReferenceNbr (String EmployerPayrollReferenceNbr)
{
if (EmployerPayrollReferenceNbr == null) throw new IllegalArgumentException ("EmployerPayrollReferenceNbr is mandatory.");
if (EmployerPayrollReferenceNbr.length() > 15)
{
log.warning("Length > 15 - truncated");
EmployerPayrollReferenceNbr = EmployerPayrollReferenceNbr.substring(0,14);
}
set_Value ("EmployerPayrollReferenceNbr", EmployerPayrollReferenceNbr);
}
/** Get Employer’s payroll no.
@return Employer’s payroll reference n */
public String getEmployerPayrollReferenceNbr() 
{
return (String)get_Value("EmployerPayrollReferenceNbr");
}
/** Set First name (only).
@param FirstNameContactPerson First name (only) for the cont */
public void setFirstNameContactPerson (String FirstNameContactPerson)
{
if (FirstNameContactPerson != null && FirstNameContactPerson.length() > 18)
{
log.warning("Length > 18 - truncated");
FirstNameContactPerson = FirstNameContactPerson.substring(0,17);
}
set_Value ("FirstNameContactPerson", FirstNameContactPerson);
}
/** Get First name (only).
@return First name (only) for the cont */
public String getFirstNameContactPerson() 
{
return (String)get_Value("FirstNameContactPerson");
}
/** Set Initial .
@param InitialContactPerson Initial for the contact person */
public void setInitialContactPerson (String InitialContactPerson)
{
if (InitialContactPerson != null && InitialContactPerson.length() > 18)
{
log.warning("Length > 18 - truncated");
InitialContactPerson = InitialContactPerson.substring(0,17);
}
set_Value ("InitialContactPerson", InitialContactPerson);
}
/** Get Initial .
@return Initial for the contact person */
public String getInitialContactPerson() 
{
return (String)get_Value("InitialContactPerson");
}
/** Set Default.
@param IsDefault Default value */
public void setIsDefault (boolean IsDefault)
{
set_Value ("IsDefault", new Boolean(IsDefault));
}
/** Get Default.
@return Default value */
public boolean isDefault() 
{
Object oo = get_Value("IsDefault");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Reduced EI.
@param IsReducedEI Reduced E.I employer */
public void setIsReducedEI (boolean IsReducedEI)
{
set_Value ("IsReducedEI", new Boolean(IsReducedEI));
}
/** Get Reduced EI.
@return Reduced E.I employer */
public boolean isReducedEI() 
{
Object oo = get_Value("IsReducedEI");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Last name .
@param LastNameContactPerson Last name for the contact pers */
public void setLastNameContactPerson (String LastNameContactPerson)
{
if (LastNameContactPerson != null && LastNameContactPerson.length() > 50)
{
log.warning("Length > 50 - truncated");
LastNameContactPerson = LastNameContactPerson.substring(0,49);
}
set_Value ("LastNameContactPerson", LastNameContactPerson);
}
/** Get Last name .
@return Last name for the contact pers */
public String getLastNameContactPerson() 
{
return (String)get_Value("LastNameContactPerson");
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
/** Get Record ID/ColumnName
@return ID/ColumnName pair */
public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), getName());
}
/** Set Employer.
@param P_Employer_ID Employer */
public void setP_Employer_ID (int P_Employer_ID)
{
if (P_Employer_ID < 1) throw new IllegalArgumentException ("P_Employer_ID is mandatory.");
set_ValueNoCheck ("P_Employer_ID", new Integer(P_Employer_ID));
}
/** Get Employer.
@return Employer */
public int getP_Employer_ID() 
{
Integer ii = (Integer)get_Value("P_Employer_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Phone area code .
@param PhoneAreaCodeContactPerson Phone area code Contact Person */
public void setPhoneAreaCodeContactPerson (String PhoneAreaCodeContactPerson)
{
if (PhoneAreaCodeContactPerson != null && PhoneAreaCodeContactPerson.length() > 3)
{
log.warning("Length > 3 - truncated");
PhoneAreaCodeContactPerson = PhoneAreaCodeContactPerson.substring(0,2);
}
set_Value ("PhoneAreaCodeContactPerson", PhoneAreaCodeContactPerson);
}
/** Get Phone area code .
@return Phone area code Contact Person */
public String getPhoneAreaCodeContactPerson() 
{
return (String)get_Value("PhoneAreaCodeContactPerson");
}
/** Set Phone Extension .
@param PhoneExtensionContactPerson Phone Extension  */
public void setPhoneExtensionContactPerson (String PhoneExtensionContactPerson)
{
if (PhoneExtensionContactPerson != null && PhoneExtensionContactPerson.length() > 8)
{
log.warning("Length > 8 - truncated");
PhoneExtensionContactPerson = PhoneExtensionContactPerson.substring(0,7);
}
set_Value ("PhoneExtensionContactPerson", PhoneExtensionContactPerson);
}
/** Get Phone Extension .
@return Phone Extension  */
public String getPhoneExtensionContactPerson() 
{
return (String)get_Value("PhoneExtensionContactPerson");
}
/** Set Phone number.
@param PhoneNumberContactPerson Phone number Contact Person */
public void setPhoneNumberContactPerson (String PhoneNumberContactPerson)
{
if (PhoneNumberContactPerson != null && PhoneNumberContactPerson.length() > 8)
{
log.warning("Length > 8 - truncated");
PhoneNumberContactPerson = PhoneNumberContactPerson.substring(0,7);
}
set_Value ("PhoneNumberContactPerson", PhoneNumberContactPerson);
}
/** Get Phone number.
@return Phone number Contact Person */
public String getPhoneNumberContactPerson() 
{
return (String)get_Value("PhoneNumberContactPerson");
}

/** PrintLanguageRoe AD_Reference_ID=2000074 */
public static final int PRINTLANGUAGEROE_AD_Reference_ID=2000074;
/** English = E */
public static final String PRINTLANGUAGEROE_English = "E";
/** French = F */
public static final String PRINTLANGUAGEROE_French = "F";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isPrintLanguageRoeValid (String test)
{
return test.equals("E") || test.equals("F");
}
/** Set Print language to be used in printing the ROE.
@param PrintLanguageRoe Print language to be used in printing the ROE */
public void setPrintLanguageRoe (String PrintLanguageRoe)
{
if (PrintLanguageRoe == null) throw new IllegalArgumentException ("PrintLanguageRoe is mandatory");
if (!isPrintLanguageRoeValid(PrintLanguageRoe))
throw new IllegalArgumentException ("PrintLanguageRoe Invalid value - " + PrintLanguageRoe + " - Reference_ID=2000074 - E - F");
if (PrintLanguageRoe.length() > 1)
{
log.warning("Length > 1 - truncated");
PrintLanguageRoe = PrintLanguageRoe.substring(0,0);
}
set_Value ("PrintLanguageRoe", PrintLanguageRoe);
}
/** Get Print language to be used in printing the ROE.
@return Print language to be used in printing the ROE */
public String getPrintLanguageRoe() 
{
return (String)get_Value("PrintLanguageRoe");
}
/** Set Signatory.
@param Signatory Signatory */
public void setSignatory (String Signatory)
{
if (Signatory != null && Signatory.length() > 60)
{
log.warning("Length > 60 - truncated");
Signatory = Signatory.substring(0,59);
}
set_Value ("Signatory", Signatory);
}
/** Get Signatory.
@return Signatory */
public String getSignatory() 
{
return (String)get_Value("Signatory");
}

/** Type AD_Reference_ID=2100445 */
public static final int TYPE_AD_Reference_ID=2100445;
/** Obligation Epargne Canada = O */
public static final String TYPE_ObligationEpargneCanada = "O";
/** Provincial = P */
public static final String TYPE_Provincial = "P";
/** Federal = F */
public static final String TYPE_Federal = "F";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isTypeValid (String test)
{
return test.equals("O") || test.equals("P") || test.equals("F");
}
/** Set Type.
@param Type Type of Validation (SQL, Java Script, Java Language) */
public void setType (String Type)
{
if (Type == null) throw new IllegalArgumentException ("Type is mandatory");
if (!isTypeValid(Type))
throw new IllegalArgumentException ("Type Invalid value - " + Type + " - Reference_ID=2100445 - O - P - F");
if (Type.length() > 1)
{
log.warning("Length > 1 - truncated");
Type = Type.substring(0,0);
}
set_Value ("Type", Type);
}
/** Get Type.
@return Type of Validation (SQL, Java Script, Java Language) */
public String getType() 
{
return (String)get_Value("Type");
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
}
