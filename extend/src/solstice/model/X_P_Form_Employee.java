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
/** Generated Model for P_Form_Employee
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Form_Employee extends PO
{
/** Standard Constructor
@param ctx context
@param P_Form_Employee_ID id
@param trxName transaction
*/
public X_P_Form_Employee (Properties ctx, int P_Form_Employee_ID, String trxName)
{
super (ctx, P_Form_Employee_ID, trxName);
/** if (P_Form_Employee_ID == 0)
{
setP_Employee_ID (0);
setP_Form_Employee_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Form_Employee (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100669 */
public static final int Table_ID=2100669;

/** TableName=P_Form_Employee */
public static final String Table_Name="P_Form_Employee";

protected static KeyNamePair Model = new KeyNamePair(2100669,"P_Form_Employee");

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
StringBuffer sb = new StringBuffer ("X_P_Form_Employee[").append(get_ID()).append("]");
return sb.toString();
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
/** Set FormNumber.
@param FormNumber FormNumber */
public void setFormNumber (String FormNumber)
{
if (FormNumber != null && FormNumber.length() > 30)
{
log.warning("Length > 30 - truncated");
FormNumber = FormNumber.substring(0,29);
}
set_Value ("FormNumber", FormNumber);
}
/** Get FormNumber.
@return FormNumber */
public String getFormNumber() 
{
return (String)get_Value("FormNumber");
}

/** FormType AD_Reference_ID=2100439 */
public static final int FORMTYPE_AD_Reference_ID=2100439;
/** Original = O */
public static final String FORMTYPE_Original = "O";
/** Modified = M */
public static final String FORMTYPE_Modified = "M";
/** Cancelled = C */
public static final String FORMTYPE_Cancelled = "C";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isFormTypeValid (String test)
{
return test == null || test.equals("O") || test.equals("M") || test.equals("C");
}
/** Set Form Type.
@param FormType Form Type */
public void setFormType (String FormType)
{
if (!isFormTypeValid(FormType))
throw new IllegalArgumentException ("FormType Invalid value - " + FormType + " - Reference_ID=2100439 - O - M - C");
if (FormType != null && FormType.length() > 1)
{
log.warning("Length > 1 - truncated");
FormType = FormType.substring(0,0);
}
set_Value ("FormType", FormType);
}
/** Get Form Type.
@return Form Type */
public String getFormType() 
{
return (String)get_Value("FormType");
}
/** Set Generated.
@param IsGenerated This Line is generated */
public void setIsGenerated (boolean IsGenerated)
{
set_Value ("IsGenerated", new Boolean(IsGenerated));
}
/** Get Generated.
@return This Line is generated */
public boolean isGenerated() 
{
Object oo = get_Value("IsGenerated");
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

/** P_Employer_ID AD_Reference_ID=2000042 */
public static final int P_EMPLOYER_ID_AD_Reference_ID=2000042;
/** Set Employer.
@param P_Employer_ID Employer */
public void setP_Employer_ID (int P_Employer_ID)
{
if (P_Employer_ID <= 0) set_Value ("P_Employer_ID", null);
 else 
set_Value ("P_Employer_ID", new Integer(P_Employer_ID));
}
/** Get Employer.
@return Employer */
public int getP_Employer_ID() 
{
Integer ii = (Integer)get_Value("P_Employer_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Employee.
@param P_Form_Employee_ID Employee */
public void setP_Form_Employee_ID (int P_Form_Employee_ID)
{
if (P_Form_Employee_ID < 1) throw new IllegalArgumentException ("P_Form_Employee_ID is mandatory.");
set_ValueNoCheck ("P_Form_Employee_ID", new Integer(P_Form_Employee_ID));
}
/** Get Employee.
@return Employee */
public int getP_Form_Employee_ID() 
{
Integer ii = (Integer)get_Value("P_Form_Employee_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Form_ID AD_Reference_ID=2100336 */
public static final int P_FORM_ID_AD_Reference_ID=2100336;
/** Set Form.
@param P_Form_ID Form */
public void setP_Form_ID (int P_Form_ID)
{
if (P_Form_ID <= 0) set_Value ("P_Form_ID", null);
 else 
set_Value ("P_Form_ID", new Integer(P_Form_ID));
}
/** Get Form.
@return Form */
public int getP_Form_ID() 
{
Integer ii = (Integer)get_Value("P_Form_ID");
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
/** Get Record ID/ColumnName
@return ID/ColumnName pair */
public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), String.valueOf(getP_Year_ID()));
}

/** Taxation_Region_ID AD_Reference_ID=2000026 */
public static final int TAXATION_REGION_ID_AD_Reference_ID=2000026;
/** Set Taxation Region.
@param Taxation_Region_ID Taxation Region */
public void setTaxation_Region_ID (int Taxation_Region_ID)
{
if (Taxation_Region_ID <= 0) set_Value ("Taxation_Region_ID", null);
 else 
set_Value ("Taxation_Region_ID", new Integer(Taxation_Region_ID));
}
/** Get Taxation Region.
@return Taxation Region */
public int getTaxation_Region_ID() 
{
Integer ii = (Integer)get_Value("Taxation_Region_ID");
if (ii == null) return 0;
return ii.intValue();
}
}
