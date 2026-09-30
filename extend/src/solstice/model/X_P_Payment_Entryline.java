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
/** Generated Model for P_Payment_Entryline
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_Payment_Entryline extends PO
{
/** Standard Constructor
@param ctx context
@param P_Payment_Entryline_ID id
@param trxName transaction
*/
public X_P_Payment_Entryline (Properties ctx, int P_Payment_Entryline_ID, String trxName)
{
super (ctx, P_Payment_Entryline_ID, trxName);
/** if (P_Payment_Entryline_ID == 0)
{
setAmtAcctCr (Env.ZERO);
setAmtAcctDr (Env.ZERO);
setC_Activity_ID (0);
setC_ElementValue_ID (0);
setC_ValidCombination_ID (0);
setLine (0);
setP_Payment_Entryline_ID (0);
setP_Payment_ID (0);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_Payment_Entryline (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2100666 */
public static final int Table_ID=2100666;

/** TableName=P_Payment_Entryline */
public static final String Table_Name="P_Payment_Entryline";

protected static KeyNamePair Model = new KeyNamePair(2100666,"P_Payment_Entryline");

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
StringBuffer sb = new StringBuffer ("X_P_Payment_Entryline[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Accounted Credit.
@param AmtAcctCr Accounted Credit Amount */
public void setAmtAcctCr (BigDecimal AmtAcctCr)
{
if (AmtAcctCr == null) throw new IllegalArgumentException ("AmtAcctCr is mandatory.");
set_Value ("AmtAcctCr", AmtAcctCr);
}
/** Get Accounted Credit.
@return Accounted Credit Amount */
public BigDecimal getAmtAcctCr() 
{
BigDecimal bd = (BigDecimal)get_Value("AmtAcctCr");
if (bd == null) return Env.ZERO;
return bd;
}
/** Set Accounted Debit.
@param AmtAcctDr Accounted Debit Amount */
public void setAmtAcctDr (BigDecimal AmtAcctDr)
{
if (AmtAcctDr == null) throw new IllegalArgumentException ("AmtAcctDr is mandatory.");
set_Value ("AmtAcctDr", AmtAcctDr);
}
/** Get Accounted Debit.
@return Accounted Debit Amount */
public BigDecimal getAmtAcctDr() 
{
BigDecimal bd = (BigDecimal)get_Value("AmtAcctDr");
if (bd == null) return Env.ZERO;
return bd;
}

/** C_Activity_ID AD_Reference_ID=142 */
public static final int C_ACTIVITY_ID_AD_Reference_ID=142;
/** Set Crew cost.
@param C_Activity_ID Crew cost */
public void setC_Activity_ID (int C_Activity_ID)
{
if (C_Activity_ID < 1) throw new IllegalArgumentException ("C_Activity_ID is mandatory.");
set_Value ("C_Activity_ID", new Integer(C_Activity_ID));
}
/** Get Crew cost.
@return Crew cost */
public int getC_Activity_ID() 
{
Integer ii = (Integer)get_Value("C_Activity_ID");
if (ii == null) return 0;
return ii.intValue();
}
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

/** C_ElementValue_ID AD_Reference_ID=132 */
public static final int C_ELEMENTVALUE_ID_AD_Reference_ID=132;
/** Set Account Element.
@param C_ElementValue_ID Account Element */
public void setC_ElementValue_ID (int C_ElementValue_ID)
{
if (C_ElementValue_ID < 1) throw new IllegalArgumentException ("C_ElementValue_ID is mandatory.");
set_Value ("C_ElementValue_ID", new Integer(C_ElementValue_ID));
}
/** Get Account Element.
@return Account Element */
public int getC_ElementValue_ID() 
{
Integer ii = (Integer)get_Value("C_ElementValue_ID");
if (ii == null) return 0;
return ii.intValue();
}
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
/** Set Base.
@param C_SalesRegion_ID Base */
public void setC_SalesRegion_ID (int C_SalesRegion_ID)
{
if (C_SalesRegion_ID <= 0) set_Value ("C_SalesRegion_ID", null);
 else 
set_Value ("C_SalesRegion_ID", new Integer(C_SalesRegion_ID));
}
/** Get Base.
@return Base */
public int getC_SalesRegion_ID() 
{
Integer ii = (Integer)get_Value("C_SalesRegion_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** C_ValidCombination_ID AD_Reference_ID=175 */
public static final int C_VALIDCOMBINATION_ID_AD_Reference_ID=175;
/** Set Combination.
@param C_ValidCombination_ID Valid Account Combination */
public void setC_ValidCombination_ID (int C_ValidCombination_ID)
{
if (C_ValidCombination_ID < 1) throw new IllegalArgumentException ("C_ValidCombination_ID is mandatory.");
set_Value ("C_ValidCombination_ID", new Integer(C_ValidCombination_ID));
}
/** Get Combination.
@return Valid Account Combination */
public int getC_ValidCombination_ID() 
{
Integer ii = (Integer)get_Value("C_ValidCombination_ID");
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

/** Org_ID AD_Reference_ID=417 */
public static final int ORG_ID_AD_Reference_ID=417;
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
/** Set origine.
@param Origine origine */
public void setOrigine (String Origine)
{
if (Origine != null && Origine.length() > 4)
{
log.warning("Length > 4 - truncated");
Origine = Origine.substring(0,3);
}
set_Value ("Origine", Origine);
}
/** Get origine.
@return origine */
public String getOrigine() 
{
return (String)get_Value("Origine");
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
/** Set P_Payment_Entryline_ID.
@param P_Payment_Entryline_ID P_Payment_Entryline_ID */
public void setP_Payment_Entryline_ID (int P_Payment_Entryline_ID)
{
if (P_Payment_Entryline_ID < 1) throw new IllegalArgumentException ("P_Payment_Entryline_ID is mandatory.");
set_Value ("P_Payment_Entryline_ID", new Integer(P_Payment_Entryline_ID));
}
/** Get P_Payment_Entryline_ID.
@return P_Payment_Entryline_ID */
public int getP_Payment_Entryline_ID() 
{
Integer ii = (Integer)get_Value("P_Payment_Entryline_ID");
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
}
