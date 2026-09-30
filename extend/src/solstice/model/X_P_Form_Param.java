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
/** Generated Model for P_Form_Param
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Form_Param extends PO
{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/** Standard Constructor
@param ctx context
@param P_Form_Param_ID id
@param trxName transaction
*/
public X_P_Form_Param (Properties ctx, int P_Form_Param_ID, String trxName)
{
super (ctx, P_Form_Param_ID, trxName);
/** if (P_Form_Param_ID == 0)
{
setCommunicationPreferred (null);
setP_Employee_Accounting_ID (0);
setP_Employee_ID (0);
setP_FORM_PARAM_ID (0);
setPhoneAreaCodeContactPerson (null);
setPhoneExtensionContactPerson (null);
setPhoneNumberContactPerson (null);
setSubmissionNumber (0);
setTransmitterNumber (null);
setType (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Form_Param (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100806 */
public static final int Table_ID=2100806;

/** TableName=P_Form_Param */
public static final String Table_Name="P_Form_Param";

protected static KeyNamePair Model = new KeyNamePair(2100806,"P_Form_Param");

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
StringBuffer sb = new StringBuffer ("X_P_Form_Param[").append(get_ID()).append("]");
return sb.toString();
}

/** CommunicationPreferred AD_Reference_ID=2000075 */
public static final int COMMUNICATIONPREFERRED_AD_Reference_ID=2000075;
/** English = E */
public static final String COMMUNICATIONPREFERRED_English = "E";
/** French = F */
public static final String COMMUNICATIONPREFERRED_French = "F";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isCommunicationPreferredValid (String test)
{
return test.equals("E") || test.equals("F");
}
/** Set Communication Preferred.
@param CommunicationPreferred Communication Preferred */
public void setCommunicationPreferred (String CommunicationPreferred)
{
if (CommunicationPreferred == null) throw new IllegalArgumentException ("CommunicationPreferred is mandatory");
if (!isCommunicationPreferredValid(CommunicationPreferred))
throw new IllegalArgumentException ("CommunicationPreferred Invalid value - " + CommunicationPreferred + " - Reference_ID=2000075 - E - F");
if (CommunicationPreferred.length() > 1)
{
log.warning("Length > 1 - truncated");
CommunicationPreferred = CommunicationPreferred.substring(0,0);
}
set_Value ("CommunicationPreferred", CommunicationPreferred);
}
/** Get Communication Preferred.
@return Communication Preferred */
public String getCommunicationPreferred() 
{
return (String)get_Value("CommunicationPreferred");
}

/** CommunicationPreferredAccoungtingPerson AD_Reference_ID=2000075 */
public static final int COMMUNICATIONPREFERREDACCOUNGTINGPERSON_AD_Reference_ID=2000075;
/** English = E */
public static final String COMMUNICATIONPREFERREDACCOUNGTINGPERSON_English = "E";
/** French = F */
public static final String COMMUNICATIONPREFERREDACCOUNGTINGPERSON_French = "F";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isCommunicationPreferredAccoungtingPersonValid (String test)
{
return test == null || test.equals("E") || test.equals("F");
}
/** Set Preferred Communication Language.
@param CommunicationPreferredAccoungtingPerson Preferred Communication Language */
public void setCommunicationPreferredAccoungtingPerson (String CommunicationPreferredAccoungtingPerson)
{
if (!isCommunicationPreferredAccoungtingPersonValid(CommunicationPreferredAccoungtingPerson))
throw new IllegalArgumentException ("CommunicationPreferredAccoungtingPerson Invalid value - " + CommunicationPreferredAccoungtingPerson + " - Reference_ID=2000075 - E - F");
if (CommunicationPreferredAccoungtingPerson != null && CommunicationPreferredAccoungtingPerson.length() > 1)
{
log.warning("Length > 1 - truncated");
CommunicationPreferredAccoungtingPerson = CommunicationPreferredAccoungtingPerson.substring(0,0);
}
set_Value ("CommunicationPreferredAccoungtingPerson", CommunicationPreferredAccoungtingPerson);
}
/** Get Preferred Communication Language.
@return Preferred Communication Language */
public String getCommunicationPreferredAccoungtingPerson() 
{
return (String)get_Value("CommunicationPreferredAccoungtingPerson");
}
/** Set EMail Address.
@param EMail Electronic Mail Address */
public void setEMail (String EMail)
{
if (EMail != null && EMail.length() > 40)
{
log.warning("Length > 40 - truncated");
EMail = EMail.substring(0,39);
}
set_Value ("EMail", EMail);
}
/** Get EMail Address.
@return Electronic Mail Address */
public String getEMail() 
{
return (String)get_Value("EMail");
}
/** Set Facsimile No..
@param FacsimileNumber Facsimile No. */
public void setFacsimileNumber (String FacsimileNumber)
{
if (FacsimileNumber != null && FacsimileNumber.length() > 30)
{
log.warning("Length > 30 - truncated");
FacsimileNumber = FacsimileNumber.substring(0,29);
}
set_Value ("FacsimileNumber", FacsimileNumber);
}
/** Get Facsimile No..
@return Facsimile No. */
public String getFacsimileNumber() 
{
return (String)get_Value("FacsimileNumber");
}
/** Set File Number.
@param FileNumber File Number */
public void setFileNumber (String FileNumber)
{
if (FileNumber != null && FileNumber.length() > 4)
{
log.warning("Length > 4 - truncated");
FileNumber = FileNumber.substring(0,3);
}
set_Value ("FileNumber", FileNumber);
}
/** Get File Number.
@return File Number */
public String getFileNumber() 
{
return (String)get_Value("FileNumber");
}
/** Set Releve 1 (RL-1).
@param FormNumber_RL1 Releve 1 (RL-1) */
public void setFormNumber_RL1 (int FormNumber_RL1)
{
set_Value ("FormNumber_RL1", new Integer(FormNumber_RL1));
}
/** Get Releve 1 (RL-1).
@return Releve 1 (RL-1) */
public int getFormNumber_RL1() 
{
Integer ii = (Integer)get_Value("FormNumber_RL1");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Releve 2 (RL-2).
@param FormNumber_RL2 Releve 2 (RL-2) */
public void setFormNumber_RL2 (int FormNumber_RL2)
{
set_Value ("FormNumber_RL2", new Integer(FormNumber_RL2));
}
/** Get Releve 2 (RL-2).
@return Releve 2 (RL-2) */
public int getFormNumber_RL2() 
{
Integer ii = (Integer)get_Value("FormNumber_RL2");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Releve 3 (RL-3).
@param FormNumber_RL3 Releve 3 (RL-3) */
public void setFormNumber_RL3 (int FormNumber_RL3)
{
set_Value ("FormNumber_RL3", new Integer(FormNumber_RL3));
}
/** Get Releve 3 (RL-3).
@return Releve 3 (RL-3) */
public int getFormNumber_RL3() 
{
Integer ii = (Integer)get_Value("FormNumber_RL3");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Quebec Enterprise Number.
@param NEQ Quebec Enterprise Number */
public void setNEQ (String NEQ)
{
if (NEQ != null && NEQ.length() > 10)
{
log.warning("Length > 10 - truncated");
NEQ = NEQ.substring(0,9);
}
set_Value ("NEQ", NEQ);
}
/** Get Quebec Enterprise Number.
@return Quebec Enterprise Number */
public String getNEQ() 
{
return (String)get_Value("NEQ");
}
/** Set Employee accounting.
@param P_Employee_Accounting_ID Employee accounting */
public void setP_Employee_Accounting_ID (int P_Employee_Accounting_ID)
{
if (P_Employee_Accounting_ID < 1) throw new IllegalArgumentException ("P_Employee_Accounting_ID is mandatory.");
set_Value ("P_Employee_Accounting_ID", new Integer(P_Employee_Accounting_ID));
}
/** Get Employee accounting.
@return Employee accounting */
public int getP_Employee_Accounting_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Accounting_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Employee.
@param P_Employee_ID Employee */
public void setP_Employee_ID (int P_Employee_ID)
{
if (P_Employee_ID < 1) throw new IllegalArgumentException ("P_Employee_ID is mandatory.");
set_Value ("P_Employee_ID", new Integer(P_Employee_ID));
}
/** Get Employee.
@return Employee */
public int getP_Employee_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_FORM_PARAM_ID.
@param P_FORM_PARAM_ID P_FORM_PARAM_ID */
public void setP_FORM_PARAM_ID (int P_FORM_PARAM_ID)
{
if (P_FORM_PARAM_ID < 1) throw new IllegalArgumentException ("P_FORM_PARAM_ID is mandatory.");
set_ValueNoCheck ("P_FORM_PARAM_ID", new Integer(P_FORM_PARAM_ID));
}
/** Get P_FORM_PARAM_ID.
@return P_FORM_PARAM_ID */
public int getP_FORM_PARAM_ID() 
{
Integer ii = (Integer)get_Value("P_FORM_PARAM_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** PackageType AD_Reference_ID=2100442 */
public static final int PACKAGETYPE_AD_Reference_ID=2100442;
/** Original File = 1 */
public static final String PACKAGETYPE_OriginalFile = "1";
/** Replacement of an Original File = 2 */
public static final String PACKAGETYPE_ReplacementOfAnOriginalFile = "2";
/** File - Test = 3 */
public static final String PACKAGETYPE_File_Test = "3";
/** Modified File = 4 */
public static final String PACKAGETYPE_ModifiedFile = "4";
/** Replacement of a Modified File = 5 */
public static final String PACKAGETYPE_ReplacementOfAModifiedFile = "5";
/** Deleted File = 6 */
public static final String PACKAGETYPE_DeletedFile = "6";
/** Fichier de remplacement d'un fichier annule = 7 */
public static final String PACKAGETYPE_FichierDeRemplacementDUnFichierAnnule = "7";
/** Replace Erroneous Data = 8 */
public static final String PACKAGETYPE_ReplaceErroneousData = "8";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isPackageTypeValid (String test)
{
return test == null || test.equals("1") || test.equals("2") || test.equals("3") || test.equals("4") || test.equals("5") || test.equals("6") || test.equals("7") || test.equals("8");
}
/** Set File Type.
@param PackageType File Type */
public void setPackageType (String PackageType)
{
if (!isPackageTypeValid(PackageType))
throw new IllegalArgumentException ("PackageType Invalid value - " + PackageType + " - Reference_ID=2100442 - 1 - 2 - 3 - 4 - 5 - 6 - 7 - 8");
if (PackageType != null && PackageType.length() > 1)
{
log.warning("Length > 1 - truncated");
PackageType = PackageType.substring(0,0);
}
set_Value ("PackageType", PackageType);
}
/** Get File Type.
@return File Type */
public String getPackageType() 
{
return (String)get_Value("PackageType");
}
/** Set Phone Area Code.
@param PhoneAreaCodeAccountingPerson Phone Area Code */
public void setPhoneAreaCodeAccountingPerson (String PhoneAreaCodeAccountingPerson)
{
if (PhoneAreaCodeAccountingPerson != null && PhoneAreaCodeAccountingPerson.length() > 3)
{
log.warning("Length > 3 - truncated");
PhoneAreaCodeAccountingPerson = PhoneAreaCodeAccountingPerson.substring(0,2);
}
set_Value ("PhoneAreaCodeAccountingPerson", PhoneAreaCodeAccountingPerson);
}
/** Get Phone Area Code.
@return Phone Area Code */
public String getPhoneAreaCodeAccountingPerson() 
{
return (String)get_Value("PhoneAreaCodeAccountingPerson");
}
/** Set Phone Area Code.
@param PhoneAreaCodeContactPerson Phone Area Code */
public void setPhoneAreaCodeContactPerson (String PhoneAreaCodeContactPerson)
{
if (PhoneAreaCodeContactPerson == null) throw new IllegalArgumentException ("PhoneAreaCodeContactPerson is mandatory.");
if (PhoneAreaCodeContactPerson.length() > 3)
{
log.warning("Length > 3 - truncated");
PhoneAreaCodeContactPerson = PhoneAreaCodeContactPerson.substring(0,2);
}
set_Value ("PhoneAreaCodeContactPerson", PhoneAreaCodeContactPerson);
}
/** Get Phone Area Code.
@return Phone Area Code */
public String getPhoneAreaCodeContactPerson() 
{
return (String)get_Value("PhoneAreaCodeContactPerson");
}
/** Set Phone Extension.
@param PhoneExtensionAccountingPerson Phone Extension */
public void setPhoneExtensionAccountingPerson (String PhoneExtensionAccountingPerson)
{
if (PhoneExtensionAccountingPerson != null && PhoneExtensionAccountingPerson.length() > 4)
{
log.warning("Length > 4 - truncated");
PhoneExtensionAccountingPerson = PhoneExtensionAccountingPerson.substring(0,3);
}
set_Value ("PhoneExtensionAccountingPerson", PhoneExtensionAccountingPerson);
}
/** Get Phone Extension.
@return Phone Extension */
public String getPhoneExtensionAccountingPerson() 
{
return (String)get_Value("PhoneExtensionAccountingPerson");
}
/** Set Phone Extension .
@param PhoneExtensionContactPerson Phone Extension  */
public void setPhoneExtensionContactPerson (String PhoneExtensionContactPerson)
{
if (PhoneExtensionContactPerson == null) throw new IllegalArgumentException ("PhoneExtensionContactPerson is mandatory.");
if (PhoneExtensionContactPerson.length() > 4)
{
log.warning("Length > 4 - truncated");
PhoneExtensionContactPerson = PhoneExtensionContactPerson.substring(0,3);
}
set_Value ("PhoneExtensionContactPerson", PhoneExtensionContactPerson);
}
/** Get Phone Extension .
@return Phone Extension  */
public String getPhoneExtensionContactPerson() 
{
return (String)get_Value("PhoneExtensionContactPerson");
}
/** Set Phone Nunber.
@param PhoneNumberAccountingPerson Phone Nunber */
public void setPhoneNumberAccountingPerson (String PhoneNumberAccountingPerson)
{
if (PhoneNumberAccountingPerson != null && PhoneNumberAccountingPerson.length() > 8)
{
log.warning("Length > 8 - truncated");
PhoneNumberAccountingPerson = PhoneNumberAccountingPerson.substring(0,7);
}
set_Value ("PhoneNumberAccountingPerson", PhoneNumberAccountingPerson);
}
/** Get Phone Nunber.
@return Phone Nunber */
public String getPhoneNumberAccountingPerson() 
{
return (String)get_Value("PhoneNumberAccountingPerson");
}
/** Set Phone number.
@param PhoneNumberContactPerson Phone number */
public void setPhoneNumberContactPerson (String PhoneNumberContactPerson)
{
if (PhoneNumberContactPerson == null) throw new IllegalArgumentException ("PhoneNumberContactPerson is mandatory.");
if (PhoneNumberContactPerson.length() > 8)
{
log.warning("Length > 8 - truncated");
PhoneNumberContactPerson = PhoneNumberContactPerson.substring(0,7);
}
set_Value ("PhoneNumberContactPerson", PhoneNumberContactPerson);
}
/** Get Phone number.
@return Phone number */
public String getPhoneNumberContactPerson() 
{
return (String)get_Value("PhoneNumberContactPerson");
}
/** Set ShemaValue.
@param ShemaValue ShemaValue */
public void setShemaValue (String ShemaValue)
{
if (ShemaValue != null && ShemaValue.length() > 30)
{
log.warning("Length > 30 - truncated");
ShemaValue = ShemaValue.substring(0,29);
}
set_Value ("ShemaValue", ShemaValue);
}
/** Get ShemaValue.
@return ShemaValue */
public String getShemaValue() 
{
return (String)get_Value("ShemaValue");
}

/** Source AD_Reference_ID=2100443 */
public static final int SOURCE_AD_Reference_ID=2100443;
/** ROEs obtained from Revenu Quebec = A */
public static final String SOURCE_ROEsObtainedFromRevenuQuebec = "A";
/** Fac-similes obtenus d'un tiers = B */
public static final String SOURCE_Fac_SimilesObtenusDUnTiers = "B";
/** Facsimles produced by = C */
public static final String SOURCE_FacsimlesProducedBy = "C";
/** Combination of above-mentioned cases = D */
public static final String SOURCE_CombinationOfAbove_MentionedCases = "D";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isSourceValid (String test)
{
return test == null || test.equals("A") || test.equals("B") || test.equals("C") || test.equals("D");
}
/** Set Source.
@param Source Source */
public void setSource (String Source)
{
if (!isSourceValid(Source))
throw new IllegalArgumentException ("Source Invalid value - " + Source + " - Reference_ID=2100443 - A - B - C - D");
if (Source != null && Source.length() > 1)
{
log.warning("Length > 1 - truncated");
Source = Source.substring(0,0);
}
set_Value ("Source", Source);
}
/** Get Source.
@return Source */
public String getSource() 
{
return (String)get_Value("Source");
}
/** Set Submission Number.
@param SubmissionNumber Submission Number */
public void setSubmissionNumber (int SubmissionNumber)
{
set_Value ("SubmissionNumber", new Integer(SubmissionNumber));
}
/** Get Submission Number.
@return Submission Number */
public int getSubmissionNumber() 
{
Integer ii = (Integer)get_Value("SubmissionNumber");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Transmittal Number.
@param TransmitterNumber Transmittal Number */
public void setTransmitterNumber (String TransmitterNumber)
{
if (TransmitterNumber == null) throw new IllegalArgumentException ("TransmitterNumber is mandatory.");
if (TransmitterNumber.length() > 8)
{
log.warning("Length > 8 - truncated");
TransmitterNumber = TransmitterNumber.substring(0,7);
}
set_Value ("TransmitterNumber", TransmitterNumber);
}
/** Get Transmittal Number.
@return Transmittal Number */
public String getTransmitterNumber() 
{
return (String)get_Value("TransmitterNumber");
}

/** TransmitterType AD_Reference_ID=2100444 */
public static final int TRANSMITTERTYPE_AD_Reference_ID=2100444;
/** Create Files for Yourself = 1 */
public static final String TRANSMITTERTYPE_CreateFilesForYourself = "1";
/** Create Files for Other Declarants = 2 */
public static final String TRANSMITTERTYPE_CreateFilesForOtherDeclarants = "2";
/** Create Files for Yourself and Others = 3 */
public static final String TRANSMITTERTYPE_CreateFilesForYourselfAndOthers = "3";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isTransmitterTypeValid (String test)
{
return test == null || test.equals("1") || test.equals("2") || test.equals("3");
}
/** Set Transmission Options.
@param TransmitterType Transmission Options */
public void setTransmitterType (String TransmitterType)
{
if (!isTransmitterTypeValid(TransmitterType))
throw new IllegalArgumentException ("TransmitterType Invalid value - " + TransmitterType + " - Reference_ID=2100444 - 1 - 2 - 3");
if (TransmitterType != null && TransmitterType.length() > 1)
{
log.warning("Length > 1 - truncated");
TransmitterType = TransmitterType.substring(0,0);
}
set_Value ("TransmitterType", TransmitterType);
}
/** Get Transmission Options.
@return Transmission Options */
public String getTransmitterType() 
{
return (String)get_Value("TransmitterType");
}

/** Type AD_Reference_ID=2100445 */
public static final int TYPE_AD_Reference_ID=2100445;
/** Provincial = P */
public static final String TYPE_Provincial = "P";
/** Federal = F */
public static final String TYPE_Federal = "F";
/** Canada Savings Bonds Contributions = O */
public static final String TYPE_CanadaSavingsBondsContributions = "O";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isTypeValid (String test)
{
return test.equals("P") || test.equals("F") || test.equals("O");
}
/** Set Code Type.
@param Type Type of Code/Validation (SQL, Java Script, Java Language) */
public void setType (String Type)
{
if (Type == null) throw new IllegalArgumentException ("Type is mandatory");
if (!isTypeValid(Type))
throw new IllegalArgumentException ("Type Invalid value - " + Type + " - Reference_ID=2100445 - P - F - O");
if (Type.length() > 1)
{
log.warning("Length > 1 - truncated");
Type = Type.substring(0,0);
}
set_Value ("Type", Type);
}
/** Get Code Type.
@return Type of Code/Validation (SQL, Java Script, Java Language) */
public String getType() 
{
return (String)get_Value("Type");
}
}
