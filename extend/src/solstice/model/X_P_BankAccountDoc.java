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
/** Generated Model for P_BankAccountDoc
 *  @author Marc Morissette - Solstice (generated) 
 *  @version Release 2.6.1 - $Id: SolsticeGenerateModel.java,v 1.2 2007/11/02 19:41:27 jeaham01 Exp $ */
public class X_P_BankAccountDoc extends PO
{
/** Standard Constructor
@param ctx context
@param P_BankAccountDoc_ID id
@param trxName transaction
*/
public X_P_BankAccountDoc (Properties ctx, int P_BankAccountDoc_ID, String trxName)
{
super (ctx, P_BankAccountDoc_ID, trxName);
/** if (P_BankAccountDoc_ID == 0)
{
setCurrentNext (0);
setName (null);
setP_BankAccount_ID (0);
setP_BankAccountDoc_ID (0);
setPaymentRule (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_P_BankAccountDoc (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=2000043 */
public static final int Table_ID=2000043;

/** TableName=P_BankAccountDoc */
public static final String Table_Name="P_BankAccountDoc";

protected static KeyNamePair Model = new KeyNamePair(2000043,"P_BankAccountDoc");

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
StringBuffer sb = new StringBuffer ("X_P_BankAccountDoc[").append(get_ID()).append("]");
return sb.toString();
}

/** Check_PrintFormat_ID AD_Reference_ID=268 */
public static final int CHECK_PRINTFORMAT_ID_AD_Reference_ID=268;
/** Set Check Print Format.
@param Check_PrintFormat_ID Print Format for printing Checks */
public void setCheck_PrintFormat_ID (int Check_PrintFormat_ID)
{
if (Check_PrintFormat_ID <= 0) set_Value ("Check_PrintFormat_ID", null);
 else 
set_Value ("Check_PrintFormat_ID", new Integer(Check_PrintFormat_ID));
}
/** Get Check Print Format.
@return Print Format for printing Checks */
public int getCheck_PrintFormat_ID() 
{
Integer ii = (Integer)get_Value("Check_PrintFormat_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Current Next.
@param CurrentNext The next number to be used */
public void setCurrentNext (int CurrentNext)
{
set_Value ("CurrentNext", new Integer(CurrentNext));
}
/** Get Current Next.
@return The next number to be used */
public int getCurrentNext() 
{
Integer ii = (Integer)get_Value("CurrentNext");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Deposit next no.
@param DepositNext Deposit next no */
public void setDepositNext (int DepositNext)
{
set_Value ("DepositNext", new Integer(DepositNext));
}
/** Get Deposit next no.
@return Deposit next no */
public int getDepositNext() 
{
Integer ii = (Integer)get_Value("DepositNext");
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
/** Set Bank Account.
@param P_BankAccount_ID Account at the Bank  */
public void setP_BankAccount_ID (int P_BankAccount_ID)
{
if (P_BankAccount_ID < 1) throw new IllegalArgumentException ("P_BankAccount_ID is mandatory.");
set_ValueNoCheck ("P_BankAccount_ID", new Integer(P_BankAccount_ID));
}
/** Get Bank Account.
@return Account at the Bank  */
public int getP_BankAccount_ID() 
{
Integer ii = (Integer)get_Value("P_BankAccount_ID");
if (ii == null) return 0;
return ii.intValue();
}
/** Set Bank Account Document.
@param P_BankAccountDoc_ID Checks, Transfers, etc.  */
public void setP_BankAccountDoc_ID (int P_BankAccountDoc_ID)
{
if (P_BankAccountDoc_ID < 1) throw new IllegalArgumentException ("P_BankAccountDoc_ID is mandatory.");
set_Value ("P_BankAccountDoc_ID", new Integer(P_BankAccountDoc_ID));
}
/** Get Bank Account Document.
@return Checks, Transfers, etc.  */
public int getP_BankAccountDoc_ID() 
{
Integer ii = (Integer)get_Value("P_BankAccountDoc_ID");
if (ii == null) return 0;
return ii.intValue();
}

/** PaymentRule AD_Reference_ID=2100390 */
public static final int PAYMENTRULE_AD_Reference_ID=2100390;
/** Cash = B */
public static final String PAYMENTRULE_Cash = "B";
/** Direct Debit = D */
public static final String PAYMENTRULE_DirectDebit = "D";
/** Credit Card = K */
public static final String PAYMENTRULE_CreditCard = "K";
/** On Credit = P */
public static final String PAYMENTRULE_OnCredit = "P";
/** Check = S */
public static final String PAYMENTRULE_Check = "S";
/** Direct Deposit = T */
public static final String PAYMENTRULE_DirectDeposit = "T";
/** Is test a valid value.
@param test testvalue
@returns true if valid **/
public boolean isPaymentRuleValid (String test)
{
return test.equals("B") || test.equals("D") || test.equals("K") || test.equals("P") || test.equals("S") || test.equals("T");
}
/** Set Payment Rule.
@param PaymentRule How you pay the invoice */
public void setPaymentRule (String PaymentRule)
{
if (PaymentRule == null) throw new IllegalArgumentException ("PaymentRule is mandatory");
if (!isPaymentRuleValid(PaymentRule))
throw new IllegalArgumentException ("PaymentRule Invalid value - " + PaymentRule + " - Reference_ID=2100390 - B - D - K - P - S - T");
if (PaymentRule.length() > 1)
{
log.warning("Length > 1 - truncated");
PaymentRule = PaymentRule.substring(0,0);
}
set_Value ("PaymentRule", PaymentRule);
}
/** Get Payment Rule.
@return How you pay the invoice */
public String getPaymentRule() 
{
return (String)get_Value("PaymentRule");
}
}
