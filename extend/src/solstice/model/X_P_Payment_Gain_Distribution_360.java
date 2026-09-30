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
/** Generated Model for P_Payment_Gain_Distribution_360
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Payment_Gain_Distribution_360 extends PO
{
/** Standard Constructor
@param ctx context
@param P_Payment_Gain_Distribution_360_ID id
@param trxName transaction
*/
public X_P_Payment_Gain_Distribution_360 (Properties ctx, int P_Payment_Gain_Distribution_360_ID, String trxName)
{
super (ctx, P_Payment_Gain_Distribution_360_ID, trxName);
/** if (P_Payment_Gain_Distribution_360_ID == 0)
{
setAccount_ID (0);
setC_Activity_ID (0);
setDistribution_Type (null);
setLine (0);
setP_Payment_Gain_Distribution_360_ID (0);
setP_Payment_Gain_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Payment_Gain_Distribution_360 (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=3201225 */
public static final int Table_ID=3201225;

/** TableName=P_Payment_Gain_Distribution_360 */
public static final String Table_Name="P_Payment_Gain_Distribution_360";

protected static KeyNamePair Model = new KeyNamePair(3201225,"P_Payment_Gain_Distribution_360");

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
StringBuffer sb = new StringBuffer ("X_P_Payment_Gain_Distribution_360[").append(get_ID()).append("]");
return sb.toString();
}

/** Account_ID AD_Reference_ID=132 */
public static final int ACCOUNT_ID_AD_Reference_ID=132;
/** Set Account.
@param Account_ID Account used */
public void setAccount_ID (int Account_ID)
{
if (Account_ID < 1) throw new IllegalArgumentException ("Account_ID is mandatory.");
set_Value ("Account_ID", new Integer(Account_ID));
}
/** Get Account.
@return Account used */
public int getAccount_ID() 
{
Integer ii = (Integer)get_Value("Account_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Amount.
@param Amount Amount in a defined currency */
public void setAmount (BigDecimal Amount)
{
set_Value ("Amount", Amount);
}
/** Get Amount.
@return Amount in a defined currency */
public BigDecimal getAmount() 
{
BigDecimal bd = (BigDecimal)get_Value("Amount");
if (bd == null) return Env.ZERO;
return bd;
}

/** C_Activity_ID AD_Reference_ID=142 */
public static final int C_ACTIVITY_ID_AD_Reference_ID=142;
/** Set Function.
@param C_Activity_ID Function */
public void setC_Activity_ID (int C_Activity_ID)
{
if (C_Activity_ID < 1) throw new IllegalArgumentException ("C_Activity_ID is mandatory.");
set_Value ("C_Activity_ID", new Integer(C_Activity_ID));
}
/** Get Function.
@return Function */
public int getC_Activity_ID() 
{
Integer ii = (Integer)get_Value("C_Activity_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** C_BPartner_ID AD_Reference_ID=252 */
public static final int C_BPARTNER_ID_AD_Reference_ID=252;
/** Set Business Partner .
@param C_BPartner_ID Identifies a Business Partner */
public void setC_BPartner_ID (int C_BPartner_ID)
{
if (C_BPartner_ID <= 0) set_Value ("C_BPartner_ID", null);
 else 
set_Value ("C_BPartner_ID", new Integer(C_BPartner_ID));
}
/** Get Business Partner .
@return Identifies a Business Partner */
public int getC_BPartner_ID() 
{
Integer ii = (Integer)get_Value("C_BPartner_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** C_Campaign_ID AD_Reference_ID=143 */
public static final int C_CAMPAIGN_ID_AD_Reference_ID=143;
/** Set Campaign.
@param C_Campaign_ID Marketing Campaign */
public void setC_Campaign_ID (int C_Campaign_ID)
{
if (C_Campaign_ID <= 0) set_Value ("C_Campaign_ID", null);
 else 
set_Value ("C_Campaign_ID", new Integer(C_Campaign_ID));
}
/** Get Campaign.
@return Marketing Campaign */
public int getC_Campaign_ID() 
{
Integer ii = (Integer)get_Value("C_Campaign_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** C_Project_ID AD_Reference_ID=141 */
public static final int C_PROJECT_ID_AD_Reference_ID=141;
/** Set Project.
@param C_Project_ID Financial Project */
public void setC_Project_ID (int C_Project_ID)
{
if (C_Project_ID <= 0) set_Value ("C_Project_ID", null);
 else 
set_Value ("C_Project_ID", new Integer(C_Project_ID));
}
/** Get Project.
@return Financial Project */
public int getC_Project_ID() 
{
Integer ii = (Integer)get_Value("C_Project_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** C_SalesRegion_ID AD_Reference_ID=144 */
public static final int C_SALESREGION_ID_AD_Reference_ID=144;
/** Set Code main d'oeuvre.
@param C_SalesRegion_ID Code main d'oeuvre */
public void setC_SalesRegion_ID (int C_SalesRegion_ID)
{
if (C_SalesRegion_ID <= 0) set_Value ("C_SalesRegion_ID", null);
 else 
set_Value ("C_SalesRegion_ID", new Integer(C_SalesRegion_ID));
}
/** Get Code main d'oeuvre.
@return Code main d'oeuvre */
public int getC_SalesRegion_ID() 
{
Integer ii = (Integer)get_Value("C_SalesRegion_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** Distribution_Type AD_Reference_ID=2100323 */
public static final int DISTRIBUTION_TYPE_AD_Reference_ID=2100323;
/** A = A */
public static final String DISTRIBUTION_TYPE_A = "A";
/** B = B */
public static final String DISTRIBUTION_TYPE_B = "B";
/** S = S */
public static final String DISTRIBUTION_TYPE_S = "S";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isDistribution_TypeValid (String test)
{
return test.equals("A") || test.equals("B") || test.equals("S");
}
/** Set Gain Distribution Type.
@param Distribution_Type Gain Distribution Type */
public void setDistribution_Type (String Distribution_Type)
{
if (Distribution_Type == null) throw new IllegalArgumentException ("Distribution_Type is mandatory");
if (!isDistribution_TypeValid(Distribution_Type))
throw new IllegalArgumentException ("Distribution_Type Invalid value - " + Distribution_Type + " - Reference_ID=2100323 - A - B - S");
if (Distribution_Type.length() > 1)
{
log.warning("Length > 1 - truncated");
Distribution_Type = Distribution_Type.substring(0,0);
}
set_Value ("Distribution_Type", Distribution_Type);
}
/** Get Gain Distribution Type.
@return Gain Distribution Type */
public String getDistribution_Type() 
{
return (String)get_Value("Distribution_Type");
}
/** Set Line No.
@param Line Unique line for this document */
public void setLine (int Line)
{
set_Value ("Line", new Integer(Line));
}
/** Get Line No.
@return Unique line for this document */
public int getLine() 
{
Integer ii = (Integer)get_Value("Line");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Product.
@param M_Product_ID Product, Service, Item */
public void setM_Product_ID (int M_Product_ID)
{
if (M_Product_ID <= 0) set_Value ("M_Product_ID", null);
 else 
set_Value ("M_Product_ID", new Integer(M_Product_ID));
}
/** Get Product.
@return Product, Service, Item */
public int getM_Product_ID() 
{
Integer ii = (Integer)get_Value("M_Product_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** Org_ID AD_Reference_ID=130 */
public static final int ORG_ID_AD_Reference_ID=130;
/** Set Organization.
@param Org_ID Organizational entity within client */
public void setOrg_ID (int Org_ID)
{
if (Org_ID <= 0) set_Value ("Org_ID", null);
 else 
set_Value ("Org_ID", new Integer(Org_ID));
}
/** Get Organization.
@return Organizational entity within client */
public int getOrg_ID() 
{
Integer ii = (Integer)get_Value("Org_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Expense Correction.
@param P_ExpenseCorrection_ID Expense Correction */
public void setP_ExpenseCorrection_ID (int P_ExpenseCorrection_ID)
{
if (P_ExpenseCorrection_ID <= 0) set_Value ("P_ExpenseCorrection_ID", null);
 else 
set_Value ("P_ExpenseCorrection_ID", new Integer(P_ExpenseCorrection_ID));
}
/** Get Expense Correction.
@return Expense Correction */
public int getP_ExpenseCorrection_ID() 
{
Integer ii = (Integer)get_Value("P_ExpenseCorrection_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_Payment_Gain_Distribution_360_ID.
@param P_Payment_Gain_Distribution_360_ID P_Payment_Gain_Distribution_360_ID */
public void setP_Payment_Gain_Distribution_360_ID (int P_Payment_Gain_Distribution_360_ID)
{
if (P_Payment_Gain_Distribution_360_ID < 1) throw new IllegalArgumentException ("P_Payment_Gain_Distribution_360_ID is mandatory.");
set_ValueNoCheck ("P_Payment_Gain_Distribution_360_ID", new Integer(P_Payment_Gain_Distribution_360_ID));
}
/** Get P_Payment_Gain_Distribution_360_ID.
@return P_Payment_Gain_Distribution_360_ID */
public int getP_Payment_Gain_Distribution_360_ID() 
{
Integer ii = (Integer)get_Value("P_Payment_Gain_Distribution_360_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set P_Payment_Gain_ID.
@param P_Payment_Gain_ID P_Payment_Gain_ID */
public void setP_Payment_Gain_ID (int P_Payment_Gain_ID)
{
if (P_Payment_Gain_ID < 1) throw new IllegalArgumentException ("P_Payment_Gain_ID is mandatory.");
set_Value ("P_Payment_Gain_ID", new Integer(P_Payment_Gain_ID));
}
/** Get P_Payment_Gain_ID.
@return P_Payment_Gain_ID */
public int getP_Payment_Gain_ID() 
{
Integer ii = (Integer)get_Value("P_Payment_Gain_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Payment Gain Info.
@param PaymentGainInfo Payment Gain Info */
public void setPaymentGainInfo (String PaymentGainInfo)
{
throw new IllegalArgumentException ("PaymentGainInfo is virtual column");
}
/** Get Payment Gain Info.
@return Payment Gain Info */
public String getPaymentGainInfo() 
{
return (String)get_Value("PaymentGainInfo");
}
/** Set Quantity.
@param Quantity Quantity */
public void setQuantity (BigDecimal Quantity)
{
set_Value ("Quantity", Quantity);
}
/** Get Quantity.
@return Quantity */
public BigDecimal getQuantity() 
{
BigDecimal bd = (BigDecimal)get_Value("Quantity");
if (bd == null) return Env.ZERO;
return bd;
}
}
