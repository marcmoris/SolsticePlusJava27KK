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
/** Generated Model for P_Method_Deduction
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Method_Deduction extends PO
{
/** Standard Constructor
@param ctx context
@param P_Method_Deduction_ID id
@param trxName transaction
*/
public X_P_Method_Deduction (Properties ctx, int P_Method_Deduction_ID, String trxName)
{
super (ctx, P_Method_Deduction_ID, trxName);
/** if (P_Method_Deduction_ID == 0)
{
setName (null);
setP_Method_Deduction_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Method_Deduction (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000073 */
public static final int Table_ID=2000073;

/** TableName=P_Method_Deduction */
public static final String Table_Name="P_Method_Deduction";

protected static KeyNamePair Model = new KeyNamePair(2000073,"P_Method_Deduction");

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
StringBuffer sb = new StringBuffer ("X_P_Method_Deduction[").append(get_ID()).append("]");
return sb.toString();
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
/** Set Accumulation.
@param IsAccumulation Accumulation */
public void setIsAccumulation (boolean IsAccumulation)
{
set_Value ("IsAccumulation", new Boolean(IsAccumulation));
}
/** Get Accumulation.
@return Accumulation */
public boolean isAccumulation() 
{
Object oo = get_Value("IsAccumulation");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set isCalculatedWhenEligibleSalaryIsZero.
@param isCalculatedWhenEligibleSalaryIsZero isCalculatedWhenEligibleSalaryIsZero */
public void setisCalculatedWhenEligibleSalaryIsZero (boolean isCalculatedWhenEligibleSalaryIsZero)
{
set_Value ("isCalculatedWhenEligibleSalaryIsZero", new Boolean(isCalculatedWhenEligibleSalaryIsZero));
}
/** Get isCalculatedWhenEligibleSalaryIsZero.
@return isCalculatedWhenEligibleSalaryIsZero */
public boolean isCalculatedWhenEligibleSalaryIsZero() 
{
Object oo = get_Value("isCalculatedWhenEligibleSalaryIsZero");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set isDeductionWithoutEligibleSalary.
@param isDeductionWithoutEligibleSalary isDeductionWithoutEligibleSalary */
public void setisDeductionWithoutEligibleSalary (boolean isDeductionWithoutEligibleSalary)
{
set_Value ("isDeductionWithoutEligibleSalary", new Boolean(isDeductionWithoutEligibleSalary));
}
/** Get isDeductionWithoutEligibleSalary.
@return isDeductionWithoutEligibleSalary */
public boolean isDeductionWithoutEligibleSalary() 
{
Object oo = get_Value("isDeductionWithoutEligibleSalary");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set Is Employer Part When No Employee Part.
@param IsEmployerWhenZero Is Employer Part When No Employee Part */
public void setIsEmployerWhenZero (boolean IsEmployerWhenZero)
{
set_Value ("IsEmployerWhenZero", new Boolean(IsEmployerWhenZero));
}
/** Get Is Employer Part When No Employee Part.
@return Is Employer Part When No Employee Part */
public boolean isEmployerWhenZero() 
{
Object oo = get_Value("IsEmployerWhenZero");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set IsWithHoursEligible.
@param IsWithHoursEligible IsWithHoursEligible */
public void setIsWithHoursEligible (boolean IsWithHoursEligible)
{
set_Value ("IsWithHoursEligible", new Boolean(IsWithHoursEligible));
}
/** Get IsWithHoursEligible.
@return IsWithHoursEligible */
public boolean isWithHoursEligible() 
{
Object oo = get_Value("IsWithHoursEligible");
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
/** Set Method.
@param P_Method_Deduction_ID Method */
public void setP_Method_Deduction_ID (int P_Method_Deduction_ID)
{
if (P_Method_Deduction_ID < 1) throw new IllegalArgumentException ("P_Method_Deduction_ID is mandatory.");
set_Value ("P_Method_Deduction_ID", new Integer(P_Method_Deduction_ID));
}
/** Get Method.
@return Method */
public int getP_Method_Deduction_ID() 
{
Integer ii = (Integer)get_Value("P_Method_Deduction_ID");
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
