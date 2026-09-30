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
/** Generated Model for P_Payment_Registry
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Payment_Registry extends PO
{
/** Standard Constructor
@param ctx context
@param P_Payment_Registry_ID id
@param trxName transaction
*/
public X_P_Payment_Registry (Properties ctx, int P_Payment_Registry_ID, String trxName)
{
super (ctx, P_Payment_Registry_ID, trxName);
/** if (P_Payment_Registry_ID == 0)
{
setP_Payment_Registry_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Payment_Registry (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000155 */
public static final int Table_ID=2000155;

/** TableName=P_Payment_Registry */
public static final String Table_Name="P_Payment_Registry";

protected static KeyNamePair Model = new KeyNamePair(2000155,"P_Payment_Registry");

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
StringBuffer sb = new StringBuffer ("X_P_Payment_Registry[").append(get_ID()).append("]");
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
/** Set Amount calculed.
@param AmountCalc Amount calculed */
public void setAmountCalc (BigDecimal AmountCalc)
{
set_Value ("AmountCalc", AmountCalc);
}
/** Get Amount calculed.
@return Amount calculed */
public BigDecimal getAmountCalc() 
{
BigDecimal bd = (BigDecimal)get_Value("AmountCalc");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Amountcumul.
@param Amountcumul Amountcumul */
public void setAmountcumul (BigDecimal Amountcumul)
{
set_Value ("Amountcumul", Amountcumul);
}
/** Get Amountcumul.
@return Amountcumul */
public BigDecimal getAmountcumul() 
{
BigDecimal bd = (BigDecimal)get_Value("Amountcumul");
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
/** Set Employee_Part_Cumul.
@param Employee_Part_Cumul Employee_Part_Cumul */
public void setEmployee_Part_Cumul (BigDecimal Employee_Part_Cumul)
{
set_Value ("Employee_Part_Cumul", Employee_Part_Cumul);
}
/** Get Employee_Part_Cumul.
@return Employee_Part_Cumul */
public BigDecimal getEmployee_Part_Cumul() 
{
BigDecimal bd = (BigDecimal)get_Value("Employee_Part_Cumul");
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
/** Set Employer_Part_Cumul.
@param Employer_Part_Cumul Employer_Part_Cumul */
public void setEmployer_Part_Cumul (BigDecimal Employer_Part_Cumul)
{
set_Value ("Employer_Part_Cumul", Employer_Part_Cumul);
}
/** Get Employer_Part_Cumul.
@return Employer_Part_Cumul */
public BigDecimal getEmployer_Part_Cumul() 
{
BigDecimal bd = (BigDecimal)get_Value("Employer_Part_Cumul");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set P_Count_Line.
@param P_Count_Line P_Count_Line */
public void setP_Count_Line (int P_Count_Line)
{
set_Value ("P_Count_Line", new Integer(P_Count_Line));
}
/** Get P_Count_Line.
@return P_Count_Line */
public int getP_Count_Line() 
{
Integer ii = (Integer)get_Value("P_Count_Line");
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

/** P_Employee_ID AD_Reference_ID=2000010 */
public static final int P_EMPLOYEE_ID_AD_Reference_ID=2000010;
/** Set Employee .
@param P_Employee_ID Employee */
public void setP_Employee_ID (int P_Employee_ID)
{
if (P_Employee_ID <= 0) set_Value ("P_Employee_ID", null);
 else 
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
/** Set Gain.
@param P_Gain_ID Gain */
public void setP_Gain_ID (int P_Gain_ID)
{
if (P_Gain_ID <= 0) set_Value ("P_Gain_ID", null);
 else 
set_Value ("P_Gain_ID", new Integer(P_Gain_ID));
}
/** Get Gain.
@return Gain */
public int getP_Gain_ID() 
{
Integer ii = (Integer)get_Value("P_Gain_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_LINE_TYPE.
@param P_LINE_TYPE P_LINE_TYPE */
public void setP_LINE_TYPE (String P_LINE_TYPE)
{
if (P_LINE_TYPE != null && P_LINE_TYPE.length() > 1)
{
log.warning("Length > 1 - truncated");
P_LINE_TYPE = P_LINE_TYPE.substring(0,0);
}
set_Value ("P_LINE_TYPE", P_LINE_TYPE);
}
/** Get P_LINE_TYPE.
@return P_LINE_TYPE */
public String getP_LINE_TYPE() 
{
return (String)get_Value("P_LINE_TYPE");
}
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

/** P_Payment_ID AD_Reference_ID=2000059 */
public static final int P_PAYMENT_ID_AD_Reference_ID=2000059;
/** Set Payment.
@param P_Payment_ID Payment */
public void setP_Payment_ID (int P_Payment_ID)
{
if (P_Payment_ID <= 0) set_Value ("P_Payment_ID", null);
 else 
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
/** Set P_Payment_Registry_ID.
@param P_Payment_Registry_ID P_Payment_Registry_ID */
public void setP_Payment_Registry_ID (int P_Payment_Registry_ID)
{
if (P_Payment_Registry_ID < 1) throw new IllegalArgumentException ("P_Payment_Registry_ID is mandatory.");
set_Value ("P_Payment_Registry_ID", new Integer(P_Payment_Registry_ID));
}
/** Get P_Payment_Registry_ID.
@return P_Payment_Registry_ID */
public int getP_Payment_Registry_ID() 
{
Integer ii = (Integer)get_Value("P_Payment_Registry_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** P_Period_ID AD_Reference_ID=2000009 */
public static final int P_PERIOD_ID_AD_Reference_ID=2000009;
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
/** Set Quantity calculed.
@param QuantityCalc Quantity calculed */
public void setQuantityCalc (BigDecimal QuantityCalc)
{
set_Value ("QuantityCalc", QuantityCalc);
}
/** Get Quantity calculed.
@return Quantity calculed */
public BigDecimal getQuantityCalc() 
{
BigDecimal bd = (BigDecimal)get_Value("QuantityCalc");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Quantitycumul.
@param Quantitycumul Quantitycumul */
public void setQuantitycumul (BigDecimal Quantitycumul)
{
set_Value ("Quantitycumul", Quantitycumul);
}
/** Get Quantitycumul.
@return Quantitycumul */
public BigDecimal getQuantitycumul() 
{
BigDecimal bd = (BigDecimal)get_Value("Quantitycumul");
if (bd == null) return Env.ZERO;
return bd;
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
}
