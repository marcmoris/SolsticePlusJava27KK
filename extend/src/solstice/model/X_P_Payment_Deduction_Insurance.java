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
/** Generated Model for P_Payment_Deduction_Insurance
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Payment_Deduction_Insurance extends PO
{
/** Standard Constructor
@param ctx context
@param P_Payment_Deduction_Insurance_ID id
@param trxName transaction
*/
public X_P_Payment_Deduction_Insurance (Properties ctx, int P_Payment_Deduction_Insurance_ID, String trxName)
{
super (ctx, P_Payment_Deduction_Insurance_ID, trxName);
/** if (P_Payment_Deduction_Insurance_ID == 0)
{
setP_Deduction_ID (0);
setP_Payment_Deduction_Insurance_ID (0);
setP_Payment_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Payment_Deduction_Insurance (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=3201234 */
public static final int Table_ID=3201234;

/** TableName=P_Payment_Deduction_Insurance */
public static final String Table_Name="P_Payment_Deduction_Insurance";

protected static KeyNamePair Model = new KeyNamePair(3201234,"P_Payment_Deduction_Insurance");

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
StringBuffer sb = new StringBuffer ("X_P_Payment_Deduction_Insurance[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Accumulation .
@param AccumulationAmount Accumulation  */
public void setAccumulationAmount (BigDecimal AccumulationAmount)
{
set_Value ("AccumulationAmount", AccumulationAmount);
}
/** Get Accumulation .
@return Accumulation  */
public BigDecimal getAccumulationAmount() 
{
BigDecimal bd = (BigDecimal)get_Value("AccumulationAmount");
if (bd == null) return Env.ZERO;
return bd;
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
/** Set Employer Part.
@param Employer_Part Employer Part */
public void setEmployer_Part (BigDecimal Employer_Part)
{
set_Value ("Employer_Part", Employer_Part);
}
/** Get Employer Part.
@return Employer Part */
public BigDecimal getEmployer_Part() 
{
BigDecimal bd = (BigDecimal)get_Value("Employer_Part");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Hours Eligible.
@param Hours_Eligible Hours Eligible */
public void setHours_Eligible (BigDecimal Hours_Eligible)
{
set_Value ("Hours_Eligible", Hours_Eligible);
}
/** Get Hours Eligible.
@return Hours Eligible */
public BigDecimal getHours_Eligible() 
{
BigDecimal bd = (BigDecimal)get_Value("Hours_Eligible");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Exonerated.
@param IsExonerated Exonerated */
public void setIsExonerated (boolean IsExonerated)
{
set_Value ("IsExonerated", new Boolean(IsExonerated));
}
/** Get Exonerated.
@return Exonerated */
public boolean isExonerated() 
{
Object oo = get_Value("IsExonerated");
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}
/** Set origine.
@param Origine origine */
public void setOrigine (String Origine)
{
if (Origine != null && Origine.length() > 3)
{
log.warning("Length > 3 - truncated");
Origine = Origine.substring(0,2);
}
set_Value ("Origine", Origine);
}
/** Get origine.
@return origine */
public String getOrigine() 
{
return (String)get_Value("Origine");
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
/** Set Deduction.
@param P_Deduction_Param_ID Deduction */
public void setP_Deduction_Param_ID (int P_Deduction_Param_ID)
{
if (P_Deduction_Param_ID <= 0) set_Value ("P_Deduction_Param_ID", null);
 else 
set_Value ("P_Deduction_Param_ID", new Integer(P_Deduction_Param_ID));
}
/** Get Deduction.
@return Deduction */
public int getP_Deduction_Param_ID() 
{
Integer ii = (Integer)get_Value("P_Deduction_Param_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Deduction.
@param P_Employee_Deduction_ID Deduction */
public void setP_Employee_Deduction_ID (int P_Employee_Deduction_ID)
{
if (P_Employee_Deduction_ID <= 0) set_Value ("P_Employee_Deduction_ID", null);
 else 
set_Value ("P_Employee_Deduction_ID", new Integer(P_Employee_Deduction_ID));
}
/** Get Deduction.
@return Deduction */
public int getP_Employee_Deduction_ID() 
{
Integer ii = (Integer)get_Value("P_Employee_Deduction_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_Payment_Deduction_Insurance_ID.
@param P_Payment_Deduction_Insurance_ID P_Payment_Deduction_Insurance_ID */
public void setP_Payment_Deduction_Insurance_ID (int P_Payment_Deduction_Insurance_ID)
{
if (P_Payment_Deduction_Insurance_ID < 1) throw new IllegalArgumentException ("P_Payment_Deduction_Insurance_ID is mandatory.");
set_ValueNoCheck ("P_Payment_Deduction_Insurance_ID", new Integer(P_Payment_Deduction_Insurance_ID));
}
/** Get P_Payment_Deduction_Insurance_ID.
@return P_Payment_Deduction_Insurance_ID */
public int getP_Payment_Deduction_Insurance_ID() 
{
Integer ii = (Integer)get_Value("P_Payment_Deduction_Insurance_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Payment.
@param P_Payment_ID Payment */
public void setP_Payment_ID (int P_Payment_ID)
{
if (P_Payment_ID < 1) throw new IllegalArgumentException ("P_Payment_ID is mandatory.");
set_Value ("P_Payment_ID", new Integer(P_Payment_ID));
}
/** Get Payment.
@return Payment */
public int getP_Payment_ID() 
{
Integer ii = (Integer)get_Value("P_Payment_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Period.
@param P_Period_ID Period of the Calendar   */
public void setP_Period_ID (int P_Period_ID)
{
if (P_Period_ID <= 0) set_Value ("P_Period_ID", null);
 else 
set_Value ("P_Period_ID", new Integer(P_Period_ID));
}
/** Get Period.
@return Period of the Calendar   */
public int getP_Period_ID() 
{
Integer ii = (Integer)get_Value("P_Period_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Retrieval.
@param RetrievalAmount Retrieval */
public void setRetrievalAmount (BigDecimal RetrievalAmount)
{
set_Value ("RetrievalAmount", RetrievalAmount);
}
/** Get Retrieval.
@return Retrieval */
public BigDecimal getRetrievalAmount() 
{
BigDecimal bd = (BigDecimal)get_Value("RetrievalAmount");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Salary Eligible.
@param Salary_Eligible Salary Eligible */
public void setSalary_Eligible (BigDecimal Salary_Eligible)
{
set_Value ("Salary_Eligible", Salary_Eligible);
}
/** Get Salary Eligible.
@return Salary Eligible */
public BigDecimal getSalary_Eligible() 
{
BigDecimal bd = (BigDecimal)get_Value("Salary_Eligible");
if (bd == null) return Env.ZERO;
return bd;
}
}
