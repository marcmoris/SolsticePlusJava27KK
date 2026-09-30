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
/** Generated Model for P_Insurance_Program
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Insurance_Program extends PO
{
/** Standard Constructor
@param ctx context
@param P_Insurance_Program_ID id
@param trxName transaction
*/
public X_P_Insurance_Program (Properties ctx, int P_Insurance_Program_ID, String trxName)
{
super (ctx, P_Insurance_Program_ID, trxName);
/** if (P_Insurance_Program_ID == 0)
{
setP_Insurance_Program_ID (0);
setP_Profile_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Insurance_Program (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100863 */
public static final int Table_ID=2100863;

/** TableName=P_Insurance_Program */
public static final String Table_Name="P_Insurance_Program";

protected static KeyNamePair Model = new KeyNamePair(2100863,"P_Insurance_Program");

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
StringBuffer sb = new StringBuffer ("X_P_Insurance_Program[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Age Limit Max.
@param AgeLimitMax Age Limit Max */
public void setAgeLimitMax (BigDecimal AgeLimitMax)
{
set_Value ("AgeLimitMax", AgeLimitMax);
}
/** Get Age Limit Max.
@return Age Limit Max */
public BigDecimal getAgeLimitMax() 
{
BigDecimal bd = (BigDecimal)get_Value("AgeLimitMax");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Couple Max Employer.
@param CoupleMaxEmployer Couple Max Employer */
public void setCoupleMaxEmployer (BigDecimal CoupleMaxEmployer)
{
set_Value ("CoupleMaxEmployer", CoupleMaxEmployer);
}
/** Get Couple Max Employer.
@return Couple Max Employer */
public BigDecimal getCoupleMaxEmployer() 
{
BigDecimal bd = (BigDecimal)get_Value("CoupleMaxEmployer");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Exempt Max Employer.
@param ExemptMaxEmployer Exempt Max Employer */
public void setExemptMaxEmployer (BigDecimal ExemptMaxEmployer)
{
set_Value ("ExemptMaxEmployer", ExemptMaxEmployer);
}
/** Get Exempt Max Employer.
@return Exempt Max Employer */
public BigDecimal getExemptMaxEmployer() 
{
BigDecimal bd = (BigDecimal)get_Value("ExemptMaxEmployer");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Family Max Employer.
@param FamilyMaxEmployer Family Max Employer */
public void setFamilyMaxEmployer (BigDecimal FamilyMaxEmployer)
{
set_Value ("FamilyMaxEmployer", FamilyMaxEmployer);
}
/** Get Family Max Employer.
@return Family Max Employer */
public BigDecimal getFamilyMaxEmployer() 
{
BigDecimal bd = (BigDecimal)get_Value("FamilyMaxEmployer");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Individual Max Employer.
@param IndividualMaxEmployer Individual Max Employer */
public void setIndividualMaxEmployer (BigDecimal IndividualMaxEmployer)
{
set_Value ("IndividualMaxEmployer", IndividualMaxEmployer);
}
/** Get Individual Max Employer.
@return Individual Max Employer */
public BigDecimal getIndividualMaxEmployer() 
{
BigDecimal bd = (BigDecimal)get_Value("IndividualMaxEmployer");
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
/** Set Name .
@param Name Alphanumeric identifier of the entity */
public void setName (String Name)
{
if (Name != null && Name.length() > 60)
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
/** Get Record ID/ColumnName
@return ID/ColumnName pair */
public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), getName());
}
/** Set Program.
@param P_Insurance_Program_ID Program */
public void setP_Insurance_Program_ID (int P_Insurance_Program_ID)
{
if (P_Insurance_Program_ID < 1) throw new IllegalArgumentException ("P_Insurance_Program_ID is mandatory.");
set_ValueNoCheck ("P_Insurance_Program_ID", new Integer(P_Insurance_Program_ID));
}
/** Get Program.
@return Program */
public int getP_Insurance_Program_ID() 
{
Integer ii = (Integer)get_Value("P_Insurance_Program_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Profile_ID AD_Reference_ID=2000030 */
public static final int P_PROFILE_ID_AD_Reference_ID=2000030;
/** Set Profile.
@param P_Profile_ID Profile */
public void setP_Profile_ID (int P_Profile_ID)
{
if (P_Profile_ID < 1) throw new IllegalArgumentException ("P_Profile_ID is mandatory.");
set_Value ("P_Profile_ID", new Integer(P_Profile_ID));
}
/** Get Profile.
@return Profile */
public int getP_Profile_ID() 
{
Integer ii = (Integer)get_Value("P_Profile_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Single parent Max Employer.
@param SingleMaxEmployer Single parent Max Employer */
public void setSingleMaxEmployer (BigDecimal SingleMaxEmployer)
{
set_Value ("SingleMaxEmployer", SingleMaxEmployer);
}
/** Get Single parent Max Employer.
@return Single parent Max Employer */
public BigDecimal getSingleMaxEmployer() 
{
BigDecimal bd = (BigDecimal)get_Value("SingleMaxEmployer");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Search Key.
@param Value Search key for the record in the format required - must be unique */
public void setValue (String Value)
{
if (Value != null && Value.length() > 30)
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
